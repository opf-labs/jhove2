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


import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.reportable.AbstractReportable;
import org.xml.sax.SAXParseException;

import com.sleepycat.persist.model.Persistent;


/**
 * A data structure to hold warning and error messages captured during XML
 * validation.
 * 
 * @author rnanders
 */
@Persistent
public class ValidationResults extends AbstractReportable {

    /** Warnings found during XML parsing. */
    protected ValidationMessageList parserWarnings = new ValidationMessageList();

    /** Errors found during XML parsing. */
    protected ValidationMessageList parserErrors = new ValidationMessageList();

    /** Fatal errors found during XML parsing. */
    protected ValidationMessageList fatalParserErrors = new ValidationMessageList();
    
    protected ValidationResults(){
    	super();
    }

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
        parserErrors.addMessage(exception);
    }

    /**
     * Add a parser fatal error message to the list.
     * 
     * @param exception
     *            the exception object containing the fatal error message
     */
    protected void addFatalError(SAXParseException exception) {
        fatalParserErrors.addMessage(exception);
    }

}
