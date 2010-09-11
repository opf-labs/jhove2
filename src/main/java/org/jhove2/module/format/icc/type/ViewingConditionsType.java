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
 */

package org.jhove2.module.format.icc.type;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteOrder;

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
import org.jhove2.module.format.icc.field.StandardIlluminant;

/** ICC viewing conditions type, as defined in ICC.1:2004-10,
 * \u00a7 10.26.
 * 
 * @author slabram
 */
public class ViewingConditionsType
    extends AbstractReportable
    implements Parser
{
    /** Viewing conditions type signature. */
    public static final String SIGNATURE = "view";
    
    /** Illuminant type in raw form. */
    protected long illuminantType;
    
    /** Illuminant type in descriptive form. */
    protected String illuminantType_d;
    
    /** Validation status. */
    protected Validity isValid;
 
    /** Signature. */
    protected StringBuffer signature = new StringBuffer(4);   

    /** CIE "absolute" XYZ values for illuminant. */
    protected XYZNumber xyzForIlluminant;

    /** CIE "absolute" XYZ values for surround. */
    protected XYZNumber xyzForSurround;
 
    /** Invalid standard illuminant message.*/
    protected Message invalidStandardIlluminantMessage;
    
    /** Invalid tag type message. */
    protected Message invalidTagTypeMessage;

    /** Non-zero data in reserved field message. */
    protected Message nonZeroDataInReservedFieldMessage;
    
    /** Instantiate a new <code>ViewingConditionsType</code> element. */
    public ViewingConditionsType() {
        super();
        
        this.isValid = Validity.Undetermined;
    }
    
    /** Parse an ICC viewing conditions tag type.
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
        
        /* CIE "absolute" XYZ for illuminant. */
        int x = input.readSignedInt();
        int y = input.readSignedInt();
        int z = input.readSignedInt();
        this.xyzForIlluminant = new XYZNumber(x, y, z);
        consumed += 12;
        
        /* CIE "absolute" XYZ for surround. */
        x = input.readSignedInt();
        y = input.readSignedInt();
        z = input.readSignedInt();
        this.xyzForSurround = new XYZNumber(x, y, z);
        consumed += 12;
        
        /** Illuminant type. */
        this.illuminantType = input.readUnsignedInt();
        StandardIlluminant type =
            StandardIlluminant.getStandardIlluminant(this.illuminantType,
                                                     jhove2);
        if (type != null) {
            this.illuminantType_d = type.getIlluminant();
        }
        else {
            this.isValid = Validity.False;
            Object [] args = new Object [] {input.getPosition()-4L, this.illuminantType};
            this.invalidStandardIlluminantMessage = new Message(Severity.ERROR,
                    Context.OBJECT,
                    "org.jhove2.module.format.icc.ICCTag.InvalidStandardIlluminant",
                    args, jhove2.getConfigInfo());
        }
        consumed += 4;
        
        return consumed;
    }

    /** Get illuminant type in descriptive form.
     * @return Illuminant type
     */
    @ReportableProperty(order=4, value="Illuminant type in descriptive form.",
            ref="ICC.1:2004-10, \ua077 10.26")
    public String getIlluminantType_descriptive() {
        return this.illuminantType_d;
    }
    
    /** Get illuminant type in raw form.
     * @return Illuminant type
     */
    @ReportableProperty(order=3, value="Illuminant type in raw form.",
            ref="ICC.1:2004-10, \ua077 10.26")
    public long getIlluminantType_raw() {
        return this.illuminantType;
    }

    /** Get invalid standard illuminant message.
     * @return Invalid standard illuminant message
     */
    @ReportableProperty(order=13, value="Invalid standard illuminant.")
    public Message getInvalidStandardIlluminantMessage() {
        return this.invalidStandardIlluminantMessage;
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
            ref="ICC.1:2004-10, \ua077 10.26")
    public Message getNonZeroDataInReservedFieldMessage() {
        return this.nonZeroDataInReservedFieldMessage;
    }

    /** Get CIE "absolute" XYZ values for illuminant.
     * @return CIE XYZ values for illuminant
     */
    @ReportableProperty(order=1, value="CIE 'absolute' XYZ values for lluminant.",
            ref="ICC.1:2004-10, \ua077 10.26")
    public XYZNumber getXYZForIlluminant() {
        return this.xyzForIlluminant;
    }

    /** Get CIE "absolute" XYZ values for surround.
     * @return CIE XYZ values for surround
     */
    @ReportableProperty(order=2, value="CIE 'absolute' XYZ values for lluminant.",
            ref="ICC.1:2004-10, \ua077 10.26")
    public XYZNumber getXYZForSurround() {
        return this.xyzForSurround;
    }
    
    /** Get validation status.
     * @return Validation status
     */
    @ReportableProperty(order=5, value="Validation status.",
            ref="ICC.1:2004-10, \u00a7 10.26")
    public Validity isValid() {
        return this.isValid;
    }
}
