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

package org.jhove2.module.format.gzip;


import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.Date;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipException;

import org.jhove2.module.format.gzip.field.CompressionMethod;
import org.jhove2.module.format.gzip.field.CompressionType;
import org.jhove2.module.format.gzip.field.OperatingSystem;


/**
 * An input stream for reading compressed data in the GZIP file format.
 * <p>
 * This implementation is compliant with
 * <a href="http://www.ietf.org/rfc/rfc1952.txt">RFC 1952</a> (GZIP
 * file format specification version 4.3) and supports multiple member
 * GZIP files.</p>
 * <p>
 * From RFC 1952, section 2.2:</p>
 * <blockquote>
 * A GZip file consists of a series of "members" (compressed data
 * sets). [...] The members simply appear one after another in the file,
 * with no additional information before, between, or after them.
 * <blockquote>
 */
public class GzipInputStream extends InflaterInputStream
{
    /** GZip header magic number. */
    public final static int GZIP_MAGIC = 0x8b1f;
    /** The Deflate compression method. */
    public final static int DEFLATE = 8;

    /** File header flags. */
    private final static int FTEXT      = 1;    // Extra text
    private final static int FHCRC      = 2;    // Header CRC
    private final static int FEXTRA     = 4;    // Extra field
    private final static int FNAME      = 8;    // File name
    private final static int FCOMMENT   = 16;   // File comment
    private final static int FRESERVED  = 224;  // Reserved flags

    /** CRC-32 for uncompressed data. */
    protected CRC32 crc = new CRC32();
    /** Whether the main input stream has been closed. */
    private boolean closed = false;
    /** Whether the end of main input stream has been reached. */
    private boolean eos;
    /** The number of entries found so far, including the current entry. */
    private int entryCount = 0;
    /** The position in the underlying (compressed) input stream. */
    private long pos = 0L;
    /** The current entry. */
    private GzipEntryProperties entry = null;
    /** The input stream to read the current entry data. */
    private final InputStream entryInputStream;
    /** Whether the end of the current entry input stream has been reached. */
    private boolean entryEof = true;                    // No active entry.

    /**
     * Creates a new input stream with a default buffer size.
     * @param  in   the input stream.
     *
     * @throws ZipException if a GZip format error has occurred or the
     *                      compression method used is unsupported.
     * @throws IOException  if an I/O error has occurred.
     */
    public GzipInputStream(InputStream in) throws IOException {
        this(in, 512);
    }

    /**
     * Creates a new input stream with the specified buffer size.
     * @param  in     the input stream.
     * @param  size   the input buffer size.
     *
     * @throws ZipException if a GZip format error has occurred or the
     *                      compression method used is unsupported.
     * @throws IOException  if an I/O error has occurred.
     * @throws IllegalArgumentException if size is &lt;= 0.
     */
    public GzipInputStream(InputStream in, int size) throws IOException {
        super(new PushbackInputStream(in, size), new Inflater(true), size);

        this.entryInputStream = new FilterInputStream(this) {
        	public void close() throws IOException {
        		closeEntry();
        	}
        };
    }

    /**
     * Reads the next GZip file entry and positions the stream at the
     * beginning of the entry data.
     * @return the next GZip file entry, or <code>null</code> if there
     * are no more entries.
     *
     * @throws ZipException if a GZip format error has occurred.
     * @throws IOException  if an I/O error has occurred.
     */
    public GzipEntryProperties getNextEntry() throws IOException {
    	this.closeEntry();
    	this.entry = this.readHeader();
        this.entryEof = (this.entry == null);
        return this.entry;
    }

    /**
     * Closes the current GZip entry and positions the stream for
     * reading the next entry.
     *
     * @throws ZipException if a GZip format error has occurred.
     * @throws IOException  if an I/O error has occurred.
     */
    public void closeEntry() throws IOException {
        this.ensureOpen();
        if (! this.entryEof) {
            // Skip remaining entry data.
            byte[] tmpbuf = new byte[256];
	    while (this.read(tmpbuf) != -1) { /* Keep on reading... */ }
        }
        this.entryEof = true;
    }

    /**
     * Returns an input stream on the current GZip entry data stream.
     * Once the entry data have been read, is it safe to call
     * {@link InputStream#close} on the returned stream.
     *
     * @return an input stream on the entry data.
     */
    public InputStream getEntryInputStream() {
        return this.entryInputStream;
    }

