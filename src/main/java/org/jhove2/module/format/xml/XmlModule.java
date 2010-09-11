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

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.Message;
import org.jhove2.core.Message.Context;
import org.jhove2.core.Message.Severity;
import org.jhove2.core.format.Format;
import org.jhove2.core.io.Input;
import org.jhove2.core.source.Source;
import org.jhove2.module.format.BaseFormatModule;
import org.jhove2.module.format.Validator;

/**
 * JHOVE2 XML module. This module parses and XML instance and captures selected
 * characterization information
 * 
 * @author rnanders
 */
public class XmlModule extends BaseFormatModule implements Validator {

    /** Module version identifier. */
    public static final String VERSION = "2.0.0";

    /** Module release date. */
    public static final String RELEASE = "2010-09-10";

    /** Module rights statement. */
    public static final String RIGHTS = "Copyright 2010 by The Board of Trustees of the Leland Stanford Junior University. "
            + "Available under the terms of the BSD license.";

    /** Module validation coverage. */
    public static final Coverage COVERAGE = Coverage.Inclusive;
    
    /** The JHOVE2 object passed in by the parse method */
    protected JHOVE2 jhove2; 
    
    /** The Source object passed in by the parse method */
    protected  Source source;

    /** XML validation status. */
    protected Validity validity;

    /**
     * Get module validation coverage.
     * 
     * @return the coverage
     */
    @Override
    public Coverage getCoverage() {
        return COVERAGE;
    }

    /**
     * Get source unit's validation status.
     * 
     * @return the validity
     */
    @Override
    public Validity isValid() {
        if (validity == null) {
            try {
                validate(jhove2, source);
            }
            catch (JHOVE2Exception e) {
            }
        }
        return validity;
    }

