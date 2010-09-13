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
import java.util.List;

import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.Message;
import org.jhove2.core.format.Format;
import org.jhove2.core.io.Input;
import org.jhove2.core.source.Source;
import org.jhove2.module.format.Validator;
import org.jhove2.module.format.tiff.IFD;
import org.jhove2.module.format.tiff.IFDEntry;
import org.jhove2.module.format.tiff.TiffIFD;
import org.jhove2.module.format.tiff.TiffModule;

/**
 * @author mstrong
 *
 */
public class TiffItProfile 
extends TiffProfile 
implements Validator {


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
     * @param source
     *            TIFF-IT source unit
     * @return TIFF-IT validation status
     * @see org.jhove2.module.format.Validator#validate(org.jhove2.core.JHOVE2,
     *      org.jhove2.core.source.Source)
     */
    @Override
    public Validity validate(JHOVE2 jhove2, Source source, Input input)
    throws JHOVE2Exception {
        if (this.module != null) {
            int version = ((TiffModule) this.module).getTiffVersion();
            if (version < 6) {
                isValid = Validity.False;
            }
            else {

                List<IFD> ifdList = ((TiffModule) this.module).getIFDs();
                if (ifdList != null) {
                    /* traverse through list */
                    for (IFD ifd : ifdList) {
                        List<IFDEntry> entries = ifd.getIFDEntries();
                        /* check entries for validity and conforance to Profile*/
                    }
                }
            }

        }
        return this.isValid;
    }

    @Override
    public void validateThisProfile(JHOVE2 jhove2, TiffIFD ifd)
            throws JHOVE2Exception {
        // TODO Auto-generated method stub
        
    }
    
    /** Get TIFF-IT profile validation coverage.
     * @return TIFF-IT profile validation coverage
     */
    @Override
    public Coverage getCoverage() {
        return COVERAGE;
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
