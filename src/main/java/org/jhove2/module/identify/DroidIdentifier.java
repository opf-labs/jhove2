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
 */

package org.jhove2.module.identify;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.FormatIdentification;
import org.jhove2.core.I8R;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.Message;
import org.jhove2.core.WrappedProductInfo;
import org.jhove2.core.FormatIdentification.Confidence;
import org.jhove2.core.Message.Context;
import org.jhove2.core.Message.Severity;
import org.jhove2.core.app.Invocation;
import org.jhove2.core.reportable.ReportableFactory;
import org.jhove2.core.source.Source;
import org.jhove2.module.AbstractModule;


import uk.gov.nationalarchives.droid.JHOVE2IAnalysisController;
import uk.gov.nationalarchives.droid.IdentificationFile;
import uk.gov.nationalarchives.droid.FileFormatHit;
import uk.gov.nationalarchives.droid.signatureFile.FileFormat;
import uk.gov.nationalarchives.droid.ConfigFile;
import uk.gov.nationalarchives.droid.signatureFile.FFSignatureFile;

/**
 * Identifier that wraps the DROID identification tool
 * Note that this wrapper is using a "light" version of DROID --
 * a modified version of the DROID code which has been stripped of
 * DROID GUI, command line, reporting, and profiling capabilities,
 * and merely uses the API to run the DROID identification engine
 * against a file
 * 
 * @author smorrissey
 */
