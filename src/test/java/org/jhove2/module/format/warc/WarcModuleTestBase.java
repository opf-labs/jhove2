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
package org.jhove2.module.format.warc;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.jhove2.ConfigTestBase;
import org.jhove2.app.util.FeatureConfigurationUtil;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.Message;
import org.jhove2.core.io.Input;
import org.jhove2.core.source.FileSource;
import org.jhove2.core.source.Source;
import org.jhove2.module.format.Validator.Validity;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Tests of WARC Module
 * @see org.jhove2.module.warc.WarcModule
 * @author nicl
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath*:**/warc-test-config.xml",
		"classpath*:**/persist-test-config.xml",
        "classpath*:**/test-config.xml",
        "classpath*:**/filepaths-config.xml" })
public class WarcModuleTestBase extends ConfigTestBase {

    protected JHOVE2 JHOVE2;

    protected String warcDirBasePath;

	protected WarcModule warcModuleMold;

	@BeforeClass 
	public static void setUpBeforeClass() throws Exception {
    	ArrayList<String> paths = new ArrayList<String>();
    	paths.add("classpath*:**/warc-test-config.xml");
    	paths.add("classpath*:**/persist-test-config.xml");
    	paths.add("classpath*:**/test-config.xml");
    	ConfigTestBase.setCONTEXT_PATHS(paths);
    	ConfigTestBase.setUpBeforeClass();
    }

    public JHOVE2 getJHOVE2() {
        return JHOVE2;
    }

    @Resource
    public void setJHOVE2(JHOVE2 jHOVE2) {
        JHOVE2 = jHOVE2;
    }

    public String getWarcDirBasePath() {
        return warcDirBasePath;
    }

    @Resource
    public void setWarcDirBasePath(String warcDirBasePath) {
        this.warcDirBasePath = warcDirBasePath;
    }

    public WarcModule getWarcModule() {
    	return warcModuleMold;
    }

    @Resource(name = "WarcModule")
    public void setWarcModule(WarcModule warcModule) {
    	this.warcModuleMold = warcModule;
    }

    /**
     * Test method for UTF8 Declaration information
     */
    @Test
    public void testTruth() {
        assertTrue(true);
    }

    public void test_cases(Object[][] cases, boolean debug) {
        String warcExampleDirPath = null;
        // debug
        //System.out.println("warcDirBasePath: " + warcDirBasePath);
        try {
            warcExampleDirPath = FeatureConfigurationUtil
                    .getFilePathFromClasspath(warcDirBasePath,
                            "WARC examples base directory");
        }
        catch (JHOVE2Exception e) {
            fail("Could not create base directory");
        }
        File warcExampleDir = new File(warcExampleDirPath);
        // debug
        //System.out.println(warcExampleDir.getPath());
        assertTrue("Directory does not exist: " + warcExampleDir.getPath(), warcExampleDir.exists());

    	WarcModule warcModule;
    	long consumed;
    	Validity validity;

    	for (int i=0; i<cases.length; ++i) {
        	Validity expectedValidity = (Validity)cases[i][0];
        	String testFilename = (String)cases[i][1];
        	String[][] expectedRecordMessages = (String[][])cases[i][2];
        	String[] expectedReaderMessages = (String[])cases[i][3];

            File testFile = new File(warcExampleDirPath, testFilename);
            //System.out.println(testFile.getPath());
            assertTrue("File does not exist: " + testFile.getPath(), testFile.exists());
            try {
                FileSource fileSource = (FileSource)JHOVE2.getSourceFactory().getSource(JHOVE2, testFile);
    			Input input = fileSource.getInput(JHOVE2);
            	warcModule = warcModuleMold.getTestInstance();
    			consumed = warcModule.parse(JHOVE2, fileSource, input);
    			validity = warcModule.validate(JHOVE2, fileSource, input);

    			/*
    			System.out.println(" " + testFilename);
    			System.out.println(" " + consumed);
    			System.out.println(" " + validity.toString());
    			*/

    			if (!debug) {
        			Assert.assertEquals(expectedValidity, validity);
    			}

    			List<Message> messages = fileSource.getMessages();
    			if (!debug) {
        			Assert.assertEquals(expectedReaderMessages.length, messages.size());
    			}

    			for (int j=0; j<messages.size(); ++j) {
        			if (!debug) {
        				Assert.assertEquals(expectedReaderMessages[j], messages.get(j).toString());
        			} else {
        				System.out.println(" " + messages.get(j));
	    			}
    			}

    			List<Source> children = fileSource.getChildSources();
    			if (!debug) {
        			Assert.assertEquals(expectedRecordMessages.length, children.size());
    			}

    			for (int j=0; j<children.size(); ++j) {
    				messages = children.get(j).getMessages();
        			if (!debug) {
        				Assert.assertEquals(expectedRecordMessages[j].length, messages.size());
        			} else {
        				System.out.println(" >" + j);
	    			}
    				for (int k=0; k<messages.size(); ++k) {
    	    			if (!debug) {
            				Assert.assertEquals(expectedRecordMessages[j][k], messages.get(k).toString());
    	    			} else {
            				System.out.println("  " + messages.get(k));
    	    			}

        			}
    			}
            }
            catch (Exception e) {
                fail("Exception thrown: " + e.getMessage());
            }        
        }
    }

    // To be continued...

    /*
    try {
    	//fileSource.addModule( warcModule );
    	//warcModule.setParentSourceId( fileSource.getSourceId() );
    	//warcModule = (WarcModule)warcModule.getModuleAccessor().persistModule( warcModule );

    	//System.out.println( warcModule.isValid() );
		//List<Source> children = fileSource.getChildSources();

        //Displayer displayer = new TextDisplayer();
        //displayer.setConfigInfo(JHOVE2.getConfigInfo());
		//displayer.display(fileSource);
	}
    catch (FileNotFoundException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
    catch (UnsupportedEncodingException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
    catch (JHOVE2Exception e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	*/

    /*
    public void display() {
        try {
            input.setPosition(0);
            JHOVE2.characterize(fileSource, input);
            Displayer displayer = new TextDisplayer();
            displayer.setConfigInfo(JHOVE2.getConfigInfo());
            displayer.display(fileSource);          
        }
        catch (Exception e){
            e.printStackTrace();
            fail("exception");
        }
    }
    */

}
