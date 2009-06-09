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

/** JHOVE2 directory source unit.
 * 
 * @author mstrong, slabrams
 */
public class DirectorySource
	extends AbstractAggregateSource
{
	/** Underlying Java {@link java.io.File} for the directory. */
	protected File directory;

	/** Directory hidden status: true if directory is hidden. */
	protected boolean isHidden;
	
	/** Directory read status: true if directory is readable. */
	protected boolean isReadable;
		
	/** Directory last modified date. */
	protected Date lastModified;
	
	/** Directory name. */
	protected String directoryName;
	
	/** Absolute directory path. */
	protected String path;
	
	/** Instantiate a new <code>DirectorySource</code>.
	 * @param directory Underlying Java {@link java.io.File}
	 */
	public DirectorySource(File directory) {
		super();
		
		this.directory     = directory;
		this.directoryName = directory.getName();
		try {
			this.path    = directory.getCanonicalPath();
		}
		catch (IOException e) {
			this.path    = this.directoryName;
		}
		this.isExpandable = true;
		this.isExtant     = directory.exists();
		if (this.isExtant) {
			this.isHidden     = directory.isHidden();
			this.isReadable   = directory.canRead();
			this.lastModified = new Date(directory.lastModified());
			
			File [] list = this.directory.listFiles();
			for (int i=0; i<list.length; i++) {
				this.src.add(SourceFactory.getSource(list[i]));
			}
		}
	}

	/** Get directory last modified date.
	 * @return Directory last modified date
	 */
	@ReportableProperty(value=3, desc="Directory last modified date.")
	public Date getLastModified() {
		return this.lastModified;
	}

	/** Get directory name.
	 * @return Directory name
	 */
	@ReportableProperty(value=1, desc="Directory name.")
	public String getDirectoryName() {
		return this.directoryName;
	}

	/** Get absolute directory path.
	 * @return Absolute directory path
	 */
	@ReportableProperty(value=2, desc="Absolute directory path.")
	public String getDirectoryPath() {
		return this.path;
	}
	
	/** Get Java {@link java.io.File} underlying the directory.
	 * @return Underlying Java file.
	 */
	public File getFile() {
		return this.directory;
	}

	/** Get file hidden status.
	 * @return True if file is hidden
	 */
	@ReportableProperty(value=4, desc="Directory hidden status: true if file is hidden.")
	public boolean isHidden() {
		return this.isHidden;
	}
	
	/** Get file read status.
	 * @return True if file is readable
	 */
	@ReportableProperty(value=5, desc="Directory read status: true if file is readable.")
	public boolean isReadable() {
		return this.isReadable;
	}
	
}
