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
import org.jhove2.module.format.tiff.type.Short;
import org.jhove2.persist.FormatProfileAccessor;

import com.sleepycat.persist.model.Persistent;

/**
 * @author MStrong
 * 
 */
@Persistent
public class TiffEPProfile extends TiffProfile {

    /** Profile version identifier. */
    public static final String VERSION = "2.0.0";

    /** Profile release date. */
    public static final String RELEASE = "2010-09-10";

    /** Profile rights statement. */
    public static final String RIGHTS = "Copyright 2010 by The Regents of the University of California. "
            + "Available under the terms of the BSD license.";

    /** Profile validation coverage. */
    public static final Coverage COVERAGE = Coverage.Inclusive;

    /** missing strip tag or tile tag message */
    private Message missingStripOrTileTagsMessage;

    /** sensingMethod tag value is invalid message */
    private Message invalidSensingMethodValueMessage;

    /** missing CFAPattern tag or CFARepeatPatternDim tag message */
    private Message missingCFAPatternTagsMessage;

    /** missing required YCbCr Data and/or ReferenceBlackWhite tag message */
    private Message missingRequiredYCbCrDataMessage;

    /** invalid newSubFileType tag value message */
    private Message invalidNewSubfileTypeMessage;
    
    /**
     * 
     * @param format
     * @param formatProfileAccessor
     */
    public TiffEPProfile(Format format, FormatProfileAccessor formatProfileAccessor) {
        super(format, formatProfileAccessor);
    }
    
