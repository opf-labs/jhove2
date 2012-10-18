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

package org.jhove2.module.aggrefy;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.source.ClumpSource;
import org.jhove2.core.source.Source;
import org.jhove2.module.AbstractModule;
import org.jhove2.persist.AggrefierAccessor;

import com.sleepycat.persist.model.Persistent;

/**
 * JHOVE2 aggregate identifier module.
 * Identifies presumptive instances of Clump formats (e.g. ShapeFiles) in
 * an aggregate Source, and performs callback to JHOVE2 framework 
 * to characterize those Clump format instances
 * 
 * @author smorrissey
 */
@Persistent
public class AggrefierModule
	extends AbstractModule 
	implements Aggrefier
{
	/** Identification module version identifier. */
	public static final String VERSION = "2.1.0";
	/** Identification module release date. */
	public static final String RELEASE = "2012-10-31";
	/** Identification module rights statement. */
	public static final String RIGHTS = "Copyright 2010 by The Regents of the University of California, "
		+ "Ithaka Harbors, Inc., and The Board of Trustees of the Leland "
		+ "Stanford Junior University. "
		+ "Available under the terms of the BSD license.";

	/**
	 * Instantiate a new <code>AggrefierModule</code>.
	 */
	public AggrefierModule() {
		this(null);
	}
	/**
	 * Instantiate a new <code>AggrefierModule</code>.  
	 * @param aggrefierAccessor AggrefierAccessor to manage access to (possibly persisted) Recognizers
	 */
	public AggrefierModule(AggrefierAccessor aggrefierAccessor){
		super(VERSION, RELEASE, RIGHTS, Scope.Generic, aggrefierAccessor);
	}
	
	/**
	 * Detect presumptive instances of a clump format in a source unit, and identify
	 * Note that the child sources of the Source being inspected for the presence of
	 * aggregates may be presumptively assigned to more than one clump format.
	 *
	 * @param jhove2 JHOVE2 framework
	 * @param source Aggregate source unit
	 * @return Presumptively identified Clump sources
	 * @throws IOException
	 *             I/O exception encountered identifying the source unit
	 * @throws JHOVE2Exception
	 * @see org.jhove2.module.aggrefy.Aggrefier#identify(org.jhove2.core.JHOVE2,
	 * org.jhove2.core.source.Source)
	 */
	@Override
	public Set<ClumpSource> identify(JHOVE2 jhove2, Source source)
		throws IOException, JHOVE2Exception
	{
		Set<ClumpSource> clumpSources = 
			new TreeSet<ClumpSource>();
		for (Recognizer recognizer:this.getRecognizers()) {	
			recognizer = (Recognizer) recognizer.getModuleAccessor().startTimerInfo(recognizer);
			clumpSources.addAll((Collection<? extends ClumpSource>) recognizer.recognize(jhove2, source));
			recognizer = (Recognizer) recognizer.getModuleAccessor().endTimerInfo(recognizer);
		}	
		return clumpSources;
	}

	/**
	 * Accessor for clump format instance identifiers.
	 *
	 * @return Aggregate recognizers
	 */
	@Override
	public List<Recognizer> getRecognizers() throws JHOVE2Exception {
		if (this.getModuleAccessor()==null){
			throw new JHOVE2Exception("AggrefierAccessor is null");
		}
		AggrefierAccessor aa = (AggrefierAccessor)this.getModuleAccessor();
		return aa.getRecognizers(this);
	}

	/**
	 * Mutator for clump format instance identifiers.
	 *
	 * @param recognizers the new recognizers
	 */
	public void setRecognizers(List<Recognizer> recognizers) throws JHOVE2Exception {		
		if (this.getModuleAccessor()==null){
			throw new JHOVE2Exception("AggrefierAccessor is null");
		}		
//		List<Recognizer> oldRecogs = this.getRecognizers();
//		if (oldRecogs != null){
//			for (Recognizer recog:oldRecogs){
//				this.deleteRecognizer(recog);
//			}
//		}
//		if (recognizers != null){
//			for (Recognizer recog:recognizers){
//				this.addRecognizer(recog);
//			}
//		}
		AggrefierAccessor aa = (AggrefierAccessor) this.getModuleAccessor();
		aa.setRecognizers(this, recognizers);
		return;
	}
	@Override
	public Recognizer addRecognizer(Recognizer recognizer)
			throws JHOVE2Exception {
		if (this.getModuleAccessor()==null){
			throw new JHOVE2Exception("AggrefierAccessor is null");
		}
		AggrefierAccessor aa = (AggrefierAccessor)this.getModuleAccessor();
		return aa.addRecognizer(this, recognizer);
	}
	@Override
	public Recognizer deleteRecognizer(Recognizer recognizer)
			throws JHOVE2Exception {
		if (this.getModuleAccessor()==null){
			throw new JHOVE2Exception("AggrefierAccessor is null");
		}
		AggrefierAccessor aa = (AggrefierAccessor)this.getModuleAccessor();
		return aa.deleteRecognizer(this, recognizer);
	}

}
