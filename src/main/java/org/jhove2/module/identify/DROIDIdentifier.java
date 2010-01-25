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
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.Format;
import org.jhove2.core.FormatIdentification;
import org.jhove2.core.I8R;
import org.jhove2.core.Invocation;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.Message;
import org.jhove2.core.WrappedProduct;
import org.jhove2.core.FormatIdentification.Confidence;
import org.jhove2.core.Message.Context;
import org.jhove2.core.Message.Severity;
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
public class DROIDIdentifier
	extends AbstractModule
	implements Identifier
{
	/** Framework version identifier. */
	public static final String VERSION = "0.5.4";

	/** Framework release date. */
	public static final String RELEASE = "2009-12-22";

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

	/** Default DROID directory name */
	public static final String DEFAULT_DROID_DIRNAME = "droid";
	
	/** DROID configuration file name. */
	public static final String PROP_CONFIG_FILE_NAME = "file.droid.config";
		
	/** DROID signature file name. */
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

	/**Instantiate a new <code>DROIDIdentifier</code> module that wraps DROID.
	 * @throws JHOVE2Exception 
	 */
	public DROIDIdentifier()
		throws JHOVE2Exception
	{
		super(VERSION, RELEASE, RIGHTS);
		WrappedProduct droid = ReportableFactory.getReportable(WrappedProduct.class,
				                                               "DROIDProduct");
		this.setWrappedProduct(droid);
	}
	
	/**
	 * Presumptively identify the format of a source unit.
	 * 
	 * @param jhove2
	 *            JHOVE2 framework
	 * @param source
	 *            Source unit
	 * @return Set of presumptive format identifications
	 * @throws IOException
	 *             I/O exception encountered identifying the source unit
	 * @throws JHOVE2Exception
	 */
	@Override
	public Set<FormatIdentification> identify(JHOVE2 jhove2, Source source)
		throws IOException, JHOVE2Exception
	{
		DROIDWrappedProduct droid;
		Set<FormatIdentification> presumptiveFormatIds =
			new TreeSet<FormatIdentification>();
		try {
			ConfigFile configFile = getCachedConfigFile(this.getConfigFilePath());
			FFSignatureFile sigFile = getCachedSignatureFile(configFile, this.getSigFilePath());
			droid = new DROIDWrappedProduct(configFile, sigFile);
			IdentificationFile idf = droid.identify(source);
			boolean matchFound = this.matchFound(idf);
			if (matchFound){
				String msgText = idf.getWarning();				
				Message idWarningMessage = null;
				if (msgText != null && msgText.length()>0) {
					Object [] messageParms = new Object []{msgText};
					idWarningMessage = new Message(Severity.WARNING,
							Context.OBJECT,
							"org.jhove2.module.identify.DROIDIdentifier.identify.idWarningMessage",
							messageParms);
				}
				for (int i=0; i<idf.getNumHits(); i++){		
					ArrayList<Message> idMessages = new ArrayList<Message>();
					ArrayList<Message> unmatchedPUIDMessages = 
						new ArrayList<Message>();
					Message hitWarningMsg = null;
					if (idWarningMessage != null){
						idMessages.add(idWarningMessage);
					}
					FileFormatHit ffh = idf.getHit(i);
					String hitWarning = ffh.getHitWarning();
					if (hitWarning != null && hitWarning.length()>0) {
						Object[]messageParms = new Object[]{hitWarning};
						hitWarningMsg = new Message(Severity.WARNING,
								Context.OBJECT,
								"org.jhove2.module.identify.DROIDIdentifier.identify.hitWarningMsg",
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
								"org.jhove2.module.identify.DROIDIdentifier.identify.missingPUID",
								messageParms);
						unmatchedPUIDMessages.add(missingPuid);
					}
					else {
						jhoveFormatId = getPuidToJhoveId().get(puid);	
					}
					idMessages.addAll(unmatchedPUIDMessages);
					I8R jhoveId = null;
					if (jhoveFormatId != null) {
						jhoveId = new I8R(jhoveFormatId);
					}
					FormatIdentification fi =
						new FormatIdentification(jhoveId, jhoveConfidence,
							                     this.getReportableIdentifier(),
							                     droidId, idMessages);
					presumptiveFormatIds.add(fi);					
				}
			}
		}
		catch (IOException e) {
			throw e;
		}
		catch (Exception ex) {
			throw new JHOVE2Exception("Failure running DROID " + ex.getMessage(),ex);
		}
		return presumptiveFormatIds;
	}

	/**
	 * If DROID config file has not yet been parsed, parse and return it;
	 * Otherwise simply returns parsed config file
	 * @param configFilePath path to DROID config file
	 * @return parsed config file object
	 * @throws Exception
	 */
	private static synchronized ConfigFile getCachedConfigFile(String configFilePath)
		throws Exception
	{
		if (cachedConfigFile == null) {
			cachedConfigFile = DROIDWrappedProduct.parseConfigFile(configFilePath);
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
		if (cachedSigFile == null) {
			cachedSigFile = DROIDWrappedProduct.parseSignatureFile(configFile,
					                                               sigFilePath);
		}
		return cachedSigFile;
	}

	/**
	 * Load DROID PUID to JHOVE2 Format Identifier map from properties file if map 
	 * is not already populated and returns map; otherwise just returns map
	 * @return DROID PUID to JHOVE2 Format Identifier map
	 * @throws JHOVE2Exception
	 */
	public static ConcurrentMap<String,String> getPuidToJhoveIdOld()
		throws JHOVE2Exception
	{
		if (puidToJhoveId == null){
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
	
    /**
     * Gets the mapping from DROID PUID to JHOVE2 Format Identifier. 
     * Initializes the static map on first invocation.
     * 
     * @return DROID PUID to JHOVE2 Format Identifier map
     * @throws JHOVE2Exception
     */
    public static ConcurrentMap<String,String> getPuidToJhoveId()
        throws JHOVE2Exception
    {
        if (puidToJhoveId == null){
            ConcurrentHashMap<String, String> map = new ConcurrentHashMap<String, String>();
            puidToJhoveId = map;
            /*
             * Use Spring to get instances of all objects inheriting from
             * BaseFormatModule
             */
            Map<String, Object> formatMap = ReportableFactory
                    .getObjectsForType(Format.class);
            /* For each of the formats */
            for (Entry<String, Object> entry : formatMap.entrySet()) {
                /* Get the format object */
                Format format = (Format) entry.getValue();
                /* Get the JHOVE format identifier for the format */
                I8R formatID = format.getIdentifier();
                /* For each aliasIdentifier of the format */
                for (I8R alias :  format.getAliasIdentifiers()) {
                    if (alias.getNamespace().equals(I8R.Namespace.PUID)) {
                        /* Add an entry into the format identifier to module map */
                        puidToJhoveId.put(alias.getValue(), formatID.getValue());
                        //System.out.println(alias.getValue() + " = " + formatID.getValue());
                    }
                }
            }
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
					"org.jhove2.module.identify.DROIDIdentifier.fileNotIdentifiedMessage",
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
					"org.jhove2.module.identify.DROIDIdentifier.fileNotRunMessage",
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
					"org.jhove2.module.identify.DROIDIdentifier.fileErrorMessage",
					messageParms);
			break;
		default:
			matchFound = true;
			break;
		}// end switch
		return matchFound;
	}

	/** Get DROID configuration file path.
	 * @return DROID configuration file path
	 */
	@ReportableProperty(order = 17, value = "DROID configuration file path.")
	public String getConfigFilePath() {
		return getDROIDHomePath().concat(getConfigFileName());
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
	@ReportableProperty(order = 15, value = "DROID File Error Message.")
	public Message getFileErrorMessage() {
		return fileErrorMessage;
	}

	/** Get DROID signature file path.
	 * @return DROID signature file path
	 */
	@ReportableProperty(order = 18, value = "DROID signature file path.")
	public String getSigFilePath() {
		return getDROIDHomePath().concat(getSigFileName());
	}
	
	/** Get DROID configuration file name.
	 * @return DROID configuration file name
	 */
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

	/** Get DROID home pathname.
	 * @return Get DROID home pathname
	 */
	private static synchronized String getDROIDHomePath() {
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

	/** Get DROID signature file name.
	 * @return DROID signature file name;
	 */
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

	/** Set File Error message.
	 * @param fileErrorMessage File Error message
	 */
	public void setFileErrorMessage(Message fileErrorMessage) {
		this.fileErrorMessage = fileErrorMessage;
	}
	
	/** Set File Not Identified message.
	 * @param fileNotIdentifiedMessage File Not Identified message
	 */
	public void setFileNotIdentifiedMessage(Message fileNotIdentifiedMessage) {
		this.fileNotIdentifiedMessage = fileNotIdentifiedMessage;
	}

	/** Set File Not Run message.
	 * @param fileNotRunMessage File Not Run message
	 */
	public void setFileNotRunMessage(Message fileNotRunMessage) {
		this.fileNotRunMessage = fileNotRunMessage;
	}
	
	/**
	 * Get file source identifier module.
	 * @return File source identifier module
	 */
	@Override
	public Identifier getFileSourceIdentifier() {
		return this;
	}
}
