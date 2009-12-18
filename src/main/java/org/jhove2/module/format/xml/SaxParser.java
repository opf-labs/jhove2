package org.jhove2.module.format.xml;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.AbstractReportable;
import org.jhove2.core.JHOVE2Exception;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class SaxParser extends AbstractReportable {
	protected String parser;
	protected Map<String, String> features;
	protected XMLReader xmlReader;
	protected XmlModule xmlModule;

	/**
	 * This property is optionally set by the Spring config file that
	 * instantiates the module
	 * 
	 * @param saxParser
	 */
	public void setParser(String parser) {
		this.parser = parser;
	}

	/**
	 * @return saxParser
	 */
	@ReportableProperty(order = 1, value = "Java class used to parse the XML")
	public String getParser() {
		return parser;
	}

	public void setFeatures(Map<String, String> features) {
		this.features = features;
	}

	@ReportableProperty(order = 2, value = "SAX Parser Feature Settings")
	public List<String> getSaxFeatures() {
		ArrayList<String> features = new ArrayList<String>();
		for (Entry<String, String> entry : this.features.entrySet()) {
			features.add(entry.getKey() + " = " + entry.getValue());
		}
		return features;
	}

	protected void setXmlModule(XmlModule xmlModule) {
		this.xmlModule = xmlModule;
	}

	protected XMLReader getXmlReader() throws JHOVE2Exception {
		if (xmlReader == null) {
			createXmlReader();
			customizeXmlReaderFeatures();
			specifyXmlReaderHandlers();
		}
		return xmlReader;
	}

	private void createXmlReader() throws JHOVE2Exception {
		try {
			if (parser != null) {
				// Sax Parser class name has been specified in the Spring config
				// file
				xmlReader = XMLReaderFactory.createXMLReader(parser);
			} else {
				// Sax Parser class name will be determined from value of
				// org.xml.sax.driver
				// set as an environmental variable or a META-INF value from a
				// jar file in the classpath
				xmlReader = XMLReaderFactory.createXMLReader();
				parser = xmlReader.getClass().getName();
			}
		} catch (SAXException e) {
			throw new JHOVE2Exception("Could not create a SAX parser", e);
		}
	}

	private void customizeXmlReaderFeatures() {
		if (features != null) {
			for (Entry<String, String> entry : features.entrySet()) {
				try {
					xmlReader.setFeature(entry.getKey(), Boolean
							.parseBoolean(entry.getValue()));
				} catch (SAXNotRecognizedException e) {
					entry.setValue("Feature not recognized by parser");
				} catch (SAXNotSupportedException e) {
					entry.setValue("Feature not supported by parser");
				}
			}
		}
	}

	private void specifyXmlReaderHandlers() {
		XmlParserContentHandler contentHandler = 
			new XmlParserContentHandler(xmlReader, xmlModule);
		xmlReader.setContentHandler(contentHandler);
		XmlParserDtdHandler dtdHandler = new XmlParserDtdHandler(xmlModule);
		xmlReader.setDTDHandler(dtdHandler);
		XmlParserErrorHandler xmlParserErrortHandler = new XmlParserErrorHandler(
				xmlModule);
		xmlReader.setErrorHandler(xmlParserErrortHandler);
	}

}
