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
import java.util.Set;
import java.util.TreeSet;

import org.jhove2.annotation.ReportableProperty;

/** A JHOVE2 format.
 * 
 * @author mstrong, slabrams
 */
public class Format
	extends AbstractReporter
{
	/** Format ambiguities. Ambiguous formats should report their caveats. */
	public enum Ambiguity {
		Ambiguous,
		Unambiguous
	}
	
	/** Format type. */
	public enum Type {
		Family,
		Format
	}
	/** Format alias identifiers. */
	protected Set<Identifier> aliasIdentifiers;
	
	/** Format alias names. */
	protected Set<String> aliasNames;
	
	/** Format ambiguity. */
	protected Ambiguity ambiguity;
	
	/** Format caveats. */
	protected String caveats;
	
	/** Format canonical identifier, in the JHOVE2 namespace. */
	protected Identifier identifier;
	
	/** Format canonical name. */
	protected String name;
	
	/** Format informative note. */
	protected String note;
	
	/** Format specifications. */
	protected List<Document> specifications;
	
	/** Format type. */
	protected Type type;
	
	/** Format version. */
	protected String version;
	
	/** Instantiate a new <code>Format</code>.
	 * @param name       Format canonical name
	 * @param identifier Format canonical identifier
	 */
	public Format(String name, Identifier identifier, Type type,
			      Ambiguity ambiguity) {
		super();
		
		this.name       = name;
		this.identifier = identifier;
		this.type       = type;
		this.ambiguity  = ambiguity;
		
		this.aliasIdentifiers = new TreeSet<Identifier>();
		this.aliasNames       = new TreeSet<String>();
		this.specifications = new ArrayList<Document>();
	}
	
	/** Add format alias identifier.
	 * @param identifier Format alias identifier
	 */
	public void setAliasIdentifier(Identifier identifier) {
		this.aliasIdentifiers.add(identifier);
	}
	
	/** Add a Set of format alias identifiers.
	 * @param identifiers Format alias identifiers
	 */
	public void setAliasIdentifiers(Set<Identifier> identifiers) {
		this.aliasIdentifiers.addAll(identifiers);
	}
	
	/** Add format alias name.
	 * @param name Format alias name
	 */
	public void setAliasName(String name) {
		this.aliasNames.add(name);
	}
	
	/** Add a Set of format alias names.
	 * @param names Format alias names
	 */
	public void setAliasNames(Set<String> names) {
		this.aliasNames.addAll(names);
	}
	
	/** Add format specification.
	 * @param specification Format specification
	 */
	public void setSpecification(Document specification) {
		this.specifications.add(specification);
	}

	/** Add a List of format specifications.
	 * @param specification Format specifications
	 */
	public void setSpecifications(List<Document> specifications) {
		this.specifications.addAll(specifications);
	}

	/** Get format alias identifiers.
	 * @return Format alias identifiers
	 */
	@ReportableProperty(value=5, desc="Format alias identifiers.")
	public Set<Identifier> getAliasIdentifiers() {
		return this.aliasIdentifiers;
	}

	/** Get format alias names.
	 * @return Format alias names
	 */
	@ReportableProperty(value=6, desc="Format alias names.")
	public Set<String> getAliasNames() {
		return this.aliasNames;
	}

	/** Get format ambiguity.
	 * @return Format ambiguity
	 */
	@ReportableProperty(value=8, desc="Format ambiguity.")
	public Ambiguity getAmbiguity() {
		return this.ambiguity;
	}

	/** Get format caveats.
	 * @return Format caveats
	 */
	@ReportableProperty(value=9, desc="Format caveats.")
	public String getCaveats() {
		return this.caveats;
	}
	
	/** Get format canonical identifier, in the JHOVE2 namespace.
	 * @return Format canonical identifier
	 */
	@ReportableProperty(value=2, desc="Format canonical identifier, in the JHOVE2 namespace.")
	public Identifier getIdentifier() {
		return this.identifier;
	}
	
	/** Get format canonical name.
	 * @return Format canonical name
	 */
	@ReportableProperty(value=1, desc="Format canonical name.")
	public String getName() {
		return this.name;
	}

	/** Format informative note.
	 * @return Format informative note
	 */
	@ReportableProperty(value=10, desc="Format informative note.")
	public String getNote() {
		return this.note;
	}

	/** Get format specifications.
	 * @return Format specifications
	 */
	@ReportableProperty(value=7, desc="Format specifications.")
	public List<Document> getSpecifications() {
		return this.specifications;
	}

	/** Get format type.
	 * @return Format type
	 */
	@ReportableProperty(value=4, desc="Format type.")
	public Type getType() {
		return this.type;
	}

	/** Get format version.
	 * @return Format version
	 */
	@ReportableProperty(value=3, desc="Format version.")
	public String getVersion() {
		return this.version;
	}
	
	/** Set format caveats.
	 * @param caveats Format caveats
	 */
	public void setCaveats(String caveats) {
		this.caveats = caveats;
	}
	
	/** Set format informative note.
	 * @param note Format informative note
	 */
	public void setNote(String note) {
		this.note = note;
	}
	
	/** Set format version.
	 * @param version Format version
	 */
	public void setVersion(String version) {
		this.version = version;
	}
}
