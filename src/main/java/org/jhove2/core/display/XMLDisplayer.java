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

import org.jhove2.core.AbstractModule;
import org.jhove2.core.Displayable;
import org.jhove2.core.I8R;

/** JHOVE2 XML displayer.
 * 
 * @author mstrong, slabrams
 */
public class XMLDisplayer
	extends AbstractModule
	implements Displayable
{
	/** XML displayer version identifier. */
	public static final String VERSION = "1.0.0";

	/** XML displayer release date. */
	public static final String DATE = "2009-06-11";
	
	/** XML displayer rights statement. */
	public static final String RIGHTS =
		"Copyright 2009 by The Regents of the University of California, " +
		"Ithaka Harbors, Inc., and The Board of Trustees of the Leland " +
		"Stanford Junior University. " +
		"Available under the terms of the BSD license.";
	
	/** Collection element. */
	public static final String COLLECTION = "collection";
	
	/** Identifier element. */
	public static final String IDENTIFIER = "identifier";
	
	/** Name element. */
	public static final String NAME = "name";
	
	/** Namespace attribute. */
	public static final String NAMESPACE = "namespace";
	
	/** Properties element. */
	public static final String PROPERTIES = "properties";
	
	/** Property element. */
	public static final String PROPERTY = "property";
	
	/** Reportable element. */
	public static final String REPORTABLE = "reportable";
	
	/** Root element. */
	public static final String ROOT = "jhove2";
	
	/** Schema location attribute. */
	public static final String SCHEMA_LOCATION = ":schemaLocation";
	
	/** Size attribute. */
	public static final String SIZE = "size";
	
	/** Value element. */
	public static final String VALUE = "value";
	
	/** xmlns attribute. */
	public static final String XMLNS = "xmlns:";
	
	/** XSI attribute. */
	public static final String XSI = "xsi";
	
	/** XSI URI. */
	public static final String XSI_URI = "http://www.w3.org/2001/XMLSchema-instance";
	
	/** JHOVE2 namespace prefix. */
	protected String prefix;
	
	/** JHOVE2 schema. */
	protected String schema;
	
	/** JHOVE2 namespace URI. */
	protected String uri;

	/** Instantiate a new <code>XMLDisplayer</code>.
	 */
	public XMLDisplayer() {
		super(VERSION, DATE, RIGHTS);
		
		this.prefix = "j2:";
		this.uri    = "http://jhove2.org/ns/display/1.0.0";
		this.schema = "http://jhove2.org/xsd/display/1.0.0/jhove2.xsd";
	}

	/** Start display.
	 * @param out   Print stream
	 * @param level Nesting level
	 * @see org.jhove2.core.Displayable#startDisplay(java.io.PrintStream, int)
	 */
	@Override
	public void startDisplay(PrintStream out, int level) {
		declaration(out);
		startTag(out, level, ROOT, XMLNS+this.prefix, this.uri,
				                   XMLNS+XSI, XSI_URI,
				                   XSI+SCHEMA_LOCATION, this.uri+" "+this.schema);
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
		startTag(out, level, REPORTABLE);
		tag     (out, level+1, NAME, name);
		tag     (out, level+1, IDENTIFIER, identifier.getValue(), NAMESPACE,
				                           identifier.getNamespace().toString());
		startTag(out, level+1, PROPERTIES);
	}
	
	/** Start display of a property collection.
	 * @param out        Print stream
	 * @param level      Nesting level
	 * @param name       Property collection name
	 * @param identifier Property collection identifier in the JHOVE2 namespace
	 * @param size       Property collection size
	 * @param order      Ordinal position of this reportable with respect to
	 *                   enclosing {@link org.jhove2.core.Reportable} or
	 *                   collection
	 * @see org.jhove2.core.Displayable#startCollection(java.io.PrintStream, int, java.lang.String, org.jhove2.core.I8R, int, boolean)
	 */
	@Override
	public void startCollection(PrintStream out, int level, String name,
			                    I8R identifier, int size, int order) {
		startTag(out, level, COLLECTION, SIZE, Integer.toString(size));
		tag     (out, level+1, NAME, name);
		tag     (out, level+1, IDENTIFIER, identifier.getValue(), NAMESPACE,
				                           identifier.getNamespace().toString());
		startTag(out, level+1, PROPERTIES);
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
		startTag(out, level,   PROPERTY);
		tag     (out, level+1, NAME, name);
		tag     (out, level+1, IDENTIFIER, identifier.getValue(), NAMESPACE,
				                           identifier.getNamespace().toString());
		tag     (out, level+1, VALUE, value.toString());
		endTag  (out, level,   PROPERTY);
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
		endTag(out, level, COLLECTION);
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
		endTag(out, level, REPORTABLE);
	}
	
	/** End display.
	 * @param out   Print stream
	 * @param level Nesting level
	 * @see org.jhove2.core.Displayable#endDisplay(java.io.PrintStream, int)
	 */
	@Override
	public void endDisplay(PrintStream out, int level) {
		endTag(out, level, ROOT);
	}
	
	/** Display XML declaration.
	 * @param out
	 */
	public void declaration(PrintStream out) {
		out.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
	}
	
	/** Display start tag.
	 * @param out   Print stream
	 * @param level Nesting level
	 * @param name  Tag name
	 */
	public void startTag(PrintStream out, int level, String name) {
		String indent = Displayer.getIndent(level);
		
		out.println(indent + "<" + this.prefix + name + ">");
	}
	
	/** Display start tag.
	 * @param out   Print stream
	 * @param level Nesting level
	 * @param name  Tag name
	 * @param attrs Tag attributes
	 */
	public void startTag(PrintStream out, int level, String name,
			             String... attrs) {
		String indent = Displayer.getIndent(level);
		
		out.print(indent + "<" + this.prefix + name);
		for (int i=0; i<attrs.length; i+=2) {
			out.print(" " + attrs[i] + "=\"" + attrs[i+1] + "\"");
		}
		out.println(">");
	}
	
	/** Display tag.
	 * @param out     Print stream
	 * @param level   Nesting level
	 * @param name    Tag name
	 * @param content Tag content
	 */
	public void tag(PrintStream out, int level, String name, String content) {
		String indent = Displayer.getIndent(level);
		
		out.println(indent + "<"  + this.prefix + name + ">" + content +
				             "</" + this.prefix + name + ">");
	}
	/** Display tag.
	 * @param out     Print stream
	 * @param level   Nesting level
	 * @param name    Tag name
	 * @param content Tag content
	 * @param attrs   Tag attributes
	 */
	public void tag(PrintStream out, int level, String name, String content,
			        String... attrs) {
		String indent = Displayer.getIndent(level);
		
		out.print(indent + "<"  + this.prefix + name);
		for (int i=0; i<attrs.length; i+=2) {
			out.print(" " + attrs[i] + "=\"" + attrs[i] + "\"");
		}
		out.println(">" + content + "</" + this.prefix + name + ">");
	}
	
	/** Display end tag.
	 * @param out   Print stream
	 * @param level Nesting level
	 * @param name  Tag name
	 */
	public void endTag(PrintStream out, int level, String name) {
		String indent = Displayer.getIndent(level);
		
		out.println(indent + "</" + this.prefix + name + ">");
	}
}
