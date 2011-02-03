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
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.source.Source;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author smorrissey
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:**/test-config.xml", 
"classpath*:**/filepaths-config.xml"}) 
public class ErrFileParserTest {
	static String FILENAME = "/file/name";
	static String LINE = "22";
	static String POS = "445";
	static String TEXT = "This is the message text";
	static String SEVERITY = "W";
	static String MSGCODE = "202";
	static String CODEDMESSAGE = OpenSpErrMessageParser.MESSAGE_LEVEL + SEVERITY +
	OpenSpErrMessageParser.MESSAGE_CODE + MSGCODE +
	OpenSpErrMessageParser.LINE + LINE +
	OpenSpErrMessageParser.POSITION + POS +
	OpenSpErrMessageParser.MESSAGE_TEXT + TEXT;
	static String UNCODEDMESSAGE = OpenSpErrMessageParser.MESSAGE_LEVEL + OpenSpErrMessageParser.NA +
	OpenSpErrMessageParser.MESSAGE_CODE + OpenSpErrMessageParser.NA +
	OpenSpErrMessageParser.LINE + LINE +
	OpenSpErrMessageParser.POSITION + POS +
	OpenSpErrMessageParser.MESSAGE_TEXT + TEXT;
	protected ErrFileParser parser;
	protected String conformButWarn;
	protected String conformNoErr;
	protected String noDoctype;
	protected String spaceBeforeDoctype;
	protected String unmatchedPubid;
	protected String unmatchedSysid;
	protected String sgmlDirBasePath;
	protected JHOVE2 JHOVE2;
	protected SgmlModule testSgmlModule;
	/**
	 * @throws java.lang.Exception
	 */
	 @Before
	 public void setUp() throws Exception {
		parser = new ErrFileParser();
	}

