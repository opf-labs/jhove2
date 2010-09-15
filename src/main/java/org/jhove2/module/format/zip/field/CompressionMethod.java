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

package org.jhove2.module.format.zip.field;

import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;

/** Zip local file header compression method.
 * 
 * @author slabrams
 */
public class CompressionMethod
    implements Comparable<CompressionMethod>
{
    /** Singleton Zip local file header methods. */
    protected static Set<CompressionMethod> methods;

    /** Zip local file header compression method description. */
    protected String description;

    /** Zip local file header compression method. */
    protected int method;

    /**
     * Instantiate a new <code>CompressionMethod</code> object.
     * 
     * @param method
     *            Zip local file header compression method
     * @param description
     *            Zip local file header compression method description
     */
    public CompressionMethod(short method, String description) {
        this.method      = method;
        this.description = description;
    }

    /**
     * Get the Zip local file header compression method. 
     * @param method Zip local file header compression method
     * @param jhove2    JHOVE2 framework
     * @return Zip local file header compression method, or null
     * @throws JHOVE2Exception
     */
    public static synchronized CompressionMethod getCompressionMethod(int method, JHOVE2 jhove2)
            throws JHOVE2Exception {
        if (methods == null) {
            /* Initialize the ZIp local file header compression methods from a Java resource bundle. */
            methods = new TreeSet<CompressionMethod>();
            Properties props = jhove2.getConfigInfo().getProperties("CompressionMethods");
            if (props != null) {
                Set<String> set = props.stringPropertyNames();
                Iterator<String> iter = set.iterator();
                while (iter.hasNext()) {
                    String met = iter.next();
                    String des = props.getProperty(met);
                    CompressionMethod m = new CompressionMethod(Short.valueOf(met), des);
                    methods.add(m);
                }
            }
        }
        CompressionMethod met = null;
        Iterator<CompressionMethod> iter = methods.iterator();
        while (iter.hasNext()) {
            CompressionMethod m = iter.next();
            if (m.getMethod() == method) {
                met = m;
                break;
            }
        }
        return met;
    }

    /**
     * Get the Zip local file header compression methods.
     * @return Zip local file header compression methods
     */
    public static Set<CompressionMethod> getMethods() {
        return methods;
    }

    /**
     * Get the Zip local file header compression method.
     * @return Zip local file header compression method
     */
    public int getMethod() {
        return this.method;
    }

    /**
     * Get the Zip local file header compression method description.
     * @return Zip local file header compression method description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Convert the Zip local file header compression method to a Java string in the form:
     * "method: description".
     * @return Java string representation of the Zip local file header method
     */
    public String toString() {
        return this.getMethod() + ": " + this.getDescription();
    }

    /**
     * Compare Zip local file header compression method.
     * @param method
     *            Zip local file header compression method to be compared
     * @return -1, 0, or 1 if this Zip local file header compression method is less than,
     *         equal to, or greater than the second
     */
    @Override
    public int compareTo(CompressionMethod method) {
        int met = method.getMethod();
        
        if (this.method < met) {
            return -1;
        }
        else if (this.method > met) {
            return 1;
        }
        return 0;
    }
}
