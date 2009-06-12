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

/** An abstract JHOVE2 aggregate source unit.
 * 
 * @author mstrong, slabrams
 *
 */
public abstract class AbstractAggregateSource
	extends AbstractSource
	implements AggregateSource
{
	/** Child source units. */
	protected List<Source> children;
	
	/** Instantiate a new <code>AbstractAggregateSource</code>.
	 */
	public AbstractAggregateSource() {
		super();
		
		this.children = new ArrayList<Source>();
	}
	
	/** Instantiate a new <code>AbstractAggregateSource</code>.
	 * @param child    First child source units
	 * @param children Remaining child source units
	 */
	public AbstractAggregateSource(Source child, Source... children) {
		this();
		
		this.children.add(child);
		if (children != null && children.length > 0) {
			for (int i=0; i<children.length; i++) {
				this.children.add(children[i]);
			}
		}
	}
	
	/** Instantiate a new <code>AbstractAggregateSource</code>.
	 * @param children Child source units
	 */
	public AbstractAggregateSource(List<Source> children) {
		this();
		
		this.children.addAll(children);
	}
	
	/** Add a child source unit.
	 * @param child Child source unit
	 * @see org.jhove2.core.source.AggregateSource#addChildSource(org.jhove2.core.source.Source)
	 */
	@Override
	public void addChildSource(Source child) {
		this.children.add(child);
	}
	
	/** Get child source units.
	 * @return Child source units
	 * @see org.jhove2.core.source.AggregateSource#getChildSources()
	 */
	@Override
	public List<Source> getChildSources() {
		return this.children;
	}

	/** Get number of child source units.
	 * @return Number of child source units
	 * @see org.jhove2.core.source.AggregateSource#getNumChildSources()
	 */
	@Override
	public int getNumChildSources() {
		return this.children.size();
	}
}
