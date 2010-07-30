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
public class FloatObject
    extends AbstractReportable {

    private float value;

    /**  no-arg constructor for Float object */
    public FloatObject() {    
    }
    
    @ReportableProperty(order = 1, value = "Tag FLOAT value")
    public float getValue() {
        return value;
    }
}


