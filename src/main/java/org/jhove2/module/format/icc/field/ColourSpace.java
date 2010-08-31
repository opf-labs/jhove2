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

/** ICC data colour space as defined in ICC.1:2004-10, Table 15.
 * 
 * @author slabrams
 */
public class ColourSpace
implements Comparable<ColourSpace>
{
    /** Singleton data colour spaces. */
    protected static Set<ColourSpace> spaces;

    /** Profile class. */
    protected String colourSpace;

    /** Profile signature. */
    protected String signature;

    /**
     * Instantiate a new <code>ColourSpace</code> object.
     * 
     * @param signature
     *            Profile signature
     * @param colourSpace
     *            data colour space
     */
    public ColourSpace(String signature, String colourSpace) {
        this.signature    = signature;
        this.colourSpace = colourSpace;
    }

    /**
     * Get the data colour space for a signature. 
     * @param signature Data colour space signature
     * @param jhove2    JHOVE2 framework
     * @return Data colour space, or null if the signature is not a colour space signature
     * @throws JHOVE2Exception
     */
    public static synchronized ColourSpace getColourSpace(String signature, JHOVE2 jhove2)
            throws JHOVE2Exception {
        if (spaces == null) {
            /* Initialize the data colour spaces from a Java resource bundle. */
            spaces = new TreeSet<ColourSpace>();
            Properties props = jhove2.getConfigInfo().getProperties("ColourSpaces");
            if (props != null) {
                Set<String> set = props.stringPropertyNames();
                Iterator<String> iter = set.iterator();
                while (iter.hasNext()) {
                    String sig = iter.next();
                    String spa = props.getProperty(sig);
                    ColourSpace space = new ColourSpace(sig, spa);
                    spaces.add(space);
                }
            }
        }
        ColourSpace colourSpace = null;
        Iterator<ColourSpace> iter = spaces.iterator();
        while (iter.hasNext()) {
            ColourSpace space = iter.next();
            if (space.getSignature().equals(signature)) {
                colourSpace = space;
                break;
            }
        }
        return colourSpace;
    }

    /**
     * Get the data colour spaces.
     * @return data colour spaces
     */
    public static Set<ColourSpace> getColourSpacees() {
        return spaces;
    }

    /**
     * Get the data colour space.
     * @return data colour space
     */
    public String getColourSpace() {
        return this.colourSpace;
    }

    /**
     * Get the data colour space signature.
     * @return data colour space signature
     */
    public String getSignature() {
        return this.signature;
    }

    /**
     * Convert the data colour space to a Java string in the form:
     * "signature: space".
     * @return Java string representation of the data colour space
     */
    public String toString() {
        return this.getSignature() + ": " + this.getColourSpace();
    }

    /**
     * Compare data colour space.
     * @param space
     *            Data colour space to be compared
     * @return -1, 0, or 1 if this data colour space is less than,
     *         equal to, or greater than the second
     */
    @Override
    public int compareTo(ColourSpace space) {
        return this.signature.compareTo(space.getSignature());
    }
}
