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

package org.jhove2.module;

import java.util.ArrayList;
import java.util.List;

import org.jhove2.core.Agent;
import org.jhove2.core.WrappedProduct;
import org.jhove2.core.TimerInfo;
import org.jhove2.core.reportable.AbstractReportable;

/**
 * An abstract JHOVE2 module, an independently distributable product that
 * reports its elapsed processing time.
 * 
 * @author mstrong, slabrams, smorrissey
 */
public abstract class AbstractModule
	extends AbstractReportable
	implements Module 
 {
	/** Product developers. */
	protected List<Agent> developers;

	/** Product name, based on the simple class name. */
	protected String name;

	/** Product informative note. */
	protected String note;

	/** Product release date in ISO 8601 form: "YYYY-MM-DD". */
	protected String releaseDate;

	/** Product rights statement. */
	protected String rights;
	
	/** Module scope: generic or specific. */
	protected Scope scope;

	/** Product version identifier in three-part form: "M.N.P". */
	protected String version;
	
	/**
	 * Module wrapped product. This field should be defined if the module does
	 * not directly perform its own processing, but rather invokes an external
	 * tool.
	 */
	protected WrappedProduct wrappedProduct;
	
	/** Timer info used to track elapsed time for running of this module. */
	protected TimerInfo timerInfo;
	
	/**
	 * Instantiate a new <code>AbstractModule</code>.
	 * 
	 * @param version
	 *            Module version identifier in three-part form: "M.N.P"
	 * @param release
	 *            Module release date in ISO 8601 format: "YYYY-MM-DD"
	 * @param rights
	 *            Module rights statement
	 * @param scope
	 *            Module scope: generic or specific
	 */
	public AbstractModule(String version, String release, String rights,
			              Scope scope)
	{	
		super();
		this.version     = version;
		this.releaseDate = release;
		this.rights      = rights;
		this.scope        = scope;
		
		this.developers  = new ArrayList<Agent>();		
		this.timerInfo   = new TimerInfo();
		this.name        = this.getClass().getSimpleName();
	}

	public AbstractModule(){
		super();
	}
	/**
	 * Get module developers.
	 * 
	 * @return Product developers
	 */
	@Override
	public List<Agent> getDevelopers() {
		return this.developers;
	}

	/**
	 * Get module informative note.
	 * 
	 * @return Product informative note
	 */
	@Override
	public String getNote() {
		return this.note;
	}

	/**
	 * Get module release date.
	 * 
	 * @return Product release date
	 */
	@Override
	public String getReleaseDate() {
		return this.releaseDate;
	}

	/**
	 * Get module rights statement.
	 * 
	 * @return Product rights statement
	 */
	@Override
	public String getRightsStatement() {
		return this.rights;
	}
	
	/** Get module scope: generic or specific (to a source unit).
	 * @return Module scope
	 */
	@Override
	public Scope getScope() {
		return this.scope;
	}

	/**
	 * Get module version.
	 * 
	 * @return Product version
	 */
	@Override
	public String getVersion() {
		return this.version;
	}


	/**
	 * Add module developers.
	 * 
	 * @param developers
	 *            Product developers
	 */
	public void setDevelopers(List<Agent> developers) {
		this.developers = developers;
	}

	/**
	 * Set module informative note.
	 * 
	 * @param note
	 *            Product informative note
	 */
	public void setNote(String note) {
		this.note = note;
	}
	
	/**
	 * Get wrapped external product.
	 * 
	 * @return Wrapped external product
	 */
	@Override
	public WrappedProduct getWrappedProduct() {
		return this.wrappedProduct;
	}
	
	/**
	 * Set wrapped product.
	 * 
	 * @param product
	 *            Wrapped product
	 */
	public void setWrappedProduct(WrappedProduct product) {
		this.wrappedProduct = product;
	}
	
	/** Get module timer information.
	 * @return Module timer information
	 */
	@Override
	public TimerInfo getTimerInfo() {
		return timerInfo;
	}
}
