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
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.source.Source;

/**
 * Command invoked by JHOVE2 application framework to characterize some aspect
 * of a {@link org.jhove2.core.source.Source}, for example, identification,
 * format feature extraction/validation/profile validation, aggregate detection
 * and characterization, assessment, message digest calculation.
 * 
 * These commands should be thought of as essentially stateless (except for the
 * metadata members common to all {@link org.jhove2.module.Module}s that
 * describe the Module itself).
 * 
 * The JHOVE2 framework is configured by plugging a sequence of Command
 * objects into the JHOVE2.commands List field.
 * 
 * @author smorrissey, slabrams
 */
public interface Command
	extends Module
{
	/**
	 * Execute a characterization command
	 * @param source Source on which command is to be executed
	 * @param jhove2 JHOVE2 application framework for configuration information and callback
	 * @throws JHOVE2Exception
	 */
	public void execute(JHOVE2 jhove2, Source source)
	   throws JHOVE2Exception;
	
	/** Add module associated with the command.
	 * @param module Module associated with the command
	 */
	public void addModule(Module module);
	
	/** Get modules associated with the command.
	 * @return Modules associated with the command
	 */
	@ReportableProperty(value = "Modules associated with the command.")
	public List<Module> getModules();
}
