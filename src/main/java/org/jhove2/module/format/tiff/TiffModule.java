/**
 * 
 */
package org.jhove2.module.format.tiff;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteOrder;
import java.util.LinkedList;
import java.util.List;

import org.jhove2.annotation.ReportableProperty;
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

    /** TIFF module version identifier. */
    public static final String VERSION = "2.0.0";

    /** TIFF module release date. */
    public static final String RELEASE = "2010-07-31";

    /** TIFF module rights statement. */
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

    /** TIFF Invalid Field Message */
    protected List<Message> invalidFirstTwoBytesMessage;

    /** TIFF Invalid Field Message */
    protected List<Message> PrematureEOFMessage;

    /** TIFF Invalid Field Message */
    protected List<Message> invalidMagicNumberMessage;

    /** TIFF Invalid Field Message */
    protected List<Message> ByteOffsetNotWordAlignedMessage;

    /** TIFF version, defaults to 4.  As features are recognized, update the version accordingly */
    protected int version = 4;

    /** List of IFDs */
    List<IFD> ifdList = new LinkedList<IFD>();

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
     * returns the list of IFDs for this TIFF object
     * 
     * @return List<IFD>
     */
    @ReportableProperty(order = 2, value="IFDs.")
    public List<IFD> getIFDs() {
        return ifdList;
    }

    /**
     * 
     * @return IFH Image File Header
     */
    @ReportableProperty(order = 1, value="IFH")
    public IFH getIFH() {
        return ifh;
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

        try {
            input.setPosition(start);

            // read the first two bytes to determine the endianess
            byte[] b = new byte[2];
            b[0] = input.readSignedByte();
            b[1] = input.readSignedByte();
            ByteOrder byteOrder = null;

            /* validate first 2 bytes */
            if ((b[0] != b[1]) && 
                (b[0] == 0x49 || b[0] == 0x4D)) {
                this.isValid = Validity.False;
                numErrors++;
                Object[]messageArgs = new Object[]{0, input.getPosition(), b[0]};
                this.invalidFirstTwoBytesMessage.add(new Message(Severity.ERROR,
                        Context.OBJECT,
                        "org.jhove2.module.format.tiff.TIFFModule.invalidFirstTwoBytesMessage",
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
            
            /* set the endianess so subsequent reads are the correct endianess */
            input.setByteOrder(byteOrder);

            int magic = input.readUnsignedShort();
            if (magic != 43 && magic != 42) {
                Object[]messageArgs = new Object[]{magic};
                this.invalidMagicNumberMessage.add(new Message(Severity.ERROR,
                        Context.OBJECT,
                        "org.jhove2.module.format.tiff.TIFFModule.invalidMagicNumberMessage",
                        messageArgs, jhove2.getConfigInfo()));

            }
            else if(magic == 43) {
                // we got a Big TIFF here
            }
            ifh.setMagicNumber(magic);

            ifdList = parseIFDs(jhove2, input);  
            
            /* loop through IfdList and validate each one */

        } catch (EOFException e) {
            this.isValid = Validity.False;
            this.PrematureEOFMessage.add(new Message(Severity.ERROR,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.TIFFModule.PrematureEOFMessage",
                    jhove2.getConfigInfo()));       
            throw new JHOVE2Exception("TiffModuel.parse(): EOFException", e);
        }
        finally {
            if (input != null) {
                input.close();
            }
        }
        return 0;
    }

    /** 
     * parse the IFD validating that there is at least one offset
     * and that it is word-aligned.  Following the offset
     * of the first IFD, parse the linked list of IFDs 
     *  
     * @throws JHOVE2Exception 
     * 
     */
    private List<IFD> parseIFDs(JHOVE2 jhove2, Input input) throws JHOVE2Exception{
        long offset = 0L;
        try {
            /* read the offset to the 0th IFD */
            offset = input.readUnsignedInt();
            ifh.setFirstIFD(offset);

            /* must have at least 1 IFD */
            if (offset == 0L) {
                this.isValid = Validity.False;
                this.invalidFieldMessage.add(new Message(Severity.ERROR,
                        Context.OBJECT,
                        "org.jhove2.module.format.tiff.TIFFModule.NoIFDInTIFFFileMessage",
                        jhove2.getConfigInfo()));               
            }

            /* offset must be word aligned (even number) */
            if ((offset & 1) != 0) {
                this.isValid = Validity.False;
                Object[]messageArgs = new Object[]{0, input.getPosition(), offset};
                this.ByteOffsetNotWordAlignedMessage.add(new Message(Severity.ERROR,
                        Context.OBJECT,
                        "org.jhove2.module.format.tiff.TIFFModule.ByteOffsetNotWordAlignedMessage",
                        messageArgs, jhove2.getConfigInfo()));               
            }
        }
        catch (IOException e) {
            throw new JHOVE2Exception ("TiffModule.parseIFDs(): IOException",e);
        }

        /* Parse the list of IFDs */                  
        List list = new LinkedList();
        long nextIfdOffset = offset;
        while (nextIfdOffset != 0L) {
            IFD ifd = parseIFDList(nextIfdOffset, list, jhove2, input);
            nextIfdOffset  = ifd.getNextIFD(); 
        }
        return list;

    }


    /** 
     * following the offsets, process the list of IFDs in the TIFF file
     * 
     * @param offset 
     * @throws JHOVE2Exception 
     * */
    private IFD parseIFDList(long ifdOffset, List<IFD> list, JHOVE2 jhove2, Input input) throws JHOVE2Exception {

        IFD ifd = new TiffIFD(input);  

        TiffTag.getTiffTags(IFD.getTiffTags(jhove2.getConfigInfo()));
        TiffType.getTiffTypes(IFD.getTiffType(jhove2.getConfigInfo()));

        ifd.setOffset(ifdOffset);

        try {
            /* parse for the appropriate IFD type */
            ifd.parse(jhove2, input);

            if (ifdList.size () == 0) {
                ifd.setFirst (true);
            }
            else if (ifdList.size() == 1 ) {
                // For some profiles, the second IFD is assumed to
                // be the thumbnail.  This may not be valid under
                // all circumstances.
                ifd.setThumbnail (true);
            }
            list.add(ifd);
            version = ifd.getVersion();

            // TODO:  parse subIFDs chains here

            // TODO: parse EXIF/GPS/InterOP/GlobalParms IFDChains here
        }  

        catch (EOFException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (JHOVE2Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
        return ifd;
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
