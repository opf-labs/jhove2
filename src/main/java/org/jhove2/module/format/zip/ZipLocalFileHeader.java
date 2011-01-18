/**
 * JHOVE2 - Next-generation architecture for format-aware characterization
 *
 * Copyright (c) 2009 by The Regents of the University of California,
 * Ithaka Harbors, Inc., and The Board of Trustees of the Leland Stanford
 * Junior University.
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

package org.jhove2.module.format.zip;

import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.Digest;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.Message;
import org.jhove2.core.Message.Context;
import org.jhove2.core.Message.Severity;
import org.jhove2.core.io.Input;
import org.jhove2.core.reportable.AbstractReportable;
import org.jhove2.module.format.Validator.Validity;
import org.jhove2.module.format.zip.field.BitFlag;
import org.jhove2.module.format.zip.field.Compatibility;
import org.jhove2.module.format.zip.field.CompressionMethod;
import org.jhove2.module.format.zip.field.Version;

import com.sleepycat.persist.model.Persistent;

/** Zip local file header.
 * 
 * @author slabrams
 */
@Persistent
public class ZipLocalFileHeader
        extends AbstractReportable
{
    /** Platform compatibility in raw form. */
    protected short compatibility;
    
    /** Platform compatibility in descriptive format. */
    protected String compatibility_d;
    
    /** Compressed size of the file, in bytes. */
    protected long compressed;
 
    /** CRC-32 digest of the file. */
    protected Digest crc32;
    
    /** Extra field length, in bytes. */
    protected int extraFieldLength;
    
    /** File name. */
    protected String fileName;
    
    /** File name length, in bytes. */
    protected int fileNameLength;
    
    /** General purpose bit flag in raw form. */
    protected int flag;
    
    /** General purpose bit flag in descriptive form. */
    protected List<String> flag_d;
    
    /** Validation status. */
    protected Validity isValid;
    
    /** Last modification date/time. */
    protected Date lastModified;
    
    /** Compression method in raw form. */
    protected int method;
    
    /** Compression method in descriptive form. */
    protected String method_d;
   
    /** Optional descriptor signature, in hexadecimal. */
    protected String signature;
    
    /** Uncompressed size of the file, in bytes. */
    protected long uncompressed;
    
    /** Version needed to extract in raw form. */
    protected short version;
    
    /** Version needed to extract in descriptive form. */
    protected String version_d;
    
    /** Invalid compression method message. */
    protected Message invalidCompressionMessage;
    
    /** Invalid version message. */
    protected Message invalidVersionMessage;
  
    /** Instantiate a new <code>ZipLocalFileHeader</code>. */
    public ZipLocalFileHeader() {
        super();
        
        this.flag_d  = new ArrayList<String>();
        this.isValid = Validity.Undetermined;
    }
    
    /** 
     * Parse a Zip local file header.
     * @param jhove2
     *            JHOVE2 framework
     * @param input
     *            Zip input
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
        
        int sig = input.readSignedInt();
        this.signature = String.format("0x%04x", sig);
        consumed += 4;
        
        /* Version needed to extract. */
        int v = input.readUnsignedShort();
        this.compatibility = (short)(v >> 8);
        Compatibility comp =
                Compatibility.getCompatibility(this.compatibility, jhove2);
        if (comp != null) {
            this.compatibility_d = comp.getPlatform();
        }
        else {
            this.compatibility_d = "unused";
        }
        this.version = (short)(v & 0x000000ff);
        Version ver = Version.getVersion(this.version, jhove2);
        if (ver != null) {
            this.version_d = ver.getFeature();
        }
        else {
            numErrors++;
            this.isValid = Validity.False;
            Object [] args = new Object [] {input.getPosition()-1L, this.version};
            this.invalidVersionMessage = new Message(Severity.ERROR,
                    Context.OBJECT,
                    "org.jhove2.module.format.zip.ZipLocalFileHeader.invalidVersion",
                    args, jhove2.getConfigInfo());
        }
        consumed += 2;
        
        /** General purpose bit flag. Need to read the compression method
         * before expanding the flag into its descriptive components.
         */
        this.flag = input.readUnsignedShort();
        consumed += 2;
        
        /** Compression method. */
        this.method = input.readUnsignedShort();
        CompressionMethod met =
            CompressionMethod.getCompressionMethod(this.method, jhove2);
        if (met != null) {
           this.method_d = met.getDescription(); 
        }
        else {
            numErrors++;
            this.isValid = Validity.False;
            Object [] args = new Object [] {input.getPosition()-2L, this.method};
            this.invalidCompressionMessage = new Message(Severity.ERROR,
                    Context.OBJECT,
                    "org.jhove2.module.format.zip.ZipLocalFileHeader.invalidCompression",
                    args, jhove2.getConfigInfo());
        }
        consumed += 2;
        
        /** Bit flag descriptive components. */
        Set<BitFlag> flags = BitFlag.getBitFlags(jhove2);
        Iterator<BitFlag> pfIter = flags.iterator();
        while (pfIter.hasNext()) {
            BitFlag flag = pfIter.next();
            int bitPosition = flag.getPosition();
            int mask = 1 << bitPosition;
            if ((this.flag & mask) == 0L) {
                this.flag_d.add(flag.getNegativeValue());
            }
            else {
                this.flag_d.add(flag.getPositiveValue());
            }
        }
        if (this.method == 6) {
            if ((this.flag & 0x02) == 0) {
                this.flag_d.add(1, "4K sliding dictionary");
            }
            else {
                this.flag_d.add(1, "8K sliding dictionary");
            }
            if ((this.flag & 0x04) == 0) {
                this.flag_d.add(2, "2 Shannon-Fano trees used");
            }
            else {
                this.flag_d.add(2, "3 Shannon-Fano trees used");
            }
        }
        else if (this.method == 8 || this.method == 9) {
            int opt = (this.flag & 0x06) >> 1;
            if (opt == 0) {
                this.flag_d.add(1, "Normal (-en) compression option");
            }
            else if (opt == 1) {
                this.flag_d.add(1, "Maximum (-exx/-ex) compression option");
            }
            else if (opt == 2) {
                this.flag_d.add(1, "Fast (-ef) compression option");
            }
            else if (opt == 3) {
                this.flag_d.add(1, "Super Fast (-es) compression option ");
            }
        }
        else if (this.method == 14) {
            if ((this.flag & 0x02) == 0) {
                this.flag_d.add(1, "No end-of-stream (EOS) markers");
            }
            else {
                this.flag_d.add(1, "End-of-stream (EOS) markers used");
            }
        }
        
        /** Last modified date/time. */
        int time = input.readUnsignedShort();
        int date = input.readUnsignedShort();
        int yy = ((date & 0xfe00) >>  9) + 1980;
        int mo = ((date & 0x01e0) >>  5) -    1;
        int dd =   date & 0x001f;
        int hh =  (time & 0xf800) >> 11;
        int mm =  (time & 0x07e0) >>  5;
        int ss =  (time & 0x001f)  *  2;
        Calendar cal = new GregorianCalendar(yy, mo, dd, hh, mm, ss);
        this.lastModified = cal.getTime();
        consumed += 4;
        
        /** CRC-32 digest. */
        long crc = input.readUnsignedInt();
        this.crc32 = new Digest(String.format("%08x", crc), "CRC-32");
        consumed += 4;
        
        /** Compressed file size. */
        this.compressed = input.readUnsignedInt();
        consumed += 4;
        
        /** Uncompressed file size. */
        this.uncompressed = input.readUnsignedInt();
        consumed += 4;
        
        /** File name length. */
        this.fileNameLength = input.readUnsignedShort();
        consumed += 2;
        
        /** Extra field length. */
        this.extraFieldLength = input.readUnsignedShort();
        consumed += 2;
        
        /** File name. */
        StringBuffer sb = new StringBuffer(this.fileNameLength);
        for (int i=0; i<this.fileNameLength; i++) {
            short b = input.readUnsignedByte();
            sb.append((char) b);
        }
        this.fileName = sb.toString();
        consumed += this.fileNameLength;
        
        /** Extra field. */
        for (int i=0; i<this.extraFieldLength; i++) {
            input.readUnsignedByte();
        }
        consumed += this.extraFieldLength;
        
        return consumed;
    }
    
    /** Get platform compatibility in descriptive form.
     * @return Platform compatibility
     */
    @ReportableProperty(order=3, value="Platform compatibility in descriptive form.")
    public String getCompatibility_descriptive() {
        return this.compatibility_d;
    }
    
    /** Get platform compatibility in raw form.
     * @return Platform compatibility
     */
    @ReportableProperty(order=2, value="Platform compatibility in raw form.")
    public short getCompatibility_raw() {
        return this.compatibility;
    }
    
    /** Get compressed file size, in bytes.
     * @return Compressed file size
     */
    @ReportableProperty(order=13, value="Compressed file size, in bytes")
    public long getCompressedSize() {
        return this.compressed;
    }
    
    /** Get compression method in descriptive form.
     * @return Compression method
     */
    @ReportableProperty(order=9, value="Compression method in descriptive form.")
    public String getCompressionMethod_descriptive() {
        return this.method_d;
    }
    
    /** Get compression method in raw form.
     * @return Compression method
     */
    @ReportableProperty(order=8, value="Compression method in raw form.")
    public int getCompressionMethod_raw() {
        return this.method;
    }
    
    /** Get CRC-32 digest of the file.
     * @return CRC-32 digest
     */
    @ReportableProperty(order=11, value="CRC-32 digest of the file.")
    public Digest getCRC32() {
        return this.crc32;
    }
    
    /** Get extra field length, in bytes.
     * @return Extra field length
     */
    @ReportableProperty(order=16, value="Extra field length")
    public int getExtraFieldLength() {
        return this.extraFieldLength;
    }
    
    /** Get file name.
     * @return File name
     */
    @ReportableProperty(order=15, value="File name.")
    public String getFileName() {
        return this.fileName;
    }
    
    /** Get file name length, in bytes.
     * @return File name length
     */
    @ReportableProperty(order=14, value="File name length, in bytes.")
    public int getFileNameLength() {
        return this.fileNameLength;
    }
    
    /** Get general purpose bit flag in descriptive form.
     * @return General purpose bit flag
     */
    @ReportableProperty(order=7, value="General purpose bit flag in descriptive form.")
    public List<String> getGeneralPurposeBitFlags() {
        return this.flag_d;
    }
    
    /** Get general purpose bit flag in raw form.
     * @return General purpose bit flag
     */
    @ReportableProperty(order=6, value="General purpose bit flag in raw form")
    public String getGeneralPurposeBitFlags_raw() {
        return String.format("0x%04x", this.flag);
    }
    
    /** Get invalid compression method message.
     * @return Invalid compression method message
     */
    @ReportableProperty(order=53, value="Invalid compression method message.")
    public Message getInvalidCompressionMessage() {
        return this.invalidCompressionMessage;
    }
    
    /** Get invalid version message.
     * @return Invalid version message
     */
    @ReportableProperty(order=51, value="Invalid version message.")
    public Message getInvalidVersionMessage() {
        return this.invalidVersionMessage;
    }
    
    /** Get last modified date/time.
     * @return Last modified date/time
     */
    @ReportableProperty(order=10, value="Last modified date/time.")
    public Date getLastModifiedDateTime() {
        return this.lastModified;
    }
    
    /** Get data descriptor signature, in hexadecimal.
     * @return Data descriptor signature
     */
    @ReportableProperty(order=1, value="Data descriptor signature, in hexadecimal.")
    public String getSignature() {
        return this.signature;
    }
    
    /** Get uncompressed file size, in bytes.
     * @return Uncompressed file size
     */
    @ReportableProperty(order=13, value="Uncompressed file size, in bytes.")
    public long getUncompressedSize() {
        return this.uncompressed;
    }
    
    /** Get version needed to extract in descriptive form.
     * @return Version needed to extract
     */
    @ReportableProperty(order=5, value="Version needed to extract in raw form.")
    public String getVersionNeededToExtract_descriptive() {
        return this.version_d;
    }
    
    /** Get version needed to extract in raw form.
     * @return Version needed to extract
     */
    @ReportableProperty(order=4, value="Version needed to extract in raw form.")
    public short getVersionNeededToExtract_raw() {
        return this.version;
    }
    
    /** Get validity.
     * @return Validity
     */
    @ReportableProperty(order=17, value="Header validity")
    public Validity isValid()
    {
         return this.isValid;
    }
}
