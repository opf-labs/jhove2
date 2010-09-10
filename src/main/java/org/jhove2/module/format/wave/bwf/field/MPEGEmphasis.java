/**
 * JHOVE2 - Next-generation architecture for emphasis-aware characterization
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
 * o Neither the description of the University of California/California Digital
 *   Library, Ithaka Harbors/Portico, or Stanford University, nor the descriptions of
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

package org.jhove2.module.format.wave.bwf.field;

import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;

/** Broadcast Wave Emphasis (BWF) MPEG-1 decoder de-emphasis
 * .
 * @author slabrams
 */
public class MPEGEmphasis
implements Comparable<MPEGEmphasis>
{
    /** Singleton MPEG emphasis emphases. */
    protected static Set<MPEGEmphasis> emphases;

    /** MPEG emphasis emphasis. */
    protected int emphasis;

    /** MPEG description. */
    protected String description;

    /**
     * Instantiate a new <code>MPEGEmphasis</code> object.
     * 
     * @param emphasis
     *            MPEG emphasis
     * @param description
     *            MPEG emphasis description
     */
    public MPEGEmphasis(int emphasis, String description) {
        this.emphasis   = emphasis;
        this.description = description;
    }
    
    /** Initialize the emphases.
     * @param jhove2 JHOVE2 framework
     * @throws JHOVE2Exception 
     */
    protected static synchronized void init(JHOVE2 jhove2)
        throws JHOVE2Exception
    {
        if (emphases == null) {
            /* Initialize the Emphasis emphases from a Java resource bundle. */
            emphases = new TreeSet<MPEGEmphasis>();
            Properties props = jhove2.getConfigInfo().getProperties("MPEGEmphases");
            if (props != null) {
                Set<String> set = props.stringPropertyNames();
                Iterator<String> iter = set.iterator();
                while (iter.hasNext()) {
                    String emp  = iter.next();
                    String des = props.getProperty(emp);
                    MPEGEmphasis e =
                        new MPEGEmphasis(Integer.valueOf(emp, 16), des);
                    emphases.add(e);
                }
            }
        }
    }

    /**
     * Get the description for a emphasis. 
     * @param emphasis   MPEG emphasis
     * @param jhove2 JHOVE2 framework
     * @return Emphasis MPEG emphasis description, or null if the emphasis is not defined
     * @throws JHOVE2Exception
     */
    public static synchronized MPEGEmphasis getMPEGEmphasis(int emphasis, JHOVE2 jhove2)
        throws JHOVE2Exception
    {
        init(jhove2);
        MPEGEmphasis emp = null;
        Iterator<MPEGEmphasis> iter = emphases.iterator();
        while (iter.hasNext()) {
            MPEGEmphasis e = iter.next();
            if (e.getEmphasis() == emphasis) {
                emp = e;
                break;
            }
        }
        return emp;
    }

    /**
     * Get the MPEG emphases.
     * @param jhove2 JHOVE2 framework
     * @return MPEG emphases
     * @throws JHOVE2Exception 
     */
    public static Set<MPEGEmphasis> getEmphases(JHOVE2 jhove2)
        throws JHOVE2Exception
    {
        init(jhove2);
        return emphases;
    }

    /**
     * Get the MPEG emphasis.
     * @return MPEG emphasis 
     */
    public int getEmphasis() {
        return this.emphasis;
    }

    /**
     * Get the emphasis description.
     * @return Emphasis description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Convert the MPEG emphasis to a Java string in the form:
     * "emphasis: description".
     * @return Java string representation of the description
     */
    public String toString() {
        return this.getEmphasis() + ": " + this.getDescription();
    }

    /**
     * Compare MPEG emphasis.
     * @param emphasis
     *             MPEG emphasis to be compared
     * @return -1, 0, or 1 if this MPEG emphasis emphasis is less than,
     *         equal to, or greater than the second
     */
    @Override
    public int compareTo(MPEGEmphasis emphasis) {
        int emp = emphasis.getEmphasis();
        if (this.emphasis < emp) {
            return -1;
        }
        else if (this.emphasis > emp) {
            return 1;
        }
        return 0;
    }
}
