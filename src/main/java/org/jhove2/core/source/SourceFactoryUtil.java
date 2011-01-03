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
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.Invocation;

/**
 * Factory for JHOVE2 file and directory source units.
 * 
 * @author mstrong, slabrams, smorrissey
 */
public class SourceFactoryUtil
{
	/**
	 * Get source unit from a file system path name.
	 * 
	 * @param pathName
	 *            File system path names
	 * @param sourceFactory SourceFactory to configure Accessors for source
	 * @return Source unit
	 * @throws FileNotFoundException
	 *             File not found
	 * @throws IOException
	 *             I/O exception instantiating source
	 * @throws JHOVE2Exception 
	 */
	public static synchronized Source getSource(String pathName, SourceFactory sourceFactory)
		throws FileNotFoundException, IOException, JHOVE2Exception
	{
		return getSource(new File(pathName), sourceFactory);
	}

	/**
	 * Get source unit from a Java {@link java.io.File}.
	 * 
	 * @param file
	 *            Java {@link java.io.File}
	 * @param sourceFactory  SourceFactory which configures accessors for this source
	 * @return Source unit
	 * @throws FileNotFoundException
	 *             File not found
	 * @throws IOException
	 *             I/O exception instantiating source
	 * @throws JHOVE2Exception 
	 */
	public static synchronized Source getSource(File file, SourceFactory sourceFactory)
		throws FileNotFoundException, IOException, JHOVE2Exception
	{
		Source source;
		if (file.isDirectory()) {
			
			source =  new DirectorySource(file, sourceFactory);
		}
		else{
			source =  new FileSource(file);
		}
		if (source.getSourceAccessor()==null){
			source.setSourceAccessor(sourceFactory.createSourceAccessor(source));
		}
		return source;
	}

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
	 * @param sourceFactory SourceFactory which configures accessors for this source
	 * @return Source unit
	 * @throws IOException
	 *             I/O exception instantiating source
	 */
	public static synchronized Source getSource(String tmpPrefix,
			                                    String tmpSuffix,
			                                    int bufferSize, URL url, 
			                                    SourceFactory sourceFactory)
		throws IOException
	{
		Source source = new URLSource(tmpSuffix, tmpSuffix, bufferSize, url);
		source.setSourceAccessor(sourceFactory.createSourceAccessor(source));
		return source;
	}

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
	 * @param sourceFactory 
	 * 			SourceFactory which configures accessors for this source
	 * @return Source unit
	 * @throws IOException
	 *             I/O exception instantiating source
	 */
	public static synchronized Source getSource(String tmpPrefix,
			                                    String tmpSuffix,
			                                    int bufferSize, 
			                                    ZipFile zip,
			                                    ZipEntry entry, 
			                                    SourceFactory sourceFactory)
		throws IOException
	{
		Source source;
		InputStream stream = zip.getInputStream(entry);
		if (entry.isDirectory()) {
			source =  new ZipDirectorySource(stream, entry);
		}
		else {
			source = new ZipFileSource(tmpSuffix, tmpSuffix, bufferSize, stream, entry);
		}
		if (source.getSourceAccessor()==null){
			source.setSourceAccessor(sourceFactory.createSourceAccessor(source));
		}
		return source;
	}	

	/**
	 * Get source unit from sequence of file system objects (files and
	 * directories).
	 * @param pathName
	 *            First path name
	 * @param sourceFactory 
	 * 			SourceFactory which configures accessors for this source
	 * @param pathNames
	 *            Remaining path names
	 * @return Source
	 * @throws IOException
	 * @throws JHOVE2Exception
	 */
	public static synchronized Source getSource(String pathName, 
			SourceFactory sourceFactory, String... pathNames)
	throws IOException, JHOVE2Exception {
		List<String> list = new ArrayList<String>();
		list.add(pathName);
		if (pathNames != null && pathNames.length > 0) {
			for (int i = 0; i < pathNames.length; i++) {
				list.add(pathNames[i]);
			}
		}
		return SourceFactoryUtil.getSource(list, sourceFactory);
	}

