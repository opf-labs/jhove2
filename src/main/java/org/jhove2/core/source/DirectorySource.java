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
import java.io.FileNotFoundException;
import java.io.IOException;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;


import com.sleepycat.persist.model.Persistent;

/**
 * File system directory source unit.
 * 
 * @author mstrong, slabrams
 */
@Persistent
public class DirectorySource
    extends AbstractSource
    implements FileSystemSource
{
	/** Directory existence. */
	protected boolean isExtant;

	/** Directory readability. */
	protected boolean isReadable;
  
	/** Directory path. */
	protected String path;

	protected DirectorySource(){
		super();
		this.isAggregate = true;
	}
	
	/**
	 * Instantiate a new <code>DirectorySource</code>.
	 * 
     * @param jhove2 JHOVE2 framework object
	 * @param pathName
	 *            Directory path name
	 * @param sourceFactory SourceFactory which configures accessors for this source
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws JHOVE2Exception 
	 * @throws JHOVE2Exception 
	 */
	protected DirectorySource(JHOVE2 jhove2, String pathName,
	                          SourceFactory sourceFactory)
	    throws FileNotFoundException, IOException, JHOVE2Exception
	{
		this(jhove2, new File(pathName), sourceFactory);
	}

	/**
	 * Instantiate a new <code>DirectorySource</code>.
	 * 
     * @param jhove2 JHOVE2 framework object
	 * @param file
	 *            Java {@link java.io.File} representing a directory
	 * @param sourceFactory  SourceFactory which configures accessors for this source
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws JHOVE2Exception 
	 */
	protected DirectorySource(JHOVE2 jhove2, File file, SourceFactory sourceFactory)
	    throws FileNotFoundException, IOException, JHOVE2Exception
	{
		super(jhove2, file);
		this.setSourceAccessor(sourceFactory.createSourceAccessor(this));
		this.path = file.getName();
		try {
			this.path = file.getCanonicalPath();
		} catch (IOException e) {
			/* Let path stay initialized to just the file name. */
		}
		this.isExtant = file.exists();
		if (this.isExtant) {
			this.isReadable = file.canRead();
			File[] list = file.listFiles();
			for (int i = 0; i < list.length; i++) {
				Source source = sourceFactory.getSource(jhove2, list[i]);
				source = this.addChildSource(source);
			} 
		}
		this.isAggregate = true;
	}

	/**
	 * Get directory path.
	 * 
	 * @return Directory path
	 */
	@ReportableProperty(order = 1, value = "Directory path.")
	public String getPath() {
		return this.path;
	}

    /**
     * Get directory source name.
     * 
     * @return Directory source name
     * @see org.jhove2.core.source.NamedSource#getSourceName()
     */
    @Override
    public String getSourceName() {
        return this.path;
    }
    
	/**
	 * Get directory existence.
	 * 
	 * @return True if directory exists
	 */
	@Override
	public boolean isExtant() {
		return this.isExtant;
	}

	/**
	 * Get directory readability.
	 * 
	 * @return True if directory is readable
	 */
	@Override
	public boolean isReadable() {
		return this.isReadable;
	}
}
