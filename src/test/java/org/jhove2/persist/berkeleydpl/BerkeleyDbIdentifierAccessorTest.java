/**
 * JHOVE2 - Next-generation architecture for format-aware characterization
 *
 * Copyright (c) 2009 by The Regents of the University of California,
 * Ithaka Harbors, Inc., and The Board of Trustees of the Leland Stanford
 * Junior University.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * o Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 *
 * o Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * o Neither the name of the University of California/California Digital
 *   Library, Ithaka Harbors/Portico, or Stanford University, nor the names of
 *   its contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.jhove2.persist.berkeleydpl;

import static org.junit.Assert.*;

import javax.annotation.Resource;

import org.jhove2.core.JHOVE2Exception;
import org.jhove2.module.identify.DROIDIdentifier;
import org.jhove2.module.identify.SourceIdentifier;
import org.jhove2.module.identify.IdentifierModule;
import org.jhove2.persist.PersistenceManager;
import org.jhove2.persist.PersistenceManagerUtil;
import org.junit.AfterClass;
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
@ContextConfiguration(locations={"classpath*:**/test-config.xml", "classpath*:**/filepaths-config.xml"})
public class BerkeleyDbIdentifierAccessorTest {

	static String persistenceMgrClassName = "org.jhove2.config.spring.SpringBerkeleyDbPersistenceManagerFactory";
	static PersistenceManager persistenceManager = null;
	
	DROIDIdentifier bdbDROIDIdentifier;
	IdentifierModule bdbIdentifier;
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUp() throws Exception {
		PersistenceManagerUtil.createPersistenceManagerFactory(persistenceMgrClassName);
		persistenceManager = PersistenceManagerUtil.getPersistenceManagerFactory().getInstance();
		persistenceManager.initialize();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDown() throws Exception {
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



	/**
	 * Test method for {@link org.jhove2.persist.berkeleydpl.BerkeleyDbIdentifierAccessor#setFileSourceIdentifier(org.jhove2.module.identify.Identifier, org.jhove2.module.identify.SourceIdentifier)}.
	 */
	@Test
	public void testSetFileSourceIdentifier() {
		// Autowire will create bdbIdentifier (id 1) and bdbDROIDIdentifier(id 2) with parent IdentfierModule 1
		// and then will create new bdbDROIDIdentifier with id null and parent id null
		BerkeleyDbIdentifierAccessor accessor = (BerkeleyDbIdentifierAccessor) bdbIdentifier.getModuleAccessor();
		assertNull(bdbDROIDIdentifier.getModuleId());
		assertNotNull(bdbIdentifier.getModuleId());
		long id1 = bdbIdentifier.getModuleId().longValue();
		assertNull(bdbDROIDIdentifier.getParentIdentifierId());
		try {
			// this will "unlink" the first dbdDROIDIdentifier from parent, set moduleId for new dbdDroidIdentifer, and
			// set its parent to parentId
			bdbDROIDIdentifier = (DROIDIdentifier) accessor.setFileSourceIdentifier(getBdbIdentifier(), bdbDROIDIdentifier);
			assertEquals(id1, bdbIdentifier.getModuleId().longValue());
			assertEquals(bdbIdentifier.getModuleId(),bdbDROIDIdentifier.getParentIdentifierId()); 
			assertNotNull(bdbDROIDIdentifier.getModuleId());
			bdbDROIDIdentifier= (DROIDIdentifier) bdbDROIDIdentifier.getModuleAccessor().persistModule(bdbDROIDIdentifier);
			long id3 = bdbDROIDIdentifier.getModuleId().longValue();
			assertEquals (id1+2, id3);
			bdbIdentifier = (IdentifierModule) accessor.persistModule(bdbIdentifier);
			assertEquals(id1, bdbIdentifier.getModuleId().longValue());			
		} catch (JHOVE2Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/**
	 * Test method for {@link org.jhove2.persist.berkeleydpl.BerkeleyDbBaseModuleAccessor#persistModule(org.jhove2.module.Module)}.
	 */
	@Test
	public void testPersistModule() {
		try {
			IdentifierModule myIdModule = new IdentifierModule(new BerkeleyDbIdentifierAccessor());
			myIdModule = (IdentifierModule) myIdModule.getModuleAccessor().persistModule(myIdModule);
			assertNotNull(myIdModule.getModuleId());
			long modid1 = myIdModule.getModuleId().longValue();		
			myIdModule = (IdentifierModule) myIdModule.getModuleAccessor().persistModule(myIdModule);
			assertEquals(modid1, myIdModule.getModuleId().longValue());
			
			bdbIdentifier = (IdentifierModule) bdbIdentifier.getModuleAccessor().persistModule(bdbIdentifier);
			assertNotNull(bdbIdentifier.getModuleId());
			long modid2 = bdbIdentifier.getModuleId().longValue();
			bdbIdentifier = (IdentifierModule) bdbIdentifier.getModuleAccessor().persistModule(bdbIdentifier);
			assertEquals(modid2, bdbIdentifier.getModuleId().longValue());
			BerkeleyDbIdentifierAccessor accessor = (BerkeleyDbIdentifierAccessor) bdbIdentifier.getModuleAccessor();
			SourceIdentifier fsi = accessor.getFileSourceIdentifier(bdbIdentifier);
			fsi= new DROIDIdentifier(new BerkeleyDbBaseModuleAccessor());
			assertNull(fsi.getParentIdentifierId());
			assertNull(fsi.getModuleId());
			fsi = accessor.setFileSourceIdentifier(bdbIdentifier, fsi);
			assertEquals(bdbIdentifier.getModuleId(), fsi.getParentIdentifierId());
			assertNotNull(fsi.getModuleId());
			bdbIdentifier = (IdentifierModule) bdbIdentifier.getModuleAccessor().persistModule(bdbIdentifier);
			assertEquals(modid2, bdbIdentifier.getModuleId().longValue());
		} catch (JHOVE2Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}



	/**
	 * @return the bdbDROIDIdentifier
	 */
	public DROIDIdentifier getBdbDROIDIdentifier() {
		return bdbDROIDIdentifier;
	}


	/**
	 * @param bdbDROIDIdentifier the bdbDROIDIdentifier to set
	 */
	@Resource
	public void setBdbDROIDIdentifier(DROIDIdentifier bdbDROIDIdentifier) {
		this.bdbDROIDIdentifier = bdbDROIDIdentifier;
	}

	/**
	 * @return the bdbIdentifier
	 */
	public IdentifierModule getBdbIdentifier() {
		return bdbIdentifier;
	}

	/**
	 * @param bdbIdentifier the bdbIdentifier to set
	 */
	@Resource
	public void setBdbIdentifier(IdentifierModule bdbIdentifier) {
		this.bdbIdentifier = bdbIdentifier;
	}



}
