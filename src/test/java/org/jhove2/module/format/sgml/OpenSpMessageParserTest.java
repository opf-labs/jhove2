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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

import javax.annotation.Resource;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CommonTokenStream;
import org.jhove2.app.util.FeatureConfigurationUtil;
import org.jhove2.core.JHOVE2Exception;
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
public class OpenSpMessageParserTest {

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
	protected OpenSpErrMessageParser parser;
	protected String conformButWarn;
	protected String conformNoErr;
	protected String noDoctype;
	protected String spaceBeforeDoctype;
	protected String unmatchedPubid;
	protected String unmatchedSysid;
	protected String sgmlDirBasePath;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		parser = new OpenSpMessageParser();
	}

	/**
	 * Test method for {@link org.jhove2.module.format.sgml.OpenSpMessageParser#parseMessageFile(java.lang.String, SgmlModule)}.
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
		SgmlParseMessagesLexer lex = null;
    	CommonTokenStream tokens = null;
    	SgmlParseMessagesParser g = null;
		try {
			lex = new SgmlParseMessagesLexer(new ANTLRFileStream(
					conformButWarnPath, 
					"UTF8"));
		} catch (IOException e1) {
			e1.printStackTrace();
			fail("Could not lexer");
		}
        tokens = new CommonTokenStream(lex);
        g = new SgmlParseMessagesParser(tokens);
        try {
        	g.errMessages();
        	assertEquals(0,g.eLevelMessageCount);  
        	assertEquals(6,g.wLevelMessageCount);  
        	assertEquals(0,g.iLevelMessageCount);  
        	assertEquals(0,g.qLevelMessageCount);  
        	assertEquals(0,g.xLevelMessageCount);  
        	assertEquals(12,g.totMessageCount);
        	assertEquals(12,g.openSpMessages.size());
        	assertEquals(0,g.getSgmlMessagesParseErrors().size());
        }
        catch (Exception e){
        	fail(e.getMessage());
        }
        String conformNoErrPath = samplesDirPath.concat(conformNoErr);
        try {
			lex = new SgmlParseMessagesLexer(new ANTLRFileStream(
					conformNoErrPath, 
					"UTF8"));
		} catch (IOException e1) {
			e1.printStackTrace();
			fail("Could not lexer");
		}
        tokens = new CommonTokenStream(lex);
        g = new SgmlParseMessagesParser(tokens);
        try {
        	g.errMessages();
        	assertEquals(0,g.eLevelMessageCount);  
        	assertEquals(0,g.wLevelMessageCount);  
        	assertEquals(0,g.iLevelMessageCount);  
        	assertEquals(0,g.qLevelMessageCount);  
        	assertEquals(0,g.xLevelMessageCount);  
        	assertEquals(0,g.totMessageCount);
        	assertEquals(0,g.openSpMessages.size());
        	assertEquals(0,g.getSgmlMessagesParseErrors().size());
        }
        catch (Exception e){
        	fail(e.getMessage());
        }
        String nodoctypePath = samplesDirPath.concat(noDoctype);
        try {
			lex = new SgmlParseMessagesLexer(new ANTLRFileStream(
					nodoctypePath, 
					"UTF8"));
		} catch (IOException e1) {
			e1.printStackTrace();
			fail("Could not lexer");
		}
        tokens = new CommonTokenStream(lex);
        g = new SgmlParseMessagesParser(tokens);
        try {
        	g.errMessages();
        	assertEquals(2,g.eLevelMessageCount);  
        	assertEquals(0,g.wLevelMessageCount);  
        	assertEquals(0,g.iLevelMessageCount);  
        	assertEquals(0,g.qLevelMessageCount);  
        	assertEquals(0,g.xLevelMessageCount);  
        	assertEquals(2,g.totMessageCount);
        	assertEquals(2,g.openSpMessages.size());
        	assertEquals(0,g.getSgmlMessagesParseErrors().size());
        }
        catch (Exception e){
        	fail(e.getMessage());
        }
        String spaceBeforePath = samplesDirPath.concat(spaceBeforeDoctype);
        try {
			lex = new SgmlParseMessagesLexer(new ANTLRFileStream(
					spaceBeforePath, 
					"UTF8"));
		} catch (IOException e1) {
			e1.printStackTrace();
			fail("Could not lexer");
		}
        tokens = new CommonTokenStream(lex);
        g = new SgmlParseMessagesParser(tokens);
        try {
        	g.errMessages();
        	assertEquals(47,g.eLevelMessageCount);  
        	assertEquals(12,g.wLevelMessageCount);  
        	assertEquals(0,g.iLevelMessageCount);  
        	assertEquals(0,g.qLevelMessageCount);  
        	assertEquals(0,g.xLevelMessageCount);  
        	assertEquals(165,g.totMessageCount);
        	assertEquals(165,g.openSpMessages.size());
        	assertEquals(0,g.getSgmlMessagesParseErrors().size());
        }
        catch (Exception e){
        	fail(e.getMessage());
        }
        String unmatchedPubPath = samplesDirPath.concat(unmatchedPubid);
        try {
			lex = new SgmlParseMessagesLexer(new ANTLRFileStream(
					unmatchedPubPath, 
					"UTF8"));
		} catch (IOException e1) {
			e1.printStackTrace();
			fail("Could not lexer");
		}
        tokens = new CommonTokenStream(lex);
        g = new SgmlParseMessagesParser(tokens);
        try {
        	g.errMessages();
        	assertEquals(43,g.eLevelMessageCount);  
        	assertEquals(1,g.wLevelMessageCount);  
        	assertEquals(0,g.iLevelMessageCount);  
        	assertEquals(0,g.qLevelMessageCount);  
        	assertEquals(0,g.xLevelMessageCount);  
        	assertEquals(86,g.totMessageCount);
        	assertEquals(86,g.openSpMessages.size());
        	assertEquals(0,g.getSgmlMessagesParseErrors().size());
        }
        catch (Exception e){
        	fail(e.getMessage());
        }
        String unmatchedSysPath = samplesDirPath.concat(unmatchedSysid);
        try {
			lex = new SgmlParseMessagesLexer(new ANTLRFileStream(
					unmatchedSysPath, 
					"UTF8"));
		} catch (IOException e1) {
			e1.printStackTrace();
			fail("Could not lexer");
		}
        tokens = new CommonTokenStream(lex);
        g = new SgmlParseMessagesParser(tokens);
        try {
        	g.errMessages();
        	assertEquals(88,g.eLevelMessageCount);  
        	assertEquals(4,g.wLevelMessageCount);  
        	assertEquals(0,g.iLevelMessageCount);  
        	assertEquals(0,g.qLevelMessageCount);  
        	assertEquals(0,g.xLevelMessageCount);  
        	assertEquals(198,g.totMessageCount);
        	assertEquals(198,g.openSpMessages.size());
        	assertEquals(0,g.getSgmlMessagesParseErrors().size());
        }
        catch (Exception e){
        	fail(e.getMessage());
        }
	}

	/**
	 * Test method for {@link org.jhove2.module.format.sgml.AbstractOpenSpMessageParser#createMessageString(java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testCreateMessageString() {
		String uncodedMessage = AbstractOpenSpMessageParser.createMessageString(FILENAME, LINE, POS, TEXT);
		assertEquals(UNCODEDMESSAGE, uncodedMessage);
	}

	/**
	 * Test method for {@link org.jhove2.module.format.sgml.AbstractOpenSpMessageParser#createCodedMessageString(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testCreateCodedMessageString() {
		String codedMessage = AbstractOpenSpMessageParser.createCodedMessageString(FILENAME, LINE, POS, TEXT, SEVERITY, MSGCODE);
		assertEquals(CODEDMESSAGE, codedMessage);
	}

	/**
	 * @return the conformButWarn
	 */
	public String getConformButWarn() {
		return conformButWarn;
	}

	/**
	 * @param conformButWarn the conformButWarn to set
	 */
	@Resource
	public void setConformButWarn(String conformButWarn) {
		this.conformButWarn = conformButWarn;
	}

	/**
	 * @return the conformNoErr
	 */
	public String getConformNoErr() {
		return conformNoErr;
	}

	/**
	 * @param conformNoErr the conformNoErr to set
	 */
	@Resource
	public void setConformNoErr(String conformNoErr) {
		this.conformNoErr = conformNoErr;
	}

	/**
	 * @return the noDoctype
	 */
	public String getNoDoctype() {
		return noDoctype;
	}

	/**
	 * @param noDoctype the noDoctype to set
	 */
	@Resource
	public void setNoDoctype(String noDoctype) {
		this.noDoctype = noDoctype;
	}

	/**
	 * @return the spaceBeforeDoctype
	 */
	public String getSpaceBeforeDoctype() {
		return spaceBeforeDoctype;
	}

	/**
	 * @param spaceBeforeDoctype the spaceBeforeDoctype to set
	 */
	@Resource
	public void setSpaceBeforeDoctype(String spaceBeforeDoctype) {
		this.spaceBeforeDoctype = spaceBeforeDoctype;
	}

	/**
	 * @return the unmatchedPubid
	 */
	public String getUnmatchedPubid() {
		return unmatchedPubid;
	}

	/**
	 * @param unmatchedPubid the unmatchedPubid to set
	 */
	@Resource
	public void setUnmatchedPubid(String unmatchedPubid) {
		this.unmatchedPubid = unmatchedPubid;
	}

	/**
	 * @return the unmatchedSysid
	 */
	public String getUnmatchedSysid() {
		return unmatchedSysid;
	}

	/**
	 * @param unmatchedSysid the unmatchedSysid to set
	 */
	@Resource
	public void setUnmatchedSysid(String unmatchedSysid) {
		this.unmatchedSysid = unmatchedSysid;
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

}
