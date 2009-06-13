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

package org.jhove2.module.characterize;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jhove2.core.AbstractModule;
import org.jhove2.core.Characterizable;
import org.jhove2.core.Digestable;
import org.jhove2.core.FormatIdentification;
import org.jhove2.core.Identifiable;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.io.Input;
import org.jhove2.core.source.AggregateSource;
import org.jhove2.core.source.ClumpSource;
import org.jhove2.core.source.DirectorySource;
import org.jhove2.core.source.FileSource;
import org.jhove2.core.source.Source;
import org.jhove2.core.spring.Configure;

/** JHOVE2 characterization module.
 * 
 * @author mstrong, slabrams
 */
public class CharacterizerModule
	extends AbstractModule
	implements Characterizable
{
	/** Characterization process module version identifier. */
	public static final String VERSION = "1.0.0";

	/** Characterization process module release date. */
	public static final String DATE = "2009-06-13";
	
	/** Characterization process module rights statement. */
	public static final String RIGHTS =
		"Copyright 2009 by The Regents of the University of California, " +
		"Ithaka Harbors, Inc., and The Board of Trustees of the Leland " +
		"Stanford Junior University. " +
		"Available under the terms of the BSD license.";
	
	/** Instantiate a new <code>CharacterizerModule</code>.
	 * @param identifier Identification module
	 * @param digester   Message digesting module
	 */
	public CharacterizerModule() {
		super(VERSION, DATE, RIGHTS);
	}

	/** Characterize a source unit.
	 * @param jhove2 JHOVE2 framework
	 * @param source Source unit
	 * @throws IOException     If an I/O exception is raised characterizing
	 *                         the source unit
	 * @throws JHOVE2Exception
	 * @see org.jhove2.core.Characterizable#characterize(org.jhove2.core.JHOVE2, org.jhove2.core.source.Source)
	 */
	@Override
	public void characterize(JHOVE2 jhove2, Source source)
		throws IOException, JHOVE2Exception
	{
		/* Update summary counts of source units, by type. */
		if      (source instanceof ClumpSource) {
			jhove2.incrementNumClumps();
		}
		else if (source instanceof DirectorySource) {
			jhove2.incrementNumDirectories();
		}
		else if (source instanceof FileSource) {
			jhove2.incrementNumFiles();
		}
	
		if (source instanceof AggregateSource) {
			List<Source> list = ((AggregateSource) source).getChildSources();
			Iterator<Source> iter = list.iterator();
			while (iter.hasNext()) {
				Source src = iter.next();
				characterize(jhove2, src);
			}
			
			/* TODO: aggregate identification, etc. */
		}
		else {
			Input input = source.getInput();
			if (input != null) {
				/* Presumptively identifier the format(s) of the source unit. */
				Identifiable identifier =
					(Identifiable) Configure.getReportable("IdentifierModule");
				identifier.setStartTime();
				Set<FormatIdentification> formats =
					identifier.identify(jhove2, input);
				identifier.setEndTime();
				source.addModule(identifier);
			
				/* TODO: Parse and validate the source unit. */
			
				/** Calculate message digest(s) for the source unit. */
				Digestable digester =
					(Digestable) Configure.getReportable("DigesterModule");
				digester.setStartTime();
				digester.digest(jhove2, input);
				digester.setEndTime();
				source.addModule(digester);
			}
			else {
				/* TODO: what to do if no input? */
			}
		}
	}
}
