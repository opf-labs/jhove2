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

import java.util.ArrayList;
import java.util.List;

import org.jhove2.core.Processible;
import org.jhove2.core.io.Input;

/** An abstract JHOVE2 source unit.  A source unit is a formatted object that
 * can be characterized, which may be a file, a subset of a file, or a group
 * of files.
 * 
 * @author mstrong, slabrams
 */
public abstract class AbstractSource
	implements Source
{
	/** Source unit input. */
	protected Input input;
	
	/** Modules that processed the source unit. */
	protected List<Processible> modules;
	
	/** Instantiate a new <code>AbstractSource</code>.
	 */
	public AbstractSource() {
		this.modules = new ArrayList<Processible>();
	}
	
	/** Add a module that processed the source unit.
	 * @param module Module that processed the source unit
	 * @see org.jhove2.core.source.Source#addModule(org.jhove2.core.Processible)
	 */
	@Override
	public void addModule(Processible module) {
		this.modules.add(module);
	}

	/** Get {@link org.jhove2.core.io.Input} for the source unit.
	 * @return Input for the source unit
	 * @see org.jhove2.core.source.Source#getInput()
	 */
	@Override
	public Input getInput() {
		return this.input;
	}

	/** Get modules that processed the source unit.
	 * @return Modules that processed the source unit
	 * @see org.jhove2.core.source.Source#getModules()
	 */
	@Override
	public List<Processible> getModules() {
		return this.modules;
	}

}
