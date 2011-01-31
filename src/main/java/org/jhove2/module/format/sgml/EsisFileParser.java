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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.source.Source;
import org.jhove2.util.CopyUtils;

/**
 * Class to parse .out (ESIS) files produced by OpenSp onsgmls module
 * Please see module specification document for details of the ESIS file format
 * @author smorrissey
 *
 */
public class EsisFileParser implements OnsgmlsOutputParser {
	// characteristics of interest to the JHOVE2 SGML module

	protected boolean isSgmlValid = false; // is this a conforming SGML file; indicated by presence of "C" command at end of file
	protected String rootElementName; // element name of first startElementCommand
	protected  List<String> esisParseErrors;

	protected int publicIdCount = 0;
	protected int fileNamesCount = 0;
	protected int sysidsCount = 0;
	protected int extTextEntCount = 0;
	protected int notatDefCount = 0;
	protected int extDataEntCount = 0;
	protected int entrefCount = 0;
	protected int intDataEntCount = 0;
	protected int subDocEntityDefCount = 0;
	protected int subDocCommandCount = 0;
	protected int omitCommandCount = 0;
	protected int elementAttributeCount = 0;
	protected int dataAttrCount = 0;
	protected int linkAttrCount = 0;
	protected int elementCount = 0;  // number of start element commands (same element can occur more than once)
	protected int dataCount = 0;
	protected int includedSubElementsCount = 0;
	protected int emptyElementsCount = 0;
	protected int commentsCount = 0;
	protected int sDataCount = 0;
	protected int piCount = 0;
	protected int appInfoCount = 0;

	protected SortedSet<String> elementNames; 
	protected SortedSet<String> entRefNames;
	protected SortedSet<String> sdataNames;

	/** ESIS Command characters */ 
	protected String POUND = "#";   
	protected String AMP       = "&";    //38  x26
	protected String LEFTPAREN = "(";    //40  x28
	protected String RIGHTPAREN= ")";    //41  x29
	protected String DASH      = "-";    //45  x2D
	protected String PI        = "?";    //63  x3F
	protected String ACMD      = "A";   //65  x41
	protected String CCMD      = "C";    //67  x43
	protected String DCMD      = "D";   //68  x44
	protected String ECMD      = "E";   //69  x45
	protected String ICMD      = "I";   //74  x49
	protected String LCMD      = "L";   //76  x4C
	protected String NCMD      = "N";    //78  x4E
	protected String SCMD      = "S";    //83  x53
	protected String TCMD      = "T";    //84  x54
	protected String UNDER     = "_";    //95  x5F
	protected String LACMD     = "a";   //97  x61
	protected String LECMD     = "e";   //101  x65
	protected String FCMD      = "f";   //102  x66
	protected String LCICMD    = "i";   //105  x69
	protected String OCMD      = "o";   //111  x6f
	protected String PCMD      = "p";   //112  x70
	protected String LSCMD     = "s";   //115  x73
	protected String LEFTBRACE = "{";   //123  x7B
	protected String RIGHTBRACE= "}";   //125  x7d
	protected String SDATADELIM = "\\|";

	/**
	 * 
	 */
	public EsisFileParser() {
		super();
		isSgmlValid = false;
		rootElementName = null;
		this.setEsisParseErrors(new LinkedList<String>());
		this.setElementNames(new TreeSet<String>());
		this.setEntRefNames(new TreeSet<String>());
		this.setSdataNames(new TreeSet<String>());
	}

