/**
 * 
 */
package org.jhove2.module.format.tiff;

import java.util.Arrays;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.Message;
import org.jhove2.core.Message.Context;
import org.jhove2.core.Message.Severity;
//import org.jhove2.core.io.Input;
import org.jhove2.core.reportable.AbstractReportable;

/**
 * @author mstrong
 *
 */
public class IFDEntry 
extends AbstractReportable
implements Comparable {

    /** The number of values, Count of the indicated Type */
    protected long count;

    /** Name of the TIFF tag */
    protected String name;

    /** the tag that identifies the field */
    protected int tag;

    /** the field type */
    protected TiffType tiffType;

    /** Contains the value iff the value is 4 or less bytes.  Otherwise is offset to value */
    protected long valueOffset;

    /** TIFF Version - some field types define the TIFF version */
    protected int version = 4;

    /** the value if the value is more than 4 btyes */
    protected TiffValue value;

    /** type mismatch message */
    private Message TypeMismatchMessage;

    /* unknown tag message */
    private Message UnknownTagMessage;

    private Message InvalidCountMessage;

    private Message InvalidCountValueMessage;

    @ReportableProperty(order=5, value = "Entry value/offset.")
    public long getValueOffset() {
        return valueOffset;
    }

    public int getVersion() {
        return version;
    }



    public IFDEntry(int tag, int type, int count, long valueOffset) {
        this.tag = tag;
        this.tiffType = TiffType.getType(type);
        this.count = count;
        this.valueOffset = valueOffset;
    }


    /**
     * 
     * validates the IFD Entry by checking: 
     * 
     *  1) if tag is known and defined
     *  2) that the type matches expected type values for that tag definition
     *  3) that count expected matches the count read in
     *  4) performing any other validations for a particular tag
     *  
     * @param jhove2
     * @throws JHOVE2Exception
     */
    protected void validateEntry(JHOVE2 jhove2) throws JHOVE2Exception {

        /* retrieve the definition for the tag read in */
        TiffTag tag = TiffTag.getTag(this.tag);

        /* validate that the type and count read in matches what is expected for this tag */
        if (tag != null) {
            this.name = tag.getName();
            checkType(jhove2, this.tiffType, tag.getType());
            checkCount(jhove2, this.count, tag.getCardinality());
        }
        else {
            Object[]messageArgs = new Object[]{this.tag, valueOffset};
            this.UnknownTagMessage = (new Message(Severity.WARNING,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.IFDEntry.UnknownTagMessage",
                    messageArgs, jhove2.getConfigInfo()));               
        }
    }


    /**
     * checks that the count value read in matches the expected count value
     * 
     * @param jhove2
     * @param count
     * @param expectedCount
     * @throws JHOVE2Exception
     */
    private void checkCount(JHOVE2 jhove2, long count, String expectedCount ) throws JHOVE2Exception {
        if (expectedCount != null) {
            int expected = Integer.parseInt(expectedCount);
            if (count < expected) {
                Object[]messageArgs = new Object[]{this.tag, count, expectedCount};
                this.InvalidCountValueMessage = (new Message(Severity.WARNING,
                        Context.OBJECT,
                        "org.jhove2.module.format.tiff.IFDEntry.InvalidCountValueMessage",
                        messageArgs, jhove2.getConfigInfo()));               
            }
               
        }
        // TODO Auto-generated method stub
        
    }

    /**
     * compares the type that is read in with the type that is defined for the tag
     * For unsigned integers, readers accept BYTE, SHORT, LONG or IFD types
     * 
     * @param jhove2 - JHOVE2 framework
     * @param expected - the list of expected types defined for this tag
     * @param string - the type read in 
     * @throws JHOVE2Exception
     */
    private void checkType(JHOVE2 jhove2, TiffType type, String[] expected) throws JHOVE2Exception {
               
        int typeNum = type.getNum();
        String typeReadIn = type.getTypeName();
        /* type values of 6-12 were defined in TIFF 6.0 */
        if (typeNum > TiffType.getType("SBYTE").getNum()) {
            version = 6;
        }

        if (typeReadIn.equalsIgnoreCase("BYTE") || typeReadIn.equalsIgnoreCase("SHORT") ||
            typeReadIn.equalsIgnoreCase("LONG") || typeReadIn.equalsIgnoreCase("IFD")) {
            for (String expectedEntry:expected){
                if (expectedEntry.equalsIgnoreCase("BYTE") || expectedEntry.equalsIgnoreCase("SHORT") || 
                    expectedEntry.equalsIgnoreCase("LONG") || expectedEntry.equalsIgnoreCase("IFD"))             
                    return;  // type is valid
            }
        }
        for (String expectedEntry:expected){
            if (!expectedEntry.equalsIgnoreCase(typeReadIn)) {
                Object[]messageArgs = new Object[]{typeReadIn, this.tag, Arrays.toString(expected)};
                this.TypeMismatchMessage = (new Message(Severity.ERROR,
                        Context.OBJECT,
                        "org.jhove2.module.format.tiff.IFDEntry.TypeMismatchMessage",
                        messageArgs, jhove2.getConfigInfo()));               
            }        
        }
    }

    /**
     * The cardinality (number of values) for this TIFF tag  
     * @return long
     */
    @ReportableProperty (order = 4, value="Entry number of values.")
    public long getCount(){
        return count;
    }

    /**
     * The name of the tag entry
     * @return String 
     */
    @ReportableProperty (order = 1, value = "Entry tag name.")
    public String getName(){
        return name;
    }

    /**
     * The TIFF tag number
     * @return int
     */
    @ReportableProperty(order = 2, value="Entry tag.")
    public int getTag(){
        return tag;
    }

    /**
     * The field type of the value for this tag
     * @return TiffType
     */
    @ReportableProperty(order = 3, value="Tag type.")
    public TiffType getType(){
        return this.tiffType;
    }

    /**
     * Get type mismatch message
     * @return the typeMismatchMessage
     */
    @ReportableProperty(order=6, value = "type mismatch message.")
    public Message getTypeMismatchMessage() {
        return TypeMismatchMessage;
    }

    /**
     * Get the unknown tag message
     * @return the unknownTagMessage
     */
    @ReportableProperty(order=7, value = "unknown tag message.")
    public Message getUnknownTagMessage() {
        return UnknownTagMessage;
    }

    @Override
    public int compareTo(Object o) {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * validate the tag
     *  
     */
    public boolean validateTag(int tag) {

        boolean valid = true;
        return valid;
    }

    /**
     * @return the value
     */
    @ReportableProperty(order=8, value = "Tiff Tag Value.")
    public TiffValue getValue() {
        return value;
    }

    /**
     * @param object the value to set
     */
    public void setValue(Object object) {
        this.value = (TiffValue) object;
    }




}
