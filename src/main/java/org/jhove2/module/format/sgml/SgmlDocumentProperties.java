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
package org.jhove2.module.format.sgml;

import java.util.List;
//import java.util.Map;
import java.util.SortedSet;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.reportable.AbstractReportable;

import com.sleepycat.persist.model.Persistent;


/**
 * Thin container for SGML document properties extracted by parser engine
 * Allows us to free parser objects
 * @author smorrissey
 *
 */
@Persistent
public class SgmlDocumentProperties extends AbstractReportable {
    protected boolean isSgmlValid; // is this a conforming SGML file; indicated by presence of "C" command at end of file    
    protected boolean foundDoctype;
    protected boolean foundPubid;
    protected boolean foundSysid;
    protected String pubid;
    protected String systemId;
    protected String rootElementName; // element name of first startElementCommand
    protected int publicIdCount;
    protected int fileNamesCount;
    protected int sysidsCount;
    protected int extTextEntCount;
    protected int notatDefCount;
    protected int extDataEntCount;
    protected int entrefCount;
    protected int intDataEntCount;
    protected int subDocEntityDefCount;
    protected int subDocCommandCount;
    protected int omitCommandCount;
    protected int elementAttributeCount;
    protected int dataAttrCount;
    protected int linkAttrCount;
    protected int elementCount;  // number of start element commands (same element can occur more than once)
    protected int dataCount;
    protected int includedSubElementsCount;
    protected int emptyElementsCount;
    protected int commentsCount;
    protected int sDataCount;
    protected int piCount;
    protected int appInfoCount;
    protected int eLevelMessageCount;
    protected int wLevelMessageCount;
    protected int iLevelMessageCount;
    protected int xLevelMessageCount;
    protected int qLevelMessageCount;
    protected int totMessageCount;

    protected SortedSet<String> entRefNames;
    protected SortedSet<String> elementNames;      
    protected SortedSet<String> sdataNames;
;
    protected List<String> sgmlParserMessages;
    protected List<String> sgmlParserConfigSettings;
    protected List<String> parseErrors;

    /**
     * Constructor
     */
    public SgmlDocumentProperties(){
    	super();
    }
	/**
	 * @return boolean indicating if DOCTYPE statement was determines
	 */
	@ReportableProperty(order = 31, value = "Was doctype statement determined")
	public boolean getDocTypeFound() {
		return this.foundDoctype;                     
	}
	/**
	 * @return the rootElementName
	 */
	@ReportableProperty(order = 32, value = "Element name for first element in SGML document")
	public String getRootElementName() {
		return this.rootElementName;
	}

	/**
	 * @return the isSgmlValid
	 */
	@ReportableProperty(order = 33, value = "SGML document conforms to its DTD")
	public boolean isSgmlValid() {
		return this.isSgmlValid;
	}

	/**
	 * @return the publicIdCount
	 */
	@ReportableProperty(order = 34, 
			value = "Count of public identifiers associated with notations or external, text, or subdoc entities ")
	public int getPublicIdCount() {
		return this.publicIdCount;
	}

	/**
	 * @return the fileNamesCount
	 */
	@ReportableProperty(order = 34, 
			value = "Count of system (file) identifiers associated with notations or external, text, or subdoc entities, generated by the entity manager from the specified external identifier and other information about the entity or notation")
	public int getEntityFileNamesCount() {
		return this.fileNamesCount;
	}

	/**
	 * @return the sysidsCount
	 */
	@ReportableProperty(order = 35, 
			value = "Count of system identifiers associated with notations or external, text, or subdoc entities")
	public int getSysidsCount() {
		return this.sysidsCount;
	}

	/**
	 * @return the extTextEntCount
	 */
	@ReportableProperty(order = 36, 
			value = "Count of external SGML text entities")
	public int getExtTextEntCount() {
		return this.extTextEntCount;
	}

	/**
	 * @return the notatDefCount
	 */
	@ReportableProperty(order = 36, 
			value = "Count of notation names")
	public int getNotatDefCount() {
		return this.notatDefCount;
	}

