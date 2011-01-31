/**
 * JHOVE2 - Next-generation architecture for format-aware characterization
 * <p>
 * Copyright (c) 2009 by The Regents of the University of California, Ithaka
 * Harbors, Inc., and The Board of Trustees of the Leland Stanford Junior
 * University. All rights reserved.
 * </p>
 * <p>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * </p>
 * <ul>
 * <li>Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.</li>
 * <li>Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.</li>
 * <li>Neither the name of the University of California/California Digital
 * Library, Ithaka Harbors/Portico, or Stanford University, nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.</li>
 * </ul>
 * <p>
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
 * </p>
 */
package org.jhove2.module.format.xml;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.reportable.AbstractReportable;

import com.sleepycat.persist.model.Persistent;

/**
 * A nested class to contain a single NCR code point and the count of its
 * occurrences
 */
@Persistent
public class NumericCharacterReference extends AbstractReportable {

    /** The integer representation of a Unicode character. */
    protected Integer codePoint;

    /** The reference count. */
    protected Integer count;

    protected NumericCharacterReference(){
    	super();
    }
    /**
     * Instantiates a new numeric character reference.
     * 
     * @param codePoint
     *            the integer representation of a Unicode character
     */
    public NumericCharacterReference(Integer codePoint) {
    	this();
        this.codePoint = codePoint;
        this.count = 1;
    }

    /**
     * Gets the numeric character reference code point in Unicode notation.
     * 
     * @return the code point for the NCR
     */
    @ReportableProperty(order = 1, value = "Numeric character reference code point")
    public String getCodePoint() {
        return String.format("U+%04X", codePoint);
    }

    /**
     * Gets the count of NCR occurrences.
     * 
     * @return the count
     */
    @ReportableProperty(order = 2, value = "Reference count")
    /** Gets the numeric character reference count. */
    public Integer getCount() {
        return count;
    }

}