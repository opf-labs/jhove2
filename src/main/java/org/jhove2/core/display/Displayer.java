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

package org.jhove2.core.display;

import java.io.PrintStream;
import java.text.SimpleDateFormat;

import org.jhove2.core.Component;
import org.jhove2.core.Reporter;

/** Interface for a JHOVE2 displayer.
 * 
 * @author mstrong, slabrams
 */
public interface Displayer
	extends Component
{
	/** ISO 8601 date format. */
	public static final SimpleDateFormat ISO8601 =
		            new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ");
	
	/** Display visibility. */
	public enum Visibility {
		Always,
		IfFalse,
		IfNegative,
		IfNonNegative,
		IfNonPositive,
		IfPositive,
		IfTrue,
		IfNonZero,
		IfZero,
		Never;
	}
	
	/** Display formats. */
	public enum Format {
		JSON,
		Text,
		XML
	}	

	/** Print the reportable properties and messages of a
	 * {@link org.jhove2.core.Reporter} to the standard output unit.  If
	 * the Reporter has children they are also printed recursively.
	 * @param reporter Reporter
	 * @see org.jhove2.core.display.Displayer#print(java.io.PrintStream, org.jhove2.core.Reporter)
	 */
	public void print(Reporter reporter);
	
	/** Print the reportable properties and messages of a
	 * {@link org.jhove2.core.Reporter}.  If the Reporter has children
	 * they are also printed recursively.
	 * @param out      Output print stream
	 * @param reporter Reporter
	 */
	public void print(PrintStream out, Reporter reporter);
}
