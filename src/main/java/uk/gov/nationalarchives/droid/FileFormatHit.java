/*
 * Copyright The National Archives 2005-2006.  All rights reserved.
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
 * $Id: FileFormatHit.java,v 1.4 2006/03/13 15:15:25 linb Exp $
 * 
 * $Log: FileFormatHit.java,v $
 * Revision 1.4  2006/03/13 15:15:25  linb
 * Changed copyright holder from Crown Copyright to The National Archives.
 * Added reference to licence.txt
 * Changed dates to 2005-2006
 *
 * Revision 1.3  2006/02/08 08:56:35  linb
 * - Added header comments
 *
 *
 * *$History: FileFormatHit.java $
 *
 * *****************  Version 4  *****************
 * User: Walm         Date: 5/04/05    Time: 18:08
 * Updated in $/PRONOM4/FFIT_SOURCE
 * review headers
 *
 * * ******************* Version 5 **********************
 * S. Morrissey For JHOVE2  Date 09/12/2009
 * refactored to use IAnalaysis Controller for constants, 
 * and AnalysisControllerUtil for static methods
 * 
 */

package uk.gov.nationalarchives.droid;

import uk.gov.nationalarchives.droid.signatureFile.FileFormat;
import uk.gov.nationalarchives.droid.xmlReader.SimpleElement;

/**
 * holds the description of a hit (format identifier) on a file
 *
 * @author Martin Waller
 * @version 4.0.0
 */
public class FileFormatHit extends SimpleElement {
    String myHitWarning = "";
    int myHitType;
    FileFormat myHitFileFormat;

    /**
     * Creates a new blank instance of fileFormatHit
     *
     * @param theFileFormat  The file format which has been identified
     * @param theType        The scope of hit i.e. Positive/tentative
     * @param theSpecificity Flag is set to true for Positive specific hits
     * @param theWarning     A warning associated with the hit
     */
    public FileFormatHit(FileFormat theFileFormat, int theType, boolean theSpecificity, String theWarning) {
        myHitFileFormat = theFileFormat;
        if (theType == JHOVE2IAnalysisController.HIT_TYPE_POSITIVE_GENERIC_OR_SPECIFIC) {
            if (theSpecificity) {
                myHitType = JHOVE2IAnalysisController.HIT_TYPE_POSITIVE_SPECIFIC;
            } else {
                myHitType = JHOVE2IAnalysisController.HIT_TYPE_POSITIVE_GENERIC;
            }
        } else {
            myHitType = theType;
        }
        this.setIdentificationWarning(theWarning);
    }

    public FileFormatHit() {
    }

    /**
     * Updates the warning message for a hit
     * <p/>
     * Used by XML reader for IdentificationFile/FileFormatHit/IdentificationWarning element
     *
     * @param theWarning A warning associated with the hit
     */
    public void setIdentificationWarning(String theWarning) {
        myHitWarning = theWarning;
    }


    /**
     * get the fileFormat for the hit
     *
     * @return
     */
    public FileFormat getFileFormat() {
        return myHitFileFormat;
    }

    /**
     * get the name of the fileFormat of this hit
     *
     * @return
     */
    public String getFileFormatName() {
        return myHitFileFormat.getName();
    }

    /**
     * get the version of the fileFormat of this hit
     *
     * @return
     */
    public String getFileFormatVersion() {
        return myHitFileFormat.getVersion();
    }

    /**
     * Get the mime scope
     *
     * @return
     */
    public String getMimeType() {
        return myHitFileFormat.getMimeType();
    }

    /**
     * get the PUID of the fileFormat of this hit
     *
     * @return
     */
    public String getFileFormatPUID() {
        return myHitFileFormat.getPUID();
    }

    /**
     * get the code of the hit scope
     *
     * @return
     */
    public int getHitType() {
        return myHitType;
    }

    /**
     * get the name of the hit scope
     *
     * @return
     */
    public String getHitTypeVerbose() {
        String theHitType = "";
        if (myHitType == JHOVE2IAnalysisController.HIT_TYPE_POSITIVE_GENERIC) {
            theHitType = JHOVE2IAnalysisController.HIT_TYPE_POSITIVE_GENERIC_TEXT;
        } else if (myHitType == JHOVE2IAnalysisController.HIT_TYPE_POSITIVE_SPECIFIC) {
            theHitType = JHOVE2IAnalysisController.HIT_TYPE_POSITIVE_SPECIFIC_TEXT;
        } else if (myHitType == JHOVE2IAnalysisController.HIT_TYPE_TENTATIVE) {
            theHitType = JHOVE2IAnalysisController.HIT_TYPE_TENTATIVE_TEXT;
        } else if (myHitType == JHOVE2IAnalysisController.HIT_TYPE_POSITIVE_GENERIC_OR_SPECIFIC) {
            theHitType = JHOVE2IAnalysisController.HIT_TYPE_POSITIVE_GENERIC_OR_SPECIFIC_TEXT;
        }
        return theHitType;
    }

