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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
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
import org.jhove2.module.format.icc.field.ColourSpace;
import org.jhove2.module.format.icc.field.DeviceAttribute;
import org.jhove2.module.format.icc.field.PreferredCMM;
import org.jhove2.module.format.icc.field.PrimaryPlatform;
import org.jhove2.module.format.icc.field.ProfileDeviceClass;
import org.jhove2.module.format.icc.field.ProfileFlag;
import org.jhove2.module.format.icc.field.RenderingIntent;
import org.jhove2.module.format.icc.type.XYZNumber;

/** An ICC profile header.  See ICC.1:2004-10, \u00a7 7.2.
 * 
 * @author slabrams
 */
public class ICCHeader
        extends AbstractReportable
{
    /** D50 illuminant CIE XYZ values. These hexadecmial values are equivalent
     * to 0.9642, 1.0, 0.8249. */
    public static final int [] D50 = {0x0000f6d6, 0x00010000, 0x0000d32d};
    
    /** Colour space in coded form. */
    protected StringBuffer colourSpace = new StringBuffer(4);
    
    /** Colour space in symbolic form. */
    protected String colourSpace_s;
    
    /** Creation date/time in UTC. */
    protected Date dateAndTime;
    
    /** Device attributes field in coded form. */
    protected long deviceAttributesField;
    
    /** Device attributes in symbolic form. */
    protected List<String> deviceAttributes = new ArrayList<String>();
    
    /** Device manufacturer. */
    protected StringBuffer deviceManufacturer = new StringBuffer(4);
    
    /** Device model. */
    protected StringBuffer deviceModel = new StringBuffer(4);
    
    /** Profile Connection Space (PCS) illuminant. */
    protected XYZNumber illuminant;
  
    /** Header validity status. */
    protected Validity isValid;
    
    /** Preferred CMM type in coded form. */
    protected StringBuffer preferredCMM = new StringBuffer(4);
    
    /** Preferred CMM type in symbolic form. */
    protected String preferredCMM_s;
    
    /** Primary platform in coded form. */
    protected StringBuffer primaryPlatform = new StringBuffer(4);
    
    /** Primary platform in symbolic form. */
    protected String primaryPlatform_s;
    
    /** Profile Connection Space (PCS) in coded form. */
    protected StringBuffer profileConnectionSpace = new StringBuffer(4);
    
    /** Profile Connection Space (PCS) in symbolic form. */
    protected String profileConnectionSpace_s;
    
    /** Profile creator . */
    protected StringBuffer profileCreator = new StringBuffer(4);
    
    /** Profile/device class in coded form. */
    protected StringBuffer profileDeviceClass = new StringBuffer(4);
    
    /** Profile/device class in symbolic form. */
    protected String profileDeviceClass_s;
    
    /** Profile file signature. */
    protected StringBuffer profileFileSignature = new StringBuffer(4);
    
    /** Profile flags field in coded form. */
    protected long profileFlagsField;
    
    /** Profile flags in symbolic form. */
    protected List<String> profileFlags = new ArrayList<String>();
    
    /** Profile ID (MD5). */
    protected StringBuffer profileID = new StringBuffer(32);
    
    /** Profile size. */
    protected long profileSize;
    
    /** Profile version number. */
    protected StringBuffer profileVersionNumber = new StringBuffer(4);
    
    /** Rendering intent in coded form. */
    protected int renderingIntent;
    
    /** Rendering intent in symbolic form. */
    protected String renderingIntent_s;

    /** Invalid data colour space message. */
    protected Message invalidColourSpaceMessage;
    
    /** Invalid device attributes message. */
    protected Message invalidDeviceAttributesMessage;
    
    /** Invalid device manufacturer message. */
    protected Message invalidDeviceManufacturerMessage;
    
    /** Invalid device model message. */
    protected Message invalidDeviceModelMessage;
    
    /** Invalid preferred CMM type message. */
    protected Message invalidPreferredCMMMessage;
    
    /** Invalid primary platform message. */
    protected Message invalidPrimaryPlatformMessage;
  
    /** Invalid Profile Connection Space message. */
    protected Message invalidProfileConnectionSpaceMessage;
    
    /** Invalid Profile Connection Space for non-DeviceLink profile message. */
    protected Message invalidProfileConnectionSpaceForNonDeviceLinkProfileMessage;
    
    /** Invalid profile creator message. */
    protected Message invalidProfileCreatorMessage;
    
    /** Invalid profile/device class message. */
    protected Message invalidProfileDeviceClassMessage;
    
    /** Invalid profile file signature message. */
    protected Message invalidProfileFileSignatureMessage;
    
    /** Invalid profile flags message. */
    protected Message invalidProfileFlagsMessage;
    
    /** Invalid profile ID message. */
    protected Message invalidProfileIDMessage;
    
    /** Invalid rendering intent message. */
    protected Message invalidRenderingIntentMessage;
   
    /** Non-zero data in reserved block error message. */
    protected List<Message> nonZeroDataInReservedBlockMessages;
    
    /** Non-zero high-order rendering intent message. */
    protected Message nonZeroHighOrderRenderingIntentMessage;
    
    /** Profile Connection Space illuminant not D50 message. */
    protected Message pcsIlluminantNotD50Message;
  
    /** Instantiate a new <code>ICCHeader</code>
     */
    public ICCHeader()
    {
        super();
        
        this.isValid = Validity.Undetermined;
        this.nonZeroDataInReservedBlockMessages = new ArrayList<Message>();
    }
    
    /** 
     * Parse an ICC header.
     * 
     * @param jhove2
     *            JHOVE2 framework
     * @param input
     *            ICC input
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
        
        /* Profile size. */
        this.profileSize = input.readUnsignedInt();
        consumed += 4;
        
        /* Preferred CMM type. */
        boolean unidentified = true;
        for (int i=0; i<4; i++) {
            short b = input.readUnsignedByte();
            if (unidentified && b != 0) {
                unidentified = false;
            }
            this.preferredCMM.append((char)b);
            consumed++;
        }
        if (!unidentified) {
            PreferredCMM cmm =
                PreferredCMM.getPreferredCMM(this.preferredCMM.toString(),
                                             jhove2);
            if (cmm != null) {
                this.preferredCMM_s = cmm.getDescription();
            }
            else {
                numErrors++;
                this.isValid = Validity.False;
                Object [] args = new Object[] {input.getPosition()-4L,
                                               this.preferredCMM.toString()};
                this.invalidPreferredCMMMessage = new Message(Severity.ERROR,
                        Context.OBJECT,
                        "org.jhove2.module.format.icc.ICCHeader.invalidPreferredSignature",
                        args, jhove2.getConfigInfo());
            }
        }
        else {
            this.preferredCMM = null;
        }
        
        /* Profile version. */
        short [] ba = new short[4];
        for (int i=0; i<4; i++) {
            ba[i] = input.readUnsignedByte();
            consumed++;
        }
        this.profileVersionNumber.append(Short.toString(ba[0]));
        this.profileVersionNumber.append('.');
        this.profileVersionNumber.append(Short.toString((short)(ba[1] >>> 4)));
        this.profileVersionNumber.append('.');
        this.profileVersionNumber.append(Short.toString((short)(ba[2] & 0x0f)));
        this.profileVersionNumber.append(".0");
        
        /* Profile/device class. */
        for (int i=0; i<4; i++) {
            this.profileDeviceClass.append((char)input.readUnsignedByte());
            consumed++;
        }
        ProfileDeviceClass profileClass =
            ProfileDeviceClass.getProfileDeviceClass(this.profileDeviceClass.toString(),
                                                     jhove2);
        if (profileClass != null) {
            this.profileDeviceClass_s = profileClass.getProfileClass();
        }
        else {
            numErrors++;
            this.isValid = Validity.False;
            Object [] args = new Object[] {input.getPosition()-4L,
                                           this.profileDeviceClass.toString()};
            this.invalidProfileDeviceClassMessage = new Message(Severity.ERROR,
                    Context.OBJECT,
                    "org.jhove2.module.format.icc.ICCHeader.invalidProfileDeviceClass",
                    args, jhove2.getConfigInfo());
        }
        
        /* Colour space. */
        for (int i=0; i<4; i++) {
            this.colourSpace.append((char)input.readUnsignedByte());
            consumed++;
        }
        ColourSpace colourSpace =
            ColourSpace.getColourSpace(this.colourSpace.toString(), jhove2);
        if (colourSpace != null) {
            this.colourSpace_s = colourSpace.getColourSpace();
        }
        else {
            numErrors++;
            this.isValid = Validity.False;
            Object [] args = new Object[] {input.getPosition()-4L,
                                           this.colourSpace.toString()};
            this.invalidColourSpaceMessage = new Message(Severity.ERROR,
                    Context.OBJECT,
                    "org.jhove2.module.format.icc.ICCHeader.invalidColourSpace",
                    args, jhove2.getConfigInfo());
        }
        
        /* Profile Connection Space (PCS). */
        for (int i=0; i<4; i++) {
            this.profileConnectionSpace.append((char)input.readUnsignedByte());
            consumed++;
        }
        colourSpace =
            ColourSpace.getColourSpace(this.profileConnectionSpace.toString(), jhove2);
        if (colourSpace != null) {
            this.profileConnectionSpace_s = colourSpace.getColourSpace();
        }
        else {
            numErrors++;
            this.isValid = Validity.False;
            Object [] args = new Object[] {input.getPosition()-4L,
                                           this.profileConnectionSpace.toString()};
            this.invalidColourSpaceMessage = new Message(Severity.ERROR,
                    Context.OBJECT,
                    "org.jhove2.module.format.icc.ICCHeader.invalidProfileConnectionSpace",
                    args, jhove2.getConfigInfo());
        }
        if (!this.profileDeviceClass.toString().equals("link") &&
            !this.profileConnectionSpace.toString().equals("XYZ ") &&
            !this.profileConnectionSpace.toString().equals("Lab ")) {
            numErrors++;
            this.isValid = Validity.False;
            Object [] args = new Object[] {input.getPosition()-4L,
                                           this.profileConnectionSpace.toString()};
            this.invalidProfileConnectionSpaceForNonDeviceLinkProfileMessage =
                new Message(Severity.ERROR, Context.OBJECT,
                    "org.jhove2.module.format.icc.ICCHeader.invalidProfileConnectionSpaceForNonDeviceLinkProfile",
                    args, jhove2.getConfigInfo());
        }
        
        /* Data and time. */
        int [] sa = new int[6];
        for (int i=0; i<6; i++) {
            sa[i] = input.readUnsignedShort();
            consumed += 2;
        }
        /* TODO: There appears to be a bug in the GregorianCalendar class where the
         * second field is overwriting the day of month field.  For the time being
         * we choose to have the date be correct and accept an error in the seconds.
         */
        Calendar cal = new GregorianCalendar(sa[0], sa[1]-1, sa[2],
                                             sa[3], sa[4], sa[2]);
        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
        this.dateAndTime = cal.getTime();
        
        /* Profile file signature. */
        for (int i=0; i<4; i++) {
            this.profileFileSignature.append((char)input.readUnsignedByte());
            consumed++;
        }
        if (!this.profileFileSignature.toString().equals("acsp")) {
            numErrors++;
            this.isValid = Validity.False;
            Object [] args = new Object[] {input.getPosition()-4L,
                                           this.profileFileSignature.toString()};
            this.invalidProfileFileSignatureMessage = new Message(Severity.ERROR,
                    Context.OBJECT,
                    "org.jhove2.module.format.icc.ICCHeader.invalidProfileFileSignature",
                    args, jhove2.getConfigInfo());
        }
        
        /* Primary platform. */
        unidentified = true;
        for (int i=0; i<4; i++) {
            short b = input.readUnsignedByte();
            if (unidentified && b != 0) {
                unidentified = false;
            }
            this.primaryPlatform.append((char)b);
            consumed++;
        }
        if (!unidentified) {
            PrimaryPlatform primaryPlatform =
                PrimaryPlatform.getPrimaryPlatform(this.primaryPlatform.toString(), jhove2);
            if (primaryPlatform != null) {
                this.primaryPlatform_s = primaryPlatform.getPrimaryPlatform();
            }
            else {
                numErrors++;
                this.isValid = Validity.False;
                Object [] args = new Object[] {input.getPosition()-4L,
                                               this.primaryPlatform.toString()};
                this.invalidColourSpaceMessage = new Message(Severity.ERROR,
                        Context.OBJECT,
                        "org.jhove2.module.format.icc.ICCHeader.invalidPrimaryPlatform",
                        args, jhove2.getConfigInfo());
            }
        }
        else {
            this.primaryPlatform = null;
        }
        
        /* Profile flags. */
        this.profileFlagsField = input.readUnsignedInt();
        consumed += 4;
        Set<ProfileFlag> flags = ProfileFlag.getProfileFlags(jhove2);
        Iterator<ProfileFlag> pfIter = flags.iterator();
        while (pfIter.hasNext()) {
            ProfileFlag flag = pfIter.next();
            int bitPosition = flag.getPosition();
            long mask = 1L << bitPosition;
            if ((this.profileFlagsField & mask) == 0L) {
                this.profileFlags.add(flag.getNegativeValue());
            }
            else {
                this.profileFlags.add(flag.getPositiveValue());
            }
        }
        
        /* Device manufacturer. */
        unidentified = true;
        for (int i=0; i<4; i++) {
            short b = input.readUnsignedByte();
            if (unidentified && b != 0) {
                unidentified = false;
            }
            this.deviceManufacturer.append((char)b);
            consumed++;
        }
        if (unidentified) {
            this.deviceManufacturer = null;
        }
        
        /* Device model. */
        unidentified = true;
        for (int i=0; i<4; i++) {
            short b = input.readUnsignedByte();
            if (unidentified && b != 0) {
                unidentified = false;
            }
            this.deviceModel.append((char)b);
            consumed++;
        }
        if (unidentified) {
            this.deviceModel = null;
        }
        
        /* Device attributes. */
        this.deviceAttributesField = input.readSignedLong();
        consumed += 8;
        Set<DeviceAttribute> attrs = DeviceAttribute.getDeviceAttributes(jhove2);
        Iterator<DeviceAttribute> daIter = attrs.iterator();
        while (daIter.hasNext()) {
            DeviceAttribute attr = daIter.next();
            int bitPosition = attr.getPosition();
            long mask = 1L << bitPosition;
            if ((this.deviceAttributesField & mask) == 0L) {
                this.deviceAttributes.add(attr.getNegativeValue());
            }
            else {
                this.deviceAttributes.add(attr.getPositiveValue());
            }
        }
        /* Rendering intents. */
        int highOrder = input.readUnsignedShort();
        consumed += 2;
        if (highOrder != 0) {
            numErrors++;
            this.isValid = Validity.False;
            Object [] args = new Object[] {input.getPosition()-2L, highOrder};
            this.nonZeroHighOrderRenderingIntentMessage = new Message(Severity.ERROR,
                    Context.OBJECT,
                    "org.jhove2.module.format.icc.ICCHeader.nonZeroHighOrderRenderingIntent",
                    args, jhove2.getConfigInfo());
        }
        this.renderingIntent = input.readUnsignedShort();
        consumed += 2;
        RenderingIntent intent =
            RenderingIntent.getRenderingIntent(this.renderingIntent, jhove2);
        if (intent != null) {
            this.renderingIntent_s = intent.getRenderingIntent();
        }
        else {
            numErrors++;
            this.isValid = Validity.False;
            Object [] args = new Object[] {input.getPosition()-4L,
                                           this.renderingIntent};
            this.invalidRenderingIntentMessage = new Message(Severity.ERROR,
                    Context.OBJECT,
                    "org.jhove2.module.format.icc.ICCHeader.invalidRenderingIntent",
                    args, jhove2.getConfigInfo());
        }
        
        /* PCS XYZ tri-stimulus values. */
        int x = input.readSignedInt();
        int y = input.readSignedInt();
        int z = input.readSignedInt();
        this.illuminant = new XYZNumber(x, y, z);
        if (x != D50[0] || y != D50[1] || z != D50[2]) {
            numErrors++;
            this.isValid = Validity.False;
            Object [] args = new Object[] {input.getPosition(), this.illuminant.toString()};
            this.pcsIlluminantNotD50Message = new Message(Severity.ERROR,
                    Context.OBJECT,
                    "org.jhove2.module.format.icc.ICCHeader.pcsIlluminantNotD50",
                    args, jhove2.getConfigInfo());
        }
        consumed += 12;
        
        /* Profile creator. */
        unidentified = true;
        for (int i=0; i<4; i++) {
            short b = input.readUnsignedByte();
            if (unidentified && b != 0) {
                unidentified = false;
            }
            this.profileCreator.append((char)b);
            consumed++;
        }
        if (unidentified) {
            this.profileCreator = null;
        }
        
        /* Profile ID (MD5). */
        for (int i=0; i<16; i++) {
            byte b = input.readSignedByte();
            this.profileID.append(String.format("%02x", b));
            consumed++;
        }
        
        /* Reserved fields. */
        for (int i=0; i<28; i++) {
            byte b = input.readSignedByte();
            if (b != 0) {
                numErrors++;
                this.isValid = Validity.False;
                Object [] args = new Object[] {input.getPosition()-1L, b};
                this.nonZeroDataInReservedBlockMessages.add(new Message(Severity.ERROR,
                    Context.OBJECT,
                    "org.jhove2.module.format.icc.ICCHeader.nonZeroDataInReservedBlock",
                    args, jhove2.getConfigInfo()));
            }
            consumed++;
        }

        return consumed;
    }
    
    /** Get colour space of data , i.e. "the canonical input space", in coded form.
     * @return Colour space in coded form
     */
    @ReportableProperty(order=5, value="Colour space of data, i.e. \"the canonical input space\", in coded form",
            ref="ICC.1:2004-10, \u00a7 7.2.6", type=PropertyType.Coded)
    public String getColourSpace() {
        return this.colourSpace.toString();
    }
    
    /** Get colour space of data , i.e. "the canonical input space", in symbolic form.
     * @return Colour space in symbolic form
     */
    @ReportableProperty(order=5, value="Colour space of data, i.e. \"the canonical input space\", in symbolic form",
            ref="ICC.1:2004-10, \u00a7 7.2.6", type=PropertyType.Symbolic)
    public String getColourSpace_s() {
        return this.colourSpace_s;
    }
    
    /** Get profile creation date/time in UTC.
     * @return Profile creation date/time
     */
    @ReportableProperty(order=7, value="Profile creation date/time in UTC.",
            ref="ICC.1:2004-10, Table 13")
    public Date getDateAndTime() {
        return this.dateAndTime;
    }
    
    /** Get device attributes field in coded form.
     * @return device attributes field in coded form
     */
    @ReportableProperty(order=14, value="Device attributes in coded form.",
            ref="ICC.1:2004-10 \u00a7 7.2.14")
    public long getDeviceAttributesField() {
        return this.deviceAttributesField;
    }
    
    /** Get device attributes in symbolic form.
     * @return device attributes in symbolic form
     */
    @ReportableProperty(order=15, value="Device attributes in symbolic form.",
            ref="ICC.1:2004-10 \u00a7 7.2.14")
    public List<String> getDeviceAttributes() {
        return this.deviceAttributes;
    }
    
    /** Get device manufacturer.
     * @return Device manufacturer
     */
    @ReportableProperty(order=12, value="Device manufacturer.",
            ref="ICC.1:2004-10 \u00a7 7.2.12")
    public String getDeviceManufacturer() {
        if (this.deviceManufacturer != null) {
            return this.deviceManufacturer.toString();
        }
        
        return null;
    }
    
    /** Get device model.
     * @return Device model
     */
    @ReportableProperty(order=13, value="Device model.",
            ref="ICC.1:2004-10 \u00a7 7.2.13")
    public String getDeviceModel() {
        if (this.deviceModel != null) {
            return this.deviceModel.toString();
        }
        
        return null;
    }
 
    /** Get invalid data colour space message.
     * @return Invalid data colour space message
     */
    @ReportableProperty(order=23, value="Invalid data colour space.",
            ref="ICC.1:2004-10, Table 15")
    public Message getInvalidColourSpaceMessage() {
        return this.invalidColourSpaceMessage;
    }
    
    /** Get invalid device attributes message.
     * @return Invalid device attributes message
     */
    @ReportableProperty(order=31, value="Invalid device attributes.",
            ref="ICC.1:2004-10, Table 18")
    public Message getInvalidDeviceAttributesMessage() {
        return this.invalidDeviceAttributesMessage;
    }
    
    /** Get invalid device manufacturer message.
     * @return Invalid device manufacturer message
     */
    @ReportableProperty(order=29, value="Invalid device manufacturer.",
            ref="ICC.1:2004-10, \u00a7 7.2.12")
    public Message getInvalidDeviceManufacturerMessage() {
        return this.invalidDeviceManufacturerMessage;
    }
    
    /** Get invalid device model message.
     * @return Invalid device model message
     */
    @ReportableProperty(order=30, value="Invalid device model.",
            ref="ICC.1:2004-10, \u00a7 7.2.13")
    public Message getInvalidDeviceModelMessage() {
        return this.invalidDeviceModelMessage;
    }
    
    /** Get invalid preferred CMM type message.
     * @return Invalid preferred CMM type message
     */
    @ReportableProperty(order=21, value="Invalid preferred CMM type.",
            ref="ICC.1:2004-10, \u00a7 7.2.3")
    public Message getInvalidPreferredCMMMessage() {
        return this.invalidPreferredCMMMessage;
    }
    
    /** Get invalid primary platform message.
     * @return Invalid primary platform message
     */
    @ReportableProperty(order=27, value="Invalid primary platform.",
            ref="ICC.1:2004-10, Table 16")
    public Message getInvalidPrimaryPlatformMessage() {
        return this.invalidPrimaryPlatformMessage;
    }
    
    /** Get invalid Profile Connection Space message.
     * @return Invalid Profile Connection Space message
     */
    @ReportableProperty(order=24, value="Invalid Profile Connection Space (PCS).",
            ref="ICC.1:2004-10, \u00a7 7.2.7")
    public Message getInvalidProfileConnectionSpaceMessage() {
        return this.invalidProfileConnectionSpaceMessage;
    }
    
    /** Get invalid Profile Connection Space for non-DeviceLink profile message.
     * @return Invalid Profile Connection Space for non-DeviceLink profile message
     */
    @ReportableProperty(order=25, value="Invalid Profile Connection Space (PCS) for non-DeviceLink profile.",
            ref="ICC.1:2004-10, \u00a7 7.2.7")
    public Message getInvalidProfileConnectionSpaceForNonDeviceLinkProfileMessage() {
        return this.invalidProfileConnectionSpaceForNonDeviceLinkProfileMessage;
    }
    
    /** Get invalid profile creator message.
     * @return Invalid profile creator message
     */
    @ReportableProperty(order=35, value="Invalid profile creator.",
            ref="ICC.1:2004-10, \u00a7 7.2.17")
    public Message getInvalidProfileCreatorMessage() {
        return this.invalidProfileCreatorMessage;
    }
    
    /** Get invalid profile/device class message.
     * @return Invalid profile/device class message
     */
    @ReportableProperty(order=22, value="Invalid profile/device class.",
            ref="ICC.1:2004-10, Table 14")
    public Message getInvalidProfileDeviceClassMessage() {
        return this.invalidProfileDeviceClassMessage;
    }
    
    /** Get invalid profile file signature message.
     * @return Invalid profile file signature message
     */
    @ReportableProperty(order=26, value="Invalid profile file signature.",
            ref="ICC.1:2004-10, \u00a7 7.2.9")
    public Message getInvalidProfileFileSignatureMessage() {
        return this.invalidProfileFileSignatureMessage;
    }
    
    /** Get invalid profile flags message.
     * @return Invalid profile flags message
     */
    @ReportableProperty(order=28, value="Invalid profile flags.",
            ref="ICC.1:2004-10, Table 17")
    public Message getInvalidProfileFlagsMessage() {
        return this.invalidProfileFlagsMessage;
    }
    
    /** Get invalid profile ID message.
     * @return Invalid profile ID message
     */
    @ReportableProperty(order=36, value="Invalid profile ID.",
            ref="ICC.1:2004-10, \u00a7 7.2.18")
    public Message getInvalidProfileIDMessage() {
        return this.invalidProfileIDMessage;
    }
   
    /** Get invalid rendering intent message.
     * @return Invalid rendering intent message
     */
    @ReportableProperty(order=33, value="Invalid rendering intent.",
            ref="ICC.1:2004-10, Table 19")
    public Message getInvalidRenderingIntentMessage() {
        return this.invalidRenderingIntentMessage;
    }
    
    /** Get non-zero data in reserved block error message.
     * @return Non-zero data in reserved block error message
     */
    @ReportableProperty(order=37, value="Invalid non-zero data in reserved block.",
            ref="ICC.1:2004-10, \u00a7 7.2.19")
    public List<Message> getNonZeroDataInReservedBlockMessages() {
        return this.nonZeroDataInReservedBlockMessages;
    }
    
    /** Get non-zero high-order rendering intent message.
     * @return Non-zero high-order rendering intent message
     */
    @ReportableProperty(order=32, value="Invalid non-zero high-order rendering intent.",
            ref="ICC.1:2004-10, \u00a7 7.2.15")
    public Message getNonZeroHighOrderRenderingIntentMessage() {
        return this.nonZeroHighOrderRenderingIntentMessage;
    }
    
    /** Get Profile Connection Space (PCS) illuminant.
     * @return Profile Connection Space illuminant
     */
    @ReportableProperty(order=16, value="Profile Connection Space (PCS) illuminant",
            ref="ICC.1:2004-10, \u00a7 7.2.16")
    public XYZNumber getPCSIlluminant() {
        return this.illuminant;
    }
    
    /** Get Profile Connection Space (PCS) illuminant not D50 message.
     * @return Profile Connection Space illuminant no D50 message
     */
    @ReportableProperty(order=34, value="Profile Connection Space (PCS) illuminant not D50.",
            ref="ICC.1:2004-10, \u00a7 7.2.16")
    public Message getPCSIlluminantNotD50Message() {
        return this.pcsIlluminantNotD50Message;
    }
      
    /** Get preferred CMM type in coded form.
     * @return Preferred CMM type in coded form
     */
    @ReportableProperty(order=2, value="Preferred CMM type in coded form.",
            ref="ICC.1:2004-10, \u00a7 7.2.7", type=PropertyType.Coded)
    public String getPreferredCMM() {
        if (this.preferredCMM != null) {
            return this.preferredCMM.toString();
        }
        
        return null;
    }
    
    /** Get preferred CMM type in symbolic form.
     * @return Preferred CMM type in symbolic form
     */
    @ReportableProperty(order=2, value="Preferred CMM type in symbolic form.",
            ref="ICC Private and ICC Tag and CMM Registry",
            type=PropertyType.Symbolic)
    public String getPreferredCMM_s() {
        return this.preferredCMM_s;
    }
    
    /** Get primary platform in coded form.
     * @return Primary platform in coded form
     */
    @ReportableProperty(order=9, value="Primary platform in coded form.",
            ref="ICC.1:2004-10, \u00a7 7.2.10", type=PropertyType.Coded)
    public String getPrimaryPlatform() {
        if (this.primaryPlatform != null) {
            return this.primaryPlatform.toString();
        }
        
        return null;
    }
    
    /** Get primary platform in symbolic form.
     * @return Primary platform in symbolic form
     */
    @ReportableProperty(order=9, value="Primary platform in symbolic form.",
            ref="ICC.1:2004-10, \u00a7 7.2.10", type=PropertyType.Symbolic)
    public String getPrimaryPlatform_s() {
        return this.primaryPlatform_s;
    }
    
    /** Get Profile Connection Space (PCS), i.e. "the canonical output space", in coded form.
     * @return Profile Connection Space in coded form
     */
    @ReportableProperty(order=6, value="Profile Connection Space (PCS), i.e. \"the canonical output space\", in coded form",
            ref="ICC.1:2004-10, \u00a7 7.2.7", type=PropertyType.Coded)
    public String getProfileConnectionSpace() {
        return this.profileConnectionSpace.toString();
    }
    
    /** Get Profile Connection Space (PCS), i.e. "the canonical output space", in symbolic form.
     * @return Profile Connection Space in symbolic form
     */
    @ReportableProperty(order=6, value="Profile Connection Space (PCS), i.e. \"the canonical output space\", in symbolic form",
            ref="ICC.1:2004-10, \u00a7 7.2.7", type=PropertyType.Symbolic)
    public String getProfileConnectionSpace_s() {
        return this.profileConnectionSpace_s;
    }
   
    /** Get profile creator.
     * @return Profile creator
     */
    @ReportableProperty(order=18, value="Profile creator.",
            ref="ICC.1:2004-10, \u00a7 7.2.17")
    public String getProfileCreator() {
        if (this.profileCreator != null) {
            return this.profileCreator.toString();
        }
        
        return null;
    }
    
    /** Get profile/device class in coded form.
     * @return Profile/device class
     */
    @ReportableProperty(order=4, value="Profile/device class in coded form.",
            ref="ICC.1:2004-10, \u00a7 7.2.5", type=PropertyType.Coded)
    public String getProfileDeviceClass() {
        return this.profileDeviceClass.toString();
    }
    
    /** Get profile/device class in symbolic form.
     * @return Profile/device class
     */
    @ReportableProperty(order=4, value="Profile/device class in symbolic form.",
            ref="ICC.1:2004-10, \u00a7 7.2.5", type=PropertyType.Symbolic)
    public String getProfileDeviceClass_s() {
        return this.profileDeviceClass_s;
    }
    
    /** Get profile file signature.
     * @return Profile file signature
     */
    @ReportableProperty(order=8, value="Profile file signature.",
            ref="ICC.1.2004-10, \u00a7 7.2.9")
    public String getProfileFileSignature() {
        return this.profileFileSignature.toString();
    }
    
    /** Get profile flags field in coded form.
     * @return Profile flags field in coded form
     */
    @ReportableProperty(order=10, value="Profile flags in coded form.",
            ref="ICC.1:2004-10, \u00a7 7.2.11")
    public long getProfileFlagsField() {
        return this.profileFlagsField;
    }
    
    /** Get profile flags in symbolic form.
     * @return Profile flags in symbolic form
     */
    @ReportableProperty(order=11, value="Profile flags in symbolic form.",
            ref="ICC.1:2004-10, \u00a7 7.2.11")
    public List<String> getProfileFlags() {
        return this.profileFlags;
    }
    
    /** Get profile ID (MD5).
     * @return Profile ID
     */
    @ReportableProperty(order=19, value="Profile ID (MD5).",
            ref="ICC.1:2004-10, \u00a7 7.2.18")
    public String getProfileID() {
        return this.profileID.toString();
    }

    /** Get profile size.
     * @return Profile size
     */
    @ReportableProperty(order=1, value="Profile size.",
            ref="ICC.1:2004-10, \u00a7 7.2.2")
    public long getProfileSize() {
        return this.profileSize;
    }
    
    /** Get profile version number.
     * @return Profile version number
     */
    @ReportableProperty(order=3, value="Profile version number.",
            ref="ICC.1:2004-10, \u00a7 7.2.4")
    public String getProfileVersionNumber() {
        return this.profileVersionNumber.toString();
    }
 
    /** Get rendering intent in coded form.
     * @return Rendering intent in coded form
     */
    @ReportableProperty(order=16, value="Rendering intent in coded form.",
            ref="ICC.1:2004-10, \u00a7 7.2.15", type=PropertyType.Coded)
    public int getRenderingIntent() {
        return this.renderingIntent;
    }

    /** Get rendering intent in symbolic form.
     * @return Rendering intent in symbolic form
     */
    @ReportableProperty(order=16, value="Rendering intent in symbolic form.",
            ref="ICC.1:2004-10, \u00a7 7.2.15", type=PropertyType.Symbolic)
    public String getRenderingIntent_s() {
        return this.renderingIntent_s;
    }

    /** Get validity.
     * @return Validity
     */
    @ReportableProperty(order=20, value="Header validity",
            ref="ICC.1:2004-10, \u00a7 7.2")
    public Validity isValid()
    {
         return this.isValid;
    }
}
