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

import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jhove2.annotation.ReportableProperty;
import org.jhove2.annotation.ReportableProperty.PropertyType;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.Message;
import org.jhove2.core.Message.Context;
import org.jhove2.core.Message.Severity;
import org.jhove2.core.io.Input;
import org.jhove2.core.source.MeasurableSource;
import org.jhove2.core.source.Source;
import org.jhove2.module.format.Validator.Validity;
import org.jhove2.module.format.riff.GenericChunk;
import org.jhove2.module.format.wave.bwf.field.PeakFormat;

import com.sleepycat.persist.model.Persistent;

/** Broadcast Wave Format (BWF) peak envelope chunk.
 * 
 * @author slabrams
 */
@Persistent
public class PeakEnvelopeChunk
    extends GenericChunk
{
    /** Block size. */
    protected long blockSize;
    
    /** Format of peak points in raw form. */
    protected long format;
    
    /** Format of peak points in descriptive form. */
    protected String format_d;
    
    /** Number of channels. */
    protected long numChannels;
    
    /** Number of peak frames. */
    protected long numPeakFrames;
    
    /** Offset to peaks. */
    protected long offsetToPeaks;
    
    /** Points per value in raw form. */
    protected long pointsPerValue;
    
    /** Points per value in descriptive form. */
    protected String pointsPerValue_d;
    
    /** Position of peak of peaks. */
    protected long posPeakOfPeaks;
    
    /** Time stamp. */
    protected String timeStamp;
    
    /** Version. */
    protected long version;
    
    /** Invalid peak point format message. */
    protected Message invalidPointFormatMessage;
    
    /** Invalid number of peak points message. */
    protected Message invalidNumberOfPointsMessage;
    
    /** Non-NUL data in reserved field messages. */
    protected List<Message> nonNULDataInReservedFieldMessages;
    
    /** Instantiate a new <code>PeakEnvelopeChunk<code>. */
    public PeakEnvelopeChunk() {
        super();
        
        this.nonNULDataInReservedFieldMessages = new ArrayList<Message>();
    }
    
    /** 
     * Parse a WAVE chunk.
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
     */
    @Override
    public long parse(JHOVE2 jhove2, Source source, Input input)
        throws EOFException, IOException, JHOVE2Exception
    {
        long consumed = super.parse(jhove2, source, input);
        long start    = ((MeasurableSource) source).getStartingOffset();
        
        /* Version. */
        this.version = input.readUnsignedInt();
        consumed += 4;
        
        /* Format. */
        this.format = input.readUnsignedInt();
        PeakFormat fmt = PeakFormat.getPeakFormat(this.format, jhove2);
        if (fmt != null) {
            this.format_d = fmt.getDescription();
        }
        consumed += 4;
        
        /* Points per value. */
        this.pointsPerValue = input.readUnsignedInt();
        consumed += 4;
        
        /* Block size. */
        this.blockSize = input.readUnsignedInt();
        consumed += 4;
        
        /* Number of peak channels. */
        this.numChannels = input.readUnsignedInt();
        consumed += 4;
        
        /* Number of peak frames. */
        this.numPeakFrames = input.readUnsignedInt();
        consumed += 4;
        
        /* Position of the peak of peaks. */
        this.posPeakOfPeaks = input.readUnsignedInt();
        consumed += 4;
        
        /* Time stamp. */
        StringBuffer sb = new StringBuffer(28);
        for (int i=0; i<28; i++) {
            short b = input.readUnsignedByte();
            sb.append((char) b);
        }
        this.timeStamp = sb.toString();
        consumed += 28;
        
        /* Reserved: must be NUL. */
        for (int i=0; i<60; i++) {
            short b = input.readUnsignedByte();
            if (b != 0) {
                this.isValid = Validity.False;
                Object [] args = new Object [] {input.getPosition()-1L-start, b};
                Message msg = new Message(Severity.ERROR,
                        Context.OBJECT,
                        "org.jhove2.module.format.wave.bwf.PeakEnvelopeChunk.nonNULDataInReservedField",
                        args, jhove2.getConfigInfo());
                this.nonNULDataInReservedFieldMessages.add(msg);
            }
        }
        
        return consumed;
    }
    
    /** Get block size.
     * @return Block size
     */
    @ReportableProperty(order=6, value="Block size.")
    public long getBlockSize() {
        return this.blockSize;
    }
    
    /** Get format.
     * @return Format
     */
    @ReportableProperty(order=3, value="Format in raw form.",
            type=PropertyType.Descriptive)
    public String getFormat_descriptive() {
        return this.format_d;
    }
    
    /** Get format.
     * @return Format
     */
    @ReportableProperty(order=2, value="Format in raw form.",
            type=PropertyType.Raw)
    public long getFormat_raw() {
        return this.format;
    }
    
    /** Get invalid number of peak points message.
     * @return Invalid number of peak points message
     */
    @ReportableProperty(order=22, value="Invalid number of peak points message.")
    public Message getInvalidNumberOfPeakPointsMessage() {
        return this.invalidNumberOfPointsMessage;
    }
    
    /** Get invalid peak point format message.
     * @return Invalid peak point format message
     */
    @ReportableProperty(order=21, value="Invalid peak point format message.")
    public Message getInvalidPeakPointFormatMessage() {
        return this.invalidPointFormatMessage;
    }
    
    /** Get non-NUL data in reserved field messages.
     * @return Non-NUL data in reserved field messages
     */
    @ReportableProperty(order=23, value="Non-NUL data in reserved field messages.")
    public List<Message> getNonNULDataInReservedFieldMessages() {
        return this.nonNULDataInReservedFieldMessages;
    }
    
    /** Get number of channels.
     * @return Number of channels
     */
    @ReportableProperty(order=7, value="Number of channels.")
    public long getNumberOfChannels() {
        return this.numChannels;
    }
    
    /** Get number of peak frames.
     * @return Number of peak frames
     */
    @ReportableProperty(order=8, value="Number of peak frames.")
    public long getNumberOfPeakFrames() {
        return this.numPeakFrames;
    }
    
    /** Get offset to peaks. 
     * @return Offset to peaks
     */
    @ReportableProperty(order=10,value="Offset to peaks.")
    public long getOffsetToPeaks() {
        return this.offsetToPeaks;
    }
    
    /** Get points per value in descriptive form.
     * @return Points per value
     */
    @ReportableProperty(order=6, value="Points per value in descriptive form.",
            type=PropertyType.Raw)
    public String getPointsPerValue_descriptive() {
        return this.pointsPerValue_d;
    }
    
    /** Get points per value in raw form.
     * @return Points per value
     */
    @ReportableProperty(order=5, value="Points per value in raw form.",
            type=PropertyType.Raw)
    public long getPointsPerValue_raw() {
        return this.pointsPerValue;
    }
    
    /** Get position of peak of peaks.
     * @return Position of peak of peaks
     */
    @ReportableProperty(order=9, value="Position of peak of peaks.")
    public long getPositionOfPeakOfPeaks() {
        return this.posPeakOfPeaks;
    }
    
    /** Get time stamp.
     * @return Time stamp
     */
    @ReportableProperty(order=11, value="Time stamp.")
    public String getTimeStamp() {
        return this.timeStamp;
    }
}
