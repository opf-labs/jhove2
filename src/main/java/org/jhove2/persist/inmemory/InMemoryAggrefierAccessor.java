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
package org.jhove2.persist.inmemory;

import java.util.ArrayList;
import java.util.List;

import org.jhove2.core.JHOVE2Exception;
import org.jhove2.module.aggrefy.Aggrefier;
import org.jhove2.module.aggrefy.Recognizer;
import org.jhove2.persist.AggrefierAccessor;


/**
 * @author smorrissey
 *
 */

public class InMemoryAggrefierAccessor extends InMemoryBaseModuleAccessor
	implements AggrefierAccessor {
	

	/** list of configured AggregateIdentifiers that can detect instances 
	 * of an aggregate format.  Each identifier recognizes exactly one format.
     */
	protected List<Recognizer> recognizers;
	
	public InMemoryAggrefierAccessor(){
		super();
		this.recognizers = new ArrayList<Recognizer>();
	}

	/* (non-Javadoc)
	 * @see org.jhove2.persist.AggrefierAccessor#getRecognizers(org.jhove2.module.aggrefy.Aggrefier)
	 */
	@Override
	public List<Recognizer> getRecognizers(Aggrefier module)
			throws JHOVE2Exception {
		return this.recognizers;
	}

	@Override
	public Recognizer addRecognizer(Aggrefier module, Recognizer recognizer)
			throws JHOVE2Exception {
		if (module !=null && module.getModuleAccessor()!=null){
			this.persistModule(module);
		}
		if (recognizer != null && module == this.module){
			if (recognizer.getModuleAccessor()!=null){
				recognizer.getModuleAccessor().persistModule(recognizer);
			}
			if (!this.recognizers.contains(recognizer)){
				recognizers.add(recognizer);
			}
			// make sure recognizer points to its parent Aggrefier
			if (!(recognizer.getParentAggrefier()==module)){
				InMemoryRecognizerAccessor ra = (InMemoryRecognizerAccessor) 
										recognizer.getModuleAccessor();
				ra.setParentAggrefier(recognizer, module);
			}
		}
		return recognizer;
	}

	@Override
	public Recognizer deleteRecognizer(Aggrefier module, Recognizer recognizer)
			throws JHOVE2Exception {
		if (module !=null && module.getModuleAccessor()!=null){
			this.persistModule(module);
		}
		if (recognizer != null && module == this.module){
			if (recognizer.getModuleAccessor()!=null){
				recognizer.getModuleAccessor().persistModule(recognizer);
			}
			this.recognizers.remove(recognizer);
			// make sure recognizer no longer points to aggrefier
			if (recognizer.getParentAggrefier() == module){
				InMemoryRecognizerAccessor ra = (InMemoryRecognizerAccessor) recognizer.getModuleAccessor();
				ra.setParentAggrefier(recognizer, null);
			}
		}
		return recognizer;
	}

	@Override
	public List<Recognizer> setRecognizers(Aggrefier module,
			List<Recognizer> recognizers) throws JHOVE2Exception {
		if (this.module==null){
			this.persistModule(module);
		}
		if (module==this.module){
			for (Recognizer recognizer:this.recognizers){
				this.deleteRecognizer(module, recognizer);
			}
			if (recognizers != null){
				for (Recognizer recognizer:recognizers){
					this.addRecognizer(module, recognizer);
				}
			}
		}
		return this.recognizers;
	}



}
