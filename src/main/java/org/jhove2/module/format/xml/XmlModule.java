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

import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.Format;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.source.Source;
import org.jhove2.module.format.BaseFormatModule;
import org.jhove2.module.format.Validator;

import org.xml.sax.*;

/**
 * JHOVE2 XML module. This module parses and XML instance and captures selected
 * characterization information
 * 
 * @author rnanders
 */
public class XmlModule extends BaseFormatModule implements Validator {

    /** Module version identifier. */
    public static final String VERSION = "0.1.0";

    /** Module release date. */
    public static final String RELEASE = "2009-09-23";

    /** Module rights statement. */
    public static final String RIGHTS = "Copyright 2009 by The Board of Trustees of the Leland Stanford Junior University. "
            + "Available under the terms of the BSD license.";

    /** Module validation coverage. */
    public static final Coverage COVERAGE = Coverage.Inclusive;

    /** Source unit's validity status. */
    protected Validity isValid;

    /**
     * Get module validation coverage.
     */
    @Override
    public Coverage getCoverage() {
        return COVERAGE;
    }

    /**
     * Get source unit's validation status.
     */
    @Override
    public Validity isValid() {
        return validationResults.getValidity();
    }

    /**
     * Validate a source unit.
     */
    @Override
    public Validity validate(JHOVE2 jhove2, Source source)
            throws JHOVE2Exception {
        return validationResults.getValidity();
    }

    /**
     * Instantiates a new XmlModule instance.
     * 
     * @param format
     *            the Format object
     */
    public XmlModule(Format format) {
        super(VERSION, RELEASE, RIGHTS, format);
    }

    /** The instance of a SAX2 XMLReader class used to parse XML instances. */
    protected SaxParser saxParser;

    /** Data store for XML declaration information captured during the parse. */
    protected XmlDeclaration xmlDeclaration = new XmlDeclaration();

    /** The XML document's root element name. */
    protected String rootElementName;

    /** A list of the documents document type declarations. */
    protected List<DTD> dtds  = new ArrayList<DTD>();

    /** Data store for XML namespace information captured during the parse. */
    protected Map<String, Namespace> namespaces = new TreeMap<String, Namespace>();

    /** Data store for XML notation information captured during the parse. */
    protected List<Notation> notations = new ArrayList<Notation>();

    //TODO implementing this would require wrapping the input stream and 
    // checking for &# in the source XML document
    /**
     * Data store for XML character reference information captured during the
     * parse. [not implemented]
     */
    protected List<String> xmlCharacterReferences;

    /** Data store for XML entity declarations captured during the parse. */
    protected List<Entity> entities = new ArrayList<Entity>();

    /** Data store for XML entity references captured during the parse. */
    protected List<String> entityReferences = new ArrayList<String>();

    /**
     * Data store for XML processing instruction information captured during the
     * parse.
     */
    protected List<ProcessingInstruction> processingInstructions = new ArrayList<ProcessingInstruction>();

    /** Data store for XML comment information captured during the parse. */
    protected List<String> comments = new ArrayList<String>();

    /** The validation results. */
    protected ValidationResults validationResults = new ValidationResults();

    /** The well formed status of the source unit. */
    protected boolean wellFormed = false;

    /**
     * Sets the SaxParser object to be used for parsing the source unit.
     * 
     * @param saxParser
     *            the saxParser object
     */
    public void setSaxParser(SaxParser saxParser) {
        this.saxParser = saxParser;
    }

    /**
     * Gets the SaxParser object used for parsing the source unit.
     * 
     * @return SaxParser object
     */
    @ReportableProperty(order = 1, value = "XML Parser name, features, properties")
    public SaxParser getSaxParser() {
        return saxParser;
    }

    /**
     * Gets the XML Declaration data.
     * 
     * @return XML Declaration data
     */
    @ReportableProperty(order = 2, value = "XML Declaration data")
    public XmlDeclaration getXmlDeclaration() {
        return xmlDeclaration;
    }

    /**
     * Gets the root element name.
     * 
     * @return root element name
     */
    @ReportableProperty(order = 3, value = "Name of the document's root element")
    public String getRootElementName() {
        return rootElementName;
    }

    /**
     * Gets the list of documents document type declarations.
     * 
     * @return list of documents document type declarations
     */
    @ReportableProperty(order = 4, value = "List of Document Type Definitions (DTDs)")
    public List<DTD> getDTDs() {
        return dtds;
    }

    /**
     * Gets the list of XML namespaces.
     * 
     * @return list of XML namespaces
     */
    @ReportableProperty(order = 5, value = "List of XML Namespaces")
    public ArrayList<Namespace> getNamespaces() {
        if (namespaces != null) {
            return new ArrayList<Namespace>(namespaces.values());
        }
        else {
            return null;
        }
    }

    /**
     * Gets the list of XML notations.
     * 
     * @return list of XML notations
     */
    @ReportableProperty(order = 6, value = "List of Notations found in the XML document")
    public List<Notation> getNotations() {
        return notations;
    }

    /**
     * Gets the list of XML character references.
     * 
     * @return list of XML character references
     */
    @ReportableProperty(order = 7, value = "List of Character References")
    public List<String> getXmlCharacterReferences() {
        return xmlCharacterReferences;
    }

    /**
     * Gets the list of XML entity declarations.
     * 
     * @return list of XML entity declarations
     */
    @ReportableProperty(order = 8, value = "List of Entity Declarations")
    public List<Entity> getEntities() {
        return entities;
    }

    /**
     * Gets the list of XML entity references.
     * 
     * @return list of XML entity references
     */
    @ReportableProperty(order = 9, value = "List of Entity References")
    public List<String> getEntityReferences() {
        return entityReferences;
    }

    /**
     * Gets the list of XML processing instructions.
     * 
     * @return list of XML processing instructions
     */
    @ReportableProperty(order = 10, value = "List of Processing Instructions")
    public List<ProcessingInstruction> getProcessingInstructions() {
        return processingInstructions;
    }

    /**
     * Gets the list of XML comments.
     * 
     * @return list of XML comments
     */
    @ReportableProperty(order = 11, value = "List of Comments")
    public List<String> getComments() {
        return comments;
    }

    /**
     * Gets the validation results.
     * 
     * @return validation results
     */
    @ReportableProperty(order = 12, value = "Warning or error messages generated during XML Validation")
    public ValidationResults getValidationResults() {
        return validationResults;
    }

    /**
     * Gets the well-formedness status.
     * 
     * @return well-formedness status
     */
    @ReportableProperty(order = 13, value = "XML well-formed status")
    public boolean isWellFormed() {
        return wellFormed;
    }

    /**
     * Parse a source unit.
     */
    @Override
    public long parse(JHOVE2 jhove2, Source source) throws EOFException,
            IOException, JHOVE2Exception {
        if ((saxParser == null))
            saxParser = new SaxParser();
        saxParser.setXmlModule(this);
        /* The XMLReader does the parsing of the XML */
        XMLReader xmlReader = saxParser.getXmlReader();

        /* Create the InputSource object containing the XML entity to be parsed */
        InputSource saxInputSource = new InputSource(source.getInputStream());
        /* Provide the path of the source file, in case relative paths need to be resolved */
        if (source.getFile() != null) {
            saxInputSource.setSystemId(source.getFile().getAbsolutePath());
        }
        /* Here's where the parsing takes place */
        try {
            xmlReader.parse(saxInputSource);
            wellFormed = true;
        }
        catch (SAXParseException spe) {
            wellFormed = false;
        }
        catch (SAXException e) {
            throw new JHOVE2Exception("Could not parse the Source object", e);
        }
        return 0;
    }

}