    @SuppressWarnings("unused")
	private TiffEPProfile(){
    	this (null,null);
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
        checkRequiredTags(jhove2, ifd);

        /*
         * Check required values.
         */

        /* newSubfileType != 0 && !=1 */
        long subfileType = ifd.getNewSubfileType();
        if (subfileType != 0 && subfileType != 1) {
            this.isValid = Validity.False;
            this.invalidNewSubfileTypeMessage = new Message(
                    Severity.WARNING,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.profile.TIFFEPProfile.InvalidNewSubfileTypeMessage",
                    jhove2.getConfigInfo());
        }

        /* Resolution Unit */
        if (!isResolutionUnitValid(ifd, new int[] { 1, 2, 3 })) {
            this.isValid = Validity.False;
            this.invalidResolutionUnitValueMessage = new Message(
                    Severity.WARNING,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.profile.TIFFProfile.InvalidResolutionUnitValueMessage",
                    jhove2.getConfigInfo());
        }

        /* Orientation */
        if (ifd.getEntries().get(TiffIFD.ORIENTATION) != null) {
            if (!isOrientationValid(ifd, new int[] { 1, 3, 6, 8, 9 })) {
                this.isValid = Validity.False;
                this.invalidOrientationValueMessage = new Message(
                        Severity.WARNING,
                        Context.OBJECT,
                        "org.jhove2.module.format.tiff.profile.TIFFProfile.InvalidOrientationValueMessage",
                        jhove2.getConfigInfo());
            }
        }

        /* PhotometricInterpretation */
        int photometricInterpretation = ifd.getPhotometricInterpretation();
        if (!(photometricInterpretation == 1 || photometricInterpretation == 2
                || photometricInterpretation == 6
                || photometricInterpretation == 32803 || photometricInterpretation > 32767)) {
            this.isValid = Validity.False;
            this.invalidPhotometricInterpretationValueMessage = new Message(
                    Severity.WARNING,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.profile.TIFFProfile.InvalidPhotometricInterpretationValueMessage",
                    jhove2.getConfigInfo());
        }

        /* planar configuration */
        int planarConfiguration = ifd.getPlanarConfiguration();
        if (planarConfiguration != 1 && planarConfiguration != 2) {
            this.isValid = Validity.False;
            this.invalidPlanarConfigurationValueMessage = new Message(
                    Severity.WARNING,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.profile.TIFFProfile.InvalidPhotometricInterpretationValueMessage",
                    jhove2.getConfigInfo());
        }
        /* sensing method */
        IFDEntry entry = null;
        int sensingMethod = -1;
        if ((entry = ifd.getEntries().get(TiffIFD.SENSINGMETHOD)) != null) {
            sensingMethod = ((Short) entry.getValue()).getValue();
        }
        if (entry == null || (sensingMethod < 0 || sensingMethod > 8)) {
            this.isValid = Validity.False;
            this.invalidSensingMethodValueMessage = new Message(
                    Severity.WARNING,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.profile.TIFFEPProfile.InvalidSensingMethodValueMessage",
                    jhove2.getConfigInfo());
        }

        if (photometricInterpretation == 32803) {
            if (ifd.getEntries().get(TiffIFD.CFAREPEATPATTTERNDIM) == null
                    || ifd.getEntries().get(TiffIFD.CFAREPEATPATTTERNDIM) == null) {
                this.isValid = Validity.False;
                this.missingCFAPatternTagsMessage = new Message(
                        Severity.WARNING,
                        Context.OBJECT,
                        "org.jhove2.module.format.tiff.profile.TIFFEPProfile.MissingCFAPatternTagsMessage",
                        jhove2.getConfigInfo());

            }
        }
        /* samples per pixel */
        int samplesPerPixel = ifd.getSamplesPerPixel();
        if (photometricInterpretation == 1
                || photometricInterpretation == 32803) {
            if (samplesPerPixel != 1) {
                this.isValid = Validity.False;
                this.invalidSPPValueMessage = new Message(
                        Severity.WARNING,
                        Context.OBJECT,
                        "org.jhove2.module.format.tiff.profile.TIFFProfile.InvalidSPPValueMessage",
                        jhove2.getConfigInfo());
            }
        }
        if (photometricInterpretation == 2 || photometricInterpretation == 6) {
            if (samplesPerPixel != 3) {
                this.isValid = Validity.False;
                this.invalidSPPValueMessage = new Message(
                        Severity.WARNING,
                        Context.OBJECT,
                        "org.jhove2.module.format.tiff.profile.TIFFProfile.InvalidSPPValueMessage",
                        jhove2.getConfigInfo());
            }
        }
        if (photometricInterpretation == 6) {
            if (ifd.getEntries().get(TiffIFD.YCBCRCOEFFICIENTS) == null
                    || ifd.getEntries().get(TiffIFD.YCBCRSUBSAMPLING) == null
                    || ifd.getEntries().get(TiffIFD.YCBCRPOSITIONING) == null
                    || ifd.getEntries().get(TiffIFD.REFERENCEBLACKWHITE) == null) {
                this.isValid = Validity.False;
                this.missingRequiredYCbCrDataMessage = new Message(
                        Severity.WARNING,
                        Context.OBJECT,
                        "org.jhove2.module.format.tiff.profile.TIFFEPProfile.MissingRequiredYCbCrDataMessage",
                        jhove2.getConfigInfo());
            }
        }

        /* compression */
        int compression = ifd.getCompression();
        if (compression != TiffIFD.NULL) {
            if (!(compression == 1 || compression == 7 || compression > 32767)) {
                this.isValid = Validity.False;
                this.invalidCompressionValueMessage = new Message(
                        Severity.WARNING,
                        Context.OBJECT,
                        "org.jhove2.module.format.tiff.profile.TIFFProfile.InvalidCompressionValueMessage",
                        jhove2.getConfigInfo());
            }
        }

    }

