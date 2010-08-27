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

package org.jhove2.module.format.icc.type;

import java.io.EOFException;
import java.io.IOException;
import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.Message;
import org.jhove2.core.Message.Context;
import org.jhove2.core.Message.Severity;
import org.jhove2.core.io.Input;
import org.jhove2.core.reportable.AbstractReportable;
import org.jhove2.module.format.Validator.Validity;

/** ICC 8-bit lookup table (LUT) type element, as defined in
 * ICC.1:2004-10, \u00a7 10.9.
 * 
 * @author slabrams
 */
public class LUT8Type
        extends AbstractReportable
{
    /** LUT type signature. */
    public static final String SIGNATURE = "mft1";

    /** E00 parameter. */
    protected S15Fixed16Number e00;

    /** E01 parameter. */
    protected S15Fixed16Number e01;

    /** E02 parameter. */
    protected S15Fixed16Number e02;

    /** E10 parameter. */
    protected S15Fixed16Number e10;

    /** E11 parameter. */
    protected S15Fixed16Number e11;

    /** E12 parameter. */
    protected S15Fixed16Number e12;

    /** E20 parameter. */
    protected S15Fixed16Number e20;

    /** E21 parameter. */
    protected S15Fixed16Number e21;

    /** E22 parameter. */
    protected S15Fixed16Number e22;
    
    /** Validation status. */
    protected Validity isValid;
    
    /** Number of CLUT grid points. */
    protected short numGridPoints;
    
    /** Number of input channels. */
    protected short numInputChannels;
    
    /** Number of output channels. */
    protected short numOutputChannels;
   
    /** Signature. */
    protected StringBuffer signature = new StringBuffer(4);   

    /** Invalid tag type message. */
    protected Message invalidTagTypeMessage;
     
    /** Non-zero data in reserved field message. */
    protected Message nonZeroDataInReservedFieldMessage;
    
    /** Instantiate a new <code>LUT8Type</code>. */
    public LUT8Type() {
        super();
        
        this.isValid = Validity.Undetermined;
    }
    
    /** Parse an ICC LUT 8 tag type.
     * @param jhove2 JHOVE2 framework
     * @param input  ICC input
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
        long consumed  = 0L;
        int  numErrors = 0;
        this.isValid = Validity.True;
  
        /* Tag signature. */
        for (int i=0; i<4; i++) {
            short b = input.readUnsignedByte();
            this.signature.append((char) b);
        }
        if (!this.signature.toString().equals(SIGNATURE)) {
            numErrors++;
            this.isValid = Validity.False;
            Object [] args =
                new Object [] {input.getPosition()-4L, SIGNATURE,
                               signature.toString()};
            this.invalidTagTypeMessage = new Message(Severity.ERROR,
                Context.OBJECT,
                "org.jhove2.module.format.icc.ICCTag.InvalidTagType",
                args, jhove2.getConfigInfo());
        }
        consumed += 4;
        
        /* Reserved. */
        int reserved = input.readSignedInt();
        if (reserved != 0) {
            numErrors++;
            this.isValid = Validity.False;
            Object [] args = new Object [] {input.getPosition()-4L};
            this.nonZeroDataInReservedFieldMessage = new Message(Severity.ERROR,
                    Context.OBJECT,
                    "org.jhove2.module.format.icc.ICCTag.NonZeroDataInReservedField",
                    args, jhove2.getConfigInfo());
        }
        consumed += 4;
        
        /* Number of input channels. */
        this.numInputChannels = input.readUnsignedByte();
        consumed++;

        /* Number of output channels. */
        this.numOutputChannels = input.readUnsignedByte();
        consumed++;
 
        /* Reserved. */
        byte res = input.readSignedByte();
        if (res != 0) {
            numErrors++;
            this.isValid = Validity.False;
            Object [] args = new Object [] {input.getPosition()-4L};
            this.nonZeroDataInReservedFieldMessage = new Message(Severity.ERROR,
                    Context.OBJECT,
                    "org.jhove2.module.format.icc.ICCTag.NonZeroDataInReservedField",
                    args, jhove2.getConfigInfo());
        }
        consumed++;

        /* E00 parameter. */
        this.e00 = new S15Fixed16Number(input.readSignedInt());
        consumed += 4;

        /* E01 parameter. */
        this.e01 = new S15Fixed16Number(input.readSignedInt());
        consumed += 4;

        /* E02 parameter. */
        this.e02 = new S15Fixed16Number(input.readSignedInt());
        consumed += 4;

        /* E10 parameter. */
        this.e10 = new S15Fixed16Number(input.readSignedInt());
        consumed += 4;

        /* E11 parameter. */
        this.e11 = new S15Fixed16Number(input.readSignedInt());
        consumed += 4;

        /* E12 parameter. */
        this.e12 = new S15Fixed16Number(input.readSignedInt());
        consumed += 4;

        /* E20 parameter. */
        this.e20 = new S15Fixed16Number(input.readSignedInt());
        consumed += 4;

        /* E21 parameter. */
        this.e21 = new S15Fixed16Number(input.readSignedInt());
        consumed += 4;

        /* E22 parameter. */
        this.e22 = new S15Fixed16Number(input.readSignedInt());
        consumed += 4;

        return consumed;
    }
    
    /** Get E00 parameter.
     * @return E00 parameter
     */
    @ReportableProperty(order=4, value="E00 parameter.",
            ref="ICC.1:2004-10, Table 34")
    public S15Fixed16Number getE00Parameter() {
        return this.e00;
    }
    
    /** Get E01 parameter.
     * @return E01 parameter
     */
    @ReportableProperty(order=5, value="E01 parameter.",
            ref="ICC.1:2004-10, Table 34")
    public S15Fixed16Number getE01Parameter() {
        return this.e01;
    }
    
    /** Get E02 parameter.
     * @return E02 parameter
     */
    @ReportableProperty(order=6, value="E02 parameter.",
            ref="ICC.1:2004-10, Table 34")
    public S15Fixed16Number getE02Parameter() {
        return this.e02;
    }
    
    /** Get E10 parameter.
     * @return E10 parameter
     */
    @ReportableProperty(order=7, value="E10 parameter.",
            ref="ICC.1:2004-10, Table 34")
    public S15Fixed16Number getE10Parameter() {
        return this.e10;
    }
    
    /** Get E11 parameter.
     * @return E11 parameter
     */
    @ReportableProperty(order=8, value="E11 parameter.",
            ref="ICC.1:2004-10, Table 34")
    public S15Fixed16Number getE11Parameter() {
        return this.e11;
    }
    
    /** Get E12 parameter.
     * @return E12 parameter
     */
    @ReportableProperty(order=9, value="E12 parameter.",
            ref="ICC.1:2004-10, Table 34")
    public S15Fixed16Number getE12Parameter() {
        return this.e12;
    }
    
    /** Get E20 parameter.
     * @return E20 parameter
     */
    @ReportableProperty(order=10, value="E20 parameter.",
            ref="ICC.1:2004-10, Table 34")
    public S15Fixed16Number getE20Parameter() {
        return this.e20;
    }
    
    /** Get E21 parameter.
     * @return E21 parameter
     */
    @ReportableProperty(order=11, value="E21 parameter.",
            ref="ICC.1:2004-10, Table 34")
    public S15Fixed16Number getE21Parameter() {
        return this.e21;
    }
    
    /** Get E22 parameter.
     * @return E22 parameter
     */
    @ReportableProperty(order=12, value="E22 parameter.",
            ref="ICC.1:2004-10, Table 34")
    public S15Fixed16Number getE22Parameter() {
        return this.e22;
    }
    
    /** Get invalid tag type message.
     * @return Invalid tag type message
     */
    @ReportableProperty(order=51, value="Invalid tag type.")
    public Message getInvalidTagTypeMessage() {
        return this.invalidTagTypeMessage;
    }
 
    /** Get non-zero data in reserved field message.
     * @return Non-zero data in reserved field message
     */
    @ReportableProperty(order=52, value="Non-zero data in reserved field.",
            ref="ICC.1:2004-10, \ua077 10.20")
    public Message getNonZeroDataInReservedFieldMessage() {
        return this.nonZeroDataInReservedFieldMessage;
    }

    /** Get number of input channels.
     * @return Number of input channels
     */
    @ReportableProperty(order=1, value="Number of input channels.",
            ref="ICC.1:2004-10, Table 35.")
    public short getNumberOfInputChannels() {
        return this.numInputChannels;
    }
   
    /** Get number of output channels.
     * @return Number of output channels
     */
    @ReportableProperty(order=2, value="Number of output channels.",
            ref="ICC.1:2004-10, Table 35")
    public short getNumberOfOutputChannels() {
        return this.numOutputChannels;
    }
    
    /** Get number of colour lookup table (CLUT) grid points.
     * @return Number of CLUT grid points
     */
    @ReportableProperty(order=3, value="Number of colour lookup table (CLUT) grid points.",
            ref="ICC.1:2004-10, Table 34")
    public short getNumberOfCLUTGridPoints() {
        return this.numGridPoints;
    }
      
    /** Get validation status.
     * @return Validation status
     */
    @ReportableProperty(order=13, value="Validation status.",
            ref="ICC.1:2004-10, \u00a7 10.8")
    public Validity isValid() {
        return this.isValid;
    }
}
