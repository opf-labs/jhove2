/**
 * JHOVE2 - Next-generation architecture for format-aware characterization
 * <p>
 * Copyright (c) 2009 by The Regents of the University of California, Ithaka
 * Harbors, Inc., and The Board of Trustees of the Leland Stanford Junior
 * University. All rights reserved.
 * </p>
 * <p>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * </p>
 * <ul>
 * <li>Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.</li>
 * <li>Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.</li>
 * <li>Neither the name of the University of California/California Digital
 * Library, Ithaka Harbors/Portico, or Stanford University, nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.</li>
 * </ul>
 * <p>
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
 * </p>
 */
package org.jhove2.module.format.xml;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.jhove2.app.util.FeatureConfigurationUtil;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.source.FileSource;
import org.jhove2.module.format.Validator.Validity;
import org.jhove2.module.format.xml.NumericCharacterReferences.NumericCharacterReference;
import org.jhove2.module.format.xml.ValidationResults.ValidationMessageList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Tests of XML Module
 * @see org.jhove2.module.xml.XmlModule
 * @author rnanders
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:**/xml-test-config.xml",
        "classpath*:**/test-config.xml", "classpath*:**/filepaths-config.xml" })
public class XmlCharacterReferenceTest {

    private XmlModule testXmlModule;
    private JHOVE2 JHOVE2;
    private String xmlDirBasePath;
    private boolean initialized;

    public XmlModule getTestXmlModule() {
        return testXmlModule;
    }

    @Resource(name = "TestXmlModule")
    public void setTestXmlModule(XmlModule xmlModule) {
        this.testXmlModule = xmlModule;
    }

    public JHOVE2 getJHOVE2() {
        return JHOVE2;
    }

    @Resource
    public void setJHOVE2(JHOVE2 jHOVE2) {
        JHOVE2 = jHOVE2;
    }

    public String getXmlDirBasePath() {
        return xmlDirBasePath;
    }

    @Resource
    public void setXmlDirBasePath(String xmlDirBasePath) {
        this.xmlDirBasePath = xmlDirBasePath;
    }

    private void parse(String relativePath) {
        String xmlExampleDirPath = null;
        try {
            xmlExampleDirPath = FeatureConfigurationUtil
                    .getFilePathFromClasspath(xmlDirBasePath,
                            "XML examples base directory");
        }
        catch (JHOVE2Exception e1) {
            fail("Could not create base directory");
        }
        File xmlExampleDir = new File(xmlExampleDirPath);
        assertTrue(xmlExampleDir.exists());
        File testFile = new File(xmlExampleDirPath, relativePath);
        assertTrue(testFile.exists());
        try {
            FileSource fileSource = new FileSource(testFile);
            testXmlModule.parse(JHOVE2, fileSource);
        }
        catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }        
    }
    
    @Before
    public void initialize() {
        if (! initialized) {
            parse("samples/character-reference-sample.xml");
        }
    }

    /**
     * Test method for SAX Parser information
     */
    @Test
    public void testSaxParserInfo() {
        SaxParser saxParser = testXmlModule.saxParser;
        assertEquals("org.apache.xerces.parsers.SAXParser",saxParser.parser);
        List<String> features = saxParser.getSaxFeatures();
        assertTrue(features.contains("http://xml.org/sax/features/validation = true"));
        List<String> properties = saxParser.getSaxProperties();
        assertTrue(properties.contains("http://xml.org/sax/properties/lexical-handler = org.jhove2.module.format.xml.SaxParserLexicalHandler"));
    }

    /**
     * Test method for XML Declaration information
     */
    @Test
    public void testXmlDeclaration() {
        XmlDeclaration xmlDecl = testXmlModule.xmlDeclaration;
        assertEquals("1.0",xmlDecl.getVersion());
        assertEquals("UTF-8",xmlDecl.getEncoding());
        assertEquals("yes",xmlDecl.getStandalone());
    }

    /**
     * Test method for root element information
     */
    @Test
    public void testRootElement() {
        RootElement root = testXmlModule.rootElement;
        assertEquals("characterReferences",root.getName());
        assertEquals("[noNamespace]",root.getNamespace());
    }

    /**
     * Test method for namespace information
     */
    @Test
    public void testNamespaceInfo() {
        NamespaceInformation nsInfo = testXmlModule.getNamespaceInformation();
        assertEquals(0,nsInfo.getNamespaceCount());
    }

    
    /**
     * Test method for Character Entity References
     */
    @Test
    public void testCharacterEntityReferences() {
        TreeMap<String, EntityReferences.EntityReference> erMap = testXmlModule.entityReferences.entityReferenceMap;
        assertEquals(5,erMap.size());
        assertEquals(5, erMap.get("amp").getCount().intValue());
        assertEquals(4, erMap.get("apos").getCount().intValue());
        assertEquals(2, erMap.get("gt").getCount().intValue());
        assertEquals(3, erMap.get("lt").getCount().intValue());
        assertEquals(1, erMap.get("quot").getCount().intValue());
    }

    /**
     * Test method for Numeric Character References
     */   
    @Test
    public void testNumericCharacterReferences() {
        TreeMap<Integer, NumericCharacterReferences.NumericCharacterReference> ncrMap = testXmlModule.numericCharacterReferences.numericCharacterReferenceMap;
        assertEquals(6, ncrMap.size());
        assertEquals(3, ncrMap.get(0x0009).getCount().intValue());
        assertEquals(1, ncrMap.get(0x000A).getCount().intValue());
        assertEquals(1, ncrMap.get(0x000D).getCount().intValue());
        assertEquals(1, ncrMap.get(0x00B6).getCount().intValue());
        assertEquals(2, ncrMap.get(0x2021).getCount().intValue());
        assertEquals(1, ncrMap.get(0x12415).getCount().intValue());
    }

    /**
     * Test method for Comment Information
     */   
    @Test
    public void testCommentInfo() {
        CommentInformation commentInfo = testXmlModule.getCommentInformation();
        assertEquals(2, commentInfo.getCount());
        ArrayList<String> comments = commentInfo.getComments();
        assertTrue(comments.contains(" Character Entity References "));
        assertTrue(comments.contains(" Numeric Character References (Unicode code points) "));
    }

    /**
     * Test method for Validation Information
     */   
    @Test
    public void testValidationInfo() {
        ValidationResults vr = testXmlModule.getValidationResults();
        ValidationMessageList warnings = vr.getParserWarnings();
        assertEquals(0, warnings.getValidationMessageCount());
        ValidationMessageList errors = vr.getParserErrors();
        assertEquals(0, errors.getValidationMessageCount());
        ValidationMessageList fatals = vr.getFatalParserErrors();
        assertEquals(0, fatals.getValidationMessageCount());
    }

    /**
     * Test method for Validity
     */   
    @Test
    public void testValidatity() {
        assertTrue(testXmlModule.isWellFormed());
        assertEquals(Validity.True, testXmlModule.isValid());
    }
 
}
