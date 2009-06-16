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

/** An abstract JHOVE2 module, a {@link org.jhove2.core.Product} that performs
 * one or more processes in an elapsed duration.
 * 
 * @author mstrong, slabrams
 */
public class AbstractModule
	extends AbstractProduct
	implements Durable
{	
	/** Module elapsed time, end. */
	protected long endTime;
	
	/** Module elapsed time, start. */
	protected long startTime;
	
	/** Instantiate a new <code>AbstractModule</code>.
	 * @oaran version Module version identifier in three-part form: "M.N.P"
	 * @param date    Module release date in ISO 8601 format: "YYYY-MM-DD"
	 * @param rights  Module rights statement
	 */
	public AbstractModule(String version, String date, String rights) {
		super(version, date, rights);
		
		this.startTime = System.currentTimeMillis();
		this.endTime   = Duration.UNINITIALIZED;
	}
	
	/** Get elapsed time, in milliseconds.  The shortest reportable
	 * elapsed time is 1 milliscond.
	 * @return Elapsed time, in milliseconds
	 */
	@Override
	public Duration getElapsedTime() {
		if (this.endTime == Duration.UNINITIALIZED) {
			this.endTime = System.currentTimeMillis();
		}
		
		return new Duration(this.endTime - this.startTime);
	}

	/** Set the end time of the elapsed duration.
	 */
	@Override
	public void setEndTime() {
		this.endTime = System.currentTimeMillis();
	}

	/** Set the start time of the elapsed duration.
	 */
	@Override
	public void setStartTime() {
		this.startTime = System.currentTimeMillis();
	}
}
