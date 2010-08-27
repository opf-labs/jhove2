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

/** ICC standard observer, as defined in ICC.1:2004-10, Table 40.
 * 
 * @author slabrams
 */
public class StandardObserver
    implements Comparable<StandardObserver>
{
    /** Singleton standard observer observer. */
    protected static Set<StandardObserver> observers;

    /** Standard observer value. */
    protected long value;

    /** Standard observer. */
    protected String observer;

    /**
     * Instantiate a new <code>StandardObserver</code> object.
     * @param value Standard observer value
     * @param observer
     *            Standard observer
     */
    public StandardObserver(long value, String observer) {
        this.value    = value;
        this.observer = observer;
    }

    /**
     * Get the observer for a standard observer value.
     * 
     * @param value  Standard observer value
     * @param jhove2 JHOVE2 framework
     * @return Standard observer, or null if the value is not defined
     * @throws JHOVE2Exception
     */
    public static synchronized StandardObserver getStandardObserver(long value, JHOVE2 jhove2)
            throws JHOVE2Exception {
        if (observers == null) {
            /* Initialize the standard observers from a Java resource bundle. */
            observers = new TreeSet<StandardObserver>();
            Properties props = jhove2.getConfigInfo().getProperties("StandardObservers");
            if (props != null) {
                Set<String> set = props.stringPropertyNames();
                Iterator<String> iter = set.iterator();
                while (iter.hasNext()) {
                    String val  = iter.next();
                    String obs = props.getProperty(val);
                    StandardObserver observer = new StandardObserver(Integer.valueOf(val), obs);
                    observers.add(observer);
                }
            }
        }
        StandardObserver observer = null;
        Iterator<StandardObserver> iter = observers.iterator();
        while (iter.hasNext()) {
            StandardObserver obs = iter.next();
            if (obs.getValue() == value) {
                observer = obs;
                break;
            }
        }

        return observer;
    }

    /**
     * Get the standard observer.
     * @return Standard observer
     */
    public String getObserver() {
        return this.observer;
    }
 
    /**
     * Get the standard observer value.
     * @return Standard observer value
     */
    public long getValue() {
        return this.value;
    }

    /**
     * Convert the standard observer to a Java string in the form:
     * "value: observer".
     * @return Java string representation of the standard observer
     */
    public String toString() {
        return this.value + ": " + this.observer;
    }

    /**
     * Compare standard observer.
     * @param type
     *            Standard observer
     * @return -1, 0, or 1 if this value is less than,
     *         equal to, or greater than the second
     */
    @Override
    public int compareTo(StandardObserver type) {
        long value = type.getValue();
        if (this.value < value) {
            return -1;
        }
        else if (this.value > value) {
            return  1;
        }
        return 0;
    }
}
