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
import org.jhove2.core.Displayable;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.config.Configure;
import org.jhove2.core.io.Input.Type;

/** JHOVE2 command line application.
 * 
 * @author mstrong, slabrams
 */
public class JHOVE2CommandLine {
	/** Usage statement return code. */
	public static final int EUSAGE = 1;
	
	/** Caught exception return code. */
	public static final int EEXCEPTION = -1;
	
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
			JHOVE2CommandLineParser parser = new JHOVE2CommandLineParser();
			List<String> pathNames = parser.parse(args);

			/* Initialize the JHOVE2 framework. */
			JHOVE2 jhove2 = Configure.getReportable(JHOVE2.class, "JHOVE2");
			jhove2.setCommandLine(args);
			jhove2.setBufferSize(parser.getBufferSize());
			jhove2.setBufferType(parser.getBufferType());
			jhove2.setFailFastLimit(parser.getFailFastLimit());
			
			/* Characterize all file system path names. */
			jhove2.characterize(pathNames);

			/* Display the characterization results. */
			Displayable displayer =
				Configure.getReportable(Displayable.class,
						                parser.getDisplayer());
			displayer.setShowIdentifiers(parser.getShowIdentifiers());
			jhove2.setDisplayer(displayer);
			jhove2.display();
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace(System.err);
			System.exit(EEXCEPTION);
		}
	}
	
	/** Get application usage statement.
	 * @return Application usage statement
	 */
	public static String getUsage()
		throws JHOVE2Exception
	{
		Type   [] bufferTypes = Type.values();
		String [] displayers  = Configure.getReportableNames(Displayable.class);
		
		StringBuffer usage = new StringBuffer("usage: ");
		usage.append(JHOVE2CommandLine.class.getName());
		usage.append(" [-I]");
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
		usage.append(" <file> ...");
		
		return usage.toString();
	}
}
