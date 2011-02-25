package org.jhove2.module.format.shapefile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;

import javax.annotation.Resource;

import org.jhove2.config.spring.SpringConfigInfo;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.source.ClumpSource;
import org.jhove2.core.source.FileSource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:**/test-config.xml", 
"classpath*:**/filepaths-config.xml"})
public class EmptyShapefileTest {
	
	ClumpSource clump;
	ShapefileModule ShapefileModule;
	ShapefileFeatures features;
	JHOVE2 JHOVE2;

	@Before
	public void setUp() throws Exception {
		JHOVE2.setConfigInfo(new SpringConfigInfo());
		try {
			String wbDirName = "src/test/resources/examples/shapefiles/multipleEmpty";
			File wbDir = new File(wbDirName);
			clump = JHOVE2.getSourceFactory().getClumpSource(JHOVE2);
			for (File file : wbDir.listFiles()) {
				if (file.getName().startsWith("abc.")) {
					FileSource fs = (FileSource) JHOVE2.getSourceFactory().getSource(JHOVE2, file);
					clump.addChildSource(fs);
				}
			}			
		} catch (Exception e) {
			fail("Could not create clump source");
		}
		try {
			
			ShapefileModule.parse(JHOVE2, clump, null);
		} catch (Exception e) {
			;
		} finally {
			features = ShapefileModule.getShapefileFeatures();
		}
	}

	@Test
	public void testFiles() {
		assertEquals(5,features.getMemberFiles().size());
		assertEquals("abc",features.getShapefileStem());
		assertEquals("DBF => abc.dbf",features.memberFiles.get(0));
	}

	@Test
	public void testMainShapefile() {
		assertEquals(0,features.shapefileHeader.getFileLength());
		assertEquals(0,features.shapefileHeader.getMaxY(),0.00000001);
		assertEquals(null,features.shapefileHeader.getShapeType());
		assertEquals(0,features.getShapefileRecordCount());
	}
	
	@Test
	public void testDbfFile() {
		assertEquals(0,features.dbfHeader.getFieldCount());
		assertEquals(0,features.dbfHeader.getRecordCount());
		assertEquals(0,features.dbfHeader.getRecordLength());
	}
	/**
	 * @param shapefileModule the shapefileModule to set
	 */
	@Resource(name="ShapefileModule")
	public void setShapefileModule(ShapefileModule shapefileModule) {
		this.ShapefileModule = shapefileModule;
	}	
	
    @Resource
    public void setJHOVE2(JHOVE2 jHOVE2) {
        JHOVE2 = jHOVE2;
    }
}
