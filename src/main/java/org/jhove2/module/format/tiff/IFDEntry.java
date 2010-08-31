/**
 * 
 */
package org.jhove2.module.format.tiff;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.Message;
import org.jhove2.core.Message.Context;
import org.jhove2.core.Message.Severity;
import org.jhove2.core.io.Input;
import org.jhove2.core.reportable.AbstractReportable;
import org.jhove2.module.format.Validator.Validity;
import org.jhove2.module.format.tiff.type.Ascii;
import org.jhove2.module.format.tiff.type.AsciiArray;
import org.jhove2.module.format.tiff.type.Byte;
import org.jhove2.module.format.tiff.type.ByteArray;
import org.jhove2.module.format.tiff.type.Double;
import org.jhove2.module.format.tiff.type.FloatObject;
import org.jhove2.module.format.tiff.type.Long;
import org.jhove2.module.format.tiff.type.LongArray;
import org.jhove2.module.format.tiff.type.Rational;
import org.jhove2.module.format.tiff.type.RationalArray;
import org.jhove2.module.format.tiff.type.SByte;
import org.jhove2.module.format.tiff.type.SByteArray;
import org.jhove2.module.format.tiff.type.SLong;
import org.jhove2.module.format.tiff.type.SLongArray;
import org.jhove2.module.format.tiff.type.SShort;
import org.jhove2.module.format.tiff.type.SShortArray;
import org.jhove2.module.format.tiff.type.Short;
import org.jhove2.module.format.tiff.type.ShortArray;

/**
 * @author mstrong
 *
 */
