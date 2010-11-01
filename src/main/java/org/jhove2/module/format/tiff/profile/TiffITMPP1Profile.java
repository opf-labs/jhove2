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
import org.jhove2.module.format.tiff.type.ByteArray;
import org.jhove2.module.format.tiff.type.Byte;

/**
 * @author MStrong
 * 
 */
public class TiffITMPP1Profile extends TiffItProfile {

    /** Profile version identifier. */
    public static final String VERSION = "2.0.0";

    /** Profile release date. */
    public static final String RELEASE = "2010-10-20";

    /** Profile rights statement. */
    public static final String RIGHTS = "Copyright 2010 by The Regents of the University of California. "
        + "Available under the terms of the BSD license.";

    /** Profile validation coverage. */
    public static final Coverage COVERAGE = Coverage.Inclusive;

    /** invalid DotRange message */
    protected Message invalidDotRangeMessage;

    /** invalid PixelIntensityRange value message */
    protected Message invalidPixelIntensityRangeValueMessage;

    public TiffITMPP1Profile(Format format) {
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
        
        /* Check required tags. */        
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

        int spp = ifd.getSamplesPerPixel();
        if (spp == TiffIFD.NULL) {
            this.isValid = Validity.False;
            Object[] args = new Object[] { "SamplesPerPixel" };
            Message msg = new Message(
                    Severity.WARNING,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.profile.TIFFProfile.MissingRequiredTag",
                    args, jhove2.getConfigInfo());
            this.missingRequiredTagMessages.add(msg);
        }

        int pi = ifd.getPhotometricInterpretation();
        if (pi == TiffIFD.NULL) {
            this.isValid = Validity.False;
            Object[] args = new Object[] { "PhotometricInterpretation" };
            Message msg = new Message(
                    Severity.WARNING,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.profile.TIFFProfile.MissingRequiredTag",
                    args, jhove2.getConfigInfo());
            this.missingRequiredTagMessages.add(msg);
        }

        /* Check required values. */        
        if (!isNewSubfileTypeValid(ifd, 0)) {
            this.isValid = Validity.False;
            this.invalidNewSubfileTypeMessage = new Message(
                    Severity.WARNING,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.profile.TIFFProfile.invalidNewSubfileTypeMessage",
                    jhove2.getConfigInfo());
        }

        if (bps == null || (bps[0] != 8)) {
            this.isValid = Validity.False;
            Object[] args = new Object[] { 8 };
            this.invalidBPSValueMessage = new Message(
                    Severity.WARNING,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.profile.TIFFITProfile.InvalidBPSValueMessage",
                    args, jhove2.getConfigInfo());
        }

        if (!isCompressionValid(ifd, 1)) {
            this.isValid = Validity.False;
            this.invalidCompressionValueMessage = new Message(
                    Severity.WARNING,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.profile.TIFFProfile.InvalidCompressionValueMessage",
                    jhove2.getConfigInfo());
        }

        if (!isPhotometricInterpretationValid (ifd, 0)) {
            this.isValid = Validity.False;
            this.invalidPhotometricInterpretationValueMessage = new Message(
                    Severity.WARNING,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.profile.TIFFProfile.InvalidPhotometricInterpretationValueMessage",
                    jhove2.getConfigInfo());
        }

        if (!isOrientationValid(ifd, 1)) {
            this.isValid = Validity.False;
            this.invalidOrientationValueMessage = new Message(
                    Severity.WARNING,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.profile.TIFFProfile.InvalidOrientationValueMessage",
                    jhove2.getConfigInfo());
        }

        if (!isSamplesPerPixelValid(ifd, 1)) {
            this.isValid = Validity.False;
            Object[] args = new Object[] { 1 };
            this.invalidSPPValueMessage = new Message(
                    Severity.WARNING,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.profile.TIFFITProfile.InvalidSPPValueMessage",
                    args, jhove2.getConfigInfo());
        }

        if (!isResolutionUnitValid(ifd, new int[] {2, 3})) {
            this.isValid = Validity.False;
            this.invalidResolutionUnitValueMessage = new Message(
                    Severity.WARNING,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.profile.TIFFProfile.InvalidResolutionUnitValueMessage",
                    jhove2.getConfigInfo());
        }

        if (!isDotRangeValid(ifd, 0, 255)) {
            this.isValid = Validity.False;
            this.invalidDotRangeMessage = new Message(
                    Severity.WARNING,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.profile.TIFFITProfile.InvalidDotRangeMessage",
                    jhove2.getConfigInfo());
        }

        IFDEntry entry = null;
        int[] validValues;
        short indicator = TiffIFD.NULL;
        if ((entry = ifd.getEntries().get(TiffIFD.IMAGECOLORINDICATOR)) != null) {
             indicator = ((Byte) entry.getValue()).getValue();
        }
        /* ImageColorIndicator=0, 1 */
        validValues = new int[] { 0 , 1};        
        if (!isImageColorIndicatorValid(ifd, validValues)) {
            this.isValid = Validity.False;
            this.invalidImageColorIndicatorMessage = new Message(
                    Severity.WARNING,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.profile.TIFFITProfile.InvalidImageColorIndicatorMessage",
                    jhove2.getConfigInfo());
        }
        /* , ImageColorValue is defined only if ImageColorIndicator=1*/
        if (indicator == 1) {
            if ((entry = ifd.getEntries().get(TiffIFD.IMAGECOLORVALUE)) == null) {
                this.isValid = Validity.False;
                Object[] args = new Object[] { "ImageColorValue" };
                Message msg = new Message(
                        Severity.WARNING,
                        Context.OBJECT,
                        "org.jhove2.module.format.tiff.profile.TIFFProfile.MissingRequiredTag",
                        args, jhove2.getConfigInfo());
                this.missingRequiredTagMessages.add(msg);
            }
        }
        
        /*
         * PixelIntensityRange (0, 255) 
         */
        if ((entry = ifd.getEntries().get(TiffIFD.PIXELINTENSITYRANGE)) != null) {
            short [] pixelIR = ((ByteArray) entry.getValue()).getByteArray();
            if ( pixelIR.length < 2) {
                this.isValid = Validity.False;
                this.invalidPixelIntensityRangeValueMessage = new Message(
                        Severity.WARNING,
                        Context.OBJECT,
                        "org.jhove2.module.format.tiff.profile.TIFFITProfile.InvalidPixelIntensityRangeValueMessage",
                        jhove2.getConfigInfo());
            }
            if (pixelIR[0] != 0 || pixelIR[1] != 255) {
                this.isValid = Validity.False;
                this.invalidPixelIntensityRangeValueMessage = new Message(
                        Severity.WARNING,
                        Context.OBJECT,
                        "org.jhove2.module.format.tiff.profile.TIFFITProfile.InvalidPixelIntensityRangeValueMessage",
                        jhove2.getConfigInfo());
            }                
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

        if ((entry = ifd.getEntries().get(TiffIFD.INKSET)) != null) {
            this.isValid = Validity.False;
            Object[] args = new Object[] { entry.getName() };
            this.tagShouldNotBePresentMessage = new Message(
                    Severity.WARNING,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.profile.TIFFProfile.tagShouldNotBePresentMessage",
                    args, jhove2.getConfigInfo());
        }

        if ((entry = ifd.getEntries().get(TiffIFD.INKNAMES)) != null) {
            this.isValid = Validity.False;
            Object[] args = new Object[] { entry.getName() };
            this.tagShouldNotBePresentMessage = new Message(
                    Severity.WARNING,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.profile.TIFFProfile.tagShouldNotBePresentMessage",
                    args, jhove2.getConfigInfo());
        }

        if ((entry = ifd.getEntries().get(TiffIFD.NUMBEROFINKS)) != null) {
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

        if ((entry = ifd.getEntries().get(TiffIFD.COLORSEQUENCE)) != null) {
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

        if ((entry = ifd.getEntries().get(TiffIFD.RASTERPADDING)) != null) {
            this.isValid = Validity.False;
            Object[] args = new Object[] { entry.getName() };
            this.tagShouldNotBePresentMessage = new Message(
                    Severity.WARNING,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.profile.TIFFProfile.tagShouldNotBePresentMessage",
                    args, jhove2.getConfigInfo());
        }

        if ((entry = ifd.getEntries().get(TiffIFD.TRAPINDICATOR)) != null) {
            this.isValid = Validity.False;
            Object[] args = new Object[] { entry.getName() };
            this.tagShouldNotBePresentMessage = new Message(
                    Severity.WARNING,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.profile.TIFFProfile.tagShouldNotBePresentMessage",
                    args, jhove2.getConfigInfo());
        }

        if ((entry = ifd.getEntries().get(TiffIFD.CMYKEQUIVALENT)) != null) {
            this.isValid = Validity.False;
            Object[] args = new Object[] { entry.getName() };
            this.tagShouldNotBePresentMessage = new Message(
                    Severity.WARNING,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.profile.TIFFProfile.tagShouldNotBePresentMessage",
                    args, jhove2.getConfigInfo());
        }

        if ((entry = ifd.getEntries().get(TiffIFD.ICCPROFILE)) != null) {
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
    @ReportableProperty(order = 1, value = "Invalid DotRange message.")
    public Message getInvalidDotRangeMessage() {
        return invalidDotRangeMessage;
    }

    /**
     * @return the invalidPixelIntensityRangeValueMessage
     */
    @ReportableProperty(order = 2, value = "Invalid PixelIntensityRange value message.")
    public Message getInvalidPixelIntensityRangeValueMessage() {
        return invalidPixelIntensityRangeValueMessage;
    }
}