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
import org.jhove2.core.source.Source;
import org.jhove2.module.format.Parser;
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
    implements Parser
{
    /** D50 illuminant CIE XYZ values. These hexadecmial values are equivalent
     * to 0.9642, 1.0, 0.8249. */
    public static final int [] D50 = {0x0000f6d6, 0x00010000, 0x0000d32d};
    
    /** Colour space in raw form. */
    protected StringBuffer colourSpace = new StringBuffer(4);
    
    /** Colour space in descriptive form. */
    protected String colourSpace_d;
    
    /** Creation date/time in UTC. */
    protected Date dateAndTime;
    
    /** Device attributes field in raw form. */
    protected long deviceAttributes;
    
    /** Device attributes in descriptive form. */
    protected List<String> deviceAttributes_d = new ArrayList<String>();
    
    /** Device manufacturer. */
    protected StringBuffer deviceManufacturer = new StringBuffer(4);
    
    /** Device model. */
    protected StringBuffer deviceModel = new StringBuffer(4);
    
    /** Profile Connection Space (PCS) illuminant. */
    protected XYZNumber illuminant;
    
    /** Profile Connection Space (PCS) D50 illuminant status:
     * true if illuminant is D50.
     */
    protected boolean isD50Illuminant;
    
    /** DeviceLink profile status: true if a DeviceLink profile. */
    protected boolean isDeviceLinkProfile;
  
    /** Header validity status. */
    protected Validity isValid;
    
    /** Offset of where ICC Header begins */
    protected long offset;
    
    /** Preferred CMM type in raw form. */
    protected StringBuffer preferredCMM = new StringBuffer(4);
    
    /** Preferred CMM type in descriptive form. */
    protected String preferredCMM_d;
    
    /** Primary platform in raw form. */
    protected StringBuffer primaryPlatform = new StringBuffer(4);
    
    /** Primary platform in descriptive form. */
    protected String primaryPlatform_d;
    
    /** Profile Connection Space (PCS) in raw form. */
    protected StringBuffer profileConnectionSpace = new StringBuffer(4);
    
    /** Profile Connection Space (PCS) in descriptive form. */
    protected String profileConnectionSpace_d;
    
    /** Profile creator . */
    protected StringBuffer profileCreator = new StringBuffer(4);
    
    /** Profile/device class in raw form. */
    protected StringBuffer profileDeviceClass = new StringBuffer(4);
    
    /** Profile/device class in descriptive form. */
    protected String profileDeviceClass_d;
    
    /** Profile file signature. */
    protected StringBuffer profileFileSignature = new StringBuffer(4);
    
    /** Profile flags field in raw form. */
    protected long profileFlags;
    
    /** Profile flags in descriptive form. */
    protected List<String> profileFlags_d = new ArrayList<String>();
    
    /** Profile ID (MD5). */
    protected StringBuffer profileID = new StringBuffer(32);
    
    /** Profile size. */
    protected long profileSize;
    
    /** Profile version number. */
    protected StringBuffer profileVersionNumber = new StringBuffer(4);
    
    /** Rendering intent in raw form. */
    protected int renderingIntent;
    
    /** Rendering intent in descriptive form. */
    protected String renderingIntent_d;

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
   
    /** Non-zero data in reserved field error message. */
    protected List<Message> nonZeroDataInReservedFieldMessages;
    
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
        this.nonZeroDataInReservedFieldMessages = new ArrayList<Message>();
    }
    
    /** 
     * Parse an ICC header.
     * 
     * @param jhove2
     *            JHOVE2 framework
     * @param source
     *            ICC source
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
        long consumed = 0L;
        int numErrors = 0;
        this.isValid = Validity.True;
        Input input  = source.getInput(jhove2, ByteOrder.BIG_ENDIAN);
        long offset  = source.getStartingOffset();
        
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
            this.preferredCMM.append((char) b);
        }
        if (!unidentified) {
            PreferredCMM cmm =
                PreferredCMM.getPreferredCMM(this.preferredCMM.toString(),
                                             jhove2);
            if (cmm != null) {
                this.preferredCMM_d = cmm.getDescription();
            }
            else {
                numErrors++;
                this.isValid = Validity.False;
                Object [] args = new Object[] {input.getPosition()-4L-offset,
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
        consumed += 4;
        
        /* Profile version. */
        short [] ba = new short[4];
        for (int i=0; i<4; i++) {
            ba[i] = input.readUnsignedByte();
        }
        this.profileVersionNumber.append(Short.toString(ba[0]));
        this.profileVersionNumber.append('.');
        this.profileVersionNumber.append(Short.toString((short)(ba[1] >>> 4)));
        this.profileVersionNumber.append('.');
        this.profileVersionNumber.append(Short.toString((short)(ba[2] & 0x0f)));
        this.profileVersionNumber.append(".0");
        consumed += 4;
        
        /* Profile/device class. */
        this.isDeviceLinkProfile = false;
        for (int i=0; i<4; i++) {
            this.profileDeviceClass.append((char)input.readUnsignedByte());
        }
        ProfileDeviceClass profileClass =
            ProfileDeviceClass.getProfileDeviceClass(this.profileDeviceClass.toString(),
                                                     jhove2);
        if (profileClass != null) {
            this.profileDeviceClass_d = profileClass.getProfileClass();
            if (this.profileDeviceClass.toString().equals("link")) {
                this.isDeviceLinkProfile = true;
            }
        }
        else {
            numErrors++;
            this.isValid = Validity.False;
            Object [] args = new Object[] {input.getPosition()-4L-offset,
                                           this.profileDeviceClass.toString()};
            this.invalidProfileDeviceClassMessage = new Message(Severity.ERROR,
                    Context.OBJECT,
                    "org.jhove2.module.format.icc.ICCHeader.invalidProfileDeviceClass",
                    args, jhove2.getConfigInfo());
        }
        consumed += 4;
        
        /* Colour space. */
        for (int i=0; i<4; i++) {
            this.colourSpace.append((char)input.readUnsignedByte());
        }
        ColourSpace colourSpace =
            ColourSpace.getColourSpace(this.colourSpace.toString(), jhove2);
        if (colourSpace != null) {
            this.colourSpace_d = colourSpace.getColourSpace();
        }
        else {
            numErrors++;
            this.isValid = Validity.False;
            Object [] args = new Object[] {input.getPosition()-4L-offset,
                                           this.colourSpace.toString()};
            this.invalidColourSpaceMessage = new Message(Severity.ERROR,
                    Context.OBJECT,
                    "org.jhove2.module.format.icc.ICCHeader.invalidColourSpace",
                    args, jhove2.getConfigInfo());
        }
        consumed += 4;
        
        /* Profile Connection Space (PCS). */
        for (int i=0; i<4; i++) {
            this.profileConnectionSpace.append((char)input.readUnsignedByte());
        }
        colourSpace =
            ColourSpace.getColourSpace(this.profileConnectionSpace.toString(), jhove2);
        if (colourSpace != null) {
            this.profileConnectionSpace_d = colourSpace.getColourSpace();
        }
        else {
            numErrors++;
            this.isValid = Validity.False;
            Object [] args = new Object[] {input.getPosition()-4L-offset,
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
            Object [] args = new Object[] {input.getPosition()-4L-offset,
                                           this.profileConnectionSpace.toString()};
            this.invalidProfileConnectionSpaceForNonDeviceLinkProfileMessage =
                new Message(Severity.ERROR, Context.OBJECT,
                    "org.jhove2.module.format.icc.ICCHeader.invalidProfileConnectionSpaceForNonDeviceLinkProfile",
                    args, jhove2.getConfigInfo());
        }
        consumed += 4;
        
        /* Data and time. */
        int [] sa = new int[6];
        for (int i=0; i<6; i++) {
            sa[i] = input.readUnsignedShort();
        }
        Calendar cal = new GregorianCalendar(sa[0], sa[1]-1, sa[2],
                                             sa[3], sa[4], sa[5]);
        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
        this.dateAndTime = cal.getTime();
        consumed += 12;
        System.out.println("# DATE " + this.dateAndTime);
        
        /* Profile file signature. */
        for (int i=0; i<4; i++) {
            this.profileFileSignature.append((char)input.readUnsignedByte());
        }
        if (!this.profileFileSignature.toString().equals("acsp")) {
            numErrors++;
            this.isValid = Validity.False;
            Object [] args = new Object[] {input.getPosition()-4L-offset,
                                           this.profileFileSignature.toString()};
            this.invalidProfileFileSignatureMessage = new Message(Severity.ERROR,
                    Context.OBJECT,
                    "org.jhove2.module.format.icc.ICCHeader.invalidProfileFileSignature",
                    args, jhove2.getConfigInfo());
        }
        consumed += 4;
        
        /* Primary platform. */
        unidentified = true;
        for (int i=0; i<4; i++) {
            short b = input.readUnsignedByte();
            if (unidentified && b != 0) {
                unidentified = false;
            }
            this.primaryPlatform.append((char) b);
        }
        if (!unidentified) {
            PrimaryPlatform primaryPlatform =
                PrimaryPlatform.getPrimaryPlatform(this.primaryPlatform.toString(), jhove2);
            if (primaryPlatform != null) {
                this.primaryPlatform_d = primaryPlatform.getPrimaryPlatform();
            }
            else {
                numErrors++;
                this.isValid = Validity.False;
                Object [] args = new Object[] {input.getPosition()-4L-offset,
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
        consumed += 4;
        
        /* Profile flags. */
        this.profileFlags = input.readUnsignedInt();
        Set<ProfileFlag> flags = ProfileFlag.getProfileFlags(jhove2);
        Iterator<ProfileFlag> pfIter = flags.iterator();
        while (pfIter.hasNext()) {
            ProfileFlag flag = pfIter.next();
            int bitPosition = flag.getPosition();
            long mask = 1L << bitPosition;
            if ((this.profileFlags & mask) == 0L) {
                this.profileFlags_d.add(flag.getNegativeValue());
            }
            else {
                this.profileFlags_d.add(flag.getPositiveValue());
            }
        }
        consumed += 4;
        
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
        this.deviceAttributes = input.readSignedLong();
        Set<DeviceAttribute> attrs = DeviceAttribute.getDeviceAttributes(jhove2);
        Iterator<DeviceAttribute> daIter = attrs.iterator();
        while (daIter.hasNext()) {
            DeviceAttribute attr = daIter.next();
            int bitPosition = attr.getPosition();
            long mask = 1L << bitPosition;
            if ((this.deviceAttributes & mask) == 0L) {
                this.deviceAttributes_d.add(attr.getNegativeValue());
            }
            else {
                this.deviceAttributes_d.add(attr.getPositiveValue());
            }
        }
        consumed += 8;
        
        /* Rendering intents. */
        int highOrder = input.readUnsignedShort();
        if (highOrder != 0) {
            numErrors++;
            this.isValid = Validity.False;
            Object [] args = new Object[] {input.getPosition()-2L-offset, highOrder};
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
            this.renderingIntent_d = intent.getRenderingIntent();
        }
        else {
            numErrors++;
            this.isValid = Validity.False;
            Object [] args = new Object[] {input.getPosition()-4L-offset,
                                           this.renderingIntent};
            this.invalidRenderingIntentMessage = new Message(Severity.ERROR,
                    Context.OBJECT,
                    "org.jhove2.module.format.icc.ICCHeader.invalidRenderingIntent",
                    args, jhove2.getConfigInfo());
        }
        consumed += 2;
        
        /* PCS illuminat. */
        int x = input.readSignedInt();
        int y = input.readSignedInt();
        int z = input.readSignedInt();
        this.illuminant = new XYZNumber(x, y, z);
        if (x == D50[0] && y == D50[1] && z == D50[2]) {
            this.isD50Illuminant = true;
        }
        else {
            this.isD50Illuminant = false;
            
            numErrors++;
            this.isValid = Validity.False;
            Object [] args = new Object[] {input.getPosition()-12L-offset, this.illuminant.toString()};
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
            this.profileCreator.append((char) b);
        }
        if (unidentified) {
            this.profileCreator = null;
        }
        consumed += 4;
        
        /* Profile ID (MD5). */
        for (int i=0; i<16; i++) {
            byte b = input.readSignedByte();
            this.profileID.append(String.format("%02x", b));
        }
        consumed += 16;
        
        /* Reserved field; must be all 0x0000. */
        for (int i=0; i<7; i++) {
            long in = input.readSignedInt();
            if (in != 0L) {
                numErrors++;
                this.isValid = Validity.False;
                Object [] args = new Object[] {input.getPosition()-1L-offset, in};
                this.nonZeroDataInReservedFieldMessages.add(new Message(Severity.ERROR,
                    Context.OBJECT,
                    "org.jhove2.module.format.icc.ICCHeader.nonZeroDataInReservedField",
                    args, jhove2.getConfigInfo()));
            }
        }
        consumed += 28;

        return consumed;
    }
    
    /** Get colour space of data , i.e. "the canonical input space", in raw form.
     * @return Colour space in raw form
     */
    @ReportableProperty(order=7, value="Colour space of data, i.e. \"the canonical input space\", in raw form",
            ref="ICC.1:2004-10, \u00a7 7.2.6", type=PropertyType.Raw)
    public String getColourSpace_raw() {
        return this.colourSpace.toString();
    }
    
    /** Get colour space of data , i.e. "the canonical input space", in descriptive form.
     * @return Colour space in descriptive form
     */
    @ReportableProperty(order=8, value="Colour space of data, i.e. \"the canonical input space\", in descriptive form",
            ref="ICC.1:2004-10, \u00a7 7.2.6", type=PropertyType.Descriptive)
    public String getColourSpace_descriptive() {
        return this.colourSpace_d;
    }
    
    /** Get profile creation date/time in UTC.
     * @return Profile creation date/time
     */
    @ReportableProperty(order=12, value="Profile creation date/time in UTC.",
            ref="ICC.1:2004-10, Table 13")
    public Date getDateAndTime() {
        return this.dateAndTime;
    }
    
    /** Get device attributes field in raw form.
     * @return device attributes field in raw form
     */
    @ReportableProperty(order=21, value="Device attributes in raw form.",
            ref="ICC.1:2004-10 \u00a7 7.2.14", type=PropertyType.Raw)
    public String getDeviceAttributes_raw() {
        return String.format("0x%08x", this.deviceAttributes);
    }
    
    /** Get device attributes in descriptive form.
     * @return device attributes in descriptive form
     */
    @ReportableProperty(order=22, value="Device attributes in descriptive form.",
            ref="ICC.1:2004-10 \u00a7 7.2.14", type=PropertyType.Descriptive)
    public List<String> getDeviceAttributes() {
        return this.deviceAttributes_d;
    }
    
    /** Get device manufacturer.
     * @return Device manufacturer
     */
    @ReportableProperty(order=19, value="Device manufacturer.",
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
    @ReportableProperty(order=20, value="Device model.",
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
    @ReportableProperty(order=53, value="Invalid data colour space.",
            ref="ICC.1:2004-10, Table 15")
    public Message getInvalidColourSpaceMessage() {
        return this.invalidColourSpaceMessage;
    }
    
    /** Get invalid device attributes message.
     * @return Invalid device attributes message
     */
    @ReportableProperty(order=61, value="Invalid device attributes.",
            ref="ICC.1:2004-10, Table 18")
    public Message getInvalidDeviceAttributesMessage() {
        return this.invalidDeviceAttributesMessage;
    }
    
    /** Get invalid device manufacturer message.
     * @return Invalid device manufacturer message
     */
    @ReportableProperty(order=59, value="Invalid device manufacturer.",
            ref="ICC.1:2004-10, \u00a7 7.2.12")
    public Message getInvalidDeviceManufacturerMessage() {
        return this.invalidDeviceManufacturerMessage;
    }
    
    /** Get invalid device model message.
     * @return Invalid device model message
     */
    @ReportableProperty(order=60, value="Invalid device model.",
            ref="ICC.1:2004-10, \u00a7 7.2.13")
    public Message getInvalidDeviceModelMessage() {
        return this.invalidDeviceModelMessage;
    }
    
    /** Get invalid preferred CMM type message.
     * @return Invalid preferred CMM type message
     */
    @ReportableProperty(order=51, value="Invalid preferred CMM type.",
            ref="ICC.1:2004-10, \u00a7 7.2.3")
    public Message getInvalidPreferredCMMMessage() {
        return this.invalidPreferredCMMMessage;
    }
    
    /** Get invalid primary platform message.
     * @return Invalid primary platform message
     */
    @ReportableProperty(order=57, value="Invalid primary platform.",
            ref="ICC.1:2004-10, Table 16")
    public Message getInvalidPrimaryPlatformMessage() {
        return this.invalidPrimaryPlatformMessage;
    }
    
    /** Get invalid Profile Connection Space message.
     * @return Invalid Profile Connection Space message
     */
    @ReportableProperty(order=54, value="Invalid Profile Connection Space (PCS).",
            ref="ICC.1:2004-10, \u00a7 7.2.7")
    public Message getInvalidProfileConnectionSpaceMessage() {
        return this.invalidProfileConnectionSpaceMessage;
    }
    
    /** Get invalid Profile Connection Space for non-DeviceLink profile message.
     * @return Invalid Profile Connection Space for non-DeviceLink profile message
     */
    @ReportableProperty(order=55, value="Invalid Profile Connection Space (PCS) for non-DeviceLink profile.",
            ref="ICC.1:2004-10, \u00a7 7.2.7")
    public Message getInvalidProfileConnectionSpaceForNonDeviceLinkProfileMessage() {
        return this.invalidProfileConnectionSpaceForNonDeviceLinkProfileMessage;
    }
    
    /** Get invalid profile creator message.
     * @return Invalid profile creator message
     */
    @ReportableProperty(order=65, value="Invalid profile creator.",
            ref="ICC.1:2004-10, \u00a7 7.2.17")
    public Message getInvalidProfileCreatorMessage() {
        return this.invalidProfileCreatorMessage;
    }
    
    /** Get invalid profile/device class message.
     * @return Invalid profile/device class message
     */
    @ReportableProperty(order=52, value="Invalid profile/device class.",
            ref="ICC.1:2004-10, Table 14")
    public Message getInvalidProfileDeviceClassMessage() {
        return this.invalidProfileDeviceClassMessage;
    }
    
    /** Get invalid profile file signature message.
     * @return Invalid profile file signature message
     */
    @ReportableProperty(order=56, value="Invalid profile file signature.",
            ref="ICC.1:2004-10, \u00a7 7.2.9")
    public Message getInvalidProfileFileSignatureMessage() {
        return this.invalidProfileFileSignatureMessage;
    }
    
    /** Get invalid profile flags message.
     * @return Invalid profile flags message
     */
    @ReportableProperty(order=58, value="Invalid profile flags.",
            ref="ICC.1:2004-10, Table 17")
    public Message getInvalidProfileFlagsMessage() {
        return this.invalidProfileFlagsMessage;
    }
    
    /** Get invalid profile ID message.
     * @return Invalid profile ID message
     */
    @ReportableProperty(order=66, value="Invalid profile ID.",
            ref="ICC.1:2004-10, \u00a7 7.2.18")
    public Message getInvalidProfileIDMessage() {
        return this.invalidProfileIDMessage;
    }
   
    /** Get invalid rendering intent message.
     * @return Invalid rendering intent message
     */
    @ReportableProperty(order=63, value="Invalid rendering intent.",
            ref="ICC.1:2004-10, Table 19")
    public Message getInvalidRenderingIntentMessage() {
        return this.invalidRenderingIntentMessage;
    }
    
    /** Get the offset where the ICC Header starts in the input
     *  @return the offset
     */
    public long getOffset() {
        return offset;
    }

    /** Get non-zero data in reserved field error message.
     * @return Non-zero data in reserved field error message
     */
    @ReportableProperty(order=67, value="Invalid non-zero data in reserved field.",
            ref="ICC.1:2004-10, \u00a7 7.2.19")
    public List<Message> getNonZeroDataInReservedFieldMessages() {
        return this.nonZeroDataInReservedFieldMessages;
    }
    
    /** Get non-zero high-order rendering intent message.
     * @return Non-zero high-order rendering intent message
     */
    @ReportableProperty(order=62, value="Invalid non-zero high-order rendering intent.",
            ref="ICC.1:2004-10, \u00a7 7.2.15")
    public Message getNonZeroHighOrderRenderingIntentMessage() {
        return this.nonZeroHighOrderRenderingIntentMessage;
    }
    
    /** Get Profile Connection Space (PCS) illuminant.
     * @return Profile Connection Space illuminant
     */
    @ReportableProperty(order=25, value="Profile Connection Space (PCS) illuminant",
            ref="ICC.1:2004-10, \u00a7 7.2.16")
    public XYZNumber getPCSIlluminant() {
        return this.illuminant;
    }
    
    /** Get Profile Connection Space (PCS) illuminant not D50 message.
     * @return Profile Connection Space illuminant no D50 message
     */
    @ReportableProperty(order=64, value="Profile Connection Space (PCS) illuminant not D50.",
            ref="ICC.1:2004-10, \u00a7 7.2.16")
    public Message getPCSIlluminantNotD50Message() {
        return this.pcsIlluminantNotD50Message;
    }
      
    /** Get preferred CMM type in raw form.
     * @return Preferred CMM type in raw form
     */
    @ReportableProperty(order=2, value="Preferred CMM type in raw form.",
            ref="ICC.1:2004-10, \u00a7 7.2.7", type=PropertyType.Raw)
    public String getPreferredCMM_raw() {
        if (this.preferredCMM != null) {
            return this.preferredCMM.toString();
        }
        
        return null;
    }
    
    /** Get preferred CMM type in descriptive form.
     * @return Preferred CMM type in descriptive form
     */
    @ReportableProperty(order=3, value="Preferred CMM type in descriptive form.",
            ref="ICC Private and ICC Tag and CMM Registry",
            type=PropertyType.Descriptive)
    public String getPreferredCMM_descriptive() {
        return this.preferredCMM_d;
    }
    
    /** Get primary platform in raw form.
     * @return Primary platform in raw form
     */
    @ReportableProperty(order=14, value="Primary platform in raw form.",
            ref="ICC.1:2004-10, \u00a7 7.2.10", type=PropertyType.Raw)
    public String getPrimaryPlatform_raw() {
        if (this.primaryPlatform != null) {
            return this.primaryPlatform.toString();
        }
        
        return null;
    }
    
    /** Get primary platform in descriptive form.
     * @return Primary platform in descriptive form
     */
    @ReportableProperty(order=15, value="Primary platform in descriptive form.",
            ref="ICC.1:2004-10, \u00a7 7.2.10", type=PropertyType.Descriptive)
    public String getPrimaryPlatform_descriptive() {
        return this.primaryPlatform_d;
    }
    
    /** Get Profile Connection Space (PCS), i.e. "the canonical output space", in raw form.
     * @return Profile Connection Space in raw form
     */
    @ReportableProperty(order=9, value="Profile Connection Space (PCS), i.e. \"the canonical output space\", in raw form",
            ref="ICC.1:2004-10, \u00a7 7.2.7", type=PropertyType.Raw)
    public String getProfileConnectionSpace_raw() {
        return this.profileConnectionSpace.toString();
    }
    
    /** Get Profile Connection Space (PCS), i.e. "the canonical output space", in descriptive form.
     * @return Profile Connection Space in descriptive form
     */
    @ReportableProperty(order=10, value="Profile Connection Space (PCS), i.e. \"the canonical output space\", in descriptive form",
            ref="ICC.1:2004-10, \u00a7 7.2.7", type=PropertyType.Descriptive)
    public String getProfileConnectionSpace_descriptive() {
        return this.profileConnectionSpace_d;
    }
   
    /** Get profile creator.
     * @return Profile creator
     */
    @ReportableProperty(order=27, value="Profile creator.",
            ref="ICC.1:2004-10, \u00a7 7.2.17")
    public String getProfileCreator() {
        if (this.profileCreator != null) {
            return this.profileCreator.toString();
        }
        
        return null;
    }
    
    /** Get profile/device class in raw form.
     * @return Profile/device class
     */
    @ReportableProperty(order=5, value="Profile/device class in raw form.",
            ref="ICC.1:2004-10, \u00a7 7.2.5", type=PropertyType.Raw)
    public String getProfileDeviceClass_raw() {
        return this.profileDeviceClass.toString();
    }
    
    /** Get profile/device class in descriptive form.
     * @return Profile/device class
     */
    @ReportableProperty(order=6, value="Profile/device class in descriptive form.",
            ref="ICC.1:2004-10, \u00a7 7.2.5", type=PropertyType.Descriptive)
    public String getProfileDeviceClass_descriptive() {
        return this.profileDeviceClass_d;
    }
    
    /** Get profile file signature.
     * @return Profile file signature
     */
    @ReportableProperty(order=13, value="Profile file signature.",
            ref="ICC.1.2004-10, \u00a7 7.2.9")
    public String getProfileFileSignature() {
        return this.profileFileSignature.toString();
    }
    
    /** Get profile flags field in raw form.
     * @return Profile flags field in raw form
     */
    @ReportableProperty(order=16, value="Profile flags in raw form.",
            ref="ICC.1:2004-10, \u00a7 7.2.11", type=PropertyType.Raw)
    public String getProfileFlags_raw() {
        return String.format("0x%08x", this.profileFlags);
    }
    
    /** Get profile flags in descriptive form.
     * @return Profile flags in descriptive form
     */
    @ReportableProperty(order=17, value="Profile flags in descriptive form.",
            ref="ICC.1:2004-10, \u00a7 7.2.11", type=PropertyType.Descriptive)
    public List<String> getProfileFlags() {
        return this.profileFlags_d;
    }
    
    /** Get profile ID (MD5).
     * @return Profile ID
     */
    @ReportableProperty(order=28, value="Profile ID (MD5).",
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
    @ReportableProperty(order=4, value="Profile version number.",
            ref="ICC.1:2004-10, \u00a7 7.2.4")
    public String getProfileVersionNumber() {
        return this.profileVersionNumber.toString();
    }
 
    /** Get rendering intent in raw form.
     * @return Rendering intent in raw form
     */
    @ReportableProperty(order=23, value="Rendering intent in raw form.",
            ref="ICC.1:2004-10, \u00a7 7.2.15", type=PropertyType.Raw)
    public int getRenderingIntent_raw() {
        return this.renderingIntent;
    }

    /** Get rendering intent in descriptive form.
     * @return Rendering intent in descriptive form
     */
    @ReportableProperty(order=24, value="Rendering intent in descriptive form.",
            ref="ICC.1:2004-10, \u00a7 7.2.15", type=PropertyType.Descriptive)
    public String getRenderingIntent_descriptive() {
        return this.renderingIntent_d;
    }
    
    /** Get Profile Connection Space (PCS) illuminant status:
     * true if illuminant is D50 .
     * @return PCS illuminant status
     */
    @ReportableProperty(order=26,
            value="Profile Connection Space (PCS) status: true if illuminant is D50.")
    public boolean isD50Illuminant() {
        return this.isD50Illuminant;
    }
    
    /** Get DeviceLink profile status: true if a DeviceLink profile.
     * @return DeviceLink profile status
     */
    @ReportableProperty(order=11, value="DeviceLink profile status: true if a DeviceLink profile.")
    public boolean isDeviceLinkProfile() {
        return this.isDeviceLinkProfile;
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
    
    /**
     * @param offset the offset to set
     */
    public void setOffset(long offset) {
        this.offset = offset;
    }
}
