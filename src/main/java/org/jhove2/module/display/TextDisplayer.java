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

/**
 * JHOVE2 text displayer.
 * 
 * @author mstrong, slabrams
 */
public class TextDisplayer extends AbstractDisplayer {
	/** Text displayer version identifier. */
	public static final String VERSION = "1.9.5";

	/** Text displayer release date. */
	public static final String RELEASE = "2010-02-16";

	/** Text displayer rights statement. */
	public static final String RIGHTS = "Copyright 2010 by The Regents of the University of California, "
			+ "Ithaka Harbors, Inc., and The Board of Trustees of the Leland "
			+ "Stanford Junior University. "
			+ "Available under the terms of the BSD license.";

	/**
	 * Instantiate a new <code>TextDisplayer</code>.
	 */
	public TextDisplayer() {
		super(VERSION, RELEASE, RIGHTS);
		this.setShouldIndent(true);
	}

	/**
	 * Start display.
	 * 
	 * @param out
	 *            Print stream
	 * @param level
	 *            Nesting level
	 * @see org.jhove2.module.display.Displayer#startDisplay(java.io.PrintStream,
	 *      int)
	 */
	@Override
	public void startDisplay(PrintStream out, int level) {
	}

	/**
	 * Start display of a {@link org.jhove2.core.reportable.Reportable}.
	 * 
	 * @param out
	 *            Print stream
	 * @param level
	 *            Nesting level
	 * @param name
	 *            Reportable name
	 * @param identifier
	 *            Reportable identifier in the JHOVE2 namespace
	 * @param order
	 *            Ordinal position of this reportable with respect to enclosing
	 *            {@link org.jhove2.core.reportable.Reportable} or collection
	 * @see org.jhove2.module.display.Displayer#startReportable(java.io.PrintStream,
	 *      int, java.lang.String, org.jhove2.core.I8R, int)
	 */
	@Override
	public void startReportable(PrintStream out, int level, String name,
			I8R identifier, int order) {
		this.startReportable(out, level, name, identifier, order, null);
	}
	/**
	 * Start display of a {@link org.jhove2.core.reportable.Reportable}.
	 * 
	 * @param out
	 *            Print stream
	 * @param level
	 *            Nesting level
	 * @param name
	 *            Reportable name
	 * @param identifier
	 *            Reportable identifier in the JHOVE2 namespace
	 * @param order
	 *            Ordinal position of this reportable with respect to enclosing
	 *            {@link org.jhove2.core.reportable.Reportable} or collection
	 * @param typeIdentifier 
	 * 			  Reportable scope identifier in the JHOVE2 namespace
	 * @see org.jhove2.module.display.Displayer#startReportable(java.io.PrintStream,
	 *      int, java.lang.String, org.jhove2.core.I8R, int, org.jhove2.core.I8R)
	 */
	@Override
	public void startReportable(PrintStream out, int level, String name,
			I8R identifier, int order, I8R typeIdentifier) {
		StringBuffer buffer = new StringBuffer(getIndent(level, 
				this.getShouldIndent()));

		buffer.append(name);
		if (typeIdentifier != null){
			String typeName = typeIdentifier.getValue();
			int i = typeName.lastIndexOf("/");
			if (i > -1 ){
				typeName = typeName.substring(i+1);
				if (!typeName.equals(name)) {
					buffer.append(" {");
					buffer.append(typeName);
					buffer.append("}");
				}
			}
		}
		if (this.getShowIdentifiers()) {
			buffer.append(" <" + identifier + ">");
		}
		buffer.append(":");
		out.println(buffer);
	}
	/**
	 * Start display of a property collection.
	 * 
	 * @param out
	 *            Print stream
	 * @param level
	 *            Nesting level
	 * @param name
	 *            Property collection name
	 * @param identifier
	 *            Property collection identifier in the JHOVE2 namespace
	 * @param size
	 *            Property collection size
	 * @param order
	 *            Ordinal position of this reportable with respect to enclosing
	 *            {@link org.jhove2.core.reportable.Reportable} or collection
	 * @see org.jhove2.module.display.Displayer#startCollection(java.io.PrintStream,
	 *      int, java.lang.String, org.jhove2.core.I8R, int, int)
	 */
	@Override
	public void startCollection(PrintStream out, int level, String name,
			I8R identifier, int size, int order) {
		StringBuffer buffer = new StringBuffer(getIndent(level,
				this.getShouldIndent()));

		buffer.append(name);
		if (this.getShowIdentifiers()) {
			buffer.append(" <" + identifier + ">");
		}
		buffer.append(":");
		out.println(buffer);
	}

	/**
	 * Display property.
	 * 
	 * @param out
	 *            Print stream
	 * @param level
	 *            Nesting level
	 * @param name
	 *            Property name
	 * @param identifier
	 *            Property identifier in the JHOVE2 namespace
	 * @param value
	 *            Property value
	 * @param order
	 *            Ordinal position of this reportable with respect to enclosing
	 *            {@link org.jhove2.core.reportable.Reportable} or collection
	 * @param unit Unit of measure (may be null)
	 * @see org.jhove2.module.display.Displayer#displayProperty(java.io.PrintStream,
	 *      int, java.lang.String, org.jhove2.core.I8R, java.lang.Object, int, java.lang.String)
	 */
	@Override
	public void displayProperty(PrintStream out, int level, String name,
			I8R identifier, Object value, int order, String unit) {
		StringBuffer buffer = new StringBuffer(getIndent(level,this.getShouldIndent()));
		buffer.append(name);
		if (this.getShowIdentifiers()) {
			buffer.append(" <" + identifier + ">");
		}
		if (unit != null){
			buffer.append(" (" + unit + ")");
		}
		buffer.append(": " + value);
		out.println(buffer);
	}

	/**
	 * End display of a property collection.
	 * 
	 * @param out
	 *            Print stream
	 * @param level
	 *            Nesting level
	 * @param name
	 *            Property collection name
	 * @param identifier
	 *            Property identifier in the JHOVE2 namespace
	 * @param size
	 *            Property collection size
	 * @see org.jhove2.module.display.Displayer#endCollection(java.io.PrintStream,
	 *      int, java.lang.String, org.jhove2.core.I8R, int)
	 */
	@Override
	public void endCollection(PrintStream out, int level, String name,
			I8R identifier, int size) {
	}

	/**
	 * End display of a {@link org.jhove2.core.reportable.Reportable}.
	 * 
	 * @param out
	 *            Print stream
	 * @param level
	 *            Nesting level
	 * @param name
	 *            Reportable name
	 * @param identifier
	 *            Reportable in the JHOVE2 namespace
	 * @see org.jhove2.module.display.Displayer#endReportable(java.io.PrintStream,
	 *      int, java.lang.String, org.jhove2.core.I8R)
	 */
	@Override
	public void endReportable(PrintStream out, int level, String name,
			I8R identifier) {
	}

	/**
	 * End display.
	 * 
	 * @param out
	 *            Print stream
	 * @param level
	 *            Nesting level
	 * @see org.jhove2.module.display.Displayer#endDisplay(java.io.PrintStream,
	 *      int)
	 */
	@Override
	public void endDisplay(PrintStream out, int level) {
	}

}
