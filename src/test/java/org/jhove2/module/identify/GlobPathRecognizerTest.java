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
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.test.context.junit4.*;
import org.springframework.test.context.ContextConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;
import java.util.Collection;
import java.util.HashMap;

import javax.annotation.Resource;


import org.jhove2.core.JHOVE2;
import org.jhove2.core.FormatIdentification;
import org.jhove2.core.source.ClumpSource;
import org.jhove2.core.source.FileSource;
import org.jhove2.core.source.FileSetSource;
import org.jhove2.core.source.Source;


/**
 * @author Sheila Morrissey
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:**/globpathrecognizer-config.xml",
		"classpath*:**/test-config.xml"})
public class GlobPathRecognizerTest{

	private GlobPathRecognizer strictShapeFileRecognizer;
	private GlobPathRecognizer relaxedShapeFileRecognizer;
	private String testDirPath;
	private ArrayList<String> testFileList;
	private ArrayList<String> groupKeys;
	private Integer expectedGroupCount;// expect the same number of groups for strict and relaxed
	private ArrayList<String> failStrictKeys;
	private ArrayList<String> failRelaxedKeys;
	private HashMap<String, String> strictKeyCountMap;
	private HashMap<String, String> relaxedKeyCountMap;
	private JHOVE2 JHOVE2;

	/**
	 * Test method for {@link org.jhove2.module.identify.GlobPathRecognizer#groupSources(org.jhove2.core.source.Source)}.
	 */
	@Test
	public void testGroupSources() {		
		try {			
			FileSetSource fsSource = new FileSetSource();
			for (String fileName:this.getTestFileList()){
				String testFilePath = testDirPath.concat(fileName);
				FileSource fs = new FileSource(new File(testFilePath));
				fsSource.addChildSource(fs);
			}
			strictShapeFileRecognizer.compilePatterns();
			Collection <GlobPathMatchInfoGroup> gInfoGroupStrict = 
				strictShapeFileRecognizer.groupSources(fsSource);
			assertEquals(expectedGroupCount.intValue(), gInfoGroupStrict.size());
			TreeSet<String> expectedKeys = new TreeSet<String>(this.getGroupKeys());		
			TreeSet<String> actualKeys = new TreeSet<String>();
			for (GlobPathMatchInfoGroup group:gInfoGroupStrict){
				actualKeys.add(group.getGroupKey());
			}
			for (String key:expectedKeys){			
				String newKey = this.getTestDirPath().concat(key);
				assertTrue(actualKeys.contains(newKey));
			}
			relaxedShapeFileRecognizer.compilePatterns();
			gInfoGroupStrict = 
				relaxedShapeFileRecognizer.groupSources(fsSource);
			assertEquals(expectedGroupCount.intValue(), gInfoGroupStrict.size());
			expectedKeys = new TreeSet<String>(this.getGroupKeys());		
			actualKeys = new TreeSet<String>();
			for (GlobPathMatchInfoGroup group:gInfoGroupStrict){
				actualKeys.add(group.getGroupKey());
			}
			for (String key:expectedKeys){			;
			String newKey = this.getTestDirPath().concat(key);
			assertTrue(actualKeys.contains(newKey));
			}
		}
		catch (Exception e){
			fail("Exceptpion thrown:" + e.getMessage());
		}
	}