    /**
     * Checks if the tags required for TIFF/EP Profile are present
     * 
     * @param jhove2
     * @param ifd
     * @throws JHOVE2Exception
     */
    private void checkRequiredTags(JHOVE2 jhove2, TiffIFD ifd)
            throws JHOVE2Exception {
        /* ImageLength */
        if (!ifd.hasImageLength()) {
            this.isValid = Validity.False;
            Object[] args = new Object[] { "ImageLength" };
            Message msg = new Message(
                    Severity.WARNING,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.profile.TIFFProfile.MissingRequiredTag",
                    args, jhove2.getConfigInfo());
            this.missingRequiredTagMessages.add(msg);
        }

        /* ImageWidth */
        if (!ifd.hasImageWidth()) {
            this.isValid = Validity.False;
            Object[] args = new Object[] { "ImageWidth" };
            Message msg = new Message(
                    Severity.WARNING,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.profile.TIFFProfile.MissingRequiredTag",
                    args, jhove2.getConfigInfo());
            this.missingRequiredTagMessages.add(msg);
        }

        /* BitsPerSample */
        if ((ifd.getEntries().get(TiffIFD.BITSPERSAMPLE)) == null) {
            this.isValid = Validity.False;
            Object[] args = new Object[] { "BitsPerSample" };
            Message msg = new Message(
                    Severity.WARNING,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.profile.TIFFProfile.MissingRequiredTag",
                    args, jhove2.getConfigInfo());
            this.missingRequiredTagMessages.add(msg);
        }

        /* ImageDescription */
        if ((ifd.getEntries().get(TiffIFD.IMAGEDESCRIPTION)) == null) {
            this.isValid = Validity.False;
            Object[] args = new Object[] { "ImageDescription" };
            Message msg = new Message(
                    Severity.WARNING,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.profile.TIFFProfile.MissingRequiredTag",
                    args, jhove2.getConfigInfo());
            this.missingRequiredTagMessages.add(msg);
        }

        /* Compression */
        if ((ifd.getEntries().get(TiffIFD.COMPRESSION)) == null) {
            this.isValid = Validity.False;
            Object[] args = new Object[] { "Compression" };
            Message msg = new Message(
                    Severity.WARNING,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.profile.TIFFProfile.MissingRequiredTag",
                    args, jhove2.getConfigInfo());
            this.missingRequiredTagMessages.add(msg);
        }

        /* XSampling Frequency */
        if (!ifd.hasXResolution()) {
            this.isValid = Validity.False;
            Object[] args = new Object[] { "XResolution" };
            Message msg = new Message(
                    Severity.WARNING,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.profile.TIFFProfile.MissingRequiredTag",
                    args, jhove2.getConfigInfo());
            this.missingRequiredTagMessages.add(msg);
        }

        /* YSampling Frequency */
        if (!ifd.hasYResolution()) {
            this.isValid = Validity.False;
            Object[] args = new Object[] { "YResolution" };
            Message msg = new Message(
                    Severity.WARNING,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.profile.TIFFProfile.MissingRequiredTag",
                    args, jhove2.getConfigInfo());
            this.missingRequiredTagMessages.add(msg);
        }

        /* Scanner Manufacturer */
        if ((ifd.getEntries().get(TiffIFD.MAKE)) == null) {
            this.isValid = Validity.False;
            Object[] args = new Object[] { "Scanner Manufacturer - MAKE tag" };
            Message msg = new Message(
                    Severity.WARNING,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.profile.TIFFProfile.MissingRequiredTag",
                    args, jhove2.getConfigInfo());
            this.missingRequiredTagMessages.add(msg);
        }

        /* Scanner ModelName */// MODEL
        if ((ifd.getEntries().get(TiffIFD.MODEL)) == null) {
            this.isValid = Validity.False;
            Object[] args = new Object[] { "Scanner ModelName - MODEL tag" };
            Message msg = new Message(
                    Severity.WARNING,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.profile.TIFFProfile.MissingRequiredTag",
                    args, jhove2.getConfigInfo());
            this.missingRequiredTagMessages.add(msg);
        }

        /* Scanning Software */// SOFTWARE
        if ((ifd.getEntries().get(TiffIFD.SOFTWARE)) == null) {
            this.isValid = Validity.False;
            Object[] args = new Object[] { "Scanning Software - SOFTWARE tag" };
            Message msg = new Message(
                    Severity.WARNING,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.profile.TIFFProfile.MissingRequiredTag",
                    args, jhove2.getConfigInfo());
            this.missingRequiredTagMessages.add(msg);
        }

        /* Copyright */
        if ((ifd.getEntries().get(TiffIFD.COPYRIGHT)) == null) {
            this.isValid = Validity.False;
            Object[] args = new Object[] { "Copyright" };
            Message msg = new Message(
                    Severity.WARNING,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.profile.TIFFProfile.MissingRequiredTag",
                    args, jhove2.getConfigInfo());
            this.missingRequiredTagMessages.add(msg);
        }

        /* DateTimeCreated */
        if ((ifd.getEntries().get(TiffIFD.DATETIMEORIGINAL)) == null) {
            this.isValid = Validity.False;
            Object[] args = new Object[] { "DateTimeOriginal" };
            Message msg = new Message(
                    Severity.WARNING,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.profile.TIFFProfile.MissingRequiredTag",
                    args, jhove2.getConfigInfo());
            this.missingRequiredTagMessages.add(msg);
        }

        /* DateTime */
        if ((ifd.getEntries().get(TiffIFD.DATETIME)) == null) {
            this.isValid = Validity.False;
            Object[] args = new Object[] { "DateTime" };
            Message msg = new Message(
                    Severity.WARNING,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.profile.TIFFProfile.MissingRequiredTag",
                    args, jhove2.getConfigInfo());
            this.missingRequiredTagMessages.add(msg);
        }

        /* TIFF/EPStandardID */
        if ((ifd.getEntries().get(TiffIFD.TIFFEPSTANDARDID)) == null) {
            this.isValid = Validity.False;
            Object[] args = new Object[] { "TIFF/EP Standard ID" };
            Message msg = new Message(
                    Severity.WARNING,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.profile.TIFFProfile.MissingRequiredTag",
                    args, jhove2.getConfigInfo());
            this.missingRequiredTagMessages.add(msg);
        }

        /*
         * must have full complement of Strip tags or tile tags StripOffsets,
         * StripByteCounts, RowsPerStrip present OR TileWidth, TileLength,
         * TileOffsets, TileByteCounts present
         */
        if (!(ifd.hasStripOffsets && ifd.hasStripByteCounts() && ifd
                .hasRowsPerStrip())
                && !(ifd.hasTileWidth() && ifd.hasTileLength()
                        && ifd.hasTileOffsets() && ifd.hasTileByteCounts())) {
            if ((ifd.getEntries().get(TiffIFD.TIFFEPSTANDARDID)) == null) {
                this.isValid = Validity.False;
                this.missingStripOrTileTagsMessage = new Message(
                        Severity.WARNING,
                        Context.OBJECT,
                        "org.jhove2.module.format.tiff.profile.TIFFEPProfile.MissingStripOrTileTags",
                        jhove2.getConfigInfo());
            }
        }
    }

