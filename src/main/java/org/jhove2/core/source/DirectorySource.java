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

import org.jhove2.annotation.ReportableProperty;

/** JHOVE2 directory source unit.
 * 
 * @author mstrong, slabrams
 */
public class DirectorySource
	extends AbstractAggregateSource
{
	/** Java {@link java.io.File} representing the directory. */
	protected File dir;
	
	/** Directory existence. */
	protected boolean isExtant;
	
	/** Directory readability. */
	protected boolean isReadable;
	
	/** Directory name. */
	protected String name;
	
	/** Directory path. */
	protected String path;
	
	/** Instantiate a new <code>DirectorySource</code>.
	 * @param pathName Directory path name
	 */
	public DirectorySource(String pathName) {
		this(new File(pathName));
	}
	
	/** Instantiate a new <code>DirectorySource</code>.
	 * @param dir Java {@link java.io.File} representing a directory
	 */
	public DirectorySource(File dir) {
		super();
		
		this.dir  = dir;
		this.name = dir.getName();
		try {
			this.path = dir.getCanonicalPath();
		} catch (IOException e) {
			/* Let path stay uninitialized. */
		}		
		this.isExtant = dir.exists();
		if (this.isExtant) {
			this.isReadable = dir.canRead();
			File [] files = dir.listFiles();
			for (int i=0; i<files.length; i++) {
				Source source = SourceFactory.getSource(files[i]);
				this.children.add(source);
			}
		}
	}
	
	/** Get directory existence.
	 * @return True if directory exists
	 */
	@ReportableProperty(order=3, value="Directory existence: true if the " +
			"directory exists")
	public boolean isExtant() {
		return this.isExtant;
	}
	
	/** Get directory readability.
	 * @return True if directory is readable
	 */
	@ReportableProperty(order=4, value="Directory readability: true if the " +
			"directory is readable.")
	public boolean isReadable() {
		return this.isReadable;
	}
	
	/** Get directory name.
	 * @return Directory name
	 */
	@ReportableProperty(order=1, value="Directory name.")
	public String getName() {
		return this.name;
	}
	
	/** Get directory path.
	 * @return Directory path
	 */
	@ReportableProperty(order=2, value="Directory path.")
	public String getPath() {
		return this.path;
	}
}