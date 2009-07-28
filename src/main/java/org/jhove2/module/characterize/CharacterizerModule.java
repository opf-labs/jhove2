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
import java.util.List;
import java.util.Set;

import org.jhove2.core.Format;
import org.jhove2.core.FormatIdentification;
import org.jhove2.core.I8R;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.config.Configure;
import org.jhove2.core.source.AggregateSource;
import org.jhove2.core.source.ClumpSource;
import org.jhove2.core.source.Source;
import org.jhove2.module.AbstractModule;
import org.jhove2.module.digest.Digester;
import org.jhove2.module.dispatch.Dispatcher.Disposition;
import org.jhove2.module.identify.Aggrefier;
import org.jhove2.module.identify.Identifier;

/** JHOVE2 characterization module.  The characterization strategy is:
 * (1) presumptively-identify the format of the source unit;
 * (2) dispatch the source unit to the module associated with the format for
 * parsing, feature extraction, and validation;
 * (3) perform assessment on the source based on its extracted reportable
 * properties and validation status;
 * (4) if an aggregate source unit, (a) presumptively-identify any formats
 * found amongst its child source units, and if so, (b) gather those
 * source units into a Clump, and (c) dispatch to the module associated
 * with the format for feature extraction and validation;
 * (5) if a non-aggregate source unit, optionally calculate message digests.
 * 
 * @author mstrong, slabrams
 */
public class CharacterizerModule
	extends AbstractModule
	implements Characterizer
{
	/** Characterization process module version identifier. */
	public static final String VERSION = "1.0.0";

	/** Characterization process module release date. */
	public static final String RELEASE = "2009-07-17";
	
	/** Characterization process module rights statement. */
	public static final String RIGHTS =
		"Copyright 2009 by The Regents of the University of California, " +
		"Ithaka Harbors, Inc., and The Board of Trustees of the Leland " +
		"Stanford Junior University. " +
		"Available under the terms of the BSD license.";

	/** Instantiate a new <code>CharacterizerModule</code>.
	 */
	public CharacterizerModule() {
		super(VERSION, RELEASE, RIGHTS);
	}

	/** Characterize a source unit.  The characterization strategy is:
	 * (1) presumptively-identify the format of the source unit;
	 * (2) dispatch the source unit to the module associated with the format
	 * for parsing, feature extraction, and validation;
	 * (3) perform assessment on the source based on its extracted reportable
	 * properties and validation status;
	 * (4) if an aggregate source unit, (a) presumptively-identify any formats
	 * found amongst its child source units, and if so, (b) gather those
	 * source units into a Clump, and (c) dispatch to the module associated
	 * with the format for feature extraction and validation;
	 * (5) if a non-aggregate source unit, optionally calculate message
	 * digests. 
	 * @param jhove2 JHOVE2 framework
	 * @param source Source unit
	 * @throws IOException     I/O exception encountered characterizing the
	 *                         source unit
	 * @throws JHOVE2Exception
	 * @see org.jhove2.module.characterize.Characterizer#characterize(org.jhove2.core.JHOVE2, org.jhove2.core.source.Source)
	 */
	@Override
	public void characterize(JHOVE2 jhove2, Source source)
		throws IOException, JHOVE2Exception
	{
		/* (1) Presumptively-identify the source unit's format. */
		Identifier identifier =	Configure.getReportable(Identifier.class,
				                                        "IdentifierModule");
		if (identifier != null) {
			jhove2.dispatch(source, identifier);
			Set<FormatIdentification> formats = identifier.getPresumptiveFormats();
			if (formats.size() > 0) {
				/* (2) Dispatch the source unit to the module associated with
				 * format for parsing, feature extraction, and validation.
				 */
				for (FormatIdentification fid : formats) {
					Format format = fid.getPresumptiveFormat();
					I8R id = format.getIdentifier();
					jhove2.dispatch(source, id);
				}
			}
			
			/* TODO: implement characterization assessment */
			//Assessor assessor =
			//	Configure.getReportable(Assessor.class, "AssessorModule");
			//if (assessor != null) {
				/* (3) Perform assessment on the source based on its extracted
				 * reportable properties and validation status.
				 */
			//	jhove2.dispatch(source, assessor);
			//}
			//else {
			//    /* TODO: can't instantiate assessor module. */
			//}
		}
		else {
			/* TODO: can't instantiate identifier module. */
		}
		
		if (source instanceof AggregateSource) {
			/* (4) (a) Presumptively-identify any formats found amongst the
			 * child source units; and, if so, (b) gather the source units
			 * into a new Clump source unit; and (c) dispatch the Clump to
			 * the module associated with its format. */
			Aggrefier aggrefier = Configure.getReportable(Aggrefier.class,
					                                      "AggrefierModule");
			if (aggrefier != null) {
				jhove2.dispatch(source, aggrefier, Disposition.DontAddToSource);
				Set<FormatIdentification> formats = aggrefier.getPresumptiveFormats();
				if (formats.size() > 0) {
					ClumpSource clump = new ClumpSource();
					List<Source> sources = aggrefier.getSources();
					for (Source src : sources) {
						clump.addChildSource(src);
						source.deleteChildSource(src);
					}
					clump.addModule(aggrefier);
					
					for (FormatIdentification fid : formats) {
						Format format = fid.getPresumptiveFormat();
						I8R id = format.getIdentifier();
						jhove2.dispatch(clump, id);
					}
					source.addChildSource(clump);
				}
			}
		}
		else {
			/* (5) Optionally calculate message digests for the non-aggregate
			 * source unit.
			 */
			if (jhove2.getCalcDigests()) {
				Digester digester = Configure.getReportable(Digester.class,
						                                    "DigesterModule");
				if (digester != null) {
					jhove2.dispatch(source, digester);
				}
				else {
					/* TODO: can't instantiate digester module. */
				}
			}
		}
	}
}
