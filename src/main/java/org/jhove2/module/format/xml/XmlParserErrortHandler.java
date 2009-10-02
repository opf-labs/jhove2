package org.jhove2.module.format.xml;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/** A Handler for capturing and storing SAXParseException events generated during XML Validation */
public class XmlParserErrortHandler extends DefaultHandler {

	/** The module that is doing the parsing also contains the data structures that store the reportable data */
	private XmlModule xmlModule;
	
	/** Initializer */ 
	public XmlParserErrortHandler(XmlModule xmlModule) {
		this.xmlModule = xmlModule;
	}

	/** capture details of conditions that are not errors or fatal errors */
	public void warning(SAXParseException spe) throws SAXException {
		xmlModule.xmlValidationResults.addParserWarning(spe);
	}

	/** capture details of conditions that correspond to the definition of "error" 
	 * in section 1.2 of the W3C XML 1.0 Recommendation. 
     */
	public void error(SAXParseException spe) throws SAXException {
		xmlModule.xmlValidationResults.addParserError(spe);
	}

	/** capture details of conditions that correspond to the definition of "fatal error" 
	 * in section 1.2 of the W3C XML 1.0 Recommendation. 
     */
	public void fatalError(SAXParseException spe) throws SAXException {
		xmlModule.xmlValidationResults.addFatalError(spe);
	}
	
}
