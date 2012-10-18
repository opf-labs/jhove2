package org.jhove2.module.format.tiff;

import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;


/**
 * Class to test the loading of the Tifftags Set
 * from the tiff tags properties file
 * 
 * @author mstrong
 *
 */

public class TiffTypeTest {

    private Set<TiffType> TiffTypeSet = null;
    private boolean print = false;

    @Test
    public void testGettype() {
        int type;
        type = 10; //SRATIONAL type

        TiffType tiffType;
        tiffType = TiffType.getType(type);
        assertTrue ("type " + type + " does not exist", tiffType.num() == type);

    }

    @Test
    public void testEqualsTiffType() {
        TiffType tiffType = TiffType.getType(10);  // SRATIONAL Type
        assertTrue("Type 10 does not equal " + tiffType.SRATIONAL.num(), tiffType.equals(TiffType.SRATIONAL));

    }

}
