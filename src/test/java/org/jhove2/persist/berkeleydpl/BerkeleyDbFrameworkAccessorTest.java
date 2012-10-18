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

import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.module.Command;
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
		"classpath*:**/filepaths-config.xml"
})
public class BerkeleyDbFrameworkAccessorTest extends BerkeleyDbTestBase{
	
	JHOVE2 bdbJHOVE2;
	JHOVE2 localJHOVE2;
	List<Command> bdbCommands;
	
	/**
	 * Test method for {@link org.jhove2.persist.berkeleydpl.BerkeleyDbBaseModuleAccessor#persistModule(org.jhove2.module.Module)}.
	 */
	@Test
	public void testPersistModule() {
		try {
			localJHOVE2 = new JHOVE2(new BerkeleyDbFrameworkAccessor());
			assertNull(localJHOVE2.getModuleId());
			localJHOVE2 = (JHOVE2) localJHOVE2.getModuleAccessor().persistModule(localJHOVE2);
			assertNotNull(localJHOVE2.getModuleId());
		} catch (JHOVE2Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link org.jhove2.persist.berkeleydpl.BerkeleyDbFrameworkAccessor#getCommands(org.jhove2.core.JHOVE2)}.
	 */
	@Test
	public void testGetCommands() {
		try {
			BerkeleyDbFrameworkAccessor accessor = new BerkeleyDbFrameworkAccessor();
			localJHOVE2 = new JHOVE2(accessor);
			List<Command> commands = accessor.getCommands(localJHOVE2);
			assertEquals(0,commands.size());
			assertNotNull(localJHOVE2.getModuleId());
			long localId = localJHOVE2.getModuleId().longValue();
			localJHOVE2 = (JHOVE2) localJHOVE2.getModuleAccessor().persistModule(localJHOVE2);
			assertEquals(localId, localJHOVE2.getModuleId().longValue());
			
		} catch (JHOVE2Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link org.jhove2.persist.berkeleydpl.BerkeleyDbFrameworkAccessor#setCommands(org.jhove2.core.JHOVE2, java.util.List)}.
	 */
	@Test
	public void testSetCommands() {
		assertNotNull(bdbJHOVE2.getModuleId());
		long longVal =  bdbJHOVE2.getModuleId().longValue();
		BerkeleyDbFrameworkAccessor accessor = (BerkeleyDbFrameworkAccessor) bdbJHOVE2.getModuleAccessor();
		try {
			List<Command> commands = accessor.getCommands(bdbJHOVE2);
			assertEquals(3, commands.size());
			bdbJHOVE2 = (JHOVE2) bdbJHOVE2.getModuleAccessor().persistModule(bdbJHOVE2);
			assertNotNull(bdbJHOVE2.getModuleId());
			longVal =  bdbJHOVE2.getModuleId().longValue();
			List<Command> newCommands  = accessor.setCommands(bdbJHOVE2, null);
			assertEquals(0, newCommands.size());
			for (Command command:commands){
				assertEquals(longVal, command.getJhove2ModuleId().longValue());
				BerkeleyDbCommandAccessor ma = (BerkeleyDbCommandAccessor)command.getModuleAccessor();
				command = (Command) ma.retrieveModule(command.getModuleId());
				assertNull(command.getJhove2ModuleId());
				
			}
		} catch (JHOVE2Exception e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * @param bdbJHOVE2 the bdbJHOVE2 to set
	 */
	@Resource
	public void setBdbJHOVE2(JHOVE2 bdbJHOVE2) {
		this.bdbJHOVE2 = bdbJHOVE2;
	}


}
