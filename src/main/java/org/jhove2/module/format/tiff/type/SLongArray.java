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
 */
public class SLongArray
    extends AbstractReportable {
    private long[] valueArray;

    /**  no-arg constructor for SLongArray object */
    public SLongArray() {
    }

    public void setValue(Input input, long count) throws IOException {
        valueArray = new long [(int) count];
        for (int i=0; i<count; i++) {
            valueArray[i] = input.readSignedLong();
        }
    }
    
    /**
     * @return the value
     */
    @ReportableProperty(order = 1, value = "Tag SLONG Array value")
    public String getValue() {
        return this.toString();
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