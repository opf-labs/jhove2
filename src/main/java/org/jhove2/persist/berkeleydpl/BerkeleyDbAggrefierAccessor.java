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

import java.util.ArrayList;
import java.util.List;

import org.jhove2.core.JHOVE2Exception;
import org.jhove2.module.AbstractModule;
import org.jhove2.module.aggrefy.AbstractRecognizer;
import org.jhove2.module.aggrefy.Aggrefier;
import org.jhove2.module.aggrefy.Recognizer;
import org.jhove2.persist.AggrefierAccessor;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.EntityIndex;
import com.sleepycat.persist.model.Persistent;

/**
 * @author smorrissey
 *
 */
@Persistent
public class BerkeleyDbAggrefierAccessor extends BerkeleyDbBaseModuleAccessor
implements AggrefierAccessor {

	/**
	 * Constructor
	 */
	public BerkeleyDbAggrefierAccessor(){
		super();
	}

	/* (non-Javadoc)
	 * @see org.jhove2.persist.AggrefierAccessor#getRecognizers(org.jhove2.module.aggrefy.Aggrefier)
	 */
	@Override
	public List<Recognizer> getRecognizers(Aggrefier module)
	throws JHOVE2Exception {
		List<Recognizer> recognizers =  new ArrayList<Recognizer>();
		if (module != null){
			EntityIndex<Long, AbstractRecognizer> subIndex = null;
			EntityCursor<AbstractRecognizer> cursor = null;
			try{
				if (module.getModuleId()== null){ // new module, never persisted
					this.persistModule(module);
				}
				subIndex = 
					this.getBerkeleyDbPersistenceManager().
					getRecognizerByParentAggrefier().subIndex(module.getModuleId());

				cursor = subIndex.entities();
				for (AbstractModule recog:cursor){
					recognizers.add((Recognizer)recog);
				}
			}
			catch (DatabaseException e){
				throw new JHOVE2Exception ("Unable to get Recognizers " , e);
			}
			finally{
				if (cursor != null){
					try{
						cursor.close();
					}
					catch (DatabaseException e){
						throw new JHOVE2Exception ("Unable to close cursor", e);
					}
				}
			}
		}
		return recognizers;
	}

	@Override
	public Recognizer addRecognizer(Aggrefier module, Recognizer recognizer)
			throws JHOVE2Exception {
		if (module != null && recognizer != null){
			if (module.getModuleId()==null){
				this.persistModule(module);
			}
			recognizer.setParentAggrefierId(module.getModuleId());
		}
		return recognizer;
	}

	@Override
	public Recognizer deleteRecognizer(Aggrefier module, Recognizer recognizer)
			throws JHOVE2Exception {
		if (module != null && recognizer != null){
			if (module.getModuleId()==null){
				this.persistModule(module);
			}
			Long parentKey = recognizer.getParentAggrefierId();
			if (parentKey !=null && parentKey.equals(module.getModuleId())){
				recognizer.setParentAggrefierId(null);
			}
		}
		return recognizer;
	}

	/* (non-Javadoc)
	 * @see org.jhove2.persist.AggrefierAccessor#setRecognizers(org.jhove2.module.aggrefy.Aggrefier, java.util.List)
	 */
	@Override
	public List<Recognizer> setRecognizers(Aggrefier module, List<Recognizer>recognizers) 
	throws JHOVE2Exception{
		List<Recognizer> childRecognizers = new ArrayList<Recognizer>();
		if (module != null){
			if (module.getModuleId()== null){ // new module, never persisted
				module=(Aggrefier) this.persistModule(module);
			}
			//un-link any existing recognizers from this aggrefier
			List<Recognizer> oldRecognizers = this.getRecognizers(module);
			if (oldRecognizers != null){
				for (Recognizer recognizer:oldRecognizers){
					this.deleteRecognizer(module, recognizer);
				}
			}
			if (recognizers != null){
				for (Recognizer recog:recognizers){
					this.addRecognizer(module, recog);
					childRecognizers.add(recog);
				}
			}
		}
		return childRecognizers;
	}

}
