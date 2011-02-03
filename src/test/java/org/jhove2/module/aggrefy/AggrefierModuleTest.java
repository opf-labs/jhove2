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
package org.jhove2.module.aggrefy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.jhove2.app.util.FeatureConfigurationUtil;
import org.jhove2.core.I8R;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.format.FormatIdentification;
import org.jhove2.core.source.ClumpSource;
import org.jhove2.core.source.DirectorySource;
import org.jhove2.core.source.FileSetSource;
import org.jhove2.core.source.FileSource;
import org.jhove2.core.source.Source;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Sheila Morrissey
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:**/aggrefier-config.xml",
		"classpath*:**/test-config.xml", "classpath*:**/filepaths-config.xml"})
public class AggrefierModuleTest {

	private AggrefierModule TestAggrefier;
	private GlobPathRecognizer strictShapeFileRecognizer;
	private String shapeDirBasePath;
	private ArrayList<String> shapeFileList;
	private HashMap<String, String> shapeStrictKeyCountMap;
	private JHOVE2 JHOVE2;
	private GlobPathRecognizer quickenFileRecognizer;
	private String quickenDirBasePath;
	private List<String> quickenFileList;
	private HashMap<String, String> quickenStrictKeyCountMap;
	private String emptyDirBasePath;

	/**
	 * Test method for {@link org.jhove2.module.aggrefy.AggrefierModule#identify(org.jhove2.core.JHOVE2, org.jhove2.core.source.Source)}.
	 */
	@Test
	public void testIdentify() {
		FileSetSource fsSource = null;
		JHOVE2 jhove2 = null;
		try {
		    jhove2 = new JHOVE2();
			fsSource = JHOVE2.getSourceFactory().getFileSetSource();
		} catch (JHOVE2Exception e2) {
			e2.printStackTrace();
			fail();
		}
		String shapeDirPath = null;
		String quickenDirPath = null;
		String emptyDirPath = null;
		try {
			shapeDirPath = 
				FeatureConfigurationUtil.getFilePathFromClasspath(shapeDirBasePath, "shapefile dir");
			quickenDirPath = 
				FeatureConfigurationUtil.getFilePathFromClasspath(quickenDirBasePath, "quicken dir");
			
			emptyDirPath = 
				FeatureConfigurationUtil.getFilePathFromClasspath(emptyDirBasePath, "empty dir");
		} catch (JHOVE2Exception e1) {
			fail("Could not create base directory");
		}
		emptyDirBasePath = emptyDirBasePath.concat("empty/");
		try {		
			File emptyDir = new File(emptyDirPath);
			if (!emptyDir.exists()){
				emptyDir.mkdirs();
			}
		}
		catch (Exception e){
			fail("Could not create empty directory");
		}
		try {
			for (String shapeFileName:shapeFileList){
				String testFilePath = shapeDirPath.concat(shapeFileName);
				FileSource fs = (FileSource)JHOVE2.getSourceFactory().getSource(jhove2, new File(testFilePath));
				fs=(FileSource) fsSource.addChildSource(fs);
			}
			for (String quickenFileName:quickenFileList){
				String testFilePath = quickenDirPath.concat(quickenFileName);
				FileSource fs = (FileSource)JHOVE2.getSourceFactory().getSource(jhove2, new File(testFilePath));
				fs=(FileSource) fsSource.addChildSource(fs);
			}
			Set<ClumpSource> clumpSources = 
				TestAggrefier.identify(JHOVE2, fsSource);
			assertEquals(shapeStrictKeyCountMap.entrySet().size() + 
					quickenStrictKeyCountMap.entrySet().size(), clumpSources.size());
			for (ClumpSource clumpSource:clumpSources){
				for (FormatIdentification fi:clumpSource.getPresumptiveFormats()){
					assertEquals(fi.getConfidence(),GlobPathRecognizer.GLOB_PATH_CONFIDENCE);
					I8R format = fi.getJHOVE2Identifier();
					if (format.equals(strictShapeFileRecognizer.getFormatIdentifier())){
						String sourceKey = getSourceKey(clumpSource);
						assertTrue(shapeStrictKeyCountMap.containsKey(sourceKey));
						int expectedSourceCount = 
							Integer.parseInt(shapeStrictKeyCountMap.get(sourceKey));
						assertEquals(expectedSourceCount, clumpSource.getNumChildSources());
					}
					else if (format.equals(quickenFileRecognizer.getFormatIdentifier())){
						String sourceKey = getSourceKey(clumpSource);
						assertTrue(quickenStrictKeyCountMap.containsKey(sourceKey));
						int expectedSourceCount = 
							Integer.parseInt(quickenStrictKeyCountMap.get(sourceKey));
						assertEquals(expectedSourceCount, clumpSource.getNumChildSources());
					}
					else {
						fail("Unmatched format: " + format.getValue());
					}
				}
			}
			DirectorySource dSource = (DirectorySource) JHOVE2.getSourceFactory().getSource(
					jhove2, new File(emptyDirPath)); 
			for (ClumpSource clumpSource:clumpSources){
				clumpSource=(ClumpSource) dSource.addChildSource(clumpSource);
			}
			Set<ClumpSource> newClumpSources = TestAggrefier.identify(JHOVE2, dSource);
			assertEquals(0, newClumpSources.size());
			
		}
		catch (Exception e){
			fail("Exception thrown: " + e.getMessage());
		}
	}

