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
import org.jhove2.core.TimerInfo;
import org.jhove2.core.reportable.ReportableFactory;
import org.jhove2.core.source.AggregateSource;
import org.jhove2.core.source.ClumpSource;
import org.jhove2.core.source.Source;
import org.jhove2.module.AbstractCommand;

/**
 * Command to invoke message digesting on non-aggregate Sources
 * 
 * @author smorrissey
 */
public class DigesterCommand
	extends AbstractCommand
{
	/** IdentifierCommand module version identifier. */
	public static final String VERSION = "0.5.4";

	/** IdentifierCommand module release date. */
	public static final String RELEASE = "2010-01-26";

	/** IdentifierCommand module rights statement. */
	public static final String RIGHTS = "Copyright 2010 by The Regents of the University of California, "
		+ "Ithaka Harbors, Inc., and The Board of Trustees of the Leland "
		+ "Stanford Junior University. "
		+ "Available under the terms of the BSD license.";
	
	/** Instantiate a new <code>DigesterCommand</code>.
	 */
	public DigesterCommand(){
		super(VERSION, RELEASE, RIGHTS, Scope.Generic);
	}

	/**
	 * If Source is not an aggregate source, and if JHOVE2 framework has been configured
	 * to calculate digests, calculates message digest
	 * @param source Source whose digest is to be calculates
	 * @param jhove2 JHOVE2 framework object
	 * @throws JHOVE2Exception
	 * @see org.jhove2.module.Command#execute(org.jhove2.core.JHOVE2, org.jhove2.core.source.Source)
	 */
	@Override
	public void execute(JHOVE2 jhove2, Source source) 
		throws JHOVE2Exception
	{
		if (!(source instanceof AggregateSource ||
			  source instanceof ClumpSource)) {
			if (jhove2.getInvocation().getCalcDigests()) {
				try {	
					Digester digester = 
						ReportableFactory.getReportable(Digester.class,
								                       "DigesterModule");
					source.addModule(digester);
					TimerInfo timer = digester.getTimerInfo();
					timer.setStartTime();
					try {
						digester.digest(jhove2, source);
					}
					finally {
						timer.setEndTime();
					}
				}
				catch (IOException e) {
					throw new JHOVE2Exception("I/O Exception on digest", e);
				}
			}
		}
		return;
	}
}
