/**
 * 
 */
package org.jhove2.module.format.tiff.type;

import java.io.IOException;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.io.Input;
import org.jhove2.core.reportable.AbstractReportable;

/**
 * @author mstrong
 *
 */
public class Ascii 
extends AbstractReportable {
    
    private String value;
    /*
     * no-arg constructor
     */
    public Ascii()  {
    }
    
    /*
     * read in the ASCII value (i.e. String)
     */
    public void setValue(Input input, long count) throws IOException {
        byte[] buf = new byte[(int) count];

        StringBuffer sb = new StringBuffer();
        for (int i=0; i< count; i++) {
            buf[i] = (byte) input.readUnsignedByte();
            if (buf[i] == 0)
                break;
            sb.append(buf[i]);
        }
        value = sb.toString();
    }

    @ReportableProperty(order = 1, value="Tag ASCII value")
    public String getValue(){
        return value;
    }
}

