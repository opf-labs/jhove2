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
package org.jhove2.module.format.sgml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import javax.annotation.Resource;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.jhove2.app.util.FeatureConfigurationUtil;
import org.jhove2.core.JHOVE2Exception;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:**/test-config.xml", 
		"classpath*:**/filepaths-config.xml"})

public class DoctypeFinderParserTest {

	protected String sgmlDirBasePath;
	protected String normOutPubid;
	protected String normOutSysid;
	protected String normOutNoDoctype;
	protected String normOutZero;
	
	protected String samplesDirPath;
	
	@Before
	public void setUp() throws Exception {
		try {
			samplesDirPath = 
				FeatureConfigurationUtil.getFilePathFromClasspath(sgmlDirBasePath, "sgml samples dir");
		} catch (JHOVE2Exception e1) {
			fail("Could not create base directory");
		}
	}

	@Test
	public void testNormdoc() {
		DoctypeFinderLexer lex = null;
    	CommonTokenStream tokens = null;
    	DoctypeFinderParser g = null;
    	String normOutPubidPath = samplesDirPath.concat(normOutPubid);
    	try {
			lex = new DoctypeFinderLexer(new ANTLRFileStream(
					normOutPubidPath, 
						"UTF8"));
		} catch (IOException e) {
			e.printStackTrace();
			fail("Could not create lexer");
		}
		tokens = new CommonTokenStream(lex);
        g = new DoctypeFinderParser(tokens);
        try {
			g.normdoc();
			assertTrue(g.foundDoctype);
			assertTrue(g.foundPubid);
			assertFalse(g.foundSysid);
			assertEquals(g.pubid, "\"-//ISO 99999:9999//DTD dtd0300//EN\"");
		} catch (RecognitionException e) {
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

	/**
	 * @return the normOutNoDoctype
	 */
	public String getNormOutNoDoctype() {
		return normOutNoDoctype;
	}

	/**
	 * @param normOutNoDoctype the normOutNoDoctype to set
	 */
	@Resource
	public void setNormOutNoDoctype(String normOutNoDoctype) {
		this.normOutNoDoctype = normOutNoDoctype;
	}

	/**
	 * @return the normOutPubid
	 */
	public String getNormOutPubid() {
		return normOutPubid;
	}

	/**
	 * @param normOutPubid the normOutPubid to set
	 */
	@Resource
	public void setNormOutPubid(String normOutPubid) {
		this.normOutPubid = normOutPubid;
	}

	/**
	 * @return the normOutSysid
	 */
	public String getNormOutSysid() {
		return normOutSysid;
	}

	/**
	 * @param normOutSysid the normOutSysid to set
	 */
	@Resource
	public void setNormOutSysid(String normOutSysid) {
		this.normOutSysid = normOutSysid;
	}

	/**
	 * @return the normOutZero
	 */
	public String getNormOutZero() {
		return normOutZero;
	}

	/**
	 * @param normOutZero the normOutZero to set
	 */
	@Resource
	public void setNormOutZero(String normOutZero) {
		this.normOutZero = normOutZero;
	}

}
