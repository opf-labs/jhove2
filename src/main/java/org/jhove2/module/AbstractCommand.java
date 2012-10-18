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

package org.jhove2.module;

import static com.sleepycat.persist.model.DeleteAction.NULLIFY;
import static com.sleepycat.persist.model.Relationship.MANY_TO_ONE;

import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.persist.CommandAccessor;
import org.jhove2.persist.ModuleAccessor;

import com.sleepycat.persist.model.Persistent;
import com.sleepycat.persist.model.SecondaryKey;

/** Abstract {@link org.jhove2.module.Command}.
 * 
 * @author smorrissey, slabrams
 */
@Persistent
public abstract class AbstractCommand
	extends AbstractModule
	implements Command
{
	/** foreign key linking Command to parent JHOVE2 framework */
	@SecondaryKey(relate=MANY_TO_ONE, relatedEntity=AbstractModule.class,
			onRelatedEntityDelete=NULLIFY)
	protected Long jhove2ModuleId;
	
	/** Instantiate a new <code>AbstractCommand</code>. 
	 * 	 * @param version
	 *            Module version identifier in three-part form: "M.N.P"
	 * @param release
	 *            Module release date in ISO 8601 format: "YYYY-MM-DD"
	 * @param rights
	 *            Module rights statement
	 * @param scope
	 *            Module scope: generic or specific (to a source unit)
	 * @param moduleAccessor 
	 *            Command persistence manager
	 */
	public AbstractCommand(String version, String release, String rights,
			               Scope scope, ModuleAccessor moduleAccessor) {
		super(version, release, rights, scope, moduleAccessor);
	}
	/**
	 *Instantiate a new <code>AbstractCommand</code>. 
	 */
	public AbstractCommand(){
		this(null);
	}
	/**
	 * Instantiate a new <code>AbstractCommand</code>. 
	 * @param moduleAccessor 
	 * 		      Module persistence manager
	 */
	public AbstractCommand(ModuleAccessor moduleAccessor){
		this(null, null, null, Scope.Generic, moduleAccessor);
	}

	@Override
	public Long getJhove2ModuleId() {
		return jhove2ModuleId;
	}

	@Override
	public void setJhove2ModuleId(Long jhove2ModuleId) throws JHOVE2Exception {
		Long oldId = this.jhove2ModuleId;
		this.jhove2ModuleId = jhove2ModuleId;
		this.getModuleAccessor().verifyNewParentModuleId(this, oldId, jhove2ModuleId);
	}
	
	@Override
	public JHOVE2 getParentFramework() throws JHOVE2Exception{
		CommandAccessor ca;
		try {
			ca = (CommandAccessor)this.getModuleAccessor();
		}
		catch (Exception e){
			throw new JHOVE2Exception ("Failed to cast ModuleAccessor to CommandAccessor", e);
		}	
		return ca.getParentFramework(this);
	}
	
}
