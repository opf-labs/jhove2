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

package org.jhove2.module.format.tiff.tiffit;

import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.format.Format;
import org.jhove2.core.io.Input;
import org.jhove2.core.source.Source;
import org.jhove2.module.format.AbstractFormatProfile;
import org.jhove2.module.format.Validator;

/**
 * @author mstrong
 *
 */
public class TiffItProfile 
	extends AbstractFormatProfile 
	implements Validator {

	public static final String VERSION = "2.0.0";

	/** TIFF-IT profile release date. */
	public static final String RELEASE = "2010-09-10";

	/** TIFF-IT profile rights statement. */
	public static final String RIGHTS =
		"Copyright 2010 by The Regents of the University of California. " +
		"Available under the terms of the BSD license.";
	
	/** TIFF-IT profile validation coverage. */
	public static final Coverage COVERAGE = Coverage.Inclusive;

	/** TIFF-IT validation status. */
	protected Validity isValid;

	/**
	 * Instantiate a new <code>TiffItProfile</code>.
	 * 
	 * @param format
	 *            TIFF-IT format
	 */
	public TiffItProfile(Format format) {
		super(VERSION, RELEASE, RIGHTS, format);

		this.isValid = Validity.Undetermined;
	}

	public TiffItProfile(){
		this(null);
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

	/**
	 * Validate an TIFF-IT source unit.
	 * 
	 * @param jhove2
	 *            JHOVE2 framework
	 * @param source
	 *            TIFF-IT source unit
	 * @param input
	 *            TIFF-IT source input
	 * @return TIFF-IT validation status
	 * @see org.jhove2.module.format.Validator#validate(org.jhove2.core.JHOVE2,
	 *      org.jhove2.core.source.Source, org.jhove2.core.io.Input)
	 */
	@Override
	public Validity validate(JHOVE2 jhove2, Source source, Input input)
	    throws JHOVE2Exception
	{
		if (this.module != null) {
			this.isValid = Validity.True;
		}
		return this.isValid;
	}

}
