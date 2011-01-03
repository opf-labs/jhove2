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
import com.sleepycat.persist.model.Persistent;

/**
 * Message digest algorithm and value.
 * 
 * @author mstrong, slabrams
 */
@Persistent
public class Digest
    extends AbstractReportable
    implements Comparable<Digest>
{
	/** Message digest algorithm. */
	protected String algorithm;

	/** Message digest value, hex encoded. */
	protected String value;

	/**
	 * Instantiate a new <code>Digest</code>.
	 * 
	 * @param value
	 *            Message digest value, hex encoded
	 * @param algorithm
	 *            Message digest algorithm
	 */
	public Digest(String value, String algorithm) {
	    super();
		this.value = value;
		this.algorithm = algorithm;
	}
	
	public Digest(){
		this(null, null);
	}

	/**
	 * Get the message digest algorithm.
	 * 
	 * @return Message digest algorithm
	 */
	@ReportableProperty(order=1, value="Message digest algorithm.")
	public String getAlgorithm() {
		return this.algorithm;
	}

	/**
	 * Get the message digest value.
	 * 
	 * @return Message digest value, hex encoded
	 */
	@ReportableProperty(order=2, value="Message digest value.")
	public String getValue() {
		return this.value;
	}

	/**
	 * Get a String representation of the message digest, in the form
	 * "algorithm:value".
	 * 
	 * @return String representation of the message digest
	 */
	@Override
	public String toString() {
		return "[" + this.algorithm + "] " + value;
	}

	/**
	 * Lexically compare message digest.
	 * 
	 * @param digest
	 *            Message digest to be compared
	 * @return -1, 0, or 1 if this identifier value is less than, equal to, or
	 *         greater than the second
	 * @see java.lang.Comparable#compareTo(Object)
	 */
	@Override
	public int compareTo(Digest digest) {
		int ret = this.algorithm.compareToIgnoreCase(digest.getAlgorithm());
		if (ret == 0) {
			ret = this.value.compareToIgnoreCase(digest.getValue());
		}
		return ret;
	}
}
