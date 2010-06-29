package org.jhove2.module.format.tiff;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.jhove2.core.JHOVE2Exception;

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
public class TiffTag implements Comparable<TiffTag> {

    /** Singleton TIFF Tag. */
    protected static SortedSet<TiffTag> tags;

    /** the tag that identifies this field */
    protected int tag;

    /** descriptive name of the tag  */
    protected String name;

    /** the field type  */
    protected String[] type;
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
        this.type = type;
        this.cardinality = cardinality;
        this.defaultValue = defaultValue;
        this.version = version;
    }


    /**
     * 
     * given a number representing a tag, return the TiffTag object associated to it
     * 
     * @param tagValue - number with defines the tag field
     * @param props - Properties file which stores the tiff tag definitions
     * @return TiffTag
     */
    public static TiffTag getTag(int tagValue, Properties props) throws JHOVE2Exception {
        TiffTag tifftag = null;

        if (tags == null){
            tags = getTiffTags(props);
        }
        /* find tag which matches tagValue */
        Iterator<TiffTag> iter = tags.iterator();
        while (iter.hasNext()){
            TiffTag tag = iter.next();
            if (tagValue == tag.getTag()){
                tifftag = tag;
                break;
            }
        }
        return tifftag;


    }

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
        return tifftag;


    }

    /**
     * Initialize the set of Tiff Types from the properties
     * 
     * @param Properties - Properties retreived from Java Properties file
     * @return SortedSet<TiffTag> - the sorted set of TIFF tag definitions
     * @throws JHOVE2Exception
     */
    protected static SortedSet<TiffTag> getTiffTags(Properties props) throws JHOVE2Exception {
        {
            if (tags == null) {
                TiffTag tiffTag = null;
                tags = new TreeSet<TiffTag>();
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
                        type = values[1].split(",");

                        /* retrieve cardinality/count/length */
                        if (values.length >= 3) {
                            cardinality = values[2];
                        }
                        /* retrieve default value */
                        if (values.length >= 4) {
                            defaultValue = values[3];
                        }

                        /* retrieve version */
                        if (values.length >= 5) {
                            version = Integer.parseInt(values[4]);
                        }
                        tiffTag = new TiffTag(tag, name, type, cardinality, defaultValue, version);
                        tags.add(tiffTag);
                    }
                }   
            }
        }
        return tags;
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
     *  String[] array
     * @return
     */
    public String[] getType() {
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

}