    /**
     * Validate the XML parse results.
     * 
     * @param jhove2
     *            the jhove2
     * @param source
     *            the source
     * 
     * @return the validity
     * 
     * @throws JHOVE2Exception
     *             the JHOV e2 exception
     */
    @Override
    public Validity validate(JHOVE2 jhove2, Source source)
            throws JHOVE2Exception {
        /* See if validity has been previously set to False, e.g. by parse exception trap */
        if ((validity != null) && (validity == Validity.False)) {
            return validity;
        }            
       /* Check to see if there were SAX parser errors of any sort */
        if (validationResults.fatalParserErrors.getValidationMessageCount() > 0) {
            Object[]messageArgs = new Object[]{"Fatal Parser Errors found"};
            saxParserMessages.add(new Message(Severity.ERROR,
                    Context.OBJECT,
                    "org.jhove2.module.format.xml.XmlModule.validationErrorsFound",
                    messageArgs, jhove2.getConfigInfo()));
            return (validity = Validity.False);
        }
        else if (validationResults.parserErrors.getValidationMessageCount() > 0) {
            Object[]messageArgs = new Object[]{"Parser Errors found"};
            saxParserMessages.add(new Message(Severity.ERROR,
                    Context.OBJECT,
                    "org.jhove2.module.format.xml.XmlModule.validationErrorsFound",
                    messageArgs, jhove2.getConfigInfo()));
            return (validity = Validity.False);
        }
        /* See if validity has been previously set to undetermined, e.g. if schema file not found */
        if ((validity != null) && (validity == Validity.Undetermined)) {
            return validity;
        }    
        /* No validation errors found, but make sure schema validation was enabled, if appropriate */
        if (namespaceInformation.hasSchemaLocations) {
            if (! saxParser.hasFeature("http://apache.org/xml/features/validation/schema")) {
                Object[]messageArgs = new Object[]{"Schema location(s) specified, but schema validation is disabled by SAX feature setting."};
                saxParserMessages.add(new Message(Severity.WARNING,
                        Context.OBJECT,
                        "org.jhove2.module.format.xml.XmlModule.validationDisabled",
                        messageArgs, jhove2.getConfigInfo()));
                return (validity = Validity.Undetermined);
            }
        }
        /* No validation errors found, but make sure validation was enabled */
        if (saxParser.hasFeature("http://apache.org/xml/features/validation/dynamic")) {
            return (validity = Validity.True);
        }
        else if (saxParser.hasFeature("http://xml.org/sax/features/validation")) {
            return (validity = Validity.True);
        }
        else {
            Object[]messageArgs = new Object[]{"XML validation is disabled by SAX feature setting."};
            saxParserMessages.add(new Message(Severity.WARNING,
                    Context.OBJECT,
                    "org.jhove2.module.format.xml.XmlModule.validationDisabled",
                    messageArgs, jhove2.getConfigInfo()));
            return (validity = Validity.Undetermined);
        }
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
    
    public XmlModule(){
    	this(null);
    }

    /** The instance of a SAX2 XMLReader class used to parse XML instances. */
    protected SaxParser saxParser;

    /** If true, run a separate parse to extract numeric character references */
    protected boolean ncrParser = false;

    /** Data store for XML declaration information captured during the parse. */
    protected XmlDeclaration xmlDeclaration = new XmlDeclaration();

    /** The XML document's root element. */
    protected RootElement rootElement;

    /** A list of the documents document scope declarations. */
    protected List<DTD> dtds = new ArrayList<DTD>();

    /** Data store for XML namespace information captured during the parse. */
    protected NamespaceInformation namespaceInformation = new NamespaceInformation();

    /** Data store for XML notation information captured during the parse. */
    protected List<Notation> notations = new ArrayList<Notation>();

    /** Data store for XML entity declarations captured during the parse. */
    protected List<Entity> entities = new ArrayList<Entity>();

    /** Data store for XML entity references captured during the parse. */
    protected EntityReferences entityReferences = new EntityReferences();

    /**
     * Data store for XML numeric character references captured during the
     * parse.
     */
    protected NumericCharacterReferences numericCharacterReferences = new NumericCharacterReferences();

    /**
     * Data store for XML processing instruction information captured during the
     * parse.
     */
    protected List<ProcessingInstruction> processingInstructions = new ArrayList<ProcessingInstruction>();

    /** Data store for XML comment information captured during the parse. */
    protected CommentInformation commentInformation = new CommentInformation();

    /** The validation results. */
    protected ValidationResults validationResults = new ValidationResults();

    /** Fail fast message. */
    protected Message failFastMessage;

    /** SAX Parser messages. */
    protected ArrayList<Message> saxParserMessages = new ArrayList<Message>();

    /** The well formed status of the source unit. */
    protected Validity wellFormed = Validity.Undetermined;

    /**
     * Sets the SaxParser object to be used for parsing the source unit.
     * 
     * @param saxParser
     *            the saxParser object
     */
    public void setSaxParser(SaxParser saxParser) {
        this.saxParser = saxParser;
        this.saxParser.setXmlModule(this);
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

    /** Sets the flag that specifies whether or not to collect comment text */
    public void setCollectCommentText(boolean collectCommentText) {
        this.commentInformation.collectCommentText = collectCommentText;
    }

    /**
     * Sets the class name of the parser to be used for extracting numeric
     * character references
     */
    public void setNcrParser(boolean ncrParser) {
        this.ncrParser = ncrParser;
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
    @ReportableProperty(order = 3, value = "The document's root element")
    public RootElement getRootElement() {
        return rootElement;
    }

    /**
     * Gets the list of documents document scope declarations (DTDs).
     * 
     * @return list of documents document scope declarations (DTDs)
     */
    @ReportableProperty(order = 4, value = "List of Document Scope Definitions (DTDs)")
    public List<DTD> getDTDs() {
        return dtds;
    }

    /**
     * Gets the list of XML namespaces.
     * 
     * @return list of XML namespaces
     */
    @ReportableProperty(order = 5, value = "XML Namespace Information")
    public NamespaceInformation getNamespaceInformation() {
        return namespaceInformation;
    }

    /**
     * Gets the list of XML entity declarations.
     * 
     * @return list of XML entity declarations
     */
    @ReportableProperty(order = 6, value = "List of Entity Declarations")
    public List<Entity> getEntities() {
        return entities;
    }

    /**
     * Gets the list of XML entity references.
     * 
     * @return list of XML entity references
     */
    @ReportableProperty(order = 7, value = "List of Entity References")
    public ArrayList<EntityReference> getEntityReferences() {
        return entityReferences.getEntityReferenceList();
    }

    /**
     * Gets the list of XML notations.
     * 
     * @return list of XML notations
     */
    @ReportableProperty(order = 8, value = "List of Notations found in the XML document")
    public List<Notation> getNotations() {
        return notations;
    }

    /**
     * Gets the list of XML Numeric Character References.
     * 
     * @return list of XML Numeric Character References
     */
    @ReportableProperty(order = 9, value = "List of Numeric Character References")
    public ArrayList<NumericCharacterReference> getNumericCharacterReferences() {
        return numericCharacterReferences.getNumericCharacterReferenceList();
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
    public CommentInformation getCommentInformation() {
        return commentInformation;
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
    public Validity isWellFormed() {
        return wellFormed;
    }

    /**
     * Get fail fast message.
     * 
     * @return Fail fast message
     */
    @ReportableProperty(order = 14, value = "Fail fast message.")
    public Message getFailFast() {
        return this.failFastMessage;
    }

    /**
     * Get SAX Parser messages.
     * 
     * @return SAX Parser messages
     */
    @ReportableProperty(order = 15, value = "SAX Parser Messages.")
    public List<Message> getSaxParserMessages() {
        return this.saxParserMessages;
    }

    /**
     * Parse a source unit.
     * 
     * @param jhove2
     *            the jhove2
     * @param source
     *            the source
     * 
     * @return the long
     * 
     * @throws EOFException
     *             the EOF exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws JHOVE2Exception
     *             the JHOV e2 exception
     */
    @Override
    public long parse(JHOVE2 jhove2, Source source) throws 
            IOException, JHOVE2Exception {
        Input input = null;
        try {
            this.jhove2 = jhove2;
            this.source = source;
            
            /* Use SAX2 to get what information is available from that mechanism */
            saxParser.parse(source, jhove2);

            /* Get the input object */
            input = source.getInput(jhove2);

            /*
             * Do a separate parse of the XML Declaration at the start of the
             * document
             */
            input.setPosition(source.getStartingOffset());
            xmlDeclaration.parse(input);

            /* Do a separate parse to inventory numeric character references */
            if (this.ncrParser) {
                input.setPosition(0L);
                numericCharacterReferences.parse(input,
                        xmlDeclaration.encodingFromSAX2, jhove2);
            }
            validate(jhove2,source);
            return 0;
            
        } finally {
            if (input != null) {
                input.close();
            }
        }
   }

}