	/* (non-Javadoc)
	 * @see org.jhove2.module.format.sgml.OnsgmlsOutputParser#parseEsisFile(java.lang.String, org.jhove2.core.JHOVE2, org.jhove2.core.source.Source, org.jhove2.module.format.sgml.SgmlModule)
	 */
	@Override
	public SgmlDocumentProperties parseEsisFile(String esisPath, JHOVE2 jhove2,
			Source source, SgmlModule sgm) throws JHOVE2Exception, IOException {
		BufferedReader onsgmlsOutput = null;		
		String tempMessage = null;
		boolean foundCInOutput = false;
		boolean foundDataAfterCInInput = false;
		try {
			onsgmlsOutput = new BufferedReader
			(new InputStreamReader(new FileInputStream(esisPath), "utf-8"));
			if (onsgmlsOutput.ready())
				// read through output to get counts, start element, sdata entities, entity references
			{
				while ((tempMessage = onsgmlsOutput.readLine()) != null)	                	
				{
					if (foundCInOutput){
						//readLine() does not return line-termination characters
						boolean isEmptyLine = (tempMessage.length()==0);
						if (!isEmptyLine){
							esisParseErrors.add(OnsgmlsOutputParser.ESISERR + "Content found Conforming File Message");
							foundDataAfterCInInput = true;
							continue;
						}
						continue;
					}
					if (tempMessage.startsWith(ACMD))
					{
						elementAttributeCount++;
						this.checkForSdataEntities(tempMessage);
					}
					else if (tempMessage.startsWith(DCMD))
					{
						dataAttrCount++;
					}
					else if (tempMessage.startsWith(LACMD))
					{
						linkAttrCount++; 
					}
					else if (tempMessage.startsWith(LEFTPAREN))
					{
						String elementName = tempMessage.substring(1);
						if (elementCount==0){
							if(tempMessage.length()>0){							
								rootElementName = elementName;
							}
						}
						elementCount++;
						this.elementNames.add(elementName);
					}
					else if (tempMessage.startsWith(DASH))
					{
						dataCount++;
						this.checkForSdataEntities(tempMessage);
					}
					else if (tempMessage.startsWith(RIGHTPAREN))
					{
						continue;
					}
					else if (tempMessage.startsWith(AMP))
					{
						entrefCount++;
						if(tempMessage.length()>0){
							String entRef = tempMessage.substring(1);
							entRefNames.add(entRef);
						}

					}
					else if (tempMessage.startsWith(PI))
					{
						piCount++;
					}
					else if (tempMessage.startsWith(NCMD))
					{
						notatDefCount++;
					}
					else if (tempMessage.startsWith(ECMD))
					{
						extDataEntCount++;
					}
					else if (tempMessage.startsWith(ICMD))
					{
						intDataEntCount++;
					}
					else if (tempMessage.startsWith(SCMD))
					{
						subDocEntityDefCount++;
					}
					else if (tempMessage.startsWith(TCMD))
					{
						extTextEntCount++;
					}
					else if (tempMessage.startsWith(LSCMD))
					{
						sysidsCount++; 
					}
					else if (tempMessage.startsWith(PCMD))
					{
						publicIdCount++; 
					}
					else if (tempMessage.startsWith(FCMD))
					{
						fileNamesCount++; 
					}
					else if (tempMessage.startsWith(LEFTBRACE))
					{
						subDocCommandCount++;
					}
					else if (tempMessage.startsWith(RIGHTBRACE))
					{
						continue;
					}
					else if (tempMessage.startsWith(LCMD))
					{
						continue;
					}
					else if (tempMessage.startsWith(POUND))
					{
						appInfoCount++;
					}
					else if (tempMessage.startsWith(CCMD))
					{
						foundCInOutput = true;
					}
					else if (tempMessage.startsWith(LCICMD))
					{
						includedSubElementsCount++;
					}
					else if (tempMessage.startsWith(LECMD))
					{
						emptyElementsCount++;
					}
					else if (tempMessage.startsWith(UNDER))
					{
						commentsCount++;
					}
					else if (tempMessage.startsWith(OCMD))
					{
						omitCommandCount++;
					}
				}// end while		
			}// end if (onsgmlsOutput.ready())
		}// end try
		finally{
			if (onsgmlsOutput != null){
				onsgmlsOutput.close();
			}
		}
		if (foundCInOutput && !foundDataAfterCInInput){
			isSgmlValid = true;
		}
		if (sgm.getDocumentProperties()==null){
			sgm.setDocumentProperties(new SgmlDocumentProperties());
		}
		SgmlDocumentProperties props = sgm.getDocumentProperties();
		this.extractDocProperties(sgm.getDocumentProperties());		
		return props;		
	}

