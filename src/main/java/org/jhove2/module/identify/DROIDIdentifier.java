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
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentMap;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.app.util.FeatureConfigurationUtil;
import org.jhove2.core.I8R;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.Message;
import org.jhove2.core.Message.Context;
import org.jhove2.core.Message.Severity;
import org.jhove2.core.format.FormatIdentification;
import org.jhove2.core.format.FormatIdentification.Confidence;
import org.jhove2.core.io.Input;
import org.jhove2.core.source.Source;
import org.jhove2.persist.ModuleAccessor;

import com.sleepycat.persist.model.NotPersistent;
import com.sleepycat.persist.model.Persistent;


import uk.gov.nationalarchives.droid.JHOVE2IAnalysisController;
import uk.gov.nationalarchives.droid.IdentificationFile;
import uk.gov.nationalarchives.droid.FileFormatHit;
import uk.gov.nationalarchives.droid.signatureFile.FileFormat;
import uk.gov.nationalarchives.droid.ConfigFile;
import uk.gov.nationalarchives.droid.signatureFile.FFSignatureFile;

/**
 * Identifier that wraps the DROID identifier tool
 * Note that this wrapper is using a "light" version of DROID --
 * a modified version of the DROID code which has been stripped of
 * DROID GUI, command line, reporting, and profiling capabilities,
 * and merely uses the API to run the DROID identifier engine
 * against a file
 * 
 * @author smorrissey
 */
