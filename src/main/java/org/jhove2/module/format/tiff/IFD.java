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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.config.ConfigInfo;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.Message;
import org.jhove2.core.Message.Context;
import org.jhove2.core.Message.Severity;
import org.jhove2.core.io.Input;
import org.jhove2.core.reportable.AbstractReportable;
import org.jhove2.core.source.Source;
import org.jhove2.module.format.Validator.Validity;

import com.sleepycat.persist.model.Persistent;

/**
 * @author mstrong
 *
 */
@Persistent
public abstract class IFD 
extends AbstractReportable {

    protected static Properties tiffTagProps;

    protected static Properties tiffTypeProps;

    /**
     * @return Properties the Tiff Tag Properties stored in the Java Properties file
     */
    public static Properties getTiffTags(ConfigInfo config)  throws JHOVE2Exception {
        if (tiffTagProps==null){
            tiffTagProps = config.getProperties("TiffTags");
        }
        return tiffTagProps;
    }

    /**
     * @return Properties the Tiff Type Properties stored in the Java Properties file
     */
    public static Properties getTiffType(ConfigInfo config)  throws JHOVE2Exception {
        if (tiffTypeProps==null){
            tiffTypeProps = config.getProperties("TiffTypes");
        }
        return tiffTypeProps;
    }

    /** IFD Entries in the IFD */
    protected HashMap<Integer, IFDEntry> entries = new HashMap<Integer, IFDEntry>();

    /** True if this is the first IFD. */
    private boolean first;

    /** validity of IFD */
    protected Validity isValid;

    /** offset to the next IFD */
    protected long nextIFD;

    /** number of IFD Entries in the IFD */ 
    protected int numEntries;

    /** offset of the IFD */ 
    protected long offset;

    /** True if this is the "thumbnail" IFD. */
    private boolean thumbnail;


    /** TIFF version determined by data in IFDEntry */
    protected int version;


    /** Message for Zero IFD Entries */
    private Message zeroIFDEntriesMessage;

    /** 
     * Instantiate an IFD Class with the Input source
     * @param input
     */
    public IFD() {
        super();
    }

    /**
     * Sort the entries in the HashMap and then return only the IFDEntries
     * @return List<IFDEntry>
     */
    @ReportableProperty(order = 3, value="IFD entries.")
    public List<IFDEntry> getIFDEntries() {
        Map<Integer, IFDEntry> sortedEntries = new TreeMap<Integer, IFDEntry>(entries);
        List<IFDEntry> sortedList = new ArrayList<IFDEntry>(sortedEntries.values());
        return sortedList;
    }
    
    /**
     * returns the entries
     * @return Map<Integer, IFDEntry>
     */
    public Map<Integer, IFDEntry> getEntries() {
        return this.entries;
    }



    /**
     * get the offsetof the next IFD 
     * @return long
     */
    @ReportableProperty(order = 4, value = "Offset of next IFD.")
    public long getNextIFD() {
        return nextIFD;
    }

    /**
     * get the number of IFD entries
     * @return int
     */
    @ReportableProperty (order = 2, value = "Number of IFD entries.")
    public int getNumEntries() {
        return numEntries;
    }

    /**
     * get the byte offset of the IFD 
     * @return long
     */
    @ReportableProperty(order = 1, value = "Byte offset of IFD.")
    public long getOffset() {
        return offset;
    }

    /**
     * @return the version for this IFD
     */
    public int getVersion() {
        return this.version;
    }
   
    /**
     * Get the Zero IFD entries message
     */
    @ReportableProperty(order=5, value = "Zero IFD Entries message.")
    public Message getZeroIFDEntriesMessage(){
        return zeroIFDEntriesMessage;
    }

    /**
     * @return the flag indicating this if the first IFD
     */
    public boolean isFirst() {
        return first;
    }

    /**
     * @return the flag indicating this IFD is a thumbnail
     */
    public boolean isThumbnail() {
        return thumbnail;
    }

    /**
     * @return the isValid
     */
    public Validity isValid() {
        return isValid;
    }

    /**
     * Parse an IFD. Read and parse each IFDEntry encountered.
     * @param input
     *            JHOVE2 framework
     * @param input
     *            Input
     * @param tiff2FormatMapper Tiff2FormatMapFactory to map tiff id to Format
     * @throws EOFException
     *             If End-of-File is reached reading the source unit
     * @throws IOException
     *             If an I/O exception is raised reading the source unit
     * @throws JHOVE2Exception
     */
    public void parse(JHOVE2 jhove2, Source source, Input input, Tiff2FormatMapFactory tiff2FormatMapper)
        throws EOFException, IOException, JHOVE2Exception
    {
        this.isValid = Validity.Undetermined;
        long offsetInIFD = this.offset;
        this.nextIFD = 0L;

        try {
            /* Read the first byte. */
            input.setPosition(offsetInIFD);
            numEntries = input.readUnsignedShort();
            offsetInIFD += 2;

            if (this.numEntries < 1){
                this.isValid = Validity.False;
                Object[]messageArgs = new Object[]{0, input.getPosition(), numEntries};
                this.zeroIFDEntriesMessage = new Message(Severity.ERROR,
                        Context.OBJECT,
                        "org.jhove2.module.format.tiff.IFD.zeroIFDEntriesMessage",
                        messageArgs, jhove2.getConfigInfo());  
            }

            long length = numEntries * 12;
            /* go to the field that contains the offset to the next IFD - 0 if none */
            offsetInIFD += length;
            this.nextIFD = 0L;
        }
        catch (EOFException e) {
            throw new EOFException("Premature EOF" + offsetInIFD);
        }

        try {
            /* parse the IFD traversing through the list of Directory Entries (IFDEntry) */
            IFDEntry.resetPrevTag(0);
            
            for (int i=0; i<this.numEntries; i++) {
                IFDEntry ifdEntry = new IFDEntry();
                ifdEntry.parse(jhove2, source, input, tiff2FormatMapper);
                Validity validity = ifdEntry.isValid();
                if (validity != Validity.True) {
                    this.isValid = validity;
                }
                int version = ifdEntry.getVersion();
                if (version > this.version) {
                    this.version = version;
                }
                this.entries.put(ifdEntry.getTag(), ifdEntry);

                /* reset the input position to point to the next IFD Entry (after parsing current IFD Entry,
                 * input position is modified when reading data at location that IFD Entry value offset points to)
                 * Calculation is:
                 *  offset (offset start of IFD) + 
                 *  14 (2 bytes for numofEntries field + 12 bytes for IFD Entry 0) +
                 *  12 * i (12 bytes for each IFD read in so far) 
                 */
                input.setPosition(this.offset + 14 + 12*i);
            }
        }
        catch (IOException e) {
            throw new IOException ("IOException while reading IFD " + (offset + 2), e);
        }
    }

    /**
     * @parm boolean -
     * the flag indicating this if the first IFD
     */
    public void setFirst(boolean first) {
        this.first = first;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public void setThumbnail(boolean thumbnail) {
        this.thumbnail = thumbnail;
    }

    /**
     * validate the IFD
     * 
     * @return Validity
     * @throws IOException 
     * @throws FileNotFoundException 
     * @throws JHOVE2Exception 
     */
    abstract Validity validate(JHOVE2 jhove2, Source source, Input input) throws JHOVE2Exception, FileNotFoundException, IOException;


}
