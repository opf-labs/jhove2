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
package uk.gov.nationalarchives.droid;

import static org.junit.Assert.*;


import javax.annotation.Resource;

import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.reportable.ReportableFactory;
import org.jhove2.core.source.SourceFactory;
import org.jhove2.module.identify.DROIDWrappedProduct;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import uk.gov.nationalarchives.droid.signatureFile.FileFormat;

/**
 * @author smorrissey
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:**/ukDroid-config.xml", "classpath*:**/filepaths-config.xml"})
public class DroidTest {
	private DROIDWrappedProduct droid;
	private String configFileName;
	private String sigFileName;
	private String droidDirBasePath;
	private String sampleFile;
	private String sampleFilePUID;
	private String sampleBadFile;

	/**
	 * Test method for 
	 */
	@Test
	public void testIdentify() {	
		String samplesDirPath = null;
		try {
			samplesDirPath = 
				ReportableFactory.getFilePathFromClasspath(droidDirBasePath, "droid samples dir");
		} catch (JHOVE2Exception e1) {
			fail("Could not create base directory");
		}
		String sampleFilePath = samplesDirPath.concat(sampleFile);
		try {	
			String configFilePath = ReportableFactory.getFilePathFromClasspath(configFileName, "Droid config file");
			String sigFilePath = ReportableFactory.getFilePathFromClasspath(sigFileName, "Droid signature file");
			
			droid = new DROIDWrappedProduct(configFilePath, sigFilePath);
			IdentificationFile idf = droid.identify(sampleFilePath);
			assertEquals(JHOVE2IAnalysisController.FILE_CLASSIFICATION_POSITIVE,
					idf.getClassification());
			assertEquals(1,idf.getNumHits());
			FileFormatHit ffh = idf.getHit(0);
			assertEquals(JHOVE2IAnalysisController.HIT_TYPE_POSITIVE_SPECIFIC, 
					ffh.getHitType());
			FileFormat ff = ffh.getFileFormat();
			assertEquals(this.getSampleFilePUID(), ff.getPUID());
			String badFilePath = samplesDirPath.concat(sampleBadFile);
			idf = droid.identify(badFilePath);
			assertEquals(JHOVE2IAnalysisController.FILE_CLASSIFICATION_NOHIT,
					idf.getClassification());
			assertEquals(0,idf.getNumHits());
		} catch (Exception e) {
			fail("Exception thrown: " + e.getMessage());
			e.printStackTrace();
		}
		// now try it on an input stream
		try {		
			String configFilePath = ReportableFactory.getFilePathFromClasspath(configFileName, "Droid config file");
			String sigFilePath = ReportableFactory.getFilePathFromClasspath(sigFileName, "Droid signature file");
			droid = new DROIDWrappedProduct(configFilePath, sigFilePath);			
			IdentificationFile idf = droid.identify(SourceFactory.getSource(sampleFilePath));
			assertEquals(JHOVE2IAnalysisController.FILE_CLASSIFICATION_POSITIVE,
					idf.getClassification());
			assertEquals(1,idf.getNumHits());
			FileFormatHit ffh = idf.getHit(0);
			assertEquals(JHOVE2IAnalysisController.HIT_TYPE_POSITIVE_SPECIFIC, 
					ffh.getHitType());
			FileFormat ff = ffh.getFileFormat();
			assertEquals(this.getSampleFilePUID(), ff.getPUID());
			String badFilePath = samplesDirPath.concat(sampleBadFile);
			idf = droid.identify(badFilePath);
			assertEquals(JHOVE2IAnalysisController.FILE_CLASSIFICATION_NOHIT,
					idf.getClassification());
			assertEquals(0,idf.getNumHits());
		} catch (Exception e) {
			fail("Exception thrown: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public String getConfigFileName() {
		return configFileName;
	}

	@Resource
	public void setConfigFileName(String configFileName) {
		this.configFileName = configFileName;
	}


	public String getSigFileName() {
		return sigFileName;
	}

	@Resource
	public void setSigFileName(String sigFileName) {
		this.sigFileName = sigFileName;
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

}
