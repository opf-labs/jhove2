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
import java.util.Set;

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
import org.jhove2.module.format.unicode.Unicode.EOL;

/** UTF-8 character stream modeling class.
 * 
 * @author mstrong, slabrams
 */
public class UTF8
	extends AbstractModel
	implements Parsable, Validatable
{	
	/** Non-line ending C0 control characters. CR and LF are therefore
	 * <em>not</em> found in this set. */
	protected Set<C0Control> c0Characters;
	
	/** C1 control characters. */
	protected Set<C1Control> c1Characters;
	
	/** Unicode code blocks. */
	protected Set<CodeBlock> codeBlocks;
	
	/** End-of-Line (EOL) characters. */
	protected Set<EOL> eolCharacters;
	
	/** Validity status. */
	protected Validity isValid;

	/** Number of characters. */
	protected long numCharacters;
	
	/** Number of lines.  A line is terminated by a CR, CRLF, LF, or EOF. */
	protected long numLines;
	
	/** Number of non-characters. */
	protected long numNonCharacters;

	/** Instantiate a new <code>UTF8</code>.
	 */
	public UTF8() {
		super();
		
		this.isValid = Validity.Undetermined;
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
		return 0;
	}

	/** Validate a source unit.  Implicitly set the starting and ending elapsed
	 * time.
	 * @param jhove2 JHOVE2 framework
	 * @return Validation status
	 * @see org.jhove2.core.Validatable#validate(org.jhove2.core.JHOVE2)
	 */
	@Override
	public Validity validate(JHOVE2 jhove2) {
		return this.isValid;
	}

	/** Get non-line ending C0 control characters.  Therefore CR and LF will
	 * <em>not</em> be reported in this set.
	 * @return Set of non-line ending C0 control characters
	 */
	@ReportableProperty(order=5, value="Set of non-line-ending C0 control " +
			"characters.  Thus, CR and LF are not included in this set.")
	public Set<C0Control> getC0Characters() {
		return this.c0Characters;
	}
	
	/** Get C1 control characters.
	 * @return Set of C1 control characters
	 */
	@ReportableProperty(order=6, value="Set of C1 control characters.")
	public Set<C1Control> getC1Characters() {
		return this.c1Characters;
	}
	
	/** Get code blocks.
	 * @return Set of code blocks
	 */
	@ReportableProperty(order=4, value="Set of Unicode code blocks.")
	public Set<CodeBlock> getCodeBlocks() {
		return this.codeBlocks;
	}
	
	/** Get End-of-Line (EOL) characters.
	 * @return Set of EOL characters
	 */
	@ReportableProperty(order=3, value="Set of End-of-Line (EOL) characters.")
	public Set<EOL> getEOLCharacters() {
		return this.eolCharacters;
	}
	
	/** Get number of characters.
	 * @return Number of characters
	 */
	@ReportableProperty(order=1, value="Number of UTF-8 characters.")
	public long getNumCharacters() {
		return this.numCharacters;
	}
	
	/** Get number of lines.  A line is terminated by a CR, CRLF, LF, or EOF
	 * @return Number of lines
	 */
	@ReportableProperty(order=2, value="Number of lines.  A line is " +
			"terminated by a CR, CRLF, LF, or End-of-File (EOF).")
	public long getNumLines() {
		return this.numLines;
	}
	
	/** Get number of non-characters.
	 * @return Number of non-characters
	 */
	@ReportableProperty(order=7, value="Number of UTF-8 non-characters.")
	public long getNumNonCharacters() {
		return this.numNonCharacters;
	}
	
	/** Get validation status.
	 * @return Validation status: true if valid
	 * @see org.jhove2.core.Validatable#isValid()
	 */
	@Override
	public Validity isValid() {
		return this.isValid;
	}
}
