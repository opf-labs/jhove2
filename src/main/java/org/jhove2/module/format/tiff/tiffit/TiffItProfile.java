/**
 * 
 */
package org.jhove2.module.format.tiff.tiffit;

import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.format.Format;
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

	public static final String VERSION = "1.9.5";

	/** TIFF-IT profile release date. */
	public static final String RELEASE = "2010-05-17";

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


	/** Get TIFF-IT profile validation coverage.
	 * @return TIFF-IT profile validation coverage
	 */
	@Override
	public Coverage getCoverage() {
		// TODO Auto-generated method stub
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
	 * @return TIFF-IT validation status
	 * @see org.jhove2.module.format.Validator#validate(org.jhove2.core.JHOVE2,
	 *      org.jhove2.core.source.Source)
	 */
	@Override
	public Validity validate(JHOVE2 jhove2, Source source)
			throws JHOVE2Exception {
		if (this.module != null) {
			this.isValid = Validity.True;
		}
		return this.isValid;
	}

}
