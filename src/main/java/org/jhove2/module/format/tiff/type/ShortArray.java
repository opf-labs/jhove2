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
 * Stores an array of TIFF SHORT types
 * @see org.jhove2.module.format.tiff.type.Short
 * 
 * @author mstrong
 *
 */
@Persistent
public class ShortArray
    extends AbstractReportable {  
    
    private int[] valueArray;

    /**  no-arg constructor for ShortArray object */
    public ShortArray() {}
    
    /**
     * @return the value stored in the ShortArray reportable object
     */
    @ReportableProperty(order = 1, value = "Tag SHORT Array value")
    public String getValue() {
        return this.toString();
    }
    /**
     * @return the value Array
     */
    public int[] getShortArrayValue() {
        return valueArray;
    }
    
    public void setValue(Input input, long count) throws IOException {
        valueArray = new int [(int) count];
        for (int i=0; i<count; i++) {
            valueArray[i] = (int) input.readUnsignedShort();
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