    /**
     * Returns 0 after EOF has reached for the current entry data,
     * otherwise always return 1.
     * <p>
     * Programs should not count on this method to return the actual
     * number of bytes that could be read without blocking.</p>
     * @return 1 before EOF and 0 after EOF has reached for current
     *         entry.
     *
     * @throws IOException  if an I/O error has occurred.
     */
    public int available() throws IOException {
        this.ensureOpen();
        return (this.entryEof)? 0: 1;
    }

    /**
     * Reads uncompressed data into an array of bytes.  If
     * <code>len</code> is not zero, the method will block until some
     * input can be uncompressed; otherwise no bytes are read and
     * <code>0</code> is returned.
     * @param  buf   the buffer into which the data is read.
     * @param  off   the start offset in the destination buffer.
     * @param  len   the maximum number of bytes read.
     * @return the actual number of bytes read, or -1 if the end of the
     *         compressed input stream is reached.
     *
     * @throws ZipException if the compressed input data is corrupt.
     * @throws IOException  if an I/O error has occurred.
     * @throws NullPointerException if <code>buf</code> is
     *                              <code>null</code>.
     * @throws IndexOutOfBoundsException if <code>off</code> is negative,
     * <code>len</code> is negative, or <code>len</code> is greater than
     * <code>buf.length - off</code>.
     */
    public int read(byte[] buf, int off, int len) throws IOException {
        this.ensureOpen();
        if ((this.eos) || (this.entryEof)) {
            return -1;
        }
        int n = super.read(buf, off, len);
        if (n == -1) {
            this.entryEof = true;
            if (this.readTrailer()) {
                this.eos = true;
            }
        }
        else {
            // this.pos += n;
            crc.update(buf, off, n);
        }
        return n;
    }

    /**
     * Closes this input stream and releases any system resources
     * associated with the stream.
     *
     * @throws IOException if an I/O error has occurred.
     */
    public void close() throws IOException {
        if (!closed) {
            super.close();
            // Close the default inflater as we have no way to let the
            // superclass know that the default inflater impl. is being used.
            this.inf.end();
            this.closed = true;
        }
    }

    /**
     * Returns the current position in the input stream.
     *
     * @return the current position as an number of bytes.
     */
    public long getOffset() {
        return this.pos;
    }

    /**
     * Check to make sure that this stream has not been closed.
     */
    private void ensureOpen() throws IOException {
        if (closed) {
            throw new IOException("Stream closed");
        }
    }

    /**
     * Reads the next GZip member header and returns a GZip entry object
     * describing the member.
     *
     * @throws ZipException if the compressed input data is corrupt.
     * @throws IOException  if an I/O error has occurred.
     */
    private GzipEntryProperties readHeader() throws IOException {
        if (this.eos) {
            return null;        // end of stream reached.
        }
        CheckedInputStream cis = new CheckedInputStream(this.in, this.crc);
        // Reset inflater for reading next GZip member.
        this.inf.reset();
        // Reset CRC to compute header CRC (CRC16).
        this.crc.reset();

        // Entry index and start offset
        this.entryCount++;
        long startOffset = this.getOffset();
        // Check magic number
        int header = readUShort(cis);
        if (header != GZIP_MAGIC) {
            throw new ZipException("Not in GZIP format: invalid magic number");
        }
        // Read compression method
        int cm = this.readUByte(cis);
        if (cm != DEFLATE) {
            throw new ZipException("Invalid compression method: " + cm);
        }
        CompressionMethod method = CompressionMethod.fromValue(cm);
        // Read flags
        int flg = this.readUByte(cis);
        // Read MTIME field
        long time = this.readUInt(cis);
        Date date = (time != 0L)? new Date(time * 1000L): null;
        // Read XFL field
        CompressionType xflags = null;
        int xfl = this.readUByte(cis);
        if (xfl != 0) {
            xflags = CompressionType.fromValue(xfl);
        }
        // Read OS field
        OperatingSystem os = OperatingSystem.fromValue(this.readUByte(cis));
        // Check ASCII text flag
        boolean asciiFlag = ((flg & FTEXT) == FTEXT);
        // Read optional extra fields
        byte[] extraFields = null;
        if ((flg & FEXTRA) == FEXTRA) {
            int xlen = this.readUShort(cis);
            extraFields = this.readBytes(cis, xlen);
        }
        // Read optional file name
        String fileName = null;
        if ((flg & FNAME) == FNAME) {
            fileName = this.readString(cis);
        }
        // Read optional file comment
        String comment = null;
        if ((flg & FCOMMENT) == FCOMMENT) {
            comment = this.readString(cis);
        }
        // Check that no reserved flags is set
        int reservedFlags = (flg & FRESERVED);
        // Check optional header CRC
        int readCrc16 = -1;
        int computedCrc16 = ((int)(this.crc.getValue())) & 0x0000ffff;
        if ((flg & FHCRC) == FHCRC) {
            readCrc16 = this.readUShort(cis);
        }
        // Create GZip entry object with header fields
        GzipEntryProperties e = new GzipEntryProperties(this.entryCount, startOffset, method,
                                    xflags, date, fileName, os, comment,
                                    asciiFlag, extraFields, reservedFlags,
                                    readCrc16, computedCrc16);
        // Reset CRC to compute data CRC (CRC32).
        this.crc.reset();
        return e;
    }

