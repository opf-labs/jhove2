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

package org.jhove2.module.display;

import java.io.PrintStream;

import org.jhove2.core.I8R;

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
	public static final String RELEASE = "2009-06-11";
	
	/** JSON displayer rights statement. */
	public static final String RIGHTS =
		"Copyright 2009 by The Regents of the University of California, " +
		"Ithaka Harbors, Inc., and The Board of Trustees of the Leland " +
		"Stanford Junior University. " +
		"Available under the terms of the BSD license.";
	

	/** Instantiate a new <code>JSON</code> displayer.
	 */
	public JSONDisplayer() {
		super(VERSION, RELEASE, RIGHTS);
	}


	/** Start display.
	 * @param out   Print stream
	 * @param level Nesting level
	 * @see org.jhove2.core.Displayable#startDisplay(java.io.PrintStream, int)
	 */
	@Override
	public void startDisplay(PrintStream out, int level) {
		String indent = getIndent(level);
		
		out.println(indent + "{");
	}
	
	/** Start display of a {@link org.jhove2.core.Reportable}.
	 * @param out        Print stream
	 * @param level      Nesting level
	 * @param name       Reportable name
	 * @param identifier Reportable identifier in the JHOVE2 namespace
	 * @param order      Ordinal position of this reportable with respect to
	 *                   enclosing {@link org.jhove2.core.Reportable} or
	 *                   collection
	 * @see org.jhove2.core.Displayable#startReportable(java.io.PrintStream, java.lang.String, org.jhove2.core.I8R, int, boolean)
	 */
	@Override
	public void startReportable(PrintStream out, int level, String name,
			                    I8R identifier, int order) {
		StringBuffer buffer = new StringBuffer(getIndent(level));
		
		if (order == 0) {
			buffer.append(" ");
		}
		else {
			buffer.append(",");
		}
		buffer.append("\"" + name + "\": {");
		out.println(buffer);
	}
	
	/** Start display of a property collection.
	 * @param out        Print stream
	 * @param level      Nesting level
	 * @param name       Property collection name
	 * @param identifier Property collection identifier in the JHOVE2 namespace
	 * @param size       Property collection size
	 * @param iorder      Ordinal position of this reportable with respect to
	 *                   enclosing {@link org.jhove2.core.Reportable} or
	 *                   collection
	 * @see org.jhove2.core.Displayable#startCollection(java.io.PrintStream, int, java.lang.String, org.jhove2.core.I8R, int, boolean)
	 */
	@Override
	public void startCollection(PrintStream out, int level, String name,
			                    I8R identifier, int size, int order) {
		StringBuffer buffer = new StringBuffer(getIndent(level));
		
		if (order == 0) {
			buffer.append(" ");
		}
		else {
			buffer.append(",");
		}
		buffer.append("\"" + name + "\": {");
		out.println(buffer);
	}
	
	/** Display property.
	 * @param out        Print stream
	 * @param level      Nesting level
	 * @param name       Property name
	 * @param identifier Property identifier in the JHOVE2 namespace
	 * @param value      Property value
	 * @param order      Ordinal position of this reportable with respect to
	 *                   enclosing {@link org.jhove2.core.Reportable} or
	 *                   collection
	 * @see org.jhove2.core.Displayable#displayProperty(java.io.PrintStream, int, java.lang.String, org.jhove2.core.I8R, java.lang.Object, boolean)
	 */
	@Override
	public void displayProperty(PrintStream out, int level, String name,
			                    I8R identifier, Object value, int order) {
		StringBuffer buffer = new StringBuffer(getIndent(level));
		
		if (order == 0) {
			buffer.append(" ");
		}
		else {
			buffer.append(",");
		}
		buffer.append("\"" + name + "\": " + value);
		out.println(buffer);
	}

	/** End display of a property collection.
	 * @param out        Print stream
	 * @param level      Nesting level
	 * @param name       Property collection name
	 * @param identifier Property identifier in the JHOVE2 namespace
	 * @param size       Property collection size
	 * @see org.jhove2.core.Displayable#endCollection(java.io.PrintStream, java.lang.String, org.jhove2.core.I8R, int)
	 */
	@Override
	public void endCollection(PrintStream out, int level, String name,
			                  I8R identifier, int size) {
		String indent = getIndent(level+1);
		
		out.println(indent + "}");
	}

	/** End display of a {@link org.jhove2.core.Reportable}.
	 * @param out        Print stream
	 * @param level      Nesting level
	 * @param name       Reportable name
	 * @param identifier Reportable in the JHOVE2 namespace
	 * @see org.jhove2.core.Displayable#endReportable(java.io.PrintStream, java.lang.String, org.jhove2.core.I8R)
	 */
	@Override
	public void endReportable(PrintStream out, int level, String name,
			                  I8R identifier) {
		String indent = getIndent(level+1);
		
		out.println(indent + "}");
	}
	
	/** End display.
	 * @param out   Print stream
	 * @param level Nesting level
	 * @see org.jhove2.core.Displayable#endDisplay(java.io.PrintStream, int)
	 */
	@Override
	public void endDisplay(PrintStream out, int level) {
		String indent = getIndent(level);
		
		out.println(indent + "}");
	}
}
