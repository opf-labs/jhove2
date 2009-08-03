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

package org.jhove2.module.display;

import org.jhove2.core.JHOVE2;
import org.jhove2.module.AbstractModule;

/** JHOVE2 displayer utility.
 * 
 * @author mstrong, slabrams
 */
public abstract class AbstractDisplayer
	extends AbstractModule
	implements Displayer
{
	/** Show identifiers flag: if true, show identifiers in JSON and Text
	 * display mode.
	 */
	protected boolean showIdentifiers;
	
	/** Instantiate a new <code>AbstractDisplayer</code>.
	 * @param version AbstractDisplayer version identifier
	 * @param date    AbstractDisplayer build date
	 * @param rights  AbstractDisplayer rights statement
	 */
	public AbstractDisplayer(String version, String date, String rights) {
		super(version, date, rights);
		
		this.showIdentifiers = JHOVE2.DEFAULT_SHOW_IDENTIFIERS;
	}
	
	/** Get indentation appropriate for a nesting level.
	 * @param level Nesting level
	 * @return Indentation string
	 */
	public static synchronized String getIndent(int level) {
		StringBuffer indent = new StringBuffer();
		for (int i=0; i<level; i++) {
			indent.append(" ");
		}
		
		return indent.toString();
	}

	/** Get show identifiers flag.
	 * @return Show identifiers flag; if true, show identifiers in JSON and
	 *         Text display mode
	 * @see org.jhove2.module.display.Displayer#getShowIdentifiers()
	 */
	@Override
	public boolean getShowIdentifiers() {
		return this.showIdentifiers;
	}
	
	/** Set show identifiers flag.
	 * @param flag If true, show identifiers in JSON and Text display mode
	 * @see org.jhove2.module.display.Displayer#setShowIdentifiers(boolean)
	 */
	@Override
	public void setShowIdentifiers(boolean flag) {
		this.showIdentifiers = flag;
	}
}
