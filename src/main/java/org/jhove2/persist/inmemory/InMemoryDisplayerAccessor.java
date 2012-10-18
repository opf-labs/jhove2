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
import org.jhove2.core.app.Application;
import org.jhove2.module.Module;
import org.jhove2.module.display.Displayer;
import org.jhove2.persist.DisplayerAccessor;

/**
 * @author smorrissey
 *
 */
public class InMemoryDisplayerAccessor extends InMemoryBaseModuleAccessor
		implements DisplayerAccessor {

	protected Application parentApplication;
	/**
	 * 
	 */
	public InMemoryDisplayerAccessor() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.jhove2.persist.DisplayerAccessor#getParentApplication(org.jhove2.module.display.Displayer)
	 */
	@Override
	public Application getParentApplication(Displayer displayer)
			throws JHOVE2Exception {
		return this.parentApplication;
	}
	
	protected Application setParentApplication(Displayer displayer, Application app){
		this.parentApplication = app;
		return this.parentApplication;
	}

	@Override
	public void verifyNewParentModuleId(Module module,
			Long oldId, Long newId) throws JHOVE2Exception{
		// Module accessors for modules that have parent module will override this
		if (module != null & module==this.module){
			if (newId != null){
				if (this.parentApplication==null){
					throw new JHOVE2Exception("non-null new application moduleId and null parentApplication");
				}
				else {
					Long psid = this.parentApplication.getModuleId();
					if (psid==null || !(newId.equals(psid))){
						throw new JHOVE2Exception("parentApplication id not equal to child's new parentApplication");
					}
				}
			}
			else {
				// newId is null; parentAggrefier should also have null id
				if (this.parentApplication != null && this.parentApplication.getModuleId() != null){
					throw new JHOVE2Exception("null new parentApplication id and non-null parentApplication");
				}
			}
		}
		return;
	}
}
