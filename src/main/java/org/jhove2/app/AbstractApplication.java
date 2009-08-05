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

package org.jhove2.app;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.jhove2.core.JHOVE2;
import org.jhove2.core.io.Input.Type;
import org.jhove2.module.AbstractModule;

/**
 * Abstract JHOVE2 application .
 * 
 * @author mstrong, slabrams
 */
public abstract class AbstractApplication extends AbstractModule implements
		Application {
	/** {@link org.jhove2.core.io.Input} buffer size. */
	protected int bufferSize;

	/** Message digests flag; if true, calculate message digests. */
	protected boolean calcDigests;

	/** Application command line. */
	protected String commandLine;

	/** Application invocation date/timestamp. */
	protected Date dateTime;

	/** {@link org.jhove2.core.io.Input} buffer type. */
	protected Type bufferType;

	/** Delete temporary files flag. */
	protected boolean deleteTempFiles;

	/** {@link org.jhove2.module.display.Displayer}. */
	protected String displayer;

	/** Fail fast limit. */
	protected int failFastLimit;

	/** Application framework. */
	protected JHOVE2 framework;

	/** File system path names. */
	protected List<String> names;

	/** Output file. */
	protected String outputFile;

	/**
	 * Show identifiers flag; tf true, show identifiers in non-XML display
	 * modes.
	 */
	protected boolean showIdentifiers;

	/** Temporary directory. */
	protected String tempDirectory;

	/** Application user name. */
	protected String userName;

	/** Application current working directory. */
	protected String workingDirectory;

	/**
	 * Instantiate a new <code>AbstractApplication</code>.
	 * 
	 * @param version
	 *            Application version identifier in three-part form: "M.N.P"
	 * @param release
	 *            Application release date in ISO 8601 format: "YYYY-MM-DD"
	 * @param rights
	 *            Application rights statement
	 */
	public AbstractApplication(String version, String release, String rights) {
		super(version, release, rights);

		this.bufferSize = JHOVE2.DEFAULT_BUFFER_SIZE;
		this.bufferType = JHOVE2.DEFAULT_BUFFER_TYPE;
		this.calcDigests = JHOVE2.DEFAULT_CALC_DIGESTS;
		this.dateTime = new Date();
		this.deleteTempFiles = JHOVE2.DEFAULT_DELETE_TEMP_FILES;
		this.displayer = JHOVE2.DEFAULT_DISPLAYER;
		this.failFastLimit = JHOVE2.DEFAULT_FAIL_FAST_LIMIT;
		this.names = new ArrayList<String>();
		this.outputFile = null;
		this.showIdentifiers = JHOVE2.DEFAULT_SHOW_IDENTIFIERS;
		Properties props = System.getProperties();
		this.tempDirectory = props.getProperty("java.io.tmpdir");
		this.userName = props.getProperty("user.name");
		this.workingDirectory = props.getProperty("user.dir");
	}

	/**
	 * Get {@link org.jhove2.core.io.Input} buffer size.
	 * 
	 * @return Buffer size
	 * @see org.jhove2.app.Application#getBufferSize()
	 */
	@Override
	public int getBufferSize() {
		return this.bufferSize;
	}

	/**
	 * Get {@link org.jhove2.core.io.Input} buffer type.
	 * 
	 * @return Input buffer type
	 * @see org.jhove2.app.Application#getBufferType()
	 */
	@Override
	public Type getBufferType() {
		return this.bufferType;
	}

	/**
	 * Get message digests flag.
	 * 
	 * @return Message digests flag; if true, calculate message digests
	 * @see org.jhove2.app.Application#getCalcDigests()
	 */
	@Override
	public boolean getCalcDigests() {
		return this.calcDigests;
	}

	/**
	 * Get application command line.
	 * 
	 * @return Application command line
	 * @see org.jhove2.app.Application#getCommandLine()
	 */
	@Override
	public String getCommandLine() {
		return this.commandLine;
	}

	/**
	 * Get application invocation date/timestamp.
	 * 
	 * @return Application invocation date/timestamp
	 * @see org.jhove2.app.Application#getDateTime()
	 */
	@Override
	public Date getDateTime() {
		return this.dateTime;
	}

	/**
	 * Get delete temporary files flag.
	 * 
	 * @return Delete temporary files flag
	 * @see org.jhove2.app.Application#getDeleteTempFiles()
	 */
	@Override
	public boolean getDeleteTempFiles() {
		return this.deleteTempFiles;
	}

	/**
	 * Get displayer.
	 * 
	 * @return Displayer
	 * @see org.jhove2.app.Application#getDisplayer()
	 */
	@Override
	public String getDisplayer() {
		return this.displayer;
	}

	/**
	 * Get fail fast limit.
	 * 
	 * @return Fail fast limit
	 * @see org.jhove2.app.Application#getFailFastLimit()
	 */
	@Override
	public int getFailFastLimit() {
		return this.failFastLimit;
	}

	/**
	 * Get application framework.
	 * 
	 * @return Application framework
	 * @see org.jhove2.app.Application#getFramework()
	 */
	@Override
	public JHOVE2 getFramework() {
		return this.framework;
	}

	/**
	 * Get output file.
	 * 
	 * @return Output file, or null if no file is specified
	 * @see org.jhove2.app.Application#getOutputFile()
	 */
	@Override
	public String getOutputFile() {
		return this.outputFile;
	}

	/**
	 * Get file system path names.
	 * 
	 * @return File system path names
	 * @see org.jhove2.app.Application#getPathNames()
	 */
	@Override
	public List<String> getPathNames() {
		return this.names;
	}

	/**
	 * Get show identifiers flag.
	 * 
	 * @return Show identifiers flag
	 * @see org.jhove2.app.Application#getShowIdentifiers()
	 */
	@Override
	public boolean getShowIdentifiers() {
		return this.showIdentifiers;
	}

	/**
	 * Get temporary directory.
	 * 
	 * @return Temporary directory
	 * @see org.jhove2.app.Application#getTempDirectory()
	 */
	@Override
	public String getTempDirectory() {
		return this.tempDirectory;
	}

	/**
	 * Get application user name.
	 * 
	 * @return Application user name
	 * @see org.jhove2.app.Application#getUserName()
	 */
	@Override
	public String getUserName() {
		return this.userName;
	}

	/**
	 * Get application working directory.
	 * 
	 * @return Application working directory
	 * @see org.jhove2.app.Application#getWorkingDirectory()
	 */
	@Override
	public String getWorkingDirectory() {
		return this.workingDirectory;
	}

	/**
	 * Set application framework.
	 * 
	 * @param framework
	 *            Application framework
	 * @see org.jhove2.app.Application#setFramework(org.jhove2.core.JHOVE2)
	 */
	@Override
	public void setFramework(JHOVE2 framework) {
		this.framework = framework;
	}
}
