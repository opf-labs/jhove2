/**
 * 
 */
package org.jhove2.module.format.tiff.type;

import java.io.IOException;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.io.Input;
import org.jhove2.core.reportable.AbstractReportable;

/**
 * @author mstrong
 *
 * LongArray class - holds an Array of LONG Tiff type values
 * @see org.jhove2.module.format.tiff.type.Long
 *
 */
public class LongArray
    extends AbstractReportable {
    
    private long[] valueArray;

    /**  no-arg constructor for LongArray object */
    public LongArray() {
    }

    public void setValue(Input input, long count) throws IOException {
        valueArray = new long [(int) count];
        for (int i=0; i<count; i++) {
            valueArray[i] = input.readUnsignedInt();
        }
    }
    
    /**
     * @return the value
     */
    @ReportableProperty(order = 1, value = "Tag LONG Array value")
    public String getValue() {
        return this.toString();
    }

    /**
     * @return the value
     */
    public long[] getLongArrayValue() {
        return this.valueArray;
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

