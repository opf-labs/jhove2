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

package org.jhove2.core.source;

import java.io.File;

import org.jhove2.annotation.ReportableProperty;


/** A JHOVE2 bytestream source unit.  A bytestream is a proper subset of a file.
 * 
 * @author mstrong, slabrams
 */
public class BytestreamSource
	extends FileSource
{
	/** Ending byte offset within the file. */
	protected long end;
	
	/** Starting byte offset within the file. */
	protected long start;
	
	/** Instantiate a new <code>BytestreamSource</code>.
	 * @param file  Java {@link java.io.File} underlying the bytestream
	 * @param start Starting byte offset within the file
	 * @param end   Ending byte offset within the file
	 */
	public BytestreamSource(File file, long start, long end) {
		super(file);
		
		this.start = start;
		this.end   = end;
	}

	/** Get ending byte offset within the file.
	 * @return Ending byte offset
	 */
	@ReportableProperty(value=2, desc="Ending byte offset within the file")
	public long getEndingOffset() {
		return this.end;
	}

	/** Get starting byte offset within the file.
	 * @return Starting byte offset
	 */
	@ReportableProperty(value=1, desc="Starting byte offset within the file")
	public long getStartingOffset() {
		return this.start;
	}
}
