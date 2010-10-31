/**
 * JHOVE2 - Next-generation architecture for format-aware characterization
 * 
 * Copyright (c) 2009 by The Regents of the University of California, Ithaka
 * Harbors, Inc., and The Board of Trustees of the Leland Stanford Junior
 * University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * o Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * 
 * o Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * o Neither the name of the University of California/California Digital
 * Library, Ithaka Harbors/Portico, or Stanford University, nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
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
package org.jhove2.module.format.tiff.profile;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.Message;
import org.jhove2.core.Message.Context;
import org.jhove2.core.Message.Severity;
import org.jhove2.core.format.Format;
import org.jhove2.module.format.tiff.IFDEntry;
import org.jhove2.module.format.tiff.TiffIFD;
import org.jhove2.module.format.tiff.type.AsciiArray;

/**
 * @author MStrong
 * 
 */
public class TiffITCTP2Profile extends TiffItProfile {

    /** Profile version identifier. */
    public static final String VERSION = "2.0.1";

    /** Profile release date. */
    public static final String RELEASE = "2010-10-20";

    /** Profile rights statement. */
    public static final String RIGHTS = "Copyright 2010 by The Regents of the University of California. "
        + "Available under the terms of the BSD license.";

    /** Profile validation coverage. */
    public static final Coverage COVERAGE = Coverage.Inclusive;

    private Message invalidDotRangeMessage;

