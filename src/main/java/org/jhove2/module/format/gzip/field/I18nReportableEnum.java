/**
 * JHOVE2 - Next-generation architecture for format-aware characterization
 *
 * Copyright (c) 2009 by The Regents of the University of California,
 * Ithaka Harbors, Inc., and The Board of Trustees of the Leland Stanford
 * Junior University.
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

package org.jhove2.module.format.gzip.field;


import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.annotation.ReportableProperty.PropertyType;
import org.jhove2.core.reportable.AbstractReportable;

import com.sleepycat.persist.model.Persistent;


/**
 * An enum-like suport class that supports injection of descriptions
 * (from resource bundles).
 * <p>
 * To ease implementation of format validation, this class does not
 * limit the value set. Instances could be created for unknown values,
 * they are then marked as {@link #isValid invalid}.</p>
 * <p>
 * Subclasses shall invoke the {@link #initValues} method
 * upon descriptions injection to get a {#link Map} of valid
 * values.  Any value not present in the map should be considered
 * invalid.</p>
 */
@Persistent
abstract public class I18nReportableEnum extends AbstractReportable
                                    implements Comparable<I18nReportableEnum>
{
    /** The integer value for the enum instance. */
    protected int value;
    /** The value description. */
    protected String label;
    /** Whether the value is valid. */
    protected boolean valid;

    /** Zero argument constructor. */
    protected I18nReportableEnum() {
        super();
    }

    /**
     * Creates an enum value.
     * @param  value   the integer value for the enum instance.
     * @param  label   the value description or <code>null</code>.
     * @param  valid   whether the value is valid.
     */
    protected I18nReportableEnum(int value, String label, boolean valid) {
        super();
        this.value = value;
        this.label = label;
        this.valid = valid;
    }

    /**
     * Returns the property raw value.
     *
     * @return the property raw value, as an integer.
     */
    @ReportableProperty(order = 1, value = "Value", type = PropertyType.Raw)
    public int getValue() {
        return this.value;
    }

    /**
     * Returns the property description.
     *
     * @return the property description as a internationalizable string
     *         loaded from a resource bundle.
     */
    @ReportableProperty(order = 2, value = "Description",
                                   type  = PropertyType.Descriptive)
    public String getDescription() {
        return this.label;
    }

    /** {@inheritDoc} */
    public boolean isValid() {
        return this.valid;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return this.value;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object o) {
        return ((this.getClass().equals(o.getClass())) &&
                (((I18nReportableEnum)o).value == this.value));
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder(32);
        b.append(this.value);
        if (this.valid) {
            b.append(" - ").append((this.label != null)? this.label: "");
        }
        return b.toString();
    }

    /** {@inheritDoc} */
    @Override
    public int compareTo(I18nReportableEnum o) {
        return this.value - o.value;
    }

    /**
     * Initializes the list of known (i.e. valid) values. In case the
     * resource bundle should not be trusted, the subclasses should
     * provide the list of allowed values. Values not present in this
     * array are ignored.
     * @param  clazz    the concrete subclass.
     * @param  props    the resource bundle to load descriptions from.
     * @param  values   the list of valid values or <code>null</code>
     *                  if the resource bundle can be trusted.
     *
     * @return a {@link Map} associating integer keys to subclass
     *         instances for valid values.
     * @throws IllegalArgumentException if <code>clazz</code> or
     *         <code>props</code> is <code>null</code>.
     * @throws RuntimeException to wrap any error raised by subclass
     *         constructor.
     */
    protected static <T extends I18nReportableEnum>
                Map<Integer,T> initValues(Class<T> clazz,
                                          Properties props, int[] values) {
        if (clazz == null) {
            throw new IllegalArgumentException("clazz");
        }
        if (props == null) {
            throw new IllegalArgumentException("props");
        }

        try {
            Constructor<T> c = clazz.getDeclaredConstructor(
                                    int.class, String.class, boolean.class);

            Map<Integer,T> m = new ConcurrentHashMap<Integer,T>();
            if ((values != null) && (values.length != 0)) {
                // List of valid values is enforced.
                for (int i : values) {
                    Integer key = Integer.valueOf(i);
                    String desc = props.getProperty(String.valueOf(i));
                    m.put(key, c.newInstance(key, desc, Boolean.TRUE));
                }
            }
            else {
                // All values found in the resource bundle are deemed valid.
                for (String s : props.stringPropertyNames()) {
                    try {
                        Integer key = Integer.valueOf(s);
                        String desc = props.getProperty(s);
                        m.put(key, c.newInstance(key, desc, Boolean.TRUE));
                    }
                    catch (Exception e) {
                        // Property name is not an integer => ignore.
                    }
                }
            }
            return m;
        }
        catch (RuntimeException e) {
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
