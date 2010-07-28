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
package org.jhove2.app.util.traverser;

import static org.junit.Assert.*;

import javax.annotation.Resource;

import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.format.Format;
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
public class ReportableInstanceTraverserTest {

	Format testSgmlFormat;
	ReportableInstanceTraverser rit;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		rit = new ReportableInstanceTraverser("org.jhove2.module.format.xml.ValidationResults",true);
//		rit = new ReportableInstanceTraverser("org.jhove2.module.format.xml.XmlModule",true);
		//		rit = new ReportableInstanceTraverser(testSgmlFormat.getClass().getCanonicalName(),true);
//		rit = new ReportableInstanceTraverser("org.jhove2.module.format.xml.ValidationResults",true);
	}

	/**
	 * Test method for {@link org.jhove2.app.util.traverser.ReportableInstanceTraverser#extractDocInfo()}.
	 */
	@Test
	public void testExtractDocInfo() {
		try {
			rit.extractDocInfo();
			for(PropertyDoc prop:rit.getAllReportablePropertiesInfo()){
				System.out.println("\t" + prop.dottedName + "\t " + prop.typeString);
			}
//			for(PropertyDoc prop:rit.getAllReportablePropertiesInfo()){
//				System.out.println(prop.name + "\t" + prop.id + "\t " + prop.desc + "\t" );
//			}
			assertEquals(4,rit.getReportablePropertiesInfo().size());
			assertEquals(25, rit.getAllReportablePropertiesInfo().size());
		} catch (JHOVE2Exception e) {
			fail("Exception thrown");
			e.printStackTrace();
		}
	}

	/**
	 * @return the testSgmlFormat
	 */
	public Format getTestSgmlFormat() {
		return testSgmlFormat;
	}

	/**
	 * @param testSgmlFormat the testSgmlFormat to set
	 */
	@Resource
	public void setTestSgmlFormat(Format testSgmlFormat) {
		this.testSgmlFormat = testSgmlFormat;
	}

}
