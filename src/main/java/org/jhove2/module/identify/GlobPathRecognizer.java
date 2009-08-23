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
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;
import java.util.HashMap;
import java.util.regex.*;

import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.FormatIdentification;
import org.jhove2.core.FormatIdentification.Confidence;
import org.jhove2.core.source.Source;
import org.jhove2.core.Format;
import org.jhove2.module.AbstractModule;

/**
 * @author smorrissey
 *
 */
public class GlobPathRecognizer extends AbstractModule implements
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
	protected String fileGroupingExpr;
	/** String containing regular expression to identify required candidate files */
	protected String mustHaveExpr;
	/** String containing regular expression to identify optional candidate files */
	protected String mayHaveExpr;
	/** Capture group index in fileGroupingToken which captures the part of
	 *  the file path which indicates related files */
	protected int fileGroupingCaptureGroupIndex;
	/** Capture group index in fileGroupingToken which captures the part of
	 *  the file path which we will be comparing for "must-have" files */
	protected int mustHaveCaptureGroupIndex;
	/** Capture group index in fileGroupingToken which captures the part of
	 *  the file path which we will be comparing for "may-have" files */
	protected int mayHaveCaptureGroupIndex;
	/** Minimum number of files that must match the mustHaveExpr in order for 
	 * a set of Sources to be considered an instance of an aggregate Format.  
	 * Allows us to identify potentially defective instances of a format */
	protected int minMustHavesToIdentify;
	/** Indicates whether or not to include in the Source that is part of the 
	 *  FormatIdentification returned by this class any files which match the grouping expression, 
	 *  but do not match either must mustHaveExpr or mayHaveExpr */
	protected boolean includeUnmatchedFromGroup;
	/** Pattern compiled from the fileGroupingExpr */
	protected Pattern fileGroupingPattern;
	/** Pattern constructed from mustHaveExpr */
	protected Pattern mustHavePattern;
	/** Pattern constructed from mayHaveExpr */
	protected Pattern mayHavePattern;
	
	/**
	 * Instantiate a new <code>FilePathGlobbingRecognizer</code>.
	 */
	public GlobPathRecognizer(String version, String release, String rights) {
		super(VERSION, RELEASE, RIGHTS);
		this.presumptiveFormatIds = new TreeSet<FormatIdentification>();
	}
	/**
	 * Instantiate a new <code>FilePathGlobbingRecognizer</code>.
	 * @param format Format which this class can recognize
	 * @param fileGroupingExpr  String containing regular expression to group candidate files
	 * @param mustHaveExpr String containing regular expression to identify required candidate files
	 * @param mayHaveExpr  String containing regular expression to identify optional candidate files
	 * @param fileGroupingCaptureGroupIndex int containing capture group index in fileGroupingExpr that
	 *                            captures the part of the file path which indicates related files 
	 * @param mustHaveCaptureGroupIndex int containing capture group index in fileGroupingExpr that
	 *                           captures the part of the file path which indicates file is required
	 * @param mayHaveCaptureGroupIndex int containing capture group index in fileGroupingExpr that
	 *                           captures the part of the file path which indicates file is optional
	 * @param minMustHavesToIdentify int containing minimum number of files that must match the
	 *                           mustHaveExpr in order for a set of Sources to be considered an 
	 *                           instance of an aggregate Format.  Allows us to identify potentially 
	 *                           defective instances
	 * @param includedUnmatchedFromGroup boolean which indicates whether or not to include in a Source
	 *                           that is part of the FormatIdentification returned by this class any 
	 *                           files which match the grouping expression, but do not match either 
	 *                           mustHaveExpr or mayHaveExpr 
	 */
	public GlobPathRecognizer(Format format, String fileGroupingExpr,
			String mustHaveExpr, String mayHaveExpr, 
			int fileGroupingCaptureGroupIndex,
			int mustHaveCaptureGroupIndex, 
			int mayHaveCaptureGroupIndex, 
			int minMustHavesToIdentify, 
			boolean includeUnmatchedFromGroup) {
		this(VERSION, RELEASE, RIGHTS);
		this.format = format;
		this.fileGroupingExpr = fileGroupingExpr;
		this.mustHaveExpr = mustHaveExpr;
		this.mayHaveExpr = mayHaveExpr;
		this.fileGroupingCaptureGroupIndex = fileGroupingCaptureGroupIndex;
		this.mustHaveCaptureGroupIndex = mustHaveCaptureGroupIndex;
		this.mayHaveCaptureGroupIndex = mayHaveCaptureGroupIndex;
		this.minMustHavesToIdentify = minMustHavesToIdentify;
		this.includeUnmatchedFromGroup = includeUnmatchedFromGroup;
	}

	/* (non-Javadoc)
	 * @see org.jhove2.module.identify.Identifier#identify(org.jhove2.core.JHOVE2, org.jhove2.core.source.Source)
	 */
	@Override
	public Set<FormatIdentification> identify(JHOVE2 jhove2, Source source)
			throws IOException, JHOVE2Exception {
		this.compilePatterns();
		Collection<GlobPathMatchInfoGroup> sourceGroups = this.groupSources(source);
		for (GlobPathMatchInfoGroup sourceGroup:sourceGroups){
			FormatIdentification fi = this.recognizeGroupedSource(sourceGroup);
			if (fi != null){
				this.presumptiveFormatIds.add(fi);
			}
		}
		return this.presumptiveFormatIds;
	}
	/* (non-Javadoc)
	 * @see org.jhove2.module.identify.Identifier#getPresumptiveFormats()
	 */
	@Override
	public Set<FormatIdentification> getPresumptiveFormatIds() {
		return this.presumptiveFormatIds;
	}
	/**
	 * Constructs candidate instances of an aggregate format by grouping 
	 * together all children of source parameter that match fileGroupingToken
	 * @param source Source object whose child Sources are to be explored for groups
	 *        constituting instances of a format
	 * @return Collection of GlobPathMatchInfoGroup objects, each of which contains a list
	 *         of likely related Sources comprising an instance of a Format,
	 *         and indications as to whether or not each Source in the group is a
	 *         required or optional or unspecified component of that Format instances
	 * @throws JHOVE2Exception
	 */
	protected Collection <GlobPathMatchInfoGroup> groupSources(Source source)
			throws JHOVE2Exception {
		HashMap<String,  GlobPathMatchInfoGroup> groupMap = 
			new HashMap<String, GlobPathMatchInfoGroup>();
		for (Source childSource:source.getChildSources()){
			String filePath = childSource.getFile().getPath();
			// does the Source file path match the pattern that indicates a related file?
			Matcher m = this.fileGroupingPattern.matcher(filePath);
			if (m.matches()){
				// might have more than one instance of a format in the Source, so
				// we have to group related files together
				// get the value of the capture group which is the key to a format instance
				//  (group of files)
				String groupString = m.group(this.fileGroupingCaptureGroupIndex);
				// get the value of the capture group that indicates a file in the group
				//   is one of the files required by the format definition
				String mustHaveString = m.group(this.mustHaveCaptureGroupIndex);
				// get the value of the capture group that indicates a file in the group
				//   is one of the files considered optional by the format definition
				String mayHaveString = m.group(this.mayHaveCaptureGroupIndex);
				GlobPathMatchInfo fileInfo = new GlobPathMatchInfo(childSource);
				Matcher m2 = this.mustHavePattern.matcher(mustHaveString);
				boolean matchesMustHaves = m2.matches();
				fileInfo.setMustHave(matchesMustHaves);
				m2 = this.mayHavePattern.matcher(mayHaveString);
				boolean matchesMayHaves = m2.matches();
				fileInfo.setMayHave(matchesMayHaves);
				GlobPathMatchInfoGroup infoGroup;
				//is this the first occurrence of grouping key?
				if (!(groupMap.containsKey(groupString))){
					//if so, add grouping key and new GlobPathMatchInfoGroup to groupMaP
					infoGroup = new GlobPathMatchInfoGroup();
					infoGroup.setGroupKey(groupString);
					groupMap.put(groupString, infoGroup);
				}
				else {
					// otherwise just retrieve 
					infoGroup = groupMap.get(groupString);
				}
				// add information about current Source to list associated with this grouping key 
				infoGroup.getSourceMatchInfoList().add(fileInfo);
				// increment counter information associated with this grouping key
				if (matchesMustHaves){
					infoGroup.setMustHaveCount(infoGroup.getMustHaveCount()+1);
				}
				if (matchesMayHaves){
					infoGroup.setMayHaveCount(infoGroup.getMayHaveCount()+1);
				}
				if (!matchesMustHaves && !matchesMayHaves){
					infoGroup.setUnmatchedCount(infoGroup.getUnmatchedCount()+1);
				}
			}// end if (m.matches()){
		}// end for (Source childSource:source.getChildSources())
		// we don't need the keys to the map any more; just return the values
		return groupMap.values();	
	}

	/**
	 * Inspects candidate group to determine if it comprises instance of Format
	 * @return FormatIdentification for this group if it comprises instance of a Format;
	 *         otherwise returns null;
	 * @throws JHOVE2Exception
	 */
	protected FormatIdentification recognizeGroupedSource(GlobPathMatchInfoGroup fileGroup)
			throws JHOVE2Exception {
		FormatIdentification fi = null;
		if (fileGroup.getMustHaveCount() >= this.minMustHavesToIdentify){
			fi = new FormatIdentification(this.format, Confidence.PositiveGeneric, this);
			for (GlobPathMatchInfo sourceInfo:fileGroup.getSourceMatchInfoList()){
				if ((sourceInfo.isMustHave() || sourceInfo.isMayHave()) ||
				    (this.includeUnmatchedFromGroup)) {
					fi.getSources().add(sourceInfo.getSource());
				}				
			}
		}
		return fi;
	}
	
	protected void compilePatterns() throws JHOVE2Exception{
		try {
			this.fileGroupingPattern = Pattern.compile(this.fileGroupingExpr);
		}
		catch (PatternSyntaxException e){
			throw new JHOVE2Exception("Exception thrown compiling fileGroupingToken: " 
					+ this.fileGroupingExpr, e);
		}
		try {
			this.mustHavePattern = Pattern.compile(this.mustHaveExpr);
		}
		catch (PatternSyntaxException e){
			throw new JHOVE2Exception("Exception thrown compiling mustHaveToken: " 
					+ this.mustHaveExpr, e);
		}
		try {
			this.mayHavePattern = Pattern.compile(this.mayHaveExpr);
		}
		catch (PatternSyntaxException e){
			throw new JHOVE2Exception("Exception thrown compiling mayHaveToken: " 
					+ this.fileGroupingExpr, e);
		}
		return;
	}
	public Format getFormat() {
		return format;
	}
	public void setFormat(Format format) {
		this.format = format;
	}
	public String getFileGroupingExpr() {
		return fileGroupingExpr;
	}
	public void setFileGroupingExpr(String groupingExpr) {
		this.fileGroupingExpr = groupingExpr;
	}
	public String getMustHaveExpr() {
		return mustHaveExpr;
	}
	public void setMustHaveExpr(String mustHaveExpr) {
		this.mustHaveExpr = mustHaveExpr;
	}
	public String getMayHaveExpr() {
		return mayHaveExpr;
	}
	public void setMayHaveExpr(String mayHaveExpr) {
		this.mayHaveExpr = mayHaveExpr;
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
	public int getFileGroupingCaptureGroupIndex() {
		return fileGroupingCaptureGroupIndex;
	}
	public void setFileGroupingCaptureGroupIndex(int fileGroupingCaptureGroupIndex) {
		this.fileGroupingCaptureGroupIndex = fileGroupingCaptureGroupIndex;
	}

}
