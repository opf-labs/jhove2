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

/** JHOVE2 installation properties.
 * 
 * @author mstrong, slabrams
 */
public class Installation
	implements Reportable
{
	/** Platform architecture. */
	protected String architecture;
	
	/** Java classpath. */
	protected String classpath;
	
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
	
	/** Number of processors available to the JVM. */
	protected int numProcessors;
	
	/** Operating system name. */
	protected String osName;
	
	/** Operating system version. */
	protected String osVersion;
	
	/** Instantiate a new <code>Installation</code> reportable.
	 */
	public Installation() {
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
	
	/** Get platform architecture.
	 * @return Platform architecture
	 */
	@ReportableProperty(order=11, value="Platform architecture.")
	public String getArchitecture() {
		return this.architecture;
	}
	
	/** Get Java classpath.
	 * @return Java classpath
	 */
	@ReportableProperty(order=51, value="Java classpath.")
	public String getClasspath() {
		return this.classpath;
	}
	
	/** Get JRE home.
	 * @return JRE home
	 */
	@ReportableProperty(order=33, value="JRE home.")
	public String getJREHome() {
		return this.jreHome;
	}
	
	/** Get JRE vendor.
	 * @return JRE vendor
	 */
	@ReportableProperty(order=31, value="JRE vendor.")
	public String getJREVendor() {
		return this.jreVendor;
	}
	
	/** Get JRE version.
	 * @return JRE version
	 */
	@ReportableProperty(order=32, value="JRE version.")
	public String getJREVersion() {
		return this.jreVersion;
	}
	/** Get JVM name.
	 * @return JVM name
	 */
	@ReportableProperty(order=43, value="JVM name.")
	public String getJVMName() {
		return this.jvmName;
	}
	
	/** Get JVM vendor.
	 * @return JVM vendor
	 */
	@ReportableProperty(order=41, value="JVM vendor.")
	public String getJVMVendor() {
		return this.jvmVendor;
	}
	
	/** Get JVM version.
	 * @return JVM version
	 */
	@ReportableProperty(order=42, value="JVM version.")
	public String getJVMVersion() {
		return this.jvmVersion;
	}
	
	/** Get Java library path.
	 * @return Java library path
	 */
	@ReportableProperty(order=52, value="Java library path.")
	public String getLibraryPath() {
		return this.libraryPath;
	}
	
	/** Get maximum memory available to the JVM, in bytes.
	 * @return maximum memory available to the JVM, in bytes
	 */
	@ReportableProperty(order=13, value="Maximum memory available to the " +
			"JVM, in bytes.")
	public long getMaxMemory() {
		return this.maxMemory;
	}
	
	/** Get number of processors available to the JVM.
	 * @return Number of processors.
	 */
	@ReportableProperty(order=12, value="Number of processors available to " +
	"the JVM.")
	public int getNumProcessors() {
		return this.numProcessors;
	}
	
	/** Get operating system name.
	 * @return Operating system name
	 */
	@ReportableProperty(order=21, value="Operating system name.")
	public String getOSName() {
		return this.osName;
	}
	
	/** Get operating system version.
	 * @return Operating system version
	 */
	@ReportableProperty(order=22, value="Operating system version.")
	public String getOSVersion() {
		return  this.osVersion;
	}
}
