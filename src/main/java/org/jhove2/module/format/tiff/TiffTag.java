/**
 * JHOVE2 - Next-generation architecture for format-aware characterization
 * <p>
 * Copyright (c) 2010 by The Regents of the University of California. All rights reserved.
 * </p>
 * <p>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * </p>
 * <ul>
 * <li>Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.</li>
 * <li>Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.</li>
 * <li>Neither the name of the University of California/California Digital
 * Library, Ithaka Harbors/Portico, or Stanford University, nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.</li>
 * </ul>
 * <p>
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
 * </p>
 */
package org.jhove2.module.format.tiff;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;

import com.sleepycat.persist.model.Persistent;

/**
 * Tiff Tags. Tags are initialized from a properties file in the
 * form:
 * 
 * <code>Number:Type|Count|Default Value|Version</code>
 * 
 * for the tag number, type, cardinality, default value and TIFF version
 * separated by the | character.
 * Default Value and Version are not required however to indicate version
 * an empty default value || must be defined.
 * 
 * Example: 320:ColorMap|Short|3*2^BPS||5
 * 
 * @author mstrong
 */
@Persistent
public class TiffTag implements Comparable<TiffTag> {

    /** Singleton TIFF Tag. */
    protected static TreeSet<TiffTag> tags;

    /** the tag that identifies this field */
    protected int tag;

    /** descriptive name of the tag  */
    protected String name;

    /** the field type  */
    protected List<String> type;
    /**
     * the number of values, count of the indicated Type 
     */
    protected String cardinality;

    /** default value for this tag */
    protected String defaultValue;

    /** presence of a tag indicates TIFF version number.  
     *  A tag is assumed to be version 4
     */  
    protected int version = 4;


    /**
     *    Instantiate a new <code>TiffTag</code>.
     *    
     *          TiffTag tag field
     * @param tag
     *          descriptive name of tag
     * @param name
     *          String array of types
     * @param type
     *          count of the type
     * @param cardinality
     *          default value for this tag
     * @param defaultValue
     *          TIFF version for this tag
     * @param version
     */
    public TiffTag(int tag, String name, String[] type, String cardinality, String defaultValue, int version) {
        this.tag = tag;
        this.name = name;
        this.type = Arrays.asList(type);
        this.cardinality = cardinality;
        this.defaultValue = defaultValue;
        this.version = version;
    }

    @SuppressWarnings("unused")
	private TiffTag(){}

    /**
     * 
     * given a number representing a tag, return the TiffTag object associated to it
     * 
     * @param tagValue - number with defines the tag field
     * @return TiffTag
     */
    public static TiffTag getTag(int tagValue) throws JHOVE2Exception {
        TiffTag tifftag = null;
        if (tags != null){
            /* find tag which matches tagValue */
            Iterator<TiffTag> iter = tags.iterator();
            while (iter.hasNext()){
                TiffTag tag = iter.next();
                if (tagValue == tag.getTag()){
                    tifftag = tag;
                    break;
                }
            }
        }
        else {
            throw new JHOVE2Exception ("TiffTags not initialized");
        }
        return tifftag;


    }

    /**
     * Initialize the set of Tiff Types from the properties
     * 
     * @param Properties - Properties retreived from Java Properties file
     * @return SortedSet<TiffTag> - the sorted set of TIFF tag definitions
     * @throws JHOVE2Exception
     */
    protected static TreeSet<TiffTag> getTiffTags(JHOVE2 jhove2) throws JHOVE2Exception {        
        if (tags == null) {
            TiffTag tiffTag = null;
            tags = new TreeSet<TiffTag>();
            Properties props = jhove2.getConfigInfo().getProperties("TiffTags");
            if (props != null) {
                Enumeration<?> e = props.propertyNames();
                while (e.hasMoreElements()){

                    tiffTag = null;
                    String key = (String) e.nextElement();
                    String name = null;
                    String[] type;
                    String cardinality = null;
                    String defaultValue = null;
                    int version = 4;

                    String value = props.getProperty(key);
                    String[] values = value.split("\\|");
                    // tag|Name|Type[,Type,...]|Cardinality|Default
                    int tag = Integer.parseInt(key);
                    name = values[0];
                    type = values[1].toUpperCase().split(",");

                    /* retrieve cardinality/count/length 
                     * count field is null if the string value it contains is not parseable to int. 
                     */
                    if (values.length >= 3) {
                        if (isParsableToInt(values[2]))
                            cardinality = values[2];               
                    }
                    /* retrieve default value */
                    if (values.length >= 4) {
                        defaultValue = values[3];
                    }

                    /* retrieve version */
                    if (values.length >= 5) {
                        if (isParsableToInt(values[4])) {
                        version = Integer.parseInt(values[4]);
                        }
                    }
                    tiffTag = new TiffTag(tag, name, type, cardinality, defaultValue, version);
                    tags.add(tiffTag);
                }
            }   
        }
        return tags;
    }

    /**
     * performs test to check if string is an integer
     * 
     * @param i
     * @return
     */
    private static boolean isParsableToInt(String i)
    {
        try {
            Integer.parseInt(i);
            return true;
        }
        catch(NumberFormatException e) {
            return false;
        }
    }

    /**
     * 
     * @return int
     */
    public int getTag() {
        return tag;
    }

    /**
     *  A tag can have more than one type
     * 
     *  List<String> array
     * @return
     */
    public List<String> getType() {
        return type;
    }

    /**
     * 
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @return String
     */
    public String getCardinality() {
        return cardinality;
    }

    /**
     * presence of a tag indicates TIFF version number.  A tag is assumed to 
     * be version 4
     *  
     * @return int
     */
    public int getVersion() {
        return version;
    }

    /**
     * 
     * @return String
     */
    public String getDefValue() {
        return defaultValue;
    }

    /**
     * return the Sorted Set of the Tiff Tags
     * 
     * @return Set<TiffTag>
     */
    public Set<TiffTag> getTags() {
        return tags;
    }

    /**
     * Compare TiffTag.
     * 
     * @param TiffTag
     *            the tiff tag number to be compared
     * @return -1, 0, or 1 if this tag is less than, equal to, or greater
     *         than the second
     */

    @Override
    public int compareTo(TiffTag o) {
        int ret = 0;
        int value = o.getTag();
        if (this.tag < value) {
            ret = -1;
        } else if (this.tag > value) {
            ret = 1;
        }

        return ret;
    }


    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + tag;
        return result;
    }


    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TiffTag other = (TiffTag) obj;
        if (tag != other.tag)
            return false;
        return true;
    }

}
