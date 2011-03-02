/**
 * 
 */
package org.jhove2.persist.berkeleydpl;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.Resource;

import org.jhove2.app.util.FeatureConfigurationUtil;
import org.jhove2.config.ConfigInfo;
import org.jhove2.config.spring.SpringConfigInfo;
import org.jhove2.core.I8R;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.Message;
import org.jhove2.core.TimerInfo;
import org.jhove2.core.Message.Context;
import org.jhove2.core.Message.Severity;
import org.jhove2.core.format.FormatIdentification;
import org.jhove2.core.format.FormatIdentification.Confidence;
import org.jhove2.core.source.AbstractSource;
import org.jhove2.core.source.Source;
import org.jhove2.module.Module;
import org.jhove2.module.digest.DigesterModule;
import org.jhove2.module.identify.IdentifierModule;
import org.jhove2.persist.PersistenceManager;
import org.jhove2.persist.PersistenceManagerUtil;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author smorrissey
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:**/test-config.xml", 
"classpath*:**/filepaths-config.xml"})
public class BerkeleyDbSourceAccessorTest {

	static String persistenceMgrClassName = "org.jhove2.config.spring.SpringBerkeleyDbPersistenceManagerFactory";
	static PersistenceManager persistenceManager = null;

	BerkeleyDbSourceFactory sourceFactory;
	Source source;
	protected String sgmlDirBasePath;
	protected String sgmlDirPath;
	protected String tempDirBasePath;
	protected static JHOVE2 jhove2;
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		PersistenceManagerUtil.createPersistenceManagerFactory(persistenceMgrClassName);
		persistenceManager = PersistenceManagerUtil.getPersistenceManagerFactory().getInstance();
		persistenceManager.initialize();
		jhove2 = new JHOVE2();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		if (persistenceManager != null){
			try{
				persistenceManager.close();
			}
			catch (JHOVE2Exception je){
				System.err.println(je.getMessage());
				je.printStackTrace(System.err);
			}
		}
	}

	@Before
	public void setUp() throws Exception {
		sourceFactory = new BerkeleyDbSourceFactory();
		jhove2.setSourceFactory(sourceFactory);
		try {
			sgmlDirPath = 
				FeatureConfigurationUtil.getFilePathFromClasspath(sgmlDirBasePath, "temp dir");
			String tmpDirPath = 
				FeatureConfigurationUtil.getFilePathFromClasspath(tempDirBasePath, "temp dir");
			tempDirBasePath = tempDirBasePath.concat("empty/");	
			File emptyDir = new File(tmpDirPath);
			if (!emptyDir.exists()){
				emptyDir.mkdirs();
			}
		} catch (JHOVE2Exception e1) {
			fail("Could not create base directory");
		}
		File fsgml = new File(sgmlDirPath);
		sgmlDirPath = fsgml.getPath();
	}


	/**
	 * Test method for {@link org.jhove2.persist.berkeleydpl.BerkeleyDbSourceAccessor#addModule(org.jhove2.core.source.Source, org.jhove2.module.Module)}.
	 */
	@Test
	public void testAddModule() {
		long sourceId=0L, moduleId=0L;
		try {
			source = sourceFactory.getSource(jhove2, sgmlDirPath);
			source = source.getSourceAccessor().persistSource(source);
			assertNotNull( source.getSourceId());
			sourceId = source.getSourceId().longValue();
			source = source.getSourceAccessor().retrieveSource(source.getSourceId());
			assertEquals(sourceId, source.getSourceId().longValue());			
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		} 
		Module module1 = null;
		Module module2 = null;
		try {
			module1 = new DigesterModule(new BerkeleyDbBaseModuleAccessor());
			module2 = new IdentifierModule(new BerkeleyDbIdentifierAccessor());
		} catch (JHOVE2Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		assertNull(module1.getModuleId());
		assertNull(module1.getParentSourceId());
		assertNull(module2.getModuleId());
		assertNull(module2.getParentSourceId());

		try {
			module1 = source.addModule(module1);
			assertNotNull(module1.getModuleId());
			moduleId = module1.getModuleId().longValue();
			assertTrue(0L < moduleId);
			assertEquals(source.getSourceId(), module1.getParentSourceId());
			String module2Id = module2.getReportableIdentifier().toString();
			if (!AbstractSource.getModuleIDs().contains(module2Id)){
				module2 = source.addModule(module2);
				assertEquals(sourceId, module2.getModuleId().longValue());
				assertEquals(source.getSourceId(), module2.getParentSourceId());
				assertEquals(2, source.getNumModules());
				List<Module> childModules = source.getModules();
				assertEquals(2, childModules.size());
				for (Module module :childModules){
					assertEquals(source.getSourceId(), module.getParentSourceId());
				}		
				source = source.getSourceAccessor().retrieveSource(source.getSourceId());
				assertEquals(sourceId, source.getSourceId().longValue());
			}
		} catch (JHOVE2Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}


	/**
	 * Test method for {@link org.jhove2.persist.berkeleydpl.BerkeleyDbSourceAccessor#addChildSource(org.jhove2.core.source.Source, org.jhove2.core.source.Source)}.
	 */
	@Test
	public void testAddChildSource() {
		try {
			source = sourceFactory.getSource(jhove2, sgmlDirPath);
			source = source.getSourceAccessor().persistSource(source);
			assertNotNull(source.getSourceId());
			source = source.getSourceAccessor().retrieveSource(source.getSourceId());	

			int childSourceCount = source.getNumChildSources();

			Source source02 = sourceFactory.getSource(jhove2, tempDirBasePath);
			assertNotNull(source02.getSourceId());
			assertNull(source02.getParentSourceId());

			source02 = source.addChildSource(source02);
			assertEquals(source.getSourceId(), source.getSourceId());
			assertEquals(source.getSourceId(), source02.getParentSourceId());

			source02 = source.deleteChildSource(source02);
			assertNull(source02.getParentSourceId());
			assertEquals(source.getSourceId(), source.getSourceId());

			source02 = source.addChildSource(source02);
			assertEquals(source.getSourceId(), source.getSourceId());
			assertEquals(source.getSourceId(), source02.getParentSourceId());

			Source source03 = sourceFactory.getSource(jhove2, tempDirBasePath);
			assertNotNull(source03.getSourceId());
			assertNull(source03.getParentSourceId());
			source03 = source.addChildSource(source03);	
			assertEquals(source.getSourceId(), source03.getParentSourceId());
			assertEquals(source.getSourceId(), source02.getParentSourceId());

			assertEquals(childSourceCount + 2, source.getNumChildSources());
			for (Source cSource:source.getChildSources()){
				assertEquals(source.getSourceId(), cSource.getParentSourceId());
			}

			source03 = source.deleteChildSource(source03);
			assertNull(source03.getParentSourceId());
			assertEquals(childSourceCount + 1, source.getNumChildSources());

			source02 = source.deleteChildSource(source02);
			assertNull(source02.getParentSourceId());
			assertEquals(childSourceCount, source.getNumChildSources());


		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		} 
	}


	/**
	 * Test method for {@link org.jhove2.persist.berkeleydpl.BerkeleyDbSourceAccessor#startTimerInfo(org.jhove2.core.source.Source)}.
	 */
	@Test
	public void testStartTimerInfo() {
		try {
			source = sourceFactory.getSource(jhove2, tempDirBasePath);
			TimerInfo timer = source.getTimerInfo();
			/* This test is dependent on the process load of the underlying OS.
			 * Build in a sufficient threshold.
			 */
			/* assertEquals(1,timer.getElapsedTime().getDuration()); */
			assertTrue(25 >= timer.getElapsedTime().getDuration());
			source = source.startTimer();
			Thread.sleep(500);
			source = source.endTimer();
			assertTrue(400L <= source.getTimerInfo().getElapsedTime().getDuration());

		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		} 
	}

	/**
	 * Test method for {@link org.jhove2.persist.berkeleydpl.BerkeleyDbSourceAccessor#addMessage(org.jhove2.core.source.Source, org.jhove2.core.Message)}.
	 */
	@Test
	public void testAddMessage() {
		Message message01 = null;
		Message message02 = null;
		ConfigInfo configInfo = new SpringConfigInfo();

		try {
			source = sourceFactory.getSource(jhove2, tempDirBasePath);
			assertEquals(0, source.getMessages().size());
			message01 = new Message(Severity.ERROR,
					Context.OBJECT, 
					"org.jhove2.module.format.utf8.UTF8Module.failFastMessage", configInfo);
			source =  source.addMessage(message01);
			assertEquals(1, source.getMessages().size());
			message02 = new Message(Severity.ERROR,
					Context.OBJECT, 
					"org.jhove2.module.format.utf8.UTF8Module.failFastMessage", configInfo);
			source =  source.addMessage(message02);
			assertEquals(2, source.getMessages().size());		
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		} 
	}

	/**
	 * Test method for {@link org.jhove2.persist.berkeleydpl.BerkeleyDbSourceAccessor#addPresumptiveFormat(org.jhove2.core.source.Source, org.jhove2.core.format.FormatIdentification)}.
	 */
	@Test
	public void testAddPresumptiveFormat() {
		I8R i8r = new I8R("http://jhove2.org/terms/format/directory");
		FormatIdentification fi = 
			new FormatIdentification(i8r, Confidence.PositiveSpecific);

		try {
			source = sourceFactory.getSource(jhove2, tempDirBasePath);
			assertEquals(0,source.getPresumptiveFormats().size());
			source = source.addPresumptiveFormat(fi);
			assertEquals(1,source.getPresumptiveFormats().size());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/**
	 * Test method for {@link org.jhove2.persist.berkeleydpl.BerkeleyDbSourceAccessor#addPresumptiveFormats(org.jhove2.core.source.Source, java.util.Set)}.
	 */
	@Test
	public void testAddPresumptiveFormats() {
		I8R i8r = new I8R("http://jhove2.org/terms/format/directory");
		FormatIdentification fi = 
			new FormatIdentification(i8r, Confidence.PositiveSpecific);
		FormatIdentification fi2 = 
			new FormatIdentification(i8r, Confidence.Tentative);
		Set<FormatIdentification>  fis = new TreeSet<FormatIdentification>();
		try {
			source = sourceFactory.getSource(jhove2, tempDirBasePath);
			assertEquals(0,source.getPresumptiveFormats().size());
			source = source.addPresumptiveFormats(fis);
			assertEquals(0,source.getPresumptiveFormats().size());
			fis.add(fi);
			fis.add(fi2);
			source = source.addPresumptiveFormats(fis);
			assertEquals(2,source.getPresumptiveFormats().size());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}



	/**
	 * @return the sgmlDirBasePath
	 */
	public String getSgmlDirBasePath() {
		return sgmlDirBasePath;
	}

	/**
	 * @param sgmlDirBasePath the sgmlDirBasePath to set
	 */
	@Resource
	public void setSgmlDirBasePath(String sgmlDirBasePath) {
		this.sgmlDirBasePath = sgmlDirBasePath;
	}
	public String getTempDirBasePath() {
		return tempDirBasePath;
	}
	@Resource
	public void setTempDirBasePath(String emptyDirPath) {
		this.tempDirBasePath = emptyDirPath;
	}
}
