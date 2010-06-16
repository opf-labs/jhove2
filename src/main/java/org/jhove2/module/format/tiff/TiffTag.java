package org.jhove2.module.format.tiff;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.jhove2.module.format.utf8.unicode.C0Control;


public class TiffTag implements Comparable<TiffTag> {

    /** Singleton TIFF Tag. */
    protected static SortedSet<TiffTag> tags;

    private static int print = 0;
    // Name Type Cardinality Default
    public int tag;
    public String name;
    public String type;
    public String cardinality;
    public String defaultValue;

    public TiffTag(int tag, String name, String type, String cardinality) {
        this.tag = tag;
        this.name = name;
        this.type = type;
        this.cardinality = cardinality;
    }

    public int getTag() {
        return tag;
    }
    public String getType() {
        return type;
    }
    public String getName() {
        return name;
    }
    public String getCardinality() {
        return cardinality;
    }

    public static TiffTag getTag(int tagValue, Properties props){
        TiffTag tiffTag = null;
        if (tags == null) {
            /* Initialize the tags from the properties. */
            tags = new TreeSet<TiffTag>();
            if (props != null) {
                Enumeration<?> e = props.propertyNames();
                while (e.hasMoreElements()){

                    String key = (String) e.nextElement();
                    String name = null;
                    String type;
                    String cardinality = null;

                    String[] values = props.getProperty(key).split("\t");
                    // tag has Name Type Cardinality Default
                    int tag = Integer.parseInt(key);
                    name = values[0];
                    type = values[1];
                    if (values.length >= 3) {
                        cardinality = values[2];
                    }
                    tiffTag = new TiffTag(tag, name, type, cardinality);
                    tags.add(tiffTag);
                    if (print == 0)
                        System.out.println(
                                "tiff tag ordinal=" + tiffTag.getTag() + " Name = " + tiffTag.getName() + " type = " + tiffTag.getType() + " card = " + tiffTag.getCardinality());

                }
            }   
        }
        
        // tags are populated from properties, find tag which matches tagValue
        
        print++;
        TiffTag tifftag = null;
        Iterator<TiffTag> iter = tags.iterator();
        while (iter.hasNext()) {
            TiffTag tag = iter.next();
            if (tagValue == tag.getTag()) {
                tiffTag = tag;
                break;
            }
        }
        return tiffTag;


    }

    public Set<TiffTag> getTags() {
        return tags;
    }

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
