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

import org.jhove2.util.externalprocess.FilepathFilter;

/**
 * Class for setting options for OpenSp sgmlnorm utility
 * @author smorrissey
 *
 */
public class SgmlNormOptions implements OpenSpOptions {

	public static final String SHOWOPENENTOPT = "e";
	public static final String SHOWGIOPT = "g";
	public static final String SHOWMISOOPT = "x";
	public static final String DOCTYPEOPT = "d";
	public static final String MARKEDSECOPT = "m";
	public static final String COMMENTSOPT = "n";
    
	public static final String MAXERROPT = "-E";
	public static final String SHOWMESSOPT = "--error-numbers";
	
	public static final String SEARCHDIROPT = "-D";
	public static final String INCLUDEOPT = "-i";
	public static final String WARNOPT = "-w";

	
	/** option -E: OpenSp exits after maxErrors; if 0: no limit; default value is 200 */
	protected int maxErrors = OpenSpOptions.DEFAULTMAXERRORS;
	
	/** option --error-numbers Show message numbers in error output */
	protected boolean showMessageNumbers;
	
	/** option -e  Show open entities in error messages */
	protected boolean showOpenEntities;
	/** option -g  Show the generic identifiers of open elements in error messages */
	protected boolean showGI;
    /** option -x Show information about relevant clauses (from ISO 8879:1986) in error messages.  */
	protected boolean showIsoRefs;
	/** option -d create doctype statement default is TRUE because getting docytpe is reason we run this for SGML module */
	protected boolean showDoctype = true;
	/** option -m output marked sections */
	protected boolean showMarkedSections;
	/** option -n output comments */
	protected boolean showComments;
	
	/** option -c: use catalog PathToCatalog  if empty or null, does not use catalog */
	protected String catalogPath;
	
	/** option -D: search these directories for files specified in system identifiers*/
	protected List<String>searchDiretories;
	/** option -i overrides "IGNORE" to "INCLUDE" for marked sections for entities */
	protected List<String> includedEntities;
	/** warning options -w */
	protected List<String> warningOptions;
	
