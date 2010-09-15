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

/** ICC rendering intent as defined in ICC.1:2004-10, Table 19.
 * 
 * @author slabrams
 */
public class RenderingIntent
    implements Comparable<RenderingIntent>
{
    /** Singleton rendering intents. */
    protected static Set<RenderingIntent> intents;

    /** Rendering intent. */
    protected String intent;

    /** Rendering intent value. */
    protected int value;

    /**
     * Instantiate a new <code>RenderingIntent</code> object.
     * 
     * @param value
     *            Rendering intent value
     * @param Rintent
     *            Rendering intent
     */
    public RenderingIntent(int value, String intent) {
        this.value  = value;
        this.intent = intent;
    }
    
    /** Initialize the rendering intents.
     * @param jhove2 JHOVE2 framework
     * @throws JHOVE2Exception 
     */
    protected static synchronized void init(JHOVE2 jhove2)
        throws JHOVE2Exception
    {
        if (intents == null) {
            /* Initialize the rendering intents from a Java resource bundle. */
            intents = new TreeSet<RenderingIntent>();
            Properties props =
                jhove2.getConfigInfo().getProperties("RenderingIntents");
            if (props != null) {
                Set<String> set = props.stringPropertyNames();
                Iterator<String> iter = set.iterator();
                while (iter.hasNext()) {
                    String val = iter.next();
                    String ren = props.getProperty(val);
                    RenderingIntent intent =
                        new RenderingIntent(Integer.valueOf(val), ren);
                    intents.add(intent);
                }
            }
        }
    }

    /**
     * Get the rendering intent for a value. 
     * @param value  Rendering intent value
     * @param jhove2 JHOVE2 framework
     * @return Rendering intent, or null if the value is not a rendering intent value
     * @throws JHOVE2Exception
     */
    public static synchronized RenderingIntent getRenderingIntent(int value,
                                                                  JHOVE2 jhove2)
        throws JHOVE2Exception
    {
        init(jhove2);
        RenderingIntent renderingIntent = null;
        Iterator<RenderingIntent> iter = intents.iterator();
        while (iter.hasNext()) {
            RenderingIntent intent = iter.next();
            if (intent.getValue() == value) {
                renderingIntent = intent;
                break;
            }
        }
        return renderingIntent;
    }

    /**
     * Get the rendering intents.
     * @param jhove2 JHOVE2 framework
     * @return rendering intents
     * @throws JHOVE2Exception 
     */
    public static Set<RenderingIntent> getRenderingIntents(JHOVE2 jhove2)
        throws JHOVE2Exception
    {
        init(jhove2);
        return intents;
    }

    /**
     * Get the rendering intent.
     * @return rendering intent
     */
    public String getRenderingIntent() {
        return this.intent;
    }

    /**
     * Get the rendering intent value.
     * @return rendering intent value
     */
    public int getValue() {
        return this.value;
    }

    /**
     * Convert the rendering intent to a Java string in the form:
     * "value: intent".
     * @return Java string representation of the rendering intent
     */
    public String toString() {
        return this.getValue() + ": " + this.getRenderingIntent();
    }

    /**
     * Compare rendering intent.
     * @param intent
     *            Rendering intent to be compared
     * @return -1, 0, or 1 if this rendering intent is less than,
     *         equal to, or greater than the second
     */
    @Override
    public int compareTo(RenderingIntent intent) {
        int in = intent.getValue();
        if (this.value < in) {
            return -1;
        }
        else if (this.value > in) {
            return  1;
        }
        return 0;
    }
}
