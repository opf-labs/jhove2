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

import java.util.Date;
import java.util.Properties;

import org.jhove2.annotation.Reportable;
import org.jhove2.annotation.ReportableProperty;

/** The JHOVE2 core processing framework.
 * 
 * @author mstrong, slabrams
 */
@Reportable("JHOVE2 core processing framework.")
public class JHOVE2
	extends AbstractModule
{
	/** Framework version identifier. */
	public static final String VERSION = "2.0.0";

	/** Framework release date. */
	public static final String DATE = "2009-06-04";
	
	/** Framework rights statement. */
	public static final String RIGHTS =
		"Copyright 2009 by The Regents of the University of California, " +
		"Ithaka Harbors, Inc., and The Board of Trustees of the Leland " +
		"Stanford Junior University. " +
		"Available under the terms of the BSD license.";
	
	/** Default buffer size. */
	public static final int DEFAULT_BUFFER_SIZE = 131072;
	
	/** Default fail fast limit. */
	public static final int DEFAULT_FAIL_FAST_LIMIT = 0;
	
	/** Platform architecture. */
	protected String architecture;
	
	/** {@link org.jhove2.core.io.Input} buffer size. */
	protected int bufferSize;
	
	/** Java classpath. */
	protected String classpath;

	/** Fail fast limit.  Processing of a given source unit is terminated once
	 * the number of detected errors exceeds the limit.  A limit of 0
	 * indicates no fail fast, i.e., process and report all errors. 
	 */
	protected int failFastLimit;
	
	/** JRE home. */
	protected String jreHome;
	
	/** JRE vendor. */
	protected String jreVendor;
	
	/** JRE version. */
	protected String jreVersion;
	
	/** JVM name. */
	protected String jvmName;
	
	/** JVM vendor. */
	protected String jvmVendor;
	
	/** JVM version. */
	protected String jvmVersion;
	
	/** Java library path. */
	protected String libraryPath;
	
	/** Maximum memory available to the JVM, in bytes. */
	protected long maxMemory;
	
	/** Number of bytestream source units. */
	protected int numBytestreams;
	
	/** Number of clump source units. */
	protected int numClumps;
	
	/** Number of container source units. */
	protected int numContainers;
	
	/** Number of directory source units. */
	protected int numDirectories;
	
	/** Number of file source units. */
	protected int numFiles;
	
	/** Number of processors available to the JVM. */
	protected int numProcessors;
	
	/** Operating system name. */
	protected String osName;
	
	/** Operating system version. */
	protected String osVersion;
	
	/** Used memory, in bytes. */
	protected long useMemory;
	
	/** User name. */
	protected String userName;
	
	/** Current working directory. */
	protected String workingDirectory;

	/** Instantiate a new <code>JHOVE2</code> core framework.
	 */
	public JHOVE2() {
		super(VERSION, DATE, RIGHTS);
		
		/* Initialize the framework. */
		initInstallation();
		initInvocation();
		
		this.bufferSize     = DEFAULT_BUFFER_SIZE;
		this.failFastLimit  = DEFAULT_FAIL_FAST_LIMIT;
		
		this.numBytestreams = 0;
		this.numClumps      = 0;
		this.numContainers  = 0;
		this.numDirectories = 0;
		this.numFiles       = 0;
	}

	/* Initialize the static framework installation properties.
	 */
	protected void initInstallation() {
		Runtime rt = Runtime.getRuntime();	
		this.maxMemory     = rt.maxMemory();
		this.numProcessors = rt.availableProcessors();

		Properties prop = System.getProperties();
		this.architecture  = prop.getProperty("os.arch");
		this.classpath     = prop.getProperty("java.class.path");
		this.jreHome       = prop.getProperty("java.home");
		this.jreVendor     = prop.getProperty("java.vendor");
		this.jreVersion    = prop.getProperty("java.version");
		this.jvmName       = prop.getProperty("java.vm.name");
		this.jvmVendor     = prop.getProperty("java.vm.vendor");
		this.jvmVersion    = prop.getProperty("java.vm.version");
		this.libraryPath   = prop.getProperty("java.library.path");
		this.osName        = prop.getProperty("os.name");
		this.osVersion     = prop.getProperty("os.version");
	}
	
	/** Initialize the framework invocation properties.
	 */
	protected void initInvocation() {
		Properties prop = System.getProperties();
		this.userName         = prop.getProperty("user.name");
		this.workingDirectory = prop.getProperty("user.dir");
	}
	
	/** Get platform architecture.
	 * @return Platform architecture
	 */
	@ReportableProperty(order=5, value="Platform architecture.")
	public String getArchitecture() {
		return this.architecture;
	}

	/** Get {@link org.jhove2.core.io.Input} buffer size.
	 * @return Input buffer size
	 */
	@ReportableProperty(order=18, value="Input buffer size.")
	public int getBufferSize() {
		return this.bufferSize;
	}
	
	/** Get Java classpath.
	 * @return Java classpath
	 */
	@ReportableProperty(order=16, value="Java classpath.")
	public String getClasspath() {
		return this.classpath;
	}
	
	/** Get application invocation date/timestamp.
	 * @return Application invocation date/timestamp
	 */
	@ReportableProperty(order=3, value="Application invocation " +
			"date/timestatmp.")
	public Date getDateTime() {
		return new Date(this.timeInitial);
	}
	
	/** Get fail fast limit.  Processing of a given source unit is terminated
	 * once the number of detected errors exceeds the limit.  A limit of 0
	 * indicates no fail fast, i.e., process and report all errors. 
	 * @return Fail fast limit
	 */
	@ReportableProperty(order=19, value="Fail fast limit.")
	public int getFailFastLimit() {
		return this.failFastLimit;
	}
	
	/** Get JRE home.
	 * @return JRE home
	 */
	@ReportableProperty(order=12, value="JRE home.")
	public String getJREHome() {
		return this.jreHome;
	}
	
	/** Get JRE vendor.
	 * @return JRE vendor
	 */
	@ReportableProperty(order=10, value="JRE vendor.")
	public String getJREVendor() {
		return this.jreVendor;
	}
	
	/** Get JRE version.
	 * @return JRE version
	 */
	@ReportableProperty(order=11, value="JRE version.")
	public String getJREVersion() {
		return this.jreVersion;
	}
	/** Get JVM name.
	 * @return JVM name
	 */
	@ReportableProperty(order=14, value="JVM name.")
	public String getJVMName() {
		return this.jvmName;
	}
	
	/** Get JVM vendor.
	 * @return JVM vendor
	 */
	@ReportableProperty(order=13, value="JVM vendor.")
	public String getJVMVendor() {
		return this.jvmVendor;
	}
	
	/** Get JVM version.
	 * @return JVM version
	 */
	@ReportableProperty(order=15, value="JVM version.")
	public String getJVMVersion() {
		return this.jvmVersion;
	}
	
	/** Get Java library path.
	 * @return Java library path
	 */
	@ReportableProperty(order=17, value="Java library path.")
	public String getLibraryPath() {
		return this.libraryPath;
	}
	
	/** Get maximum memory available to the JVM, in bytes.
	 * @return maximum memory available to the JVM, in bytes
	 */
	@ReportableProperty(order=7, value="Maximum memory available to the " +
			"JVM, in bytes.")
	public long getMaxMemory() {
		return this.maxMemory;
	}

	/** Get application memory usage.  This is calculated naively as the Java
	 * {@link java.lang.Runtime}'s total memory minus free memory at the time
	 * of method invocation.
	 * @return Memory usage, in bytes
	 */
	@ReportableProperty(order=28, value="Application memory usage, in bytes.")
	public long getMemoryUsage() {
		Runtime rt = Runtime.getRuntime();
		long use = rt.totalMemory() - rt.freeMemory();
		
		return use;
	}
	
	/** Get number of aggregate source units processed.
	 * @return Number of aggregate source units processed
	 */
	@ReportableProperty(order=25, value="Number of bytestream source units " +
			"processed.")
	public int getNumBytestreamSources() {
		return this.numBytestreams;
	}
	
	/** Get number of clump source units processed.
	 * @return Number of clump source units processed
	 */
	@ReportableProperty(order=27, value="Number of clump source units " +
			"processed.")
	public int getNumClumpSources() {
		return this.numClumps;
	}
	
	/** Get number of container source units processed.
	 * @return Number of container source units processed
	 */
	@ReportableProperty(order=26, value="Number of bytestream source units " +
			"processed.")
	public int getNumContainerSources() {
		return this.numContainers;
	}
	
	/** Get number of directory source units processed.
	 * @return Number of directory source units processed
	 */
	@ReportableProperty(order=23, value="Number of directory source units " +
			"processed.")
	public int getNumDirectorySources() {
		return this.numDirectories;
	}
	
	/** Get number of file source units processed.
	 * @return Number of file source units processed
	 */
	@ReportableProperty(order=24, value="Number of file source units " +
			"processed.")
	public int getNumFileSources() {
		return this.numFiles;
	}
	
	/** Get number of processors available to the JVM.
	 * @return Number of processors.
	 */
	@ReportableProperty(order=6, value="Number of processors available to " +
	"the JVM.")
	public int getNumProcessors() {
		return this.numProcessors;
	}
	
	/** Get number of source units processed.
	 * @return Number of source units processed
	 */
	@ReportableProperty(order=22, value="Number of source units processed.")
	public int getNumSources() {
		return this.numDirectories + this.numFiles + this.numBytestreams + 
		       this.numContainers  + this.numClumps;
	}
	
	/** Get operating system name.
	 * @return Operating system name
	 */
	@ReportableProperty(order=8, value="Operating system name.")
	public String getOSName() {
		return this.osName;
	}
	
	/** Get operating system version.
	 * @return Operating system version
	 */
	@ReportableProperty(order=9, value="Operating system version.")
	public String getOSVersion() {
		return  this.osVersion;
	}
	
	/** Get application user name.
	 * @return Application user name
	 */
	@ReportableProperty(order=1, value="Application user name.")
	public String getUserName() {
		return this.userName;
	}
	
	/** Get application current working directory.
	 * @return Application current working directory
	 */
	@ReportableProperty(order=2, value="Application current working directory.")
	public String getWorkingDirectory() {
		return this.workingDirectory;
	}
	
	/** Set {@link org.jhove2.core.io.Input} buffer size.
	 * @param size Buffer size
	 */
	public void setBufferSize(int size) {
		this.bufferSize = size;
	}
	
	/** Set fail fast limit.  Processing of a given source unit is terminated
	 * once the number of detected errors exceeds the limit.  A limit of 0
	 * indicates no fail fast, i.e., process and report all errors. 
	 * @param limit Fail fast limit
	 */
	public void setFailFastLimit(int limit) {
		this.failFastLimit = limit;
	}
}
