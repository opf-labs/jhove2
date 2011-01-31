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

import com.sleepycat.persist.model.Persistent;

/**
 * @author smorrissey
 *
 */
@Persistent
public abstract class AbstractSourceFactory
    implements SourceFactory
{
	public AbstractSourceFactory(){
		super();
	}

	/** Get source from formatted object name, which may be a file, a directory,
	 * or a URL.  Note that a URL source requires the creation of a temporary
	 * file.
	 * @param name Formatted object name
     * @param tmpDirectory Temporary directory
     * @param tmpPrefix Temporary prefix
     * @param tmpSuffix Temporary suffix
     * @param bufferSize Buffer size (for creating temporary file)
	 * @see org.jhove2.core.source.SourceFactory#getSource(java.lang.String, java.io.File, java.lang.String, java.lang.String, int)
	 */
	@Override
	public Source getSource(String name, File tmpDirectory, String tmpPrefix,
	                        String tmpSuffix, int bufferSize)
	    throws FileNotFoundException, IOException, JHOVE2Exception
	{
		Source source = SourceFactoryUtil.getSource(name, this, tmpDirectory,
		                                            tmpPrefix, tmpSuffix,
		                                            bufferSize);
		source = source.getSourceAccessor().persistSource(source);
		return source;
	}

	/** Get source from a file system object, either a file or a directory.
	 * @param file Object file
	 * @see org.jhove2.core.source.SourceFactory#getSource(java.io.File)
	 */
	@Override
	public Source getSource(File file)
	    throws FileNotFoundException, IOException, JHOVE2Exception
	{
		Source source = SourceFactoryUtil.getSource(file, this);
		source = source.getSourceAccessor().persistSource(source);
		return source;
	}

	/** Get source unit from a URL.  Note that this requires the creation of a
	 * temporary file.
	 * @param url Object URL
     * @param tmpDirectory Temporary directory
     * @param tmpPrefix Temporary prefix
     * @param tmpSuffix Temporary suffix
     * @param bufferSize Buffer size (for creating temporary file)
	 * @see org.jhove2.core.source.SourceFactory#getSource(java.net.URL, java.io.File, java.lang.String, java.lang.String, int, java.net.URL)
	 */
	@Override
	public Source getSource(URL url, File tmpDirectory, String tmpPrefix,
	                        String tmpSuffix, int bufferSize)
	    throws IOException, JHOVE2Exception
	{
		Source source = SourceFactoryUtil.getSource(url, this, tmpDirectory,
		                                            tmpPrefix, tmpSuffix, 
		                                            bufferSize);
		source = source.getSourceAccessor().persistSource(source);
		return source;
	}

	/** Get source unit from a Zip file entry.
	 * @param zip Zip file
	 * @param entry Zip file entry
     * @param tmpDirectory Temporary directory
     * @param tmpPrefix Temporary prefix
     * @param tmpSuffix Temporary suffix
     * @param bufferSize Buffer size (for creating temporary file)
	 * @see org.jhove2.core.source.SourceFactory#getSource(java.lang.String, java.lang.String, int, java.util.zip.ZipFile, java.util.zip.ZipEntry)
	 */
	@Override
	public Source getSource(ZipFile zip, ZipEntry entry, File tmpDirectory,
	                        String tmpPrefix, String tmpSuffix, int bufferSize)
	    throws IOException, JHOVE2Exception
	{
		Source source = SourceFactoryUtil.getSource(zip, entry, this,
		                                            tmpDirectory,tmpPrefix,
		                                            tmpSuffix, bufferSize);
		source = source.getSourceAccessor().persistSource(source);
		return source;
	}

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
	@Override
    public Source getSource(File tmpDirectory, String tmpPrefix,
                            String tmpSuffix, int bufferSize,
                            String name, String...names)
        throws FileNotFoundException, IOException, JHOVE2Exception
    {
        Source source = SourceFactoryUtil.getSource(this, tmpDirectory,
                                                    tmpPrefix, tmpSuffix,
                                                    bufferSize, name, names);
        source = source.getSourceAccessor().persistSource(source);
        return source;
    }
   
	/** Get source unit from a list of formatted object names, which may be
	 * files, directories, or URLs.  Note that URL source units require the
	 * creation of temporary files.
	 * @param names Formatted object names
     * @param tmpDirectory Temporary directory
     * @param tmpPrefix Temporary prefix
     * @param tmpSuffix Temporary suffix
     * @param bufferSize Buffer size (for creating temporary file)
	 * @see org.jhove2.core.source.SourceFactory#getSource(java.util.List. java.io.File, java.lang.String, java.lang.String, int)
	 */
	@Override
	public Source getSource(List<String> names, File tmpDirectory,
	                        String tmpPrefix, String tmpSuffix,
	                        int bufferSize)
	    throws FileNotFoundException, IOException, JHOVE2Exception
	{
		Source source = SourceFactoryUtil.getSource(names, this, tmpDirectory,
		                                            tmpPrefix,tmpSuffix,
		                                            bufferSize);
		source = source.getSourceAccessor().persistSource(source);
		return source;
	}

	/** Get Clump source. */
	@Override
	public ClumpSource getClumpSource() throws JHOVE2Exception
	{
		ClumpSource source = SourceFactoryUtil.getClumpSource(this);
		source = (ClumpSource) source.getSourceAccessor().persistSource(source);
		return source;
	}

	/* Get FileSet source. */
	@Override
	public FileSetSource getFileSetSource() throws JHOVE2Exception {
		FileSetSource source = SourceFactoryUtil.getFileSetSource(this);
		source = (FileSetSource) source.getSourceAccessor().persistSource(source);
		return source;
	}
	
	/* Get ByteStream source. */
	@Override
	public ByteStreamSource getByteStreamSource(Source parent, long offset,
	                                            long size, File tmpDirectory,
	                                            String tmpPrefix,
	                                            String tmpSuffix,
	                                            int bufferSize) 
	    throws IOException, JHOVE2Exception
	{
		ByteStreamSource source =
		    SourceFactoryUtil.getByteStreamSource(parent, offset, size, this,
		                                          tmpDirectory, tmpPrefix,
		                                          tmpSuffix, bufferSize);
		source = (ByteStreamSource) source.getSourceAccessor().persistSource(source);
		return source;		
	}
}
