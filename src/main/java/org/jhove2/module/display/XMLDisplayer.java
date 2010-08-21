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
 * JHOVE2 XML displayer.
 * 
 * @author mstrong, slabrams, smorrissey
 */
public class XMLDisplayer
	extends AbstractDisplayer
{
	/** XML displayer version identifier. */
	public static final String VERSION = "2.0.0";

	/** XML displayer release date. */
	public static final String RELEASE = "2010-09-10";

	/** XML displayer rights statement. */
	public static final String RIGHTS = "Copyright 2010 by The Regents of the University of California, "
		+ "Ithaka Harbors, Inc., and The Board of Trustees of the Leland "
		+ "Stanford Junior University. "
		+ "Available under the terms of the BSD license.";


	/** Name attribute. */
	public static final String ATTNAME = "name";
	/** I8R value for feature in a Reportable) */
	public static final String ATTIDENTIFIER = "fid";  
	/** I8R namepsace for feature in a Reportable. */
	public static final String ATTIDNAMESPACE = "fidns";	
	/** I8R value for scope of a Reportable. */
	public static final String ATTTYPEID = "ftid";
	/** I8R namespace for scope of a Reportable. */
	public static final String ATTTYPEIDNAMESPACE = "ftidns";	
	/** unit of measure for value of element, where applicable */
	public static final String ATTUNITOFMEASURE = "funit";
	/** Root element. */
	public static final String ELEROOT = "jhove2";
	/** feature element. */
	public static final String ELEFEATURE = "feature";
	/** features element. */
	public static final String ELEFEATURES = "features";	
	/** Value element. */
	public static final String ELEVALUE = "value";
	/** Symbolic value element. */
	public static final String ELESYMBOLIC = "symbolicValue";

	/** Schema location attribute. */
	public static final String SCHEMA_LOCATION = ":schemaLocation";
	/** xmlns attribute. */
	public static final String XMLNS = "xmlns:";
	/** XSI attribute. */
	public static final String XSI = "xsi";
	/** XSI URI. */
	public static final String XSI_URI = "http://www.w3.org/2001/XMLSchema-instance";
	/** default namespace prefix */
	public static final String JHOVE2_PREFIX = "j2";
	/** default JHOVE2 Schema URI */
	public static final String JHOVE2_URI = "http://jhove2.org/xsd/1.0.0";
	/** default JHOVE2 Schema location */
	public static final String JHOVE2_SCHEMA_LOC = "http://jhove2.org/xsd/1.0.0/jhove2.xsd";

	/** JHOVE2 namespace prefix. */
	protected String prefix;

	/** JHOVE2 schema. */
	protected String schemaLoc;

	/** JHOVE2 namespace URI. */
	protected String uri;

	/**
	 * Instantiate a new <code>XMLDisplayer</code>.
	 */
	public XMLDisplayer() {
		super(VERSION, RELEASE, RIGHTS);
		this.setShouldIndent(false);
		this.prefix = JHOVE2_PREFIX;
		this.uri = JHOVE2_URI;
		this.schemaLoc = JHOVE2_SCHEMA_LOC;
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
		declaration(out);
		startTag(out, level, ELEROOT, XMLNS + this.prefix, this.uri, XMLNS + XSI,
				XSI_URI);
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
		if (typeIdentifier != null){
			startTag(out, level, ELEFEATURE,
					ATTNAME,            name,
					ATTIDENTIFIER,      identifier.getValue(),
					ATTIDNAMESPACE,     identifier.getNamespace().toString(),
					ATTTYPEID,          typeIdentifier.getValue(),
					ATTTYPEIDNAMESPACE, typeIdentifier.getNamespace().toString());
		}
		else {
			startTag(out, level, ELEFEATURE,
					ATTNAME,        name,
					ATTIDENTIFIER,  identifier.getValue(),
					ATTIDNAMESPACE, identifier.getNamespace().toString());
		}
		startTag(out, level + 1, ELEFEATURES);
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
		startTag(out, level, ELEFEATURE,
				ATTNAME,        name,
				ATTIDENTIFIER,  identifier.getValue(),
				ATTIDNAMESPACE, identifier.getNamespace().toString());
		startTag(out, level + 1, ELEFEATURES);
	}

	/**
	 * Display raw or coded property.
	 * 
	 * @param out
	 *            Print stream
	 * @param level
	 *            Nesting level
	 * @param name
	 *            Property name
	 * @param identifier
	 *            Property identifier in the JHOVE2 namespace
	 * @param coded
	 *            Property raw or coded value
	 * @param symbolic
	 *            Property symbolic value (optional, may be null)
	 * @param order
	 *            Ordinal position of this reportable with respect to enclosing
	 *            {@link org.jhove2.core.reportable.Reportable} or collection
	 * @param unit Unit of measure (optional, may be null)
	 * @see org.jhove2.module.display.Displayer#displayProperty(java.io.PrintStream,
	 *      int, java.lang.String, org.jhove2.core.I8R, java.lang.Object, java.lang.Object, int, java.lang.String)
	 */
	@Override
	public void displayProperty(PrintStream out, int level, String name,
			                    I8R identifier, Object coded, Object symbolic,
			                    int order, String unit) {
		if (unit == null) {
			startTag(out, level, ELEFEATURE,
				     ATTNAME,        name,
				     ATTIDENTIFIER,  identifier.getValue(), 
				     ATTIDNAMESPACE, identifier.getNamespace().toString());
		}
		else {
			startTag(out, level, ELEFEATURE,
					ATTNAME,          name,
					ATTIDENTIFIER,    identifier.getValue(), 
					ATTIDNAMESPACE,   identifier.getNamespace().toString(),
					ATTUNITOFMEASURE, unit);
		}
		tag(out, level + 1, ELEVALUE, coded.toString());
		if (symbolic != null) {
		    tag(out, level+1, ELESYMBOLIC, symbolic.toString());
		}
		endTag(out, level, ELEFEATURE);
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
		endTag(out, level + 1, ELEFEATURES);
		endTag(out, level, ELEFEATURE);
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
		endTag(out, level + 1, ELEFEATURES);
		endTag(out, level, ELEFEATURE);
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
		endTag(out, level, ELEROOT);
	}

	/**
	 * Display XML declaration.
	 * 
	 * @param out
	 */
	public void declaration(PrintStream out) {
		out.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
	}

	/**
	 * Display start tag.
	 * 
	 * @param out
	 *            Print stream
	 * @param level
	 *            Nesting level
	 * @param name
	 *            Tag name
	 */
	public void startTag(PrintStream out, int level, String name) {
		String indent = getIndent(level, 
				this.getShouldIndent());
		out.print(indent + "<" + this.prefix + ":" + name + ">" + this.getLineEnd());
	}

	/**
	 * Display start tag.
	 * 
	 * @param out
	 *            Print stream
	 * @param level
	 *            Nesting level
	 * @param name
	 *            Tag name
	 * @param attrs
	 *            Tag attributes
	 */
	public void startTag(PrintStream out, int level, String name,
			String... attrs) {
		String indent = AbstractDisplayer.getIndent(level, 
				this.getShouldIndent());
		out.print(indent + "<" + this.prefix + ":" + name);
		for (int i = 0; i < attrs.length; i += 2) {
			out.print(" " + attrs[i] + "=\"" + escapeAttr(attrs[i + 1]) + "\"");
		}
		out.print(">" + this.getLineEnd());
	}

	/**
	 * Display tag.
	 * 
	 * @param out
	 *            Print stream
	 * @param level
	 *            Nesting level
	 * @param name
	 *            Tag name
	 * @param content
	 *            Tag content
	 */
	public void tag(PrintStream out, int level, String name, String content) {
		String indent = AbstractDisplayer.getIndent(level, 
				this.getShouldIndent());
		out.print(indent + "<" + this.prefix + ":" + name + ">" + escape(content)
				+ "</" + this.prefix + ":" + name + ">" + this.getLineEnd());
	}

	/**
	 * Display end tag.
	 * 
	 * @param out
	 *            Print stream
	 * @param level
	 *            Nesting level
	 * @param name
	 *            Tag name
	 */
	public void endTag(PrintStream out, int level, String name) {
		String indent = AbstractDisplayer.getIndent(level, 
				this.getShouldIndent());
		out.print(indent + "</" + this.prefix + ":" + name + ">" + this.getLineEnd());
	}

	/**
	 * Replace invalid characters with escaped values.
	 * 
	 * @param value
	 *            String value
	 * @return Escaped version of the string
	 */
	protected String escape(String value) {
		return encodeContent(value);
	}

	/**
	 *   Encodes a content String in XML-clean form, converting characters
     *   to entities as necessary and removing control characters disallowed
     *   by XML.  The null string will be converted to an empty string.
	 *   Developed by:  Jhove - JSTOR/Harvard Object Validation Environment
     *   Copyright 2003 by JSTOR and the President and Fellows of Harvard College
     *   Please see JHOVE(1)-LICENSE.txt file for license information
     * 
	 * @param content String to be encoded
	 * @return
	 */
	protected static String encodeContent (String content)
	{
		if (content == null) {
			content = "";
		}
		StringBuffer buffer = new StringBuffer (content);
		/* Remove disallowed control characters from the content string. */
		int n = buffer.length ();
		for (int i=0; i<n; i++) {
			char ch = buffer.charAt (i);
			if ((0x00 <= ch && ch <= 0x08) || (0x0b <= ch && ch <= 0x0c) ||
					(0x0e <= ch && ch <= 0x1f) ||  0x7f == ch) {
				buffer.deleteCharAt (i--);
				n--;
			}
		}
		n = 0;
		while ((n = buffer.indexOf ("&", n)) > -1) {
			buffer.insert (n+1, "amp;");
			n +=5;
		}
		n = 0;
		while ((n = buffer.indexOf ("<", n)) > -1) {
			buffer.replace (n, n+1, "&lt;");
			n += 4;
		}
		n = 0;
		while ((n = buffer.indexOf (">", n)) > -1) {
			buffer.replace (n, n+1, "&gt;");
			n += 4;
		}
		return buffer.toString ();
	}

	/**
	 * Replace invalid attribute characters with escape values.
	 * 
	 * @param value
	 *            String value
	 * @return Escaped version of the string
	 */
	protected String escapeAttr(String value) {
		value = escape(value);
		return value.replace("\"", "&quot;");
	}
	/**
	 * Determines EOL character
	 * @return "\n" if shouldIndent; else ""
	 */
	protected String getLineEnd(){
		String lineEnd = this.getShouldIndent()? "\n" : "";
		return lineEnd;
	}
}
