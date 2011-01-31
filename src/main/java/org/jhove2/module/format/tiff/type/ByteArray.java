/**
 * 
 */
package org.jhove2.module.format.tiff.type;

import java.io.IOException;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.io.Input;
import org.jhove2.core.reportable.AbstractReportable;

import com.sleepycat.persist.model.Persistent;

/**
 * Stores an array of Tiff BYTE types
 * 
 * @see org.jhove2.module.format.tiff.type.Byte
 * @author mstrong
 *
 */
@Persistent
public class ByteArray
extends AbstractReportable {
    private short[] valueArray;

    /** no-arg constructor */
    public ByteArray() {
    }

    /**
     * The Byte Array in a reportable format
     * @return String
     */
    @ReportableProperty(order = 1, value="Tag BYTE Array value")
    public String getValueArray(){
        return this.toString();
    }

    /**
     * The actual byte array
     * @return short[]
     */
    public short[] getByteArray(){
        return this.valueArray;
    }
    
    public void setValue(Input input, long count) throws IOException {
        valueArray = new short [(int) count];
        for (int i=0; i<count; i++) {
            valueArray[i] = (short) input.readUnsignedByte();
        }
    }
    
    @Override
    public String toString() {
        StringBuffer result = new StringBuffer();
        if (valueArray.length > 0) {
            result.append(valueArray[0]);
            for (int i=1; i < valueArray.length; i++) {
                result.append(", ");
                result.append(valueArray[i]);
            }
        }
        return result.toString();        
    }
}

