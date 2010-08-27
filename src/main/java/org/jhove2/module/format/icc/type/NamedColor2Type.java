/**
 * HOVE2 - Next-generation architecture for format-aware characterization
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
import org.jhove2.module.format.Validator.Validity;

/** ICC named color 2 type element, as defined in ICC.1:2004-10, 
 * \u00a7 10.14.
 * 
 * @author slabrams
 */
public class NamedColor2Type
        extends AbstractReportable
{
    /** Type signature. */
    public static final String SIGNATURE = "ncl2";
    
    /** Named colours. */
    protected List<NamedColour> colours;

    /** Count of named colours. */
    protected long count;
    
    /** Vendor specific flag. */
    protected long flag;
    
    /** Validation status. */
    protected Validity isValid;
    
    /** Number of device channels. */
    protected long numCoordinates;
    
    /** Colour name prefix. */
    protected StringBuffer prefix = new StringBuffer(32);
 
    /** Signature (of tag type). */
    protected StringBuffer signature = new StringBuffer(4);   
    
    /** Colour name suffix. */
    protected StringBuffer suffix = new StringBuffer(32);
   
    /** Invalid tag type message. */
    protected Message invalidTagTypeMessage;
    
    /** Non-zero data in reserved field message. */
    protected Message nonZeroDataInReservedFieldMessage;
    
    /** Instantiate a new <code>ResponseCurveSet16Type</code>. */
    public NamedColor2Type() {
        super();
        
        this.isValid = Validity.Undetermined;
        this.colours = new ArrayList<NamedColour>();
    }
    
    /** Parse an ICC signature tag type element.
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
        this.isValid   = Validity.True;
  
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
        
        /** Vendor specific flag. */
        this.flag = input.readUnsignedInt();
        consumed += 2;
        
        /** Count of named colours. */
        this.count = input.readUnsignedInt();
        consumed += 4;
        
        /** Number of device coordinates. */
        this.numCoordinates = input.readUnsignedInt();
        consumed += 4;
        
        /** Colour name prefix. */
        for (int i=0; i<32; i++) {
            short ch = input.readUnsignedByte();
            if (ch != 0) {
                this.prefix.append((char) ch);
            }
        }
        consumed += 32;
        
        /** Colour name suffix. */
        for (int i=0; i<32; i++) {
            short ch = input.readUnsignedByte();
            if (ch != 0) {
                this.suffix.append((char) ch);
            }
        }
        consumed += 32;
 
        /** Named colours. */
        long m = this.numCoordinates*2;
        for (int i=0; i<this.count; i++) {
            /* Colour root name. */
            StringBuffer root = new StringBuffer(32);
            for (int j=0; j<32; j++) {
                short ch = input.readUnsignedByte();
                if (ch != 0) {
                    root.append((char) ch);
                }
            }
            consumed += 32;
            
            /* PCS coordinations. */
            List<Integer> pcsCoords = new ArrayList<Integer>();
            for (int j=0; j<3; j++) {
                pcsCoords.add(input.readUnsignedShort());
            }
            consumed += 6;
            
            /* Device coordinates. */
            List<Integer> devCoords = new ArrayList<Integer>();
            for (long j=0; j<m; j++) {
                devCoords.add(input.readUnsignedShort());
            }
            consumed += m;
            
            NamedColour colour = new NamedColour(root.toString(), pcsCoords,
                                                 devCoords);
            this.colours.add(colour);
        }
             
        return consumed;
    }
    
    /** Get colour prefix.
     * @return Colour prefix 
     */
    @ReportableProperty(order=5, value="Colour prefix.",
            ref="ICC.1:2004-10, Table 45")
    public String getColourPrefix() {
        return this.prefix.toString();
    }
    
    /** Get colour suffix.
     * @return Colour suffix 
     */
    @ReportableProperty(order=5, value="Colour suffix.",
            ref="ICC.1:2004-10, Table 45")
    public String getColourSuffix() {
        return this.suffix.toString();
    }
    
    /** Get count of named colours.
     * @return Count of named colours
     */
    @ReportableProperty(order=3, value="Count of named colours.",
            ref="ICC.1:2004-10, Table 45")
    public long getCount() {
        return this.count;
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
    
    /** Get number of device coordinates per colour.
     * @return Number of device coordinates 
     */
    @ReportableProperty(order=4, value="Number of device coordinates per colour.",
            ref="ICC.1:2004-10, Table 45")
    public long getNumberOfDeviceCoordinates() {
        return this.numCoordinates;
    }
    
    /** Get named colours.
     * @return Named colours
     */
    @ReportableProperty(order=7, value="Named colours.",
            ref="ICC.1:2004-10, Table 45")
    public List<NamedColour> getNamedColours() {
        return this.colours;
    }
    
    /** Get type signature.
     * @return Type signature
     */
    @ReportableProperty(order=1, value="Type signature.",
            ref="ICC.1:2004-10, \u00a6 10.14")
    public String getSignature() {
        return this.signature.toString();
    }
     
    /** Get validation status.
     * @return Validation status
     */
    @ReportableProperty(order=5, value="Validation status.",
            ref="ICC.1:2004-10, \u00a7 10.14")
    public Validity isValid() {
        return this.isValid;
    }
}
