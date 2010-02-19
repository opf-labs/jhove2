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

import org.jhove2.core.source.Source;

/**
 * Convenience class for managing information about a Source which is a member of
 * a group of possibly related sources. Used by {@org.jhove2.GlobPathRecognizer}
 * 
 * @author smmorrissey
 *
 */
public class GlobPathMatchInfo {
	/** Source that is member of group */
	protected Source source;
	/** matches mustHave token */
	protected boolean mustHave;
	/** matchs mayHave token */
	protected boolean mayHave;
	
	/**
	 * Constructor
	 */
	public GlobPathMatchInfo(){}
	/**
	 * Constructor
	 * @param Source that is member of group
	 */
	public GlobPathMatchInfo(Source source){
		this();
		this.source = source;
	}
	/**
	 * Get Source that is member of group
	 * @return
	 */
	public Source getSource() {
		return source;
	}
	/**
	 * Set Source that is member of group
	 * @param Source that is member of group
	 */
	public void setSource(Source source) {
		this.source = source;
	}
	/**
	 * Get mustHave
	 * @return mustHave
	 */
	public boolean isMustHave() {
		return mustHave;
	}
	/**
	 * Set mustHave
	 * @param mustHave
	 */
	public void setMustHave(boolean mustHave) {
		this.mustHave = mustHave;
	}
	/**
	 * Get mayHave
	 * @return mayHave
	 */
	public boolean isMayHave() {
		return mayHave;
	}
	/**
	 * Set mayHave
	 * @param mayHave
	 */
	public void setMayHave(boolean mayHave) {
		this.mayHave = mayHave;
	}
	
}
