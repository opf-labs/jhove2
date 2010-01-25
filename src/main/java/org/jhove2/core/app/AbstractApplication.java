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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jhove2.core.Installation;
import org.jhove2.core.Invocation;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.reportable.ReportableFactory;
import org.jhove2.core.source.Source;
import org.jhove2.module.AbstractModule;
import org.jhove2.module.display.Displayer;

/**
 * Abstract JHOVE2 application .
 * 
 * @author mstrong, slabrams, smorrissey
 */
public abstract class AbstractApplication
	extends AbstractModule
	implements Application
{
	/** Application command line. */
	protected String commandLine;

	/** Application invocation date/timestamp. */
	protected Date dateTime;

	/** Application framework. */
	protected JHOVE2 framework;
	
	/** Application displayer */
	protected Displayer displayer;

	/** Application installation properties.  */
	protected Installation installation;

	/** Application invocation properties.  */
	protected Invocation invocation;
	
	/** Application source units. */
	protected List<Source> sources;

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
	public AbstractApplication(String version, String release, String rights)
		throws JHOVE2Exception
	{
		super(version, release, rights);

		/* Default application installation and invocation properties. */
		this.setInstallation(Installation.getInstance());
		this.setInvocation  (new Invocation());
		
		/* Initialize the default displayer. */
		try {
			this.displayer = ReportableFactory.getReportable(Displayer.class,
					Displayer.DEFAULT_DISPLAYER_TYPE);
		}
		catch (JHOVE2Exception e){
			try {
				this.displayer = 
					(Displayer) (Class.forName(Displayer.DEFAULT_DISPLAYER_CLASS).newInstance());
			}
			catch (Exception ex){
				throw new JHOVE2Exception("Unable to instantiate default displayer", ex);
			}
		}
		
		this.sources = new ArrayList<Source>();
	}

	/** Get application {@link org.jhove2.core.Invocation} properties.
	 * @return Application invocation properties
	 */
	@Override
	public Invocation getInvocation() {
		return this.invocation;
	}

	/** Get application {@link org.jhove2.core.Installation} properties.
	 * @return Application installation properties
	 */
	@Override
	public Installation getInstallation() {
		return this.installation;
	}

	/**
	 * Get application command line.
	 * 
	 * @return Application command line
	 * @see org.jhove2.core.app.Application#getCommandLine()
	 */
	@Override
	public String getCommandLine() {
		return this.commandLine;
	}

	/**
	 * Get application invocation date/timestamp.
	 * 
	 * @return Application invocation date/timestamp
	 * @see org.jhove2.core.app.Application#getDateTime()
	 */
	@Override
	public Date getDateTime() {
		return this.dateTime;
	}

	/** Get application displayer.
	 * @return Application displayer
	 */
	@Override
	public Displayer getDisplayer() {
		return this.displayer;
	}
	
	/**
	 * Get application framework.
	 * 
	 * @return Application framework
	 * @see org.jhove2.core.app.Application#getFramework()
	 */
	@Override
	public JHOVE2 getFramework() {
		return this.framework;
	}
	
	/** Get application {@link org.jhove2.core.source.Source} units.
	 * @return Application source units
	 */
	public List<Source> getSources() {
		return this.sources;
	}

	/** Set application command line.
	 * @param commandLine Application command line
	 */
	public void setCommandLine(String commandLine) {
		this.commandLine = commandLine;
	}

	/** Set application invocation date/timestamp.
	 * @param dateTime Application invocation date/timestamp
	 */
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	/** Set application displayer.
	 * @param displayer Application displayer
	 */
	@Override
	public void setDisplayer(Displayer displayer) {
		this.displayer = displayer;
	}

	/**
	 * Set application framework.
	 * 
	 * @param framework
	 *            Application framework
	 * @see org.jhove2.core.app.Application#setFramework(org.jhove2.core.JHOVE2)
	 */
	@Override
	public void setFramework(JHOVE2 framework) {
		this.framework = framework;
	}

	/** Set application {@link org.jhove2.core.Installation} properties.
	 * @param installation Application installation properties
	 */
	public void setInstallation(Installation installation) {
		this.installation = installation;
	}

	/** Set application {@link org.jhove2.core.Invocation} properties.
	 * @param invocation Application invocation properties
	 */
	public void setInvocation(Invocation invocation) {
		this.invocation = invocation;
	}

	/** Add an application source unit.
	 * @param source Source unit to be added
	 */
	@Override
	public void setSource(Source source) {
		this.sources.add(source);
	}
}
