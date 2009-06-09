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

package org.jhove2.module.format.utf8.ascii;

import java.util.Iterator;
import java.util.Set;

import org.jhove2.annotation.ReportableMessage;
import org.jhove2.core.AbstractFormatProfileComponent;
import org.jhove2.core.Format;
import org.jhove2.core.JHOVE2;
import org.jhove2.module.format.message.BaseFormatIncompatability;
import org.jhove2.module.format.message.BaseFormatNotSpecified;
import org.jhove2.module.format.utf8.UTF8;
import org.jhove2.module.format.utf8.ascii.message.NonBasicLatin;
import org.jhove2.unicode.CodeBlock;

/** An ASCII character stream profile of UTF-8.
 * 
 * @author mstrong, slabrams
 */
public class ASCII
	extends AbstractFormatProfileComponent
{
	/** ASCII profile version identifier. */
	public static final String VERSION = "1.0.0";

	/** ASCII profile release date. */
	public static final String DATE = "2009-05-28";
	
	/** ASCII profile development stage. */
	public static final Stage STAGE = Stage.Development;
	
	/** Basic Latin code block name. TODO: make this configurable. */
	public static final String BASIC_LATIN = "Basic Latin";
	
	/** Validation status. */
	protected Validity isValid;
	
	/** Instantiate a new <code>ASCII</code> profile.
	 * @param format ASCII format
	 */
	public ASCII(Format format) {
		super(VERSION, DATE, STAGE, format);
		
		reset();
	}
	
	/** Reset ASCII properties.
	 */
	@Override
	public void reset() {
		super.reset();
		
		this.isValid = Validity.Unknown;
	}

	/** Validate the UTF-8 character.
	 * @param jhove2 JHOVE2 framework
	 */
	@Override
	public Validity validate(JHOVE2 jhove2) {
		reset();
		
		if (this.base != null) {
			if (this.base instanceof UTF8) {
				UTF8 utf8 = (UTF8) this.base;
				this.isValid = Validity.False;
				Set<CodeBlock> codeBlocks = utf8.getCodeBlocks();
				if (codeBlocks.size() == 1) {
					Iterator<CodeBlock> iter = codeBlocks.iterator();
					CodeBlock codeBlock = iter.next();
					String name = codeBlock.getName();
					if (name.equals(BASIC_LATIN)) {
						this.isValid = Validity.True;
					}
				}
				if (this.isValid == Validity.False) {
					@ReportableMessage
					NonBasicLatin msg = new NonBasicLatin();
					addMessage(msg);
				}
			}
			else {
				@ReportableMessage
				BaseFormatIncompatability msg =
					new BaseFormatIncompatability(this.base.getReportableName());
				addMessage(msg);
			}
		}
		else {
			@ReportableMessage
			BaseFormatNotSpecified msg =
				new BaseFormatNotSpecified();
			addMessage(msg);
		}
		
		return this.isValid;
	}
	
	/** Get validation status.
	 * @return True if a valid ASCII character stream
	 * @see org.jhove2.core.Validatable#isValid()
	 */
	@Override
	public Validity isValid() {
		return this.isValid;
	}
}
