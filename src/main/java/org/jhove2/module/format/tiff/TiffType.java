package org.jhove2.module.format.tiff;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.module.format.utf8.unicode.C0Control;


public class TiffType implements Comparable<TiffType> {

    /** Singleton TIFF Type. */
    protected static SortedSet<TiffType> types;

    protected String type;
    protected int num;
    protected int size;
    
    private static int print = 0;

    public TiffType(String type, int num, int size) {
        this.type = type;
        this.num = num;
        this.size = size;
    }
    @ReportableProperty(order=1, value = "Tag Type.")
    public String getType() {
        return type;
    }
    @ReportableProperty(order=2, value = "Type Number.")
    public int getNum() {
        return num;
    }
    @ReportableProperty(order=3, value = "Type Field Size.")
    public int getSize() {
        return size;
    }

    public static TiffType getType(int typeNum, Properties props){
        if (types == null) {
            /* Initialize the tags from the properties. */
            types = new TreeSet<TiffType>();
            if (props != null) {
                Enumeration<?> e = props.propertyNames();
                while (e.hasMoreElements()){
                    String type = (String) e.nextElement();
                    String [] values = props.getProperty(type).split(" ");
                    int num = Integer.parseInt(values[0]);
                    int size = Integer.parseInt(values[1]);
                    TiffType ttype = new TiffType(type, num, size);
                    types.add(ttype);
                    if (print == 0)
                        System.out.println("tiff type ordinal =" + ttype.getNum()+ " Type = " + ttype.getType() + " Size = " + ttype.getSize() );
                    
                }
            } 
        }
        print++;
        TiffType ttype = null;
        Iterator<TiffType> iter = types.iterator();
        while (iter.hasNext()) {
            TiffType type = iter.next();
            if (typeNum == type.getNum()) {
                ttype = type;
                break;
            }
        }
        return ttype;
    }

    public static TiffType getType(String typeString){
        TiffType tiffType = null;
        Iterator<TiffType> iter = types.iterator();
        while (iter.hasNext()) {
            TiffType ttype = iter.next();
            if (typeString.equalsIgnoreCase(ttype.getType())) {
                tiffType = ttype;
                break;
            }
        }
        return tiffType;
    }
    
    public static TiffType getType(int type){
        TiffType tiffType = null;
        Iterator<TiffType> iter = types.iterator();
        while (iter.hasNext()) {
            TiffType ttype = iter.next();
            if (type == ttype.getNum()) {
                tiffType = ttype;
                break;
            }
        }
        return tiffType;
    }
    public Set<TiffType> getTypes() {
        return types;
    }

    /**
     * Get the type name.
     * 
     * @return Type name
     */
    public String getName() {
        return this.type;
    }


    /**
     * Convert the tiff type to a Java string.
     * 
     * @return Java string representation of the type
     */
    public String toString() {
        return this.getName();
    }

    @Override
    public int compareTo(TiffType o) {
        int ret = 0;
        int value = o.getNum();
        if (this.num < value) {
            ret = -1;
        } else if (this.num > value) {
            ret = 1;
        }

        return ret;
    }

}
