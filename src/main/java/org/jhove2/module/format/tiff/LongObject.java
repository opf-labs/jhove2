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
public class LongObject 
extends AbstractReportable {
    private long value;

    /**  no-arg constructor for Long object */
    public LongObject() {
    }
    
    LongObject (long value) {
        this.value = value;
    }
    @ReportableProperty(order = 1, value="Tag Long value")
    public long getValue(){
        return value;
    }
}
