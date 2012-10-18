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
import org.jhove2.core.source.Source;
import org.jhove2.module.Module;
import org.jhove2.persist.PersistenceManagerUtil;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.persist.model.NotPersistent;
import com.sleepycat.persist.model.Persistent;

/**
 * @author smorrissey
 *
 */
@Persistent
public class BerkeleyDbBaseAccessor implements BerkeleyDbAccessor {
	@NotPersistent
	private BerkeleyDbPersistenceManager berkeleyDbPersistenceManager = null;
	/* (non-Javadoc)
	 * @see org.jhove2.persist.berkeleydpl.BerkeleyDbAccessor#getBerkeleyDbPersistenceManager()
	 */
	
	public BerkeleyDbBaseAccessor(){}
	
	@Override
	public BerkeleyDbPersistenceManager getBerkeleyDbPersistenceManager()
			throws JHOVE2Exception {
		if (berkeleyDbPersistenceManager == null){
			berkeleyDbPersistenceManager = 
				(BerkeleyDbPersistenceManager) PersistenceManagerUtil.getPersistenceManagerFactory().getInstance();
		}
		return berkeleyDbPersistenceManager;
	}
	
	protected boolean isSameId(Long id01, Long id02){
    	boolean isSame = false;
    	if (id01==null && id02==null){
    		isSame = true;
    	} 
    	else {
    		isSame = (id01!=null && id02 !=null && id01.equals(id02));
    	}
    	return isSame;
    }
	
	protected Source retrieveSource(Long key) throws JHOVE2Exception {
		Source source = null;
		if (key != null){
			try{
				source = this.getBerkeleyDbPersistenceManager().getSourceBySourceId().get(key);
			}
			catch (DatabaseException e){
				throw new JHOVE2Exception("Could not retrieve Source for key" + key.toString(), e);
			}
		}
		return source;
	}
	
	protected Module retrieveModule(Long key) throws JHOVE2Exception {
		Module module = null;
		if (key != null){
			try{
				module = getBerkeleyDbPersistenceManager().getModuleByModuleId().get(key);
			}
			catch (DatabaseException e){
				throw new JHOVE2Exception("Could not retrieve module for key" + key.toString(), e);
			}
		}
		return module;
	}
}
