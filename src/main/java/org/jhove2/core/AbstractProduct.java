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

/** Abstract JHOVE2 product, an independently distributable
 * {@link org.jhove2.core.Reportable}.
 * 
 * @author mstrong, slabrams
 */
public abstract class AbstractProduct
	implements Product
{
	/** Product developers. */
	protected List<Agent> developers;
	
	/** Product informative note. */
	protected String note;
	
	/** Product release date in ISO 8601 form: "YYYY-MM-DD". */
	protected String date;
	
	/** Product rights statement. */
	protected String rights;

	/** Product version identifier in three-part form: "M.N.P". */
	protected String version;
	
	/** Instantiate a new <code>AbstractProduct</code>.
	 * @oaran version Product version identifier in three-part form: "M.N.P"
	 * @param date    Product release date in ISO 8601 format: "YYYY-MM-DD"
	 * @param rights  Product rights statement
	 */
	public AbstractProduct(String version, String date, String rights) {
		this.version = version;
		this.date    = date;
		this.rights  = rights;
		
		this.developers  = new ArrayList<Agent>();
	}
	
	/** Add product developer.
	 * @param developer Product developer
	 */
	public void addDeveloper(Agent developer) {
		this.developers.add(developer);
	}
	
	/** Get product developers.
	 * @return Product developers
	 * @see org.jhove2.core.Product#getDevelopers()
	 */
	@Override
	public List<Agent> getDevelopers() {
		return this.developers;
	}

	/** Get product informative note.
	 * @return Product informative note
	 * @see org.jhove2.core.Product#getNote()
	 */
	@Override
	public String getNote() {
		return this.note;
	}

	/** Get product release date.
	 * @return AbstractProduct release date
	 * @see org.jhove2.core.Product#getReleaseDate()
	 */
	@Override
	public String getReleaseDate() {
		return this.date;
	}

	/** Get product rights statement.
	 * @return Product rights statement
	 * @see org.jhove2.core.Product#getRightsStatement()
	 */
	@Override
	public String getRightsStatement() {
		return this.rights;
	}

	/** Get product version.
	 * @return Product version
	 * @see org.jhove2.core.Product#getVersion()
	 */
	@Override
	public String getVersion() {
		return this.version;
	}

	/** Set product informative note.
	 * @param note Product informative note
	 */
	public void setNote(String note) {
		this.note = note;
	}
}