	private String getSourceKey(Source source) throws JHOVE2Exception{
		Source firstSource = source.getChildSources().get(0);
		String fileName = firstSource.getFile().getName();
		int i = fileName.indexOf(".");
		if (i<0){
			i=fileName.length();
		}
		return fileName.substring(0, i);
	}


	public AggrefierModule getTestAggrefier() {
		return TestAggrefier;
	}
	@Resource(name="TestAggrefier")
	public void setTestAggrefier(AggrefierModule aggrefier) {
		TestAggrefier = aggrefier;
	}

	public GlobPathRecognizer getStrictShapeFileRecognizer() {
		return strictShapeFileRecognizer;
	}
	@Resource(name="testStrictShapeFileRecognizer")
	public void setStrictShapeFileRecognizer(
			GlobPathRecognizer strictShapeFileRecognizer) {
		this.strictShapeFileRecognizer = strictShapeFileRecognizer;
	}

	public String getShapeDirBasePath() {
		return shapeDirBasePath;
	}
	@Resource
	public void setShapeDirBasePath(String shapeDirPath) {
		this.shapeDirBasePath = shapeDirPath;
	}

	public ArrayList<String> getShapeFileList() {
		return shapeFileList;
	}
	@Resource
	public void setShapeFileList(ArrayList<String> shapeFileList) {
		this.shapeFileList = shapeFileList;
	}

	public HashMap<String, String> getShapeStrictKeyCountMap() {
		return shapeStrictKeyCountMap;
	}
	@Resource
	public void setShapeStrictKeyCountMap(
			HashMap<String, String> shapeStrictKeyCountMap) {
		this.shapeStrictKeyCountMap = shapeStrictKeyCountMap;
	}

	public JHOVE2 getJHOVE2() {
		return JHOVE2;
	}
	@Resource
	public void setJHOVE2(JHOVE2 jHOVE2) {
		JHOVE2 = jHOVE2;
	}

	public GlobPathRecognizer getQuickenFileRecognizer() {
		return quickenFileRecognizer;
	}
	@Resource
	public void setQuickenFileRecognizer(GlobPathRecognizer quickenFileRecognizer) {
		this.quickenFileRecognizer = quickenFileRecognizer;
	}

	public String getQuickenDirBasePath() {
		return quickenDirBasePath;
	}
	@Resource
	public void setQuickenDirBasePath(String quickenDirPath) {
		this.quickenDirBasePath = quickenDirPath;
	}

	public List<String> getQuickenFileList() {
		return quickenFileList;
	}
	@Resource
	public void setQuickenFileList(List<String> quickenFileList) {
		this.quickenFileList = quickenFileList;
	}

	public HashMap<String, String> getQuickenStrictKeyCountMap() {
		return quickenStrictKeyCountMap;
	}
	@Resource
	public void setQuickenStrictKeyCountMap(
			HashMap<String, String> quickenStrictKeyCountMap) {
		this.quickenStrictKeyCountMap = quickenStrictKeyCountMap;
	}

	public String getEmptyDirBasePath() {
		return emptyDirBasePath;
	}
	@Resource
	public void setEmptyDirBasePath(String emptyDirPath) {
		this.emptyDirBasePath = emptyDirPath;
	}

}
