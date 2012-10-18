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

import java.util.List;

import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.source.Source;
import org.jhove2.module.Module;
import org.jhove2.persist.ModuleAccessor;


/**
 * @author smorrissey
 *
 */
public class InMemoryBaseModuleAccessor implements ModuleAccessor {
	/** the Module for which this the the Accessor*/
	protected Module module;
	/** the Source on which this Module has been invoked */
	protected Source parentSource;
	
	public InMemoryBaseModuleAccessor(){
		super();
	}
	/* (non-Javadoc)
	 * @see org.jhove2.persist.ModuleAccessor#persistModule(org.jhove2.module.Module)
	 */
	@Override
	public Module persistModule(Module module) throws JHOVE2Exception {
		this.module = module;
		this.setParentSource(module.getParentSource(), module);
		return this.module;
	}
	@Override
	public Module endTimerInfo(Module module) throws JHOVE2Exception {
		if (this.module==null){
			this.module = module;
		}
		this.module.getTimerInfo().setEndTime();
		return this.module;
	}
	@Override
	public Module startTimerInfo(Module module) throws JHOVE2Exception {
		if (this.module==null){
			this.module = module;
		}
		this.module.getTimerInfo().setStartTime();
		return this.module;
	}
	@Override
	public Module resetTimerInfo(Module module) throws JHOVE2Exception{
		if (this.module==null){
			this.module = module;
		}
		this.module.getTimerInfo().resetStartTime();
		return this.module;
	}

	@Override
	public Source getParentSource(Module module) throws JHOVE2Exception{		
		return this.parentSource;
	}

	/**
	 * @param parentSource the parentSource to set
	 * @throws JHOVE2Exception 
	 */
	protected void setParentSource(Source parentSource, Module module) throws JHOVE2Exception {
		this.parentSource = parentSource;
		if (parentSource != null){
			List<Module> pModules = parentSource.getModules();
			if (pModules!= null && (!(pModules.contains(module)))){
				parentSource.addModule(module);
			}
		}
	}
	@Override
	public void verifyNewParentSourceId(Module module, Long oldId,
			Long newId) throws JHOVE2Exception {
		if (module != null & module==this.module){
			if (newId != null){
				if (this.parentSource==null){
					throw new JHOVE2Exception("non-null new parentID and null parent Source");
				}
				else {
					Long psid = this.parentSource.getSourceId();
					if (!(newId.equals(psid))){
						throw new JHOVE2Exception("parent Source id not equal to child's new parentSourceId");
					}
				}
			}
			else {
				// newId is null; parentSource should also have null id
				if (this.parentSource != null && this.parentSource.getSourceId() != null){
					throw new JHOVE2Exception("null new parentID and non-null parent sourceID");
				}
			}
		}
	}
	@Override
	public void verifyNewParentModuleId(Module module,
			Long oldId, Long newId) throws JHOVE2Exception{
		// Module accessors for modules that have parent module will override this
		return;
	}
}
