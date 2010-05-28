/**
 * 
 */
package org.jhove2.module.format.tiff;

import java.io.IOException;

import org.jhove2.annotation.ReportableProperty;
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
    extends AbstractReportable {

    public enum Type {
        BYTE(1), ASCII(2), SHORT(3), LONG(4), RATIONAL(5), SBYTE(6),
        UNDEFINED(7), SSHORT(8), SLONG(9), SRATIONAL(10), FLOAT(11),
        DOUBLE(12), IFD(13);

        private int fieldType;

        Type (int type){
            this.fieldType = type;
        }
    }
	
    /** The number of values, Count of the indicated Type */
    protected long count;

    /** Name of the TIFF tag */
	protected String name;

    /** the tag that identifies the field */
	protected int tag;
	
	/** the field type */
    protected Type type;

    /** Contains the value iff the value is 4 or less bytes.  Otherwise is offset to value */
	protected long valueOffset;

    private Object ByteOffsetNotWordAlignedMessage;
 	
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
    public Type getType(){
        return type;
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
            this.tag = input.readUnsignedShort();
            this.type = Type.values()[input.readUnsignedShort()];
            
            /* type values of 6-12 were defined in TIFF 6.0 */
            if (this.type.ordinal() >= Type.IFD.ordinal()) {
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

}
