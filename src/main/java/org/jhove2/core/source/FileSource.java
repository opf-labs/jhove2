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
import java.util.Date;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;


import com.sleepycat.persist.model.Persistent;

/**
 * File source unit. Represents physical file on file system.
 * 
 * @author mstrong, slabrams
 */
@Persistent
public class FileSource
	extends AbstractSource
	implements MensurableSource, FileSystemSource  
{
    /** Ending offset, in bytes, relative to the parent source.  If there is
     * no parent, the ending offset is the size.
     */
    protected long endingOffset;
 
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

	/** File path name. */
	protected String path;

	/** File size, in bytes. */
	protected long size;
	   
    /** File source name. */
    protected String sourceName;

    /** Starting offset, in bytes, relative to the parent source.  If there
     * is no parent, the starting offset is 0.
     */
    protected long startingOffset;
    
	/**
	 * Instantiate a new <code>FileSource</code>.
	 * 
     * @param jhove2 JHOVE2 framework object
	 * @param pathName
	 *            File path name
	 * @throws FileNotFoundException
	 *             File not found
	 * @throws IOException
	 *             I/O exception instantiating source
	 * @throws JHOVE2Exception 
	 */
	protected FileSource(JHOVE2 jhove2, String pathName)
	    throws FileNotFoundException, IOException, JHOVE2Exception
	{
		this(jhove2, new File(pathName));
	}
	
	@SuppressWarnings("unused")
	private FileSource(){
		super();
	}
	/**
	 * Instantiate a new <code>FileSource</code>.
	 * 
     * @param jhove2 JHOVE2 framework object
	 * @param file
	 *            Java {@link java.io.File}
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws JHOVE2Exception 
	 */
	protected FileSource(JHOVE2 jhove2, File file)
	    throws FileNotFoundException, IOException, JHOVE2Exception
	{
		super(jhove2, file);

		this.sourceName = file.getName();
		try {
			this.path = file.getCanonicalPath();
			this.sourceName = this.path;
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
		this.startingOffset = 0L;
		this.endingOffset = this.size;
	}

    /** Get ending offset of the source unit, in bytes, relative to the
     * parent source.  If there is no parent, the ending offset is the
     * size.
     * @return Starting offset of the source unit
     */
    @Override
    public long getEndingOffset() {
        return this.endingOffset;
    }
    
    /**
     * Get file last modified date.
     * 
     * @return File last modified date
     */
    @ReportableProperty(order = 2, value = "File last modified date.")
    public Date getLastModified() {
        return this.lastModified;
    }
    
	/**
	 * Get file path.
	 * 
	 * @return File path
	 */
	@ReportableProperty(order = 1, value = "File path.")
	public String getPath() {
		return this.path;
	}

    /** Get size, in bytes.
     * @return Size
     */
    @Override
    public long getSize() {
        return this.size;
    }

    /** Get file source name.
     * @return File source name
     * @see org.jhove2.core.source.NamedSource#getSourceName()
     */
    @Override
    public String getSourceName()
    {
        return this.sourceName;
    }

    /** Get starting offset of the source unit, in bytes, relative to the
     * parent source.  If there is no parent, the starting offset is 0.
     * @return Starting offset of the source unit
     */
    @Override
    public long getStartingOffset() {
        return this.startingOffset;
    }
    
    /**
     * Get file existence.
     * 
     * @return True if file exists
     */
    @Override
    public boolean isExtant() {
        return this.isExtant;
    }

    /**
     * Get file hiddeness.
     * 
     * @return True if file is hidden
     */
    @ReportableProperty(order = 3, value = "File hiddeness: True if the file is "
            + "hidden")
    public boolean isHidden() {
        return this.isHidden;
    }

	/**
	 * Get file readability.
	 * 
	 * @return True if file is readable
	 */
    @Override
	public boolean isReadable() {
		return this.isReadable;
	}

	/**
	 * Get file specialness.
	 * 
	 * @return True if file is special
	 */
	@ReportableProperty(order = 4, value = "File specialness: true if the file is "
			+ "special.")
	public boolean isSpecial() {
		return this.isSpecial;
	}
}
