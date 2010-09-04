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
import java.util.ArrayList;
import java.util.List;
import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.io.Input;
import org.jhove2.module.format.riff.GenericChunk;

/** WAVE MPEG-1 audio chunk, as defined by EBU Tech 3285–E –- Supplement 1,
 * July 1997.
 * 
 * @author slabrams
 */
public class MPEGChunk
    extends GenericChunk
{
    /** Ancillary data length. */
    protected int dataLength;
   
    /** Ancillary data type in hexadecimal form. */
    protected String dataType;
    
    /** Ancillary data type in descriptive form. */
    protected List<String> dataType_d;
    
    /** Frame size. */
    protected int frameSize;
    
    /** Sound information, in hexadecimal form. */
    protected String soundInformation;
    
    /** Sound information in descriptive form. */
    protected List<String> soundInformation_d;
    
    /** Instantiate a new <code>MPEGChunk</code>. */
    public MPEGChunk() {
        super();
        
        this.dataType_d         = new ArrayList<String>();
        this.soundInformation_d = new ArrayList<String>();
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
        
        /* Sound information. */
        int sh = input.readUnsignedShort();
        this.soundInformation = String.format("0x%04x", sh);
        consumed += 2;
        
        /* Frame size. */
        this.frameSize = input.readUnsignedShort();
        consumed += 2;
        
        /* Ancillary data length. */
        this.dataLength = input.readUnsignedShort();
        consumed += 2;
        
        /* Ancillary data type. */
        sh = input.readUnsignedShort();
        this.soundInformation = String.format("0x%04x", sh);
        consumed += 2;
        
        return consumed;
    }
    
    /** Get ancillary data length.
     * @return Ancillary data length
     */
    @ReportableProperty(order=4, value="Ancillary data length.")
    public int getAncillaryDataLength() {
        return this.dataLength;
    }
    
    /** Get ancillary data type in descriptive form.
     * @return Ancillary data type in descriptive form
     */
    @ReportableProperty(order=6, value="Ancillary data type in descriptive form.")
    public List<String> getAncillaryDataTypes() {
        return this.dataType_d;
    }
    
    /** Get ancillary data type in raw form.
     * @return Ancillary data type
     */
    @ReportableProperty(order=5, value="Ancillary data type in hexadecimal form.")
    public String getAncillaryDataType_raw() {
        return this.dataType;
    }
    
    /** Get frame size.
     * @return Frame size
     */
    @ReportableProperty(order=3, value="Frame size.")
    public int getFrameSize() {
        return this.frameSize;
    }
    
    /** Get sound information in descriptive form.
     * @return Sound information
     */
    @ReportableProperty(order=2, value="Sound information in descriptive form.")
    public List<String> getSoundInformations() {
        return this.soundInformation_d;
    }
    
    /** Get sound information in raw form.
     * @return Sound information
     */
    @ReportableProperty(order=1, value="Sound information in hexadecimal form.")
    public String getSoundInformation() {
        return this.soundInformation;
    }
}
