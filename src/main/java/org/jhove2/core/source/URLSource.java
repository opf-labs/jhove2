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
import java.net.URL;
import java.nio.ByteOrder;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.Invocation;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.io.Input;
import org.jhove2.core.io.InputFactory;

import com.sleepycat.persist.model.Persistent;

/**
 * URL source unit. Represents source unit, possibly remote, that is designated by a URL using any
 * of the following schemes: http, https, ftp, file, and jar. Redirection is not supported.
 * 
 * @author mstrong, slabrams
 */
@Persistent
public class URLSource
    extends AbstractSource
    implements MeasurableSource, NamedSource
{
    /** Ending offset, in bytes, relative to the
     * parent source.  If there is no parent, the ending offset is the
     * size.
     */
    protected long endingOffset;
    
    /** Temporary backing file. */
    protected File file;
 
    /** File size, in bytes. */
    protected long size;
 
    /** Starting offset, in bytes, relative to the
     * parent source.  If there is no parent, the ending offset is the
     * size.
     */
    protected long startingOffset;
    
    /** URL. */
    protected String url;

	protected URLSource(){
		super();
	}
	/**
	 * Instantiate a new <code>URLSource</code>.
	 * @param jhove2 JHOVE2 framework object
     * @param url    Source URL
	 * @throws IOException
	 */
	protected URLSource(JHOVE2 jhove2, URL url)
	    throws IOException
	{
		super(jhove2);
        Invocation inv = jhove2.getInvocation();
        this.file = createTempFile(url.openStream(), trimPath(url.getPath()),
                                   inv.getTempDirectoryFile(),
                                   inv.getTempPrefix(), inv.getTempSuffix(),
                                   inv.getBufferSize());
        this.isTemp = true;
	    
		this.url = url.toString();
        this.size = this.file.length();
        this.startingOffset = 0L;
        this.endingOffset   = this.size;
        if (this.size > 0L) {
            this.endingOffset--;
        }
	}

	/** Get the trailing part of the URL path.
	 * @param path URL path
	 * @return Trailing part of the URL path
	 */
    private static synchronized String trimPath(String path) {
        int in = path.lastIndexOf("/");
        if (in > -1) {
            path = path.substring(in+1);
        }
        return path;
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
        input.setDeleteTempFileOnClose(this.deleteTempFileOnClose);
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
        throws IOException
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

    /** Get starting offset of the source unit, in bytes, relative to the
     * parent source.  If there is no parent, the starting offset is 0.
     * @return Starting offset of the source unit
     */
    @Override
    public long getStartingOffset() {
        return this.startingOffset;
    }
    
	/**
	 * Get URL source name.
	 * 
	 * @return URL source name
	 */
	@Override
	public String getSourceName() {
		return this.url;
	}
	
	/** Get URL.
	 * @return URL
	 */
	@ReportableProperty(order=1, value="URL")
	public String getURL() {
	    return this.url;
	}
	
	/** Compare the URL.
	 * @return True, if the URLs are identical
	 * @see org.jhove2.core.source.AbstractSource#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null){
			return false;
		}
		if (this == obj){
			return true;
		}
		if (!(obj instanceof URLSource)){
			return false;
		}
		boolean equals = this.url.equalsIgnoreCase(((NamedSource)obj).getSourceName());
		if (!equals){
			return false;
		}
		return super.equals(obj);
	}
	
	/** Generate a unique hash code for the URL source.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((this.url == null) ? 0 : this.url.hashCode());
        return result;
    }
    
	/** Compare to another source unit.
         * @param source
         * @return -1, 0, or 1 if the URL is less than, equals to, or greater than
	 * the compared URL
	 */
	@Override
	public int compareTo(Source source){
		if (source==null){
			return 1;
		}
		if (this==source){
			return 0;
		}
		if (!(source instanceof URLSource)){
			int compareSource = this.getReportableIdentifier().
				compareTo(source.getReportableIdentifier());
			return compareSource;
		}		
		int stCompare = this.url.compareToIgnoreCase(((NamedSource)source).getSourceName());
		if (stCompare < 0){
		    return -1;
		}
		else if (stCompare > 0){
		    return 1;
		}
		return super.compareTo(source);
	}
}
