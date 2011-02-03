/**
 * 
 */
package org.jhove2.module.format.tiff;

import static org.junit.Assert.assertTrue;

import java.nio.ByteOrder;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

/**
 * @author mstrong
 *
 */

public class WordAlignmentTest extends TiffModuleTestBase{

    private String wordAlignmentErrorFile;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        parse(wordAlignmentErrorFile);
    }


    /**
     * Test that the ByteOffsetNotWordAlignedMessage is present for the COPYRIGHT field
     */
    @Test
    public void testWordAlignmentMessage() {
        List<IFD> ifdList = testTiffModule.getIFDs();
        for (IFD ifd : ifdList) {
            Map<Integer, IFDEntry> ifdEntryList = ifd.getEntries();
            IFDEntry ifdEntry = null;
            
            if ((ifdEntry = ifdEntryList.get(TiffIFD.COPYRIGHT)) != null) {
                assertTrue("WordAlignmentError flagged", ifdEntry.getByteOffsetNotWordAlignedMessage() != null);
            }
        }
    }

    public String getWordAlignmentErrorFile() {
        return wordAlignmentErrorFile;
    }

    @Resource
    public void setWordAlignmentErrorFile(String wordAlignmentErrorFile) {
        this.wordAlignmentErrorFile = wordAlignmentErrorFile;
    }


}
