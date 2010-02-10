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

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.reportable.AbstractReportable;
import org.jhove2.module.AbstractModule;

/**
 * JHOVE2 product, an independently distributable and configurable
 * {@link org.jhove2.core.reportable.Reportable}.
 * 
 * @author mstrong, slabrams, smorrissey
 */
public class WrappedProduct
	extends AbstractReportable
{
	/** Product maintenance authority . */
	protected String authority;
	
	/** Product constraints. */
	protected String constraints;
	
	/** Product developers. */
	protected String developers;
	
	/** Product environments. */
	protected String environments;
	
	/** Product source programming languages. */
	protected String languages;

	/** Product name. */
	protected String name;
	
	/** Product note. */
	protected String note;
	
	/** Product open source status. */
	protected boolean isOpenSource;
	
	/** Product release date. */
	protected String releaseDate;
	
	/** Product rights statement. */
	protected String rightsStatement;
	
	/** Product version. */
	protected String version;
	
	/** Instantiate a new <code>WrappedProduct</code>.
	 * @param name Product name
	 */
	public WrappedProduct(String name, String version, String releaseDate,
			              String rightsStatement) {
		super();
		this.name            = name;
		this.version         = version;
		this.releaseDate     = releaseDate;
		this.rightsStatement = rightsStatement;
	}
	
	/** Get product maintenance authority.
	 * @return Product maintenance authority
	 */
	@ReportableProperty(order = 6, value = "Product maintenance authority.")
	public String getAuthority() {
		return this.authority;
	}

	/** Get product constraints.
	 * @return Product constraints
	 */
	@ReportableProperty(order = 9, value = "Product constraints.")
	public String getConstraints() {
		return this.constraints;
	}
	
	/** Get product developers.
	 * @return Product developers
	 */
	@ReportableProperty(order = 5, value = "Product developers.")
	public String getDevelopers() {
		return this.developers;
	}

	/** Get product environments, i.e. operating systems.
	 * @return Product environments
	 */
	@ReportableProperty(order = 7, value = "Product environments, i.e. " +
			"operating systems.")
	public String getEnvironments() {
		return this.environments;
	}
	
	/** Get product source languages.
	 * @return Product source languages
	 */
	@ReportableProperty(order = 8, value = "Product source code languges.")
	public String getLanguages() {
		return this.languages;
	}
	
	/** Get product name.
	 * @return Product name
	 */
	@ReportableProperty(order = 1, value = "Product informative name.")
	public String getName() {
		return this.name;
	}
	
	/** Get product note.
	 * @return Product note
	 */
	@ReportableProperty(order = 11, value = "Product note.")
	public String getNote() {
		return this.note;
	}

	/** Get product release date.
	 * @return Product release date
	 */
	@ReportableProperty(order = 3, value = "Product release date.")
	public String getReleaseDate() {
		return this.releaseDate;
	}
	
	/** Get product rights statement.
	 * @return Product rights statement
	 */
	@ReportableProperty(order = 4, value = "Product rights statement.")
	public String getRightsStatement() {
		return this.rightsStatement;
	}
	
	/** Get product version.
	 * @return Product version
	 */
	@ReportableProperty(order = 2, value = "Product version.")
	public String getVersion() {
		return this.version;
	}
	
	/** Get product open source status.
	 * @return Product open source status
	 */
	@ReportableProperty(order = 10, value = "Product open source status")
	public boolean isOpenSource() {
		return this.isOpenSource;
	}

	/** Set product maintenance authority.
	 * @param authority Product maintenance authority.
	 */
	public void setAuthority(String authority) {
		this.authority = authority;
	}
	
	/** Set product constraints.
	 * @param constraints Product constraints
	 */
	public void setConstraints(String constraints) {
		this.constraints = constraints;
	}
	
	/** Set product developers.
	 * @param developers Product developers
	 */
	public void setDevelopers(String developers) {
		this.developers = developers;
	}

	/** Set product environments, e.g. operating systems.
	 * @param environments Product environments
	 */
	public void setEnvironments(String environments) {
		this.environments = environments;
	}

	/** Set product source code languages.
	 * @param languages Product source code languages
	 */
	public void setLanguages(String languages) {
		this.languages = languages;
	}
	
	/** Set product informative note.
	 * @param note Product informative note
	 */
	public void setNote(String note) {
		this.note = note;
	}
	
	/** Set product release date.
	 * @param releaseDate Product release date
	 */
	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}
	
	/** Set product rights statement.
	 * @param rightsStatement Product rights statement
	 */
	public void setRightsStatement(String rightsStatement) {
		this.rightsStatement = rightsStatement;
	}

	/** Set product open source status.
	 * @param isOpenSource Product open source status
	 */
	public void setOpenSource(boolean isOpenSource) {
		this.isOpenSource = isOpenSource;
	}
	
	/** Set product version.
	 * @param version Product version
	 */
	public void setVersion(String version) {
		this.version = version;
	}
}
