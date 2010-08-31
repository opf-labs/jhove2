/**
 * 
 */
package org.jhove2.module.format.tiff.type;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.reportable.AbstractReportable;

/**
 * stores the Tiff SLONG type value
 * A 32-bite (4-byte) signed (two-complement) integer
 * @author mstrong
 *
 */
public class SLong 
extends AbstractReportable {
    private long value;

    /**  no-arg constructor for Long object */
    public SLong() {
    }
    
    public SLong (long value) {
        this.value = value;
    }
    @ReportableProperty(order = 1, value="Tag SLong value")
    public long getValue(){
        return value;
    }
}
