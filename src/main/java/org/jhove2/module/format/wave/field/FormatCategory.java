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
 * o Neither the category of the University of California/California Digital
 *   Library, Ithaka Harbors/Portico, or Stanford University, nor the categorys of
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

package org.jhove2.module.format.wave.field;

import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;

/** WAVE format chunk format category.
 * 
 * @author slabrams
 */
public class FormatCategory
    implements Comparable<FormatCategory>
{
    /** Singleton format chunk form categories. */
    protected static Set<FormatCategory> categories;

    /** Format chunk form format. */
    protected int format;

    /** Format format chunk format category package-qualified class category. */
    protected String category;

    /**
     * Instantiate a new <code>FormatCategory</code> object.
     * 
     * @param format
     *            Format chunk format
     * @param category
     *            Format chunk format category
     */
    public FormatCategory(int format, String category) {
        this.format   = format;
        this.category = category;
    }

    /** Initialize the format categories. 
     * @param jhove2 JHOVE2 framework
     * @throws JHOVE2Exception 
     */
    protected static synchronized void init(JHOVE2 jhove2)
        throws JHOVE2Exception
    {
        if (categories == null) {
            /* Initialize the Format categories from a Java resource bundle. */
            categories = new TreeSet<FormatCategory>();
            Properties props = jhove2.getConfigInfo().getProperties("FormatCategories");
            if (props != null) {
                Set<String> set = props.stringPropertyNames();
                Iterator<String> iter = set.iterator();
                while (iter.hasNext()) {
                    String fmt  = iter.next();
                    String cat = props.getProperty(fmt);
                    FormatCategory category =
                        new FormatCategory(Integer.valueOf(fmt, 16), cat);
                    categories.add(category);
                }
            }
        }
    }
    
    /**
     * Get the class category for a format. 
     * @param format Format chunk format
     * @param jhove2 JHOVE2 framework
     * @return Format format chunk format category, or null if the format is not defined
     * @throws JHOVE2Exception
     */
    public static synchronized FormatCategory getFormatCategory(int format, JHOVE2 jhove2)
        throws JHOVE2Exception
    {
        init(jhove2);
        FormatCategory category = null;
        Iterator<FormatCategory> iter = categories.iterator();
        while (iter.hasNext()) {
            FormatCategory cat = iter.next();
            if (cat.getFormat() == format) {
                category = cat;
                break;
            }
        }
        return category;
    }

    /**
     * Get the Format format chunk format categories.
     * @param jhove2 JHOVE2 framework
     * @return Format format chunk format categories
     * @throws JHOVE2Exception 
     */
    public static Set<FormatCategory> getFormatCategories(JHOVE2 jhove2)
        throws JHOVE2Exception
    {
        init(jhove2);
        return categories;
    }

    /**
     * Get the Format chunk format.
     * @return Format chunk format 
     */
    public int getFormat() {
        return this.format;
    }

    /**
     * Get the Format chunk package-qualified class category.
     * @return Format chunk package-qualified class category
     */
    public String getCategory() {
        return this.category;
    }

    /**
     * Convert the Format format chunk format category category to a Java string in the form:
     * "format: category".
     * @return Java string representation of the description
     */
    public String toString() {
        return this.getFormat() + ": " + this.getCategory();
    }

    /**
     * Compare Format chunk form format.
     * @param chunk
     *            Format format chunk format category to be compared
     * @return -1, 0, or 1 if this Format chunk form format is less than,
     *         equal to, or greater than the second
     */
    @Override
    public int compareTo(FormatCategory format) {
        int frm = format.getFormat();
        if (this.format < frm) {
            return -1;
        }
        else if (this.format > frm) {
            return 1;
        }
        return 0;
    }
}
