/**
 * JHOVE2 - Next-generation architecture for flag-aware characterization
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

/** Broadcast Wave Format (BWF) peak envelop chunk points per peak.
 * 
 * @author slabrams
 */
public class PeakPoint
    implements Comparable<PeakPoint>
{
    /** Singleton peak points. */
    protected static Set<PeakPoint> points;

    /** Peak point. */
    protected long point;

    /** Peak description. */
    protected String description;

    /**
     * Instantiate a new <code>PeakPoint</code> object.
     * 
     * @param point
     *            Peak point
     * @param description
     *            Peak point description
     */
    public PeakPoint(long point, String description) {
        this.point       = point;
        this.description = description;
    }
    
    /** Initialize the points.
     * @param jhove2 JHOVE2 framework
     * @throws JHOVE2Exception 
     */
    protected static synchronized void init(JHOVE2 jhove2)
        throws JHOVE2Exception
    {
        if (points == null) {
            /* Initialize the peak points from a Java resource bundle. */
            points = new TreeSet<PeakPoint>();
            Properties props = jhove2.getConfigInfo().getProperties("PeakPoints");
            if (props != null) {
                Set<String> set = props.stringPropertyNames();
                Iterator<String> iter = set.iterator();
                while (iter.hasNext()) {
                    String pt  = iter.next();
                    String des = props.getProperty(pt);
                    PeakPoint p =
                        new PeakPoint(Integer.valueOf(pt), des);
                    points.add(p);
                }
            }
        }
    }

    /**
     * Get the description for a point. 
     * @param point   Peak point
     * @param jhove2 JHOVE2 framework
     * @return Point Peak point description, or null if the point is not defined
     * @throws JHOVE2Exception
     */
    public static synchronized PeakPoint getPeakPoint(long point, JHOVE2 jhove2)
        throws JHOVE2Exception
    {
        init(jhove2);
        PeakPoint pt = null;
        Iterator<PeakPoint> iter = points.iterator();
        while (iter.hasNext()) {
            PeakPoint p = iter.next();
            if (p.getPoint() == point) {
                pt = p;
                break;
            }
        }
        return pt;
    }

    /**
     * Get the peak points.
     * @param jhove2 JHOVE2 framework
     * @return Peak points
     * @throws JHOVE2Exception 
     */
    public static Set<PeakPoint> getPoints(JHOVE2 jhove2)
        throws JHOVE2Exception
    {
        init(jhove2);
        return points;
    }

    /**
     * Get the peak point.
     * @return peak point 
     */
    public long getPoint() {
        return this.point;
    }

    /**
     * Get the point description.
     * @return Point description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Convert the peak point to a Java string in the form:
     * "point: description".
     * @return Java string representation of the description
     */
    public String toString() {
        return this.getPoint() + ": " + this.getDescription();
    }

    /**
     * Compare peak point point.
     * @param point
     *             Peak point to be compared
     * @return -1, 0, or 1 if this peak point point is less than,
     *         equal to, or greater than the second
     */
    @Override
    public int compareTo(PeakPoint point) {
        long pt = point.getPoint();
        if (this.point < pt) {
            return -1;
        }
        else if (this.point > pt) {
            return 1;
        }
        return 0;
    }
}
