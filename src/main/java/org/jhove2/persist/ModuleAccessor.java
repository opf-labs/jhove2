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
package org.jhove2.persist;

import org.jhove2.core.JHOVE2Exception;
import org.jhove2.module.Module;


/**
 * Interface to manage persistence of Module objects, and accessors and mutators
 * for member fields that themselves are persisted objects
 * @author smorrissey
 *
 */
public interface ModuleAccessor {
	/**
	 * Persists the Module object (may be no-op, if ModuleAccessor is for in-memory JHOVE2)
	 * @param source Module to be persisted
	 * @return Module object that was persisted
	 * @throws JHOVE2Exception
	 */
	public Module persistModule(Module module) throws JHOVE2Exception;
	/**
	 * Retrieves persisted Module object dereferenced via a key
	 * @param key Object by which persisted Module is dereferenced and retrieved
	 * @return Module object corresponding to key
	 * @throws JHOVE2Exception
	 */
	public Module retrieveModule(Object key) throws JHOVE2Exception;
	/**
	 * Starts Module's TimerInfo
	 * @param module
	 * @return Module with TimerInfo started
	 * @throws JHOVE2Exception
	 */
	public Module startTimerInfo(Module module) throws JHOVE2Exception;
	/**
	 * Ends Module's TimerInfo
	 * @param module
	 * @return Module with TimerInfo ended
	 * @throws JHOVE2Exception
	 */
	public Module endTimerInfo(Module module) throws JHOVE2Exception;
	/**
	 * Resets Module's TimerInfo
	 * @param module
	 * @return Module with TimerInfo reset
	 * @throws JHOVE2Exception
	 */
	public Module resetTimerInfo(Module module) throws JHOVE2Exception;
	
}
