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

import java.util.ArrayList;
import java.util.List;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.reportable.AbstractReportable;
import org.jhove2.module.format.Validator.Validity;
import org.xml.sax.SAXParseException;

/**
 * A data structure to hold warning and error messages captured during XML
 * validation.
 * 
 * @author rnanders
 */
public class ValidationResults extends AbstractReportable {

    /** XML validity status. */
    protected Validity isValid = Validity.True;

    /** Warnings found during XML parsing. */
    protected ValidationMessageList parserWarnings = new ValidationMessageList();

    /** Errors found during XML parsing. */
    protected ValidationMessageList parserErrors = new ValidationMessageList();

    /** Fatal errors found during XML parsing. */
    protected ValidationMessageList fatalParserErrors = new ValidationMessageList();

    /**
     * Get the warnings found during XML parsing.
     * 
     * @return the parser warning messages
     */
    @ReportableProperty(order = 1, value = "Warnings found during XML parsing")
    public ValidationMessageList getParserWarnings() {
        return parserWarnings;
    }

    /**
     * Get the errors found during XML parsing.
     * 
     * @return the parser error messages
     */
    @ReportableProperty(order = 2, value = "Errors found during XML parsing")
    public ValidationMessageList getParserErrors() {
        return parserErrors;
    }

    /**
     * Get the fatal errors found during XML parsing.
     * 
     * @return the parser fatal error messages
     */
    @ReportableProperty(order = 3, value = "Fatal errors found during XML parsing")
    public ValidationMessageList getFatalParserErrors() {
        return fatalParserErrors;
    }

    /**
     * Add a parser warning message to the list.
     * 
     * @param exception
     *            the exception object containing the warning message
     */
    protected void addParserWarning(SAXParseException exception) {
        parserWarnings.addMessage(exception);
    }

    /**
     * Add a parser error message to the list.
     * 
     * @param exception
     *            the exception object containing the error message
     */
    protected void addParserError(SAXParseException exception) {
        isValid = Validity.False;
        parserErrors.addMessage(exception);
    }

    /**
     * Add a parser fatal error message to the list.
     * 
     * @param exception
     *            the exception object containing the fatal error message
     */
    protected void addFatalError(SAXParseException exception) {
        isValid = Validity.False;
        fatalParserErrors.addMessage(exception);
    }

    /**
     * Gets the validity.
     * 
     * @return the validity object
     */
    public Validity getValidity() {
        return this.isValid;
    }

    /**
     * A data structure to hold the list of messages of a specific message scope.
     */
    public class ValidationMessageList extends AbstractReportable {

        /** The validation message list. */
        protected List<ValidationMessage> validationMessages;

        /**
         * Adds the message.
         * 
         * @param exception
         *            the exception object containing the message
         */
        protected void addMessage(SAXParseException exception) {
            if (validationMessages == null) {
                validationMessages = new ArrayList<ValidationMessage>();
            }
            validationMessages.add(new ValidationMessage(exception));
        }

        /**
         * Gets the validation message count.
         * 
         * @return the validation message count
         */
        @ReportableProperty(order = 1, value = "Number of messages")
        public int getValidationMessageCount() {
            if (validationMessages == null) {
                return 0;
            }
            else {
                return validationMessages.size();
            }
        }

        /**
         * Gets the validation messages.
         * 
         * @return the validation message list
         */
        @ReportableProperty(order = 2, value = "List of messages")
        public List<ValidationMessage> getValidationMessages() {
            return validationMessages;
        }
    }

    /**
     * A data structure to hold a single parser exception message.
     */
    public class ValidationMessage extends AbstractReportable {

        /** detail message from the SAXParseException. */
        protected String message;

        /** The line number at which the SAXParseException occurred, or -1. */
        protected int lineNumber;

        /** The column number at which the SAXParseException occurred, or -1. */
        protected int columnNumber;

        /**
         * Instantiates a new validation message.
         * 
         * @param exception
         *            the exception object containing the message and document
         *            location
         */
        public ValidationMessage(SAXParseException exception) {
            this.message = exception.getMessage();
            this.lineNumber = exception.getLineNumber();
            this.columnNumber = exception.getColumnNumber();
        }

        /**
         * Gets the message text.
         * 
         * @return the message text
         */
        @ReportableProperty(order = 1, value = "SAXParseException message")
        public String getMessage() {
            return message;
        }

        /**
         * Gets the line number of the document location.
         * 
         * @return the line number of the document location
         */
        @ReportableProperty(order = 2, value = "Line number at which the SAXParseException occurred")
        public int getLineNumber() {
            return lineNumber;
        }

        /**
         * Gets the column number of the document location.
         * 
         * @return the column number of the document location
         */
        @ReportableProperty(order = 3, value = "Column number at which the SAXParseException occurred")
        public int getColumnNumber() {
            return columnNumber;
        }

    }

}
