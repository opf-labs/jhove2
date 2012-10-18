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

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.annotation.Resource;

import org.jhove2.ConfigTestBase;
import org.jhove2.app.util.FeatureConfigurationUtil;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.source.Source;
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
		"classpath*:**/test-config.xml", 
		"classpath*:**/persist-test-config.xml",
		"classpath*:**/filepaths-config.xml"})
public class SgmlNormFileParserTest  extends ConfigTestBase{

	protected String normOutNoDoctype;
	protected String normOutPubid;
	protected String normOutSysid;
	protected String normOutZero;
	protected String sgmlDirBasePath;
	protected String samplesDirPath;
	protected JHOVE2 JHOVE2;
	protected SgmlModule testSgmlModule;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		try {
			samplesDirPath = 
				FeatureConfigurationUtil.getFilePathFromClasspath(sgmlDirBasePath, "sgml samples dir");
		} catch (JHOVE2Exception e1) {
			fail("Could not create base directory");
		}
	}

	/**
	 * Test method for {@link org.jhove2.module.format.sgml.SgmlNormFileParser#parseNormFile(java.lang.String, org.jhove2.core.JHOVE2, org.jhove2.core.source.Source, org.jhove2.module.format.sgml.SgmlModule)}.
	 */
	@Test
	public void testParseNormFile() {
		OpenSpWrapper sp = (OpenSpWrapper) testSgmlModule.getSgmlParser();
		SgmlNormFileParser parser = new SgmlNormFileParser();
		sp.setDoctypeParser(parser);
		Source inputSource = null;
		String normOutPubidPath = samplesDirPath.concat(normOutPubid);
		 try {
			 inputSource = JHOVE2.getSourceFactory().getSource(JHOVE2, normOutPubidPath);
		 } catch (FileNotFoundException e) {
			 e.printStackTrace();
			 fail(e.getMessage());
		 } catch (IOException e) {
			 e.printStackTrace();
			 fail(e.getMessage());
		 } catch (JHOVE2Exception e) {
			 e.printStackTrace();
			 fail(e.getMessage());
		 }
		 try {
			parser.parseNormFile(normOutPubidPath, JHOVE2, inputSource, testSgmlModule);
			SgmlDocumentProperties props = testSgmlModule.getDocumentProperties();
			assertTrue(props.foundDoctype);
			assertTrue(props.foundPubid);
			assertFalse(props.foundSysid);
			assertEquals(props.pubid, "-//ISO 99999:9999//DTD dtd0300//EN");
		} catch (JHOVE2Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	/**
	 * @param normOutNoDoctype the normOutNoDoctype to set
	 */
	@Resource
	public void setNormOutNoDoctype(String normOutNoDoctype) {
		this.normOutNoDoctype = normOutNoDoctype;
	}
	
	/**
	 * @param normOutPubid the normOutPubid to set
	 */
	@Resource
	public void setNormOutPubid(String normOutPubid) {
		this.normOutPubid = normOutPubid;
	}
	
	/**
	 * @param normOutSysid the normOutSysid to set
	 */
	@Resource
	public void setNormOutSysid(String normOutSysid) {
		this.normOutSysid = normOutSysid;
	}

	/**
	 * @param normOutZero the normOutZero to set
	 */
	@Resource
	public void setNormOutZero(String normOutZero) {
		this.normOutZero = normOutZero;
	}
	
	/**
	 * @param sgmlDirBasePath the sgmlDirBasePath to set
	 */
	@Resource
	public void setSgmlDirBasePath(String sgmlDirBasePath) {
		this.sgmlDirBasePath = sgmlDirBasePath;
	}
	
	 /**
	  * @param jHOVE2 the jHOVE2 to set
	  */
	 @Resource
	 public void setJHOVE2(JHOVE2 jHOVE2) {
		 JHOVE2 = jHOVE2;
	 }
	 /**
	  * @param testSgmlModule the testSgmlModule to set
	  */
	 @Resource
	 public void setTestSgmlModule(SgmlModule testSgmlModule) {
		 this.testSgmlModule = testSgmlModule;
	 }
}
