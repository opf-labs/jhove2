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

import java.util.List;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.Agent;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.TimerInfo;
import org.jhove2.core.WrappedProduct;
import org.jhove2.core.reportable.Reportable;
import org.jhove2.core.source.Source;
import org.jhove2.persist.ModuleAccessor;

/**
 * Interface for JHOVE2 modules. A module is an object that performs some process and reports
 * results via {@linkplain Reportable reportable properties}.  Results include elapsed processing
 * time. The module may perform the process directly or be a thin wrapper around a third-party
 * tool.
 * 
 * @author mstrong, slabrams, smorrissey
 */
public interface Module
	extends Reportable
{
	/** Module scope */
	public enum Scope {
          /**
           * Module reports generic information and should only be called once.
           */
          Generic,
          /**
           * Module reports information specific to a source unit and should be called every time a
           * matching source unit is characterized.
           */
          Specific
	}
	
	/**
	 * Get module developers.
	 * 
	 * @return Module developers
	 */
	@ReportableProperty(order = 4, value = "Module developers.")
	public List<Agent> getDevelopers();

	/**
	 * Get module informative note.
	 * 
	 * @return Module informative note
	 */
	@ReportableProperty(order = 7, value = "Module informative note.")
	public String getNote();
	
	/**
	 * Get module release date.
	 * 
	 * @return Module release date
	 */
	@ReportableProperty(order = 2, value = "Module release date.")
	public String getReleaseDate();

	/**
	 * Get module rights statement.
	 * 
	 * @return Module rights statement
	 */
	@ReportableProperty(order = 3, value = "Module rights statement.")
	public String getRightsStatement();
	
	/** Get module scope: generic or specific.
	 * @return Module scope
	 */
	@ReportableProperty(order = 5, value = "Module scope: generic or " +
			"specific (to a source unit.")
	public Scope getScope();

    /**
     * Get object which maintains timer information about the running of this module
     * @return TimerInfo with timer information about the running of this module
     */
    @ReportableProperty(order = 8, value = "Timer info for this module.")
    public TimerInfo getTimerInfo();
    
	/**
	 * Get module version.
	 * 
	 * @return Module version
	 */
	@ReportableProperty(order = 1, value = "Module version identifier.")
	public String getVersion();
	
	/**
	 * Get metadata about any tool wrapped by a Module, and used to perform a
	 * process.
	 * 
	 * @return Wrapped {@link org.jhove2.core.WrappedProduct}, or null if the
	 *         module directly performs its process
	 */
	@ReportableProperty(order = 6, value = "External product wrapped by the module.")
	public WrappedProduct getWrappedProduct();

    /**
     * Set module developers.
     * 
     * @param developers
     *            Product developers
     */
    public void setDevelopers(List<Agent> developers);
    
	/** Set module informative note.
	 * @param note Module informative note
	 */
	public void setNote(String note);
	
	/** Set module release date.
	 * @param release Module release date
	 */
	public void setReleaseDate(String release);
	
	/** Set module rights statement.
	 * @param rights Module rights statement
	 */
	public void setRights(String rights);
	
	/** Set module scope.
	 * @param scope Module scope
	 */
	public void setScope(Scope scope);
	
	/**
	 * Set TimerInfo
	 * @param timer Module TimerInfo
	 */
	public void setTimerInfo(TimerInfo timer);
	
	/** Set module version.
	 * @param version Module version
	 */
	public void setVersion(String version);
	   
    /**
     * Set wrapped product.
     * 
     * @param product
     *            Wrapped product
     */
    public void setWrappedProduct(WrappedProduct product);
	/**
    /**
     * Semantics of moduleId field (e.g., as unique key) the responsibility of ModuleAccessor
     * Some memory models will not require unique keys (e.g. InMemoryBaseModuleAccessor, which
     * attaches no meaning to sourceId field); others (e.g. BerkeleyDbBaseModuleAccessor) do

	 * @return the moduleId
	 */
	public Long getModuleId();
	/**
	 * @return the moduleParentSourceId
	 */
	public Long getParentSourceId();
	/**
     * Set moduleParentSourceId field. Convenience method for ModuleAccessors that use "foreign key"
     * semantics for moduleId and moduleParentSourceId fields.
     * It is the responsibility of SourceAccessor to maintain any "foreign key" semantics that
     * its persistence model makes use of to relate a Module's parent Source to moduleParentSourceId field
     * 
     * The preferred method for setting a child Module's (module)'s parent to parentSource is
     * parentSource.addModule(module); to remove a child (setting child's parent to null) is
     * parentSource.deleteModule(module).
     * 
     * Changing a Module's parent Source should be accomplished via a sequence of method invocations:  first
     * oldParentSource..deleteModule(module); then newParentSource.addModule(module).
     * 
	 * @param moduleParentSourceId the moduleParentSourceId to set
	 * @throws JHOVE2Exception
	 */
	public void setParentSourceId(Long parentSourceId) throws JHOVE2Exception;
	/**
	 * Get parent Source
	 * @return Source to which this module is attached
	 */
	public Source getParentSource() throws JHOVE2Exception;	
	/**
	 * @return the moduleAccessor
	 */
	public ModuleAccessor getModuleAccessor();
	/**
	 * @param moduleAccessor the moduleAccessor to set
	 */
	public void setModuleAccessor(ModuleAccessor moduleAccessor);
}
