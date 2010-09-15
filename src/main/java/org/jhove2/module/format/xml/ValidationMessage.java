package org.jhove2.module.format.xml;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.reportable.AbstractReportable;
import org.xml.sax.SAXParseException;

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