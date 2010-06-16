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

    public String type;
    public int num;

    private static int print = 0;

    public TiffType(String type, int num) {
        this.type = type;
        this.num = num;
    }
    @ReportableProperty(order=1, value = "Tag Type.")
    public String getType() {
        return type;
    }
    @ReportableProperty(order=2, value = "Type Number.")
    public int getNum() {
        return num;
    }

    public static TiffType getType(int typeNum, Properties props){
        if (types == null) {
            /* Initialize the tags from the properties. */
            types = new TreeSet<TiffType>();
            if (props != null) {
                Enumeration<?> e = props.propertyNames();
                while (e.hasMoreElements()){
                    String type = (String) e.nextElement();
                    int num = Integer.parseInt(props.getProperty(type));
                    
                    TiffType ttype = new TiffType(type, num);
                    types.add(ttype);
                    if (print == 0)
                        System.out.println("tiff type ordinal =" + ttype.getNum()+ " Type = " + ttype.getType()   );
                    
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
    
    public Set<TiffType> getTypes() {
        return types;
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
