/**
 * 
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
	static String CODEDMESSAGE = OpenSpMessageParser.MESSAGE_LEVEL + SEVERITY +
	                             OpenSpMessageParser.MESSAGE_CODE + MSGCODE +
	                             OpenSpMessageParser.LINE + LINE +
	                             OpenSpMessageParser.POSITION + POS +
	                             OpenSpMessageParser.MESSAGE_TEXT + TEXT;
	static String UNCODEDMESSAGE = OpenSpMessageParser.MESSAGE_LEVEL + OpenSpMessageParser.NA +
    							 OpenSpMessageParser.MESSAGE_CODE + OpenSpMessageParser.NA +
    							 OpenSpMessageParser.LINE + LINE +
    							 OpenSpMessageParser.POSITION + POS +
    							 OpenSpMessageParser.MESSAGE_TEXT + TEXT;
								 OpenSpMessageParser parser;
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
        	assertEquals(89,g.eLevelMessageCount);  
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
	 * Test method for {@link org.jhove2.module.format.sgml.OpenSpMessageParser#createMessageString(java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testCreateMessageString() {
		String uncodedMessage = OpenSpMessageParser.createMessageString(FILENAME, LINE, POS, TEXT);
		assertEquals(UNCODEDMESSAGE, uncodedMessage);
	}

	/**
	 * Test method for {@link org.jhove2.module.format.sgml.OpenSpMessageParser#createCodedMessageString(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testCreateCodedMessageString() {
		String codedMessage = OpenSpMessageParser.createCodedMessageString(FILENAME, LINE, POS, TEXT, SEVERITY, MSGCODE);
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
