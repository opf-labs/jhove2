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

package org.jhove2.module.format.wave.bwf;

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
import org.jhove2.core.source.MensurableSource;
import org.jhove2.core.source.Source;
import org.jhove2.module.format.Validator.Validity;
import org.jhove2.module.format.riff.GenericChunk;

import com.sleepycat.persist.model.Persistent;

/** Broadcast Wave Format (BWF) broadcast audio extension chunk.
 * 
 * @author slabrams
 */
@Persistent
public class BroadcastAudioExtensionChunk
    extends GenericChunk
{
    /** Coding history. */
    protected String codingHistory;
    
    /** Description. */
    protected String description;
    
    /** Origination date. */
    protected String originationDate;
    
    /** Origination time. */
    protected String originationTime;
       
    /** Originator. */
    protected String originator;
  
    /** Originator reference. */
    protected String originatorReference;
   
    /** Time reference high. */
    protected long timeReferenceHigh;
    
    /** Time reference low. */
    protected long timeReferenceLow;
    
    /** SMPTE UMID in hexadecimal. */
    protected String umid;
    
    /** BWF version. */
    protected int version;
    
    /** Non-NUL data in reserved field messages. */
    protected List<Message> nonNULDataInReservedFieldMessages;
    
    /** Instantiate a new <code>BroadcastAudioExtensionChunk</code>. */
    public BroadcastAudioExtensionChunk() {
        super();
        
        this.nonNULDataInReservedFieldMessages = new ArrayList<Message>();
    }
    
    /** 
     * Parse a WAVE chunk.
     * 
     * @param jhove2
     *            JHOVE2 framework
     * @param source
     *            WAVE source unit
     * @param input  WAVE source input
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
        long consumed = super.parse(jhove2, source, input);
        long start    = ((MensurableSource) source).getStartingOffset();
        int numErrors = 0;
        
        /* Description. */
        StringBuffer sb = new StringBuffer(256);
        for (int i=0; i<256; i++) {
            short b = input.readUnsignedByte();
            if (b != 0) {
                sb.append((char) b);
            }
        }
        this.description = sb.toString();
        consumed += 256;
        
        /* Originator. */
        sb = new StringBuffer(32);
        for (int i=0; i<32; i++) {
            short b = input.readUnsignedByte();
            if (b != 0) {
                sb.append((char) b);
            }
        }
        this.originator = sb.toString();
        consumed += 32;
        
        /* Originator reference. */
        sb = new StringBuffer(32);
        for (int i=0; i<32; i++) {
            short b = input.readUnsignedByte();
            if (b != 0) {
                sb.append((char) b);
            }
        }
        this.originatorReference = sb.toString();
        consumed += 32;
        
        /* Origination date. */
        sb = new StringBuffer(10);
        for (int i=0; i<10; i++) {
            short b = input.readUnsignedByte();
            sb.append((char) b);
        }
        this.originationDate = sb.toString();
        consumed += 10;
        
        /* Origination time. */
        sb = new StringBuffer(8);
        for (int i=0; i<8; i++) {
            short b = input.readUnsignedByte();
            sb.append((char) b);
        }
        this.originationTime = sb.toString();
        consumed += 8;
        
        /* Time reference low. */
        this.timeReferenceLow = input.readUnsignedInt();
        consumed += 4;
        
        /* Time reference high. */
        this.timeReferenceHigh = input.readUnsignedInt();
        consumed += 4;
        
        /* BWF version. */
        this.version = input.readUnsignedShort();
        consumed += 2;
        
        /* SMPTE UMID. */
        sb = new StringBuffer(128);
        for (int i=0; i<64; i++) {
            short b = input.readUnsignedByte();
            sb.append(String.format("%02x", b));
        }
        this.umid = sb.toString();
        consumed += 64;
        
        /* Reserved, must be NUL. */
        for (int i=0; i<190; i++) {
            short b = input.readUnsignedByte();
            if (b != 0) {
                numErrors++;
                this.isValid = Validity.False;
                Object [] args = new Object [] {input.getPosition()-1L-start, b};
                Message msg = new Message(Severity.ERROR, Context.OBJECT,
                        "org.jhove2.module.format.wave.bwf.BroadcastAudioExtensionChunk.nonNULDataInReservedField",
                        args, jhove2.getConfigInfo());
                this.nonNULDataInReservedFieldMessages.add(msg);
            }
        }
        consumed += 190;
        
        /* Coding history. */
        long len = this.size - 602L;
        sb = new StringBuffer((int) len);
        for (long i=0; i<len; i++) {
            short b = input.readUnsignedByte();
            if (b != 0) {
                sb.append((char) b);
            }
            else {
                break;
            }
        }
        this.codingHistory = sb.toString();
        consumed += len;
        
        return consumed;
    }
   
    /** Get coding history.
     * @return Coding history
     */
    @ReportableProperty(order=10, value="Coding history.")
    public String getCodingHistory() {
        return this.codingHistory;
    }
    
    /** Get description. 
     * @return Description
     */
    @ReportableProperty(order=1, value="Description.")
    public String getDescription() {
        return this.description;
    }
    
    /** Get non-NUL data in reserved field messages.
     * @return Non-NUL data in reserved field messages
     */
    @ReportableProperty(order=21, value="Non-NUL data in reserved field messages.")
    public List<Message> getNonNULDataInReservedFieldMessages() {
        return this.nonNULDataInReservedFieldMessages;
    }
    
    /** Get origination date.
     * @return origination date
     */
    @ReportableProperty(order=4, value="Origination date.")
    public String getOriginationDate() {
        return this.originationDate;
    }
    
    /** Get origination time.
     * @return Origination time
     */
    @ReportableProperty(order=5, value="Origination time.")
    public String getOriginationTime() {
        return this.originationTime;
    }
    
    /** Get originator. 
     * @return Originator
     */
    @ReportableProperty(order=2, value="Originator.")
    public String getOriginator() {
        return this.originator;
    }
    
    /** Get originator reference.
     * @return Originator reference
     */
    @ReportableProperty(order=3, value="Originator reference.")
    public String getOriginatorReference() {
        return this.originatorReference;
    }
    
    /** Get time reference high.
     * @return Time reference high
     */
    @ReportableProperty(order=7, value="First sample count since midnight, high word.")
    public long getTimeReferenceHigh() {
        return this.timeReferenceHigh;
    }
    
    /** Get time reference low.
     * @return time reference low
     */
    @ReportableProperty(order=6, value="First sample count since midnight, low word.")
    public long getTimeReferenceLow() {
        return this.timeReferenceLow;
    }
    
    /** Get SMPTE UMID, in hexadecimal.
     * @return SMPTE UMID
     */
    @ReportableProperty(order=9, value="SMPTE UMID.")
    public String getSMPTEUMID() {
        return this.umid;
    }
    
    /** Get BWF version.
     * @return BWF version
     */
    @ReportableProperty(order=8, value="BWF version.")
    public int getBWFVersion() {
        return this.version;
    }
}
