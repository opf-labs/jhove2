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
 * Created  JHOVE2 2009.09.12
 * from static contants and methods in original uk.gov.nationalarchives.droid.AnalysisController
 * Please see the file DROID-LICENSE.txt in the JHOVE2 distribution for a complete statement
 *  of the BSD license rights governing the use of DROID source code.
 */
package uk.gov.nationalarchives.droid;

import uk.gov.nationalarchives.droid.signatureFile.FFSignatureFile;

/**
 * Reverse-engineered interface to captures static constants fields used by various DROID classes
 * 
 * Static fields are a copy of methods from class
 * uk.gov.nationalarchives.droid in the UK National Archives DROID  distribution.
 * 
 * Method signatures are intended to facilitate caching of parsed DROID configuration and 
 * signature files.
 * 
 * @author smorrissey
 *
 */
public interface JHOVE2IAnalysisController {
    //Application version
    public static final String DROID_VERSION = "4.01JHOVE2";
	   //File classification constants
    public static final int FILE_CLASSIFICATION_POSITIVE = 1;
    public static final int FILE_CLASSIFICATION_TENTATIVE = 2;
    public static final int FILE_CLASSIFICATION_NOHIT = 3;
    public static final int FILE_CLASSIFICATION_ERROR = 4;
    public static final int FILE_CLASSIFICATION_NOTCLASSIFIED = 5;
    public static final String FILE_CLASSIFICATION_POSITIVE_TEXT = "Positive";
    public static final String FILE_CLASSIFICATION_TENTATIVE_TEXT = "Tentative";
    public static final String FILE_CLASSIFICATION_NOHIT_TEXT = "Not identified";
    public static final String FILE_CLASSIFICATION_ERROR_TEXT = "Error";
    public static final String FILE_CLASSIFICATION_NOTCLASSIFIED_TEXT = "Not yet run";

    //hit scope constants
    public static final int HIT_TYPE_POSITIVE_SPECIFIC = 10;
    public static final int HIT_TYPE_POSITIVE_GENERIC = 11;
    public static final int HIT_TYPE_TENTATIVE = 12;
    public static final int HIT_TYPE_POSITIVE_GENERIC_OR_SPECIFIC = 15;
    public static final String HIT_TYPE_POSITIVE_SPECIFIC_TEXT = "Positive (Specific Format)";
    public static final String HIT_TYPE_POSITIVE_GENERIC_TEXT = "Positive (Generic Format)";
    public static final String HIT_TYPE_TENTATIVE_TEXT = "Tentative";
    public static final String HIT_TYPE_POSITIVE_GENERIC_OR_SPECIFIC_TEXT = "Positive";

    //Buffer size for reading random access files
    public static final int FILE_BUFFER_SIZE = 100000000;
    
    //default values
    public static final int CONFIG_DOWNLOAD_FREQ = 30;
    public static final String CONFIG_FILE_NAME = "DROID_config.xml";
    public static final String FILE_LIST_FILE_NAME = "DROID_filecollection.xml";
    public static final String PRONOM_WEB_SERVICE_URL = "http://www.nationalarchives.gov.uk/pronom/service.asmx";
    public static final String SIGNATURE_FILE_NAME = "DROID_SignatureFile.xml";
    public static final String PUID_RESOLUTION_URL = "http://www.nationalarchives.gov.uk/pronom/";
    public static final String BROWSER_PATH = "/usr/bin/firefox";
    public static final String LABEL_APPLICATION_VERSION = "DROIDVersion";
    public static final String LABEL_DATE_CREATED = "DateCreated";
    public static final String START_MODE_DROID = "Droid";
    public static final String PROFILE_REPORT_PATH = "reports";
    public static final String REPORT_TEMP_DIR = "tempDir";
    /**
     * Date format to read/write dates to XML
     */
    public static final String XML_DATE_FORMAT = "yyyy'-'MM'-'dd'T'HH:mm:ss";
    /**
    
    /**
     * Namespace for the xml file collection file
     */
    public static final String FILE_COLLECTION_NS = "http://www.nationalarchives.gov.uk/pronom/FileCollection";
    /**
     * Namespace for the xml configuration file
     */
    public static final String CONFIG_FILE_NS = "http://www.nationalarchives.gov.uk/pronom/ConfigFile";
    /**
     * Namespace for the xml file format signatures file
     */
    public static final String SIGNATURE_FILE_NS = "http://www.nationalarchives.gov.uk/pronom/SignatureFile";
    
    /**
     * Reads a configuration file, and loads the contents into memory.
     *
     * @param theFileName The name of the configuration file to open
     * @throws Exception on error
     */
    public void readConfiguration(String theFileName) throws Exception;
    
    /**
     * Reads in and parses the signature file
     * Updates the configuration file with this signature file
     *
     * @param theSigFileName Name of the signature file
     * @return name of sig file
     * @throws Exception on error
     */
    public String readSigFile(String theSigFileName) throws Exception;
    
    /**
     * Download the latest signature file from the PRONOM web service, save it to file
     * An input flag determines whether or not to load it in to the current instance of uk
     *
     * @param theFileName   file where to save signature file
     * @param isLoadSigFile Flag indicating whether to load the signature file into the current instance of uk
     */
    public void downloadwwwSigFile(String theFileName, boolean isLoadSigFile);
    
    /**
     * Sets the URL of the signature file webservices
     *
     * @param sigFileURL signature file
     */
    public void setSigFileURL(String sigFileURL);
    /**
     * 
     * @return
     */
    public FFSignatureFile getSigFile();
    /**
     * 
     * @param currentVersion
     * @return
     */
    public boolean isNewerSigFileAvailable(int currentVersion);

}
