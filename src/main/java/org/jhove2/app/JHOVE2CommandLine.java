/**
 * JHOVE2 - Next-generation architecture for format-aware characterization
 *
 * Copyright (c) 2010 by The Regents of the University of California,
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

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jhove2.config.spring.SpringConfigInfo;
import org.jhove2.core.Invocation;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.TimerInfo;
import org.jhove2.core.app.AbstractApplication;
import org.jhove2.core.io.Input;
import org.jhove2.core.io.Input.Type;
import org.jhove2.core.source.Source;
import org.jhove2.core.source.SourceFactory;
import org.jhove2.module.display.Displayer;

/**
 * JHOVE2 command line application.
 * 
 * @author mstrong, slabrams, smorrissey
 */
public class JHOVE2CommandLine
extends AbstractApplication
{
	/** JHOVE2 application version identifier. */
	public static final String VERSION = "1.9.5";

	/** JHOVE2 application release date. */
	public static final String RELEASE = "2010-03-05";

	/** JHOVE2 application rights statement. */
	public static final String RIGHTS = "Copyright 2010 by The Regents of the University of California, "
		+ "Ithaka Harbors, Inc., and The Board of Trustees of the Leland "
		+ "Stanford Junior University. "
		+ "Available under the terms of the BSD license.";

	/** Usage statement return code. */
	public static final int EUSAGE = 1;

	/** Caught exception return code. */
	public static final int EEXCEPTION = -1;

	/** indicates if source file list from command line is considered a URL */
	protected boolean isUrl = false;

	/**
	 * Instantiate a new <code>JHOVE2CommandLine</code>.
	 * @throws JHOVE2Exception 
	 */
	public JHOVE2CommandLine()
	throws JHOVE2Exception
	{
		/* Super constructor creates {@link org.jhove2.core.AppConfigInfo} with
		 * JHOVE2 default settings, and the JHOVE2 default displayer with
		 * default displayer settings.
		 */
		super(VERSION, RELEASE, RIGHTS, Scope.Generic);
	}

	/**
	 * Main entry point for the JHOVE2 command line application.
	 * 
	 * @param args Command line arguments
	 */
	public static void main(String[] args)
	{	
		try {
			SpringConfigInfo factory = new SpringConfigInfo();

			/* Create and initialize the JHOVE2 command-line application. */

			JHOVE2CommandLine app = new JHOVE2CommandLine();
			app.getDisplayer().setConfigInfo(factory);
			app.setDateTime(new Date());

			/* Parse the application command line and update the default (or
			 * Spring-wired) settings in {@link org.jhove2.core.AppConfigInfo}
			 * with any command line options.
			 */
			List<String> pathNames = app.parse(args); 

			/* Create and initialize the JHOVE2 framework and register it with
			 * the application.
			 */		
			JHOVE2 jhove2 = SpringConfigInfo.getReportable(JHOVE2.class,
			"JHOVE2");
			TimerInfo timer = jhove2.getTimerInfo();
			timer.setStartTime();

			jhove2.setInvocation(app.getInvocation());
			jhove2.setInstallation(app.getInstallation());
			jhove2.addModule(app.getDisplayer());
			app.setFramework(jhove2);

			/* Create a FileSet source unit out of files and directories
			 * specified on the command line.  Add the JHOVE2 application and
			 * framework as modules processing the source unit.
			 * If -u option is set, create (single) URL source object
			 */
			Source source = null;
			if (app.isUrl){
				if (pathNames.size()>1){
					throw new JHOVE2Exception("only one URL can be passed as an argument");
				}
				int bufferSize = app.getInvocation().getBufferSize();
				String pre = app.getInvocation().getTempPrefix();
				String post = app.getInvocation().getTempSuffix();
				String strUrl = pathNames.get(0);
				URL url = new URL(strUrl);
				source = SourceFactory.getSource(pre, post, bufferSize, url);
			}
			else {
				source = SourceFactory.getSource(pathNames);
			}
			app.setSource(source);

			/* Characterize the FileSet source unit (and all subsidiary
			 * source units that it encapsulates.
			 */
			TimerInfo time2 = jhove2.getTimerInfo();
			time2.setStartTime();

			jhove2.characterize(source);

			time2.setEndTime();
			timer.setEndTime();

			/* Display characterization information for the FileSet.
			 */
			app.getDisplayer().display(app);
		}
		catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace(System.err);
			System.exit(EEXCEPTION);
		}
	}

	/**
	 *
	 * Parse the JHOVE2 application command line and update the default values
	 * in the {@link org.jhove2.core.Invocation} object with any
	 * command-line settings; and construct the list of file system paths to be
	 * processed by the application.  Also save the commandLine string as an
	 * instance member.
	 * 
	 *  -i  Show the unique formal identifiers for all reportable properties in results.
	 *  -k  Calculate message digests.
	 *  -u  Source to be characterized is a (single) URL
	 *  -b size     I/O buffer size (default=131072)
	 *  -B scope     I/O buffer type (default=Direct)
	 *  -d format   Results format (default=Text)
	 *  -f limit    Fail fast limit (default=0; no limit on the number of reported errors.
	 *  -t temp     Temporary file directory (default=java.io.tmpdir)
	 *  -T  		   Save temporary files
	 *  -o file     Output file (default=standard output unit)
	 *  -h  Display a help message
	 *  file ...    One or more files or directories to be characterized.
	 *   
	 * @param args
	 *            Command line arguments
	 * @return File system path names
	 * @throws JHOVE2Exception 
	 */
	public List<String> parse(String[] args)
	throws JHOVE2Exception
	{
		Parser parser = new Parser(JHOVE2CommandLine.class.getName());

		ArrayList<String> pathNames = new ArrayList<String>();
		boolean showIdentifiers = this.getDisplayer().getShowIdentifiers();
		String filePathname   = null;
		Integer bufferSize    = null;
		String bufferType     = null;
		String displayerType  = null;
		Integer failFastLimit = null;
		String tempDirectory  = null;
		String[] otherArgs    = null;

		Invocation config = this.getInvocation();

		StringBuffer bufferTypeValues = new StringBuffer("");
		for (Input.Type i : Input.Type.values()) {
			if (i.ordinal() > 0)
				bufferTypeValues.append("|");
			bufferTypeValues.append(i.toString());
		}

		String[] displayers = SpringConfigInfo.getReportableNames(Displayer.class);
		StringBuffer displayerValues = new StringBuffer("");
		for (int i=0;i<displayers.length;i++){
			if (i>0)
				displayerValues.append("|");
			displayerValues.append(displayers[i]);
		}

		/* set the command line options
		 */
		Parser.Option urlSourceO =
			parser.addHelp((parser.addBooleanOption('u',"url-source")),
			"Source is URL");
		Parser.Option showIdentifiersO =
			parser.addHelp((parser.addBooleanOption('i',"show-identifiers")),
			"Show identifiers");
		Parser.Option setCalcDigestsO =
			parser.addHelp(parser.addBooleanOption('k', "calc-digests"),
			"Calculate message digests");
		Parser.Option bufferSizeO =
			parser.addHelp(parser.addIntegerOption('b', "buffer-size"), 
					"<buffersize>",
					"I/O buffer size (default=" +
					Invocation.DEFAULT_BUFFER_SIZE + ")");
		Parser.Option bufferTypeO =
			parser.addHelp(parser.addStringOption('B', "buffer-type"), 
					bufferTypeValues.toString(),
			"I/O buffer type (default=Direct)");
		Parser.Option displayerTypeO =
			parser.addHelp(parser.addStringOption('d', "display"), 
					displayerValues.toString(),
			"Display format");
		Parser.Option failFastLimitO =
			parser.addHelp(parser.addIntegerOption('f', "fail-fast"), 
					"<failFastLimit>",
					"Fail fast limit (default=0; " +
			"no limit on the number of reported errors)");
		Parser.Option tempDirectoryO =
			parser.addHelp(parser.addStringOption('t', "temp"), 
					"<tempDirectory>",
			"Temporary file directory (default=java.io.tmpdir)");
		Parser.Option deleteTempFilesO =
			parser.addHelp(parser.addBooleanOption('T', "save-temp-files"),
			"Save temporary files");
		Parser.Option filePathnameO =
			parser.addHelp(parser.addStringOption('o', "output"),
					"<outfile>",
			"Output file (default=standard output unit)");
		Parser.Option helpO =
			parser.addHelp(parser.addBooleanOption('h', "help"),
			"Show this help message");

		try {
			parser.parse(args);
			/* Test for valid option values
			 */
			if ((bufferType = (String)parser.getOptionValue(bufferTypeO)) != null) {
				if (bufferTypeValues.indexOf(bufferType) == -1) {
					throw new Parser.IllegalOptionValueException(bufferTypeO,
							bufferType);
				}
				else {
					config.setBufferType(Type.valueOf(bufferType));
				}
			}
			displayerType = (String)parser.getOptionValue(displayerTypeO);
			if (displayerType != null) {
				if (displayerValues.indexOf(displayerType) == -1) {
					throw new Parser.IllegalOptionValueException(displayerTypeO,
							displayerType);
				}
				else {
					setDisplayer((Displayer)SpringConfigInfo.getReportable(Displayer.class,
							displayerType));
				}
			}

			otherArgs = parser.getRemainingArgs();

			/* ensure there is at least one file in command line
			 */
			 if (otherArgs.length < 1) {
				 parser.getUsage();
				 System.exit(EUSAGE);
			 }
		}
		catch ( Parser.OptionException e ) {
			System.err.println(e.getMessage());
			parser.getUsage();
			System.exit(EUSAGE);
		}

		/* get the options and values
		 */     
		if ((Boolean)parser.getOptionValue(setCalcDigestsO) != null) {
			config.setCalcDigests(true);
		}
		if ((bufferSize = (Integer)parser.getOptionValue(bufferSizeO)) != null) {
			config.setBufferSize(bufferSize.intValue());
		}
		if ((bufferType = (String)parser.getOptionValue(bufferTypeO)) != null) {
			config.setBufferType(Type.valueOf(bufferType));
		}
		displayerType = (String)parser.getOptionValue(displayerTypeO);
		if (displayerType != null) {
			this.setDisplayer((Displayer)SpringConfigInfo.getReportable(Displayer.class,
					displayerType));
		}
		if ((failFastLimit = (Integer)parser.getOptionValue(failFastLimitO)) != null) {
			config.setFailFastLimit(failFastLimit.intValue());
		}
		if ((Boolean)parser.getOptionValue(urlSourceO) != null) {
			this.isUrl = true;
		}
		/* Make sure that the displayer has already been set. */
		if ((Boolean)parser.getOptionValue(showIdentifiersO) != null) {
			showIdentifiers = true;
			this.getDisplayer().setShowIdentifiers(showIdentifiers);
		}  
		if ((tempDirectory = (String)parser.getOptionValue(tempDirectoryO)) != null) {
			config.setTempDirectory(tempDirectory);
		}
		if ((filePathname = (String)parser.getOptionValue(filePathnameO)) != null) {
			this.getDisplayer().setFilePathname(filePathname);
		}
		if ((Boolean)parser.getOptionValue(deleteTempFilesO) != null) {
			config.setDeleteTempFiles(false);
		}
		if ( Boolean.TRUE.equals(parser.getOptionValue(helpO))) {
			parser.getUsage();
			System.exit(0);
		}

		/* process remaining arguments 	
		 */
		 for ( int i = 0; i < otherArgs.length; ++i ) {
			 pathNames.add(otherArgs[i]);
		 }

		 /* save the command line string
		  */
		 for (int i = 0; i < args.length; i++) {
			 if (i == 0) {
				 this.commandLine = args[i];
			 } else {
				 this.commandLine += " " + args[i];
			 }
		 }

		 return pathNames;
	}
}
