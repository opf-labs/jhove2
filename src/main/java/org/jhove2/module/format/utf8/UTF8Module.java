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

package org.jhove2.module.format.utf8;

import java.io.EOFException;
import java.io.IOException;

import org.jhove2.core.AbstractModule;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.Parsable;
import org.jhove2.core.Validatable;
import org.jhove2.core.source.Source;

/** JHOVE2 UTF-8 module.
 * 
 * @author mstrong, slabrams
 */
public class UTF8Module
	extends AbstractModule
	implements Parsable, Validatable
{
	/** UTF-8 module version identifier. */
	public static final String VERSION = "1.0.0";

	/** UTF-8 module release date. */
	public static final String DATE = "2009-06-16";
	
	/** UTF-8 module rights statement. */
	public static final String RIGHTS =
		"Copyright 2009 by The Regents of the University of California. " +
		"Available under the terms of the BSD license.";
	
	/** UTF-8 modeling class. */
	protected UTF8 utf8;

	/** Instantiate a new <code>UTF8Module</code>.
	 * @param utf8 UTF-8 modeling class
	 */
	public UTF8Module(UTF8 utf8) {
		super(VERSION, DATE, RIGHTS);
		
		this.utf8 = utf8;
	}

	/** Parse a source unit.
	 * @param jhove2 JHOVE2 framework
	 * @param source Source unit
	 * @return 0 
	 * @throws EOFException    If End-of-File is reached reading the source unit
	 * @throws IOException     If an I/O exception is raised reading the source
	 *                         unit
	 * @throws JHOVE2Exception
	 * @see org.jhove2.core.Parsable#parse(org.jhove2.core.JHOVE2, org.jhove2.core.source.Source)
	 */
	@Override
	public long parse(JHOVE2 jhove2, Source source)
		throws EOFException, IOException, JHOVE2Exception
	{
		return this.utf8.parse(jhove2, source);
	}

	/** Validate a source unit.  Implicitly set the starting and ending elapsed
	 * time.
	 * @param jhove2 JHOVE2 framework
	 * @return Validation status
	 * @see org.jhove2.core.Validatable#validate(org.jhove2.core.JHOVE2)
	 */
	@Override
	public Validity validate(JHOVE2 jhove2) {
		return this.utf8.validate(jhove2);
	}

	/** Get validation status.
	 * @return Validation status
	 * @see org.jhove2.core.Validatable#isValid()
	 */
	@Override
	public Validity isValid() {
		return this.utf8.isValid();
	}
}
