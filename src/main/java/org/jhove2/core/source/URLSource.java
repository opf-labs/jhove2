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
import java.net.URL;

import org.jhove2.core.io.Input;
import org.jhove2.core.io.InputFactory;
import org.jhove2.core.io.Input.Type;

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
    implements NamedSource
{
    /** URL name. */
    protected String name;
    
	/** URL backing the source unit. */
	protected URL url;

	protected URLSource(){
		super();
	}
	/**
	 * Instantiate a new <code>URLSource</code>.
     * @param url     Source URL
	 * @param jhove2 JHOVE2 framework object
	 * @throws IOException
	 */

	protected URLSource(URL url, File tmpDirectory, String tmpPrefix,
	                    String tmpSuffix, int bufferSize)
	    throws IOException
	{
		super(url.openStream(), tmpDirectory, tmpPrefix,tmpSuffix, bufferSize);
		
		this.name = url.toString();
		this.url  = url;
	}

	/**
	 * Get {@link org.jhove2.core.io.Input} for the source unit.
	 * 
	 * @param bufferSize
	 *            Input buffer size
	 * @param bufferType
	 *            Input buffer type
	 * @return Input
	 * @throws FileNotFoundException
	 *             File not found
	 * @throws IOException
	 *             I/O exception instantiating input
	 * @see org.jhove2.core.source.Source#getInput(int,
	 *      org.jhove2.core.io.Input.Type)
	 */
	@Override
	public Input getInput(int bufferSize, Type bufferType)
		throws FileNotFoundException, IOException
	{
		return InputFactory.getInput(this.file, (this.isTemp && this.deleteTempFiles),
		                             bufferSize, bufferType);
	}

	/**
	 * Get URL name.
	 * 
	 * @return URL name
	 */
	@Override
	public String getSourceName() {
		return this.name;
	}
	
	/** Get URL.
	 * 
	 * @return URL
	 */
	public URL getURL(){
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
		URLSource uObj = (URLSource)obj;
		if (this.getURL()==null){
			if (uObj.getURL()!= null){
				return false;
			}
		}
		else if (uObj.getURL()==null){
			return false;
		}
		boolean equals = this.name.equalsIgnoreCase(uObj.getSourceName());
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
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((url == null) ? 0 : url.hashCode());
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
		URLSource uObj = (URLSource)source;
		if (this.getURL()==null){
			if (uObj.getURL()!= null){
				return -1;
			}
		}
		else if (uObj.getURL()==null){
			return 1;
		}
		else {			
			int stCompare = this.name.compareToIgnoreCase(uObj.getSourceName());
			if (stCompare < 0){
				return -1;
			}
			else if (stCompare > 0){
				return 1;
			}
		}
		return super.compareTo(source);
	}
}
