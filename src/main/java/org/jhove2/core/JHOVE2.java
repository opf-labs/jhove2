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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

import org.jhove2.annotation.ReportableMessage;
import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.Digest.Algorithm;
import org.jhove2.core.display.Displayer;
import org.jhove2.core.io.Input;
import org.jhove2.core.io.Input.Type;
import org.jhove2.core.io.InputFactory;
import org.jhove2.core.message.FileNotFound;
import org.jhove2.core.message.IOExceptionMessage;
import org.jhove2.core.message.SpringConfigurationException;
import org.jhove2.core.message.UndispatchableIdentifier;
import org.jhove2.core.source.AggregateSource;
import org.jhove2.core.source.BytestreamSource;
import org.jhove2.core.source.ClumpSource;
import org.jhove2.core.source.ContainerSource;
import org.jhove2.core.source.DirectorySource;
import org.jhove2.core.source.FileSource;
import org.jhove2.core.source.Source;
import org.jhove2.core.source.SourceFactory;
import org.jhove2.core.util.Initializer;
import org.jhove2.module.digest.Digester;
import org.jhove2.module.identify.Identify;
import org.springframework.beans.BeansException;

/** JHOVE2 core processing framework.  Applications using the framework should
 * invoke one of the <code>character()</code> methods, <em>not</em> the
 * <code>process()</code> method, which is used by the framework for its
 * internal processing.
 * 
 * @author mstrong, slabrams
 */
