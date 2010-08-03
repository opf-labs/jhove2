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
public class ByteArray
extends AbstractReportable {
    private byte[] value;

    @ReportableProperty(order = 1, value="Tag BYTE Array value")
    public byte[] getValue(){
        return value;
    }
    
    /** no-arg constructor */
    public ByteArray() {
    }
}

