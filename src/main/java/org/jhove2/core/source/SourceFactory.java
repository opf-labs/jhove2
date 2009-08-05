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
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.jhove2.core.JHOVE2;

/**
 * Factory for JHOVE2 file and directory source units.
 * 
 * @author mstrong, slabrams
 */
public abstract class SourceFactory {
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
			throws FileNotFoundException, IOException {
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
			throws FileNotFoundException, IOException {
		if (file.isDirectory()) {
			return new DirectorySource(file);
		}

		return new FileSource(file);
	}

	/**
	 * Get source unit from a URL.
	 * 
	 * @param jhove2
	 *            JHOVE2 framework
	 * @param url
	 *            URL
	 * @return Source unit
	 * @throws IOException
	 *             I/O exception instantiating source
	 */
	public static synchronized Source getSource(JHOVE2 jhove2, URL url)
			throws IOException {
		return new URLSource(jhove2, url);
	}

	/**
	 * Get source unit from a Zip file entry.
	 * 
	 * @param jhove2
	 *            JHOVE2 framework
	 * @param zip
	 *            Zip file
	 * @param entry
	 *            Zip file entry
	 * @return Source unit
	 * @throws IOException
	 *             I/O exception instantiating source
	 */
	public static synchronized Source getSource(JHOVE2 jhove2, ZipFile zip,
			ZipEntry entry) throws IOException {
		InputStream stream = zip.getInputStream(entry);
		if (entry.isDirectory()) {
			return new ZipDirectorySource(stream, entry);
		}

		return new ZipFileSource(jhove2, stream, entry);
	}
}
