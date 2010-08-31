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

/** ICC primary platform as defined by ICC.1:2004-10, Table 16.
 * 
 * @author slabrams
 */
public class PrimaryPlatform
implements Comparable<PrimaryPlatform>
{
    /** Singleton primary platforms. */
    protected static Set<PrimaryPlatform> platforms;

    /** Profile class. */
    protected String primaryPlatform;

    /** Profile signature. */
    protected String signature;

    /**
     * Instantiate a new <code>PrimaryPlatform</code> object.
     * 
     * @param signature
     *            Profile signature
     * @param Primary platform
     *            Primary platform
     */
    public PrimaryPlatform(String signature, String primaryPlatform) {
        this.signature       = signature;
        this.primaryPlatform = primaryPlatform;
    }

    /**
     * Get the primary platform for a signature. 
     * @param signature Primary platform signature
     * @param jhove2    JHOVE2 framework
     * @return Primary platform, or null if the signature is not a colour space signature
     * @throws JHOVE2Exception
     */
    public static synchronized PrimaryPlatform getPrimaryPlatform(String signature, JHOVE2 jhove2)
            throws JHOVE2Exception {
        if (platforms == null) {
            /* Initialize the primary platforms from a Java resource bundle. */
            platforms = new TreeSet<PrimaryPlatform>();
            Properties props = jhove2.getConfigInfo().getProperties("PrimaryPlatforms");
            if (props != null) {
                Set<String> set = props.stringPropertyNames();
                Iterator<String> iter = set.iterator();
                while (iter.hasNext()) {
                    String sig = iter.next();
                    String pla = props.getProperty(sig);
                    PrimaryPlatform space = new PrimaryPlatform(sig, pla);
                    platforms.add(space);
                }
            }
        }
        PrimaryPlatform primaryPlatform = null;
        Iterator<PrimaryPlatform> iter = platforms.iterator();
        while (iter.hasNext()) {
            PrimaryPlatform platform = iter.next();
            if (platform.getSignature().equals(signature)) {
                primaryPlatform = platform;
                break;
            }
        }
        return primaryPlatform;
    }

    /**
     * Get the primary platforms.
     * @return primary platforms
     */
    public static Set<PrimaryPlatform> getPrimaryPlatformes() {
        return platforms;
    }

    /**
     * Get the primary platform.
     * @return primary platform
     */
    public String getPrimaryPlatform() {
        return this.primaryPlatform;
    }

    /**
     * Get the primary platform signature.
     * @return primary platform signature
     */
    public String getSignature() {
        return this.signature;
    }

    /**
     * Convert the primary platform to a Java string in the form:
     * "signature: platform".
     * @return Java string representation of the primary platform
     */
    public String toString() {
        return this.getSignature() + ": " + this.getPrimaryPlatform();
    }

    /**
     * Compare primary platform.
     * @param space
     *            Primary platform to be compared
     * @return -1, 0, or 1 if this primary platform is less than,
     *         equal to, or greater than the second
     */
    @Override
    public int compareTo(PrimaryPlatform space) {
        return this.signature.compareTo(space.getSignature());
    }
}