public class IFDEntry 
extends AbstractReportable
implements Comparable<Object> {

    /** The number of values, Count of the indicated Type */
    protected long count;

    /** Name of the TIFF tag */
    protected String name;

    /** the tag that identifies the field */
    protected int tag;

    /** Contains the offset to the value field */
    protected long offsetOfValue;

    /** the previous tag read */
    public static int prevTag;

    /** the field type */
    protected TiffType tiffType;

    /** Contains the value iff the value is 4 or less bytes.  Otherwise is offset to value */
    protected long valueOffset;

    /** TIFF Version - some field types define the TIFF version */
    protected int version = 4;

    /** Tag Sort order error message */
    private Message TagSortOrderErrorMessage;

    /** Byte offset not word aligned message */
    private Message ByteOffsetNotWordAlignedMessage;

    /** Value Offset reference location invalid message */
    private Message ValueOffsetReferenceLocationFileMessage;

    /** Unknown Type Message */
    private List<Message> unknownTypeMessages;

    /** type mismatch message */
    private Message TypeMismatchMessage;

    /** unknown tag message */
    private Message UnknownTagMessage;

    /** invalid count message */
    private Message InvalidCountMessage;

    /** invalid count value message */
    private Message InvalidCountValueMessage;

    private Validity isValid = Validity.Undetermined;

    /** Possible TIFF values
     * Each TIFF value can be of a different type so it needs to be stored in a
     * separate object
     */
    /** the value if the value is more than 4 btyes */
    boolean isArray = false;

    protected Ascii asciiValue;

    protected AsciiArray asciiArrayValue;

    protected Byte byteValue;

    protected SByte sByteValue;

    protected ByteArray byteArrayValue;

    protected SByteArray sbyteArrayValue;

    /** 32-bit (4-byte) unsigned integer 
     * @see org.jhove2.module.format.tiff.type.Long */
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


    @ReportableProperty(order=5, value = "Entry value/offset.")
    public long getValueOffset() {
        return valueOffset;
    }

    public int getVersion() {
        return version;
    }


    /** no arg constructor */
    public IFDEntry() {
        super();
        this.unknownTypeMessages = new ArrayList<Message>();
    }

    public IFDEntry(int tag, int type, long count, long valueOffset) {
        this.tag = tag;
        this.tiffType = TiffType.getType(type);
        this.count = count;
        this.valueOffset = valueOffset;
    }


    /**
     * parse the IFD Entry 
     * @throws IOException, JHOVE2Exception 
     */
    public void parse(JHOVE2 jhove2, Input input) throws IOException, JHOVE2Exception{
        this.isValid = Validity.True;
        this.tag = input.readUnsignedShort();
        if (tag > prevTag)
            prevTag = tag;
        else {
            this.isValid = Validity.False;
            Object[]messageArgs = new Object[]{tag, input.getPosition()};
            this.TagSortOrderErrorMessage = (new Message(Severity.ERROR,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.IFD.TagSortOrderErrorMessage",
                    messageArgs, jhove2.getConfigInfo()));
        }               

        int type = input.readUnsignedShort();
        this.tiffType = TiffType.getType(type);

        /* Skip over tags with unknown type; those outside of defined range. */
        if (type < TiffType.types.first().getNum() || type > TiffType.types.last().getNum()) {
            Object[]messageArgs = new Object[]{type, input.getPosition() };
            this.unknownTypeMessages.add(new Message(Severity.WARNING,
                    Context.OBJECT,
                    "org.jhove2.module.format.tiff.IFD.UnknownTypeMessage",
                    messageArgs, jhove2.getConfigInfo()));               
        }
        else {
            /* set the version */
            if (type <= TiffType.Type.SBYTE.num() && type <= TiffType.Type.IFD.num()) {
                this.version = 6;
            }

            this.count = (int) input.readUnsignedInt();

            /* store the offset of the value field in the IFD Entry */
            this.valueOffset = input.getPosition(); 
            long value = input.readUnsignedInt();

            if (calcValueSize(type, count) > 4) {
                /* the value read is the offset to the value */
                long size = input.getSize();

                /* test that the value offset is within the file */
                if (value > size) {
                    this.isValid = Validity.False;
                    Object[]messageArgs = new Object[]{tag, value, size};
                    this.ValueOffsetReferenceLocationFileMessage = (new Message(Severity.ERROR,
                            Context.OBJECT,
                            "org.jhove2.module.format.tiff.IFD.ValueOffsetReferenceLocationFileMessage",
                            messageArgs, jhove2.getConfigInfo()));  
                    throw new JHOVE2Exception ("Value offset is not within the file");
                }

                /* test offset is word aligned */
                if ((value & 1) != 0){
                    this.isValid = Validity.False;
                    Object[]messageArgs = new Object[]{0, input.getPosition(), valueOffset};
                    this.ByteOffsetNotWordAlignedMessage = (new Message(Severity.ERROR,
                            Context.OBJECT,
                            "org.jhove2.module.format.tiff.IFD.ValueByteOffsetNotWordAlignedMessage",
                            messageArgs, jhove2.getConfigInfo()));               
                    throw new JHOVE2Exception ("Value byte offset is not word aligned");
                }
                /* update the valueOffset to contain the value (which is the offset to the value) */
                this.valueOffset = value;
            }

            /* reset the input position so that the offset is set up correctly since when you read values the
             * input position gets changed from where you want to be in the IFD 
             * the offset of the Value field + 4 bytes will get you to the next Tag field
             */
            input.setPosition(valueOffset + 4);

        }
        validateEntry(jhove2);
        if (this.isValid != Validity.False)
            this.isValid = Validity.True;
        readValues(input);
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
            this.isValid = Validity.False;
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
                this.isValid = Validity.False;
                Object[]messageArgs = new Object[]{this.tag, count, expectedCount};
                this.InvalidCountValueMessage = (new Message(Severity.WARNING,
                        Context.OBJECT,
                        "org.jhove2.module.format.tiff.IFDEntry.InvalidCountValueMessage",
                        messageArgs, jhove2.getConfigInfo()));               
            }

        }        
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
                this.isValid = Validity.False;
                Object[]messageArgs = new Object[]{typeReadIn, this.tag, Arrays.toString(expected)};
                this.TypeMismatchMessage = (new Message(Severity.ERROR,
                        Context.OBJECT,
                        "org.jhove2.module.format.tiff.IFDEntry.TypeMismatchMessage",
                        messageArgs, jhove2.getConfigInfo()));               
            }        
        }
    }

    /**
     * read in value based on type
     *   
     * @param input
     * @param entry
     * @throws IOException
     */
    public void readValues(Input input) throws IOException {

        input.setPosition(valueOffset);
        int type = this.tiffType.getNum();

        if (count <= 1 ) {
            /* store a single value */
            if (type == TiffType.Type.BYTE.num() || type == TiffType.Type.UNDEFINED.num()) 
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
            if (type == TiffType.Type.BYTE.num() || type == TiffType.Type.UNDEFINED.num()) {
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
            else if (type == TiffType.Type.SBYTE.num()) { 
                sbyteArrayValue = new SByteArray();
                sbyteArrayValue.setValue(input, count);
            }
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


    /**
     * Calculate how many bytes a given number of fields of a given
     * type will require.
     * @param type Field type
     * @param count Field count
     */
    public static long calcValueSize (int type, long count)
    {
        int fieldSize = 0;
        fieldSize = TiffType.getType(type).getSize();
        return  count*fieldSize;
    }

    /**
     * reset the previous tag value 
     */
    public static void resetPrevTag(int value) {
        prevTag = value;
    }
    /**
     * The cardinality (number of values) for this TIFF tag  
     * @return long
     */
    @ReportableProperty (order=4, value="Entry number of values.")
    public long getCount(){
        return count;
    }

    /**
     * The name of the tag entry
     * @return String 
     */
    @ReportableProperty (order=1, value = "Entry tag name.")
    public String getName(){
        return name;
    }

    /**
     * The TIFF tag number
     * @return int
     */
    @ReportableProperty(order=2, value="Entry tag.")
    public int getTag(){
        return tag;
    }

    /**
     * Get the unknown tag message
     * @return the unknownTagMessage
     */
    @ReportableProperty(order=3, value = "unknown tag message.")
    public Message getUnknownTagMessage() {
        return UnknownTagMessage;
    }

    /**
     * The field type of the value for this tag
     * @return TiffType
     */
    @ReportableProperty(order=4, value="Tag type.")
    public TiffType getType(){
        return this.tiffType;
    }


    /**
     * @return the value
     */
    @ReportableProperty(order=5, value = "Tiff Tag Value.")
    public Object getValue(){
        int type = this.tiffType.getNum();
        if (isArray) {
            if (type == TiffType.Type.BYTE.num() || type == TiffType.Type.UNDEFINED.num())
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
                return sLongArrayValue;
            else if (type == TiffType.Type.SRATIONAL.num())
                return sRationalArrayValue;
        }
        else  {
            if (type == TiffType.Type.BYTE.num() || type == TiffType.Type.UNDEFINED.num())
                return byteValue;
            else if (type == TiffType.Type.ASCII.num())
                return asciiValue;
            else if (type == TiffType.Type.SHORT.num())
                return shortValue;
            else if (type == TiffType.Type.LONG.num())
                return longValue;
            else if (type == TiffType.Type.RATIONAL.num())
                return rationalValue;
            else if (type == TiffType.Type.SBYTE.num())
                return sByteValue;
            else if (type == TiffType.Type.SLONG.num())
                return sLongValue;
            else if (type == TiffType.Type.SRATIONAL.num())
                return sRationalValue;
        }

        return null;
    }


    /**
     * @return the isValid
     */
    public Validity isValid() {
        return isValid;
    }

    /**
     * Get the tag sort order error message
     * 
     * @return the tagSortOrderErrorMessage
     */
    @ReportableProperty(order=6, value = "tag sort order error message.")
    public Message getTagSortOrderErrorMessage() {
        return TagSortOrderErrorMessage;
    }

    /**
     * Get the unknown type message
     * 
     * @return the unknownTypeMessage
     */
    @ReportableProperty(order=7, value = "unknown type message.")
    public List<Message> getUnknownTypeMessage() {
        return unknownTypeMessages;
    }
    /**
     * Get byte offset not word aligned message
     * 
     * @return the byteOffsetNotWordAlignedMessage
     */
    @ReportableProperty(order=8, value = "Byte offset not word aligned message.")
    public Message getByteOffsetNotWordAlignedMessage() {
        return ByteOffsetNotWordAlignedMessage;
    }

    /**
     * Get the value offset reference location file message
     * 
     * @return the valueOffsetReferenceLocationFileMessage
     */
    @ReportableProperty(order=9, value = "value offset reference location file message.")
    public Message getValueOffsetReferenceLocationFileMessage() {
        return ValueOffsetReferenceLocationFileMessage;
    }


    /**
     * @return the typeMismatchMessage
     */
    @ReportableProperty(order=10, value="type mismatch message.")
    public Message getTypeMismatchMessage() {
        return TypeMismatchMessage;
    }

    /**
     * @return the invalidCountMessage
     */
    @ReportableProperty(order=11, value="Invalid Count Message.")
    public Message getInvalidCountMessage() {
        return InvalidCountMessage;
    }

    /**
     * @return the invalidCountValueMessage
     */
    @ReportableProperty(order=12, value="Invalid Count Value Message.")
    public Message getInvalidCountValueMessage() {
        return InvalidCountValueMessage;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }

}
