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

package org.jhove2.module.format.riff.field;

import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;

/** RIFF chunk form type.
 * 
 * @author slabrams
 */
public class FormType
    implements Comparable<FormType>
{
    /** Singleton RIFF chunk form types. */
    protected static Set<FormType> types;

    /** RIFF chunk form type. */
    protected String type;

    /** RIFF chunk form type description. */
    protected String description;

    /**
     * Instantiate a new <code>FormType</code> object.
     * 
     * @param type
     *            RIFF chunk form type
     * @param RIFF chunk form type description
     *            RIFF chunk form type description
     */
    public FormType(String type, String description) {
        this.type        = type;
        this.description = description;
    }

    /**
     * Get the description for a type. 
     * @param type   RIFF chunk form type
     * @param jhove2 JHOVE2 framework
     * @return RIFF chunk form type, or null if the type is not defined
     * @throws JHOVE2Exception
     */
    public static synchronized FormType getFormType(String type, JHOVE2 jhove2)
            throws JHOVE2Exception {
        if (types == null) {
            /* Initialize the RIFF chunk form types from a Java resource bundle. */
            types = new TreeSet<FormType>();
            Properties props = jhove2.getConfigInfo().getProperties("FormTypes");
            if (props != null) {
                Set<String> set = props.stringPropertyNames();
                Iterator<String> iter = set.iterator();
                while (iter.hasNext()) {
                    String typ = iter.next();
                    String des = props.getProperty(typ);
                    FormType formType = new FormType(typ, des);
                    types.add(formType);
                }
            }
        }
        FormType formType = null;
        Iterator<FormType> iter = types.iterator();
        while (iter.hasNext()) {
            FormType typ = iter.next();
            if (typ.getType().equals(type)) {
                formType = typ;
                break;
            }
        }
        return formType;
    }

    /**
     * Get the RIFF chunk form types.
     * @return RIFF chunk form types
     */
    public static Set<FormType> getFormTypes() {
        return types;
    }

    /**
     * Get the RIFF chunk form type.
     * @return RIFF chunk form type 
     */
    public String getType() {
        return this.type;
    }

    /**
     * Get the RIFF chunk form type description.
     * @return RIFF chunk form type description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Convert the RIFF chunk form type description to a Java string in the form:
     * "type: description".
     * @return Java string representation of the description
     */
    public String toString() {
        return this.getType() + ": " + this.getDescription();
    }

    /**
     * Compare RIFF chunk form type.
     * @param description
     *            RIFF chunk form type description to be compared
     * @return -1, 0, or 1 if this RIFF chunk form type is less than,
     *         equal to, or greater than the second
     */
    @Override
    public int compareTo(FormType type) {
        return this.type.compareTo(type.getType());
    }
}
