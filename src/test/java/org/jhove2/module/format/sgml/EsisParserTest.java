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

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CommonTokenStream;
import org.jhove2.app.util.FeatureConfigurationUtil;
import org.jhove2.core.JHOVE2Exception;
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
@ContextConfiguration(locations={"classpath*:**/test-config.xml", 
		"classpath*:**/filepaths-config.xml"})
public class EsisParserTest {

	public static final String TESTKEY = "testkey";
	protected HashMap<String,Integer> map;
	protected HashMap<String, List<String>> listMap;
	protected String validEsisFileName;
	protected String invalidEsisFileName;
	protected String sgmlDirBasePath;
	protected String validEsisFilePath;
	protected String invalidEsisFilePath;
	protected String samplesDirPath;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		map = new HashMap<String,Integer>();
		listMap = new HashMap<String, List<String>>();		
	}

	/**
	 * Test method for {@link org.jhove2.module.format.sgml.EsisParser#parseEsisFile(java.lang.String)}.
	 */
	@Test
	public void testParseEsisFile(){
		try {
			samplesDirPath = 
				FeatureConfigurationUtil.getFilePathFromClasspath(sgmlDirBasePath, "sgml samples dir");
		} catch (JHOVE2Exception e1) {
			fail("Could not create base directory");
		}
		validEsisFilePath = samplesDirPath.concat(validEsisFileName);
    	ESISCommandsLexer lex = null;
    	CommonTokenStream tokens = null;
    	ESISCommandsParser g = null;
		try {
			lex = new ESISCommandsLexer(new ANTLRFileStream(
					validEsisFilePath, 
					"UTF8"));
		} catch (IOException e1) {
			e1.printStackTrace();
			fail("Could not create base directory");
		}
        tokens = new CommonTokenStream(lex);
        g = new ESISCommandsParser(tokens);
        try {
        	g.esis();
        	assertTrue(g.isSgmlValid);
        	assertEquals(196, g.sDataCount);
        	assertEquals(16, g.publicIdCount);
        	assertEquals(16, g.fileNamesCount);
        	assertEquals(16, g.extTextEntCount);
        	assertEquals(2615, g.intDataEntCount);
        }
        catch (Exception e){
        	fail(e.getMessage());
        }
        invalidEsisFilePath = samplesDirPath.concat(invalidEsisFileName);
        try {
			lex = new ESISCommandsLexer(new ANTLRFileStream(
					invalidEsisFilePath, 
					"UTF8"));
		} catch (IOException e1) {
			e1.printStackTrace();
			fail("Could not create base directory");
		}
        tokens = new CommonTokenStream(lex);
        g = new ESISCommandsParser(tokens);
        try {
        	g.esis();
        	assertFalse(g.isSgmlValid);
        }
        catch (Exception e){
        	fail(e.getMessage());
        }
	}
	
	/**
	 * Test method for {@link org.jhove2.module.format.sgml.EsisParser#updateMapCounter(java.util.HashMap, java.lang.String)}.
	 */
	@Test
	public void testUpdateCounter() {
		EsisParser.updateMapCounter(map, TESTKEY);
		int testVal = map.get(TESTKEY);
		assertEquals(1, testVal);
		EsisParser.updateMapCounter(map, TESTKEY);
		testVal = map.get(TESTKEY);
		assertEquals(2, testVal);
	}

	/**
	 * Test method for {@link org.jhove2.module.format.sgml.EsisParser#updateMapList(java.util.HashMap, java.lang.String, java.lang.String )}.
	 */
	@Test
	public void testUpdateMapList() {
		EsisParser.updateMapList(listMap, TESTKEY, "item1");
		assertEquals(1, listMap.get(TESTKEY).size());
		EsisParser.updateMapList(listMap, TESTKEY, "item2");
		assertEquals(2, listMap.get(TESTKEY).size());
		List<String> itemList = listMap.get(TESTKEY);
		assertTrue(itemList.contains("item1"));
		assertTrue(itemList.contains("item2"));
	}

	/**
	 * @return the validEsisFileName
	 */
	public String getValidEsisFileName() {
		return validEsisFileName;
	}

	/**
	 * @param validEsisFileName the validEsisFileName to set
	 */
	@Resource
	public void setValidEsisFileName(String validEsisFileName) {
		this.validEsisFileName = validEsisFileName;
	}

	/**
	 * @return the invalidEsisFileName
	 */
	public String getInvalidEsisFileName() {
		return invalidEsisFileName;
	}

	/**
	 * @param invalidEsisFileName the invalidEsisFileName to set
	 */
	@Resource
	public void setInvalidEsisFileName(String invalidEsisFileName) {
		this.invalidEsisFileName = invalidEsisFileName;
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
	
}
