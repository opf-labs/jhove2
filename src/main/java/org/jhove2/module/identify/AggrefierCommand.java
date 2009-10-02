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

import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Command;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.config.Configure;
import org.jhove2.core.source.AggregateSource;
import org.jhove2.core.source.ClumpSource;
import org.jhove2.core.source.Source;
import org.jhove2.module.AbstractModule;

/**
 * JHOVE2Command to execute identification on AggregateSource objects, to detect any
 * presumptive Clump format instances
 * 
 * @author smorrissey
 *
 */
public class AggrefierCommand extends AbstractModule implements JHOVE2Command {
	/** Identification module version identifier. */
	public static final String VERSION = "1.0.0";
	/** Identification module release date. */
	public static final String RELEASE = "2009-08-21";
	/** Identification module rights statement. */
	public static final String RIGHTS = "Copyright 2009 by The Regents of the University of California, "
		+ "Ithaka Harbors, Inc., and The Board of Trustees of the Leland "
		+ "Stanford Junior University. "
		+ "Available under the terms of the BSD license.";

	/**
	 * Constructor
	 */
	public AggrefierCommand(){
		this(VERSION, RELEASE, RIGHTS);
	}
	
	/**
	 * Constructor
	 * @param version Version of this module
	 * @param release Release date of this module
	 * @param rights  Rights statement for this module
	 */
	public AggrefierCommand(String version, String release, String rights) {
		super(version, release, rights);
	}

	/**
	 * If Source is an aggregate source, invokes aggregate identification
	 * to detect possible ClumpSource instances in the aggregate;
	 * If any ClumpSources are found, performs call back to JHOVE2 framework
	 * to characterize them
	 * @param source Source 
	 * @param jhove2 JHOVE2 framework object
	 * @throws JHOVE2Exception
	 * @see org.jhove2.core.JHOVE2Command#execute(org.jhove2.core.source.Source, org.jhove2.core.JHOVE2)
	 */
	@Override
	public void execute(Source source, JHOVE2 jhove2) throws JHOVE2Exception {
		source.addModule(this);
		if (source instanceof AggregateSource){
			try {
				AggregateIdentifier aggrefier = 
					Configure.getReportable(AggregateIdentifier.class, "Aggrefier");
				aggrefier.getTimerInfo().setStartTime();
				Set<ClumpSource> clumpSources = aggrefier.identify(jhove2, source);
				aggrefier.getTimerInfo().setEndTime();
				source.addModule(aggrefier);
				for (ClumpSource clumpSource : clumpSources){
					// make clump child of source, and remove clump's children
					// as direct children of source
					source.addChildSource(clumpSource);				
					for (Source src:clumpSource.getChildSources()){
						source.deleteChildSource(src);					
					}
					// now characterize the ClumpSource
					jhove2.characterize(clumpSource);
				}
			} catch (IOException e) {
				throw new JHOVE2Exception("AggrefierModule: IO exception", e);
			}
		}	
		return;
	}
	
}
