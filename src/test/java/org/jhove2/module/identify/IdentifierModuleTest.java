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
package org.jhove2.module.identify;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.jhove2.app.util.FeatureConfigurationUtil;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.format.FormatIdentification;
import org.jhove2.core.format.FormatIdentification.Confidence;
import org.jhove2.core.io.Input;
import org.jhove2.core.source.FileSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Note that you must set the jhove2.home environment variable in order to run this
 * test successfully
 * @author smorrissey
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:**/identifier-config.xml",
		"classpath*:**/test-config.xml", "classpath*:**/filepaths-config.xml"})
public class IdentifierModuleTest {
	
	private JHOVE2 JHOVE2;
	private IdentifierModule identifier;
	private String droidDirBasePath;
	private String sampleFile;
	private String shapeDirBasePath;
	private List<String> testFileList;
	private String zipPuid = "x-fmt/263";
	private String zipJhoveId = "http://jhove2.org/terms/format/zip";
	/**
	 * Test method for {@link org.jhove2.module.identify.IdentifierModule#identify(org.jhove2.core.JHOVE2, org.jhove2.core.source.Source)}.
	 */
	@Test
	public void testIdentify() {
		FileSource source = null;
		Input      input  = null;
		Set<FormatIdentification> ids = null;
		String droidDirPath = null;
		try {
			droidDirPath = 
				FeatureConfigurationUtil.getFilePathFromClasspath(droidDirBasePath, "droid dir");
		} catch (JHOVE2Exception e1) {
			fail("Could not create base directory");
		}
		String zipFilePath = droidDirPath.concat(sampleFile);
		try {
			source = new FileSource(zipFilePath);
			input  = source.getInput(JHOVE2);
		} catch (Exception e) {
			fail("Couldn't create source: " + e.getMessage());
		} 
		try {
		    input.setPosition(0);
			ids = identifier.identify(JHOVE2, source, input);
			assertEquals(1, ids.size());
			for (FormatIdentification fi : ids){
				assertEquals(Confidence.PositiveSpecific, fi.getConfidence());
				assertEquals(zipPuid, fi.getNativeIdentifier().getValue());
				assertEquals(zipJhoveId, fi.getJHOVE2Identifier().getValue());
			}

		} catch (Exception e) {
			fail("Couldn't identify: zip " + e.getMessage());
		} 
		for (String testFile:testFileList){
			ids.clear();
			String shapeDirPath = null;
			try {
				shapeDirPath = 
					FeatureConfigurationUtil.getFilePathFromClasspath(shapeDirBasePath, "shapefile dir");
			}
			catch (Exception e){
				fail("unable to find shapefile sample dir");
			}
			String testFilePath = shapeDirPath.concat(testFile);
			try {
				source = new FileSource(testFilePath);
			} catch (Exception e) {
				fail("Couldn't create source: " + e.getMessage());
			} 
			try {
			    input.setPosition(0);
				ids = identifier.identify(JHOVE2, source, input);
			}
			catch (Exception ex){
				fail(ex.getMessage());
			}
		}	
	
	}
	public JHOVE2 getJHOVE2() {
		return JHOVE2;
	}
	@Resource (name="JHOVE2")
	public void setJHOVE2(JHOVE2 jHOVE2) {
		JHOVE2 = jHOVE2;
	}
	public IdentifierModule getIdentifier() {
		return identifier;
	}
	@Resource(name="testIdentifier")
	public void setIdentifier(IdentifierModule testIdentifier) {
		this.identifier = testIdentifier;
	}
	public String getDroidDirBasePath() {
		return droidDirBasePath;
	}
	@Resource
	public void setDroidDirBasePath(String samplesDirPath) {
		this.droidDirBasePath = samplesDirPath;
	}
	public String getSampleFile() {
		return sampleFile;
	}
	@Resource
	public void setSampleFile(String sampleFile) {
		this.sampleFile = sampleFile;
	}
	public String getZipPuid() {
		return zipPuid;
	}
	public void setZipPuid(String zipPuid) {
		this.zipPuid = zipPuid;
	}
	public String getZipJhoveId() {
		return zipJhoveId;
	}
	public void setZipJhoveId(String zipJhoveId) {
		this.zipJhoveId = zipJhoveId;
	}
	public String getShapeDirBasePath() {
		return shapeDirBasePath;
	}
	@Resource
	public void setShapeDirBasePath(String testShapeDirPath) {
		this.shapeDirBasePath = testShapeDirPath;
	}
	public List<String> getTestFileList() {
		return testFileList;
	}
	@Resource
	public void setTestFileList(List<String> testFileList) {
		this.testFileList = testFileList;
	}

}
