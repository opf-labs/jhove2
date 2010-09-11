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
import org.jhove2.module.format.Validator.Validity;
import org.jhove2.module.format.icc.field.PerceptualRenderingIntent;
import org.jhove2.module.format.icc.field.SaturationRenderingIntent;
import org.jhove2.module.format.icc.field.Tag;
import org.jhove2.module.format.icc.field.TechnologySignature;
import org.jhove2.module.format.icc.type.ChromaticityType;
import org.jhove2.module.format.icc.type.ColorantOrderType;
import org.jhove2.module.format.icc.type.ColorantTableType;
import org.jhove2.module.format.icc.type.CurveType;
import org.jhove2.module.format.icc.type.DateTimeType;
import org.jhove2.module.format.icc.type.DescriptionType;
import org.jhove2.module.format.icc.type.LUT16Type;
import org.jhove2.module.format.icc.type.LUT8Type;
import org.jhove2.module.format.icc.type.LUTAToBType;
import org.jhove2.module.format.icc.type.LUTBToAType;
import org.jhove2.module.format.icc.type.MeasurementType;
import org.jhove2.module.format.icc.type.MultiLocalizedUnicodeType;
import org.jhove2.module.format.icc.type.NamedColor2Type;
import org.jhove2.module.format.icc.type.ParametricCurveType;
import org.jhove2.module.format.icc.type.ProfileSequenceDescriptionType;
import org.jhove2.module.format.icc.type.ResponseCurveSet16Type;
import org.jhove2.module.format.icc.type.S15Fixed16ArrayType;
import org.jhove2.module.format.icc.type.SignatureType;
import org.jhove2.module.format.icc.type.TextType;
import org.jhove2.module.format.icc.type.ViewingConditionsType;
import org.jhove2.module.format.icc.type.XYZType;

/** ICC tag.  See ICC.1:2004-10, \u00a7 7.3.1.
 * 
 * @author slabrams
 */