    /**
     * get any warning associated with the hit
     *
     * @return
     */
    public String getHitWarning() {
        return myHitWarning;
    }

    /**
     * For positive hits, this returns true if hit is Specific
     * or returns false if hit is Generic.
     * Meaningless for Tentative hits. (though returns false)
     *
     * @return
     */
    public boolean isSpecific() {
        return myHitType == JHOVE2IAnalysisController.HIT_TYPE_POSITIVE_SPECIFIC;
    }


    /**
     * Populates the details of the IdentificationFile when it is read in from XML file
     *
     * @param theName  Name of the attribute read in
     * @param theValue Value of the attribute read in
     */
    public void setAttributeValue(String theName, String theValue) {
        if (theName.equals("HitStatus")) {
            this.setStatus(theValue);
        } else if (theName.equals("FormatName")) {
            this.setName(theValue);
        } else if (theName.equals("FormatVersion")) {
            this.setVersion(theValue);
        } else if (theName.equals("FormatPUID")) {
            this.setPUID(theValue);
        } else if (theName.equals("HitWarning")) {
            this.setIdentificationWarning(theValue);
        } else {
            MessageDisplay.unknownAttributeWarning(theName, this.getElementName());
        }
    }

    /**
     * Set hit status.  Used by XML reader for IdentificationFile/FileFormatHit/Status element
     *
     * @param value
     */
    public void setStatus(String value) {
        //String value = element.getText();
        if (value.equals(JHOVE2IAnalysisController.HIT_TYPE_POSITIVE_GENERIC_TEXT)) {
            myHitType = JHOVE2IAnalysisController.HIT_TYPE_POSITIVE_GENERIC;
        } else if (value.equals(JHOVE2IAnalysisController.HIT_TYPE_POSITIVE_SPECIFIC_TEXT)) {
            myHitType = JHOVE2IAnalysisController.HIT_TYPE_POSITIVE_SPECIFIC;
        } else if (value.equals(JHOVE2IAnalysisController.HIT_TYPE_TENTATIVE_TEXT)) {
            myHitType = JHOVE2IAnalysisController.HIT_TYPE_TENTATIVE;
        } else if (value.equals(JHOVE2IAnalysisController.HIT_TYPE_POSITIVE_GENERIC_OR_SPECIFIC_TEXT)) {
            myHitType = JHOVE2IAnalysisController.HIT_TYPE_POSITIVE_GENERIC_OR_SPECIFIC;
        } else {
            MessageDisplay.generalWarning("Unknown hit status listed: " + value);
        }
    }

    /**
     * Set hit format name.  Used by XML reader for IdentificationFile/FileFormatHit/Name element
     *
     * @param value
     */
    public void setName(String value) {
        //if necessary, this creates a new dummy File format
        if (myHitFileFormat == null) {
            myHitFileFormat = new FileFormat();
        }
        myHitFileFormat.setAttributeValue("Name", value);
    }

    /**
     * Set hit format version.  Used by XML reader for IdentificationFile/FileFormatHit/Version element
     *
     * @param value
     */
    public void setVersion(String value) {
        if (myHitFileFormat == null) {
            myHitFileFormat = new FileFormat();
        }
        myHitFileFormat.setAttributeValue("Version", value);
    }

    /**
     * Set hit format PUID.  Used by XML reader for IdentificationFile/FileFormatHit/PUID element
     *
     * @param value
     */
    public void setPUID(String value) {
        if (myHitFileFormat == null) {
            myHitFileFormat = new FileFormat();
        }
        myHitFileFormat.setAttributeValue("PUID", value);
    }

    /**
     * Set hit format MIME scope.  Used by XML reader for IdentificationFile/FileFormatHit/PUID element
     *
     * @param value
     */
    public void setMimeType(String value) {
        if (myHitFileFormat == null) {
            myHitFileFormat = new FileFormat();
        }
        myHitFileFormat.setAttributeValue("MIMEType", value);
    }


}
