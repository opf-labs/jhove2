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

package org.jhove2.module.format.zip;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.format.Format;
import org.jhove2.core.io.Input;
import org.jhove2.core.source.FileSetSource;
import org.jhove2.core.source.Source;
import org.jhove2.core.source.SourceFactory;
import org.jhove2.module.format.BaseFormatModule;
import org.jhove2.module.format.Validator;
import org.jhove2.persist.FormatModuleAccessor;

import com.sleepycat.persist.model.Persistent;

/**
 * JHOVE2 Zip module.
 * 
 * @author mstrong, slabrams
 */
@Persistent
public class ZipModule
	extends BaseFormatModule
    implements Validator
{
	/** Zip module version identifier. */
	public static final String VERSION = "2.0.0";

	/** Zip module release date. */
	public static final String RELEASE = "2010-09-10";

	/** Zip module rights statement. */
	public static final String RIGHTS =
		"Copyright 2010 by The Regents of the University of California, " +
		"Ithaka Harbors, Inc., and The Board of Trustees of the Leland " +
		"Stanford Junior University. " +
		"Available under the terms of the BSD license.";
	   
    /** Module validation coverage. */
    public static final Coverage COVERAGE = Coverage.Selective;
    
    /** Archive extra data record signature. */
    public static final int ARCHIVE_EXTRA_DATA_RECORD_SIGNATURE = 0x08064b50;
    
    /** Central directory structure signature. */
    public static final int CENTRAL_DIRECTORY_STRUCTURE_SIGNATURE = 0x02014b50;
  
    /** Digital signature header signature. */
    public static final int DIGITAL_SIGNATURE_HEADER_SIGNATURE = 0x05054b50;
    
    /** End of central directory signature. */
    public static final int END_OF_CENTRAL_DIRECTORY_SIGNATURE = 0x06054b50;
    
    /** Local file header signature. */
    public static final int LOCAL_FILE_HEADER_SIGNATURE = 0x04034b50;
    
    /** Zip64 end of central directory signature. */
    public static final int ZIP64_END_OF_CENTRAL_DIRECTORY_SIGNATURE = 0x06064b50;
    
    /** Zip64 end of central directory locator signature. */
    public static final int ZIP64_END_OF_CENTRAL_DIRECTORY_LOCATOR_SIGNATURE = 0x07064b50;
    
	/** Zip file entries. */
	protected List<ZipFileEntry> entries;
	
	/** Validation status. */
	protected Validity isValid;
	
	/**
	 * Instantiate a new <code>ZipModule</code>.
	 * 
	 * @param format
	 *            Zip format
     * @param formatModuleAccessor 
     *       FormatModuleAccessor to manage access to Format Profiles
	 */
	public ZipModule(Format format, 
    		FormatModuleAccessor formatModuleAccessor) {
		super(VERSION, RELEASE, RIGHTS, format, formatModuleAccessor);
		this.entries = new ArrayList<ZipFileEntry> ();
		this.isValid = Validity.Undetermined;
	}
	
	public ZipModule(){
		this(null,null);
	}

	/**
	 * Parse a Zip source unit.
	 * 
	 * @param source
	 *            Zip source unit
	 * @input param
	 *            Zip source input
	 * @return 0
	 * @throws EOFException
	 *             If End-of-File is reached reading the source unit
	 * @throws IOException
	 *             If an I/O exception is raised reading the source unit
	 * @throws JHOVE2Exception
	 * @see org.jhove2.module.format.FormatModule#parse(org.jhove2.core.JHOVE2,
	 *      org.jhove2.core.source.Source, org.jhove2.core.io.Input)
	 */
	@Override
	public long parse(JHOVE2 jhove2, Source source, Input input)
		throws EOFException, IOException, JHOVE2Exception
	{		
	    long consumed = 0L;
        /* this.isValid = Validity.True; */
	    input.setByteOrder(ByteOrder.LITTLE_ENDIAN);
		        
	    /* Use the native Java Zip classes to retrieve the (possibly)
	     * compressed entries as individual source units.
	     */
	    File file = source.getFile();
	    ZipFile zip = new ZipFile(file, ZipFile.OPEN_READ);
	    if (zip != null) {
	        source.setIsAggregate(true);
	        try {
	            /*
	             * Zip entries are not necessarily in hierarchical order.
	             * Build a map of directory names and source units so we 
	             * can associate file entries to their correct parent.
	             */
	            Map<String, Source> map = new TreeMap<String, Source>();
	            Enumeration<? extends ZipEntry> en = zip.entries();
	            SourceFactory factory = jhove2.getSourceFactory();
	    		if (factory == null){
	    			throw new JHOVE2Exception("JHOVE2 SourceFactory is null");
	    		}
	            while (en.hasMoreElements()) {
	                ZipEntry entry = en.nextElement();
	                if (entry.isDirectory()) {
	                    Source src =
	                    	factory.getSource(jhove2, zip, entry);
	                    if (src != null) {
	                        String key = entry.getName();
	                        /* Remove trailing slash. Although this always
	                         * should be a forward slash (/), in practice a
	                         * backward slash (\) may be found. */
	                        int len = key.length() - 1;
	                        char ch = key.charAt(len);
	                        if (ch == '/') {
	                            key = key.substring(0, len);
	                        }
	                        else if (ch == '\\') {
	                            key = key.substring(0, len);
	                        }
	                        map.put(key, src);
	                    }
	                }
	            }

	            /*
	             * Characterize each entry and associate it with its parent
	             * source unit.
	             */
	            en = zip.entries();
	            while (en.hasMoreElements()) {
	                ZipEntry entry = en.nextElement();
	                String name = entry.getName();
	                if (entry.isDirectory()) {
                        /* Remove trailing slash. Although this always should
                         * be a forward slash (/), in practice a backward
                         * slash (\) may be found. */
	                    int len = name.length() - 1;
	                    char ch = name.charAt(len);
	                    if (ch == '/') {
	                        name = name.substring(0, len);
	                    }
	                    else if (ch == '\\') {
	                        name = name.substring(0, len);
	                    }
	                    /* Get the source unit from the map. */
	                    Source src = map.get(name);
	                    if (src != null) {
	                        /* Make sure to close the Input after
	                         * characterization is completed.
	                         */
	                        Input inpt = src.getInput(jhove2);
	                        try {
	                            src = jhove2.characterize(src, inpt);
	                        }
	                        finally {
	                            if (inpt != null) {
	                                inpt.close();
	                            }
	                        }
	                        
	                        /* Check if the pathname includes a directory
	                         * component. Although the path separator always
	                         * should be a forward slash (/), in practice a
	                         * backward slash (\) may be found.
	                         */
	                        int in = name.lastIndexOf('/');
	                        if (in < 0) {
	                            in = name.lastIndexOf('\\');
	                        }
	                        if (in > -1 && in < name.length() - 1) {
	                            /*
	                             * Directory is a child of a Zip directory
	                             * entry that can be retrieved from the map.
	                             */
	                            String key = name.substring(0, in);
	                            Source parent = map.get(key);
	                            if (parent != null) {
	                            	src = parent.addChildSource(src);
	                            }
	                        }
	                        else {
	                            /* Directory is a child of the Zip file. */
	                        	src = source.addChildSource(src);
	                        }
	                    }
	                }
	                else {
	                    Source src =
	                    	factory.getSource(jhove2, zip, entry);
	                    if (src != null) {
	                        /* Some FileSources may be children of a FileSetSource
	                         * if the filename includes a directory that is not
	                         * a Zip entry.  These FileSources should not be
	                         * characterized now, since we'll characterize the
	                         * FileSet later, including its children.
	                         */
	                        boolean hasFileSetParent = false;

	                        /* Check if the pathname includes a directory
	                         * component. Although the path separator always
	                         * should be a forward slash (/), in practice a
	                         * backward slash (\) may be found.
	                         */
	                        int in = name.lastIndexOf('/');
	                        if (in < 0) {
	                            in = name.lastIndexOf('\\');
	                        }
	                        if (in < 0) {
	                            /* File is a child of the Zip file. */
	                        	src = source.addChildSource(src);
	                        } else {
	                            /*
	                             * File is a child of a Zip file entry that can
	                             * be retrieved from the map.
	                             */
	                            String key = name.substring(0, in);
	                            Source parent = map.get(key);
	                            if (parent != null) {
	                                if (parent instanceof FileSetSource) {
	                                    hasFileSetParent = true;
	                                }
	                            	src = parent.addChildSource(src);
	                            }
	                            else {
	                                hasFileSetParent = true;
	                                /* The filename includes a parent directory,
	                                 * but that directory is not a Zip entry.
	                                 * Create a FileSetSource, a child of the
	                                 * Zip file and the parent of this file, to
	                                 * represent the directory. 
	                                 */
	                                parent = jhove2.getSourceFactory().getFileSetSource();
	                                parent = source.addChildSource(parent);
                                    map.put(key, parent);
	                                src = parent.addChildSource(src);
	                            }
	                        }
	                        /* Only characterize sources that are not children
	                         * of a FileSetSource.
	                         */
	                        if (!hasFileSetParent) {
	                            /* Make sure to close the Input after
	                             * characterization is completed.
	                             */
	                            Input inpt = src.getInput(jhove2);
	                            try {
	                                src = jhove2.characterize(src, inpt);
	                            }
	                            finally {
	                                if (inpt != null) {
	                                    inpt.close();
	                                }
                                }
                            }
	                    }
	                }
	            }
	            /* Make sure to characterize any FileSets that were created to
	             * represent non-Zip-entry directories.
	             */
	            Set<String> set = map.keySet();
	            Iterator<String> iter = set.iterator();
	            while (iter.hasNext()) {
	                String key = iter.next();
	                Source src = map.get(key);
	                if (src != null && src instanceof FileSetSource) {
                        /* Make sure to close the Input after
                         * characterization is completed.
                         */
	                    Input inpt = src.getInput(jhove2);
                        try {
                            src = jhove2.characterize(src, inpt);
                        }
                        finally {
                            if (inpt != null) {
                                inpt.close();
                            }
                        }
	                }
	            }
	        }  
	        finally {
	            zip.close();
	        }
	    }

		return consumed;
	}

    /** Validate the Zip file.
     * @param jhove2 JHOVE2 framework object
     * @param source Zip file source unit
     * @param input  Zip file source input
     * @see org.jhove2.module.format.Validator#validate(org.jhove2.core.JHOVE2, org.jhove2.core.source.Source, org.jhove2.core.io.Input)
     */
    @Override
    public Validity validate(JHOVE2 jhove2, Source source, Input input)
        throws JHOVE2Exception
    {
        return this.isValid();
    }
    
    /** Get validation coverage.
     * @return Validation coverage
     * @see org.jhove2.module.format.Validator#getCoverage()
     */
    @Override
    public Coverage getCoverage() {
        return COVERAGE;
    }
    
    /** Get Zip file entries.
     * @return Zip file entries
     */
    @ReportableProperty(order=1, value="Zip file entries")
    public List<ZipFileEntry> getZipFileEntries() {
        return this.entries;
    }
    
    /** Get validity.
     * @return Validity
     * @see org.jhove2.module.format.Validator#isValid()
     */
    @Override
    public Validity isValid()
    {
        return this.isValid;
    }
}
