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

package org.jhove2.core.format;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.Document;
import org.jhove2.core.I8R;
import org.jhove2.core.reportable.AbstractReportable;

/**
 * A JHOVE2 format.
 * 
 * @author mstrong, slabrams, smmorrissey
 */
public class Format extends AbstractReportable implements Comparable<Format> {
	/** Format ambiguities. Ambiguous presumptiveFormatIds should report their caveats. */
	public enum Ambiguity {
		Ambiguous, Unambiguous
	}

	/** Format scope. */
	public enum Type {
		Family, Format
	}

	/** Format alias identifiers. */
	protected Set<I8R> aliasIdentifiers;

	/** Format alias names. */
	protected Set<String> aliasNames;

	/** Format ambiguity. */
	protected Ambiguity ambiguity;

	/** Format caveats. */
	protected String caveats;

	/** Format canonical identifier, in the JHOVE2 namespace. */
	protected I8R identifier;

	/** Format canonical name. */
	protected String name;

	/** Format informative note. */
	protected String note;

	/** Format specifications. */
	protected List<Document> specifications;

	/** Format scope. */
	protected Type type;

	/** Format version. */
	protected String version;

	/**
	 * Instantiate a new <code>Format</code>.
	 * 
	 * @param name
	 *            Format canonical name
	 * @param identifier
	 *            Format canonical identifier
	 * @param scope
	 *            Format scope
	 * @param ambiguity
	 *            Format ambiguity
	 */
	public Format(String name, I8R identifier, Type type, Ambiguity ambiguity) {
		super();

		this.name = name;
		this.identifier = identifier;
		this.type = type;
		this.ambiguity = ambiguity;

		this.aliasIdentifiers = new TreeSet<I8R>();
		this.aliasNames = new TreeSet<String>();
		this.specifications = new ArrayList<Document>();
	}

	/**
	 * Get format alias identifiers.
	 * 
	 * @return Format alias identifiers
	 */
	@ReportableProperty(order = 5, value = "Format alias identifiers.")
	public Set<I8R> getAliasIdentifiers() {
		return this.aliasIdentifiers;
	}

	/**
	 * Get format alias names.
	 * 
	 * @return Format alias names
	 */
	@ReportableProperty(order = 6, value = "Format alias names.")
	public Set<String> getAliasNames() {
		return this.aliasNames;
	}

	/**
	 * Get format ambiguity.
	 * 
	 * @return Format ambiguity
	 */
	@ReportableProperty(order = 8, value = "Format ambiguity.")
	public Ambiguity getAmbiguity() {
		return this.ambiguity;
	}

	/**
	 * Get format caveats.
	 * 
	 * @return Format caveats
	 */
	@ReportableProperty(order = 9, value = "Format caveats.")
	public String getCaveats() {
		return this.caveats;
	}

	/**
	 * Get format canonical identifier, in the JHOVE2 namespace.
	 * 
	 * @return Format canonical identifier
	 */
	@ReportableProperty(order = 2, value = "Format canonical identifier, in the "
			+ "JHOVE2 namespace.")
	public I8R getIdentifier() {
		return this.identifier;
	}

	/**
	 * Get format canonical name.
	 * 
	 * @return Format canonical name
	 */
	@ReportableProperty(order = 1, value = "Format canonical name.")
	public String getName() {
		return this.name;
	}

	/**
	 * Format informative note.
	 * 
	 * @return Format informative note
	 */
	@ReportableProperty(order = 10, value = "Format informative note.")
	public String getNote() {
		return this.note;
	}

	/**
	 * Get format specifications.
	 * 
	 * @return Format specifications
	 */
	@ReportableProperty(order = 7, value = "Format specifications.")
	public List<Document> getSpecifications() {
		return this.specifications;
	}

	/**
	 * Get format scope.
	 * 
	 * @return Format scope
	 */
	@ReportableProperty(order = 4, value = "Format scope.")
	public Type getType() {
		return this.type;
	}

	/**
	 * Get format version.
	 * 
	 * @return Format version
	 */
	@ReportableProperty(order = 3, value = "Format version.")
	public String getVersion() {
		return this.version;
	}

	/**
	 * Add a Set of format alias identifiers.
	 * 
	 * @param identifiers
	 *            Format alias identifiers
	 */
	public void setAliasIdentifiers(Set<I8R> identifiers) {
		TreeSet<I8R> naliasIdentifiers = new TreeSet<I8R>();
		naliasIdentifiers.addAll(identifiers);
		this.aliasIdentifiers = naliasIdentifiers;
	}


	/**
	 * Add a Set of format alias names.
	 * 
	 * @param names
	 *            Format alias names
	 */
	public void setAliasNames(Set<String> names) {
		this.aliasNames.addAll(names);
	}

	/**
	 * Set format caveats.
	 * 
	 * @param caveats
	 *            Format caveats
	 */
	public void setCaveats(String caveats) {
		this.caveats = caveats;
	}

	/**
	 * Set format informative note.
	 * 
	 * @param note
	 *            Format informative note
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * Add a List of format specifications.
	 * 
	 * @param specifications
	 *            Format specifications
	 */
	public void setSpecifications(List<Document> specifications) {
		ArrayList<Document> specs = new ArrayList<Document>();
		specs.addAll(specifications);
		this.specifications = specs;
	}

	/**
	 * Set format version.
	 * 
	 * @param version
	 *            Format version
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	public boolean equals(Object obj){
		if (obj==null){
			return false;
		}
		if (this==obj){
			return true;
		}
		if (!(obj instanceof Format)){
			return false;
		}
		Format fObjt = (Format)obj;
		return this.getIdentifier().equals(fObjt.getIdentifier());
	}
	@Override
	public int compareTo(Format format) {
		if (format==null){
			return +1;
		}
		if (this == format){
			return 0;
		}
		return this.getIdentifier().compareTo(format.getIdentifier());
	}
}
