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


import org.jhove2.core.JHOVE2Exception;

/**
 * Wrapper around OpenSP SGML parser and onsgmlNorm utility. 
 * 
 * After the SGML file is parsed by OpenSp the output (in ESIS format) 
 * is parsed using an ANTLR-generated Java parser class.  The grammar 
 * has been decorated with Java members and methods to accumulate information
 * about the features of interest in the SGML file.  The grammar accepts 
 * the OpenSP indication as to whether or not the SGML instance conforms
 * to its DTD, and hence is to be considered valid.
 * 
 * No DOCTYPE information is returned in the ESIS file.  If the SGML module
 * is configured to ask for doctype, then we run the onsgml "normalization"
 * utility, and extract the doctype from the first line of its output.
 * 
 * @author smorrissey
 *
 */
public class OpenSpWrapper {

	public ESISCommandsParser parseFile(SgmlModule sgm)
	throws JHOVE2Exception {
		String esisFilePath = null;
		esisFilePath = this.parseSgmlFile(sgm.sgmFilePath);
		EsisParser esisParser = new EsisParser();
		ESISCommandsParser grammarParser = 
			esisParser.parseEsisFile(esisFilePath);
		return grammarParser;
	}
	
	public String parseSgmlFile(String sgmlPath)
	throws JHOVE2Exception {
		String esisFilePath = null;
		
		return esisFilePath;
	}
	
	public String createDoctype(String sgmlPath)
	throws JHOVE2Exception {
		String docType = null;
		
		return docType;		
	}
}