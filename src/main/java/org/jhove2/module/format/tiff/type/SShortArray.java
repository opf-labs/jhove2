/**
 * 
 */
package org.jhove2.module.format.tiff.type;

import java.io.IOException;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.io.Input;
import org.jhove2.core.reportable.AbstractReportable;

/**
 * Stores an array of TIFF SSHORT types
 * @see org.jhove2.module.format.tiff.type.SShort
 * 
 * @author mstrong
 *
 */
public class SShortArray
    extends AbstractReportable {  
    
    private short[] valueArray;

    /**  no-arg constructor for SShortArray object */
    public SShortArray() {}
    
    /**
     * @return the value
     */
    @ReportableProperty(order = 1, value = "Tag SSHORT Array value")
    public String getValueArray() {
        return this.toString();
    }
    
    /**
     * @return the value
     */
    public short[] getValue() {
        return this.valueArray;
    }
    
    public void setValue(Input input, long count) throws IOException {
        valueArray = new short [(int) count];
        for (int i=0; i<count; i++) {
            valueArray[i] = input.readSignedShort();
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
