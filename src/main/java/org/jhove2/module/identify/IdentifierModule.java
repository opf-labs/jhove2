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
import java.util.Set;
import java.util.TreeSet;

import org.jhove2.core.I8R;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.format.FormatIdentification;
import org.jhove2.core.format.FormatIdentification.Confidence;
import org.jhove2.core.io.Input;
import org.jhove2.core.source.ClumpSource;
import org.jhove2.core.source.DirectorySource;
import org.jhove2.core.source.FileSetSource;
import org.jhove2.core.source.Source;
import org.jhove2.module.AbstractModule;
import org.jhove2.persist.IdentifierAccessor;

import com.sleepycat.persist.model.Persistent;

/**
 * JHOVE2 identifier module for non-Clump sources
 * @author mstrong, slabrams, smorrissey
 */
@Persistent
public class IdentifierModule
extends AbstractModule 
implements Identifier
{
	/** Identification module version identifier. */
	public static final String VERSION = "2.0.0";

	/** Identification module release date. */
	public static final String RELEASE = "2010-09-10";

	/** Identification module rights statement. */
	public static final String RIGHTS = "Copyright 2010 by The Regents of the University of California, "
		+ "Ithaka Harbors, Inc., and The Board of Trustees of the Leland "
		+ "Stanford Junior University. "
		+ "Available under the terms of the BSD license.";


	/** flag to indicate bypass of Identification if Source is pre-identified */
	protected boolean shouldSkipIdentifyIfPreIdentified;

	/**
	 * Instantiate a new <code>IdentifierModule</code>.
	 */
	public IdentifierModule() {
		this(null);
	}

	/**
	 * Instantiate a new <code>IdentifierModule</code>.
	 * @param identifierAccessor IdentifierAccessor
	 */
	public IdentifierModule(IdentifierAccessor identifierAccessor) {
		super(VERSION, RELEASE, RIGHTS, Scope.Generic, identifierAccessor);
	}
	/**
	 * Presumptively identify the format of a source unit.
	 * 
	 * @param jhove2
	 *            JHOVE2 framework
	 * @param source
	 *            Source unit
	 * @param input
	 *            Source input
	 * @return Presumptively identified presumptiveFormatIds
	 * @throws IOException
	 *             I/O exception encountered identifying the source unit
	 * @throws JHOVE2Exception
	 * @see org.jhove2.module.identify.Identifier#identify(org.jhove2.core.JHOVE2,
	 *      org.jhove2.core.source.Source)
	 */
	@Override
	public Set<FormatIdentification> identify(JHOVE2 jhove2, Source source,
			Input input)
			throws IOException, JHOVE2Exception
			{
		Set<FormatIdentification> presumptiveFormatIDs = 
			new TreeSet<FormatIdentification>();
		Set<FormatIdentification> existingIds = source.getPresumptiveFormats();
		boolean preIdentified = (existingIds != null && existingIds.size()> 0);
		if (!(preIdentified && this.shouldSkipIdentifyIfPreIdentified)){
			if (source instanceof ClumpSource) {
				/* ClumpSources are only created when identified as instances
				 * of a particular clump format, so should have identifications
				 * already.
				 */
				;
			}
			else if (source instanceof DirectorySource) {
				FormatIdentification id =
					new FormatIdentification(new I8R(I8R.JHOVE2_PREFIX + "/" +
							I8R.JHOVE2_FORMAT_INFIX +
					"/directory"),
					Confidence.PositiveSpecific,
					this.getReportableIdentifier());
				presumptiveFormatIDs.add(id);
			}
			else if (source instanceof FileSetSource) {
				FormatIdentification id =
					new FormatIdentification(new I8R(I8R.JHOVE2_PREFIX + "/" +
							I8R.JHOVE2_FORMAT_INFIX +
					"/file-set"),
					Confidence.PositiveSpecific,
					this.getReportableIdentifier());
				presumptiveFormatIDs.add(id);
			}
			else {   /* Identify file source unit. */	
				SourceIdentifier sourceIdentifier = this.getSourceIdentifier();
				sourceIdentifier = (SourceIdentifier) sourceIdentifier.getModuleAccessor().startTimerInfo(sourceIdentifier);
				try {
					Set<FormatIdentification> formats =
						sourceIdentifier.identify(jhove2, source, input);
					presumptiveFormatIDs.addAll(formats);
				}
				finally {
					sourceIdentifier = (SourceIdentifier) sourceIdentifier.getModuleAccessor().endTimerInfo(sourceIdentifier);
				}
			}
		}
		return presumptiveFormatIDs;
	}

	/**
	 * Get file source identifier module.
	 * @return File source identifier module
	 */
	@Override
	public SourceIdentifier getSourceIdentifier()  
	throws JHOVE2Exception{
		if (this.getModuleAccessor()==null){
			throw new JHOVE2Exception("IdentifierAccessor is null");
		}
		IdentifierAccessor ia = (IdentifierAccessor) this.getModuleAccessor();
		return ia.getFileSourceIdentifier(this);
	}

	/**
	 * Set file source identifier module.
	 * @param sourceIdentifier File source identifier module
	 */
	public SourceIdentifier setSourceIdentifier(SourceIdentifier fileSourceIdentifier)  
	throws JHOVE2Exception{
		if (this.getModuleAccessor()==null){
			throw new JHOVE2Exception("IdentifierAccessor is null");
		}
		IdentifierAccessor ia = (IdentifierAccessor) this.getModuleAccessor();
		return ia.setFileSourceIdentifier(this, fileSourceIdentifier);
	}

	/**
	 * @return the shouldSkipIdentifyIfPreIdentified
	 */
	@Override
	public boolean isShouldSkipIdentifyIfPreIdentified() {
		return shouldSkipIdentifyIfPreIdentified;
	}

	/**
	 * @param shouldSkipIdentifyIfPreIdentified the shouldSkipIdentifyIfPreIdentified to set
	 */
	@Override
	public void setShouldSkipIdentifyIfPreIdentified(
			boolean shouldSkipIdentifyIfPreIdentified) {
		this.shouldSkipIdentifyIfPreIdentified = shouldSkipIdentifyIfPreIdentified;
	}


}
