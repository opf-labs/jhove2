/**
 * 
 */
package org.jhove2.module.format.tiff;

import static org.junit.Assert.assertTrue;

import java.nio.ByteOrder;
import java.util.List;
import java.util.Map;
import org.jhove2.module.format.tiff.type.Short;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

/**
 * @author mstrong
 *
 */

public class IntelTiffFileTest extends TiffModuleTestBase{

    private String intelTestFile;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        parse(intelTestFile);
    }

    /**
     * Test methods for TIFF Parser 
     */
    @Test
    public void testByteOrder() {
        ByteOrder byteOrder = testTiffModule.getIFH().getByteOrder();
        assertTrue(byteOrder == ByteOrder.LITTLE_ENDIAN);

    }

    @Test
    public void testIFDZeroEntriesMessages() {
        List<IFD> ifdList = testTiffModule.getIFDs();
        for (IFD ifd : ifdList) {
            assertTrue (ifd.getZeroIFDEntriesMessage() == null);        
        }
    }

    @Test
    public void testTagExistence() {
        List<IFD> ifdList = testTiffModule.getIFDs();
        for (IFD ifd : ifdList) {
            Map<Integer, IFDEntry> ifdEntryList = ifd.getEntries();
            IFDEntry ifdEntry = null;
            /* test 254 does exist and unknownTagMessage is null */
            if ((ifdEntry = ifdEntryList.get(TiffIFD.NEWSUBFILETYPE)) != null)
                assertTrue("Known tag 254 (NewSubfileType) flagged", ifdEntry.getUnknownTagMessage() == null);
            /* test 34851 tag does not exist */
            ifdEntry = ifdEntryList.get(34851);
            assertTrue("Tag 34851 should not exist in Tiff file ", ifdEntry == null);
        }
    }

    @Test
    public void testUnknownTagEntry() {
        List<IFD> ifdList = testTiffModule.getIFDs();
        for (IFD ifd : ifdList) {
            Map<Integer, IFDEntry> ifdEntryList = ifd.getEntries();
            IFDEntry ifdEntry = null;
            /* test 20515 does exist but tag is not defined */
            if ((ifdEntry = ifdEntryList.get(20515)) != null) {
                assertTrue("Unknown tag 20515 flagged", ifdEntry.getUnknownTagMessage() != null);

                /* test that the value type is what is expected */
                Object value = ifdEntry.getValue(); 
                String className = value.getClass().getName();
                assertTrue("Value type " + className + " is not of expected Short type", value instanceof org.jhove2.module.format.tiff.type.Short);

                /* test that the value is as expected */
                if (value instanceof org.jhove2.module.format.tiff.type.Short) {
                    int shortValue = ((Short)value).getValue();
                    assertTrue("Value of tag 20515: <" + shortValue + "> does not equal expected value of 6", shortValue==6);
                }
            }
        }
    }

    public String getIntelTestFile() {
        return intelTestFile;
    }

    @Resource
    public void setIntelTestFile(String intelTestFile) {
        this.intelTestFile = intelTestFile;
    }
}
