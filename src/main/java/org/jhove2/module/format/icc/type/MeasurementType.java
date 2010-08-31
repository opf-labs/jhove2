/**
 * HOVE2 - Next-generation architecture for format-aware characterization
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

/** ICC measurement type element, as defined in ICC.1:2004-10, \u00a7 10.12.
 * 
 * @author slabrams
 */
public class MeasurementType
        extends AbstractReportable
{
    /** Measurement type signature. */
    public static final String SIGNATURE = "meas";
    
    /** Measurement flare in raw form. */
    protected long measurementFlare;
    
    /** Measurement flare in descriptive form. */
    protected String measurementFlare_d;
    
    /** Measurement geometry in raw form. */
    protected long measurementGeometry;
    
    /** Measurement geomtry in descriptive form. */
    protected String measurementGeometry_d;
    
    /** Validation status. */
    protected Validity isValid;
 
    /** Signature. */
    protected StringBuffer signature = new StringBuffer(4);   

    /** Standard illuminant in raw form. */
    protected long standardIlluminant;
    
    /** Standard illuminant in descriptive form. */
    protected String standardIlluminant_d;

    /** Standard observer in raw form. */
    protected long standardObserver;
    
    /** Standard observer in descriptive form. */
    protected String standardObserver_d;
    
    /** XYZ tristimilus values for measurement backing. */
    protected XYZNumber xyzForMeasurement;
 
    /** Invalid tag type message. */
    protected Message invalidTagTypeMessage;

    /** Non-zero data in reserved field message. */
    protected Message nonZeroDataInReservedFieldMessage;
    
    /** Instantiate a new <code>MeasurementsType</code> element. */
    public MeasurementType() {
        super();
        
        this.isValid = Validity.Undetermined;
    }
    
    /** Parse an ICC measurement tag type.
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
        String signature = this.signature.toString();
        if (!signature.equals(SIGNATURE)) {
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
        
        /** Standard observer. */
        this.standardObserver = input.readUnsignedInt();
        consumed += 4;
        
        /* XYZ tristimulus values for measurement backing. */
        int x = input.readSignedInt();
        int y = input.readSignedInt();
        int z = input.readSignedInt();
        this.xyzForMeasurement = new XYZNumber(x, y, z);
        consumed += 12;
         
        /** Measurement geometry. */
        this.measurementGeometry = input.readUnsignedInt();
        consumed += 4;
        
        /** Measurement flare. */
        this.measurementFlare = input.readUnsignedInt();
        consumed += 4;
        
        /** Standard illuminant. */
        this.standardIlluminant = input.readUnsignedInt();
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

    /** Get measurement flare in descriptive form.
     * @return Measurement flare
     */
    @ReportableProperty(order=5, value="Measurement flare in descriptive form.",
            ref="ICC.1:2004-10, \ua077 10.12")
    public String getMeasurementFlare_descriptive() {
        return this.measurementFlare_d;
    }
    
    /** Get measurement flare in raw form.
     * @return Measurement flare
     */
    @ReportableProperty(order=4, value="Measurement flare in raw form.",
            ref="ICC.1:2004-10, \ua077 10.12")
    public long getMeasurementFlare_raw() {
        return this.measurementFlare;
    }

    /** Get measurement geometry in descriptive form.
     * @return Measurement geometry
     */
    @ReportableProperty(order=7, value="Measurement geomtry in descriptive form.",
            ref="ICC.1:2004-10, \ua077 10.12")
    public String getMeasurementGeometry_descriptive() {
        return this.measurementGeometry_d;
    }
    
    /** Get measurement geometry in raw form.
     * @return Measurement geometry
     */
    @ReportableProperty(order=6, value="Measurement geometry in raw form.",
            ref="ICC.1:2004-10, \ua077 10.12")
    public long getMeasurementGeomtry_raw() {
        return this.measurementGeometry;
    }
    /** Get non-zero data in reserved field message.
     * @return Non-zero data in reserved field message
     */
    @ReportableProperty(order=12, value="Non-zero data in reserved field.",
            ref="ICC.1:2004-10, \ua077 10.12")
    public Message getNonZeroDataInReservedFieldMessage() {
        return this.nonZeroDataInReservedFieldMessage;
    }

    /** Get standard illuminant in descriptive form.
     * @return Standard illuminant
     */
    @ReportableProperty(order=9, value="Standard illuminant in descriptive form.",
            ref="ICC.1:2004-10, \ua077 10.12")
    public String getStandardIlluminant_descriptive() {
        return this.standardIlluminant_d;
    }
    
    /** Get standard illuminant type in raw form.
     * @return Illuminant type
     */
    @ReportableProperty(order=8, value="Standard illuminant in raw form.",
            ref="ICC.1:2004-10, \ua077 10.12")
    public long getStandardIlluminant_raw() {
        return this.standardIlluminant;
    }

    /** Get standard observer in descriptive form.
     * @return Standard observer
     */
    @ReportableProperty(order=2, value="Standard observer in descriptive form.",
            ref="ICC.1:2004-10, \ua077 10.12")
    public String getStandardObserver_descriptive() {
        return this.standardObserver_d;
    }
    
    /** Get standard observer in raw form.
     * @return Standard observer
     */
    @ReportableProperty(order=1, value="Standard observer in raw form.",
            ref="ICC.1:2004-10, \ua077 10.12")
    public long getStandardObserver_raw() {
        return this.standardObserver;
    }
    
    /** XYZ tristimulus values for measurement backing.
     * @return XYZ values for measurement backing
     */
    @ReportableProperty(order=3, value="XYZ tristimulus values for measurement backing.",
            ref="ICC.1:2004-10, \ua077 10.12")
    public XYZNumber getXYZForMeasurementBacking() {
        return this.xyzForMeasurement;
    }

    /** Get validation status.
     * @return Validation status
     */
    @ReportableProperty(order=10, value="Validation status.",
            ref="ICC.1:2004-10, \u00a7 10.12")
    public Validity isValid() {
        return this.isValid;
    }
}
