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

import org.jhove2.core.JHOVE2Exception;

/**
 * Factory for JHOVE2 file and directory source units.
 * 
 * @author mstrong, slabrams, smorrissey
 */
public class SourceFactoryUtil
{
	/**
	 * Get source unit from a file, directory or URL object name.
	 * 
	 * @param name Object name, either a file name, directory name, or a URL
	 * @param sourceFactory SourceFactory to configure Accessors for source
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
	public static synchronized Source getSource(String name,
	                                            SourceFactory sourceFactory,
	                                            File tmpDirectory,
	                                            String tmpPrefix,
	                                            String tmpSuffix,
	                                            int bufferSize)
		throws FileNotFoundException, IOException, JHOVE2Exception
	{
	    Source source = null;
	    
	    /* First check if the name is a URL. If not, assume it's a file or
	     * directory name.
	     */
        try{
            URL url = new URL(name);
            source = SourceFactoryUtil.getSource(url, sourceFactory,
                                                 tmpDirectory, tmpPrefix,
                                                 tmpSuffix, bufferSize);
        }
        catch (MalformedURLException m){
            File file = new File(name);
            source = SourceFactoryUtil.getSource(file, sourceFactory);
        }
        
		return source;
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
		Source source = null;
		if (file.isDirectory()) {		
			source = new DirectorySource(file, sourceFactory);
			/* Note, the DirectorySource constructor automatically associates
			 * the source factory, so we don't need to do it here.
			 */
		}
		else{
			source = new FileSource(file);
			source.setSourceAccessor(sourceFactory.createSourceAccessor(source));
		}
		
