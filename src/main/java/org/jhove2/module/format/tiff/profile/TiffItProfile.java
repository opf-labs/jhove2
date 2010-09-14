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

package org.jhove2.module.format.tiff.profile;

import java.util.ArrayList;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.Message;
import org.jhove2.core.Message.Context;
import org.jhove2.core.Message.Severity;
import org.jhove2.core.format.Format;
import org.jhove2.module.format.Validator;
import org.jhove2.module.format.tiff.TiffIFD;

/**
 * @author mstrong
 *
 */
public class TiffItProfile 
        extends TiffProfile 
        implements Validator 
{


    /** Invalid Value for samples per pixel message */
    protected Message invalidSPPValueMessage;

    /** Invalid Value for bits per sample message */
    protected Message invalidBPSValueMessage;

    /** Invalid ImageColorIndicator message */
    protected Message invalidImageColorIndicatorMessage;

    /** invalid BackgroundColorIndicator message */
    protected Message invalidBackgroundColorIndicatorMessage;
    
    /** invalid NewSubfileType message */
    protected Message invalidNewSubfileTypeMessage;

    /**
     * Instantiate a new <code>TiffItProfile</code>.
     * 
     * @param format
     *            TIFF-IT format
     */
    public TiffItProfile(Format format) {
        super(format);

        this.isValid = Validity.Undetermined;
        this.missingRequiredTagMessages = new ArrayList<Message>();
    }

    public TiffItProfile(){
        this(null);
    }

    /**
     * Validate an TIFF-IT source unit.
     * 
     * @param jhove2
     *            JHOVE2 framework
     * @param IFD
     *            TIFF IFD
     * @see org.jhove2.module.format.tiff.profile.TiffProfile#validateThisProfile
     *      (org.jhove2.core.JHOVE2,
     *      org.jhove2.module.format.tiff.TiffIFD)
     */
    @Override
    public void validateThisProfile(JHOVE2 jhove2, TiffIFD ifd) throws JHOVE2Exception 
    {
        /* Check required tags. */
        if (!ifd.hasImageLength()) {
            this.isValid = Validity.False;
            Object [] args = new Object [] {"ImageLength"};
            Message msg = new Message(Severity.ERROR, Context.OBJECT,
                    "org.jhove2.module.format.tiff.profile.TIFFProfile.MissingRequiredTag",
                    args, jhove2.getConfigInfo());
            this.missingRequiredTagMessages.add(msg);
        }
        if (!ifd.hasImageWidth()) {
            this.isValid = Validity.False;
            Object [] args = new Object [] {"ImageWidth"};
            Message msg = new Message(Severity.ERROR, Context.OBJECT,
                    "org.jhove2.module.format.tiff.profile.TIFFProfile.MissingRequiredTag",
                    args, jhove2.getConfigInfo());
            this.missingRequiredTagMessages.add(msg);
        }
        if (!ifd.hasStripOffsets()) {
            this.isValid = Validity.False;
            Object [] args = new Object [] {"StripOffsets"};
            Message msg = new Message(Severity.ERROR, Context.OBJECT,
                    "org.jhove2.module.format.tiff.profile.TIFFProfile.MissingRequiredTag",
                    args, jhove2.getConfigInfo());
            this.missingRequiredTagMessages.add(msg);
        }
        if (!ifd.hasRowsPerStrip()) {
            this.isValid = Validity.False;
            Object [] args = new Object [] {"RowsPerStrip"};
            Message msg = new Message(Severity.ERROR, Context.OBJECT,
                    "org.jhove2.module.format.tiff.profile.TIFFProfile.MissingRequiredTag",
                    args, jhove2.getConfigInfo());
            this.missingRequiredTagMessages.add(msg);
        }
        if (!ifd.hasStripByteCounts()) {
            this.isValid = Validity.False;
            Object [] args = new Object [] {"StripByteCounts"};
            Message msg = new Message(Severity.ERROR, Context.OBJECT,
                    "org.jhove2.module.format.tiff.profile.TIFFProfile.MissingRequiredTag",
                    args, jhove2.getConfigInfo());
            this.missingRequiredTagMessages.add(msg);
        }
        if (!ifd.hasXResolution()) {
            this.isValid = Validity.False;
            Object [] args = new Object [] {"XResolution"};
            Message msg = new Message(Severity.ERROR, Context.OBJECT,
                    "org.jhove2.module.format.tiff.profile.TIFFProfile.MissingRequiredTag",
                    args, jhove2.getConfigInfo());
            this.missingRequiredTagMessages.add(msg);
        }
        if (!ifd.hasYResolution()) {
            this.isValid = Validity.False;
            Object [] args = new Object [] {"YResolution"};
            Message msg = new Message(Severity.ERROR, Context.OBJECT,
                    "org.jhove2.module.format.tiff.profile.TIFFProfile.MissingRequiredTag",
                    args, jhove2.getConfigInfo());
            this.missingRequiredTagMessages.add(msg);
        }
    }
    
    /** Get TIFF-IT profile validation coverage.
     * @return TIFF-IT profile validation coverage
     */
    @Override
    public Coverage getCoverage() {
        return COVERAGE;
    }

    /** get invalid bits per sample value message
     * 
     * @return invalid bitsPerSample value message
     */
    @ReportableProperty(order = 1, value = "Invalid BPS Value Message.")
    public Message getInvalidBPSValueMessage() {
        return invalidBPSValueMessage;
    }

    /** get invalid samples per pixel value message
     * 
     * @return invalid samples per pixel value message
     */
    @ReportableProperty(order = 2, value = "Invalid SPP Value Message.")
    public Message getInvalidSPPValueMessage() {
        return invalidSPPValueMessage;
    }

    /**
     * @return the invalidInvalidImageColorIndicatorMessage
     */
    @ReportableProperty(order = 3, value = "Invalid ImageColorIndicator Value Message.")
    public Message getInvalidImageColorIndicatorMessage() {
        return invalidImageColorIndicatorMessage;
    }

    /**
     * @return the invalidBackgroundColorIndicatorMessage
     */
    @ReportableProperty(order = 4, value = "Invalid BackgroundColorIndicator Value Message.")
    public Message getInvalidBackgroundColorIndicatorMessage() {
        return invalidBackgroundColorIndicatorMessage;
    }

    /**
     * @return the invalidNewSubfileTypeMessage
     */
    @ReportableProperty(order = 5, value = "Invalid NewSubfileType message")
    public Message getInvalidNewSubfileTypeMessage() {
        return invalidNewSubfileTypeMessage;
    }

    /**
     * Get TIFF-IT validation status.
     * 
     * @return TIFF-IT validation status
     * @see org.jhove2.module.format.Validator#isValid()
     */
    @Override
    public Validity isValid() {
        return this.isValid;
    }

}
