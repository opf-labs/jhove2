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

import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.persist.SourceAccessor;

/**
 * @author smorrissey
 *
 */
public interface SourceFactory {

	/**
	 * Get source unit from a file system path name.
	 * 
	 * @param pathName
	 *            File system path name
	 * @return Source unit
	 * @throws FileNotFoundException
	 *             File not found
	 * @throws IOException
	 *             I/O exception instantiating source
	 * @throws JHOVE2Exception 
	 */
	public Source getSource(String pathName)
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
	 * @param tmpPrefix
	 *            Temporary file prefix
	 * @param tmpSuffix
	 *            Temporary file suffix
	 * @param bufferSize
	 *            Buffer size for transfer to temporary file
	 * @param url
	 *            URL
	 * @return Source unit
	 * @throws IOException
	 *             I/O exception instantiating source
	 * @throws JHOVE2Exception 
	 */
	public Source getSource(String tmpPrefix,String tmpSuffix,
			                                    int bufferSize, URL url)
	throws IOException, JHOVE2Exception;

	/**
	 * Get source unit from a Zip file entry by creating a temporary file.
	 * 
	 * @param tmpPrefix
	 *            Temporary file prefix
	 * @param tmpSuffix
	 *            Temporary file suffix
	 * @param bufferSize
	 *            Buffer size during transfer to temporary file
	 * @param zip
	 *            Zip file
	 * @param entry
	 *            Zip file entry
	 * @return Source unit
	 * @throws IOException
	 *             I/O exception instantiating source
	 * @throws JHOVE2Exception 
	 */
	public Source getSource(String tmpPrefix,
			                                    String tmpSuffix,
			                                    int bufferSize, ZipFile zip,
			                                    ZipEntry entry)
		throws IOException, JHOVE2Exception;

	/**
	 * Get source unit from sequence of file system objects (files and
	 * directories).
	 * 
	 * @param pathName
	 *            First path name
	 * @param pathNames
	 *            Remaining path names
	 * @return Source
	 * @throws IOException
	 * @throws JHOVE2Exception
	 */
	public Source getSource(String pathName, String... pathNames)
	throws IOException, JHOVE2Exception ;
	/**
	 * Make Source from list of file system objects (files and directories) and URLS
	 * 
	 * @param pathNames
	 *            File system path names
	 * @return Source
	 * @throws IOException
	 * @throws JHOVE2Exception
	 */
	public Source getSource(List<String> pathNames)
		throws FileNotFoundException, IOException, JHOVE2Exception;
	
	/**
	 * Make Source from list of file system objects (files and directories) and URLS
	 * 
	 * @param pathNames
	 *            File system path names
	 * @param tmpPrefix
	 * 			  Prefix for any temp files created
	 * @param tmpSuffix
	 * 			  Suffix for any temp files created
	 * @param bufferSize
     *            Buffer size for reading URLS
	 * @return Source
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws JHOVE2Exception
	 */
	public Source getSource(List<String> pathNames, String tmpPrefix,
            String tmpSuffix,
            int bufferSize)
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
	public ByteStreamSource getByteStreamSource(JHOVE2 jhove2, Source parent, long offset, long size) 
	throws IOException, JHOVE2Exception;
}
