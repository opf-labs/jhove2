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

package org.jhove2.module.format.utf8.message;

import org.jhove2.annotation.ReportableMessage;
import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.message.AbstractMessage;

/** UTF-8 invalid byte value message [Unicode, D92].
 * 
 * @author mstrong, slabrams
 */
@ReportableMessage(desc="Invalid byte value.",
		ref="[Unicode, D92]")
public class InvalidByteValue
	extends AbstractMessage
{
	/** Byte offset. */
	protected long offset;
	
	/** Ordinal byte position: 1 through 4. */
	protected int position;
	
	/** Invalid byte value. */
	protected int value;
	
	/**
	 * @param message
	 */
	public InvalidByteValue(long offset, int position, int value) {
		super(Context.Object, Severity.Error, "Invalid byte value");
		
		this.offset   = offset;
		this.position = position;
		this.value    = value;
	}

	/** Get byte offset.
	 * @return Byte offset
	 */
	@ReportableProperty(value=1, desc="Byte offset.")
	public long getOffset() {
		return this.offset;
	}
	
	/** Get ordinal byte position: 1 through 4.
	 * @return Ordinal byte position
	 */
	@ReportableProperty(value=2, desc="Ordinal byte position: 1 through 4.")
	public int getPosition() {
		return this.position;
	}
	
	/** Get invalid byte value.
	 * @return Invalid byte value
	 */
	@ReportableProperty(value=3, desc="Invalid byte value.",
			ref ="[Unicode, D92]")
	public int getValue() {
		return this.value;
	}
	
	/** Get {@link java.lang.String} representation of the message.
	 * @return String representation of the message
	 * @see org.jhove2.core.message.Message#toString()
	 */
	@Override
	public String toString() {
		return super.toString() + ": Offset=" + this.offset + "; Byte position=" +
		       this.position + "; Invalid value=" + this.value;
	}
}
