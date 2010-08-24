/**
 * JHOVE2 - Next-generation architecture for format-aware characterization
 * <p>
 * Copyright (c) 2010 by The Regents of the University of California. All rights reserved.
 * </p>
 * <p>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * </p>
 * <ul>
 * <li>Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.</li>
 * <li>Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.</li>
 * <li>Neither the name of the University of California/California Digital
 * Library, Ithaka Harbors/Portico, or Stanford University, nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.</li>
 * </ul>
 * <p>
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
 * </p>
 */

package org.jhove2.module.format.icc;

import java.io.EOFException;
import java.io.IOException;
import org.jhove2.annotation.ReportableProperty;
import org.jhove2.annotation.ReportableProperty.PropertyType;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.Message;
import org.jhove2.core.Message.Context;
import org.jhove2.core.Message.Severity;
import org.jhove2.core.io.Input;
import org.jhove2.core.reportable.AbstractReportable;
import org.jhove2.module.format.Validator.Validity;
import org.jhove2.module.format.icc.field.Tag;

/** ICC tag.  See ICC.1:2004-10, \u00a7 7.3.1.
 * 
 * @author slabrams
 */
public class ICCTag
    extends AbstractReportable
{
    /** Tag validity. */
    protected Validity isValid;
    
    /** Tag offset. */
    protected long offset;
    
    /** Tag signature in raw form. */
    protected StringBuffer signature = new StringBuffer(4);
    
    /** Tag signature in descriptive form. */
    protected String signature_d;
    
    /** Tag size. */
    protected long size;
    
    /** Tag vendor. */
    protected String vendor;
    
    /** Invalid tag message. */
    protected Message invalidTagMessage;
    
    /** Non-ICC tag message. */
    protected Message nonICCTagMessage;
    
    /** Offset not word aligned message. */
    protected Message offsetNotWordAlignedMessage;
    
    /** Instantiate a new <code>ICCTag</code.
     */
    public ICCTag() {
        super();
        
        this.isValid = Validity.Undetermined;
    }
    
    /** Parse an ICC tag.
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
        long consumed = 0L;
        int numErrors = 0;
        this.isValid = Validity.True;
  
        /* Tag signature. */
        for (int i=0; i<4; i++) {
            short b = input.readUnsignedByte();
            this.signature.append((char) b);
        }
        Tag tag = Tag.getTag(this.signature.toString(), jhove2);
        if (tag != null) {
            this.signature_d = tag.getName();
            this.vendor      = tag.getVendor();
        }
        else {
            numErrors++;
            this.isValid = Validity.False;
            Object [] args = new Object [] {input.getPosition()-4L, signature.toString()};
            this.invalidTagMessage = new Message(Severity.ERROR,
                Context.OBJECT,
                "org.jhove2.module.format.icc.ICCTag.InvalidTag",
                args, jhove2.getConfigInfo());
        }
        consumed += 4;
        
        /* Tag offset. */
        this.offset = input.readUnsignedInt();
        if ((this.offset & 0x00000003) != 0L) {
            numErrors++;
            this.isValid = Validity.False;
            Object [] args = new Object [] {input.getPosition()-4L, this.offset};
            this.offsetNotWordAlignedMessage = new Message(Severity.ERROR,
                    Context.OBJECT,
                    "org.jhove2.module.format.icc.ICCTAG.OffsetNotWordAligned",
                    args, jhove2.getConfigInfo());
        }
        consumed += 4;
        
        /* Tag size. */
        this.size = input.readUnsignedInt();
        consumed += 4;
        
        return consumed;
    }
    
    /** Get invalid tag message.
     * @return Invalid tag message
     */
    @ReportableProperty(order=21, value="Invalid tag.",
            ref="ICC, \"Private and ICC Tag and CMM Registry\" (as of November 3, 2009")
    public Message getInvalidTagMessage() {
        return this.invalidTagMessage;
    }
    
    /** Get tag offset.
     * @return Tag offset
     */
    @ReportableProperty(order=4, value="Tag offset.",
            ref="ICC.1:2004-10, \u00a7 7.3.1")
    public long getOffset() {
        return this.offset;
    }
    
    /** Get tag offset not word aligned message.
     * @return Tag offset not word aligned message
     */
    @ReportableProperty(order=22, value="Offset not word aligned.",
            ref="ICC.1:2004-10, \u00a7 7.3.4")
    public Message getOffsetNotWordAligned() {
        return this.offsetNotWordAlignedMessage;
    }
    
    /** Get tag signature in coded form.
     * @return Tag signature in coded form
     */
    @ReportableProperty(order=2, value="Tag signature in raw form.",
            ref="ICC.1:2004-10, \u00a7 7.3.1", type=PropertyType.Raw)
    public String getSignature_raw() {
        return this.signature.toString();
    }
    
    /** Get tag signature in descriptive form.
     * @return Tag signature in descriptive form
     */
    @ReportableProperty(order=3, value="Tag signature in descriptive form.",
            ref="ICC.1:2004-10, \u00a7 9", type=PropertyType.Descriptive)
    public String getSignature_descriptive() {
        return this.signature_d;
    }
    
    /** Get tag size.
     * @return Tag size
     */
    @ReportableProperty(order=5, value="Tag size.", ref="ICC.1:2004-10, \u00a7 7.3.1")
    public long getSize() {
        return this.size;
    }
    
    /** Get tag vendor.
     * @return Tag vendor
     */
    @ReportableProperty(order=1, value="Tag vendor.",
            ref="ICC, \"Private and ICC Tag and CMM Regsitry\" (as of November 3, 2009")
    public String getVendor() {
        return this.vendor;
    }
    
    /** Get tag validity.
     * @return Tag validity
     */
    @ReportableProperty(order=5, value="Tag validity.")
    public Validity isValid() {
        return this.isValid;
    }
}
