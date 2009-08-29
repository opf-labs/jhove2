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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.jhove2.core.Format;
import org.jhove2.core.FormatIdentification;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.FormatIdentification.Confidence;
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
@ContextConfiguration(locations={"classpath*:**/aggrefier-config.xml"})
public class AggrefierModuleTest {

	private AggrefierModule Aggrefier;
	private GlobPathRecognizer strictShapeFileRecognizer;
	private String shapeDirPath;
	private ArrayList<String> shapeFileList;
	private HashMap<String, String> shapeStrictKeyCountMap;
	private JHOVE2 JHOVE2;
	private GlobPathRecognizer quickenFileRecognizer;
	private String quickenDirPath;
	private List<String> quickenFileList;
	private HashMap<String, String> quickenStrictKeyCountMap;

	/**
	 * Test method for {@link org.jhove2.module.identify.AggrefierModule#identify(org.jhove2.core.JHOVE2, org.jhove2.core.source.Source)}.
	 */
	@Test
	public void testIdentify() {
		FileSetSource fsSource = new FileSetSource();
		try {
			for (String shapeFileName:shapeFileList){
				String testFilePath = shapeDirPath.concat(shapeFileName);
				FileSource fs = new FileSource(new File(testFilePath));
				fsSource.addChildSource(fs);
			}
			for (String quickenFileName:quickenFileList){
				String testFilePath = quickenDirPath.concat(quickenFileName);
				FileSource fs = new FileSource(new File(testFilePath));
				fsSource.addChildSource(fs);
			}
			Set<FormatIdentification> fiSet = 
				Aggrefier.identify(JHOVE2, fsSource);
			assertEquals(shapeStrictKeyCountMap.entrySet().size() + 
					quickenStrictKeyCountMap.entrySet().size(), fiSet.size());
			for (FormatIdentification fi:fiSet){
				assertEquals(fi.getConfidence(),Confidence.PositiveGeneric);
				Format format = fi.getPresumptiveFormat();	
				if (format.equals(strictShapeFileRecognizer.getFormat())){
					String sourceKey = getSourceKey(fi.getSource());
					assertTrue(shapeStrictKeyCountMap.containsKey(sourceKey));
					int expectedSourceCount = 
						Integer.parseInt(shapeStrictKeyCountMap.get(sourceKey));
					assertEquals(expectedSourceCount, fi.getSource().getChildSources().size());
				}
				else if (format.equals(quickenFileRecognizer.getFormat())){
					String sourceKey = getSourceKey(fi.getSource());
					assertTrue(quickenStrictKeyCountMap.containsKey(sourceKey));
					int expectedSourceCount = 
						Integer.parseInt(quickenStrictKeyCountMap.get(sourceKey));
					assertEquals(expectedSourceCount, fi.getSource().getChildSources().size());
				}
				else {
					fail("Unmatched format: " + format.getName());
				}
			}
		}
		catch (Exception e){
			fail("Exception thrown: " + e.getMessage());
		}
	}

	private String getSourceKey(Source source){
		Source firstSource = source.getChildSources().get(0);
		String fileName = firstSource.getFile().getName();
		int i = fileName.indexOf(".");
		if (i<0){
			i=fileName.length();
		}
		return fileName.substring(0, i);
	}
	
	
	public AggrefierModule getAggrefier() {
		return Aggrefier;
	}
	@Resource
	public void setAggrefier(AggrefierModule aggrefier) {
		Aggrefier = aggrefier;
	}

	public GlobPathRecognizer getStrictShapeFileRecognizer() {
		return strictShapeFileRecognizer;
	}
	@Resource
	public void setStrictShapeFileRecognizer(
			GlobPathRecognizer strictShapeFileRecognizer) {
		this.strictShapeFileRecognizer = strictShapeFileRecognizer;
	}

	public String getShapeDirPath() {
		return shapeDirPath;
	}
	@Resource
	public void setShapeDirPath(String shapeDirPath) {
		this.shapeDirPath = shapeDirPath;
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

	public String getQuickenDirPath() {
		return quickenDirPath;
	}
	@Resource
	public void setQuickenDirPath(String quickenDirPath) {
		this.quickenDirPath = quickenDirPath;
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

}
