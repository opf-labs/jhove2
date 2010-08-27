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

/** ICC saturation rendering intent gamut.
 * 
 * @author slabrams
 */
public class SaturationRenderingIntent
    implements Comparable<SaturationRenderingIntent>
{
    /** Singleton saturation rendering intent gamuts. */
    protected static Set<SaturationRenderingIntent> gamuts;

    /** Saturation rendering intent gamut. */
    protected String gamut;

    /** Saturation rendering intent gamut signature. */
    protected String signature;

    /**
     * Instantiate a new <code>SaturationRenderingIntent</code> object.
     * 
     * @param signature
     *            Saturation rendering intent gamut signature
     * @param gamut
     *            saturation rendering intent gamut
     */
    public SaturationRenderingIntent(String signature, String gamut) {
        this.signature = signature;
        this.gamut     = gamut;
    }

    /**
     * Get the saturation rendering intent gamut for a signature. 
     * @param signature Data saturation rendering intent gamut signature
     * @param jhove2    JHOVE2 framework
     * @return Data saturation rendering intent gamut, or null if the signature is not a saturation rendering intent gamut signature
     * @throws JHOVE2Exception
     */
    public static synchronized SaturationRenderingIntent getSaturationRenderingIntent(String signature, JHOVE2 jhove2)
            throws JHOVE2Exception {
        if (gamuts == null) {
            /* Initialize the saturation rendering intent gamuts from a Java resource bundle. */
            gamuts = new TreeSet<SaturationRenderingIntent>();
            Properties props = jhove2.getConfigInfo().getProperties("SaturationRenderingIntents");
            if (props != null) {
                Set<String> set = props.stringPropertyNames();
                Iterator<String> iter = set.iterator();
                while (iter.hasNext()) {
                    String sig = iter.next();
                    String gam = props.getProperty(sig);
                    SaturationRenderingIntent gamut = new SaturationRenderingIntent(sig, gam);
                    gamuts.add(gamut);
                }
            }
        }
        SaturationRenderingIntent gamut = null;
        Iterator<SaturationRenderingIntent> iter = gamuts.iterator();
        while (iter.hasNext()) {
            SaturationRenderingIntent gam = iter.next();
            if (gam.getSignature().equals(signature)) {
                gamut = gam;
                break;
            }
        }
        return gamut;
    }

    /**
     * Get the saturation rendering intent gamuts.
     * @return saturation rendering intent gamuts
     */
    public static Set<SaturationRenderingIntent> getSaturationRenderingIntentes() {
        return gamuts;
    }

    /**
     * Get the saturation rendering intent gamut.
     * @return saturation rendering intent gamut
     */
    public String getGamut() {
        return this.gamut;
    }

    /**
     * Get the saturation rendering intent gamut signature.
     * @return saturation rendering intent gamut signature
     */
    public String getSignature() {
        return this.signature;
    }

    /**
     * Convert the saturation rendering intent gamut to a Java string in the form:
     * "signature: gamut".
     * @return Java string representation of the saturation rendering intent gamut
     */
    public String toString() {
        return this.getSignature() + ": " + this.getGamut();
    }

    /**
     * Compare saturation rendering intent gamut.
     * @param space
     *            Saturation rendering intent gamut to be compared
     * @return -1, 0, or 1 if this saturation rendering intent gamut is less than,
     *         equal to, or greater than the second
     */
    @Override
    public int compareTo(SaturationRenderingIntent gamut) {
        return this.signature.compareTo(gamut.getSignature());
    }
}
