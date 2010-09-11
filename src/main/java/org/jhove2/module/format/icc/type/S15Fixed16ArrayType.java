/**
 * JHOVE2 - Next-generation architecture for format-aware characterization
 *
 * Copyright (c) 2009 by The Regents of the University of California
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
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.Message;
import org.jhove2.core.Message.Context;
import org.jhove2.core.Message.Severity;
import org.jhove2.core.io.Input;
import org.jhove2.core.reportable.AbstractReportable;
import org.jhove2.core.source.Source;
import org.jhove2.module.format.Validator.Validity;

/** ICC signed 32-bit fixed point number array type element,
 * as defined in ICC.1:2004-10, \u00a7 10.18.
 * 
 * @author slabrams
 */
public class S15Fixed16ArrayType
    extends AbstractReportable
{
    /** Type signature. */
    public static final String SIGNATURE = "sf32";
    
    /** Validation status. */
    protected Validity isValid;
    
    /** Number of values. */
    protected long numberOfValues;

    /** Signature (of tag type). */
    protected StringBuffer signature = new StringBuffer(4);   
    
    /** Values. */
    protected List<S15Fixed16Number> values;
    
    /** Invalid tag type message. */
    protected Message invalidTagTypeMessage;
    
    /** Non-zero data in reserved field message. */
    protected Message nonZeroDataInReservedFieldMessage;
    
    /** Instantiate a new <code>S15Fixed16ArrayType</code>. */
    public S15Fixed16ArrayType() {
        super();
        
        this.isValid = Validity.Undetermined;
        this.values  = new ArrayList<S15Fixed16Number>();
    }
    
    /** Parse an ICC fixed signed array tag type element.
     * @param jhove2 JHOVE2 framework
     * @param source ICC source
     * @param elementSize Size in bytes of the element
     * @return Number of bytes consumed
     * @throws EOFException
     *             If End-of-File is reached reading the source unit
     * @throws IOException
     *             If an I/O exception is raised reading the source unit
     * @throws JHOVE2Exception
     */
    public long parse(JHOVE2 jhove2, Source source, long elementSize)
        throws EOFException, IOException, JHOVE2Exception
    {
        long consumed  = 0L;
        int  numErrors = 0;
        this.isValid   = Validity.True;
        Input input    = source.getInput(jhove2, ByteOrder.BIG_ENDIAN);
  
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
        
        this.numberOfValues = (elementSize-8)/4;
        for (int i=0; i<this.numberOfValues; i++) {
            int in = input.readSignedInt();
            S15Fixed16Number s15f16 = new S15Fixed16Number(in);
            this.values.add(s15f16);
            consumed += 4;
        }
            
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
    @ReportableProperty(order=12, value="Non-zero data in reserved field.")
    public Message getNonZeroDataInReservedFieldMessage() {
        return this.nonZeroDataInReservedFieldMessage;
    }
    
    /** Get number of values.
     * @return Number of values
     */
    @ReportableProperty(order=2, value="Values.",
            ref="ICC.1:2004-10, \u00a7 10.18")
    public long getNumberOfValues() {
        return this.numberOfValues;
    }
    /** Get type signature.
     * @return Type signature
     */
    @ReportableProperty(order=1, value="Type signature.",
            ref="ICC.1:2004-10, \u00a7 10.18")
    public String getSignature() {
        return this.signature.toString();
    }
    
    /** Get values.
     * @return Values
     */
    @ReportableProperty(order=3, value="Values.",
            ref="ICC.1:2004-10, \u00a7 10.18")
    public List<S15Fixed16Number> getValues() {
        return this.values;
    }
    
    /** Get validation status.
     * @return Validation status
     */
    @ReportableProperty(order=4, value="Validation status.",
            ref="ICC.1:2004-10, \u00a7 10.18")
    public Validity isValid() {
        return this.isValid;
    }
}
