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
 * Tessella/NPD/4826
 * PRONOM 5a
 *
 * $Id: UrlByteReader.java,v 1.9 2006/03/13 15:15:28 linb Exp $
 *
 * $Log: UrlByteReader.java,v $
 * Revision 1.9  2006/03/13 15:15:28  linb
 * Changed copyright holder from Crown Copyright to The National Archives.
 * Added reference to licence.txt
 * Changed dates to 2005-2006
 *
 * Revision 1.8  2006/02/09 15:31:23  linb
 * Updates to javadoc and code following the code review
 *
 * Revision 1.7  2006/02/09 13:17:42  linb
 * Changed StreamByteReader to InputStreamByteReader
 * Refactored common code from UrlByteReader and InputStreamByteReader into new class StreamByteReader, from which they both inherit
 * Updated javadoc
 *
 * Revision 1.6  2006/02/09 12:14:16  linb
 * Changed some javadoc to allow it to be created cleanly
 *
 * Revision 1.5  2006/02/08 12:03:37  linb
 * - add more comments
 *
 * Revision 1.4  2006/02/08 11:45:49  linb
 * - add support for streams
 *
 * Revision 1.3  2006/02/08 08:56:35  linb
 * - Added header comments
 *
 */

package uk.gov.nationalarchives.droid.binFileReader;

import uk.gov.nationalarchives.droid.IdentificationFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * The <code>UrlByteReader</code> class is a <code>ByteReader</code> that
 * reads its data from a URL.
 *
 * @author linb
 */
public class UrlByteReader extends StreamByteReader {

    /**
     * Creates a new instance of UrlByteReader
     */
    private UrlByteReader(IdentificationFile theIDFile, boolean readFile) {
        super(theIDFile);
        if (readFile) {
            this.readUrl();
        }

    }

    /**
     * Static constructor for class.  Trys to read url into a buffer. If it doesn't fit,
     * save it to a file, and return a FileByteReader with that file.
     */
    static ByteReader newUrlByteReader(IdentificationFile theIDFile, boolean readFile) {
        UrlByteReader byteReader = new UrlByteReader(theIDFile, readFile);
        if (byteReader.tempFile == null) {
            return byteReader;
        } else {
            return new FileByteReader(theIDFile, readFile, byteReader.tempFile.getPath());
        }
    }

    /**
     * Read data into buffer or temporary file from the url specified by <code>theIDFile</code>.
     */
    private void readUrl() {
        URL url;
        try {
            url = new URL(myIDFile.getFilePath());
        } catch (MalformedURLException ex) {
            this.setErrorIdent();
            this.setIdentificationWarning("URL is malformed");
            return;
        }
        try {
            readStream(url.openStream());
        } catch (IOException ex) {
            this.setErrorIdent();
            this.setIdentificationWarning("URL could not be read");
        }
    }

    /**
     * Get a <code>URL<code> object for this path
     *
     * @param path the path for which to get the URL
     * @return the URL represented by <code>path</code> or <code>null</code> if
     *         it cannot be represented
     */
    public static URL getURL(String path) {
        URL url = null;
        try {
            url = new URL(path);
            if (url.getProtocol().equalsIgnoreCase("http")) {
                return url;
            } else {
                return null;
            }

        } catch (MalformedURLException ex) {
            return null;
        }

    }

    /**
     * Check for a valid URL
     *
     * @param path the URL to check
     * @return <code>true</code> if <code>path</code> is a valid URL
     */
    public static boolean isURL(String path) {

        URL url = null;
        try {
            url = new URL(path);
            if (url.getProtocol().equalsIgnoreCase("http")) {
                return true;
            }

        } catch (MalformedURLException ex) {
            return false;
        }

        return false;

    }
}
