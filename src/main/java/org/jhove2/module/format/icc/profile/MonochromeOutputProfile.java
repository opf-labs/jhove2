/**
 * JHOVE2 - Next-generation architecture for format-aware characterization
 *
 * Copyright (c) 2009 by The Regents of the University of California,
 * Ithaka Harbors, Inc., and The Board of Trustees of the Leland Stanford
 * Junior University.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * o Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 *
 * o Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * o Neither the name of the University of California/California Digital
 *   Library, Ithaka Harbors/Portico, or Stanford University, nor the names of
 *   its contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
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
 */

package org.jhove2.module.format.icc.profile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.Message;
import org.jhove2.core.Message.Context;
import org.jhove2.core.Message.Severity;
import org.jhove2.core.format.Format;
import org.jhove2.core.io.Input;
import org.jhove2.core.source.Source;
import org.jhove2.module.format.AbstractFormatProfile;
import org.jhove2.module.format.Validator;
import org.jhove2.module.format.icc.ICCModule;
import org.jhove2.module.format.icc.ICCTag;
import org.jhove2.module.format.icc.ICCTagTable;

/** ICC monochrome output profile, as defined in ICC.1:2004-10, \u00a7 8.5.3.
 * 
 * @author slabrams
 */
public class MonochromeOutputProfile
    extends AbstractFormatProfile
    implements Validator
{
    /** Profile version identifier. */
    public static final String VERSION = "2.0.0";

    /** Profile release date. */
    public static final String RELEASE = "2010-09-10";

    /** Profile rights statement. */
    public static final String RIGHTS =
        "Copyright 2010 by The Regents of the University of California. " +
        "Available under the terms of the BSD license.";

    /** Profile validation coverage. */
    public static final Coverage COVERAGE = Coverage.Inclusive;

    /** Validation status. */
    protected Validity isValid;

    /** Missing required tag messages. */
    protected List<Message> missingRequiredTagMessages;
    
    /** Instantiate a new <code>MonochromeOutputProfile</code>.
     * @param format Profile format 
     */
    public MonochromeOutputProfile(Format format) {
        super(VERSION, RELEASE, RIGHTS, format);
        
        this.isValid = Validity.Undetermined;
        this.missingRequiredTagMessages = new ArrayList<Message>();
    }

    /** Validate the monochrome output profile.
     * @param jhove2 JHOVE2 framework
     * @param source ICC source unit
     * @param input  ICC source input
     * @see org.jhove2.module.format.Validator#validate(org.jhove2.core.JHOVE2, org.jhove2.core.source.Source, org.jhove2.core.io.Input)
     */
    @Override
    public Validity validate(JHOVE2 jhove2, Source source, Input input)
        throws JHOVE2Exception
    {
        if (this.module != null) {
            ICCTagTable table = ((ICCModule) this.module).getTagTable();
            if (table != null) {
                if (table.hasCommonRequirements()) {
                    this.isValid = Validity.True;
            
                    boolean hasGrayTRCTag = false;
                    List<ICCTag> tags = table.getTags();
                    Iterator<ICCTag> iter = tags.iterator();
                    while (iter.hasNext()) {
                        ICCTag tag = iter.next();
                
                        String signature = tag.getSignature_raw();
                        if (signature.equals("kTRC")) {
                            hasGrayTRCTag = true;
                        }
                    }
                    if (!hasGrayTRCTag) {
                        this.isValid = Validity.False;
                        Object [] args = new Object [] {"GrayTRC (\"kTRC\")"};
                        Message msg = new Message(Severity.WARNING, Context.OBJECT,
                            "org.jhove2.module.format.icc.ICCTagTable.MissingRequiredTag",
                            args, jhove2.getConfigInfo());
                        this.missingRequiredTagMessages.add(msg);
                    }
                }
            }
        }
        return this.isValid;
    }

    /** Get coverage.
     * @return Coverage
     * @see org.jhove2.module.format.Validator#getCoverage()
     */
    @Override
    public Coverage getCoverage()
    {
        return COVERAGE;
    }

    /** Get missing required tag messages.
     * @return missing required messages
     */
    @ReportableProperty(order=1, value="Missing required tags.",
            ref="ICC.1:2004-10, \u00a7 8.5.3")
    public List<Message> getMissingRequiredTagMessages() {
        return this.missingRequiredTagMessages;
    }

    /** Get validation status.
     * @return Validation status
     * @see org.jhove2.module.format.Validator#isValid()
     */
    @Override
    public Validity isValid()
    {
        return this.isValid;
    }
}
