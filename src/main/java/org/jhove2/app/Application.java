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

import java.util.Date;
import java.util.List;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.io.Input.Type;
import org.jhove2.module.Module;

/** Interface for JHOVE2 applications.
 * 
 * @author mstrong, slabrams
 */
public interface Application
	extends Module
{
	/** Get {@link org.jhove2.core.io.Input} buffer size.
	 * @return Buffer size
	 */
	public int getBufferSize();
	
	/** Get {@link org.jhove2.core.io.Input} buffer type.
	 * @return Input buffer type
	 */
	public Type getBufferType();
	
	/** Get message digests flag.
	 * @return Message digests flag; if true, calculate message digests
	 */
	public boolean getCalcDigests();
	
	/** Get application command line.
	 * @return Application command line
	 */
	@ReportableProperty(order=3, value="Application command line.")
	public String getCommandLine();
	
	/** Get application invocation date/timestamp.
	 * @return Application invocation date/timestamp
	 */
	@ReportableProperty(order=2, value="Application invocation date/timestatmp.")
	public Date getDateTime();
		
	/** Get delete temporary files flag.
	 * @param Delete temporary files flag
	 */
	public boolean getDeleteTempFiles();
	
	/** Get {@link org.jhove2.core.Displayeble}.
	 * @return AbstractDisplayer
	 */
	public String getDisplayer();
	
	/** Get fail fast limit.
	 * @return Fail fast limit
	 */
	public int getFailFastLimit();

	/** Get application framework.
	 * @return Application framework
	 */
	@ReportableProperty(order=5, value="Application framework.")
	public JHOVE2 getJHOVE2();

	/** Get output file.
	 * @return Output file, or null if no file is specified
	 */
	public String getOutputFile();
	
	/** Get file system path names.
	 * @return File system path names
	 */
	public List<String> getPathNames();
	
	/** Get show identifiers flag.
	 * @param Show identifiers flag
	 */
	public boolean getShowIdentifiers();
	
	/** Get temporary directory.
	 * @return Temporary directory
	 */
	public String getTempDirectory();
	
	/** Get application user name.
	 * @return Application user name
	 */
	@ReportableProperty(order=1, value="Application user name.")
	public String getUserName();
	
	/** Get application current working directory.
	 * @return Appliction current working directory
	 */
	@ReportableProperty(order=4, value="Application current working directory.")
	public String getWorkingDirectory();
	
	/** Set application framework.
	 * @param jhove2 Application framework
	 */
	public void setJHOVE2(JHOVE2 jhove2);
}
