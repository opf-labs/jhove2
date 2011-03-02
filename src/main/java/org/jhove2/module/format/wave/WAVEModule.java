/**
 * JHOVE2 - Next-generation architecture for format-aware characterization
 *
 * Copyright (c) 2009 by The Regents of the University of California.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * o Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 *
 * o Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * o Neither the name of the University of California/California Digital
 *   Library, Ithaka Harbors/Portico, or Stanford University, nor the names of
 *   its contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package org.jhove2.module.format.wave;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.Message;
import org.jhove2.core.Message.Context;
import org.jhove2.core.Message.Severity;
import org.jhove2.core.format.Format;
import org.jhove2.core.io.Input;
import org.jhove2.core.source.MeasurableSource;
import org.jhove2.core.source.Source;
import org.jhove2.module.format.BaseFormatModule;
import org.jhove2.module.format.Validator;
import org.jhove2.module.format.riff.Chunk;
import org.jhove2.module.format.riff.ChunkFactory;
import org.jhove2.persist.FormatModuleAccessor;

import com.sleepycat.persist.model.Persistent;

/** WAVE (waveform audio file format) module.
 * 
 * @author slabrams
 */
@Persistent
public class WAVEModule
    extends BaseFormatModule
    implements Validator
{
    /** WAVE module version identifier. */
    public static final String VERSION = "2.0.0";

    /** WAVE module release date. */
    public static final String RELEASE = "2010-09-10";

    /** WAVE module rights statement. */
    public static final String RIGHTS =
        "Copyright 2010 by The Regents of the University of California" +
        "Available under the terms of the BSD license.";
    
    /** Module validation coverage. */
    public static final Coverage COVERAGE = Coverage.Inclusive;
  
    /** Chunk factory. */
    protected ChunkFactory chunkFactory;
    
    /** WAVE chunks. */
    protected List<Chunk> chunks;
    
    /** WAVE validation status. */
    protected Validity isValid;
    
    /** Duplicate chunk message. */
    protected Message duplicateChunkMessage;
    
    /** File truncated message: the RIFF chunk size is greater than the file
     * size.
     */
    protected Message fileTruncatedMessage;
    
    /** Format chunk not before data chunk message. */
    protected Message formatChunkNotBeforeDataChunkMessage;
    
    /** Missing required chunk message. */
    protected Message missingRequiredDataChunkMessage;
    
    /** Missing required fact chunk message. */
    protected Message missingRequiredFactChunkMessage;
    
    /** Missing required format chunk message. */
    protected Message missingRequiredFormatChunkMessage;
    
    /** RIFF chunk size smaller than file size message.
     * This is considered an error according to the US Federal Agencies
     * Digitization Guidelines Initiative (FADGI).
     */
    protected Message riffChunkSmallerThanFileMessage;

    /** Instantiate a new <code>WAVEModule</code>.
     * @param format WAVE format
     * @param formatModuleAccessor peristence manager
     */
    public WAVEModule(Format format, FormatModuleAccessor formatModuleAccessor)
    {
        super(VERSION, RELEASE, RIGHTS, format, formatModuleAccessor);
        
        this.chunks  = new ArrayList<Chunk>();
        this.isValid = Validity.Undetermined;
    }
    
    @SuppressWarnings("unused")
	private WAVEModule(){
    	this(null, null);
    }
    
    /** 
     * Parse a WAVE source unit.
     * 
     * @param jhove2
     *            JHOVE2 framework
     * @param source
     *            WAVE source unit
     * @param input  WAVE source input
     * @return Number of bytes consumed
     * @throws EOFException
     *             If End-of-File is reached reading the source unit
     * @throws IOException
     *             If an I/O exception is raised reading the source unit
     * @throws JHOVE2Exception
     * @see org.jhove2.module.format.Parser#parse(org.jhove2.core.JHOVE2,
     *      org.jhove2.core.source.Source, org.jhove2.core.io.Input)
     */
    @Override
    public long parse(JHOVE2 jhove2, Source source, Input input)
        throws EOFException, IOException, JHOVE2Exception
    {
        long consumed = 0L;
        this.isValid = Validity.True;
        input.setByteOrder(ByteOrder.LITTLE_ENDIAN);
        input.setPosition(((MeasurableSource) source).getStartingOffset());
                
        StringBuffer sb = new StringBuffer(4);
        for (int i=0; i<4; i++) {
            short b = input.readUnsignedByte();
            sb.append((char) b);
        }
        Chunk chunk = this.chunkFactory.getChunk(sb.toString());
        consumed += chunk.parse(jhove2, source, input);
        this.chunks.add(chunk);

        return consumed;
    }

    /** Validate the WAVE source unit.
     * @param jhove2 JHOVE2 framework
     * @param source WAVE source unit
     * @param input  WAVE source input
     * @see org.jhove2.module.format.Validator#validate(org.jhove2.core.JHOVE2, org.jhove2.core.source.Source, org.jhov2.core.io.Input)
     */
    @Override
    public Validity validate(JHOVE2 jhove2, Source source, Input input)
        throws JHOVE2Exception
    {
        /* Only FILR, FLLR, PAD_, JUNK, and JUNQ chunks can have more than 
         * one instance.
         */
        Map<String, Integer> map = new HashMap<String, Integer>();
        /* A valid WAVE must have a format chunk followed by a data chunk.
         */
        Iterator<Chunk> it = this.chunks.iterator();
        while (it.hasNext()) {
            Chunk ch = it.next();
            String id = ch.getIdentifier();
            if (id.equals("RIFF")) {
                Integer c = map.get(id);
                int ct = (c == null ? 0 : c.intValue());
                map.put(id, ct+1);
                int     formatCategory = 0; 
                boolean hasDataChunk   = false;
                boolean hasFactChunk   = false;
                boolean hasFormatChunk = false;
                Object [] args         = null;
                List<Chunk> children = ch.getChildChunks();
                Iterator<Chunk> iter = children.iterator();
                while (iter.hasNext()) {
                    Chunk chunk = iter.next();
                    id = chunk.getIdentifier();
                    c = map.get(id);
                    ct = (c == null ? 0 : c.intValue());
                    map.put(id, ct+1);
                    if (id.equals("data")) {
                        hasDataChunk = true;
                        
                        /* Data chunk must come after the format chunk. */
                        if (!hasFormatChunk) {
                            this.isValid = Validity.False;
                            this.formatChunkNotBeforeDataChunkMessage =
                                new Message(Severity.ERROR, Context.OBJECT,
                                        "org.jhove2.module.format.wave.WaveModule.formatChunkNotBeforeDataChunk",
                                        args, jhove2.getConfigInfo());
                        }
                    }
                    else if (id.equals("fact")) {
                        hasFactChunk = true;
                    }
                    else if (id.equals("fmt ")) {
                        hasFormatChunk = true;
                        
                        formatCategory =
                            ((FormatChunk) chunk).getFormatCategory_raw();
                    }
                    
                    /* Check if RIFF chunk size (+8 to account for the ID and
                     * size fields) is equal to file size.
                     */
                    long chunkSize = ch.getSize();
                    long fileSize  = ((MeasurableSource) source).getSize();
                    if ((chunkSize+8) < fileSize) {
                        this.isValid = Validity.False;
                        Object [] a = new Object [] {chunkSize, fileSize};
                        this.riffChunkSmallerThanFileMessage =
                            new Message(Severity.ERROR, Context.OBJECT,
                                    "org.jhove2.module.format.wave.WaveModule.riffChunkSmallerThanFile",
                                    a, jhove2.getConfigInfo());
                    }
                    
                    /* Check if the file has been truncated, i.e. the RIFF
                     * chunk size is greater than the file size.
                     */
                    if ((chunkSize+8) > fileSize) {
                        this.isValid = Validity.False;
                        Object [] a = new Object [] {chunkSize, fileSize};
                        this.fileTruncatedMessage =
                            new Message(Severity.ERROR, Context.OBJECT,
                                    "org.jhove2.module.format.wave.WaveModule.fileTruncated",
                                    a, jhove2.getConfigInfo());
                            
                    }
                }
                /* Format and data chunks are required. */
                if (!hasDataChunk) {
                    this.isValid = Validity.False;
                    this.missingRequiredDataChunkMessage = 
                        new Message(Severity.ERROR, Context.OBJECT,
                                "org.jhove2.module.format.wave.WaveModule.missingRequiredDataChunk",
                                args, jhove2.getConfigInfo());
                }
                if (!hasFormatChunk) {
                    this.isValid = Validity.False;
                    this.missingRequiredFormatChunkMessage = 
                        new Message(Severity.ERROR, Context.OBJECT,
                                "org.jhove2.module.format.wave.WaveModule.missingRequiredFormatChunk",
                                args, jhove2.getConfigInfo());
                }
                
                /* Fact chunk is required for non-PCM data. */
                if (formatCategory != FormatChunk.WAVE_FORMAT_PCM &&
                    !hasFactChunk) {
                    this.isValid = Validity.False;
                    this.missingRequiredFactChunkMessage =
                        new Message(Severity.ERROR, Context.OBJECT,
                                "org.jhove2.module.format.wave.WaveModule.missingRequiredFactChunk",
                                args, jhove2.getConfigInfo());
                }
                break;
            }
        }
        Set<String> keys = map.keySet();
        Iterator<String> iter = keys.iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            int ct = map.get(key);
            if (ct > 1 && !key.equals("FILR") && !key.equals("FLLR") &&
                          !key.equals("PAD_") && !key.equals("JUNK") &&
                          !key.equals("JUNQ")) {
                this.isValid = Validity.False;
                Object [] args = new Object [] {key};
                this.duplicateChunkMessage =
                    new Message(Severity.ERROR, Context.OBJECT,
                            "org.jhove2.module.format.wave.WaveModule.duplicateChunk",
                            args, jhove2.getConfigInfo());
            }
        }
        
        return this.isValid();
    }
    
    /** Get chunk factory.
     * @return Chunk factory
     */
    public ChunkFactory getChunkFactory() {
        return this.chunkFactory;
    }

    /** Get chunks.
     * @return Chunks
     */
    @ReportableProperty(order=1, value="Chunks.")
    public List<Chunk> getChunks() {
        return this.chunks;
    }
    
    /** Get module coverage.
     * @return Module coverage
     * @see org.jhove2.module.format.Validator#getCoverage()
     */
    @Override
    public Coverage getCoverage()
    {
        return COVERAGE;
    }   
      
    /** Get duplicate chunk message.
     * @return Duplicate chunk message
     */
    @ReportableProperty(order=27, value="Duplicate chunk message.")
    public Message getDuplicateChunkMessage() {
        return this.duplicateChunkMessage;
    }
    
    /** Get file truncated message.
     * @return File truncated message
     */
    @ReportableProperty(order=26, value="File truncated message.")
    public Message getFileTruncatedMessage() {
        return this.fileTruncatedMessage;
    }
        
    /** Get format chunk not before data chunk message.
     * @return Format chunk not before data chunk message
     */
    @ReportableProperty(order=21, value="Format chunk does not appear before the data chunk.")
    public Message getFormatChunkNotBeforeDataChunkMessage() {
        return this.formatChunkNotBeforeDataChunkMessage;
    }
    
    /** Get missing required data chunk message.
     * @return Missing required data chunk message
     */
    @ReportableProperty(order=23, value="Missing required data chunk message.")
    public Message getMissingRequiredDataChunkMessage() {
        return this.missingRequiredDataChunkMessage;
    }
    
    /** Get missing required fact chunk message.
     * @return Missing required fact chunk message.
     */
    @ReportableProperty(order=24, value="Missing required fact chunk message.")
    public Message getMissingRequiredFactChunkMessage() {
        return this.missingRequiredFactChunkMessage;
    }
    
    /** Get missing required format chunk message.
     * @return Missing required format chunk message
     */
    @ReportableProperty(order=22, value="Missing required format chunk message.")
    public Message getMissingRequiredFormatChunkMessage() {
        return this.missingRequiredFormatChunkMessage;
    }
    
    /** Get RIFF chunk smaller than file size message.
     * @return RIFF chunk smaller than file size message
     */
    @ReportableProperty(order=25, value="RIFF chunk smaller than file size message.")
    public Message getRIFFChunkSmallerThanFileMessage() {
        return this.riffChunkSmallerThanFileMessage;
    }

    /** Get validation status.
     * @return Validation status
     * @see org.jhove2.module.format.Validator#isValid()
     */
    @Override
    public Validity isValid()
    {
        return this.isValid;
    }
    
    /** Set chunk factory.
     * @param factory Chunk factory
     */
    public void setChunkFactory(ChunkFactory factory) {
        this.chunkFactory = factory;
    }
}
