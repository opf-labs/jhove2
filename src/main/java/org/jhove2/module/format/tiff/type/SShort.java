/**
 * 
 */
package org.jhove2.module.format.tiff.type;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.reportable.AbstractReportable;


/**
 * stores the Tiff SSHORT type value
 * a 16-byte (2-byte) signed (two-complement) integer
 * 
 * @author mstrong
 *
 */
public class SShort
    extends AbstractReportable {
    private short value;

    public SShort (short value) {
        this.value = value;
    }
    
    /**  no-arg constructor for Short object */
    public SShort () {
    }
    
    /**
     * @return the value
     */
    @ReportableProperty(order = 1, value = "Tag SSHORT  value")
    public short getValue() {
        return this.value;
    }


}
