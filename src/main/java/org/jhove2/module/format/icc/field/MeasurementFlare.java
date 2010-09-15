/**
 * JHOVE2 - Next-generation architecture for format-aware characterization
 *
 * Copyright (c) 2009 by The Regents of the University of California
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

package org.jhove2.module.format.icc.field;

import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;

/** ICC measurement flare, as defined in ICC.1:2004-10, Table 42.
 * 
 * @author slabrams
 */
public class MeasurementFlare
    implements Comparable<MeasurementFlare>
{
    /** Singleton measurement flare. */
    protected static Set<MeasurementFlare> flares;

    /** Measurement flare value. */
    protected long value;

    /** Measurement flare. */
    protected String flare;

    /**
     * Instantiate a new <code>MeasurementFlare</code> object.
     * @param value Measurement flare value
     * @param flare
     *            Measurement flare
     */
    public MeasurementFlare(long value, String flare) {
        this.value    = value;
        this.flare = flare;
    }

    /** Initialize the measurement flares.
     * @param jhove2 JHOVE2 framework
     * @throws JHOVE2Exception 
     */
    protected static synchronized void init(JHOVE2 jhove2)
        throws JHOVE2Exception
    {
        if (flares == null) {
            /* Initialize the measurement flares from a Java resource bundle. */
            flares = new TreeSet<MeasurementFlare>();
            Properties props = jhove2.getConfigInfo().getProperties("MeasurementFlares");
            if (props != null) {
                Set<String> set = props.stringPropertyNames();
                Iterator<String> iter = set.iterator();
                while (iter.hasNext()) {
                    String val  = iter.next();
                    String fla = props.getProperty(val);
                    MeasurementFlare flare = new MeasurementFlare(Integer.valueOf(val), fla);
                    flares.add(flare);
                }
            }
        }
    }
    
    /**
     * Get the flare for a measurement flare value.
     * 
     * @param value  Measurement flare value
     * @param jhove2 JHOVE2 framework
     * @return Measurement flare, or null if the value is not defined
     * @throws JHOVE2Exception
     */
    public static synchronized MeasurementFlare getMeasurementFlare(long value, JHOVE2 jhove2)
        throws JHOVE2Exception
    {
        init(jhove2);
        MeasurementFlare flare = null;
        Iterator<MeasurementFlare> iter = flares.iterator();
        while (iter.hasNext()) {
            MeasurementFlare fla = iter.next();
            if (fla.getValue() == value) {
                flare = fla;
                break;
            }
        }

        return flare;
    }

    /**
     * Get the measurement flares.
     * @param jhove2 JHOVE2 framework
     * @return Measurement flares
     * @throws JHOVE2Exception 
     */
    public static Set<MeasurementFlare> getMeasurementFlares(JHOVE2 jhove2)
        throws JHOVE2Exception
    {
        init(jhove2);
        return flares;
    }
    
    /**
     * Get the measurement flare.
     * @return Measurement flare
     */
    public String getFlare() {
        return this.flare;
    }
 
    /**
     * Get the measurement flare value.
     * @return Measurement flare value
     */
    public long getValue() {
        return this.value;
    }

    /**
     * Convert the measurement flare to a Java string in the form:
     * "value: flare".
     * @return Java string representation of the measurement flare
     */
    public String toString() {
        return this.value + ": " + this.flare;
    }

    /**
     * Compare measurement flare.
     * @param type
     *            Measurement flare
     * @return -1, 0, or 1 if this value is less than,
     *         equal to, or greater than the second
     */
    @Override
    public int compareTo(MeasurementFlare type) {
        long value = type.getValue();
        if (this.value < value) {
            return -1;
        }
        else if (this.value > value) {
            return  1;
        }
        return 0;
    }
}