public class DroidIdentifier
	extends AbstractModule
	implements Identifier
{
	/** Framework version identifier. */
	public static final String VERSION = "1.0.0";

	/** Framework release date. */
	public static final String RELEASE = "2009-09-05";

	/** Framework rights statement. */
	public static final String RIGHTS = "Copyright 2009 by The Regents of the University of California, "
		+ "Ithaka Harbors, Inc., and The Board of Trustees of the Leland "
		+ "Stanford Junior University. "
		+ "Available under the terms of the BSD license.";
	
	/** DROID product name */
	public static final String DROID_NAME = "DROID (Digital Record Object Identification)";

	/** Bean name for bean for properties file mapping PUIDs to JHOVE2 format identifiers */
	public static final String PUIDMAP_BEANNAME = "DroidMap";

	/** Bean name for bean for properties file mapping JHOVE2 format identifiers to JHOVE2 format bean names */
	public static final String JHOVE2BEANMAP_BEANNAME = "FormatBeanMap";

	/** Bean name for bean for properties file mapping PUIDs to JHOVE2 format identifiers */
	public static final String DROIDCONFIG_BEANNAME = "DroidConfigMap";
	
	/** System property name for DROID home directory path */
	public static final String DROID_HOME_ENV = "droid.home";

	/** default DROID directory name */
	public static final String DEFAULT_DROID_DIRNAME = "droid";
	
	public static final String PROP_CONFIG_FILE_NAME = "file.droid.config";
		
	public static final String PROP_SIG_FILE_NAME = "file.droid.sig";

	/** File not identified message (returned by DROID). */
	protected Message fileNotIdentifiedMessage;

	/** File not run message(returned by DROID). */
	protected Message fileNotRunMessage;

	/** File Error Message(returned by DROID). */
	protected Message fileErrorMessage;


	/** Path to directory containing DROID config and sig files */
	private static String droidHomePath = null;

	/** DROID Configuration file name */
	private static String configFileName = null;
	
	/** DROID signature file name */
	private static String sigFileName = null;

	private static ConcurrentMap<String, String> droidFilePaths;
	
	/** map from DROID PUIDs to JHOVE2 format ids */
	private static ConcurrentMap<String, String> puidToJhoveId;

	/** static member to cache parsed droid config file */
	private static ConfigFile cachedConfigFile = null;

	/** static member to cache parsed droid signature file */
	private static FFSignatureFile cachedSigFile = null;

	/**Instantiate a new <code>DROIDIdentifier</code> module.
	 * Uses static values for version, release date, and rights Strings
	 */
	public DroidIdentifier(){
		this(VERSION, RELEASE, RIGHTS);		
	}

	/**
	 * Instantiate a new <code>DROID</code> module.
	 * 
	 * @param version
	 *            DROID version identifier
	 * @param date
	 *            DROID release date
	 * @param rights
	 *            DROID rights statement
	 */
	public DroidIdentifier(String version, String date, String rights) {
		super(version, date, rights);	
		this.setWrappedProduct(new WrappedProductInfo(DROID_NAME));
	}

	/**
	 * If DROID config file has not yet been parsed, parse and return it;
	 * Otherwise simply returns parsed config file
	 * @param configFilePath path to DROID confi file
	 * @return parsed config file object
	 * @throws Exception
	 */
	private static synchronized ConfigFile getCachedConfigFile(String configFilePath)
		throws Exception
	{
		if (cachedConfigFile == null) {
			cachedConfigFile = DroidWrappedProduct.parseConfigFile(configFilePath);
		}
		return cachedConfigFile;
	}

	/**
	 * If DROID signature file has not yet been parsed, parses and returns it;
	 * Otherwise, just returns parsed signature file
	 * @param configFile  parsed DROID config file object
	 * @param sigFilePath  path to DROID signature file
	 * @return parsed signature file contents
	 * @throws Exception
	 */
	private static synchronized FFSignatureFile getCachedSignatureFile(ConfigFile configFile,
			                                                           String sigFilePath)
		throws Exception
	{
		if (cachedSigFile==null) {
			cachedSigFile = DroidWrappedProduct.parseSignatureFile(configFile,
					sigFilePath);
		}
		return cachedSigFile;
	}

	@Override
	public Set<FormatIdentification> identify(JHOVE2 jhove2, Source source)
	throws IOException, JHOVE2Exception {
		DroidWrappedProduct droid;
		Set<FormatIdentification> presumptiveFormatIds =
			new TreeSet<FormatIdentification>();
		try {
			ConfigFile configFile = getCachedConfigFile(this.getConfigFilePath());
			FFSignatureFile sigFile = getCachedSignatureFile(configFile, this.getSigFilePath());
			droid = new DroidWrappedProduct(configFile, sigFile);
			IdentificationFile idf = droid.identify(source);
			boolean matchFound = this.matchFound(idf);
			if (matchFound){
				String msgText = idf.getWarning();				
				Message idWarningMessage = null;
				if (msgText != null && msgText.length()>0){
					Object[]messageParms = new Object[]{msgText};
					idWarningMessage = new Message(Severity.WARNING,
							Context.OBJECT,
							"org.jhove2.module.identify.DroidIdentifier.identify.idWarningMessage",
							messageParms);
				}
				for (int i=0; i<idf.getNumHits(); i++){		
					ArrayList<Message> idMessages = new ArrayList<Message>();
					ArrayList<Message> unmatchedPuidMessages = 
						new ArrayList<Message>();
					Message hitWarningMsg = null;
					if (idWarningMessage != null){
						idMessages.add(idWarningMessage);
					}
					FileFormatHit ffh = idf.getHit(i);
					String hitWarning = ffh.getHitWarning();
					if (hitWarning != null && hitWarning.length()>0){
						Object[]messageParms = new Object[]{hitWarning};
						hitWarningMsg = new Message(Severity.WARNING,
								Context.OBJECT,
								"org.jhove2.module.identify.DroidIdentifier.identify.hitWarningMsg",
								messageParms);
						idMessages.add(hitWarningMsg);
					}
					FileFormat ff = ffh.getFileFormat();				
					String puid = ff.getPUID();
					I8R droidId = new I8R(puid, I8R.Namespace.PUID);
					Confidence jhoveConfidence = this.getJhoveConfidence(ffh);	
					// look up the JHOVE2 format id corresponding to DROID format id (PUID)
					String jhoveFormatId = null;
					if (! getPuidToJhoveId().containsKey(puid)){
						// if there is no match, attach an ERROR message to the FormatIdentification, 
						// and use the default JHOVE2 format
						Object[]messageParms = new Object[]{puid};
						Message missingPuid = new Message(Severity.ERROR,
								Context.PROCESS,
								"org.jhove2.module.identify.DroidIdentifier.identify.missingPuid",
								messageParms);
						unmatchedPuidMessages.add(missingPuid);
					}
					else {
						jhoveFormatId = getPuidToJhoveId().get(puid);	
					}
					idMessages.addAll(unmatchedPuidMessages);
					I8R jhoveId = null;
					if (jhoveFormatId != null) {
						jhoveId = new I8R(jhoveFormatId);
					}
					FormatIdentification fi = new FormatIdentification(jhoveId, jhoveConfidence,
							this.getReportableIdentifier(), droidId, idMessages);
					presumptiveFormatIds.add(fi);					
				}
			}
		} catch (IOException e) {
			throw e;
		} catch (Exception ex){
			throw new JHOVE2Exception("Failure running DROID " + ex.getMessage(),ex);
		}
		return presumptiveFormatIds;
	}

	/**
	 * Load DROID PUID to JHOVE2 Format Identifier map from properties file if map 
	 * is not already populated and returns map; otherwise just returns map
	 * @return DROID PUID to JHOVE2 Format Identifier map
	 * @throws JHOVE2Exception
	 */
	public static ConcurrentMap<String,String> getPuidToJhoveId () throws JHOVE2Exception{
		if (puidToJhoveId==null){
			ConcurrentHashMap<String, String> map = new ConcurrentHashMap<String, String>();
			Properties props = null;
			props = ReportableFactory.getProperties(PUIDMAP_BEANNAME);
			Set<String> keys = props.stringPropertyNames();
			for (String key : keys) {
				String value = props.getProperty(key);
				map.put(key, value);
			}
			puidToJhoveId = map;
		}
		return puidToJhoveId;
	}
	
	private static ConcurrentMap<String,String> getDroidFilePaths()
		throws JHOVE2Exception
	{
		if (droidFilePaths == null){
			ConcurrentHashMap<String, String> map = new ConcurrentHashMap<String, String>();
			Properties props = null;
			props = ReportableFactory.getProperties(DROIDCONFIG_BEANNAME);
			Set<String> keys = props.stringPropertyNames();
			for (String key : keys) {
				String value = props.getProperty(key);
				map.put(key, value);
			}
			droidFilePaths = map;
		}
		return droidFilePaths;
	}

	/**
	 * Checks DROID file classification codes to see if DROID was able to match file
	 * NOTE:  SIDE EFFECTS:  if there are errors, or if no identification can be made, or
	 *                       if DROID returns warning message, this method populates 
	 *                       the relevant Message member of this object instance
	 * @param idf DROID {@link uk.gov.nationalarchives.droid.IdentificationFile} object
	 * @return true if DROID able to identify file; otherwise false
	 * @throws JHOVE2Exception 
	 */
	protected boolean matchFound(IdentificationFile idf)
		throws JHOVE2Exception
	{
		boolean matchFound = false;
		int classification = idf.getClassification();
		String msgText = null;
		switch (classification){
		case JHOVE2IAnalysisController.FILE_CLASSIFICATION_NOHIT:
			msgText = idf.getWarning();
			if (msgText==null){
				msgText = new String("");
			}
			Object[]messageParms = new Object[]{msgText};
			this.fileNotIdentifiedMessage = new Message(Severity.WARNING,
					Context.OBJECT,
					"org.jhove2.module.identify.DroidIdentifier.fileNotIdentifiedMessage",
					messageParms);
			break;
		case JHOVE2IAnalysisController.FILE_CLASSIFICATION_NOTCLASSIFIED:
			msgText = idf.getWarning();
			if (msgText==null){
				msgText = new String("");
			}
			messageParms = new Object[]{msgText};
			this.fileNotRunMessage = new Message(Severity.ERROR,
					Context.PROCESS,
					"org.jhove2.module.identify.DroidIdentifier.fileNotRunMessage",
					messageParms);
			break;
		case JHOVE2IAnalysisController.FILE_CLASSIFICATION_ERROR:
			msgText = idf.getWarning();
			if (msgText==null){
				msgText = new String("");
			}
			messageParms = new Object[]{msgText};
			this.fileErrorMessage = new Message(Severity.ERROR,
					Context.PROCESS,
					"org.jhove2.module.identify.DroidIdentifier.fileErrorMessage",
					messageParms);
			break;
		default:
			matchFound = true;
			break;
		}// end switch
		return matchFound;
	}
	
	/**
	 * Map from DROID confidence levels to JHOVE2 confidence levels
	 * @param ffh File format hit containing DROID confidence level
	 * @return JHOVE2 confidence level for this identification
	 */
	protected Confidence getJhoveConfidence(FileFormatHit ffh) {
		int droidConfidence = ffh.getHitType();
		Confidence jhoveConfidence;
		switch (droidConfidence){
		case JHOVE2IAnalysisController.HIT_TYPE_POSITIVE_SPECIFIC:
			jhoveConfidence = Confidence.PositiveSpecific;
			break;
		case JHOVE2IAnalysisController.HIT_TYPE_POSITIVE_GENERIC_OR_SPECIFIC:
			jhoveConfidence = Confidence.PositiveGeneric;
			break;
		case JHOVE2IAnalysisController.HIT_TYPE_POSITIVE_GENERIC:
			jhoveConfidence = Confidence.PositiveGeneric;
			break;
		case JHOVE2IAnalysisController.HIT_TYPE_TENTATIVE:
			jhoveConfidence = Confidence.Tentative;
			break;
		default:
			jhoveConfidence = Confidence.Negative;
		}	
		return jhoveConfidence;
	}
	
	/**
	 * Get DROID File Not Found message.
	 * 
	 * @return File Not Found message
	 */
	@ReportableProperty(order = 13, value = "DROID File Not Identified Message")
	public Message getFileNotIdentifiedMessage() {
		return this.fileNotIdentifiedMessage;
	}
	
	/**
	 * Get DROID File Not run message.
	 * 
	 * @return File Not run message
	 */
	@ReportableProperty(order = 14, value = "DROID File Not Run Message")
	public Message getFileNotRunMessage() {
		return fileNotRunMessage;
	}
	
	/**
	 * Get DROID File error message.
	 * 
	 * @return File error message
	 */
	@ReportableProperty(order = 15, value = "DROID File Error Message")
	public Message getFileErrorMessage() {
		return fileErrorMessage;
	}

	@ReportableProperty(order = 17, value = "DROID Config file path")
	public String getConfigFilePath() {
		return getDroidHomePath().concat(getConfigFileName());
	}

	@ReportableProperty(order = 18, value = "DROID Signature file path")
	public String getSigFilePath() {
		return getDroidHomePath().concat(getSigFileName());
	}

	public void setFileNotIdentifiedMessage(Message fileNotIdentifiedMessage) {
		this.fileNotIdentifiedMessage = fileNotIdentifiedMessage;
	}

	public void setFileNotRunMessage(Message fileNotRunMessage) {
		this.fileNotRunMessage = fileNotRunMessage;
	}

	public void setFileErrorMessage(Message fileErrorMessage) {
		this.fileErrorMessage = fileErrorMessage;
	}
	
	private static synchronized String getConfigFileName() {
		if (configFileName == null){
			try {
				configFileName = getDroidFilePaths().get(PROP_CONFIG_FILE_NAME);
			} catch (JHOVE2Exception e) {
				;
			}
		}
		return configFileName;
	}

	private static synchronized String getSigFileName() {
		if (sigFileName == null){
			try {
				sigFileName = getDroidFilePaths().get(PROP_SIG_FILE_NAME);
			} catch (JHOVE2Exception e) {
				;
			}
		}
		return sigFileName;
	}

	private static synchronized String getDroidHomePath() {
		if (droidHomePath == null){
			String separator = System.getProperty("file.separator");
			StringBuffer droidHome = new StringBuffer();
			String dHome;
			try {
				dHome = getDroidFilePaths().get(DROID_HOME_ENV);
			} catch (JHOVE2Exception e) {
				dHome = null;
			}	
			if (dHome == null){				
				dHome = Invocation.getJHOVE2HomeFromEnv(System.getProperties());	
				droidHome.append(dHome);
				if (dHome.lastIndexOf(separator) != dHome.length()-1){
					droidHome.append(separator);
				}
				droidHome.append(DEFAULT_DROID_DIRNAME);
				droidHome.append(separator);
			}
			else {
				droidHome.append(dHome);
				if (dHome.lastIndexOf(separator) != dHome.length()-1){
					droidHome.append(separator);
				}
			}
			droidHomePath = droidHome.toString();
		}		
		return droidHomePath;
	}
}