	/**
	 * Method to extract fields from ANTLR parser and make deep copy into SgmlDocumentProperties object.
	 * Clears those objects in the ANTLR parser.
	 * @param props updated SgmlDocumentProperties object with content of ANTLR parser fields
	 */
	protected void extractDocProperties(SgmlDocumentProperties props){
		if (getEsisParseErrors() != null){
			if (props.getParseErrors() == null){
				props.setParseErrors(new ArrayList<String>());
			}
			props.getParseErrors().addAll(
					CopyUtils.copyAndClearList(getEsisParseErrors()));
			setEsisParseErrors(null);
		}		
		props.setAppInfoCount(appInfoCount);
		props.setCommentsCount(commentsCount);
		props.setDataAttrCount(dataAttrCount);
		props.setDataCount(dataCount);
		props.setElementAttributeCount(elementAttributeCount);
		props.setElementCount(elementCount);
		props.setElementNames(CopyUtils.copyAndClearSet(elementNames));
		props.setEmptyElementsCount(emptyElementsCount);
		props.setEntRefNames(CopyUtils.copyAndClearSet(entRefNames));
		props.setEntityFileNamesCount(fileNamesCount);
		props.setEntrefCount(entrefCount);
		props.setExtDataEntCount(extDataEntCount);
		props.setExtTextEntCount(extTextEntCount);
		props.setIncludedSubElementsCount(includedSubElementsCount);
		props.setIntDataEntCount(intDataEntCount);
		props.setLinkAttrCount(linkAttrCount);
		props.setNotatDefCount(notatDefCount);
		props.setOmitCommandCount(omitCommandCount);
		props.setProcessingInstructionsCount(piCount);
		props.setPublicIdCount(publicIdCount);
		String strName = null;
		if (rootElementName != null){
			strName = new String(rootElementName);
			rootElementName = null;	
		}
		props.setRootElementName(strName);			
		props.setsDataCount(sDataCount);
		props.setSdataNames(CopyUtils.copyAndClearSet(sdataNames));
		props.setSubDocCommandCount(subDocCommandCount);
		props.setSubDocEntityDefCount(subDocEntityDefCount);
		props.setSysidsCount(sysidsCount);
		props.setSgmlValid(isSgmlValid);		
		return;
	}

	/**
	 * Method to inspect contents of data (element content) and element attribute commands for SDATA entities
	 * @param tempMessage
	 */
	protected void checkForSdataEntities(String tempMessage){
		if (tempMessage != null){
			int start = -1;
			int end =-1;
			String testString = tempMessage;
			while ((start = testString.indexOf(SDATADELIM))>-1){
				if (testString.length()>start+SDATADELIM.length()){
					testString = testString.substring(start+SDATADELIM.length());
					end = testString.indexOf(SDATADELIM);
					if (end > 0){
						String sdataString = testString.substring(0,end);
						sDataCount++;
						sdataNames.add(sdataString);
						if (testString.length() > end+SDATADELIM.length()){
							testString = testString.substring(end+SDATADELIM.length());
						}
						else {
							testString = "";
						}
					}
					else{
						testString = "";
					}
				}
				else {
					testString = "";
				}
			}// end while			
		}
		return;
	}

	/**
	 * @return the esisParseErrors
	 */
	public List<String> getEsisParseErrors() {
		return esisParseErrors;
	}

	/**
	 * @param esisParseErrors the esisParseErrors to set
	 */
	public void setEsisParseErrors(List<String> esisParseErrors) {
		this.esisParseErrors = esisParseErrors;
	}

	/**
	 * @return the entRefNames
	 */
	public SortedSet<String> getEntRefNames() {
		return entRefNames;
	}

	/**
	 * @param entRefNames the entRefNames to set
	 */
	public void setEntRefNames(SortedSet<String> entRefNames) {
		this.entRefNames = entRefNames;
	}

	/**
	 * @return the sdataNames
	 */
	public SortedSet<String> getSdataNames() {
		return sdataNames;
	}

	/**
	 * @param sdataNames the sdataNames to set
	 */
	public void setSdataNames(SortedSet<String> sdataNames) {
		this.sdataNames = sdataNames;
	}

	/**
	 * @return the elementNames
	 */
	public SortedSet<String> getElementNames() {
		return elementNames;
	}

	/**
	 * @param elementNames the elementNames to set
	 */
	public void setElementNames(SortedSet<String> elementNames) {
		this.elementNames = elementNames;
	}

}
