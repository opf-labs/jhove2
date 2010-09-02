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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.Digest;
import org.jhove2.core.reportable.AbstractReportable;
import org.jhove2.module.format.Validator.Validity;

/** Zip local file header.
 * 
 * @author slabrams
 */
public class ZipLocalFileHeader
        extends AbstractReportable
{
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
    
    /** Version needed to extract. */
    protected int version;
  
    /** Instantiate a new <code>ZipLocalFileHeader</code>. */
    public ZipLocalFileHeader() {
        super();
        
        this.flag_d  = new ArrayList<String>();
        this.isValid = Validity.Undetermined;
    }
    
    /** Get compressed file size, in bytes.
     * @return Compressed file size
     */
    @ReportableProperty(order=9, value="Compressed file size, in bytes")
    public long getCompressedSize() {
        return this.compressed;
    }
    
    /** Get compression method in descriptive form.
     * @return Compression method
     */
    @ReportableProperty(order=6, value="Compression method in descriptive form.")
    public String getCompressionMethod_descriptive() {
        return this.method_d;
    }
    
    /** Get compression method in raw form.
     * @return Compression method
     */
    @ReportableProperty(order=5, value="Compression method in raw form.")
    public int getCompressionMethod_raw() {
        return this.method;
    }
    
    /** Get CRC-32 digest of the file.
     * @return CRC-32 digest
     */
    @ReportableProperty(order=8, value="CRC-32 digest of the file.")
    public Digest getCRC32() {
        return this.crc32;
    }
    
    /** Get extra field length, in bytes.
     * @return Extra field length
     */
    @ReportableProperty(order=13, value="Extra field length")
    public int getExtraFieldLength() {
        return this.extraFieldLength;
    }
    
    /** Get file name.
     * @return File name
     */
    @ReportableProperty(order=12, value="File name.")
    public String getFileName() {
        return this.fileName;
    }
    
    /** Get file name length, in bytes.
     * @return File name length
     */
    @ReportableProperty(order=11, value="File name length, in bytes.")
    public int getFileNameLength() {
        return this.fileNameLength;
    }
    
    /** Get general purpose bit flag in descriptive form.
     * @return General purpose bit flag
     */
    @ReportableProperty(order=4, value="General purpose bit flag in descriptive form.")
    public List<String> getGeneralPurposeBitFlags() {
        return this.flag_d;
    }
    
    /** Get general purpose bit flag in raw form.
     * @return General purpose bit flag
     */
    @ReportableProperty(order=3, value="General purpose bit flag in raw form")
    public String getGeneralPurposeBitFlags_raw() {
        return String.format("0x%04x", this.flag);
    }
    
    /** Get last modified date/time.
     * @return Last modified date/time
     */
    @ReportableProperty(order=7, value="Last modified date/time.")
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
    @ReportableProperty(order=10, value="Uncompressed file size, in bytes.")
    public long getUncompressedSize() {
        return this.uncompressed;
    }
    
    /** Get version needed to extract.
     * @return Version needed to extract
     */
    @ReportableProperty(order=2, value="Version needed to extract.")
    public int getVersionNeededToExtract() {
        return this.version;
    }
    
    /** Get validity.
     * @return Validity
     */
    @ReportableProperty(order=14, value="Header validity")
    public Validity isValid()
    {
         return this.isValid;
    }
}
