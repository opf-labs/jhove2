/**
 * JHOVE2 - Next-generation architecture for format-aware characterization
 *
 * Copyright (c) 2012 by The Regents of the University of California,
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

import org.jhove2.core.JHOVE2Exception;
import org.jhove2.module.Module;
import org.jhove2.module.aggrefy.Aggrefier;
import org.jhove2.module.aggrefy.Recognizer;
import org.jhove2.persist.RecognizerAccessor;

/**
 * @author smorrissey
 *
 */
public class InMemoryRecognizerAccessor extends InMemoryBaseModuleAccessor
		implements RecognizerAccessor {
	
	protected Aggrefier parentAggrefier;

	/* (non-Javadoc)
	 * @see org.jhove2.persist.RecognizerAccessor#getParentAggrefier(org.jhove2.module.aggrefy.Recognizer)
	 */
	@Override
	public Aggrefier getParentAggrefier(Recognizer recognizer)
			throws JHOVE2Exception {
		return parentAggrefier;
	}

	/**
	 * Connect Recognizer with parent Aggrefier
	 * @param recognizer Recognizer managed by this accessor
	 * @param aggrefier Parent of this Recognizer
	 * @return aggrefier
	 */
	protected Aggrefier setParentAggrefier(Recognizer recognizer,
			Aggrefier aggrefier) {
		this.parentAggrefier = aggrefier;
		return parentAggrefier;
	}
	
	@Override
	public void verifyNewParentModuleId(Module module,
			Long oldId, Long newId) throws JHOVE2Exception{
		// Module accessors for modules that have parent module will override this
		if (module != null & module==this.module){
			if (newId != null){
				if (this.parentAggrefier==null){
					throw new JHOVE2Exception("non-null new aggrefier moduleId and null parentAggrefier");
				}
				else {
					Long psid = this.parentAggrefier.getModuleId();
					if (psid==null ||  !(newId.equals(psid))){
						throw new JHOVE2Exception("parentAggrefier id not equal to child's new parentAggrefier");
					}
				}
			}
			else {
				// newId is null; parentAggrefier should also have null id
				if (this.parentAggrefier != null && this.parentAggrefier.getModuleId() != null){
					throw new JHOVE2Exception("null new parentAggrefier and non-null parentAggrefier");
				}
			}
		}
		return;
	}

}
