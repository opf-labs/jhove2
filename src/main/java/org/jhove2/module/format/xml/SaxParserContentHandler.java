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

import java.util.Map;
import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Locator;
import org.xml.sax.ext.Locator2;
import org.xml.sax.SAXException;

/**
 * An instance of this class is registered with the SAX parser to handle events
 * related to the XML document's content, such as start and end of the document,
 * start and end of an element, character data, and processing instructions that
 * are encountered.
 * 
 * @author rnanders
 * 
 * @see <a
 *      href="http://www.saxproject.org/apidoc/org/xml/sax/ContentHandler.html">ContentHander javadoc</a>
 * 
 */
public class SaxParserContentHandler extends DefaultHandler implements
        ContentHandler {

    /** The XmlModule object that is invoking the parser. */
    private XmlModule xmlModule;

    /**
     * The XMLReader object (the actual parser) to which this handler is
     * registered
     */
    private XMLReader xmlReader;

    /**
     * Used to obtain the location of SAX event in the XML source document
     * 
     * @see org.xml.sax.Locator Locator
     */
    @SuppressWarnings("unused")
    private Locator locator;

    /**
     * SAX2 extension to augment the entity information provided though a
     * Locator Used to obtain the character encoding and version values from the
     * XML Declaration
     * 
     * @see org.xml.sax.ext.Locator2 Locator2
     */
    private Locator2 locator2;

    /**
     * Instantiates a new SaxParserContentHandler object.
     * 
     * @param xmlReader
     *            the XMLReader object that is the parser
     * @param xmlModule
     *            the XmlModule object that is invoking the parser
     */
    public SaxParserContentHandler(XMLReader xmlReader, XmlModule xmlModule) {
        this.xmlReader = xmlReader;
        this.xmlModule = xmlModule;
    }

    /**
     * Receive a Locator object for document events.
     */
    @Override
    public void setDocumentLocator(Locator locator) {
        this.locator = locator;
        if (locator instanceof Locator2) {
            this.locator2 = (Locator2) locator;
        }
    }

    /**
     * Receive notification of the beginning of the document. In this
     * application we are using the event to capture the value of the XML
     * Declaration's encoding and version
     */
    @Override
    public void startDocument() {
        if (locator2 != null) {
            xmlModule.xmlDeclaration.encoding = locator2.getEncoding();
            xmlModule.xmlDeclaration.version = locator2.getXMLVersion();
        }
    }

    /**
     * Receive notification of the end of the document. In this application we
     * are using this event to capture the value of the is-standalone parser
     * feature. The value is true if the document specified standalone="yes" in
     * its XML declaration, and otherwise is false.
     * 
     * @see <a
     *      href="http://www.saxproject.org/apidoc/org/xml/sax/package-summary.html">SAX2
     *      Standard Feature Flags</a>
     */
    @Override
    public void endDocument() {
        try {
            boolean isStandalone = xmlReader
                    .getFeature("http://xml.org/sax/features/is-standalone");
            xmlModule.xmlDeclaration.standalone = (isStandalone) ? "yes" : "no";
        }
        catch (SAXException e) {
            xmlModule.xmlDeclaration.standalone = null;
        }
    }

    /**
     * This event fires at the beginning of every element in the XML document.
     * In this application we are using the event to capture the root element's
     * name
     */
    @Override
    public void startElement(String uri, String localName, String qName,
            Attributes atts) throws SAXException {
        /* The first element we encounter is the root, so we save the name info */
        if (xmlModule.rootElementName == null) {
            if (qName.equals("")) {
                xmlModule.rootElementName = localName;
            }
            else {
                xmlModule.rootElementName = qName;
            }
        }
    }

    /**
     * Capture prefix-URI Namespace mapping
     */
    @Override
    public void startPrefixMapping(String prefix, String uri)
            throws SAXException {
        Map<String, Namespace> nsMap = this.xmlModule.namespaces;
        /*
         * Constructing a map key to ensure that unique combinations of prefix
         * and URI are only listed one time
         */
        String key;
        if (prefix.length() < 1) {
            /* make sure the default namespace heads the list */
            key = " " + uri;
        }
        else {
            key = prefix + uri;
        }
        /* only add the prefix/URI combination if not already present */
        if (!nsMap.containsKey(key)) {
            Namespace ns = new Namespace();
            ns.prefix = prefix;
            ns.uri = uri;
            nsMap.put(key, ns);
        }
    }

    /**
     * Receive notification of a processing instruction
     */
    @Override
    public void processingInstruction(String target, String data)
            throws SAXException {
        ProcessingInstruction pi = new ProcessingInstruction();
        pi.target = target;
        pi.data = data;
        xmlModule.processingInstructions.add(pi);
    }

}
