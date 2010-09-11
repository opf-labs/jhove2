/**
 * JHOVE2 - Next-generation architecture for flag-aware characterization
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

/** Broadcast Wave Flag (BWF) MPEG-1 flag.
 * 
 * @author slabrams
 */
public class MPEGFlag
    implements Comparable<MPEGFlag>
{
    /** Singleton MPEG flag flags. */
    protected static Set<MPEGFlag> flags;

    /** MPEG flag flag. */
    protected int flag;

    /** MPEG description. */
    protected String description;

    /**
     * Instantiate a new <code>MPEGFlag</code> object.
     * 
     * @param flag
     *            MPEG flag
     * @param description
     *            MPEG flag description
     */
    public MPEGFlag(int flag, String description) {
        this.flag   = flag;
        this.description = description;
    }
    
    /** Initialize the flags.
     * @param jhove2 JHOVE2 framework
     * @throws JHOVE2Exception 
     */
    protected static synchronized void init(JHOVE2 jhove2)
        throws JHOVE2Exception
    {
        if (flags == null) {
            /* Initialize the Flag flags from a Java resource bundle. */
            flags = new TreeSet<MPEGFlag>();
            Properties props = jhove2.getConfigInfo().getProperties("MPEGFlags");
            if (props != null) {
                Set<String> set = props.stringPropertyNames();
                Iterator<String> iter = set.iterator();
                while (iter.hasNext()) {
                    String flg  = iter.next();
                    String des = props.getProperty(flg);
                    MPEGFlag f =
                        new MPEGFlag(Integer.valueOf(flg, 16), des);
                    flags.add(f);
                }
            }
        }
    }

    /**
     * Get the description for a flag. 
     * @param flag   MPEG flag
     * @param jhove2 JHOVE2 framework
     * @return Flag MPEG flag description, or null if the flag is not defined
     * @throws JHOVE2Exception
     */
    public static synchronized MPEGFlag getMPEGFlag(int flag, JHOVE2 jhove2)
        throws JHOVE2Exception
    {
        init(jhove2);
        MPEGFlag flg = null;
        Iterator<MPEGFlag> iter = flags.iterator();
        while (iter.hasNext()) {
            MPEGFlag f = iter.next();
            if (f.getFlag() == flag) {
                flg = f;
                break;
            }
        }
        return flg;
    }

    /**
     * Get the MPEG flags.
     * @param jhove2 JHOVE2 framework
     * @return MPEG flags
     * @throws JHOVE2Exception 
     */
    public static Set<MPEGFlag> getFlags(JHOVE2 jhove2)
        throws JHOVE2Exception
    {
        init(jhove2);
        return flags;
    }

    /**
     * Get the MPEG flag.
     * @return MPEG flag 
     */
    public int getFlag() {
        return this.flag;
    }

    /**
     * Get the flag description.
     * @return Flag description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Convert the MPEG flag to a Java string in the form:
     * "flag: description".
     * @return Java string representation of the description
     */
    public String toString() {
        return this.getFlag() + ": " + this.getDescription();
    }

    /**
     * Compare MPEG flag flag.
     * @param flag
     *             MPEG flag to be compared
     * @return -1, 0, or 1 if this MPEG flag flag is less than,
     *         equal to, or greater than the second
     */
    @Override
    public int compareTo(MPEGFlag flag) {
        int flg = flag.getFlag();
        if (this.flag < flg) {
            return -1;
        }
        else if (this.flag > flg) {
            return 1;
        }
        return 0;
    }
}
