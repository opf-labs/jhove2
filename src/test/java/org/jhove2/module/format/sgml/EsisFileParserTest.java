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

import org.jhove2.app.util.FeatureConfigurationUtil;
import org.jhove2.core.Invocation;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.source.Source;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:**/test-config.xml", 
		"classpath*:**/filepaths-config.xml"})
public class EsisFileParserTest {

	protected String validEsisFileName;
	protected String emptyEsisFileName;
	protected String sgmlDirBasePath;
	protected String unrecognizedEsisFileName;
	protected String invalidEsisFileName;
	protected String validEsisFileName02;
	protected String validEsisFileName03;
	protected String samplesDirPath;
	protected String validEsisFilePath;
	protected JHOVE2 JHOVE2;
	protected SgmlModule testSgmlModule;
	private String validEsisFilePath02;
	private String validEsisFilePath03;
	private String emptyEsisFilePath;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testParseEsisFile() {
		try {
			samplesDirPath = 
				FeatureConfigurationUtil.getFilePathFromClasspath(sgmlDirBasePath, "sgml samples dir");
		} catch (JHOVE2Exception e1) {
			fail("Could not create base directory");
		}
		validEsisFilePath = samplesDirPath.concat(validEsisFileName);
		EsisFileParser parser = new EsisFileParser();
		Source inputSource = null;
		Invocation inv = JHOVE2.getInvocation();
		try {
			inputSource = JHOVE2.getSourceFactory().getSource(validEsisFilePath,
                    inv.getTempDirectoryFile(),
                    inv.getTempPrefix(),
                    inv.getTempSuffix(),
                    inv.getBufferSize());
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
			parser.parseEsisFile(validEsisFilePath, JHOVE2, inputSource, testSgmlModule);
			assertTrue(parser.isSgmlValid);  
		} catch (JHOVE2Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} 
		validEsisFilePath02 = samplesDirPath.concat(validEsisFileName02);
		parser = new EsisFileParser();
		try {
			inputSource = JHOVE2.getSourceFactory().getSource(validEsisFileName02,
                    inv.getTempDirectoryFile(),
                    inv.getTempPrefix(),
                    inv.getTempSuffix(),
                    inv.getBufferSize());
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
			parser.parseEsisFile(validEsisFilePath02, JHOVE2, inputSource, testSgmlModule);
			assertTrue(parser.isSgmlValid);  
			assertEquals(1, parser.sDataCount);
        	assertEquals(1, parser.publicIdCount);
        	assertEquals(1, parser.fileNamesCount);
        	assertEquals(1, parser.extTextEntCount);
        	assertEquals(2, parser.intDataEntCount);
        	assertTrue(testSgmlModule.getDocumentProperties().getSdataNames().contains("[percnt]"));
		} catch (JHOVE2Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} 
		validEsisFilePath03 = samplesDirPath.concat(validEsisFileName03);
		parser = new EsisFileParser();
		try {
			inputSource = JHOVE2.getSourceFactory().getSource(validEsisFileName03,
                    inv.getTempDirectoryFile(),
                    inv.getTempPrefix(),
                    inv.getTempSuffix(),
                    inv.getBufferSize());
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
			parser.parseEsisFile(validEsisFilePath03, JHOVE2, inputSource, testSgmlModule);
			assertTrue(parser.isSgmlValid);  
		} catch (JHOVE2Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} 	
		emptyEsisFilePath = samplesDirPath.concat(emptyEsisFileName);
		parser = new EsisFileParser();
		try {
			inputSource = JHOVE2.getSourceFactory().getSource(emptyEsisFilePath,
                    inv.getTempDirectoryFile(),
                    inv.getTempPrefix(),
                    inv.getTempSuffix(),
                    inv.getBufferSize());
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
			parser.parseEsisFile(emptyEsisFilePath, JHOVE2, inputSource, testSgmlModule);
			assertFalse(parser.isSgmlValid);  
		} catch (JHOVE2Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} 
		
	}


	/**
	 * @param validEsisFileName the validEsisFileName to set
	 */
	@Resource
	public void setValidEsisFileName(String validEsisFileName) {
		this.validEsisFileName = validEsisFileName;
	}
	/**
	 * @param emptyEsisFileName the emptyEsisFileName to set
	 */
	@Resource
	public void setEmptyEsisFileName(String emptyEsisFileName) {
		this.emptyEsisFileName = emptyEsisFileName;
	}
	
	/**
	 * @param sgmlDirBasePath the sgmlDirBasePath to set
	 */
	@Resource
	public void setSgmlDirBasePath(String sgmlDirBasePath) {
		this.sgmlDirBasePath = sgmlDirBasePath;
	}
	
	/**
	 * @param unrecognizedEsisFileName the unrecognizedEsisFileName to set
	 */
	@Resource
	public void setUnrecognizedEsisFileName(String unrecognizedEsisFileName) {
		this.unrecognizedEsisFileName = unrecognizedEsisFileName;
	}

	/**
	 * @param invalidEsisFileName the invalidEsisFileName to set
	 */
	@Resource
	public void setInvalidEsisFileName(String invalidEsisFileName) {
		this.invalidEsisFileName = invalidEsisFileName;
	}

	/**
	 * @param validEsisFileName02 the validEsisFileName02 to set
	 */
	@Resource
	public void setValidEsisFileName02(String validEsisFileName02) {
		this.validEsisFileName02 = validEsisFileName02;
	}


	/**
	 * @param validEsisFileName03 the validEsisFileName03 to set
	 */
	@Resource
	public void setValidEsisFileName03(String validEsisFileName03) {
		this.validEsisFileName03 = validEsisFileName03;
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
