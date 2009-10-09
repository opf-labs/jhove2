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

import org.jhove2.annotation.ReportableProperty;

/**
 * A JHOVE2 human or corporate agent.
 * 
 * @author mstrong, slabrams
 */
public class Agent extends AbstractReportable {
	/** Agent types. */
	public enum Type {
		Corporate, Personal
	}

	/** Agent postal address. */
	protected String address;

	/** Personal agent corporate affiliation. */
	protected Agent affiliation;

	/** Agent email address. */
	protected String email;

	/** Agent fax number. */
	protected String fax;

	/** Agent name. */
	protected String name;

	/** Agent informative note. */
	protected String note;

	/** Agent telephone number. */
	protected String telephone;

	/** Agent type. */
	protected Type type;

	/** Agent URI. */
	protected String uri;

	/**
	 * Instantiate a new <code>Agent</code>.
	 * 
	 * @param name
	 *            Agent name
	 * @param type
	 *            Agent type
	 */
	public Agent(String name, Type type) {
		super();

		this.name = name;
		this.type = type;
	}

	/**
	 * Get agent postal address.
	 * 
	 * @return Agent postal address
	 */
	@ReportableProperty(order = 4, value = "Agent postal address.")
	public String getAddress() {
		return this.address;
	}

	/**
	 * Get personal agent corporate affiliation.
	 * 
	 * @return Agent corporate affiliation
	 */
	@ReportableProperty(order = 3, value = "Personal agent corporate affiliation.")
	public Agent getAffiliation() {
		return this.affiliation;
	}

	/**
	 * Get agent email address.
	 * 
	 * @return Agent email address
	 */
	@ReportableProperty(order = 7, value = "Agent email address.")
	public String getEmail() {
		return this.email;
	}

	/**
	 * Get agent fax number.
	 * 
	 * @return Agent fax number
	 */
	@ReportableProperty(order = 6, value = "Agent fax number.")
	public String getFax() {
		return this.fax;
	}

	/**
	 * Get agent name.
	 * 
	 * @return Agent name
	 */
	@ReportableProperty(order = 1, value = "Agent name.")
	public String getFileName() {
		return this.name;
	}

	/**
	 * Get agent informative note.
	 * 
	 * @return Agent informative note
	 */
	@ReportableProperty(order = 9, value = "Agent informative note.")
	public String getNote() {
		return this.note;
	}

	/**
	 * Get agent telephone number.
	 * 
	 * @return Agent telephone number
	 */
	@ReportableProperty(order = 5, value = "Agent telephone number.")
	public String getTelephone() {
		return this.telephone;
	}

	/**
	 * Get agent type.
	 * 
	 * @return Agent type
	 */
	@ReportableProperty(order = 2, value = "Agent type.")
	public Type getType() {
		return this.type;
	}

	/**
	 * Get agent URI.
	 * 
	 * @return Agent URI
	 */
	@ReportableProperty(order = 8, value = "Agent URI.")
	public String getURI() {
		return this.uri;
	}

	/**
	 * Set agent postal address.
	 * 
	 * @param address
	 *            Agent postal address
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * Set personal agent corporate affiliation.
	 * 
	 * @param affiliation
	 *            Agent corporate affiliation
	 */
	public void setAffiliation(Agent affiliation) {
		this.affiliation = affiliation;
	}

	/**
	 * Set agent email address.
	 * 
	 * @param email
	 *            Agent email address
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Set agent fax number.
	 * 
	 * @param fax
	 *            Agent fax number
	 */
	public void setFax(String fax) {
		this.fax = fax;
	}

	/**
	 * Set agent informative note.
	 * 
	 * @param note
	 *            Agent informative note
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * Set agent telephone number.
	 * 
	 * @param telephone
	 *            Agent telephone number
	 */
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	/**
	 * Set agent URI.
	 * 
	 * @param uri
	 *            Agent URI
	 */
	public void setURI(String uri) {
		this.uri = uri;
	}
}
