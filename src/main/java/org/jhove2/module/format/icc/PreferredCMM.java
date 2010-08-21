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

package org.jhove2.module.format.icc;

import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;

/** ICC preferred Colour Management Module (CMM).
 * 
 * @author slabrams
 */
public class PreferredCMM
    implements Comparable<PreferredCMM>
{
    /** Singleton preferred CMMs. */
    protected static Set<PreferredCMM> CMMs;

    /** CMM description. */
    protected String description;

    /** CMM signature. */
    protected String signature;

    /**
     * Instantiate a <code>PreferredCMM</code> object.
     * 
     * @param signature
     *            CMM signature
     * @param description
     *            CMM description
     */
    public PreferredCMM(String signature, String description) {
        this.signature   = signature;
        this.description = description;
    }

    /**
     * Get the preferred CMM description for a signature.
     * 
     * @param signature CMM signature
     * @param jhove2    JHOVE2 framework
     * @return Preferred CMM, or null if the signature is not a CMM signature
     * @throws JHOVE2Exception
     */
    public static synchronized PreferredCMM getPreferredCMM(String signature, JHOVE2 jhove2)
            throws JHOVE2Exception {
        if (CMMs == null) {
            /* Initialize the CMMs from s Java resource bundle. */
            CMMs = new TreeSet<PreferredCMM>();
            Properties props = jhove2.getConfigInfo().getProperties("PreferredCMM");
            if (props != null) {
                Set<String> set = props.stringPropertyNames();
                Iterator<String> iter = set.iterator();
                while (iter.hasNext()) {
                    String sig  = iter.next();
                    String desc = props.getProperty(sig);
                    PreferredCMM prefCMM = new PreferredCMM(sig, desc);
                    CMMs.add(prefCMM);
                }
            }
        }
        PreferredCMM prefCMM = null;
        Iterator<PreferredCMM> iter = CMMs.iterator();
        while (iter.hasNext()) {
            PreferredCMM cmm = iter.next();
            if (cmm.getSignature().equals(signature)) {
                prefCMM = cmm;
                break;
            }
        }

        return prefCMM;
    }

    /**
     * Get the preferred CMMs.
     * 
     * @return PreferredCMMs
     */
    public static Set<PreferredCMM> getPreferredCMMs() {
        return CMMs;
    }

    /**
     * Get the preferred CMM description.
     * 
     * @return Preferred CMM description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Get the preferred CMM signature.
     * 
     * @return Preferred CMM signature
     */
    public String getSignature() {
        return this.signature;
    }

    /**
     * Convert the preferred CMM to a Java string in the form: "signature: description".
     * 
     * @return Java string representation of the preferred CMM
     */
    public String toString() {
        return this.getSignature() + ": " + this.getDescription();
    }

    /**
     * Compare preferred CMM.
     * 
     * @param cmm
     *            Preferred CMM to be compared
     * @return -1, 0, or 1 if this CMM is less than, equal to, or greater
     *         than the second
     */
    @Override
    public int compareTo(PreferredCMM cmm) {
        return this.signature.compareTo(cmm.getSignature());
    }
}
