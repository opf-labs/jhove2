package org.jhove2.module.format.tiff;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
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
    private boolean print = true;

    @Test
    public void testGettypeProperties() {
        Properties TiffTypeProps;
        try {
            TiffTypeProps = JHOVE2.getConfigInfo().getProperties("TiffType");
            if (TiffTypeProps != null){
                TiffTypeSet = TiffType.getTiffTypes(TiffTypeProps);
            }
            if (print){
                TiffType[] TiffTypeArray = (TiffType[]) TiffTypeSet.toArray(new TiffType[TiffTypeSet.size()]);
                for (TiffType tiffType:TiffTypeArray)
                    System.out.println("tiff type " + tiffType.getNum() + "=" 
                            + tiffType.getTypeName()  
                            + ", Size = " + tiffType.getSize());
                
            }
        }
        catch (JHOVE2Exception e) {
            e.printStackTrace();
            fail("exception");
        }
    }

    @Test
    public void testGettype() {
        int type;
        type = 10; //SRATIONAL type

        TiffType tiffType;
        try {
            tiffType = TiffType.getType(type);
            assertTrue ("type " + type + " does not exist", tiffType.getNum() == type);
        }
        catch (Exception e) {
            e.printStackTrace();
            fail("exception");
        }
        
    }

    public JHOVE2 getJHOVE2() {
        return JHOVE2;
    }
    @Resource (name="JHOVE2")
    public void setJHOVE2(JHOVE2 jHOVE2) {
        JHOVE2 = jHOVE2;
    }

}
