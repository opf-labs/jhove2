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
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.SortedSet;

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

    public static final String DOCTYPENOTDETERMINED = "Doctype not determined";
    
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
    
    /** parser directive -- should sgmlnorm be run in order to extract doctype statement; default is false */
    protected boolean findDoctype;
    
    protected String docTypeString = DOCTYPENOTDETERMINED;
    
    protected ESISCommandsParser grammarParser;
    
    protected String sgmFilePath;
    
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
        this.validity = Validity.Undetermined;
        File sgmFile = source.getFile();
		try {
			this.sgmFilePath = sgmFile.getCanonicalPath();
		} catch (IOException e) {
			throw new JHOVE2Exception(
					"IO Exception thrown attempting to determine canonical path for SGML source",
					e);
		}
        OpenSpWrapper sp = new OpenSpWrapper();
        this.grammarParser = sp.parseFile(this);
        if (this.isFindDoctype()){
        	this.docTypeString = sp.createDoctype(this.sgmFilePath);
        }
        this.validity = this.validate(jhove2, source);
		return 0;
	}


	/* (non-Javadoc)
	 * @see org.jhove2.module.format.Validator#validate(org.jhove2.core.JHOVE2, org.jhove2.core.source.Source)
	 */
	@Override
	public Validity validate(JHOVE2 jhove2, Source source)
	throws JHOVE2Exception {
		Validity validity;
        if (this.isSgmlValid()){
        	validity = Validity.True;
        }
        else {
        	validity = Validity.False;
        }
		return validity;
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
	@ReportableProperty(order = 30, value = "Indicates if SGML instance conforms to its DTD")
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

	/**
	 * @return the findDoctype
	 */
	@ReportableProperty(order = 25, value = "Parser setting:  Run normalizer to construct DOCTYPE statement")
	public boolean isFindDoctype() {
		return findDoctype;
	}

	/**
	 * @param findDoctype the findDoctype to set
	 */
	public void setFindDoctype(boolean findDoctype) {
		this.findDoctype = findDoctype;
	}


	/**
	 * @return the source
	 */
	public Source getSource() {
		return source;
	}

	/**
	 * @return the docTypeString
	 */
	@ReportableProperty(order = 31, value = "SGML Doctype string, returns 'NOT determined' if not found or not requested")
	public String getDocTypeString() {
		return docTypeString;
	}

	/**
	 * @return the rootElementName
	 */
	@ReportableProperty(order = 32, value = "Element name for first element in SGML document")
	public String getRootElementName() {
		return this.grammarParser.rootElementName;
	}

	/**
	 * @return the isSgmlValid
	 */
	@ReportableProperty(order = 33, value = "SGML document conforms to its DTD")
	public boolean isSgmlValid() {
		return this.grammarParser.isSgmlValid;
	}

	/**
	 * @return the publicIdCount
	 */
	@ReportableProperty(order = 34, value = "Count of public identifiers associated with notations or external, text, or subdoc entities, ")
	public int getPublicIdCount() {
		return this.grammarParser.publicIdCount;
	}

	/**
	 * @return the fileNamesCount
	 */
	public int getFileNamesCount() {
		return this.grammarParser.fileNamesCount;
	}

	/**
	 * @return the sysidsCount
	 */
	public int getSysidsCount() {
		return this.grammarParser.sysidsCount;
	}

	/**
	 * @return the extTextEntCount
	 */
	public int getExtTextEntCount() {
		return this.grammarParser.extTextEntCount;
	}

	/**
	 * @return the notatDefCount
	 */
	public int getNotatDefCount() {
		return this.grammarParser.notatDefCount;
	}

	/**
	 * @return the extDataEntCount
	 */
	public int getExtDataEntCount() {
		return this.grammarParser.extDataEntCount;
	}

	/**
	 * @return the entrefCount
	 */
	public int getEntrefCount() {
		return this.grammarParser.entrefCount;
	}

	/**
	 * @return the intDataEntCount
	 */
	public int getIntDataEntCount() {
		return this.grammarParser.intDataEntCount;
	}

	/**
	 * @return the subDocEntityDefCount
	 */
	public int getSubDocEntityDefCount() {
		return this.grammarParser.subDocEntityDefCount;
	}

	/**
	 * @return the subDocCommandCount
	 */
	public int getSubDocCommandCount() {
		return this.grammarParser.subDocCommandCount;
	}

	/**
	 * @return the omitCommandCount
	 */
	public int getOmitCommandCount() {
		return this.grammarParser.omitCommandCount;
	}

	/**
	 * @return the elementAttributeCount
	 */
	public int getElementAttributeCount() {
		return this.grammarParser.elementAttributeCount;
	}

	/**
	 * @return the dataAttrCount
	 */
	public int getDataAttrCount() {
		return this.grammarParser.dataAttrCount;
	}

	/**
	 * @return the linkAttrCount
	 */
	public int getLinkAttrCount() {
		return this.grammarParser.linkAttrCount;
	}

	/**
	 * @return the elementCount
	 */
	public int getElementCount() {
		return this.grammarParser.elementCount;
	}

	/**
	 * @return the dataCount
	 */
	public int getDataCount() {
		return this.grammarParser.dataCount;
	}

	/**
	 * @return the includedSubElementsCount
	 */
	public int getIncludedSubElementsCount() {
		return this.grammarParser.includedSubElementsCount;
	}

	/**
	 * @return the emptyElementsCount
	 */
	public int getEmptyElementsCount() {
		return this.grammarParser.emptyElementsCount;
	}

	/**
	 * @return the commentsCount
	 */
	public int getCommentsCount() {
		return this.grammarParser.commentsCount;
	}

	/**
	 * @return the sDataCount
	 */
	public int getsDataCount() {
		return this.grammarParser.sDataCount;
	}

	/**
	 * @return the piCount
	 */
	public int getPiCount() {
		return this.grammarParser.piCount;
	}

	/**
	 * @return the appInfo
	 */
	public int getAppInfoCount() {
		return this.grammarParser.appInfoCount;
	}

	/**
	 * @return the pubIds
	 */
	public SortedSet<String> getPubIds() {
		return this.grammarParser.pubIds;
	}

	/**
	 * @return the extEntFileNames
	 */
	public SortedSet<String> getExtEntFileNames() {
		return this.grammarParser.extEntFileNames;
	}

	/**
	 * @return the extEntSysidNames
	 */
	public SortedSet<String> getExtEntSysidNames() {
		return this.grammarParser.extEntSysidNames;
	}

	/**
	 * @return the extTextEntNames
	 */
	public SortedSet<String> getExtTextEntNames() {
		return this.grammarParser.extTextEntNames;
	}

	/**
	 * @return the notatNames
	 */
	public SortedSet<String> getNotatNames() {
		return this.grammarParser.notatNames;
	}

	/**
	 * @return the extDataEntNames
	 */
	public SortedSet<String> getExtDataEntNames() {
		return this.grammarParser.extDataEntNames;
	}

	/**
	 * @return the entRefNames
	 */
	public SortedSet<String> getEntRefNames() {
		return this.grammarParser.entRefNames;
	}

	/**
	 * @return the subDocEntDefNames
	 */
	public SortedSet<String> getSubDocEntDefNames() {
		return this.grammarParser.subDocEntDefNames;
	}

	/**
	 * @return the subDocCommandNames
	 */
	public SortedSet<String> getSubDocCommandNames() {
		return this.grammarParser.subDocCommandNames;
	}

	/**
	 * @return the elementNames
	 */
	public SortedSet<String> getElementNames() {
		return this.grammarParser.elementNames;
	}

	/**
	 * @return the sdataNames
	 */
	public SortedSet<String> getSdataNames() {
		return this.grammarParser.sdataNames;
	}

	/**
	 * @return the intEnt2Value
	 */
	public HashMap<String, String> getIntEnt2Value() {
		return this.grammarParser.intEnt2Value;
	}

	/**
	 * @return the intEnt2Type
	 */
	public HashMap<String, String> getIntEnt2Type() {
		return this.grammarParser.intEnt2Type;
	}

	/**
	 * @return the elemAttributeType2Count
	 */
	public HashMap<String, Integer> getElemAttributeType2Count() {
		return this.grammarParser.elemAttributeType2Count;
	}

	/**
	 * @return the dataAttributeType2Count
	 */
	public HashMap<String, Integer> getDataAttributeType2Count() {
		return this.grammarParser.dataAttributeType2Count;
	}

	/**
	 * @return the linkAttributeType2Count
	 */
	public HashMap<String, Integer> getLinkAttributeType2Count() {
		return this.grammarParser.linkAttributeType2Count;
	}

	/**
	 * @return the extEntName2dataAttrNames
	 */
	public HashMap<String, List<String>> getExtEntName2dataAttrNames() {
		return this.grammarParser.extEntName2dataAttrNames;
	}

	/**
	 * @return the progInstructions
	 */
	public List<String> getProgInstructions() {
		return this.grammarParser.progInstructions;
	}

	/**
	 * @return the appInfos
	 */
	public List<String> getAppInfos() {
		return this.grammarParser.appInfos;
	}

}
