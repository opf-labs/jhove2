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

package org.jhove2.module.format.utf8.ascii;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.Message;
import org.jhove2.core.Message.Context;
import org.jhove2.core.Message.Severity;
import org.jhove2.core.format.Format;
import org.jhove2.core.source.Source;
import org.jhove2.module.format.AbstractFormatProfile;
import org.jhove2.module.format.Validator;
import org.jhove2.module.format.utf8.UTF8Module;
import org.jhove2.module.format.utf8.unicode.CodeBlock;

/**
 * ASCII profile of UTF-8.
 * 
 * @author mstrong, slabrams
 */
public class ASCIIProfile
	extends AbstractFormatProfile
	implements Validator
{
	/** Basic Latin code block. */
	public static final String BASIC_LATIN = "Basic Latin";
	
	/** ASCII profile version identifier. */
	public static final String VERSION = "2.0.0";

	/** ASCII profile release date. */
	public static final String RELEASE = "2010-09-10";

	/** ASCII profile rights statement. */
	public static final String RIGHTS =
		"Copyright 2010 by The Regents of the University of California. " +
		"Available under the terms of the BSD license.";
	
	/** ASCII profile validation coverage. */
	public static final Coverage COVERAGE = Coverage.Inclusive;

	/** ASCII validation status. */
	protected Validity isValid;

	/** Non-Basic Latin code blocks message. */
	protected Message nonBasicLatinMessage;

	/** Non-Basic Latin code blocks. */
	protected Set<CodeBlock> nonBasicLatinCodeBlocks;

	/**
	 * Instantiate a new <code>ASCIIProfile</code>.
	 * 
	 * @param format
	 *            ASCII format
	 */
	public ASCIIProfile(Format format) {
		super(VERSION, RELEASE, RIGHTS, format);

		this.isValid = Validity.Undetermined;
		this.nonBasicLatinCodeBlocks = new TreeSet<CodeBlock>();
	}
	
	public ASCIIProfile(){
		this(null);
	}

	/**
	 * Validate an ASCII source unit.
	 * 
	 * @param jhove2
	 *            JHOVE2 framework
	 * @param source
	 *            ASCII source unit
	 * @return ASCII validation status
	 * @see org.jhove2.module.format.Validator#validate(org.jhove2.core.JHOVE2,
	 *      org.jhove2.core.source.Source)
	 */
	@Override
	public Validity validate(JHOVE2 jhove2, Source source) throws JHOVE2Exception {
		if (this.module != null) {
			this.isValid = Validity.True;

			Set<CodeBlock> blocks = ((UTF8Module) this.module).getCodeBlocks();
			Iterator<CodeBlock> iter = blocks.iterator();
			while (iter.hasNext()) {
				CodeBlock block = iter.next();
				String name = block.getName();
				if (!name.equals(BASIC_LATIN)) {
					this.isValid = Validity.False;
					this.nonBasicLatinCodeBlocks.add(block);
				}
			}
		}
		if (this.nonBasicLatinCodeBlocks.size() > 0) {
			StringBuffer buffer = new StringBuffer();
			Iterator<CodeBlock> iter = this.nonBasicLatinCodeBlocks.iterator();
			for (int i = 0; iter.hasNext(); i++) {
				CodeBlock block = iter.next();
				if (i > 0) {
					buffer.append(",");
				}
				buffer.append(block);
			}
			Object[]messageArgs = new Object[]{buffer.toString()};
			this.nonBasicLatinMessage = new Message(Severity.ERROR,
					Context.OBJECT, 
					"org.jhove2.module.format.utf8.ascii.ASCIIProfile.nonBasicLatinMessage",
					messageArgs, jhove2.getConfigInfo());
		}

		return this.isValid;
	}

	/** Get ASCII profile validation coverage.
	 * @return ASCII profile validation coverage
	 */
	@Override
	public Coverage getCoverage() {
		return COVERAGE;
	}
	
	/**
	 * Get non-Basic Latin code block message.
	 * 
	 * @return Non-Basic Latin code block
	 */
	@ReportableProperty("Non-Basic Latin code blocks message.")
	public Message getNonBasicLatinCodeBlocks() {
		return this.nonBasicLatinMessage;
	}

	/**
	 * Get ASCII validation status.
	 * 
	 * @return ASCII validation status
	 * @see org.jhove2.module.format.Validator#isValid()
	 */
	@Override
	public Validity isValid() {
		return this.isValid;
	}
}
