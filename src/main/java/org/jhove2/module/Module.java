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
import org.jhove2.core.WrappedProduct;
import org.jhove2.core.TimerInfo;
import org.jhove2.core.reportable.Reportable;

/**
 * Interface for JHOVE2 modules. A module is a
 * {@link org.jhove2.core.reportable.Reportable} that perform some process and report their
 * elapsed processing time. A module generally directly performs its process;
 * however, some module implementations may be thin wrappers around 3rd party
 * tools.
 * 
 * @author mstrong, slabrams, smorrissey
 */
public interface Module
	extends Reportable
{
	/**
	 * Get product developers.
	 * 
	 * @return Product developers
	 */
	@ReportableProperty(order = 4, value = "Product developers.")
	public List<Agent> getDevelopers();


	/**
	 * Get product informative note.
	 * 
	 * @return Product informative note
	 */
	@ReportableProperty(order = 6, value = "Product informative note.")
	public String getNote();
	
	/**
	 * Get product release date.
	 * 
	 * @return Product release date
	 */
	@ReportableProperty(order = 3, value = "Product release date.")
	public String getReleaseDate();

	/**
	 * Get product rights statement.
	 * 
	 * @return Product rights statement
	 */
	@ReportableProperty(order = 5, value = "Product rights statement.")
	public String getRightsStatement();
	/**
	 * Get product version.
	 * 
	 * @return Product version
	 */
	@ReportableProperty(order = 2, value = "Product version identifier.")
	public String getVersion();
	
	/**
	 * Get metadata about any tool wrapped by a Module, and used to perform a
	 * process.
	 * 
	 * @return Wrapped {@link org.jhove2.core.WrappedProduct}, or null if the
	 *         module directly performs its process
	 */
	@ReportableProperty(order = 7, value = "External tool wrapped by the module.")
	public WrappedProduct getWrappedProduct();
	/**
	 * Get metadata about this module
	 * @return ProductInfo {@link org.jhove2.core.ProductInfo}
	 */

	/**
	 * Get object which maintains timer information about the running of this module
	 * @return TimerInfo with timer information about the running of this module
	 */
	@ReportableProperty(order = 8, value = "Timer info for this module.")
	public TimerInfo getTimerInfo();
}
