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

/**
 * @author MStrong
 *
 * Adds the functionality of help to the CmdLineParser
 * dynamically create basic output for a --help option
 */

import jargs.gnu.CmdLineParser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Parser extends CmdLineParser {
	List<String> optionHelpStrings = new ArrayList<String>();
	String appName;
	
	public Parser(String appName) {
		super();
		this.appName = appName;
	}
	

	/**
	 * 
	 * Build option help string
	 * 
	 * @param option - command line option
	 * @param helpString - string that describes the option
	 * 
	 * @return command line option
	 */
	public Option addHelp(Option option, String helpString) {
		optionHelpStrings.add("[{-" + option.shortForm() + "/--" + option.longForm() + "}] \n\t" + helpString);
		return option;
	}
	
	/**
	 * Build option help string with option value help 
	 * 
	 * @param option - command line option
	 * @param valueHelp - string in angle brackets describing option value
	 * @param helpString - string that describes option
	 * 
	 * @return command line option
	 */
	public Option addHelp(Option option, String valueHelp, String helpString) {
		optionHelpStrings.add("[{-" + option.shortForm() + "/--" + option.longForm() + "} " 
				+ valueHelp + "] \n\t" + helpString);
		return option;
	}

	/**
	 * Get application usage statement.
	 * 
	 * @return Application usage statement
	 */
	public void getUsage() {
        System.err.println("usage: " + appName + " [options] <file> ...");
        for (Iterator<String> i = optionHelpStrings.iterator(); i.hasNext(); ) {
        	System.err.println(i.next());
        }
    }
}
