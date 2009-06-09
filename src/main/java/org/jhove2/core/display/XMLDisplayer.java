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

import org.jhove2.core.Identifier;

/** JHOVE2 XML displayer.
 * 
 * @author mstrong, slabrams
 */
public class XMLDisplayer
	extends AbstractDisplayer
{
	/** XML displayer version identifier. */
	public static final String VERSION = "1.0.0";

	/** XML displayer release date. */
	public static final String DATE = "2009-05-28";
	
	/** XML displayer development stage. */
	public static final Stage STAGE = Stage.Development;
	
	/** JHOVE2 output namespace prefix. */
	protected String namespacePrefix;
	
	/** JHOVE2 output namespace URI. */
	protected String namespaceURI;
	
	/** JHOVE2 output schema location. */
	protected String schemaLocation;
	
	/** XML encoding. */
	protected String xmlEncoding;
	
	/** XML stand-alone status. */
	protected boolean xmlStandalone;
	
	/** XML version. */
	protected String xmlVersion;
	
	/** XML Schema instance namespace URI. */
	protected String xsiNamespaceURI;
	
	/** Instantiate a new <code>XMLDisplayer</code>.
	 */
	public XMLDisplayer() {
		super(VERSION, DATE, STAGE);
		
		/* TODO: make these values configurable from Spring! */
		this.namespacePrefix = "j2";
		this.namespaceURI    = "http://jhove2.org/namespace/output/2.0";
		this.schemaLocation  = "http://jhove2.org/schema/output/2.0";
		this.xmlVersion      = "1.0";
		this.xmlEncoding     = "UTF-8";
		this.xmlStandalone   = true;
		this.xsiNamespaceURI = "http://www.w3.org/2001/XMLSchema-instance";
	}
	
	/** Print the start of the JHOVE2 output.
	 * @param out Output print stream
	 * @see org.jhove2.core.display.AbstractDisplayer#printStart(java.io.PrintStream)
	 */
	public void printStart(PrintStream out) {
		printDeclaration(out, this.xmlVersion, this.xmlEncoding, this.xmlStandalone);
		printStartTag   (out, "", this.namespacePrefix, "jhove2",
				         "xmlns:j2", this.namespaceURI,
				         "xmlns:xsi", this.xsiNamespaceURI,
				         "xsi:schemaLocation", this.namespaceURI + " " +
				                               this.schemaLocation);
	}

	/** Print the start of a reportable.
	 * @param out        Output print stream
	 * @param level      Reporter nesting level
	 * @param name       Reporter name
	 * @param identifier Reporter identifier, in the JHOVE2 namespace
	 * @param first      True if the first child reportable of a Reporter or
	 *                   collection
	 * @see org.jhove2.core.display.Displayer#printReportableStart(java.io.PrintStream, int, java.lang.String, org.jhove2.core.Identifier, boolean)
	 */
	@Override
	public void printReporterStart(PrintStream out, int level, String name,
			                      Identifier identifier, boolean first) {
		String indent = getIndent(level+1);
		printStartTag(out, indent    , this.namespacePrefix, "reportable");
		printTag     (out, indent+" ", this.namespacePrefix, "name", name);
		printTag     (out, indent+" ", this.namespacePrefix, "identifier",
				      identifier.getValue(), "namespace",
				      identifier.getNamespace().toString());
		printStartTag(out, indent+" ", this.namespacePrefix, "properties");
	}
	
	/** Print the start of a property collection.
	 * @param out        Output print stream
	 * @param level      Property nesting level
	 * @param name       Property name
	 * @param identifier Property identifier, in the JHOVE2 namespace
	 * @param size       Number of properties in the collection
	 * @param first      True if the first collection property of a Reporter
	 *                   or collection
	 * @see org.jhove2.core.display.Displayer#printPropertyStart(java.io.PrintStream, int, java.lang.String, org.jhove2.core.Identifier, int, boolean)
	 */
	@Override
	public void printCollectionStart(PrintStream out, int level, String name,
			                         Identifier identifier, int size,
			                         boolean first) {
		String indent = getIndent(level+1);
		printStartTag(out, indent    , this.namespacePrefix, "collection", "size", Integer.toString(size));
		printTag     (out, indent+" ", this.namespacePrefix, "name", name);
		printTag     (out, indent+" ", this.namespacePrefix, "identifier",
				     identifier.getValue(), "namespace",
				     identifier.getNamespace().toString());
		printStartTag(out, indent+" ", this.namespacePrefix, "properties");
	}	

	/** Print a  reportable property.
	 * @param out        Output print stream
	 * @param level      Property nesting level
	 * @param name       Property name
	 * @param identifier Property identifier, in the JHOVE2 namespace
	 * @param value      Property value
	 * @param first      True if the first property of a Reporter or collection
	 * @see org.jhove2.core.display.Displayer#printProperty(java.io.PrintStream, int, java.lang.String, org.jhove2.core.Identifier, java.lang.Object, boolean)
	 */
	@Override
	public void printProperty(PrintStream out, int level, String name,
			                  Identifier identifier, Object value,
			                  boolean first) {
		String indent = getIndent(level+2);
		printStartTag(out, indent    , this.namespacePrefix, "property");
		printTag     (out, indent+" ", this.namespacePrefix, "name", name);
		printTag     (out, indent+" ", this.namespacePrefix, "identifier",
				      identifier.getValue(), "namespace",
				      identifier.getNamespace().toString());
		printTag     (out, indent+" ", this.namespacePrefix, "value", value.toString());
		printEndTag  (out, indent    , this.namespacePrefix, "property");
	}
	
	/** Print the end of a property collection.
	 * @param out        Output print stream
	 * @param level      Property nesting level
	 * @param name       Property name
	 * @param identifier Property identifier, in the JHOVE2 namespace
	 * @see org.jhove2.core.display.Displayer#printPropertyEnd(java.io.PrintStream, int, java.lang.String, org.jhove2.core.Identifier)
	 */
	@Override
	public void printCollectionEnd(PrintStream out, int level, String name,
			                       Identifier identifier) {
		String indent = getIndent(level+1);
		printEndTag(out, indent+" ", this.namespacePrefix, "properties");
		printEndTag(out, indent    , this.namespacePrefix, "collection");
	}

	/** Print the end of a reportable.
	 * @param out        Output print stream
	 * @param level      Reporter nesting level
	 * @param name       Reporter name
	 * @param identifier Reporter identifier, in the JHOVE2 namespace
	 * @see org.jhove2.core.display.Displayer#printReporterEnd(java.io.PrintStream, int, java.lang.String, org.jhove2.core.Identifier)
	 */
	@Override
	public void printReporterEnd(PrintStream out, int level, String name,
			                     Identifier identifier) {
		String indent = getIndent(level+1);
		printEndTag(out, indent+" ", this.namespacePrefix, "properties");
		printEndTag(out, indent    , this.namespacePrefix, "reportable");
	}
	
	/** Print the end of the JHOVE2 output.
	 * @param out Output print stream
	 * @see org.jhove2.core.display.AbstractDisplayer#printEnd(java.io.PrintStream)
	 */
	@Override
	public void printEnd(PrintStream out) {
		printEndTag(out, "", this.namespacePrefix, "jhove2");
	}
	
	/** Get JHOVE2 output namespace URI.
	 * @return JHOVE2 output namespace URI
	 */
	public String getNamespaceURI() {
		return this.namespaceURI;
	}
	
	/** Get JHOVE2 output schema location.
	 * @return JHOVE2 output schema location
	 */
	public String getSchemaLocation() {
		return this.schemaLocation;
	}
	
	/** Get XML version.
	 * @return XML version
	 */
	public String getXMLVersion() {
		return this.xmlVersion;
	}
	
	/** Get XML schema instance namespace URI.
	 * @return XML schema instance namespace URI
	 */
	public String getXMLSchemaNamespaceURI() {
		return this.xsiNamespaceURI;
	}
	
	/** Set JHOVE2 output namespace prefix.
	 * @param prefix JHOVE2 output namespace prefix
	 */
	public void setNamespacePrefix(String prefix) {
		this.namespacePrefix = prefix;
	}

	/** Set JHOVE2 output namespace URI.
	 * @param uri JHOVE2 output namespace URI
	 */
	public void setNamespaceURI(String uri) {
		this.namespaceURI = uri;
	}
	
	/** Set JHOVE2 output schema location.
	 * @param location JHOVE2 output schema location
	 */
	public void setSchemaLocation(String location) {
		this.schemaLocation = location;
	}
	
	/** Set XML encoding.
	 * @param encoding XML encoding
	 */
	public void setXMLEncoding(String encoding) {
		this.xmlEncoding = encoding;
	}
	
	/** Set XML stand-alone status.
	 * @param standalone XML stand-alone status
	 */
	public void setXMLStandalone(boolean standalone) {
		this.xmlStandalone = standalone;
	}
	
	/** Set XML version.
	 * @param version XML version
	 */
	public void setXMLVersion(String version) {
		this.xmlVersion = version;
	}
	
	/** Set XML Schema instance namespace URI.
	 * @param uri XML Schema instance namespace URI
	 */
	public void setXMLSchemaNamespaceURI(String uri) {
		this.xsiNamespaceURI = uri;
	}
	
	/** Print XML declaration.
	 * @param out        Output print stream
	 * @param version    XML version
	 * @param encoding   Encoding
	 * @param standalone Stand-alone status
	 */
	protected static synchronized void printDeclaration(PrintStream out,
			                                            String version,
			                                            String encoding,
			                                            boolean standalone) {
		out.println("<?xml version=\"" + version + "\" encoding=\"" +
				    encoding + "\" standalone=\"" + (standalone ? "yes" : "no") + "\"?>");
	}
	
	/** Print start tag.
	 * @paran out    Output print stream
	 * @param indent Indentation
	 * @param prefix Namespace prefix
	 * @param tag    Tag name
	 */
	protected static synchronized void printStartTag(PrintStream out,
			                                         String indent,
			                                         String prefix,
			                                         String tag) {
		out.println(indent + "<" + prefix + ":" + tag + ">");
	}
	
	/** Print start tag with attributes.
	 * @paran out     Output print stream
	 * @param indent  Indentation
	 * @param prefix  Namespace prefix
	 * @param tag     Tag name
	 * @param attr    Tag attributes
	 */
	protected static synchronized void printStartTag(PrintStream out,
			                                         String indent,
			                                         String prefix,
			                                         String tag,
			                                         String...attr) {
		out.print(indent + "<" + prefix + ":" + tag);
		for (int i=0; i<attr.length; i+=2) {
			out.print(" " + attr[i] + "=\"" + attr[i+1] + "\"");
		}
		out.println(">");
	}
	
	/** Print tag with attributes.
	 * @paran out     Output print stream
	 * @param indent  Indentation
	 * @param prefix  Namespace prefix
	 * @param tag     Tag name
	 * @param content Tag content
	 * @param attr    Tag attributes
	 */
	protected static synchronized void printTag(PrintStream out,
			                                    String indent,
			                                    String prefix,
			                                    String tag,
			                                    String content,
			                                    String...attr) {
		out.print(indent + "<" + prefix + ":" + tag);
		for (int i=0; i<attr.length; i+=2) {
			out.print(" " + attr[i] + "=\"" + attr[i+1] + "\"");
		}
		out.println(">" + content + "</" + prefix + ":" + tag + ">");
	}
	
	/** Print end tag.
	 * @param out    Output print stream
	 * @param indent Identation
	 * @param prefix Namespace prefix
	 * @param tag    Tag name
	 */
	protected static synchronized void printEndTag(PrintStream out,
			                                       String indent,
			                                       String prefix,
			                                       String tag) {
		out.println(indent + "</" + prefix + ":" + tag + ">");
	}
}
