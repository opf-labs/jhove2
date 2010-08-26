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

import java.io.IOException;
import java.util.List;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.config.ConfigInfo;
import org.jhove2.core.Message.Context;
import org.jhove2.core.Message.Severity;
import org.jhove2.core.source.FileSystemSource;
import org.jhove2.core.source.Source;
import org.jhove2.core.source.SourceCounter;
import org.jhove2.module.AbstractModule;
import org.jhove2.module.Command;

/**
 * The JHOVE2 core processing framework.
 * 
 * @author mstrong, slabrams, smorrissey
 */
public class JHOVE2  
	extends AbstractModule
{
	/** Framework version identifier. */
	public static final String VERSION = "2.0.0";

	/** Framework release date. */
	public static final String RELEASE = "2010-09-10";

	/** Framework rights statement. */
	public static final String RIGHTS = "Copyright 2010 by The Regents of the University of California, "
		+ "Ithaka Harbors, Inc., and The Board of Trustees of the Leland "
		+ "Stanford Junior University. "
		+ "Available under the terms of the BSD license.";

	/** Counter to track number and scope of sources processed by framework */
	protected SourceCounter sourceCounter;

	/** List of commands to be executed in sequence in order to characterize Source */
	protected List<Command> commands;

	/** Invocation settings for framework.  If not configured, default values will be used */
	protected Invocation invocation;
	
	/** Installation settings for framework. */
	protected Installation installation;
	
	/** Configuration settings for framework.  If not configured, default values will be used  */
	protected ConfigInfo configInfo;

	/**
	 * Instantiate a new <code>JHOVE2</code> core framework with a default
	 * configuration.
	 * 
	 * @throws JHOVE2Exception
	 */
	public JHOVE2()
		throws JHOVE2Exception
	{
		this(new Invocation());
	}
	
	/**
	 * Instantiate a new <code>JHOVE2</code> core framework with a specific
	 * configuration.
	 * @param invocation Configuration settings for this instance of the
	 *                      JHOVE2 framework
	 */
	public JHOVE2(Invocation invocation) {
		super(VERSION, RELEASE, RIGHTS, Scope.Generic);
		
		this.invocation = invocation;
		this.sourceCounter = new SourceCounter();		
	}

	/**
	 * Characterize a {@link org.jhove2.core.source.Source} unit.
	 * This method will be used as a call-back by any format module that must
	 * recursively characterize components of a format instance.
	 * 
	 * @param source
	 *            Source unit
	 * @throws JHOVE2Exception
	 * @throws IOException
	 */
	public void characterize(Source source)
		throws IOException, JHOVE2Exception
	{
		TimerInfo timer = source.getTimerInfo();
		timer.setStartTime();
		
		/* Update summary counts of source units, by scope. */
        try {
            this.sourceCounter.incrementSourceCounter(source);				
		
            /* Characterize the source unit. */
            boolean tryIt = true;
            if (source instanceof FileSystemSource) {
                FileSystemSource fs = (FileSystemSource) source;
                String name = fs.getSourceName();
                if (!fs.isExtant()) {
                    source.addMessage(new Message(Severity.ERROR,
                        Context.PROCESS,
                        "org.jhove2.core.source.FileSystemSource.FileNotFoundMessage",
                        new Object[]{name}, this.getConfigInfo()));
                    tryIt = false;
                }
                else if (!fs.isReadable()) {
                    source.addMessage(new Message(Severity.ERROR,
                        Context.PROCESS,
                        "org.jhove2.core.source.FileSystemSource.FileNotReadableMessage",
                        new Object[]{name}, this.getConfigInfo()));
                    tryIt = false;
                }
            }
		    if (tryIt) {
		        source.setDeleteTempFiles(this.getInvocation().getDeleteTempFiles());
		        for (Command command : this.commands){
		            TimerInfo time2 = command.getTimerInfo();
		            time2.resetStartTime();
		            try {
		                command.execute(this, source);
		            }
		            finally {
		                time2.setEndTime();
		            }
		        }
			}
		}
        finally {
			source.close();
			timer.setEndTime();
		}
	}
    
	/**
	 * Determine if the fail fast limit has been exceeded.
	 * 
	 * @param numErrors
	 *            Number of errors
	 * @return True if the fail fast limit has been exceeded
	 */
	public boolean failFast(int numErrors) {
		if (this.invocation.getFailFastLimit() > 0 && 
				numErrors > this.invocation.getFailFastLimit()) {
			return true;
		}
		return false;
	}

	/**
	 * Get list of commands to be executed in sequence to characterize
	 * a source unit.
	 * @return List of command to be executed
	 */
	@ReportableProperty(order = 3, value = "Configured commands.")
	public List<Command> getCommands() {
		return commands;
	}

	/**
	 * Get framework invocation properties.
	 * @return Framework invocation properties
	 */
	@ReportableProperty(order = 2, value="Framework invocation properties.")
	public Invocation getInvocation() {
		return invocation;
	}
	
	/** Get framework {@link org.jhove2.core.Installation} properties.
	 * @return Framework installation properties
	 */
	@ReportableProperty(order = 1, value = "Framework installation properties.")
	public Installation getInstallation() {
		return installation;
	}
	
	/**
	 * Get framework memory usage. This is calculated naively as the Java
	 * {@link java.lang.Runtime}'s total memory minus free memory at the time of
	 * method abstractApplication.
	 * 
	 * @return Memory usage, in bytes
	 */
	@ReportableProperty(order = 6, value = "Framework memory usage, in bytes.")
	public long getMemoryUsage() {
		Runtime rt = Runtime.getRuntime();
		long use = rt.totalMemory() - rt.freeMemory();

		return use;
	}
	
	/**
	 * Get counter to track number and scope of source units processed
	 * by the JHOVE2 framework.
	 * @return Source unit counter
	 */
	@ReportableProperty(order = 5, value = "Source unit counter, by scope.")
	public SourceCounter getSourceCounter() {
		return sourceCounter;
	}
	
	
	/**
	 * Set commands to be executed in sequence to characterize
	 * {@link org.jhove2.core.source.Source} units.
	 * @param commands Commands to be executed
	 */
	public void setCommands(List<Command> commands) {
		this.commands = commands;
	}

	/**
	 * Set framework invocation properties.
	 * @param invocation Framework invocation properties
	 */
	public void setInvocation(Invocation invocation) {
		this.invocation = invocation;
	}

	/**
	 * Set framework installation properties.
	 * @param installation Framework installation properties
	 */
	public void setInstallation(Installation installation) {
		this.installation = installation;
	}

	/**
	 * Set counter to track number and scope of
	 * {@link org.jhove2.core.source.Source}s processed by
	 * the JHOVE2 framework.
	 * @param sourceCounter Source unit counter
	 */
	public void setSourceCounter(SourceCounter sourceCounter) {
		this.sourceCounter = sourceCounter;
	}

	/**
	 * @return the configInfo
	 */
	public ConfigInfo getConfigInfo() {
		return configInfo;
	}

	/**
	 * @param configInfo the configInfo to set
	 */
	public void setConfigInfo(ConfigInfo configInfo) {
		this.configInfo = configInfo;
	}

}
