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
import org.jhove2.module.identify.Identifier;
import org.jhove2.module.identify.SourceIdentifier;
import org.jhove2.persist.SourceIdentifierAccessor;

/**
 * @author smorrissey
 *
 */
public class InMemorySourceIdentifierAccessor extends InMemoryBaseModuleAccessor
		implements SourceIdentifierAccessor {
	
	protected Identifier parentIdentifier;

	/**
	 * 
	 */
	public InMemorySourceIdentifierAccessor() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.jhove2.persist.SourceIdentifierAccessor#getParentIdentifier(org.jhove2.module.identify.SourceIdentifier)
	 */
	@Override
	public Identifier getParentIdentifier(SourceIdentifier sourceIdentifier)
			throws JHOVE2Exception {
		return this.parentIdentifier;
	}
	
	protected Identifier setParentIdentifier(SourceIdentifier sourceIdentifier, Identifier identifier)
	throws JHOVE2Exception {
		this.parentIdentifier = identifier;
		return this.parentIdentifier;
	}
	
	@Override
	public void verifyNewParentModuleId(Module module,
			Long oldId, Long newId) throws JHOVE2Exception{
		if (module != null & module==this.module){
			if (newId != null){
				if (this.parentIdentifier==null){
					throw new JHOVE2Exception("non-null new parent module moduleId and null parentApplication");
				}
				else {
					Long psid = this.parentIdentifier.getModuleId();
					if (psid==null || !(newId.equals(psid))){
						throw new JHOVE2Exception("parent module id not equal to child's new parent module");
					}
				}
			}
			else {
				// newId is null; parentAggrefier should also have null id
				if (this.parentIdentifier != null && this.parentIdentifier.getModuleId() != null){
					throw new JHOVE2Exception("null new parent module id and non-null parent module");
				}
			}
		}		
		return;
	}

}
