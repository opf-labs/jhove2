/**
 * 
 */
package org.jhove2.module.format.tiff;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteOrder;
import java.util.List;

import org.jhove2.core.Invocation;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.Message;
import org.jhove2.core.Message.Context;
import org.jhove2.core.Message.Severity;
import org.jhove2.core.format.Format;
import org.jhove2.core.io.Input;
import org.jhove2.core.source.FileSource;
import org.jhove2.core.source.Source;
import org.jhove2.core.source.ZipFileSource;
import org.jhove2.module.format.BaseFormatModule;
import org.jhove2.module.format.Validator;
import org.jhove2.module.format.utf8.unicode.Unicode.EOL;

/**
 * JHOVE2 TIFF module. This module parses a TIFF instance and captures selected
 * characterization information
 * 
 * @author mstrong
 *
 */
public class TiffModule 
    extends BaseFormatModule 
    implements Validator 
{

    /** UTF-8 module version identifier. */
    public static final String VERSION = "1.9.5";

    /** UTF-8 module release date. */
    public static final String RELEASE = "2010-02-16";

    /** UTF-8 module rights statement. */
    public static final String RIGHTS =
        "Copyright 2010 by The Regents of the University of California. " +
        "Available under the terms of the BSD license.";

    /** TIFF module validation coverage. */
    public static final Coverage COVERAGE = Coverage.Inclusive;

    /** TIFF Module validity status. */
    protected Validity isValid;

    /** TIFF IFH - Image File Header */
    protected IFH ifh = new IFH();
    
    /** TIFF Invalid Field Message */
    protected List<Message> invalidFieldMessage;
    
    /** TIFF version, defaults to 4.  As features are recognized, update the version accordingly */
    protected int version = 4;

    private Message failFastMessage;

    /**
     * Instantiate a new <code>TIFFModule</code>.
     * 
     * @param format
     *            TIFF format
     */
    public TiffModule(Format format) {
        super(VERSION, RELEASE, RIGHTS, format);
    }


    /**
     * Parse a source unit.
     * 
     * @param jhove2
     *            JHOVE2 framework
     * @param source
     *            TIFF source unit
     * @return 0
     * @throws EOFException
     *             If End-of-File is reached reading the source unit
     * @throws IOException
     *             If an I/O exception is raised reading the source unit
     * @throws JHOVE2Exception
     * @see org.jhove2.module.format.FormatModule#parse(org.jhove2.core.JHOVE2,
     *      org.jhove2.core.source.Source)
     */
    @Override
    public long parse(JHOVE2 jhove2, Source source)
        throws EOFException, IOException, JHOVE2Exception
    {
        long consumed = 0L;
        this.isValid = Validity.Undetermined;
        int numErrors = 0;
        Input input = null;
        try {
            Invocation config = jhove2.getInvocation();
            input = source.getInput(config.getBufferSize(), 
                    config.getBufferType());
            long start = 0L;
            long end = 0L;
            if (source instanceof FileSource) {
                end = ((FileSource) source).getSize();
            } else if (source instanceof ZipFileSource) {
                end = ((ZipFileSource) source).getSize();
            }
            ;
            input.setPosition(start);

            EOL eol = null;
            long position = start;

            // read the first two bytes to determine the endianess
            byte[] b = new byte[2];
            b[0] = input.readSignedByte();
            b[1] = input.readSignedByte();
            ByteOrder byteOrder = null;
            // test for little endian ("II")
            if ((b[0] != b[1]) && (b[0] == 0x49 || b[0] == 0x4D)) {
                this.isValid = Validity.False;
                numErrors++;
                //TODO: fix this message
                Object[]messageArgs = new Object[]{0, input.getPosition(), b[0]};
                this.invalidFieldMessage.add(new Message(Severity.ERROR,
                        Context.OBJECT,
                        "org.jhove2.module.format.tiff.TIFFModule.invalidFieldMessages",
                        messageArgs, jhove2.getConfigInfo()));
            }
            
            if (b[0] == 0x49) {  // 'I'
                byteOrder = ByteOrder.LITTLE_ENDIAN;
            }
            else if (b[0] ==0x4D) {  // 'M'
                byteOrder = ByteOrder.BIG_ENDIAN;
            }
            
            ifh.setByteOrdering(new String(b));
            ifh.setByteOrder(byteOrder);

            // set the endianess so subsequent reads are the correct endianess
            input.setByteOrder(byteOrder);
            
            int magic = input.readUnsignedShort();
            if (magic != 43 || magic != 42) {
                //TODO:  error message
                // invalid TIFF File
            }
            else if(magic == 43) {
                // we got a Big TIFF here
            }
            ifh.setMagicNumber(magic);
                        
            long offset = input.readUnsignedInt();
            if (offset == 0L) {
                // no IFD in file
                this.isValid = Validity.False;
                Object[]messageArgs = new Object[]{0, input.getPosition(), offset};
                this.invalidFieldMessage.add(new Message(Severity.ERROR,
                        Context.OBJECT,
                        "org.jhove2.module.format.tiff.TIFFModule.NoIFDInTIFFFileMessage",
                        messageArgs, jhove2.getConfigInfo()));               
            }
            
            if ((offset & 1) != 0) {
                this.isValid = Validity.False;
                Object[]messageArgs = new Object[]{0, input.getPosition(), offset};
                this.invalidFieldMessage.add(new Message(Severity.ERROR,
                        Context.OBJECT,
                        "org.jhove2.module.format.tiff.TIFFModule.ByteOffsetNotWordAlignedMessage",
                        messageArgs, jhove2.getConfigInfo()));               
            }
            /* Parse the IFDs */
            IFD ifd = new IFD();
            ifd.parse(jhove2, input, offset);
            
        } catch (EOFException e) {
            // TODO:  Report error message Premature EOF
            this.isValid = Validity.False;
        }
        
       
        finally {
            if (input != null) {
                input.close();
            }
        }

        return consumed;
    }

    /**
     * Get module validation coverage.
     * 
     * @return the coverage
     */
    @Override
    public Coverage getCoverage() {
        return COVERAGE;
    }

    /**
     * Validate a TIFF source unit.
     * 
     * @param jhove2
     *            JHOVE2 framework
     * @param source
     *            TIFF source unit
     * @return UTF-8 validation status
     * @see org.jhove2.module.format.Validator#validate(org.jhove2.core.JHOVE2,
     *      org.jhove2.core.source.Source)
     *      
     */
   @Override
    public Validity validate(JHOVE2 jhove2, Source source) throws JHOVE2Exception {
        return this.isValid;
    }
    /**
     * Get TIFF validation status.
     * 
     * @return TIFF validation status
     * @see org.jhove2.module.format.Validator#isValid()
     */
    @Override
    public Validity isValid() {
        return this.isValid;
    }

}
