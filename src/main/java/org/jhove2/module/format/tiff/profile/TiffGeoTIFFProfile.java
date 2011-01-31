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
import org.jhove2.module.format.tiff.TiffIFD;
import org.jhove2.persist.FormatProfileAccessor;

import com.sleepycat.persist.model.Persistent;

/**
 * @author MStrong
 * 
 */
@Persistent
public class TiffGeoTIFFProfile extends TiffProfile {

    /** Profile version identifier. */
    public static final String VERSION = "2.0.0";

    /** Profile release date. */
    public static final String RELEASE = "2010-09-10";

    /** Profile rights statement. */
    public static final String RIGHTS = "Copyright 2010 by The Regents of the University of California. "
            + "Available under the terms of the BSD license.";

    /** Profile validation coverage. */
    public static final Coverage COVERAGE = Coverage.Inclusive;

    /** ModelTransformation tag / ModelTiePoint tag not defined message */
    private Message modelTagsNotDefinedMessage;

    /**
     * 
     * @param format
     * @param formatProfileAccessor
     */
    public TiffGeoTIFFProfile(Format format, FormatProfileAccessor formatProfileAccessor) {
        super(format, formatProfileAccessor);
    }
    
    @SuppressWarnings("unused")
	private TiffGeoTIFFProfile(){
    	this(null,null);
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

        if (ifd.getEntries().get(TiffIFD.GEOKEYDIRECTORY) == null) {
            this.isValid = Validity.False;
            Object[] args = new Object[] { "GeoKeyDirectory" };
            Message msg = new Message(
                    Severity.WARNING,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.profile.TIFFProfile.MissingRequiredTag",
                    args, jhove2.getConfigInfo());
            this.missingRequiredTagMessages.add(msg);
        }

        /*
         * Exactly one of modelTiepointTag and modelTransformationTag must be
         * present.
         */
        boolean hasModelTiepoint = (ifd.getEntries().get(TiffIFD.MODELTIEPOINT) != null);
        boolean hasModelTransformation = (ifd.getEntries().get(
                TiffIFD.MODELTRANSFORMATION) != null);
        if ((hasModelTiepoint && hasModelTransformation)
                || (!hasModelTiepoint && !hasModelTransformation)) {
            this.isValid = Validity.False;
            this.modelTagsNotDefinedMessage = new Message(
                    Severity.WARNING,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.profile.TIFFGeoTiffProfile.ModelTagsNotDefinedMessage",
                    jhove2.getConfigInfo());
        }
    }

    /**
     * @return the modelTagsNotDefinedMessage
     */
    @ReportableProperty(order = 1, value = "Model tags not defined message.")
    public Message getModelTagsNotDefinedMessage() {
        return modelTagsNotDefinedMessage;
    }

}
