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

package org.jhove2.module.format.shapefile;

import java.io.EOFException;
import java.io.IOException;
import java.util.List;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.format.Format;
import org.jhove2.core.source.ClumpSource;
import org.jhove2.core.source.NamedSource;
import org.jhove2.core.source.Source;
import org.jhove2.module.format.BaseFormatModule;
import org.jhove2.module.format.Validator;

/**
 * JHOVE2 Shapefile module.
 * 
 * @author mstrong, slabrams
 */
public class ShapefileModule
	extends BaseFormatModule
	implements  Validator
{
	/** Shapefile module version identifier. */
	public static final String VERSION = "2.0.0";

	/** Shapefile module release date. */
	public static final String RELEASE = "2010-09-10";

	/** Shapefile module rights statement. */
	public static final String RIGHTS = "Copyright 2010 by The Board of " +
		"Trustees of the Leland Stanford Junior University. " +
		"Available under the terms of the BSD license.";
	
	/** Directory module validation coverage. */
	public static final Coverage COVERAGE = Coverage.Inclusive;

	/** Shapefile validation status. */
	protected Validity isValid;

	/** File prefix shared by the Shapefile main, index, and dBASE files. */
	protected String prefix;
	;

	/**
	 * Instantiate a new <code>ShapefileModule</code>.
	 * 
	 * @param format
	 *            Shapefile format
	 */
	public ShapefileModule(Format format) {
		super(VERSION, RELEASE, RIGHTS, format);
		this.isValid = Validity.Undetermined;
	}
	public ShapefileModule(){
		this(null);
	}
	/**
	 * Parse an Shapefile source unit.
	 * 
	 * @param jhove2
	 *            JHOVE2 framework
	 * @param source
	 *            Shapefile source unit
	 * @return 0
	 * @throws EOFException
	 *             If End-of-File is reached reading the source unit
	 * @throws IOException
	 *             If an I/O exception is raised reading the source unit
	 * @throws JHOVE2Exception
	 * @see org.jhove2.module.format.Parser#parse(org.jhove2.core.JHOVE2,
	 *      org.jhove2.core.source.Source)
	 */
	@Override
	public long parse(JHOVE2 jhove2, Source source)
		throws EOFException, IOException, JHOVE2Exception
	{
		if (source instanceof ClumpSource) {
			this.isValid = Validity.True;

			List<Source> sources = source.getChildSources();
			for (Source src : sources) {
				this.prefix = ((NamedSource) src).getSourceName();
				int in = this.prefix.indexOf('.');
				if (in > -1) {
					this.prefix = this.prefix.substring(0, in);
					break;
				}
			}
		} else {
			this.isValid = Validity.False;
		}

		return 0;
	}

	/**
	 * Validate a Shapefile source unit.
	 * 
	 * @param jhove2
	 *            JHOVE2 framework
	 * @param source
	 *            Source unit
	 * @return UTF-8 validation status
	 * @see org.jhove2.module.format.Validator#validate(org.jhove2.core.JHOVE2,
	 *      org.jhove2.core.source.Source)
	 */
	@Override
	public Validity validate(JHOVE2 jhove2, Source source) throws JHOVE2Exception {
		return this.isValid;
	}

	/** Get Shapefile module validation coverage.
	 * @return Shapefile module validation coverage
	 */
	@Override
	public Coverage getCoverage() {
		return COVERAGE;
	}
	
	/**
	 * Get file prefix shared by the Shapefile main, index, and dBASE files.
	 * 
	 * @return File prefix
	 */
	@ReportableProperty("File prefix shared by the Shapeful main, index, "
			+ "and dBASE files.")
	public String getPrefix() {
		return this.prefix;
	}

	/**
	 * Get Shapefile validation status.
	 * 
	 * @return Shapefile validation status
	 * @see org.jhove2.module.format.Validator#isValid()
	 */
	@Override
	public Validity isValid() {
		return this.isValid;
	}
}
