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

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.annotation.ReportableProperty.PropertyType;
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

/** ICC signature type, as defined in ICC.1:2004-10, \u00a7 10.19.
 * 
 * @author slabrams
 */
public class SignatureType
    extends AbstractReportable
    implements Parser
{
    /** Signature type signature. */
    public static final String SIGNATURE = "sig ";
    
    /** Validation status. */
    protected Validity isValid;

    /** Content signature. */
    protected StringBuffer content = new StringBuffer(4);
    
    /** Content signature description. */
    protected String content_d;

    /** Signature (of tag type). */
    protected StringBuffer signature = new StringBuffer(4);   
    
    /** Invalid tag type message. */
    protected Message invalidTagTypeMessage;
    
    /** Non-zero data in reserved field message. */
    protected Message nonZeroDataInReservedFieldMessage;
    
    /** Instantiate a new <code>SignatureType</code>. */
    public SignatureType() {
        super();
        
        this.isValid = Validity.Undetermined;
    }
    
    /** Parse an ICC signature tag type element.
     * @param jhove2 JHOVE2 framework
     * @param source ICC source
     * @return Number of bytes consumed
     * @throws EOFException
     *             If End-of-File is reached reading the source unit
     * @throws IOException
     *             If an I/O exception is raised reading the source unit
     * @throws JHOVE2Exception
     */
    @Override
    public long parse(JHOVE2 jhove2, Source source)
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
        
        /* Content signature. */
        for (int i=0; i<4; i++) {
            short b = input.readUnsignedByte();
            content.append((char) b);
        }
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

    /** Get content signature in raw form.
     * @return Content signature in raw form
     */
    @ReportableProperty(order=1, value="Content signature in raw form.",
            ref="ICC.1:2004-10, \ua077 10.19", type=PropertyType.Raw)
    public String getContentSignature_raw() {
        return this.content.toString();
    }

    /** Get content signature in description form.
     * @return Content signature in description form
     */
    @ReportableProperty(order=1, value="Content signature in descriptive form.",
            ref="ICC.1:2004-10, \ua077 10.19", type=PropertyType.Descriptive)
    public String getContentSignature_description() {
        return this.content_d;
    }
    
    /** Get non-zero data in reserved field message.
     * @return Non-zero data in reserved field message
     */
    @ReportableProperty(order=12, value="Non-zero data in reserved field.")
    public Message getNonZeroDataInReservedFieldMessage() {
        return this.nonZeroDataInReservedFieldMessage;
    }
     
    /** Get validation status.
     * @return Validation status
     */
    @ReportableProperty(order=3, value="Validation status.",
            ref="ICC.1:2004-10, \u00a7 10.19")
    public Validity isValid() {
        return this.isValid;
    }
    
    /** Set the content signature description.
     * @param description Content signature description
     */
    public void setContentSignature_description(String description) {
        this.content_d = description;
    }
}
