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

package org.jhove2.module.format.wave.bwf;

import java.util.Iterator;
import java.util.List;
import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.Message;
import org.jhove2.core.Message.Context;
import org.jhove2.core.Message.Severity;
import org.jhove2.core.format.Format;
import org.jhove2.core.source.Source;
import org.jhove2.module.format.AbstractFormatProfile;
import org.jhove2.module.format.Validator;
import org.jhove2.module.format.riff.Chunk;
import org.jhove2.module.format.wave.FormatChunk;
import org.jhove2.module.format.wave.WAVEModule;

/** Broadcase Wave Format (BWF) profile.
 * 
 * @author slabrams
 */
public class BroadcastWaveFormatProfile
        extends AbstractFormatProfile
        implements Validator
{
    /** Broadcast Wave Format module version identifier. */
    public static final String VERSION = "2.0.0";

    /** Broadcast Wave Format module release date. */
    public static final String RELEASE = "2010-09-10";

    /** Broadcast Wave Format module rights statement. */
    public static final String RIGHTS =
        "Copyright 2010 by The Regents of the University of California" +
        "Available under the terms of the BSD license.";
    
    /** Broadcast Wave Format validation coverage. */
    public static final Coverage COVERAGE = Coverage.Inclusive;
  
    /** Broadcast Wave Format validation status. */
    protected Validity isValid;
    
    /** Baseline WAVE format is invalid message. */
    protected Message baselineWAVEFormatIsInvalidMessage;
    
    /** Missing required broadcast audio extension chunk message. */
    protected Message missingRequiredBextChunkMessage;
  
    /** Missing required MPEG-1 chunk message. */
    protected Message missingRequiredMPEG1ChunkMessage;

    /** Instantiate a new <code>BroadcastWaveProfile</code>
     * @param format Broadcast Wave Format.
     */
    public BroadcastWaveFormatProfile(Format format)
    {
        super(VERSION, RELEASE, RIGHTS, format);
        
        this.isValid = Validity.Undetermined;
    }

    /** Get profile coverage.
     * @return Profile coverage
     * @see org.jhove2.module.format.Validator#getCoverage()
     */
    @Override
    public Coverage getCoverage()
    {
        return COVERAGE;
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

    /** Validate the Broadcast Wave Format (BWF) source unit
     * @param jhove2 JHOVE2 framework
     * @param source Broadcast Wave Format source unit
     * @see org.jhove2.module.format.Validator#validate(org.jhove2.core.JHOVE2, org.jhove2.core.source.Source)
     */
    @Override
    public Validity validate(JHOVE2 jhove2, Source source)
            throws JHOVE2Exception
    {                
        Object [] args = null;
        
        /* Base WAVE must be valid. */
        this.isValid = ((WAVEModule) this.module).isValid();
        if (this.isValid == Validity.False) {
            this.baselineWAVEFormatIsInvalidMessage = new Message(Severity.ERROR,
                    Context.OBJECT,
                    "org.jhove2.module.format.wave.bwf.baselineWAVEForamtIsInvalid",
                    args, jhove2.getConfigInfo());
        }
        
        /* Broadcast audio extension chunk is required. */
        List<Chunk> chs = ((WAVEModule) this.module).getChunks();
        Iterator<Chunk> it = chs.iterator();
        while (it.hasNext()) {
            Chunk ch = it.next();
            String id = ch.getIdentifier();
            if (id.equals("RIFF")) {
                int     formatCategory = 0; 
                boolean hasBextChunk   = false;
                boolean hasMPEG1Chunk  = false;
                List<Chunk> children = ch.getChildChunks();
                Iterator<Chunk> iter = children.iterator();
                while (iter.hasNext()) {
                    Chunk chunk = iter.next();
                    id = chunk.getIdentifier();
                    
                    if (id.equals("bext")) {
                        hasBextChunk = true;
                    }
                    else if (id.equals("fmt ")) {
                        formatCategory =
                            ((FormatChunk) chunk).getFormatCategory_raw();
                    }
                    else if (id.equals("mext")) {
                        hasMPEG1Chunk = true;
                    }
                }
                
                if (!hasBextChunk) {
                    this.isValid = Validity.False;
                    this.missingRequiredBextChunkMessage = new Message(Severity.ERROR,
                            Context.OBJECT,
                            "org.jhove2.module.format.wave.bwf.BroadcastWaveFormatProfile.missingRequiredBextChunk",
                            args, jhove2.getConfigInfo());
                }
                if (formatCategory == FormatChunk.WAVE_FORMAT_MPEG &&
                    !hasMPEG1Chunk) {
                    this.isValid = Validity.False;
                    this.missingRequiredMPEG1ChunkMessage = new Message(Severity.ERROR,
                            Context.OBJECT,
                            "org.jhove2.module.format.wave.bwf.BroadcastWaveFormatProfile.missingRequiredMPEG1Chunk",
                            args, jhove2.getConfigInfo());
                    
                }
                break;
            }
        }
        
        return this.isValid;
    }
    
    /** Get baseline WAVE format is invalid message.
     * @return Baseline WAVE format is invalid message
     */
    @ReportableProperty(order=21, value="Baseline WAVE format is invalid.")
    public Message getBaselineWAVEFormatIsInvalidMessage() {
        return this.baselineWAVEFormatIsInvalidMessage;
    }
    
    /** Get missing required broadcast audio extension chunk message.
     * @return Missing required broadcast audio extension chunk message
     */
    @ReportableProperty(order=22, value="Missing required broadcast audio extension chunk.")
    public Message getMisingRequiredBroadcastAudioExtensionChunkMessage() {
        return this.missingRequiredBextChunkMessage;
    }
    
    /** Get missing required MPEG-1 chunk message.
     * @return Missing required MPEG-1 chunk message
     */
    @ReportableProperty(order=23, value="Missing required MPEG-1 chunk message.")
    public Message getMissingRequiredMPEG1ChunkMessage() {
        return this.missingRequiredMPEG1ChunkMessage;
    }
}
