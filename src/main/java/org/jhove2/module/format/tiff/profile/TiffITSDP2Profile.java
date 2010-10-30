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

/**
 * @author MStrong
 * 
 */
public class TiffITSDP2Profile extends TiffItProfile {

    /** Profile version identifier. */
    public static final String VERSION = "2.0.0";

    /** Profile release date. */
    public static final String RELEASE = "2010-09-10";

    /** Profile rights statement. */
    public static final String RIGHTS = "Copyright 2010 by The Regents of the University of California. "
            + "Available under the terms of the BSD license.";

    /** Profile validation coverage. */
    public static final Coverage COVERAGE = Coverage.Inclusive;
    
    /** invalid Inkset value message */
    protected Message InvalidInksetValueMessage;

    /** invalid colorSequence tag value message */
    protected Message InvalidColorSequenceMessage;

    /** invalid NumberOfInks value message */
    protected Message InvalidNumberOfInksValueMessage;

    public TiffITSDP2Profile(Format format) {
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
        
        /* Check tags which must NOT be defined */
        
        IFDEntry entry = null;
        if ((entry = ifd.getEntries().get(TiffIFD.DOCUMENTNAME)) != null) {
            Object[] args = new Object[] { entry.getName() };
            this.tagShouldNotBePresentMessage = new Message(
                    Severity.WARNING,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.profile.TIFFProfile.tagShouldNotBePresentMessage",
                    args, jhove2.getConfigInfo());
        }
        
          if  ((entry = ifd.getEntries().get(TiffIFD.MODEL)) != null) {
              Object[] args = new Object[] { entry.getName() };
              this.tagShouldNotBePresentMessage = new Message(
                      Severity.WARNING,
                      Context.OBJECT,
                      "org.jhove2.module.format.tiff.profile.TIFFProfile.tagShouldNotBePresentMessage",
                      args, jhove2.getConfigInfo());
          }
         if   ((entry = ifd.getEntries().get(TiffIFD.PAGENAME)) != null) {
             Object[] args = new Object[] { entry.getName() };
             this.tagShouldNotBePresentMessage = new Message(
                     Severity.WARNING,
                     Context.OBJECT,
                     "org.jhove2.module.format.tiff.profile.TIFFProfile.tagShouldNotBePresentMessage",
                     args, jhove2.getConfigInfo());
         }
         if ((entry = ifd.getEntries().get(TiffIFD.HOSTCOMPUTER)) != null) {
             Object[] args = new Object[] { entry.getName() };
             this.tagShouldNotBePresentMessage = new Message(
                     Severity.WARNING,
                     Context.OBJECT,
                     "org.jhove2.module.format.tiff.profile.TIFFProfile.tagShouldNotBePresentMessage",
                     args, jhove2.getConfigInfo());
         }
         if ((entry = ifd.getEntries().get(TiffIFD.SITE)) != null) {
             Object[] args = new Object[] { entry.getName() };
             this.tagShouldNotBePresentMessage = new Message(
                     Severity.WARNING,
                     Context.OBJECT,
                     "org.jhove2.module.format.tiff.profile.TIFFProfile.tagShouldNotBePresentMessage",
                     args, jhove2.getConfigInfo());
         }
         if ((entry = ifd.getEntries().get(TiffIFD.COLORSEQUENCE)) != null) {
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


        /* Check required values. */

        if (!isNewSubfileTypeValid(ifd, 0)) {
            this.isValid = Validity.False;
            this.invalidNewSubfileTypeMessage = new Message(
                    Severity.WARNING,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.profile.TIFFProfile.invalidNewSubfileTypeMessage",
                    jhove2.getConfigInfo());
        }
        
        int[] bps = ifd.getBitsPerSample();
        if (bps[0] != 1) {
            this.isValid = Validity.False;
            Object[] args = new Object[] { 1 };
            this.invalidBPSValueMessage = new Message(
                    Severity.WARNING,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.profile.TIFFITProfile.InvalidBPSValueMessage",
                    args, jhove2.getConfigInfo());
        }

        if (!isCompressionValid(ifd, new int [] {1, 4, 8} )) {
            this.isValid = Validity.False;
            this.invalidCompressionValueMessage = new Message(
                    Severity.WARNING,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.profile.TIFFProfile.InvalidCompressionValueMessage",
                    jhove2.getConfigInfo());
        }

        if (!isPhotometricInterpretationValid (ifd, 5)) {
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
                    "org.jhove2.module.format.tiff.profile.TIFFProfile.invalidOrientationValueMessage",
                    jhove2.getConfigInfo());
        }

        if (!isSamplesPerPixelValid(ifd, new int[] {1, 4})) {
            this.isValid = Validity.False;
            Object[] args = new Object[] { 1 };
            this.invalidSPPValueMessage = new Message(
                    Severity.WARNING,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.profile.TIFFITProfile.InvalidSPPValueMessage",
                    args, jhove2.getConfigInfo());
        }

        if (!isPlanarConfigurationValid(ifd, 2)) {
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


       if ((entry = ifd.getEntries().get(TiffIFD.INKSET)) != null) {
            int inkset = (Short) entry.getValue();
                if ( inkset != 1) {
                    this.isValid = Validity.False;
                    this.InvalidInksetValueMessage = new Message(
                            Severity.WARNING,
                            Context.OBJECT,
                            "org.jhove2.module.format.tiff.profile.TIFFITSDProfile.InvalidInksetValueMessage",
                            jhove2.getConfigInfo());
                }
            }       
        
        /*
         * If NumberOfInks tag is used, it must have the same value as the value of SamplesPerPixel
         */
        if ((entry = ifd.getEntries().get(TiffIFD.NUMBEROFINKS)) != null) {
            if ((Short) entry.getValue() != 4) {
                this.isValid = Validity.False;
                this.InvalidNumberOfInksValueMessage = new Message(
                        Severity.WARNING,
                        Context.OBJECT,
                        "org.jhove2.module.format.tiff.profile.TIFFISDTProfile.InvalidNumberOfInksValueMessage",
                        jhove2.getConfigInfo());
            }
        }
    }
    /**
     * @return the invalidInksetValueMessage
     */
    @ReportableProperty(order = 1, value = "Invalid Inkset Value message")
    public Message getInvalidInksetValueMessage() {
        return InvalidInksetValueMessage;
    }

    /**
     * @return the invalidColorSequenceMessage
     */
    @ReportableProperty(order = 2, value = "Invalid ColorSequence value message")
    public Message getInvalidColorSequenceMessage() {
        return InvalidColorSequenceMessage;
    }

    /**
     * @return the invalidNumberOfInksValueMessage
     */
    @ReportableProperty(order = 3, value = "Invalid NumberOfInks value message")
    public Message getInvalidNumberOfInksValueMessage() {
        return InvalidNumberOfInksValueMessage;
    }

}
