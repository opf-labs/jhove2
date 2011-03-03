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
import java.util.Set;

import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.Message;
import org.jhove2.core.format.FormatIdentification;
import org.jhove2.core.reportable.Reportable;
import org.jhove2.core.source.AbstractSource;
import org.jhove2.core.source.Source;
import org.jhove2.module.AbstractModule;
import org.jhove2.module.Module;
import org.jhove2.persist.SourceAccessor;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.EntityIndex;
import com.sleepycat.persist.model.Persistent;


/**
 * When a Source is created, it will be persisted.
 * When a ChildSource is created, it will be persisted.
 * When a ChildSource is added to its parent, it will be updated with parent SourceID
 * @author smorrissey
 *
 */
@Persistent
public class BerkeleyDbSourceAccessor 
	extends BerkeleyDbBaseAccessor 
	implements BerkeleyDbAccessor, SourceAccessor {
	
	/**
	 * Constructor
	 */
	public BerkeleyDbSourceAccessor() {
		super();
	}
	/**
	 * Constructor
	 * @param source for which this is accessor
	 */
	public BerkeleyDbSourceAccessor(Source source){
		this();
	}
	
	/* (non-Javadoc)
	 * @see org.jhove2.persist.SourceAccessor#addChildSource(org.jhove2.core.source.Source, org.jhove2.core.source.Source)
	 */
	@Override
	public Source addChildSource(Source parentSource, Source childSource)
			throws JHOVE2Exception {
		if (parentSource != null && childSource != null){
			if (parentSource.getSourceId()==null){
				// key field in parentSource will be updated automatically
				parentSource = this.persistSource(parentSource);
			}
			childSource.setParentSourceId(parentSource.getSourceId());
			// key field in parentSource will be updated automatically if it is a new Source
			childSource  = this.persistSource(childSource);
		}
		return childSource;
	}
    
	/* (non-Javadoc)
	 * @see org.jhove2.persist.SourceAccessor#addModule(org.jhove2.core.source.Source, org.jhove2.module.Module)
	 */
	@Override
	public Module addModule(Source source, Module module)
			throws JHOVE2Exception {
		if (source != null && module != null){
			if (source.getSourceId()== null){
				// sourceId field will be updated automatically
				source = this.persistSource(source);
			}
			module.setParentSourceId(source.getSourceId());
			module=module.getModuleAccessor().persistModule(module);
		}
		return module;
	}

	/* (non-Javadoc)
	 * @see org.jhove2.persist.SourceAccessor#deleteChildSource(org.jhove2.core.source.Source, org.jhove2.core.source.Source)
	 */
	@Override
	public Source deleteChildSource(Source parentSource, Source childSource)
			throws JHOVE2Exception {
		if (parentSource != null && childSource != null){
			if (childSource.getParentSourceId().equals(parentSource.getSourceId())){
				childSource.setParentSourceId(null);
				childSource = this.persistSource(childSource);
			}
		}
		return childSource;
	}


	/* (non-Javadoc)
	 * @see org.jhove2.persist.SourceAccessor#getChildSources(org.jhove2.core.source.Source)
	 */
	@Override
	public List<Source> getChildSources(Source parentSource)
			throws JHOVE2Exception {
		ArrayList<Source> childSources = new ArrayList<Source>();
		if (parentSource != null){
			EntityIndex<Long, AbstractSource> subIndex = null;
			EntityCursor<AbstractSource> cursor = null;
			try{
				subIndex = 
					this.getBerkeleyDbPersistenceManager().getSourceByParentSource().subIndex(parentSource.getSourceId());
				cursor = subIndex.entities();
				for (AbstractSource source: cursor){
					childSources.add(source);
				}// end for
			}
			catch (DatabaseException e){
				throw new JHOVE2Exception ("Unable to get Child Sources for parentSource id = " 
						+ parentSource.getSourceId(), e);
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
		}// end if
		return childSources;
	}


	/* (non-Javadoc)
	 * @see org.jhove2.persist.SourceAccessor#getModules(org.jhove2.core.source.Source)
	 */
	@Override
	public List<Module> getModules(Source source) throws JHOVE2Exception {
		ArrayList<Module> childModules = new ArrayList<Module>();
		if (source != null){
			EntityIndex<Long, AbstractModule> subIndex = null;
			EntityCursor<AbstractModule> cursor = null;
			try{
				Long sourceId = source.getSourceId();
				subIndex = 
					this.getBerkeleyDbPersistenceManager().getModuleByParentSource().subIndex(sourceId);
				cursor = subIndex.entities();
				for (AbstractModule module: cursor){
					childModules.add(module);
				}// end for
			}
			catch (DatabaseException e){
				throw new JHOVE2Exception ("Unable to get Child Modules for source id = " 
						+ source.getSourceId(), e);
			}
			finally{
				if (cursor != null){
					try{
						cursor.close();
					}
					catch (DatabaseException e){
						throw new JHOVE2Exception ("Unable to ge tChild Modules for Source id = " 
								+ source.getSourceId(), e);
					}
				}
			}
		}
		
		
		return childModules;
	}

	/* (non-Javadoc)
	 * @see org.jhove2.persist.SourceAccessor#getNumChildSources(org.jhove2.core.source.Source)
	 */
	@Override
	public int getNumChildSources(Source parentSource) throws JHOVE2Exception {
		int size = 0;
		if (parentSource != null){
			List<Source> childSources = this.getChildSources(parentSource);
			size = childSources.size();
		}
		return size;
	}

	/* (non-Javadoc)
	 * @see org.jhove2.persist.SourceAccessor#getNumModules()
	 */
	@Override
	public int getNumModules(Source source) throws JHOVE2Exception {
		int size = 0;
		if (source != null){
			List<Module> modules = this.getModules(source);
			size = modules.size();
		}
		return size;
	}

	/* (non-Javadoc)
	 * @see org.jhove2.persist.SourceAccessor#persist(org.jhove2.core.source.Source)
	 */
	@Override
	public Source persistSource(Source source) throws JHOVE2Exception {
//		When one of the put methods in the PrimaryIndex is called and a new key is assigned, 
//		the assigned value is returned to the caller via the key field of the entity object that is passed as a parameter.
//      The returned source object has its key field updated
		if (source != null){
			try{
				this.getBerkeleyDbPersistenceManager().getSourceBySourceId().put((AbstractSource) source); 
			}
			catch(DatabaseException e){
				throw new JHOVE2Exception("Could not persist Source" , e);
			}
		}
		return source;
	}

	@Override
	public Source retrieveSource(Object key) throws JHOVE2Exception {
		Long longKey = null;
		Source source = null;
		if (key != null && key instanceof Long){
			try{
				longKey = (Long)key;
				source = this.getBerkeleyDbPersistenceManager().getSourceBySourceId().get(longKey);
			}
			catch (DatabaseException e){
				throw new JHOVE2Exception("Could not retrieve Source for key" + key.toString(), e);
			}
		}
		return source;
	}


	@Override
	public Source endTimerInfo(Source source) throws JHOVE2Exception {;
		if (source != null){
			source.getTimerInfo().setEndTime();
			source = this.persistSource(source);
		}
		return source;
	}

	@Override
	public Source startTimerInfo(Source source) throws JHOVE2Exception {
		if (source != null){
			source.getTimerInfo().setStartTime();
			source = this.persistSource(source);
		}
		return source;
	}

	@Override
	public Source addMessage(Source source, Message message)
			throws JHOVE2Exception {
		if (source != null && message != null){
			source.getMessages().add(message);
			source = this.persistSource(source);
		}
		return source;
	}

	@Override
	public Source addPresumptiveFormat(Source source, FormatIdentification fi)
			throws JHOVE2Exception {
		if (source != null && fi != null){
			source.getPresumptiveFormats().add(fi);
			source = this.persistSource(source);
		}
		return source;
	}

	@Override
	public Source addPresumptiveFormats(Source source,
			Set<FormatIdentification> fis) throws JHOVE2Exception {;
		if (source != null && fis != null){
			source.getPresumptiveFormats().addAll(fis);
			source = this.persistSource(source);
		}
		return source;
	}

}
