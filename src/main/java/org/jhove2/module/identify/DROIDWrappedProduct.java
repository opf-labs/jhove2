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

import static uk.gov.nationalarchives.droid.binFileReader.AbstractByteReader.newByteReader;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.WrappedProductInfo;
import org.jhove2.core.source.FileSource;
import org.jhove2.core.source.Source;
import org.jhove2.core.source.URLSource;

import uk.gov.nationalarchives.droid.JHOVE2AnalysisControllerUtil;
import uk.gov.nationalarchives.droid.ConfigFile;
import uk.gov.nationalarchives.droid.IdentificationFile;
import uk.gov.nationalarchives.droid.JHOVE2IAnalysisController;
import uk.gov.nationalarchives.droid.binFileReader.ByteReader;
import uk.gov.nationalarchives.droid.signatureFile.FFSignatureFile;


/**
 * JHOVE2 wrapper around droid programming API, adapted for JHOVE2 from
 * {@link uk.gov.nationalarchives.droid.Droid}
 * Please see the file DROID-LICENSE.txt in the JHOVE2 distribution for a complete statement
 *  of the BSD license rights governing the use of DROID source code.
 *  
 *  @author smorrissey
 */
public class DROIDWrappedProduct extends WrappedProductInfo {

	public static final String NAME = "Jhove2Droid";
	
	/** Framework version identifier. */
	public static final String VERSION = "0.0.1";

	/** Framework release date. */
	public static final String RELEASE = "2009-09-12";

	/** Framework rights statement. */
	public static final String RIGHTS = "Extensions to DROID Copyright 2009 by The Regents of the University of California, "
		+ "Ithaka Harbors, Inc., and The Board of Trustees of the Leland "
		+ "Stanford Junior University. "
		+ "Available under the terms of the BSD license."
		+ " Please see DROID-LICENSE.txt file for UK National Archives rights statement for DROID,"
		+ " DROID is also available under the terms of the BSD license";
	
    protected DROIDAnalysisController analysisControl = null;
      
    /**
     * No-args constructor. 
     *
     * @throws Exception
     */
    public DROIDWrappedProduct() throws Exception {
    	this(NAME, VERSION, RELEASE, RIGHTS);
    }
    /**
     * Constructor
     * @param name Name of this wrapped product
     * @throws Exception
     */
    public DROIDWrappedProduct(String name) throws Exception {
    	this(name, VERSION, RELEASE, RIGHTS);
    }   
    
    /**
     * Cosntructor
     * @param name Name of this wrapped product
     * @param version Version  of this wrapped product
     * @param date Release date of this wrapped product
     * @param rights Rights statement of this wrapped product
     */
    public DROIDWrappedProduct(String name, String version, String date, String rights) {
    	super(name, version, date, rights);
    	analysisControl = new DROIDAnalysisController();
    }
    
    /**
     * Convenience constructor added by JHOVE2
     * @param configFilePath String containing path to local copy of DROID config file
     * @param sigFilePath String containing path to local copy of DROID signature file
     * @throws Exception
     */
    public DROIDWrappedProduct (String configFilePath, String sigFilePath) throws Exception {
    	this();
    	this.setConfigFile(parseConfigFile(configFilePath));
    	this.setSigFile((parseSignatureFile(this.getConfigFile(), sigFilePath)));
    }

    /**
     * Cosntructor
     * @param configFile Parsed ConfigFile object
     * @param sigFile  Parsed Signature file object
     * @throws Exception
     */
    public DROIDWrappedProduct(ConfigFile configFile, FFSignatureFile sigFile) throws Exception {
    	this();
    	this.setConfigFile(configFile);
    	this.setSigFile(sigFile);
    }

    /**
     * Parses DROID configuration file
     * @param configFilePath path to DROID configuration file
     * @return ConfigFile object containing parsed contents of configuration file
     * @throws Exception
     */
    public static ConfigFile parseConfigFile(String configFilePath) throws Exception{
    	ConfigFile configFile = null;
    	configFile = JHOVE2AnalysisControllerUtil.loadConfigFile(configFilePath);
    	return configFile;
    }
    