	/**
	 * Make Source from list of file system objects (files and directories) and URLS
	 * 
	 * @param pathNames
	 *            File system path names
	 * @param sourceFactory 
	 * 			SourceFactory which configures accessors for this source
	 * @return Source
	 * @throws IOException
	 * @throws JHOVE2Exception
	 */
	public static synchronized Source getSource(List<String> pathNames, SourceFactory sourceFactory)
		throws FileNotFoundException, IOException, JHOVE2Exception
	{
		return SourceFactoryUtil.getSource(
				pathNames, 
				Invocation.DEFAULT_TEMP_PREFIX,
				Invocation.DEFAULT_TEMP_SUFFIX,
				Invocation.DEFAULT_BUFFER_SIZE, sourceFactory);
	}
	
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
	 * @param sourceFactory 
	 * 			SourceFactory which configures accessors for this source
	 * @return Source
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws JHOVE2Exception
	 */
	public static synchronized Source getSource(List<String> pathNames, String tmpPrefix,
            String tmpSuffix,
            int bufferSize, 
            SourceFactory sourceFactory)
		throws FileNotFoundException, IOException, JHOVE2Exception
	{
		Source source = null;		
		if (pathNames.size() == 1) {
			
			String pathName = pathNames.get(0);
			try{
				URL url = new URL(pathName);
				source = SourceFactoryUtil.getSource(tmpPrefix, tmpSuffix, bufferSize, url, sourceFactory);
				source.setSourceAccessor(sourceFactory.createSourceAccessor(source));
			}
			catch (MalformedURLException m){
				source = SourceFactoryUtil.getSource(pathName,sourceFactory);
				if (source.getSourceAccessor()==null){
					source.setSourceAccessor(sourceFactory.createSourceAccessor(source));
				}
			}			
		}
		else {
			source = new FileSetSource();
			source.setSourceAccessor(sourceFactory.createSourceAccessor(source));
			Iterator<String> iter = pathNames.iterator();
			while (iter.hasNext()) {
				String pathName = iter.next();
				Source src = null;
				try{
					URL url = new URL(pathName);
					src = SourceFactoryUtil.getSource(tmpPrefix, tmpSuffix, bufferSize, url, sourceFactory);
				}
				catch (MalformedURLException m){
					src = SourceFactoryUtil.getSource(pathName, sourceFactory);
				}
				src=((FileSetSource)source).addChildSource(src);
			}
		}
		
		return source;
	}
	/**
	 * Create new ClumpSource
	 * @param sourceFactory
	 * 			SourceFactory which configures accessors for this source
	 * @return ClumpSource
	 * @throws JHOVE2Exception 
	 */
	public static synchronized ClumpSource getClumpSource(SourceFactory sourceFactory) 
	throws JHOVE2Exception{		
		ClumpSource source = new ClumpSource();
		source.setSourceAccessor(sourceFactory.createSourceAccessor(source));
//		source = (ClumpSource)source.getSourceAccessor().persistSource(source);
		return source;
	}
	
	/**
	 * Create new empty FileSetSource
	 * @param sourceFactory
	 * 			SourceFactory which configures accessors for this source
	 * @return FileSetSource
	 */
	public static synchronized FileSetSource getFileSetSource(SourceFactory sourceFactory){
		FileSetSource source = new FileSetSource();
		source.setSourceAccessor(sourceFactory.createSourceAccessor(source));
		return source;
	}
	
	public static synchronized ByteStreamSource getByteStreamSource (SourceFactory sourceFactory,
			JHOVE2 jhove2, Source parent, long offset, long size) throws IOException, JHOVE2Exception{
		ByteStreamSource source = new ByteStreamSource(jhove2, parent, offset, size, sourceFactory);
//		source = (ByteStreamSource)source.getSourceAccessor().persistSource(source);
		return source;
	}
}
