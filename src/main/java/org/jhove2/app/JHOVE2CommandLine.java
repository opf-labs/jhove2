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

import java.util.List;

import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.config.Configure;
import org.jhove2.core.io.Input.Type;
import org.jhove2.module.display.Displayer;

/** JHOVE2 command line application.
 * 
 * @author mstrong, slabrams
 */
public class JHOVE2CommandLine
	extends AbstractApplication
{
	/** JHOVE2 application version identifier. */
	public static final String VERSION = "2.0.0";

	/** JHOVE2 application release date. */
	public static final String RELEASE = "2009-07-29";
	
	/** JHOVE2 application rights statement. */
	public static final String RIGHTS =
		"Copyright 2009 by The Regents of the University of California, " +
		"Ithaka Harbors, Inc., and The Board of Trustees of the Leland " +
		"Stanford Junior University. " +
		"Available under the terms of the BSD license.";

	/** Usage statement return code. */
	public static final int EUSAGE = 1;
	
	/** Caught exception return code. */
	public static final int EEXCEPTION = -1;

	/** Instantiate a new <code>JHOVE2CommandLine</code>.
	 */
	public JHOVE2CommandLine() {
		super(VERSION, RELEASE, RIGHTS);
	}
	
	/** Main entry point for the JHOVE2 command line application.
	 * @param args Command line arguments
	 */
	public static void main(String[] args) {

		try {
			if (args.length < 1) {
				System.out.println(getUsage());
				System.exit(EUSAGE);
			}

			/* Parse the application command line. */
			JHOVE2CommandLine app = Configure.getReportable(Application.class, "JHOVE2CommandLine");
			List<String> pathNames = app.parse(args);

			/* Initialize the JHOVE2 framework. */
			JHOVE2 jhove2 = Configure.getReportable(JHOVE2.class, "JHOVE2");
			jhove2.setApplication(app);
				
			/* Characterize and display all file system path names. */
			jhove2.characterize(pathNames);
			jhove2.display(app);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace(System.err);
			System.exit(EEXCEPTION);
		}
	}
	
	/** Parse the JHOVE2 application command line.
	 * @param args Command line arguments
	 * @return File system path names
	 */
	public List<String> parse(String [] args) {
		for (int i=0; i<args.length; i++) {
			if (i == 0) {
				this.commandLine = args[i];
			}
			else {
				this.commandLine += " " + args[i];
			}
			if (args[i].charAt(0) == '-') {
				if (args[i].length() > 1) {
					char opt = Character.toLowerCase(args[i].charAt(1));
					if      (opt == 'b' && i+1 < args.length) {
						this.bufferSize = Integer.valueOf(args[++i]);
						this.commandLine += " " + args[i];
					}
					else if (opt == 'B' && i+1 < args.length) {
						this.bufferType = Type.valueOf(args[++i]);
						this.commandLine += " " + args[i];
					}
					else if (opt == 'd' && i+1 < args.length) {
						this.displayer = args[++i] + "Displayer";
						this.commandLine += " " + args[i];
					}
					else if (opt == 'f' && i+1 < args.length) {
						this.failFastLimit = Integer.valueOf(args[++i]);
						this.commandLine += " " + args[i];
					}
					else if (opt == 'o' && i+1 < args.length) {
						this.outputFile = args[++i];
						this.commandLine += " " + args[i];
					}
					else if (opt == 't' && i+1 < args.length) {
						this.tempDirectory = args[++i];
						this.commandLine += " " + args[i];
					}
					else {
						if (args[i].indexOf('i') > -1) {
							this.showIdentifiers = true;
						}
						if (args[i].indexOf('k') > -1) {
							this.calcDigests = true;
						}
						if (args[i].indexOf('T') > -1) {
							this.deleteTempFiles = false;
						}
					}
				}
			}
			else {
				this.names.add(args[i]);
			}
		}
		
		return this.names;
	}

	/** Get application usage statement.
	 * @return Application usage statement
	 */
	public static String getUsage()
		throws JHOVE2Exception
	{
		Type   [] bufferTypes = Type.values();
		String [] displayers  = Configure.getReportableNames(Displayer.class);
		
		StringBuffer usage = new StringBuffer("usage: ");
		usage.append(JHOVE2CommandLine.class.getName());
		usage.append(" [-ik]");
		usage.append(" [-b <bufferSize>]");
		usage.append(" [-B ");
		for (int i=0; i<bufferTypes.length; i++) {
			if ( i > 0) {
				usage.append("|");
			}
			usage.append(bufferTypes[i]);
		}
		usage.append("]");
		usage.append(" [-d ");
		for (int i=0; i<displayers.length; i++) {
			if (i > 0) {
				usage.append("|");
			}
			int in = displayers[i].indexOf("Displayer");
			usage.append(displayers[i].substring(0, in));
		}
		usage.append("]");
		usage.append(" [-f <failFastLimit>]");
		usage.append(" [-t <tempDirectory]");
		usage.append(" [-o <file>]");
		usage.append(" <file> ...");
		
		return usage.toString();
	}
}
