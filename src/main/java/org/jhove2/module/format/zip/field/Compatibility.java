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

/** Zip local file header version compatibility.
 * 
 * @author slabrams
 */
public class Compatibility
    implements Comparable<Compatibility>
{
    /** Singleton Zip local file header version compatibilities. */
    protected static Set<Compatibility> compatibilties;

    /** Zip local file header version compatability platform. */
    protected String platform;

    /** Zip local file header version compatibility. */
    protected short compatibility;

    /**
     * Instantiate a new <code>Compatibility</code> object.
     * 
     * @param compatibility
     *            Zip local file header version compatibility
     * @param platform
     *            Zip local file header version compatibility platform
     */
    public Compatibility(short compatibility, String platform) {
        this.compatibility = compatibility;
        this.platform      = platform;
    }

    /**
     * Get the Zip local file header version compatibility. 
     * @param compatibility Zip local file header version compatibility
     * @param jhove2    JHOVE2 framework
     * @return Zip local file header version compatibility, or null
     * @throws JHOVE2Exception
     */
    public static synchronized Compatibility getCompatibility(short compatibility, JHOVE2 jhove2)
            throws JHOVE2Exception {
        if (compatibilties == null) {
            /* Initialize the ZIp local file header version compatibilities from a Java resource bundle. */
            compatibilties = new TreeSet<Compatibility>();
            Properties props = jhove2.getConfigInfo().getProperties("Compatibilities");
            if (props != null) {
                Set<String> set = props.stringPropertyNames();
                Iterator<String> iter = set.iterator();
                while (iter.hasNext()) {
                    String com = iter.next();
                    String pla = props.getProperty(com);
                    Compatibility compat = new Compatibility(Short.valueOf(com), pla);
                    compatibilties.add(compat);
                }
            }
        }
        Compatibility compat = null;
        Iterator<Compatibility> iter = compatibilties.iterator();
        while (iter.hasNext()) {
            Compatibility com = iter.next();
            if (com.getCompatibility() == compatibility) {
                compat = com;
                break;
            }
        }
        return compat;
    }

    /**
     * Get the Zip local file header version compatibilities.
     * @return Zip local file header version compatibilities
     */
    public static Set<Compatibility> getCompatibilities() {
        return compatibilties;
    }

    /**
     * Get the Zip local file header version compatibility signature.
     * @return Zip local file header version compatibility signature
     */
    public short getCompatibility() {
        return this.compatibility;
    }

    /**
     * Get the Zip local file header version compatibility platform.
     * @return Zip local file header version compatibility platform
     */
    public String getPlatform() {
        return this.platform;
    }

    /**
     * Convert the Zip local file header version compatibility to a Java string in the form:
     * "compatibility: platform".
     * @return Java string representation of the Zip local file header version compatibility
     */
    public String toString() {
        return this.getCompatibility() + ": " + this.getPlatform();
    }

    /**
     * Compare Zip local file header version compatibility.
     * @param compatibility
     *            Zip local file header version compatibility to be compared
     * @return -1, 0, or 1 if this ZIp local file header version compatibility is less than,
     *         equal to, or greater than the second
     */
    @Override
    public int compareTo(Compatibility compatibility) {
        short com = compatibility.getCompatibility();
        
        if (this.compatibility < com) {
            return -1;
        }
        else if (this.compatibility > com) {
            return 1;
        }
        return 0;
    }
}
