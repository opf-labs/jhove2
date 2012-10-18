/**
 * JHOVE2 - Next-generation architecture for format-aware characterization
 * <p>
 * Copyright (c) 2012 by The Regents of the University of California, Ithaka
 * Harbors, Inc., and The Board of Trustees of the Leland Stanford Junior
 * University. All rights reserved.
 * </p>
 * <p>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * </p>
 * <ul>
 * <li>Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.</li>
 * <li>Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.</li>
 * <li>Neither the name of the University of California/California Digital
 * Library, Ithaka Harbors/Portico, or Stanford University, nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.</li>
 * </ul>
 * <p>
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
 * </p>
 */
package org.jhove2;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;



import org.jhove2.config.spring.SpringConfigInfo;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Tool to enable setting of test Spring context 
 * This is necessary because creation of various classes from beans 
 * (for formats, modules created by commands, etc.) is determined by
 * context in SpringConfigInfo, and not by context set by a unit test's
 * ContextConfiguration annotation.
 * 
 * @see org.jhove2.config.spring.SpringConfigInfo
 * @author smorrissey
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
		"classpath*:**/persist-test-config.xml",
		"classpath*:**/test-config.xml"})
public class ConfigTestBase {

	protected static List<String> CONTEXT_PATHS;
	protected static final String[] DEFAULT_PATHS = {
		"classpath*:**/persist-test-config.xml",
		"classpath*:**/test-config.xml"};
	
	@BeforeClass 
	public static void setUpBeforeClass() throws Exception {
		List<String> paths = ConfigTestBase.getCONTEXT_PATHS();
		if (paths==null){
			paths = new ArrayList<String>();
			for (String path:DEFAULT_PATHS){
				paths.add(path);
			}
		}
		SpringConfigInfo.resetContext(paths);
	} 
	
	@AfterClass public static void tearDownAfterClass() throws Exception { 
		SpringConfigInfo.resetContext(null);
		ConfigTestBase.setCONTEXT_PATHS(null);
	}
    /**
     * Test method n     */
    @Test
    public void testTruth() {
        assertTrue(true);
    }

	/**
	 * @param cONTEXT_PATHS the cONTEXT_PATHS to set
	 */
	protected static synchronized void setCONTEXT_PATHS(List<String> cONTEXT_PATHS) {
		CONTEXT_PATHS = cONTEXT_PATHS;
	}
	/**
	 * @return the cONTEXT_PATHS
	 */
	protected static synchronized List<String> getCONTEXT_PATHS() {
		return CONTEXT_PATHS;
	}

}
