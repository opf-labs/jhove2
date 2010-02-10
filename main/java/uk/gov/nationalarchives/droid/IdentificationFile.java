/*
 * © The National Archives 2005-2006.  All rights reserved.
 * See Licence.txt for full licence details.
 *
 * Developed by:
 * Tessella Support Services plc
 * 3 Vineyard Chambers
 * Abingdon, OX14 3PX
 * United Kingdom
 * http://www.tessella.com
 *
 * Tessella/NPD/4305
 * PRONOM 4
 *
 * $Id: IdentificationFile.java,v 1.9 2006/03/13 15:15:25 linb Exp $
 * 
 * $Log: IdentificationFile.java,v $
 * Revision 1.9  2006/03/13 15:15:25  linb
 * Changed copyright holder from Crown Copyright to The National Archives.
 * Added reference to licence.txt
 * Changed dates to 2005-2006
 *
 * Revision 1.8  2006/02/09 15:31:23  linb
 * Updates to javadoc and code following the code review
 *
 * Revision 1.7  2006/02/09 13:17:41  linb
 * Changed StreamByteReader to InputStreamByteReader
 * Refactored common code from UrlByteReader and InputStreamByteReader into new class StreamByteReader, from which they both inherit
 * Updated javadoc
 *
 * Revision 1.6  2006/02/09 12:14:15  linb
 * Changed some javadoc to allow it to be created cleanly
 *
 * Revision 1.5  2006/02/08 11:45:48  linb
 * - add support for streams
 *
 * Revision 1.4  2006/02/08 08:56:35  linb
 * - Added header comments
 *
 *
 *$History: IdentificationFile.java $
 * 
 * *****************  Version 10  *****************
 * User: Walm         Date: 5/04/05    Time: 18:08
 * Updated in $/PRONOM4/FFIT_SOURCE
 * review headers
 * 
 * *****************  Version 9  *****************
 * User: Walm         Date: 29/03/05   Time: 11:11
 * Updated in $/PRONOM4/FFIT_SOURCE
 * Read in hit results from a file collection file
 *
 * *****************  Version 8  *****************
 * User: Walm         Date: 24/03/05   Time: 11:17
 * Updated in $/PRONOM4/FFIT_SOURCE
 * add Text versions of file classifications
 *
 * *****************  Version 7  *****************
 * User: Walm         Date: 15/03/05   Time: 15:31
 * Updated in $/PRONOM4/FFIT_SOURCE
 * initialise identification warning
 *
 * *****************  Version 6  *****************
 * User: Walm         Date: 15/03/05   Time: 14:40
 * Updated in $/PRONOM4/FFIT_SOURCE
 * File classifications are now integer constants defined in
 * AnalysisController
 *
 * *****************  Version 5  *****************
 * User: Mals         Date: 14/03/05   Time: 18:09
 * Updated in $/PRONOM4/FFIT_SOURCE
 * return null identification status
 *
 * *****************  Version 4  *****************
 * User: Mals         Date: 14/03/05   Time: 15:07
 * Updated in $/PRONOM4/FFIT_SOURCE
 * Added positive ,tentitive and unidentified status setters
 *
 * *****************  Version 3  *****************
 * User: Mals         Date: 14/03/05   Time: 14:00
 * Updated in $/PRONOM4/FFIT_SOURCE
 * Changed location of FileFormatHit
 *
 * *****************  Version 2  *****************
 * User: Mals         Date: 11/03/05   Time: 12:20
 * Updated in $/PRONOM4/FFIT_SOURCE
 * Added setters
 *
 * *****************  Version 1  *****************
 * User: Mals         Date: 11/03/05   Time: 11:54
 * Created in $/PRONOM4/FFIT_SOURCE
 * Reprsents an analysed or to be analysed file
 *
 * * ******************* Version 5 **********************
 * S. Morrissey For JHOVE2  Date 09/12/2009
 * refactored to use IAnalaysis Controller for constants, 
 * and AnalysisControllerUtil for static methods
 * 
 *	 public String getFileName() altered for JHOVE2 so that extensions from URLSources will be visible
 *   for external signature checking
 * 
 */

package uk.gov.nationalarchives.droid;

import uk.gov.nationalarchives.droid.binFileReader.UrlByteReader;
import uk.gov.nationalarchives.droid.xmlReader.SimpleElement;

/**
 * Reprsents an analysed or to be analysed file.
 * <p/>
 * <p>Holds details on warnings, status and format hits for the file.
 *
 * @author Shahzad Malik
 * @version V1.R0.M.0, 11-Mar-2005
 */
public class IdentificationFile extends SimpleElement implements Comparable {


