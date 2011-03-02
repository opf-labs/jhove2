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

package org.jhove2.core.reportable;

import org.jhove2.core.I8R;

import com.sleepycat.persist.model.Persistent;

/**
 * Base class for all classes that implement Reportable interface
 * Ensures all Reportables correctly form and populate I8R jhoveIdentifier field
 * and the (user-configurable) name field
 * @author smorrissey
 *
 */
@Persistent
public abstract class AbstractReportable
	implements Reportable
{
    /** Object's unique JHOVE2 identifier  cannot be set by user */
	protected I8R myI8R = null;
	
	/** Object's simple name;  may be configure by user */
	protected String reportableName = null;
	
	public AbstractReportable(){};
	
	/** Get reportable identifier.
	 * @return Reportable identifier
	 * @see org.jhove2.core.reportable.Reportable#getReportableIdentifier()
	 */
	@Override
	public I8R getReportableIdentifier() {
		if (this.myI8R == null){
			this.myI8R = I8R.makeReportableI8R(this);
		}
		return this.myI8R;
	}
	
	/** Get reportable name.
	 * @return Reportable name
	 * @see org.jhove2.core.reportable.Reportable#getReportableName()
	 */
	@Override
	public String getReportableName(){
		if (this.reportableName == null) {
			this.reportableName = this.getClass().getSimpleName();
		}
		return reportableName;
	}
	
	/**
	 * Set reportable name.
	 * @param name Reportable name
	 * @see org.jhove2.core.reportable.Reportable#setReportableName(String)
	 */
	@Override
	public void setReportableName(String name){
		this.reportableName = name;
	}	
}
