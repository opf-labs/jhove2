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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.Message;
import org.jhove2.core.Message.Context;
import org.jhove2.core.Message.Severity;
import org.jhove2.core.source.Source;
import org.jhove2.util.CopyUtils;

/**
 * Class to process ESIS output from ongmls.
 * Uses lexer and parser classes generated by ANTLR.
 * Also contains static utility methods used by generated
 * parser code to accumulate information about properties in 
 * the ESIS file.
 * @author smorrissey
 */
public class EsisParser {

	/** prefix for parser error messages to indicate which grammar generated parse errors */
	public static final String ESISERR = "ESIS: ";

	/**
	 * Invokes ANTLR parser on ESIS file passed as input parameter.
	 * Returns parser object which, after parse, maintains accumulated
	 * feature information about the SGML file, which will be accessed
	 * by the SgmlModule class to report properties about the file
	 * @param esisPath String containing path to onsmls ESIS output
	 * @param sgm SgmlModule to which messages may be attached
	 * @param JHOVE2 jhove2 object with ConfigInfo
	 * @param Source object to which messages may be attached
	 * @return parser object with information about the SGML instance.
	 * @throws JHOVE2Exception
	 * @throws IOException 
	 * @throws RecognitionException 
	 */
	public ESISCommandsParser parseEsisFile(String esisPath, JHOVE2 jhove2, Source source, SgmlModule sgm)
	throws JHOVE2Exception, IOException, RecognitionException {
		ESISCommandsLexer lex = null;
		ESISCommandsParser parser = null;
		try {
			lex = new ESISCommandsLexer(new ANTLRFileStream(esisPath, "UTF8"));
		} catch (IOException e) {
			String eMessage = e.getLocalizedMessage();
			if (eMessage==null){
				eMessage = "";
			}
			Object[]messageArgs = new Object[]{esisPath, eMessage};
			Message message = new Message(
					Severity.ERROR,
					Context.PROCESS,
					"org.jhove2.module.format.sgml.EsisParser.IOExceptionForEsisLexer",
					messageArgs,
					jhove2.getConfigInfo());
			sgm.getSgmlParserErrorMessages().add(message);
			throw e;
		}
		CommonTokenStream tokens = new CommonTokenStream(lex);
		parser = new ESISCommandsParser(tokens);
		try {
			parser.esis();
		} catch (RecognitionException e) {
			String eMessage = e.getLocalizedMessage();
			if (eMessage==null){
				eMessage = "";
			}
			Object[]messageArgs = new Object[]{esisPath, eMessage};
			Message message = new Message(
					Severity.ERROR,
					Context.PROCESS,
					"org.jhove2.module.format.sgml.EsisParser.RecognitionExceptionForEsisLexer",
					messageArgs,
					jhove2.getConfigInfo());
			sgm.getSgmlParserErrorMessages().add(message);
			throw e;
		}
		return parser;		
	}
	/**
	 * Method to extract fields from ANTLR parser and make deep copy into SgmlDocumentProperties object.
	 * Clears those objects in the ANTLR parser.
	 * @param esisParser ANTLR parser object containing extracted data
	 * @param props updated SgmlDocumentProperties object with content of ANTLR parser fields
	 */
	public void extractDocProperties(ESISCommandsParser esisParser, SgmlDocumentProperties props){
		if (esisParser.getEsisParseErrors() != null){
			if (props.getParseErrors() == null){
				props.setParseErrors(new ArrayList<String>());
			}
			props.getParseErrors().addAll(
					CopyUtils.copyAndClearList(esisParser.getEsisParseErrors()));
		}		
		props.setAppInfoCount(esisParser.appInfoCount);
		props.setAppInfos(CopyUtils.copyAndClearList(esisParser.appInfos));
		props.setCommentsCount(esisParser.commentsCount);
		props.setDataAttrCount(esisParser.dataAttrCount);
		props.setDataAttributeType2Count(CopyUtils.copyAndClearIntMap(esisParser.dataAttributeType2Count));
		props.setDataCount(esisParser.dataCount);
		props.setElemAttributeType2Count(CopyUtils.copyAndClearIntMap(esisParser.elemAttributeType2Count));
		props.setElementAttributeCount(esisParser.elementAttributeCount);
		props.setElementCount(esisParser.elementCount);
		props.setElementNames(CopyUtils.copyAndClearSet(esisParser.elementNames));
		props.setEmptyElementsCount(esisParser.emptyElementsCount);
		props.setEntRefNames(CopyUtils.copyAndClearSet(esisParser.entRefNames));
		props.setEntityFileNamesCount(esisParser.fileNamesCount);
		props.setEntrefCount(esisParser.entrefCount);
		props.setExtDataEntCount(esisParser.extDataEntCount);
		props.setExtDataEntNames(CopyUtils.copyAndClearSet(esisParser.extDataEntNames));
		props.setExtEntFileNames(CopyUtils.copyAndClearSet(esisParser.extEntFileNames));
		props.setExtEntName2dataAttrNames(CopyUtils.copyAndClearListMap(esisParser.extEntName2dataAttrNames));
		props.setExtEntSysidNames(CopyUtils.copyAndClearSet(esisParser.extEntSysidNames));
		props.setExtTextEntCount(esisParser.extTextEntCount);
		props.setExtTextEntNames(CopyUtils.copyAndClearSet(esisParser.extTextEntNames));
		props.setIncludedSubElementsCount(esisParser.includedSubElementsCount);
		props.setIntDataEntCount(esisParser.intDataEntCount);
		props.setIntEnt2Type(CopyUtils.copyAndClearStringMap(esisParser.intEnt2Type));
		props.setInternalDataEntitytName2Value(CopyUtils.copyAndClearStringMap(esisParser.intEnt2Value));
		props.setInternalEntType2Count(CopyUtils.copyAndClearIntMap(esisParser.intEntType2Count));
		props.setLinkAttrCount(esisParser.linkAttrCount);
		props.setLinkAttributeType2Count(CopyUtils.copyAndClearIntMap(esisParser.linkAttributeType2Count));
		props.setNotatDefCount(esisParser.notatDefCount);
		props.setNotatNames(CopyUtils.copyAndClearSet(esisParser.notatNames));
		props.setOmitCommandCount(esisParser.omitCommandCount);
		props.setProcessingInstructionsCount(esisParser.piCount);
		props.setProcessingInstructions(CopyUtils.copyAndClearList(esisParser.progInstructions));
		props.setPubIds(CopyUtils.copyAndClearSet(esisParser.pubIds));
		props.setPublicIdCount(esisParser.publicIdCount);
		props.setRootElementName(esisParser.rootElementName);
		props.setsDataCount(esisParser.sDataCount);
		props.setSdataNames(CopyUtils.copyAndClearSet(esisParser.sdataNames));
		props.setSubDocCommandCount(esisParser.subDocCommandCount);
		props.setSubDocCommandNames(CopyUtils.copyAndClearSet(esisParser.subDocCommandNames));
		props.setSubDocEntDefNames(CopyUtils.copyAndClearSet(esisParser.subDocEntDefNames));
		props.setSubDocEntityDefCount(esisParser.subDocEntityDefCount);
		props.setSysidsCount(esisParser.sysidsCount);
		props.setSgmlValid(esisParser.isSgmlValid);		
		return;
	}

	/**
	 * Utility method to update list in a map; used in ESIS grammar
	 * @param map Map<String, List<String>> of lists to be updated
	 * @param key String key to list to be updated
	 * @param newItem String item to be added to target list
	 */
	public static void updateMapList(HashMap<String, List<String>>map, String key, String newItem){
		List<String> list = map.get(key);
		if (list==null){
			list = new ArrayList<String>();
		}
		list.add(newItem);
		map.put(key,list);
		return;
	}

	/**
	 * Utility method to update counter value in a map; used in ESIS grammar
	 * @param map Map<String, Integer> with counters to be updated
	 * @param key String key to counter to be updated
	 */
	public static void updateMapCounter(HashMap<String, Integer> map, String key){
		Integer val = map.get(key);
		if (val==null){
			val=0;
		}
		int iVal = val.intValue();
		iVal++;
		val = Integer.valueOf(iVal);
		map.put(key, val);
		return;
	}
}
