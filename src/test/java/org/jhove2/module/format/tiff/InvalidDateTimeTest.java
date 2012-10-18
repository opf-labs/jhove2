/**
 * 
 */
package org.jhove2.module.format.tiff;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

/**
 * @author mstrong
 *
 */

public class InvalidDateTimeTest extends TiffModuleTestBase{

    private String invalidDateTimeFile;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    	super.setUp();
        parse(invalidDateTimeFile);
    }


    /**
     * Test that the InvalidDateTimeMessage is present for the DATETIME field
     */
    @Test
    public void testInvalidDateTimeMessage() {
        List<IFD> ifdList = testTiffModule.getIFDs();
        for (IFD ifd : ifdList) {
            Map<Integer, IFDEntry> ifdEntryList = ifd.getEntries();
            IFDEntry ifdEntry = null;
            
            if ((ifdEntry = ifdEntryList.get(TiffIFD.DATETIME)) != null) {
                assertTrue("InvalidDateTime error not flagged", ifdEntry.getInvalidDateTimeMessage() != null);
            }
        }
    }

    public String getInvalidDateTimeFile() {
        return invalidDateTimeFile;
    }

    @Resource
    public void setInvalidDateTimeFile(String invalidDateTimeFile) {
        this.invalidDateTimeFile = invalidDateTimeFile;
    }


}
