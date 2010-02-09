/**
 * JHOVE2 - Next-generation architecture for format-aware characterization
 *
 * Copyright (c) 2009 by The Regents of the University of California,
 * Ithaka Harbors, Inc., and The Board of Trustees of the Leland Stanford
 * Junior University.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * o Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 *
 * o Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * o Neither the name of the University of California/California Digital
 *   Library, Ithaka Harbors/Portico, or Stanford University, nor the names of
 *   its contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
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
 *
 * Created for JHOVE2 2009.09.12
 * from static methods in original uk.gov.nationalarchives.droid.AnalysisController
 * Please see the file DROID-LICENSE.txt in the JHOVE2 distribution for a complete statement
 *  of the BSD license rights governing the use of DROID source code.
 *  
 *  2010.02.09 S. Morrissey Modified to suppress re-write of DROID config file 
 */
package uk.gov.nationalarchives.droid;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import uk.gov.nationalarchives.droid.JHOVE2IAnalysisController;
import uk.gov.nationalarchives.droid.signatureFile.FFSignatureFile;
import uk.gov.nationalarchives.droid.xmlReader.SAXModelBuilder;

/**
 * Makes static utility methods out of some non-static classes from
 * uk.gov.nationalarchives.droid.AnalysisController to make it possible
 * to cache both the configuration file and the signature file
 * 
 * Some minor adaptations were made to those methods as required
 *  
 * @author smorrissey
 *
 */
public class JHOVE2AnalysisControllerUtil {
	/*
	 * Created a date object with value of date in format yyyy-MM-ddTHH:mm:ss (e.g. 2005-02-24T12:35:23)
	 *
	 * @param XMLFormatDate date in format yyyy-MM-ddTHH:mm:ss
	 * @return date with value set
	 * @throws java.text.ParseException on error
	 */
	public static java.util.Date parseXMLDate(String XMLFormatDate) throws java.text.ParseException {
		SimpleDateFormat xmlDateFormat = new SimpleDateFormat(JHOVE2IAnalysisController.XML_DATE_FORMAT);
		return xmlDateFormat.parse(XMLFormatDate);
	}

	/**
	 * Creates an XML format date yyyy-MM-ddTHH:mm:ss from a date object.
	 * <p/>
	 * For example, 2005-02-24T12:35:23
	 *
	 * @param aDate Date to represent
	 * @return Date in formatyyyy-MM-ddTHH:mm:ss (e.g. 2005-02-24T12:35:23)
	 */
	public static String writeXMLDate(java.util.Date aDate) {
		SimpleDateFormat xmlDateFormat = new SimpleDateFormat(JHOVE2IAnalysisController.XML_DATE_FORMAT);
		return xmlDateFormat.format(aDate);
	}

	/**
	 * Return the version of the uk application
	 *
	 * @return string
	 */
	public static String getDROIDVersion() {
		String theVersion = JHOVE2IAnalysisController.DROID_VERSION;

		//remove number after last .  This is a development version, not to be displayed in About box
		int theLastDot = theVersion.lastIndexOf(".");
		if (theLastDot > -1) {
			if (theVersion.indexOf(".") < theLastDot) {
				theVersion = theVersion.substring(0, theLastDot);
			}
		}
		return theVersion;
	}
	/**
	 * Loads the ConfigurationFile object with information from file 
	 * @param theFileName  path to XML file containing configuration information
	 * @return ConfigurationFile object
	 * @throws Exception
	 */
	public static ConfigFile loadConfigFile(String theFileName)throws Exception {
		ConfigFile configFile = null;
		checkFile(theFileName);
		//prepare for XML read
		MessageDisplay.resetXMLRead();
		//prepare to read in the XML file
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setNamespaceAware(true);
		SAXParser saxParser = factory.newSAXParser();
		XMLReader parser = saxParser.getXMLReader();
		SAXModelBuilder mb = new SAXModelBuilder();
		mb.setObjectPackage("uk.gov.nationalarchives.droid");
		mb.setupNamespace(JHOVE2IAnalysisController.CONFIG_FILE_NS, true);
		parser.setContentHandler(mb);

		//read in the XML file
		java.io.BufferedReader in = new java.io.BufferedReader(
				new java.io.InputStreamReader(
						new java.io.FileInputStream(theFileName), "UTF8"));
		parser.parse(new InputSource(in));
		configFile = (ConfigFile) mb.getModel();
		configFile.setFileName(theFileName);

		//let the user know the outcome if there were any problems
		int numXMLWarnings = MessageDisplay.getNumXMLWarnings();
		if (numXMLWarnings > 0) {
			String successMessage = "The configuration file " + theFileName;
			successMessage += " contained " + numXMLWarnings + " warning(s)";
			MessageDisplay.generalWarning(successMessage);
		}
		return configFile;
	}

