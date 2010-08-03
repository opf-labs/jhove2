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

    protected Ascii asciiValue;

    protected AsciiArray asciiArrayValue;

    protected Byte byteValue;

    protected ByteArray byteArrayValue;

    protected LongObject longValue;

    protected double[] doubleArrayValue;

    protected Integer intValue;

    protected FloatObject floatValue;

    protected Double doubleValue;

    protected LongArray longArrayValue;

    protected Rational rationalValue;

    protected RationalArray rationalArrayValue;

    protected Rational sRationalValue;
  
    protected RationalArray sRationalArrayValue;

    protected Short shortValue;

    protected IntegerArray shortArrayValue;

    protected Short sshortValue;

    protected ShortArray sshortArrayValue;


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
        if (count == 1 ) {
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
                longValue = new LongObject(input.readUnsignedInt());
            else if (type == TiffType.Type.RATIONAL.num()) {
                long num = input.readUnsignedInt();
                long denom = input.readUnsignedInt();
                rationalValue = new Rational(num, denom);
            }
            else if (type == TiffType.Type.SBYTE.num()) 
                byteValue = new Byte(input.readSignedByte());
            else if (type == TiffType.Type.SSHORT.num())
                shortValue = new Short(input.readSignedShort());
            else if (type == TiffType.Type.SLONG.num())
                longValue = new LongObject(input.readSignedInt());
            else if (type == TiffType.Type.SRATIONAL.num()) {
                long num = input.readSignedInt();
                long demon = input.readSignedInt();
                rationalValue = new Rational(num, demon);
            }
        }
        else {
            /* need to read into an array */
        }
    }

    @ReportableProperty(order = 1, value="Tag value")
    public Object getValue(){
        if (type == 4)
            return longValue;
        else if (type == 1)
            return byteValue;
        else
            return null;
    }


}
