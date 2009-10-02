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

import org.jhove2.core.FormatIdentification;
import org.jhove2.core.I8R;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.FormatIdentification.Confidence;
import org.jhove2.core.source.ClumpSource;
import org.jhove2.core.source.DirectorySource;
import org.jhove2.core.source.FileSetSource;
import org.jhove2.core.source.Source;
import org.jhove2.core.source.ZipDirectorySource;
import org.jhove2.module.AbstractModule;

/**
 * JHOVE2 identification module for non-Clump sources
 * @author mstrong, slabrams, smorrissey
 */
public class IdentifierModule extends AbstractModule 
implements Identifier {
	/** Identification module version identifier. */
	public static final String VERSION = "1.0.0";

	/** Identification module release date. */
	public static final String RELEASE = "2009-09-09";

	/** Identification module rights statement. */
	public static final String RIGHTS = "Copyright 2009 by The Regents of the University of California, "
		+ "Ithaka Harbors, Inc., and The Board of Trustees of the Leland "
		+ "Stanford Junior University. "
		+ "Available under the terms of the BSD license.";
	
	/** bean name for identifier for non-clump, directory, zip etc. sources  (for example, DROID)
	 *  We will need a new instance of the identifier for each source, so we do not have
	 *  an Identifier member here; instead we use the bean name to instantiate a
	 *  new Identifier as needed */
	
	protected Identifier fileSourceIdentifier;
	
	/**
	 * Instantiate a new <code>IdentifierModule</code>.
	 */
	public IdentifierModule() {
		super(VERSION, RELEASE, RIGHTS);
	}


	/**
	 * Presumptively identify the format of a source unit.
	 * 
	 * @param jhove2
	 *            JHOVE2 framework
	 * @param source
	 *            Source unit
	 * @return Presumptively identified presumptiveFormatIds
	 * @throws IOException
	 *             I/O exception encountered identifying the source unit
	 * @throws JHOVE2Exception
	 * @see org.jhove2.module.identify.Identifier#identify(org.jhove2.core.JHOVE2,
	 *      org.jhove2.core.source.Source)
	 */
	@Override
	public Set<FormatIdentification> identify(JHOVE2 jhove2, Source source)
	throws IOException, JHOVE2Exception {
		Set<FormatIdentification> presumptiveFormatIds = 
			new TreeSet<FormatIdentification>();
		if (source instanceof ClumpSource) {
			// ClumpSources are only created when identified as instances
			// of a particular clump format, so should have identifications
			// already
			;
		} else if (source instanceof DirectorySource
				|| source instanceof ZipDirectorySource) {
			FormatIdentification id = new FormatIdentification(
					new I8R("info:jhove2/format/directory"),
					Confidence.PositiveSpecific, this.getJhove2Identifier());
			presumptiveFormatIds.add(id);
		} else if (source instanceof FileSetSource) {
			FormatIdentification id = new FormatIdentification(
					new I8R("info:jhove2/format/file-set"),
					Confidence.PositiveSpecific, this.getJhove2Identifier());
			presumptiveFormatIds.add(id);
		} else {
			/* Identify file source unit. */						
			fileSourceIdentifier.getTimerInfo().setStartTime();
			presumptiveFormatIds.addAll(
					fileSourceIdentifier.identify(jhove2, source));
			fileSourceIdentifier.getTimerInfo().setEndTime();
			source.addModule(fileSourceIdentifier);
		}
		return presumptiveFormatIds;
	}

	/**
	 * Accessor for fileSourceIdentifierBeanName
	 * @return fileSourceIdentifierBeanName
	 */
	public Identifier getFileSourceIdentifier() {
		return fileSourceIdentifier;
	}

	/**
	 * Mutator for fileSourceIdentifierBeanName
	 * @param fileSourceIdentifierBeanName
	 */
	public void setFileSourceIdentifier(Identifier fileSourceIdentifier) {
		this.fileSourceIdentifier = fileSourceIdentifier;
	}

}
