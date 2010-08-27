/**
 * 
 */
package org.jhove2.module.format.tiff;

import java.io.IOException;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.io.Input;
import org.jhove2.core.reportable.AbstractReportable;

/**
 * @author MStrong
 *
 */
public class TiffValue
extends AbstractReportable {

    int type;

    boolean isArray = false;

    protected Ascii asciiValue;

    protected AsciiArray asciiArrayValue;

    protected Byte byteValue;

    protected SByte sByteValue;

    protected ByteArray byteArrayValue;

    /** 32-bit (4-byte) unsigned integer 
     * @see org.jhove2.module.format.tiff.Long */
    protected Long longValue;

    protected LongArray longArrayValue;

    protected SLong sLongValue;

    protected SLongArray sLongArrayValue;

    protected FloatObject floatValue;

    protected Double doubleValue;

    protected Rational rationalValue;

    protected RationalArray rationalArrayValue;

    protected Rational sRationalValue;

    protected RationalArray sRationalArrayValue;

    protected Short shortValue;

    protected ShortArray shortArrayValue;

    protected SShort sShortValue;

    protected SShortArray sShortArrayValue;

   /** no-arg constructor */
    public TiffValue() {
    }

    /**
     * read in value based on type
     *   
     * @param input
     * @param entry
     * @throws IOException
     */
    public void readValue(Input input, IFDEntry entry) throws IOException {

        this.type = entry.getType().getNum();
        long count = entry.getCount();
        
        if (count <= 1 ) {
            /* store a single value */
            if (type == TiffType.Type.BYTE.num()) 
                byteValue = new Byte(input.readUnsignedByte());
            else if (type == TiffType.Type.ASCII.num()){
                asciiValue = new Ascii();
                asciiValue.setValue(input, count );
            }
            else if (type == TiffType.Type.SHORT.num())
                shortValue = new Short(input.readUnsignedShort());
            else if (type == TiffType.Type.LONG.num())
                longValue = new Long(input.readUnsignedInt());
            else if (type == TiffType.Type.RATIONAL.num()) {
                long num = input.readUnsignedInt();
                long denom = input.readUnsignedInt();
                rationalValue = new Rational(num, denom);
            }
            else if (type == TiffType.Type.SBYTE.num()) 
                sByteValue = new SByte(input.readSignedByte());
            else if (type == TiffType.Type.SSHORT.num())
                sShortValue = new SShort(input.readSignedShort());
            else if (type == TiffType.Type.SLONG.num())
                sLongValue = new SLong(input.readSignedLong());
            else if (type == TiffType.Type.SRATIONAL.num()) {
                long num = input.readSignedInt();
                long demon = input.readSignedInt();
                sRationalValue = new Rational(num, demon);
            }
            /*
             * TODO:  FLOAT & DOUBLE
             */
        }
        else {
            isArray = true;
            /* read into an array */
            if (type == TiffType.Type.BYTE.num()) {
                byteArrayValue = new ByteArray();
                byteArrayValue.setValue(input, count);
            }
            else if (type == TiffType.Type.ASCII.num()){
                asciiArrayValue = new AsciiArray();
                asciiArrayValue.setValue(input, count);
            }
            else if (type == TiffType.Type.SHORT.num()) {
                shortArrayValue = new ShortArray();
                shortArrayValue.setValue(input, count);
            }
            else if (type == TiffType.Type.LONG.num()) {
                longArrayValue = new LongArray();
                longArrayValue.setValue(input, count);
            }
            else if (type == TiffType.Type.RATIONAL.num()) {
                long num = input.readUnsignedInt();
                long denom = input.readUnsignedInt();
                rationalValue = new Rational(num, denom);
            }
            else if (type == TiffType.Type.SBYTE.num()) 
                byteValue = new Byte(input.readSignedByte());
            else if (type == TiffType.Type.SSHORT.num()) {
                sShortArrayValue = new SShortArray();
                sShortArrayValue.setValue(input, count);
            }
            else if (type == TiffType.Type.SLONG.num()) {
                sLongArrayValue = new SLongArray();
                sLongArrayValue.setValue(input, count);
            }
            else if (type == TiffType.Type.SRATIONAL.num()) {
                long num = input.readSignedLong();
                long demon = input.readSignedLong();
                rationalValue = new Rational(num, demon);
            }

        }
    }


    @ReportableProperty(order = 1, value="Tag value")
    public Object getValue(){
        if (isArray) {
            if (type == TiffType.Type.BYTE.num())
                return byteArrayValue;
            else if (type == TiffType.Type.ASCII.num())
                return asciiArrayValue;
            else if (type == TiffType.Type.SHORT.num())
                return shortArrayValue;
            else if (type == TiffType.Type.LONG.num())
                return longArrayValue;
            else if (type == TiffType.Type.RATIONAL.num())
                return rationalArrayValue;
            else if (type == TiffType.Type.SSHORT.num())
                return sShortArrayValue;
            else if (type == TiffType.Type.SLONG.num())
                return longArrayValue;
            else if (type == TiffType.Type.SRATIONAL.num())
                return longArrayValue;
        }
        else  {
            if (type == TiffType.Type.BYTE.num())
                return byteValue;
            else if (type == TiffType.Type.ASCII.num())
                return asciiValue;
            else if (type == TiffType.Type.SHORT.num())
                return shortValue;
            else if (type == TiffType.Type.LONG.num())
                return longValue;
            else if (type == TiffType.Type.RATIONAL.num())
                return longArrayValue;
            else if (type == TiffType.Type.SBYTE.num())
                return longArrayValue;
            else if (type == TiffType.Type.SLONG.num())
                return longArrayValue;
            else if (type == TiffType.Type.SRATIONAL.num())
                return longArrayValue;
        }

        return null;
    }


}
