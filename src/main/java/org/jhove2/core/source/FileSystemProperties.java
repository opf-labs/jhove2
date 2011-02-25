/**
 * JHOVE2 - Next-generation architecture for format-aware characterization
 *
 * Copyright (c) 2010 by The Regents of the University of California,
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

import java.util.Date;
import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.reportable.AbstractReportable;
import com.sleepycat.persist.model.Persistent;

/** File system properties,for {@link org.jhove2.core.source.Source} units
 * that are physical files or directories on the file system.
 * 
 * @author slabrams
 */
@Persistent
public class FileSystemProperties
    extends AbstractReportable
{ 
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
    
    /** File pathname. */
    protected String path;

    /** No argument constructor. */
    public FileSystemProperties()
    {
        super();
    }
    
    /** Instantiate a new <code>FileSystemProperties</code> reportable.
     * @param path       File pathname
     * @param isExtant   File is extant
     * @param isReadable File is readable
     * @param isHidden   File is hidden
     * @param isSpecial  File is special
     * @param lastModified File last modified date
     */
    public FileSystemProperties(String path, boolean isExtant,
                                boolean isReadable, boolean isHidden,
                                boolean isSpecial, Date lastModified) {
        super();
        
        this.path         = path;
        this.isExtant     = isExtant;
        this.isReadable   = isReadable;
        this.isHidden     = isHidden;
        this.isSpecial    = isSpecial;
        this.lastModified = lastModified;
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
     * Get file existence.
     * 
     * @return True if file exists
     */
    @ReportableProperty(order = 3, value = "File existence: true if the file exists")
    public boolean isExtant() {
        return this.isExtant;
    }

    /**
     * Get file hiddeness.
     * 
     * @return True if file is hidden
     */
    @ReportableProperty(order = 5, value = "File hiddeness: True if the file is "
            + "hidden")
    public boolean isHidden() {
        return this.isHidden;
    }
    
    /**
     * Get file readability.
     * 
     * @return True if file is readable
     */
    @ReportableProperty(order = 4, value = "File readability: true if the " +
            "file is readable.")
    public boolean isReadable() {
        return this.isReadable;
    }

    /**
     * Get file specialness.
     * 
     * @return True if file is special
     */
    @ReportableProperty(order = 6, value = "File specialness: true if the file is "
            + "special.")
    public boolean isSpecial() {
        return this.isSpecial;
    }
    
    /** Get file pathname.
     * @return File pathname
     */
    @ReportableProperty(order = 1, value="File pathname.")
    public String getPath() {
        return this.path;
    }
 }
