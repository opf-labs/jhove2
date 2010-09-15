/**
 * 
 */
package org.jhove2.module.format.tiff.type;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.reportable.AbstractReportable;

/**
 * Store the Tiff LONG type,
 * 32-bit (4-byte) unsigned integer
 * @author mstrong
 *
 */
public class Long 
extends AbstractReportable {
    private long value;

    /**  no-arg constructor for Long object */
    public Long() {
    }
    
    public Long (long value) {
        this.value = value;
    }
    @ReportableProperty(order = 1, value="Tag Long value")
    public long getValue(){
        return value;
    }

}