    /**
     * Reads GZip member trailer and returns true if the end of stream
     * has been reached, false if there are more members (concatenated
     * GZip data set).
     * @return <code>true</code> if the end of stream has been reached;
     *         <code>false</code> if there are more members.
     *
     * @throws ZipException if the compressed input data is corrupt.
     * @throws IOException  if an I/O error has occurred.
     */
    private boolean readTrailer() throws IOException {
    	int n = this.inf.getRemaining();
    	if (n > 0) {
    		((PushbackInputStream)this.in).unread(this.buf, this.len - n, n);
    	}
        long csize = this.inf.getBytesRead();
        long size  = this.inf.getBytesWritten();
        this.pos += csize;
 
        // Check member data CRC
        long readCrc32 = readUInt(in);
        long computedCrc32 = this.crc.getValue();
        // Check expanded size
        long readISize = readUInt(in);
        // rfc1952; ISIZE is the input size modulo 2^32.
        long computedISize = inf.getBytesWritten() & 0xffffffffL;
        if (this.entry != null) {
            this.entry.setSizes(csize, size);
            this.entry.setISize(readISize, computedISize);
            this.entry.setDataCrc(readCrc32, computedCrc32);
        }
        return (this.in.available() == 0);
    }

    /*
     * Reads unsigned integer in Intel byte order.
     */
    private long readUInt(InputStream in) throws IOException {
        long s = readUShort(in);
        return ((long)readUShort(in) << 16) | s;
    }

    /*
     * Reads unsigned short in Intel byte order.
     */
    private int readUShort(InputStream in) throws IOException {
        int b = readUByte(in);
        return (readUByte(in) << 8) | b;
    }

    /**
     * Read a null-terminated ISO 8859-1 encoded string from the input
     * stream.
     * @return the read string minus the ending null.
     *
     * @throws IOException  if an I/O error has occurred or the stream
     *         ended before the terminating null was reached.
     */
    private String readString(InputStream in) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(256);
        int b;
        while ((b = readUByte(in)) != 0x00) {
            bos.write(b);
        }
        return new String(bos.toByteArray(), "ISO-8859-1");
    }

    /*
     * Reads unsigned byte.
     */
    private int readUByte(InputStream in) throws IOException {
        int b = in.read();
        if (b == -1) {
            throw new EOFException();
        }
        if (b < -1 || b > 255) {
            // Report on this.in, not argument in; see read{Header, Trailer}.
            throw new IOException(this.in.getClass().getName()
                        + ".read() returned value out of range -1..255: " + b);
        }
        this.pos++;
        return b;
    }

    /**
     * Reads the specified number of bytes from the input stream.
     * @param  n   the number of bytes to read.
     * @return an byte array filled with the read data.
     *
     * @throws IOException  if an I/O error has occurred or the stream
     *         ended before the <code>n</code> bytes could be read.
     */
    private byte[] readBytes(InputStream in, int n) throws IOException {
        byte[] tmpbuf = new byte[n];
        int l = in.read(tmpbuf, 0, n);
        if (l != n) {
            throw new EOFException();
        }
        this.pos += n;
        return tmpbuf;
    }

}
