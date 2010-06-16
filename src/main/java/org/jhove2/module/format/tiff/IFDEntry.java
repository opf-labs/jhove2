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
extends AbstractReportable
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

    @ReportableProperty(order = 3, value="Entry type.")
    public TiffType getType(){
        return tiffType;
    }

    @ReportableProperty(order=5, value = "Entry value/offset.")
    public long getValueOffset() {
        return valueOffset;
    }

    public int getVersion() {
        return version;
    }

    protected static Properties tiffTagProps;

    protected static Properties tiffTypeProps;


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

            TiffTag tifftag = TiffTag.getTag(tag, getTiffTags(jhove2.getConfigInfo()));
            this.tiffType = TiffType.getType(type, getTiffType(jhove2.getConfigInfo()));

            /* type values of 6-12 were defined in TIFF 6.0 */
            if (this.tiffType.num > TiffType.getType("SBYTE").num){
                version = 6;
            }

            this.count = input.readUnsignedInt();
            this.valueOffset = input.readUnsignedInt();
            if ((this.valueOffset & 1) != 0){
                /* TODO: Report an 'offset not word aligned' error */
                Object[]messageArgs = new Object[]{0, input.getPosition(), this.valueOffset};
                this.ByteOffsetNotWordAlignedMessage = (new Message(Severity.ERROR,
                        Context.OBJECT,
                        "org.jhove2.module.format.tiff.IFDEntry.ValueByteOffsetNotWordAlignedMessage",
                        messageArgs, jhove2.getConfigInfo()));               
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }        
        catch (JHOVE2Exception e) {
            e.printStackTrace();
        }        
    }

    protected void validateTag(JHOVE2 jhove2, int tagNum, String type, String count) throws JHOVE2Exception {
        TiffTag tag = TiffTag.getTag(tagNum, getTiffTags(jhove2.getConfigInfo()));
        if (!(tag.getType().equalsIgnoreCase(type))) {
            Object[]messageArgs = new Object[]{tagNum, type, this.valueOffset};
            this.TypeMismatchMessage = (new Message(Severity.ERROR,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.IFDEntry.TypeMismatchMessage",
                    messageArgs, jhove2.getConfigInfo()));               
        }
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
     * @return the Tiff Tag Properties
     */
    public static Properties getTiffTags(ConfigInfo config)  throws JHOVE2Exception {
        if (tiffTagProps==null){
            tiffTagProps = config.getProperties("TiffTag");
        }
        return tiffTagProps;
    }

    /**
     * @return the Tiff Type Properties
     */
    public static Properties getTiffType(ConfigInfo config)  throws JHOVE2Exception {
        if (tiffTypeProps==null){
            tiffTypeProps = config.getProperties("TiffType");
        }
        return tiffTypeProps;
    }


}
