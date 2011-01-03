/**
 * JHOVE2 - Next-generation architecture for format-aware characterization
 * <p>
 * Copyright (c) 2010 by The Regents of the University of California, Ithaka
 * Harbors, Inc., and The Board of Trustees of the Leland Stanford Junior
 * University. All rights reserved.
 * </p>
 * <p>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * </p>
 * <ul>
 * <li>Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.</li>
 * <li>Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.</li>
 * <li>Neither the name of the University of California/California Digital
 * Library, Ithaka Harbors/Portico, or Stanford University, nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.</li>
 * </ul>
 * <p>
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
 * </p>
 */
package org.jhove2.module.format.sgml;

import java.io.EOFException;
import java.io.IOException;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.Message;
import org.jhove2.core.Message.Context;
import org.jhove2.core.Message.Severity;
import org.jhove2.core.format.Format;
import org.jhove2.core.io.Input;
import org.jhove2.core.source.Source;
import org.jhove2.module.format.BaseFormatModule;
import org.jhove2.module.format.Validator;
import org.jhove2.persist.FormatModuleAccessor;

import com.sleepycat.persist.model.NotPersistent;
import com.sleepycat.persist.model.Persistent;

/**
 * JHOVE2 SGML module.  This module will parse and validate an SGML document instance,
 *  and capture selected characterization information.
 *  
 * @author smorrissey
 */
@Persistent
public class SgmlModule
    extends BaseFormatModule
    implements Validator
{
	/** Directory module version identifier. */
	public static final String VERSION = "2.0.0";

	/** Directory module release date. */
	public static final String RELEASE = "2010-09-10";

	/** Directory module rights statement. */
	public static final String RIGHTS = "Copyright 2010 by Ithaka Harbors, Inc."
		+ "Available under the terms of the BSD license.";
	/** Module validation coverage. */
	public static final Coverage COVERAGE = Coverage.Inclusive;

	/** SGML validation status. */
	protected Validity validity;

	/** parser directive -- should sgmlnorm be run in order to extract doctype statement; default is false */
	protected boolean shouldFindDoctype;

	/** Parser engine for parsing SGML files and extracting significant properties */
	@NotPersistent
	protected SgmlParser sgmlParser;

	/** Container for SGML document properties extracted by parser */
	protected SgmlDocumentProperties documentProperties;

	/**
	 * Instantiates a new SgmlModule instance.
	 * 
	 * @param format
	 *            the Format object
	 */
	public SgmlModule(Format format, 
			FormatModuleAccessor formatModuleAccessor) {
		super(VERSION, RELEASE, RIGHTS, format, formatModuleAccessor);
		this.validity = Validity.Undetermined;
	}
	
	/** Error message indicating command shell to invoke external parser is invalid */
	protected Message invalidCommandShellMessage;

	/**
	 * Instantiates a new <code>SgmlModule</code> instance.
	 * 
	 * @param format
	 *            the Format object
	 */
	public SgmlModule(Format format) {
		this(format, null);
	}
	
	/** Instantiate a new <code>SgmlModule</code>. */
	public SgmlModule() {
		this(null, null);
	}

	/** Parse the format.
	 * @param jhove2 JHOVE2 framework
	 * @param source Source unit
	 * @param input  Source input
	 * @return Number of bytes consumed
	 */
	@Override
	public long parse(JHOVE2 jhove2, Source source, Input input)
	    throws EOFException, IOException, JHOVE2Exception
	{
		this.documentProperties = sgmlParser.parseFile(this, jhove2, source);
		if (this.documentProperties != null){
			if (this.isShouldFindDoctype()){
				sgmlParser.determineDoctype(this, jhove2, source);
			}    
		}
		this.sgmlParser.cleanUp();
		this.sgmlParser = null;
		return 0;
	}


	/** Validate the SGML source unit.
	 * @param jhove2 JHOVE2 framework
	 * @param source SGML source unit
	 * @param input  SGML source input
	 * @see org.jhove2.module.format.Validator#validate(org.jhove2.core.JHOVE2, org.jhove2.core.source.Source, org.jhove2.core.io.Input)
	 * There are no profiles of the SGML format; this method will return the validation status of the SGML document
	 */
	@Override
	public Validity validate(JHOVE2 jhove2, Source source, Input input)
	    throws JHOVE2Exception
	{
		if (this.getDocumentProperties() != null){
			if (this.getDocumentProperties().getParseErrors() != null &&
				    this.getDocumentProperties().getParseErrors().size() > 0){
					this.validity = Validity.Undetermined;
					Message message = new Message(
							Severity.ERROR,
							Context.PROCESS,
							"org.jhove2.module.format.sgml.SgmlModule.OpenSpParseErrorsDetected",							
							jhove2.getConfigInfo());
					source=source.addMessage(message);
				}
			else {
				if (this.getDocumentProperties().isSgmlValid()){
					this.validity = Validity.True;
			    }
			    else {
				    this.validity = Validity.False;
			    }	
			}			
		}
		return this.validity;
	}

	/** Get validation coverage.
	 * @see org.jhove2.module.format.Validator#getCoverage()
	 */
	@Override
	public Coverage getCoverage() {
		return COVERAGE;
	}

	/** Get validation status.
	 * @see org.jhove2.module.format.Validator#isValid()
	 */
	@Override
	public Validity isValid() {
		return this.validity;
	}

	/**
	 * @return the sgmlParser
	 */
	public SgmlParser getSgmlParser() {
		return this.sgmlParser;
	}

	/**
	 * @param sgmlParser the sgmlParser to set
	 */
	public void setSgmlParser(SgmlParser sgmlParser) {
		this.sgmlParser = sgmlParser;
	}

	/**
	 * @return the findDoctype
	 */
	@ReportableProperty(order = 25, value = "Parser setting:  Run normalizer to construct DOCTYPE statement")
	public boolean isShouldFindDoctype() {
		return shouldFindDoctype;
	}

	/**
	 * @param findDoctype the findDoctype to set
	 */
	public void setShouldFindDoctype(boolean findDoctype) {
		this.shouldFindDoctype = findDoctype;
	}

	/**
	 * @return the documentProperties
	 */
	@ReportableProperty(order = 26, value = "SGML document properties")
	public SgmlDocumentProperties getDocumentProperties() {
		return documentProperties;
	}

	/**
	 * @param documentProperties the documentProperties to set
	 */
	public void setDocumentProperties(SgmlDocumentProperties documentProperties) {
		this.documentProperties = documentProperties;
	}

	/**
	 * @return the invalidCommandShellMessage
	 */
	@ReportableProperty(order = 27, value = "Invalid Command Shell configuration Error Message")
	public Message getInvalidCommandShellMessage() {
		return invalidCommandShellMessage;
	}

	/**
	 * @param invalidCommandShellMessage the invalidCommandShellMessage to set
	 */
	public void setInvalidCommandShellMessage(Message invalidCommandShellMessage) {
		this.invalidCommandShellMessage = invalidCommandShellMessage;
	}

}
