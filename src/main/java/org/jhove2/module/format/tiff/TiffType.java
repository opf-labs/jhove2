package org.jhove2.module.format.tiff;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.JHOVE2Exception;


public class TiffType implements Comparable<TiffType> {

    /** Singleton Set of TIFF Types. */
    protected static SortedSet<TiffType> types;

    /** the type of the tiff tag */
    protected String typeName;

    /** the number assigned to this field type */
    protected int num;

    /** the size of the type */
    protected int size;

    /**
     * Instantiate a new <code>TiffType</code>
     *      
     * @param type - descriptive name of the type     
     * @param num - the number assigned to the tiff type
     * @param size - the size of the type
     */
    public TiffType(String type, int num, int size) {
        this.typeName = type;
        this.num = num;
        this.size = size;
    }

    /**
     * Returns a TiffType given the name associated with this type
     * @param typeString - name of the type (ex: "BYTE")
     * @return TiffType
     */
    public static TiffType getType(String typeString){
        TiffType tiffType = null;
        Iterator<TiffType> iter = types.iterator();
        while (iter.hasNext()) {
            TiffType ttype = iter.next();
            if (typeString.equalsIgnoreCase(ttype.getTypeName())) {
                tiffType = ttype;
                break;
            }
        }
        return tiffType;
    }

    /**
     * Return a type given a number assigned to the type
     * 
     * @param number - the number associated with this type (ex: 1=Byte)
     * @return TiffType
     */
    public static TiffType getType(int number)
    {
        TiffType tiffType = null;
        Iterator<TiffType> iter = types.iterator();
        while (iter.hasNext()) {
            TiffType ttype = iter.next();
            if (number == ttype.getNum()) {
                tiffType = ttype;
                break;
            }
        }
        return tiffType;
    }

    /**
     * Given a type number, return the TiffType object associated with it
     * 
     * @param typeNum - number which defines the type
     * @param props - Properties object which stores the tiff type definitions
     * @return TiffType
     * @throws JHOVE2Exception 
     */
    public static TiffType getType(int typeNum, Properties props) 
        throws JHOVE2Exception
    {
        if (types == null) {
            types = getTiffTypes(props);
        } 
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

    /**
     * Initialize the set of Tiff Types from the properties
     *  
     * @param Properties - Properties retrieved from Java Properties file
     * @return SortedSet<TiffType> - the sorted set of TIFF type definitions
     * @throws JHOVE2Exception

     * @param props
     */
    protected static SortedSet<TiffType> getTiffTypes(Properties props) 
        throws JHOVE2Exception 
    {
        if (types == null) {
            types = new TreeSet<TiffType>();
            if (props != null) {
                Enumeration<?> e = props.propertyNames();
                while (e.hasMoreElements()){
                    String num = (String) e.nextElement();
                    String [] values = props.getProperty(num).split(" ");
                    int number = Integer.parseInt(num);
                    String type = values[0];
                    int size = Integer.parseInt(values[1]);
                    TiffType ttype = new TiffType(type, number, size);
                    types.add(ttype);
                }
            } 
        }
        return types;
    }



    @ReportableProperty(order=1, value = "Tag Type.")
    public String getTypeName() {
        return typeName;
    }
    @ReportableProperty(order=2, value = "Type Number.")
    public int getNum() {
        return num;
    }
    @ReportableProperty(order=3, value = "Type Field Size.")
    public int getSize() {
        return size;
    }
    
    /** 
     * Return the sorted set of the TIFF types
     * @return SortedSet<TiffType>
     */
    public SortedSet<TiffType> getTypes() {
        return types;
    }

    /**
     * Convert the tiff type to a Java string.
     * 
     * @return Java string representation of the type
     */
    public String toString() {
        return this.typeName;
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
