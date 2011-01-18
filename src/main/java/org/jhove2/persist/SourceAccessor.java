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

import java.util.List;
import java.util.Set;

import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.Message;
import org.jhove2.core.format.FormatIdentification;
import org.jhove2.core.source.Source;
import org.jhove2.module.Module;

/**
 * Interface to manage persistence of Source objects, and accessors and mutators
 * for member fields that themselves are persisted objects
 * @author smorrissey
 *
 */
public interface SourceAccessor  {

	/**
	 * Persists the Source object (may be no-op, if SourceAccessor is for in-memory JHOVE2)
	 * @param source Source to be persisted
	 * @return Source object that was persisted
	 * @throws JHOVE2Exception
	 */
	public Source persistSource(Source source) throws JHOVE2Exception;

	/**
	 * Retrieves persisted Source object dereferenced via a key
	 * @param key Object by which persisted Source is dereferenced and retrieved
	 * @return Source object corresponding to key
	 * @throws JHOVE2Exception
	 */
	public Source retrieveSource(Object key) throws JHOVE2Exception ;
	/**
	 * Add child Source to Source
	 * @param parentSource Source to which child Source is to be attached
	 * @param childSource Source to be added to parent
	 * @return Child source
	 * @throws JHOVE2Exception
	 */
	public Source addChildSource(Source parentSource, Source childSource) throws JHOVE2Exception;

	/**
	 * Get child Sources of a Source
	 * @param parentSource whose children are to be returned
	 * @return List of child Sources
	 * @throws JHOVE2Exception
	 */
	public List<Source> getChildSources(Source parentSource) throws JHOVE2Exception;
	/**
	 * Get count of child Sources for a Source
	 * @param parentSource Source for which count of child Sources is to be returned
	 * @return int count of child Sources
	 * @throws JHOVE2Exception
	 */
	public int getNumChildSources(Source parentSource) throws JHOVE2Exception;
	/**
	 * Remove Source from list of a Source's children
	 * @param parentSource Source from which child Source is to be deleted
	 * @param childSource Source to be deleted from Parent;s list of children
	 * @return Child source deleted from parent's list of children
	 * @throws JHOVE2Exception
	 */
	public Source deleteChildSource(Source parentSource, Source childSource) throws JHOVE2Exception;
	/**
	 * Add module to a Source
	 * @param source Source to which module is to be added
	 * @param module Module to be added
	 * @return Module added to Source
	 * @throws JHOVE2Exception
	 */
	public Module addModule(Source source, Module module) throws JHOVE2Exception;

	/**
	 * Get list of Modules that have processed a Source
	 * @param source Source processed by Modules
	 * @return List of Modules which processed Source
	 * @throws JHOVE2Exception
	 */
	public List<Module> getModules (Source source) throws JHOVE2Exception;
	/**
	 * Get count of Modules that have processed a Source
	 * @return count of Modules that have processed a Source
	 * @throws JHOVE2Exception
	 */
	public int getNumModules(Source source) throws JHOVE2Exception;
	
	public Source addMessage(Source source, Message message) throws JHOVE2Exception;
	/**
	 * Adds presumptive format to Source
	 * @param Source to which format is to be added
	 * @param format to be added
	 * @return Source with format added
	 * @throws JHOVE2Exception
	 */
	public Source addPresumptiveFormat(Source source, FormatIdentification fi) throws JHOVE2Exception;
	/**
	 * Adds presumptive formats to Source
	 * @param Source to which formats are to be added
	 * @param List of formats to be added
	 * @return Source with formats added
	 * @throws JHOVE2Exception
	 */
	public Source addPresumptiveFormats(Source source, Set<FormatIdentification> fis) throws JHOVE2Exception;
	/**
	 * Starts Source's TimerInfo timer
	 * @param source with TimerInfo to be started
	 * @return source with TimerInfo started
	 * @throws JHOVE2Exception
	 */
	public Source startTimerInfo (Source source) throws JHOVE2Exception;
	/**
	 * Stops Source's TimerInfo timer
	 * @param source with TimerInfo to be stopped
	 * @return source with TimerInfo stopped
	 * @throws JHOVE2Exception
	 */
	public Source endTimerInfo (Source source) throws JHOVE2Exception;
}
