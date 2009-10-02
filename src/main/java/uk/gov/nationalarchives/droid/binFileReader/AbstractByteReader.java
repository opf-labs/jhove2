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
 * Tessella/NPD/4826
 * PRONOM 5a
 *
 * $Id: AbstractByteReader.java,v 1.9 2006/03/13 15:15:28 linb Exp $
 *
 * $Log: AbstractByteReader.java,v $
 * Revision 1.9  2006/03/13 15:15:28  linb
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
 * Revision 1.5  2006/02/08 12:51:52  linb
 * Added javadoc comments for file.
 *
 * Revision 1.4  2006/02/08 12:03:37  linb
 * - add more comments
 *
 * Revision 1.3  2006/02/08 11:45:48  linb
 * - add support for streams
 *
 * Revision 1.2  2006/02/08 08:58:09  linb
 * - Added header comments
 *
 * SMM for JHOVE2 2009.09.22
 *   added new static method 
 *   public static ByteReader newByteReader(IdentificationFile theIDFile, InputStream inputStream)
 *   to allow JHOVE2 to pass InputStream rather than just file objects to be identified by DROID
 */

package uk.gov.nationalarchives.droid.binFileReader;

import java.io.InputStream;

import uk.gov.nationalarchives.droid.FileFormatHit;
import uk.gov.nationalarchives.droid.IdentificationFile;

/**
 * Abstract base class for the ByteReader interface.
 * <p/>
 * This implements the methods that are passed on to the <code>IdentificationFile</code>
 * object.
 *
 * @author linb
 */
public abstract class AbstractByteReader implements ByteReader {

    /**
     * The file represented by this object
     */
    protected IdentificationFile myIDFile;

    /**
     * Creates a ByteReader object, and depending on the readFile setting,
     * it may or may not read in the binary file specified
     *
     * @param theIDFile The file to be read in
     */
    protected AbstractByteReader(IdentificationFile theIDFile) {
        myIDFile = theIDFile;
    }

    /**
     * Creates a ByteReader object, and depending on the readFile setting,
     * it may or may not read in the binary file specified
     *
     * @param theIDFile The file to be read in
     * @return
     */
    public static ByteReader newByteReader(IdentificationFile theIDFile) {
        return newByteReader(theIDFile, true);
    }

    /**
     * Static constructor for a ByteReader object, and depending on the readFile setting,
     * it may or may not read in the binary file specified.
     * <p/>
     * This may create a FileByteReader, UrlByteReader or InputStreamByteReader, depending on the
     * the nature of the IdentificationFile passed in.
     *
     * @param theIDFile The file to be read in
     * @param readFile  Flag specifying whether file should be read in or not
     * @return
     */
    public static ByteReader newByteReader(IdentificationFile theIDFile, boolean readFile) {
        if (InputStreamByteReader.isInputStream(theIDFile.getFilePath())) {
            return InputStreamByteReader.newInputStreamByteReader(theIDFile, readFile);
        } else if (UrlByteReader.isURL(theIDFile.getFilePath())) {
            return UrlByteReader.newUrlByteReader(theIDFile, readFile);
        } else {
            return new FileByteReader(theIDFile, readFile);
        }

    }

   /// ****************************ADDED FOR JHOVE2 *********************************************
    /**
     * Static constructor for a ByteReader object, and depending on the readFile setting,
     * it may or may not read in the binary file specified.
     * <p/>
     * This will create InputStreamByteReader, if the FilePath of the IdentificationFile has been set
     * to "-"; otherwise it will invoke the DROID static method newByteReader(theIDFile)
     * It will read the underlying InputStream
     *
     * @param theIDFile The file to be read in
     * @param inputStream  InputStream to be turned into an InputStreamByteReader and read
     * @return
     */
    public static ByteReader newByteReader(IdentificationFile theIDFile, InputStream inputStream) {
    	if (InputStreamByteReader.isInputStream(theIDFile.getFilePath())) {
            return InputStreamByteReader.newInputStreamByteReader(theIDFile, inputStream);
        } else {
            return newByteReader(theIDFile);
        }
    }
    /// **************************** END ADDED FOR JHOVE2 *********************************************
    
    
    /* Setters for identification status */
    /**
     * Set identification status to Positive
     */
    public void setPositiveIdent() {
        this.myIDFile.setPositiveIdent();
    }

    /**
     * Set identification status to Tentative
     */
    public void setTentativeIdent() {
        this.myIDFile.setTentativeIdent();
    }

    /**
     * Set identification status to No identification
     */
    public void setNoIdent() {
        this.myIDFile.setNoIdent();
    }

    /**
     * Set identification status to Error
     */
    public void setErrorIdent() {
        this.myIDFile.setErrorIdent();
    }

    /**
     * Checks whether the file has yet been classified
     */
    public boolean isClassified() {
        return this.myIDFile.isClassified();
    }

    /**
     * Get classification of the file
     */
    public int getClassification() {
        return this.myIDFile.getClassification();
    }

    /**
     * Set identification warning
     *
     * @param theWarning the warning message to use
     */
    public void setIdentificationWarning(String theWarning) {
        this.myIDFile.setWarning(theWarning);
    }

    /**
     * Get any warning message created when identifying this file
     */
    public String getIdentificationWarning() {
        return myIDFile.getWarning();
    }

    /**
     * Add another hit to the list of hits for this file.
     *
     * @param theHit The <code>FileFormatHit</code> to be added
     */
    public void addHit(FileFormatHit theHit) {
        this.myIDFile.addHit(theHit);
    }

    /**
     * Remove a hit from the list of hits for this file.
     *
     * @param theIndex Index of the hit to be removed
     */
    public void removeHit(int theIndex) {
        this.myIDFile.removeHit(theIndex);
    }

    /**
     * Get number of file format hits
     */
    public int getNumHits() {
        return myIDFile.getNumHits();
    }

    /**
     * Get a file format hit
     *
     * @param theIndex index of the <code>FileFormatHit</code> to get
     * @return the hit associated with <code>theIndex</code>
     */
    public FileFormatHit getHit(int theIndex) {
        return myIDFile.getHit(theIndex);
    }


    /**
     * Get file path of the associated file
     */
    public String getFilePath() {
        return this.myIDFile.getFilePath();
    }

    /**
     * Get file name of the associated file
     */
    public String getFileName() {
        return this.myIDFile.getFileName();
    }
    
        /**
     * Closes any files associated with the ByteReader.
     *
     * Empty implementation - must be overridden by classes that hold files
     * open.
     */
    public void close() {
    }

}
