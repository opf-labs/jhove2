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

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;
import java.util.HashMap;
import java.util.regex.*;

import org.jhove2.core.I8R;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.FormatIdentification;
import org.jhove2.core.FormatIdentification.Confidence;
import org.jhove2.core.source.Source;
import org.jhove2.core.source.ClumpSource;
import org.jhove2.module.AbstractModule;

/**
 * Aggregate identifier that uses filepath globbing to detect instances of Clump Formats
 * Each instance of a GlobPathRecognizer can recognize instances of a single format
 * 
 * @author smorrissey
 */
public class GlobPathRecognizer
	extends AbstractModule
	implements Recognizer
{
	/** Identification module version identifier. */
	public static final String VERSION = "1.9.5";

	/** Identification module release date. */
	public static final String RELEASE = "2010-02-16";

	/** Identification module rights statement. */
	public static final String RIGHTS = "Copyright 2010 by The Regents of the University of California, "
		+ "Ithaka Harbors, Inc., and The Board of Trustees of the Leland "
		+ "Stanford Junior University. "
		+ "Available under the terms of the BSD license.";

	/** Aggrgate identifier confidence. */
	public static final Confidence GLOB_PATH_CONFIDENCE = Confidence.Tentative;

	/** Format which this recognizer can detect*/
	protected I8R format;

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
	public GlobPathRecognizer() {
		super(VERSION, RELEASE, RIGHTS, Scope.Generic);
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
	 * @param includeUnmatchedFromGroup boolean which indicates whether or not to include in a Source
	 *                           that is part of the FormatIdentification returned by this class any 
	 *                           files which match the grouping expression, but do not match either 
	 *                           mustHaveExpr or mayHaveExpr 
	 */
	public GlobPathRecognizer(I8R format, String fileGroupingExpr,
			String mustHaveExpr, String mayHaveExpr, 
			int fileGroupingCaptureGroupIndex,
			int mustHaveCaptureGroupIndex, 
			int mayHaveCaptureGroupIndex, 
			int minMustHavesToIdentify, 
			boolean includeUnmatchedFromGroup) {
		this();
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

	/** Aggregate identifier of the source unit.
	 * @param jhove2 JHOVE2 core framework
	 * @param source Source unit
	 * @return Recognized clump source units
	 * @see org.jhove2.module.identify.Aggrefier#identify(org.jhove2.core.JHOVE2, org.jhove2.core.source.Source)
	 */
	@Override
	public Set<ClumpSource> identify(JHOVE2 jhove2, Source source)
		throws IOException, JHOVE2Exception {
		Set<ClumpSource> clumpSources = 
			new TreeSet<ClumpSource>();
		this.compilePatterns();
		Collection<GlobPathMatchInfoGroup> sourceGroups = this.groupSources(source);
		for (GlobPathMatchInfoGroup sourceGroup:sourceGroups){
			ClumpSource clumpSource = this.recognizeGroupedSource(sourceGroup);
			if (clumpSource != null){
				clumpSources.add(clumpSource);
			}
		}
		return clumpSources;
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
			File sourceFile = childSource.getFile();
			if (sourceFile !=  null){
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
					boolean matchesMustHaves = false;
					Matcher m2 = null;
					if (this.mustHavePattern != null){
						m2 = this.mustHavePattern.matcher(mustHaveString);
						matchesMustHaves = m2.matches();
					}
					fileInfo.setMustHave(matchesMustHaves);
					boolean matchesMayHaves = false;
					if (this.mayHavePattern != null){
						m2 = this.mayHavePattern.matcher(mayHaveString);
						matchesMayHaves = m2.matches();
					}
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
			}//end if sourceFile != null
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
	protected ClumpSource recognizeGroupedSource(GlobPathMatchInfoGroup fileGroup)
	throws JHOVE2Exception {
		FormatIdentification fi = null;
		ClumpSource clumpSource = null;
		if (fileGroup.getMustHaveCount() >= this.minMustHavesToIdentify){
			fi = new FormatIdentification(this.format, GLOB_PATH_CONFIDENCE, 
					this.getReportableIdentifier());
			clumpSource = new ClumpSource();
			clumpSource.addPresumptiveFormat(fi);
			for (GlobPathMatchInfo sourceInfo:fileGroup.getSourceMatchInfoList()){
				if ((sourceInfo.isMustHave() || sourceInfo.isMayHave()) ||
						(this.includeUnmatchedFromGroup)) {
					clumpSource.addChildSource(sourceInfo.getSource());
				}	
			}
		}
		return clumpSource;
	}
	
	/**
	 * Compiles regular expression patterns used in globbing
	 * @throws JHOVE2Exception
	 */
	protected void compilePatterns() throws JHOVE2Exception{
		try {
			this.fileGroupingPattern = Pattern.compile(this.fileGroupingExpr);
		}
		catch (PatternSyntaxException e){
			throw new JHOVE2Exception("Exception thrown compiling fileGroupingToken: " 
					+ this.fileGroupingExpr, e);
		}
		if (this.mustHaveExpr != null){
			try {
				this.mustHavePattern = Pattern.compile(this.mustHaveExpr);
			}
			catch (PatternSyntaxException e){
				throw new JHOVE2Exception("Exception thrown compiling mustHaveToken: " 
						+ this.mustHaveExpr, e);
			}
		}
		if (this.mayHaveExpr != null){
			try {
				this.mayHavePattern = Pattern.compile(this.mayHaveExpr);
			}
			catch (PatternSyntaxException e){
				throw new JHOVE2Exception("Exception thrown compiling mayHaveToken: " 
						+ this.fileGroupingExpr, e);
			}
		}
		return;
	}
	
	/**
	 * Get identifier for  Format which this recognizer can detect
	 * @return Format which this recognizer can detect
	 */
	public I8R getFormatIdentifier() {
		return format;
	}
	
	/**
	 * Sets identifier for Format which this recognizer can detect
	 * @param format Format which this recognizer can detect
	 */
	public void setFormatIdentifier(I8R format) {
		this.format = format;
	}
	
	/**
	 * Get String containing regular expression to group candidate files
	 * @return String containing regular expression to group candidate files
	 */
	public String getFileGroupingExpr() {
		return fileGroupingExpr;
	}
	
	/**
	 * Set String containing regular expression to group candidate files
	 * @param groupingExpr Regular expression to group candidate files
	 */
	public void setFileGroupingExpr(String groupingExpr) {
		this.fileGroupingExpr = groupingExpr;
	}
	
	/**
	 * Get String containing regular expression to identify required candidate files
	 * @return Regular expression to identify required candidate files
	 */
	public String getMustHaveExpr() {
		return mustHaveExpr;
	}
	
	/**
	 * Sets String containing regular expression to identify required candidate files
	 * @param mustHaveExpr Regular expression to identify required candidate files
	 */
	public void setMustHaveExpr(String mustHaveExpr) {
		this.mustHaveExpr = mustHaveExpr;
	}
	
	/**
	 * Gets String containing regular expression to identify optional candidate files
	 * @return Regular expression to identify optional candidate files
	 */
	public String getMayHaveExpr() {
		return mayHaveExpr;
	}
	
	/**
	 * Sets String containing regular expression to identify optional candidate files
	 * @param mayHaveExpr Regular expression to identify optional candidate files
	 */
	public void setMayHaveExpr(String mayHaveExpr) {
		this.mayHaveExpr = mayHaveExpr;
	}
	
	/**
	 * Get minimum number of files that must match the mustHaveExpr in order for 
	 * a set of Sources to be considered an instance of an aggregate Format.  
	 * Allows us to identify potentially defective instances of a format
	 * 
	 * @return minMustHavesToIdentify
	 */
	public int getMinMustHavesToIdentify() {
		return minMustHavesToIdentify;
	}
	
	/**
	 * Sets minimum number of files that must match the mustHaveExpr in order for 
	 * a set of Sources to be considered an instance of an aggregate Format.  
	 * Allows us to identify potentially defective instances of a format
	 * 
	 * @param minMustHavesToIdentify
	 */
	public void setMinMustHavesToIdentify(int minMustHavesToIdentify) {
		this.minMustHavesToIdentify = minMustHavesToIdentify;
	}
	
	/**
	 * Get indicator as to whether or not to include in the Source that is part of the 
	 *  FormatIdentification returned by this class any files which match the grouping expression, 
	 *  but do not match either must mustHaveExpr or mayHaveExpr
	 *  
	 * @return boolean
	 */
	public boolean isIncludeUnmatchedFromGroup() {
		return includeUnmatchedFromGroup;
	}
	
	/**
	 * Sets indicator as to whether or not to include in the Source that is part of the 
	 *  FormatIdentification returned by this class any files which match the grouping expression, 
	 *  but do not match either must mustHaveExpr or mayHaveExpr
	 *  
	 * @param includeUnmatchedFromGroup
	 */
	public void setIncludeUnmatchedFromGroup(boolean includeUnmatchedFromGroup) {
		this.includeUnmatchedFromGroup = includeUnmatchedFromGroup;
	}
	
	/**
	 *  Gets capture group index in fileGroupingToken which captures the part of
	 *  the file path which we will be comparing for "must-have" files 
	 *  
	 * @return mustHaveCaptureGroupIndex
	 */
	public int getMustHaveCaptureGroupIndex() {
		return mustHaveCaptureGroupIndex;
	}
	/**
	 *  Sets capture group index in fileGroupingToken which captures the part of
	 *  the file path which we will be comparing for "must-have" files 
	 *  
	 * @param mustHaveCaptureGroupIndex
	 */
	public void setMustHaveCaptureGroupIndex(int mustHaveCaptureGroupIndex) {
		this.mustHaveCaptureGroupIndex = mustHaveCaptureGroupIndex;
	}
	
	/**
	 * Get capture group index in fileGroupingToken which captures the part of
	 *  the file path which we will be comparing for "may-have" files 
	 *  
	 * @return mayHaveCaptureGroupIndex
	 */
	public int getMayHaveCaptureGroupIndex() {
		return mayHaveCaptureGroupIndex;
	}
	
	/**
	 * Set capture group index in fileGroupingToken which captures the part of
	 *  the file path which we will be comparing for "may-have" files 
	 *  
	 * @param mayHaveCaptureGroupIndex
	 */
	public void setMayHaveCaptureGroupIndex(int mayHaveCaptureGroupIndex) {
		this.mayHaveCaptureGroupIndex = mayHaveCaptureGroupIndex;
	}
	
	/**
	 * Get capture group index in fileGroupingToken which captures the part of
	 *  the file path which indicates related files 
	 *  
	 * @return fileGroupingCaptureGroupIndex
	 */
	public int getFileGroupingCaptureGroupIndex() {
		return fileGroupingCaptureGroupIndex;
	}
	
	/**
	 * Set capture group index in fileGroupingToken which captures the part of
	 *  the file path which indicates related files
	 *  
	 * @param fileGroupingCaptureGroupIndex
	 */
	public void setFileGroupingCaptureGroupIndex(int fileGroupingCaptureGroupIndex) {
		this.fileGroupingCaptureGroupIndex = fileGroupingCaptureGroupIndex;
	}
}
