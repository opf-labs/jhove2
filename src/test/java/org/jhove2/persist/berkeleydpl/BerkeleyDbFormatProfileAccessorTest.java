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
import org.jhove2.module.format.FormatModule;
import org.jhove2.module.format.utf8.UTF8Module;
import org.jhove2.module.format.utf8.ascii.ASCIIProfile;
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
public class BerkeleyDbFormatProfileAccessorTest {

	
	static String persistenceMgrClassName = "org.jhove2.config.spring.SpringBerkeleyDbPersistenceManagerFactory";
	static PersistenceManager persistenceManager = null;
	
	UTF8Module bdbUTF8Module;
	ASCIIProfile bdbASCIIProfile;
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		PersistenceManagerUtil.createPersistenceManagerFactory(persistenceMgrClassName);
		persistenceManager = PersistenceManagerUtil.getPersistenceManagerFactory().getInstance();
		persistenceManager.initialize();
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

	/**
	 * Test method for {@link org.jhove2.persist.berkeleydpl.BerkeleyDbFormatProfileAccessor#setFormatModule(org.jhove2.module.format.FormatProfile, org.jhove2.module.format.FormatModule)}.
	 */
	@Test
	public void testSetFormatModule() {
		assertNull(bdbASCIIProfile.getFormatModuleId());
		BerkeleyDbFormatProfileAccessor accessor = (BerkeleyDbFormatProfileAccessor) bdbASCIIProfile.getModuleAccessor();
		try {
			bdbASCIIProfile = (ASCIIProfile) accessor.setFormatModule(bdbASCIIProfile, bdbUTF8Module);
			assertEquals(bdbASCIIProfile.getFormatModuleId(), bdbUTF8Module.getModuleId());
			FormatModule fm = accessor.getFormatModule(bdbASCIIProfile);
			assertEquals(fm.getModuleId(), bdbUTF8Module.getModuleId());
		} catch (JHOVE2Exception e) {
			e.printStackTrace();
			fail (e.getMessage());
		}	
	}

	/**
	 * Test method for {@link org.jhove2.persist.berkeleydpl.BerkeleyDbBaseModuleAccessor#persistModule(org.jhove2.module.Module)}.
	 */
	@Test
	public void testPersistModule() {
		assertNull(bdbASCIIProfile.getFormatModuleId());
		try {
			bdbASCIIProfile = (ASCIIProfile) bdbASCIIProfile.getModuleAccessor().persistModule(bdbASCIIProfile);
			assertNotNull(bdbASCIIProfile.getModuleId());
		} catch (JHOVE2Exception e) {
			e.printStackTrace();
			fail (e.getMessage());
		}
		
	}
	/**
	 * @param bdbUTF8Module the bdbUTF8Module to set
	 */
	@Resource
	public void setBdbUTF8Module(UTF8Module bdbUTF8Module) {
		this.bdbUTF8Module = bdbUTF8Module;
	}

	/**
	 * @param bdbASCIIProfile the bdbASCIIProfile to set
	 */
	@Resource
	public void setBdbASCIIProfile(ASCIIProfile bdbASCIIProfile) {
		this.bdbASCIIProfile = bdbASCIIProfile;
	}

}