	/**
	 * Test method for {@link org.jhove2.module.format.sgml.ErrFileParser#parseMessageFile(java.lang.String, org.jhove2.core.JHOVE2, org.jhove2.core.source.Source, org.jhove2.module.format.sgml.SgmlModule)}.
	 */
	 @Test
	 public void testParseMessageFile() {
		 String samplesDirPath = null;
		 try {
			 samplesDirPath = 
				 FeatureConfigurationUtil.getFilePathFromClasspath(sgmlDirBasePath, "sgml samples dir");
		 } catch (JHOVE2Exception e1) {
			 fail("Could not create base directory");
		 }
		 String conformButWarnPath = samplesDirPath.concat(conformButWarn);
		 OpenSpWrapper sp = (OpenSpWrapper) testSgmlModule.getSgmlParser();
		 sp.setMessageParser(parser);
		 Source inputSource = null;
		 try {
			 inputSource = JHOVE2.getSourceFactory().getSource(JHOVE2, conformButWarnPath);
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
			 parser.parseMessageFile(conformButWarnPath, JHOVE2, inputSource, testSgmlModule);
			 assertEquals(0,parser.eLevelMessageCount);  
			 assertEquals(6,parser.wLevelMessageCount);  
			 assertEquals(0,parser.iLevelMessageCount);  
			 assertEquals(0,parser.qLevelMessageCount);  
			 assertEquals(0,parser.xLevelMessageCount);  
			 assertEquals(12,parser.totMessageCount);
			 assertEquals(12,testSgmlModule.getDocumentProperties().getSgmlParserMessages().size());

		 } catch (JHOVE2Exception e) {
			 e.printStackTrace();
			 fail(e.getMessage());
		 } catch (IOException e) {
			 e.printStackTrace();
			 fail(e.getMessage());
		 } 

		 String conformNoErrPath = samplesDirPath.concat(conformNoErr);
		 parser = new ErrFileParser();
		 sp.setMessageParser(parser);
		 testSgmlModule.setDocumentProperties(new SgmlDocumentProperties());
		 try {
			 inputSource = JHOVE2.getSourceFactory().getSource(JHOVE2, conformNoErrPath);
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
			 parser.parseMessageFile(conformNoErrPath, JHOVE2, inputSource, testSgmlModule);
			 assertEquals(0,parser.eLevelMessageCount);  
			 assertEquals(0,parser.wLevelMessageCount);  
			 assertEquals(0,parser.iLevelMessageCount);  
			 assertEquals(0,parser.qLevelMessageCount);  
			 assertEquals(0,parser.xLevelMessageCount);  
			 assertEquals(0,parser.totMessageCount);
			 assertEquals(0,testSgmlModule.getDocumentProperties().getSgmlParserMessages().size());

		 } catch (JHOVE2Exception e) {
			 e.printStackTrace();
			 fail(e.getMessage());
		 } catch (IOException e) {
			 e.printStackTrace();
			 fail(e.getMessage());
		 } 
		 
		 String nodoctypePath = samplesDirPath.concat(noDoctype);
		 parser = new ErrFileParser();
		 sp.setMessageParser(parser);
		 testSgmlModule.setDocumentProperties(new SgmlDocumentProperties());
		 try {
			 inputSource = JHOVE2.getSourceFactory().getSource(JHOVE2, nodoctypePath);
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
			 parser.parseMessageFile(nodoctypePath, JHOVE2, inputSource, testSgmlModule);
			 assertEquals(2,parser.eLevelMessageCount);  
			 assertEquals(0,parser.wLevelMessageCount);  
			 assertEquals(0,parser.iLevelMessageCount);  
			 assertEquals(0,parser.qLevelMessageCount);  
			 assertEquals(0,parser.xLevelMessageCount);  
			 assertEquals(2,parser.totMessageCount);
			 assertEquals(2,testSgmlModule.getDocumentProperties().getSgmlParserMessages().size());

		 } catch (JHOVE2Exception e) {
			 e.printStackTrace();
			 fail(e.getMessage());
		 } catch (IOException e) {
			 e.printStackTrace();
			 fail(e.getMessage());
		 } 
		 
		 String spaceBeforePath = samplesDirPath.concat(spaceBeforeDoctype);
		 parser = new ErrFileParser();
		 sp.setMessageParser(parser);
		 testSgmlModule.setDocumentProperties(new SgmlDocumentProperties());
		 try {
			 inputSource = JHOVE2.getSourceFactory().getSource(JHOVE2, spaceBeforePath);
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
			 parser.parseMessageFile(spaceBeforePath, JHOVE2, inputSource, testSgmlModule);
			 assertEquals(47,parser.eLevelMessageCount);  
			 assertEquals(12,parser.wLevelMessageCount);  
			 assertEquals(0,parser.iLevelMessageCount);  
			 assertEquals(0,parser.qLevelMessageCount);  
			 assertEquals(0,parser.xLevelMessageCount);  
			 assertEquals(165,parser.totMessageCount);
			 assertEquals(165,testSgmlModule.getDocumentProperties().getSgmlParserMessages().size());

		 } catch (JHOVE2Exception e) {
			 e.printStackTrace();
			 fail(e.getMessage());
		 } catch (IOException e) {
			 e.printStackTrace();
			 fail(e.getMessage());
		 } 
		 
		 String unmatchedPubPath = samplesDirPath.concat(unmatchedPubid);
		 parser = new ErrFileParser();
		 sp.setMessageParser(parser);
		 testSgmlModule.setDocumentProperties(new SgmlDocumentProperties());
		 try {
			 inputSource = JHOVE2.getSourceFactory().getSource(JHOVE2, unmatchedPubPath);
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
			 parser.parseMessageFile(unmatchedPubPath, JHOVE2, inputSource, testSgmlModule);
			 assertEquals(43,parser.eLevelMessageCount);  
			 assertEquals(1,parser.wLevelMessageCount);  
			 assertEquals(0,parser.iLevelMessageCount);  
			 assertEquals(0,parser.qLevelMessageCount);  
			 assertEquals(0,parser.xLevelMessageCount);  
			 assertEquals(86,parser.totMessageCount);
			 assertEquals(86,testSgmlModule.getDocumentProperties().getSgmlParserMessages().size());

		 } catch (JHOVE2Exception e) {
			 e.printStackTrace();
			 fail(e.getMessage());
		 } catch (IOException e) {
			 e.printStackTrace();
			 fail(e.getMessage());
		 } 
		 
		 String unmatchedSysPath = samplesDirPath.concat(unmatchedSysid);
		 parser = new ErrFileParser();
		 sp.setMessageParser(parser);
		 testSgmlModule.setDocumentProperties(new SgmlDocumentProperties());
		 try {
			 inputSource = JHOVE2.getSourceFactory().getSource(JHOVE2, unmatchedSysPath);
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
			 parser.parseMessageFile(unmatchedSysPath, JHOVE2, inputSource, testSgmlModule);
			 assertEquals(88,parser.eLevelMessageCount);  
			 assertEquals(4,parser.wLevelMessageCount);  
			 assertEquals(0,parser.iLevelMessageCount);  
			 assertEquals(0,parser.qLevelMessageCount);  
			 assertEquals(0,parser.xLevelMessageCount);  
			 assertEquals(198,parser.totMessageCount);
			 assertEquals(198,testSgmlModule.getDocumentProperties().getSgmlParserMessages().size());

		 } catch (JHOVE2Exception e) {
			 e.printStackTrace();
			 fail(e.getMessage());
		 } catch (IOException e) {
			 e.printStackTrace();
			 fail(e.getMessage());
		 } 
		 
	 }

	 /**
	  * @param conformButWarn the conformButWarn to set
	  */
	 @Resource
	 public void setConformButWarn(String conformButWarn) {
		 this.conformButWarn = conformButWarn;
	 }

	 /**
	  * @param conformNoErr the conformNoErr to set
	  */
	 @Resource
	 public void setConformNoErr(String conformNoErr) {
		 this.conformNoErr = conformNoErr;
	 }

	 /**
	  * @param noDoctype the noDoctype to set
	  */
	 @Resource
	 public void setNoDoctype(String noDoctype) {
		 this.noDoctype = noDoctype;
	 }

	 /**
	  * @param sgmlDirBasePath the sgmlDirBasePath to set
	  */
	 @Resource
	 public void setSgmlDirBasePath(String sgmlDirBasePath) {
		 this.sgmlDirBasePath = sgmlDirBasePath;
	 }
	 /**
	  * @param spaceBeforeDoctype the spaceBeforeDoctype to set
	  */
	 @Resource
	 public void setSpaceBeforeDoctype(String spaceBeforeDoctype) {
		 this.spaceBeforeDoctype = spaceBeforeDoctype;
	 }

	 /**
	  * @param unmatchedPubid the unmatchedPubid to set
	  */
	 @Resource
	 public void setUnmatchedPubid(String unmatchedPubid) {
		 this.unmatchedPubid = unmatchedPubid;
	 }

	 /**
	  * @param unmatchedSysid the unmatchedSysid to set
	  */
	 @Resource
	 public void setUnmatchedSysid(String unmatchedSysid) {
		 this.unmatchedSysid = unmatchedSysid;
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
