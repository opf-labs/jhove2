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
import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.Digest;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.io.Input;
import org.jhove2.core.reportable.AbstractReportable;
import org.jhove2.module.format.Validator.Validity;

/** Zip data descriptor.
 * 
 * @author slabrams
 */
public class ZipDataDescriptor
        extends AbstractReportable
{    
    /** Data descriptor signature (optional). */
    public static final int DATA_DESCRIPTOR_SIGNATURE = 0x08074b50;
  
    /** Compressed size of the file, in bytes. */
    protected long compressed;
    
    /** CRC-32 digest of the file. */
    protected Digest crc32;
    
    /** Validation status. */
    protected Validity isValid;
   
    /** Optional descriptor signature, in hexadecimal. */
    protected String signature;
    
    /** Uncompressed size of the file, in bytes. */
    protected long uncompressed;
    
    /** Instantiate a new <code>ZipDataDescriptor</code>.
     */
    public ZipDataDescriptor() {
        super();
        
        this.isValid = Validity.Undetermined;
    }
    
    /** 
     * Parse a Zip data descriptor.
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
        this.isValid = Validity.True;
        
        /* Optional signature or CRC-32 digest. */
        long in = input.readUnsignedInt();
        if (in == DATA_DESCRIPTOR_SIGNATURE) {
            this.signature = String.format("0x%08x", in);
            
            in = input.readUnsignedInt();
            consumed += 4;
        }
        this.crc32 = new Digest(String.format("0x%08x", in), "CRC-32");
        consumed += 4;
        
        /* Compressed file size. */
        this.compressed = input.readUnsignedInt();
        consumed += 4;
        
        /* Uncompressed file size. */
        this.uncompressed = input.readUnsignedInt();
        consumed += 4;
        
        return consumed;
    }
    
    /** Get compressed file size, in bytes.
     * @return Compressed file size
     */
    @ReportableProperty(order=3, value="Compressed file size, in bytes")
    public long getCompressedSize() {
        return this.compressed;
    }
    
    /** Get CRC-32 digest of the file.
     * @return CRC-32 digest
     */
    @ReportableProperty(order=2, value="CRC-32 digest of the file.")
    public Digest getCRC32() {
        return this.crc32;
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
    @ReportableProperty(order=4, value="Uncompressed file size, in bytes.")
    public long getUncompressedSize() {
        return this.uncompressed;
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
