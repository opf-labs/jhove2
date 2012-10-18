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
package org.jhove2.module.aggrefy;

import static com.sleepycat.persist.model.DeleteAction.NULLIFY;
import static com.sleepycat.persist.model.Relationship.MANY_TO_ONE;

import org.jhove2.core.JHOVE2Exception;
import org.jhove2.module.AbstractModule;
import org.jhove2.persist.ModuleAccessor;
import org.jhove2.persist.RecognizerAccessor;

import com.sleepycat.persist.model.Persistent;
import com.sleepycat.persist.model.SecondaryKey;

/**
 * @author smorrissey
 *
 */
@Persistent
public abstract class AbstractRecognizer extends AbstractModule implements
		Recognizer {

	/** foreign key linking GlobPathRecognizer to parent Aggrefier module */
	@SecondaryKey(relate=MANY_TO_ONE, relatedEntity=AbstractModule.class,
			onRelatedEntityDelete=NULLIFY)
	protected Long parentAggrefierId;
	
	/**
	 * @param version
	 * @param release
	 * @param rights
	 * @param scope
	 * @param moduleAccessor
	 */
	public AbstractRecognizer(String version, String release, String rights,
			Scope scope, ModuleAccessor moduleAccessor) {
		super(version, release, rights, scope, moduleAccessor);
	}

	/**
	 * @param moduleAccessor
	 */
	public AbstractRecognizer(ModuleAccessor moduleAccessor) {
		super(moduleAccessor);
	}

	/**
	 * 
	 */
	public AbstractRecognizer() {
		super();
	}


	/* (non-Javadoc)
	 * @see org.jhove2.module.aggrefy.Recognizer#getParentAggrefier()
	 */
	@Override
	public Aggrefier getParentAggrefier() throws JHOVE2Exception {
		RecognizerAccessor ra = null;
		try {
			ra = (RecognizerAccessor)this.getModuleAccessor();
		}
		catch (Exception e){
			throw new JHOVE2Exception ("Failed to cast ModuleAccessor to RecognizerAccessor", e);
		}		
		return ra.getParentAggrefier(this);
	}

	/* (non-Javadoc)
	 * @see org.jhove2.module.aggrefy.Recognizer#getParentAggrefierId()
	 */
	@Override
	public Long getParentAggrefierId() {
		return this.parentAggrefierId;
	}

	/* (non-Javadoc)
	 * @see org.jhove2.module.aggrefy.Recognizer#setParentAggrefierId(java.lang.Long)
	 */
	@Override
	public void setParentAggrefierId(Long parentAggrefierId) throws JHOVE2Exception {
		Long oldId = this.getParentAggrefierId();
		this.parentAggrefierId = parentAggrefierId;
		this.getModuleAccessor().verifyNewParentModuleId(this, oldId, parentAggrefierId);
	}

}