    /**
     * Parses DROID signature file
     * @param configFile ConfigFile object containing parsed contents of configuration file
     * @param sigFilePath path to DROID signature file
     * @return FFSignatureFile object containing parsed contents of signature file
     * @throws Exception
     */
    public static FFSignatureFile parseSignatureFile(ConfigFile configFile,
    			String sigFilePath) throws Exception {
    	FFSignatureFile sigFile =
    		JHOVE2AnalysisControllerUtil.loadSigFile(configFile, sigFilePath);
    	return sigFile;
    }
    
    /**
     * Read the signature file
     *
     * @param signatureFile
     * @throws Exception
     */
    public void readSignatureFile(String signatureFile) throws Exception {
        analysisControl.readSigFile(signatureFile);
    }

    /**
     * get the signature file version
     *
     * @return
     */
    public String getSignatureFileVersion() {
        return this.analysisControl.getSigFile().getVersion();
    }

    /**
     * identify files using DROID 
     *
     * @param file full path to a disk file
     * @return IdentificationFile
     */
    public IdentificationFile identify(String file) {

        IdentificationFile identificationFile = new IdentificationFile(file);
        ByteReader byteReader = null;
        try {
            byteReader = newByteReader(identificationFile);
            analysisControl.getSigFile().runFileIdentification(byteReader);
        }finally{
            byteReader.close();
        }
        return identificationFile;
    }

    /**
     * identify files using DROID
     * @param source Source to be identified by DROID
     * @return
     */
    public IdentificationFile identify(Source source) {
        IdentificationFile identificationFile = new IdentificationFile();
        identificationFile.setFilePath("-"); // necessary to force DROID to treat this as InputStream not file
        ByteReader byteReader = null;
        try {
		  byteReader = newByteReader(identificationFile, source.getInputStream());
          if (identificationFile.getClassification()!= JHOVE2IAnalysisController.FILE_CLASSIFICATION_ERROR){
        	  if (source instanceof FileSource){
        		  // set the path so DROID will make use of external signatures as well as internal ones
        		  identificationFile.setFilePath(source.getFile().getCanonicalPath());
        	  }
        	  else if (source instanceof URLSource){
        		// set the path so DROID will make use of external signatures as well as internal ones
        		  identificationFile.setFilePath(((URLSource)source).getURLString());
        	  }
            	analysisControl.getSigFile().runFileIdentification(byteReader);
            }
        }
	    catch (FileNotFoundException e) {
	    	identificationFile.setIDStatus(JHOVE2IAnalysisController.FILE_CLASSIFICATION_NOTCLASSIFIED);
		}
	    catch (IOException e) {
	    	identificationFile.setIDStatus(JHOVE2IAnalysisController.FILE_CLASSIFICATION_NOTCLASSIFIED);
		}
        finally{
            byteReader.close();
        }
        return identificationFile;
    }

    /**
     * Accessor for parsed ConfigFile
     * @return parsed ConfigFile
     */
    public ConfigFile getConfigFile(){
    	return this.analysisControl.getConfigFile();
    }
    /**
     * Mutator for parsed ConfigFile
     * @param configFile
     */
	public void setConfigFile(ConfigFile configFile) {
		this.analysisControl.setConfigFile(configFile);	
	}
	/**
	 * Accessor for parsed signature file object
	 * @return parsed signature file object
	 */
	public FFSignatureFile getSigFile() {
		return this.analysisControl.getSigFile();
	}
	/**
	 * Mutator for parsed signature file object
	 * @param sigFile parsed signature file object
	 */
	public void setSigFile(FFSignatureFile sigFile) {
		this.analysisControl.setSigFile(sigFile);
	}
	/**
	 * Accessor for path to configuration file
	 * @return path to configuration file
	 */
	@ReportableProperty
	public String getConfigFilePath(){
		return this.analysisControl.getConfigFile().getFileName();
	}
	/**
	 * Accessor for path to signature file
	 * @return path to signature file
	 */
	@ReportableProperty
	public String getSigFilePath(){
		return this.analysisControl.getSignatureFileName();
	}
	/**
	 * accessor for signature file version
	 * @return signature file version
	 */
	@ReportableProperty
	public int getSigFileVersion(){
		return this.analysisControl.getSigFileVersion();
	}
	/**
	 * accessor for  \signature file date
	 * @return signature file date
	 */
	@ReportableProperty
	public String getSigFileDate(){
		return this.analysisControl.getSignatureFileDate();
	}
}
