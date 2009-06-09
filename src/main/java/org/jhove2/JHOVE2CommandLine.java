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

package org.jhove2;

import java.util.List;

import org.jhove2.core.JHOVE2;
import org.jhove2.core.display.Displayer;
import org.jhove2.core.display.DisplayerFactory;
/** The JHOVE2 command line application.
 * 
 * @author mstrong, slabrams
 */
public class JHOVE2CommandLine
{
	/** Usage syntax. */
	public static final String USAGE =
		"usage: java " + JHOVE2CommandLine.class.getName() +
		" [-b <bufferSize>] [-d JSON|Text|XML] [-f <failFastLimit>]" +
		" <file> ...";
	
	/** Return code for usage statement. */
	public static final int EUSAGE = 1;
	
	/** JHOVE2 command line application main entry.
	 * @param args Application command line arguments
	 */
	public static void main(String [] args) {
		/* If no command line arguments, print the application usage statement
		 * to standard output.
		 */
		if (args.length < 1) {
			System.out.println(USAGE);
			System.exit(EUSAGE);
		}
		
		/* Process command line arguments and retrieve list of file system
		 * object names (files, directories).
		 */
		JHOVE2CommandLineParser parser = new JHOVE2CommandLineParser();
		List<String> names = parser.parse(args);
		
		/* Initialize the JHOVE2 framework and process all file system
		 * names.
		 */
		JHOVE2 jhove2 = new JHOVE2();
		jhove2.setBufferSize(parser.getBufferSize());
		jhove2.setFailFastLimit(parser.getFailFastLimit());
		
		jhove2.characterize(names);
		
		/* Initialize a JHOVE2 displayer and display results. */
		Displayer display = DisplayerFactory.getDisplayer(parser.getDisplayFormat());
		display.print(jhove2);
	}
}
