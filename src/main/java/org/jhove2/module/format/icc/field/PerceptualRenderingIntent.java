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

/** ICC perceptual rendering intent gamut.
 * 
 * @author slabrams
 */
public class PerceptualRenderingIntent
    implements Comparable<PerceptualRenderingIntent>
{
    /** Singleton perceptual rendering intent gamuts. */
    protected static Set<PerceptualRenderingIntent> gamuts;

    /** Perceptual rendering intent gamut. */
    protected String gamut;

    /** Perceptual rendering intent gamut signature. */
    protected String signature;

    /**
     * Instantiate a new <code>PerceptualRenderingIntent</code> object.
     * 
     * @param signature
     *            Perceptual rendering intent gamut signature
     * @param gamut
     *            perceptual rendering intent gamut
     */
    public PerceptualRenderingIntent(String signature, String gamut) {
        this.signature = signature;
        this.gamut     = gamut;
    }

    /**
     * Get the perceptual rendering intent gamut for a signature. 
     * @param signature Data perceptual rendering intent gamut signature
     * @param jhove2    JHOVE2 framework
     * @return Data perceptual rendering intent gamut, or null if the signature is not a perceptual rendering intent gamut signature
     * @throws JHOVE2Exception
     */
    public static synchronized PerceptualRenderingIntent getPerceptualRenderingIntent(String signature, JHOVE2 jhove2)
            throws JHOVE2Exception {
        if (gamuts == null) {
            /* Initialize the perceptual rendering intent gamuts from a Java resource bundle. */
            gamuts = new TreeSet<PerceptualRenderingIntent>();
            Properties props = jhove2.getConfigInfo().getProperties("PerceptualRenderingIntents");
            if (props != null) {
                Set<String> set = props.stringPropertyNames();
                Iterator<String> iter = set.iterator();
                while (iter.hasNext()) {
                    String sig = iter.next();
                    String gam = props.getProperty(sig);
                    PerceptualRenderingIntent gamut = new PerceptualRenderingIntent(sig, gam);
                    gamuts.add(gamut);
                }
            }
        }
        PerceptualRenderingIntent gamut = null;
        Iterator<PerceptualRenderingIntent> iter = gamuts.iterator();
        while (iter.hasNext()) {
            PerceptualRenderingIntent gam = iter.next();
            if (gam.getSignature().equals(signature)) {
                gamut = gam;
                break;
            }
        }
        return gamut;
    }

    /**
     * Get the perceptual rendering intent gamuts.
     * @return perceptual rendering intent gamuts
     */
    public static Set<PerceptualRenderingIntent> getPerceptualRenderingIntentes() {
        return gamuts;
    }

    /**
     * Get the perceptual rendering intent gamut.
     * @return perceptual rendering intent gamut
     */
    public String getGamut() {
        return this.gamut;
    }

    /**
     * Get the perceptual rendering intent gamut signature.
     * @return perceptual rendering intent gamut signature
     */
    public String getSignature() {
        return this.signature;
    }

    /**
     * Convert the perceptual rendering intent gamut to a Java string in the form:
     * "signature: gamut".
     * @return Java string representation of the perceptual rendering intent gamut
     */
    public String toString() {
        return this.getSignature() + ": " + this.getGamut();
    }

    /**
     * Compare perceptual rendering intent gamut.
     * @param space
     *            Pperceptual rendering intent gamut to be compared
     * @return -1, 0, or 1 if this perceptual rendering intent gamut is less than,
     *         equal to, or greater than the second
     */
    @Override
    public int compareTo(PerceptualRenderingIntent gamut) {
        return this.signature.compareTo(gamut.getSignature());
    }
}
