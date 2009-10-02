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

import static org.junit.Assert.*;


import org.jhove2.core.JHOVE2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.annotation.Resource;

/**
 * @author Sheila Morrissey
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:**/test-config.xml"})
public class URLSourceTest {

	private String ptcUrlString = "http://portico.org/index.html";
	private String cdlUrlString = "http://www.cdlib.org/";
	private URL ptcURL;
	private URL cdlURL;
	private JHOVE2 jhove2;
	

	/**
	 * Test method for {@link org.jhove2.core.source.URLSource#equals(java.lang.Object)}.
	 */
	@Test
	public void testEqualsObject() {
		try {
			ptcURL = new URL(ptcUrlString);
			cdlURL = new URL(cdlUrlString);
			URLSource uPtc = new URLSource(jhove2.getAppConfigInfo().getTempPrefix(), 
					jhove2.getAppConfigInfo().getTempSuffix(), jhove2.getAppConfigInfo().getBufferSize(),
					ptcURL);
			URLSource uPtc2 = new URLSource(jhove2.getAppConfigInfo().getTempPrefix(), 
					jhove2.getAppConfigInfo().getTempSuffix(), jhove2.getAppConfigInfo().getBufferSize(), ptcURL);
			URLSource uCdl = new URLSource(jhove2.getAppConfigInfo().getTempPrefix(), 
					jhove2.getAppConfigInfo().getTempSuffix(), jhove2.getAppConfigInfo().getBufferSize(), cdlURL);
			assertEquals(uPtc,uPtc);
			// this will fail, because the temp file underlying each URL will be different
			assertFalse(uPtc.equals(uPtc2));
			assertFalse(uPtc2.equals(uPtc));			
			assertFalse(uPtc.equals(null));
			assertFalse(uPtc.equals(uCdl));
			ClumpSource clump = new ClumpSource();
			assertFalse(uPtc.equals(clump));
			ClumpSource clump2 = new ClumpSource();
			clump.addChildSource(uPtc);
			clump2.addChildSource(uPtc);
			assertEquals(clump, clump2);
			clump2.deleteChildSource(uPtc);
			clump2.addChildSource(uCdl);
			assertFalse(clump.equals(clump2));
		} catch (MalformedURLException e) {
			fail("Malformed URL " + e.getMessage());
		} catch (IOException e) {
			fail("IOException " + e.getMessage());
		}
		
	}

	/**
	 * Test method for {@link org.jhove2.core.source.URLSource#compareTo(org.jhove2.core.source.Source)}.
	 */
	@Test
	public void testCompareTo() {
		try {
			ptcURL = new URL(ptcUrlString);
			cdlURL = new URL(cdlUrlString);
			URLSource uPtc = new URLSource(jhove2.getAppConfigInfo().getTempPrefix(), 
					jhove2.getAppConfigInfo().getTempSuffix(), jhove2.getAppConfigInfo().getBufferSize(), ptcURL);
			URLSource uPtc2 = new URLSource(jhove2.getAppConfigInfo().getTempPrefix(), 
					jhove2.getAppConfigInfo().getTempSuffix(), jhove2.getAppConfigInfo().getBufferSize(), ptcURL);
			assertEquals(0,uPtc.compareTo(uPtc));
			assertEquals(1, uPtc.compareTo(null));
			boolean notEq = uPtc.compareTo(uPtc2)!=0;
			assertTrue(notEq);
			ClumpSource clump = new ClumpSource();
			assertEquals(1,uPtc.compareTo(clump));
		} catch (MalformedURLException e) {
			fail("Malformed URL " + e.getMessage());
		} catch (IOException e) {
			fail("IOException " + e.getMessage());
		}
	}

	public JHOVE2 getJhove2() {
		return jhove2;
	}
	@Resource
	public void setJhove2(JHOVE2 jhove2) {
		this.jhove2 = jhove2;
	}

}
