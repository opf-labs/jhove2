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
public class SByteArray
    extends AbstractReportable {
    
    private byte[] valueArray;

    /**  no-arg constructor for SByteArray object */
    public SByteArray() {}
    
    /**
     * @return the value
     */
    @ReportableProperty(order = 1, value = "Tag SBYTE Array value")
    public String getValueArray() {
        return this.toString();
    }
    
    public void setValue(Input input, long count) throws IOException {
        valueArray = new byte [(int) count];
        for (int i=0; i<count; i++) {
            valueArray[i] = input.readSignedByte();
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
