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

import org.jhove2.core.Component;
import org.jhove2.core.Durable;
import org.jhove2.core.Duration;
import org.jhove2.core.AbstractReporter;

/** Abstract JHOVE2 source unit.
 * 
 * @author mstrong, slabrams
 */
public abstract class AbstractSource
	extends AbstractReporter
	implements Durable, Source
{
	/** Modules reporting properties of the source unit. */
	protected List<Component> modules;

	/** Source unit existence status: true of the source unit exists. */
	protected boolean isExtant;

	/** Final time, in milliseconds. */
	protected long timeFinal;
	
	/** Initial time, in milliseconds. */
	protected long timeInitial;
	
	/** Instantiate a new <code>AbstractSource</code>.
	 */
	public AbstractSource() {
		super();
	
		setInitialTime();
		this.timeFinal = UNINITIALIZED;
		this.modules = new ArrayList<Component>();
	}
	
	/** Add module reporting source unit reportable properties.
	 * @param Module Module reporting source reportable properties
	 * @see org.jhove2.core.Source#addModule(org.jhove2.core.Component)
	 */
	@Override
	public void addModule(Component module) {
		this.modules.add(module);
	}
	
	/** Get source unit elapsed time, in milliseconds.
	 * @return Source unit elapsed time, in milliseconds
	 * @see org.jhove2.core.Durable#getElapsedTime()
	 */
	@Override
	public Duration getElapsedTime() {
		if (this.timeFinal == UNINITIALIZED) {
			setFinalTime();
		}
		return new Duration(this.timeFinal - this.timeInitial);
	}
	
	/** Get modules reporting properties of the source unit.
	 * @return Components reporting properties of the source unit.
	 * @see org.jhove2.core.source.Source#getComponents()
	 */
	@Override
	public List<Component> getModules() {
		return this.modules;
	}
	
	/** Get source unit existence status.
	 * @return True if source unit exists
	 * @see org.jhove2.core.source.Source#isExtant()
	 */
	@Override
	public boolean isExtant() {
		return this.isExtant;
	}

	/** Set source unit initial time.
	 * @see org.jhove2.core.Durable#setInitialTime()
	 */
	@Override
	public void setInitialTime() {
		this.timeInitial = System.currentTimeMillis();
	}

	/** Set source unit final time.
	 * @see org.jhove.core2.Durable#setFinalTime()
	 */
	@Override
	public void setFinalTime() {
		this.timeFinal = System.currentTimeMillis();
	}
}
