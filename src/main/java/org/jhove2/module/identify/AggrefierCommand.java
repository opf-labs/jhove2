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

import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.TimerInfo;
import org.jhove2.core.source.AggregateSource;
import org.jhove2.core.source.ClumpSource;
import org.jhove2.core.source.Source;
import org.jhove2.module.AbstractCommand;

/**
 * Command to execute identifier on AggregateSource objects, to detect any
 * presumptive {2link org.jhove2.core.source.ClumpSource} format instances
 * 
 * @author smorrissey
 */
public class AggrefierCommand
	extends AbstractCommand
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
	
	protected AggrefierFactory aggrefierFactory;

	/** Instantiate a new <code>AggrefierCommand</code>.
	 */
	public AggrefierCommand(){
		super(VERSION, RELEASE, RIGHTS, Scope.Generic);
	}

	/**
	 * If Source is an aggregate source, invokes aggregate identifier
	 * to detect possible ClumpSource instances in the aggregate;
	 * If any ClumpSources are found, performs call back to JHOVE2 framework
	 * to characterize them
	 * @param jhove2 JHOVE2 framework object
	 * @param source Source 
	 * @throws JHOVE2Exception
	 * @see org.jhove2.module.Command#execute(org.jhove2.core.JHOVE2, org.jhove2.core.source.Source)
	 */
	@Override
	public void execute(JHOVE2 jhove2, Source source)
		throws JHOVE2Exception
	{
		if (source instanceof AggregateSource){
			try {
				Aggrefier aggrefier = 
					this.getAggrefierFactory().makeAggrefier();
				TimerInfo timer = aggrefier.getTimerInfo();
				timer.setStartTime();
				try {
	                /* Register the aggrefying modules. */
	                jhove2.addModule(aggrefier);
	                List<Recognizer> recognizers = aggrefier.getRecognizers();
	                for (Recognizer recognizer : recognizers) {
	                    jhove2.addModule(recognizer);
	                }
	                
	                /* Identify the aggregate. */
					Set<ClumpSource> clumpSources = null;
					clumpSources = aggrefier.identify(jhove2, source);
					do {									
						for (ClumpSource clumpSource : clumpSources){
							/* Make clump child of source, and remove clump's
							 * children as direct children of source.
							 */
							source.addChildSource(clumpSource);				
							for (Source src:clumpSource.getChildSources()){
								source.deleteChildSource(src);					
							}
							/* Characterize the ClumpSource. */
							jhove2.characterize(clumpSource);
						}
						/* Determine if the addition of clump sources at this level
						 * results new possible clump sources.
						 */
						if (clumpSources.size()>0){
							/* aggregate identifier again on this source with
							 * its new child sources.
							 */
							clumpSources = aggrefier.identify(jhove2, source);
						}
					} while (clumpSources.size()>0);
				}
				finally {
					timer.setEndTime();
				}
			}
			catch (IOException e) {
				throw new JHOVE2Exception("AggrefierModule: IO exception", e);
			}
		}	
		return;
	}

	/**
	 * @return the aggrefierFactory
	 */
	public AggrefierFactory getAggrefierFactory() {
		return aggrefierFactory;
	}

	/**
	 * @param aggrefierFactory the aggrefierFactory to set
	 */
	public void setAggrefierFactory(AggrefierFactory aggrefierFactory) {
		this.aggrefierFactory = aggrefierFactory;
	}
}
