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
import java.util.ArrayList;
import java.util.List;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.Message;
import org.jhove2.core.Reportable;
import org.jhove2.core.Message.Context;
import org.jhove2.core.Message.Severity;
import org.jhove2.core.io.Input;
import org.jhove2.module.format.Validator.Validity;
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
	implements Reportable
{
	/** Byte Order Mark (BOM). */
	public static final int BOM = 0xFEFF;
	
	/** Encoded byte places. */
	public static final String [] PLACE = {"2nd", "3rd", "4th"};
	
	/** Marker that the character code point is uninitialized or unknown. */
	public static final int UNINITIALIZED = -1;
	
	/** C0 control.  Null if character is not a C0 control. */
	protected C0Control c0control;

	/** C1 control.  Null if character is not a C1 control. */
	protected C1Control c1control;
	
	/** Unicode code block. */
	protected CodeBlock codeBlock;

	/** Character code point. */
	protected int codePoint;
		
	/** Code point out of range message. */
	protected Message codePointOutOfRangeMessage;

	/** Invalid byte value message. */
	protected List<Message> invalidByteValueMessages;
	
	/** Character Byte Order Mark (BOM) status. */
	protected boolean isBOM;
	
	/** Character C0 control status. */
	protected boolean isC0Control;
	
	/** Character C1 control status. */
	protected boolean isC1Control;
	
	/** Non-character status. */
	protected boolean isNonCharacter;
	
	/** Character validation status. */
	protected Validity isValid;
	
	/** Character byte offset. */
	protected long offset;
	
	/** Character encoded size, in bytes. */
	protected int size;

	/** Instantiate a new <code>UTF8Character</code>
	 */
	public UTF8Character() {
		super();
		
		this.codePoint                = UNINITIALIZED;		
		this.invalidByteValueMessages = new ArrayList<Message>();
		this.isBOM                    = false;
		this.isC0Control              = false;
		this.isC1Control              = false;
		this.isNonCharacter           = false;
		this.isValid                  = Validity.Undetermined;
		this.size                     = 0;
	}

	/** Parse a source unit input.  Implicitly set the start and end elapsed
	 * time.
	 * @param jhove2 JHOVE2 framework
	 * @param input Input
	 * @return Number of bytes consumed
	 * @throws EOFException    If End-of-File is reached reading the source unit
	 * @throws IOException     If an I/O exception is raised reading the source
	 *                         unit
	 * @throws JHOVE2Exception
	 */
	public long parse(JHOVE2 jhove2, Input input)
		throws EOFException, IOException, JHOVE2Exception
	{
		this.isValid = Validity.True;
		
		/* Read the first byte. */
		long consumed = 0L;
		int[] b = new int[4];
		b[0] = input.readUnsignedByte();
		if (b[0] == Input.EOF) {
			this.isValid = Validity.False;
			throw new EOFException();
		}
		consumed++;
		
		/* Determine size of the character [Unicode, D92]. */
		if      (0x00 <= b[0] && b[0] <= 0x7F) {
			this.size = 1;
		}
		else if (0xC2 <= b[0] && b[0] <= 0xDF) {
			this.size = 2;
		}
		else if  (0xe0 <= b[0] && b[0] <= 0xEF) {
			this.size = 3;
		}
		else if  (0xF0 <= b[0] && b[0] <= 0xF4) {
			this.size = 4;
		}
		else if ((0x80 <= b[0] && b[0] <= 0xC1) ||
		         (0xF5 <= b[0] && b[0] <= 0xFF)) {
			this.isValid = Validity.False;
			this.invalidByteValueMessages.add(new Message(Severity.ERROR,
					Context.OBJECT, "Invalid first byte value at offset " +
					input.getPosition() + ": " + b[0]));
		}
		
		/* Read the remaining bytes. */
		for (int i=1; i<this.size; i++) {
			b[i] = input.readUnsignedByte();
			if (b[i] == Input.EOF) {
				this.isValid = Validity.False;
				throw new EOFException();
			}
			consumed++;

			if ((i == 2 && ((this.size == 3 &&
					         ((b[0] == 0xE0 && (0x0A > b[i] || b[i] > 0xBF)) ||
					          (b[0] == 0xED && (0x80 > b[i] || b[i] > 0x9F)))) ||
					        (this.size == 4 &&
					         ((b[0] == 0xF0 && (0x90 > b[i] || b[i] > 0xBF)) ||
					          (b[0] == 0xF4 && (0x80 > b[i] || b[i] > 0x8F)))))) ||
				(0x80 > b[i] || b[i] > 0xBF)) {
				this.isValid = Validity.False;
				this.invalidByteValueMessages.add(new Message(Severity.ERROR,
						Context.OBJECT, "Invalid " + PLACE[i-1] + " byte " +
						"value at offset " + input.getPosition() + ": " +
						b[i]));
			}
		}
		
		/* Determine the character's code point. */
		if      (this.size == 1) {
			this.codePoint =   b[0];
		}
		else if (this.size == 2) {
			this.codePoint = ((b[0] & 0x1f) <<  6) + (  b[1] & 0x3f);
		}
		else if (this.size == 3) {
			this.codePoint = ((b[0] & 0x0f) << 12) + (( b[1] & 0x3f) <<  6) +
					                                  ( b[2] & 0x3f);
		}
		else if (this.size == 4) {
			this.codePoint = ((b[0] & 0x07) << 18) + (( b[1] & 0x3f) << 12) +
				 	                                  ((b[2] & 0x3f) <<  6) +
					                                   (b[3] & 0x3f);
		}

		/* Set character properties. */
		if (this.codePoint == BOM) {
			this.isBOM = true;
		}	
		
		this.codeBlock = CodeBlock.getBlock(this.codePoint);
		this.c0control = C0Control.getControl(this.codePoint);
		this.c1control = C1Control.getControl(this.codePoint);

		/* Check for code point outside of valid range [Unicode, D76]. */
		if (this.codePoint < 0x00 ||
			(0xD7FF < this.codePoint && this.codePoint < 0xE000) ||
			this.codePoint > 0x10FFFF) {
			this.isValid = Validity.False;
			this.codePointOutOfRangeMessage =
				new Message(Severity.ERROR, Context.OBJECT,
						    "Code point out of range at offset " +
						    (input.getPosition() - consumed) +": " +
						    this.codePoint);
		}
		
		/* Check if code point is a non-character [Unicode, D14] */
		if ((this.codePoint >= 0xFDD0  && this.codePoint <= 0xFDEF) ||
		     this.codePoint == 0x0FFFE || this.codePoint == 0x0FFFF ||
			 this.codePoint == 0x1FFFE || this.codePoint == 0x1FFFF ||
			 this.codePoint == 0x2FFFE || this.codePoint == 0x2FFFF ||
			 this.codePoint == 0x3FFFE || this.codePoint == 0x3FFFF ||
			 this.codePoint == 0x4FFFE || this.codePoint == 0x4FFFF ||
			 this.codePoint == 0x5FFFE || this.codePoint == 0x5FFFF ||
			 this.codePoint == 0x6FFFE || this.codePoint == 0x6FFFF ||
			 this.codePoint == 0x7FFFE || this.codePoint == 0x7FFFF ||
			 this.codePoint == 0x8FFFE || this.codePoint == 0x8FFFF ||
			 this.codePoint == 0x9FFFE || this.codePoint == 0x9FFFF ||
			 this.codePoint ==0x10FFFE || this.codePoint ==0x10FFFF) {
			this.isNonCharacter = true;
		}
		
		return consumed;
	}

	/** Validate a source unit.
	 * @param jhove2 JHOVE2 framework
	 * @return Source unit validity
	 */
	public Validity validate(JHOVE2 jhove2) {
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
	 * @param codePoint     Current character code point
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
	
	/** Get code point out of range message.
	 * @return Code point our of range message
	 */
	@ReportableProperty(order=12, value="Code point out of range message.")
	public Message getCodePointOutOfRange() {
		return this.codePointOutOfRangeMessage;
	}
	
	/** Get invalid byte value message.
	 * @return Invalid byte value message
	 */
	@ReportableProperty(order=11, value="Invalid byte value message.")
	public List<Message> getInvalidByteValues() {
		return this.invalidByteValueMessages;
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
	 */
	public Validity isValid() {
		return this.isValid;
	}
}
