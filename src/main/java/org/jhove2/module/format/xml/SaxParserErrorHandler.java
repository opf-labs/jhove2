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

import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.Message;
import org.jhove2.core.Message.Context;
import org.jhove2.core.Message.Severity;
import org.jhove2.module.format.Validator.Validity;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * An instance of this class is registered with the SAX parser to handle events
 * related to errors or warnings that occur during the parsing of the XML
 * document.
 * 
 * @author rnanders
 * @see <a
 *      href="http://www.saxproject.org/apidoc/org/xml/sax/ErrorHandler.html">ErrorHandler javadoc</a>
 */
public class SaxParserErrorHandler
    extends DefaultHandler
    implements ErrorHandler
{
    /** The XmlModule object that is invoking the parser. */
    private XmlModule xmlModule;

    /**
     * Instantiates a new SaxParserErrorHandler object.
     * 
     * @param xmlModule
     *            the XmlModule object that is invoking the parser
     */
    public SaxParserErrorHandler(XmlModule xmlModule) {
        this.xmlModule = xmlModule;
    }

    /**
     * Receive notification of a warning.
     */
    @Override
    public void warning(SAXParseException exception) throws SAXException {
        xmlModule.validationResults.addParserWarning(exception);
        /* If we get an warning like the following, we cannot read the schema file
         * schema_reference.4: Failed to read schema document 'file:myschema.xsd', 
         * because 
         * 1) could not find the document; 
         * 2) the document could not be read; 
         * 3) the root element of the document is not <xsd:schema>. */
        if (exception.getMessage().contains("schema_reference.4:")) {
            xmlModule.validity = Validity.Undetermined;
            try {
                Object[]messageArgs = new Object[]{exception.getMessage()};
                xmlModule.saxParserMessages.add(new Message(Severity.ERROR,
                        Context.OBJECT,
                        "org.jhove2.module.format.xml.XmlModule.entityReferenceNotResolved",
                        messageArgs, xmlModule.jhove2.getConfigInfo()));
            }
            catch (JHOVE2Exception e) {
            }
        }
    }

    /**
     * Receive notification of a recoverable error.
     */
    @Override
    public void error(SAXParseException exception) throws SAXException {
        xmlModule.validationResults.addParserError(exception);
    }

    /**
     * Receive notification of a non-recoverable error.
     */
    @Override
    public void fatalError(SAXParseException exception) throws SAXException {
        xmlModule.validationResults.addFatalError(exception);
    }

}