	/**
	 * @return the extDataEntCount
	 */
	@ReportableProperty(order = 37, 
			value = "Count of  external data entity definitions")
	public int getExtDataEntCount() {
		return this.extDataEntCount;
	}

	/**
	 * @return the entrefCount
	 */
	@ReportableProperty(order = 37, 
			value = "Count of external data entity references")
	public int getEntrefCount() {
		return this.entrefCount;
	}

	/**
	 * @return the intDataEntCount
	 */
	@ReportableProperty(order = 38, 
			value = "Count of internal data entity definitions")
	public int getIntDataEntCount() {
		return this.intDataEntCount;
	}

	/**
	 * @return the subDocEntityDefCount
	 */
	@ReportableProperty(order = 38, 
			value = "Count of sub-document entity definitions")
	public int subDocEntityDefCount() {
		return this.subDocEntityDefCount;
	}

	/**
	 * @return the subDocCommandCount
	 */
	@ReportableProperty(order = 38, 
			value = "Count of SGML subdocument entities")
	public int getSubDocCommandCount() {
		return this.subDocCommandCount;
	}

	/**
	 * @return the omitCommandCount
	 */
	@ReportableProperty(order = 39, 
			value = "Count of omitted start-tag, end-tag, or attribue markup instances")
	public int getOmitCommandCount() {
		return this.omitCommandCount;
	}

	/**
	 * @return the elementAttributeCount
	 */
	@ReportableProperty(order = 40, 
			value = "Count of attributes associated with elements")
	public int getElementAttributeCount() {
		return this.elementAttributeCount;
	}

	/**
	 * @return the dataAttrCount
	 */
	@ReportableProperty(order = 41, 
			value = "Count of data attributes for a external entities")
	public int getDataAttrCount() {
		return this.dataAttrCount;
	}

	/**
	 * @return the linkAttrCount
	 */
	@ReportableProperty(order = 41, 
			value = "Count of link attributes associated with elements")
	public int getLinkAttrCount() {
		return this.linkAttrCount;
	}

	/**
	 * @return the elementCount
	 */
	@ReportableProperty(order = 42, 
			value = "Count of elements in SGML document")
	public int getElementCount() {
		return this.elementCount;
	}

	/**
	 * @return the dataCount
	 */
	@ReportableProperty(order = 43, 
			value = "Count of element data content")
	public int getDataCount() {
		return this.dataCount;
	}

	/**
	 * @return the includedSubElementsCount
	 */
	@ReportableProperty(order = 44, 
			value = "Count of included subelements")
	public int getIncludedSubElementsCount() {
		return this.includedSubElementsCount;
	}

	/**
	 * @return the emptyElementsCount
	 */
	@ReportableProperty(order = 45, 
			value = "Count of empty elements")
	public int getEmptyElementsCount() {
		return this.emptyElementsCount;
	}

	/**
	 * @return the commentsCount
	 */
	@ReportableProperty(order = 46, 
			value = "Count of comments")
	public int getCommentsCount() {
		return this.commentsCount;
	}

	/**
	 * @return the sDataCount
	 */
	@ReportableProperty(order = 47, 
			value = "Count of internal SDATA entities")
	public int getSDataCount() {
		return this.sDataCount;
	}

	/**
	 * @return the processing instruction count
	 */
	@ReportableProperty(order = 48, 
			value = "Count of processing instructions")
	public int getProcessingInstructionsCount() {
		return this.piCount;
	}

	/**
	 * @return the appInfo count
	 */
	@ReportableProperty(order = 48, 
			value = "Count of APPINFO declared in DTD, and appearing in the text")
	public int getAppInfoCount() {
		return this.appInfoCount;
	}
	/**
	 * @return the entRefNames
	 */
	@ReportableProperty(order = 55, 
			value = "Names of external data entities referenced in SGML document")
	public SortedSet<String> getEntRefNames() {
		return this.entRefNames;
	}

	/**
	 * @return the elementNames
	 */
	@ReportableProperty(order = 57, 
			value = "Unique names of elements appearing in SGML document")
	public SortedSet<String> getElementNames() {
		return this.elementNames;
	}

