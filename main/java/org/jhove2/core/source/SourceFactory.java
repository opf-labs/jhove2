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
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.jhove2.core.JHOVE2Exception;

/**
 * Factory for JHOVE2 file and directory source units.
 * 
 * @author mstrong, slabrams, smorrissey
 */
public class SourceFactory
{
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
	 */
	public static synchronized Source getSource(String pathName)
		throws FileNotFoundException, IOException
	{
		// TODO: check if file exists and if not, throw FileNotFound exception
		return getSource(new File(pathName));
	}

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
	 */
	public static synchronized Source getSource(File file)
		throws FileNotFoundException, IOException
	{
		if (file.isDirectory()) {
			return new DirectorySource(file);
		}

		return new FileSource(file);
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
	 * @return Source unit
	 * @throws IOException
	 *             I/O exception instantiating source
	 */
	public static synchronized Source getSource(String tmpPrefix,
			                                    String tmpSuffix,
			                                    int bufferSize, URL url)
		throws IOException
	{
		return new URLSource(tmpSuffix, tmpSuffix, bufferSize, url);
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
	 * @return Source unit
	 * @throws IOException
	 *             I/O exception instantiating source
	 */
	public static synchronized Source getSource(String tmpPrefix,
			                                    String tmpSuffix,
			                                    int bufferSize, ZipFile zip,
			                                    ZipEntry entry)
		throws IOException
	{
		InputStream stream = zip.getInputStream(entry);
		if (entry.isDirectory()) {
			return new ZipDirectorySource(stream, entry);
		}

		return new ZipFileSource(tmpSuffix, tmpSuffix, bufferSize, stream, entry);
	}	

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
	public static synchronized Source getSource(String pathName, String... pathNames)
	throws IOException, JHOVE2Exception {
		List<String> list = new ArrayList<String>();
		list.add(pathName);
		if (pathNames != null && pathNames.length > 0) {
			for (int i = 0; i < pathNames.length; i++) {
				list.add(pathNames[i]);
			}
		}
		return SourceFactory.getSource(list);
	}

	/**
	 * Make Source from list of file system objects (files and directories).
	 * 
	 * @param pathNames
	 *            File system path names
	 * @return Source
	 * @throws IOException
	 * @throws JHOVE2Exception
	 */
	public static synchronized Source getSource(List<String> pathNames)
		throws IOException, JHOVE2Exception
	{
		Source source = null;
		Iterator<String> iter = pathNames.iterator();
		// TODO: handle FileNotFound exception in getSource()
		if (pathNames.size() == 1) {
			String pathName = iter.next();
			source = SourceFactory.getSource(pathName);
		}
		else {
			source = new FileSetSource();
			while (iter.hasNext()) {
				String pathName = iter.next();
				Source src = SourceFactory.getSource(pathName);
				((FileSetSource) source).addChildSource(src);
			}
		}
		return source;
	}
}
