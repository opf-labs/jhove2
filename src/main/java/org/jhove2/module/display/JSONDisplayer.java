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

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.I8R;

/**
 * JSON displayer. The JSON format is defined by RFC 4627.
 * 
 * @author mstrong, slabrams, smorrissey
 */
public class JSONDisplayer extends AbstractDisplayer {
	/** JSON displayer version identifier. */
	public static final String VERSION = "1.0.0";

	/** JSON displayer release date. */
	public static final String RELEASE = "2009-07-30";

	/** JSON displayer rights statement. */
	public static final String RIGHTS = "Copyright 2009 by The Regents of the University of California, "
		+ "Ithaka Harbors, Inc., and The Board of Trustees of the Leland "
		+ "Stanford Junior University. "
		+ "Available under the terms of the BSD license.";

	/**
	 * Instantiate a new <code>JSON</code> displayer.
	 */
	public JSONDisplayer() {
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
		String indent = getIndent(level, 
				this.getShouldIndent());

		out.println(indent + "{");
	}

	/**
	 * Start display of a {@link org.jhove2.core.Reportable}.
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
	 *            {@link org.jhove2.core.Reportable} or collection
	 * @see org.jhove2.module.display.Displayer#startReportable(java.io.PrintStream,
	 *      int, java.lang.String, org.jhove2.core.I8R, int)
	 */
	@Override
	public void startReportable(PrintStream out, int level, String name,
			I8R identifier, int order) {
		this.startReportable(out, level, name, identifier, order, null);
	}

	/**
	 * Start display of a {@link org.jhove2.core.Reportable}.
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
	 *            {@link org.jhove2.core.Reportable} or collection
	 * @param typeIdentifier 
	 * 			  Reportable type identifier in the JHOVE2 namespace
	 * @see org.jhove2.module.display.Displayer#startReportable(java.io.PrintStream,
	 *      int, java.lang.String, org.jhove2.core.I8R, int, java.lang.String, org.jhove2.core.I8R)
	 */
	@Override
	public void startReportable(PrintStream out, int level, String name,
			I8R identifier, int order, I8R typeIdentifier) {
		String indent = getIndent(this.getShowIdentifiers() ? 2 * level : level, 
				this.getShouldIndent());
		StringBuffer buffer = new StringBuffer(indent);

		if (order == 0) {
			buffer.append(" ");
		} else {
			buffer.append(",");
		}
		buffer.append("\"" + name + "\": {");
		if (typeIdentifier != null){
			String typeName = typeIdentifier.getValue();
			int i = typeName.lastIndexOf("/");
			if (i > -1 ){
				typeName = typeName.substring(i+1);		
				buffer.append("\n" + indent + "  \"type\": \"" + typeName
						+ "\"" + "\n" + indent + ",");
				if (this.getShowIdentifiers()){
					buffer.append("\"identifier\": \"" + identifier
							+ "\"" + "\n" + indent + ",");
				}
				out.print(buffer);
			}
		}
		else if (this.getShowIdentifiers())  {			 
			buffer.append("\n" + indent + "  \"identifier\": \"" + identifier
					+ "\"" + "\n" + indent + ",");				out.println(buffer);

		}
		else{
			out.println(buffer);
		}

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
	 *            {@link org.jhove2.core.Reportable} or collection
	 * @see org.jhove2.module.display.Displayer#startCollection(java.io.PrintStream,
	 *      int, java.lang.String, org.jhove2.core.I8R, int, int)
	 */
	@Override
	public void startCollection(PrintStream out, int level, String name,
			I8R identifier, int size, int order) {
		String indent = getIndent(this.getShowIdentifiers() ? 2 * level : level, 
				this.getShouldIndent());
		StringBuffer buffer = new StringBuffer(indent);

		if (order == 0) {
			buffer.append(" ");
		} else {
			buffer.append(",");
		}
		buffer.append("\"" + name + "\": {");
		if (this.getShowIdentifiers()) {
			buffer.append("\n" + indent + "  \"identifier\": \"" + identifier
					+ "\"" + "\n" + indent + " ,\"value\": {");
		}
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
	 *            {@link org.jhove2.core.Reportable} or collection
	 * @see org.jhove2.module.display.Displayer#displayProperty(java.io.PrintStream,
	 *      int, java.lang.String, org.jhove2.core.I8R, java.lang.Object, int, java.lang.String)
	 */
	@Override
	public void displayProperty(PrintStream out, int level, String name,
			I8R identifier, Object value, int order, String unitOfMeasure) {
		String indent = getIndent((this.getShowIdentifiers() ? 2 * level : level), 
				this.getShouldIndent());
		StringBuffer buffer = new StringBuffer(indent);

		if (order == 0) {
			buffer.append(" ");
		} else {
			buffer.append(",");
		}
		buffer.append("\"" + name + "\": ");
		boolean mustShowUnits = (!unitOfMeasure.equals(ReportableProperty.NOT_APPLICABLE));
		boolean mustBracket = (this.getShowIdentifiers() || (mustShowUnits));
		if (mustBracket){
			if (this.getShowIdentifiers()) {
				buffer.append("{\n" + indent + "   \"identifier\": \"" + identifier);
//						+ "\"" + "\n" + indent + "  ,\"value\": ");
				if (mustShowUnits){
					// have to show units as well as value
					buffer.append("\"" + "\n" + indent + "   ,\"unit of measure\": \"" + unitOfMeasure);
				}	
				buffer.append("\"" + "\n" + indent);
			}
			else {
				// just show units and value, not the identifier
				buffer.append("{\n" + indent + "   \"unit of measure\": \"" + unitOfMeasure);
				buffer.append("\"" + "\n" + indent);
			}
			// now  show value
			buffer.append("  ,\"value\": ");
		}
		if (value instanceof Number) {
			buffer.append(value);
		} else {
			buffer.append("\"");
			buffer.append(escape(value.toString()));
			buffer.append("\"");
		}
//		if (this.getShowIdentifiers()) {
		if (mustBracket) {
			buffer.append("\n" + indent + " }");
		}
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
		String indent = getIndent(this.getShowIdentifiers() ? (2 * level) + 1
				: level + 1, 
				this.getShouldIndent());
		StringBuffer buffer = new StringBuffer(indent);

		if (this.getShowIdentifiers()) {
			buffer.append(" }\n" + indent);
		}
		buffer.append("}");
		out.println(buffer);
	}

	/**
	 * End display of a {@link org.jhove2.core.Reportable}.
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
		String indent = getIndent(this.getShowIdentifiers() ? (2 * level) + 1
				: level + 1, 
				this.getShouldIndent());
		StringBuffer buffer = new StringBuffer(indent);
		buffer.append("}");
		out.println(buffer);
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
		String indent = getIndent(level, 
				this.getShouldIndent());

		out.println(indent + "}");
	}

	/**
	 * Replace invalid characters with escaped values. The escape character is a
	 * backslash (\). All literal backslashes (\) are replaced with (\\). All
	 * quotes (") are replaced with (\").
	 * 
	 * @param value
	 *            String value
	 * @return Escaped version of the string
	 */
	protected String escape(String value) {
		value = value.replace("\\", "\\\\");
		return value.replace("\"", "\\\"");
	}

}