	/**
	 * @return the sdataNames
	 */
	@ReportableProperty(order = 58, 
			value = "Unique SDATA entity names for SDATA entities appearing in SGML document")
	public SortedSet<String> getSdataNames() {
		return this.sdataNames;
	}
	/**
	 * @return list of errors from parse of SGML file
	 */
	@ReportableProperty(order = 67, 
			value = "List of parse errors encountered when parsing file")
	public List<String> getParseErrors(){
		return this.parseErrors;
	}
	
	/**
	 * Return public identifier for SGML document
	 * @return public identifier for SGML document if found, else null
	 */
	@ReportableProperty(order = 68, 
			value = "Public identifier for SGML document")
	public String getPublicIdentifier() {
		return this.pubid;
	}

	/**
	 * Return boolean indicator whether public identifier for SGML document was found
	 * @return true if public identifier found, else false
	 */
	@ReportableProperty(order = 69, 
			value = "Indicates if public identifier for SGML document was found")
	public boolean getPublicIdentifierFound() {
		return this.foundPubid;
	}

	/**
	 * Return system identifier for SGML document
	 * @return system identifier for SGML document if found, else null
	 */
	@ReportableProperty(order = 70, 
			value = "System identifier for SGML document")
	public String getSystemIdentifier() {
		return this.systemId;
	}

	/**
	 * Return boolean indicator whether system identifier for SGML document was found
	 * @return true if system identifier found, else false
	 */
	@ReportableProperty(order = 71, 
			value = "Indicates if system identifier for SGML document was found")
	public boolean getSystemIdentifierFound() {
		return this.foundSysid;
	}
	
	/**
	 * Return list of error, warning, info or other validation messages returned by SGML parser
	 * @return List of messages
	 */
	@ReportableProperty(order = 72, 
			value = "List of error, warning, info or other validation messages returned by SGML parser")
	public List<String> getSgmlParserMessages() {
		return sgmlParserMessages;
	}
	/**
	 * Return count of error-level validation messages returned by SGML parser
	 * @return count of error-level validation messages
	 */
	@ReportableProperty(order = 73, 
			value = "Count of error-level validation messages returned by SGML parser")
	public int getErrorLevelMessageCount() {
		return eLevelMessageCount;
	}
	/**
	 * Return count of warning-level validation messages returned by SGML parser
	 * @return count of warning level validation messages
	 */
	@ReportableProperty(order = 74, 
			value = "Count of warning-level validation messages returned by SGML parser")
	public int getWarningLevelMessageCount() {
		return wLevelMessageCount;
	}
	/**
	 * Return count of info-level validation messages returned by SGML parser
	 * @return count of info level validation messages
	 */
	@ReportableProperty(order = 75, 
			value = "Count of info-level validation messages returned by SGML parser")
	public int getInfoLevelMessageCount() {
		return iLevelMessageCount;
	}
	/**
	 * Return count of cross-ref validation messages returned by SGML parser
	 * @return count of cross-ref validation messages
	 */
	@ReportableProperty(order = 76, 
			value = "Count of cross-ref validation messages returned by SGML parser")
	public int getXrefLevelMessageCount() {
		return xLevelMessageCount;
	}
	/**
	 * Return count of quantity (name-length) validation messages returned by SGML parser
	 * @return count of quantity (name-length) validation messages
	 */
	@ReportableProperty(order = 77, 
			value = "Count of quantity (name-length) validation messages returned by SGML parser")
	public int getQnameLevelMessageCount() {
		return qLevelMessageCount;
	}
	/**
	 * Return count of all validation messages returned by SGML parser
	 * @return count of all validation messages
	 */
	@ReportableProperty(order = 78, 
			value = "Count of all validation messages returned by SGML parser")
	public int getTotMessageCount() {
		return totMessageCount;
	}
	
