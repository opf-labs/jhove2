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
	 * @return Source unit
	 * @throws FileNotFoundException
	 *             File not found
	 * @throws IOException
	 *             I/O exception instantiating source
	 * @throws JHOVE2Exception 
	 */
	public static synchronized Source getSource(JHOVE2 jhove2, String name,
	                                            SourceFactory sourceFactory)
		throws FileNotFoundException, IOException, JHOVE2Exception
	{
	    Source source = null;
	    
	    /* First check if the name is a URL. If not, assume it's a file or
	     * directory name.
	     */
        try{
            URL url = new URL(name);
            source = SourceFactoryUtil.getSource(jhove2, url, sourceFactory);
        }
        catch (MalformedURLException m){
            File file = new File(name);
            source = SourceFactoryUtil.getSource(jhove2, file, sourceFactory);
        }
        
		return source;
	}

	/**
	 * Get source unit from a Java {@link java.io.File}.
	 * 
     * @param jhove2 JHOVE2 framework object
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
	public static synchronized Source getSource(JHOVE2 jhove2, File file,
	                                            SourceFactory sourceFactory)
		throws FileNotFoundException, IOException, JHOVE2Exception
	{
		Source source = null;
		if (file.isDirectory()) {		
			source = new DirectorySource(jhove2, file, sourceFactory);
			/* Note, the DirectorySource constructor automatically associates
			 * the source factory, so we don't need to do it here.
			 */
		}
		else{
			source = new FileSource(jhove2, file);
			source.setSourceAccessor(sourceFactory.createSourceAccessor(source));
		}
		
		return source;
	}

	/**
	 * Get source unit from a URL by creating a local temporary file.
     * @param jhove2 JHOVE2 framework object
     * @param url URL
     * @param sourceFactory SourceFactory which configures accessors for this source
	 * @return URL source unit
	 * @throws IOException
	 *             I/O exception instantiating source
	 */
	public static synchronized Source getSource(JHOVE2 jhove2, URL url,
	                                            SourceFactory sourceFactory)
		throws IOException
	{
		Source source = new URLSource(jhove2, url);
		source.setSourceAccessor(sourceFactory.createSourceAccessor(source));
		return source;
	}

	/**
	 * Get source unit from a Zip file entry by creating a temporary file.
	 * @param jhove2 JHOVE2 framework object
     * @param zip
     *            Zip file
     * @param entry
     *            Zip file entry
     * @param sourceFactory 
     *          SourceFactory which configures accessors for this source
	 * @return Source unit
	 * @throws IOException
	 *             I/O exception instantiating source
	 */
	public static synchronized Source getSource(JHOVE2 jhove2, ZipFile zip,
	                                            ZipEntry entry,
	                                            SourceFactory sourceFactory)
		throws IOException
	{
		Source source = null;
		if (entry.isDirectory()) {
			source = new ZipDirectorySource(entry);
		}
		else {
	        InputStream stream = zip.getInputStream(entry);
			source = new ZipFileSource(jhove2, entry, stream);
	        stream.close();
		}
		source.setSourceAccessor(sourceFactory.createSourceAccessor(source));

		return source;
	}	
	
	/**
	 * Make Source from list of formatted objects, which may be files,
	 * directories. and URLS
	 * 
     * @param jhove2 JHOVE2 framework object
	 * @param sourceFactory 
	 * 			SourceFactory which configures accessors for this source
     * @param name First object name, which may be a file, directory, or URL
     * @param names
     *            Additional object names, which may be files, directories, or URLs
	 * @return Source
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws JHOVE2Exception
	 */
	public static synchronized Source getSource(JHOVE2 jhove2, 
	                                            SourceFactory sourceFactory,
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
        return SourceFactoryUtil.getSource(jhove2, list, sourceFactory);
	}
	   
    /**
     * Make Source from list of formatted objects, which may be files,
     * directories. and URLS
     * @param jhove2 JHOVE2 framework object
     * @param names
     *            Object names, which may be files, directories, or URLs
     * @param sourceFactory 
     *          SourceFactory which configures accessors for this source
     * @return Source
     * @throws FileNotFoundException
     * @throws IOException
     * @throws JHOVE2Exception
     */
    public static synchronized Source getSource(JHOVE2 jhove2,
                                                List<String> pathNames, 
                                                SourceFactory sourceFactory)
        throws FileNotFoundException, IOException, JHOVE2Exception
    {
        Source source = null;       
        if (pathNames.size() == 1) {
            String name = pathNames.get(0);
            source = SourceFactoryUtil.getSource(jhove2, name, sourceFactory);        
        }
        else {
            source = new FileSetSource();
            source.setSourceAccessor(sourceFactory.createSourceAccessor(source));
            Iterator<String> iter = pathNames.iterator();
            while (iter.hasNext()) {
                Source src = null;
                String name = iter.next();
                src = SourceFactoryUtil.getSource(jhove2, name, sourceFactory);
                src = ((FileSetSource)source).addChildSource(src);
            }
        }
        
        return source;
    }
    
    /** Create new ByteStreamSource
     * @param jhove2 JHOVE2 framework object
     * @param parent Parent source unit
     * @param offset Starting byte offset byte strea,
     * @param size   Size of byte stream
     * @param name   Name, if known
     * @param sourceFactory Source factory
     * @return Byte stream source unit
     * @throws IOException
     * @throws JHOVE2Exception
     */
    public static synchronized ByteStreamSource getByteStreamSource(JHOVE2 jhove2,
                                                    Source parent, long offset,
                                                    long size, String name,
                                                    SourceFactory sourceFactory)
        throws IOException, JHOVE2Exception
    {
        ByteStreamSource source = new ByteStreamSource(jhove2, parent, offset,
                                                       size, name, sourceFactory);
        return source;
    }    
	/**
	 * Create new ClumpSource
	 * @param sourceFactory
	 * 			SourceFactory which configures accessors for this source
	 * @return Clump source unt
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
	 * @return FileSet source unit
	 */
	public static synchronized FileSetSource getFileSetSource(SourceFactory sourceFactory) {
		FileSetSource source = new FileSetSource();
		source.setSourceAccessor(sourceFactory.createSourceAccessor(source));
		return source;
	}
}
