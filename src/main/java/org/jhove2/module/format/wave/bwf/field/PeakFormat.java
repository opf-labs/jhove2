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

/** Broadcast Wave Format (BWF) peak envelope chunk format.
 * 
 * @author slabrams
 */
public class PeakFormat
    implements Comparable<PeakFormat>
{
    /** Singleton Peak format formats. */
    protected static Set<PeakFormat> formats;

    /** Peak format format. */
    protected long format;

    /** Peak format description. */
    protected String description;

    /**
     * Instantiate a new <code>PeakFormat</code> object.
     * 
     * @param format
     *            Peak format
     * @param description
     *            Peak format description
     */
    public PeakFormat(int format, String description) {
        this.format      = format;
        this.description = description;
    }
    
    /** Initialize the formats.
     * @param jhove2 JHOVE2 framework
     * @throws JHOVE2Exception 
     */
    protected static synchronized void init(JHOVE2 jhove2)
        throws JHOVE2Exception
    {
        if (formats == null) {
            /* Initialize the peak formats from a Java resource bundle. */
            formats = new TreeSet<PeakFormat>();
            Properties props = jhove2.getConfigInfo().getProperties("PeakFormats");
            if (props != null) {
                Set<String> set = props.stringPropertyNames();
                Iterator<String> iter = set.iterator();
                while (iter.hasNext()) {
                    String fmt  = iter.next();
                    String des = props.getProperty(fmt);
                    PeakFormat f =
                        new PeakFormat(Integer.valueOf(fmt), des);
                    formats.add(f);
                }
            }
        }
    }

    /**
     * Get the description for a format. 
     * @param format   Peak format
     * @param jhove2 JHOVE2 framework
     * @return Format Peak format description, or null if the format is not defined
     * @throws JHOVE2Exception
     */
    public static synchronized PeakFormat getPeakFormat(long format, JHOVE2 jhove2)
        throws JHOVE2Exception
    {
        init(jhove2);
        PeakFormat fmt = null;
        Iterator<PeakFormat> iter = formats.iterator();
        while (iter.hasNext()) {
            PeakFormat f = iter.next();
            if (f.getFormat() == format) {
                fmt = f;
                break;
            }
        }
        return fmt;
    }

    /**
     * Get the peak formats.
     * @param jhove2 JHOVE2 framework
     * @return Peak formats
     * @throws JHOVE2Exception 
     */
    public static Set<PeakFormat> getFormats(JHOVE2 jhove2)
        throws JHOVE2Exception
    {
        init(jhove2);
        return formats;
    }

    /**
     * Get the peak format.
     * @return peak format 
     */
    public long getFormat() {
        return this.format;
    }

    /**
     * Get the format description.
     * @return Format description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Convert the peak format to a Java string in the form:
     * "format: description".
     * @return Java string representation of the description
     */
    public String toString() {
        return this.getFormat() + ": " + this.getDescription();
    }

    /**
     * Compare peak format format.
     * @param format
     *             Peak format to be compared
     * @return -1, 0, or 1 if this peak format format is less than,
     *         equal to, or greater than the second
     */
    @Override
    public int compareTo(PeakFormat format) {
        long fmt = format.getFormat();
        if (this.format < fmt) {
            return -1;
        }
        else if (this.format > fmt) {
            return 1;
        }
        return 0;
    }
}
