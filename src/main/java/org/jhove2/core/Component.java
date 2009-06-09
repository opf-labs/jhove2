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

import org.jhove2.annotation.ReportableProperty;

/** Interface for JHOVE2 components.  A component is an
 * independently-distributable and installable
 * {@link org.jhove2.core.Reporter}.
 * 
 * @author mstrong, slabrams
 */
public interface Component
	extends Reporter, Durable
{
	/** Component development stages. */
	public enum Stage {
		Development,
		Alpha,
		Beta,
		Production,
		Deprecated
	}
	
	/** Get component developer.
	 * @return Component developer
	 */
	@ReportableProperty(value=3, desc="Component developer")
	public Agent getDeveloper();

	/** Get component informative note.
	 * @return Component informative note
	 */
	@ReportableProperty(value=6, desc="Component informative note.")
	public String getNote();
	
	/** Get component release date.
	 * @return AbstractComponent release date
	 */
	@ReportableProperty(value=2, desc="Component release date.")
	public String getReleaseDate();
	
	/** Get component development stage.
	 * @return Component development stage
	 */
	@ReportableProperty(value=5, desc="Component development stage")
	public Stage getStage();
	
	/** Get component rights statement.
	 * @return Component rights statement
	 */
	@ReportableProperty(value=4, desc="Component rights statement.")
	public String getRightsStatement();
	
	/** Get component version.
	 * @return Component version
	 */
	@ReportableProperty(value=1, desc="Component version identifier.")
	public String getVersion();
}
