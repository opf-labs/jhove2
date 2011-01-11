/**
 * JHOVE2 - Next-generation architecture for format-aware characterization
 * <p>
 * Copyright (c) 2010 by The Regents of the University of California. All rights reserved.
 * </p>
 * <p>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * </p>
 * <ul>
 * <li>Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.</li>
 * <li>Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.</li>
 * <li>Neither the name of the University of California/California Digital
 * Library, Ithaka Harbors/Portico, or Stanford University, nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.</li>
 * </ul>
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * </p>
 */

package org.jhove2.module.format.tiff;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteOrder;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.Message;
import org.jhove2.core.Message.Context;
import org.jhove2.core.Message.Severity;
import org.jhove2.core.format.Format;
import org.jhove2.core.io.Input;
import org.jhove2.core.source.Source;
import org.jhove2.module.format.BaseFormatModule;
import org.jhove2.module.format.Validator;
import org.jhove2.persist.FormatModuleAccessor;

import com.sleepycat.persist.model.Persistent;

/**
 * JHOVE2 TIFF module. This module parses a TIFF instance and captures selected
 * characterization information
 * 
 * @author mstrong
 *
 */
@Persistent
public class TiffModule 
       extends BaseFormatModule 
       implements Validator 
{
    /** TIFF module version identifier. */
    public static final String VERSION = "2.0.0";

    /** TIFF module release date. */
    public static final String RELEASE = "2010-09-10";

    /** TIFF module rights statement. */
    public static final String RIGHTS =
        "Copyright 2010 by The Regents of the University of California. " +
        "Available under the terms of the BSD license.";

    /** TIFF module validation coverage. */
    public static final Coverage COVERAGE = Coverage.Inclusive;

    /** TIFF Module validity status. */
    protected Validity validity;

    /** TIFF IFH - Image File Header */
    protected IFH ifh = new IFH();

    /** Fail fast message. */
    protected Message failFastMessage;

    /** TIFF Invalid Field Message */
    protected List<Message> invalidFieldMessage;

    /** TIFF Invalid Field Message */
    protected List<Message> invalidFirstTwoBytesMessage;

    /** TIFF Invalid Field Message */
    protected List<Message> prematureEOFMessage;

    /** TIFF Invalid Field Message */
    protected List<Message> invalidMagicNumberMessage;

    /** TIFF Invalid Field Message */
    protected List<Message> byteOffsetNotWordAlignedMessage;

    /** TIFF version, defaults to 4.  As features are recognized, update the version accordingly */
    protected int version = 4;

    /** List of IFDs */
    List<IFD> ifdList = new LinkedList<IFD>();

    /** The JHOVE2 object passed in by the parse method */
    protected JHOVE2 jhove2; 

    /** The Source object passed in by the parse method */
    protected  Source source;

    /** Map from tags to formats for the content of the tags. */
    public static Map<Integer, Format> tagToFormatMap;
    
    /**
     * Instantiate a new <code>TIFFModule</code>.
     * 
     * @param format
     *            TIFF format
     * @param formatModuleAccessor 
     *       FormatModuleAccessor to manage access to Format Profiles
     */
    public TiffModule(Format format, 
    		FormatModuleAccessor formatModuleAccessor) {
        super(VERSION, RELEASE, RIGHTS, format, formatModuleAccessor);
		this.validity = Validity.Undetermined;
        
        tagToFormatMap = new ConcurrentHashMap<Integer, Format>();
    }
    
    public TiffModule() {
        this(null, null);
    }

    /**
     * Parse a source unit.
     * 
     * @param jhove2
     *            JHOVE2 framework
     * @param source
     *            TIFF source unit
     * @param input
     *            TIFF source input
     * @return Number of bytes consumed
     * @throws EOFException
     *             If End-of-File is reached reading the source unit
     * @throws IOException
     *             If an I/O exception is raised reading the source unit
     * @throws JHOVE2Exception
     * @see org.jhove2.module.format.Parser#parse(org.jhove2.core.JHOVE2,
     *      org.jhove2.core.source.Source, org.jhove2.core.io.Input)
     */
    @Override
    public long parse(JHOVE2 jhove2, Source source, Input input)
    throws EOFException, IOException, JHOVE2Exception
    {
        long consumed = 0L;
        this.validity = Validity.Undetermined;
        
        /* initialize the tiff tags */
        TiffTag.getTiffTags(jhove2);

        int numErrors = 0;
        long start  = source.getStartingOffset();

        input.setPosition(start);
        try {
            // read the first two bytes to determine the endianess
            byte[] b = new byte[2];
            b[0] = input.readSignedByte();
            b[1] = input.readSignedByte();
            consumed +=2;
            ByteOrder byteOrder = null;

            /* validate first 2 bytes */
            if ((b[0] != b[1]) && 
                (b[0] == 0x49 || b[0] == 0x4D)) {
                this.validity = Validity.False;
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
            consumed +=2;
            if (magic != 43 && magic != 42) {
                this.validity = Validity.False;
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
            ifdList = parseIFDList(jhove2, source, input);  

            /* loop through IfdList and validate each one */
            for (IFD ifd:ifdList){
                if (ifd instanceof TiffIFD) {
                    ((TiffIFD) ifd).postParse();
                    ifd.validate(jhove2, source);
                    }
                }
            }
        catch (EOFException e) {
            this.validity = Validity.False;
            this.prematureEOFMessage.add(new Message(Severity.ERROR,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.TIFFModule.PrematureEOFMessage",
                    jhove2.getConfigInfo()));       
            return consumed;
        }
        finally {
            this.jhove2 = null;
            this.source = null;
        }
        
        return consumed;
    }

    /** 
     * parse the IFD(s) validating that there is at least one offset
     * and that it is word-aligned.  Following the offset
     * of the first IFD, parse the linked list of IFDs 
     *  
     * @throws JHOVE2Exception 
     * 
     */
    private List<IFD> parseIFDList(JHOVE2 jhove2, Source source, Input input) 
    throws EOFException, IOException, JHOVE2Exception{
        long offset = 0L;
        try {
            /* read the offset to the 0th IFD */
            offset = input.readUnsignedInt();
            ifh.setFirstIFD(offset);

            /* must have at least 1 IFD */
            if (offset == 0L) {
                this.validity = Validity.False;
                this.invalidFieldMessage.add(new Message(Severity.ERROR,
                        Context.OBJECT,
                        "org.jhove2.module.format.tiff.TIFFModule.NoIFDInTIFFFileMessage",
                        jhove2.getConfigInfo()));               
            }

        }
        catch (IOException e) {
            throw new JHOVE2Exception ("TiffModule.parseIFDs(): IOException reading offset to first IFD",e);
        }

        /* Parse the list of IFDs */                  
        List<IFD> list = new LinkedList<IFD>();
        long nextIfdOffset = offset;
        while (nextIfdOffset != 0L) {
            /* offset must be word aligned (even number) */
            if ((offset & 1) != 0) {
                this.validity = Validity.False;
                Object[]messageArgs = new Object[]{0, input.getPosition(), offset};
                this.byteOffsetNotWordAlignedMessage.add(new Message(Severity.ERROR,
                        Context.OBJECT,
                        "org.jhove2.module.format.tiff.TIFFModule.ByteOffsetNotWordAlignedMessage",
                        messageArgs, jhove2.getConfigInfo()));   
            }
            IFD ifd = parseIFD(nextIfdOffset, list, jhove2, source, input);
            nextIfdOffset  = ifd.getNextIFD(); 
        }
        return list;

    }


    /** 
     * following the offsets, process the IFD in the list of IFDs in the TIFF file
     * 
     * @param offset 
     * @throws JHOVE2Exception 
     * */
    private IFD parseIFD(long ifdOffset, List<IFD> list, JHOVE2 jhove2, Source source, Input input) 
    throws EOFException, IOException, JHOVE2Exception {

        IFD ifd = new TiffIFD();  

        ifd.setOffset(ifdOffset);

        /* parse for the appropriate IFD type */
        ifd.parse(jhove2, source, input, tagToFormatMap);

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
        int version = ifd.getVersion();
        if (version > this.version) {
            this.version = version;
        }

        // TODO:  parse subIFDs chains here

        // TODO: parse EXIF/GPS/InterOP/GlobalParms IFDChains here

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
    public Validity validate(JHOVE2 jhove2, Source source, Input input)
        throws JHOVE2Exception
    {
        return this.validity;
    }

    /**
     * Get TIFF source unit's validation status.
     * 
     * @return the validity
     */
    @Override
    public Validity isValid() {
        return this.validity;
    }

    /**
     * 
     * @return IFH Image File Header
     */
    @ReportableProperty(order = 1, value="IFH")
    public IFH getIFH() {
        return this.ifh;
    }

    /**
     * returns the list of IFDs for this TIFF object
     * 
     * @return List<IFD>
     */
    @ReportableProperty(order = 2, value="IFDs.")
    public List<IFD> getIFDs() {
        return this.ifdList;
    }

     /**
     * Get fail fast message.
     * 
     * @return Fail fast message
     */
    @ReportableProperty(order = 3, value = "Fail fast message.")
    public Message getFailFast() {
        return this.failFastMessage;
    }

    /**
     * @return the version
     */
    @ReportableProperty(order = 4, value = "TIFF version.")
    public int getTiffVersion() {
        return this.version;
    }

    /** Set the tag-to-format map.
     * @param map Tag-to-format map
     */
    public void setTagToFormatMap(Map<Integer, Format> map) {
        tagToFormatMap = map;
    }
}
