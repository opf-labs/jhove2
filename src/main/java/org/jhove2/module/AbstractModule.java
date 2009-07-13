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

import org.jhove2.core.Product;
import org.jhove2.core.Duration;

/** An abstract JHOVE2 module, a {@link org.jhove2.core.Product} that reports
 * its elapsed processing time.
 * 
 * @author mstrong, slabrams
 */
public class AbstractModule
	extends Product
	implements Module
{	
	/** Module elapsed end time. */
	protected long endTime;
	
	/** Module elapsed start time. */
	protected long startTime;
	
	/** Module wrapped product.  This field should be defined if the module
	 * does not directly perform its own processing, but rather invokes
	 * an external tool.
	 */
	protected Product product;
	
	/** Instantiate a new <code>AbstractModule</code>.
	 * @oaran version Module version identifier in three-part form: "M.N.P"
	 * @param release Module release date in ISO 8601 format: "YYYY-MM-DD"
	 * @param rights  Module rights statement
	 */
	public AbstractModule(String version, String release, String rights) {
		super(version, release, rights);
		
		this.startTime = System.currentTimeMillis();
		this.endTime   = Duration.UNINITIALIZED;
	}
	
	/** Get elapsed time, in milliseconds.  The reported time will never be
	 * less than 1 milliscond.
	 * @return Elapsed time, in milliseconds
	 * @see org.jhove2.core.Temporal#getElapsedTime()
	 */
	@Override
	public Duration getElapsedTime() {
		if (this.endTime == Duration.UNINITIALIZED) {
			this.endTime = System.currentTimeMillis();
		}
		
		return new Duration(this.endTime - this.startTime);
	}

	/** Get wrapped {@link org.jhove2.core.Product}.
	 * @return Wrapped {@link org.jhove2.core.Product}
	 * @see org.jhove2.module.Module#getWrappedProduct()
	 */
	@Override
	public Product getWrappedProduct() {
		return this.product;
	}
	
	/** Set the end time of the elapsed duration.
	 * @return End time, in millieconds
	 * @see org.jhove2.core.Temporal#setEndTime()
	 */
	@Override
	public long setEndTime() {
		return this.endTime = System.currentTimeMillis();
	}
	
	/** Set the restart time of the elapsed duration.  All subsequent time
	 * (until the next invocation of the setEndTime() method) will be added
	 * to the time already accounted for by an earlier invocation of the
	 * setEndTime() method.
	 * @return Current time minus the elapsed time, in milliseconds
	 * @see org.jhove2.core.Temporal#setReStartTime()
	 */
	public long setRestartTime() {
		if (this.endTime == Duration.UNINITIALIZED) {
			return this.startTime = System.currentTimeMillis();
		}
		return this.startTime = System.currentTimeMillis() - this.endTime + this.startTime;
	}
	
	/** Set the start time of the elapsed duration.
	 * @return Start time, in milliseconds
	 * @see org.jhove2.core.Temporal#setStartTime()
	 */
	@Override
	public long setStartTime() {
		return this.startTime = System.currentTimeMillis();
	}

	/** Set wrapped product.
	 * @param product Wrapped product
	 */
	public void setWrappedProduct(Product product) {
		this.product = product;
	}
}
