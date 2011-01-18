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

import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.source.Source;

/**
 * Class for parsing output of OpenSp sgmlnorm utility.  This utility is run in order to extract the DOCTYPE statement
 * and any public or system identifer from an SGML document.
 * 
 * Please see the SGML module specification for details about running this utility.
 * 
 * @author smorrissey
 *
 */
public class SgmlNormFileParser implements SgmlNormParser {
	protected String DOCTYPE  	= "<!DOCTYPE";
	protected String DOCTYPELC	= "<!doctype";
	protected String PUBLIC     = "PUBLIC";
	protected String PUBLICLC   = "public";
	protected String SYSTEM     = "SYSTEM";
	protected String SYSTEMLC   = "system";
	protected String QUOTE      = "\"";
	protected String SQUOTE     = "'";

	protected boolean foundDoctype = false;
	protected boolean foundPubid = false;
	protected boolean foundSysid = false;
	protected String pubid = null;
	protected String systemId = null;

	/**
	 * 
	 */
	public SgmlNormFileParser() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.jhove2.module.format.sgml.SgmlNormParser#parseNormFile(java.lang.String, org.jhove2.core.JHOVE2, org.jhove2.core.source.Source, org.jhove2.module.format.sgml.SgmlModule)
	 */
	@Override
	public void parseNormFile(String normFilepath, JHOVE2 jhove2,
			Source source, SgmlModule sgm) throws JHOVE2Exception, IOException {
		BufferedReader sgmlNormOutput = null;		
		String tempMessage = null;
	
		try {
			sgmlNormOutput = new BufferedReader
			(new InputStreamReader(new FileInputStream(normFilepath), "utf-8"));
			if (sgmlNormOutput.ready())
			{
				while ((tempMessage = sgmlNormOutput.readLine()) != null)	                	
				{
					if (foundDoctype){
						continue;
					}
					if (tempMessage.startsWith(DOCTYPE)||tempMessage.startsWith(DOCTYPELC)){
						foundDoctype=true;
						int endPubId = this.lookForPubid(tempMessage);
						String restOfMessage = tempMessage;
						if (endPubId > -1 && tempMessage.length()>endPubId + 1){
							restOfMessage = tempMessage.substring(endPubId + 1);
						}
						this.lookForSystemId(restOfMessage);
						continue;
					}
					else {
						continue;
					}
				}// end while
			}// end if (sgmlNormOutput.ready())
		}// end try
		finally {
			if (sgmlNormOutput != null){
				sgmlNormOutput.close();
			}
		}
		if (sgm.getDocumentProperties()==null)	{
			sgm.setDocumentProperties(new SgmlDocumentProperties());
		}
		this.extractDocProperties(sgm.getDocumentProperties());
		return;
	}

	/**
	 * Extract significant properties obtained from parsed file and add to SgmlDocumentProperties object associated with the file
	 * @param props
	 */
	protected void extractDocProperties(SgmlDocumentProperties props) {
		props.setFoundDoctype(this.foundDoctype);
		String strPubid = null;
		if (this.pubid != null){
			strPubid = new String(this.pubid);
			this.pubid = null;
		}
		props.setPublicIdentifier(strPubid);
				
		props.setFoundPubid(this.foundPubid);
		
		String strSysId = null;
		if (this.systemId != null){
			strSysId = new String(this.systemId);
			this.systemId = null;
		}
		props.setSystemIdentifier(strSysId);			
		props.setFoundSysid(this.foundSysid);
		return;		
	}
	/**
	 * Parse String containing DOCTYPE statement to see if there is a public identifier
	 * If pubid is found, update foundPubId to "true", and extract and store pubid in field "pubid"
	 * @param tempMessage String containing DOCTYPE statement
	 * @return position of terminal single or double quote of the public identifier, or -1 if no public identifier found
	 */
	protected int lookForPubid(String tempMessage) {
		int endPubId = -1;
		int firstQuote = -1;
		int endQuote = -1;
		int startPubId = tempMessage.indexOf(PUBLIC);
		if (startPubId < 0){
			startPubId = tempMessage.indexOf(PUBLICLC);
		}
		if (startPubId >=0 && tempMessage.length()>startPubId+1){
			boolean useQuote = false;
			firstQuote = tempMessage.substring(startPubId).indexOf(QUOTE);
			if (firstQuote > -1){
				useQuote = true;				
			}
			else {
				firstQuote = tempMessage.substring(startPubId).indexOf(SQUOTE);				
			}
			if (firstQuote > -1 && tempMessage.length()> firstQuote + startPubId + 1){
				firstQuote = firstQuote + startPubId;
				if (useQuote){
					endQuote = tempMessage.substring(firstQuote+1).indexOf(QUOTE);
				}
				else {
					endQuote = tempMessage.substring(firstQuote+1).indexOf(SQUOTE);
				}
				if (endQuote > -1){
					endQuote = endQuote + firstQuote +1;
					this.foundPubid = true;
					this.pubid = tempMessage.substring(firstQuote+1,endQuote);
					endPubId = endQuote;
				}
			}
		}
		return endPubId;
	}
	/**
	 * Parse String containing DOCTYPE statement to see if it contains System Identifier
	 * If found, set this.foundSysid to true, and set this.systemId to value of System Identifier
	 * @param restOfMessage String containing DOCTYPE statement
	 */
	protected void lookForSystemId(String restOfMessage) {
		int firstQuote = -1;
		int endQuote = -1;
		int startSysId = restOfMessage.indexOf(SYSTEM);
		if (startSysId < 0){
			startSysId = restOfMessage.indexOf(SYSTEMLC);
		}
		if (startSysId >=0 && restOfMessage.length()>startSysId+1){
			boolean useQuote = false;
			firstQuote = restOfMessage.substring(startSysId).indexOf(QUOTE);
			if (firstQuote > -1){
				useQuote = true;				
			}
			else {
				firstQuote = restOfMessage.substring(startSysId).indexOf(SQUOTE);				
			}
			if (firstQuote > -1 && restOfMessage.length()> firstQuote + startSysId + 1){
				firstQuote = firstQuote + startSysId;
				if (useQuote){
					endQuote = restOfMessage.substring(firstQuote+1).indexOf(QUOTE);
				}
				else {
					endQuote = restOfMessage.substring(firstQuote+1).indexOf(SQUOTE);
				}
				if (endQuote > -1){
					endQuote = endQuote + firstQuote +1;
					this.foundSysid = true;
					this.systemId = restOfMessage.substring(firstQuote+1,endQuote);
				}
			}
		}
		return;
	}



}
