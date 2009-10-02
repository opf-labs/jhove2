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
 * $Id: ByteReader.java,v 1.6 2006/03/13 15:15:28 linb Exp $
 *
 * $Log: ByteReader.java,v $
 * Revision 1.6  2006/03/13 15:15:28  linb
 * Changed copyright holder from Crown Copyright to The National Archives.
 * Added reference to licence.txt
 * Changed dates to 2005-2006
 *
 * Revision 1.5  2006/02/09 13:17:41  linb
 * Changed StreamByteReader to InputStreamByteReader
 * Refactored common code from UrlByteReader and InputStreamByteReader into new class StreamByteReader, from which they both inherit
 * Updated javadoc
 *
 * Revision 1.4  2006/02/09 12:14:16  linb
 * Changed some javadoc to allow it to be created cleanly
 *
 * Revision 1.3  2006/02/08 12:51:53  linb
 * Added javadoc comments for file.
 *
 * Revision 1.2  2006/02/08 08:56:35  linb
 * - Added header comments
 *
 */
package uk.gov.nationalarchives.droid.binFileReader;

import uk.gov.nationalarchives.droid.FileFormatHit;

/**
 * Interface for accessing the bytes from a file, URL or stream.
 * <p/>
 * Create an instance with <code>AbstractByteReader.newByteReader()</code>.
 *
 * @author linb
 */
public interface ByteReader {

    /* Setters for identification status */
    /**
     * Set identification status to Positive
     */
    public void setPositiveIdent();

    /**
     * Set identification status to Tentative
     */
    public void setTentativeIdent();

    /**
     * Set identification status to No identification
     */
    public void setNoIdent();

    /**
     * Set identification status to Error
     */
    public void setErrorIdent();

    /**
     * Checks whether the file has yet been classified
     */
    public boolean isClassified();

    /**
     * Get classification of the file
     */
    public int getClassification();

    /**
     * Set identification warning
     *
     * @param theWarning the warning message to use
     */
    public void setIdentificationWarning(String theWarning);

    /**
     * Get any warning message created when identifying this file
     */
    public String getIdentificationWarning();

    /**
     * Add another hit to the list of hits for this file.
     *
     * @param theHit The <code>FileFormatHit</code> to be added
     */
    public void addHit(FileFormatHit theHit);

    /**
     * Remove a hit from the list of hits for this file.
     *
     * @param theIndex Index of the hit to be removed
     */
    public void removeHit(int theIndex);

    /**
     * Get number of file format hits
     */
    public int getNumHits();

    /**
     * Get a file format hit
     *
     * @param theIndex index of the <code>FileFormatHit</code> to get
     * @return the hit associated with <code>theIndex</code>
     */
    public FileFormatHit getHit(int theIndex);

    /**
     * Get file path of the associated file
     */
    public String getFilePath();

    /**
     * Get file name of the associated file
     */
    public String getFileName();


    /**
     * Position the file marker at a given byte position.
     * <p/>
     * The file marker is used to record how far through the file
     * the byte sequence matching algorithm has got.
     *
     * @param markerPosition The byte number in the file at which to position the marker
     */
    public void setFileMarker(long markerPosition);

    /**
     * Gets the current position of the file marker.
     *
     * @return the current position of the file marker
     */
    public long getFileMarker();

    /**
     * Get a byte from file
     *
     * @param fileIndex position of required byte in the file
     * @return the byte at position <code>fileIndex</code> in the file
     */
    public byte getByte(long fileIndex);

    /**
     * Returns the number of bytes in the file
     */
    public long getNumBytes();
    /**
     * Returns the byte array buffer
     *
     * @return the buffer associated with the file
     */
    public byte[] getbuffer();

    /**
     * Closes any files associated with the ByteReader.
     */
    public void close();
}
