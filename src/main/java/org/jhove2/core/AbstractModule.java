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

package org.jhove2.core;

import java.util.ArrayList;
import java.util.List;

import org.jhove2.annotation.Reportable;

/** An abstract JHOVE2 module.  A module is an independently-distributable
 * {@link org.jhove2.annotation.Reportable}.
 * 
 * @author mstrong, slabrams
 */
@Reportable("An independently-distributable reporter.")
public class AbstractModule
	implements Module, Durable
{
	/** Module developers. */
	protected List<Agent> developers;
	
	/** Module informative note. */
	protected String note;
	
	/** Module release date in ISO 8601 form: "YYYY-MM-DD". */
	protected String date;
	
	/** Module rights statement. */
	protected String rights;
	
	/** Module elapsed time, final. */
	protected long timeFinal;
	
	/** Module elapsed time, initial. */
	protected long timeInitial;
	
	/** Module version identifier in three-part form: "M.N.P". */
	protected String version;
	
	/** Instantiate a new <code>AbstractModule</code>.
	 * @oaran version Module version identifier in three-part form: "M.N.P"
	 * @param date    Module release date in ISO 8601 format: "YYYY-MM-DD"
	 * @param rights  Module rights statement
	 */
	public AbstractModule(String version, String date, String rights) {
		this.version = version;
		this.date    = date;
		this.rights  = rights;
		
		this.developers  = new ArrayList<Agent>();
		this.timeFinal   = Duration.UNINITIALIZED;
		this.timeInitial = System.currentTimeMillis();
	}
	
	/** Add module developer.
	 * @param developer Module developer
	 */
	public void addDeveloper(Agent developer) {
		this.developers.add(developer);
	}
	
	/** Get module developers.
	 * @return Module developers
	 * @see org.jhove2.core.Module#getDevelopers()
	 */
	@Override
	public List<Agent> getDevelopers() {
		return this.developers;
	}

	/** Get module informative note.
	 * @return Module informative note
	 * @see org.jhove2.core.Module#getNote()
	 */
	@Override
	public String getNote() {
		return this.note;
	}

	/** Get module release date.
	 * @return AbstractModule release date
	 * @see org.jhove2.core.Module#getReleaseDate()
	 */
	@Override
	public String getReleaseDate() {
		return this.date;
	}

	/** Get module rights statement.
	 * @return Module rights statement
	 * @see org.jhove2.core.Module#getRightsStatement()
	 */
	@Override
	public String getRightsStatement() {
		return this.rights;
	}

	/** Get module version.
	 * @return Module version
	 * @see org.jhove2.core.Module#getVersion()
	 */
	@Override
	public String getVersion() {
		return this.version;
	}

	/** Set module informative note.
	 * @param note Module informative note
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/** Get elapsed time, in milliseconds.  The shortest reportable
	 * elapsed time is 1 milliscond.
	 * @return Elapsed time, in milliseconds
	 */
	@Override
	public Duration getElapsedTime() {
		if (this.timeFinal == Duration.UNINITIALIZED) {
			this.timeFinal = System.currentTimeMillis();
		}
		
		return new Duration(this.timeFinal - this.timeInitial);
	}

	/** Set the initial time of the elapsed duration.
	 */
	@Override
	public void setFinalTime() {
		this.timeFinal = System.currentTimeMillis();
	}

	/** Set the final time of the elapsed duration.
	 */
	@Override
	public void setInitialTime() {
		this.timeInitial = System.currentTimeMillis();
	}
}
