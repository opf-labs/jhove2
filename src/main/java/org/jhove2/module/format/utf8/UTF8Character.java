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

package org.jhove2.module.format.utf8;

import java.io.EOFException;
import java.io.IOException;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.AbstractModel;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.Parsable;
import org.jhove2.core.Validatable;
import org.jhove2.core.source.Source;
import org.jhove2.module.format.unicode.C0Control;
import org.jhove2.module.format.unicode.C1Control;
import org.jhove2.module.format.unicode.CodeBlock;
import org.jhove2.module.format.unicode.Unicode;
import org.jhove2.module.format.unicode.Unicode.EOL;

/** JHOVE2 UTF-8 character modeling class.
 * 
 * @author mstrong, slabrams
 */
public class UTF8Character
	extends AbstractModel
	implements Parsable, Validatable
{
	/** C0 control. */
	protected C0Control c0control;

	/** C1 control. */
	protected C1Control c1control;
	
	/** Unicode code block. */
	protected CodeBlock codeBlock;
	
	/** Byte Order Mark (BOM). */
	public static final int BOM = 0xFEFF;
	
	/** Marker that the character code point is uninitialized or unknown. */
	public static final int UNINITIALIZED = -1;
	
	/** Character code point. */
	protected int codePoint;
	
	/** Byte Order Mark (BOM) status. */
	protected boolean isBOM;
	
	/** C0 control status. */
	protected boolean isC0Control;
	
	/** C1 control status. */
	protected boolean isC1Control;
	
	/** Non-character status. */
	protected boolean isNonCharacter;
	
	/** Validation status. */
	protected Validity isValid;
	
	/** Character encoded size, in bytes. */
	protected int size;

	/** Instantiate a new <code>UTF8Character</code>
	 */
	public UTF8Character() {
		super();
	}

	/** Parse a source unit.  Implicitly set the start and end elapsed time.
	 * @param jhove2 JHOVE2 framework
	 * @param source Source unit
	 * @return Number of bytes consumed
	 * @throws EOFException    If End-of-File is reached reading the source unit
	 * @throws IOException     If an I/O exception is raised reading the source
	 *                         unit
	 * @throws JHOVE2Exception
	 * @see org.jhove2.core.Parsable#parse(org.jhove2.core.JHOVE2, org.jhove2.core.source.Source)
	 */
	@Override
	public long parse(JHOVE2 jhove2, Source source) throws EOFException,
			IOException, JHOVE2Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	/** Validate a source unit.  Implicitly set the starting and ending elapsed
	 * time.
	 * @param jhove2 JHOVE2 framework
	 * @see org.jhove2.core.Validatable#validate(org.jhove2.core.JHOVE2)
	 */
	@Override
	public Validity validate(JHOVE2 jhove2) {
		// TODO Auto-generated method stub
		return this.isValid;
	}
	
	/** Get code point.
	 * @return Code point
	 */
	@ReportableProperty(order=1, value="Code point.")
	public int getCodePoint() {
		return this.codePoint;
	}

	/** Determine line ending characters.
	 * @param prevCodePoint Previous character code point
	 * @param currChar Current character code point
	 * @return The line ending characters (CR, LF, or CRLF) or null if not at
	 *         a line ending
	 */
	public static synchronized EOL getEOL(int prevCodePoint, int codePoint)
	{
		EOL eol = null;
		
		if (codePoint == Unicode.LF) {
			if (prevCodePoint == Unicode.CR) {
				eol = EOL.CRLF;
			}
			else {
				eol = EOL.LF;
			}
		}
		else if (prevCodePoint == Unicode.CR){
			eol = EOL.CR;
		}
		
		return eol;
	}
	
	/** Get encoded size, in bytes.
	 * @return Encoded size, in bytes
	 */
	@ReportableProperty(order=2, value="Encoded size, in bytes")
	public int getSize() {
		return this.size;
	}
	
	/** Get Unicode code block.
	 * @return Unicode code block, or null if not in any code block
	 */
	@ReportableProperty(order=3, value="Code block")
	public CodeBlock getCodeBlock() {
		return this.codeBlock;
	}
	
	/** Get C0 control.
	 * @return C0 control or null if not a C0 control
	 */
	@ReportableProperty(order=4, value="C0 Control")
	public C0Control getC0Control() {
		return this.c0control;
	}

	/** Get C1 control.
	 * @return C1 control or null if not a c1 control
	 */
	@ReportableProperty(order=5, value="C1 Control")
	public C1Control getC1Control() {
		return this.c1control;
	}

	/** Get Byte Order Mark (BOM) status.
	 * @return True if a BOM
	 */
	@ReportableProperty(order=6, value="Byte Order Mark (BOM) status: true if a BOM")
	public boolean isByteOrderMark() {
		return this.isBOM;
	}
	
	/** Get C0 control status.
	 * @return True if C0 control
	 */
	@ReportableProperty(order=4, value="C0 control status: true if a C0 control")
	public boolean isC0Control() {
		return this.isC0Control;
	}
	
	/** Get C1 control status.
	 * @return True if C1 control
	 */
	@ReportableProperty(order=5, value="C1 control status: true if a C1 control")
	public boolean isC1Control() {
		return this.isC1Control;
	}
	
	/** Get non-character status.
	 * @return True if not a character
	 */
	@ReportableProperty(order=7, value="Non-character status: true if not a character")
	public boolean isNonCharacter() {
		return this.isNonCharacter;
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
