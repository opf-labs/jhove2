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
public class DoubleArray
    extends AbstractReportable {

    public double[] valueArray;

    /*
     * no-arg constructor
     */
    public DoubleArray() {
    }
    
    /**
     * @return the value in String form
     */
    @ReportableProperty(order = 1, value = "Tag DoubleArray value")
    public String getValueArray() {
        return this.toString();
    }
    
    /**
     * @return the value double[]
     */
    public double[] getValue() {
        return this.valueArray;
    }
    
    public void setValue(Input input, long count) throws IOException {
        valueArray = new double [(int) count];
        for (int i=0; i<count; i++) {
            valueArray[i] = input.readDouble();
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
