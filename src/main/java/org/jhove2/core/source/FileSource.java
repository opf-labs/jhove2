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
import java.nio.ByteOrder;
import java.util.Date;

import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.io.Input;
import org.jhove2.core.io.InputFactory;


import com.sleepycat.persist.model.Persistent;

/**
 * File source unit. Represents an abstract file, which can either be a
 * physical file in file system or a file embedded inside of a container.
 * 
 * @author mstrong, slabrams
 */
@Persistent
public class FileSource
	extends AbstractSource
	implements MeasurableSource, NamedSource  
{
    /** Ending offset, in bytes, relative to the parent source.  If there is
     * no parent, the ending offset is the size.
     */
    protected long endingOffset;
    
    /** Backing file. */
    protected File file;

	/** File last modified date. */
	protected Date lastModified;

	/** File name. */
	protected String name;

	/** File size, in bytes. */
	protected long size;

    /** Starting offset, in bytes, relative to the parent source.  If there
     * is no parent, the starting offset is 0.
     */
    protected long startingOffset;
    
	/**
	 * Instantiate a new <code>FileSource</code>.
	 * 
     * @param jhove2 JHOVE2 framework object
	 * @param name
	 *            File name
	 * @throws FileNotFoundException
	 *             File not found
	 * @throws IOException
	 *             I/O exception instantiating source
	 * @throws JHOVE2Exception 
	 */
	protected FileSource(JHOVE2 jhove2, String name)
	    throws JHOVE2Exception
	{
	    this(jhove2, new File(name));
	}
	
	@SuppressWarnings("unused")
	private FileSource(){
		super();
	}
	   
    /**
     * Instantiate a new file system <code>FileSource</code>.
     * 
     * @param jhove2 JHOVE2 framework object
     * @param file
     *            Java {@link java.io.File}
     * @throws IOException
     * @throws FileNotFoundException
     * @throws JHOVE2Exception 
     */
    protected FileSource(JHOVE2 jhove2, File file)
        throws JHOVE2Exception
    {
        this(jhove2, file, true);
    }
    
	/**
	 * Instantiate a new <code>FileSource</code>.
	 * 
     * @param jhove2 JHOVE2 framework object
	 * @param file
	 *            Java {@link java.io.File}
	 * @param fileSystemFile
	 *            True if the file is a physical file on the file system, and 
	 *            not embedded within a container
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws JHOVE2Exception 
	 */
	protected FileSource(JHOVE2 jhove2, File file, boolean fileSystemFile)
	    throws JHOVE2Exception
	{
		super(jhove2);

		this.file = file;
		this.name = file.getName();
        this.size = file.length();
		this.startingOffset = 0L;
		this.endingOffset = this.size;
        if (this.size > 0L) {
            this.endingOffset--;
        }

        if (fileSystemFile) {
            /* Get file system-specific properties. */
            String path = name;
            try {
                path = file.getCanonicalPath();
            } catch (IOException e) {
                /* Let path stay initialized to just the file name. */
            }
            this.fileSystemProperties =
                new FileSystemProperties(path, file.exists(), file.canRead(),
                                         file.isHidden(), !file.isFile(),
                                         new Date(file.lastModified()));
        }
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
     * Get {@link java.io.File} backing the source unit.
     * 
     * @return File backing the source unit
     * @see org.jhove2.core.source.Source#getFile()
     */
    @Override
    public File getFile() {
        return this.file;
    }
    
    /**
     * Create and get {@link org.jhove2.core.io.Input} for the source unit. 
     * If this method is called explicitly, then the corresponding Input.close()
     * method must be called to avoid a resource leak.
     * @param jhove2 JHOVE2 framework object
     * @param order
     *            Byte order
     * @return Source unit input
     * @throws IOException
     *             I/O exception getting input
     */
    @Override
    public Input getInput(JHOVE2 jhove2, ByteOrder order)
        throws IOException
    {
        Input input = InputFactory.getInput(jhove2, this.file, this.isTemp, order);
        if (input != null) {
            input.setDeleteTempFileOnClose(this.deleteTempFileOnClose);
        }
        return input;
    }
    
    /**
     * Get {@link java.io.InputStream} backing the source unit.
     * If this method is called explicitly, then the corresponding
     * InputStream.close() method must be called to avoid a resource leak. 
     * 
     * @return Input stream backing the source unit, or null if a Clump,
     *         Directory, or FileSet source
     * @throws FileNotFoundException  Backing file not found
     * @throws IOException Backing file could not be created
     * @see org.jhove2.core.source.Source#getInputStream()
     */
    @Override
    public InputStream getInputStream()
        throws FileNotFoundException, IOException
    {
        InputStream stream = null;
        if (this.file != null) {
            stream = new FileInputStream(this.file);
        }
        return stream;
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
        return this.name;
    }

    /** Get starting offset of the source unit, in bytes, relative to the
     * parent source.  If there is no parent, the starting offset is 0.
     * @return Starting offset of the source unit
     */
    @Override
    public long getStartingOffset() {
        return this.startingOffset;
    }
}
