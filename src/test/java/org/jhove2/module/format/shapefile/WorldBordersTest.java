package org.jhove2.module.format.shapefile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;

import javax.annotation.Resource;

import org.jhove2.core.JHOVE2;
import org.jhove2.core.source.ClumpSource;
import org.jhove2.core.source.FileSource;
import org.jhove2.core.source.SourceFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:**/test-config.xml", 
"classpath*:**/filepaths-config.xml"})
public class WorldBordersTest {
	
	ClumpSource clump;
	ShapefileModule ShapefileModule;
	ShapefileFeatures features;
	SourceFactory factory;

	@Before
	public void setUp() throws Exception {
		try {
		    JHOVE2 jhove2 = new JHOVE2();
			clump = factory.getClumpSource();
			String wbDirName = "src/test/resources/examples/shapefiles/worldBorders";
			File wbDir = new File(wbDirName);
			for (File file : wbDir.listFiles()) {
				if (file.getName().startsWith("TM_WORLD_BORDERS-0.2.")) {
					FileSource fs = (FileSource) factory.getSource(jhove2, file);
					clump.addChildSource(fs);
				}
			}			
		} catch (Exception e) {
			fail("Could not create clump source");
		}
		try {
			ShapefileModule.parse(new JHOVE2(), clump, null);
			features = ShapefileModule.getShapefileFeatures();
		} catch (Exception e) {
			fail("Could not parse clump source");
		}
	}

	@Test
	public void testFiles() {
		assertEquals(4,features.getMemberFiles().size());
		assertEquals("TM_WORLD_BORDERS-0.2",features.getShapefileStem());
		assertEquals("DBF => TM_WORLD_BORDERS-0.2.dbf",features.memberFiles.get(0));
	}

	@Test
	public void testMainShapefile() {
		assertEquals(3239232,features.shapefileHeader.getFileLength());
		assertEquals(83.62359600000008,features.shapefileHeader.getMaxY(),0.00000001);
		assertEquals("Polygon",features.shapefileHeader.getShapeType());
		assertEquals(246,features.getShapefileRecordCount());
	}
	
	@Test
	public void testDbfFile() {
		assertEquals(11,features.dbfHeader.getFieldCount());
		assertEquals(246,features.dbfHeader.getRecordCount());
		assertEquals(99,features.dbfHeader.getRecordLength());
	}

	/**
	 * @param factory the factory to set
	 */
	@Resource(name="SourceFactory")
	public void setFactory(SourceFactory factory) {
		this.factory = factory;
	}

	/**
	 * @param shapefileModule the shapefileModule to set
	 */
	@Resource(name="ShapefileModule")
	public void setShapefileModule(ShapefileModule shapefileModule) {
		this.ShapefileModule = shapefileModule;
	}	
}
