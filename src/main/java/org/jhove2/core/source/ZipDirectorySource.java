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
import java.util.Date;
import java.util.zip.ZipEntry;

import org.jhove2.annotation.ReportableProperty;

import com.sleepycat.persist.model.Persistent;

/**
 * Container source unit that is directory within a ZIP file.
 * 
 * @author mstrong, slabrams
 */
@Persistent
public class ZipDirectorySource
    extends AbstractSource
    implements NamedSource
{
	/** Zip directory comment. */
	protected String comment;

	/** Zip directory last modified date. */
	protected Date lastModified;

	/** Zip directory path. */
	protected String path;

    /** Zip directory source name. */
    protected String sourceName;

	protected ZipDirectorySource(){
		super();
	}
	/**
	 * Instantiate a new <code>ZipDirectorySource</code>.
     * @param entry
     *            Zip directory entry
	 */
	protected ZipDirectorySource(ZipEntry entry) {
		super();

		this.isAggregate = true;
		this.path = entry.getName();
		/* Delete trailing slash (/), if found. */
		int in = this.path.lastIndexOf(File.separator);
		if (in == this.path.length() - 1) {
			this.path = this.path.substring(0, in);
		}
		this.sourceName = this.path;
		in = this.sourceName.lastIndexOf(File.separator);
		if (in > -1) {
			this.sourceName = this.sourceName.substring(in + 1);
		}
		this.lastModified = new Date(entry.getTime());
		this.comment = entry.getComment();
	}

	/**
	 * Get Zip directory comment.
	 * 
	 * @return Zip directory comment
	 */
	@ReportableProperty(order = 3, value = "Zip directory comment.")
	public String getComment() {
		return this.comment;
	}

	/**
	 * Get Zip directory last modified date.
	 * 
	 * @return Zip directory last modified date
	 */
	@ReportableProperty(order = 2, value = "Zip directory last modified date.")
	public Date getLastModified() {
		return this.lastModified;
	}

	/**
	 * Get Zip directory source name.
	 * 
	 * @return Zip directory source name
	 * @see org.jhove2.core.source.NamedSource#getSourceName()
	 */
	@Override
	public String getSourceName() {
		return this.sourceName;
	}

	/**
	 * Get Zip directory path.
	 * 
	 * @return Zip directory path
	 */
	@ReportableProperty(order = 1, value = "Zip directory path.")
	public String getPath() {
		return this.path;
	}
	
	/** Determine equality with another Zip Directory source.
	 * @param obj Zip Directory source to be compared
	 */
	@Override
	public boolean equals(Object obj){
		boolean equals = false;		
		if (obj==null){
			return false;
		}
		if (this == obj){
			return true;
		}
		equals = obj instanceof ZipDirectorySource;
		if (!equals){
			return false;
		}
		ZipDirectorySource zObj = (ZipDirectorySource)obj;
		if (this.getPath() == null){
			if (zObj.getPath() != null){
				return false;
			}
		}
		else if (zObj.getPath() == null){
			return false;
		}
		equals = this.getPath().equalsIgnoreCase(zObj.getPath());
		if (!equals){
			return false;
		}
		/* Don't bother to check name; it is constructed from path. */
		if (this.getLastModified() == null){
			if (zObj.getLastModified() != null){
				return false;
			}
		}
		else if (zObj.getLastModified() == null){
			return false;
		}
		equals = this.getLastModified().equals(zObj.getLastModified());
		if (!equals){
			return false;
		}
		if (this.getComment() == null){
			if (zObj.getComment()!= null){
				return false;
			}
		}
		else if (zObj.getComment() == null){
			return false;
		}
		equals = this.getComment().equalsIgnoreCase(zObj.getComment());
		if (!equals){
			return false;
		}
		return super.equals(obj);
	}
	
	/** Generate unique hash code for the Zip Directory source.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((comment == null) ? 0 : comment.hashCode());
        result = prime * result
                + ((lastModified == null) ? 0 : lastModified.hashCode());
        result = prime * result + ((sourceName == null) ? 0 : sourceName.hashCode());
        result = prime * result + ((path == null) ? 0 : path.hashCode());
        return result;
    }

    @Override
	public int compareTo(Source source){
		int comp = 0;
		if (source==null){
			return 1;
		}
		if (this==source){
			return 0;
		}
		if (!(source instanceof ZipDirectorySource)){
			int compareSource = this.getReportableIdentifier().
				compareTo(source.getReportableIdentifier());
			return compareSource;
		}
		ZipDirectorySource zObj = (ZipDirectorySource)source;
		if (this.getPath()==null){
			if (zObj.getPath() != null){
				return -1;
			}
		}
		else if (zObj.getPath() == null){
			return 1;
		}
		else {
			comp = this.getPath().compareToIgnoreCase(zObj.getPath());
			if (comp < 0){
				return -1;
			}
			else if (comp > 0){
				return 1;
			}
		}
		if (this.getLastModified() == null){
			if (zObj.getLastModified() != null){
				return -1;
			}
		}
		else if (zObj.getLastModified() == null){
			return 1;
		}
		else {
			comp = this.getLastModified().compareTo(zObj.getLastModified());
			if (comp < 0){
				return -1;
			}
			else if (comp > 0){
				return 1;
			}
		}
		if (this.getComment() == null){
			if (zObj.getComment()!= null){
				return -1;
			}
		}
		else if (zObj.getComment() == null){
			return 1;
		}
		else {
			comp = this.getComment().compareToIgnoreCase(zObj.getComment());
			if (comp < 0){
				return -1;
			}
			else if (comp > 0){
				return 1;
			}
		}		
		return super.compareTo(source);
	}
}
