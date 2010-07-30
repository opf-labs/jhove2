package org.jhove2.module.format.tiff;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.reportable.AbstractReportable;

public class AsciiArray 
    extends AbstractReportable {
    public String[] value;

    /*
     * no-arg constructor
     */
    public AsciiArray() {
    }
    
    @ReportableProperty(order = 1, value="Tag ASCII Array value")
    public String[] getValue(){
        return value;
    }
}