	/**
	 * Test method for {@link org.jhove2.module.identify.GlobPathRecognizer#recognizeGroupedSource(org.jhove2.module.identify.GlobPathMatchInfoGroup)}.
	 */
	@Test
	public void testRecognizeGroupedSource() {
		try{
			FileSetSource fsSource = new FileSetSource();
			for (String fileName:this.getTestFileList()){
				String testFilePath = testDirPath.concat(fileName);
				FileSource fs = new FileSource(new File(testFilePath));
				fsSource.addChildSource(fs);
			}
			ArrayList<String> fullFailKeys = new ArrayList<String>();
			for (String failKey:failStrictKeys){
				fullFailKeys.add(testDirPath.concat(failKey));
			}
			HashMap<String, Integer> fullKeyCountMap = new HashMap<String, Integer>();
			for (String key:strictKeyCountMap.keySet()){
				String newKey = testDirPath.concat(key);
				Integer newValue = Integer.valueOf(strictKeyCountMap.get(key));
				fullKeyCountMap.put(newKey, newValue);
			}
			strictShapeFileRecognizer.compilePatterns();
			Collection <GlobPathMatchInfoGroup> gInfoGroupStrict = 
				strictShapeFileRecognizer.groupSources(fsSource);
			for (GlobPathMatchInfoGroup infoGroup:gInfoGroupStrict){
				Source clumpSource = 
					strictShapeFileRecognizer.recognizeGroupedSource(infoGroup);
				if (clumpSource==null){
					assertTrue(fullFailKeys.contains(infoGroup.groupKey));
				}
				else {
					assertEquals(fullKeyCountMap.get(infoGroup.groupKey).intValue(),
							clumpSource.getChildSources().size());
					for (FormatIdentification fi: clumpSource.getPresumptiveFormatIdentifications()){				
						assertFalse(fullFailKeys.contains(infoGroup.groupKey));
						assertEquals(strictShapeFileRecognizer.getFormatIdentifier(),
								fi.getJhove2Identification());
						assertEquals(fi.getConfidence(),GlobPathRecognizer.GLOB_PATH_CONFIDENCE);
					}
				}
			}
			// now test relaxed recognizer
			fullFailKeys = new ArrayList<String>();
			for (String failKey:failRelaxedKeys){
				fullFailKeys.add(testDirPath.concat(failKey));
			}
			fullKeyCountMap = new HashMap<String, Integer>();
			for (String key:relaxedKeyCountMap.keySet()){
				String newKey = testDirPath.concat(key);
				Integer newValue = Integer.valueOf(relaxedKeyCountMap.get(key));
				fullKeyCountMap.put(newKey, newValue);
			}
			relaxedShapeFileRecognizer.compilePatterns();
			gInfoGroupStrict = 
				relaxedShapeFileRecognizer.groupSources(fsSource);
			for (GlobPathMatchInfoGroup infoGroup:gInfoGroupStrict){
				Source clumpSource = 
					relaxedShapeFileRecognizer.recognizeGroupedSource(infoGroup);
				if (clumpSource==null){
					assertTrue(fullFailKeys.contains(infoGroup.groupKey));
				}
				else {
					assertEquals(fullKeyCountMap.get(infoGroup.groupKey).intValue(),
							clumpSource.getChildSources().size());
					for (FormatIdentification fi : clumpSource.getPresumptiveFormatIdentifications()){
						assertFalse(fullFailKeys.contains(infoGroup.groupKey));
						assertEquals(relaxedShapeFileRecognizer.getFormatIdentifier(),
								fi.getJhove2Identification());
						assertEquals(fi.getConfidence(),GlobPathRecognizer.GLOB_PATH_CONFIDENCE);
					}
				}
			}
		}
		catch (Exception e){
			fail("Exceptpion thrown:" + e.getMessage());
		}
	}