	/**
	 * Returns List of configuration options for SGML parser(s
	 * @return the list of configuration options for SGML parser(s)
	 */
	@ReportableProperty(order = 79, 
			value = "List of all SGML parser configuration settings")
	public List<String> getSgmlParserConfigSettings() {
		return sgmlParserConfigSettings;
	}
	/**
	 * @param isSgmlValid the isSgmlValid to set
	 */
	public void setSgmlValid(boolean isSgmlValid) {
		this.isSgmlValid = isSgmlValid;
	}
	/**
	 * @param rootElementName the rootElementName to set
	 */
	public void setRootElementName(String rootElementName) {
		this.rootElementName = rootElementName;
	}
	/**
	 * @param publicIdCount the publicIdCount to set
	 */
	public void setPublicIdCount(int publicIdCount) {
		this.publicIdCount = publicIdCount;
	}
	/**
	 * @param fileNamesCount the fileNamesCount to set
	 */
	public void setEntityFileNamesCount(int fileNamesCount) {
		this.fileNamesCount = fileNamesCount;
	}
	/**
	 * @param sysidsCount the sysidsCount to set
	 */
	public void setSysidsCount(int sysidsCount) {
		this.sysidsCount = sysidsCount;
	}
	/**
	 * @param extTextEntCount the extTextEntCount to set
	 */
	public void setExtTextEntCount(int extTextEntCount) {
		this.extTextEntCount = extTextEntCount;
	}
	/**
	 * @param notatDefCount the notatDefCount to set
	 */
	public void setNotatDefCount(int notatDefCount) {
		this.notatDefCount = notatDefCount;
	}
	/**
	 * @param extDataEntCount the extDataEntCount to set
	 */
	public void setExtDataEntCount(int extDataEntCount) {
		this.extDataEntCount = extDataEntCount;
	}
	/**
	 * @param entrefCount the entrefCount to set
	 */
	public void setEntrefCount(int entrefCount) {
		this.entrefCount = entrefCount;
	}
	/**
	 * @param intDataEntCount the intDataEntCount to set
	 */
	public void setIntDataEntCount(int intDataEntCount) {
		this.intDataEntCount = intDataEntCount;
	}
	/**
	 * @param subDocEntityDefCount the subDocEntityDefCount to set
	 */
	public void setSubDocEntityDefCount(int subDocEntityDefCount) {
		this.subDocEntityDefCount = subDocEntityDefCount;
	}
	/**
	 * @param subDocCommandCount the subDocCommandCount to set
	 */
	public void setSubDocCommandCount(int subDocCommandCount) {
		this.subDocCommandCount = subDocCommandCount;
	}
	/**
	 * @param omitCommandCount the omitCommandCount to set
	 */
	public void setOmitCommandCount(int omitCommandCount) {
		this.omitCommandCount = omitCommandCount;
	}
	/**
	 * @param elementAttributeCount the elementAttributeCount to set
	 */
	public void setElementAttributeCount(int elementAttributeCount) {
		this.elementAttributeCount = elementAttributeCount;
	}
	/**
	 * @param dataAttrCount the dataAttrCount to set
	 */
	public void setDataAttrCount(int dataAttrCount) {
		this.dataAttrCount = dataAttrCount;
	}
	/**
	 * @param linkAttrCount the linkAttrCount to set
	 */
	public void setLinkAttrCount(int linkAttrCount) {
		this.linkAttrCount = linkAttrCount;
	}
	/**
	 * @param elementCount the elementCount to set
	 */
	public void setElementCount(int elementCount) {
		this.elementCount = elementCount;
	}
	/**
	 * @param dataCount the dataCount to set
	 */
	public void setDataCount(int dataCount) {
		this.dataCount = dataCount;
	}
	/**
	 * @param includedSubElementsCount the includedSubElementsCount to set
	 */
	public void setIncludedSubElementsCount(int includedSubElementsCount) {
		this.includedSubElementsCount = includedSubElementsCount;
	}
	/**
	 * @param emptyElementsCount the emptyElementsCount to set
	 */
	public void setEmptyElementsCount(int emptyElementsCount) {
		this.emptyElementsCount = emptyElementsCount;
	}
	/**
	 * @param commentsCount the commentsCount to set
	 */
	public void setCommentsCount(int commentsCount) {
		this.commentsCount = commentsCount;
	}
	/**
	 * @param sDataCount the sDataCount to set
	 */
	public void setsDataCount(int sDataCount) {
		this.sDataCount = sDataCount;
	}
	/**
	 * @param piCount the piCount to set
	 */
	public void setProcessingInstructionsCount(int piCount) {
		this.piCount = piCount;
	}
	/**
	 * @param appInfoCount the appInfoCount to set
	 */
	public void setAppInfoCount(int appInfoCount) {
		this.appInfoCount = appInfoCount;
	}
	/**
	 * @param entRefNames the entRefNames to set
	 */
	public void setEntRefNames(SortedSet<String> entRefNames) {
		this.entRefNames = entRefNames;
	}
	/**
	 * @param elementNames the elementNames to set
	 */
	public void setElementNames(SortedSet<String> elementNames) {
		this.elementNames = elementNames;
	}
	/**
	 * @param sdataNames the sdataNames to set
	 */
	public void setSdataNames(SortedSet<String> sdataNames) {
		this.sdataNames = sdataNames;
	}
	/**
	 * @param parseErrors the parseErrors to set
	 */
	public void setParseErrors(List<String> parseErrors) {
		this.parseErrors = parseErrors;
	}
	/**
	 * @param foundDoctype the foundDoctype to set
	 */
	public void setFoundDoctype(boolean foundDoctype) {
		this.foundDoctype = foundDoctype;
	}
	/**
	 * @param foundPubid the foundPubid to set
	 */
	public void setFoundPubid(boolean foundPubid) {
		this.foundPubid = foundPubid;
	}
	/**
	 * @param foundSysid the foundSysid to set
	 */
	public void setFoundSysid(boolean foundSysid) {
		this.foundSysid = foundSysid;
	}
	/**
	 * @param pubid the pubid to set
	 */
	public void setPublicIdentifier(String pubid) {
		this.pubid = pubid;
	}
	/**
	 * @param systemId the systemId to set
	 */
	public void setSystemIdentifier(String systemId) {
		this.systemId = systemId;
	}
	/**
	 * @param sgmlParserMessages the sgmlParserMessages to set
	 */
	public void setSgmlParserMessages(List<String> sgmlParserMessages) {
		this.sgmlParserMessages = sgmlParserMessages;
	}
	/**
	 * @param eLevelMessageCount the eLevelMessageCount to set
	 */
	public void setErrorLevelMessageCount(int eLevelMessageCount) {
		this.eLevelMessageCount = eLevelMessageCount;
	}
	/**
	 * @param wLevelMessageCount the wLevelMessageCount to set
	 */
	public void setWarningLevelMessageCount(int wLevelMessageCount) {
		this.wLevelMessageCount = wLevelMessageCount;
	}
	/**
	 * @param iLevelMessageCount the iLevelMessageCount to set
	 */
	public void setInfoLevelMessageCount(int iLevelMessageCount) {
		this.iLevelMessageCount = iLevelMessageCount;
	}
	/**
	 * @param xLevelMessageCount the xLevelMessageCount to set
	 */
	public void setXrefLevelMessageCount(int xLevelMessageCount) {
		this.xLevelMessageCount = xLevelMessageCount;
	}
	/**
	 * @param qLevelMessageCount the qLevelMessageCount to set
	 */
	public void setQnameLevelMessageCount(int qLevelMessageCount) {
		this.qLevelMessageCount = qLevelMessageCount;
	}
	/**
	 * @param totMessageCount the totMessageCount to set
	 */
	public void setTotMessageCount(int totMessageCount) {
		this.totMessageCount = totMessageCount;
	}
	/**
	 * @param sgmlParserConfigSettings the sgmlParserConfigSettings to set
	 */
	public void setSgmlParserConfigSettings(List<String> sgmlParserConfigSettings) {
		this.sgmlParserConfigSettings = sgmlParserConfigSettings;
	}

}
