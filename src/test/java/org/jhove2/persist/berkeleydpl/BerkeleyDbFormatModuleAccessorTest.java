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

import java.util.List;

import javax.annotation.Resource;

import org.jhove2.core.JHOVE2Exception;
import org.jhove2.module.format.FormatProfile;
import org.jhove2.module.format.utf8.UTF8Module;
import org.jhove2.module.format.utf8.ascii.ASCIIProfile;
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
		"classpath*:**/persist/berkeleydpl/bdb-test-config.xml",
		"classpath*:**/filepaths-config.xml"})
public class BerkeleyDbFormatModuleAccessorTest extends BerkeleyDbTestBase{

	UTF8Module bdbUTF8Module;
	ASCIIProfile bdbASCIIProfile;

	/**
	 * Test method for {@link org.jhove2.persist.berkeleydpl.BerkeleyDbFormatModuleAccessor#getProfiles(org.jhove2.module.format.FormatModule)}.
	 */
	@Test
	public void testGetProfiles() {
		try {
			List<FormatProfile> profiles = bdbUTF8Module.getProfiles();
			assertEquals (1,profiles.size());
		} catch (JHOVE2Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/**
	 * Test method for {@link org.jhove2.persist.berkeleydpl.BerkeleyDbFormatModuleAccessor#setProfiles(org.jhove2.module.format.FormatModule, java.util.List)}.
	 */
	@Test
	public void testSetProfiles() {
		List<FormatProfile> profiles;
		try {
			profiles = bdbUTF8Module.getProfiles();		
			assertEquals (1,profiles.size());
			bdbASCIIProfile = (ASCIIProfile) profiles.get(0);
			assertEquals(bdbUTF8Module.getModuleId(), bdbASCIIProfile.getFormatModuleId());
			BerkeleyDbFormatModuleAccessor accessor = (BerkeleyDbFormatModuleAccessor) bdbUTF8Module.getModuleAccessor();
			profiles = accessor.setProfiles(bdbUTF8Module, null);
			assertEquals (0,profiles.size());
			profiles.add(bdbASCIIProfile);
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
		Long oldId = bdbUTF8Module.getModuleId();
		try {
			bdbUTF8Module = (UTF8Module) bdbUTF8Module.getModuleAccessor().persistModule(bdbUTF8Module);
			assertEquals(oldId.longValue(),bdbUTF8Module.getModuleId().longValue());
			BerkeleyDbBaseModuleAccessor ma = (BerkeleyDbBaseModuleAccessor)bdbUTF8Module.getModuleAccessor();
			bdbUTF8Module= (UTF8Module) ma.retrieveModule(bdbUTF8Module.getModuleId());
			assertEquals(oldId.longValue(),bdbUTF8Module.getModuleId().longValue());
		} catch (JHOVE2Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
	}


	/**
	 * @return the bdbUTF8Module
	 */
	public UTF8Module getBdbUTF8Module() {
		return bdbUTF8Module;
	}

	/**
	 * @return the bdbASCIIProfile
	 */
	public ASCIIProfile getBdbASCIIProfile() {
		return bdbASCIIProfile;
	}

	/**
	 * @param bdbUTF8Module the bdbUTF8Module to set
	 */
	@Resource (name="bdbUTF8Module")
	public void setBdbUTF8Module(UTF8Module bdbUTF8Module) {
		this.bdbUTF8Module = bdbUTF8Module;
	}

	/**
	 * @param bdbASCIIProfile the bdbASCIIProfile to set
	 */
	@Resource (name="bdbASCIIProfile")
	public void setBdbASCIIProfile(ASCIIProfile bdbASCIIProfile) {
		this.bdbASCIIProfile = bdbASCIIProfile;
	}

}
