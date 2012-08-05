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

package org.jhove2.module.format.gzip.properties;


import java.util.Date;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.reportable.AbstractReportable;
import org.jhove2.module.format.gzip.field.CompressionMethod;
import org.jhove2.module.format.gzip.field.CompressionType;
import org.jhove2.module.format.gzip.field.OperatingSystem;

import com.sleepycat.persist.model.Persistent;


/**
 * A GZip file entry.
 */
@Persistent
public class GzipEntryProperties extends AbstractReportable {

    /** WARC record data container. */
    protected GzipEntryData entry;

    /**
     * Constructor required by the persistence layer.
     */
    public GzipEntryProperties() {
    }

    /**
     * Construct GZip entry base property instance with the supplied data.
     * @param entry GZip entry data
     */
    public GzipEntryProperties(GzipEntryData entry) {
        this.entry = entry;
    }

    /**
     * Returns the index of this entry in the GZip file.
     * @return the index (0-based) of the entry.
     */
    public int getIndex() {
        return entry.index;
    }

    /**
     * Returns whether the entry is compliant or not.
     * @return whether the entry is compliant or not
     */
    @ReportableProperty(order = 1, value = "Is non compliancy.")
    public boolean getIsNonCompliant() {
        return entry.isNonCompliant;
    }

    /**
     * Returns the offset of the beginning of this entry in the GZip
     * file.
     * @return the offset of this entry, as a number of bytes from the
     *         start of the GZip file.
     */
    @ReportableProperty(order = 2, value = "Offset value.")
    public long getOffset() {
        return entry.offset;
    }

    /**
     * Returns the name of the compressed file.
     * @return the name of the compressed file or <code>null</code> if
     *         the compressed data did not come from a file.
     */
    @ReportableProperty(order = 3, value = "GZip entry name.")
    public String getName() {
        return entry.fileName;
    }

    /**
     * Returns the GZip member comment.
     * @return the GZip member comment or <code>null</code> if absent.
     */
    @ReportableProperty(order = 4, value = "GZip entry comment.")
    public String getComment() {
        return entry.comment;
    }


    /**
     * Returns the most recent modification time of the original
     * compressed file as a {@link Date} object.
     * @return last modification date of the compressed file or
     *         <code>null</code> if none is present in the GZip header.
     */
    @ReportableProperty(order = 5, value = "GZip entry date.")
    public Date getDate() {
        return (entry.date != null)? new Date(entry.date.getTime()): null;
    }

    /**
     * Returns the most recent modification time of the original
     * compressed file as a number of milliseconds since 00:00:00 GMT,
     * Jan. 1, 1970.
     * @return last modification time of the compressed file or
     *         <code>-1</code> if none is present in the GZip header.
     */
    public long getTime() {
        return (entry.date != null)? entry.date.getTime(): -1L;
    }

    /**
     * Returns the compression method used for this entry.
     * @return the compression method.
     */
    @ReportableProperty(order = 6, value = "GZip entry compression method.")
    public CompressionMethod getCompressionMethod() {
        return entry.method;
    }

    /**
     * Returns the compression type indicated in the extra flags of
     * the member header.
     * @return the compression type or <code>null</code> if absent.
     *
     * @see    #isExtraFlagsValid
     */
    public CompressionType getCompressionFlags() {
        return entry.extraFlags;
    }

    /**
     * Returns the operating system on which the GZip member was
     * compressed.
     * @return the operating system.
     *
     * @see    #isOperatingSystemValid
     */
    @ReportableProperty(order = 7, value = "GZip entry operating system.")
    public OperatingSystem getOperatingSystem() {
        return entry.os;
    }

    /**
     * Returns whether the GZip member is announced as containing only
     * ASCII text.
     * @return the ASCII text flag from the member header.
     */
    public boolean isAscii() {
        return entry.asciiFlag;
    }

    /**
     * Returns the extra fields of the GZip member header.
     * @return the extra fields as an array of bytes or
     *         <code>null</code> if none are present.
     */
    public byte[] getExtra() {
        int l = entry.extraFields.length;
        byte[] copy = new byte[l];
        System.arraycopy(entry.extraFields, 0, copy, 0, l);
        return copy;
    }

