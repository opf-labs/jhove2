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
package org.jhove2.module.identify;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.*;

import org.jhove2.core.FormatIdentification;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.source.Source;
import org.jhove2.core.Format;
import org.jhove2.module.AbstractModule;

/**
 * @author smorrissey
 *
 */
public class FilePathGlobbingRecognizer extends AbstractModule implements
		Identifier {
	/** Identification module version identifier. */
	public static final String VERSION = "1.0.0";

	/** Identification module release date. */
	public static final String RELEASE = "2009-08-21";

	/** Identification module rights statement. */
	public static final String RIGHTS = "Copyright 2009 by The Regents of the University of California, "
			+ "Ithaka Harbors, Inc., and The Board of Trustees of the Leland "
			+ "Stanford Junior University. "
			+ "Available under the terms of the BSD license.";
	
	
	/** Presumptively identified presumptiveFormatIds. */
	protected Set<FormatIdentification> presumptiveFormatIds;
	/** Format which this recognizer can detect*/
	protected Format format;
	/** String containing regular expression to group candidate files */
	protected String fileGroupingToken;
	/** String containing regular expression to identify required candidate files */
	protected String mustHaveToken;
	/** String containing regular expression to identify optional candidate files */
	protected String mayHaveToken;
	/** Capture group index of in fileGroupingToken which captures the part of
	 *  the file path which we will be comparing for "must-have" files
	 */
	protected int mustHaveCaptureGroupIndex;
	/** Capture group index of in fileGroupingToken which captures the part of
	 *  the file path which we will be comparing for "may-have" files
	 */
	protected int mayHaveCaptureGroupIndex;
	/**
	 * count of files matching mustHaveTokens to identify a 
	 * Source as an instance of Format.  
	 * Allows us to identify potentially defective instances */
	protected int minMustHavesToIdentify;
	/** 
	 * Indicates whether or not to include in 
	 *  the Source that is part FormatIdentification returned by this class any the files 
	 *  in the Source being identified which match the grouping token, but do not match 
	 *  either must MustHaveTokens or MayHaveTokens */
	protected boolean includeUnmatchedFromGroup;
	
	/**
	 * Instantiate a new <code>FilePathGlobbingRecognizer</code>.
	 */
	public FilePathGlobbingRecognizer(String version, String release, String rights) {
		super(VERSION, RELEASE, RIGHTS);
		this.presumptiveFormatIds = new TreeSet<FormatIdentification>();
	}
	/**
	 * Instantiate a new <code>FilePathGlobbingRecognizer</code>.
	 * @param format Format which this class can recognize
	 * @param fileGroupingToken  String containing regular expression to group candidate files
	 * @param mustHaveToken String containing regular expression to identify required candidate files
	 * @param mayHaveToken  String containing regular expression to identify optional candidate files
	 * @param minMustHavesToIdentify count of files matching mustHaveTokens to identify a 
	 *         Source as an instance of Format.  Allows us to identify potentially defective instances
	 * @param includedUnmatchedFromGroup boolean which indicates whether or not to include in 
	 *        the Source that is part FormatIdentification returned by this class any the files 
	 *        in the Source being identified which match the grouping token, but do not match 
	 *        either must MustHaveTokens or MayHaveTokens
	 */
	public FilePathGlobbingRecognizer(Format format, String fileGroupingToken,
			String mustHaveToken, String mayHaveToken, int mustHaveCaptureGroupIndex, 
			int mayHaveCaptureGroupIndex, int minMustHavesToIdentify, 
			boolean includeUnmatchedFromGroup) {
		this(VERSION, RELEASE, RIGHTS);
		this.format = format;
		this.fileGroupingToken = fileGroupingToken;
		this.mustHaveToken = mustHaveToken;
		this.mayHaveToken = mayHaveToken;
		this.mustHaveCaptureGroupIndex = mustHaveCaptureGroupIndex;
		this.mayHaveCaptureGroupIndex = mayHaveCaptureGroupIndex;
		this.minMustHavesToIdentify = minMustHavesToIdentify;
		this.includeUnmatchedFromGroup = includeUnmatchedFromGroup;
	}
	/**
	 * 
	 * @param source
	 * @return
	 * @throws JHOVE2Exception
	 */
	protected List<List<Source>> groupSources(Source source)
			throws JHOVE2Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 * @return
	 * @throws JHOVE2Exception
	 */
	protected Set<FormatIdentification> recognizeGroupedSources()
			throws JHOVE2Exception {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see org.jhove2.module.identify.Identifier#identify(org.jhove2.core.JHOVE2, org.jhove2.core.source.Source)
	 */
	@Override
	public Set<FormatIdentification> identify(JHOVE2 jhove2, Source source)
			throws IOException, JHOVE2Exception {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see org.jhove2.module.identify.Identifier#getPresumptiveFormats()
	 */
	@Override
	public Set<FormatIdentification> getPresumptiveFormatIds() {
		return this.presumptiveFormatIds;
	}

	public Format getFormat() {
		return format;
	}
	public void setFormat(Format format) {
		this.format = format;
	}
	public String getFileGroupingToken() {
		return fileGroupingToken;
	}
	public void setFileGroupingToken(String groupingToken) {
		this.fileGroupingToken = groupingToken;
	}
	public String getMustHaveToken() {
		return mustHaveToken;
	}
	public void setMustHaveToken(String mustHaveToken) {
		this.mustHaveToken = mustHaveToken;
	}
	public String getMayHaveToken() {
		return mayHaveToken;
	}
	public void setMayHaveToken(String mayHaveToken) {
		this.mayHaveToken = mayHaveToken;
	}
	public int getMinMustHavesToIdentify() {
		return minMustHavesToIdentify;
	}
	public void setMinMustHavesToIdentify(int minMustHavesToIdentify) {
		this.minMustHavesToIdentify = minMustHavesToIdentify;
	}
	public boolean isIncludeUnmatchedFromGroup() {
		return includeUnmatchedFromGroup;
	}
	public void setIncludedUnmatchedFromGroup(boolean includeUnmatchedFromGroup) {
		this.includeUnmatchedFromGroup = includeUnmatchedFromGroup;
	}
	public int getMustHaveCaptureGroupIndex() {
		return mustHaveCaptureGroupIndex;
	}
	public void setMustHaveCaptureGroupIndex(int mustHaveCaptureGroupIndex) {
		this.mustHaveCaptureGroupIndex = mustHaveCaptureGroupIndex;
	}
	public int getMayHaveCaptureGroupIndex() {
		return mayHaveCaptureGroupIndex;
	}
	public void setMayHaveCaptureGroupIndex(int mayHaveCaptureGroupIndex) {
		this.mayHaveCaptureGroupIndex = mayHaveCaptureGroupIndex;
	}

}
