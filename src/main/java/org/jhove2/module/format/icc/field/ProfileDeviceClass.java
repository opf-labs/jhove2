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

/**
 * @author slabrams
 *
 */
public class ProfileDeviceClass
    implements Comparable<ProfileDeviceClass>
{
    /** Singleton profile/device classes. */
    protected static Set<ProfileDeviceClass> classes;

    /** Profile class. */
    protected String profileClass;

    /** Profile signature. */
    protected String signature;

    /**
     * Instantiate a new <code>ProfileDeviceClass</code> object.
     * 
     * @param signature
     *            Profile signature
     * @param profileClass
     *            Profile/device class
     */
    public ProfileDeviceClass(String signature, String profileClass) {
        this.signature    = signature;
        this.profileClass = profileClass;
    }

    /**
     * Get the profile/device class for a signature. 
     * @param signature profile/device class signature
     * @param jhove2    JHOVE2 framework
     * @return profile/device class, or null if the signature is not a CMM signature
     * @throws JHOVE2Exception
     */
    public static synchronized ProfileDeviceClass getProfileDeviceClass(String signature, JHOVE2 jhove2)
            throws JHOVE2Exception {
        if (classes == null) {
            /* Initialize the profile/device classes from a Java resource bundle. */
            classes = new TreeSet<ProfileDeviceClass>();
            Properties props = jhove2.getConfigInfo().getProperties("ProfileClasses");
            if (props != null) {
                Set<String> set = props.stringPropertyNames();
                Iterator<String> iter = set.iterator();
                while (iter.hasNext()) {
                    String sig = iter.next();
                    String cls = props.getProperty(sig);
                    ProfileDeviceClass pClass = new ProfileDeviceClass(sig, cls);
                    classes.add(pClass);
                }
            }
        }
        ProfileDeviceClass profileClass = null;
        Iterator<ProfileDeviceClass> iter = classes.iterator();
        while (iter.hasNext()) {
            ProfileDeviceClass pClass = iter.next();
            if (pClass.getSignature().equals(signature)) {
                profileClass = pClass;
                break;
            }
        }
        return profileClass;
    }

    /**
     * Get the profile/device classes.
     * 
     * @return Profile/device classes
     */
    public static Set<ProfileDeviceClass> getProfileDeviceClasses() {
        return classes;
    }

    /**
     * Get the profile/device class.
     * 
     * @return Profile/device class
     */
    public String getProfileClass() {
        return this.profileClass;
    }

    /**
     * Get the profile/device class signature.
     * 
     * @return Profile/device class signature
     */
    public String getSignature() {
        return this.signature;
    }

    /**
     * Convert the profile/device class to a Java string in the form:
     * "signature: class".
     * 
     * @return Java string representation of the profile/device class
     */
    public String toString() {
        return this.getSignature() + ": " + this.getProfileClass();
    }

    /**
     * Compare profile/device class.
     * 
     * @param cmm
     *            Profile/device class to be compared
     * @return -1, 0, or 1 if this profile/device class is less than,
     *         equal to, or greater than the second
     */
    @Override
    public int compareTo(ProfileDeviceClass pClass) {
        return this.signature.compareTo(pClass.getSignature());
    }
}
