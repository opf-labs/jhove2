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
 * o Neither the category of the University of California/California Digital
 *   Library, Ithaka Harbors/Portico, or Stanford University, nor the categorys of
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

/** Broadcast Wave Format (BWF) MPEG-1 mode.
 * 
 * @author slabrams
 */
public class MPEGMode
    implements Comparable<MPEGMode>
{
    /** Singleton MPEG mode modes. */
    protected static Set<MPEGMode> modes;

    /** MPEG mode mode. */
    protected int mode;

    /** MPEG description. */
    protected String description;

    /**
     * Instantiate a new <code>MPEGMode</code> object.
     * 
     * @param mode
     *            MPEG mode
     * @param description
     *            MPEG mode description
     */
    public MPEGMode(int mode, String description) {
        this.mode   = mode;
        this.description = description;
    }
    
    /** Initialize the modes.
     * @param jhove2 JHOVE2 framework
     * @throws JHOVE2Exception 
     */
    protected static synchronized void init(JHOVE2 jhove2)
        throws JHOVE2Exception
    {
        if (modes == null) {
            /* Initialize the Mode modes from a Java resource bundle. */
            modes = new TreeSet<MPEGMode>();
            Properties props = jhove2.getConfigInfo().getProperties("MPEGModes");
            if (props != null) {
                Set<String> set = props.stringPropertyNames();
                Iterator<String> iter = set.iterator();
                while (iter.hasNext()) {
                    String mod  = iter.next();
                    String des = props.getProperty(mod);
                    MPEGMode m =
                        new MPEGMode(Integer.valueOf(mod, 16), des);
                    modes.add(m);
                }
            }
        }
    }

    /**
     * Get the description for a mode. 
     * @param mode   MPEG mode
     * @param jhove2 JHOVE2 framework
     * @return Mode MPEG mode description, or null if the mode is not defined
     * @throws JHOVE2Exception
     */
    public static synchronized MPEGMode getMPEGMode(int mode, JHOVE2 jhove2)
        throws JHOVE2Exception
    {
        init(jhove2);
        MPEGMode mod = null;
        Iterator<MPEGMode> iter = modes.iterator();
        while (iter.hasNext()) {
            MPEGMode m = iter.next();
            if (m.getMode() == mode) {
                mod = m;
                break;
            }
        }
        return mod;
    }

    /**
     * Get the MPEG modes.
     * @param jhove2 JHOVE2 framework
     * @return MPEG modes
     * @throws JHOVE2Exception 
     */
    public static Set<MPEGMode> getModes(JHOVE2 jhove2)
        throws JHOVE2Exception
    {
        init(jhove2);
        return modes;
    }

    /**
     * Get the MPEG mode.
     * @return MPEG mode 
     */
    public int getMode() {
        return this.mode;
    }

    /**
     * Get the mode description.
     * @return Mode description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Convert the MPEG mode to a Java string in the form:
     * "mode: description".
     * @return Java string representation of the description
     */
    public String toString() {
        return this.getMode() + ": " + this.getDescription();
    }

    /**
     * Compare MPEG mode.
     * @param mode
     *             MPEG mode to be compared
     * @return -1, 0, or 1 if this MPEG mode mode is less than,
     *         equal to, or greater than the second
     */
    @Override
    public int compareTo(MPEGMode mode) {
        int mod = mode.getMode();
        if (this.mode < mod) {
            return -1;
        }
        else if (this.mode > mod) {
            return 1;
        }
        return 0;
    }
}
