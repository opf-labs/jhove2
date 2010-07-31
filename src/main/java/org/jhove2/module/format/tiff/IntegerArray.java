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
public class IntegerArray
    extends AbstractReportable {
    private Integer value;

    /**  no-arg constructor for IntegerArray object */
    public IntegerArray() {
    }
    
    /**
     * @return the value
     */
    @ReportableProperty(order = 1, value = "Tag Integer Array value")
    public Integer getValue() {
        return this.value.intValue();
    }

}
