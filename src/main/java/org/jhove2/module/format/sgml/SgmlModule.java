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
import org.jhove2.core.format.Format;
import org.jhove2.core.source.Source;
import org.jhove2.module.format.BaseFormatModule;
import org.jhove2.module.format.Validator;

/**
 * JHOVE2 SGML module.  This module will parse and validate an SGML document instance,
 *  and capture selected characterization information.
 *  
 * @author smorrissey
 */
public class SgmlModule extends BaseFormatModule implements Validator {
	/** Directory module version identifier. */
	public static final String VERSION = "1.9.6";

	/** Directory module release date. */
	public static final String RELEASE = "2010-05-19";

	/** Directory module rights statement. */
	public static final String RIGHTS = "Copyright 2010 by Ithaka Harbors, Inc."
		+ "Available under the terms of the BSD license.";
	/** Module validation coverage. */
	public static final Coverage COVERAGE = Coverage.Inclusive;
   
	/** SGML validation status. */
	protected Validity validity;

    /** The JHOVE2 object passed in by the parse method */
    protected JHOVE2 jhove2; 
    
    /** The Source object passed in by the parse method */
    protected  Source source;
   
    /** parser directive -- should sgmlnorm be run in order to extract doctype statement; default is false */
    protected boolean shouldFindDoctype;
    
    /** Parser engine for parsing SGML files and extracting significant properties */
    protected SgmlParser sgmlParser;

    /** Container for SGML document properties extracted by parser */
    protected SgmlDocumentProperties documentProperties;

	/**
     * Instantiates a new SgmlModule instance.
     * 
     * @param format
     *            the Format object
     */
    public SgmlModule(Format format) {
        super(VERSION, RELEASE, RIGHTS, format);
    }
    
	/** Parse the format.
	 * @param jhove2 JHOVE2 framework
	 * @param source Source unit
	 */
	@Override
	public long parse(JHOVE2 jhove2, Source source)
	throws EOFException, IOException, JHOVE2Exception
	{
        this.jhove2 = jhove2;
        this.source = source;
        this.validity = Validity.Undetermined;
        this.documentProperties = sgmlParser.parseFile(this);
        if (this.isShouldFindDoctype()){
        	sgmlParser.determineDoctype(this);
        }    
        this.jhove2 = null;
        this.source = null;
        this.sgmlParser.cleanUp();
        this.sgmlParser = null;
		return 0;
	}


	/* (non-Javadoc)
	 * @see org.jhove2.module.format.Validator#validate(org.jhove2.core.JHOVE2, org.jhove2.core.source.Source)
	 * There are no profiles of the SGML format; this method will return the validation status of the SGML document
	 */
	@Override
	public Validity validate(JHOVE2 jhove2, Source source)
	throws JHOVE2Exception {
		 if (this.getDocumentProperties() != null && this.getDocumentProperties().isSgmlValid()){
	        	this.validity = Validity.True;
	        }
	        else {
	        	this.validity = Validity.False;
	        }
		return this.validity;
	}

	/* (non-Javadoc)
	 * @see org.jhove2.module.format.Validator#getCoverage()
	 */
	@Override
	public Coverage getCoverage() {
		return COVERAGE;
	}

	/* (non-Javadoc)
	 * @see org.jhove2.module.format.Validator#isValid()
	 */
	@Override
	public Validity isValid() {
		if (validity == null) {
			try {
				validate(jhove2, source);
			}
			catch (JHOVE2Exception e) {
			}
		}
		return validity;
	}

    /**
	 * @return the sgmlParser
	 */
	public SgmlParser getSgmlParser() {
		return sgmlParser;
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
	 * @return the source
	 */
	public Source getSource() {
		return source;
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

}
