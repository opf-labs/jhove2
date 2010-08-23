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

/** ICC profile flags as defined by ICC.1:2004-10, Table 17.
 * 
 * @author slabrams
 */
public class ProfileFlag
implements Comparable<ProfileFlag>
{
    /** Singleton profile flags. */
    protected static Set<ProfileFlag> flags;

    /** Profile flag bit position. */
    protected int position;

    /** Profile flag negative value. */
    protected String negativeValue;
    
    /** Profile flag positive value. */
    protected String positiveValue;

    /**
     * Instantiate a new <code>ProfileFlag</code> object.
     * @param position Profile flag bit position
     * @param negative
     *            Profile flag negative value
     * @param positive
     *            Profile flag positive value
     */
    public ProfileFlag(int position, String negative, String positive) {
        this.position      = position;
        this.negativeValue = negative;
        this.positiveValue = positive;
    }

    /**
     * Initialize the profile flags from their properties file. 
     * @param jhove2 JHOVE2 framework
     */
    protected static synchronized void initProfileFlags(JHOVE2 jhove2)
        throws JHOVE2Exception
    {
        /* Initialize the profile flags from a Java resource bundle. */
        flags = new TreeSet<ProfileFlag>();
        Properties props =
            jhove2.getConfigInfo().getProperties("ProfileFlags");
        if (props != null) {
            Set<String> set = props.stringPropertyNames();
            Iterator<String> iter = set.iterator();
            while (iter.hasNext()) {
                String bit = iter.next();
                String neg = props.getProperty(bit);
                String pos = neg;
                int in = neg.indexOf('|');
                if (in > 0) {
                    pos = neg.substring(in+1);
                    neg = neg.substring(0, in);
                }
                ProfileFlag flag =
                    new ProfileFlag(Integer.valueOf(bit), neg, pos);
                flags.add(flag);
            }
        }
    }

    /**
     * Get the profile flag negative value.
     * @return profile flag negative value
     */
    public String getNegativeValue() {
        return this.negativeValue;
    }
    
    /**
     * Get the profile flags.
     * @param jhove2 JHOVE2 framework
     * @return profile flags
     * @throws JHOVE2Exception 
     */
    public static Set<ProfileFlag> getProfileFlags(JHOVE2 jhove2) throws JHOVE2Exception {
        if (flags == null) {
            initProfileFlags(jhove2);
        }
        return flags;
    }

    /**
     * Get the profile flag bit position.
     * @return profile flag bit position
     */
    public int getPosition() {
        return this.position;
    }

    /**
     * Get the profile flag posative value.
     * @return profile flag posative value
     */
    public String getPositiveValue() {
        return this.positiveValue;
    }

    /**
     * Convert the profile flag to a Java string in the form:
     * "position: negative|positive".
     * @return Java string representation of the profile flag
     */
    public String toString() {
        return this.getPosition() + ": " + this.getNegativeValue() + "|" +
                                           this.getPositiveValue();
    }

    /**
     * Compare profile flag.
     * @param flag
     *            Profile flag to be compared
     * @return -1, 0, or 1 if this profile flag is less than,
     *         equal to, or greater than the second
     */
    @Override
    public int compareTo(ProfileFlag flag) {
        int pos = flag.getPosition();
        if (this.position < pos) {
            return -1;
        }
        else if (this.position > pos) {
            return  1;
        }
        return 0;
    }
}
