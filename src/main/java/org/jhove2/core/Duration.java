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

import com.sleepycat.persist.model.Persistent;

/**
 * Represents the time used to complete a task.
 * 
 * @author mstrong, slabrams
 */
@Persistent
public class Duration {
	/** Indicator of an uninitialized time. */
	public static final long UNINITIALIZED = -1L;

	/** Duration, in milliseconds. */
	protected long duration;

	/**
	 * Instantiate a new <code>Duration</code>.
	 * 
	 * @param duration
	 *            Duration, in milliseconds
	 */
	public Duration(long duration) {
		this.duration = duration;
	}
	
	
	@SuppressWarnings("unused")
	private Duration(){
		this(0L);
	}

	/**
	 * Get duration, in milliseconds.
	 * 
	 * @return Duration, in milliseconds
	 */
	public long getDuration() {
		return this.duration;
	}

	/**
	 * Get {@link java.lang.String} representation of the duration, of the form:
	 * "hh:mm:ss.msec"
	 */
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();

		int hh = 0;
		int mm = 0;
		int ss = 0;
		int ms = 0;

		if (this.duration >= 0L) {
			hh = (int) this.duration / 3600000;
			long ln = this.duration % 3600000;
			mm = (int) ln / 60000;
			ln = (int) ln % 60000;
			ss = (int) ln / 1000;
			ms = (int) ln % 1000;
		}
		if (hh < 10) {
			buffer.append("0");
		}
		buffer.append(Integer.toString(hh));
		buffer.append(":");
		if (mm < 10) {
			buffer.append("0");
		}
		buffer.append(Integer.toString(mm));
		buffer.append(":");
		if (ss < 10) {
			buffer.append("0");
		}
		buffer.append(Integer.toString(ss));
		buffer.append(".");
		if (ms < 100) {
			buffer.append("0");
			if (ms < 10) {
				buffer.append("0");
			}
		}
		buffer.append(Integer.toString(ms));

		return buffer.toString();
	}
}