	/**
	 * Parses and loads the DROID signature file
	 * Modified 2010.02.09 to supprress rewrite of config file
	 * @param configFile ConfigurationFile object
	 * @param theSigFileName  path to DROID signataure file
	 * @return loaded FFSignatureFile object
	 * @throws Exception
	 */
	public static FFSignatureFile loadSigFile(ConfigFile configFile, String theSigFileName) 
	throws Exception{
		FFSignatureFile sigFile = null;
		//checks that the file exists, throws an error if it doesn't
		checkFile(theSigFileName);
		//store the name of the new signature file
		configFile.setSigFile(theSigFileName);
		//prepare for XML read
		MessageDisplay.resetXMLRead();
		//carry out XML read
		sigFile = parseSigFile(theSigFileName);
		sigFile.prepareForUse();
		String theVersion = sigFile.getVersion();
		try {
			configFile.setSigFileVersion(theVersion);
		} catch (Exception e) {
			throw e;
		}
		int numXMLWarnings = MessageDisplay.getNumXMLWarnings();
		if (numXMLWarnings > 0) {
			String successMessage = "The signature file " + theSigFileName + 
			" was loaded with " + numXMLWarnings + " warnings";
			String cmdlineMessage = numXMLWarnings + " warnings were found";
			MessageDisplay.generalInformation(successMessage, cmdlineMessage);		}
//			{
//				//update the configuration file to contain the details of this signature file
//				try {
//					configFile.saveConfiguration();
//				} catch (IOException e) {
//					MessageDisplay.generalWarning("Unable to save configuration updates");
//				}
//			}
			return sigFile;
	}

	/**
	 * Checks that a file name corresponds to a file that exists and can be opened
	 *
	 * @param theFileName file name
	 * @throws Exception on error
	 */
	public static void checkFile(String theFileName) throws Exception {

		java.io.File file = new java.io.File(theFileName);
		if (!file.exists()) {
			throw new Exception("The file " + theFileName + " does not exist");
		} else if (!file.canRead()) {
			throw new Exception("The file " + theFileName + " cannot be read");
		} else if (file.isDirectory()) {
			throw new Exception("The file " + theFileName + " is a directory");
		}
	}

	/**
	 * Create a new signature file object based on a signature file
	 *
	 * @param theFileName the file name
	 * @return sig file
	 * @throws Exception on error
	 */
	public static FFSignatureFile parseSigFile(String theFileName) throws Exception {
		SAXModelBuilder mb = new SAXModelBuilder();
		XMLReader parser = getXMLReader(mb);

		//read in the XML file
		java.io.BufferedReader in = new java.io.BufferedReader
		(new java.io.InputStreamReader
				(new FileInputStream(theFileName), "UTF8"));
		parser.parse(new InputSource(in));
		return (FFSignatureFile) mb.getModel();
	}

	/**
	 * Create the XML parser for the signature file
	 *
	 * @param mb sax builder
	 * @return XMLReader
	 * @throws Exception on error
	 */
	public static XMLReader getXMLReader(SAXModelBuilder mb) throws Exception {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setNamespaceAware(true);
		SAXParser saxParser = factory.newSAXParser();
		XMLReader parser = saxParser.getXMLReader();
		mb.setupNamespace(JHOVE2IAnalysisController.SIGNATURE_FILE_NS, true);
		parser.setContentHandler(mb);
		return parser;
	}

}
