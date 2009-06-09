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

/** An abstract JHOVE2 component.  A component is an
 * independently-distributable and installable
 * {@link org.jhove2.core.Reporter}.
 * 
 * @author mstrong, slabrams
 */
public abstract class AbstractComponent
	extends AbstractReporter
	implements Component, Durable
{
	/** Component developer. */
	protected Agent developer;

	/** Component informative note. */
	protected String note;

	/** Component release date. */
	protected String date;
	
	/** Component rights statement. */
	protected String rights;
	
	/** Component development stage. */
	protected Stage stage;

	/** Final time, in milliseconds. */
	protected long timeFinal;
	
	/** Initial time, in milliseconds. */
	protected long timeInitial;
	
	/** Component version. */
	protected String version;
	
	/** Instantiate a new <code>AbstractComponent</code>.
	 * @param version Component version identifier
	 * @param date    Component release date
	 * @param stage   Component development stage
	 */
	public AbstractComponent(String version, String date, Stage stage) {
		super();
		
		this.version = version;
		this.date    = date;
		this.stage   = stage;
		
		setInitialTime();
		this.timeFinal = UNINITIALIZED;
	}

	/** Get component developer.
	 * @return Component developer
	 * @see org.jhove2.core.Component#getDeveloper()
	 */
	public Agent getDeveloper() {
		return this.developer;
	}
	
	/** Get component elapsed time, in milliseconds.
	 * @return Component elapsed time, in milliseconds
	 * @see org.jhove2.core.Durable#getElapsedTime()
	 */
	@Override
	public Duration getElapsedTime() {
		if (this.timeFinal == UNINITIALIZED) {
			setFinalTime();
		}
		return new Duration(this.timeFinal - this.timeInitial);
	}

	/** Get component informative note.
	 * @return Component informative note
	 * @see org.jhove2.core.Component#getNote()
	 */
	@Override
	public String getNote() {
		return this.note;
	}

	/** Get component release date.
	 * @return Component release date
	 * @see org.jhove2.core.Component#getReleaseDate()
	 */
	@Override
	public String getReleaseDate() {
		return this.date;
	}

	/** Get component rights statement.
	 * @return Component rights statement
	 * @see org.jhove2.core.Component#getRightsStatement()
	 */
	@Override
	public String getRightsStatement() {
		return this.rights;
	}
	
	/** Get component development stage.
	 * @return Component development stage
	 * @see org.jhove2.core.Component#getStage()
	 */
	public Stage getStage() {
		return this.stage;
	}

	/** Get component version.
	 * @return Component version
	 * @see org.jhove2.core.Component#getVersion()
	 */
	@Override
	public String getVersion() {
		return this.version;
	}
	
	/** Set component developer.
	 * @param developer Component developer
	 */
	public void setDeveloper(Agent developer) {
		this.developer = developer;
	}

	/** Set component initial time.
	 * @see org.jhove2.core.Durable#setInitialTime()
	 */
	@Override
	public void setInitialTime() {
		this.timeInitial = System.currentTimeMillis();
	}

	/** Set component final time.
	 * @see org.jhove.core2.Durable#setFinalTime()
	 */
	@Override
	public void setFinalTime() {
		this.timeFinal = System.currentTimeMillis();
	}
	
	/** Set component informative note.
	 * @param note Component informative note
	 */
	public void setNote(String note) {
		this.note = note;
	}
	
	/** Set component rights statement.
	 * @param rights Component rights statement
	 */
	public void setRightsStatement(String rights) {
		this.rights = rights;
	}
}
