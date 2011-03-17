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
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.reportable.Reportable;
import org.jhove2.persist.SourceAccessor;

/**
 * @author smorrissey
 *
 */
public interface SourceFactory {

	/**
	 * Get source unit from a formatted object name, which can be a file, a.
	 * directory, or a URL. Note that a URL source unit requires the
	 * creation of a temporary file.
	 * @param jhove2 JHOVE2 framework object
	 * @param name Formatted object name
	 * @return File, Directory, or URL Source unit
	 * @throws IOException
	 *             I/O exception instantiating source
	 * @throws JHOVE2Exception 
	 */
	public Source getSource(JHOVE2 jhove2, String name)
	    throws IOException, JHOVE2Exception;
 
	/**
	 * Get source unit from a Java {@link java.io.File}.
	 * 
     * @param jhove2 JHOVE2 framework object
	 * @param file
	 *            Java {@link java.io.File}
	 * @return File or Directory source unit
	 * @throws IOException
	 *             I/O exception instantiating source
	 * @throws JHOVE2Exception 
	 */
	public Source getSource(JHOVE2 jhove2, File file)
		throws IOException, JHOVE2Exception;

	/**
	 * Get URL source unit from a URL by creating a local temporary file.
	 * 
     * @param jhove2 JHOVE2 framework object
     * @param url
     *            URL
	 * @return URL source unit
	 * @throws IOException
	 *             I/O exception instantiating source
	 * @throws JHOVE2Exception 
	 */
	public Source getSource(JHOVE2 jhove2, URL url)
	    throws IOException, JHOVE2Exception;

	/**
	 * Get a source unit from an inputstream by creating a temporary file
	 * Used for example to create soure unit from a Zip file entry
     * @param jhove2 JHOVE2 framework object
	 * @param inputStream InputStream containing contents of Source
	 * @param name file name Stream would have within original container
	 * @param otherProperties non-file system reportable features associated with source
	 * @return Source unit
	 * @throws IOException
	 * @throws JHOVE2Exception 
	 */
	public Source getSource(JHOVE2 jhove2, InputStream inputStream, String name, Reportable otherProperties)
		throws IOException, JHOVE2Exception;

    /**
     * Get FileSet source unit from a formatted object name, which can be a file, a.
     * directory, or a URL. Note that a URL source unit requires the
     * creation of a temporary file.
     * 
     * @param jhove2 JHOVE2 framework object
     * @param name First formatted object name
     * @param names Remaining formatted object names
     * @return FileSet source unit
     * @throws IOException
     *             I/O exception instantiating source
     * @throws JHOVE2Exception 
     */
    public Source getSource(JHOVE2 jhove2, String name, String...names)
        throws IOException, JHOVE2Exception;
   
	/**
	 * Make FileSetSource from list of formatted objects, which may be files,
	 * directories, and URLS. Note that URL source units require the
	 * creation of temporary files.
	 * 
     * @param jhove2 JHOVE2 framework object
	 * @param names Formatted object names
	 * @return FileSet source
	 * @throws IOException
	 * @throws JHOVE2Exception
	 */
	public Source getSource(JHOVE2 jhove2, List<String> names)
		throws IOException, JHOVE2Exception;
	   
    /**
     * Utility method to create ByteStreamSource
     * @param jhove2 JHOVE2 framework object
     * @param parent Parent source unit
     * @param offset Starting offset
     * @param size   Size
     * @param name   Name, if known
     * @return ByteStream source unit
     * @throws JHOVE2Exception
     */
    public ByteStreamSource getByteStreamSource(JHOVE2 jhove2, Source parent,
                                                long offset, long size,
                                                String name) 
        throws IOException, JHOVE2Exception;
    
	/**
	 * Utility method to create new empty ClumpSource
     * @param jhove2 JHOVE2 framework object
	 * @return Clump source unit
	 * @throws JHOVE2Exception
	 */
	public ClumpSource getClumpSource(JHOVE2 jhove2) throws JHOVE2Exception;
	   
//    /**
//     * Utility method to create empty non-file system DirectorySource
//     * @param jhove2 JHOVE2 framework object
//     * @param name   Directory name 
//     * @return Directory source unit
//     * @throws IOException
//     * @throws JHOVE2Exception 
//     */
//    public DirectorySource getDirectorySource(JHOVE2 jhove2, String name)
//        throws IOException, JHOVE2Exception;
    
    /**
     * Utility method to create empty non-file system DirectorySource
     * @param jhove2 JHOVE2 framework object
     * @param name   Directory name 
     * @param isFileSystemDirectory boolean indicating if Directory is "actual" or just "virtual" 
     *           artifact of source processing
     * @return Directory source unit
     * @throws IOException
     * @throws JHOVE2Exception 
     */
    public DirectorySource getDirectorySource(JHOVE2 jhove2, String name, boolean isFileSystemDirectory)
        throws IOException, JHOVE2Exception;
	/**
	 * Utility method to create empty FileSetSource
     * @param jhove2 JHOVE2 framework object
	 * @return FileSet source unit
	 * @throws JHOVE2Exception 
	 */
	public FileSetSource getFileSetSource(JHOVE2 jhove2) throws JHOVE2Exception;

	/**
	 * Create SourceAccessor for this SourceFactory type
	 * This will be different, for example, for each persistence model 
	 * @param Source for which accessor is to be created
	 * @return SourceAccessor
	 */
	public SourceAccessor createSourceAccessor(Source source);

}
