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

/**
 * JHOVE2 product, an independently distributable and configurable
 * {@link org.jhove2.core.Reportable}.
 * 
 * @author mstrong, slabrams, smorrissey
 */
public class WrappedProductInfo extends AbstractReportable {
	/** Product developers. */
	protected List<Agent> developers;

	/** Wrapped Product name */
	protected String name;

	/** Product informative note. */
	protected String note;

	/** Product release date in ISO 8601 form: "YYYY-MM-DD". */
	protected String releaseDate;

	/** Product rights statement. */
	protected String rightsStatement;

	/** Product version identifier in three-part form: "M.N.P". */
	protected String version;
	
	/** Source programming language(s) in which product is written */
	protected List<String> productLanguages;
	
	/** Environment(s) (Operating systems, etc.) in which product will run */
	protected List<String> productEnvironments;
	
	/** Constraints for running product */
	protected List<String> productConstraints;
	
	/** indicates proprietary versus open source */
	protected boolean proprietaryProduct;
	
	/** maintaining authority for product */
	protected String productAuthority;

	public WrappedProductInfo(){
		this.developers = new ArrayList<Agent>();
		this.productLanguages = new ArrayList<String>();
		this.productEnvironments = new ArrayList<String>();
		this.productConstraints = new ArrayList<String>();
	}
	
	public WrappedProductInfo(String name){
		this();
		this.name = name;		
	}
	/**
	 * Instantiate a new <code>AbstractProduct</code>.
	 * 
	 * @param version
	 *            Product version identifier in three-part form: "M.N.P"
	 * @param date
	 *            Product release date in ISO 8601 format: "YYYY-MM-DD"
	 * @param rights
	 *            Product rights statement
	 */
	public WrappedProductInfo(String name, String version, String date, String rights) {
		this(name);
		this.version = version;
		this.releaseDate = date;
		this.rightsStatement = rights;
		
	}

	/**
	 * Get product developers.
	 * 
	 * @return Product developers
	 */
	@ReportableProperty(order = 4, value = "Product developers.")
	public List<Agent> getDevelopers() {
		return this.developers;
	}

	/**
	 * Get product name, based on the class simple name.
	 * 
	 * @return Product name
	 */
	@ReportableProperty(order = 1, value = "Product name, based on the class "
		+ "simple name.")
	public String getName() {
		return this.name;
	}

	/**
	 * Get product informative note.
	 * 
	 * @return Product informative note
	 */
	@ReportableProperty(order = 6, value = "Product informative note.")
	public String getNote() {
		return this.note;
	}

	/**
	 * Get product release date.
	 * 
	 * @return Product release date
	 */
	@ReportableProperty(order = 3, value = "Product release date.")
	public String getReleaseDate() {
		return this.releaseDate;
	}

	/**
	 * Get product rights statement.
	 * 
	 * @return Product rights statement
	 */
	@ReportableProperty(order = 5, value = "Product rights statement.")
	public String getRightsStatement() {
		return this.rightsStatement;
	}

	/**
	 * Get product version.
	 * 
	 * @return Product version
	 */
	@ReportableProperty(order = 2, value = "Product version identifier.")
	public String getVersion() {
		return this.version;
	}

	/**
	 * Add product developer.
	 * 
	 * @param developer
	 *            Product developer
	 */
	public void addDeveloper(Agent developer) {
		this.developers.add(developer);
	}

	/**
	 * Add product developers.
	 * 
	 * @param developers
	 *            Product developers
	 */
	public void setDevelopers(List<Agent> developers) {
		this.developers.addAll(developers);
	}

	/**
	 * Set product informative note.
	 * 
	 * @param note
	 *            Product informative note
	 */
	public void setNote(String note) {
		this.note = note;
	}
	@ReportableProperty(order = 7, value = "Source code languge(s) of product.")
	public List<String> getProductLanguages() {
		return productLanguages;
	}

	public void setProductLanguages(List<String> productLanguages) {
		this.productLanguages = productLanguages;
	}
	@ReportableProperty(order = 8, value = "Environments (OS) in which product will run.")
	public List<String> getProductEnvironments() {
		return productEnvironments;
	}

	public void setProductEnvironments(List<String> productEnvironments) {
		this.productEnvironments = productEnvironments;
	}
	@ReportableProperty(order = 9, value = "Constraints for running product.")
	public List<String> getProductConstraints() {
		return productConstraints;
	}

	public void setProductConstraints(List<String> productConstraints) {
		this.productConstraints = productConstraints;
	}

	public void setReportableName(String name) {
		this.name = name;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	@ReportableProperty(order = 10, value = "Indicates if product is proprietary rather than open source.")
	public boolean isProprietaryProduct() {
		return proprietaryProduct;
	}

	public void setProprietaryProduct(boolean proprietaryProduct) {
		this.proprietaryProduct = proprietaryProduct;
	}
	@ReportableProperty(order = 11, value = "Maintaining authority for product.")
	public String getProductAuthority() {
		return productAuthority;
	}

	public void setProductAuthority(String productAuthority) {
		this.productAuthority = productAuthority;
	}

	public void setRightsStatement(String rightsStatement) {
		this.rightsStatement = rightsStatement;
	}
	

}
