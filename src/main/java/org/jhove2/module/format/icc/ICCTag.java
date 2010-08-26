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
import org.jhove2.module.format.icc.field.TechnologySignature;
import org.jhove2.module.format.icc.type.CurveType;
import org.jhove2.module.format.icc.type.DateTimeType;
import org.jhove2.module.format.icc.type.DescriptionType;
import org.jhove2.module.format.icc.type.MultiLocalizedUnicodeType;
import org.jhove2.module.format.icc.type.ParametricCurveType;
import org.jhove2.module.format.icc.type.SignatureType;
import org.jhove2.module.format.icc.type.TextType;
import org.jhove2.module.format.icc.type.XYZType;

/** ICC tag.  See ICC.1:2004-10, \u00a7 7.3.1.
 * 
 * @author slabrams
 */
public class ICCTag
    extends AbstractReportable
{
    /** Curve type element. */
    protected CurveType curveType;
    
    /** Date/time type element. */
    protected DateTimeType dateTimeType;
    
    /** Text description type element. */
    protected DescriptionType descriptionType;
    
    /** Tag validity. */
    protected Validity isValid;
    
    /** Tag offset. */
    protected long offset;
    
    /** Tag signature in raw form. */
    protected StringBuffer signature = new StringBuffer(4);
    
    /** Tag signature in descriptive form. */
    protected String signature_d;
    
    /** Signature type element. */
    protected SignatureType signatureType;
    
    /** Tag size. */
    protected long size;
    
    /** Tag vendor. */
    protected String vendor;
    
    /** Non-ICC tag message. */
    protected Message nonICCTagMessage;
    
    /** Parametric curve element. */
    protected ParametricCurveType parametricType;
    
    /** Text type element. */
    protected TextType textType;
    
    /** Multi-localized Unicode type element. */
    protected MultiLocalizedUnicodeType unicodeType;
    
    /** XYZ tristimulus value array type element. */
    protected XYZType xyzType;
    
    /** Invalid technology signature message. */
    protected Message invalidTechnologySignatureMessage;
    
    /** Offset not word aligned message. */
    protected Message offsetNotWordAlignedMessage;

    /** Unknown tag message. */
    protected Message unknownTagMessage;
    
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
        String signature = this.signature.toString();
        Tag tag = Tag.getTag(signature, jhove2);
        if (tag != null) {
            this.signature_d = tag.getName();
            this.vendor      = tag.getVendor();
        }
        else {
            numErrors++;
            this.isValid = Validity.False;
            Object [] args = new Object [] {input.getPosition()-4L, signature.toString()};
            this.unknownTagMessage = new Message(Severity.ERROR,
                Context.OBJECT,
                "org.jhove2.module.format.icc.ICCTag.UnknownTag",
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
        
        /* Parse the tag type, remembering to save and restore the current
         * position in the ICC input.
         */
        long position = input.getPosition();
        input.setPosition(this.offset);
        if (signature.equals("bkpt") ||
            signature.equals("bXYZ") ||
            signature.equals("gXYZ") ||
            signature.equals("rXYZ") ||
            signature.equals("wtpt")) {
            this.xyzType = new XYZType();
            this.xyzType.parse(jhove2, input, this.size);
            
            Validity isValid = this.xyzType.isValid();
            if (isValid != Validity.True) {
                this.isValid = isValid;
            }
        }
        else if (signature.equals("bTRC") ||
                 signature.equals("gTRC") ||
                 signature.equals("kTRC") ||
                 signature.equals("rTRC")) {
            /* Pre-parse the type element to see if it is a curve ("curv")
             * or a parametric curve ("para") type, resetting the position to
             * the original.
             */
            short b = input.readUnsignedByte();
            input.setPosition(this.offset);
            Validity isValid = Validity.Undetermined;
            if (((char) b) == 'c') {
                this.curveType = new CurveType();
                this.curveType.parse(jhove2, input);
                
                isValid = this.curveType.isValid();
            }
            else {
                this.parametricType = new ParametricCurveType();
                this.parametricType.parse(jhove2, input);
                
                isValid = this.parametricType.isValid();
            }
            if (isValid != Validity.True) {
                this.isValid = isValid;
            }
        }
        else if (signature.equals("calt")) {
            this.dateTimeType = new DateTimeType();
            this.dateTimeType.parse(jhove2, input);
            
            Validity isValid = this.dateTimeType.isValid();
            if (isValid != Validity.True) {
                this.isValid = isValid;
            }
        }
        else if (signature.equals("cprt") ||
                 signature.equals("desc") ||
                 signature.equals("dmdd") ||
                 signature.equals("dmnd") ||
                 signature.equals("vued")) {
            /* Pre-parse the type element to see if it is a text type ("desc")
             * or a multi-localized Unicode type ("mlut"), resetting the
             * position to the original.
             */
            short b = input.readUnsignedByte();
            input.setPosition(this.offset);
            Validity isValid = Validity.Undetermined;
            if (((char) b) == 'd') {
                this.descriptionType = new DescriptionType();
                this.descriptionType.parse(jhove2, input);
                
                isValid = this.descriptionType.isValid();
            }
            else if (((char) b) == 't') {
                this.textType = new TextType();
                this.textType.parse(jhove2, input, this.size);
                
                isValid = this.textType.isValid();
            }
            else {
                this.unicodeType = new MultiLocalizedUnicodeType();
                this.unicodeType.parse(jhove2, input);
            
                isValid = this.unicodeType.isValid();
            }
            if (isValid != Validity.True) {
                this.isValid = isValid;
            }
        }
        else if (signature.equals("targ")) {
            this.textType = new TextType();
            this.textType.parse(jhove2, input, this.size);
            
            Validity isValid = this.textType.isValid();
            if (isValid != Validity.True) {
                this.isValid = isValid;
            }
        }
        else if (signature.equals("tech")) {
            this.signatureType = new SignatureType();
            this.signatureType.parse(jhove2, input);
            
            Validity isValid = this.signatureType.isValid();
            if (isValid != Validity.True) {
                this.isValid = isValid;
            }
            
            String content = this.signatureType.getContentSignature_raw();
            TechnologySignature technology =
                TechnologySignature.getTechnology(content, jhove2); 
            if (technology != null) {
                this.signatureType.setContentSignature_description(technology.getTechnology());
            }
            else {
                numErrors++;
                this.isValid = Validity.False;
                Object [] args = new Object[] {input.getPosition()-4L,
                                               content.toString()};
                this.invalidTechnologySignatureMessage = new Message(Severity.ERROR,
                        Context.OBJECT,
                        "org.jhove2.module.format.icc.ICCTag.invalidTechnologySignature",
                        args, jhove2.getConfigInfo());
            }
        }
        input.setPosition(position);

        return consumed;
    }
    
    /** Get curve type element.
     * @return Curve type element
     */
    @ReportableProperty(order=7, value="Curve type element.",
            ref="ICC.1:2001-04, \u00a7 10.5")
    public CurveType getCurveType() {
        return this.curveType;
    }
    
    /** Get date/time type element.
     * @return Date/time type element
     */
    @ReportableProperty(order=7, value="Date/time type element.",
            ref="ICC.1:2001-04, \u00a7 10.7")
    public DateTimeType getDateTimeType() {
        return this.dateTimeType;
    }
    
    /** Get text description type element.
     * @return Text description type element
     */
    @ReportableProperty(order=7, value="Text description type element.",
            ref="ICC.1:2001-04, \u00a7 6.5.17")
    public DescriptionType getDescriptionType() {
        return this.descriptionType;
    }
    
    /** Get invalid technology signature message.
     * @return Invalid technology signature message
     */
    @ReportableProperty(order=13, value="Invalid technology signature message.",
            ref="ICC.1:2004-10, Table 22")
    public Message getInvalidTechnologySignature() {
        return this.invalidTechnologySignatureMessage;
    }
    
    /** Get multi-localized Unicode type element.
     * @return Multi-localized Unicode type element
     */
    @ReportableProperty(order=7, value="Multi-localized Unicode string type.",
            ref="ICC.1:2004-10, \u00a7 10.13")
    public MultiLocalizedUnicodeType getMultiLocalizedUnicodeType() {
        return this.unicodeType;
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
    @ReportableProperty(order=12, value="Offset not word aligned.",
            ref="ICC.1:2004-10, \u00a7 7.3.4")
    public Message getOffsetNotWordAligned() {
        return this.offsetNotWordAlignedMessage;
    }
    
    /** Get parametric curve type element.
     * @return Parametric curve type element
     */
    @ReportableProperty(order=7, value="Parametric curve type element.",
            ref="ICC.1:2001-04, \u00a7 10.51")
    public ParametricCurveType getParametricCurveType() {
        return this.parametricType;
    }
    
    /** Get tag signature in coded form.
     * @return Tag signature in coded form
     */
    @ReportableProperty(order=1, value="Tag signature in raw form.",
            ref="ICC.1:2004-10, \u00a7 7.3.1", type=PropertyType.Raw)
    public String getSignature_raw() {
        return this.signature.toString();
    }
    
    /** Get tag signature in descriptive form.
     * @return Tag signature in descriptive form
     */
    @ReportableProperty(order=2, value="Tag signature in descriptive form.",
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
    
    /** Get text type element.
     * @return Text type element
     */
    @ReportableProperty(order=7, value="Text type element.",
            ref="ICC.1:2004-10, \u00a7 10.20")
    public TextType getTextType() {
        return this.textType;
    }
    
    /** Get unknown tag message.
     * @return Unknown tag message
     */
    @ReportableProperty(order=11, value="Unknown tag.",
            ref="ICC, \"Private and ICC Tag and CMM Registry\" (as of November 3, 2009")
    public Message getUnknownTagMessage() {
        return this.unknownTagMessage;
    }
       
    /** Get tag vendor.
     * @return Tag vendor
     */
    @ReportableProperty(order=6, value="Tag vendor.",
            ref="ICC, \"Private and ICC Tag and CMM Regsitry\" (as of November 3, 2009")
    public String getVendor() {
        return this.vendor;
    }
    
    /** Get XYZ type element.
     * @return XYZ type element
     */
    @ReportableProperty(order=7, value="XYZ type element.",
            ref="ICC.1:2004-10, \u00a7 10.27")
    public XYZType getXYZType() {
        return this.xyzType;
    }
    
    /** Get tag validity.
     * @return Tag validity
     */
    @ReportableProperty(order=8, value="Tag validity.")
    public Validity isValid() {
        return this.isValid;
    }
}
