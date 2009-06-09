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

package org.jhove2.core.source;

import java.util.List;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.Component;
import org.jhove2.core.Durable;
import org.jhove2.core.Reporter;

/** Interface for JHOVE2 source units.  A source unit is a formatted byte
 * stream that is an entire file, a proper subset of a file, or a set of
 * files.
 * 
 * @author mstrong, slabrams
 */
public interface Source
	extends Reporter, Durable
{
	/** Add module reporting source unit reportable properties.
	 * @param Module Module reporting source reportable properties
	 */
	public void addModule(Component module);
		
	/** Get modules reporting properties of the source unit.
	 * @return Components reporting properties of the source unit.
	 */
	@ReportableProperty(value=2, desc="Modules reporting properties of " +
			"the Source unit.")
	public List<Component> getModules();
	
	/** Get source unit existence status.
	 * @return True if source unit exists
	 */
	@ReportableProperty(value=1, desc="Source unit existence status: true " +
			"if source unit exists")
	public boolean isExtant();
}
