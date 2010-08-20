package org.jhove2.module.format.tiff;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.SortedSet;
import java.util.TreeSet;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;


/**
 * TiffType
 * 
 * Class to store the defined TIFF Types.  
 * 
 * A properties file holds the definitions
 * which can be added to when new TIFF Types are defined.
 * 
 * @author mstrong
 *
 */
public class TiffType implements Comparable<TiffType> {

    public enum Type {
        BYTE       (1, 1),
        ASCII      (2, 1),
        SHORT      (3, 2),  /*(unsigned 16-bit) type */
        LONG       (4, 4),  /*(unsigned 32-bit) type */
        RATIONAL   (5, 8),  /*(two LONGs) type*/
        SBYTE      (6, 1),  /*(signed 8-bit) type */
        UNDEFINED  (7, 1),  /*(unsigned 8-bit) type */
        SSHORT     (8, 2),  /*(signed 16-bit) type */
        SLONG      (9, 4),  /*(signed 32-bit) type */
        SRATIONAL  (10, 8), /*(two SLONGs) type */
        FLOAT      (11, 2), /*(32-bit IEEE floating point) type */
        DOUBLE     (12, 8), /*(64-bit IEEE floating point) type */
        IFD        (13, 4); /*(LONG) type */


        /**
         *  field type number
         */
        private final int num;  

        /**
         *  size of field
         */
        private final int size; 
        Type(int num, int size) {
            this.num = num;
            this.size = size;
        }
        
        public int num() {
            return num;
        }
        public int size() {
            return size;
        }
    }

    /** Singleton Set of TIFF Types. */
    protected static TreeSet<TiffType> types;

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
     * Initialize the set of Tiff Types from the properties
     *  
     * @param Properties - Properties retrieved from Java Properties file
     * @return SortedSet<TiffType> - the sorted set of TIFF type definitions
     * @throws JHOVE2Exception

     * @param props
     */
    protected static TreeSet<TiffType> getTiffTypes(JHOVE2 jhove2) 
        throws JHOVE2Exception 
    {
        if (types == null) {
            types = new TreeSet<TiffType>();
            Properties props = jhove2.getConfigInfo().getProperties("TiffTypes");
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



    /**
     * the name of the type
     * 
     * @return String
     */
    @ReportableProperty(order=1, value = "Tag Type.")
    public String getTypeName() {
        return typeName;
    }

    /**
     * The integer value which represents this type
     * 
     * @return int
     */
    @ReportableProperty(order=2, value = "Type Number.")
    public int getNum() {
        return num;
    }

    /**
     * given the type name, returns the number
     * 
     * @param type
     * @return int
     */
    public int getNum(String type) {
        return num;
    }

    /**
     * the number of bytes for the type field
     * 
     * @return int
     */
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

    /**
     * compares the tiff types by comparing the tiff type number
     */
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

    /**
     * check equality of the tiff types by comparing the tiff name
     */
    public boolean equals(String value) {
        boolean ret = false;
        if (this.typeName.equalsIgnoreCase(value)) {
            ret = true;
        }
        return ret;
    }

}
