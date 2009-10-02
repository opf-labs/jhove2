package org.jhove2.module.format.xml;

import java.util.ArrayList;
import java.util.List;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.AbstractReportable;
import org.jhove2.module.format.Validator.Validity;
import org.xml.sax.SAXParseException;

/** A data structure to hold warning and error messages capture during XML validation */
public class XmlValidationResults extends AbstractReportable {
	
	/** XML validity status. */
	protected Validity isValid = Validity.True;
	
	/** Warnings found during XML parsing */
	protected XmlValidationMessageList parserWarnings = new XmlValidationMessageList();
	/** Errors found during XML parsing */
	protected XmlValidationMessageList parserErrors = new XmlValidationMessageList();
	/** Fatal errors found during XML parsing */
	protected XmlValidationMessageList fatalParserErrors = new XmlValidationMessageList();
	
	/** Warnings found during XML parsing */
	@ReportableProperty(order = 1, value = "Warnings found during XML parsing")
	public XmlValidationMessageList getParserWarnings() {
		return parserWarnings;
	}
	
	/** Errors found during XML parsing */
	@ReportableProperty(order = 2, value = "Errors found during XML parsing")
	public XmlValidationMessageList getParserErrors() {
		return parserErrors;
	}
	
	/** Fatal errors found during XML parsing */
	@ReportableProperty(order = 3, value = "Fatal errors found during XML parsing")
	public XmlValidationMessageList getFatalParserErrors() {
		return fatalParserErrors;
	}
			
	/** Add a parser warning message to the list */
	protected void addParserWarning(SAXParseException spe) {
		parserWarnings.addMessage(spe);
	}
	/** Add a parser error message to the list */
	protected void addParserError(SAXParseException spe) {
		isValid = Validity.False;
		parserErrors.addMessage(spe);
	}
	/** Add a parser fatal error message to the list */
	protected void addFatalError(SAXParseException spe) {
		isValid = Validity.False;
		fatalParserErrors.addMessage(spe);
	}
	
	public Validity getValidity() {
		return this.isValid;
	}

	/** A data structure to hold the list of messages of a specific message type */
	public class XmlValidationMessageList extends AbstractReportable {
		protected List<XmlValidationMessage> validationMessages;
		
		protected void addMessage(SAXParseException spe) {
			if (validationMessages == null) {
				validationMessages = new ArrayList<XmlValidationMessage>();				
			}
			validationMessages.add(new XmlValidationMessage(spe));
		}
		
		@ReportableProperty(order = 1, value = "Number of messages")
		public int getValidationMessageCount(){
			if (validationMessages == null) {
				return 0;
			} else {
				return validationMessages.size();
			}
		}

		@ReportableProperty(order = 2, value = "List of messages")
		public List<XmlValidationMessage> getValidationMessages() {
			return validationMessages;
		}		
	}
	
	/** A data structure to hold a single message */
	public class XmlValidationMessage extends AbstractReportable {
		/** 
		 * detail message from the SAXParseException
		 */
		protected String message;
		
	    /**
	     *  The line number at which the SAXParseException occurred, or -1.
	     */
		protected int lineNumber;

	    /**
	     * The column number at which the SAXParseException occurred, or -1.
	     */
		protected int columnNumber;
		

		public XmlValidationMessage(SAXParseException spe) {
			this.message = spe.getMessage();
			this.lineNumber = spe.getLineNumber();
			this.columnNumber = spe.getColumnNumber();
		}

		@ReportableProperty(order = 1, value = "SAXParseException message")
		public String getMessage() {
			return message;
		}

		@ReportableProperty(order = 2, value = "Line number at which the SAXParseException occurred")
		public int getLineNumber() {
			return lineNumber;
		}

		@ReportableProperty(order = 3, value = "Column number at which the SAXParseException occurred")
		public int getColumnNumber() {
			return columnNumber;
		}
		

	}


}
