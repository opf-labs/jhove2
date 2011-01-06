/**
 * JHOVE2 - Next-generation architecture for format-aware characterization
 * <p>
 * Copyright (c) 2011 by The Regents of the University of California, Ithaka
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

package org.jhove2.module.format.icc;

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
 * Tests of ICC Module
 * 
 * @see org.jhove2.module.icc.ICCModule
 * @author slabrams
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:**/j2test-icc-config.xml",
        "classpath*:**/test-config.xml", "classpath*:**/filepaths-config.xml" })
        
public class ICCModuleTestBase {

    protected ICCModule testIccModule;
    private JHOVE2 JHOVE2;
    private String iccDirBasePath;
    protected boolean initialized;

    public ICCModule getTestIccModule() {
        return this.testIccModule;
    }

    @Resource(name = "TestIccModule")
    public void setTestIccModule(ICCModule testIccModule) {
        this.testIccModule = testIccModule;
    }

    public JHOVE2 getJHOVE2() {
        return this.JHOVE2;
    }

    @Resource
    public void setJHOVE2(JHOVE2 jhove2) {
        this.JHOVE2 = jhove2;
    }

    public String getIccDirBasePath() {
        return this.iccDirBasePath;
    }

    @Resource
    public void setIccDirBasePath(String iccDirBasePath) {
        this.iccDirBasePath = iccDirBasePath;
    }

    protected void parse(String relativePath) {
        String iccExampleDirPath = null;
        try {
            iccExampleDirPath =
                FeatureConfigurationUtil.getFilePathFromClasspath(this.iccDirBasePath,
                                                                  "ICC examples base directory");
        }
        catch (JHOVE2Exception e1) {
            fail("Could not create base directory");
        }
        File iccExampleDir = new File(iccExampleDirPath);
        assertTrue(iccExampleDir.exists());
        File testFile = new File(iccExampleDirPath, relativePath);
        assertTrue(testFile.exists());
        try {
            FileSource fileSource = new FileSource(testFile);
            Input input = fileSource.getInput(this.JHOVE2);
            this.testIccModule.parse(this.JHOVE2, fileSource, input);
        }
        catch (Exception e) {
            // fail("Exception thrown: " + e.getMessage());
        }
    }

    /**
     * Test method for ICC Declaration information
     */
    @Test
    public void testTruth() {
        assertTrue(true);
    }
}
