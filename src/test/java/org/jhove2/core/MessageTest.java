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
package org.jhove2.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Locale;

import javax.annotation.Resource;

import org.jhove2.ConfigTestBase;
import org.jhove2.config.ConfigInfo;
import org.jhove2.core.Message.Context;
import org.jhove2.core.Message.Severity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Sheila Morrissey
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
		"classpath*:**/message-test-config.xml", 
		"classpath*:**/persist-test-config.xml",
		"classpath*:**/test-config.xml"})
public class MessageTest extends ConfigTestBase{

	private String ASCIIProfilenonBasicLatinMessage;
	private String ASCIIProfilenonBasicLatinMessageparm;
	private String UTF8ModuleFailFastMessage;
	private String FeatureExtractorCommandmoduleNotFoundMessage;
	private String FeatureExtractorCommandmoduleNotFoundMessageParam;
	private String FeatureExtractorCommandmoduleNotFormatModuleMessagePRE;
	private String FeatureExtractorCommandmoduleNotFormatModuleMessagePOST;
	private String UTF8CharacterinvalidByteValueMessages01;
	private String UTF8CharacterinvalidByteValueMessages02;
	private String UTF8CharacterinvalidByteValueMessages03;
	private JHOVE2 jhove2;

	/**
	 * Test method for {@link org.jhove2.core.Message#Message(org.jhove2.core.Message.Severity, org.jhove2.core.Message.Context, java.lang.String, ConfigInfo)}.
	 */
	@Test
	public void testMessageSeverityContextString() {
		Message message = null;
		StringBuffer sb = new StringBuffer(UTF8ModuleFailFastMessage);
		try {
			message = new Message(Severity.ERROR,
					Context.OBJECT, 
					"org.jhove2.module.format.utf8.UTF8Module.failFastMessage", jhove2.getConfigInfo());
			assertEquals(sb.toString(), message.getLocalizedMessageText());
		} catch (JHOVE2Exception e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Test method for {@link org.jhove2.core.Message#Message(org.jhove2.core.Message.Severity, org.jhove2.core.Message.Context, java.lang.String, java.lang.Object[], ConfigInfo)}.
	 */
	@Test
	public void testMessageSeverityContextStringObjectArray() {

		Locale locale = new Locale("en", "US");
		Message message = null;
		Object[] messageParms = null;
		
		StringBuffer sb = new StringBuffer(ASCIIProfilenonBasicLatinMessage);
		sb.append(ASCIIProfilenonBasicLatinMessageparm);
		try {
			messageParms = new Object[]{ASCIIProfilenonBasicLatinMessageparm};
			message = new Message(Severity.ERROR,
					Context.OBJECT, 
					"org.jhove2.module.format.utf8.ascii.ASCIIProfile.nonBasicLatinMessage",
					messageParms, locale, jhove2.getConfigInfo());
			assertEquals(sb.toString(), message.getLocalizedMessageText());
			
		sb = new StringBuffer(FeatureExtractorCommandmoduleNotFoundMessage);
		sb.append(FeatureExtractorCommandmoduleNotFoundMessageParam);
		messageParms = new Object[]{FeatureExtractorCommandmoduleNotFoundMessageParam};
		message = new Message(Severity.ERROR,
				Context.PROCESS ,
				"org.jhove2.module.format.DispatcherCommand.moduleNotFoundMessage",
				messageParms, locale, jhove2.getConfigInfo());
		assertEquals(sb.toString(), message.getLocalizedMessageText());
		
		sb = new StringBuffer(FeatureExtractorCommandmoduleNotFormatModuleMessagePRE);
		sb.append(FeatureExtractorCommandmoduleNotFoundMessageParam);
		sb.append(FeatureExtractorCommandmoduleNotFormatModuleMessagePOST);
		messageParms = new Object[]{FeatureExtractorCommandmoduleNotFoundMessageParam};
		message = new Message(Severity.ERROR,
				Context.PROCESS,
				"org.jhove2.module.format.DispatcherCommand.moduleNotFormatModuleMessage",
				(Object[])messageParms, locale, jhove2.getConfigInfo());
		assertEquals(sb.toString(), message.getLocalizedMessageText());
		
		sb = new StringBuffer(UTF8CharacterinvalidByteValueMessages01);
		int bNumber = 1;
		long offset = 333333L;
		int bValue = 5024;
		sb.append(bNumber);
		sb.append(UTF8CharacterinvalidByteValueMessages02);
		sb.append("333,333");
		sb.append(UTF8CharacterinvalidByteValueMessages03);
		sb.append("5,024");
		messageParms = new Object[]{bNumber, offset, bValue};
		message = new Message(Severity.ERROR,
				Context.OBJECT,
				"org.jhove2.module.format.utf8.UTF8Character.invalidByteValueMessages",
				messageParms, locale, jhove2.getConfigInfo());
		assertEquals(sb.toString(), message.getLocalizedMessageText());
		
		sb = new StringBuffer("Code point out of range at offset ");
		sb.append("333,333");
		sb.append(" : ");
		sb.append("5,024");
		messageParms = new Object[]{offset, bValue};
		message = new Message(Severity.ERROR,
				Context.OBJECT,
				"org.jhove2.module.format.utf8.UTF8Character.codePointOutOfRangeMessage",
				messageParms, locale, jhove2.getConfigInfo());
		assertEquals(sb.toString(), message.getLocalizedMessageText());
		
		sb = new StringBuffer("Byte Order Mark (BOM) at byte offset ");
		sb.append("333,333");
		messageParms = new Object[]{offset};
		message = new Message(Severity.INFO,
				Context.OBJECT,
				"org.jhove2.module.format.utf8.UTF8Module.bomMessage",
				messageParms, locale, jhove2.getConfigInfo());
		assertEquals(sb.toString(), message.getLocalizedMessageText());
		
		sb = new StringBuffer("DROID identification warning message: ");
		String msg = "some DROID message";
		sb.append(msg);
		messageParms = new Object[]{msg};
		message = new Message(Severity.WARNING,
				Context.OBJECT,
				"org.jhove2.module.identify.DROIDIdentifier.identify.idWarningMessage",
				messageParms, locale, jhove2.getConfigInfo());
		assertEquals(sb.toString(), message.getLocalizedMessageText());
		
		sb = new StringBuffer("DROID hit warning message: ");
		msg = "A hit warning message";
		sb.append(msg);
		messageParms = new Object[]{msg};
		message = new Message(Severity.WARNING,
				Context.OBJECT,
				"org.jhove2.module.identify.DROIDIdentifier.identify.hitWarningMsg",
				messageParms, locale, jhove2.getConfigInfo());
		assertEquals(sb.toString(), message.getLocalizedMessageText());
		
		sb = new StringBuffer("Unmatched PUID: ");
		msg = "x-fmt/392";
		sb.append(msg);
		messageParms = new Object[]{msg};
		message = new Message(Severity.ERROR,
				Context.PROCESS,
				"org.jhove2.module.identify.DROIDIdentifier.identify.missingPUID",
				messageParms, locale, jhove2.getConfigInfo());
		assertEquals(sb.toString(), message.getLocalizedMessageText());
		
		sb = new StringBuffer("No identification match on internal or external signatures found by DROID ");
		msg = "Some droid text";
		sb.append(msg);
		messageParms = new Object[]{msg};
		message = new Message(Severity.WARNING,
				Context.OBJECT,
				"org.jhove2.module.identify.DROIDIdentifier.fileNotIdentifiedMessage",
				messageParms, locale, jhove2.getConfigInfo());
		assertEquals(sb.toString(), message.getLocalizedMessageText());
		
		sb = new StringBuffer("DROID returns file-not-run message  ");
		sb.append(msg);
		message = new Message(Severity.ERROR,
				Context.PROCESS,
				"org.jhove2.module.identify.DROIDIdentifier.fileNotRunMessage",
				messageParms, locale, jhove2.getConfigInfo());
		assertEquals(sb.toString(), message.getLocalizedMessageText());
		
		sb = new StringBuffer("DROID returns file error message  ");
		sb.append(msg);
		message = new Message(Severity.ERROR,
				Context.PROCESS,
				"org.jhove2.module.identify.DROIDIdentifier.fileErrorMessage",
				messageParms, locale, jhove2.getConfigInfo());
		assertEquals(sb.toString(), message.getLocalizedMessageText());
		
		} catch (JHOVE2Exception e) {
			fail(e.getMessage());
		}
	}

	
	
	
	
	/**
	 * @return the aSCIIProfilenonBasicLatinMessage
	 */
	public String getASCIIProfilenonBasicLatinMessage() {
		return ASCIIProfilenonBasicLatinMessage;
	}

	/**
	 * @param aSCIIProfilenonBasicLatinMessage the aSCIIProfilenonBasicLatinMessage to set
	 */
	@Resource(name="ASCIIProfilenonBasicLatinMessage")
	public void setASCIIProfilenonBasicLatinMessage(
			String aSCIIProfilenonBasicLatinMessage) {
		ASCIIProfilenonBasicLatinMessage = aSCIIProfilenonBasicLatinMessage;
	}

	/**
	 * @return the aSCIIProfilenonBasicLatinMessageparm
	 */
	public String getASCIIProfilenonBasicLatinMessageparm() {
		return ASCIIProfilenonBasicLatinMessageparm;
	}

	/**
	 * @param aSCIIProfilenonBasicLatinMessageparm the aSCIIProfilenonBasicLatinMessageparm to set
	 */
	@Resource(name="ASCIIProfilenonBasicLatinMessageparm")
	public void setASCIIProfilenonBasicLatinMessageparm(
			String aSCIIProfilenonBasicLatinMessageparm) {
		ASCIIProfilenonBasicLatinMessageparm = aSCIIProfilenonBasicLatinMessageparm;
	}

	/**
	 * @return the uTF8ModuleFailFastMessage
	 */
	public String getUTF8ModuleFailFastMessage() {
		return UTF8ModuleFailFastMessage;
	}

	/**
	 * @param uTF8ModuleFailFastMessage the uTF8ModuleFailFastMessage to set
	 */
	@Resource(name="UTF8ModuleFailFastMessage")
	public void setUTF8ModuleFailFastMessage(String uTF8ModuleFailFastMessage) {
		UTF8ModuleFailFastMessage = uTF8ModuleFailFastMessage;
	}

	/**
	 * @return the featureExtractorCommandmoduleNotFoundMessage
	 */
	public String getFeatureExtractorCommandmoduleNotFoundMessage() {
		return FeatureExtractorCommandmoduleNotFoundMessage;
	}

	/**
	 * @param featureExtractorCommandmoduleNotFoundMessage the featureExtractorCommandmoduleNotFoundMessage to set
	 */
	@Resource(name="FeatureExtractorCommandmoduleNotFoundMessage")
	public void setFeatureExtractorCommandmoduleNotFoundMessage(
			String featureExtractorCommandmoduleNotFoundMessage) {
		FeatureExtractorCommandmoduleNotFoundMessage = featureExtractorCommandmoduleNotFoundMessage;
	}

	/**
	 * @return the featureExtractorCommandmoduleNotFoundMessageParam
	 */
	public String getFeatureExtractorCommandmoduleNotFoundMessageParam() {
		return FeatureExtractorCommandmoduleNotFoundMessageParam;
	}

	/**
	 * @param featureExtractorCommandmoduleNotFoundMessageParam the featureExtractorCommandmoduleNotFoundMessageParam to set
	 */
	@Resource(name="FeatureExtractorCommandmoduleNotFoundMessageParam")
	public void setFeatureExtractorCommandmoduleNotFoundMessageParam(
			String featureExtractorCommandmoduleNotFoundMessageParam) {
		FeatureExtractorCommandmoduleNotFoundMessageParam = featureExtractorCommandmoduleNotFoundMessageParam;
	}

	/**
	 * @return the featureExtractorCommandmoduleNotFormatModuleMessagePRE
	 */
	public String getFeatureExtractorCommandmoduleNotFormatModuleMessagePRE() {
		return FeatureExtractorCommandmoduleNotFormatModuleMessagePRE;
	}

	/**
	 * @param featureExtractorCommandmoduleNotFormatModuleMessagePRE the featureExtractorCommandmoduleNotFormatModuleMessagePRE to set
	 */
	@Resource(name="FeatureExtractorCommandmoduleNotFormatModuleMessagePRE")
	public void setFeatureExtractorCommandmoduleNotFormatModuleMessagePRE(
			String featureExtractorCommandmoduleNotFormatModuleMessagePRE) {
		FeatureExtractorCommandmoduleNotFormatModuleMessagePRE = featureExtractorCommandmoduleNotFormatModuleMessagePRE;
	}

	/**
	 * @return the featureExtractorCommandmoduleNotFormatModuleMessagePOST
	 */
	public String getFeatureExtractorCommandmoduleNotFormatModuleMessagePOST() {
		return FeatureExtractorCommandmoduleNotFormatModuleMessagePOST;
	}

	/**
	 * @param featureExtractorCommandmoduleNotFormatModuleMessagePOST the featureExtractorCommandmoduleNotFormatModuleMessagePOST to set
	 */
	@Resource(name="FeatureExtractorCommandmoduleNotFormatModuleMessagePOST")
	public void setFeatureExtractorCommandmoduleNotFormatModuleMessagePOST(
			String featureExtractorCommandmoduleNotFormatModuleMessagePOST) {
		FeatureExtractorCommandmoduleNotFormatModuleMessagePOST = featureExtractorCommandmoduleNotFormatModuleMessagePOST;
	}

	/**
	 * @return the uTF8CharacterinvalidByteValueMessages01
	 */
	public String getUTF8CharacterinvalidByteValueMessages01() {
		return UTF8CharacterinvalidByteValueMessages01;
	}

	/**
	 * @param uTF8CharacterinvalidByteValueMessages01 the uTF8CharacterinvalidByteValueMessages01 to set
	 */
	@Resource(name="UTF8CharacterinvalidByteValueMessages01")
	public void setUTF8CharacterinvalidByteValueMessages01(
			String uTF8CharacterinvalidByteValueMessages01) {
		UTF8CharacterinvalidByteValueMessages01 = uTF8CharacterinvalidByteValueMessages01;
	}

	/**
	 * @return the uTF8CharacterinvalidByteValueMessages02
	 */
	public String getUTF8CharacterinvalidByteValueMessages02() {
		return UTF8CharacterinvalidByteValueMessages02;
	}

	/**
	 * @param uTF8CharacterinvalidByteValueMessages02 the uTF8CharacterinvalidByteValueMessages02 to set
	 */
	@Resource(name="UTF8CharacterinvalidByteValueMessages02")
	public void setUTF8CharacterinvalidByteValueMessages02(
			String uTF8CharacterinvalidByteValueMessages02) {
		UTF8CharacterinvalidByteValueMessages02 = uTF8CharacterinvalidByteValueMessages02;
	}

	/**
	 * @return the uTF8CharacterinvalidByteValueMessages03
	 */
	public String getUTF8CharacterinvalidByteValueMessages03() {
		return UTF8CharacterinvalidByteValueMessages03;
	}

	/**
	 * @param uTF8CharacterinvalidByteValueMessages03 the uTF8CharacterinvalidByteValueMessages03 to set
	 */
	@Resource(name="UTF8CharacterinvalidByteValueMessages03")
	public void setUTF8CharacterinvalidByteValueMessages03(
			String uTF8CharacterinvalidByteValueMessages03) {
		UTF8CharacterinvalidByteValueMessages03 = uTF8CharacterinvalidByteValueMessages03;
	}

	/**
	 * @return the jhove2
	 */
	public JHOVE2 getJhove2() {
		return jhove2;
	}

	/**
	 * @param jhove2 the jhove2 to set
	 */
	@Resource(name="JHOVE2")
	public void setJhove2(JHOVE2 jhove2) {
		this.jhove2 = jhove2;
	}

}
