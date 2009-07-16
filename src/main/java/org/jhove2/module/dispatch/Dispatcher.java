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

package org.jhove2.module.dispatch;

import java.io.EOFException;
import java.io.IOException;

import org.jhove2.core.I8R;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.source.Source;
import org.jhove2.module.Module;

/** Interface for JHOVE2 dispatcher modules.  A dispatcher module instantiates
 * and invokes the module associated with an identifier.
 * 
 * @author mstrong, slabrams
 */
public interface Dispatcher
	extends Module
{
	/** Dispatch a source unit to the module associated with an identifier.
	 * @param jhove2     JHOVE2 framework
	 * @param source     Source unit
	 * @param identifier Module identifier
	 * @return Module
	 * @throws EOFException    End-of-file encountered parsing the source unit
	 * @throws IOException     I/O exception encountered parsing the source unit
	 * @throws JHOVE2Exception
	 */
	public Module dispatch(JHOVE2 jhove2, Source source, I8R identifier)
		throws EOFException, IOException, JHOVE2Exception;

	/** Dispatch a source unit to a module.
	 * @param jhove2 JHOVE2 framework
	 * @param source Source unit
	 * @param module Module 
	 * @throws EOFException    End-of-file encountered parsing the source unit
	 * @throws IOException     I/O exception encountered parsing the source unit
	 * @throws JHOVE2Exception
	 */
	public void dispatch(JHOVE2 jhove2, Source source, Module module)
		throws EOFException, IOException, JHOVE2Exception;
}
