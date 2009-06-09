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
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.jhove2.annotation.ReportableMessage;
import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.AbstractFormatComponent;
import org.jhove2.core.Format;
import org.jhove2.core.FormatProfileComponent;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.Parsable;
import org.jhove2.core.Validatable;
import org.jhove2.core.io.Input;
import org.jhove2.core.message.EndOfFile;
import org.jhove2.core.message.FailFastLimitExceeded;
import org.jhove2.module.format.utf8.message.ByteOrderMark;
import org.jhove2.unicode.C0Control;
import org.jhove2.unicode.C1Control;
import org.jhove2.unicode.CodeBlock;
import org.jhove2.unicode.Unicode;
import org.jhove2.unicode.Unicode.EOL;

/** A UTF-8 character stream.
 * 
 * @author mstrong, slabrams
 */
public class UTF8
	extends AbstractFormatComponent
	implements Parsable, Validatable
{
	/** UTF-8 component version identifier. */
	public static final String VERSION = "1.0.0";

	/** UTF-8 component release date. */
	public static final String DATE = "2009-05-28";
	
	/** UTF-8 component development stage. */
	public static final Stage STAGE = Stage.Development;
	
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
	 * @param format UTF-8 format
	 */
	public UTF8(Format format) {
		super(VERSION, DATE, STAGE, format);
		
		reset();
	}
	
	/** Reset UTF-8 properties.
	 * @see org.jhove2.core.Reporter#reset();
	 */
	@Override
	public void reset() {
		super.reset();
		
		this.c0Characters  = new TreeSet<C0Control>();
		this.c1Characters  = new TreeSet<C1Control>();
		this.codeBlocks    = new TreeSet<CodeBlock>();
		this.eolCharacters = new TreeSet<EOL>();
		this.isValid       = Validity.Unknown;
		this.numCharacters    = 0L;
		this.numLines         = 0L;
		this.numNonCharacters = 0L;
	}
	
	/** Parse an entire UTF-8 character stream.
	 * @param jhove2 JHOVE2 framework
	 * @param input  Input
	 * @return Number of bytes consumed
	 * @see org.jhove2.core.Parsable#parse(Input)
	 */
	@Override
	public long parse(JHOVE2 jhove2, Input input)
		throws IOException
	{
		File file = input.getFile();
		
		return parse(jhove2, input, 0, file.length());
	}
	
	/** Parse a portion of a UTF-8 character stream.
	 * @param jhove2 JHOVE2 framework
	 * @param input  Input
	 * @param start  Starting byte offset
	 * @param end    Ending byte offset
	 * @return Number of bytes consumed
	 * @throws IOException 
	 */
	public long parse(JHOVE2 jhove2, Input input, long start, long end)
		throws IOException
	{
		reset();
		this.isValid  = Validity.True;
		int numErrors = 0;

		UTF8Character ch = new UTF8Character();
		input.setPosition(start);

		long consumed = 0L;
		EOL  eol = null;
		long position = start;
		int  prevCodePoint = UTF8Character.UNINITIALIZED;
		while (position < end) {
			long n = 0L;
			try {
				n = ch.parse(jhove2, input);
			} catch (EOFException e) {
				this.isValid = Validity.False;
				@ReportableMessage
				EndOfFile msg = new EndOfFile(position);
				addMessage(msg);
				break;
			}
			int codePoint = ch.getCodePoint();
			if (ch.getNumMessages() > 0) {
				addMessages(ch.getMessages());
				numErrors += ch.getNumErrorMessages();
				if (failFast(jhove2, numErrors)) {
					@ReportableMessage
					FailFastLimitExceeded msg =
						new FailFastLimitExceeded(jhove2.getFailFastLimit());
					addMessage(msg);
					break;
				}
			}
			this.numCharacters++;
			
			/* Determine character properties. */
			eol = UTF8Character.getEOL(prevCodePoint, codePoint);
			if (eol != null) {
				this.numLines++;
				this.eolCharacters.add(eol);
			}
			CodeBlock codeBlock = ch.getCodeBlock();
			if (codeBlock != null) {
				this.codeBlocks.add(codeBlock);
			}

			C0Control c0 = ch.getC0Control();
			if (c0 != null && !c0.getMnemonic().equals("CR") &&
					          !c0.getMnemonic().equals("LF")) {
				this.c0Characters.add(c0);
			}
			C1Control c1 = ch.getC1Control();
			if (c1 != null) {
				this.c1Characters.add(c1);
			}
			if (position == start && ch.isByteOrderMark()) {
				@ReportableMessage
				ByteOrderMark msg = new ByteOrderMark(position);
				addMessage(msg);
			}
			if (ch.isNonCharacter()) {
				this.numNonCharacters++;
			}
			if (ch.isValid() == Validity.False) {
				this.isValid = Validity.False;
			}
			
			prevCodePoint = codePoint;
			position += n;
			consumed += n;
		}
		eol = UTF8Character.getEOL(prevCodePoint, UTF8Character.UNINITIALIZED);
		if (eol != null) {
			/* TODO: this.eolCharacters.add(eol); */
			this.numLines++;
			this.eolCharacters.add(eol);
		}
		else if (prevCodePoint != Unicode.LF) {
			this.numLines++;
		}
		
		/* Invoke registered profiles. */
		Iterator<FormatProfileComponent> iter = this.profiles.iterator();
		while (iter.hasNext()) {
			FormatProfileComponent profile = iter.next();
			profile.setInitialTime();
			Validity v = profile.validate(jhove2);
			profile.setFinalTime();
		}
		
		return consumed;
	}

	/** Validate the UTF-8 character stream.
	 * @param jhove2 JHOVE2 framework
	 */
	@Override
	public Validity validate(JHOVE2 jhove2) {
		return this.isValid;
	}

	/** Get non-line ending C0 control characters.  Therefore CR and LF will
	 * <em>not</em> be reported in this set.
	 * @return Set of non-line ending C0 control characters
	 */
	@ReportableProperty(value=5, desc="Set of non-line-ending C0 control " +
			"characters.  Thus, CR and LF are not included in this set.")
	public Set<C0Control> getC0Characters() {
		return this.c0Characters;
	}
	
	/** Get C1 control characters.
	 * @return Set of C1 control characters
	 */
	@ReportableProperty(value=6, desc="Set of C1 control characters.")
	public Set<C1Control> getC1Characters() {
		return this.c1Characters;
	}
	
	/** Get code blocks.
	 * @return Set of code blocks
	 */
	@ReportableProperty(value=4, desc="Set of Unicode code blocks.")
	public Set<CodeBlock> getCodeBlocks() {
		return this.codeBlocks;
	}
	
	/** Get End-of-Line (EOL) characters.
	 * @return Set of EOL characters
	 */
	@ReportableProperty(value=3, desc="Set of End-of-Line (EOL) characters.")
	public Set<EOL> getEOLCharacters() {
		return this.eolCharacters;
	}
	
	/** Get number of characters.
	 * @return Number of characters
	 */
	@ReportableProperty(value=1, desc="Number of UTF-8 characters.")
	public long getNumCharacters() {
		return this.numCharacters;
	}
	
	/** Get number of lines.  A line is terminated by a CR, CRLF, LF, or EOF
	 * @return Number of lines
	 */
	@ReportableProperty(value=2, desc="Number of lines.  A line is " +
			"terminated by a CR, CRLF, LF, or End-of-File (EOF).")
	public long getNumLines() {
		return this.numLines;
	}
	
	/** Get number of non-characters.
	 * @return Number of non-characters
	 */
	@ReportableProperty(value=7, desc="Number of UTF-8 non-characters.")
	public long getNumNonCharacters() {
		return this.numNonCharacters;
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
