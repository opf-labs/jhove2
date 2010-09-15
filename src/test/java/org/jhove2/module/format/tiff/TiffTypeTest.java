package org.jhove2.module.format.tiff;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Properties;
import java.util.Set;

import javax.annotation.Resource;

import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Class to test the loading of the Tifftags Set
 * from the tiff tags properties file
 * 
 * @author mstrong
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:**/test-config.xml",
        "classpath*:**/tiff-test-config.xml","classpath*:**/filepaths-config.xml"})
        public class TiffTypeTest {

    private JHOVE2 JHOVE2;
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



public JHOVE2 getJHOVE2() {
    return JHOVE2;
}
@Resource (name="JHOVE2")
public void setJHOVE2(JHOVE2 jHOVE2) {
    JHOVE2 = jHOVE2;
}

}
