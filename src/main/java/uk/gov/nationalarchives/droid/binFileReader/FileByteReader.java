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
 * $Id: FileByteReader.java,v 1.8 2006/03/13 15:15:28 linb Exp $
 *
 * $Log: FileByteReader.java,v $
 * Revision 1.8  2006/03/13 15:15:28  linb
 * Changed copyright holder from Crown Copyright to The National Archives.
 * Added reference to licence.txt
 * Changed dates to 2005-2006
 *
 * Revision 1.7  2006/02/09 15:34:10  linb
 * Updates to javadoc and code following the code review
 *
 * Revision 1.5  2006/02/09 15:31:23  linb
 * Updates to javadoc and code following the code review
 *
 * Revision 1.5  2006/02/09 13:17:42  linb
 * Changed StreamByteReader to InputStreamByteReader
 * Refactored common code from UrlByteReader and InputStreamByteReader into new class StreamByteReader, from which they both inherit
 * Updated javadoc
 *
 * Revision 1.4  2006/02/09 12:14:16  linb
 * Changed some javadoc to allow it to be created cleanly
 *
 * Revision 1.3  2006/02/08 08:56:35  linb
 * - Added header comments
 * 
 *   * *****************************************
 * S. Morrissey For JHOVE2  Date 09/12/2009
 * refactored to use IAnalaysis Controller for constants, 
 * and AnalysisControllerUtil for static methods
 * 
 */

package uk.gov.nationalarchives.droid.binFileReader;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import uk.gov.nationalarchives.droid.IdentificationFile;
import uk.gov.nationalarchives.droid.JHOVE2IAnalysisController;

/**
 * The <code>FileByteReader</code> class is a <code>ByteReader</code> that
 * reads its data from a file.
 * <p/>
 * <p>This class can have two files associated with it: The file represented by it
 * (its <code>IdentificationFile</code>) and a (possibly different) backing file.
 * The purpose of this separation is so that this object can represent a URL that
 * has been downloaded or an InputStream that has been saved to disk.
 *
 * @author linb
 */
public class FileByteReader extends AbstractByteReader {
	// added for jHOVE2
	boolean isTempFile = false;

	// added for jHOVE2
    FileByteReader(IdentificationFile theIDFile, boolean readFile, String filePath, boolean isTempFile) {   	
    	super(theIDFile);
        this.file = new File(filePath);
        if (readFile) {
            this.readFile();
        }
        this.isTempFile = isTempFile;
    }
    
    /**
     * Creates a new instance of FileByteReader
     * <p/>
     * <p>This constructor can set the <code>IdentificationFile</code> to
     * a different file than the actual file used. For example, if <code>theIDFile</code>
     * is a URL or stream, and is too big to be buffered in memory, it could be written
     * to a temporary file.  This file would then be used as a backing file to store
     * the data.
     *
     * @param theIDFile the file represented by this object
     * @param readFile  <code>true</code> if the file is to be read
     * @param filePath  the backing file (containing the data)
     */
    FileByteReader(IdentificationFile theIDFile, boolean readFile, String filePath) {
//        super(theIDFile);
//        this.file = new File(filePath);
//        if (readFile) {
//            this.readFile();
//        }
    	// changed for JHOVE2
    	this(theIDFile, readFile, filePath, false);
    }

    /**
     * Creates a new instance of FileByteReader
     * <p/>
     * <p>This constructor uses the same file to contain the data as is specified by
     * <code>theIDFile</code>.
     *
     * @param theIDFile the source file from which the bytes will be read.
     * @param readFile  <code>true</code> if the file is to be read
     */
    FileByteReader(IdentificationFile theIDFile, boolean readFile) {
        this(theIDFile, readFile, theIDFile.getFilePath());
    }

    private int randomFileBufferSize = JHOVE2IAnalysisController.FILE_BUFFER_SIZE;
    private boolean isRandomAccess = false;


    protected byte[] fileBytes;
    private long numBytes;
    private long fileMarker;

