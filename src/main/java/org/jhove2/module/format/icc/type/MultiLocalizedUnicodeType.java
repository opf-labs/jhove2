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

package org.jhove2.module.format.icc.type;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteOrder;
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
import org.jhove2.core.source.Source;
import org.jhove2.module.format.Parser;
import org.jhove2.module.format.Validator.Validity;

/** ICC multi-localized Unicode type, as defined in ICC.1:2004-10, \u00a7 10.13.
 * 
 * @author slabrams
 */
public class MultiLocalizedUnicodeType
    extends AbstractReportable
    implements Parser
{
    /** Recommended name record size, per ICC.1:2004-10, \u00a7 10.13. */
    public static final int RECOMMENDED_NAME_RECORD_SIZE = 12;

    /** Multi-localized Unicode type signature. */
    public static final String SIGNATURE = "mluc";
    
    /** Validation status. */
    protected Validity isValid;
    
    /** Name record size. */
    protected long nameRecordSize;
    
    /** Number of names. */
    protected long numberOfNames;

    /** Signature. */
    protected StringBuffer signature = new StringBuffer(4);   
    
    /** Invalid tag type message. */
    protected Message invalidTagTypeMessage;

    /** Name records. */
    protected List<NameRecord> nameRecords;
    
    /** Name record size not 12 message. */
    protected Message nameRecordSizeNot12Message;
    
    /** Non-zero data in reserved field message. */
    protected Message nonZeroDataInReservedFieldMessage;
    
    /** Instantiate a new <code>MultiLocalizedUnicodeType</code>. */
    public MultiLocalizedUnicodeType() {
        super();
        
        this.isValid     = Validity.Undetermined;
        this.nameRecords = new ArrayList<NameRecord>();
    }
    
    /** Parse an ICC multi-localized Unicode tag type.
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
        long position  = input.getPosition();
  
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
        
        /* Number of names. */
        this.numberOfNames = input.readUnsignedInt();
        consumed += 4;
        
        /* Name record size. */
        this.nameRecordSize = input.readUnsignedInt();
        if (this.nameRecordSize != RECOMMENDED_NAME_RECORD_SIZE) {
            Object [] args = new Object [] {input.getPosition()-4L, this.nameRecordSize};
            this.nameRecordSizeNot12Message = new Message(Severity.WARNING,
                    Context.OBJECT,
                    "org.jhove2.module.format.icc.type.MultiLocalizedUnicodeType.NameRecordSizeNot12",
                    args, jhove2.getConfigInfo());
        }
        consumed += 4;
        
        for (int i=0; i<this.numberOfNames; i++) {
            NameRecord record = new NameRecord();
            consumed += record.parse(jhove2, source);
            
            long length = record.getLength()/2L; /* Length in bytes, not 16-bit characters. */
            long offset = record.getOffset();
            StringBuffer name = new StringBuffer();
            input.setPosition(position + offset);
            for (int j=0; j<length; j++) {
                name.append(input.readChar());
            }
            record.setName(name.toString());
            
            this.nameRecords.add(record);
        }
         
        return consumed;
    }
    
    /** Get invalid tag type message.
     * @return Invalid tag type message
     */
    @ReportableProperty(order=11, value="Invalid tag type.")
    public Message getInvalidTagTypeMessage() {
        return this.invalidTagTypeMessage;
    }

    /** Get name records.
     * @return Name records
     */
    @ReportableProperty(order=4, value="Name records.",
            ref="ICC.1:2004-10, Table 44")
    public List<NameRecord> getNameRecords() {
        return this.nameRecords;
    }
    
    /** Get name record size.
     * @return Name record size
     */
    @ReportableProperty(order=3, value="Name record size.",
            ref="ICC.1:2004-10, Table 44")
    public long getNameRecordSize() {
        return this.nameRecordSize;
    }
    
    /** Get non-zero data in reserved field message.
     * @return Non-zero data in reserved field message
     */
    @ReportableProperty(order=12, value="Non-zero data in reserved field.")
    public Message getNonZeroDataInReservedFieldMessage() {
        return this.nonZeroDataInReservedFieldMessage;
    }
    /** Get number of names.
     * @return Number of names
     */
    @ReportableProperty(order=2, value="Number of names.",
            ref="ICC.1:2004-10, Table 44")
    public long getNumberOfNames() {
        return this.numberOfNames;
    }
    
    /** Get signature.
     * @return Signature.
     */
    @ReportableProperty(order=1, value="Signature.",
            ref="ICC.1:2004-10, Table 44")
    public String getSignature() {
        return this.signature.toString();
    }
    
    /** Get validation status.
     * @return Validation status
     */
    @ReportableProperty(order=5, value="Validation status.",
            ref="ICC.1:2004-10, \u00a7 10")
    public Validity isValid() {
        return this.isValid;
    }
}