	/**
	 * Test method for {@link org.jhove2.module.identify.GlobPathRecognizer#identify(org.jhove2.core.JHOVE2, org.jhove2.core.source.Source)}.
	 */
	@Test
	public void testIdentify() {
		try {
			FileSetSource fsSource = new FileSetSource();
			for (String fileName:this.getTestFileList()){
				String testFilePath = testDirPath.concat(fileName);
				FileSource fs = new FileSource(new File(testFilePath));
				fsSource.addChildSource(fs);
			}
			Set<ClumpSource> sources = 
				strictShapeFileRecognizer.identify(JHOVE2, fsSource);
			assertEquals(strictKeyCountMap.size(), sources.size());
			for (ClumpSource cSource:sources){
				Set<FormatIdentification> fiSet = cSource.getPresumptiveFormatIdentifications();
				for (FormatIdentification fi:fiSet){
					assertEquals(strictShapeFileRecognizer.getFormatIdentifier(),
							fi.getJhove2Identification());
					assertEquals(fi.getConfidence(),GlobPathRecognizer.GLOB_PATH_CONFIDENCE);
				}
			}
			sources = relaxedShapeFileRecognizer.identify(JHOVE2, fsSource);

			assertEquals(relaxedKeyCountMap.size(), sources.size());
			for (Source cSource:sources){
				Set<FormatIdentification> fiSet = cSource.getPresumptiveFormatIdentifications();

				for (FormatIdentification fi:fiSet){
					assertEquals(relaxedShapeFileRecognizer.getFormatIdentifier(),
							fi.getJhove2Identification());
					assertEquals(fi.getConfidence(),GlobPathRecognizer.GLOB_PATH_CONFIDENCE);
				}
			}
		}
		catch (Exception e){
			fail("Exceptpion thrown:" + e.getMessage());		
		}
	}

	public GlobPathRecognizer getStrictShapeFileRecognizer() {
		return strictShapeFileRecognizer;
	}
	@Resource(name="testStrictShapeFileRecognizer")
	public void setStrictShapeFileRecognizer(
			GlobPathRecognizer strictShapeFileRecognizer) {
		this.strictShapeFileRecognizer = strictShapeFileRecognizer;
	}

	public GlobPathRecognizer getRelaxedShapeFileRecognizer() {
		return relaxedShapeFileRecognizer;
	}
	@Resource(name="relaxedShapeFileRecognizer")
	public void setRelaxedShapeFileRecognizer(
			GlobPathRecognizer relaxedShapeFileRecognizer) {
		this.relaxedShapeFileRecognizer = relaxedShapeFileRecognizer;
	}

	public String getTestDirPath() {
		return testDirPath;
	}
	@Resource
	public void setTestDirPath(String testDirPath) {
		this.testDirPath = testDirPath;
	}

	public ArrayList<String> getTestFileList() {
		return testFileList;
	}
	@Resource
	public void setTestFileList(ArrayList<String> testFileList) {
		this.testFileList = testFileList;
	}

	public ArrayList<String> getGroupKeys() {
		return groupKeys;
	}
	@Resource
	public void setGroupKeys(ArrayList<String> groupKeys) {
		this.groupKeys = groupKeys;
	}

	public int getExpectedGroupCount() {
		return expectedGroupCount;
	}
	@Resource
	public void setExpectedGroupCount(Integer expectedGroupCount) {
		this.expectedGroupCount = expectedGroupCount;
	}

	public ArrayList<String> getFailStrictKeys() {
		return failStrictKeys;
	}
	@Resource
	public void setFailStrictKeys(ArrayList<String> failStrictKeys) {
		this.failStrictKeys = failStrictKeys;
	}

	public ArrayList<String> getFailRelaxedKeys() {
		return failRelaxedKeys;
	}
	@Resource
	public void setFailRelaxedKeys(ArrayList<String> failRelaxedKeys) {
		this.failRelaxedKeys = failRelaxedKeys;
	}

	public HashMap<String, String> getStrictKeyCountMap() {
		return strictKeyCountMap;
	}
	@Resource
	public void setStrictKeyCountMap(HashMap<String, String> strictKeyCountMap) {
		this.strictKeyCountMap = strictKeyCountMap;
	}

	public HashMap<String, String> getRelaxedKeyCountMap() {
		return relaxedKeyCountMap;
	}
	@Resource
	public void setRelaxedKeyCountMap(HashMap<String, String> relaxedKeyCountMap) {
		this.relaxedKeyCountMap = relaxedKeyCountMap;
	}

	public JHOVE2 getJHOVE2() {
		return JHOVE2;
	}
	@Resource
	public void setJHOVE2(JHOVE2 jHOVE2) {
		JHOVE2 = jHOVE2;
	}
}
