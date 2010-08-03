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
public class TiffTagTest {

    private JHOVE2 JHOVE2;
    private Set<TiffTag> tiffTagSet = null;
    private boolean print = false;

    @Test
    public void testGetTagIntProperties() {
        Properties tiffTagProps;
        try {
            tiffTagProps = JHOVE2.getConfigInfo().getProperties("TiffTag");
            if (tiffTagProps != null){
                tiffTagSet = TiffTag.getTiffTags(tiffTagProps);
            }
            if (print){
                TiffTag[] tiffTagArray = (TiffTag[]) tiffTagSet.toArray(new TiffTag[tiffTagSet.size()]);
                for (TiffTag tiffTag:tiffTagArray)
                    System.out.println("tiff tag # =" + tiffTag.getTag() 
                            + " Name = " + tiffTag.getName() + " type = " 
                            + Arrays.toString(tiffTag.getType()) 
                            + " card = " + tiffTag.getCardinality() + " defaultValue = " + tiffTag.getDefValue() 
                            + " version = " + tiffTag.getVersion());
                
            }
        }
        catch (JHOVE2Exception e) {
            e.printStackTrace();
            fail("exception");
        }
    }

    @Test
    public void testGetTag() {
        int tag;
        tag = 315; //Artist tag

        TiffTag tiffTag;
        try {
            tiffTag = TiffTag.getTag(tag);
            assertTrue ("Tag " + tag + " does not exist", tiffTag.getTag() == tag);
        }
        catch (JHOVE2Exception e) {
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