public class JHOVE2
	extends AbstractComponent
{
	/** Framework version identifier. */
	public static final String VERSION = "2.0.0";

	/** Framework release date. */
	public static final String DATE = "2009-06-04";
	
	/** Framework development stage. */
	public static final Stage STAGE = Stage.Development;
	
	/** Default buffer size. */
	public static final int DEFAULT_BUFFER_SIZE = 131072;
	
	/** Default fail fast limit. */
	public static final int DEFAULT_FAIL_FAST_LIMIT = 0;
	
	/** Digester module identifier. */
	public static final Identifier DIGESTER =
		new Identifier(Identifier.JHOVE2_PREFIX +
				       Identifier.JHOVE2_REPORTER_INFIX +
				       Digester.class.getName().replace('.', '/'));
	
	/** Identify module identifier. */
	public static final Identifier IDENTIFY =
		new Identifier(Identifier.JHOVE2_PREFIX +
				       Identifier.JHOVE2_REPORTER_INFIX +
				       Identify.class.getName().replace('.', '/'));
	
	/** Platform architecture. */
	protected String architecture;
	
	/** {@link org.jhove2.core.io.Input} buffer size. */
	protected int bufferSize;
	
	/** Java classpath. */
	protected String classpath;
	
	/** Framework dispatch map.  This map associates component and format
	 * identifiers with component names.  The names MUST be defined as Spring
	 * Beans in configuration files.
	 */
	protected Map<String,String> dispatch;
	
	/** Framework displayer. */
	protected Displayer displayer;

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
	
	/** Processed source unit. */
	protected Source src;
	
	/** Used memory, in bytes. */
	protected long useMemory;
	
	/** User name. */
	protected String userName;
	
	/** Current working directory. */
	protected String workingDirectory;

	/** Instantiate a new <code>JHOVE2</code>.
	 */
	public JHOVE2() {
		super(VERSION, DATE, STAGE);
		
		/* Initialize. */
		this.bufferSize       = DEFAULT_BUFFER_SIZE;
		this.failFastLimit    = DEFAULT_FAIL_FAST_LIMIT;
		
		/* Set the framework installation properties. */
		Runtime rt = Runtime.getRuntime();
		this.maxMemory        = rt.maxMemory();
		this.numProcessors    = rt.availableProcessors();
		Properties prop = System.getProperties();
		this.architecture     = prop.getProperty("os.arch");
		this.classpath        = prop.getProperty("java.class.path");
		this.jreHome          = prop.getProperty("java.home");
		this.jreVendor        = prop.getProperty("java.vendor");
		this.jreVersion       = prop.getProperty("java.version");
		this.jvmName          = prop.getProperty("java.vm.name");
		this.jvmVendor        = prop.getProperty("java.vm.vendor");
		this.jvmVersion       = prop.getProperty("java.vm.version");
		this.libraryPath      = prop.getProperty("java.library.path");
		this.osName           = prop.getProperty("os.name");
		this.osVersion        = prop.getProperty("os.version");
		this.userName         = prop.getProperty("user.name");
		this.workingDirectory = prop.getProperty("user.dir");
		
		/* Initialize the dispatching map. */
		try {
			Properties props = Initializer.getProperties("Dispatch");
			this.dispatch = new TreeMap<String,String>();
			Set<String> set = props.stringPropertyNames();
			Iterator<String> iter = set.iterator();
			while (iter.hasNext()) {
				String identifier = iter.next();
				String name       = props.getProperty(identifier);

				this.dispatch.put(identifier, name);
			}
		} catch (BeansException e) {
			@ReportableMessage
			SpringConfigurationException msg =
				new SpringConfigurationException(e);
			addMessage(msg);
		}
	}
	
	/** Reset the framework properties.
	 */
	@Override
	public void reset() {
		super.reset();
		
		this.numBytestreams = 0;
		this.numClumps      = 0;
		this.numContainers  = 0;
		this.numDirectories = 0;
		this.numFiles       = 0;
	}
	
	/** Characterize file system names.
	 * @param names File system names
	 */
	public void characterize(List<String> names) {
		reset();
		
		Source src = null;
		if (names.size() > 1) {
			ClumpSource clump = new ClumpSource();
			Iterator<String> iter = names.iterator();
			while (iter.hasNext()) {
				String name = iter.next();
				Source s = SourceFactory.getSource(name);
				clump.addSource(s);
			}
			src = clump;
		}
		else {
			Iterator<String> iter = names.iterator();
			String name = iter.next();
			src = SourceFactory.getSource(name);
		}
		
		/* Register the source unit with the framework and process. */
		this.src = src;
		process(src);
	}
	
	/** Characterize file system names.
	 * @param name  First file system name
	 * @param names Remaining file system names
	 */
	public void characterize(String name, String... names) {
		reset();
		
		Source src = null;
		if (names.length > 0) {
			ClumpSource clump = new ClumpSource();
			Source s = SourceFactory.getSource(name);
			clump.addSource(s);
			for (int i=0; i<names.length; i++) {
				s = SourceFactory.getSource(names[i]);
				clump.addSource(s);
			}
			src = clump;
		}
		else {
			src = SourceFactory.getSource(name);
		}
		
		/* Register the source unit with the framework and process. */
		process(src);
	}
	
	/** Process source unit encountered during processing.  This method
	 * should <em>not</em> be invoked by applications using the
	 * framework; use one of the <code>characterize()</code> methods
	 * instead.
	 * @param src Source unit
	 */
	public void process(Source src) {
		/* Update summary counts of source units. */
		if      (src instanceof BytestreamSource) {
			this.numBytestreams++;
		}
		else if (src instanceof ClumpSource) {
			this.numClumps++;
		}
		else if (src instanceof ContainerSource) {
			this.numContainers++;
		}
		else if (src instanceof DirectorySource) {
			this.numDirectories++;
		}
		else if (src instanceof FileSource) {
			this.numFiles++;
		}

		if (src instanceof AggregateSource &&
			((AggregateSource) src).isExpandable()) {
			/* Process each member of an expandable aggregate source unit. */
			src.setInitialTime();
			List<Source> list = ((AggregateSource) src).getSources();
			Iterator<Source> iter = list.iterator();
			while (iter.hasNext()) {
				process(iter.next());
			}
			src.setFinalTime();
		}
		else {
			/* Process a single file (or non-expandable container). */
			FileSource f = (FileSource) src;
			f.setInitialTime();
			if (src.isExtant()) {
				Input input = null;
				/* TODO: get buffer size and type from configuration. */
				try {
					input = InputFactory.getInput(f.getFile(), this.bufferSize,
							                      Type.Direct);
				} catch (FileNotFoundException e) {
					@ReportableMessage
					FileNotFound msg = new FileNotFound(f.getFileName());
					f.addMessage(msg);
				} catch (IOException e) {
					@ReportableMessage
					IOExceptionMessage msg =
						new IOExceptionMessage(e);
					f.addMessage(msg);
				}

				/* Identify the presumptive formats of the source unit. */
				Set<FormatIdentification> formats =
					(Set<FormatIdentification>) dispatch(f, input, IDENTIFY);
				
				/* Dispatch the source unit to the appropriate components
				 * for processing.
				 */
				Iterator<FormatIdentification> iter = formats.iterator();
				while (iter.hasNext()) {
					FormatIdentification identification = iter.next();
					dispatch(f, input, identification.getFormat().getIdentifier());
				}
				
				/* Calculate message digests for the source unit. */
				dispatch(f, input, DIGESTER, EnumSet.of(Algorithm.CRC32,
             		   	                                Algorithm.MD5,
             		   	                                Algorithm.SHA1,
             		   	                                Algorithm.SHA256));
			}
			else {
				@ReportableMessage
				FileNotFound msg = new FileNotFound(f.getFileName());
				f.addMessage(msg);
			}
			f.setFinalTime();
		}
	}
	
	/** Dispatch a source unit to the appropriate component for processing.
	 * @param src        Source 
	 * @param input      Source unit input
	 * @param identifier Component or format identifier
	 * @param args       Component-specific arguments
	 */
	public Object dispatch (Source src, Input input, Identifier identifier,
			                Object... args) {
		Object ret = null;

		if (dispatch != null && dispatch.size() > 0) {
			String name = this.dispatch.get(identifier.getValue());
			if (name != null) {
				try {
					Component c = (Component) Initializer.getContext().getBean(name);
					try {
						/* If component is durable, initialize its processing time. */
						if (c instanceof Durable) {
							c.setInitialTime();
						}
					
						/* Invoke the component behavior defined by its capabilities. */
						if (c instanceof Digestable) {
							((Digestable) c).digest(this, input,
									                (EnumSet<Algorithm>) args[0]);
						}
						else if (c instanceof Identifiable) {
							ret = ((Identifiable) c).identify(this, input);
						}
						else if (c instanceof Parsable) {
							ret = ((Parsable) c).parse(this, input);
						}
					
						if (c instanceof Durable) {
							c.setFinalTime();
						}
					} catch (IOException e) {
						@ReportableMessage
						IOExceptionMessage msg = new IOExceptionMessage(e);
						src.addMessage(msg);
					}
					src.addModule(c);
				} catch (BeansException e) {
					@ReportableMessage
					SpringConfigurationException msg =
						new SpringConfigurationException(e);
					src.addMessage(msg);
				}
			}
			else {
				@ReportableMessage
				UndispatchableIdentifier msg =
					new UndispatchableIdentifier(identifier);
				src.addMessage(msg);
			}
		}
		
		return ret;
	}
	
	/** Get platform architecture.
	 * @return Platform architecture
	 */
	@ReportableProperty(value=5, desc="Platform architecture.")
	public String getArchitecture() {
		return this.architecture;
	}

	/** Get {@link org.jhove2.core.io.Input} buffer size.
	 * @return Input buffer size
	 */
	@ReportableProperty(value=18, desc="Input buffer size.")
	public int getBufferSize() {
		return this.bufferSize;
	}
	
	/** Get Java classpath.
	 * @return Java classpath
	 */
	@ReportableProperty(value=16, desc="Java classpath.")
	public String getClasspath() {
		return this.classpath;
	}
	
	/** Get application invocation date/timestamp.
	 * @return Application invocation date/timestamp
	 */
	@ReportableProperty(value=3, desc="Application invocation " +
			"date/timestatmp.")
	public Date getDateTime() {
		return new Date(this.timeInitial);
	}
	
	/** Get {@link org.jhove2.core.display.Displayer}.
	 * @return Displayer
	 */
	@ReportableProperty(value=20, desc="Application displayer.")
	public Displayer getDisplayer() {
		return this.displayer;
	}
	
	/** Get fail fast limit.  Processing of a given source unit is terminated
	 * once the number of detected errors exceeds the limit.  A limit of 0
	 * indicates no fail fast, i.e., process and report all errors. 
	 * @return Fail fast limit
	 */
	@ReportableProperty(value=19, desc="Fail fast limit.")
	public int getFailFastLimit() {
		return this.failFastLimit;
	}
	
	/** Get JRE home.
	 * @return JRE home
	 */
	@ReportableProperty(value=12, desc="JRE home.")
	public String getJREHome() {
		return this.jreHome;
	}
	
	/** Get JRE vendor.
	 * @return JRE vendor
	 */
	@ReportableProperty(value=10, desc="JRE vendor.")
	public String getJREVendor() {
		return this.jreVendor;
	}
	
	/** Get JRE version.
	 * @return JRE version
	 */
	@ReportableProperty(value=11, desc="JRE version.")
	public String getJREVersion() {
		return this.jreVersion;
	}
	/** Get JVM name.
	 * @return JVM name
	 */
	@ReportableProperty(value=14, desc="JVM name.")
	public String getJVMName() {
		return this.jvmName;
	}
	
	/** Get JVM vendor.
	 * @return JVM vendor
	 */
	@ReportableProperty(value=13, desc="JVM vendor.")
	public String getJVMVendor() {
		return this.jvmVendor;
	}
	
	/** Get JVM version.
	 * @return JVM version
	 */
	@ReportableProperty(value=15, desc="JVM version.")
	public String getJVMVersion() {
		return this.jvmVersion;
	}
	
	/** Get Java library path.
	 * @return Java library path
	 */
	@ReportableProperty(value=17, desc="Java library path.")
	public String getLibraryPath() {
		return this.libraryPath;
	}
	
	/** Get maximum memory available to the JVM, in bytes.
	 * @return maximum memory available to the JVM, in bytes
	 */
	@ReportableProperty(value=7, desc="Maximum memory available to the " +
	"JVM, in bytes.")
	public long getMaxMemory() {
		return this.maxMemory;
	}

	/** Get application memory usage.  This is calculated naively as the Java
	 * {@link java.lang.Runtime}'s total memory minus free memory at the time
	 * of method invocation.
	 * @return Memory usage, in bytes
	 */
	@ReportableProperty(value=28, desc="Application memory usage, in bytes.")
	public long getMemoryUsage() {
		Runtime rt = Runtime.getRuntime();
		long use = rt.totalMemory() - rt.freeMemory();
		
		return use;
	}
	
	/** Get number of aggregate source units processed.
	 * @return Number of aggregate source units processed
	 */
	@ReportableProperty(value=25, desc="Number of bytestream source units " +
			"processed.")
	public int getNumBytestreamSources() {
		return this.numBytestreams;
	}
	
	/** Get number of clump source units processed.
	 * @return Number of clump source units processed
	 */
	@ReportableProperty(value=27, desc="Number of clump source units " +
			"processed.")
	public int getNumClumpSources() {
		return this.numClumps;
	}
	
	/** Get number of container source units processed.
	 * @return Number of container source units processed
	 */
	@ReportableProperty(value=26, desc="Number of bytestream source units " +
			"processed.")
	public int getNumContainerSources() {
		return this.numContainers;
	}
	
	/** Get number of directory source units processed.
	 * @return Number of directory source units processed
	 */
	@ReportableProperty(value=23, desc="Number of directory source units " +
			"processed.")
	public int getNumDirectorySources() {
		return this.numDirectories;
	}
	
	/** Get number of file source units processed.
	 * @return Number of file source units processed
	 */
	@ReportableProperty(value=24, desc="Number of file source units " +
			"processed.")
	public int getNumFileSources() {
		return this.numFiles;
	}
	
	/** Get number of processors available to the JVM.
	 * @return Number of processors.
	 */
	@ReportableProperty(value=6, desc="Number of processors available to " +
	"the JVM.")
	public int getNumProcessors() {
		return this.numProcessors;
	}
	
	/** Get number of source units processed.
	 * @return Number of source units processed
	 */
	@ReportableProperty(value=22, desc="Number of source units processed.")
	public int getNumSources() {
		return this.numDirectories + this.numFiles + this.numBytestreams + 
		       this.numContainers  + this.numClumps;
	}
	
	/** Get operating system name.
	 * @return Operating system name
	 */
	@ReportableProperty(value=8, desc="Operating system name.")
	public String getOSName() {
		return this.osName;
	}
	
	/** Get operating system version.
	 * @return Operating system version
	 */
	@ReportableProperty(value=9, desc="Operating system version.")
	public String getOSVersion() {
		return  this.osVersion;
	}
	
	/** Get processed source unit.
	 * @return Processed source unit
	 */
	@ReportableProperty(value=4, desc="Source unit processed by the framework.")
	public Source getSource() {
		return this.src;
	}
	
	/** Get application user name.
	 * @return Application user name
	 */
	@ReportableProperty(value=1, desc="Application user name.")
	public String getUserName() {
		return this.userName;
	}
	
	/** Get application current working directory.
	 * @return Application current working directory
	 */
	@ReportableProperty(value=2, desc="Application current working directory.")
	public String getWorkingDirectory() {
		return this.workingDirectory;
	}
	
	/** Set {@link org.jhove2.core.io.Input} buffer size.
	 * @param size Buffer size
	 */
	public void setBufferSize(int size) {
		this.bufferSize = size;
	}
	
	/** Set {@link org.jhove2.core.display.Displayer}.
	 * @param displayer Displayer
	 */
	public void setDisplayer(Displayer displayer) {
		this.displayer = displayer;
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
