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
import java.io.InputStream;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.Digest;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.format.Format;
import org.jhove2.core.io.Input;
import org.jhove2.core.source.DirectorySource;
import org.jhove2.core.source.Source;
import org.jhove2.core.source.SourceFactory;
import org.jhove2.module.digest.AbstractArrayDigester;
import org.jhove2.module.digest.CRC32Digester;
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
	            /* Zip entries (files and directories) are not necessarily in
	             * hierarchical order.  Also, directories may be implicit, that
	             * is, referred to in the pathnames of files or directories but
	             * not explicitly present in the form of a directory entry.
	             * 
	             * Since all files and directories need to be associated with 
	             * the correct parent directory in order for aggregate
	             * characterization to work properly, we there are three stages
	             * of processing:
	             * 
	             * (1) Identify all explicit directory entries, creating
	             *     Directory sources and putting them into a map keyed to
	             *     the directory pathname.
	             *     
	             * (2) Identify all implicit directories (by extracting
	             *     directories from pathnames and checking to see if they
	             *     are not already in the map), creating Directory sources
	             *     and putting them into the map.  Also characterize any
	             *     top-level file entries (children of the Zip file) that
	             *     are found.
	             *     
	             * (3) Directly characterize all top-level directories, that
	             *     is, those whose parent is the Zip file.  This will
	             *     implicitly characterize all child files and directories.
	             */
	            Map<String, Source> map = new TreeMap<String, Source>();
	            Enumeration<? extends ZipEntry> en = zip.entries();
	            SourceFactory factory = jhove2.getSourceFactory();
	    		if (factory == null){
	    			throw new JHOVE2Exception("JHOVE2 SourceFactory is null");
	    		}
	    		
	    		/* (1) Identify all directories that are explicit entries. */ 
	            while (en.hasMoreElements()) {
	                ZipEntry entry = en.nextElement();
	                if (entry.isDirectory()) {
	                	 String name = entry.getName();
	                	 /* Delete trailing slash from path name, if necessary. Although this
	        		     * always should be a forward slash (/), in practice a backward slash
	        		     * \) may be found.
	        		     */
	        		    int in = name.lastIndexOf('/');
	        		    if (in < 0) {
	        		        in = name.lastIndexOf('\\');
	        		    }
	        		    if (in == name.length() - 1) {
	        		        name = name.substring(0, in);
	        		    }
//	                    Source src =
//	                    	factory.getSource(jhove2, zip, entry);
	                    Source src =
	        		    		factory.getDirectorySource(jhove2, name, false);
	                    if (src != null) {
	                    	/* Get the entry-specific properties. */
		        	        long crc = entry.getCrc();
		        	        Digest crc32 = new Digest(AbstractArrayDigester.toHexString(crc),
		        	                                  CRC32Digester.ALGORITHM);
		        			ZipEntryProperties properties =
		        			    new ZipEntryProperties(name, entry.getCompressedSize(), crc32,
		        			                           entry.getComment(),
		        			                           new Date(entry.getTime()));
		        			src = src.addExtraProperties(properties);
		        			
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

                /* (2) Characterize each file entry and associate it with its
	             * parent source unit.  Directory entries are not characterized
	             * now, since all of their child files may not yet be
	             * associated with them.
	             */
	            /* Identify all directories that are not implicit entries but
	             * are implied by file and directory pathnames.  Also, create
	             * File source units for file entries, and if they are top-
	             * level entries, that is, child of the Zip file, characterize
	             * them.  Lower level file entries (and directories) will be 
	             * characterized later on as part of the recursive processing
	             * of top-level directories.
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
	                }
	                /* Check to make sure all directories implied in the
	                 * pathname are also in the map.
	                 */
	                checkForImpliedDirectories(jhove2, name, map, source,
	                                           factory);
	                if (entry.isDirectory()) {
	                    Source src = map.get(name);
	                    
	                    /* Retrieve directory parent from the map. Although
	                     * the path separator always should be a forward slash
	                     * (/), in practice a backward slash (\) may be found. */
	                    int in = name.lastIndexOf('/');
                        if (in < 0) {
                            in = name.lastIndexOf('\\');
                        }
                        if (in > -1) {
                            /* Directory is a child of a Directory retrievable
                             * from the map.
                             */
                            String key = name.substring(0, in);
                            Source parent = map.get(key);
                            src = parent.addChildSource(src);
                        }
                        else {
                            /* Directory is a child of the Zip file. */
                            src = source.addChildSource(src);
                        }
	                }
	                else {  /* Entry is a file. */
//	                    Source src =
//	                    	factory.getSource(jhove2, zip, entry);
	            		/* Recover the filename from the pathname. Although the path
	            		 * separator always should be a forward slash (/), in practice a
	            		 * backward slash (\) may be found.
	            		 */
	            		int in = name.lastIndexOf('/');
	            		if (in < 0) {
	            			in = name.lastIndexOf('\\');
	            		}
	            		if (in > -1) {
	            			name = name.substring(in+1);
	            		}
	            		/* Create a temporary Java {@link java.io.File} to represent the
	            		 * file entry.
	            		 */
	            		InputStream stream = zip.getInputStream(entry);
	            		/* Get the entry-specific properties. */
	            		long crc = entry.getCrc();
	            		Digest crc32 = new Digest(AbstractArrayDigester.toHexString(crc),
	            				CRC32Digester.ALGORITHM);
	            		ZipEntryProperties properties =
	            			new ZipEntryProperties(name, entry.getCompressedSize(), crc32,
	            					entry.getComment(),
	            					new Date(entry.getTime()));
	                    Source src =
	            			factory.getSource(jhove2, stream, name, properties);
	                    if (src != null) {
	                        /* Check if the file pathname includes a directory
	                         * component. Although the path separator always
	                         * should be a forward slash (/), in practice a
	                         * backward slash (\) may be found.
	                         */
	                    	name = entry.getName();
	                        in = name.lastIndexOf('/');
	                        if (in < 0) {
	                            in = name.lastIndexOf('\\');
	                        }
	                        if (in > -1) {
	                            /* File is a child of a Directory retrievable
	                             * from the map.
	                             */
	                            String key = name.substring(0, in);
	                            Source parent = map.get(key);
	                            src = parent.addChildSource(src);
	                        }
	                        else {
                                /* File is a child of the Zip file and can be
                                 * characterized now.  All other files will be
                                 * characterized later as part of the recursive
                                 * characterization of top-level directories.
                                 */
                                src = source.addChildSource(src);

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
	            
	            /* (3) Characterize all top-level directories, implicitly
	             * characterizing all lower-level files and directories.
	             */
	            List<Source> list = source.getChildSources();
	            Iterator<Source> iter = list.iterator();
	            while (iter.hasNext()) {
	                Source src = iter.next();
	                if (src instanceof DirectorySource) {
                        /* Make sure to close the Input after characterization
                         * is completed.
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
    
//    /** Get Zip file entries.
//     * @return Zip file entries
//     */
//    @ReportableProperty(order=1, value="Zip file entries")
//    public List<ZipFileEntry> getZipFileEntries() {
//        return this.entries;
//    }
    
    /** Get validity.
     * @return Validity
     * @see org.jhove2.module.format.Validator#isValid()
     */
    @Override
    public Validity isValid()
    {
        return this.isValid;
    }
    
    /** Check for directories implied by the pathnames for file and directory
     * entries.
     * @param jhove2  JHOVE2 framework object
     * @param name    File or directory entry pathname
     * @param map     Map of directories
     * @param source  Zip file source unit
     * @param factory Source factory
     * @throws JHOVE2Exception 
     * @throws IOException 
     * 
     */
    protected void checkForImpliedDirectories(JHOVE2 jhove2, String name,
                                              Map<String, Source> map,
                                              Source source,
                                              SourceFactory factory)
        throws IOException, JHOVE2Exception
    {
        Source parent = source;
        int n = 0;
        boolean again = true;
        
        /* Check each directory in the path.  If it is not already in the map,
         * create a new Directory source, add it to the map, and add it as a
         * child of the appropriate directory or the Zip file itself.
         */
        while (again) {
            /* Although the path separator always should be a forward slash
             * (/), in practice a backward slash (\) may be found.
             */
            int in = name.indexOf('/', n);
            if (in < 0) {
                in = name.indexOf('\\', n);
            }
            if (in > 0) {
                String key = name.substring(0, in);
            
                /* If the directory is not in the map, add it. */
                Source src = map.get(key);
                if (src == null) {
                    src = factory.getDirectorySource(jhove2, key, false);
                    src = parent.addChildSource(src);
                    map.put(key, src);
                    parent = src;
                }
            
                n = in + 1;
            }
            else {
                again = false;
            }
        }
    }
}
