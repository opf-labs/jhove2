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
import org.jhove2.core.source.Source;
import org.jhove2.module.format.Parser;
import org.jhove2.module.format.Validator.Validity;

import com.sleepycat.persist.model.Persistent;

/** ICC lookup table (LUT) A-to-B type element,
 * as defined in ICC.1:2004-10, \u00a7 10.10.
 * 
 * @author slabrams
 */
@Persistent
public class LUTAToBType
    extends AbstractReportable
    implements Parser
{
    /** LUT A-to-B type signature. */
    public static final String SIGNATURE = "mAB ";
 
    /** Validation status. */
    protected Validity isValid;
    
    /** Number of input channels. */
    protected short numInputChannels;
    
    /** Number of output channels. */
    protected short numOutputChannels;
   
    /** Offset to first "A" curve. */
    protected long offsetACurve;
    
    /** Offset to first "B" curve. */
    protected long offsetBCurve;
   
    /** Offset to colour lookup table (CLUT). */
    protected long offsetCLUT;
    
    /** Offset to matrix. */
    protected long offsetMatrix;
    
    /** Offset to first "M" curve. */
    protected long offsetMCurve;
 
    /** Signature. */
    protected StringBuffer signature = new StringBuffer(4);   

    /** Invalid tag type message. */
    protected Message invalidTagTypeMessage;
     
    /** Non-zero data in reserved field message. */
    protected Message nonZeroDataInReservedFieldMessage;
    
    /** Instantiate a new <code>LUTAToBType</code>. */
    public LUTAToBType() {
        super();
        
        this.isValid = Validity.Undetermined;
    }
    
    /** Parse an ICC LUT A-to-B tag type.
     * @param jhove2 JHOVE2 framework
     * @param source ICC source unit
     * @param input  ICC source input
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
        long consumed  = 0L;
        int  numErrors = 0;
        this.isValid   = Validity.True;
        long start     = source.getStartingOffset();
        
        /* Tag signature. */
        for (int i=0; i<4; i++) {
            short b = input.readUnsignedByte();
            this.signature.append((char) b);
        }
        if (!this.signature.toString().equals(SIGNATURE)) {
            numErrors++;
            this.isValid = Validity.False;
            Object [] args =
                new Object [] {input.getPosition()-4L-start, SIGNATURE,
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
            Object [] args = new Object [] {input.getPosition()-4L-start};
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
        short res = input.readSignedShort();
        if (res != 0) {
            numErrors++;
            this.isValid = Validity.False;
            Object [] args = new Object [] {input.getPosition()-2L-start};
            this.nonZeroDataInReservedFieldMessage = new Message(Severity.ERROR,
                    Context.OBJECT,
                    "org.jhove2.module.format.icc.ICCTag.NonZeroDataInReservedField",
                    args, jhove2.getConfigInfo());
        }
        consumed += 2;

        /* Offset to first 'B' curve. */
        this.offsetBCurve = input.readUnsignedInt();
        consumed += 4;

        /* Offset to matrix. */
        this.offsetMatrix = input.readUnsignedInt();
        consumed += 4;

        /* Offset to first 'M' curve. */
        this.offsetMCurve = input.readUnsignedInt();
        consumed += 4;

        /* Offset to colour lookup table (CLUT). */
        this.offsetCLUT = input.readUnsignedInt();
        consumed += 4;

        /* Offset to first 'A' curve. */
        this.offsetACurve = input.readUnsignedInt();
        consumed += 4;
       
        return consumed;
    }
    
    /** Get invalid tag type message.
     * @return Invalid tag type message
     */
    @ReportableProperty(order=11, value="Invalid tag type.")
    public Message getInvalidTagTypeMessage() {
        return this.invalidTagTypeMessage;
    }
 
    /** Get non-zero data in reserved field message.
     * @return Non-zero data in reserved field message
     */
    @ReportableProperty(order=12, value="Non-zero data in reserved field.",
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
    
    /** Get offset to first "A" curve.
     * @return Offset to first "A" curve
     */
    @ReportableProperty(order=7, value="Offset to first 'A' curve.",
            ref="ICC.1:2004-10, Table 35")
    public long getOffsetToFirstACurve() {
        return this.offsetACurve;
    }
    
    /** Get offset to first "B" curve.
     * @return Offset to first "B" curve
     */
    @ReportableProperty(order=3, value="Offset to first 'B' curve.",
            ref="ICC.1:2004-10, Table 35")
    public long getOffsetToFirstBCurve() {
        return this.offsetBCurve;
    }
    
    /** Get offset to colour lookup table (CLUT).
     * @return Offset to CLUT
     */
    @ReportableProperty(order=6, value="Offset to colour lookup table (CLUT).",
            ref="ICC.1:2004-10, Table 35")
    public long getOffsetToCLUT() {
        return this.offsetCLUT;
    }
    
    /** Get offset to matrix.
     * @return Offset to matrix
     */
    @ReportableProperty(order=4, value="Offset to matrix.",
            ref="ICC.1:2004-10, Table 35")
    public long getOffsetToMatrix() {
        return this.offsetMatrix;
    }
    
    /** Get offset to first "M" curve.
     * @return Offset to first "M" curve
     */
    @ReportableProperty(order=5, value="Offset to first 'M' curve.",
            ref="ICC.1:2004-10, Table 35")
    public long getOffsetToFirstMCurve() {
        return this.offsetMCurve;
    }
    
    /** Get validation status.
     * @return Validation status
     */
    @ReportableProperty(order=8, value="Validation status.",
            ref="ICC.1:2004-10, \u00a7 10.10")
    public Validity isValid() {
        return this.isValid;
    }
}
