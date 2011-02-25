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
    implements NamedSource
{  
    /** Java {java.io.File} underlying the directory, if a physical directory
     * on the file system.
     */
    protected File file;
    
	/** Directory name. */
	protected String name;

	protected DirectorySource(){
		super();
		this.isAggregate = true;
	}
	
	/**
	 * Instantiate a new <code>DirectorySource</code>.
	 * 
     * @param jhove2 JHOVE2 framework object
	 * @param name
	 *            Directory path name
	 * @throws IOException
	 * @throws JHOVE2Exception 
	 * @throws JHOVE2Exception 
	 */
	protected DirectorySource(JHOVE2 jhove2, String name)
	    throws IOException, JHOVE2Exception
	{
		this(jhove2, name, true);
	}
    
    /**
     * Instantiate a new <code>DirectorySource</code>.
     * 
     * @param jhove2 JHOVE2 framework object
     * @param name
     *            Directory path name
     * @param fileSystemDirectory True if a physical directory on the file system
     * @throws IOException
     * @throws JHOVE2Exception 
     */
    protected DirectorySource(JHOVE2 jhove2, String name,
                              boolean fileSystemDirectory)
        throws IOException, JHOVE2Exception
    {
        super(jhove2);
        
        /* Is this is a physical directory (and not a directory embedded in
         * a container, create a Java {@link java.io.File}.
         */
        this.file = null;
        if (fileSystemDirectory) {
            this.file = new File(name);
        }
        init(jhove2, name, this.file, fileSystemDirectory);
    }
    
    /**
     * Instantiate a new <code>DirectorySource</code>.
     * 
     * @param jhove2 JHOVE2 framework object
     * @param file
     *            Java {@link java.io.File} representing a directory
     * @throws IOException
     * @throws JHOVE2Exception 
     */
    protected DirectorySource(JHOVE2 jhove2, File file)
        throws IOException, JHOVE2Exception
    {
        this(jhove2, file, true);
    }
    
	/**
	 * Instantiate a new <code>DirectorySource</code>.
	 * 
     * @param jhove2 JHOVE2 framework object
	 * @param file
	 *            Java {@link java.io.File} representing a directory
	 * @param fileSystemDirectory True if a physical directory on the file
	 *                            system, as opposed to a directory embedded
	 *                            in a container
	 * @throws IOException
	 * @throws JHOVE2Exception 
	 */
	protected DirectorySource(JHOVE2 jhove2, File file,
	                          boolean fileSystemDirectory)
	    throws IOException, JHOVE2Exception
	{
		super(jhove2);
		
		init(jhove2, file.getName(), file, fileSystemDirectory);
	}
	
	/** Initialize the directory source.
	 * @param jhove2 JHOVE2 framework object
	 * @param name   Directory name
	 * @param file   Directory file, if a physical directory
	 * @param fileSystemDirectory True if a physical directory
	 * @throws IOException
	 * @throws JHOVE2Exception
	 */
	protected void init(JHOVE2 jhove2, String name, File file,
	                    boolean fileSystemDirectory)
	    throws IOException, JHOVE2Exception 
	{
        this.name = name;
		if (fileSystemDirectory) { 
		    /* Get child source units. */
		    File[] list = file.listFiles();
		    for (int i = 0; i < list.length; i++) {
		        Source source = jhove2.getSourceFactory().getSource(jhove2,
		                                                            list[i]);
		        source = this.addChildSource(source);
		    }
		    /* Get file system-specific properties. */
            String path = name;
	        try {
                path = file.getCanonicalPath();
	        } catch (IOException e) {
	            /* Let path stay initialized to just the directory name. */
	        }
            this.fileSystemProperties =
                new FileSystemProperties(path, file.exists(), file.canRead(),
                                         file.isHidden(), false,
                                         new Date(file.lastModified()));
		}
		this.isAggregate = true;
	}

    /**
     * Get directory source name.
     * 
     * @return Directory source name
     * @see org.jhove2.core.source.NamedSource#getSourceName()
     */
    @Override
    public String getSourceName() {
        return this.name;
    }
 }
