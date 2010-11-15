/**
 * JHOVE2 - Next-generation architecture for format-aware characterization
 * <p>
 * Copyright (c) 2010 by The Regents of the University of California, Ithaka
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
package org.jhove2.util.externalprocess;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import javax.annotation.Resource;

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
public class ForkShellHandlerTest {

	protected ForkShellHandler shellHandler;
	protected ForkShellHandler windowsShellHandler;
	protected String tempDirBasePath;
	protected String tempDirPath;
	protected File tempFile;
	protected String tempFilePath;
	protected FilepathFilter filter;
	public static final String HELLO = "HELLO";
	public static final String COMMANDBASE = "echo " + HELLO +  ">";
	protected String wtempDirBasePath;
	protected String wtempDirPath;
	protected File wtempFile;
	protected String wtempFilePath;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		try {
			tempDirPath = 
				FeatureConfigurationUtil.getFilePathFromClasspath(tempDirBasePath, "temp dir");
		} catch (JHOVE2Exception e1) {
			fail("Could not create base directory");
		}
		tempFile = File.createTempFile("ForkShellHandlerTest", ".txt", 
				new File(tempDirPath));
		tempFile.deleteOnExit();
		tempFilePath = tempFile.getAbsolutePath();
		if (filter != null){
			tempFilePath = filter.filter(tempFilePath);
		}
		try {
			wtempDirPath = 
				FeatureConfigurationUtil.getFilePathFromClasspath(tempDirBasePath, "wtemp dir");
		} catch (JHOVE2Exception e1) {
			fail("Could not create base directory");
		}
		wtempFile = File.createTempFile("ForkShellHandlerTest", ".txt", 
				new File(wtempDirPath));
		wtempFile.deleteOnExit();
		wtempFilePath = wtempFile.getAbsolutePath();
	}

	/**
	 * Test method for {@link org.jhove2.util.externalprocess.ForkShellHandler#executeCommand(java.lang.String)}.
	 */
	@Test
	public void testExecuteCommand() {
		String command = COMMANDBASE + tempFilePath ;
		try {
			shellHandler.executeCommand(command);
		} catch (JHOVE2Exception e) {
			e.printStackTrace();
			fail("Shell Handler threw exception");
		} catch (NoSuchShellEnvException e) {
			e.printStackTrace();
			fail("Shell Handler threw NoSuchShellEnvException");
		}
		assertTrue(tempFile.exists());
		long lngth = tempFile.length();
		assertTrue(lngth>0);
		BufferedReader reader = null;
		try {
			reader = 
				new BufferedReader(new FileReader(tempFilePath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail("Exception creating Reader on shell output file");
		}
		StringBuffer fileContents = new StringBuffer();
		String input = null;
		try {
			input = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			fail("Exception reading Reader on shell output file");
		}
		while (input != null){							
			fileContents.append(input);
			try {
				input = reader.readLine();
			} catch (IOException e) {
				break;
			}
		};
		String strContents = fileContents.toString();
		assertEquals(HELLO, strContents);
		// now do it for Windows
		Properties prop   = System.getProperties();
		String os = prop.getProperty("os.name");
		if (os.toLowerCase().startsWith("win")){
			command = COMMANDBASE + wtempFilePath ;
			try {
				windowsShellHandler.executeCommand(command);
			} catch (NoSuchShellEnvException e) {
				e.printStackTrace();
				fail("Shell Handler threw NoSuchShellEnvException");
			} catch (JHOVE2Exception e) {
				e.printStackTrace();
				fail("Shell Handler threw exception");
			}
			assertTrue(wtempFile.exists());
			lngth = wtempFile.length();
			assertTrue(lngth>0);
			reader = null;
			try {
				reader = 
					new BufferedReader(new FileReader(wtempFilePath));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				fail("Exception creating Reader on shell output file");
			}
			fileContents = new StringBuffer();
			input = null;
			try {
				input = reader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
				fail("Exception reading Reader on shell output file");
			}
			while (input != null){							
				fileContents.append(input);
				try {
					input = reader.readLine();
				} catch (IOException e) {
					break;
				}
			};
			strContents = fileContents.toString();
			assertEquals(HELLO, strContents);
		}
	}
	/**
	 * @return the shellHandler
	 */
	public ForkShellHandler getShellHandler() {
		return shellHandler;
	}

	/**
	 * @param shellHandler the shellHandler to set
	 */
	@Resource
	public void setShellHandler(ForkShellHandler shellHandler) {
		this.shellHandler = shellHandler;
	}

	/**
	 * @return the tempDirBasePath
	 */
	public String getTempDirBasePath() {
		return tempDirBasePath;
	}

	/**
	 * @param tempDirBasePath the tempDirBasePath to set
	 */
	@Resource
	public void setTempDirBasePath(String tempDirBasePath) {
		this.tempDirBasePath = tempDirBasePath;
	}

	/**
	 * @return the filter
	 */
	public FilepathFilter getFilter() {
		return filter;
	}

	/**
	 * @param filter the filter to set
	 */
	@Resource
	public void setFilter(FilepathFilter filter) {
		this.filter = filter;
	}

	/**
	 * @return the windowsShellHandler
	 */
	public ForkShellHandler getWindowsShellHandler() {
		return windowsShellHandler;
	}

	/**
	 * @param windowsShellHandler the windowsShellHandler to set
	 */
	@Resource
	public void setWindowsShellHandler(ForkShellHandler windowsShellHandler) {
		this.windowsShellHandler = windowsShellHandler;
	}

}
