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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jhove2.config.spring.SpringConfigInfo;
import org.jhove2.core.Invocation;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.app.AbstractApplication;
import org.jhove2.core.io.Input;
import org.jhove2.core.io.Input.Type;
import org.jhove2.core.source.Source;
import org.jhove2.module.display.Displayer;
import org.jhove2.persist.ApplicationModuleAccessor;
import org.jhove2.persist.PersistenceManager;
import org.jhove2.persist.PersistenceManagerUtil;

import com.sleepycat.persist.model.Persistent;

/**
 * Official JHOVE2 command line application.
 * 
 * @author mstrong, slabrams, smorrissey
 */
@Persistent
public class JHOVE2CommandLine
extends AbstractApplication
{
	/** JHOVE2 application version identifier. */
	public static final String VERSION = "2.0.0";

	/** JHOVE2 application release date. */
	public static final String RELEASE = "2010-09-10";

	/** JHOVE2 application rights statement. */
	public static final String RIGHTS = "Copyright 2010 by The Regents of the University of California, "
		+ "Ithaka Harbors, Inc., and The Board of Trustees of the Leland "
		+ "Stanford Junior University. "
		+ "Available under the terms of the BSD license.";

	/** Usage statement return code. */
	public static final int EUSAGE = 1;

	/** Caught exception return code. */
	public static final int EEXCEPTION = -1;

	protected String persistenceMgrFactoryName;
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
		this(null);
	}
	
	/**
	 * Instantiate a new <code>JHOVE2CommandLine</code>.
	 * @param ApplicationModuleAccessor
	 * @throws JHOVE2Exception 
	 */
	public JHOVE2CommandLine(ApplicationModuleAccessor applicationModuleAccessor)
	throws JHOVE2Exception
	{
		/* Super constructor creates {@link org.jhove2.core.AppConfigInfo} with
		 * JHOVE2 default settings, and the JHOVE2 default displayer with
		 * default displayer settings.
		 */
		super(VERSION, RELEASE, RIGHTS, Scope.Generic, applicationModuleAccessor);
	}

	/**
	 * Main entry point for the JHOVE2 command line application. Creates a
     * {@linkplain org.jhove2.core.source.FileSetSource file set source unit}
     * from the files specified on the command line. This file set is passed to
     * the {@link JHOVE2} object for characterization; this initiates a search
     * for all unitary and aggregate source units within the file set; these
     * are characterized in turn.
	 * 
	 * @param args Command line arguments
	 */
	public static void main(String[] args)
	{	
		PersistenceManager persistenceManager = null;
		try {
			SpringConfigInfo factory = new SpringConfigInfo();
			/* Create PersistenceManagerFactory; will be used by ApplicationModuleAccessor to
			 * manage persistence.
			 */
			PersistenceManagerUtil.createPersistenceManagerFactory(factory);
			persistenceManager = PersistenceManagerUtil.getPersistenceManagerFactory().getInstance();
			persistenceManager.initialize();

			/* Create and initialize the JHOVE2 command-line application. */
			JHOVE2CommandLine app =
			    SpringConfigInfo.getReportable(JHOVE2CommandLine.class, 
					                          "JHOVE2CommandLine");
			/* Make sure there is a default displayer. */
			Displayer displayer = app.getDisplayer();
			if (displayer == null) {
				Class defaultDisplayerClass =
				    (Class) Class.forName(Displayer.DEFAULT_DISPLAYER_CLASS);
				displayer =
				    (Displayer)SpringConfigInfo.getReportable(defaultDisplayerClass,
				                                             "Text");
				displayer = app.setDisplayer(displayer);
			}
			app.getDisplayer().setConfigInfo(factory);
			app.setDateTime(new Date());

			/* Parse the application command line and update the default (or
			 * Spring-wired) settings in {@link org.jhove2.core.AppConfigInfo}
			 * with any command line options.
			 */
			List<String> names = app.parse(args); 	
			app = (JHOVE2CommandLine) app.getModuleAccessor().persistModule(app);
			
			/* Create and initialize the JHOVE2 framework and register it with
			 * the application.
			 */		
			JHOVE2 jhove2 = SpringConfigInfo.getReportable(JHOVE2.class,
							                              "JHOVE2");
			Invocation inv = app.getInvocation();
			jhove2.setInvocation(inv);
			jhove2.setInstallation(app.getInstallation());

			/* Create a FileSet source unit out of all files, directories, and
			 * URLS specified on the command line, or a single File, Directory, or
			 * URL if only one is specified.
			 */
			Source source = jhove2.getSourceFactory().getSource(jhove2, names);
			app = (JHOVE2CommandLine) source.addModule(app);
			jhove2 = (JHOVE2) source.addModule(jhove2);
			displayer = app.getDisplayer(); /* displayer might have been updated by parse method; */
			displayer.setConfigInfo(factory);
			displayer = (Displayer) source.addModule(displayer);
			displayer = app.setDisplayer(displayer);	/* this will persist the updated Displayer linked to app */				 				
			  
			/* Characterize the FileSet source unit (and all subsidiary
			 * source units that it encapsulates.
			 */			
			jhove2 = (JHOVE2) jhove2.getModuleAccessor().startTimerInfo(jhove2);
			Input input = source.getInput(jhove2);
	        try {
	            source = jhove2.characterize(source, input);
	        }
	        finally {
	            if (input != null) {
	                input.close();
	            }
	        }			
			jhove2 = (JHOVE2) jhove2.getModuleAccessor().endTimerInfo(jhove2);
			
			/* Display characterization information for the FileSet. */
			app.getDisplayer().display(source);
		}
		catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace(System.err);
			System.exit(EEXCEPTION);
		}
		finally{
			if (persistenceManager != null){
				try{
					persistenceManager.close();
				}
				catch (JHOVE2Exception je){
					System.err.println(je.getMessage());
					je.printStackTrace(System.err);
					System.exit(EEXCEPTION);
				}
			}
		}
	}

	/**
	 *
	 * Parse the JHOVE2 application command line and update the default values
	 * in the {@link org.jhove2.core.Invocation} object with any
	 * command-line settings; and construct the list of formatted object names,
	 * which may be files, directories, or URLs, to be processed by the
	 * application.  Also save the commandLine string as an instance member.
	 * 
	 * For command line options and usage, refer to the <i>JHOVE2 User's Guide</i>.
	 *   
	 * @param args
	 *            Command line arguments
	 * @return Formatted object names
	 * @throws JHOVE2Exception 
	 */
	public List<String> parse(String[] args)
	throws JHOVE2Exception
	{
		Parser parser = new Parser(JHOVE2CommandLine.class.getName());

		ArrayList<String> names = new ArrayList<String>();
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
/************** TODO: Fail fast processing is not yet enable. ****************/
/*****************************************************************************
		Parser.Option failFastLimitO =
			parser.addHelp(parser.addIntegerOption('f', "fail-fast"), 
					"<failFastLimit>",
					"Fail fast limit (default=0; " +
			"no limit on the number of reported errors)");
 *****************************************************************************/
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
					Displayer displayer = (Displayer)SpringConfigInfo.getReportable(Displayer.class,
							displayerType);
					displayer = this.setDisplayer(displayer);
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
		// bufferTypeO and displayerTypeO already used above to set config.BufferType and this.Displayer
/************* TODO: Fail fast processing is not yet enabled. ****************/
/*****************************************************************************
		if ((failFastLimit = (Integer)parser.getOptionValue(failFastLimitO)) != null) {
			config.setFailFastLimit(failFastLimit.intValue());
		}
 *****************************************************************************/
		/* Displayer will have been set either to Default displayer by constructor,
		 * or to some other displayer by the handling of the displayerType0 option above */
		Displayer displayer = this.getDisplayer();
		if ((Boolean)parser.getOptionValue(showIdentifiersO) != null) {
			showIdentifiers = true;
			displayer.setShowIdentifiers(showIdentifiers);
		}  
		if ((tempDirectory = (String)parser.getOptionValue(tempDirectoryO)) != null) {
			config.setTempDirectory(tempDirectory);
		}
		if ((filePathname = (String)parser.getOptionValue(filePathnameO)) != null) {
			displayer.setFilePathname(filePathname);
		}
		if ((Boolean)parser.getOptionValue(deleteTempFilesO) != null) {
			config.setDeleteTempFiles(false);
		}
		if ( Boolean.TRUE.equals(parser.getOptionValue(helpO))) {
			parser.getUsage();
			System.exit(0);
		}
		displayer = this.setDisplayer(displayer);
		/* process remaining arguments 	
		 */
		 for (int i = 0; i < otherArgs.length; ++i ) {
			 names.add(otherArgs[i]);
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

		 return names;
	}

	/**
	 * @return the persistenceMgrFactoryName
	 */
	public String getPersistenceMgrFactoryName() {
		return persistenceMgrFactoryName;
	}

	/**
	 * @param persistenceMgrFactoryName the persistenceMgrFactoryName to set
	 */
	public void setPersistenceMgrFactoryName(String persistenceMgrFactoryName) {
		this.persistenceMgrFactoryName = persistenceMgrFactoryName;
	}
}
