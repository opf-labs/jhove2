/**
 * JHOVE2 - Next-generation architecture for format-aware characterization
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
import org.jhove2.core.source.MeasurableSource;
import org.jhove2.core.source.Source;
import org.jhove2.module.format.Parser;
import org.jhove2.module.format.Validator.Validity;

import com.sleepycat.persist.model.Persistent;

/** ICC response curve set type element, as define in ICC.1:2004-10,
 * \u00a7 10.17.
 * 
 * @author slabrams
 */
@Persistent
public class ResponseCurveSet16Type
    extends AbstractReportable
    implements Parser
{
    /** Type signature. */
    public static final String SIGNATURE = "rcs2";
 
    /** Count of measurement types.. */
    protected int count;
    
    /** Offsets to measurement types. */
    protected List<Long> offsets;
    
    /** Validation status. */
    protected Validity isValid;
    
    /** Number of channels. */
    protected int numChannels;
   
    /** Signature (of tag type). */
    protected StringBuffer signature = new StringBuffer(4);   
 
    /** Invalid tag type message. */
    protected Message invalidTagTypeMessage;
    
    /** Non-zero data in reserved field message. */
    protected Message nonZeroDataInReservedFieldMessage;
    
    /** Instantiate a new <code>ResponseCurveSet16Type</code>. */
    public ResponseCurveSet16Type() {
        super();
        
        this.isValid = Validity.Undetermined;
        this.offsets = new ArrayList<Long>();
    }
    
    /** Parse an ICC signature tag type element.
     * @param jhove2 JHOVE2 framework
     * @param source ICC source unit
     * @param input  ICC source input
     * @return Number of bytes consumed
     * @throws EOFException
     *             If End-of-File is reached reading the source unit
     * @throws IOException
     *             If an I/O exception is raised reading the source unit
     * @throws JHOVE2Exception
     */
    @Override
    public long parse(JHOVE2 jhove2, Source source, Input input)
        throws EOFException, IOException, JHOVE2Exception
    {
        long consumed  = 0L;
        int  numErrors = 0;
        this.isValid   = Validity.True;
        long start     = ((MeasurableSource) source).getStartingOffset();
  
        /* Tag signature. */
        for (int i=0; i<4; i++) {
            short b = input.readUnsignedByte();
            this.signature.append((char) b);
        }
        if (!this.signature.toString().equals(SIGNATURE)) {
            numErrors++;
            this.isValid = Validity.False;
            Object [] args =
                new Object [] {input.getPosition()-4L-start, SIGNATURE,
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
            Object [] args = new Object [] {input.getPosition()-4L-start};
            this.nonZeroDataInReservedFieldMessage = new Message(Severity.ERROR,
                    Context.OBJECT,
                    "org.jhove2.module.format.icc.ICCTag.NonZeroDataInReservedField",
                    args, jhove2.getConfigInfo());
        }
        consumed += 4;
        
        /** Number of channels. */
        this.numChannels = input.readUnsignedShort();
        consumed += 2;
        
        /** Count of measurement types. */
        this.count = input.readUnsignedShort();
        consumed += 2;
        
        /** Offset to measurement types. */
        for (int i=0; i<this.count; i++) {
            long in = input.readUnsignedInt();
            this.offsets.add(in);
            consumed += 4;
        }
             
        return consumed;
    }
    
    /** Get count of measurement types.
     * @return Count of measurement types
     */
    @ReportableProperty(order=2, value="Count of measurement types.",
            ref="ICC.1:2004-10, Table 50")
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
    
    /** Get number of channels.
     * @return Number of channels
     */
    @ReportableProperty(order=2, value="Number of channels.",
            ref="ICC.1:2004-10, Table 50")
    public long getNumberOfChannels() {
        return this.numChannels;
    }
    
    /** Get count of descriptions.
     * @return Count of descriptions
     */
    @ReportableProperty(order=4, value="Count of profile descriptions.",
            ref="ICC.1:2004-10, Table 50")
    public List<Long> getOffsets() {
        return this.offsets;
    }
    
    /** Get type signature.
     * @return Type signature
     */
    @ReportableProperty(order=1, value="Type signature.",
            ref="ICC.1:2004-10, \u00a6 10.17")
    public String getSignature() {
        return this.signature.toString();
    }
     
    /** Get validation status.
     * @return Validation status
     */
    @ReportableProperty(order=5, value="Validation status.",
            ref="ICC.1:2004-10, \u00a7 10.17")
    public Validity isValid() {
        return this.isValid;
    }
}
