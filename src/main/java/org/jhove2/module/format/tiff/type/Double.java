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
public class Double 
    extends AbstractReportable {

    private double value;

    /** no-arg constructor for Double object */
    public Double() {
    }
    
    public Double(double value) {
        this.value = value;
    }
    
    /**
     * @return the value
     */
    @ReportableProperty(order = 1, value = "Tag DOUBLE value")
    public double getValue() {
        return this.value;
    }

}