    private String identificationWarning = "";
    private int myIDStatus = JHOVE2IAnalysisController.FILE_CLASSIFICATION_NOTCLASSIFIED;
    private java.util.List<FileFormatHit> fileHits = new java.util.ArrayList<FileFormatHit>();
    private String filePath;

    /**
     * Creates a new instance of IdentificationFile.
     *
     * @param filePath Full file path
     */
    public IdentificationFile(String filePath) {
        setFilePath(filePath);
    }

    public IdentificationFile() {
    }

    /**
     * Set the file identification status.
     *
     * @param theStatus file identification status
     */
    public void setIDStatus(int theStatus) {
        myIDStatus = theStatus;
    }

    /**
     * Sets the file status to Postive
     */
    public void setPositiveIdent() {
        myIDStatus = JHOVE2IAnalysisController.FILE_CLASSIFICATION_POSITIVE;
    }

    /**
     * Sets the file status to tentitive
     */
    public void setTentativeIdent() {
        myIDStatus = JHOVE2IAnalysisController.FILE_CLASSIFICATION_TENTATIVE;
    }

    /**
     * Sets the status to not identified
     */
    public void setNoIdent() {
        myIDStatus = JHOVE2IAnalysisController.FILE_CLASSIFICATION_NOHIT;
    }

    /**
     * Sets the status to error during identification
     */
    public void setErrorIdent() {
        myIDStatus = JHOVE2IAnalysisController.FILE_CLASSIFICATION_ERROR;
    }

    /**
     * Set the file identification warning.
     *
     * @param warning file identification warning
     */
    public void setWarning(String warning) {
        identificationWarning = warning;
    }

    /**
     * Set the full file Path.
     *
     * @param filePath full file path
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Add a file format hit for this file.
     *
     * @param hit file format hit found
     */
    public void addHit(FileFormatHit hit) {
        fileHits.add(hit);
    }

    /**
     * Add a file format hit for this file.
     * <p/>
     * <p>Used for reading XML
     *
     * @param theHit file format hit found
     */
    public void addFileFormatHit(FileFormatHit theHit) {
        fileHits.add(theHit);
    }

    /**
     * Remove a file format hit for the file.
     *
     * @param index position in hit list of file
     */
    public void removeHit(int index) {
        fileHits.remove(index);
    }


    /**
     * Returns the file name (without the full path)
     *
     * @return
     */
    public String getFileName() {

//        if (InputStreamByteReader.isInputStream(filePath)) {
//            return ""; // Input stream has no file name
    	// altered for JHOVE2 so that extensions from URLSources will be visible
    	// for external signature checking
    	if (filePath == "-"){
    		 return ""; // Input stream has no file name
        } else if (UrlByteReader.isURL(filePath)) {
            // File part of URL
            return UrlByteReader.getURL(filePath).getFile();
        } else {
            return (new java.io.File(filePath)).getName();
        }
    }

    /**
     * Returns the full file path
     *
     * @return
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * Checks whether the file has been classified yet
     * (i.e return YES for the following classification values:
     * FILE_CLASSIFICATION_POSITIVE , FILE_CLASSIFICATION_TENTITIVE
     * FILE_CLASSIFICATION_NOHIT , FILE_CLASSIFICATION_ERROR
     *
     * @return
     */
    public boolean isClassified() {
        return (myIDStatus != JHOVE2IAnalysisController.FILE_CLASSIFICATION_NOTCLASSIFIED);
    }

    /**
     * Returns the file classification found by the identification
     * The options are setup as constants under Analysis Controller:
     * *FILE_CLASSIFICATION_POSITIVE
     * *FILE_CLASSIFICATION_TENTATIVE
     * *FILE_CLASSIFICATION_NOHIT
     * *FILE_CLASSIFICATION_ERROR
     * *FILE_CLASSIFICATION_NOTCLASSIFIED
     *
     * @return
     */
    public int getClassification() {
        return myIDStatus;
    }

