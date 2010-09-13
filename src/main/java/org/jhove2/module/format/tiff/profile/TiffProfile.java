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

package org.jhove2.module.format.tiff.profile;

import java.util.ArrayList;
import java.util.List;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.Message;
import org.jhove2.core.format.Format;
import org.jhove2.core.io.Input;
import org.jhove2.core.source.Source;
import org.jhove2.module.format.AbstractFormatProfile;
import org.jhove2.module.format.Validator;
import org.jhove2.module.format.tiff.IFD;
import org.jhove2.module.format.tiff.TiffIFD;
import org.jhove2.module.format.tiff.TiffModule;

/** Abstract class for TiffProfiles
 * 
 *  
 * @author mstrong
 */
public abstract class TiffProfile
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

    /** Invalid compression value message */
    protected Message invalidCompressionValueMessage;

    /** Invalid photometric interpretation value message */
    protected Message invalidPhotometricInterpretationValueMessage;

    /** Invalid resolution unit value message */
    protected Message invalidResolutionUnitValueMessage;


    /** Instantiate a new <code>TiffProfile</code>.
     * @param format Profile format 
     */
    public TiffProfile(Format format) {
        super(VERSION, RELEASE, RIGHTS, format);

        this.isValid = Validity.Undetermined;
        this.missingRequiredTagMessages = new ArrayList<Message>();
    }

    /** Validate the profile.
     * @param jhove2 JHOVE2 framework
     * @param source TIFF source unit
     * @see org.jhove2.module.format.Validator#validate(org.jhove2.core.JHOVE2, org.jhove2.core.source.Source)
     */
    public Validity validate(JHOVE2 jhove2, Source source, Input input)
    throws JHOVE2Exception
    {
        if (this.module != null) {
            List<IFD> IFDList = ((TiffModule) this.module).getIFDs();
            if (IFDList != null) {
                for (IFD ifd:IFDList) {
                    if (ifd instanceof TiffIFD) {
                        if (((TiffIFD) ifd).hasPhotometricInterpretation()){
                            this.isValid = Validity.True;

                            validateThisProfile(jhove2, (TiffIFD) ifd);
                        }
                    }
                }
            }
        }
        return this.isValid;
    }

    public abstract void validateThisProfile(JHOVE2 jhove2, TiffIFD ifd) throws JHOVE2Exception;

    /**
     *  Checks if the value of the Compression tag matches
     *  any of the values in the array passed to it.
     *
     *  @param ifd     The IFD being checked
     *  @param values  An array of values, any of which can be valid
     */
    protected boolean isCompressionValid (TiffIFD ifd, int [] values) 
    {
        int compression = ((TiffIFD) ifd).getCompression();
        for (int i = 0; i < values.length; i++) {
            if (compression == values[i]) {
                return true;
            }
        }
        return false;
    }

    /**
     *  Checks if the value of the PlanarConfiguration tag matches
     *  the value passed to it.
     *
     *  @param ifd     The IFD being checked
     *  @param value   A value which must match the tag value to
     *                 satisfy the test.
     */
    protected boolean isCompressionValid (TiffIFD ifd, int value)
    {
        int [] values = { value } ;
        return isCompressionValid (ifd, values);
    }

    /**
     *  Checks if the value of the PhotometricInterpretation tag matches
     *  any of the values in the array passed to it.
     *
     *  @param ifd     The IFD being checked
     *  @param values  An array of values, any of which can be valid
     */
    protected boolean isPhotometricInterpretationValid (TiffIFD ifd,
            int [] values) 
    {
        int pInt = ifd.getPhotometricInterpretation();
        for (int i = 0; i < values.length; i++) {
            if (pInt == values[i]) {
                return true;
            }
        }
        return false;
    }

    /**
     *  Checks if the value of the PhotometricInterpretation tag matches
     *  the value passed to it.
     *
     *  @param ifd     The IFD being checked
     *  @param value   A value which must match the tag value to
     *                 satisfy the test.
     */
    protected boolean isPhotometricInterpretationValid (TiffIFD ifd,
            int value)
    {
        int [] values = { value } ;
        return isPhotometricInterpretationValid (ifd, values);
    }

    /**
     *  Checks if the value of the ResolutionUnit tag matches
     *  any of the values in the array passed to it.
     *
     *  @param ifd     The IFD being checked
     *  @param values  An array of values, any of which will
     *                 satisfy the test.
     */
    protected boolean isResolutionUnitValid (TiffIFD ifd, int [] values) 
    {
        int ru = ifd.getResolutionUnit();
        for (int i = 0; i < values.length; i++) {
            if (ru == values[i]) {
                return true;
            }
        }
        return false;
    }

    /**
     *  Checks if the value of the ResolutionUnit tag matches
     *  the value passed to it.
     *
     *  @param ifd     The IFD being checked
     *  @param value   A value which must match the tag value to
     *                 satisfy the test.
     */
    protected boolean isResolutionUnitValid (TiffIFD ifd, int value)
    {
        int [] values = { value } ;
        return isResolutionUnitValid (ifd, values);
    }


    /**
     *  Checks if the value of the XResolution tag matches
     *  any of the values in the array passed to it.
     *
     *  @param ifd     The IFD being checked
     *  @param values  An array of values, any of which will
     *                 satisfy the test.
     */
    protected boolean isXResolutionValid (TiffIFD ifd, int [] values) 
    {
        long xf = ifd.getXResolution();
        for (int i = 0; i < values.length; i++) {
            if (xf == values[i]) {
                return true;
            }
        }
        return false;
    }



    /**
     *  Checks if the value of the XResolution tag matches
     *  any of the values in the array passed to it.
     *
     *  @param ifd     The IFD being checked
     *  @param values  An array of values, any of which will
     *                 satisfy the test.
     */
    protected boolean isYResolutionValid (TiffIFD ifd, int [] values) 
    {
        long yf = ifd.getYResolution().toLong ();
        for (int i = 0; i < values.length; i++) {
            if (yf == values[i]) {
                return true;
            }
        }
        return false;
    }




    /**
     *  Checks if the value of the SamplesPerPixel tag matches
     *  any of the values in the array passed to it.
     *
     *  @param ifd     The IFD being checked
     *  @param values  An array of values, any of which will
     *                 satisfy the test.
     */
    protected boolean isSamplesPerPixelValid (TiffIFD ifd, int [] values) 
    {
        int spp = ifd.getSamplesPerPixel ();
        for (int i = 0; i < values.length; i++) {
            if (spp == values[i]) {
                return true;
            }
        }
        return false;
    }

    /**
     *  Checks if the value of the SamplesPerPixel tag matches
     *  the value passed to it.
     *
     *  @param ifd     The IFD being checked
     *  @param value   A value which must match the tag value to
     *                 satisfy the test.
     */
    protected boolean isSamplesPerPixelValid (TiffIFD ifd, int value)
    {
        int [] values = { value } ;
        return isSamplesPerPixelValid (ifd, values);
    }

    /**
     *  Checks if the value of the PlanarConfiguration tag matches
     *  any of the values in the array passed to it.
     *
     *  @param ifd     The IFD being checked
     *  @param values  An array of values, any of which will
     *                 satisfy the test.
     */
    protected boolean isPlanarConfigurationValid (TiffIFD ifd, int [] values) 
    {
        int planarConfiguration = ifd.getPlanarConfiguration ();
        for (int i = 0; i < values.length; i++) {
            if (planarConfiguration == values[i]) {
                return true;
            }
        }
        return false;
    }

    /**
     *  Checks if the value of the PlanarConfiguration tag matches
     *  the value passed to it.
     *
     *  @param ifd     The IFD being checked
     *  @param value   A value which must match the tag value to
     *                 satisfy the test.
     */
    protected boolean isPlanarConfigurationValid (TiffIFD ifd, int value)
    {
        int [] values = { value } ;
        return isPlanarConfigurationValid (ifd, values);
    }

    /**
     *  Checks if the value of the Orientation tag matches
     *  any of the values in the array passed to it.
     *
     *  @param ifd     The IFD being checked
     *  @param values  An array of values, any of which will
     *                 satisfy the test.
     */
    protected boolean isOrientationValid (TiffIFD ifd, int [] values) 
    {
        int orientation = ifd.getOrientation();

        for (int i = 0; i < values.length; i++) {
            if (orientation == values[i]) {
                return true;
            }
        }
        return false;
    }

    /**
     *  Checks if the value of the Orientation tag matches
     *  the value passed to it.
     *
     *  @param ifd     The IFD being checked
     *  @param value   A value which must match the tag value to
     *                 satisfy the test.
     */
    protected boolean isOrientationValid (TiffIFD ifd, int value)
    {
        int [] values = { value } ;
        return isOrientationValid (ifd, values);
    }

    /**
     *  Checks if the value of the ImageColorIndicator tag matches
     *  any of the values in the array passed to it.
     *
     *  @param ifd     The IFD being checked
     *  @param values  An array of values, any of which will
     *                 satisfy the test.
     */
    protected boolean isImageColorIndicatorValid (TiffIFD ifd, int [] values) 
    {
        int imageColorIndicator = ifd.getImageColorIndicator ();
        for (int i = 0; i < values.length; i++) {
            if (imageColorIndicator == values[i]) {
                return true;
            }
        }
        return false;
    }

    /**
     *  Checks if the value of the ImageColorIndicator tag matches
     *  the value passed to it.
     *
     *  @param ifd     The IFD being checked
     *  @param value   A value which must match the tag value to
     *                 satisfy the test.
     */
    protected boolean isImageColorIndicatorValid (TiffIFD ifd, int value)
    {
        int [] values = { value } ;
        return isImageColorIndicatorValid (ifd, values);
    }

    /**
     *  Checks if the value of the NewSubfileType tag matches
     *  any of the values in the array passed to it.
     *
     *  @param ifd     The IFD being checked
     *  @param values  An array of long values, any of which will
     *                 satisfy the test.
     */
    protected boolean isNewSubfileTypeValid (TiffIFD ifd, long [] values) 
    {
        long newSubfileType = ifd.getNewSubfileType ();
        for (int i = 0; i < values.length; i++) {
            if (newSubfileType == values[i]) {
                return true;
            }
        }
        return false;
    }

    /**
     *  Checks if the value of the NewSubfileType tag matches
     *  the value passed to it.
     *
     *  @param ifd     The IFD being checked
     *  @param value   A value which must match the tag value to
     *                 satisfy the test.
     */
    protected boolean isNewSubfileTypeValid (TiffIFD ifd, long value)
    {
        long [] values = { value } ;
        return isNewSubfileTypeValid (ifd, values);
    }

    /**
     *  Checks if the value of the BackgroundColorIndicator tag matches
     *  any of the values in the array passed to it.
     *
     *  @param ifd     The IFD being checked
     *  @param values  An array of values, any of which will
     *                 satisfy the test.
     */
    protected boolean isBackgroundColorIndicatorValid (TiffIFD ifd,
            int [] values) 
    {
        int backgroundIndicator = ifd.getBackgroundColorIndicator ();
        for (int i = 0; i < values.length; i++) {
            if (backgroundIndicator == values[i]) {
                return true;
            }
        }
        return false;
    }

    /**
     *  Checks if the value of the BackgroundColorIndicator tag matches
     *  the value passed to it.
     *
     *  @param ifd     The IFD being checked
     *  @param value   A value which must match the tag value to
     *                 satisfy the test.
     */
    protected boolean isBackgroundColorIndicatorValid (TiffIFD ifd,
            int value)
    {
        int [] values = { value } ;
        return isBackgroundColorIndicatorValid (ifd, values);
    }

    /** Checks the DotRange against a minimum and a maximum value.  Returns
     * true if the DotRange exists, is well-formed (i.e., has at least
     * 2 values, and the first two values equal minValue and maxValue
     * respectively.
     */
    protected boolean isDotRangeValid (TiffIFD ifd, int minValue,
            int maxValue) 
    {
        int [] dotRange = ifd.getDotRange ();
        if (dotRange == null || dotRange.length < 2) {
            return false;
        }
        return (dotRange[0] == minValue || dotRange[1] == maxValue);
    }


    /**
     *  Checks if the value of the ImageWidth tag matches
     *  any of the values in the array passed to it.
     *
     *  @param ifd     The IFD being checked
     *  @param values  An array of values, any of which will
     *                 satisfy the test.
     */
    protected boolean isImageWidthValid (TiffIFD ifd, int [] values) 
    {
        long iw = ifd.getImageWidth ();
        for (int i = 0; i < values.length; i++) {
            if (iw == values[i]) {
                return true;
            }
        }
        return false;
    }

    /**
     *  Checks if the value of the Indexed tag matches
     *  any of the values in the array passed to it.
     *
     *  @param ifd     The IFD being checked
     *  @param values  An array of values, any of which will
     *                 satisfy the test.
     */
    protected boolean isIndexedValid (TiffIFD ifd, int [] values) 
    {
        int ix = ifd.getIndexed ();
        for (int i = 0; i < values.length; i++) {
            if (ix == values[i]) {
                return true;
            }
        }
        return false;
    }

    /**
     *  Checks if the value of the Indexed tag matches
     *  any of the values in the array passed to it.
     *
     *  @param ifd     The IFD being checked
     *  @param values  An array of values, any of which will
     *                 satisfy the test.
     */
    protected boolean isFillOrderValid (TiffIFD ifd, int [] values) 
    {
        int f = ifd.getFillOrder ();
        for (int i = 0; i < values.length; i++) {
            if (f == values[i]) {
                return true;
            }
        }
        return false;
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
    @ReportableProperty(order=1, value="Missing required tags.")
    public List<Message> getMissingRequiredTagMessages() {
        return this.missingRequiredTagMessages;
    }

    /** Get invalid compression value message.
     * @return Invalid Compression Value Message
     */
    @ReportableProperty(order=2, value="Invalid Compression Value Message.")
    public Message getInvalidCompressionValueMessage() {
        return this.invalidCompressionValueMessage;
    }

    /** Get invalid PhotometricInterpretation Value Message
     * @return invalid PhotometricInterpretation Value Message
     */
    @ReportableProperty(order=3, value="Invalid PhotometricInterpretation Value Message.")
    public Message getinvalidPhotometricInterpretationValueMessage() {
        return this.invalidPhotometricInterpretationValueMessage;
    }

    /** Get Invalid Resolution Unit message.
     * @return Invalid Resolution Unit message
     */
    @ReportableProperty(order=4, value="Invalid Resolution Unit Value message.")
    public Message getInvalidResolutionUnitValueMessage() {
        return this.invalidResolutionUnitValueMessage;
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
