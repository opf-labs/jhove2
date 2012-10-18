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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.Message;
import org.jhove2.core.format.FormatIdentification;
import org.jhove2.core.reportable.Reportable;
import org.jhove2.core.source.Source;
import org.jhove2.module.Module;
import org.jhove2.persist.SourceAccessor;


/**
 * SourceAccessor for in-memory persistence model
 * Makes no attempt to cache or manage memory
 * @author smorrissey
 *
 */
public class InMemorySourceAccessor implements SourceAccessor {
	/** Child source units. */
	protected List<Source> children;	
	/** Extra properties of the source.  Extra properties are those not known
	 * at the time the source is instantiated and not associated with a
	 * specific {@link org.jhove2.module.format.FormatModule}.
	 */
	protected List<Reportable> extraProperties;	
	/** Modules which processed this Source */
	protected List<Module> modules;
	/** Source for which this is accessor*/
	protected Source source;
	/** Parent Source */
	protected Source parentSource;
	
	/**
	 * Constructor
	 */
	public InMemorySourceAccessor(){
		super();
		this.children        = new ArrayList<Source>();
		this.extraProperties = new ArrayList<Reportable>();
		this.modules         = new ArrayList<Module>();
	}
	
	/**
	 * Constructor
	 * @param source for which this is accessor
	 */
	public InMemorySourceAccessor(Source source){
		this();
		this.source=source;
		InMemorySourceAccessor sa = (InMemorySourceAccessor)source.getSourceAccessor();
		if (sa != null){
			this.children = sa.children;
			this.extraProperties = sa.extraProperties;
			this.modules = sa.modules;
			this.parentSource = sa.parentSource;
		}
	}

	/* (non-Javadoc)
	 * @see org.jhove2.persist.SourceAccessor#addChildSource(org.jhove2.core.source.Source, org.jhove2.core.source.Source)
	 */
	@Override
	public Source addChildSource(Source parentSource, Source childSource)
			throws JHOVE2Exception {
		if (childSource != null && parentSource==this.source){
			if (!(this.children.contains(childSource))){
				this.children.add(childSource);
			}
			// make sure child points to its parent
			if (!(childSource.getParentSource()==parentSource)){
				InMemorySourceAccessor sa = (InMemorySourceAccessor) childSource.getSourceAccessor();
				sa.parentSource=parentSource;
			}
		}
		return childSource;
	}
    
	/* (non-Javadoc)
	 * @see org.jhove2.persist.SourceAccessor#addModule(org.jhove2.core.source.Source, org.jhove2.module.Module)
	 */
	@Override
	public Module addModule(Source source, Module module)
			throws JHOVE2Exception {
		if (module != null && source==this.source){
			if (!(this.modules.contains(module))){
				this.modules.add(module);
			}
			// make sure module points to its parent source
			if (!(module.getParentSource()==source)){
				InMemoryBaseModuleAccessor ma = (InMemoryBaseModuleAccessor) module.getModuleAccessor();
				ma.setParentSource(source, module);
			}
		}
		return module;
	}  
    /** Add an extra properties {@link org.jhove2.core.reportable.Reportable}
     * to be associated with the source unit.  Extra properties are those not
     * known at the time the source unit is instantiated but which are not
     * associated with a particular {@link org.jhove2.module.format.FormatModule}.
     * @param properties Extra properties reportable
     * @return Source with extra properties added
     * @throws JHOVE2Exception
     */
	@Override
    public Source addExtraProperties(Source source, Reportable properties)
        throws JHOVE2Exception
    {
        source.getExtraProperties().add(properties);
        return  source;
    }

	@Override
	public Source deleteChildSource(Source parentSource, Source childSource)
			throws JHOVE2Exception {
		if (childSource != null && parentSource==this.source){
			this.children.remove(childSource);
			// make sure child no longer points to parent
			if ((childSource.getParentSource()==parentSource)){
				InMemorySourceAccessor sa = (InMemorySourceAccessor) childSource.getSourceAccessor();
				sa.parentSource=null;
			}
		}
		return childSource;
	}

	@Override 
	public Module deleteModule(Source source, Module module) throws JHOVE2Exception {
		if (module != null && source == this.source){
			this.modules.remove(module);
			// make sure module no longer points to source as parent
			if (module.getParentSource()==source){
				InMemoryBaseModuleAccessor ma = (InMemoryBaseModuleAccessor) module.getModuleAccessor();
				ma.setParentSource(null, module);
			}
		}
		return module;
	}
	
	@Override
	public List<Source> getChildSources(Source parentSource)
			throws JHOVE2Exception {
		return this.children;
	}

	@Override
	public List<Module> getModules(Source source) throws JHOVE2Exception {
		return this.modules;
	}

	@Override
	public int getNumChildSources(Source parentSource) throws JHOVE2Exception {
		return this.children.size();
	}


	@Override
	public int getNumModules(Source source) throws JHOVE2Exception {
		return this.modules.size();
	}


	/* (non-Javadoc)
	 * @see org.jhove2.persist.SourceAccessor#persist(org.jhove2.core.source.Source)
	 */
	@Override
	public Source persistSource(Source source) throws JHOVE2Exception {
		if (!(source.getSourceAccessor()==this)){
			throw new JHOVE2Exception
				("Attempting to persist source for which this is not the SourceAccessor");
		}
		List<Source> children = source.getChildSources();
		List<Module> modules = source.getModules();
		this.children = children;
		this.modules = modules;
		this.source = source;
		this.parentSource = source.getParentSource();	
		return this.source;
	}


	@Override
	public Source endTimerInfo(Source source) throws JHOVE2Exception {
		source.getTimerInfo().setEndTime();
		return source;
	}


	@Override
	public Source startTimerInfo(Source source) throws JHOVE2Exception {
		source.getTimerInfo().setStartTime();
		return source;
	}


	@Override
	public Source addMessage(Source source, Message message)
			throws JHOVE2Exception {
		source.getMessages().add(message);
		return  source;
	}


	@Override
	public Source addPresumptiveFormat(Source source, FormatIdentification fi)
			throws JHOVE2Exception {
		source.getPresumptiveFormats().add(fi);
		return source;
	}


	@Override
	public Source addPresumptiveFormats(Source source,
			Set<FormatIdentification> fis) throws JHOVE2Exception {
		source.getPresumptiveFormats().addAll(fis);
		return source;
	}

	@Override
	public Source getParentSource(Source childSource) throws JHOVE2Exception {
		return this.parentSource;
	}

	@Override
	public void verifyNewParentSourceId(Source childSource, Long oldId,
			Long newId) throws JHOVE2Exception {
		// sourceId and parentSourceId have no meaning in this model; all values should be null
		if (childSource != null && this.source.equals(childSource)){
			if (newId != null){
				if (this.parentSource==null){
					throw new JHOVE2Exception("non-null parentID and null parent source");
				}
				else {
					Long psid = this.parentSource.getSourceId();
					if (!(newId.equals(psid))){
						throw new JHOVE2Exception("parent Source id not equal to child's new parentSourceId");
					}
				}
			}
			else {
				// note cannot tell difference between null parentSourceId and parentSource with null sourceId
				if (this.parentSource != null && this.parentSource.getSourceId()!=null){
					throw new JHOVE2Exception("parentSource id not equal null; new Id is null");
				}
			}
		}
	}

}
