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
import java.util.Map;
import java.util.SortedSet;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.reportable.AbstractReportable;


/**
 * Thin container for SGML document properties extracted by parser engine
 * Allows us to free parser objects
 * @author smorrissey
 *
 */
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

    protected SortedSet<String> pubIds;
    protected SortedSet<String> extEntFileNames;
    protected SortedSet<String> extEntSysidNames;
    protected SortedSet<String> extTextEntNames;
    protected SortedSet<String> notatNames;
    protected SortedSet<String> extDataEntNames;
    protected SortedSet<String> entRefNames;
    protected SortedSet<String> subDocEntDefNames;
    protected SortedSet<String> subDocCommandNames;
    protected SortedSet<String> elementNames;      
    protected SortedSet<String> sdataNames;

    protected Map<String, String> intEnt2Value;
    protected Map<String, String> intEnt2Type;

    protected Map<String, Integer> intEntType2Count;
    protected Map<String, Integer> elemAttributeType2Count;
    protected Map<String, Integer> dataAttributeType2Count;
    protected Map<String, Integer> linkAttributeType2Count;

    protected Map<String, List<String>> extEntName2dataAttrNames;

    protected List<String> processingInstructions ;
    protected List<String> appInfos;
    protected List<String> sgmlParserMessages;
    protected  List<String> parseErrors;

    
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
	 * @return the pubIds
	 */
	@ReportableProperty(order = 49, 
			value = "Public identifiers associated with notations or external, text, or subdoc entities")
	public SortedSet<String> getPubIds() {
		return this.pubIds;
	}

	/**
	 * @return the extEntFileNames
	 */
	@ReportableProperty(order = 50, 
			value = "System (file) identifiers associated with notations or external, text, or subdoc entities, generated by the entity manager from the specified external identifier and other information about the entity or notation")
	public SortedSet<String> getExtEntFileNames() {
		return this.extEntFileNames;
	}

	/**
	 * @return the extEntSysidNames
	 */
	@ReportableProperty(order = 51, 
			value = "System identifiers associated with notations or external, text, or subdoc entities")
	public SortedSet<String> getExtEntSysidNames() {
		return this.extEntSysidNames;
	}

	/**
	 * @return the extTextEntNames
	 */
	@ReportableProperty(order = 52, 
			value = "External SGML text entity names")
	public SortedSet<String> getExtTextEntNames() {
		return this.extTextEntNames;
	}

	/**
	 * @return the notatNames
	 */
	@ReportableProperty(order = 53, 
			value = "Notation names")
	public SortedSet<String> getNotatNames() {
		return this.notatNames;
	}

	/**
	 * @return the extDataEntNames
	 */
	@ReportableProperty(order = 54, 
			value = "External SGML data entity names")
	public SortedSet<String> getExtDataEntNames() {
		return this.extDataEntNames;
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
	 * @return the subDocEntDefNames
	 */
	@ReportableProperty(order = 56, 
			value = "Names of defined sub-document entities")
	public SortedSet<String> getSubDocEntDefNames() {
		return this.subDocEntDefNames;
	}

	/**
	 * @return the subDocCommandNames
	 */
	@ReportableProperty(order = 56, 
			value = "Names of defined sub-document entities appearing in SGML document")
	public SortedSet<String> getSubDocCommandNames() {
		return this.subDocCommandNames;
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
	 * @return the intEnt2Value
	 */
	@ReportableProperty(order = 59, 
			value = "Map from internal data entity name to entity value")
	public Map<String, String> getInternalDataEntitytName2Value() {
		return this.intEnt2Value;
	}

	/**
	 * @return the intEnt2Type
	 */
	@ReportableProperty(order = 60, 
			value = "Map from internal data entity name to entity type")
	public Map<String, String> getIntEnt2Type() {
		return this.intEnt2Type;
	}

	/**
	 * @return the intEntType2Count
	 */
	@ReportableProperty(order = 61, 
			value = "Map from internal data entity types to count of that type in document")
	public Map<String, Integer> getInternalEntType2Count(){
		return this.intEntType2Count;
	}
	
	/**
	 * @return the elemAttributeType2Count
	 */
	@ReportableProperty(order = 62, 
			value = "Map from element attribute types to count of that type in document")
	public Map<String, Integer> getElemAttributeType2Count() {
		return this.elemAttributeType2Count;
	}

	/**
	 * @return the dataAttributeType2Count
	 */
	@ReportableProperty(order = 63, 
			value = "Map from external entity data attribute types to count of that type in document")
	public Map<String, Integer> getDataAttributeType2Count() {
		return this.dataAttributeType2Count;
	}

	/**
	 * @return the linkAttributeType2Count
	 */
	@ReportableProperty(order = 64, 
			value = "Map from link attribute types to count of that type in document")
	public Map<String, Integer> getLinkAttributeType2Count() {
		return this.linkAttributeType2Count;
	}

	/**
	 * @return the extEntName2dataAttrNames
	 */
	@ReportableProperty(order = 65, 
			value = "Map from external entity name to data attribute names associated with entity in SGML document")
	public Map<String, List<String>> getExtEntName2dataAttrNames() {
		return this.extEntName2dataAttrNames;
	}

	/**
	 * @return the processingInstructions
	 */
	@ReportableProperty(order = 65, 
			value = "Processing instructions in SGML document")
	public List<String> getProcessingInstructions() {
		return this.processingInstructions;
	}

	/**
	 * @return the appInfos
	 */
	@ReportableProperty(order = 66, 
			value = "APPINFOs in SGML document")
	public List<String> getAppInfos() {
		return this.appInfos;
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
	 * @param pubIds the pubIds to set
	 */
	public void setPubIds(SortedSet<String> pubIds) {
		this.pubIds = pubIds;
	}
	/**
	 * @param extEntFileNames the extEntFileNames to set
	 */
	public void setExtEntFileNames(SortedSet<String> extEntFileNames) {
		this.extEntFileNames = extEntFileNames;
	}
	/**
	 * @param extEntSysidNames the extEntSysidNames to set
	 */
	public void setExtEntSysidNames(SortedSet<String> extEntSysidNames) {
		this.extEntSysidNames = extEntSysidNames;
	}
	/**
	 * @param extTextEntNames the extTextEntNames to set
	 */
	public void setExtTextEntNames(SortedSet<String> extTextEntNames) {
		this.extTextEntNames = extTextEntNames;
	}
	/**
	 * @param notatNames the notatNames to set
	 */
	public void setNotatNames(SortedSet<String> notatNames) {
		this.notatNames = notatNames;
	}
	/**
	 * @param extDataEntNames the extDataEntNames to set
	 */
	public void setExtDataEntNames(SortedSet<String> extDataEntNames) {
		this.extDataEntNames = extDataEntNames;
	}
	/**
	 * @param entRefNames the entRefNames to set
	 */
	public void setEntRefNames(SortedSet<String> entRefNames) {
		this.entRefNames = entRefNames;
	}
	/**
	 * @param subDocEntDefNames the subDocEntDefNames to set
	 */
	public void setSubDocEntDefNames(SortedSet<String> subDocEntDefNames) {
		this.subDocEntDefNames = subDocEntDefNames;
	}
	/**
	 * @param subDocCommandNames the subDocCommandNames to set
	 */
	public void setSubDocCommandNames(SortedSet<String> subDocCommandNames) {
		this.subDocCommandNames = subDocCommandNames;
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
	 * @param intEnt2Value the intEnt2Value to set
	 */
	public void setInternalDataEntitytName2Value(Map<String, String> intEnt2Value) {
		this.intEnt2Value = intEnt2Value;
	}
	/**
	 * @param intEnt2Type the intEnt2Type to set
	 */
	public void setIntEnt2Type(Map<String, String> intEnt2Type) {
		this.intEnt2Type = intEnt2Type;
	}
	/**
	 * @param intEntType2Count the intEntType2Count to set
	 */
	public void setInternalEntType2Count(Map<String, Integer> intEntType2Count) {
		this.intEntType2Count = intEntType2Count;
	}
	/**
	 * @param elemAttributeType2Count the elemAttributeType2Count to set
	 */
	public void setElemAttributeType2Count(
			Map<String, Integer> elemAttributeType2Count) {
		this.elemAttributeType2Count = elemAttributeType2Count;
	}
	/**
	 * @param dataAttributeType2Count the dataAttributeType2Count to set
	 */
	public void setDataAttributeType2Count(
			Map<String, Integer> dataAttributeType2Count) {
		this.dataAttributeType2Count = dataAttributeType2Count;
	}
	/**
	 * @param linkAttributeType2Count the linkAttributeType2Count to set
	 */
	public void setLinkAttributeType2Count(
			Map<String, Integer> linkAttributeType2Count) {
		this.linkAttributeType2Count = linkAttributeType2Count;
	}
	/**
	 * @param extEntName2dataAttrNames the extEntName2dataAttrNames to set
	 */
	public void setExtEntName2dataAttrNames(
			Map<String, List<String>> extEntName2dataAttrNames) {
		this.extEntName2dataAttrNames = extEntName2dataAttrNames;
	}
	/**
	 * @param processingInstructions the processingInstructions to set
	 */
	public void setProcessingInstructions(List<String> processingInstructions) {
		this.processingInstructions = processingInstructions;
	}
	/**
	 * @param appInfos the appInfos to set
	 */
	public void setAppInfos(List<String> appInfos) {
		this.appInfos = appInfos;
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

}
