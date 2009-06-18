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

import org.jhove2.core.AbstractModule;
import org.jhove2.core.Format;
import org.jhove2.core.FormatIdentification;
import org.jhove2.core.Identifiable;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.Product;
import org.jhove2.core.FormatIdentification.Confidence;
import org.jhove2.core.config.Configure;
import org.jhove2.core.io.Input;
import org.jhove2.core.source.AggregateSource;
import org.jhove2.core.source.ClumpSource;
import org.jhove2.core.source.DirectorySource;
import org.jhove2.core.source.FileSource;
import org.jhove2.core.source.Source;
import org.jhove2.core.source.ZipDirectorySource;

/** JHOVE2 identification module.
 * 
 * @author mstrong, slabrams
 */
public class IdentifierModule
	extends AbstractModule
	implements Identifiable
{
	/**Identification module version identifier. */
	public static final String VERSION = "2.0.0";

	/** Identification module release date. */
	public static final String DATE = "2009-06-16";
	
	/** Identification module rights statement. */
	public static final String RIGHTS =
		"Copyright 2009 by The Regents of the University of California, " +
		"Ithaka Harbors, Inc., and The Board of Trustees of the Leland " +
		"Stanford Junior University. " +
		"Available under the terms of the BSD license.";
	
	/** DROID product information. */
	protected static final Product droid = new DROIDWrapper();

	/** Presumptively identified formats. */
	protected Set<FormatIdentification> formats;
	
	/** Instantiate a new <code>IdentifierModule</code>.
	 */
	public IdentifierModule() {
		super(VERSION, DATE, RIGHTS);
		
		this.formats = new TreeSet<FormatIdentification>();
	}

	/** Presumptively identify the format of a source unit.  Implicitly set
	 * the start and ending elapsed time.
	 * @param jhove2 JHOVE2 framework
	 * @param source Source unit
	 * @return Presumptively identified formats
	 * @throws IOException     If an I/O exception is raised reading the
	 *                         source unit
	 * @throws JHOVE2Exception
	 * @see org.jhove2.core.Identifiable#getFormats()
	 */
	@Override
	public Set<FormatIdentification> identify(JHOVE2 jhove2, Source source)
			throws IOException, JHOVE2Exception
	{
		setStartTime();
		if (source instanceof AggregateSource) {
			if (source instanceof ClumpSource) {
				/* Identify clump source unit. */
				FormatIdentification id =
					new FormatIdentification(Configure.getReportable(Format.class,
								                                     "ClumpFormat"),
						                     Confidence.PositiveSpecific);
				this.formats.add(id);
			}
			else if (source instanceof DirectorySource ||
					 source instanceof ZipDirectorySource) {
				/* Identify directory source unit. */
				FormatIdentification id =
					new FormatIdentification(Configure.getReportable(Format.class,
							                                         "DirectoryFormat"),
							                 Confidence.PositiveSpecific);
				this.formats.add(id);
			}
		}
		else {
			/* Identify file source unit. */
			FileSource fileSource = (FileSource) source;
			if (fileSource.isExtant() && fileSource.isReadable()) {
				
				/* TODO: implement DROID. */
				Input input = fileSource.getInput(jhove2.getBufferSize(),
						                          jhove2.getBufferType());
				/* Test for Zip. */
				if (input != null) {
					byte [] zip = new byte[] {0x50, 0x4b, 0x03, 0x04};
					boolean isZip = true;
					for (int i=0; i<4; i++) {
						short b = input.readUnsignedByte();
						if (b != zip[i]) {
							isZip = false;
							break;
						}
					}
					
					FormatIdentification id = null;
					if (isZip) {
						id = new FormatIdentification(Configure.getReportable(Format.class,
										                                      "ZipFormat"),
								                      Confidence.PositiveGeneric,
								                      droid);
					}
					else {
						/* Default to UTF-8. */
						id = new FormatIdentification(Configure.getReportable(Format.class,
							                                                  "UTF8Format"),
						                              Confidence.PositiveGeneric,
						                              droid);
					}
					this.formats.add(id);
				}
			}
		}
		setEndTime();
		
		return this.formats;
	}

	/** Get presumptive format identifications.
	 * @return Presumptive format identifications
	 * @see org.jhove2.core.Identifiable#identify(org.jhove2.core.JHOVE2, org.jhove2.core.io.Input)
	 */
	@Override
	public Set<FormatIdentification> getFormats() {
		return this.formats;
	}
}
