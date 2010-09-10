/**
 * JHOVE2 - Next-generation architecture for extension-aware characterization
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

/** Broadcast Wave ModeExtension (BWF) MPEG-1 joint-stereo coding parameter
 * .
 * @author slabrams
 */
public class MPEGModeExtension
    implements Comparable<MPEGModeExtension>
{
    /** Singleton MPEG extension extensions. */
    protected static Set<MPEGModeExtension> extensions;

    /** MPEG mode extension. */
    protected int extension;

    /** MPEG mode extension description. */
    protected String description;

    /**
     * Instantiate a new <code>MPEGModeExtension</code> object.
     * 
     * @param extension
     *            MPEG mode extension
     * @param description
     *            MPEG mode extension description
     */
    public MPEGModeExtension(int extension, String description) {
        this.extension   = extension;
        this.description = description;
    }
    
    /** Initialize the mode extensions.
     * @param jhove2 JHOVE2 framework
     * @throws JHOVE2Exception 
     */
    protected static synchronized void init(JHOVE2 jhove2)
        throws JHOVE2Exception
    {
        if (extensions == null) {
            /* Initialize the ModeExtension extensions from a Java resource bundle. */
            extensions = new TreeSet<MPEGModeExtension>();
            Properties props = jhove2.getConfigInfo().getProperties("MPEGModeExtensions");
            if (props != null) {
                Set<String> set = props.stringPropertyNames();
                Iterator<String> iter = set.iterator();
                while (iter.hasNext()) {
                    String ext  = iter.next();
                    String des = props.getProperty(ext);
                    MPEGModeExtension ex =
                        new MPEGModeExtension(Integer.valueOf(ext, 16), des);
                    extensions.add(ex);
                }
            }
        }
    }

    /**
     * Get the description for a MPEG mode extension. 
     * @param extension MPEG mode extension
     * @param jhove2 JHOVE2 framework
     * @return ModeExtension MPEG mode extension description, or null if the extension is not defined
     * @throws JHOVE2Exception
     */
    public static synchronized MPEGModeExtension getMPEGModeExtension(int extension, JHOVE2 jhove2)
        throws JHOVE2Exception
    {
        init(jhove2);
        MPEGModeExtension ext = null;
        Iterator<MPEGModeExtension> iter = extensions.iterator();
        while (iter.hasNext()) {
            MPEGModeExtension ex = iter.next();
            if (ex.getModeExtension() == extension) {
                ext = ex;
                break;
            }
        }
        return ext;
    }

    /**
     * Get the MPEG mode extensions.
     * @param jhove2 JHOVE2 framework
     * @return MPEG mode extensions
     * @throws JHOVE2Exception 
     */
    public static Set<MPEGModeExtension> getModeExtensions(JHOVE2 jhove2)
        throws JHOVE2Exception
    {
        init(jhove2);
        return extensions;
    }

    /**
     * Get the MPEG mode extension.
     * @return MPEG mode extension 
     */
    public int getModeExtension() {
        return this.extension;
    }

    /**
     * Get the MPEG mode extension description.
     * @return MPEG mode extension description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Convert the MPEG mode extension to a Java string in the form:
     * "extension: description".
     * @return Java string representation of the description
     */
    public String toString() {
        return this.getModeExtension() + ": " + this.getDescription();
    }

    /**
     * Compare MPEG mode extension extension.
     * @param extension
     *             MPEG mode extension to be compared
     * @return -1, 0, or 1 if this MPEG extension extension is less than,
     *         equal to, or greater than the second
     */
    @Override
    public int compareTo(MPEGModeExtension extension) {
        int ext = extension.getModeExtension();
        if (this.extension < ext) {
            return -1;
        }
        else if (this.extension > ext) {
            return 1;
        }
        return 0;
    }
}
