/**
 * JHOVE2 - Next-generation architecture for format-aware characterization
 *
 * Copyright (c) 2009 by The Regents of the University of California.
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

package org.jhove2.module.format.wave;

import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.format.Format;
import org.jhove2.core.source.Source;
import org.jhove2.module.format.AbstractFormatProfile;
import org.jhove2.module.format.Validator;
/** Broadcase Wave Format (BWF) profile.
 * 
 * @author slabrams
 */
public class BroadcastWaveFormatProfile
        extends AbstractFormatProfile
        implements Validator
{
    /** Broadcast Wave Format module version identifier. */
    public static final String VERSION = "2.0.0";

    /** Broadcast Wave Format module release date. */
    public static final String RELEASE = "2010-09-10";

    /** Broadcast Wave Format module rights statement. */
    public static final String RIGHTS =
        "Copyright 2010 by The Regents of the University of California" +
        "Available under the terms of the BSD license.";
    
    /** Broadcast Wave Format validation coverage. */
    public static final Coverage COVERAGE = Coverage.Inclusive;
  
    /** Broadcast Wave Format validation status. */
    protected Validity isValid;

    /** Instantiate a new <code>BroadcastWaveProfile</code>
     * @param format Broadcast Wave Format.
     */
    public BroadcastWaveFormatProfile(Format format)
    {
        super(VERSION, RELEASE, RIGHTS, format);
        
        this.isValid = Validity.Undetermined;
    }

    /** Get profile coverage.
     * @return Profile coverage
     * @see org.jhove2.module.format.Validator#getCoverage()
     */
    @Override
    public Coverage getCoverage()
    {
        return COVERAGE;
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

    /** Validate the Broadcast Wave Format (BWF) source unit
     * @param jhove2 JHOVE2 framework
     * @param source Broadcast Wave Format source unit
     * @see org.jhove2.module.format.Validator#validate(org.jhove2.core.JHOVE2, org.jhove2.core.source.Source)
     */
    @Override
    public Validity validate(JHOVE2 jhove2, Source source)
            throws JHOVE2Exception
    {
        return this.isValid;
    }

}
