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
import java.util.List;

import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.source.Source;
import org.jhove2.util.CopyUtils;

/**
 * Class to parser .err (Message) files produced by OpenSp onsgmls and sgmlnorm utilities
 * @author smorrissey
 *
 */
public class ErrFileParser extends AbstractOpenSpMessageParser implements
OpenSpErrMessageParser {

	protected int eLevelMessageCount = 0;
	protected int wLevelMessageCount = 0;
	protected int iLevelMessageCount = 0;
	protected int xLevelMessageCount = 0;
	protected int qLevelMessageCount = 0;
	protected int totMessageCount = 0;

	protected List<String> openSpMessages;

	/**
	 * 
	 */
	public ErrFileParser() {
		super();
		this.openSpMessages  = new ArrayList<String>(); 
	}

	/* (non-Javadoc)
	 * @see org.jhove2.module.format.sgml.AbstractOpenSpMessageParser#parseMessageFile(java.lang.String, org.jhove2.core.JHOVE2, org.jhove2.core.source.Source, org.jhove2.module.format.sgml.SgmlModule)
	 */
	@Override
	public void parseMessageFile(String messageFilePath, JHOVE2 jhove2,
			Source source, SgmlModule sgm) throws JHOVE2Exception, IOException {
		BufferedReader onsgmlsOutput = null;
		try{
			onsgmlsOutput = new BufferedReader
			(new InputStreamReader(new FileInputStream(messageFilePath), "utf-8"));
			String tempMessage = null;

			String fileName;
			String lineNumber; 
			String posNumber; 
			String messageText; 
			String messageLevel; 
			String messageCode;

			if (onsgmlsOutput.ready())
				// read through output to get entity count and text count
			{
				while ((tempMessage = onsgmlsOutput.readLine()) != null)	                	
				{				
					/**
				COLON is used as a delimiter, but is also part of paths and of message text

				C:
				/usr/bin/onsgmls:
				C:                                                          2
				/sgmlModule/examples/dir01/aipspacebeforedocytpe1_1.sgm:    3
				17:                                                         4
				73:                                                         5
				1844095592.338:                                             6
				W:                                                          7
				cannot generate system identifier for general entity "uml"  8

				C:
				/usr/bin/onsgmls:
				C:                                                           2
				/sgmlModule/examples/dir01/aipspacebeforedocytpe1_1.sgm:     3
				827:                                                         4
				231:                                                         5
				relevant clauses:                                            6
				ISO 8879:                                                    7
				1986 11.2.4p11                                               8

				C:
				/usr/bin/onsgmls:
				/sgmlModule/examples/dir01/aipspacebeforedocytpe1_1.sgm:    2
				17:                                                         3
				73:                                                         4
				1844095592.338:                                             5
				W:                                                          6
				cannot generate system identifier for general entity "uml"  7

				/usr/bin/onsgmls:                                           0
				C:                                                          1
				/sgmlModule/examples/dir01/aipspacebeforedocytpe1_1.sgm:    2
				17:                                                         3
				73:                                                         4
				1844095592.338:                                             5
				W:                                                          6
				cannot generate system identifier for general entity "uml"  7


				/usr/bin/onsgmls:
				/sgmlModule/examples/dir01/aipspacebeforedocytpe1_1.sgm:    1
				17:                                                         2
				73:                                                         3
				1844095592.338:                                             4
				W:                                                          5
				cannot generate system identifier for general entity "uml"  6

				/usr/bin/onsgmls:
				/cygdrive/c/svn_repository/portico-docs/data/RequiredFiles/requiredFiles/Elsevier_Full_Length_Article_DTD_4.3.1/art431.dtd:  2
				827:                                                        2
				231:                                                        3
				relevant clauses:                                           4
				ISO 8879:                                                   5
				1986 11.2.4p11                                              6
					 */	
					fileName = "";
					lineNumber = "";
					posNumber = "";
					messageText = "";
					messageLevel = "";
					messageCode = "";
					String[] cmdInfo = tempMessage.split(":");
					int cmdInfoLngth = cmdInfo.length;
					switch (cmdInfoLngth){
					case 9:
						if (AbstractOpenSpMessageParser.isMessageCode(cmdInfo[7])){
							messageLevel = cmdInfo[7];
							fileName = cmdInfo[2].concat(cmdInfo[3]);
							lineNumber = cmdInfo[4];
							posNumber = cmdInfo[5];
							messageText = cmdInfo[8];
							messageCode = cmdInfo[6];
							String messageString = AbstractOpenSpMessageParser.createCodedMessageString
							(fileName, lineNumber, posNumber, messageText, messageLevel, messageCode);
							this.openSpMessages.add(messageString);
							this.updateMessageCounters(messageLevel);
						}
						else {
							this.openSpMessages.add(tempMessage);
						}
						break;
					case 8:
						if (AbstractOpenSpMessageParser.isMessageCode(cmdInfo[6])){
							messageLevel = cmdInfo[6];
							if (cmdInfo[1].contains("/")){
								fileName = cmdInfo[2];
							}
							else {
								fileName = cmdInfo[1].concat(cmdInfo[2]);
							}

							lineNumber = cmdInfo[3];
							posNumber = cmdInfo[4];
							messageText = cmdInfo[7];
							messageCode = cmdInfo[5];
							String messageString = AbstractOpenSpMessageParser.createCodedMessageString
							(fileName, lineNumber, posNumber, messageText, messageLevel, messageCode);
							this.openSpMessages.add(messageString);
							this.updateMessageCounters(messageLevel);
						}
						else {
							this.openSpMessages.add(tempMessage);
						}
						break;
					case 7:
						if (AbstractOpenSpMessageParser.isMessageCode(cmdInfo[5])){
							messageLevel = cmdInfo[5];
							fileName = cmdInfo[1];
							lineNumber = cmdInfo[2];
							posNumber = cmdInfo[3];
							messageText = cmdInfo[6];
							messageCode = cmdInfo[4];
							String messageString = AbstractOpenSpMessageParser.createCodedMessageString
							(fileName, lineNumber, posNumber, messageText, messageLevel, messageCode);
							this.openSpMessages.add(messageString);
							this.updateMessageCounters(messageLevel);
						}
						else {
							this.openSpMessages.add(tempMessage);
						}
						break;					
					default:
						this.openSpMessages.add(tempMessage);
						break;
					}// end switch
					totMessageCount++;
				}// end while
			}// end if (onsgmlsOutput.ready())
			if (sgm.getDocumentProperties()==null){
				sgm.setDocumentProperties(new SgmlDocumentProperties());
			}
			this.extractDocProperties(sgm.getDocumentProperties());
		}
		finally {
			if (onsgmlsOutput != null){
				onsgmlsOutput.close();
			}
		}
		return;
	}

	protected void extractDocProperties(SgmlDocumentProperties props) {
		props.setErrorLevelMessageCount(this.eLevelMessageCount);
		props.setWarningLevelMessageCount(this.wLevelMessageCount);
		props.setInfoLevelMessageCount(this.iLevelMessageCount);
		props.setQnameLevelMessageCount(this.qLevelMessageCount);
		props.setXrefLevelMessageCount(this.xLevelMessageCount);
		props.setTotMessageCount(this.totMessageCount);
		props.setSgmlParserMessages(CopyUtils.copyAndClearList(this.openSpMessages));
	}

	/**
	 * Update counters of different message types
	 * @param messageLevel String containing message level
	 */
	protected void updateMessageCounters (String messageLevel){
		if (messageLevel.equals("E")){
			eLevelMessageCount++;
		}
		else if (messageLevel.equals("W")){
			wLevelMessageCount++;
		}
		else if (messageLevel.equals("I")){
			iLevelMessageCount++;
		}
		else if (messageLevel.equals("Q")){
			qLevelMessageCount++;
		}
		else if (messageLevel.equals("X")){
			xLevelMessageCount++;
		}
		return;
	}

	/**
	 * @return the openSpMessages
	 */
	public List<String> getOpenSpMessages() {
		return openSpMessages;
	}

	/**
	 * @param openSpMessages the openSpMessages to set
	 */
	public void setOpenSpMessages(List<String> openSpMessages) {
		this.openSpMessages = openSpMessages;
	}

}