    public TiffITCTP2Profile(Format format) {
        super(format);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.jhove2.module.format.tiff.profile.TiffProfile#validateThisProfile()
     */
    @Override
    public void validateThisProfile(JHOVE2 jhove2, TiffIFD ifd)
    throws JHOVE2Exception {

        /* Check required values. */        
        if (!isNewSubfileTypeValid(ifd, 0)) {
            this.isValid = Validity.False;
            this.invalidNewSubfileTypeMessage = new Message(
                    Severity.WARNING,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.profile.TIFFProfile.invalidNewSubfileTypeMessage",
                    jhove2.getConfigInfo());
        }

        // bps must be { 8, ... }
        int[] bps = ifd.getBitsPerSample();
        if (bps == null) {
            this.isValid = Validity.False;
            Object[] args = new Object[] { "BitsPerSample" };
            Message msg = new Message(
                    Severity.WARNING,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.profile.TIFFProfile.MissingRequiredTag",
                    args, jhove2.getConfigInfo());
            this.missingRequiredTagMessages.add(msg);
        }
        else if (bps.length < 1) {
            this.isValid = Validity.False;
            Object[] args = new Object[] { "{8,...}" };
            this.invalidBPSValueMessage = new Message(
                    Severity.WARNING,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.profile.TIFFITProfile.InvalidBPSElementsMessage",
                    args, jhove2.getConfigInfo());
        }
        else if (bps[0] != 8) {
            Object[] args = new Object[] { "{8,8,8,8}" };
            this.invalidBPSValueMessage = new Message(
                    Severity.WARNING,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.profile.TIFFITProfile.InvalidBPSElementsMessage",
                    args, jhove2.getConfigInfo());                    
        }

        if (!isPhotometricInterpretationValid (ifd, 5)) {
            this.isValid = Validity.False;
            this.invalidPhotometricInterpretationValueMessage = new Message(
                    Severity.WARNING,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.profile.TIFFProfile.InvalidPhotometricInterpretationValueMessage",
                    jhove2.getConfigInfo());
        }

        if (!isCompressionValid(ifd, new int [] {1, 7, 8} )) {
            this.isValid = Validity.False;
            this.invalidCompressionValueMessage = new Message(
                    Severity.WARNING,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.profile.TIFFProfile.InvalidCompressionValueMessage",
                    jhove2.getConfigInfo());
        }

        if (!isOrientationValid(ifd, 1)) {
            this.isValid = Validity.False;
            this.invalidOrientationValueMessage = new Message(
                    Severity.WARNING,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.profile.TIFFProfile.invalidOrientationValueMessage",
                    jhove2.getConfigInfo());
        }

        if (!isSamplesPerPixelValid(ifd, 4)) {
            this.isValid = Validity.False;
            Object[] args = new Object[] { 4 };
            this.invalidSPPValueMessage = new Message(
                    Severity.WARNING,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.profile.TIFFITProfile.InvalidSPPValueMessage",
                    args, jhove2.getConfigInfo());
        }

        if (!isPlanarConfigurationValid(ifd, 1)) {
            this.isValid = Validity.False;
            this.invalidPlanarConfigurationValueMessage = new Message(
                    Severity.WARNING,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.profile.TIFFProfile.InvalidPlanarConfigurationValueMessage",
                    jhove2.getConfigInfo());
        }

        if (!isResolutionUnitValid(ifd, new int[] {2, 3})) {
            this.isValid = Validity.False;
            this.invalidResolutionUnitValueMessage = new Message(
                    Severity.WARNING,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.profile.TIFFProfile.InvalidResolutionUnitValueMessage",
                    jhove2.getConfigInfo());
        }

        IFDEntry entry = null;
        String colorSequence = null;
        if ((entry = ifd.getEntries().get(TiffIFD.COLORSEQUENCE)) != null) {
            colorSequence = (String) ((AsciiArray) entry.getValue()).toString();
            if (!colorSequence.equals("CMYK")) {
                this.isValid = Validity.False;
                this.invalidColorSequenceMessage = new Message(
                        Severity.WARNING,
                        Context.OBJECT,
                        "org.jhove2.module.format.tiff.profile.TIFFITProfile.InvalidColorSequenceMessage",
                        jhove2.getConfigInfo());
            }
        }
        /*
         * if inkset tag is used and color sequence tag value is the default value of 'CMYK' 
         * inkset shall have the value of 1
         * Otherwise if shall have the value of 2.
         */
        if ((entry = ifd.getEntries().get(TiffIFD.INKSET)) != null) {
            int inkset = (Short) entry.getValue();
            if (colorSequence != null && colorSequence.equals("CMYK")) {
                if ( inkset != 1) {
                    this.isValid = Validity.False;
                    this.invalidInksetValueMessage = new Message(
                            Severity.WARNING,
                            Context.OBJECT,
                            "org.jhove2.module.format.tiff.profile.TIFFITProfile.InvalidInksetValueMessage",
                            jhove2.getConfigInfo());
                }
            }
        }


        if ((entry = ifd.getEntries().get(TiffIFD.NUMBEROFINKS)) != null) {
            if ((Short) entry.getValue() != 4) {
                this.isValid = Validity.False;
                this.invalidNumberOfInksValueMessage = new Message(
                        Severity.WARNING,
                        Context.OBJECT,
                        "org.jhove2.module.format.tiff.profile.TIFFITProfile.InvalidNumberOfInksValueMessage",
                        jhove2.getConfigInfo());
            }
        }

        if (!isDotRangeValid(ifd, 0, 255)) {
            this.isValid = Validity.False;
            this.invalidDotRangeMessage = new Message(
                    Severity.WARNING,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.profile.TIFFITProfile.InvalidDotRangeMessage",
                    jhove2.getConfigInfo());
        }


        /* Tags which must NOT be defined */
        if ((entry = ifd.getEntries().get(TiffIFD.DOCUMENTNAME)) != null) {
            this.isValid = Validity.False;
            Object[] args = new Object[] { entry.getName() };
            this.tagShouldNotBePresentMessage = new Message(
                    Severity.WARNING,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.profile.TIFFProfile.tagShouldNotBePresentMessage",
                    args, jhove2.getConfigInfo());
        }
        
        if ((entry = ifd.getEntries().get(TiffIFD.MODEL)) != null) {
            this.isValid = Validity.False;
            Object[] args = new Object[] { entry.getName() };
            this.tagShouldNotBePresentMessage = new Message(
                    Severity.WARNING,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.profile.TIFFProfile.tagShouldNotBePresentMessage",
                    args, jhove2.getConfigInfo());
        }
        
        if ((entry = ifd.getEntries().get(TiffIFD.PAGENAME)) != null) {
            this.isValid = Validity.False;
            Object[] args = new Object[] { entry.getName() };
            this.tagShouldNotBePresentMessage = new Message(
                    Severity.WARNING,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.profile.TIFFProfile.tagShouldNotBePresentMessage",
                    args, jhove2.getConfigInfo());
        }
        
        if ((entry = ifd.getEntries().get(TiffIFD.HOSTCOMPUTER)) != null) {
            this.isValid = Validity.False;
            Object[] args = new Object[] { entry.getName() };
            this.tagShouldNotBePresentMessage = new Message(
                    Severity.WARNING,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.profile.TIFFProfile.tagShouldNotBePresentMessage",
                    args, jhove2.getConfigInfo());
        }

        if ((entry = ifd.getEntries().get(TiffIFD.SITE)) != null) {
            this.isValid = Validity.False;
            Object[] args = new Object[] { entry.getName() };
            this.tagShouldNotBePresentMessage = new Message(
                    Severity.WARNING,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.profile.TIFFProfile.tagShouldNotBePresentMessage",
                    args, jhove2.getConfigInfo());
        }

        if ((entry = ifd.getEntries().get(TiffIFD.IT8HEADER)) != null) {
            this.isValid = Validity.False;
            Object[] args = new Object[] { entry.getName() };
            this.tagShouldNotBePresentMessage = new Message(
                    Severity.WARNING,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.profile.TIFFProfile.tagShouldNotBePresentMessage",
                    args, jhove2.getConfigInfo());
        }
    }

    /**
     * @return the invalidDotRangeMessage
     */
    @ReportableProperty(order = 1, value = "Invalid DotRange message")
    public Message getInvalidDotRangeMessage() {
        return invalidDotRangeMessage;
    }


}
