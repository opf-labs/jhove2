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


import java.io.File;
import java.io.IOException;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.util.externalprocess.ExternalProcessHandler;
import org.jhove2.util.externalprocess.FilepathFilter;

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

	//  /usr/bin/onsgmls -E0 -gnxl -wall -oentity -ocomment -oid -oincluded -ononsgml -onotation-sysid -oomitted -odata-attribute -c$CATALOG -f$SGMERR $SGMIN > $SGMOUT
	public static final String CATALOGOPT = "-c";
	public static final String ERRFILEOPT = "-f";
	public static final String BASECOMMAND = 
		"-E0 -gnxl -wall -oentity -ocomment -oid -oincluded -ononsgml -onotation-sysid -oomitted -odata-attribute ";
	///usr/bin/sgmlnorm -E0 -gnexmd --error-numbers -wall -c$CATALOG -f$SGMERR $SGMIN > $SGMOUT
	public static final String BASEDTDCOMMAND = 
		"-E0 -gnexmd --error-numbers -wall";
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

	protected ExternalProcessHandler processHandler;

	protected FilepathFilter filepathFilter = null;

	protected String onsgmlsPath;// path to ongmsl command

	protected String esisFilePath;

	protected String esisErrFilePath;

	protected String sgmFilePath;
	
	protected String sgmlnormPath; // path to Open sgmlnorm command
	
	protected String normOutPath;
	
	protected String normErrPath;
	/**
	 * Invokes onsmgls processor to parse and validate the SGML
	 * source.  Then invokes ANTLR-generated parser to accumulate
	 * feature information about the instance
	 * @param sgm SgmlModule instance invoking this method; module will have 
	 *            Source memeber
	 * @return ANTLR-generated parser with accumulated feature information
	 * @throws JHOVE2Exception
	 */
	public ESISCommandsParser parseFile(SgmlModule sgm)
	throws JHOVE2Exception {
		if (this.sgmFilePath==null){
			File sgmFile = sgm.source.getFile();
			try {
				this.sgmFilePath = sgmFile.getCanonicalPath();
				if (this.filepathFilter != null){
					this.sgmFilePath = this.filepathFilter.filter(this.sgmFilePath);
				}
			} catch (IOException e) {
				throw new JHOVE2Exception(
						"IO Exception thrown attempting to determine canonical path for SGML source",
						e);
			}
		}
		String [] onsgmlOutputs = this.parseSgmlFile(sgm);
		this.esisFilePath = onsgmlOutputs[0];
		this.esisErrFilePath = onsgmlOutputs[1];
		EsisParser esisParser = new EsisParser();
		ESISCommandsParser grammarParser = 
			esisParser.parseEsisFile(this.esisFilePath);
		return grammarParser;
	}

	/**
	 * 
	 * @param sgm
	 * @return
	 * @throws JHOVE2Exception
	 */
	public String[] parseSgmlFile(SgmlModule sgm)
	throws JHOVE2Exception {
		String esisFilePath = null;
		String errFilePath = null;
		File tempEsisFile = null;
		File tempErrFile = null;
		if (this.sgmFilePath==null){
			File sgmFile = sgm.source.getFile();
			try {
				this.sgmFilePath = sgmFile.getCanonicalPath();
				if (this.filepathFilter != null){
					this.sgmFilePath = this.filepathFilter.filter(this.sgmFilePath);
				}
			} catch (IOException e) {
				throw new JHOVE2Exception(
						"IO Exception thrown attempting to determine canonical path for SGML source",
						e);
			}
		}
		// create path names for the 2 output (esis output and err messages)
		// files generated by onsgmls
		try {
			tempEsisFile = File.createTempFile(
					sgm.jhove2.getInvocation().getTempPrefix(),
					sgm.jhove2.getInvocation().getTempSuffix().concat(".esis"), 
					new File(sgm.jhove2.getInvocation().getTempDirectory()));
		} catch (IOException e) {
			throw new JHOVE2Exception(
					"IOException attemtping to create temporary ESIS file",
					e);
		}
//		tempEsisFile.deleteOnExit();
		esisFilePath = tempEsisFile.getPath();
		if (this.filepathFilter != null){
			esisFilePath = this.filepathFilter.filter(esisFilePath);
		}
		try {
			tempErrFile = File.createTempFile(
					sgm.jhove2.getInvocation().getTempPrefix(),
					sgm.jhove2.getInvocation().getTempSuffix().concat(".esis.err"), 
					new File(sgm.jhove2.getInvocation().getTempDirectory()));
		} catch (IOException e) {
			throw new JHOVE2Exception(
					"IOException attemtping to create temporary ESIS error file",
					e);
		}
//		tempErrFile.deleteOnExit();
		errFilePath = tempErrFile.getPath();
		if (this.filepathFilter != null){
			errFilePath = this.filepathFilter.filter(errFilePath);
		}
		StringBuffer sbCommand = new StringBuffer(onsgmlsPath);
		sbCommand.append(" ");
		sbCommand.append(BASECOMMAND);
		sbCommand.append(" ");
		if (this.getCatalogPath()!= null){
			String filteredCatalogPath =this.getCatalogPath();
			if (this.filepathFilter != null){
				filteredCatalogPath = this.filepathFilter.filter(filteredCatalogPath);
			}
			sbCommand.append(CATALOGOPT);
			sbCommand.append(filteredCatalogPath);
			sbCommand.append(" ");
		}
		sbCommand.append(ERRFILEOPT);
		sbCommand.append(errFilePath);
		sbCommand.append(" ");
		sbCommand.append(this.sgmFilePath);
		sbCommand.append(" > ");
		sbCommand.append(esisFilePath);
		String command = sbCommand.toString();
		this.getProcessHandler().executeCommand(command);
		return new String[]{esisFilePath, errFilePath};
	}


	/**
	 * 
	 * @param sgm
	 * @return
	 * @throws JHOVE2Exception
	 */
	public String createDoctype(SgmlModule sgm)
	throws JHOVE2Exception {
		File tempNormFile = null;
		File tempErrFile = null;
		String docType = null;
		if (this.sgmFilePath==null){
			File sgmFile = sgm.source.getFile();
			try {
				this.sgmFilePath = sgmFile.getCanonicalPath();
				if (this.filepathFilter != null){
					this.sgmFilePath = this.filepathFilter.filter(this.sgmFilePath);
				}
			} catch (IOException e) {
				throw new JHOVE2Exception(
						"IO Exception thrown attempting to determine canonical path for SGML source",
						e);
			}
		}
		// create path names for the 2 output (esis output and err messages)
		// files generated by onsgmls
		try {
			tempNormFile = File.createTempFile(
					sgm.jhove2.getInvocation().getTempPrefix(),
					sgm.jhove2.getInvocation().getTempSuffix().concat(".norm"), 
					new File(sgm.jhove2.getInvocation().getTempDirectory()));
		} catch (IOException e) {
			throw new JHOVE2Exception(
					"IOException attemtping to create temporary NORMALIZED file",
					e);
		}
//		tempNormFile.deleteOnExit();
		this.normOutPath = tempNormFile.getPath();
		if (this.filepathFilter != null){
			this.normOutPath = this.filepathFilter.filter(this.normOutPath);
		}
		try {
			tempErrFile = File.createTempFile(
					sgm.jhove2.getInvocation().getTempPrefix(),
					sgm.jhove2.getInvocation().getTempSuffix().concat(".norm.err"), 
					new File(sgm.jhove2.getInvocation().getTempDirectory()));
		} catch (IOException e) {
			throw new JHOVE2Exception(
					"IOException attemtping to create temporary NORMALIZED error file",
					e);
		}
//		tempErrFile.deleteOnExit();
		this.normErrPath = tempErrFile.getPath();
		if (this.filepathFilter != null){
			this.normErrPath = this.filepathFilter.filter(this.normErrPath);
		}
		StringBuffer sbCommand = new StringBuffer(this.sgmlnormPath);
		sbCommand.append(" ");
		sbCommand.append(BASEDTDCOMMAND);
		sbCommand.append(" ");
		if (this.getCatalogPath()!= null){
			String filteredCatalogPath =this.getCatalogPath();
			if (this.filepathFilter != null){
				filteredCatalogPath = this.filepathFilter.filter(filteredCatalogPath);
			}
			sbCommand.append(CATALOGOPT);
			sbCommand.append(filteredCatalogPath);
			sbCommand.append(" ");
		}
		sbCommand.append(ERRFILEOPT);
		sbCommand.append(this.normErrPath);
		sbCommand.append(" ");
		sbCommand.append(this.sgmFilePath);
		sbCommand.append(" > ");
		sbCommand.append(normOutPath);
		String command = sbCommand.toString();
		this.getProcessHandler().executeCommand(command);
		return docType;		
	}


	/**
	 * @return the catalogPath
	 */
	@ReportableProperty(order = 20, value = "Parser setting:  Full path to catalog file")
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
	 * @return the processHandler
	 */
	public ExternalProcessHandler getProcessHandler() {
		return processHandler;
	}

	/**
	 * @param processHandler the processHandler to set
	 */
	public void setProcessHandler(ExternalProcessHandler processHandler) {
		this.processHandler = processHandler;
	}

	/**
	 * @return the onsgmlsPath
	 */
	public String getOnsgmlsPath() {
		return onsgmlsPath;
	}

	/**
	 * @param onsgmlsPath the onsgmlsPath to set
	 */
	public void setOnsgmlsPath(String onsgmlsPath) {
		this.onsgmlsPath = onsgmlsPath;
	}

	/**
	 * @return the filepathFilter
	 */
	public FilepathFilter getFilepathFilter() {
		return filepathFilter;
	}

	/**
	 * @param filepathFilter the filepathFilter to set
	 */
	public void setFilepathFilter(FilepathFilter filepathFilter) {
		this.filepathFilter = filepathFilter;
	}

	/**
	 * @return the esisFilePath
	 */
	public String getEsisFilePath() {
		return esisFilePath;
	}

	/**
	 * @param esisFilePath the esisFilePath to set
	 */
	public void setEsisFilePath(String esisFilePath) {
		this.esisFilePath = esisFilePath;
	}

	/**
	 * @return the esisErrFilePath
	 */
	public String getEsisErrFilePath() {
		return esisErrFilePath;
	}

	/**
	 * @param esisErrFilePath the esisErrFilePath to set
	 */
	public void setEsisErrFilePath(String esisErrFilePath) {
		this.esisErrFilePath = esisErrFilePath;
	}

	/**
	 * @return the sgmFilePath
	 */
	public String getSgmFilePath() {
		return sgmFilePath;
	}

	/**
	 * @param sgmFilePath the sgmFilePath to set
	 */
	public void setSgmFilePath(String sgmFilePath) {
		this.sgmFilePath = sgmFilePath;
	}

	/**
	 * @return the sgmlnormPath
	 */
	public String getSgmlnormPath() {
		return sgmlnormPath;
	}

	/**
	 * @param sgmlnormPath the sgmlnormPath to set
	 */
	public void setSgmlnormPath(String sgmlnormPath) {
		this.sgmlnormPath = sgmlnormPath;
	}

	/**
	 * @return the normOutPath
	 */
	public String getNormOutPath() {
		return normOutPath;
	}

	/**
	 * @param normOutPath the normOutPath to set
	 */
	public void setNormOutPath(String normOutPath) {
		this.normOutPath = normOutPath;
	}

	/**
	 * @return the normErrPath
	 */
	public String getNormErrPath() {
		return normErrPath;
	}

	/**
	 * @param normErrPath the normErrPath to set
	 */
	public void setNormErrPath(String normErrPath) {
		this.normErrPath = normErrPath;
	}

}