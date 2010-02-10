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

import static org.junit.Assert.*;

import java.util.Set;

import javax.annotation.Resource;

import org.jhove2.core.FormatIdentification;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.FormatIdentification.Confidence;
import org.jhove2.core.source.FileSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Note that you must set the jhove2.home environment variable in order to run this
 * test successfully
 * @author Sheila Morrissey
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:**/test-config.xml", 
		"classpath*:**/DROIDId-test-config.xml"})
public class DroidIdentifierTest {

	private DROIDIdentifier dROIDIdentifier;
	private String zipPuid = "x-fmt/263";
	private String zipJhoveId = "http://jhove2.org/terms/format/zip";
	private String samplesDirPath;
	private String sampleFile;
	private String sampleFilePUID;
	private String sampleBadFile;
	private JHOVE2 JHOVE2;
	private String xmlPuid = "fmt/101";
	private String xmlJhoveId = "http://jhove2.org/terms/format/xml";
	private String sampleNoFormatFile;
	private String sampleNoJhoveIdFile;



	/**
	 * Test method for {@link org.jhove2.module.identify.DROIDIdentifier#getPuidToJhoveId()}.
	 */
	@Test
	public void testGetPuidToJhoveId() {
		try {
			DROIDIdentifier.getPuidToJhoveId();
			assertTrue(DROIDIdentifier.getPuidToJhoveId().containsKey(zipPuid));
			assertEquals(zipJhoveId, DROIDIdentifier.getPuidToJhoveId().get(zipPuid));
		} catch (JHOVE2Exception e) {
			fail("exception" + e.getMessage());
		}		
	}

	/**
	 * Test method for {@link org.jhove2.module.identify.DROIDIdentifier#identify(org.jhove2.core.JHOVE2, org.jhove2.core.source.Source)}.
	 */
	@Test
	public void testIdentify() {
		FileSource source = null;
		Set<FormatIdentification> ids = null;
		String zipFilePath = samplesDirPath.concat(sampleFile);
		try {
			source = new FileSource(zipFilePath);
		} catch (Exception e) {
			fail("Couldn't create source: " + e.getMessage());
		} 
		try {
			ids = dROIDIdentifier.identify(JHOVE2, source);
			assertEquals(1, ids.size());
			for (FormatIdentification fi : ids){
				assertEquals(Confidence.PositiveSpecific, fi.getConfidence());
				assertEquals(zipPuid, fi.getIdentification().getValue());
				assertEquals(zipJhoveId, fi.getJhove2Identification().getValue());
				assertEquals(0, fi.getMessages().size());
			}
			assertNull(dROIDIdentifier.getFileErrorMessage());
			assertNull(dROIDIdentifier.getFileNotIdentifiedMessage());
			assertNull(dROIDIdentifier.getFileNotRunMessage());
//			assertEquals(0, dROIDIdentifier.getUnmatchedPuidMessages().size());
		} catch (Exception e) {
			fail("Couldn't identify: zip " + e.getMessage());
		} 
		String badFilePath = samplesDirPath.concat(sampleBadFile);
		try {
			source = new FileSource(badFilePath);
		} catch (Exception e) {
			fail("Couldn't create source: " + e.getMessage());
		}
		try {
			ids = dROIDIdentifier.identify(JHOVE2, source);
			assertEquals(0, ids.size());
			assertNull(dROIDIdentifier.getFileErrorMessage());
			assertNull(dROIDIdentifier.getFileNotRunMessage());
			assertNotNull(dROIDIdentifier.getFileNotIdentifiedMessage());
		} catch (Exception e) {
			fail("Couldn't identify: bad file " + e.getMessage());
		} 
		dROIDIdentifier.setFileNotIdentifiedMessage(null);
		dROIDIdentifier.setFileErrorMessage(null);
		dROIDIdentifier.setFileNotRunMessage(null);
		String noJhoveFormatFilePath = samplesDirPath.concat(sampleNoJhoveIdFile);
		try {
			source = new FileSource(noJhoveFormatFilePath);
		} catch (Exception e) {
			fail("Couldn't create source: " + e.getMessage());
		}
		try {
			ids = dROIDIdentifier.identify(JHOVE2, source);
			assertEquals(1, ids.size());
			for (FormatIdentification fi : ids){
				assertEquals(Confidence.PositiveSpecific, fi.getConfidence());
				assertNull(fi.getJhove2Identification());
			}
			assertNull(dROIDIdentifier.getFileErrorMessage());
			assertNull(dROIDIdentifier.getFileNotRunMessage());
			assertNull(dROIDIdentifier.getFileNotIdentifiedMessage());
		} catch (Exception e) {
			fail("Couldn't identify: bad file " + e.getMessage());
		} 
	}

	public DROIDIdentifier getDroidIdentifier() {
		return dROIDIdentifier;
	}
	@Resource
	public void setDroidIdentifier(DROIDIdentifier testDroidIdentifier) {
		this.dROIDIdentifier = testDroidIdentifier;
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

	public String getSamplesDirPath() {
		return samplesDirPath;
	}
	@Resource
	public void setSamplesDirPath(String samplesDirPath) {
		this.samplesDirPath = samplesDirPath;
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