    private RandomAccessFile randomAccessFile = null;
    private long rAFoffset = 0L;

    private static final int MIN_RAF_BUFFER_SIZE = 1000000;
    private static final int RAF_BUFFER_REDUCTION_FACTOR = 2;
    private File file;


    public boolean isRandomAccess() {
        return isRandomAccess;
    }


    public int getRandomFileBufferSize() {
        return randomFileBufferSize;
    }


    public RandomAccessFile getRandomAccessFile() {
        return randomAccessFile;
    }

    /**
     * Reads in the binary file specified.
     * <p/>
     * <p>If there are any problems reading in the file, it gets classified as unidentified,
     * with an explanatory warning message.
     */
    private void readFile() {

        //If file is not readable or is empty, then it gets classified
        //as unidentified (with an explanatory warning)

        if (!file.exists()) {
            this.setErrorIdent();
            this.setIdentificationWarning("File does not exist");
            return;
        }

        if (!file.canRead()) {
            this.setErrorIdent();
            this.setIdentificationWarning("File cannot be read");
            return;
        }

        if (file.isDirectory()) {
            this.setErrorIdent();
            this.setIdentificationWarning("This is a directory, not a file");
            return;
        }

        FileInputStream binStream;
        try {
            binStream = new FileInputStream(file);
        } catch (FileNotFoundException ex) {
            this.setErrorIdent();
            this.setIdentificationWarning("File disappeared or cannot be read");
            return;
        }

        BufferedInputStream buffStream = null;
        try {

            int numBytes = binStream.available();

            if (numBytes > 0) {
                fileBytes = new byte[numBytes];
                
                buffStream = new BufferedInputStream(binStream);
                int len = buffStream.read(fileBytes, 0, numBytes);

                if (len != numBytes) {
                    //This means that all bytes were not successfully read
                    this.setErrorIdent();
                    this.setIdentificationWarning("Error reading file: " + len + " bytes read from file when " + numBytes + " were expected");
                } else if (buffStream.read() != -1) {
                    //This means that the end of the file was not reached
                    this.setErrorIdent();
                    this.setIdentificationWarning("Error reading file: Unable to read to the end");
                } else {
                    this.numBytes = (long) numBytes;
                }
            } else {
                //If file is empty , status is error
                //this.setErrorIdent();
                this.numBytes = 0L;
                this.setIdentificationWarning("Zero-length file");
            }
            isRandomAccess = false;
        } catch (IOException e) {
            this.setErrorIdent();
            this.setIdentificationWarning("Error reading file: " + e.toString());
        } catch (OutOfMemoryError e) {
            try {
                randomAccessFile = new RandomAccessFile(file, "r");
                isRandomAccess = true;

                //record the file size
                numBytes = randomAccessFile.length();
                //try reading in a buffer
                randomAccessFile.seek(0L);
                boolean tryAgain = true;
                while (tryAgain) {
                    try {
                        fileBytes = new byte[randomFileBufferSize];
                        randomAccessFile.read(fileBytes);
                        tryAgain = false;
                    } catch (OutOfMemoryError e4) {
                        randomFileBufferSize = randomFileBufferSize / RAF_BUFFER_REDUCTION_FACTOR;
                        if (randomFileBufferSize < MIN_RAF_BUFFER_SIZE) {
                            throw e4;
                        }
                    }
                }
                rAFoffset = 0L;
            } catch (FileNotFoundException e2) {
                this.setErrorIdent();
                this.setIdentificationWarning("File disappeared or cannot be read");
            } catch (Exception e2) {
                this.setErrorIdent();
                this.setIdentificationWarning("Error reading file: " + e2.toString());
            }
        } finally {
            if (buffStream != null) {
                try {
                    buffStream.close();
                } catch (IOException e) {
                    this.setErrorIdent();
                    this.setIdentificationWarning("Unable to close file: " + e.getMessage());
                }
            }
            if (binStream != null) {
                try {
                    binStream.close();
                } catch (IOException e) {
                    this.setErrorIdent();
                    this.setIdentificationWarning("Unable to close file: " + e.getMessage());
                }
            }
            if (randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                } catch (IOException e) {
                    this.setErrorIdent();
                    this.setIdentificationWarning("Unable to close file: " + e.getMessage());
                }
            }
        }
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
        if ((markerPosition < -1L) || (markerPosition > this.getNumBytes())) {
            throw new IllegalArgumentException("  Unable to place a fileMarker at byte "
                    + Long.toString(markerPosition) + " in file " + this.myIDFile.getFilePath() + " (size = " + Long.toString(this.getNumBytes()) + " bytes)");
        } else {
            this.fileMarker = markerPosition;
        }
    }

    /**
     * Gets the current position of the file marker.
     *
     * @return the current position of the file marker
     */
    public long getFileMarker() {
        return this.fileMarker;
    }

    /**
     * Get a byte from file
     *
     * @param fileIndex position of required byte in the file
     * @return the byte at position <code>fileIndex</code> in the file
     */
    public byte getByte(long fileIndex) {

        byte theByte = 0;
        if (isRandomAccess) {
            //If the file is being read via random acces,
            //then read byte from buffer, otherwise read in a new buffer.
            long theArrayIndex = fileIndex - rAFoffset;
            if (fileIndex >= rAFoffset && theArrayIndex < randomFileBufferSize) {
                theByte = fileBytes[(int) (theArrayIndex)];
            } else {
                try {
                    //Create a new buffer:
                    /*
                    //When a new buffer is created, the requesting file position is
                    //taken to be the middle of the buffer.  This is so that it will
                    //perform equally well whether the file is being examined from
                    //start to end or from end to start
                    rAFoffset = fileIndex - (myRAFbuffer/2);
                    if(rAFoffset<0L) {
                        rAFoffset = 0L;
                    }
                    System.out.println("    re-read file buffer");
                    randomAccessFile.seek(rAFoffset);
                    randomAccessFile.read(fileBytes);
                    theByte = fileBytes[(int)(fileIndex-rAFoffset)];
                     */
                    if (fileIndex < randomFileBufferSize) {
                        rAFoffset = 0L;
                    } else if (fileIndex < rAFoffset) {
                        rAFoffset = fileIndex - randomFileBufferSize + 1;
                    } else {
                        rAFoffset = fileIndex;
                    }
                    //System.out.println("    re-read file buffer from "+rAFoffset+ " for "+myRAFbuffer+" bytes");
                    //System.out.println("    seek start");
                    randomAccessFile.seek(rAFoffset);
                    //System.out.println("        read start");
                    randomAccessFile.read(fileBytes);
                    //System.out.println(fileIndex);

                    //System.out.println("            read end");
                    theByte = fileBytes[(int) (fileIndex - rAFoffset)];

                } catch (Exception e) {
                    //
                }
            }
        } else {
            //If the file is not being read by random access, then the byte should be in the buffer array
            if (fileBytes != null) {
                theByte = fileBytes[(int) fileIndex];
            }
        }
        return theByte;
    }


    /**
     * Returns the number of bytes in the file
     */
    public long getNumBytes() {
        return numBytes;
    }
    /**
     * Returns the byte array buffer
     *
     * @return the buffer associated with the file
     */
    public byte[] getbuffer() {
        return fileBytes;
    }
    
        /**
     * Closes any input files that are open.
     */
    public void close() {
        if (randomAccessFile != null) {
            try {
                randomAccessFile.close();
            } catch (IOException e) {
                this.setErrorIdent();
                this.setIdentificationWarning("Unable to close file: " + e.getMessage());
            }
            randomAccessFile = null;
        }
        // added for JHOVE2
        if (this.file != null && this.isTempFile){
        	try{
        		file.delete();
        	}
        	catch(Exception e){
        		
        	}
        }
        // end added for JHOVE2
    }
}
