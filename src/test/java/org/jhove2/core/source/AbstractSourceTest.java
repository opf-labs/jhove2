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
package org.jhove2.core.source;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import javax.annotation.Resource;

import org.jhove2.ConfigTestBase;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.module.aggrefy.AggrefierModule;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author smorrissey
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
//"classpath*:**/abstractdisplayer-config.xml",
"classpath*:**/persist-test-config.xml",
"classpath*:**/core/test-config.xml", 
"classpath*:**/module/**/test-config.xml"}) 
//"classpath*:**/filepaths-config.xml"})
public class AbstractSourceTest extends ConfigTestBase{
	
	private AggrefierModule Aggrefier;
	private JHOVE2 JHOVE2;
	@Before
    public void setUp()
        throws Exception
    {
		AbstractSource.getModuleIDs().clear();
    }

	/**
	 * Test method for {@link org.jhove2.core.source.AbstractSource#equals(java.lang.Object)}.
	 */
	@Test
	public void testEqualsObject() {
		
		try {
			FileSetSource fsSource1 = JHOVE2.getSourceFactory().getFileSetSource(JHOVE2);
			assertEquals(fsSource1, fsSource1);
			assertFalse(fsSource1.equals(null));
			FileSetSource fsSource2 = JHOVE2.getSourceFactory().getFileSetSource(JHOVE2);
			assertEquals(fsSource2, fsSource2);
			assertFalse(fsSource2.equals(null));
			assertEquals(fsSource1,fsSource2);
			assertEquals(fsSource2,fsSource1);
			ClumpSource clump1;
			ClumpSource clump2;
			clump1 = JHOVE2.getSourceFactory().getClumpSource(JHOVE2);
			assertEquals(clump1, clump1);
			assertFalse(clump1.equals(null));
			assertFalse(clump1.equals(fsSource1));
			assertFalse(clump1.equals(fsSource2));
			assertFalse(fsSource1.equals(clump1));
			assertFalse(fsSource2.equals(clump1));
			clump2 = JHOVE2.getSourceFactory().getClumpSource(JHOVE2);
			assertEquals(clump2, clump2);
			assertFalse(clump2.equals(null));
			assertFalse(clump2.equals(fsSource1));
			assertFalse(clump2.equals(fsSource2));
			assertFalse(fsSource1.equals(clump2));
			assertFalse(fsSource2.equals(clump2));
			assertEquals(clump1, clump2);
			assertEquals(clump2, clump1);

			fsSource1=(FileSetSource) clump1.addChildSource(fsSource1);
			assertFalse(clump1.equals(fsSource1));
			assertFalse(clump1.equals(clump2));
			assertFalse(clump2.equals(clump1));
			fsSource2=(FileSetSource) clump2.addChildSource(fsSource2);
			assertEquals(clump1, clump2);

			JHOVE2=(JHOVE2) fsSource1.addModule(JHOVE2);
			assertFalse(fsSource1.equals(fsSource2));
			assertFalse(clump1.equals(clump2));
			JHOVE2=(JHOVE2) fsSource2.addModule(JHOVE2);
			// these will be false now; because fsSource1 has generic module(JHOVe2) attached to it
			assertFalse(fsSource1.equals(fsSource2));
			assertFalse(clump1.equals(clump2));
		} catch (JHOVE2Exception e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Test method for {@link org.jhove2.core.source.AbstractSource#compareTo(org.jhove2.core.source.Source)}.
	 */
	@Test
	public void testCompareTo() {

		try {
			FileSetSource fsSource1 =JHOVE2.getSourceFactory().getFileSetSource(JHOVE2);
			assertEquals(0,fsSource1.compareTo(fsSource1));
			assertEquals(1,fsSource1.compareTo(null));		
			FileSetSource fsSource2 = JHOVE2.getSourceFactory().getFileSetSource(JHOVE2);
			assertEquals(0,fsSource1.compareTo(fsSource2));		
			assertEquals(0,fsSource2.compareTo(fsSource1));
			ClumpSource clump1 = null;
			ClumpSource clump2 = null;
			try {
				clump1 = JHOVE2.getSourceFactory().getClumpSource(JHOVE2);
			} catch (JHOVE2Exception e1) {
				e1.printStackTrace();
				fail(e1.getMessage());
			}	
			try {
				clump2 = JHOVE2.getSourceFactory().getClumpSource(JHOVE2);
			} catch (JHOVE2Exception e1) {
				e1.printStackTrace();
				fail(e1.getMessage());
			}
			assertEquals(0, clump1.compareTo(clump1));
			assertEquals(0, clump1.compareTo(clump2));
			assertEquals(1, fsSource1.compareTo(clump1));
			assertEquals(-1, clump2.compareTo(fsSource2));

			fsSource1=(FileSetSource) clump1.addChildSource(fsSource1);

			assertEquals(1, clump1.compareTo(clump2));
			assertEquals(-1, clump2.compareTo(clump1));
			fsSource2=(FileSetSource) clump2.addChildSource(fsSource2);
			assertEquals(0, clump1.compareTo(clump2));

			JHOVE2=(JHOVE2) fsSource1.addModule(JHOVE2);
			assertEquals(1,fsSource1.compareTo(fsSource2));
			assertEquals(1,clump1.compareTo(clump2));
			JHOVE2=(JHOVE2) fsSource2.addModule(JHOVE2);
			assertEquals(-1,fsSource2.compareTo(fsSource1));
			assertEquals(1, clump1.compareTo(clump2));
			Aggrefier=(AggrefierModule) fsSource2.addModule(Aggrefier);
			assertEquals(1, fsSource1.compareTo(fsSource2));
			assertEquals(1,clump1.compareTo(clump2));
		} catch (JHOVE2Exception e) {
			fail(e.getMessage());
		}
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
	@Resource (name="JHOVE2")
	public void setJHOVE2(JHOVE2 jHOVE2) {
		JHOVE2 = jHOVE2;
	}

}
