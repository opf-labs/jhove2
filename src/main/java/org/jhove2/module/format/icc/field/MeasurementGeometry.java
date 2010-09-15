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

/** ICC measurement geometry, as defined in ICC.1:2004-10, Table 41.
 * 
 * @author slabrams
 */
public class MeasurementGeometry
    implements Comparable<MeasurementGeometry>
{
    /** Singleton measurement geometry. */
    protected static Set<MeasurementGeometry> geometries;

    /** Measurement geometry value. */
    protected long value;

    /** Measurement geometry. */
    protected String geometry;

    /**
     * Instantiate a new <code>MeasurementGeometry</code> object.
     * @param value Measurement geometry value
     * @param geometry
     *            Measurement geometry
     */
    public MeasurementGeometry(long value, String geometry) {
        this.value    = value;
        this.geometry = geometry;
    }
    
    /** Initialize the measurement geometries.
     * @param jhove2 JHOVE2 framework
     * @throws JHOVE2Exception 
     */
    protected static synchronized void init(JHOVE2 jhove2)
        throws JHOVE2Exception
    {
        if (geometries == null) {
            /* Initialize the measurement geometrys from a Java resource bundle. */
            geometries = new TreeSet<MeasurementGeometry>();
            Properties props = jhove2.getConfigInfo().getProperties("MeasurementGeometries");
            if (props != null) {
                Set<String> set = props.stringPropertyNames();
                Iterator<String> iter = set.iterator();
                while (iter.hasNext()) {
                    String val  = iter.next();
                    String geo = props.getProperty(val);
                    MeasurementGeometry geometry = new MeasurementGeometry(Integer.valueOf(val), geo);
                    geometries.add(geometry);
                }
            }
        }
    }

    /**
     * Get the geometry for a measurement geometry value.
     * 
     * @param value  Measurement geometry value
     * @param jhove2 JHOVE2 framework
     * @return Measurement geometry, or null if the value is not defined
     * @throws JHOVE2Exception
     */
    public static synchronized MeasurementGeometry getMeasurementGeometry(long value, JHOVE2 jhove2)
        throws JHOVE2Exception
    {
        init(jhove2);
        MeasurementGeometry geometry = null;
        Iterator<MeasurementGeometry> iter = geometries.iterator();
        while (iter.hasNext()) {
            MeasurementGeometry geo = iter.next();
            if (geo.getValue() == value) {
                geometry = geo;
                break;
            }
        }

        return geometry;
    }

    /**
     * Get the measurement geometries.
     * @param jhove2 JHOVE2 framework
     * @return Measurement geometries
     * @throws JHOVE2Exception 
     */
    public static Set<MeasurementGeometry> getMeasurementGeometries(JHOVE2 jhove2)
        throws JHOVE2Exception
    {
        init(jhove2);
        return geometries;
    }

    /**
     * Get the measurement geometry.
     * @return Measurement geometry
     */
    public String getGeometry() {
        return this.geometry;
    }
 
    /**
     * Get the measurement geometry value.
     * @return Measurement geometry value
     */
    public long getValue() {
        return this.value;
    }

    /**
     * Convert the measurement geometry to a Java string in the form:
     * "value: geometry".
     * @return Java string representation of the measurement geometry
     */
    public String toString() {
        return this.value + ": " + this.geometry;
    }

    /**
     * Compare measurement geometry.
     * @param type
     *            Measurement geometry
     * @return -1, 0, or 1 if this value is less than,
     *         equal to, or greater than the second
     */
    @Override
    public int compareTo(MeasurementGeometry type) {
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
