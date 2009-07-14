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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.List;

import org.jhove2.core.Duration;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.io.Input;
import org.jhove2.core.io.InputFactory;
import org.jhove2.core.io.Input.Type;
import org.jhove2.module.Module;

/** An abstract JHOVE2 source unit.  A source unit is a formatted object that
 * can be characterized, which may be a file, a subset of a file, or a group
 * of files.
 * 
 * @author mstrong, slabrams
 */
public abstract class AbstractSource
	implements Source
{	
	/** Child source units. */
	protected List<Source> children;
	
	/** Delete temporary files flag; if true, delete files. */
	protected boolean deleteTempFiles;
	
	/** Module elapsed time, end. */
	protected long endTime;
	
	/** Source unit backing file. */
	protected File file;
	
	/** Source unit backing file temporary status: true if the source unit
	 * backing file is a temporary file.
	 */
	protected boolean isTemp;

	/** Modules that processed the source unit. */
	protected List<Module> modules;
	
	/** Module elapsed time, start. */
	protected long startTime;
	
	/** Instantiate a new <code>AbstractSource</code>.
	 */
	protected AbstractSource() {
		this.children        = new ArrayList<Source>();
		this.deleteTempFiles = JHOVE2.DEFAULT_DELETE_TEMP_FILES;
		this.modules         = new ArrayList<Module>();
	}
	
	/** Instantiate a new <code>AbstractSource</code> backed by a file.
	 * @param jhove2 JHOVE2 framework
	 * @param file   File underlying the source unit
	 */
	public AbstractSource(File file) {
		this();
		
		this.file   = file;
		this.isTemp = false;
	}
	
	/** Instantiate a new <code>AbstractSource</code> backed by an input
	 * stream.
	 * @param jhove2 JHOVE2 framework
	 * @param stream Input stream underlying the source unit
	 * @throws IOException 
	 */
	public AbstractSource(JHOVE2 jhove2, InputStream stream)
		throws IOException
	{
		this();
		
		this.file   = createTempFile(jhove2, stream);
		this.isTemp = true;
	}
	
	/** Add a child source unit.
	 * @param child Child source unit
	 * @see org.jhove2.core.source.Source#addChildSource(org.jhove2.core.source.Source)
	 */
	@Override
	public void addChildSource(Source child) {
		this.children.add(child);
	}
	
	/** Add a module that processed the source unit.
	 * @param module Module that processed the source unit
	 * @see org.jhove2.core.source.Source#addModule(org.jhove2.core.Processible)
	 */
	@Override
	public void addModule(Module module) {
		this.modules.add(module);
	}
	
	/** Close the source unit.  If the source unit is backed by a temporary
	 * file, delete the file.
	 */
	public void close() {
		if (this.file != null && this.isTemp && this.deleteTempFiles) {
			this.file.delete();
		}
	}
	
	/** Create a temporary backing file from an input stream.
	 * @param jhove2   JHOVE2 framework
	 * @param inStream Input stream
	 * @return file Temporary backing file
	 * @throws IOException
	 */
	protected File createTempFile(JHOVE2 jhove2, InputStream inStream)
		throws IOException
	{
		File tempFile = File.createTempFile(jhove2.getTempPrefix(),
				                            jhove2.getTempSuffix());
		OutputStream outStream  = new FileOutputStream(tempFile);
		ReadableByteChannel in  = Channels.newChannel(inStream);
		WritableByteChannel out = Channels.newChannel(outStream);
		final ByteBuffer buffer =
			ByteBuffer.allocateDirect(jhove2.getBufferSize());
		
		while ((in.read(buffer)) > 0) {
			buffer.flip();
			out.write(buffer);
			buffer.compact();  /* in case write was incomplete. */
		}
		buffer.flip();
		while (buffer.hasRemaining()) {
			out.write(buffer);
		}

		/* Closing the channel implicitly closes the stream. */
		in.close();
		out.close();
		
		return tempFile;
	}

	/** Get child source units.
	 * @return Child source units
	 * @see org.jhove2.core.source.Source#getChildSources()
	 */
	@Override
	public List<Source> getChildSources() {
		return this.children;
	}
	
	/** Get delete temporary files flag; if true, delete files.
	 * @return Delete temporary files flag
	 * @see org.jhove2.core.source.Source#getDeleteTempFiles()
	 */
	@Override
	public boolean getDeleteTempFiles() {
		return this.deleteTempFiles;
	}
	
	/** Get elapsed time, in milliseconds.  The shortest reportable
	 * elapsed time is 1 milliscond.
	 * @return Elapsed time, in milliseconds
	 * @see org.jhove2.core.Temporal#getElapsedTime()
	 */
	@Override
	public Duration getElapsedTime() {
		if (this.endTime == Duration.UNINITIALIZED) {
			this.endTime = System.currentTimeMillis();
		}
		
		return new Duration(this.endTime - this.startTime);
	}
	
	/** Get {@link java.io.File} backing the source unit.
	 * @return File backing the source unit
	 * @see org.jhove2.core.source.Source#getFile()
	 */
	public File getFile() {
		return this.file;
	}
	
	/** Get {@link org.jhove2.core.io.Input} for the source unit.  Concrete
	 * classes extending this abstract class must provide an implementation of 
	 * this method if they are are based on parsable input.  Classes without
	 * parsable input (e.g. {@link org.jhove2.core.source.ClumpSource} or
	 * {@link org.jhove2.core.source.DirectorySource} can let this inherited
	 * method return null.
	 * @param bufferSize Input maximum buffer size
	 * @param bufferType Input buffer type
	 * @return null
	 * @throws FileNotFound
	 * @throws IOException
	 * @see org.jhove2.core.source.Source#getInput(int, org.jhove2.core.input.Type)
	 */
	@Override
	public Input getInput(int bufferSize, Type bufferType)
		throws FileNotFoundException, IOException
	{
		return getInput(bufferSize, bufferType, ByteOrder.LITTLE_ENDIAN);
	}
	
	/** Get {@link org.jhove2.core.io.Input} for the source unit.  Concrete
	 * classes extending this abstract class must provide an implementation of 
	 * this method if they are are based on parsable input.  Classes without
	 * parsable input (e.g. {@link org.jhove2.core.source.ClumpSource} or
	 * {@link org.jhove2.core.source.DirectorySource} can let this inherited
	 * method return null.
	 * @param bufferSize Input maximum buffer size, in bytes
	 * @param bufferType Input buffer type
	 * @param order      Byte order
	 * @return null
	 * @throws FileNotFound
	 * @throws IOException
	 * @see org.jhove2.core.source.Source#getInput(int, org.jhove2.core.input.Type, java.nio.ByteOrder)
	 */
	public Input getInput(int bufferSize, Type bufferType, ByteOrder order)
		throws FileNotFoundException, IOException
	{
		return InputFactory.getInput(this.file, bufferSize, bufferType, order);
	}
	
	/** Get {@link java.io.InputStream} backing the source unit
	 * @return Input stream backing the source unit
	 * @throws FileNotFoundException 
	 * @see org.jhove2.core.source.Source#getInputStream()
	 */
	public InputStream getInputStream()
		throws FileNotFoundException
	{
		return new FileInputStream(this.file);
	}
	
	/** Get modules that processed the source unit.
	 * @return Modules that processed the source unit
	 * @see org.jhove2.core.source.Source#getModules()
	 */
	@Override
	public List<Module> getModules() {
		return this.modules;
	}
	
	/** Get number of child source units.
	 * @return Number of child source units
	 * @see org.jhove2.core.source.Source#getNumChildSources()
	 */
	@Override
	public int getNumChildSources() {
		return this.children.size();
	}
	
	/** Get number of modules.
	 * @return Number of modules
	 * @see org.jhove2.core.source.Source#getNumModules()
	 */
	@Override
	public int getNumModules() {
		return this.modules.size();
	}
	
	/** Get source unit backing file temporary status.
	 * @return True if the source unit backing file is a temporary file
	 * @see org.jhove2.source.Source#isTemp()
	 */
	public boolean isTemp() {
		return this.isTemp;
	}

	/** Set the end time of the elapsed duration.
	 * @return End time, in milliseconds
	 * @see org.jhove2.core.Temporarl#setStartTime()
	 */
	@Override
	public long setEndTime() {
		return this.endTime = System.currentTimeMillis();
	}
	
	/** Set the restart time of the elapsed duration.  All subsequent time
	 * (until the next invocation of the setEndTime() method) will be added
	 * to the time already accounted for by an earlier invocation of the
	 * setEndTime() method.
	 * @return Current time minus the elapsed time, in milliseconds
	 * @see org.jhove2.core.Temporal#setReStartTime()
	 */
	public long setRestartTime() {
		if (this.endTime == Duration.UNINITIALIZED) {
			return this.startTime = System.currentTimeMillis();
		}
		return this.startTime = System.currentTimeMillis() - this.endTime - this.startTime;
	}
	
	/** Set the start time of the elapsed duration.
	 * @return Start time, in milliseconds
	 * @see org.jhove2.core.Temporaral#setStartTime()
	 */
	@Override
	public long setStartTime() {
		return this.startTime = System.currentTimeMillis();
	}
	
	/** Set delete temporary files flag; if true, delete files.
	 * @param flag Delete temporary files flag
	 * @see org.jhove2.core.source.Source#setDeleteTempFiles(boolean)
	 */
	@Override
	public void setDeleteTempFiles(boolean flag) {
		this.deleteTempFiles = flag;
	}
}