    /**
     * Returns the text description of the file classification found by the identification
     * The options are setup as constants under Analysis Controller:
     * *FILE_CLASSIFICATION_POSITIVE_TEXT
     * *FILE_CLASSIFICATION_TENTATIVE_TEXT
     * *FILE_CLASSIFICATION_NOHIT_TEXT
     * *FILE_CLASSIFICATION_ERROR_TEXT
     * *FILE_CLASSIFICATION_NOTCLASSIFIED_TEXT
     *
     * @return
     */
    public String getClassificationText() {
        String theClassificationText = "";
        if (myIDStatus == JHOVE2IAnalysisController.FILE_CLASSIFICATION_POSITIVE) {
            theClassificationText = JHOVE2IAnalysisController.FILE_CLASSIFICATION_POSITIVE_TEXT;
        } else if (myIDStatus == JHOVE2IAnalysisController.FILE_CLASSIFICATION_TENTATIVE) {
            theClassificationText = JHOVE2IAnalysisController.FILE_CLASSIFICATION_TENTATIVE_TEXT;
        } else if (myIDStatus == JHOVE2IAnalysisController.FILE_CLASSIFICATION_NOHIT) {
            theClassificationText = JHOVE2IAnalysisController.FILE_CLASSIFICATION_NOHIT_TEXT;
        } else if (myIDStatus == JHOVE2IAnalysisController.FILE_CLASSIFICATION_ERROR) {
            theClassificationText = JHOVE2IAnalysisController.FILE_CLASSIFICATION_ERROR_TEXT;
        } else if (myIDStatus == JHOVE2IAnalysisController.FILE_CLASSIFICATION_NOTCLASSIFIED) {
            theClassificationText = JHOVE2IAnalysisController.FILE_CLASSIFICATION_NOTCLASSIFIED_TEXT;
        }
        return theClassificationText;
    }

    /**
     * Returns any warning associated with the file
     *
     * @return
     */
    public String getWarning() {
        return identificationWarning;
    }

    /**
     * Returns number of hits found for this file
     *
     * @return
     */
    public int getNumHits() {
        return fileHits.size();
    }

    /**
     * Returns a hit object associated with the file that has been run
     * through the identification process
     *
     * @param theIndex
     * @return
     */
    public FileFormatHit getHit(int theIndex) {
        return fileHits.get(theIndex);
    }


    /**
     * Populate the details of the IdentificationFile object when read in from XML file.
     *
     * @param theName  Name of the attribute read in
     * @param theValue Value of the attribute read in
     */
    public void setAttributeValue(String theName, String theValue) {
        if (theName.equals("Name")) {
            this.setFilePath(theValue);
        } else if (theName.equals("IdentQuality")) {
            //The IdentQuality attribute value should match one of those specified in code -
            //otherwise show a warning
            if (theValue.equals(JHOVE2IAnalysisController.FILE_CLASSIFICATION_POSITIVE_TEXT)) {
                myIDStatus = JHOVE2IAnalysisController.FILE_CLASSIFICATION_POSITIVE;
            } else if (theValue.equals(JHOVE2IAnalysisController.FILE_CLASSIFICATION_TENTATIVE_TEXT)) {
                myIDStatus = JHOVE2IAnalysisController.FILE_CLASSIFICATION_TENTATIVE;
            } else if (theValue.equals(JHOVE2IAnalysisController.FILE_CLASSIFICATION_NOHIT_TEXT)) {
                myIDStatus = JHOVE2IAnalysisController.FILE_CLASSIFICATION_NOHIT;
            } else if (theValue.equals(JHOVE2IAnalysisController.FILE_CLASSIFICATION_ERROR_TEXT)) {
                myIDStatus = JHOVE2IAnalysisController.FILE_CLASSIFICATION_ERROR;
            } else if (theValue.equals(JHOVE2IAnalysisController.FILE_CLASSIFICATION_NOTCLASSIFIED_TEXT)) {
                myIDStatus = JHOVE2IAnalysisController.FILE_CLASSIFICATION_NOTCLASSIFIED;
            } else {
                MessageDisplay.generalWarning("Unknown file status listed: <" + theValue + "> is not the same as <" + 
                		JHOVE2IAnalysisController.FILE_CLASSIFICATION_POSITIVE_TEXT + ">");
            }
        } else if (theName.equals("Warning")) {
            this.setWarning(theValue);
        } else {
            MessageDisplay.unknownAttributeWarning(theName, this.getElementName());
        }
    }

    public int compareTo(Object otherIDFile) {
        // This object always smaller if path not set
        if (this.filePath == null || this.filePath.length() == 0) {
            return -1;
        }

        return this.getFilePath().compareToIgnoreCase(((IdentificationFile) otherIDFile).getFilePath());
    }

    /**
     * Base equality on filePath
     */
    public boolean equals(Object otherIDFile) {
        return otherIDFile instanceof IdentificationFile && this.filePath.toLowerCase().equals(((IdentificationFile) otherIDFile).filePath.toLowerCase());
    }

    /**
     * Use hashCode of filePath if set, as we wish to enforce this to be unique
     */
    public int hashCode() {
        if (this.filePath == null || this.filePath.length() == 0) {
            return super.hashCode();
        } else {
            return this.filePath.toLowerCase().hashCode();
        }
    }

}
