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

package org.jhove2.core.source;

import java.io.InputStream;
import java.util.Date;
import java.util.zip.ZipEntry;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.Digest;
import org.jhove2.module.digest.AbstractArrayDigester;
import org.jhove2.module.digest.CRC32Digester;

/** JHOVE2 Zip file source unit.
 * 
 * @author mstrong, slabrams
 */
public class ZipFileSource
	extends AbstractSource
	implements AtomicSource
{
	/** Zip file CRC-32 message digest. */
	protected Digest crc32;
	
	/** Zip file comment. */
	protected String comment;
	
	/** Zip file last modified date. */
	protected Date lastModified;
	
	/** Zip file name. */
	protected String name;
	
	/** Zip file path. */
	protected String path;
	
	/** Zip file size, in bytes. */
	protected long size;
	
	/** Zip file input stream. */
	protected InputStream stream;
	
	/** Instantiate a new <code>ZipFileSource</code>.
	 * @param stream Input stream for the Zip file entry
	 * @param entry  Zip file entry
	 */
	public ZipFileSource(InputStream stream, ZipEntry entry) {
		super();
		
		this.stream       = stream;
		this.path         = entry.getName();
		this.name         = this.path;
		int in = this.name.lastIndexOf('/');
		if (in > -1) {
			this.name = this.name.substring(in+1);
		}
		this.size         = entry.getSize();
		this.lastModified = new Date(entry.getTime());
		this.crc32        = new Digest(AbstractArrayDigester.toHexString(entry.getCrc()),
				                       CRC32Digester.ALGORITHM);
		this.comment      = entry.getComment();
	}

	/** Get Zip file comment.
	 * @return Zip file comment
	 */
	@ReportableProperty(order=6, value="Zip file comment.")
	public String getComment() {
		return this.comment;
	}
	
	/** Get Zip file CRC-32 message digest.
	 * @return Zip file CRC-32 message digest
	 */
	@ReportableProperty(order=5, value="Zip file CRC-32 message digest.")
	public Digest getCRC32MessageDigest() {
		return this.crc32;
	}
	
	/** Get Zip file last modified date.
	 * @return Zip file last modified date
	 */
	@ReportableProperty(order=4, value="Zip file last modified date.")
	public Date getLastModified() {
		return this.lastModified;
	}
	
	/** Get Zip file name.
	 * @return Zip file name
	 */
	@ReportableProperty(order=1, value="Zip file name.")
	public String getName() {
		return this.name;
	}
	
	/** Get Zip file path.
	 * @return Zip file path
	 */
	@ReportableProperty(order=2, value="Zip file path.")
	public String getPath() {
		return this.path;
	}
	
	/** Get Zip file size, in bytes.
	 * @return Zip file size, in bytes
	 */
	@ReportableProperty(order=3, value="Zip file size, in bytes.")
	public long getSize() {
		return this.size;
	}
}
