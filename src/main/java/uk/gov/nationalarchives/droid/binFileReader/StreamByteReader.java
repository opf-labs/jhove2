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
 * $Id: StreamByteReader.java,v 1.7 2006/03/13 15:15:28 linb Exp $
 *
 * $Log: StreamByteReader.java,v $
 * Revision 1.7  2006/03/13 15:15:28  linb
 * Changed copyright holder from Crown Copyright to The National Archives.
 * Added reference to licence.txt
 * Changed dates to 2005-2006
 *
 * Revision 1.6  2006/02/09 15:34:10  linb
 * Updates to javadoc and code following the code review
 *
 * Revision 1.5  2006/02/09 15:31:23  linb
 * Updates to javadoc and code following the code review
 *
 * Revision 1.4  2006/02/09 13:17:42  linb
 * Changed StreamByteReader to InputStreamByteReader
 * Refactored common code from UrlByteReader and InputStreamByteReader into new class StreamByteReader, from which they both inherit
 * Updated javadoc
 *
 * Revision 1.3  2006/02/09 12:14:16  linb
 * Changed some javadoc to allow it to be created cleanly
 *
 * Revision 1.2  2006/02/08 12:03:37  linb
 * - add more comments
 *
 * Revision 1.1  2006/02/08 11:45:48  linb
 * - add support for streams
 *
 *
 */

package uk.gov.nationalarchives.droid.binFileReader;

import uk.gov.nationalarchives.droid.IdentificationFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

/**
 * Class providing common functionality for reading streams.
 * <p/>
 * <p>This class cannot be instantiated (there is no constructor).
 *
 * @author linb
 */
public class StreamByteReader extends AbstractByteReader {

    /**
     * Creates a new instance of StreamByteReader
     */
    protected StreamByteReader(IdentificationFile theIDFile) {
        super(theIDFile);
    }

    /**
     * Size of buffer to store the stream in
     */
    private static final int BUFFER_SIZE = 131072;

    /**
     * Buffer to contain the contents of the stream.
     */
    protected ByteBuffer buffer = null;

    /**
     * This will be non-null if the stream has been written to a temporary file.
     */
    protected File tempFile = null;

    /**
     * Read stream into a <code>ByteBuffer</code> or temporary file.
     * <p/>
     * <p>This method allocates a buffer, and then attempts to read the stream into
     * it. If the buffer isn't big enough, the contents of it are transferred to a
     * temporary file, and then the rest of the stream is appended to this file.
     * <p/>
     * <p>After this method has been called, the field <code>tempFile</code> is
     * <code>null</code> if the contents of the stream could fit into the buffer,
     * and is the created temporary file otherwise.
     *
     * @param inStream the stream to read in.
     * @throws java.io.IOException if there is an error writing to the temporary
     *                             file
     */
    protected void readStream(InputStream inStream) throws IOException {
        ReadableByteChannel c = Channels.newChannel(inStream);
        if (buffer == null) {
            buffer = ByteBuffer.allocate(BUFFER_SIZE);
        } else {
            buffer.clear();
        }

        /*
        * Not all bytes in the channel are available at once.
        * Loop until end of file and while the buffer has space
        */
        int bytes = 0;
        while (bytes >= 0 && buffer.hasRemaining()) {
            bytes = c.read(buffer);
        }

        // Sets limit to current position, then position to start of the buffer.
        buffer.flip();

        if (buffer.limit() == 0) {
            this.setErrorIdent();
            this.setIdentificationWarning("Zero-length file");
            return;
        }

        if (bytes != -1) {
            // Haven't got the whole file
            // Write it to a temporary file
            tempFile = writeToTempFile(buffer, c);
        }
    }

    /**
     * Write contents of <code>buffer</code> to a temporary file, followed by the remaining bytes
     * in <code>channel</code>.
     * <p/>
     * <p>The bytes are read from <code>buffer</code> from the current position to its limit.
     *
     * @param buffer  contains the contents of the channel read so far
     * @param channel the rest of the channel
     * @return <code>File</code> object for the temporary file.
     * @throws java.io.IOException if there is a problem writing to the file
     */
    static File writeToTempFile(ByteBuffer buffer, ReadableByteChannel channel) throws IOException {
        File tempFile = java.io.File.createTempFile("droid", null);
        FileChannel fc = (new FileOutputStream(tempFile)).getChannel();
        ByteBuffer buf = ByteBuffer.allocate(8192);
        fc.write(buffer);
        buf.clear();
        for (; ;) {
            if (channel.read(buf) < 0) {
                break;        // No more bytes to transfer
            }
            buf.flip();
            fc.write(buf);
            buf.compact();    // In case of partial write
        }
        fc.close();
        return tempFile;
    }

    /**
     * Get a byte from file
     *
     * @param fileIndex position of required byte in the file
     * @return the byte at position <code>fileIndex</code> in the file
     */
    public byte getByte(long fileIndex) {
        return buffer.get((int) fileIndex);
    }


    /**
     * Gets the current position of the file marker.
     *
     * @return the current position of the file marker
     */
    public long getFileMarker() {
        return buffer.position();
    }


    /**
     * Returns the number of bytes in the file
     */
    public long getNumBytes() {
        return buffer == null ? 0 : buffer.limit();
    }


    /**
     * Position the file marker at a given byte position.
     * <p/>
     * <p>The file marker is used to record how far through the file
     * the byte sequence matching algorithm has got.
     *
     * @param markerPosition The byte number in the file at which to position the marker
     */
    public void setFileMarker(long markerPosition) {
        buffer.position((int) markerPosition);
    }


    public byte[] getbuffer() {
        return buffer.array();
    }
}
