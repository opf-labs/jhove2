package org.jhove2.module.format.xml;

import java.util.ArrayList;
import java.util.List;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.reportable.AbstractReportable;
import org.xml.sax.SAXParseException;

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