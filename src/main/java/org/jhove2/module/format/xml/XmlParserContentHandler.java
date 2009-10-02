package org.jhove2.module.format.xml;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.Locator2;
import org.xml.sax.helpers.DefaultHandler;

public class XmlParserContentHandler extends DefaultHandler {

	private XmlModule xmlModule;
	private XMLReader xmlReader;
	
    private Locator locator;
    private Locator2 locator2;

	
	public XmlParserContentHandler(XMLReader xmlReader, XmlModule xmlModule) {
		this.xmlReader = xmlReader;
		this.xmlModule = xmlModule;
	}
	
	/**
     * Receive a Locator object for document events.
      * @param locator A locator for all SAX document events.
     */
	@Override
    public void setDocumentLocator(Locator locator) {
		this.locator = locator;
        if (locator instanceof Locator2) {
            this.locator2 = (Locator2) locator;
        }
     }
    
	 /**
     * Receive notification of the beginning of the document.
     */
	@Override
    public void startDocument() {
        if (locator2 != null) {
        	xmlModule.xmlDeclaration.encoding = locator2.getEncoding();
        	xmlModule.xmlDeclaration.version = locator2.getXMLVersion();
        }
    }

	 /**
     * Receive notification of the beginning of the document.
     */
	@Override
    public void endDocument() {
         try {
        	boolean isStandalone = xmlReader.getFeature("http://xml.org/sax/features/is-standalone");
			xmlModule.xmlDeclaration.standalone = Boolean.toString(isStandalone);
			 		
		} catch (SAXException e) {
			xmlModule.xmlDeclaration.standalone = null;
		}
    }
	
	
	 /**
     *  Fires at the beginning of every element in the XML document
     */
	@Override
   public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException
    {
        // The first element we encounter is the root.
        // Save it.
        if (xmlModule.xmlRootElementName == null) {
            if (qName.equals("")) {
            	xmlModule.xmlRootElementName = localName;
            } else {
            	xmlModule.xmlRootElementName = qName;            	
            }
        }
    }   
	
}
