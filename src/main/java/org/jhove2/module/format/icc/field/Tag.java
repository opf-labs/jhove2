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

/** ICC tags as defined in ICC.1:2004-10, \ua007 9.
 * 
 * @author slabrams
 */
public class Tag
implements Comparable<Tag>
{
    /** Singleton tags. */
    protected static Set<Tag> tags;

    /** Tag name. */
    protected String name;
    
    /** Tag signature. */
    protected String signature;
    
    /** Tag vendor. */
    protected String vendor;

    /**
     * Instantiate a new <code>Tag</code> object.
     * @param vendor    Tag vendor
     * @param signature
     *            Tag signature
     * @param name
     *            Tag
     */
    public Tag(String vendor, String signature, String name) {
        this.vendor    = vendor;
        this.signature = signature;
        this.name      = name;
    }

    /**
     * Get the tag for a signature. 
     * @param signature  Tag signature
     * @param jhove2 JHOVE2 framework
     * @return Tag, or null if the signature is not a tag signature
     * @throws JHOVE2Exception
     */
    public static synchronized Tag getTag(String signature,
                                          JHOVE2 jhove2)
        throws JHOVE2Exception
    {
        if (tags == null) {
            /* Initialize the tags from a Java resource bundle. */
            tags = new TreeSet<Tag>();
            Properties props =
                jhove2.getConfigInfo().getProperties("Tags");
            if (props != null) {
                Set<String> set = props.stringPropertyNames();
                Iterator<String> iter = set.iterator();
                while (iter.hasNext()) {
                    String sig = iter.next();
                    String ven = props.getProperty(sig);
                    String nam = null;
                    int in = ven.indexOf('|');
                    if (in > 0) {
                        nam = ven.substring(in+1);
                        ven = ven.substring(0, in);
                    }
                    Tag tag = new Tag(ven, sig, nam);
                    tags.add(tag);
                }
            }
        }
        Tag tag = null;
        Iterator<Tag> iter = tags.iterator();
        while (iter.hasNext()) {
            Tag tg = iter.next();
            if (signature.equals(tg.getSignature())) {
                tag = tg;
                break;
            }
        }
        return tag;
    }

    /**
     * Get the tags.
     * @return tags
     */
    public static Set<Tag> getTags() {
        return tags;
    }

    /**
     * Get the tag name.
     * @return Tag name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get the tag signature.
     * @return Tag signature
     */
    public String getSignature() {
        return this.signature;
    }
    
    /** Get the tag vendor.
     * @return Tag vendor
     */
    public String getVendor() {
        return this.vendor;
    }

    /**
     * Convert the tag to a Java string in the form:
     * "signature: name".
     * @return Java string representation of the tag
     */
    public String toString() {
        return this.getSignature() + ": " + this.getName();
    }

    /**
     * Compare tag.
     * @param signature
     *            Tag to be compared
     * @return -1, 0, or 1 if this tag is less than,
     *         equal to, or greater than the second
     */
    @Override
    public int compareTo(Tag tag) {
        return this.signature.compareTo(tag.getSignature());
    }
}
