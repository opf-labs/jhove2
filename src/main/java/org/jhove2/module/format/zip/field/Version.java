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

package org.jhove2.module.format.zip.field;

import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;

/** Zip local file header version needed to extract.
 * 
 * @author slabrams
 */
public class Version
    implements Comparable<Version>
{
    /** Singleton Zip local file header versions. */
    protected static Set<Version> versions;

    /** Zip local file header version feature. */
    protected String feature;

    /** Zip local file header version. */
    protected short version;

    /**
     * Instantiate a new <code>Version</code> object.
     * 
     * @param version
     *            Zip local file header version
     * @param feature
     *            Zip local file header version feature
     */
    public Version(short version, String feature) {
        this.version = version;
        this.feature = feature;
    }

    /**
     * Get the Zip local file header version. 
     * @param version Zip local file header version
     * @param jhove2    JHOVE2 framework
     * @return Zip local file header version, or null
     * @throws JHOVE2Exception
     */
    public static synchronized Version getVersion(short version, JHOVE2 jhove2)
            throws JHOVE2Exception {
        if (versions == null) {
            /* Initialize the ZIp local file header versions from a Java resource bundle. */
            versions = new TreeSet<Version>();
            Properties props = jhove2.getConfigInfo().getProperties("Versions");
            if (props != null) {
                Set<String> set = props.stringPropertyNames();
                Iterator<String> iter = set.iterator();
                while (iter.hasNext()) {
                    String ver = iter.next();
                    String fea = props.getProperty(ver);
                    Version v = new Version(Short.valueOf(ver), fea);
                    versions.add(v);
                }
            }
        }
        Version ver = null;
        Iterator<Version> iter = versions.iterator();
        while (iter.hasNext()) {
            Version v = iter.next();
            if (v.getVersion() == version) {
                ver = v;
                break;
            }
        }
        return ver;
    }

    /**
     * Get the Zip local file header versions.
     * @return Zip local file header versions
     */
    public static Set<Version> getversions() {
        return versions;
    }

    /**
     * Get the Zip local file header version.
     * @return Zip local file header version
     */
    public short getVersion() {
        return this.version;
    }

    /**
     * Get the Zip local file header version feature.
     * @return Zip local file header version feature
     */
    public String getFeature() {
        return this.feature;
    }

    /**
     * Convert the Zip local file header version to a Java string in the form:
     * "version: feature".
     * @return Java string representation of the Zip local file header version
     */
    public String toString() {
        return this.getVersion() + ": " + this.getFeature();
    }

    /**
     * Compare Zip local file header version.
     * @param version
     *            Zip local file header version to be compared
     * @return -1, 0, or 1 if this ZIp local file header version is less than,
     *         equal to, or greater than the second
     */
    @Override
    public int compareTo(Version version) {
        short ver = version.getVersion();
        
        if (this.version < ver) {
            return -1;
        }
        else if (this.version > ver) {
            return 1;
        }
        return 0;
    }
}
