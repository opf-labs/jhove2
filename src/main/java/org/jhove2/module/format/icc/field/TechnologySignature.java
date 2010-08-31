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

/** ICC technology tag signature, as defined in ICC.1:2004-10, Table 22.
 * 
 * @author slabrams
 */
public class TechnologySignature
    implements Comparable<TechnologySignature>
{
    /** Singleton technology signatures. */
    protected static Set<TechnologySignature> technologies;

    /** Technology signature. */
    protected String signature;

    /** Technology. */
    protected String technology;

    /**
     * Instantiate a new <code>TechnologySignature</code> object.
     * 
     * @param signature
     *            Technology signature
     * @param Technology
     *            Technology
     */
    public TechnologySignature(String signature, String technology) {
        this.signature  = signature;
        this.technology = technology;
    }

    /**
     * Get the technology for a signature. 
     * @param signature Technology signature
     * @param jhove2    JHOVE2 framework
     * @return Data colour space, or null if the signature is not a colour space signature
     * @throws JHOVE2Exception
     */
    public static synchronized TechnologySignature getTechnology(String signature, JHOVE2 jhove2)
            throws JHOVE2Exception {
        if (technologies == null) {
            /* Initialize the technology signatures from a Java resource bundle. */
            technologies = new TreeSet<TechnologySignature>();
            Properties props = jhove2.getConfigInfo().getProperties("TechnologySignatures");
            if (props != null) {
                Set<String> set = props.stringPropertyNames();
                Iterator<String> iter = set.iterator();
                while (iter.hasNext()) {
                    String sig = iter.next();
                    String tec = props.getProperty(sig);
                    TechnologySignature tech = new TechnologySignature(sig, tec);
                    technologies.add(tech);
                }
            }
        }
        TechnologySignature technology = null;
        Iterator<TechnologySignature> iter = technologies.iterator();
        while (iter.hasNext()) {
            TechnologySignature tech = iter.next();
            if (tech.getSignature().equals(signature)) {
                technology = tech;
                break;
            }
        }
        return technology;
    }

    /**
     * Get the technology signatures.
     * @return technology signatures
     */
    public static Set<TechnologySignature> getSignatures() {
        return technologies;
    }

    /**
     * Get the technology signature.
     * @return Technology signature
     */
    public String getSignature() {
        return this.signature;
    }

    /**
     * Get the technology.
     * @return Technology
     */
    public String getTechnology() {
        return this.technology;
    }

    /**
     * Convert the Technology to a Java string in the form:
     * "signature: technology".
     * @return Java string representation of the technology
     */
    public String toString() {
        return this.getSignature() + ": " + this.getTechnology();
    }

    /**
     * Compare technology signature.
     * @param technology
     *            Technology to be compared
     * @return -1, 0, or 1 if this technology signature is less than,
     *         equal to, or greater than the second
     */
    @Override
    public int compareTo(TechnologySignature technology) {
        return this.signature.compareTo(technology.getSignature());
    }
}
