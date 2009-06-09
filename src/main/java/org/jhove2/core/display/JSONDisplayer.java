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
import java.util.List;
import java.util.Set;

import org.jhove2.core.Identifier;

/** JSON displayer.  The JSON format is defined by RFC 4627.
 * 
 * @author mstrong, slabrams
 */
public class JSONDisplayer
	extends AbstractDisplayer
{
	/** JSON displayer version identifier. */
	public static final String VERSION = "1.0.0";

	/** JSON displayer release date. */
	public static final String DATE = "2009-06-05";
	
	/** JSON displayer development stage. */
	public static final Stage STAGE = Stage.Development;

	/** Instantiate a new <code>JSONDisplayer</code>.
	 */
	public JSONDisplayer() {
		super(VERSION, DATE, STAGE);
	}

	/** Print the start of the JHOVE2 output.
	 * @param out Output print stream
	 * @see org.jhove2.core.display.AbstractDisplayer#printStart(java.io.PrintStream)
	 */
	@Override
	public void printStart(PrintStream out) {
		out.println("{");
	}

	/** Print the start of a reportable.
	 * @param out        Output print stream
	 * @param level      Reporter nesting level
	 * @param name       Reporter name
	 * @param identifier Reporter identifier, in the JHOVE2 namespace
	 * @param first      True if the first child reportable of a Reporter or
	 *                   collection
	 * @see org.jhove2.core.display.AbstractDisplayer#printReporterStart(java.io.PrintStream, int, java.lang.String, org.jhove2.core.Identifier, boolean)
	 */
	@Override
	public void printReporterStart(PrintStream out, int level, String name,
			                       Identifier identifier, boolean first) {
		StringBuffer buffer = new StringBuffer(getIndent(level));
		
		if (first) {
			buffer.append(" ");
		}
		else {
			buffer.append(",");
		}
		buffer.append("\"" + name + "\": {");
		out.println(buffer);
	}

	/** Print the start of a collection of properties.
	 * @param out        Output print stream
	 * @param level      Property nesting level
	 * @param name       Property name
	 * @param identifier Property identifier, in the JHOVE2 namespace
	 * @param size       Number of properties in the collection
	 * @param first      True if the first collection property of a Reporter
	 *                   or collection
	 * @see org.jhove2.core.display.AbstractDisplayer#printCollectionStart(java.io.PrintStream, int, java.lang.String, org.jhove2.core.Identifier, int, boolean)
	 */
	@Override
	public void printCollectionStart(PrintStream out, int level, String name,
			Identifier identifier, int size, boolean first) {
		StringBuffer buffer = new StringBuffer(getIndent(level));
		
		if (first) {
			buffer.append(" ");
		}
		else {
			buffer.append(",");
		}
		buffer.append("\"" + name + "\": {");
		out.println(buffer);
	}

	/** Print a reportable property.
	 * @param out        Output print stream
	 * @param level      Property nesting level
	 * @param name       Property name
	 * @param identifier Property identifier, in the JHOVE2 namespace
	 * @param value      Property value
	 * @param first      True if the first property of a Reporter or collection
	 * @see org.jhove2.core.display.AbstractDisplayer#printProperty(java.io.PrintStream, int, java.lang.String, org.jhove2.core.Identifier, java.lang.Object, boolean)
	 */
	@Override
	public void printProperty(PrintStream out, int level, String name,
			                 Identifier identifier, Object value,
			                 boolean first) {
		StringBuffer buffer = new StringBuffer(getIndent(level));
		
		if (first) {
			buffer.append(" ");
		}
		else {
			buffer.append(",");
		}
		buffer.append("\"" + name + "\": ");
		if (value instanceof List ||
			value instanceof Set) {
			/* Print nothing. */
		}
		else if (value instanceof Boolean ||
				 value instanceof Number) {
			buffer.append(value);
		}
		else {
			buffer.append("\"" + value + "\"");
		}
		out.println(buffer);
	}

	/** Print the end of a collection of properties.
	 * @param out        Output print stream
	 * @param level      Property nesting level
	 * @param name       Property name
	 * @param identifier Property identifier, in the JHOVE2 namespace
	 * @see org.jhove2.core.display.AbstractDisplayer#printCollectionEnd(java.io.PrintStream, int, java.lang.String, org.jhove2.core.Identifier)
	 */
	@Override
	public void printCollectionEnd(PrintStream out, int level, String name,
			Identifier identifier) {
		String indent = getIndent(level);
		
		out.println(indent + " }");
	}

	/** Print the end of a reportable.
	 * @param out        Output print stream
	 * @param level      Reporter nesting level
	 * @param name       Reporter name
	 * @param identifier Reporter identifier, in the JHOVE2 namespace
	 * @see org.jhove2.core.display.AbstractDisplayer#printReporterEnd(java.io.PrintStream, int, java.lang.String, org.jhove2.core.Identifier)
	 */
	@Override
	public void printReporterEnd(PrintStream out, int level, String name,
			Identifier identifier) {
		String indent = getIndent(level);
		
		out.println(indent + " }");
	}

	/** Print the end of the JHOVE2 output.
	 * @param out Output print stream
	 * @see org.jhove2.core.display.AbstractDisplayer#printEnd(java.io.PrintStream)
	 */
	@Override
	public void printEnd(PrintStream out) {
		out.println("}");
	}
}
