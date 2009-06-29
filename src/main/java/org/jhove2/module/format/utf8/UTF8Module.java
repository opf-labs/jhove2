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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.AbstractFormatModule;
import org.jhove2.core.Format;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.Message;
import org.jhove2.core.Parsable;
import org.jhove2.core.Message.Context;
import org.jhove2.core.Message.Severity;
import org.jhove2.core.io.Input;
import org.jhove2.core.source.FileSource;
import org.jhove2.core.source.Source;
import org.jhove2.core.source.ZipFileSource;
import org.jhove2.module.format.unicode.C0Control;
import org.jhove2.module.format.unicode.C1Control;
import org.jhove2.module.format.unicode.CodeBlock;
import org.jhove2.module.format.unicode.Unicode;
import org.jhove2.module.format.unicode.Unicode.EOL;

/** JHOVE2 UTF-8 module.
 * 
 * @author mstrong, slabrams
 */
public class UTF8Module
	extends AbstractFormatModule
	implements Parsable
{
	/** UTF-8 module version identifier. */
	public static final String VERSION = "1.0.0";

	/** UTF-8 module release date. */
	public static final String RELEASE = "2009-06-23";
	
	/** UTF-8 module rights statement. */
	public static final String RIGHTS =
		"Copyright 2009 by The Regents of the University of California. " +
		"Available under the terms of the BSD license.";
	
	/** Byte Order Mark (BOM) message. */
	protected Message bomMessage;
	
	/** Non-line ending C0 control characters. CR and LF are therefore
	 * <em>not</em> found in this set. */
	protected Set<C0Control> c0Characters;
	
	/** C1 control characters. */
	protected Set<C1Control> c1Characters;
	
	/** Unicode code blocks. */
	protected Set<CodeBlock> codeBlocks;
	
	/** End-of-Line (EOL) characters. */
	protected Set<EOL> eolCharacters;
	
	/** Fail fast message. */
	protected Message failFastMessage;
	
	/** Invalid UTF-8 characters. */
	protected List<UTF8Character> invalidCharacters;

	/** Number of characters. */
	protected long numCharacters;
	
	/** Number of lines.  A line is terminated by a CR, CRLF, LF, or EOF. */
	protected long numLines;
	
	/** Number of non-characters. */
	protected long numNonCharacters;

	/** Instantiate a new <code>UTF8Module</code>.
	 * @param format UTF-8 format
	 */
	public UTF8Module(Format format) {
		super(VERSION, RELEASE, RIGHTS, format);
		
		this.c0Characters  = new TreeSet<C0Control>();
		this.c1Characters  = new TreeSet<C1Control>();
		this.codeBlocks    = new TreeSet<CodeBlock>();
		this.eolCharacters = new TreeSet<EOL>();
		this.numCharacters    = 0L;
		this.numLines         = 0L;
		this.numNonCharacters = 0L;
		
		this.invalidCharacters = new ArrayList<UTF8Character>();
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
		setStartTime();
		
		long consumed = 0L;
		this.isValid  = Validity.Undetermined;
		int numErrors = 0;
		Input input = null;
		try {
			input = source.getInput(jhove2.getBufferSize(),
					                jhove2.getBufferType());
			long start    = 0L;
			long end      = 0L;
			if (source instanceof FileSource) {
				end = ((FileSource) source).getSize();
			}
			else if (source instanceof ZipFileSource) {
				end = ((ZipFileSource) source).getSize();
			};
			input.setPosition(start);

			EOL  eol = null;
			long position = start;
			int  prevCodePoint = UTF8Character.UNINITIALIZED;
			this.isValid = Validity.True;
			while (end == 0 || position < end) {
				UTF8Character ch = new UTF8Character();
				long n = 0L;
				try {
					n = ch.parse(jhove2, input);
				} catch (EOFException e) {
					this.isValid = Validity.False;

					/* TODO: EndOfFile. */
					break;
				}
				consumed += n;
				int codePoint = ch.getCodePoint();
				this.numCharacters++;
			
				Validity isValid  = ch.isValid();
				if (isValid == Validity.False) {
					this.isValid = isValid;
					if (jhove2.failFast(++numErrors)) {
						this.failFastMessage =
							new Message(Severity.INFO, Context.PROCESS,
								    "Fail fast limit exceeded; additional " +
								    "errors may exist but will not be " +
								    "reported");
						break;
					}
					this.invalidCharacters.add(ch);
				}
			
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
					this.bomMessage =
						new Message(Severity.INFO, Context.OBJECT,
							    "Byte Order Mark (BOM) at byte offset: " +
							    position);
				}
				if (ch.isNonCharacter()) {
					this.numNonCharacters++;
				}
				if (ch.isValid() == Validity.False) {
					this.isValid = Validity.False;
				}
			
				prevCodePoint = codePoint;
				position += n;
			}
			eol = UTF8Character.getEOL(prevCodePoint, UTF8Character.UNINITIALIZED);
			if (eol != null) {
				this.numLines++;
				this.eolCharacters.add(eol);
			}
			else if (prevCodePoint != Unicode.LF) {
				this.numLines++;
			}
		} finally {
			if (input != null) {
				input.close();
			}
		}
		setEndTime();
		
		return consumed;
	}

	/** Validate a UTF-8 source unit.  Implicitly set the starting and ending
	 * lapsed time.
	 * @param jhove2 JHOVE2 framework
	 * @param source Source unit
	 * @return Validation status
	 * @see org.jhove2.core.Validatable#validate(org.jhove2.core.JHOVE2)
	 */
	@Override
	public Validity validate(JHOVE2 jhove2, Source source) {
		return this.isValid;
	}
	
	/** Get Byte Order Mark (BOM) message.
	 * @return Byte Order Mark (BOM) message
	 */
	@ReportableProperty(order=11, value="Byte Order Mark (BOM) message.")
	public Message getByteOrderMark() {
		return this.bomMessage;
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
	
	/** Get fail fast message.
	 * @return Fail fast message
	 */
	@ReportableProperty(order=13, value="Fail fast message.")
	public Message getFailFast() {
		return this.failFastMessage;
	}
	
	/** Get invalid UTF-8 characters.
	 * @return Invalid UTF-8 characters
	 */
	@ReportableProperty(order=12, value="Invalid UTF-8 characters.")
	public List<UTF8Character> getInvalidCharacters() {
		return this.invalidCharacters;
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
}
