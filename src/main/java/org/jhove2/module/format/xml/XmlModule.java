/**
 * JHOVE2 - Next-generation architecture for format-aware characterization
 *
 * Copyright (c) 2009 by The Board of Trustees of the Leland Stanford Junior University.
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

package org.jhove2.module.format.xml;

import java.io.EOFException;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.Format;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.source.Source;
import org.jhove2.module.format.BaseFormatModule;
import org.jhove2.module.format.Validator;

import org.xml.sax.*;
import org.xml.sax.helpers.*;


/**
 * JHOVE2 XML module.
 * 
 * @author rnanders
 */
public class XmlModule 
	extends BaseFormatModule
	implements Validator {

	/** Module version identifier. */
	public static final String VERSION = "0.1.0";

	/** Module release date. */
	public static final String RELEASE = "2009-09-23";

	/** Module rights statement. */
	public static final String RIGHTS =
		"Copyright 2009 by The Board of Trustees of the Leland Stanford Junior University. " +
		"Available under the terms of the BSD license.";
	
	/** Module validation coverage. */
	public static final Coverage COVERAGE = Coverage.Exhaustive;
	
	/** UTF-8 validity status. */
	protected Validity isValid;


	/** Get UTF-8 module validation coverage.
	 * @return UTF-8 module validation coverage
	 */
	@Override
	public Coverage getCoverage() {
		return COVERAGE;
	}

	/**
	 * Get UTF-8 validation status.
	 * 
	 * @return UTF-8 validation status
	 * @see org.jhove2.module.format.Validator#isValid()
	 */
	@Override
	public Validity isValid() {
		return xmlValidationResults.getValidity();
	}

	/**
	 * Validate a UTF-8 source unit.
	 * 
	 * @param jhove2
	 *            JHOVE2 framework
	 * @param source
	 *            UTF-8 source unit
	 * @return UTF-8 validation status
	 * @see org.jhove2.module.format.Validator#validate(org.jhove2.core.JHOVE2,
	 *      org.jhove2.core.source.Source)
	 */
	@Override
	public Validity validate(JHOVE2 jhove2, Source source)
			throws JHOVE2Exception {
		return xmlValidationResults.getValidity();
	}
	


	public XmlModule(Format format) {
		super(VERSION, RELEASE, RIGHTS, format);
	}
	
	/**
	 * The implementation of the SAX2 XMLReader class used to parse XML instances
	 */
	protected String saxParser;
	protected XmlDeclaration xmlDeclaration = new XmlDeclaration();
	protected String xmlRootElementName;
	protected List<XmlDTD> xmlDTDs;
	protected HashMap<String,XmlNamespace> xmlNamespaceMap;
	protected List<XmlNotation> xmlNotations;
	protected List<String> xmlCharacterReferences;
	protected List<XmlEntity> xmlEntitys;
	protected List<XmlProcessingInstruction> xmlProcessingInstructions;
	protected List<String> xmlComments;
	protected XmlValidationResults xmlValidationResults = new XmlValidationResults();
	protected boolean wellFormed = false;

	/**
	 * This property is optionally set by the Spring config file that instantiates the module 
	 * @param saxParser
	 */
	public void setSaxParser(String saxParser) {
		this.saxParser = saxParser;
	}
	/**
	 * @return saxParser
	 */
	@ReportableProperty(order = 1, value = "Java class used to parse the XML")
	public String getSaxParser() {
		return saxParser;
	}

	/**
	 * @return xmlDeclaration
	 */
	@ReportableProperty(order = 2, value = "XML Declaration data")
	public XmlDeclaration getXmlDeclaration() {
		return xmlDeclaration;
	}

	/**
	 * @return xmlRootElementName
	 */
	@ReportableProperty(order = 3, value = "Name of the document's root element")
	public String getXmlRootElementName() {
		return xmlRootElementName;
	}

	/**
	 * @return xmlDtdList
	 */
	@ReportableProperty(order = 4, value = "List of Document Type Definitions (DTDs)")
	public List<XmlDTD> getXmlDtds() {
		return xmlDTDs;
	}

	/**
	 * @return xmlNamespaceMap
	 */
	@ReportableProperty(order = 5, value = "List of XML Namespaces")
	public Collection<XmlNamespace> getXmlNamespaceMap() {
		if (xmlNamespaceMap != null) {
			return xmlNamespaceMap.values();			
		} else {
			return null;
		}
	}

	/**
	 * @return xmlNotations
	 */
	@ReportableProperty(order = 6, value = "List of Notations found in the XML document")
	public List<XmlNotation> getXmlNotations() {
		return xmlNotations;
	}

	/**
	 * @return xmlCharacterReferences
	 */
	@ReportableProperty(order = 7, value = "List of Character References")
	public List<String> getXmlCharacterReferences() {
		return xmlCharacterReferences;
	}

	/**
	 * @return xmlEntities
	 */
	@ReportableProperty(order = 8, value = "List of Entities")
	public List<XmlEntity> getXmlEntitys() {
		return xmlEntitys;
	}

	/**
	 * @return xmlProcessingInstructions
	 */
	@ReportableProperty(order = 9, value = "List of Processing Instructions")
	public List<XmlProcessingInstruction> getXmlProcessingInstructions() {
		return xmlProcessingInstructions;
	}

	/**
	 * @return xmlComments
	 */
	@ReportableProperty(order = 10, value = "List of Comments")
	public List<String> getXmlComments() {
		return xmlComments;
	}
	
	/**
	 * @return xmlValidationResults
	 */
	@ReportableProperty(order = 11, value = "Warning or error messages generated during XML Validation")
	public XmlValidationResults getXmlValidationResults() {
		return xmlValidationResults;
	}

	/**
	 * @return wellFormed
	 */
	@ReportableProperty(order = 12, value = "XML well-formed status")
	public boolean isWellFormed() {
		return wellFormed;
	}

	/**
	 * Parse a source unit.
	 * 
	 * @param jhove2
	 *            JHOVE2 framework
	 * @param source
	 *            UTF-8 source unit
	 * @return 0
	 * @throws EOFException
	 *             If End-of-File is reached reading the source unit
	 * @throws IOException
	 *             If an I/O exception is raised reading the source unit
	 * @throws JHOVE2Exception
	 * @see org.jhove2.module.format.Parser#parse(org.jhove2.core.JHOVE2,
	 *      org.jhove2.core.source.Source)
	 */
	@Override
	public long parse(JHOVE2 jhove2, Source source) 
		throws EOFException, IOException, JHOVE2Exception {
		// The XMLReader does the parsing of the XML
        XMLReader xmlReader;
		try {
			if (saxParser != null) {
				// Sax Parser class name has been specified in the Spring config file
				xmlReader = XMLReaderFactory.createXMLReader(saxParser);				
			} else {
				// Sax Parser class name will be determined from value of org.xml.sax.driver
				// set as an environmental variable or a META-INF value from a jar file in the classpath
				xmlReader = XMLReaderFactory.createXMLReader();	
				this.setSaxParser(xmlReader.getClass().getName());
			}
			// Specify that validation should occur  when the XML entity is parsed
			final String validationFeature = "http://xml.org/sax/features/validation";
			xmlReader.setFeature(validationFeature, true);
			final String schemaFeature = "http://apache.org/xml/features/validation/schema";
			xmlReader.setFeature(schemaFeature, true);
		} catch (SAXException e) {
			throw new JHOVE2Exception("Could not create a SAX parser", e);
		}
        XmlParserContentHandler contentHandler = new XmlParserContentHandler(xmlReader, this);
        xmlReader.setContentHandler(contentHandler);
        XmlParserErrortHandler xmlParserErrortHandler= new XmlParserErrortHandler(this);
        xmlReader.setErrorHandler(xmlParserErrortHandler);
        // Create the InputSource object containing the XML entity to be parsed
        InputSource saxInputSource = new InputSource(source.getInputStream());
        // Provide the path of the source file, in case relative paths need to be resolved
        if (source.getFile() != null) {
            saxInputSource.setSystemId(source.getFile().getAbsolutePath());       	
        }
        try {
			xmlReader.parse(saxInputSource);
			wellFormed = true;
        } catch (SAXParseException spe) {
        	wellFormed = false;
		} catch (SAXException e) {
			throw new JHOVE2Exception("Could not parse the Source object", e);
		}
		return 0;
    }


	

}
