/**
 * JHOVE2 - Next-generation architecture for format-aware characterization
 * <p>
 * Copyright (c) 2010 by The Regents of the University of California, Ithaka
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
package org.jhove2.module.format.sgml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import javax.annotation.Resource;

import org.jhove2.app.util.FeatureConfigurationUtil;
import org.jhove2.core.Invocation;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.source.Source;
import org.jhove2.module.format.Validator.Validity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Class to test wrapper around OpenSP opensgml tool
 * 
 * THIS CLASS REQUIRES ONSGMLS TO BE INSTALLED TO RUN SUCCESSFULLY!!!
 * The path to ONSGMLS is specified the sgml/test-config.xml file
 * in the shellEnv property of the testExternalProcessHandler bean
 * 
 * @author smorrissey
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:**/test-config.xml", 
"classpath*:**/filepaths-config.xml"})

public class OpenSpWrapperTest {
	protected JHOVE2 JHOVE2;
	protected SgmlModule testSgmlModule;
	protected SgmlModule testSgmlModule02;
	protected String catalogFile;
	protected String validSgmlFile;
	protected String sgmlDirBasePath;
	protected String invalidSgmlFile;
	
	protected String sgmlDirPath;
	protected String catalogPath;
	protected Source inputSource;
	protected OpenSpWrapper sp;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

