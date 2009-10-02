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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.io.Input;
import org.jhove2.core.io.InputFactory;
import org.jhove2.core.io.Input.Type;

/**
 * JHOVE2 URL source unit.
 * 
 * @author mstrong, slabrams
 */
public class URLSource extends AbstractSource {
	/** URL backing the source unit. */
	protected URL url;

	/**
	 * Instantiate a new <code>URLSource</code>.
	 * 
     * @param String temporary file prefix
     * @param String temporary file suffix
     * @param int buffer size 
	 * @param url
	 *            URL
	 * @throws IOException
	 */

	public URLSource(String tmpPrefix, String tmpSuffix,
			int bufferSize, URL url) throws IOException {
		super(tmpPrefix, tmpSuffix, bufferSize, url.openStream());
		this.url = url;
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
			throws FileNotFoundException, IOException {
		return InputFactory.getInput(this.file, bufferSize, bufferType);
	}

	/**
	 * Get URL.
	 * 
	 * @return URL
	 */
	@ReportableProperty("URL.")
	public String getURLString() {
		return this.url.toString();
	}
	
	public URL getURL(){
		return this.url;
	}
	@Override
	public boolean equals(Object obj){
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
		boolean equals = this.getURLString().equalsIgnoreCase(uObj.getURLString());
		if (!equals){
			return false;
		}
		return super.equals(obj);
	}
	@Override
	public int compareTo(Source source){
		if (source==null){
			return 1;
		}
		if (this==source){
			return 0;
		}
		if (!(source instanceof URLSource)){
			int compareSource = this.getJhove2Identifier().
				compareTo(source.getJhove2Identifier());
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
			int stCompare = this.getURLString().compareToIgnoreCase(uObj.getURLString());
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
