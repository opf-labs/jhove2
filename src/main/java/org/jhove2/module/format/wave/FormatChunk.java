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

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.Message;
import org.jhove2.core.Message.Context;
import org.jhove2.core.Message.Severity;
import org.jhove2.core.io.Input;
import org.jhove2.module.format.Validator.Validity;
import org.jhove2.module.format.riff.GenericChunk;
import org.jhove2.module.format.wave.field.FormatCategory;

/** WAVE chunk. Note that this class represents a generic WAVE chunk, not the
 * specific "WAVE" chunk.
 * 
 * @author slabrams
 */
public class FormatChunk
    extends GenericChunk
{
    /** WAVE format A-law format. */
    public static final int WAVE_FORMAT_ALAW = 0x0006;
    
    /** WAVE format extensible format. */
    public static final int WAVE_FORMAT_EXTENSIBLE = 0xfffe;
    
    /** WAVE format MPEG-1 audio format. */
    public static final int WAVE_FORMAT_MPEG = 0x0050;

    /** WAVE format mu-law format. */
    public static final int WAVE_FORMAT_MULAW = 0x0007;
    
    /** WAVE format IEEE format. */
    public static final int WAVE_FORMAT_IEEE_FLOAT = 0x0003;
    
    /** WAVE format Pulse Code Modulation (PCM) format. */
    public static final int WAVE_FORMAT_PCM = 0x0001;
    
    /** WAVE format IBM mu-law format. */
    public static final int IBM_FORMAT_MULAW = 0x0101;
    
    /** WAVE format IBM a-law format. */
    public static final int IBM_FORMAT_ALAW = 0x0102;
    
    /** WAVE format IBM ADPCM format. */
    public static final int IBM_FORMAT_ADPCM = 0x0103;
    
    /** WAVE format chunk average bytes per second. */
    protected long avgBytesPerSec;
 
    /** WAVE format chunk data block size. */
    protected int dataBlockSize;
    
    /** WAVE format chunk format category in raw form. */
    protected int formatCategory;
    
    /** WAVE format chunk format category in descriptive form. */
    protected String formatCategory_d;
    
    /** WAVE format chunk invalid format category message. */
    protected Message invalidFormatCategoryMessage;
    
    /** WAVE format chunk number of channels. */
    protected int numChannels;
    
    /** PCM format bits per sample. */
    protected int bitsPerSample;
    
    /** WAVE format chunk sampling rate. */
    protected long samplingRate;
    
    /** WAVE format chunk size of extra information, in bytes. */
    protected int size;
    
    /** Instantiate a new <code>FormatChunk</code>. */
    public FormatChunk() {
        super();
    }
    
    /** 
     * Parse a WAVE chunk.
     * 
     * @param jhove2
     *            JHOVE2 framework
     * @param input
     *            WAVE input
     * @return Number of bytes consumed
     * @throws EOFException
     *             If End-of-File is reached reading the source unit
     * @throws IOException
     *             If an I/O exception is raised reading the source unit
     * @throws JHOVE2Exception
     */
    public long parse(JHOVE2 jhove2, Input input)
        throws EOFException, IOException, JHOVE2Exception
    {
        long consumed = super.parse(jhove2, input);
        int numErrors = 0;
        
        /* Format category */
        this.formatCategory = input.readUnsignedShort();
        FormatCategory category =
            FormatCategory.getFormatCategory(this.formatCategory, jhove2);
        if (category != null) {
            this.formatCategory_d = category.getCategory();
        }
        else {
            numErrors++;
            this.isValid = Validity.False;
            Object [] args = new Object [] {input.getPosition()-4L, this.formatCategory};
            this.invalidFormatCategoryMessage = new Message(Severity.ERROR,
                    Context.OBJECT,
                    "org.jhove2.module.format.wave.FormatChunk.invalidFormatCategory",
                    args, jhove2.getConfigInfo());
        }
        consumed += 2;
        
        /* Number of channels. */
        this.numChannels = input.readUnsignedShort();
        consumed += 2;
        
        /* Sampling rate. */
        this.samplingRate = input.readUnsignedInt();
        consumed += 4;
        
        /* Average bytes per second. */
        this.avgBytesPerSec = input.readUnsignedInt();
        consumed += 4;
        
        /* Data block size. */
        this.dataBlockSize = input.readUnsignedShort();
        consumed += 2;
        
        this.bitsPerSample = input.readUnsignedShort();
        consumed += 2;
        
        if (this.formatCategory != WAVE_FORMAT_PCM) {
            this.size = input.readUnsignedShort();
            consumed += 2;
        }
        
        return consumed;
    }
    
    /** Get WAVE format chunk average bytes per second.
     * @return WAVE format chunk average bytes per second
     */
    @ReportableProperty(order=5, value="WAVE format chunk average bytes per second.")
    public long getAverageBytesPerSecond() {
        return this.avgBytesPerSec;
    }
    
    /** Get PCM format bits per sample.
     * @return PCM format bits per sample
     */
    @ReportableProperty(order=7, value="PCM format bits per sample.")
    public int getBitsPerSample() {
        return this.bitsPerSample;
    }
    
    /** Get size of extra information, in bytes.
     * @return Size of extra information
     */
    @ReportableProperty(order=8, value="Size of extra information, in bytes.")
    public int getSizeOfExtraInformation() {
        return this.size;
    }
    
    /** Get WAVE format chunk data block size.
     * @return WAVE format chunk data block size
     */
    @ReportableProperty(order=6, value="WAVE format chunk data block size.")
    public int getDataBlockSize() {
        return this.dataBlockSize;
    }
    
    /** Get WAVE format chunk format category in descriptive form.
     * @return WAVE format chunk format category
     */
    @ReportableProperty(order=2, value="WAVE format chunk format category in descriptive form.")
    public String getFormatCategory_descriptive() {
        return this.formatCategory_d;
    }
    
    /** Get WAVE format chunk format category in raw form.
     * @return WAVE format chunk format category
     */
    @ReportableProperty(order=1, value="WAVE format chunk format category in raw form.")
    public int getFormatCategory_raw() {
        return this.formatCategory;
    }
    
    /** Get WAVE format chunk invalid format category message.
     * @return WAVE format chunk invalid format category message
     */
    @ReportableProperty(order=21, value="WAVE format chunk invalid format category message.")
    public Message getInvalidFormatCategoryMessage() {
        return this.invalidFormatCategoryMessage;
    }
    
    /** Get WAVE format chunk number of channels.
     * @return WAVE format number of channels
     */
    @ReportableProperty(order=3, value="WAVE format chunk number of channels.")
    public int getNumChannels() {
        return this.numChannels;
    }
    
    /** Get WAVE format chunk sampling rate.
     * @return WAVE format sampling rate
     */
    @ReportableProperty(order=4, value="WAVE format chunk sampling rate.")
    public long getSamplingRate() {
        return this.samplingRate;
    }
}
