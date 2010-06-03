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
    

    /** parser directive: path to catalog file; if empty or null, no catalog is assumed */
    protected String catalogPath;
    
    /** parser directive: max number of errors detected after which parser halts; default is 200; 0 means unlimited
        if < 0, will use onsgmls default */
    protected int maxErrors = -1; 
    
    /** parser directive: Show open entities in error messages */
    protected boolean showOpenEntitiesInErrMsg;  
    
    /** parser directive: Show open elements in error messages */
    protected boolean showOpenElementsInErrMsg;
    
    /** parser directive: include error number in error messages */
    protected boolean showErrNumberInErrMsg;
    
    
    /**
     * Instantiates a new XmlModule instance.
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
		return 0;
	}


	/* (non-Javadoc)
	 * @see org.jhove2.module.format.Validator#validate(org.jhove2.core.JHOVE2, org.jhove2.core.source.Source)
	 */
	@Override
	public Validity validate(JHOVE2 jhove2, Source source)
	throws JHOVE2Exception {
		// TODO Auto-generated method stub
		return null;
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
	 * @return the catalogPath
	 */
	@ReportableProperty(order = 20, value = "Full path to catalog file")
	public String getCatalogPath() {
		return catalogPath;
	}


	/**
	 * @param catalogPath the catalogPath to set
	 */
	public void setCatalogPath(String catalogPath) {
		this.catalogPath = catalogPath;
	}


	/**
	 * @return the maxErrors
	 */
	@ReportableProperty(order = 21, value = "Parser setting:  Error limit before abandoning parse")
	public int getMaxErrors() {
		return maxErrors;
	}


	/**
	 * @param maxErrors the maxErrors to set
	 */
	public void setMaxErrors(int maxErrors) {
		this.maxErrors = maxErrors;
	}


	/**
	 * @return the showOpenEntitiesInErrMsg
	 */
	@ReportableProperty(order = 22, value = "Parser setting:  Show open entities in error messages")
	public boolean isShowOpenEntitiesInErrMsg() {
		return showOpenEntitiesInErrMsg;
	}


	/**
	 * @param showOpenEntitiesInErrMsg the showOpenEntitiesInErrMsg to set
	 */
	public void setShowOpenEntitiesInErrMsg(boolean showOpenEntitiesInErrMsg) {
		this.showOpenEntitiesInErrMsg = showOpenEntitiesInErrMsg;
	}


	/**
	 * @return the showOpenElementsInErrMsg
	 */
	@ReportableProperty(order = 23, value = "Parser setting:  Show open elements in error messages")
	public boolean isShowOpenElementsInErrMsg() {
		return showOpenElementsInErrMsg;
	}


	/**
	 * @param showOpenElementsInErrMsg the showOpenElementsInErrMsg to set
	 */
	public void setShowOpenElementsInErrMsg(boolean showOpenElementsInErrMsg) {
		this.showOpenElementsInErrMsg = showOpenElementsInErrMsg;
	}


	/**
	 * @return the showErrNumberInErrMsg
	 */
	@ReportableProperty(order = 22, value = "Parser setting:  Show error message number in error messages")
	public boolean isShowErrNumberInErrMsg() {
		return showErrNumberInErrMsg;
	}


	/**
	 * @param showErrNumberInErrMsg the showErrNumberInErrMsg to set
	 */
	public void setShowErrNumberInErrMsg(boolean showErrNumberInErrMsg) {
		this.showErrNumberInErrMsg = showErrNumberInErrMsg;
	}

}
