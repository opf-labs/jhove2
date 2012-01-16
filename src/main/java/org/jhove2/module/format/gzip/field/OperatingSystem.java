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

package org.jhove2.module.format.gzip.field;


import java.util.Collections;
import java.util.Map;
import java.util.Properties;

import com.sleepycat.persist.model.Persistent;


/**
 * Enumerated type for GZip supported operating systems.
 * <p>
 * Valid values are defined in a injected
 * {@link #initValues(Properties) resource bundle}.  If no bundle
 * is injected, all {@link #fromValue mapped} value will be marked
 * as not valid.</p>
 */
@Persistent
public final class OperatingSystem extends I18nReportableEnum
{
    /** The list of valid values. */
    private static Map<Integer,OperatingSystem> values = Collections.emptyMap();

    /** Zero argument constructor. */
    protected OperatingSystem() {
        super();
    }

    /** {@inheritDoc} */
    protected OperatingSystem(int value, String label, boolean valid) {
        super(value, label, valid);
    }

    /**
     * Returns the enumerated value object corresponding to the
     * specified integer value. If the integer value is unknown, an
     * instance marked as not valid is returned.
     * @param  n   the integer value to map.
     *
     * @return a operating system objet, valid if <code>n</code> is
     *         one of the defined valid values.
     */
    public static OperatingSystem fromValue(int n) {
        OperatingSystem v = values.get(Integer.valueOf(n));
        if (v == null) {
            v = new OperatingSystem(n, null, false);
        }
        return v;
    }

    /**
     * <i>Dependency injection</i> Load description strings from the
     * specified resource bundle.
     * @param  props   the resource bundle.
     */
    public static void initValues(Properties props) {
        values = Collections.unmodifiableMap(
                        I18nReportableEnum.initValues(
                                        OperatingSystem.class, props, null));
    }
}
