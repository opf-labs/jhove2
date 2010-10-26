/**
 * JHOVE2 - Next-generation architecture for listat-aware characterization
 *
 * Copyright (c) 2009 by The Regents of the University of California
 * All rights reserved.
 *
 * Redistribution and use in source and binary lists, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * o Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 *
 * o Redistributions in binary list must reproduce the above copyright notice,
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

package org.jhove2.module.format.riff.field;

import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;

/** LIST chunk list type.
 * 
 * @author slabrams
 */
public class ListType
implements Comparable<ListType>
{
    /** Singleton LIST chunk list types. */
    protected static Set<ListType> types;

    /** LIST chunk list type. */
    protected String type;

    /** LIST chunk list type description. */
    protected String description;

    /**
     * Instantiate a new <code>ListType</code> object.
     * 
     * @param type
     *            LIST chunk list type
     * @param LIST chunk list type description
     *            LIST chunk list type description
     */
    public ListType(String type, String description) {
        this.type        = type;
        this.description = description;
    }

    /**
     * Get the description for a type. 
     * @param type   LIST chunk list type
     * @param jhove2 JHOVE2 framework
     * @return LIST chunk list type, or null if the type is not defined
     * @throws JHOVE2Exception
     */
    public static synchronized ListType getListType(String type, JHOVE2 jhove2)
            throws JHOVE2Exception {
        if (types == null) {
            /* Initialize the LIST chunk list types from a Java resource bundle. */
            types = new TreeSet<ListType>();
            Properties props = jhove2.getConfigInfo().getProperties("ListTypes");
            if (props != null) {
                Set<String> set = props.stringPropertyNames();
                Iterator<String> iter = set.iterator();
                while (iter.hasNext()) {
                    String typ = iter.next();
                    String des = props.getProperty(typ);
                    ListType listType = new ListType(typ, des);
                    types.add(listType);
                }
            }
        }
        ListType listType = null;
        Iterator<ListType> iter = types.iterator();
        while (iter.hasNext()) {
            ListType typ = iter.next();
            if (typ.getType().equals(type)) {
                listType = typ;
                break;
            }
        }
        return listType;
    }

    /**
     * Get the LIST chunk list types.
     * @return LIST chunk list types
     */
    public static Set<ListType> getListTypes() {
        return types;
    }

    /**
     * Get the LIST chunk list type.
     * @return LIST chunk list type 
     */
    public String getType() {
        return this.type;
    }

    /**
     * Get the LIST chunk list type description.
     * @return LIST chunk list type description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Convert the LIST chunk list type description to a Java string in the list:
     * "type: description".
     * @return Java string representation of the description
     */
    public String toString() {
        return this.getType() + ": " + this.getDescription();
    }

    /**
     * Compare LIST chunk list type.
     * @param description
     *            LIST chunk list type description to be compared
     * @return -1, 0, or 1 if this LIST chunk list type is less than,
     *         equal to, or greater than the second
     */
    @Override
    public int compareTo(ListType type) {
        return this.type.compareTo(type.getType());
    }
}
