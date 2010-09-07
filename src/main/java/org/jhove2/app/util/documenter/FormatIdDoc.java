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
package org.jhove2.app.util.documenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jhove2.core.Document;

/**
 * Container for information about Format and profile Formats 
 * Information is required to fill in Format Module specification
 * @author smorrissey
 */
public class FormatIdDoc {

	protected String canonicalFormatName;
	protected String canonicalFormatIdentifier;
	protected List<String> aliasFormatNames;
	protected List<String> aliasFormatIdentifiers;	
	protected Map<Document.Intention,List<ReferenceDoc>> references;

	public FormatIdDoc(){
		aliasFormatNames = new ArrayList<String>();
		aliasFormatIdentifiers = new ArrayList<String>();
		references = new HashMap<Document.Intention,List<ReferenceDoc>>() ;
	}

	/**
	 * @return the canonicalFormatName
	 */
	public String getCanonicalFormatName() {
		return canonicalFormatName;
	}

	/**
	 * @return the canonicalFormatIdentifier
	 */
	public String getCanonicalFormatIdentifier() {
		return canonicalFormatIdentifier;
	}

	/**
	 * @return the aliasFormatNames
	 */
	public List<String> getAliasFormatNames() {
		return aliasFormatNames;
	}

	/**
	 * @return the aliasFormatIdentifiers
	 */
	public List<String> getAliasFormatIdentifiers() {
		return aliasFormatIdentifiers;
	}

	/**
	 * @return the references
	 */
	public Map<Document.Intention, List<ReferenceDoc>> getReferences() {
		return references;
	}

	/**
	 * @param canonicalFormatName the canonicalFormatName to set
	 */
	public void setCanonicalFormatName(String canonicalFormatName) {
		this.canonicalFormatName = canonicalFormatName;
	}

	/**
	 * @param canonicalFormatIdentifier the canonicalFormatIdentifier to set
	 */
	public void setCanonicalFormatIdentifier(String canonicalFormatIdentifier) {
		this.canonicalFormatIdentifier = canonicalFormatIdentifier;
	}

	/**
	 * @param aliasFormatNames the aliasFormatNames to set
	 */
	public void setAliasFormatNames(List<String> aliasFormatNames) {
		this.aliasFormatNames = aliasFormatNames;
	}

	/**
	 * @param aliasFormatIdentifiers the aliasFormatIdentifiers to set
	 */
	public void setAliasFormatIdentifiers(List<String> aliasFormatIdentifiers) {
		this.aliasFormatIdentifiers = aliasFormatIdentifiers;
	}

	/**
	 * @param references the references to set
	 */
	public void setReferences(Map<Document.Intention, List<ReferenceDoc>> references) {
		this.references = references;
	}
}