    /**
     * @return the missingStripOrTileTagMessage
     */
    @ReportableProperty(order = 1, value = "Missing strip or tile tags message.")
    public Message getMissingStripOrTileTags() {
        return missingStripOrTileTagsMessage;
    }

    /**
     * @return the invalidSensingMethodValueMessage
     */
    @ReportableProperty(order = 2, value = "Invalid SensingMethod tag value message.")
    public Message getInvalidSensingMethodValueMessage() {
        return invalidSensingMethodValueMessage;
    }

    /**
     * @return the missingCFAPatternTagsMessage
     */
    @ReportableProperty(order = 3, value = "Missing CFAPattern or CFARepeatPatternDim Message.")
    public Message getMissingCFAPatternTagsMessage() {
        return missingCFAPatternTagsMessage;
    }

    /**
     * @return the missingRequiredYCbCrDataMessage
     */
    @ReportableProperty(order = 4, value = "Missing required YCbCr Data or ReferenceBlackWhite tag message")
    public Message getMissingRequiredYCbCrDataMessage() {
        return missingRequiredYCbCrDataMessage;
    }

    /**
     * @return the invalidNewSubfileTypeMessage
     */
    @ReportableProperty(order = 5, value = "Invalid NewSubfileType tag value message.")
    public Message getInvalidNewSubfileTypeMessage() {
        return invalidNewSubfileTypeMessage;
    }

}
