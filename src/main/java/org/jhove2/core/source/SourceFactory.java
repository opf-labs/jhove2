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
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.jhove2.core.JHOVE2Exception;
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
	 * @param name Formatted object name
     * @param tmpDirectory Temporary directory
     * @param tmpPrefix Temporary prefix
     * @param tmpSuffix Temporary suffix
     * @param bufferSize Buffer size (for creating temporary file)
	 * @return Source unit
	 * @throws FileNotFoundException
	 *             File not found
	 * @throws IOException
	 *             I/O exception instantiating source
	 * @throws JHOVE2Exception 
	 */
	public Source getSource(String name, File tmpDirectory, String tmpPrefix,
	                        String tmpSuffix, int bufferSize)
	    throws FileNotFoundException, IOException, JHOVE2Exception;
 
	/**
	 * Get source unit from a Java {@link java.io.File}.
	 * 
	 * @param file
	 *            Java {@link java.io.File}
	 * @return Source unit
	 * @throws FileNotFoundException
	 *             File not found
	 * @throws IOException
	 *             I/O exception instantiating source
	 * @throws JHOVE2Exception 
	 */
	public Source getSource(File file)
		throws FileNotFoundException, IOException, JHOVE2Exception;

	/**
	 * Get source unit from a URL by creating a local temporary file.
	 * 
     * @param url
     *            URL
	 * @param tmpDirectory Temporary directory
	 * @param tmpPrefix
	 *            Temporary file prefix
	 * @param tmpSuffix
	 *            Temporary file suffix
	 * @param bufferSize
	 *            Buffer size (for creating temporary file)
	 * @return Source unit
	 * @throws IOException
	 *             I/O exception instantiating source
	 * @throws JHOVE2Exception 
	 */
	public Source getSource(URL url, File tmpDirectory, String tmpPrefix,
	                        String tmpSuffix, int bufferSize)
	    throws IOException, JHOVE2Exception;

	/**
	 * Get source unit from a Zip file entry by creating a temporary file.
	 * 
     * @param zip
     *            Zip file
     * @param entry
     *            Zip file entry
     * @param tmpDirectory            
     *            Temporary directory
	 * @param tmpPrefix
	 *            Temporary file prefix
	 * @param tmpSuffix
	 *            Temporary file suffix
	 * @param bufferSize
	 *            Buffer size (for creating temporary file)
	 * @return Source unit
	 * @throws IOException
	 *             I/O exception instantiating source
	 * @throws JHOVE2Exception 
	 */
	public Source getSource(ZipFile zip, ZipEntry entry, File tmpDirectory,
	                        String tmpPrefix, String tmpSuffix, int bufferSize)
		throws IOException, JHOVE2Exception;

    /**
     * Get source unit from a formatted object name, which can be a file, a.
     * directory, or a URL. Note that a URL source unit requires the
     * creation of a temporary file.
     * @param tmpDirectory Temporary directory
     * @param tmpPrefix Temporary prefix
     * @param tmpSuffix Temporary suffix
     * @param bufferSize Buffer size (for creating temporary file)
     * @param name First formatted object name
     * @param names Remaining formatted object names
     * @return Source unit
     * @throws FileNotFoundException
     *             File not found
     * @throws IOException
     *             I/O exception instantiating source
     * @throws JHOVE2Exception 
     */
    public Source getSource(File tmpDirectory, String tmpPrefix,
                            String tmpSuffix, int bufferSize,
                            String name, String...names)
        throws FileNotFoundException, IOException, JHOVE2Exception;
   
	/**
	 * Make Source from list of formatted objects, which may be files,
	 * directories, and URLS. Note that URL source units require the
	 * creation of temporary files.
	 * @param names Formatted object names
     * @param tmpDirectory            
     *            Temporary directory
     * @param tmpPrefix
     *            Temporary file prefix
     * @param tmpSuffix
     *            Temporary file suffix
     * @param bufferSize
     *            Buffer size (for creating temporary file)
	 * @return Source
	 * @throws IOException
	 * @throws JHOVE2Exception
	 */
	public Source getSource(List<String> names, File tmpDirectory,
	                        String tmpPrefix, String tmpSuffix, int bufferSize)
		throws FileNotFoundException, IOException, JHOVE2Exception;
	
	/**
	 * Utility method to create new empty ClumpSource
	 * @return ClumpSource
	 * @throws JHOVE2Exception
	 */
	public ClumpSource getClumpSource() throws JHOVE2Exception;
	
	/**
	 * Utility method to create empty FileSetSource
	 * @return FileSetSource
	 * @throws JHOVE2Exception 
	 */
	public FileSetSource getFileSetSource() throws JHOVE2Exception;

	/**
	 * Create SourceAccessor for this SourceFactory type
	 * This will be different, for example, for each persistence model 
	 * @param Source for which accessor is to be created
	 * @return SourceAccessor
	 */
	public SourceAccessor createSourceAccessor(Source source);
	
	/**
	 * Utility method to create ByteStreamSource
	 * @return ByteStreamSource
	 * @throws JHOVE2Exception
	 */
	public ByteStreamSource getByteStreamSource(Source parent, long offset,
	                                            long size, File tmpDirectory,
	                                            String tmpPrefix,
	                                            String tmpSuffix,
	                                            int bufferSize) 
	    throws IOException, JHOVE2Exception;
}
