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
package org.jhove2.persist.berkeleydpl;

import org.jhove2.core.JHOVE2Exception;
import org.jhove2.module.AbstractModule;
import org.jhove2.module.Module;
import org.jhove2.persist.ModuleAccessor;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.persist.model.Persistent;

/**
 * @author smorrissey
 *
 */
@Persistent
public class BerkeleyDbBaseModuleAccessor 
	extends BerkeleyDbBaseAccessor 
	implements BerkeleyDbAccessor, ModuleAccessor {

	/**
	 * Constructor
	 */
	public BerkeleyDbBaseModuleAccessor(){
		super();
	}
	
	@Override
	public Module persistModule(Module module) throws JHOVE2Exception {
		if (module != null){
			//When one of the put methods in the PrimaryIndex is called and a new key is assigned, 
			//the assigned value is returned to the caller via the key field of the entity object that 
			//is passed as a parameter
			try{
				getBerkeleyDbPersistenceManager().getModuleByModuleId().put((AbstractModule) module);
			}
			catch(DatabaseException e){
				throw new JHOVE2Exception("Could not persist Module" , e);
			}
		}
		return module;
	}

	@Override
	public Module retrieveModule(Object key) throws JHOVE2Exception {
		Long longKey = null;
		Module module = null;
		if (key != null && key instanceof Long){
			try{
				longKey = (Long)key;
				module = getBerkeleyDbPersistenceManager().getModuleByModuleId().get(longKey);
			}
			catch (DatabaseException e){
				throw new JHOVE2Exception("Could not retrieve module for key" + key.toString(), e);
			}
		}
		return module;
	}

	@Override
	public Module endTimerInfo(Module module) throws JHOVE2Exception {
		if (module != null){
			module.getTimerInfo().setEndTime();
			this.persistModule(module);
		}
		return module;
	}
	
	@Override
	public Module resetTimerInfo(Module module) throws JHOVE2Exception{
		if (module != null){
			module.getTimerInfo().resetStartTime();
			this.persistModule(module);
		}
		return module;
	}
	@Override
	public Module startTimerInfo(Module module) throws JHOVE2Exception {
		if (module != null){
			module.getTimerInfo().setStartTime();
			this.persistModule(module);
		}
		return module;
	}


}
