/**
 * 
 */
package org.jhove2.module.format.tiff;

import java.io.IOException;
import java.util.Properties;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.config.ConfigInfo;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.Message;
import org.jhove2.core.Message.Context;
import org.jhove2.core.Message.Severity;
import org.jhove2.core.io.Input;
import org.jhove2.core.reportable.AbstractReportable;

/**
 * @author mstrong
 *
 */
public class IFDEntry 
extends IFD
implements Comparable {

    /*   public enum Type {
        BYTE(1), ASCII(2), SHORT(3), LONG(4), RATIONAL(5), SBYTE(6),
        UNDEFINED(7), SSHORT(8), SLONG(9), SRATIONAL(10), FLOAT(11),
        DOUBLE(12), IFD(13);

        private int fieldType;

        Type (int type){
            this.fieldType = type;
        }
    }
     */

    /** The number of values, Count of the indicated Type */
    protected long count;

    /** Name of the TIFF tag */
    protected String name;

    /** value of the previous tag */
    private static int prevTag = 0;

    /** the tag that identifies the field */
    protected int tag;

    /** the field type */
    protected TiffType tiffType;

    /** Contains the value iff the value is 4 or less bytes.  Otherwise is offset to value */
    protected long valueOffset;

    private Object ByteOffsetNotWordAlignedMessage;

    private Message TypeMismatchMessage;

    private Message TagSortOrderErrorMessage;

    private Message ValueOffsetReferenceLocationFileMessage;

    private Message UnknownTypeMessage;

    private Message UnknownTagMessage;

    /** TIFF Version - some field types define the TIFF version */
    protected static int version = 4;

    @ReportableProperty (order = 4, value="Entry number of values.")
    public long getCount(){
        return count;
    }

    @ReportableProperty (order = 2, value = "Entry tag name.")
    public String getName(){
        return name;
    }

    @ReportableProperty(order = 1, value="Entry tag.")
    public int getTag(){
        return tag;
    }

    @ReportableProperty(order = 3, value="Tag type.")
    public TiffType getType(){
        return this.tiffType;
    }

    @ReportableProperty(order=5, value = "Entry value/offset.")
    public long getValueOffset() {
        return valueOffset;
    }

    public int getVersion() {
        return version;
    }


    public IFDEntry() {
    }

    public void parse(JHOVE2 jhove2, Input input) {
        try {
            tag = input.readUnsignedShort();
            if (tag > prevTag)
                prevTag = tag;
            else {
                Object[]messageArgs = new Object[]{tag, input.getPosition()};
                this.TagSortOrderErrorMessage = (new Message(Severity.ERROR,
                        Context.OBJECT,
                        "org.jhove2.module.format.tiff.IFDEntry.TagSortOrderErrorMessage",
                        messageArgs, jhove2.getConfigInfo()));
            }               
            int type = input.readUnsignedShort();

            TiffType.getTiffTypes(getTiffType(jhove2.getConfigInfo()));
            this.tiffType = TiffType.getType(type);

            TiffTag tifftag = TiffTag.getTag(tag);
            //, getTiffTags(jhove2.getConfigInfo()));

            /* Skip over tags with unknown type. */
            if (type < TiffType.getType("BYTE").num|| type > TiffType.getType("IFD").num) {
                Object[]messageArgs = new Object[]{type, this.valueOffset };
                this.UnknownTypeMessage = (new Message(Severity.ERROR,
                        Context.OBJECT,
                        "org.jhove2.module.format.tiff.IFDEntry.UnknownTypeMessage",
                        messageArgs, jhove2.getConfigInfo()));               
            }
            else {
                /* type values of 6-12 were defined in TIFF 6.0 */
                if (type > TiffType.getType("SBYTE").num){
                    version = 6;
                }

                this.count = input.readUnsignedInt();

                /* keep track of where we are in the file */
                long valueOffset = input.getPosition(); 
                long value = input.readUnsignedInt();

                /* the value is the offset to the value */
                if (calcValueSize(type, count) > 4) {
                    long size = input.getSize();

                    /* test that the value offset is within the file */
                    if (value > size) {
                        Object[]messageArgs = new Object[]{0, input.getPosition(), this.valueOffset};
                        this.ValueOffsetReferenceLocationFileMessage = (new Message(Severity.ERROR,
                                Context.OBJECT,
                                "org.jhove2.module.format.tiff.IFDEntry.ValueOffsetReferenceLocationFileMessage",
                                messageArgs, jhove2.getConfigInfo()));               

                    }

                    /* test off set is word aligned */
                    if ((value & 1) != 0){
                        Object[]messageArgs = new Object[]{0, input.getPosition(), this.valueOffset};
                        this.ByteOffsetNotWordAlignedMessage = (new Message(Severity.ERROR,
                                Context.OBJECT,
                                "org.jhove2.module.format.tiff.IFDEntry.ValueByteOffsetNotWordAlignedMessage",
                                messageArgs, jhove2.getConfigInfo()));               
                    }

                }
                else {
                    /* the value is the actual value */
                    this.valueOffset = value;
                }
                validateTag(jhove2, tag, type, count, valueOffset);
            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }        
        catch (JHOVE2Exception e) {
            e.printStackTrace();
        }        
    }

    protected void validateTag(JHOVE2 jhove2, int tagNum, int type, long count, long valueOffset) throws JHOVE2Exception {
        TiffTag tag = TiffTag.getTag(tagNum);

        /* validate that the type read in matches possible types for that tag */
        if (tag != null) {
            String[] tagType = tag.getType();
            boolean match = false;
            for (String typeEntry:tagType){
                if (tiffType.getType(typeEntry).num == type) 
                    match = true;
            }
            if (!match) {
                Object[]messageArgs = new Object[]{tagNum, type, this.valueOffset};
                this.TypeMismatchMessage = (new Message(Severity.ERROR,
                        Context.OBJECT,
                        "org.jhove2.module.format.tiff.IFDEntry.TypeMismatchMessage",
                        messageArgs, jhove2.getConfigInfo()));               
            }
        }
        else {
            Object[]messageArgs = new Object[]{tagNum, type, this.valueOffset};
            this.UnknownTagMessage = (new Message(Severity.ERROR,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.IFDEntry.UnknownTagMessage",
                    messageArgs, jhove2.getConfigInfo()));               
            
        }
        /* TODO for each tag do some validation */
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



}
