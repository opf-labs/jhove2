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

package org.jhove2.module.format.icc;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.Message;
import org.jhove2.core.Message.Context;
import org.jhove2.core.Message.Severity;
import org.jhove2.core.io.Input;
import org.jhove2.core.reportable.AbstractReportable;
import org.jhove2.core.source.Source;
import org.jhove2.module.format.Validator.Validity;

/** ICC tag table.  See ICC.1:2004-10, \u00a7 7.3.
 * 
 * @author slabrams
 */
public class ICCTagTable
    extends AbstractReportable
{
    /** Tag count. */
    protected long count;
    
    /** Common tag requirements status.
     * For all profiles: copyright tag and profile description tag.
     * For non-DeviceLink profiles: chromatic adaption tag and media white
     * point tag.
     * For DeviceLink profiles>: A-to-B0 tag, colorant table tag, colorant
     * table out tag, and profile sequence description tag. 
     */
    protected boolean hasCommonRequirements;
    
    /** Tag table validity. */
    protected Validity isValid;
    
    /** Tags. */
    protected List<ICCTag> tags;
    
    /** Missing required tag message. */
    protected List<Message> missingRequiredTagMessages;
     
    /** Instantiate a new <code>ICCTagTable</code>.
     */
    public ICCTagTable()
    {
        super();
        
        this.isValid = Validity.Undetermined;
        this.tags    = new ArrayList<ICCTag>();
        this.missingRequiredTagMessages = new ArrayList<Message>();
    }
    
    /** Parse an ICC tag table.
     * @param jhove2 JHOVE2 framework
     * @param source ICC source
     * @param header ICC header
     * @return Number of bytes consumed
     * @throws EOFException
     *             If End-of-File is reached reading the source unit
     * @throws IOException
     *             If an I/O exception is raised reading the source unit
     * @throws JHOVE2Exception
     */
    public long parse(JHOVE2 jhove2, Source source, ICCHeader header)
        throws EOFException, IOException, JHOVE2Exception
    {
        long    consumed = 0L;
        boolean hasAToB0Tag = false;
        boolean hasChromaticAdaptionTag = false;
        boolean hasColorantTableTag = false;
        boolean hasColorantTableOutTag = false;
        boolean hasCopyrightTag = false;
        boolean hasMediaWhitePointTag = false;
        boolean hasProfileDescriptionTag = false;
        boolean hasProfileSequenceDescriptionTag = false;
        int     numErrors = 0;
        this.isValid = Validity.True;
        Input input  = source.getInput(jhove2, ByteOrder.BIG_ENDIAN);
        
        this.count = input.readUnsignedInt();
        consumed += 4;
        for (int i=0; i<this.count; i++) {
            ICCTag tag = new ICCTag();
            consumed += tag.parse(jhove2, source, header.getOffset());
            Validity validity = tag.isValid();
            if (validity != Validity.True) {
                this.isValid = validity;
            }
            
            this.tags.add(tag);
            
            String signature = tag.getSignature_raw();
            if (signature.equals("A2B0")) {
                hasAToB0Tag = true;
            }
            else if (signature.equals("chad")) {
                hasChromaticAdaptionTag = true;
            }
            else if (signature.equals("clrt")) {
                hasColorantTableTag = true;
            }
            else if (signature.equals("clot")) {
                hasColorantTableOutTag = true;
            }
            else if (signature.equals("cprt")) {
                hasCopyrightTag = true;
            }
            else if (signature.equals("desc")) {
                hasProfileDescriptionTag = true;
            }
            else if (signature.equals("pseq")) {
                hasProfileSequenceDescriptionTag = true;
            }
            else if (signature.equals("wtpt")) {
                hasMediaWhitePointTag = true;
            }
        }
        
        this.hasCommonRequirements = true;
        if (!hasCopyrightTag) {
            numErrors++;
            this.isValid = Validity.False;
            Object [] args = new Object [] {"Copyright (\"cprt\")"};
            Message msg = new Message(Severity.ERROR, Context.OBJECT,
                    "org.jhove2.module.format.icc.ICCTagTable.MissingRequiredTag",
                    args, jhove2.getConfigInfo());
            this.missingRequiredTagMessages.add(msg);
            this.hasCommonRequirements = false;
        }
        if (!hasProfileDescriptionTag) {
            numErrors++;
            this.isValid = Validity.False;
            Object [] args = new Object [] {"Profile description (\"desc\")"};
            Message msg = new Message(Severity.ERROR, Context.OBJECT,
                    "org.jhove2.module.format.icc.ICCTagTable.MissingRequiredTag",
                    args, jhove2.getConfigInfo());
            this.missingRequiredTagMessages.add(msg);
            this.hasCommonRequirements = false;
        }
        if (header.isDeviceLinkProfile()) {
            if (!hasAToB0Tag) {
                numErrors++;
                this.isValid = Validity.False;
                Object [] args = new Object [] {"A-to-B0 (\"A2B0t\")"};
                Message msg = new Message(Severity.ERROR, Context.OBJECT,
                    "org.jhove2.module.format.icc.ICCTagTable.MissingRequiredTag",
                    args, jhove2.getConfigInfo());
                this.missingRequiredTagMessages.add(msg);
                this.hasCommonRequirements = false;
            };
            if (!hasColorantTableTag) {
                numErrors++;
                this.isValid = Validity.False;
                Object [] args = new Object [] {"Colorant table (\"clrt\")"};
                Message msg = new Message(Severity.ERROR, Context.OBJECT,
                    "org.jhove2.module.format.icc.ICCTagTable.MissingRequiredTag",
                    args, jhove2.getConfigInfo());
                this.missingRequiredTagMessages.add(msg);
                this.hasCommonRequirements = false;
            }
            if (!hasColorantTableOutTag) {
                numErrors++;
                this.isValid = Validity.False;
                Object [] args = new Object [] {"Colorant table out (\"clot\")"};
                Message msg = new Message(Severity.ERROR, Context.OBJECT,
                    "org.jhove2.module.format.icc.ICCTagTable.MissingRequiredTag",
                    args, jhove2.getConfigInfo());
                this.missingRequiredTagMessages.add(msg);
                this.hasCommonRequirements = false;
            }
            if (!hasProfileSequenceDescriptionTag) {
                numErrors++;
                this.isValid = Validity.False;
                Object [] args = new Object [] {"Profile sequence description (\"pseqt\")"};
                Message msg = new Message(Severity.ERROR, Context.OBJECT,
                    "org.jhove2.module.format.icc.ICCTagTable.MissingRequiredTag",
                    args, jhove2.getConfigInfo());
                this.missingRequiredTagMessages.add(msg);
                this.hasCommonRequirements = false;
            }
        }
        else {
            if (!hasMediaWhitePointTag) {
                numErrors++;
                this.isValid = Validity.False;
                Object [] args = new Object [] {"Media white point (\"wtpt\")"};
                Message msg = new Message(Severity.ERROR, Context.OBJECT,
                    "org.jhove2.module.format.icc.ICCTagTable.MissingRequiredTag",
                    args, jhove2.getConfigInfo());
                this.missingRequiredTagMessages.add(msg);
                this.hasCommonRequirements = false;
            }
            if (!header.isD50Illuminant()) {
                if (!hasChromaticAdaptionTag) {
                    numErrors++;
                    this.isValid = Validity.False;
                    Object [] args = new Object [] {"Chromatic adaption (\"chad\")"};
                    Message msg = new Message(Severity.ERROR, Context.OBJECT,
                            "org.jhove2.module.format.icc.ICCTagTable.MissingRequiredTag",
                            args, jhove2.getConfigInfo());
                    this.missingRequiredTagMessages.add(msg);
                    this.hasCommonRequirements = false;
                }
            }
        }

        return consumed;
    }

    /** Get tag count.
     * @return Tag count
     */
    @ReportableProperty(order=1, value="Tag count.",
            ref="ICC.1:2004-10, \u00a7 7.3.2")
    public long getCount() {
        return this.count;
    }
    
    /** Get missing required tag messages.
     * @return missing required messages
     */
    @ReportableProperty(order=21, value="Missing required tags.",
            ref="ICC.1:2004-10, \u00a7 8.2 and 8.6")
    public List<Message> getMissingRequiredTagMessages() {
        return this.missingRequiredTagMessages;
    }
     
    /** Get tags.
     * @return Tags
     */
    @ReportableProperty(order=2, value="Tags.",
            ref="ICC.1:2004-10, \u00a7 7.3")
    public List<ICCTag> getTags() {
        return this.tags;
    }
    
    /** Common tag requirements status: true if common requirements are met.
     * @return Common tag requirements status
     */
    @ReportableProperty(order=3, value="Common tag requirements",
            ref="ICC.1:2004-10, \u00a7 8.2 and 8.6")
    public boolean hasCommonRequirements() {
        return this.hasCommonRequirements;
    }

    /** Get validity.
     * @return Validity
     */
    @ReportableProperty(order=4, value="Tag table validity",
            ref="ICC.1:2004-10, \u00a7 7.3")
    public Validity isValid()
    {
         return this.isValid;
    }
}
