package org.jhove2.module.format.xml;

import java.util.ArrayList;
import java.util.List;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.reportable.AbstractReportable;
import org.jhove2.module.format.Validator.Validity;
import org.xml.sax.SAXParseException;

/** A data structure to hold warning and error messages capture during XML validation */
public class ValidationResults extends AbstractReportable {
	
	/** XML validity status. */
	protected Validity isValid = Validity.True;
	
	/** Warnings found during XML parsing */
	protected ValidationMessageList parserWarnings = new ValidationMessageList();
	/** Errors found during XML parsing */
	protected ValidationMessageList parserErrors = new ValidationMessageList();
	/** Fatal errors found during XML parsing */
	protected ValidationMessageList fatalParserErrors = new ValidationMessageList();
	
	/** Warnings found during XML parsing */
	@ReportableProperty(order = 1, value = "Warnings found during XML parsing")
	public ValidationMessageList getParserWarnings() {
		return parserWarnings;
	}
	
	/** Errors found during XML parsing */
	@ReportableProperty(order = 2, value = "Errors found during XML parsing")
	public ValidationMessageList getParserErrors() {
		return parserErrors;
	}
	
	/** Fatal errors found during XML parsing */
	@ReportableProperty(order = 3, value = "Fatal errors found during XML parsing")
	public ValidationMessageList getFatalParserErrors() {
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
	public class ValidationMessageList extends AbstractReportable {
		protected List<ValidationMessage> validationMessages;
		
		protected void addMessage(SAXParseException spe) {
			if (validationMessages == null) {
				validationMessages = new ArrayList<ValidationMessage>();				
			}
			validationMessages.add(new ValidationMessage(spe));
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
		public List<ValidationMessage> getValidationMessages() {
			return validationMessages;
		}		
	}
	
	/** A data structure to hold a single message */
	public class ValidationMessage extends AbstractReportable {
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
		

		public ValidationMessage(SAXParseException spe) {
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
