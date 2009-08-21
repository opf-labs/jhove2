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

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.app.Application;
import org.jhove2.core.config.Configure;
import org.jhove2.core.info.ReportableInfo;
import org.jhove2.core.info.ReportablePropertyInfo;
import org.jhove2.core.info.ReportableSourceInfo;
import org.jhove2.core.io.Input.Type;
import org.jhove2.core.source.ClumpSource;
import org.jhove2.core.source.DirectorySource;
import org.jhove2.core.source.FileSource;
import org.jhove2.core.source.FileSetSource;
import org.jhove2.core.source.Source;
import org.jhove2.core.source.SourceFactory;
import org.jhove2.core.source.ZipDirectorySource;
import org.jhove2.core.source.ZipFileSource;
import org.jhove2.module.AbstractModule;
import org.jhove2.module.Module;
import org.jhove2.module.characterize.Characterizer;
import org.jhove2.module.dispatch.Dispatcher;
import org.jhove2.module.dispatch.Dispatcher.Disposition;
import org.jhove2.module.display.Displayer;

/**
 * The JHOVE2 core processing framework.
 * 
 * @author mstrong, slabrams
 */
public class JHOVE2
	extends AbstractModule
{
	/** Framework version identifier. */
	public static final String VERSION = "0.5.2";

	/** Framework release date. */
	public static final String RELEASE = "2009-08-04";

	/** Framework rights statement. */
	public static final String RIGHTS = "Copyright 2009 by The Regents of the University of California, "
			+ "Ithaka Harbors, Inc., and The Board of Trustees of the Leland "
			+ "Stanford Junior University. "
			+ "Available under the terms of the BSD license.";

	/** Framework display visibilities. */
	public enum DisplayVisbility {
		Always, IfFalse, IfNegative, IfNonNegative, IfNonPositive, IfNonZero, IfPositive, IfTrue, IfZero, Never
	}

	/** ISO 8601 date/time format. */
	public static final SimpleDateFormat ISO8601 = new SimpleDateFormat(
			"yyyy-MM-ss'T'hh:mm:ssZ");

	/** Default {@link org.jhove2.core.io.Input} buffer size. */
	public static final int DEFAULT_BUFFER_SIZE = 131072;

	/** Default {@link org.jhove2.core.io.Input} buffer type. */
	public static final Type DEFAULT_BUFFER_TYPE = Type.Direct;

	/** Default message digests flag: don't calculate digests. */
	public static final boolean DEFAULT_CALC_DIGESTS = false;

	/** Default delete temporary files flag: delete files. */
	public static final boolean DEFAULT_DELETE_TEMP_FILES = true;

	/** Default {@link org.jhove2.module.display.Displayer}. */
	public static final String DEFAULT_DISPLAYER = "Text";

	/** Default fail fast limit. */
	public static final int DEFAULT_FAIL_FAST_LIMIT = 0;

	/** Default show identifiers flag: don't show identifiers. */
	public static final boolean DEFAULT_SHOW_IDENTIFIERS = false;

	/** Default temporary file prefix. */
	public static final String DEFAULT_TEMP_PREFIX = "jhove2-tmp";

	/** Default temporary file suffix. */
	public static final String DEFAULT_TEMP_SUFFIX = ".dat";

	/** JHOVE2 application. */
	protected Application app;

	/** {@link org.jhove2.core.io.Input} buffer size. */
	protected int bufferSize;

	/** {@link org.jhove2.core.io.Input} buffer type. */
	protected Type bufferType;

	/** Framework message digests flag; if true, calculate digests. */
	protected boolean calcDigests;

	/** Framework characterizer module. */
	protected Characterizer characterizer;

	/** Framework delete temporary files flag; if true, delete files. */
	protected boolean deleteTempFiles;

	/** Framework dispatcher module. */
	protected Dispatcher dispatcher;

	/** Framework displayer module. */
	protected Displayer displayer;

	/**
	 * Framework fail fast limit. Processing of a given source unit is
	 * terminated once the number of detected errors exceeds the limit. A limit
	 * of 0 indicates no fail fast, i.e., process and report all errors.
	 */
	protected int failFastLimit;

	/** Framework installation properties. */
	protected Installation installation;

	/** Number of bytestream source units. */
	protected int numBytestreams;

	/** Number of clump source units. */
	protected int numClumps;

	/** Number of directory source units. */
	protected int numDirectories;

	/** Number of file source units. */
	protected int numFiles;

	/** Number of pseudo-directory source units. */
	protected int numFileSets;

	/**
	 * Framework show identifiers flag; if true, show identifiers in non-XML
	 * display modes.
	 */
	protected boolean showIdentifiers;

	/** Framework source unit. */
	protected Source source;

	/** Framework temporary directory. */
	protected String tempDirectory;

	/** Framework temporary file prefix. */
	protected String tempPrefix;

	/** Framework temporary file suffix. */
	protected String tempSuffix;

	/** Used memory, in bytes. */
	protected long useMemory;

	/** Framework display displayVisbilities. */
	protected Map<String, DisplayVisbility> visbilities;

	/**
	 * Instantiate a new <code>JHOVE2</code> core framework.
	 * 
	 * @throws JHOVE2Exception
	 */
	public JHOVE2() throws JHOVE2Exception {
		super(VERSION, RELEASE, RIGHTS);

		this.installation = new Installation();

		this.bufferSize = DEFAULT_BUFFER_SIZE;
		this.bufferType = DEFAULT_BUFFER_TYPE;
		this.calcDigests = DEFAULT_CALC_DIGESTS;
		this.deleteTempFiles = DEFAULT_DELETE_TEMP_FILES;
		this.failFastLimit = DEFAULT_FAIL_FAST_LIMIT;
		this.tempPrefix = DEFAULT_TEMP_PREFIX;
		this.tempSuffix = DEFAULT_TEMP_SUFFIX;

		this.numBytestreams = 0;
		this.numClumps = 0;
		this.numDirectories = 0;
		this.numFiles = 0;
		this.numFileSets = 0;

		/* Initialize the displayer displayVisbilities map. */
		this.visbilities = new TreeMap<String, DisplayVisbility>();
		Properties props = Configure.getProperties("DisplayVisibility");
		if (props != null) {
			Set<String> keys = props.stringPropertyNames();
			for (String key : keys) {
				DisplayVisbility value = DisplayVisbility.valueOf(props
						.getProperty(key));
				if (value != null) {
					this.visbilities.put(key, value);
				}
			}
		}
	}

	/**
	 * Characterize file system objects (files and directories).
	 * 
	 * @param pathName
	 *            First path name
	 * @param pathNames
	 *            Remaining path names
	 * @throws IOException
	 * @throws JHOVE2Exception
	 */
	public void characterize(String pathName, String... pathNames)
			throws IOException, JHOVE2Exception {
		List<String> list = new ArrayList<String>();
		list.add(pathName);
		if (pathNames != null && pathNames.length > 0) {
			for (int i = 0; i < pathNames.length; i++) {
				list.add(pathNames[i]);
			}
		}
		characterize(list);
	}

	/**
	 * Characterize file system objects (files and directories).
	 * 
	 * @param pathNames
	 *            File system path names
	 * @throws IOException
	 * @throws JHOVE2Exception
	 */
	public void characterize(List<String> pathNames) throws IOException,
			JHOVE2Exception {
		if (this.characterizer != null) {
			this.characterizer.setStartTime();
		}
		Iterator<String> iter = pathNames.iterator();
		if (pathNames.size() == 1) {
			String pathName = iter.next();
			this.source = SourceFactory.getSource(pathName);
			characterize(this.source);
		} else {
			this.source = new FileSetSource();
			while (iter.hasNext()) {
				String pathName = iter.next();
				Source src = SourceFactory.getSource(pathName);
				((FileSetSource) this.source).addChildSource(src);
			}
			characterize(this.source);
		}
		if (this.characterizer != null) {
			this.characterizer.setEndTime();
		}
	}

	/**
	 * Characterize a source unit.
	 * 
	 * @param source
	 *            Source unit
	 * @throws JHOVE2Exception
	 * @throws IOException
	 */
	public void characterize(Source source) throws IOException, JHOVE2Exception {
		source.setRestartTime();

		if (this.app != null) {
			this.bufferSize = this.app.getBufferSize();
			this.bufferType = this.app.getBufferType();
			this.calcDigests = this.app.getCalcDigests();
			this.deleteTempFiles = this.app.getDeleteTempFiles();
			this.failFastLimit = this.app.getFailFastLimit();
			this.showIdentifiers = this.app.getShowIdentifiers();
			this.tempDirectory = this.app.getTempDirectory();
		}

		/* Update summary counts of source units, by type. */
		/*
		 * if (source instanceof BytestreamSource) { this.numBytestreams++; }
		 * else
		 */if (source instanceof ClumpSource) {
			this.numClumps++;
		} else if (source instanceof DirectorySource
				|| source instanceof ZipDirectorySource) {
			this.numDirectories++;
		} else if (source instanceof FileSource
				|| source instanceof ZipFileSource) {
			this.numFiles++;
		} else if (source instanceof FileSetSource) {
			this.numFileSets++;
		}

		if (this.characterizer != null) {
			source.setDeleteTempFiles(this.deleteTempFiles);
			try {
				this.characterizer.characterize(this, source);
			} finally {
				source.close();
			}
		}
		source.setEndTime();
	}

	/**
	 * Dispatch a source unit to the module associated with an identifier.
	 * 
	 * @param source
	 *            Source unit
	 * @param identifier
	 *            Module identifier
	 * @return Module
	 * @throws EOFException
	 *             End-of-file encountered parsing the source unit
	 * @throws IOException
	 *             I/O exception encountered parsing the source unit
	 * @throws JHOVE2Exception
	 */
	public Module dispatch(Source source, I8R identifier) throws EOFException,
			IOException, JHOVE2Exception {
		Module module = null;

		if (this.dispatcher != null) {
			this.dispatcher.setRestartTime();
			module = this.dispatcher.dispatch(this, source, identifier);
			this.dispatcher.setEndTime();
		}

		return module;
	}

	/**
	 * Dispatch a source unit to the module, adding the module to the source.
	 * 
	 * @param source
	 *            Source unit
	 * @param module
	 *            Module
	 * @throws EOFException
	 *             End-of-file encountered parsing the source unit
	 * @throws IOException
	 *             I/O exception encountered parsing the source unit
	 * @throws JHOVE2Exception
	 */
	public void dispatch(Source source, Module module) throws EOFException,
			IOException, JHOVE2Exception {
		dispatch(source, module, Disposition.AddToSource);
	}

	/**
	 * Dispatch a source unit to the module.
	 * 
	 * @param source
	 *            Source unit
	 * @param module
	 *            Module
	 * @param disposition
	 *            Module disposition
	 * @throws EOFException
	 *             End-of-file encountered parsing the source unit
	 * @throws IOException
	 *             I/O exception encountered parsing the source unit
	 * @throws JHOVE2Exception
	 */
	public void dispatch(Source source, Module module, Disposition disposition)
			throws EOFException, IOException, JHOVE2Exception {
		if (this.dispatcher != null) {
			this.dispatcher.setRestartTime();
			this.dispatcher.dispatch(this, source, module, disposition);
			this.dispatcher.setEndTime();
		}
	}

	/**
	 * Display the JHOVE2 framework to the standard output stream.
	 * 
	 * @throws FileNotFoundException
	 *             Can't create output file
	 * @throws JHOVE2Exception
	 *             Can't instantiate displayer
	 */
	public void display() throws FileNotFoundException, JHOVE2Exception {
		display(this);
	}

	/**
	 * Display {@link org.jhove2.core.Reportable} to the standard output stream.
	 * 
	 * @param reportable
	 *            Reportable
	 * @throws FileNotFoundException
	 *             Can't create output file
	 * @throws JHOVE2Exception
	 *             Can't instantiate displayer
	 */
	public void display(Reportable reportable) throws FileNotFoundException,
			JHOVE2Exception {
		PrintStream out = System.out;
		if (this.app != null) {
			String outputFile = this.app.getOutputFile();
			if (outputFile != null) {
				out = new PrintStream(outputFile);
			}
		}
		display(reportable, out);
	}

	/**
	 * Display {@link org.jhove2.core.Reportable}.
	 * 
	 * @param reportable
	 *            Reportable
	 * @param out
	 *            Print stream
	 * @throws JHOVE2Exception
	 *             Can't instantiate displayer
	 */
	public void display(Reportable reportable, PrintStream out)
			throws JHOVE2Exception {
		if (this.displayer == null) {
			this.displayer = Configure.getReportable(Displayer.class,
					this.app != null ? this.app.getDisplayer()
							: DEFAULT_DISPLAYER);
		}

		this.displayer.setStartTime();
		this.displayer.setShowIdentifiers(this.showIdentifiers);

		this.displayer.startDisplay(out, 0);
		display(out, reportable, 0, 0);
		this.displayer.endDisplay(out, 0);

		this.displayer.setEndTime();
	}

	/**
	 * Display a {@link org.jhove2.core.Reportable}.
	 * 
	 * @param out
	 *            Print stream
	 * @param reportable
	 *            Reportable
	 * @param level
	 *            Nesting level
	 * @param order
	 *            Ordinal position of this reportable with respect to its
	 *            enclosing reportable or collection
	 */
	protected void display(PrintStream out, Reportable reportable, int level,
			int order) {
		ReportableInfo reportableInfo = new ReportableInfo(reportable);
		String name = reportableInfo.getName();
		I8R identifier = reportableInfo.getIdentifier();
		this.displayer.startReportable(out, level, name, identifier, order);

		int or = 0;
		List<ReportableSourceInfo> list = reportableInfo.getProperties();
		for (ReportableSourceInfo source : list) {
			Set<ReportablePropertyInfo> props = source.getProperties();
			for (ReportablePropertyInfo prop : props) {
				I8R id = prop.getIdentifier();
				DisplayVisbility visbility = this.visbilities
						.get(id.getValue());
				if (visbility != null && visbility == DisplayVisbility.Never) {
					continue;
				}
				Method method = prop.getMethod();
				String nm = method.getName();
				if (nm.indexOf("get") == 0) {
					nm = nm.substring(3);
				}

				try {
					Object value = method.invoke(reportable);
					if (value != null) {
						if (visbility != null) {
							if (value instanceof Boolean) {
								boolean b = ((Boolean) value).booleanValue();
								if ((b && visbility == DisplayVisbility.IfFalse)
										|| (!b && visbility == DisplayVisbility.IfTrue)) {
									continue;
								}
							} else if (value instanceof Number) {
								double d = ((Number) value).doubleValue();
								if ((d == 0.0 && visbility == DisplayVisbility.IfNonZero)
										|| (d != 0.0 && visbility == DisplayVisbility.IfZero)
										|| (d < 0.0 && visbility == DisplayVisbility.IfNonNegative)
										|| (d > 0.0 && visbility == DisplayVisbility.IfNonPositive)
										|| (d <= 0.0 && visbility == DisplayVisbility.IfPositive)
										|| (d >= 0.0 && visbility == DisplayVisbility.IfNegative)) {
									continue;
								}
							}
						}
						display(out, level, nm, id, value, or++);
					}
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		this.displayer.endReportable(out, level, name, identifier);
	}

	/**
	 * Display a {@link org.jhove2.annotation.ReportableProperty}.
	 * 
	 * @param out
	 *            Print stream
	 * @param level
	 *            Nesting level
	 * @param name
	 *            Property name
	 * @param identifier
	 *            Property identifier
	 * @param value
	 *            Property value
	 * @param order
	 *            Ordinal position of this property with respect to its
	 *            enclosing reportable or collection
	 */
	protected void display(PrintStream out, int level, String name,
			I8R identifier, Object value, int order) {
		if (value instanceof List) {
			List<?> lst = (List<?>) value;
			int size = lst.size();
			if (size > 0) {
				this.displayer.startCollection(out, level + 1, name,
						identifier, size, order);
				String nm = singularName(name);
				I8R id = singularIdentifier(identifier);
				int i = 0;
				for (Object prop : lst) {
					this.display(out, level + 1, nm, id, prop, i++);
				}
				this.displayer.endCollection(out, level + 1, name, identifier,
						size);
			}
		} else if (value instanceof Set) {
			Set<?> set = (Set<?>) value;
			int size = set.size();
			if (size > 0) {
				this.displayer.startCollection(out, level + 1, name,
						identifier, size, order);
				String nm = singularName(name);
				I8R id = singularIdentifier(identifier);
				int i = 0;
				for (Object prop : set) {
					display(out, level + 1, nm, id, prop, i++);
				}
				this.displayer.endCollection(out, level + 1, name, identifier,
						size);
			}
		} else if (value instanceof Reportable) {
			this.displayer.startReportable(out, level + 1, name, identifier,
					order);
			display(out, (Reportable) value, level + 2, 0);
			this.displayer.endReportable(out, level + 1, name, identifier);
		} else {
			if (value instanceof Date) {
				value = ISO8601.format(value);
			}
			this.displayer.displayProperty(out, level + 1, name, identifier,
					value, order);
		}
	}

	/**
	 * Get the singular form of a plural property identifier.
	 * 
	 * @param identifier
	 *            Property identifier
	 * @return Singular form of a property identifier
	 */
	public static synchronized I8R singularIdentifier(I8R identifier) {
		I8R singular = null;
		String value = identifier.getValue();
		int in = value.lastIndexOf('/') + 1;
		int len = value.length();
		if (value.substring(len - 3).equals("ies")) {
			singular = new I8R(value + "/" + value.substring(in, len - 3) + "y");
		} else {
			singular = new I8R(value + "/" + value.substring(in, len - 1));
		}

		return singular;
	}

	/**
	 * Get the singular form of a plural property name.
	 * 
	 * @param name
	 *            Property name
	 * @return Singular form of a property name
	 */
	public static synchronized String singularName(String name) {
		String singular = null;
		int len = name.length();
		if (name.substring(len - 3).equals("ies")) {
			singular = name.substring(0, len - 3) + "y";
		} else {
			singular = name.substring(0, len - 1);
		}

		return singular;
	}

	/**
	 * Determine if the fail fast limit has been exceeded.
	 * 
	 * @param numErrors
	 *            Number of errors
	 * @return True if the fail fast limit has been exceeded
	 */
	public boolean failFast(int numErrors) {
		if (failFastLimit > 0 && numErrors > failFastLimit) {
			return true;
		}

		return false;
	}

	/**
	 * Get JHOVE2 application.
	 * 
	 * @return JHOVE2 application
	 */
	public Application getApplication() {
		return this.app;
	}

	/**
	 * Get {@link org.jhove2.core.io.Input} buffer size.
	 * 
	 * @return Input buffer size
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
	 * Get framework message digests flag; if true, calculate digests.
	 * 
	 * @return Framework message digests flag
	 */
	@ReportableProperty(order = 3, value = "Framework message digests flag; if "
			+ "true, calculate digests.")
	public boolean getCalcDigests() {
		return this.calcDigests;
	}

	/**
	 * Get framework characterizer module.
	 * 
	 * @return Framework characterizer module
	 */
	@ReportableProperty(order = 31, value = "Framework characterizer module.")
	public Characterizer getCharacterizer() {
		return this.characterizer;
	}

	/**
	 * Get framework delete temporary files flag; if true, delete files.
	 * 
	 * @return Framework delete temporary files flag
	 */
	public boolean getDeleteTempFiles() {
		return this.deleteTempFiles;
	}

	/**
	 * Get framework dispatcher module.
	 * 
	 * @return Framework dispatch module
	 */
	@ReportableProperty(order = 32, value = "Framework dispatcher module.")
	public Dispatcher getDispatcher() {
		return this.dispatcher;
	}

	/**
	 * Get framework displayer module.
	 * 
	 * @return Framework displayer module
	 */
	@ReportableProperty(order = 33, value = "Framework displayer module.")
	public Displayer getDisplayer() {
		return this.displayer;
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
	 * Get framework installation properties.
	 * 
	 * @return Framework installation properties.
	 */
	@ReportableProperty(order = 21, value = "Framework installation properties.")
	public Installation getInstallation() {
		return this.installation;
	}

	/**
	 * Get framework memory usage. This is calculated naively as the Java
	 * {@link java.lang.Runtime}'s total memory minus free memory at the time of
	 * method abstractApplication.
	 * 
	 * @return Memory usage, in bytes
	 */
	@ReportableProperty(order = 51, value = "Framework memory usage, in bytes.")
	public long getMemoryUsage() {
		Runtime rt = Runtime.getRuntime();
		long use = rt.totalMemory() - rt.freeMemory();

		return use;
	}

	/**
	 * Get number of aggregate source units processed.
	 * 
	 * @return Number of aggregate source units processed
	 */
	@ReportableProperty(order = 46, value = "Number of bytestream source units "
			+ "processed.")
	public int getNumBytestreamSources() {
		return this.numBytestreams;
	}

	/**
	 * Get number of clump source units processed.
	 * 
	 * @return Number of clump source units processed
	 */
	@ReportableProperty(order = 42, value = "Number of clump source units "
			+ "processed.")
	public int getNumClumpSources() {
		return this.numClumps;
	}

	/**
	 * Get number of directory source units processed, including both file
	 * system directories and Zip entry directories.
	 * 
	 * @return Number of directory source units processed
	 */
	@ReportableProperty(order = 44, value = "Number of directory source units "
			+ "processed, including both file system directories and Zip "
			+ "entry directories.")
	public int getNumDirectorySources() {
		return this.numDirectories;
	}

	/**
	 * Get number of file source units processed, including both file system
	 * files and Zip entry files.
	 * 
	 * @return Number of file source units processed
	 */
	@ReportableProperty(order = 45, value = "Number of file source units "
			+ "processed, including both file system files and Zip entry "
			+ "files.")
	public int getNumFileSources() {
		return this.numFiles;
	}

	/**
	 * Get number of source units processed.
	 * 
	 * @return Number of source units processed
	 */
	@ReportableProperty(order = 41, value = "Number of source units processed.")
	public int getNumSources() {
		return this.numFileSets + this.numDirectories + this.numClumps
				+ this.numFiles + this.numBytestreams;
	}

	/**
	 * Get number of file set source units processed.
	 * 
	 * @return Number of file set source units processed
	 */
	@ReportableProperty(order = 45, value = "Number of file set source units "
			+ "processed.")
	public int getNumFileSetSources() {
		return this.numFileSets;
	}

	/**
	 * Get framework show identifiers flag; if true, show identifiers in non-XML
	 * display modes.
	 * 
	 * @return Framework message digests flag
	 */
	@ReportableProperty(order = 6, value = "Framework show identifiers flag; "
			+ "if true, show identifiers in non-XML display modes.")
	public boolean getShowIdentifiers() {
		return this.showIdentifiers;
	}

	/**
	 * Get framework source unit.
	 * 
	 * @return Framework source unit
	 */
	@ReportableProperty(order = 11, value = "Framework source unit.")
	public Source getSource() {
		return this.source;
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
	 * Get framework temporary file prefix.
	 * 
	 * @return Framework temporary file prefix
	 */
	@ReportableProperty(order = 8, value = "Framework temporary file prefix.")
	public String getTempPrefix() {
		return this.tempPrefix;
	}

	/**
	 * Get framework temporary file suffix.
	 * 
	 * @return Framework temporary file suffix
	 */
	@ReportableProperty(order = 9, value = "Framework temporary file suffix.")
	public String getTempSuffix() {
		return this.tempSuffix;
	}

	/**
	 * Increment the number of bytestream source units.
	 */
	public void incrementNumBytestreams() {
		this.numBytestreams++;
	}

	/**
	 * Increment the number of clump source units.
	 */
	public void incrementNumClumps() {
		this.numClumps++;
	}

	/**
	 * Increment the number of directory source units, including source units
	 * for both file system files and Zip entry files.
	 */
	public void incrementNumDirectories() {
		this.numDirectories++;
	}

	/**
	 * Increment the number of file source units, including source units for
	 * both file system files and Zip entry files.
	 */
	public void incrementNumFiles() {
		this.numFiles++;
	}

	/**
	 * Increment the number of file set source units.
	 */
	public void incrementNumFileSets() {
		this.numFileSets++;
	}

	/**
	 * Set JHOVE2 application.
	 * 
	 * @param app
	 *            JHOVE2 application
	 */
	public void setApplication(Application app) {
		this.app = app;
		this.app.setFramework(this);
	}

	/**
	 * Set {@link org.jhove2.core.io.Input} buffer size.
	 * 
	 * @param size
	 *            Buffer size
	 */
	public void setBufferSize(int size) {
		this.bufferSize = size;
	}

	/**
	 * Set {@link org.jhove2.core.io.Input} buffer type.
	 * 
	 * @param type
	 *            Buffer type
	 */
	public void setBufferType(Type type) {
		this.bufferType = type;
	}

	/**
	 * Set framework message digests flag; if true, calculate message digests.
	 * 
	 * @param flag
	 *            Framework message digests flag
	 */
	public void setCalcDigests(boolean flag) {
		this.calcDigests = flag;
	}

	/**
	 * Set framework {@link org.jhove2.module.characterize.Characterizer}
	 * module.
	 * 
	 * @param characterizer
	 *            Framework characterizer module
	 */
	public void setCharacterizer(Characterizer characterizer) {
		this.characterizer = characterizer;
	}

	/**
	 * Set framework delete temporary files flag; if true, delete files.
	 * 
	 * @param flag
	 *            Framework delete temporary files flag
	 */
	public void setDeleteTempFiles(boolean flag) {
		this.deleteTempFiles = flag;
	}

	/**
	 * Set framework dispatcher module.
	 * 
	 * @param dispatcher
	 *            Framework dispatcher module
	 */
	public void setDispatcher(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	/**
	 * Set framework displayer module.
	 * 
	 * @param displayer
	 *            Framework displayer module
	 */
	public void setDisplayer(Displayer displayer) {
		this.displayer = displayer;
	}

	/**
	 * Set fail fast limit. Processing of a given source unit is terminated once
	 * the number of detected errors exceeds the limit. A limit of 0 indicates
	 * no fail fast, i.e., process and report all errors.
	 * 
	 * @param limit
	 *            Fail fast limit
	 */
	public void setFailFastLimit(int limit) {
		this.failFastLimit = limit;
	}

	/**
	 * Set framework show identifiers flag; if true, show identifiers in non-XML
	 * display modes.
	 * 
	 * @param flag
	 *            Framework show identifiers flag
	 */
	public void setShowIdentifiers(boolean flag) {
		this.showIdentifiers = flag;
	}

	/**
	 * Set temporary directory.
	 * 
	 * @param directory
	 *            Temporary directory
	 */
	public void setTempDirectory(String directory) {
		this.tempDirectory = directory;
	}

	/**
	 * Set temporary file prefix.
	 * 
	 * @param prefix
	 *            Temporary file prefix
	 */
	public void setTempPrefix(String prefix) {
		this.tempPrefix = prefix;
	}

	/**
	 * Set temporary file suffix.
	 * 
	 * @param suffix
	 *            Temporary file suffix
	 */
	public void setTempSuffix(String suffix) {
		this.tempSuffix = suffix;
	}
}
