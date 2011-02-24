/**
 * 
 */
package org.jhove2.module.format.tiff;


import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.jhove2.core.JHOVE2Exception;
import org.jhove2.module.format.tiff.type.Short;
import org.jhove2.module.format.tiff.type.desc.Compression;
import org.junit.Before;
import org.junit.Test;

/**
 * @author mstrong
 *
 */
public class EmbeddedFormatTest extends TiffModuleTestBase{

    private String blueSquareTif;
    IFDEntry ifdEntry = null;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {       
        parse(blueSquareTif);
    }

    /**
     * Test methods for TIFF Parser 
     */
    @Test
    public void testCompressionDescriptionValue()  {
        // read in the compression value 
        String compression_d = null;
        List<IFD> ifdList = testTiffModule.getIFDs();
        for (IFD ifd : ifdList) {
            Map<Integer, IFDEntry> ifdEntryList = ifd.getEntries();
            if ((ifdEntry = ifdEntryList.get(TiffIFD.COMPRESSION)) != null) {
                int scheme = ((Short) ifdEntry.getValue()).getValue();
                 /* set the descriptive format for the Compression Scheme */
                Compression compression = null;
                try {
                    compression = Compression.getCompressionValue(scheme, this.getJHOVE2());
                    if (compression != null) {
                        compression_d = compression.getDescription();
                        assertTrue("Compression description value: <" + compression_d + "> is not as expected", compression_d.equalsIgnoreCase("No Compression"));
                    }
                }
                catch (JHOVE2Exception e) {
                    e.printStackTrace();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Tests that Tiff File has 2 child sources (ICCProfile & XMP)
     * 
     */
    @Test
    public void testICCProfileExists() {
        int size = 0;
        try {
            size = fileSource.getChildSources().size();
        }
        catch (JHOVE2Exception e) {
            e.printStackTrace();
        }
        assertTrue("Not all child sources reported", size == 2);
        List<IFD> ifdList = testTiffModule.getIFDs();
        for (IFD ifd : ifdList) {
            Map<Integer, IFDEntry> ifdEntryList = ifd.getEntries();
            IFDEntry ifdEntry = null;
            ifdEntry = ifdEntryList.get(TiffIFD.ICCPROFILE);
            assertTrue("ICCPRofile flagged", ifdEntry != null);
        }
    }

    public String getBlueSquareTif() {
        return blueSquareTif;
    }

    @Resource
    public void setBlueSquareTif(String blueSquareTif) {
        this.blueSquareTif = blueSquareTif;
    }


}
