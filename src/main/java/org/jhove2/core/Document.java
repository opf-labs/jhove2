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

import com.sleepycat.persist.model.Persistent;

/**
 * A specification document that is the basis for part of the characterization process.
 * 
 * @author mstrong, slabrams
 */
@Persistent
public class Document extends AbstractReportable {
	/** Document intentions. */
	public enum Intention {
          /**
           * TODO: define.
           */
          Authoritative,
          /**
           * TODO: define.
           */
          Informative,
          /**
           * TODO: define.
           */
          Speculative,
          /**
           * TODO: define.
           */
          Other,
          /**
           * TODO: define.
           */
          Unknown
	}

	/** Document types. */
	public enum Type {

          /**
           * TODO: define.
           */
          Article,
          /**
           * TODO: define.
           */
          Codebook,
          /**
           * TODO: define.
           */
          Correspondance,
          /**
           * TODO: define.
           */
          DataDictionary,
          /**
           * TODO: define.
           */
          Dissertation,
          /**
           * TODO: define.
           */
          Manual,
          /**
           * TODO: define.
           */
          Memorandum,
          /**
           * TODO: define.
           */
          Monograph,
          /**
           * TODO: define.
           */
          Note,
          /**
           * TODO: define.
           */
          Paper,
          /**
           * TODO: define.
           */
          Presentation,
          /**
           * TODO: define.
           */
          Recommendation,
          /**
           * TODO: define.
           */
          Report,
          /**
           * TODO: define.
           */
          Specification,
          /**
           * TODO: define.
           */
          Standard,
          /**
           * TODO: define.
           */
          Thesis,
          /**
           * TODO: define.
           */
          WebSite,
          /**
           * TODO: define.
           */
          Other,
          /**
           * TODO: define.
           */
          Unknown
	}

	/** Document identifiers. */
	protected List<I8R> identifiers;

	/** Document author(s). */
	protected String author;

	/** Document publication date. */
	protected String date;

	/** Document edition. */
	protected String edition;

	/** Document intention. */
	protected Intention intention;

	/** Document informative note. */
	protected String note;

	/** Document publisher(s). */
	protected String publisher;

	/** Document title. */
	protected String title;

	/** Document scope. */
	protected Type type;

	/**
	 * Instantiate a new <code>Document</code> object.
	 * 
	 * @param title
	 *            Document title
         * @param type
         * @param intention
	 *            Document intention
	 */
	public Document(String title, Type type, Intention intention) {
		this();

		this.title = title;
		this.type = type;
		this.intention = intention;

		this.identifiers = new ArrayList<I8R>();
	}
	
	private Document(){
		super();
	}

	/**
	 * Get document author(s).
	 * 
	 * @return Document author(s)
	 */
	@ReportableProperty(order = 1, value = "Document author or authors.")
	public String getAuthor() {
		return this.author;
	}

	/**
	 * Get document publication date.
	 * 
	 * @return Document publication date
	 */
	@ReportableProperty(order = 4, value = "Document publication date.")
	public String getDate() {
		return this.date;
	}

	/**
	 * Get document edition.
	 * 
	 * @return Document edition
	 */
	@ReportableProperty(order = 3, value = "Document edition or version.")
	public String getEdition() {
		return this.edition;
	}

	/**
	 * Get document identifiers.
	 * 
	 * @return List of document identifiers
	 */
	@ReportableProperty(order = 6, value = "List of document formal identifiers.")
	public List<I8R> getIdentifiers() {
		return this.identifiers;
	}

	/**
	 * Get document intention
	 * 
	 * @return Document intention
	 */
	@ReportableProperty(order = 8, value = "Document intention.")
	public Intention getIntention() {
		return this.intention;
	}

	/**
	 * Get document informative note.
	 * 
	 * @return Document informative note
	 */
	@ReportableProperty(order = 9, value = "Document informative note.")
	public String getNote() {
		return this.note;
	}

	/**
	 * Get document publisher(s).
	 * 
	 * @return Document publisher(s)
	 */
	@ReportableProperty(order = 5, value = "Document publisher or publishers.")
	public String getPublisher() {
		return this.publisher;
	}

	/**
	 * Get document title.
	 * 
	 * @return Document title
	 */
	@ReportableProperty(order = 2, value = "Document title.")
	public String getTitle() {
		return this.title;
	}

	/**
	 * Get document scope.
	 * 
	 * @return document scope
	 */
	@ReportableProperty(order = 7, value = "Document scope.")
	public Type getType() {
		return this.type;
	}

	/**
	 * Set document author(s).
	 * 
	 * @param author
	 *            Document author(2)
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * Set document publication date.
	 * 
	 * @param date
	 *            Document publication date
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * Set document edition.
	 * 
	 * @param edition
	 *            Document edition
	 */
	public void setEdition(String edition) {
		this.edition = edition;
	}

	/**
	 * Add document identifier.
	 * 
	 * @param identifier
	 *            Document identifier
	 */
	public void addIdentifier(I8R identifier) {
		this.identifiers.add(identifier);
	}

	/**
	 * Add document identifiers.
	 * 
	 * @param identifiers
	 *            List of Document identifiers
	 */
	public void setIdentifiers(List<I8R> identifiers) {
		this.identifiers = identifiers;
	}

	/**
	 * Set document informative note.
	 * 
	 * @param note
	 *            Document informative note
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * Set document publisher(s).
	 * 
	 * @param publisher
	 *            Document publisher(s)
	 */
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
}
