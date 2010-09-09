package org.jhove2.module.format.shapefile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FilenameFilter;

import org.jhove2.app.util.FeatureConfigurationUtil;
import org.jhove2.config.spring.SpringConfigInfo;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.source.ClumpSource;
import org.jhove2.core.source.FileSource;
import org.junit.Before;
import org.junit.Test;

public class EmptyShapefileTest {
	
	ClumpSource clump = new ClumpSource();
	ShapefileModule sfModule = new ShapefileModule();
	ShapefileFeatures features;

	@Before
	public void setUp() throws Exception {
		try {
			String wbDirName = "src/test/resources/examples/shapefiles/multipleEmpty";
			File wbDir = new File(wbDirName);
			for (File file : wbDir.listFiles()) {
				if (file.getName().startsWith("abc.")) {
					FileSource fs = new FileSource(file);
					clump.addChildSource(fs);
				}
			}			
		} catch (Exception e) {
			fail("Could not create clump source");
		}
		try {
			JHOVE2 jhove = new JHOVE2();
			jhove.setConfigInfo(new SpringConfigInfo());
			sfModule.parse(jhove, clump);
		} catch (Exception e) {
			;
		} finally {
			features = sfModule.getShapefileFeatures();
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
}
