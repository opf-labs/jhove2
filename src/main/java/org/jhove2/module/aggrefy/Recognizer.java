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

package org.jhove2.module.aggrefy;

import java.io.IOException;
import java.util.Set;

import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.source.ClumpSource;
import org.jhove2.core.source.Source;
import org.jhove2.module.Module;

/**
 * Interface for JHOVE2 aggregate recognizer modules, capable of
 * detecting instances of Clump formats
 * 
 * @author smmorrissey
 */
public interface Recognizer
	extends Module
{
	/**
	 * Detect presumptive instances of a clump format in a source unit, and identify.
	 * 
	 * @param jhove2
	 *            JHOVE2 framework
	 * @param source
	 *            Source unit
	 * @return Presumptively identified presumptiveFormatIds
	 * @throws IOException
	 *             I/O exception encountered identifying the source unit
	 * @throws JHOVE2Exception
	 */
	public Set<ClumpSource> recognize(JHOVE2 jhove2, Source source)
			throws IOException, JHOVE2Exception;
	
	/**
	 * @return the parentAggrefierId
	 */
	public Long getParentAggrefierId();

	/**
	 * @param parentAggrefierId the parentAggrefierId to set
	 */
	public void setParentAggrefierId(Long parentAggrefierId);
}