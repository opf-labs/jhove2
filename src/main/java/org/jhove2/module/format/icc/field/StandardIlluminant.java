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

/** ICC standard illuminant, as defined in ICC.1:2004-10, Table 43.
 * 
 * @author slabrams
 */
public class StandardIlluminant
    implements Comparable<StandardIlluminant>
{
    /** Singleton standard illuminant illuminant. */
    protected static Set<StandardIlluminant> illuminants;

    /** Standard illuminant value. */
    protected long value;

    /** Standard illuminant. */
    protected String illuminant;

    /**
     * Instantiate a new <code>StandardIlluminant</code> object.
     * @param value Standard illuminant value
     * @param illuminant
     *            Standard illuminant
     */
    public StandardIlluminant(long value, String illuminant) {
        this.value    = value;
        this.illuminant = illuminant;
    }
    
    /** Initialize the standard illuminants.
     * @param jhove2 JHOVE2 framework
     * @throws JHOVE2Exception 
     */
    protected static synchronized void init(JHOVE2 jhove2)
        throws JHOVE2Exception
    {
        if (illuminants == null) {
            /* Initialize the standard illuminants from a Java resource bundle. */
            illuminants = new TreeSet<StandardIlluminant>();
            Properties props = jhove2.getConfigInfo().getProperties("StandardIlluminants");
            if (props != null) {
                Set<String> set = props.stringPropertyNames();
                Iterator<String> iter = set.iterator();
                while (iter.hasNext()) {
                    String val  = iter.next();
                    String ill = props.getProperty(val);
                    StandardIlluminant illuminant = new StandardIlluminant(Long.valueOf(val), ill);
                    illuminants.add(illuminant);
                }
            }
        }
    }

    /**
     * Get the illuminant for a standard illuminant value.
     * 
     * @param value  Standard illuminant value
     * @param jhove2 JHOVE2 framework
     * @return Standard illuminant, or null if the value is not defined
     * @throws JHOVE2Exception
     */
    public static synchronized StandardIlluminant getStandardIlluminant(long value, JHOVE2 jhove2)
        throws JHOVE2Exception
    {
        init(jhove2);
        StandardIlluminant illuminant = null;
        Iterator<StandardIlluminant> iter = illuminants.iterator();
        while (iter.hasNext()) {
            StandardIlluminant ill = iter.next();
            if (ill.getValue() == value) {
                illuminant = ill;
                break;
            }
        }

        return illuminant;
    }
    
    /** Get the standard illuminants.
     * @param jhove2 JHOVE2 framework
     * @throws JHOVE2Exception 
     */
    public static Set<StandardIlluminant> getStandardIlluminants(JHOVE2 jhove2)
        throws JHOVE2Exception
    {
        init(jhove2);
        return illuminants;
    }

    /**
     * Get the standard illuminant.
     * @return Standard illuminant
     */
    public String getIlluminant() {
        return this.illuminant;
    }
 
    /**
     * Get the standard illuminant value.
     * @return Standard illuminant value
     */
    public long getValue() {
        return this.value;
    }

    /**
     * Convert the standard illuminant to a Java string in the form:
     * "value: illuminant".
     * @return Java string representation of the standard illuminant
     */
    public String toString() {
        return this.value + ": " + this.illuminant;
    }

    /**
     * Compare standard illuminant.
     * @param type
     *            Standard illuminant
     * @return -1, 0, or 1 if this value is less than,
     *         equal to, or greater than the second
     */
    @Override
    public int compareTo(StandardIlluminant type) {
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
