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

package org.jhove2.core;


import java.util.Properties;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.io.Input.Type;

/**
 * Configuration information for JHOVE2 framework.
 * 
 * @author mstrong, slabrams, smorrissey
 */
public class AppConfigInfo extends AbstractReportable {

	/** Default temporary file suffix. */
	public static final String DEFAULT_TEMP_SUFFIX = ".dat";

	/** Default temporary file prefix. */
	public static final String DEFAULT_TEMP_PREFIX = "jhove2-tmp";

	/** Default show identifiers flag: don't show identifiers. */
	public static final boolean DEFAULT_SHOW_IDENTIFIERS = false;

	/** Default fail fast limit. */
	public static final int DEFAULT_FAIL_FAST_LIMIT = 0;

	/** Default delete temporary files flag: delete files. */
	public static final boolean DEFAULT_DELETE_TEMP_FILES = true;

	/** Default message digests flag: don't calculate digests. */
	public static final boolean DEFAULT_CALC_DIGESTS = false;

	/** Default {@link org.jhove2.core.io.Input} buffer type. */
	public static final Type DEFAULT_BUFFER_TYPE = Type.Direct;

	/** Default {@link org.jhove2.core.io.Input} buffer size. */
	public static final int DEFAULT_BUFFER_SIZE = 131072;

	/** {@link org.jhove2.core.io.Input} buffer size. */
	protected int bufferSize;

	/** {@link org.jhove2.core.io.Input} buffer type. */
	protected Type bufferType;
	
	/** Message digests flag; if true, calculate message digests. */
	protected boolean calcDigests;

	/** Delete temporary files flag. */
	protected boolean deleteTempFiles;
	
	/** Framework temporary file prefix. */
	protected String tempPrefix;

	/** Framework temporary file suffix. */
	protected String tempSuffix;

	/**
	 * Framework fail fast limit. Processing of a given source unit is
	 * terminated once the number of detected errors exceeds the limit. A limit
	 * of 0 indicates no fail fast, i.e., process and report all errors.
	 */
	protected int failFastLimit;

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
	 * @throws JHOVE2Exception 
	 */
	public AppConfigInfo() {
		Properties props = System.getProperties();
		this.tempDirectory = props.getProperty("java.io.tmpdir");
		this.userName = props.getProperty("user.name");
		this.workingDirectory = props.getProperty("user.dir");
		
		this.bufferSize = DEFAULT_BUFFER_SIZE;
		this.bufferType = DEFAULT_BUFFER_TYPE;
		this.calcDigests = DEFAULT_CALC_DIGESTS;
		this.deleteTempFiles = DEFAULT_DELETE_TEMP_FILES;
		this.tempPrefix = DEFAULT_TEMP_PREFIX;
		this.tempSuffix = DEFAULT_TEMP_SUFFIX;
		this.failFastLimit = DEFAULT_FAIL_FAST_LIMIT;	
	}

	/**
	 * Get {@link org.jhove2.core.io.Input} buffer size.
	 * 
	 * @return Buffer size
	 */
	@ReportableProperty(order = 1, value = "Input buffer size.")
	public int getBufferSize() {
		return this.bufferSize;
	}

	/**
	 * Get {@link org.jhove2.core.io.Input} buffer type.
	 * 
	 * @return Input buffer type
	 */
	@ReportableProperty(order = 2, value = "Input buffer type.")
	public Type getBufferType() {
		return this.bufferType;
	}
	/**
	 * Get message digests flag.
	 * 
	 * @return Message digests flag; if true, calculate message digests
	 */
	@ReportableProperty(order = 3, value = "Framework message digests flag; if "
		+ "true, calculate digests.")
	public boolean getCalcDigests() {
		return this.calcDigests;
	}
	/**
	 * Get delete temporary files flag.
	 * 
	 * @return Delete temporary files flag
	 */

	public boolean getDeleteTempFiles() {
		return this.deleteTempFiles;
	}
	/**
	 * Get framework fail fast limit. Processing of a given source unit is
	 * terminated once the number of detected errors exceeds the limit. A limit
	 * of 0 indicates no fail fast, i.e., process and report all errors.
	 * 
	 * @return Fail fast limit
	 */
	@ReportableProperty(order = 5, value = "Framework fail fast limit.")
	public int getFailFastLimit() {
		return this.failFastLimit;
	}
	/**
	 * Get temporary directory.
	 * 
	 * @return Temporary directory
	 */
	@ReportableProperty(order = 7, value = "Temporary directory.")
	public String getTempDirectory() {
		return this.tempDirectory;
	}
	/**
	 * Get application user name.
	 * 
	 * @return Application user name
	 */
	@ReportableProperty(order = 1, value = "Application user name.")
	public String getUserName() {
		return this.userName;
	}
	/**
	 * Get application working directory.
	 * 
	 * @return Application working directory
	 */
	@ReportableProperty(order = 4, value = "Application current working directory.")
	public String getWorkingDirectory() {
		return this.workingDirectory;
	}
	/**
	 * Mutator for input bufferSize
	 * @param bufferSize
	 */
	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}
	/**
	 * Mutator for calcDigests flag
	 * @param calcDigests boolean indicating whether to calculate message digests (checksums)
	 */
	public void setCalcDigests(boolean calcDigests) {
		this.calcDigests = calcDigests;
	}
	/**
	 * Mutator for Input buffer type
	 * @param bufferType
	 */
	public void setBufferType(Type bufferType) {
		this.bufferType = bufferType;
	}
	/**
	 * Mutator for deleteTempFiles flag
	 * @param deleteTempFiles
	 */
	public void setDeleteTempFiles(boolean deleteTempFiles) {
		this.deleteTempFiles = deleteTempFiles;
	}
	/**
	 * Mutator for failFastLimit  A value of 0 means there will be no failFast
	 * @param failFastLimit
	 */
	public void setFailFastLimit(int failFastLimit) {
		this.failFastLimit = failFastLimit;
	}
	/**
	 * Mutator for path to directory where JHOVE2 temporary files will be created
	 * @param tempDirectory
	 */
	public void setTempDirectory(String tempDirectory) {
		this.tempDirectory = tempDirectory;
	}
	/**
	 * Mutator for user name
	 * @param userName
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * Mutator for working directory
	 * @param workingDirectory
	 */
	public void setWorkingDirectory(String workingDirectory) {
		this.workingDirectory = workingDirectory;
	}

	/**
	 * Get framework temporary file prefix.
	 * 
	 * @return Framework temporary file prefix
	 */
	@ReportableProperty(order = 8, value = "Framework temporary file prefix.")
	public String getTempPrefix() {
		return tempPrefix;
	}
	/**
	 * Mutator for prefix for JHOVE2 temporary files
	 * @param tempPrefix
	 */
	public void setTempPrefix(String tempPrefix) {
		this.tempPrefix = tempPrefix;
	}
	/**
	 * Get framework temporary file suffix.
	 * 
	 * @return Framework temporary file suffix
	 */
	@ReportableProperty(order = 9, value = "Framework temporary file suffix.")
	public String getTempSuffix() {
		return tempSuffix;
	}
	/**
	 * Mutator for framework temporary file name suffix
	 * @param tempSuffix
	 */
	public void setTempSuffix(String tempSuffix) {
		this.tempSuffix = tempSuffix;
	}

}
