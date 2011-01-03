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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
 * @author Sheila Morrissey
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:**/test-config.xml", 
		"classpath*:**/DROIDId-test-config.xml", "classpath*:**/filepaths-config.xml"})
public class DroidIdentifierTest {

	private DROIDIdentifier testDroidIdentifier;
	private String zipPuid = "x-fmt/263";
	private String zipJhoveId = "http://jhove2.org/terms/format/zip";
	private String droidDirBasePath;
	private String sampleFile;
	private String sampleFilePUID;
	private String sampleBadFile;
	private JHOVE2 JHOVE2;
	private String xmlPuid = "fmt/101";
	private String xmlJhoveId = "http://jhove2.org/terms/format/xml";
	private String sampleNoFormatFile;
	private String sampleNoJhoveIdFile;

	/**
	 * Test method for {@link org.jhove2.module.identify.DROIDIdentifier#getPUIDtoJ2ID()}.
	 */
	@Test
	public void testGetPuidToJhoveId() {
		try {
			DROIDIdentifier.getPUIDtoJ2ID(JHOVE2);
			assertTrue(DROIDIdentifier.getPUIDtoJ2ID(JHOVE2).containsKey(zipPuid));
			assertEquals(zipJhoveId, DROIDIdentifier.getPUIDtoJ2ID(JHOVE2).get(zipPuid));
		} catch (JHOVE2Exception e) {
			fail("exception" + e.getMessage());
		}		
	}

	/**
	 * Test method for {@link org.jhove2.module.identify.DROIDIdentifier#identify(org.jhove2.core.JHOVE2, org.jhove2.core.source.Source, Input)}.
	 */
	@Test
	public void testIdentify() {
		FileSource source = null;
		Input      input  = null;
		Set<FormatIdentification> ids = null;
		String samplesDirPath = null;
		try {
			samplesDirPath = 
				FeatureConfigurationUtil.getFilePathFromClasspath(droidDirBasePath, "droid samples dir");
		} catch (JHOVE2Exception e1) {
			fail("Could not create base directory");
		}
		String zipFilePath = samplesDirPath.concat(sampleFile);
		try {
			source = (FileSource)JHOVE2.getSourceFactory().getSource(zipFilePath);
			input  = source.getInput(JHOVE2);
		} catch (Exception e) {
			fail("Couldn't create source: " + e.getMessage());
		} 
		try {
		    input.setPosition(0);
			ids = testDroidIdentifier.identify(JHOVE2, source, null);
			assertEquals(1, ids.size());
			for (FormatIdentification fi : ids){
				assertEquals(Confidence.PositiveSpecific, fi.getConfidence());
				assertEquals(zipPuid, fi.getNativeIdentifier().getValue());
				assertEquals(zipJhoveId, fi.getJHOVE2Identifier().getValue());
				assertEquals(0, fi.getMessages().size());
			}
			assertNull(testDroidIdentifier.getFileErrorMessage());
			assertNull(testDroidIdentifier.getFileNotIdentifiedMessage());
			assertNull(testDroidIdentifier.getFileNotRunMessage());
		} catch (Exception e) {
			fail("Couldn't identify: zip " + e.getMessage());
		} 
		String badFilePath = samplesDirPath.concat(sampleBadFile);
		try {
			source = (FileSource)JHOVE2.getSourceFactory().getSource(badFilePath);
			input  = source.getInput(JHOVE2);
		} catch (Exception e) {
			fail("Couldn't create source: " + e.getMessage());
		}
		try {
		    input.setPosition(0);
			ids = testDroidIdentifier.identify(JHOVE2, source, null);
			assertEquals(0, ids.size());
			assertNull(testDroidIdentifier.getFileErrorMessage());
			assertNull(testDroidIdentifier.getFileNotRunMessage());
			assertNotNull(testDroidIdentifier.getFileNotIdentifiedMessage());
		} catch (Exception e) {
			fail("Couldn't identify: bad file " + e.getMessage());
		} 
		testDroidIdentifier.setFileNotIdentifiedMessage(null);
		testDroidIdentifier.setFileErrorMessage(null);
		testDroidIdentifier.setFileNotRunMessage(null);
		String noJhoveFormatFilePath = samplesDirPath.concat(sampleNoJhoveIdFile);
		try {
			source = (FileSource)JHOVE2.getSourceFactory().getSource(noJhoveFormatFilePath);
			input  = source.getInput(JHOVE2);
		} catch (Exception e) {
			fail("Couldn't create source: " + e.getMessage());
		}
		try {
			input.setPosition(0);
			ids = testDroidIdentifier.identify(JHOVE2, source, null);
			assertEquals(1, ids.size());
			for (FormatIdentification fi : ids){
				assertEquals(Confidence.PositiveSpecific, fi.getConfidence());
				assertNotNull(fi.getJHOVE2Identifier());
			}
			assertNull(testDroidIdentifier.getFileErrorMessage());
			assertNull(testDroidIdentifier.getFileNotRunMessage());
			assertNull(testDroidIdentifier.getFileNotIdentifiedMessage());
		} catch (Exception e) {
			fail("Couldn't identify: bad file " + e.getMessage());
		} 
	}

	public DROIDIdentifier getTestDroidIdentifier() {
		return testDroidIdentifier;
	}
	@Resource
	public void setTestDroidIdentifier(DROIDIdentifier testDroidIdentifier) {
		this.testDroidIdentifier = testDroidIdentifier;
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

	public String getSampleFilePUID() {
		return sampleFilePUID;
	}
	@Resource
	public void setSampleFilePUID(String sampleFilePUID) {
		this.sampleFilePUID = sampleFilePUID;
	}

	public String getSampleBadFile() {
		return sampleBadFile;
	}
	@Resource
	public void setSampleBadFile(String sampleBadFile) {
		this.sampleBadFile = sampleBadFile;
	}


	public JHOVE2 getJHOVE2() {
		return JHOVE2;
	}
	@Resource
	public void setJHOVE2(JHOVE2 jHOVE2) {
		JHOVE2 = jHOVE2;
	}

	public String getXmlPuid() {
		return xmlPuid;
	}

	public void setXmlPuid(String xmlPuid) {
		this.xmlPuid = xmlPuid;
	}

	public String getXmlJhoveId() {
		return xmlJhoveId;
	}

	public void setXmlJhoveId(String xmlJhoveId) {
		this.xmlJhoveId = xmlJhoveId;
	}

	public String getSampleNoFormatFile() {
		return sampleNoFormatFile;
	}
	@Resource
	public void setSampleNoFormatFile(String sampleNoFormatFile) {
		this.sampleNoFormatFile = sampleNoFormatFile;
	}

	public String getSampleNoJhoveIdFile() {
		return sampleNoJhoveIdFile;
	}
	@Resource
	public void setSampleNoJhoveIdFile(String sampleNoJhoveIdFile) {
		this.sampleNoJhoveIdFile = sampleNoJhoveIdFile;
	}

}
