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
import java.io.IOException;
import java.util.Date;

import org.jhove2.annotation.ReportableProperty;

/** JHOVE2 file source unit.  A file source unit is a unitary formatted object
 * 
 * @author mstrong, slabrams
 */
public class FileSource
	extends AbstractSource
{
	/** Java {@link java.io.File}. */
	protected File file;
	
	/** File existence. */
	protected boolean isExtant;
	
	/** File hiddeness. */
	protected boolean isHidden;
	
	/** File readability. */
	protected boolean isReadable;
	
	/** File specialness. */
	protected boolean isSpecial;
	
	/** File last modified date. */
	protected Date lastModified;
	
	/** File name. */
	protected String name;
	
	/** File path name. */
	protected String path;
	
	/** File size, in bytes. */
	protected long size;
	
	/** Instantiate a new <code>FileSource</code>.
	 * @param pathName File path name
	 */
	public FileSource(String pathName) {
		this(new File(pathName));
	}
	
	/** Instantiate a new <code>FileSource</code>.
	 * @param file File
	 */
	public FileSource(File file) {
		super();
		
		this.file = file;
		this.name = file.getName();
		try {
			this.path = file.getCanonicalPath();
		} catch (IOException e) {
			/* Let path stay uninitialized. */
		}		
		this.isExtant = file.exists();
		if (this.isExtant) {
			this.size = file.length();
			this.isHidden = file.isHidden();
			this.isReadable = file.canRead();
			this.isSpecial = !file.isFile();
			this.lastModified = new Date(file.lastModified());
		}
		else {
			this.size = 0L;
			this.isHidden = false;
			this.isSpecial = false;
		}
	}
	
	/** Get file existence.
	 * @return True if file exists
	 */
	@ReportableProperty(order=5, value="File existence: true if the file exists")
	public boolean isExtant() {
		return this.isExtant;
	}
	
	/** Get file hiddeness.
	 * @return True if file is hidden
	 */
	@ReportableProperty(order=7, value="File hiddeness: True if the file is " +
			"hidden")
	public boolean isHidden() {
		return this.isHidden;
	}
	
	/** Get file last modified date.
	 * @return File last modified date
	 */
	@ReportableProperty(order=4, value="File last modified date.")
	public Date getLastModified() {
		return this.lastModified;
	}
	
	/** Get file name.
	 * @return File name
	 */
	@ReportableProperty(order=1, value="File name.")
	public String getName() {
		return this.name;
	}
	
	/** Get file path.
	 * @return File path
	 */
	@ReportableProperty(order=2, value="File path.")
	public String getPath() {
		return this.path;
	}
	
	/** Get file readability.
	 * @return True if file is readable
	 */
	@ReportableProperty(order=6, value="File readability: true if the file is " +
			"readable.")
	public boolean isReadable() {
		return this.isReadable;
	}
	
	/** Get file specialness.
	 * @return True if file is special
	 */
	@ReportableProperty(order=8, value="File specialness: true if the file is " +
			"special.")
	public boolean isSpecial() {
		return this.isSpecial;
	}
	
	/** Get file size, in bytes.
	 * @return File size, in bytes
	 */
	@ReportableProperty(order=3, value="File size, in bytes.")
	public long getSize() {
		return this.size;
	}
}
