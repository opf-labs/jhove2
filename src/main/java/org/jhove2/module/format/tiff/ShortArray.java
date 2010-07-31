/**
 * 
 */
package org.jhove2.module.format.tiff;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.reportable.AbstractReportable;

/**
 * @author mstrong
 *
 */
public class ShortArray
    extends AbstractReportable {  
    
    private short[] value;

    /**  no-arg constructor for ShortArray object */
    public ShortArray() {}
    
    /**
     * @return the value
     */
    @ReportableProperty(order = 1, value = "Tag SHORT Array value")
    public short[] getValue() {
        return this.value;
    }
}
