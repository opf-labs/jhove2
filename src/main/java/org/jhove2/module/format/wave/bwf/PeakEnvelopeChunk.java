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

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.Message;
import org.jhove2.module.format.riff.GenericChunk;

/** Broadcast Wave Format (BWF) peak envelope chunk.
 * 
 * @author slabrams
 */
public class PeakEnvelopeChunk
        extends GenericChunk
{
    /** Block size. */
    protected long blockSize;
    
    /** Format of peak points. */
    protected long format;
    
    /** Number of channels. */
    protected long numChannels;
    
    /** Number of peak frames. */
    protected long numPeakFrames;
    
    /** Offset to peaks. */
    protected long offsetToPeaks;
    
    /** Points per value. */
    protected long pointsPerValue;
    
    /** Position of peak of peaks. */
    protected long posPeakOfPeaks;
    
    /** Time stamp. */
    protected String timeStamp;
    
    /** Version. */
    protected long version;
    
    /** Non-NUL data in reserved field message. */
    protected Message nonNULDataInReservedFieldMessage;
    
    /** Instantiate a new <code>PeakEnvelopeChunk<code>. */
    public PeakEnvelopeChunk() {
        super();
    }
    
    /** Get block size.
     * @return Block size
     */
    @ReportableProperty(order=4, value="Block size.")
    public long getBlockSize() {
        return this.blockSize;
    }
    
    /** Get format.
     * @return Format
     */
    @ReportableProperty(order=2, value="Format in raw form.")
    public long getFormat_raw() {
        return this.format;
    }
    
    /** Get number of channels.
     * @return Number of channels
     */
    @ReportableProperty(order=5, value="Number of channels.")
    public long getNumberOfChannels() {
        return this.numChannels;
    }
    
    /** Get number of peak frames.
     * @return Number of peak frames
     */
    @ReportableProperty(order=6, value="Number of peak frames.")
    public long getNumberOfPeakFrames() {
        return this.numPeakFrames;
    }
    
    /** Get offset to peaks. 
     * @return Offset to peaks
     */
    @ReportableProperty(order=8,value="Offset to peaks.")
    public long getOffsetToPeaks() {
        return this.offsetToPeaks;
    }
    
    /** Get points per value.
     * @return Points per value
     */
    @ReportableProperty(order=3, value="Points per value.")
    public long getPointsPerValue() {
        return this.pointsPerValue;
    }
    
    /** Get position of peak of peaks.
     * @return Position of peak of peaks
     */
    @ReportableProperty(order=7, value="Position of peak of peaks.")
    public long getPositionOfPeakOfPeaks() {
        return this.posPeakOfPeaks;
    }
    
    /** Get time stamp.
     * @return Time stamp
     */
    @ReportableProperty(order=9, value="Time stamp.")
    public String getTimeStamp() {
        return this.timeStamp;
    }
}