public class ICCTag
    extends AbstractReportable
{
    /** Chromoticity type element. */
    protected ChromaticityType chromaticityType;
    
    /** Colorant order type element. */
    protected ColorantOrderType colorantOrderType;
    
    /** Color table type element. */
    protected ColorantTableType colorantTableType;
    
    /** Curve type element. */
    protected CurveType curveType;
    
    /** Date/time type element. */
    protected DateTimeType dateTimeType;
    
    /** Text description type element. */
    protected DescriptionType descriptionType;
    
    /** Tag validity. */
    protected Validity isValid;
    
    /** 16-bit LUT type element. */
    protected LUT16Type lut16Type;
    
    /** 8-bit LUT type element. */
    protected LUT8Type lut8Type;
    
    /** LUT A-to-B type element. */
    protected LUTAToBType lutA2BType;
    
    /** LUT B-to-A type element. */
    protected LUTBToAType lutB2AType;
    
    /** Measurement type element. */
    protected MeasurementType measurementType;
    
    /** Named color 2 type element. */
    protected NamedColor2Type color2Type;
    
    /** Tag offset. */
    protected long offset;
    
    /** Profile sequence description type. */
    protected ProfileSequenceDescriptionType sequenceType;
    
    /** Response curve set 16 type. */
    protected ResponseCurveSet16Type rcs16Type;
    
    /** Tag signature in raw form. */
    protected StringBuffer signature = new StringBuffer(4);
    
    /** Tag signature in descriptive form. */
    protected String signature_d;
    
    /** Signature type element. */
    protected SignatureType signatureType;
    
    /** Signed 32-bit fixed number array type element. */
    protected S15Fixed16ArrayType s15f16Type;
    
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
    
    /** Viewing conditions type element. */
    protected ViewingConditionsType conditionsType;
    
    /** XYZ tristimulus value array type element. */
    protected XYZType xyzType;
    
    /** Incorrect tag type message. */
    protected Message incorrectTagTypeMessage;
    
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
     * @param source ICC source
     * @return Number of bytes consumed
     * @throws EOFException
     *             If End-of-File is reached reading the source unit
     * @throws IOException
     *             If an I/O exception is raised reading the source unit
     * @throws JHOVE2Exception
     */
    public long parse(JHOVE2 jhove2, Source source, long offset)
        throws EOFException, IOException, JHOVE2Exception
    {
        long consumed = 0L;
        int numErrors = 0;
        this.isValid = Validity.True;
        Input input  = source.getInput(jhove2, ByteOrder.BIG_ENDIAN);
        long  start  = source.getStartingOffset();

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
            Object [] args = new Object [] {input.getPosition()-4L-start, signature.toString()};
            this.unknownTagMessage = new Message(Severity.ERROR,
                Context.OBJECT,
                "org.jhove2.module.format.icc.ICCTag.UnknownTag",
                args, jhove2.getConfigInfo());
        }
        consumed += 4;
        
        /* Tag offset + the offset of the start of the input. */
        this.offset = input.readUnsignedInt() + offset;
        if (((this.offset-start) & 0x00000003) != 0L) {
            numErrors++;
            this.isValid = Validity.False;
            Object [] args = new Object [] {input.getPosition()-4L-start, this.offset};
            this.offsetNotWordAlignedMessage = new Message(Severity.ERROR,
                    Context.OBJECT,
                    "org.jhove2.module.format.icc.ICCTAG.OffsetNotWordAligned",
                    args, jhove2.getConfigInfo());
        }
        consumed += 4;
        
        /* Tag size. */
        this.size = input.readUnsignedInt();
        consumed += 4;
        
        /* Parse the tag type, first looking up the tag element type and
         * remembering to save and restore the current position in the ICC
         * input.
         */
        long position = input.getPosition();
        input.setPosition(this.offset);
        
        StringBuffer sb = new StringBuffer(4);
        for (int i=0; i<4; i++) {
            short b = input.readUnsignedByte();
            sb.append((char) b);
        }
        input.setPosition(this.offset);
        String sig = sb.toString();
        
        if (signature.equals("A2B0") ||
            signature.equals("A2B1") ||
            signature.equals("A2B2")) {
            Validity isValid = Validity.Undetermined;
            if (sig.equals("mAB ")) {
                this.lutA2BType = new LUTAToBType();
                this.lutA2BType.parse(jhove2, source);
                
                isValid = this.lutA2BType.isValid();
            }
            else if (sig.equals("mft1")) {
                this.lut8Type = new LUT8Type();
                this.lut8Type.parse(jhove2, source);
                
                isValid = this.lut8Type.isValid();
            }
            else if (sig.equals("mft2")) {
                this.lut16Type = new LUT16Type();
                this.lut16Type.parse(jhove2, source);
                
                isValid = this.lut16Type.isValid();
            }
            else {
                numErrors++;
                isValid = Validity.False;
                Object [] args = new Object [] {this.offset-start, sig};
                this.incorrectTagTypeMessage = new Message(Severity.ERROR,
                        Context.OBJECT,
                        "org.jhove2.module.format.icc.ICCTag.incorrectTagType",
                        args, jhove2.getConfigInfo());
            }
            if (isValid != Validity.True) {
                this.isValid = isValid;
            }            
        }
        else if (signature.equals("B2A0") ||
                 signature.equals("B2A1") ||
                 signature.equals("B2A2") ||
                 signature.equals("gamt") ||
                 signature.equals("pre0") ||
                 signature.equals("pre1") ||
                 signature.equals("pre2")) {
            Validity isValid = Validity.Undetermined;
            if (sig.equals("mBA ")) {
                this.lutB2AType = new LUTBToAType();
                this.lutB2AType.parse(jhove2, source);
                    
                isValid = this.lutB2AType.isValid();
            }
            else if (sig.equals("mft1")) {
                this.lut8Type = new LUT8Type();
                this.lut8Type.parse(jhove2, source);
                    
                isValid = this.lut8Type.isValid();
            }
            else if (sig.equals("mft2")) {
                
            }
            else {
                numErrors++;
                isValid = Validity.False;
                Object [] args = new Object [] {this.offset-start, sig};
                this.incorrectTagTypeMessage = new Message(Severity.ERROR,
                        Context.OBJECT,
                        "org.jhove2.module.format.icc.ICCTag.incorrectTagType",
                        args, jhove2.getConfigInfo());
            }
            if (isValid != Validity.True) {
                this.isValid = isValid;
            }            
        }
        else if (signature.equals("bkpt") ||
                 signature.equals("bXYZ") ||
                 signature.equals("gXYZ") ||
                 signature.equals("rXYZ") ||
                 signature.equals("wtpt")) {
            Validity isValid = Validity.Undetermined;
            if (sig.equals("XYZ ")) {
                this.xyzType = new XYZType();
                this.xyzType.parse(jhove2, source, this.size);
            
                isValid = this.xyzType.isValid();
            }
            else {
                numErrors++;
                isValid = Validity.False;
                Object [] args = new Object [] {this.offset-start, sig};
                this.incorrectTagTypeMessage = new Message(Severity.ERROR,
                        Context.OBJECT,
                        "org.jhove2.module.format.icc.ICCTag.incorrectTagType",
                        args, jhove2.getConfigInfo());
            }
            if (isValid != Validity.True) {
                this.isValid = isValid;
            }
        }
        else if (signature.equals("bTRC") ||
                 signature.equals("gTRC") ||
                 signature.equals("kTRC") ||
                 signature.equals("rTRC")) {
            Validity isValid = Validity.Undetermined;
            if (sig.equals("curv")) {
                this.curveType = new CurveType();
                this.curveType.parse(jhove2, source);
                
                isValid = this.curveType.isValid();
            }
            else if (sig.equals("para")){
                this.parametricType = new ParametricCurveType();
                this.parametricType.parse(jhove2, source);
                
                isValid = this.parametricType.isValid();
            }
            else {
                numErrors++;
                isValid = Validity.False;
                Object [] args = new Object [] {this.offset-start, sig};
                this.incorrectTagTypeMessage = new Message(Severity.ERROR,
                        Context.OBJECT,
                        "org.jhove2.module.format.icc.ICCTag.incorrectTagType",
                        args, jhove2.getConfigInfo());
            }
            if (isValid != Validity.True) {
                this.isValid = isValid;
            }
        }
        else if (signature.equals("calt")) {
            Validity isValid = Validity.Undetermined;
            if (sig.equals("dtim")) {
                this.dateTimeType = new DateTimeType();
                this.dateTimeType.parse(jhove2, source);
            
                isValid = this.dateTimeType.isValid();
            }
            else {
                numErrors++;
                isValid = Validity.False;
                Object [] args = new Object [] {this.offset-start, sig};
                this.incorrectTagTypeMessage = new Message(Severity.ERROR,
                        Context.OBJECT,
                        "org.jhove2.module.format.icc.ICCTag.incorrectTagType",
                        args, jhove2.getConfigInfo());
            }
            if (isValid != Validity.True) {
                this.isValid = isValid;
            }
        }
        else if (signature.equals("chad")) {
            Validity isValid = Validity.Undetermined;
            if (sig.equals("sf32")) {
                this.s15f16Type = new S15Fixed16ArrayType();
                this.s15f16Type.parse(jhove2, source, this.size);
            
                isValid = this.s15f16Type.isValid();
            }
            else {
                numErrors++;
                isValid = Validity.False;
                Object [] args = new Object [] {this.offset-start, sig};
                this.incorrectTagTypeMessage = new Message(Severity.ERROR,
                        Context.OBJECT,
                        "org.jhove2.module.format.icc.ICCTag.incorrectTagType",
                        args, jhove2.getConfigInfo());
            }
            if (isValid != Validity.True) {
                this.isValid = isValid;
            }
        }
        else if (signature.equals("chrm")) {
            Validity isValid = Validity.Undetermined;
            if (sig.equals("chrm")) {
                this.chromaticityType = new ChromaticityType();
                this.chromaticityType.parse(jhove2, source);
            
                isValid = this.chromaticityType.isValid();
            }
            else {
                numErrors++;
                isValid = Validity.False;
                Object [] args = new Object [] {this.offset-start, sig};
                this.incorrectTagTypeMessage = new Message(Severity.ERROR,
                        Context.OBJECT,
                        "org.jhove2.module.format.icc.ICCTag.incorrectTagType",
                        args, jhove2.getConfigInfo());
            }
            if (isValid != Validity.True) {
                this.isValid = isValid;
            }
        }
        else if (signature.equals("clro")) {
            Validity isValid = Validity.Undetermined;
            if (sig.equals("clro")) {
                this.colorantOrderType = new ColorantOrderType();
                this.colorantOrderType.parse(jhove2, source);
            
                isValid = this.colorantOrderType.isValid();
            }
            else {
                numErrors++;
                isValid = Validity.False;
                Object [] args = new Object [] {this.offset-start, sig};
                this.incorrectTagTypeMessage = new Message(Severity.ERROR,
                        Context.OBJECT,
                        "org.jhove2.module.format.icc.ICCTag.incorrectTagType",
                        args, jhove2.getConfigInfo());
            }
            if (isValid != Validity.True) {
                this.isValid = isValid;
            }
        }
        else if (signature.equals("clrt")) {
            Validity isValid = Validity.Undetermined;
            if (sig.equals("clrt")) {
                this.colorantTableType = new ColorantTableType();
                this.colorantTableType.parse(jhove2, source);
            
                isValid = this.colorantTableType.isValid();
            }
            else {
                numErrors++;
                isValid = Validity.False;
                Object [] args = new Object [] {this.offset-start, sig};
                this.incorrectTagTypeMessage = new Message(Severity.ERROR,
                        Context.OBJECT,
                        "org.jhove2.module.format.icc.ICCTag.incorrectTagType",
                        args, jhove2.getConfigInfo());
            }
            if (isValid != Validity.True) {
                this.isValid = isValid;
            }
        }
        else if (signature.equals("cprt") ||
                 signature.equals("desc") ||
                 signature.equals("dmdd") ||
                 signature.equals("dmnd") ||
                 signature.equals("targ") ||
                 signature.equals("vued")) {
            Validity isValid = Validity.Undetermined;
            if (sig.equals("desc")) {
                this.descriptionType = new DescriptionType();
                this.descriptionType.parse(jhove2, source);
                
                isValid = this.descriptionType.isValid();
            }
            else if (sig.equals("text")) {
                this.textType = new TextType();
                this.textType.parse(jhove2, source, this.size);
                
                isValid = this.textType.isValid();
            }
            else if (sig.equals("mluc")){
                this.unicodeType = new MultiLocalizedUnicodeType();
                this.unicodeType.parse(jhove2, source);
            
                isValid = this.unicodeType.isValid();
            }
            else {
                numErrors++;
                isValid = Validity.False;
                Object [] args = new Object [] {this.offset-start, sig};
                this.incorrectTagTypeMessage = new Message(Severity.ERROR,
                        Context.OBJECT,
                        "org.jhove2.module.format.icc.ICCTag.incorrectTagType",
                        args, jhove2.getConfigInfo());
            }
            if (isValid != Validity.True) {
                this.isValid = isValid;
            }
        }
        else if (signature.equals("meas")) {
            Validity isValid = Validity.Undetermined;
            if (sig.equals("meas")) {
                this.measurementType = new MeasurementType();
                this.measurementType.parse(jhove2, source);
            
                isValid = this.measurementType.isValid();
            }
            else {
                numErrors++;
                isValid = Validity.False;
                Object [] args = new Object [] {this.offset-start, sig};
                this.incorrectTagTypeMessage = new Message(Severity.ERROR,
                        Context.OBJECT,
                        "org.jhove2.module.format.icc.ICCTag.incorrectTagType",
                        args, jhove2.getConfigInfo());
            }
            if (isValid != Validity.True) {
                this.isValid = isValid;
            }
        }
        else if (signature.equals("ncl2")) {
            Validity isValid = Validity.Undetermined;
            if (sig.equals("ncl2")) {
                this.color2Type = new NamedColor2Type();
                this.color2Type.parse(jhove2, source);
            
                isValid = this.color2Type.isValid();
            }
            else {
                numErrors++;
                isValid = Validity.False;
                Object [] args = new Object [] {this.offset-start, sig};
                this.incorrectTagTypeMessage = new Message(Severity.ERROR,
                        Context.OBJECT,
                        "org.jhove2.module.format.icc.ICCTag.incorrectTagType",
                        args, jhove2.getConfigInfo());
            }
            if (isValid != Validity.True) {
                this.isValid = isValid;
            }
        }
        else if (signature.equals("pseq")) {
            Validity isValid = Validity.Undetermined;
            if (sig.equals("pseq")) {
                this.sequenceType = new ProfileSequenceDescriptionType();
                this.sequenceType.parse(jhove2, source);
            
                isValid = this.sequenceType.isValid();
            }
            else {
                numErrors++;
                isValid = Validity.False;
                Object [] args = new Object [] {this.offset-start, sig};
                this.incorrectTagTypeMessage = new Message(Severity.ERROR,
                        Context.OBJECT,
                        "org.jhove2.module.format.icc.ICCTag.incorrectTagType",
                        args, jhove2.getConfigInfo());
            }
            if (isValid != Validity.True) {
                this.isValid = isValid;
            }
        }
        else if (signature.equals("resp")) {
            Validity isValid = Validity.Undetermined;
            if (sig.equals("rcs2")) {
                this.rcs16Type = new ResponseCurveSet16Type();
                this.rcs16Type.parse(jhove2, source);
            
                isValid = this.rcs16Type.isValid();
            }
            else {
                numErrors++;
                isValid = Validity.False;
                Object [] args = new Object [] {this.offset-start, sig};
                this.incorrectTagTypeMessage = new Message(Severity.ERROR,
                        Context.OBJECT,
                        "org.jhove2.module.format.icc.ICCTag.incorrectTagType",
                        args, jhove2.getConfigInfo());
            }
            if (isValid != Validity.True) {
                this.isValid = isValid;
            }
        }
        else if (signature.equals("rig0") ||
                 signature.equals("rig2") ||
                 signature.equals("tech")) {
            Validity isValid = Validity.Undetermined;
            if (sig.equals("sig ")) {
                this.signatureType = new SignatureType();
                this.signatureType.parse(jhove2, source);
            
                isValid = this.signatureType.isValid();
            }
            else {
                numErrors++;
                isValid = Validity.False;
                Object [] args = new Object [] {this.offset-start, sig};
                this.incorrectTagTypeMessage = new Message(Severity.ERROR,
                        Context.OBJECT,
                        "org.jhove2.module.format.icc.ICCTag.incorrectTagType",
                        args, jhove2.getConfigInfo());
            }
            if (isValid != Validity.True) {
                this.isValid = isValid;
            }
            
            String content = this.signatureType.getContentSignature_raw();
            if (signature.equals("rig0")) {
                PerceptualRenderingIntent gamut =
                    PerceptualRenderingIntent.getPerceptualRenderingIntent(content, jhove2); 
                if (gamut != null) {
                    this.signatureType.setContentSignature_description(gamut.getGamut());
                }
            }
            else if (signature.equals("rig1")) {
                SaturationRenderingIntent gamut =
                    SaturationRenderingIntent.getSaturationRenderingIntent(content, jhove2); 
                if (gamut != null) {
                    this.signatureType.setContentSignature_description(gamut.getGamut());
                }
            }
            else if (signature.equals("tech")) {
                TechnologySignature technology =
                    TechnologySignature.getTechnology(content, jhove2); 
                if (technology != null) {
                    this.signatureType.setContentSignature_description(technology.getTechnology());
                }
            }
            if (this.signatureType.getContentSignature_description() == null) {
                numErrors++;
                this.isValid = Validity.False;
                Object [] args = new Object[] {input.getPosition()-4L-start,
                                               content.toString()};
                this.invalidTechnologySignatureMessage = new Message(Severity.ERROR,
                        Context.OBJECT,
                        "org.jhove2.module.format.icc.ICCTag.invalidTechnologySignature",
                        args, jhove2.getConfigInfo());
            }
        }
        else if (signature.equals("view")) {
            Validity isValid = Validity.Undetermined;
            if (sig.equals("view")) {
                this.conditionsType = new ViewingConditionsType();
                this.conditionsType.parse(jhove2, source);
            
                isValid = this.conditionsType.isValid();
            }
            else {
                numErrors++;
                isValid = Validity.False;
                Object [] args = new Object [] {this.offset-start, sig};
                this.incorrectTagTypeMessage = new Message(Severity.ERROR,
                        Context.OBJECT,
                        "org.jhove2.module.format.icc.ICCTag.incorrectTagType",
                        args, jhove2.getConfigInfo());
            }
            if (isValid != Validity.True) {
                this.isValid = isValid;
            }
        }
        input.setPosition(position);

        return consumed;
    }
    
    /** Get chromoticity type element.
     * @return Chromoticity  type element
     */
    @ReportableProperty(order=7, value="Chromoticity type element.",
            ref="ICC.1:2001-04, \u00a7 10.2")
    public ChromaticityType getChromoticityType() {
        return this.chromaticityType;
    }
    
    /** Get colorant order type element.
     * @return Colorant order type element
     */
    @ReportableProperty(order=7, value="Colorant order type element.",
            ref="ICC.1:2001-04, \u00a7 10.3")
    public ColorantOrderType getColorantOrderType() {
        return this.colorantOrderType;
    }
    
    /** Get colorant table type element.
     * @return Colorant table type element
     */
    @ReportableProperty(order=7, value="Colorant table type element.",
            ref="ICC.1:2001-04, \u00a7 10.4")
    public ColorantTableType getColorantTableType() {
        return this.colorantTableType;
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
    
    /** Get unknown tag message.
     * @return Unknown tag message
     */
    @ReportableProperty(order=23, value="Incorrect tag type.",
            ref="ICC.1:200-10, \u00a7 9.2")
    public Message getIncorrectTagTypeMessage() {
        return this.incorrectTagTypeMessage;
    }
       
    /** Get invalid technology signature message.
     * @return Invalid technology signature message
     */
    @ReportableProperty(order=24, value="Invalid technology signature message.",
            ref="ICC.1:2004-10, Table 22")
    public Message getInvalidTechnologySignature() {
        return this.invalidTechnologySignatureMessage;
    }
    
    /** Get measurement type element.
     * @return Measurement type element
     */
    @ReportableProperty(order=7, value="Measurement type element.",
            ref="ICC.1:2004-10, \u00a7 10.12")
    public MeasurementType getMesaurementType() {
        return this.measurementType;
    }
    
    /** Get multi-localized Unicode type element.
     * @return Multi-localized Unicode type element
     */
    @ReportableProperty(order=7, value="Multi-localized Unicode string type.",
            ref="ICC.1:2004-10, \u00a7 10.13")
    public MultiLocalizedUnicodeType getMultiLocalizedUnicodeType() {
        return this.unicodeType;
    }
    
    /** Get LUT A-to-B type element.
     * @return LUT A-to-B type element
     */
    @ReportableProperty(order=7, value="LUT A-to-B type element.",
            ref="ICC.1:2004-10, \u00a7 10.10")
    public LUTAToBType getLUTAToBType() {
        return this.lutA2BType;
    }
    
    /** Get LUT B-to-A type element.
     * @return LUT B-to-A type element
     */
    @ReportableProperty(order=7, value="LUT B-to-A type element.",
            ref="ICC.1:2004-10, \u00a7 10.11")
    public LUTBToAType getLUTBToAType() {
        return this.lutB2AType;
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
    
    /** Get parametric curve type element.
     * @return Parametric curve type element
     */
    @ReportableProperty(order=7, value="Parametric curve type element.",
            ref="ICC.1:2001-04, \u00a7 10.51")
    public ParametricCurveType getParametricCurveType() {
        return this.parametricType;
    }
    
    /** Get profile sequence description type element.
     * @return Profile sequence description type element
     */
    @ReportableProperty(order=7, value="Profile sequence description type element.",
            ref="ICC.1:2004-10, \u00a7 10.16")
    public ProfileSequenceDescriptionType getProfileSequenceDescriptionType() {
        return this.sequenceType;
    }
    
    /** Get response curve set 16 type element.
     * @return Response curve set 16 type element
     */
    @ReportableProperty(order=7, value="Response curve set 16 type element.",
            ref="ICC.1:2004-10, \u00a7 10.16")
    public ResponseCurveSet16Type getResponseCurveSet16Type() {
        return this.rcs16Type;
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
    
    /** Get signed 32-bit fixed array type element.
     * @return Curve type element
     */
    @ReportableProperty(order=7, value="Signed 32-bit fixed array type element.",
            ref="ICC.1:2001-04, \u00a7 10.18")
    public S15Fixed16ArrayType getSigned32BitFixedArrayType() {
        return this.s15f16Type;
    }
    
    /** Get signature type element.
     * @return Signature type element
     */
    @ReportableProperty(order=7, value="Signature type element.",
            ref="ICC.1:2004-10, \u00a7 10.19")
    public SignatureType getSignatureType() {
        return this.signatureType;
    }
    
    /** Get tag size.
     * @return Tag size
     */
    @ReportableProperty(order=5, value="Tag size.",
            ref="ICC.1:2004-10, \u00a7 7.3.1")
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
    @ReportableProperty(order=21, value="Unknown tag.",
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
    
    /** Get viewing conditions type element.
     * @return Viewing conditions type element
     */
    @ReportableProperty(order=7, value="Viewing conditions type element.",
            ref="ICC.1:2004-10, \u00a7 10.26")
    public ViewingConditionsType getViewingConditionsType() {
        return this.conditionsType;
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
