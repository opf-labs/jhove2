/**
 * 
 */
package org.jhove2.core.source;

import static org.junit.Assert.*;

import java.util.ArrayList;

import javax.annotation.Resource;

import org.jhove2.core.JHOVE2;
import org.jhove2.module.identify.AggrefierModule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author smorrissey
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:**/abstractsource-config.xml"})
public class AbstractSourceTest {
	
	private String dirPath;
	private ArrayList<String> fileList;
	private AggrefierModule Aggrefier;
	private JHOVE2 JHOVE2;

	/**
	 * Test method for {@link org.jhove2.core.source.AbstractSource#equals(java.lang.Object)}.
	 */
	@Test
	public void testEqualsObject() {
		FileSetSource fsSource1 = new FileSetSource();
		assertEquals(fsSource1, fsSource1);
		assertFalse(fsSource1.equals(null));
		FileSetSource fsSource2 = new FileSetSource();
		assertEquals(fsSource2, fsSource2);
		assertFalse(fsSource2.equals(null));
		assertEquals(fsSource1,fsSource2);
		assertEquals(fsSource2,fsSource1);
		ClumpSource clump1 = new ClumpSource();
		assertEquals(clump1, clump1);
		assertFalse(clump1.equals(null));
		assertFalse(clump1.equals(fsSource1));
		assertFalse(clump1.equals(fsSource2));
		assertFalse(fsSource1.equals(clump1));
		assertFalse(fsSource2.equals(clump1));
		ClumpSource clump2 = new ClumpSource();
		assertEquals(clump2, clump2);
		assertFalse(clump2.equals(null));
		assertFalse(clump2.equals(fsSource1));
		assertFalse(clump2.equals(fsSource2));
		assertFalse(fsSource1.equals(clump2));
		assertFalse(fsSource2.equals(clump2));
		assertEquals(clump1, clump2);
		assertEquals(clump2, clump1);
		
		clump1.addChildSource(fsSource1);
		assertFalse(clump1.equals(fsSource1));
		assertFalse(clump1.equals(clump2));
		assertFalse(clump2.equals(clump1));
		clump2.addChildSource(fsSource2);
		assertEquals(clump1, clump2);
		
		fsSource1.addModule(JHOVE2);
		assertFalse(fsSource1.equals(fsSource2));
		assertFalse(clump1.equals(clump2));
		fsSource2.addModule(JHOVE2);
		assertEquals(fsSource1, fsSource2);
		assertEquals(clump1, clump2);
		fsSource1.getModules().clear();
		assertFalse(fsSource1.equals(fsSource2));
		assertFalse(clump1.equals(clump2));
		fsSource1.addModule(Aggrefier);
		assertFalse(fsSource1.equals(fsSource2));
		assertFalse(clump1.equals(clump2));
		fsSource1.addModule(JHOVE2);
		assertFalse(fsSource1.equals(fsSource2));
		assertFalse(clump1.equals(clump2));
		fsSource2.addModule(Aggrefier);
		assertTrue(fsSource1.equals(fsSource2));
		fsSource2.getModules().clear();
		fsSource2.addModule(Aggrefier);
		fsSource2.addModule(JHOVE2);
		assertEquals(fsSource1, fsSource2);
		assertEquals(clump1, clump2);
	}

	/**
	 * Test method for {@link org.jhove2.core.source.AbstractSource#compareTo(org.jhove2.core.source.Source)}.
	 */
	@Test
	public void testCompareTo() {
		FileSetSource fsSource1 = new FileSetSource();
		assertEquals(0,fsSource1.compareTo(fsSource1));
		assertEquals(1,fsSource1.compareTo(null));		
		FileSetSource fsSource2 = new FileSetSource();
		assertEquals(0,fsSource1.compareTo(fsSource2));		
		assertEquals(0,fsSource2.compareTo(fsSource1));
		
		ClumpSource clump1 = new ClumpSource();		
		ClumpSource clump2 = new ClumpSource();
		assertEquals(0, clump1.compareTo(clump1));
		assertEquals(0, clump1.compareTo(clump2));
		assertEquals(1, fsSource1.compareTo(clump1));
		assertEquals(-1, clump2.compareTo(fsSource2));
		
		clump1.addChildSource(fsSource1);
		assertEquals(1, clump1.compareTo(clump2));
		assertEquals(-1, clump2.compareTo(clump1));
		clump2.addChildSource(fsSource2);
		assertEquals(0, clump1.compareTo(clump2));
		
		fsSource1.addModule(JHOVE2);
		assertEquals(1,fsSource1.compareTo(fsSource2));
		assertEquals(1,clump1.compareTo(clump2));
		fsSource2.addModule(JHOVE2);
		assertEquals(0,fsSource2.compareTo(fsSource1));
		assertEquals(0, clump1.compareTo(clump2));
		fsSource2.addModule(Aggrefier);
		assertEquals(-1, fsSource1.compareTo(fsSource2));
		assertEquals(1,clump1.compareTo(clump2));
		fsSource1.getModules().clear();
		fsSource1.addModule(Aggrefier);
		fsSource1.addModule(JHOVE2);
		assertEquals(0, fsSource1.compareTo(fsSource2));
	}

	public String getDirPath() {
		return dirPath;
	}
	@Resource
	public void setDirPath(String dirPath) {
		this.dirPath = dirPath;
	}

	public ArrayList<String> getFileList() {
		return fileList;
	}
	@Resource
	public void setFileList(ArrayList<String> fileList) {
		this.fileList = fileList;
	}

	public AggrefierModule getAggrefier() {
		return Aggrefier;
	}
	@Resource
	public void setAggrefier(AggrefierModule aggrefier) {
		Aggrefier = aggrefier;
	}

	public JHOVE2 getJHOVE2() {
		return JHOVE2;
	}
	@Resource
	public void setJHOVE2(JHOVE2 jHOVE2) {
		JHOVE2 = jHOVE2;
	}

}
