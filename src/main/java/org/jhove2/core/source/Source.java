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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.Durable;
import org.jhove2.core.Processible;
import org.jhove2.core.Reportable;
import org.jhove2.core.io.Input;
import org.jhove2.core.io.Input.Type;

/** Interface for JHOVE2 source units.  A source unit is a formatted object
 * that can be characterized, which may be a file, a subset of a file, or a
 * group of files.
 * 
 * @author mstrong, slabrams
 */
public interface Source
	extends Reportable, Durable
{
	/** Add a child source unit.
	 * @param child Child source unit
	 */
	public void addChildSource(Source child);
	
	/** Add a module that processed the source unit.
	 * @param module Module that processed the source unit
	 */
	public void addModule(Processible module);
	
	/** Get child source units.
	 * @return Child source units
	 */
	@ReportableProperty(order=3, value="Child source untis.")
	public List<Source> getChildSources();
	
	/** Get {@link org.jhove2.core.io.Input} for the source unit.
	 * @param bufferSize Input buffer size
	 * @param bufferType Input buffer type
	 * @return Input for the source unit
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public Input getInput(int bufferSize, Type bufferType)
		throws FileNotFoundException, IOException;
	
	/** Get modules that processed the source unit.
	 * @return Modules that processed the source unit
	 */
	@ReportableProperty(order=1, value="Modules that processed the source unit")
	public List<Processible> getModules();

	/** Get number of child source units.
	 * @return Number of child source units
	 */
	@ReportableProperty(order=2, value="Number of child source units.")
	public int getNumChildSources();
	
	/** Get number of modules.
	 * @return Number of modules
	 */
	public int getNumModules();
}