@Persistent
public class DROIDIdentifier
	extends AbstractFileSourceIdentifier
	implements SourceIdentifier
{
	/** Framework version identifier. */
	public static final String VERSION = "2.0.0";

	/** Framework release date. */
	public static final String RELEASE = "2010-09-10";

	/** Framework rights statement. */
	public static final String RIGHTS = "Copyright 2010 by The Regents of the University of California, "
		+ "Ithaka Harbors, Inc., and The Board of Trustees of the Leland "
		+ "Stanford Junior University. "
		+ "Available under the terms of the BSD license.";
	
	/** DROID product name */
	public static final String DROID_NAME = "DROID (Digital Record Object Identification)";

	/** Bean name for bean for properties file mapping PUIDs to JHOVE2 format identifiers */
	public static final String PUIDMAP_BEANNAME = "DroidMap";

	/** Bean name for bean for properties file mapping JHOVE2 format identifiers to JHOVE2 format bean names */
	public static final String JHOVE2BEANMAP_BEANNAME = "FormatBeanMap";

	/** DROID Configuration file name */
	private String configurationFileName = null;
	
	/** DROID signature file name */
	private String signatureFileName = null;
	
	/** map from DROID PUIDs to JHOVE2 format ids */
	@NotPersistent
	private static ConcurrentMap<String, String> puidToJhoveId;

	/** static member to cache parsed droid config file */
	@NotPersistent
	private static ConfigFile cachedConfigFile = null;

	/** static member to cache parsed droid signature file */
	@NotPersistent
	private static FFSignatureFile cachedSigFile = null;

	/**Instantiate a new <code>DROIDIdentifier</code> module that wraps DROID.
	 * @throws JHOVE2Exception 
	 */
	public DROIDIdentifier()
		throws JHOVE2Exception
	{
		this(null);
	}
	
	/**Instantiate a new <code>DROIDIdentifier</code> module that wraps DROID.
	 * @param moduleAccessor persistence manager
	 * @throws JHOVE2Exception 
	 */
	public DROIDIdentifier(ModuleAccessor moduleAccessor)
		throws JHOVE2Exception
	{
		this(null, null, moduleAccessor);
	}
	
	/**Instantiate a new <code>DROIDIdentifier</code> module that wraps DROID.
	 * @param configFileName path to DROID configuration file
	 * @param sigFileName path to DROID signature file
	 * @param moduleAccessor persistence manager
	 * @throws JHOVE2Exception 
	 */
	public DROIDIdentifier(String configFileName, String sigFileName, 
			ModuleAccessor moduleAccessor)
		throws JHOVE2Exception
	{
		super(VERSION, RELEASE, RIGHTS, Scope.Generic, moduleAccessor);
		this.setConfigFileName(configFileName);
		this.setSigFileName(sigFileName);
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
	public Set<FormatIdentification> identify(JHOVE2 jhove2, Source source, Input input)
		throws IOException, JHOVE2Exception
	{
		DROIDWrapper droid = new DROIDWrapper();
		Set<FormatIdentification> presumptiveFormatIds =
			new TreeSet<FormatIdentification>();
		try {
			ConfigFile configFile = getCachedConfigFile(this.getConfigurationFile());
			FFSignatureFile sigFile = getCachedSignatureFile(configFile, this.getSignatureFile());
			droid.setConfigFile(configFile);
			droid.setSigFile(sigFile);
			IdentificationFile idf = droid.identify(source);
			boolean matchFound = this.matchFound(idf, jhove2, source);
			if (matchFound){
				String msgText = idf.getWarning();				
				Message idWarningMessage = null;
				if (msgText != null && msgText.length()>0) {
					Object [] messageParms = new Object []{msgText};
					idWarningMessage = new Message(Severity.WARNING,
							Context.OBJECT,
							"org.jhove2.module.identify.DROIDIdentifier.identify.idWarningMessage",
							messageParms, jhove2.getConfigInfo());
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
								messageParms, jhove2.getConfigInfo());
						idMessages.add(hitWarningMsg);
					}
					FileFormat ff = ffh.getFileFormat();				
					String puid = ff.getPUID();
					I8R droidId = new I8R(puid, I8R.Namespace.PUID);
					Confidence jhoveConfidence = this.getJHOVE2Confidence(ffh);	
					// look up the JHOVE2 format id corresponding to DROID format id (PUID)
					String jhoveFormatId = null;
					if (! getPUIDtoJ2ID(jhove2).containsKey(puid)){
						// if there is no match, attach an ERROR message to the FormatIdentification, 
						// and use the default JHOVE2 format
						Object[]messageParms = new Object[]{puid};
						Message missingPuid = new Message(Severity.ERROR,
								Context.PROCESS,
								"org.jhove2.module.identify.DROIDIdentifier.identify.missingPUID",
								messageParms, jhove2.getConfigInfo());
						unmatchedPUIDMessages.add(missingPuid);
					}
					else {
						jhoveFormatId = getPUIDtoJ2ID(jhove2).get(puid);	
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
			cachedConfigFile = DROIDWrapper.parseConfigFile(configFilePath);
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
			cachedSigFile = DROIDWrapper.parseSignatureFile(configFile,
					                                               sigFilePath);
		}
		return cachedSigFile;
	}

    /**
     * Return the name of the configuration file.
     * @return Configuration file name
     */
    public String getConfigurationFileName() {
        return configurationFileName;
    }

    /** Get DROID configuration file path.
     * @return DROID configuration file path
     * @throws JHOVE2Exception 
     */
    @ReportableProperty(order = 1, value = "DROID configuration file path.")
    public String getConfigurationFile()
        throws JHOVE2Exception
    {
        String path = FeatureConfigurationUtil.getFilePathFromClasspath(this.getConfigurationFileName(), "DROID config file");
        return path;
    }

    
    /**
     * Map from DROID confidence levels to JHOVE2 confidence levels
     * @param ffh File format hit containing DROID confidence level
     * @return JHOVE2 confidence level for this identifier
     */
    protected Confidence getJHOVE2Confidence(FileFormatHit ffh) {
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
     * Gets the mapping from DROID PUID to JHOVE2 Format Identifier. 
     * Initializes the static map on first invocation.
     * 
     * @return DROID PUID to JHOVE2 Format Identifier map
     * @throws JHOVE2Exception
     */
    public static ConcurrentMap<String,String> getPUIDtoJ2ID(JHOVE2 jhove2)
        throws JHOVE2Exception
    {
        if (puidToJhoveId == null){
        	puidToJhoveId = jhove2.getConfigInfo().getFormatAliasIdsToJ2Ids(I8R.Namespace.PUID);
        }
        return puidToJhoveId;
    }
 
    /** Get DROID signature file path.
     * @return DROID signature file path
     * @throws JHOVE2Exception 
     */
    @ReportableProperty(order = 2, value = "DROID signature file path.")
    public String getSignatureFile() throws JHOVE2Exception {
        String path = FeatureConfigurationUtil.getFilePathFromClasspath(this.getSignatureFileName(), "DROID signature file");
        return path;
    }

    /**
     * Return the name of the signature file
     * @return the signatureFileName
     */
    public String getSignatureFileName() {
        return signatureFileName;
    }
 
	/**
	 * Checks DROID file classification codes to see if DROID was able to match file
	 * NOTE:  SIDE EFFECTS:  if there are errors, or if no identifier can be made, or
	 *                       if DROID returns warning message, this method populates 
	 *                       the relevant Message member of this object instance
	 * @param idf DROID {@link uk.gov.nationalarchives.droid.IdentificationFile} object
	 * @param jhove2 
	 * @return true if DROID able to identify file; otherwise false
	 * @throws JHOVE2Exception 
	 */
	protected boolean matchFound(IdentificationFile idf, JHOVE2 jhove2, Source source)
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
			Message fileNotIdentifiedMessage = new Message(Severity.WARNING,
					Context.OBJECT,
					"org.jhove2.module.identify.DROIDIdentifier.fileNotIdentifiedMessage",
					messageParms, jhove2.getConfigInfo());
			source.addMessage(fileNotIdentifiedMessage);
			break;
		case JHOVE2IAnalysisController.FILE_CLASSIFICATION_NOTCLASSIFIED:
			msgText = idf.getWarning();
			if (msgText==null){
				msgText = new String("");
			}
			messageParms = new Object[]{msgText};
			Message fileNotRunMessage = new Message(Severity.ERROR,
					Context.PROCESS,
					"org.jhove2.module.identify.DROIDIdentifier.fileNotRunMessage",
					messageParms, jhove2.getConfigInfo());
			source.addMessage(fileNotRunMessage);
			break;
		case JHOVE2IAnalysisController.FILE_CLASSIFICATION_ERROR:
			msgText = idf.getWarning();
			if (msgText==null){
				msgText = new String("");
			}
			messageParms = new Object[]{msgText};
			Message fileErrorMessage = new Message(Severity.ERROR,
					Context.PROCESS,
					"org.jhove2.module.identify.DROIDIdentifier.fileErrorMessage",
					messageParms, jhove2.getConfigInfo());
			source.addMessage(fileErrorMessage);
			break;
		default:
			matchFound = true;
			break;
		}// end switch
		return matchFound;
	}


	/**
	 * Set the name of the config file
	 * @param configurationFileName the configurationFileName to set
	 */
	public void setConfigFileName(String configFileName) {
		this.configurationFileName = configFileName;
	}

	/**
	 * Set the name of the signature file
	 * @param signatureFileName the signatureFileName to set
	 */
	public void setSigFileName(String sigFileName) {
		this.signatureFileName = sigFileName;
	}
}