		return source;
	}

	/**
	 * Get source unit from a URL by creating a local temporary file.
     * @param url URL
     * @param sourceFactory SourceFactory which configures accessors for this source
     * @param tmpDirectory Temporary directory
	 * @param tmpPrefix
	 *            Temporary file prefix
	 * @param tmpSuffix
	 *            Temporary file suffix
	 * @param bufferSize
	 *            Buffer size (for creating temporary file)
	 * 
	 * @return Source unit
	 * @throws IOException
	 *             I/O exception instantiating source
	 */
	public static synchronized Source getSource(URL url,
	                                            SourceFactory sourceFactory,
	                                            File tmpDirectory,
	                                            String tmpPrefix,
	                                            String tmpSuffix,
			                                    int bufferSize)
		throws IOException
	{
	    /* Try to recover the format extension from the URL path. If not
	     * available, use the temporary suffix.
	     */
	    String ext  = tmpSuffix;
	    String path = url.getPath();
	    int in = path.lastIndexOf('.');
	    if (in > 0) {
	        ext = path.substring(in);
	    }
		Source source = new URLSource(url, tmpDirectory, tmpPrefix, ext,
		                              bufferSize);
		source.setSourceAccessor(sourceFactory.createSourceAccessor(source));
		return source;
	}

	/**
	 * Get source unit from a Zip file entry by creating a temporary file.
     * @param zip
     *            Zip file
     * @param entry
     *            Zip file entry
     * @param sourceFactory 
     *          SourceFactory which configures accessors for this source
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
	 */
	public static synchronized Source getSource(ZipFile zip, ZipEntry entry,
	                                            SourceFactory sourceFactory,
	                                            File tmpDirectory,
	                                            String tmpPrefix,
			                                    String tmpSuffix,
			                                    int bufferSize)
		throws IOException
	{
		Source source = null;
		if (entry.isDirectory()) {
			source = new ZipDirectorySource(entry);
		}
		else {
		    /* Try to recover file extension.  If not available, use the 
		     * temporary suffix.
		     */
		    String ext = tmpSuffix;
		    String name = entry.getName();
		    int in = name.lastIndexOf('.');
		    if (in > 0) {
		        ext = name.substring(in);
		    }
	        InputStream stream = zip.getInputStream(entry);
			source = new ZipFileSource(entry, stream, tmpDirectory, tmpPrefix,
			                           ext, bufferSize);
	        stream.close();
		}
		source.setSourceAccessor(sourceFactory.createSourceAccessor(source));

		return source;
	}	
	
	/**
	 * Make Source from list of formatted objects, which may be files,
	 * directories. and URLS
	 * 
	 * @param names
	 *            Object names, which may be files, directories, or URLs
	 * @param tmpDirectory Temporary directory
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
	public static synchronized Source getSource(SourceFactory sourceFactory,
	                                            File tmpDirectory,
	                                            String tmpPrefix,
	                                            String tmpSuffix,
	                                            int bufferSize,
	                                            String name, String...names)
		throws FileNotFoundException, IOException, JHOVE2Exception
	{
	    List<String> list = new ArrayList<String>();
        list.add(name);
        if (names != null && names.length > 0) {
            for (int i = 0; i < names.length; i++) {
                list.add(names[i]);
            }
        }
        return SourceFactoryUtil.getSource(list, sourceFactory, tmpDirectory,
                                           tmpPrefix, tmpSuffix, bufferSize);
	}
	   
    /**
     * Make Source from list of formatted objects, which may be files,
     * directories. and URLS
     * 
     * @param names
     *            Object names, which may be files, directories, or URLs
     * @param tmpDirectory Temporary directory
     * @param tmpPrefix
     *            Prefix for any temp files created
     * @param tmpSuffix
     *            Suffix for any temp files created
     * @param bufferSize
     *            Buffer size for reading URLS
     * @param sourceFactory 
     *          SourceFactory which configures accessors for this source
     * @return Source
     * @throws FileNotFoundException
     * @throws IOException
     * @throws JHOVE2Exception
     */
    public static synchronized Source getSource(List<String> pathNames, 
                                                SourceFactory sourceFactory,
                                                File tmpDirectory,
                                                String tmpPrefix,
                                                String tmpSuffix,
                                                int bufferSize)
        throws FileNotFoundException, IOException, JHOVE2Exception
    {
        Source source = null;       
        if (pathNames.size() == 1) {
            String name = pathNames.get(0);
            source = SourceFactoryUtil.getSource(name, sourceFactory,
                                                 tmpDirectory, tmpPrefix,
                                                 tmpSuffix, bufferSize);        
        }
        else {
            source = new FileSetSource();
            source.setSourceAccessor(sourceFactory.createSourceAccessor(source));
            Iterator<String> iter = pathNames.iterator();
            while (iter.hasNext()) {
                Source src = null;
                String name = iter.next();
                src = SourceFactoryUtil.getSource(name, sourceFactory,
                                                  tmpDirectory, tmpPrefix,
                                                  tmpSuffix, bufferSize);
                src = ((FileSetSource)source).addChildSource(src);
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
	    throws JHOVE2Exception
	{		
		ClumpSource source = new ClumpSource();
		source.setSourceAccessor(sourceFactory.createSourceAccessor(source));
		return source;
	}
	
	/**
	 * Create new empty FileSetSource
	 * @param sourceFactory
	 * 			SourceFactory which configures accessors for this source
	 * @return FileSetSource
	 */
	public static synchronized FileSetSource getFileSetSource(SourceFactory sourceFactory) {
		FileSetSource source = new FileSetSource();
		source.setSourceAccessor(sourceFactory.createSourceAccessor(source));
		return source;
	}
	
	public static synchronized ByteStreamSource getByteStreamSource(Source parent,
	                                                                long offset,
	                                                                long size,
	                                                                SourceFactory sourceFactory,
	                                                                File tmpDirectory,
	                                                                String tmpPrefix,
	                                                                String tmpSuffix,
	                                                                int bufferSize)
	    throws IOException, JHOVE2Exception
	{
		ByteStreamSource source = new ByteStreamSource(parent, offset, size,
		                                               sourceFactory,
		                                               tmpDirectory, tmpPrefix,
		                                               tmpSuffix, bufferSize);
		return source;
	}
}
