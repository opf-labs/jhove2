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

import org.jhove2.core.Invocation;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;

import com.sleepycat.persist.model.Persistent;

/** JHOVE2 byte stream source.  A byte stream source is always a child of
 * some other source unit.
 * 
 * @author mstrong, slabrams
 */
@Persistent
public class ByteStreamSource
    extends AbstractSource
    implements MeasurableSource
{
    /** The backing file of the parent source unit.  If parent is itself a
     * ByteStreamSource, then the backing file of the parent's parent, and
     * so on.
     */
    protected File parentFile;
     
    /** Buffer size (for creating temporary file). */
    protected int bufferSize;

    /** Starting offset relative to parent source. */
    protected long endingOffset;
    
    /** Name, if known. */
    protected String name;
    
    /** Size of the byte stream, in bytes. */
    protected long size;
    
    /** Starting offset relative to parent source. */
    protected long startingOffset;
    
    /** Temporary directory. */
    protected File tmpDirectory;
    
    /** Temporary file prefix. */
    protected String tmpPrefix;
    
    /** Temporary file suffix. */
    protected String tmpSuffix;
    
    @SuppressWarnings("unused")
	private ByteStreamSource()
    	throws IOException, JHOVE2Exception
    {
    	super();
    }
   
    /** Instantiate a new <code>ByteStreamSource</code>.  The new byte stream
     * is automatically added as a child reportable of its parent source unit.
     * @param jhove2 JHOVE2 framework
     * @param parent Parent source
     * @param offset Starting offset relative to parent
     * @param size   Size of the byte stream
     * @param name   Byte stream name, if known
     * @param sourceFactory Source factory
     * @throws IOException 
     * @throws JHOVE2Exception 
     */
    protected ByteStreamSource(JHOVE2 jhove2, Source parent, long offset,
                               long size, String name, SourceFactory sourceFactory)
        throws IOException, JHOVE2Exception
    {
        super();
        if (parent instanceof ByteStreamSource) {
            this.parentFile = ((ByteStreamSource) parent).getParentFile();
        }
        else {
            this.parentFile = parent.getFile();
        }
        this.size           = size;
        this.startingOffset = offset;
        this.endingOffset   = offset + size;
        if (size > 0L) {
            this.endingOffset--;
        }
        this.name           = name;
        
        /* Keep a copy of the temporary file prefix and suffix and I/O buffer
         * size in case we have to create a temporary backing file.
         */
        Invocation inv     = jhove2.getInvocation();
        this.tmpDirectory  = inv.getTempDirectoryFile();
        this.tmpPrefix     = inv.getTempPrefix();
        this.tmpSuffix     = inv.getTempSuffix();
        this.bufferSize    = inv.getBufferSize();
        this.deleteTempFileOnClose = inv.getDeleteTempFilesOnClose();
        
        /* Make this byte stream a child of its parent. */
        this.setSourceAccessor(sourceFactory.createSourceAccessor(this));
        /* will update parentSourceId and sourceId fields automatically. */
        parent.addChildSource(this);
     }

    /**
     * Close the source unit and release all underlying system I/O resources,
     * including a temporary backing file, if one exists..
     */
    @Override
    public void close() {
        super.close();
        if (this.file != null && this.isTemp && this.deleteTempFileOnClose) {
            this.file.delete();
            this.file = null;
        }
    }

    /** Get ending offset of the byte stream, relative to its parent.
     * @return Ending offset
     */
    @Override
    public long getEndingOffset() {
        return this.endingOffset;
    }

    /**
     * Get {@link java.io.File} backing byte stream subset of its
     * parent source.  Note that this File is not created until
     * actually required, and will be deleted on close().
     * 
     * @return File backing the byte stream; or null if the backing
     *         file cannot be created successfully
     */
    @Override
    public File getFile()
    {
        if (this.file == null) {
            /* Get format extension from name. */
            String ext = this.tmpSuffix;
            int in = name.lastIndexOf('.');
            if (in > -1) {
                ext = name.substring(in);
            }
            try {
                this.file =
                    createTempFile(this.parentFile, this.startingOffset,
                               this.size, this.tmpDirectory,
                               this.tmpPrefix, ext, this.bufferSize);
                this.isTemp = true;
            }
            catch (IOException e) {
                /* Can't do anything more than return a null value. */
            }
        }
        return this.file;
    }

    /**
     * Get {@link java.io.InputStream} backing the source unit.
     * If this method is called explicitly, then the corresponding
     * InputStream.close() method must be called to avoid a resource leak. 
     * 
     * @return Input stream backing the source unit
     * @throws FileNotFoundException Backing file not found
     * @throws IOException Backing file could not be created
     * @see org.jhove2.core.source.Source#getInputStream()
     */
    @Override
    public InputStream getInputStream()
        throws FileNotFoundException, IOException
    {
        InputStream stream = null;
        stream = new FileInputStream(this.getFile());
        return stream;
    }
     
    /** Get parent file.  This is the file associated with the parent source
     * unit, unless the parent is also a ByteStreamSource, in which case it
     * is the file that parent source's parent, and so on.
     * @return Parent file
     */
    public File getParentFile() {
        return this.parentFile;
    }
    
    /** Get byte stream size, in bytes.
     * @return Byte stream size
     */
    @Override
    public long getSize() {
        return this.size;
    }

    /** Get starting offset of the source unit, in bytes, relative to its
     * parent.
     * @return Starting offset of the source unit
     */
    @Override
    public long getStartingOffset() {
        return this.startingOffset;
    }
}
