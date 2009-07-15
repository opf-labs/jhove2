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

import org.jhove2.annotation.ReportableProperty;

/** JHOVE2 invocation properties.
 * 
 * @author mstrong, slabrams
 */
public class Invocation
	implements Reportable
{
	/** JHOVE2 application command line. */
	protected String commandLine;
	
	/** Invocation date/timestamp. */
	protected Date dateTime;
	
	/** Temporary directory. */
	protected String tempDirectory;
	
	/** User name. */
	protected String userName;
	
	/** Framework current working directory. */
	protected String workingDirectory;

	/** Instantiate a new <code>Invocation</code> reportable.
	 */
	public Invocation() {
		this.dateTime = new Date();
		
		Properties prop = System.getProperties();
		this.tempDirectory    = prop.getProperty("java.io.tmpdir");
		this.userName         = prop.getProperty("user.name");
		this.workingDirectory = prop.getProperty("user.dir");
	}

	/** Get JHOVE2 application command line.
	 * @return JHOVE2 application command line
	 */
	@ReportableProperty(order=2, value="JHOVE2 application command line.")
	public String getCommandLine() {
		return this.commandLine;
	}
	
	/** Get invocation date/timestamp.
	 * @return Invocation date/timestamp
	 */
	@ReportableProperty(order=1, value="Invocation date/timestatmp.")
	public Date getDateTime() {
		return this.dateTime;
	}
	/** Get temporary directory.
	 * @return Temporary directory
	 */
	@ReportableProperty(order=5, value="Temporary directory.")
	public String getTempDirectory() {
		return this.tempDirectory;
	}
	
	/** Get user name.
	 * @return User name
	 */
	@ReportableProperty(order=3, value="User name.")
	public String getUserName() {
		return this.userName;
	}
	
	/** Get Current working directory.
	 * @return Current working directory
	 */
	@ReportableProperty(order=4, value="Current working directory.")
	public String getWorkingDirectory() {
		return this.workingDirectory;
	}
	
	/** Set JHOVE2 application command line.
	 * @param JHOVE2 application command line arguments
	 */
	public void setCommandLine(String [] args) {
		if (args.length > -1) {
			this.commandLine = args[0];
			for (int i=1; i<args.length; i++) {
				this.commandLine += " " + args[i];
			}
		}
	}
	/** Set temporary directory.
	 * @param directory Temporary directory
	 */
	public void setTempDirectory(String directory) {
		this.tempDirectory = directory;
	}
}
