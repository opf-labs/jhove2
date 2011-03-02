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

package org.jhove2.module.digest;

import java.io.IOException;

import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.Message;
import org.jhove2.core.Message.Context;
import org.jhove2.core.Message.Severity;
import org.jhove2.core.io.Input;
import org.jhove2.core.source.MeasurableSource;
import org.jhove2.core.source.Source;
import org.jhove2.module.AbstractCommand;
import org.jhove2.persist.ModuleAccessor;

import com.sleepycat.persist.model.Persistent;

/**
 * Command to invoke message digesting on non-aggregate Sources
 * 
 * @author smorrissey
 */
@Persistent
public class DigesterCommand
	extends AbstractCommand
{
	/** IdentifierCommand module version identifier. */
	public static final String VERSION = "2.0.0";

	/** IdentifierCommand module release date. */
	public static final String RELEASE = "2010-09-10";

	/** IdentifierCommand module rights statement. */
	public static final String RIGHTS = "Copyright 2010 by The Regents of the University of California, "
		+ "Ithaka Harbors, Inc., and The Board of Trustees of the Leland "
		+ "Stanford Junior University. "
		+ "Available under the terms of the BSD license.";
	
	protected DigesterFactory digesterFactory;
	
	/** Instantiate a new <code>DigesterCommand</code>.
	 */
	public DigesterCommand(){
		this(null);
	}
	
	/** Instantiate a new <code>DigesterCommand</code>.
	 */
	public DigesterCommand(ModuleAccessor moduleAccessor){
		super(VERSION, RELEASE, RIGHTS, Scope.Generic, moduleAccessor);
	}

	/**
	 * If Source is not an aggregate source, and if JHOVE2 framework has been configured
	 * to calculate digests, calculates message digest
     * @param jhove2 JHOVE2 framework object
	 * @param source Source unit whose digest is to be calculates
	 * @param input  Source input
	 * @throws JHOVE2Exception
	 * @see org.jhove2.module.Command#execute(org.jhove2.core.JHOVE2, org.jhove2.core.source.Source, org.jhove2.core.io.Input)
	 */
	@Override
	public void execute(JHOVE2 jhove2, Source source, Input input) 
		throws JHOVE2Exception
	{
		if (source instanceof MeasurableSource) {
			if (jhove2.getInvocation().getCalcDigests()) {
				try {	
					Digester digester = 
						this.getDigesterFactory().getDigester();
					digester = (Digester) digester.getModuleAccessor().startTimerInfo(digester);
					try {
					    /* Register the digesting module. */
						digester=(Digester) source.addModule(digester);
	                    
	                    /* Calculate the digests. */
						digester.digest(jhove2, source, input);
					}
					finally {
						digester = (Digester) digester.getModuleAccessor().endTimerInfo(digester);
					}
				}
				catch (IOException e) {
					String eMessage = e.getLocalizedMessage();
					if (eMessage==null){
						eMessage = "";
					}
					String eType = e.getClass().getCanonicalName();
					Object[]messageArgs = new Object[]{eType,eMessage};
					Message message = new Message(
							Severity.ERROR,
							Context.PROCESS,
							"org.jhove2.module.digest.DigesterCommand.IOException",
							messageArgs,
							jhove2.getConfigInfo());
					source=source.addMessage(message);
				}
			}
		}
	}

	/**
	 * @return the digesterFactory
	 */
	public DigesterFactory getDigesterFactory() {
		return digesterFactory;
	}

	/**
	 * @param digesterFactory the digesterFactory to set
	 */
	public void setDigesterFactory(DigesterFactory digesterFactory) {
		this.digesterFactory = digesterFactory;
	}
}