		sp = (OpenSpWrapper) testSgmlModule.sgmlParser;
		try {
			sgmlDirPath = 
				FeatureConfigurationUtil.getFilePathFromClasspath(sgmlDirBasePath, "temp dir");
		} catch (JHOVE2Exception e1) {
			fail("Could not create base directory");
		}
		File fsgml = new File(sgmlDirPath);
		sgmlDirPath = fsgml.getPath();
		if (sp.filepathFilter != null){
			sgmlDirPath = sp.filepathFilter.filter(sgmlDirPath);
		}
		catalogPath = sgmlDirPath.concat(catalogFile);
		File cFile = new File (catalogPath);
		catalogPath = cFile.getAbsolutePath();
		if (sp.filepathFilter != null){
			catalogPath = sp.filepathFilter.filter(catalogPath);
		}
		sp.getOnsgmlsOptions().setCatalogPath(catalogPath);
		sp.getSgmlnormOptions().setCatalogPath(catalogPath);

	}

	/**
	 * Test method for {@link org.jhove2.module.format.sgml.OpenSpWrapper#parseFile(org.jhove2.module.format.sgml.SgmlModule, JHOVE2, Source)}.
	 */
	@Test
	public void testParseFile() {
		String goodFilePath = sgmlDirBasePath.concat(validSgmlFile);
		try {
			goodFilePath = 
				FeatureConfigurationUtil.getFilePathFromClasspath(goodFilePath, 
						"valid sgm file");
		} catch (JHOVE2Exception e1) {
			fail("Could not create base directory");
		}
		File fGoodFile = new File(goodFilePath);
		goodFilePath = fGoodFile.getPath();
		try {
		    Invocation inv = JHOVE2.getInvocation();
			inputSource = JHOVE2.getSourceFactory().getSource(goodFilePath,
                    inv.getTempDirectoryFile(),
                    inv.getTempPrefix(),
                    inv.getTempSuffix(),
                    inv.getBufferSize());
		}catch (Exception e){
			e.printStackTrace();
			fail("Failed to create source for input file");
		}

		try {
			testSgmlModule.setDocumentProperties(sp.parseFile(testSgmlModule, JHOVE2, inputSource));
		} catch (JHOVE2Exception e) {
			e.printStackTrace();
			fail("unable to get esis parser");
		}
		assertTrue(testSgmlModule.getDocumentProperties().isSgmlValid());
	}

	/**
	 * Test method for {@link org.jhove2.module.format.sgml.OpenSpWrapper#parseFile(org.jhove2.module.format.sgml.SgmlModule, JHOVE2, Source)}.
	 */
	@Test
	public void testParseFile02() {
		sp = (OpenSpWrapper) testSgmlModule02.sgmlParser;
		try {
			sgmlDirPath = 
				FeatureConfigurationUtil.getFilePathFromClasspath(sgmlDirBasePath, "temp dir");
		} catch (JHOVE2Exception e1) {
			fail("Could not create base directory");
		}
		File fsgml = new File(sgmlDirPath);
		sgmlDirPath = fsgml.getPath();
		if (sp.filepathFilter != null){
			sgmlDirPath = sp.filepathFilter.filter(sgmlDirPath);
		}
		catalogPath = sgmlDirPath.concat(catalogFile);
		File cFile = new File (catalogPath);
		catalogPath = cFile.getAbsolutePath();
		if (sp.filepathFilter != null){
			catalogPath = sp.filepathFilter.filter(catalogPath);
		}
		sp.getOnsgmlsOptions().setCatalogPath(catalogPath);
		sp.getSgmlnormOptions().setCatalogPath(catalogPath);
		testSgmlModule02.setDocumentProperties(null);
		String badFilePath = sgmlDirBasePath.concat(invalidSgmlFile);
		try {
			badFilePath = 
				FeatureConfigurationUtil.getFilePathFromClasspath(badFilePath, 
						"invalid sgm file");
		} catch (JHOVE2Exception e1) {
			fail("Could not create base directory");
		}
		File fBadFile = new File(badFilePath);
		badFilePath = fBadFile.getPath();
		try {
		    Invocation inv = JHOVE2.getInvocation();
			inputSource = JHOVE2.getSourceFactory().getSource(badFilePath,
                    inv.getTempDirectoryFile(),
                    inv.getTempPrefix(),
                    inv.getTempSuffix(),
                    inv.getBufferSize());
		}catch (Exception e){
			e.printStackTrace();
			fail("Failed to create source for input file");
		}

		try {
			testSgmlModule02.setDocumentProperties(sp.parseFile(testSgmlModule02, JHOVE2, inputSource));
		} catch (JHOVE2Exception e) {
			e.printStackTrace();
			fail("unable to get esis parser");
		}
		assertFalse(testSgmlModule02.getDocumentProperties().isSgmlValid());	
	}

	/**
	 * Test method for {@link org.jhove2.module.format.sgml.OpenSpWrapper#parseFile(org.jhove2.module.format.sgml.SgmlModule, JHOVE2, Source)}.
	 */
	@Test
	public void testParseFile03() {
		
		String goodFilePath = sgmlDirBasePath.concat(validSgmlFile);
		File fGoodFile = new File(goodFilePath);
		try {
			goodFilePath = 
				FeatureConfigurationUtil.getFilePathFromClasspath(goodFilePath, 
						"valid sgm file");
		} catch (JHOVE2Exception e1) {
			fail("Could not create base directory");
		}
		// now alter path to opensp; should cause error message and null sgml properties,
		// even with good sgml file

		testSgmlModule.setDocumentProperties(null);
		fGoodFile = new File(goodFilePath);
		goodFilePath = fGoodFile.getPath();
		try {
		    Invocation inv = JHOVE2.getInvocation();
			inputSource = JHOVE2.getSourceFactory().getSource(goodFilePath,
                    inv.getTempDirectoryFile(),
                    inv.getTempPrefix(),
                    inv.getTempSuffix(),
                    inv.getBufferSize());
		}
		catch (Exception e){
			e.printStackTrace();
			fail("Failed to create source for input file");
		}	
		String oldPath = sp.getOnsgmlsPath();
		sp.setOnsgmlsPath("/invalid/path/ongmls");
		int oldMessageLength = testSgmlModule.getSgmlParserErrorMessages().size();
		try {
			testSgmlModule.setDocumentProperties(sp.parseFile(testSgmlModule, JHOVE2, inputSource));
		} catch (JHOVE2Exception e) {
			e.printStackTrace();
			fail("unable to get esis parser");
		}
		sp.setOnsgmlsPath(oldPath);
		assertNotNull(testSgmlModule.getDocumentProperties());
		try {
			assertEquals(Validity.Undetermined, testSgmlModule.validate(JHOVE2, inputSource, null));
		} catch (JHOVE2Exception e) {
			fail("sgml module Validate method threw exception " + e.getMessage());
			e.printStackTrace();
		}
		assertEquals(oldMessageLength+1, testSgmlModule.getSgmlParserErrorMessages().size());
	}

	
	/**
	 * Test method for {@link org.jhove2.module.format.sgml.OpenSpWrapper#parseSgmlFile(org.jhove2.module.format.sgml.SgmlModule)}.
	 */
	@Test
	public void testParseSgmlFile() {
		String goodFilePath = sgmlDirBasePath.concat(validSgmlFile);
		try {
			goodFilePath = 
				FeatureConfigurationUtil.getFilePathFromClasspath(goodFilePath, "valid sgm file");
		} catch (JHOVE2Exception e1) {
			fail("Could not create base directory");
		}
		File fGoodFile = new File(goodFilePath);
		goodFilePath = fGoodFile.getPath();
		try {
		    Invocation inv = JHOVE2.getInvocation();
			inputSource = JHOVE2.getSourceFactory().getSource(goodFilePath,
                    inv.getTempDirectoryFile(),
                    inv.getTempPrefix(),
                    inv.getTempSuffix(),
                    inv.getBufferSize());
		}catch (Exception e){
			e.printStackTrace();
			fail("Failed to create source for input file");
		}

		String[] outputFiles = null;
		try {
			outputFiles = sp.parseSgmlFile(
					JHOVE2,inputSource,sp.ESIS_SUFFIX,sp.onsgmlsPath, sp.getOnsgmlsOptions().getOptionString(), testSgmlModule);
		} catch (JHOVE2Exception e) {
			e.printStackTrace();
			fail("Failed to parse sgml file");
		}
		assertEquals(2, outputFiles.length);
		File goodEsisFile = new File(outputFiles[0]);
		assertTrue(goodEsisFile.exists());
		long size = goodEsisFile.length();
		assertTrue(size>0);
		File errEsisFile = new File(outputFiles[1]);
		assertTrue(errEsisFile.exists());
		size = errEsisFile.length();
		assertEquals(0,size);
	}

	/**
	 * Test method for {@link org.jhove2.module.format.sgml.OpenSpWrapper#createDoctype(org.jhove2.module.format.sgml.SgmlModule)}.
	 */
	@Test
	public void testCreateDoctype() {
		String goodFilePath = sgmlDirBasePath.concat(validSgmlFile);
		try {
			goodFilePath = 
				FeatureConfigurationUtil.getFilePathFromClasspath(goodFilePath, 
						"valid sgm file");
		} catch (JHOVE2Exception e1) {
			fail("Could not create base directory");
		}
		File fGoodFile = new File(goodFilePath);
		goodFilePath = fGoodFile.getPath();
		try {
		    Invocation inv = JHOVE2.getInvocation();
			inputSource = JHOVE2.getSourceFactory().getSource(goodFilePath,
                    inv.getTempDirectoryFile(),
                    inv.getTempPrefix(),
                    inv.getTempSuffix(),
                    inv.getBufferSize());
		}catch (Exception e){
			e.printStackTrace();
			fail("Failed to create source for input file");
		}

		testSgmlModule.setDocumentProperties(new SgmlDocumentProperties());
		try {
			sp.determineDoctype(testSgmlModule, JHOVE2, inputSource);
		} catch (JHOVE2Exception e) {
			e.printStackTrace();
			fail("Failed to run createDoctype method");
		}
		assertTrue(testSgmlModule.getDocumentProperties().getDocTypeFound());
	}

	/**
	 * @return the jHOVE2
	 */
	public JHOVE2 getJHOVE2() {
		return JHOVE2;
	}

	/**
	 * @param jHOVE2 the jHOVE2 to set
	 */
	@Resource
	public void setJHOVE2(JHOVE2 jHOVE2) {
		JHOVE2 = jHOVE2;
	}

	/**
	 * @return the testSgmlModule
	 */
	public SgmlModule getTestSgmlModule() {
		return testSgmlModule;
	}

	/**
	 * @param testSgmlModule the testSgmlModule to set
	 */
	@Resource
	public void setTestSgmlModule(SgmlModule testSgmlModule) {
		this.testSgmlModule = testSgmlModule;
	}

	/**
	 * @return the catalogFile
	 */
	public String getCatalogFile() {
		return catalogFile;
	}

	/**
	 * @param catalogFile the catalogFile to set
	 */
	@Resource
	public void setCatalogFile(String catalogFile) {
		this.catalogFile = catalogFile;
	}

	/**
	 * @return the validSgmlFile
	 */
	public String getValidSgmlFile() {
		return validSgmlFile;
	}

	/**
	 * @param validSgmlFile the validSgmlFile to set
	 */
	@Resource
	public void setValidSgmlFile(String validSgmlFile) {
		this.validSgmlFile = validSgmlFile;
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
	 * @return the invalidSgmlFile
	 */
	public String getInvalidSgmlFile() {
		return invalidSgmlFile;
	}

	/**
	 * @param invalidSgmlFile the invalidSgmlFile to set
	 */
	@Resource
	public void setInvalidSgmlFile(String invalidSgmlFile) {
		this.invalidSgmlFile = invalidSgmlFile;
	}

	/**
	 * @param testSgmlModule02 the testSgmlModule02 to set
	 */
	@Resource(name="testSgmlModule02")
	public void setTestSgmlModule02(SgmlModule testSgmlModule02) {
		this.testSgmlModule02 = testSgmlModule02;
	}

}
