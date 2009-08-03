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

package org.jhove2.core;

import java.util.List;

import org.jhove2.annotation.ReportableProperty;

/** Interface for JHOVE2 products.  A product is an independently
 * distributable and configurable {@link org.jhove2.core.Reportable}.
 * 
 * @author mstrong, slabrams
 */
public interface Product
	extends Reportable
{
	/** Get product developers.
	 * @return Product developers
	 * @see org.jhove2.core.AbstractProduct#getDevelopers()
	 */
	@ReportableProperty(order=4, value="Product developers.")
	public List<Agent> getDevelopers();
	
	/** Get product name.
	 * @return Product name
	 */
	@ReportableProperty(order=1, value="Product name.")
	public String getName();
	
	/** Get product informative note.
	 * @return Product informative note
	 */
	@ReportableProperty(order=6, value="Product informative note.")
	public String getNote();

	/** Get product release date.
	 * @return Product release date
	 */
	@ReportableProperty(order=3, value="Product release date.")
	public String getReleaseDate();

	/** Get product rights statement.
	 * @return Product rights statement
	 */
	@ReportableProperty(order=5, value="AbstractProduct rights statement.")
	public String getRightsStatement();

	/** Get product version.
	 * @return Product version
	 */
	@ReportableProperty(order=2, value="Product version identifier.")
	public String getVersion();
}
