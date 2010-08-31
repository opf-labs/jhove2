/**
 * 
 */
package org.jhove2.module.format.tiff;

import java.io.IOException;

import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.io.Input;

/**
 * @author mstrong
 *
 */
public class TiffIFD 
extends IFD
implements Comparable {
    
    
    public static final int
        DATETIME = 306;
    
    public TiffIFD() {
        super();
    }

    public IFDEntry readValues(JHOVE2 jhove2, Input input, IFDEntry entry) throws JHOVE2Exception, IOException {
        
//        if (entry.getTag() == DATETIME) {
//            StringIFDEntry strIFDEntry = new StringIFDEntry(entry);            
//            strIFDEntry.setValue(readASCII(input, entry));
//            return strIFDEntry;
//        }
        
        TiffValue value = new TiffValue();
        input.setPosition(entry.getValueOffset());
        value.readValue(input, entry);
        entry.setValue(value);
        return entry;   
       
        /* TODO: fix this 
        if (entry.getType().equals("RATIONAL")) {
            Rational rvalue = null;
            rvalue = readRational(input, offset);
        }
         */
    }


    @Override
    public int compareTo(Object o) {
        // TODO Auto-generated method stub
        return 0;
    }

}
