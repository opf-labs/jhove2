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

import java.util.ArrayList;
import java.util.List;


import org.jhove2.core.JHOVE2Exception;
import org.jhove2.module.aggrefy.AggrefierModule;
import org.jhove2.module.aggrefy.GlobPathRecognizer;
import org.jhove2.module.aggrefy.Recognizer;
import org.jhove2.persist.PersistenceManager;
import org.jhove2.persist.PersistenceManagerUtil;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
/**
 * @author smorrissey
 *
 */

public class BerkeleyDbAggrefierAccessorTest {
	
	static String persistenceMgrClassName = "org.jhove2.config.spring.SpringBerkeleyDbPersistenceManagerFactory";
	static PersistenceManager persistenceManager = null;
	
	GlobPathRecognizer bdbShapeFileRecognizer;
	AggrefierModule bdbAggrefierModule;
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
	 * Test method for {@link org.jhove2.persist.berkeleydpl.BerkeleyDbAggrefierAccessor#getRecognizers(org.jhove2.module.aggrefy.Aggrefier)}.
	 */
	@Test
	public void testGetRecognizers() {
		bdbAggrefierModule = new AggrefierModule(new BerkeleyDbAggrefierAccessor());
		assertNull(bdbAggrefierModule.getModuleId());
		BerkeleyDbAggrefierAccessor accessor = (BerkeleyDbAggrefierAccessor) bdbAggrefierModule.getModuleAccessor();
		try {
			List<Recognizer> recs = accessor.getRecognizers(bdbAggrefierModule);
			assertNotNull(bdbAggrefierModule.getModuleId());
			long modId1 = bdbAggrefierModule.getModuleId().longValue();
			assertEquals(0,recs.size());
			List<Recognizer> newRecs = new ArrayList<Recognizer>();
			bdbShapeFileRecognizer = new GlobPathRecognizer(new BerkeleyDbBaseModuleAccessor());
			assertNull(bdbShapeFileRecognizer.getModuleId());
			assertNull(bdbShapeFileRecognizer.getParentAggrefierId());
			newRecs.add(bdbShapeFileRecognizer);
			newRecs = accessor.setRecognizers(bdbAggrefierModule, newRecs);
			assertEquals(1, newRecs.size());
			bdbShapeFileRecognizer = (GlobPathRecognizer) newRecs.get(0);
			assertNotNull(bdbShapeFileRecognizer.getModuleId());
			long modId2 = bdbShapeFileRecognizer.getModuleId().longValue();
			assertEquals(modId1+1, modId2);
			assertEquals(modId1, bdbShapeFileRecognizer.getParentAggrefierId().longValue());	
			assertEquals(modId1, bdbAggrefierModule.getModuleId().longValue());
		} catch (JHOVE2Exception e) {
			e.printStackTrace();
			fail (e.getMessage());
		}
	}


	
	@Test
	public void testPersistModule() {
		bdbAggrefierModule = new AggrefierModule(new BerkeleyDbAggrefierAccessor());
		assertNull(bdbAggrefierModule.getModuleId());
		try {;
			bdbAggrefierModule   = (AggrefierModule)bdbAggrefierModule.getModuleAccessor().
				persistModule(bdbAggrefierModule);
			assertNotNull(bdbAggrefierModule.getModuleId());
			long modId3 = bdbAggrefierModule.getModuleId().longValue();
			bdbAggrefierModule   = (AggrefierModule)bdbAggrefierModule.getModuleAccessor().
			retrieveModule(bdbAggrefierModule.getModuleId());
		    assertEquals(modId3,bdbAggrefierModule.getModuleId().longValue());
		    
		    bdbAggrefierModule   = (AggrefierModule)bdbAggrefierModule.getModuleAccessor().
			persistModule(bdbAggrefierModule);
		    assertEquals(modId3,bdbAggrefierModule.getModuleId().longValue());
		} catch (JHOVE2Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/**
	 * @return the bdbShapeFileRecognizer
	 */
	public GlobPathRecognizer getBdbShapeFileRecognizer() {
		return bdbShapeFileRecognizer;
	}

	/**
	 * @return the bdbAggrefierModule
	 */
	public AggrefierModule getBdbAggrefierModule() {
		return bdbAggrefierModule;
	}


}
