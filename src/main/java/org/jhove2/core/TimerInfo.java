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
import org.jhove2.core.reportable.AbstractReportable;

/**
 * Timing information for {@link org.jhove2.core.reportable.Reportable}s that report their elapsed
 * processing times.
 * 
 * @author mstrong, slabrams, smorrissey
 */
public class TimerInfo
	extends AbstractReportable
{
	/** End of elapsed time. */
	protected long endTime;
	
	/** Time of instantiation. */
	protected long instTime;

	/** Start of elapsed. */
	protected long startTime;
	
	/** Instantiate a new <code>TimerInfo</code> object.
	 */
	public TimerInfo(){
		this.instTime  = System.currentTimeMillis();
		this.startTime = Duration.UNINITIALIZED;
		this.endTime   = Duration.UNINITIALIZED;
	}
	
	/**
	 * Get elapsed time, in milliseconds. The reportable time will never be less
	 * than 1 millisecond.
	 * 
	 * @return Elapsed time, in milliseconds
	 */
	@ReportableProperty(value="Elapsed time, milliseconds.")
	public Duration getElapsedTime(){
	    if (this.startTime == Duration.UNINITIALIZED) {
	        this.startTime = this.instTime;
	    }
		if (this.endTime == Duration.UNINITIALIZED) {
			setEndTime();
		}
		long duration = this.endTime - this.startTime;
		if (duration < 1L) {
			duration = 1L;
		}
		return new Duration(duration);
	}
	
	/** Reset the start time of the elapsed duration.
	 * return Restart time, in milliseconds
	 */
	public long resetStartTime() {
	    if (this.startTime == Duration.UNINITIALIZED) {
	        setStartTime();
	    }
	    if (this.endTime == Duration.UNINITIALIZED) {
	        this.endTime = this.startTime;
	    }
	    long duration = this.endTime - this.startTime;
	    
	    return this.startTime = System.currentTimeMillis() - duration;
	}

	/**
	 * Set the end time of the elapsed duration. Defaults to the time of
	 * invocation of the {@link org.jhove2.core.TimerInfo#getElapsedTime()}
	 * method.
	 * 
	 * @return End time, in milliseconds
	 */
	public long setEndTime() {
		return this.endTime = System.currentTimeMillis();
	}

	/**
	 * Set the start time of the elapsed duration. Defaults to the time of
	 * module instantiation.
	 * 
	 * @return Start time, in milliseconds
	 */
	public long setStartTime() {
		return this.startTime = System.currentTimeMillis();
	}
}
