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

/** ICC colorant encoding, as defined in ICC.1:2004-10, Table 24.
 * 
 * @author slabrams
 */
public class ColorantEncoding
    implements Comparable<ColorantEncoding>
{
    /** Singleton encoding parameters. */
    protected static Set<ColorantEncoding> encodings;

    /** Encoding value. */
    protected int value;

    /** Encoding type. */
    protected String type;

    /**
     * Instantiate a new <code>ColorantEncoding</code> object.
     * @param value Encoding value
     * @param type  Encoding type
     */
    public ColorantEncoding(int value, String type) {
        this.value = value;
        this.type  = type;
    }

    /**
     * Get the parameters for na encoding value.
     * @param value  Encoding value
     * @param jhove2 JHOVE2 framework
     * @return Function parameters, or null if the value is not defined
     * @throws JHOVE2Exception
     */
    public static synchronized ColorantEncoding getColorantEncoding(int value, JHOVE2 jhove2)
            throws JHOVE2Exception {
        if (encodings == null) {
            /* Initialize the colorant encodings from a Java resource bundle. */
            encodings = new TreeSet<ColorantEncoding>();
            Properties props = jhove2.getConfigInfo().getProperties("ColorantEncodings");
            if (props != null) {
                Set<String> set = props.stringPropertyNames();
                Iterator<String> iter = set.iterator();
                while (iter.hasNext()) {
                    String val = iter.next();
                    String typ = props.getProperty(val);
                    ColorantEncoding encoding = new ColorantEncoding(Integer.valueOf(val), typ);
                    encodings.add(encoding);
                }
            }
        }
        ColorantEncoding encoding = null;
        Iterator<ColorantEncoding> iter = encodings.iterator();
        while (iter.hasNext()) {
            ColorantEncoding enc = iter.next();
            if (enc.getValue() == value) {
                encoding = enc;
                break;
            }
        }

        return encoding;
    }

    /**
     * Get the encoding type.
     * @return Encoding type
     */
    public String getType() {
        return this.type;
    }
 
    /**
     * Get the encoding value.
     * @return Encoding value
     */
    public int getValue() {
        return this.value;
    }

    /**
     * Convert the colorant encoding to a Java string in the form:
     * "value: parameters".
     * @return Java string representation of the colorant encoding
     */
    public String toString() {
        return this.value + ": " + this.type;
    }

    /**
     * Compare colorant encoding.
     * @param encoding
     *            Colorant encoding
     * @return -1, 0, or 1 if this value is less than,
     *         equal to, or greater than the second
     */
    @Override
    public int compareTo(ColorantEncoding encoding) {
        int value = encoding.getValue();
        if (this.value < value) {
            return -1;
        }
        else if (this.value > value) {
            return  1;
        }
        return 0;
    }
}
