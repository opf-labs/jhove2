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
package org.jhove2.module.format.tiff.type.desc;

import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;

/**
 * @author mstrong
 * 
 */
public class Photometric implements Comparable<Photometric> {

    /** Singleton Photometric values and their descriptions. */
    protected static Set<Photometric> values;

    /** Photometric value */
    protected int value;

    /** Photometric value description */
    protected String description;

    /**
     * Instantiate a new <code>Photometric</code> object.
     * 
     * @param value
     *            Photometric value
     * @param description
     *            Photometric description
     */
    public Photometric(int value, String desc) {
        this.value = value;
        this.description = desc;
    }

    /**
     * Get the description for a Photometric value.
     * 
     * @param value
     *            Photometric value
     * @param jhove2
     *            JHOVE2 framework
     * @return Photometric description, or null if the value is not defined
     * @throws JHOVE2Exception
     */
    public static synchronized Photometric getPhotometricValue(int value,
            JHOVE2 jhove2) throws JHOVE2Exception {
        if (values == null) {
            /* Initialize the CMMs from a Java resource bundle. */
            values = new TreeSet<Photometric>();
            Properties props = jhove2.getConfigInfo().getProperties(
                    "Photometric");
            if (props != null) {
                Set<String> set = props.stringPropertyNames();
                Iterator<String> iter = set.iterator();
                while (iter.hasNext()) {
                    String val = iter.next();
                    String desc = props.getProperty(val);
                    Photometric photometric = new Photometric(Integer
                            .valueOf(val), desc);
                    values.add(photometric);
                }
            }
        }
        Photometric pValue = null;
        Iterator<Photometric> iter = values.iterator();
        while (iter.hasNext()) {
            Photometric p = iter.next();
            if (p.getValue() == value) {
                pValue = p;
                break;
            }
        }

        return pValue;
    }

    /**
     * Get the Photometric values.
     * 
     * @return Photometric values
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Get the Photometric value.
     * 
     * @return Photometric value
     */
    public int getValue() {
        return this.value;
    }

    /**
     * Convert the Photometric value to a Java string in the form:
     * "value: parameters".
     * 
     * @return Java string representation of the Photometric value
     */
    public String toString() {
        return this.value + ": " + this.description;
    }

    /**
     * Compare Photometric values
     * 
     * @param value
     *            Photometric value
     * @return -1, 0, or 1 if this value is than, equal to, or greater than the
     *         second
     * 
     */
    @Override
    public int compareTo(Photometric pValue) {
        int value = pValue.getValue();
        if (this.value < value) {
            return -1;
        }
        else if (this.value > value) {
            return 1;
        }
        return 0;
    }

}
