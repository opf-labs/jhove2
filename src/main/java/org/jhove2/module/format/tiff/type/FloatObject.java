/**
 * 
 */
package org.jhove2.module.format.tiff.type;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.reportable.AbstractReportable;

/**
 * @author mstrong
 *
 */
public class FloatObject
    extends AbstractReportable {

    private float value;

    /**  no-arg constructor for Float object */
    public FloatObject() {    
    }

    public FloatObject(float value) {
        this.value = value;
    }
    
    @ReportableProperty(order = 1, value = "Tag FLOAT value")
    public float getValue() {
        return value;
    }
}


