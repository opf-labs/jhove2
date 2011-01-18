/**
 * 
 */
package org.jhove2.module.format.tiff.type;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.reportable.AbstractReportable;

import com.sleepycat.persist.model.Persistent;


/** 
 * stores the Tiff BYTE type.  
 * 8-bit unsigned integer
 * 
 * @author mstrong
 *
 */
@Persistent
public class Byte 
    extends AbstractReportable {
    private short value;

    public Byte(short value) {
        this.value = value;
    }
    
    /**  no-arg constructor for Byte object */
    public Byte() {
    }

    @ReportableProperty(order = 1, value="Tag BYTE value")
    public short getValue(){
        return value;
    }
}

