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
 * $History: FileFormat.java $
 * 
 * *****************  Version 3  *****************
 * User: Walm         Date: 5/04/05    Time: 18:07
 * Updated in $/PRONOM4/FFIT_SOURCE/signatureFile
 * review headers
 *
 */
package uk.gov.nationalarchives.droid.signatureFile;

import uk.gov.nationalarchives.droid.MessageDisplay;
import uk.gov.nationalarchives.droid.xmlReader.SimpleElement;

import java.util.ArrayList;
import java.util.List;

/**
 * holds details of a file format
 *
 * @author Martin Waller
 * @version 4.0.0
 */
public class FileFormat extends SimpleElement {
    int identifier;
    String name;
    String version;
    String PUID;
    List<Integer> internalSigIDs = new ArrayList<Integer>();
    List<String> extensions = new ArrayList<String>();
    List<Integer> hasPriorityOver = new ArrayList<Integer>();
    String mimeType;

    /* setters */
    public void setInternalSignatureID(String theID) {
        this.internalSigIDs.add(Integer.parseInt(theID));
    }

    public void setExtension(String theExtension) {
        this.extensions.add(theExtension);
    }

    public void setHasPriorityOverFileFormatID(String theID) {
        this.hasPriorityOver.add(Integer.parseInt(theID));
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public void setAttributeValue(String theName, String theValue) {
        if (theName.equals("ID")) {
            this.identifier = Integer.parseInt(theValue);
        } else if (theName.equals("Name")) {
            this.name = theValue;
        } else if (theName.equals("Version")) {
            this.version = theValue;
        } else if (theName.equals("PUID")) {
            this.PUID = theValue;
        } else if (theName.equals("MIMEType")) {
            this.mimeType = theValue;
        } else {
            MessageDisplay.unknownAttributeWarning(name, this.getElementName());
        }
    }

    /* getters */
    public int getNumInternalSignatures() {
        return this.internalSigIDs.size();
    }

    public int getNumExtensions() {
        return this.extensions.size();
    }

    public int getNumHasPriorityOver() {
        return this.hasPriorityOver.size();
    }

    public int getInternalSignatureID(int theIndex) {
        return this.internalSigIDs.get(theIndex);
    }

    public String getMimeType() {
        return (this.mimeType == null ? "" : this.mimeType);
    }

    public String getExtension(int theIndex) {
        return this.extensions.get(theIndex);
    }

    public int getHasPriorityOver(int theIndex) {
        return this.hasPriorityOver.get(theIndex);
    }

    public int getID() {
        return identifier;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getPUID() {
        return PUID;
    }

    /**
     * Indicates whether the file extension given is valid for this file format
     *
     * @param theExtension file extension
     */
    public boolean hasMatchingExtension(String theExtension) {
        boolean matchingExtension = false;
        for (int iExtension = 0; iExtension < this.getNumExtensions(); iExtension++) {
            if (theExtension.equalsIgnoreCase(this.getExtension(iExtension))) {
                matchingExtension = true;
            }
        }//loop through Extensions
        return matchingExtension;
    }

}
