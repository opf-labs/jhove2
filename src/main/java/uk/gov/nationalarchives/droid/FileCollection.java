/*
 * � The National Archives 2005-2006.  All rights reserved.
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
  * *****************************************
 * S. Morrissey For JHOVE2  Date 09/12/2009
 * refactored to use IAnalaysis Controller for constants, 
 * and AnalysisControllerUtil for static methods
 * 
 */

package uk.gov.nationalarchives.droid;

import uk.gov.nationalarchives.droid.binFileReader.InputStreamByteReader;
import uk.gov.nationalarchives.droid.binFileReader.UrlByteReader;
import uk.gov.nationalarchives.droid.xmlReader.SimpleElement;

import java.util.Iterator;

/**
 * Class to hold configuration data for the uk.
 * Elements of this collection are IdentificationFile objects.
 * This data is read from and saved to an XML configuration file.
 */
public class FileCollection extends SimpleElement {

    /**
     * file path to store file list
     */
    private String myFileName;
    /**
     * Holds IdentificationFile objects
     */
    private IndexedHashSet<Integer, IdentificationFile> myFiles;

    /**
     * Signature file version for a file that is read in -
     * only used to check it is the same as currently loaded
     */
    private int mySigFileVersion = 0;

    private long startTime;
    private long stopTime;
    private long lastNumFiles = -1;

    /**
     * Creates a FileCollection object
     */
    public FileCollection() {
        myFileName = JHOVE2IAnalysisController.FILE_LIST_FILE_NAME;
        myFiles = new IndexedHashSet<Integer, IdentificationFile>();
    }

    /**
     * Specify the file path for where to store the file list
     *
     * @param theFileName path of where to save the file
     */
    public void setFileName(String theFileName) {
        myFileName = theFileName;
    }

    /**
     * Get iterator for files in the collection
     *
     * @return list iterator
     */
    public Iterator<IdentificationFile> getIterator() {
        return this.myFiles.iterator();
    }

    public java.util.Enumeration getIndexKeys() {
        return this.myFiles.getIndexKeys();
    }

    /**
     * Adds an element/elements to the collection
     * If filepath is a path to file then add that file
     * If filepath is a folder path then add contents of the folder
     *
     * @param theFile     Filepath of file or folder
     * @param isRecursive if true add all subfolders and subsubfolders , etc
     */
    public void addFile(String theFile, boolean isRecursive) {

        if (UrlByteReader.isURL(theFile)) {
            //File object is a URL: add if it isn't a duplicate
            myFiles.add(this.getNumFiles(), new IdentificationFile(theFile));
            return;
        }

        if (InputStreamByteReader.isInputStream(theFile)) {
            // File is an input stream: add if it isn't a duplicate
            myFiles.add(this.getNumFiles(), new IdentificationFile(theFile));
        }

        try {
            java.io.File f = new java.io.File(theFile);

            //Is file object a directory or file?
            if (f.isDirectory()) {

                //File object is a directory/folder
                //Iterate through directory ,create IdentificationFile objects
                //and add them to the collection
                java.io.File[] folderFiles = f.listFiles();
                int numFiles = 0;
                try {
                    numFiles = folderFiles.length;
                } catch (Exception e) {
                    MessageDisplay.generalWarning("Unable to read directory " + theFile + "\nThis may be because you do not have the correct permissions.");
                }
                for (int m = 0; m < numFiles; m++) {
                    if (folderFiles[m].isFile()) {
                        //If file exists and not duplicate then add
                        IdentificationFile idFile = new IdentificationFile(folderFiles[m].getPath());
                        myFiles.add(this.getNumFiles(), idFile);
                    } else if (folderFiles[m].isDirectory() && isRecursive) {
                        //If subdirectory found and recursive is on add contents of that folder
                        addFile(folderFiles[m].getPath(), isRecursive);
                    }
                }

            } else if (f.isFile()) {
                //File object is a File then add file if it isn't a duplicate
                IdentificationFile idFile = new IdentificationFile(f.getPath());
                myFiles.add(this.getNumFiles(), idFile);
            }

        } catch (Exception e) {
            MessageDisplay.generalWarning("The following error occured while adding " + theFile + ":\n" + e.toString());
        }

    }

    /**
     * Add a single file or folder to the collection
     *
     * @param theFile path to file or folder
     */
    public void setFile(String theFile) {
        this.addFile(theFile, false);
    }


    /**
     * Remove file from the file list
     *
     * @param theFileName Full file name of file to remove
     */
    public void removeFile(String theFileName) {
        IdentificationFile comparisonFile = new IdentificationFile(theFileName);
        this.myFiles.remove(comparisonFile);
    }

    /**
     * Remove file from the file list
     *
     * @param theFileIndex Index of file to remove
     */
    public void removeFile(int theFileIndex) {
        this.myFiles.removeByIndex(theFileIndex);
    }

    public boolean containsIndex(int index) {
        return this.myFiles.containsKey(index);
    }

    /**
     * Empty file list
     */
    public void removeAll() {
        myFiles.clear();
    }

    /**
     * Gets the file name where file list stored
     *
     * @return file name where file list stored
     */
    public String getFileName() {
        return myFileName;
    }

    /**
     * Gets the IdentificationFile object by position in collection
     *
     * @param theIndex position of element in collection
     * @return Specified IdentificationFile object
     */
    public IdentificationFile getFile(int theIndex) {
        return (IdentificationFile) myFiles.get(theIndex);
    }


    /**
     * Get the number of files in the collection
     *
     * @return no. of files in the collection
     */
    public int getNumFiles() {
        return myFiles.size();
    }

    /**
     * Add a new identifier file to list (used when reading in an XML file collection file)
     *
     * @param theFile A new IdentificationFile object which will be populated from file
     */
    public void addIdentificationFile(IdentificationFile theFile) {
        myFiles.add(this.getNumFiles(), theFile);
    }


    /**
     * Populates the details of the FileCollection when read in from XML file
     *
     * @param theName  Name of the attribute read in
     * @param theValue Value of the attribute read in
     */
    public void setAttributeValue(String theName, String theValue) {
        if (theName.equals(JHOVE2IAnalysisController.LABEL_APPLICATION_VERSION)) {
            setDROIDVersion(theValue);
        } else if (theName.equals("SigFileVersion")) {
            setSignatureFileVersion(theValue);
        } else if (theName.equals(JHOVE2IAnalysisController.LABEL_DATE_CREATED)) {
            setDateCreated(theValue);
        } else {
            MessageDisplay.unknownAttributeWarning(theName, this.getElementName());
        }
    }

    public void setDROIDVersion(String value) {
        if (!value.equals(JHOVE2AnalysisControllerUtil.getDROIDVersion())) {
            MessageDisplay.generalWarning("This file was generated with DROID version " + value + ".  Current version is " + JHOVE2AnalysisControllerUtil.getDROIDVersion());
        }
    }

    public void setSignatureFileVersion(String value) {
        try {
            mySigFileVersion = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            MessageDisplay.generalWarning("The SigFileVersion attribute should be an integer");
        }
    }

    public void setDateCreated(String value) {
        // Ignore the contents of this element
    }

    /**
     * returns the signature file version recorded in the file collection file
     */
    public int getLoadedFileSigFileVersion() {
        return mySigFileVersion;
    }

}
