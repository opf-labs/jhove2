/**
 * JHOVE2 - Next-generation architecture for format-aware characterization
 *
 * Copyright (c) 2012 by The Regents of the University of California,
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


package org.jhove2.app.util.messages;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import javax.annotation.Resource;

import org.jhove2.ConfigTestBase;
import org.jhove2.app.util.FeatureConfigurationUtil;
import org.jhove2.core.JHOVE2Exception;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
		"classpath*:**/test-config.xml", 
		"classpath*:**/persist-test-config.xml",
		"classpath*:**/filepaths-config.xml",
		"classpath*:**/message-test-config.xml"})
public class MessageFinderTest extends ConfigTestBase {

	protected MessageFinder mf;
	protected String messageBaseDir;
	protected String javaFileName;

	@BeforeClass 
	public static void setUpBeforeClass() throws Exception {
    	ArrayList<String> paths = new ArrayList<String>();   	
    	paths.add("classpath*:**/test-config.xml");
    	paths.add("classpath*:**/persist-test-config.xml");
    	paths.add("classpath*:**/filepaths-config.xml");
    	paths.add("classpath*:**/message-test-config.xml");    	
    	ConfigTestBase.setCONTEXT_PATHS(paths);
    	ConfigTestBase.setUpBeforeClass();
    }
	@Before
	public void setUp() throws Exception {
		mf = new MessageFinder();

	}

	@Test
	public void testFindMessageCodes() {
		String messageDirPath = null;
		try {
			messageDirPath = FeatureConfigurationUtil
			.getFilePathFromClasspath(messageBaseDir,
					"Message java class base directory");
		}
		catch (JHOVE2Exception e1) {
			fail("Could not create message base directory");
		}
		File msgExampleDir = new File(messageDirPath);
		assertTrue(msgExampleDir.exists());
		File testFile = new File(messageDirPath, javaFileName);
		assertTrue(testFile.exists());
		String javaFilePath = null;
		try {
			javaFilePath = testFile.getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		try {
			Set<String>messageCodes = mf.findMessageCodes(javaFilePath);
			for (String message:messageCodes){
				System.out.println(message);
			}
			assertEquals(25, messageCodes.size());
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/**
	 * @return the messageBaseDir
	 */
	public String getMessageBaseDir() {
		return messageBaseDir;
	}

	/**
	 * @param messageBaseDir the messageBaseDir to set
	 */
	@Resource(name="messageDirBasePath")
	public void setMessageBaseDir(String messageBaseDir) {
		this.messageBaseDir = messageBaseDir;
	}

	/**
	 * @return the javaFileName
	 */	
	public String getJavaFileName() {
		return javaFileName;
	}

	/**
	 * @param javaFileName the javaFileName to set
	 */
	@Resource(name="MessageCodeTestFile")
	public void setJavaFileName(String javaFileName) {
		this.javaFileName = javaFileName;
	}

}
