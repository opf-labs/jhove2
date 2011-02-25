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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.jhove2.core.Digest;
import org.jhove2.core.Invocation;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.module.digest.AbstractArrayDigester;
import org.jhove2.module.digest.CRC32Digester;
import org.jhove2.module.format.zip.ZipEntryProperties;

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
	 * @return Source unit
	 * @throws IOException
	 *             I/O exception instantiating source
	 * @throws JHOVE2Exception 
	 */
	public static synchronized Source getSource(JHOVE2 jhove2, String name)
		throws IOException, JHOVE2Exception
	{
	    Source source = null;
	    
	    /* First check if the name is a URL. If not, assume it's a file or
	     * directory name.
	     */
        try{
            URL url = new URL(name);
            source = SourceFactoryUtil.getSource(jhove2, url);
        }
        catch (MalformedURLException m){
            File file = new File(name);
            source = SourceFactoryUtil.getSource(jhove2, file);
        }
        
		return source;
	}

	/**
	 * Get source unit from a Java {@link java.io.File}.
	 * 
     * @param jhove2 JHOVE2 framework object
	 * @param file
	 *            Java {@link java.io.File}
	 * @return Source unit
	 * @throws IOException
	 *             I/O exception instantiating source
	 * @throws JHOVE2Exception 
	 */
	public static synchronized Source getSource(JHOVE2 jhove2, File file)
		throws IOException, JHOVE2Exception
	{
		Source source = null;
		if (file.isDirectory()) {		
			source = new DirectorySource(jhove2, file);
			/* Note, the DirectorySource constructor automatically associates
			 * the source factory, so we don't need to do it here.
			 */
		}
		else{
			source = new FileSource(jhove2, file);
		}
		
		return source;
	}

	/**
	 * Get source unit from a URL by creating a local temporary file.
     * @param jhove2 JHOVE2 framework object
     * @param url URL
	 * @return URL source unit
	 * @throws IOException
	 *             I/O exception instantiating source
	 */
	public static synchronized Source getSource(JHOVE2 jhove2, URL url)
		throws IOException
	{
		Source source = new URLSource(jhove2, url);
		return source;
	}

	/**
	 * Get source unit from a Zip file entry by creating a temporary file.
	 * @param jhove2 JHOVE2 framework object
     * @param zip
     *            Zip file
     * @param entry
     *            Zip file entry
	 * @return Source unit
	 * @throws IOException
	 *             I/O exception instantiating source
	 * @throws JHOVE2Exception 
	 */
	public static synchronized Source getSource(JHOVE2 jhove2, ZipFile zip,
	                                            ZipEntry entry)
		throws IOException, JHOVE2Exception
	{
		Source source = null;
        String name = entry.getName();
		if (entry.isDirectory()) {
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
		    source = new DirectorySource(jhove2, name, false);        
		}
		else {
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
	        Invocation inv = jhove2.getInvocation();
	        File file = AbstractSource.createTempFile(stream, name,
	                                                  inv.getTempDirectoryFile(),
	                                                  inv.getTempPrefix(),
	                                                  inv.getTempSuffix(),
	                                                  inv.getBufferSize());
            stream.close();
            
	        source = new FileSource(jhove2, file, false);
	        source.setIsTemp(true);
	        
	        /* This is a temporary fix.  We need to keep the temporary backing
	         * files for Zip components in case we need to later get an
	         * {@link java.io.InputStream} on the component
	         * (Source.getInputStream()) to pass to a third-party package that
	         * doesn't support {@link org.jhove2.core.io.Input}s.
	         * 
	         * Note that the temporary files will accumulate in the temporary
	         * directory after termination.
	         * 
	         * TODO: Find a better mechanism for dealing with this problem
	         * in the recursive processing model.
	         */
            source.setDeleteTempFileOnClose(false);
		}
		
		/* Get the entry-specific properties. */
        long crc = entry.getCrc();
        Digest crc32 = new Digest(AbstractArrayDigester.toHexString(crc),
                                  CRC32Digester.ALGORITHM);
		ZipEntryProperties properties =
		    new ZipEntryProperties(name, entry.getCompressedSize(), crc32,
		                           entry.getComment(),
		                           new Date(entry.getTime()));
		source.addExtraProperties(properties);

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
	 * @throws IOException
	 * @throws JHOVE2Exception
	 */
	public static synchronized Source getSource(JHOVE2 jhove2, 
	                                            SourceFactory sourceFactory,
	                                            String name, String...names)
		throws IOException, JHOVE2Exception
	{
	    List<String> list = new ArrayList<String>();
        list.add(name);
        if (names != null && names.length > 0) {
            for (int i = 0; i < names.length; i++) {
                list.add(names[i]);
            }
        }
        return SourceFactoryUtil.getSource(jhove2, list);
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
     * @throws IOException
     * @throws JHOVE2Exception
     */
    public static synchronized Source getSource(JHOVE2 jhove2,
                                                List<String> pathNames)
        throws IOException, JHOVE2Exception
    {
        Source source = null;       
        if (pathNames.size() == 1) {
            String name = pathNames.get(0);
            source = SourceFactoryUtil.getSource(jhove2, name);        
        }
        else {
            source = new FileSetSource(jhove2);
            Iterator<String> iter = pathNames.iterator();
            while (iter.hasNext()) {
                String name = iter.next();
                Source src = SourceFactoryUtil.getSource(jhove2, name);
                src = ((FileSetSource)source).addChildSource(src);
            }
        }
        
        return source;
    }
    
    /** Create new ByteStreamSource
     * @param jhove2 JHOVE2 framework object
     * @param parent Parent source unit
     * @param offset Starting byte offset
     * @param size   Size of byte stream
     * @param name   Name, if known
     * @return Byte stream source unit
     * @throws IOException
     * @throws JHOVE2Exception
     */
    public static synchronized ByteStreamSource getByteStreamSource(JHOVE2 jhove2,
                                                    Source parent, long offset,
                                                    long size, String name)
        throws IOException, JHOVE2Exception
    {
        ByteStreamSource source = new ByteStreamSource(jhove2, parent, offset,
                                                       size, name);
        return source;
    }    
	/**
	 * Create new ClumpSource
	 * @param sourceFactory
	 * 			SourceFactory which configures accessors for this source
     * @param jhove2 JHOVE2 framework object
	 * @return Clump source unit
	 * @throws JHOVE2Exception 
	 */
	public static synchronized ClumpSource getClumpSource(JHOVE2 jhove2) 
	    throws JHOVE2Exception
	{		
		ClumpSource source = new ClumpSource(jhove2);
		return source;
	}
	   
    /**
     * Create empty non-file system DirectorySource
     * @param jhove2 JHOVE2 framework object
     * @param name   Directory name 
     * @throws JHOVE2Exception 
     * @throws IOException 
     */
	public static synchronized DirectorySource getDirectorySource(JHOVE2 jhove2,
	                                                              String name)
	    throws IOException, JHOVE2Exception 
	{
	    File file = new File(name);
	    return new DirectorySource(jhove2, file, false);
	}
	
	/**
	 * Create new empty FileSetSource
     * @param jhove2 JHOVE2 framework object
	 * @return FileSet source unit
	 */
	public static synchronized FileSetSource getFileSetSource(JHOVE2 jhove2) {
		FileSetSource source = new FileSetSource(jhove2);
		return source;
	}
}