    /**
     * Returns the CRC16 read from the GZip member header.
     * @return the CRC16 of the GZip member header or <code>-1</code>
     *         if absent.
     *
     * @see    #getComputedHeaderCrc
     * @see    #isHeaderCrcValid
     */
    @ReportableProperty(order = 8, value = "GZip entry header crc16.")
    public String getCrc16() {
    	String crc16;
    	if (entry.readCrc16 != null) {
            crc16 = "0x" + Integer.toHexString(entry.readCrc16 & 0xffff);
    	} else {
    		crc16 = null;
    	}
        return crc16;
    }

    /**
     * Returns the CRC16 computed from the GZip member header.
     * @return the computed CRC16.
     *
     * @see    #getHeaderCrc
     * @see    #isHeaderCrcValid
     */
    public long getComputedCrc16() {
        return entry.computedCrc16;
    }

    /**
     * Returns the data CRC (a.k.a. CRC32) read from the GZip member
     * trailer.
     * @return the CRC32 of the GZip member trailer or <code>-1</code>
     *         if the member trailer has not yet been read.
     *
     * @see    #getComputedDataCrc
     * @see    #isDataCrcValid
     */
    @ReportableProperty(order = 9, value = "GZip entry crc32.")
    public String getCrc32() {
        return "0x" + Integer.toHexString(entry.readCrc32);
    }

    /**
     * Returns the data CRC (a.k.a. CRC32) computed from the read
     * member data.
     * @return the computed CRC32 or <code>-1</code> if the member
     *         trailer has not yet been read.
     *
     * @see    #getDataCrc
     * @see    #isDataCrcValid
     */
    public long getComputedCrc32() {
        return entry.computedCrc32;
    }

    /**
     * Returns the ISIZE of the GZip member trailer, i.e. the announced
     * compressed size of the member data modulo 32.
     * @return the ISIZE value of the member trailer or
     *         <code>-1</code> if the member trailer has not yet
     *         been read.
     *
     * @see    #isISizeValid
     */
    @ReportableProperty(order = 10, value = "GZip entry extracted size (ISIZE) value.")
    public long getISize() {
        return entry.readISize;
    }

    /**
     * Returns the (computed) uncompressed size of the member data.
     * @return the uncompressed size of the member data or
     *         <code>-1</code> if the member trailer has not yet
     *         been read.
     */
    @ReportableProperty(order = 11, value = "GZip entry (computed) uncompressed size, in bytes.")
    public long getSize() {
        return entry.size;
    }

    /**
     * Returns the (computed) compressed size of the member data.
     * @return the compressed size of the member data or
     *         <code>-1</code> if the member trailer has not yet
     *         been read.
     */
    @ReportableProperty(order = 12, value = "GZip entry (computed) compressed size, in bytes.")
    public long getCompressedSize() {
        return entry.csize;
    }

    /**
     * Returns the (computed) ISIZE of the GZip member trailer, i.e.
     * the compressed size of the member data modulo 32.
     * @return the computed ISIZE value of the member data or
     *         <code>-1</code> if the member trailer has not yet
     *         been read.
     *
     * @see    #isISizeValid
     */
    public long getComputedISize() {
        return entry.computedISize;
    }

    @ReportableProperty(order = 13, value = "GZip entry (computed) compression ratio.")
    public double getCompressionRatio() {
        double ratio = -1.0;
        long size  = entry.size;
        long csize = entry.csize;
        if ((size > 0L) && (csize > 0L)) {
            // Compute compression ratio with 2 decimals only.
            long l = ((size - csize) * 10000L) / size;
            ratio = l / 100.00;
        }
        return ratio;
    }

    /**
     * Returns whether this entry is compliant with the rules listed
     * in section 2.3.1.2 of RFC 1952.
     * <blockquote>
     * Compliant decompressors shall only check ID1 + ID2 (magic
     * number), CM (compression method) and unset reserved flags. They
     * may ignore FTEXT and OS header fields.
     * <blockquote>
     * <p>
     * As no GzipEntry instance can be created with invalid magic number
     * or unsupported compression method (deflate), this method only
     * checks that no reserved flag is set.</p>
     *
     * @return <code>true</code> if the entry is compliant with RFC 1952
     *         rules; <code>false</code> otherwise.
     */
    /*
    public boolean isCompliant() {
        return entry.isReservedFlagsValid();
    }
    */

    /**
     * Returns whether this entry is valid, i.e. is compliant and no
     * error (invalid CRC or ISize) was found.
     *
     * @return <code>true</code> if the entry is valid;
     *         <code>false</code> otherwise.
     */
    public boolean isValid() {
        return (entry.errors == 0);
    }

}
