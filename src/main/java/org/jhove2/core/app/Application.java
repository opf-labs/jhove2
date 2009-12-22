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

package org.jhove2.core.app;

import java.util.Date;
import java.util.List;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.source.Source;
import org.jhove2.module.Module;
import org.jhove2.module.display.Displayer;

/**
 * Interface for JHOVE2 applications.
 * 
 * @author mstrong, slabrams
 */
public interface Application
	extends Module
{
	/**
	 * Get application command line.
	 * 
	 * @return Application command line
	 */
	@ReportableProperty(order = 2, value = "Application command line.")
	public String getCommandLine();

	/**
	 * Get application invocation date/timestamp.
	 * 
	 * @return Application invocation date/timestamp
	 */
	@ReportableProperty(order = 1, value = "Application invocation date/timestamp.")
	public Date getDateTime();
	
	/**
	 * Get application displayer.
	 * @return Application displayer
	 */
	@ReportableProperty(order = 7, value = "Application displayer module.")
	public Displayer getDisplayer();
	
	/**
	 * Get application {@link org.jhove2.core.JHOVE2} framework.
	 * @return Application JHOVE2 framework
	 */
	@ReportableProperty(order = 6, value = "Application framework.")
	public JHOVE2 getFramework();
	
	/** Get application {@link org.jhove2.core.app.Installation} properties.
	 * @return Application installation properties
	 */
	@ReportableProperty(order = 4, value = "Application installation properties.")
	public Installation getInstallation();

	/**
	 * Get application {@link org.jhove2.core.app.Invocation} properties. 
	 * @return Application invocation properties
	 */
	@ReportableProperty(order = 5, value = "Application invocation properties.")
	public Invocation getInvocation();
	
	/** Get application {@link org.jhove2.core.source.Source} units.
	 * @return Application source units
	 */
	@ReportableProperty(order = 3, value = "Application source units.")
	public List<Source> getSources();
	
	/**
	 * Set application displayer
	 * @param displayer Application displayer
	 */
	public void setDisplayer(Displayer displayer);
	
	/**
	 * Set application JHOVE2 framework.
	 * 
	 * @param framework
	 *            Application JHOVE2 framework
	 */
	public void setFramework(JHOVE2 framework);
	
	/** Add an application source unit.
	 * @param source Source unit to be added
	 */
	public void setSource(Source source);
}
