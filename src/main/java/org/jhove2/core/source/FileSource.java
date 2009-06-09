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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.jhove2.annotation.ReportableProperty;


/** JHOVE2 file source unit.
 * 
 * @author mstrong, slabrams
 */
public class FileSource
	extends AbstractSource
{
	/** Underlying Java {@link java.io.File}. */
	protected File file;

	/** File hidden status: true if file is hidden. */
	protected boolean isHidden;
	
	/** File read status: true if file is readable. */
	protected boolean isReadable;
	
	/** File special status: true if file is special. */
	protected boolean isSpecial;
	
	/** File last modified date. */
	protected Date lastModified;
	
	/** File name. */
	protected String fileName;
	
	/** Absolute file path. */
	protected String path;
	
	/** File size, in bytes. */
	protected long size;
	
	/** Instantiate a new <code>FileSource</code>.
	 * @param file Underlying Java {@link java.io.File}
	 */
	public FileSource(File file) {
		super();
		
		this.file         = file;
		this.fileName     = file.getName();
		try {
			this.path     = file.getCanonicalPath();
		}
		catch (IOException e) {
			this.path     = this.fileName;
		}
		this.isExtant     = file.exists();
		if (this.isExtant) {
			this.isHidden     = file.isHidden();
			this.isReadable   = file.canRead();
			this.isSpecial    = !file.isFile();
			this.lastModified = new Date(file.lastModified());
			this.size         = file.length();
		}
	}

	/** Get file last modified date.
	 * @return File last modified date
	 */
	@ReportableProperty(value=4, desc="File last modified date")
	public Date getLastModified() {
		return this.lastModified;
	}
	
	/** Get underlying Java {@link java.io.File}.
	 * @return Underlying Java file.
	 */
	public File getFile() {
		return this.file;
	}

	/** Get file name.
	 * @return File name
	 */
	@ReportableProperty(value=1, desc="File name.")
	public String getFileName() {
		return this.fileName;
	}

	/** Get absolute file path.
	 * @return Absolute file path
	 */
	@ReportableProperty(value=2, desc="Absolute file path.")
	public String getFilePath() {
		return this.path;
	}
	
	/** Get file input stream.
	 * @return File input stream
	 * @throws FileNotFoundException 
	 */
	public InputStream getInputStream()
		throws FileNotFoundException
	{
		return new FileInputStream(this.file);
	}

	/** Get file size, in bytes.
	 * @return File size, in bytes
	 */
	@ReportableProperty(value=3, desc="File size, in bytes")
	public long getSize() {
		return this.size;
	}
	
	/** Get file existence status.
	 * @return True if file exists
	 */
	public boolean isExtant() {
		return this.isExtant;
	}
	
	/** Get file hidden status.
	 * @return True if file is hidden
	 */
	@ReportableProperty(value=5, desc="File hidden status: true if file is hidden.")
	public boolean isHidden() {
		return this.isHidden;
	}
	
	/** Get file read status.
	 * @return True if file is readable
	 */
	@ReportableProperty(value=7, desc="File read status: true if file is readable.")
	public boolean isReadable() {
		return this.isReadable;
	}
	
	/** Get file special status.
	 * @return True if file is special
	 */
	@ReportableProperty(value=6, desc="File special status: true if file is special.")
	public boolean isSpecial() {
		return this.isSpecial;
	}
}
