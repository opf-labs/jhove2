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

/** ICC parametric curve type function type, as defined in
 * ICC.1:2004-10, Table 47.
 * 
 * @author slabrams
 */
public class FunctionType
    implements Comparable<FunctionType>
{
    /** Singleton function parameters. */
    protected static Set<FunctionType> types;

    /** Function value. */
    protected int value;

    /** Function parameters. */
    protected String parameters;

    /**
     * Instantiate a new <code>FunctionType</code> object.
     * @param value Function value
     * @param parameters
     *            Function parameters
     */
    public FunctionType(int value, String parameters) {
        this.value      = value;
        this.parameters = parameters;
    }

    /**
     * Get the parameters for a function  value.
     * 
     * @param value  Function value
     * @param jhove2 JHOVE2 framework
     * @return Function parameters, or null if the value is not defined
     * @throws JHOVE2Exception
     */
    public static synchronized FunctionType getFunctionType(int value, JHOVE2 jhove2)
            throws JHOVE2Exception {
        if (types == null) {
            /* Initialize the CMMs from a Java resource bundle. */
            types = new TreeSet<FunctionType>();
            Properties props = jhove2.getConfigInfo().getProperties("FunctionTypes");
            if (props != null) {
                Set<String> set = props.stringPropertyNames();
                Iterator<String> iter = set.iterator();
                while (iter.hasNext()) {
                    String val  = iter.next();
                    String par = props.getProperty(val);
                    FunctionType type = new FunctionType(Integer.valueOf(val), par);
                    types.add(type);
                }
            }
        }
        FunctionType type = null;
        Iterator<FunctionType> iter = types.iterator();
        while (iter.hasNext()) {
            FunctionType typ = iter.next();
            if (typ.getValue() == value) {
                type = typ;
                break;
            }
        }

        return type;
    }

    /**
     * Get the function parameters.
     * @return Function parameters
     */
    public String getParameters() {
        return this.parameters;
    }
 
    /**
     * Get the function value.
     * @return Function value
     */
    public int getValue() {
        return this.value;
    }

    /**
     * Convert the function type to a Java string in the form:
     * "value: parameters".
     * @return Java string representation of the function type
     */
    public String toString() {
        return this.value + ": " + this.parameters;
    }

    /**
     * Compare function type.
     * @param type
     *            Function type
     * @return -1, 0, or 1 if this value is less than,
     *         equal to, or greater than the second
     */
    @Override
    public int compareTo(FunctionType type) {
        int value = type.getValue();
        if (this.value < value) {
            return -1;
        }
        else if (this.value > value) {
            return  1;
        }
        return 0;
    }
}
