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

import org.jhove2.annotation.ReportableProperty;
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
{
    /** I/O buffer size. */
    protected int bufferSize;
    
    /** Starting offset relative to parent source. */
    protected long endingOffset;
    
    /** Backing file that is an appropriate subset of the parent source's
     * backing file; not created unless it is actually requested.
     */
    protected File backingFile;
     
   
    /** Size of the byte stream, in bytes. */
    protected long size;
    
    /** Temporary file prefix. */
    protected String tempPrefix;
    
    /** Temporary file suffix. */
    protected String tempSuffix;
    
    @SuppressWarnings("unused")
	private ByteStreamSource()
    	throws IOException, JHOVE2Exception
    {
    	this(null, null, 0, 0, null);
    }
   
    /** Instantiate a new <code>ByteStreamSource</code>.  The new byte stream
     * is automatically added as a child reportable of its parent source unit.
     * @param jhove2 JHOVE2 framework
     * @param parent Parent source
     * @param offset Starting offset relative to parent
     * @param size   Size of the byte stream
     * @throws IOException 
     * @throws JHOVE2Exception 
     */
    protected ByteStreamSource(JHOVE2 jhove2, Source parent, long offset, long size,SourceFactory sourceFactory)
        throws IOException, JHOVE2Exception
    {
        super();
        this.file           = parent.getFile();
        this.startingOffset = offset;
        this.endingOffset   = offset + size;
        this.size           = size;
        
        /* Keep a copy of the temporary file prefix and suffix and I/O buffer
         * size in case we have to create a temporary backing file.
         */
        Invocation invocation = jhove2.getInvocation();
        this.tempPrefix = invocation.getTempPrefix();
        this.tempSuffix = invocation.getTempSuffix();
        this.bufferSize = invocation.getBufferSize();
        
        /* Make this byte stream a child of its parent. */
        this.setSourceAccessor(sourceFactory.createSourceAccessor(this));
        // will update parentSourceId and sourceId fields automatically
        parent.addChildSource(this);
     }
  
    /** Get ending offset of the byte stream, relative to its parent.
     * @return Ending offset
     */
    @ReportableProperty(order=2, value="Ending offset of the byte stream, relative to its parent.")
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
        if (this.backingFile == null) {
            if (this.file != null) {
                try {
                    this.backingFile =
                        this.createTempFile(this.tempPrefix, this.tempSuffix,
                                            this.bufferSize, this.file,
                                            this.startingOffset, this.size);
                }
                catch (IOException e) {
                    /* TODO: Create and add an appropriate message.
                     * Do we have access to a ConfigInfo?
                     */
                }
            }
            else {
                /* TODO: Create and add an appropriate message. */
            }
        }
        return this.backingFile;
    }

    /**
     * Get {@link java.io.InputStream} backing the source unit
     * 
     * @return Input stream backing the source unit
     * @throws FileNotFoundException
     * @see org.jhove2.core.source.Source#getInputStream()
     */
    @Override
    public InputStream getInputStream()
        throws FileNotFoundException
    {
        return new FileInputStream(this.getFile());
    }
     
    /** Get byte stream size, in bytes.
     * @return Byte stream size
     */
    @ReportableProperty(order=1, value="Byte stream size, in bytes.")
    public long getSize() {
        return this.size;
    }
}