	/** filters to be applied to filepaths to enable processing by ExternalProcessHandler on different operating systems*/
	protected FilepathFilter filepathFilter = null;

	
	/* (non-Javadoc)
	 * @see org.jhove2.module.format.sgml.OpenSpOptions#getOptionString()
	 */
	@Override
	public String getOptionString() {
		StringBuffer opts = new StringBuffer(MAXERROPT);		
		String strMax = Integer.toString(this.maxErrors);
		opts.append(strMax);
		opts.append(" ");
		if (showMessageNumbers){
			opts.append(SHOWMESSOPT);
			opts.append(" ");
		}
		StringBuffer flags = new StringBuffer();
		if (showOpenEntities){
			flags.append(SHOWOPENENTOPT);
		}
		if (showGI){
			flags.append(SHOWGIOPT);
		}
		if (showIsoRefs){
			flags.append(SHOWMISOOPT);
		}
		if (showDoctype){
			flags.append(DOCTYPEOPT);
		}
		if (showMarkedSections){
			flags.append(MARKEDSECOPT);
		}
		if (showComments){
			flags.append(COMMENTSOPT);
		}
		if (flags.length()>0){
			opts.append("-");
			opts.append(flags.toString());
			opts.append(" ");
		}
		if (this.searchDiretories != null){
			for (String searchDir:this.searchDiretories){
				opts.append(SEARCHDIROPT);
				opts.append(searchDir);
				opts.append(" ");
			}
		}		
		if (this.includedEntities!= null)	{
			for (String include:this.includedEntities){
				opts.append(INCLUDEOPT);
				opts.append(include);
				opts.append(" ");
			}
		}
		if (this.warningOptions != null){
			for (String warning:this.warningOptions){
				opts.append(WARNOPT);
				opts.append(warning);
				opts.append(" ");
			}
		}
		if (this.getCatalogPath()!= null && this.getCatalogPath().length()>0){
			String filteredCatalogPath =this.getCatalogPath();
			if (this.filepathFilter != null){
				filteredCatalogPath = this.filepathFilter.filter(filteredCatalogPath);
			}
			opts.append(OpenSpOptions.CATALOGOPT);
			opts.append(filteredCatalogPath);
			opts.append(" ");
		}		
		return opts.toString();
	}
	/* (non-Javadoc)
	 * @see org.jhove2.module.format.sgml.OpenSpOptions#getCatalogPath()
	 */
	@Override
	public String getCatalogPath() {
		return this.catalogPath;
	}
	/* (non-Javadoc)
	 * @see org.jhove2.module.format.sgml.OpenSpOptions#setCatalogPath(java.lang.String)
	 */
	@Override
	public void setCatalogPath(String catalogPath) {
		this.catalogPath = catalogPath;
	}
	/**
	 * @return the maxErrors
	 */
	public int getMaxErrors() {
		return maxErrors;
	}
	/**
	 * @return the showOpenEntities
	 */
	public boolean isShowOpenEntities() {
		return showOpenEntities;
	}
	/**
	 * @return the showGI
	 */
	public boolean isShowGI() {
		return showGI;
	}
	/**
	 * @return the showMessageNumbers
	 */
	public boolean isShowMessageNumbers() {
		return showMessageNumbers;
	}
	/**
	 * @return the showIsoRefs
	 */
	public boolean isShowIsoRefs() {
		return showIsoRefs;
	}
	/**
	 * @return the showDoctype
	 */
	public boolean isShowDoctype() {
		return showDoctype;
	}
	/**
	 * @return the showMarkedSections
	 */
	public boolean isShowMarkedSections() {
		return showMarkedSections;
	}
	/**
	 * @return the searchDiretories
	 */
	public List<String> getSearchDiretories() {
		return searchDiretories;
	}
	/**
	 * @return the includedEntities
	 */
	public List<String> getIncludedEntities() {
		return includedEntities;
	}
	/**
	 * @return the warningOptions
	 */
	public List<String> getWarningOptions() {
		return warningOptions;
	}
	/**
	 * @return the filepathFilter
	 */
	public FilepathFilter getFilepathFilter() {
		return filepathFilter;
	}
	/**
	 * @param maxErrors the maxErrors to set
	 */
	public void setMaxErrors(int maxErrors) {
		this.maxErrors = maxErrors;
	}
	/**
	 * @param showOpenEntities the showOpenEntities to set
	 */
	public void setShowOpenEntities(boolean showOpenEntities) {
		this.showOpenEntities = showOpenEntities;
	}
	/**
	 * @param showGI the showGI to set
	 */
	public void setShowGI(boolean showGI) {
		this.showGI = showGI;
	}
	/**
	 * @param showMessageNumbers the showMessageNumbers to set
	 */
	public void setShowMessageNumbers(boolean showMessageNumbers) {
		this.showMessageNumbers = showMessageNumbers;
	}
	/**
	 * @param showIsoRefs the showIsoRefs to set
	 */
	public void setShowIsoRefs(boolean showIsoRefs) {
		this.showIsoRefs = showIsoRefs;
	}
	/**
	 * @param showDoctype the showDoctype to set
	 */
	public void setShowDoctype(boolean showDoctype) {
		this.showDoctype = showDoctype;
	}
	/**
	 * @param showMarkedSections the showMarkedSections to set
	 */
	public void setShowMarkedSections(boolean showMarkedSections) {
		this.showMarkedSections = showMarkedSections;
	}
	/**
	 * @param searchDiretories the searchDiretories to set
	 */
	public void setSearchDiretories(List<String> searchDiretories) {
		this.searchDiretories = searchDiretories;
	}
	/**
	 * @param includedEntities the includedEntities to set
	 */
	public void setIncludedEntities(List<String> includedEntities) {
		this.includedEntities = includedEntities;
	}
	/**
	 * @param warningOptions the warningOptions to set
	 */
	public void setWarningOptions(List<String> warningOptions) {
		this.warningOptions = warningOptions;
	}
	/**
	 * @param filepathFilter the filepathFilter to set
	 */
	public void setFilepathFilter(FilepathFilter filepathFilter) {
		this.filepathFilter = filepathFilter;
	}
	/**
	 * @return the showComments
	 */
	public boolean isShowComments() {
		return showComments;
	}
	/**
	 * @param showComments the showComments to set
	 */
	public void setShowComments(boolean showComments) {
		this.showComments = showComments;
	}

}
