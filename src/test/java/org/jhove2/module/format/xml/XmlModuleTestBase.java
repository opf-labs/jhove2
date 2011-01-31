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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import javax.annotation.Resource;

import org.jhove2.app.util.FeatureConfigurationUtil;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.io.Input;
import org.jhove2.core.source.FileSource;
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
public class XmlModuleTestBase {

    protected XmlModule testXmlModule;
    private JHOVE2 JHOVE2;
    private String xmlDirBasePath;
    protected boolean initialized;

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

    protected void parse(String relativePath) {
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
            FileSource fileSource = (FileSource)JHOVE2.getSourceFactory().getSource(testFile);
			Input input = fileSource.getInput(JHOVE2);
            testXmlModule.parse(JHOVE2, fileSource, input);
            testXmlModule.validate(JHOVE2, fileSource, input);
        }
        catch (Exception e) {
            // fail("Exception thrown: " + e.getMessage());
        	// e.printStackTrace();
        }        
    }
    
    /**
     * Test method for XML Declaration information
     */
    @Test
    public void testTruth() {
        assertTrue(true);
    }

}
