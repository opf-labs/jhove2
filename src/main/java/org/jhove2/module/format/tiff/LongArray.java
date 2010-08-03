/**
 * 
 */
package org.jhove2.module.format.tiff;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.reportable.AbstractReportable;

/**
 * @author mstrong
 *
 * LongArray class - holds an Array of LONG Tiff type values
 *
 */
public class LongArray
    extends AbstractReportable {
    
    private long[] value;

    /**  no-arg constructor for LongArray object */
    public LongArray() {
    }

    /**
     * @return the value
     */
    @ReportableProperty(order = 1, value = "Tag LONG Array value")
    public long[] getValue() {
        return this.value;
    }
}

