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

import org.jhove2.core.Durable;
import org.jhove2.core.Duration;
import org.jhove2.core.Processible;
import org.jhove2.core.io.Input;

/** An abstract JHOVE2 source unit.  A source unit is a formatted object that
 * can be characterized, which may be a file, a subset of a file, or a group
 * of files.
 * 
 * @author mstrong, slabrams
 */
public abstract class AbstractSource
	implements Source, Durable
{	
	/** Child source units. */
	protected List<Source> children;
	
	/** Module elapsed time, end. */
	protected long endTime;

	/** Source unit input. */
	protected Input input;
	
	/** Modules that processed the source unit. */
	protected List<Processible> modules;
	
	/** Module elapsed time, start. */
	protected long startTime;
	
	/** Instantiate a new <code>AbstractSource</code>.
	 */
	public AbstractSource() {
		this.children = new ArrayList<Source>();
		this.modules  = new ArrayList<Processible>();
	}
	
	/** Add a child source unit.
	 * @param child Child source unit
	 * @see org.jhove2.core.source.Source#addChildSource(org.jhove2.core.source.Source)
	 */
	@Override
	public void addChildSource(Source child) {
		this.children.add(child);
	}
	
	/** Add a module that processed the source unit.
	 * @param module Module that processed the source unit
	 * @see org.jhove2.core.source.Source#addModule(org.jhove2.core.Processible)
	 */
	@Override
	public void addModule(Processible module) {
		this.modules.add(module);
	}
	
	/** Get child source units.
	 * @return Child source units
	 * @see org.jhove2.core.source.Source#getChildSources()
	 */
	@Override
	public List<Source> getChildSources() {
		return this.children;
	}
	
	/** Get elapsed time, in milliseconds.  The shortest reportable
	 * elapsed time is 1 milliscond.
	 * @return Elapsed time, in milliseconds
	 */
	@Override
	public Duration getElapsedTime() {
		if (this.endTime == Duration.UNINITIALIZED) {
			this.endTime = System.currentTimeMillis();
		}
		
		return new Duration(this.endTime - this.startTime);
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
	
	/** Get number of child source units.
	 * @return Number of child source units
	 * @see org.jhove2.core.source.Source#getNumChildSources()
	 */
	@Override
	public int getNumChildSources() {
		return this.children.size();
	}
	
	/** Set the end time of the elapsed duration.
	 * @see org.jhove2.core.Durable#setStartTime()
	 */
	@Override
	public void setEndTime() {
		this.endTime = System.currentTimeMillis();
	}

	/** Set the start time of the elapsed duration.
	 * @see org.jhove2.core.Durable#setStartTime()
	 */
	@Override
	public void setStartTime() {
		this.startTime = System.currentTimeMillis();
	}
}
