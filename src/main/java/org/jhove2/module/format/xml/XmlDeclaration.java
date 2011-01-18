/**
 * JHOVE2 - Next-generation architecture for format-aware characterization
 * <p>
 * Copyright (c) 2009 by The Regents of the University of California, Ithaka
 * Harbors, Inc., and The Board of Trustees of the Leland Stanford Junior
 * University. All rights reserved.
 * </p>
 * <p>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * </p>
 * <ul>
 * <li>Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.</li>
 * <li>Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.</li>
 * <li>Neither the name of the University of California/California Digital
 * Library, Ithaka Harbors/Portico, or Stanford University, nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.</li>
 * </ul>
 * <p>
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
 * </p>
 */
package org.jhove2.module.format.xml;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.io.Input;
import org.jhove2.core.reportable.AbstractReportable;

import com.sleepycat.persist.model.Persistent;

/**
 * This class is used to hold information about an <i>XML declaration</i>
 * discovered during parsing of an XML instance.  For example: <br />
 * &lt;?xml version="1.0" encoding="UTF-8" standalone="yes"?>
 * 
 * @author rnanders
 * @see http://www.w3.org/TR/xml/#NT-XMLDecl
 */
@Persistent
public class XmlDeclaration extends AbstractReportable {

    /** The regular expression group for the version information. */
    private static final String VERSION_REGEX = "(?:\\s*version\\s*=\\s*[\'\"]([^\'\"]+)[\'\"]){0,1}";
    
    /** The regular expression group for the optional encoding information. */
    private static final String ENCODING_REGEX = "(?:\\s*encoding\\s*=\\s*[\'\"]([^\'\"]+)[\'\"]){0,1}";
    
    /** The regular expression group for the optional standalone information. */
    private static final String STANDALONE_REGEX = "(?:\\s*standalone\\s*=\\s*[\'\"]([^\'\"]+)[\'\"]){0,1}";
    
    /** The regular expression for the XML declaration. */
    private static final String DECLARATION_REGEX = "<\\?xml" + VERSION_REGEX + ENCODING_REGEX + STANDALONE_REGEX;
    
    /** The compiled regex pattern for the XML declaration. */
    private static final Pattern DECLARATION_PATTERN = Pattern.compile(DECLARATION_REGEX);

    /** The XML version number as found by SAX2. */
    protected String versionFromSAX2;

    /** The version number actually declared. */
    protected String versionDeclared;

    /** The character encoding as found by SAX2. */
    protected String encodingFromSAX2;

    /** The character encoding actually declared. */
    protected String encodingDeclared;

    /** The standalone status as found by SAX2. */
    protected String standaloneFromSAX2;

    /** The standalone status actually declared. */
    protected String standaloneDeclared;
    
    public XmlDeclaration(){
    	super();
    }

    /**
     * Gets the version number of the XML Standard to which this instance
     * conforms.
     * 
     * @return the version number
     */
    @ReportableProperty(order = 1, value = "XML Version")
    public String getVersion() {
        return getValue(versionFromSAX2, versionDeclared);
    }

    /**
     * Gets the character encoding used in the XML instance.
     * 
     * @return the character encoding
     */
    @ReportableProperty(order = 2, value = "Character Encoding")
    public String getEncoding() {
        return getValue(encodingFromSAX2, encodingDeclared);
    }

    /**
     * Gets the standalone status of the DTD markup declarations.
     * 
     * @return the standalone status
     */
    @ReportableProperty(order = 3, value = "Standalone")
    public String getStandalone() {
        return getValue(standaloneFromSAX2, standaloneDeclared);
    }

    /**
     * Compares the value found by SAX2 and the value explicitly specified in the XML
     * Declaration. <br />
     * 
     * <ul>
     * <li>If equal, then returns the SAX2 value.</li>
     * <li>If value was omitted from the declaration, then value returned is labeled "(default)".</li>
     * <li>If the SAX2 and declared values differ, then both values are reported.</li>
     * </ul>
     * 
     * @param valueFromSAX2
     *            the value found by SAX2 parser
     * @param valueDeclared
     *            the value actually specified in the XML Declaration
     * @return the value
     */
    private String getValue(String valueFromSAX2, String valueDeclared) {
        if (valueFromSAX2 == null) {
            return valueDeclared;
        }
        else if (valueFromSAX2.equalsIgnoreCase(valueDeclared)) {
            return valueFromSAX2;
        }
        else if (valueDeclared == null) {
            return valueFromSAX2 + " [default]";
        } else {
            return valueFromSAX2 + " [!= (value declared = " + valueDeclared + ")]";
        }
    }

    /**
     * Parses the beginning of the XML document to extract the values declared
     * for version, encoding, and standalone
     * 
     * @param input
     *            the Input from the Source object
     * @throws IOException.
     */
    protected void parse(Input input) throws IOException {
        /* Get the text of the XML Declaration */
        StringBuffer sb = new StringBuffer();
        char c;
        do {
            c = (char) input.readUnsignedByte();
            sb.append(c);
        }
        while ((c != '>') && (c != Input.EOF) && (sb.length() < 100));
        String xmldecl = sb.toString();
        /* Use regular expression capture groups to extract values (or null if omitted) */
        Matcher m = XmlDeclaration.DECLARATION_PATTERN.matcher(xmldecl);
        if (m.find()) {
            versionDeclared = m.group(1);
            encodingDeclared = m.group(2);
            standaloneDeclared = m.group(3);
        }
    }

}
