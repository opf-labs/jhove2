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

import org.jhove2.core.Format;
import org.jhove2.core.FormatIdentification;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.FormatIdentification.Confidence;
import org.jhove2.core.config.Configure;
import org.jhove2.core.source.AggregateSource;
import org.jhove2.core.source.NamedSource;
import org.jhove2.core.source.Source;
import org.jhove2.module.AbstractModule;

/** JHOVE2 aggregate identification module.
 * 
 * @author mstrong, slabrams
 */
public class AggrefierModule
	extends AbstractModule
	implements Aggrefier
{
	/**Identification module version identifier. */
	public static final String VERSION = "1.0.0";

	/** Identification module release date. */
	public static final String RELEASE = "2009-07-17";
	
	/** Identification module rights statement. */
	public static final String RIGHTS =
		"Copyright 2009 by The Regents of the University of California, " +
		"Ithaka Harbors, Inc., and The Board of Trustees of the Leland " +
		"Stanford Junior University. " +
		"Available under the terms of the BSD license.";

	/** Presumptively identified formats. */
	protected Set<FormatIdentification> formats;
	
	/** Instantiate a new <code>AggrefierModule</code>.
	 */
	public AggrefierModule() {
		super(VERSION, RELEASE, RIGHTS);
		
		this.formats = new TreeSet<FormatIdentification>();
	}

	/** Presumptively identify the format of an aggregate source unit.
	 * @param jhove2 JHOVE2 framework
	 * @param source Aggregate source unit
	 * @return Presumptively identified formats
	 * @throws IOException     I/O exception encountered identifying the
	 *                         source unit
	 * @throws JHOVE2Exception
	 * @see org.jhove2.module.identify.Aggrefier#identify(org.jhove2.core.JHOVE2, org.jhove2.core.source.AggregateSource)
	 */
	@Override
	public Set<FormatIdentification> identify(JHOVE2 jhove2, AggregateSource source)
		throws IOException, JHOVE2Exception
	{
		List<Source> children = source.getChildSources();
		
		/** Presumptively-identify a shape file by looking for three files
		 * with the names: abc.shp, abc.shx, abc.dbf
		 */
		Source dbfSource = null;
		Source shpSource = null;
		Source shxSource = null;
		if (children.size() > 2) {
			for (Source src : children) {
				if (!(src instanceof AggregateSource) &&
					  src instanceof NamedSource) {
					String name = ((NamedSource) src).getName();
					if (name.equals("abc.dbf")) {
						dbfSource = src;
					}
					else if (name.equals("abc.shp")) {
						shpSource = src;
					}
					else if (name.equals("abc.shx")) {
						shxSource = src;
					}
				}
			}
		}
		if (dbfSource != null && shpSource != null && shxSource != null) {
			FormatIdentification id =
				new FormatIdentification(Configure.getReportable(Format.class,
						                                         "ShapefileFormat"),
						                 Confidence.PositiveSpecific);			
			id.setSource(dbfSource);
			id.setSource(shpSource);
			id.setSource(shxSource);
			this.formats.add(id);

		}
		
		return this.formats;
	}

	/** Get presumptive format identifications.
	 * @return Presumptive format identifications
	 * @see org.jhove2.module.identify.Identifier#getPresumptiveFormats()
	 */
	@Override
	public Set<FormatIdentification> getPresumptiveFormats() {
		return this.formats;
	}
}
