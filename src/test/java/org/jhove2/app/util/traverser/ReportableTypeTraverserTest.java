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

import org.jhove2.app.util.traverser.ReportableTypeTraverser;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.module.format.sgml.SgmlModule;
import org.jhove2.module.format.xml.XmlModule;
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
public class ReportableTypeTraverserTest {
	
	public static final String CLASS_JHOVE2 = "org.jhove2.core.JHOVE2";
	public static final String CLASS_SGML = "org.jhove2.module.format.sgml.SgmlModule";
	public static final String CLASS_XML = "org.jhove2.module.format.xml.XmlModule";
	
	public static final String NAME_JHOVE2 = "JHOVE2";
	public static final String NAME_SGML = "SgmlModule";
	public static final String NAME_XML = "XmlModule";
	
	public static final String ID_JHOVE2 = "[JHOVE2] http://jhove2.org/terms/reportable/org/jhove2/core/JHOVE2";
	public static final String ID_SGML = "[JHOVE2] http://jhove2.org/terms/reportable/org/jhove2/module/format/sgml/SgmlModule";
	public static final String ID_XML = "[JHOVE2] http://jhove2.org/terms/reportable/org/jhove2/module/format/xml/XmlModule";
	
	public static final String[]REPORTABLE_JHOVE2 = {CLASS_JHOVE2,"org.jhove2.core.source.SourceCounter","org.jhove2.module.Command",
		"org.jhove2.core.Invocation","org.jhove2.core.Installation",
		"org.jhove2.core.Agent", "org.jhove2.core.TimerInfo","org.jhove2.core.WrappedProduct"};
	

	protected JHOVE2 JHOVE2;
	protected SgmlModule testSgmlModule;
	protected XmlModule XmlModule;
	
	protected ReportableTypeTraverser mdj;
	protected ReportableTypeTraverser mds;
	protected ReportableTypeTraverser mdx;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		mdj = new ReportableTypeTraverser(JHOVE2.getClass().getCanonicalName(), true);
		mds = new ReportableTypeTraverser(testSgmlModule.getClass().getCanonicalName(), true);
		mdx = new ReportableTypeTraverser(XmlModule.getClass().getCanonicalName(), true);
	}

	/**
	 * Test method for {@link org.jhove2.app.util.traverser.ReportableTypeTraverser#extractDocInfo()}.
	 */
	@Test
	public void testExtractDocInfo() {
		try {
			mdj.setReportableFeaturesDocumenter(mdj.extractDocInfo());
			for (String key:mdj.reportableFeaturesDocumenter.keySet()){
				ReportableTypeTraverser mdd = mdj.reportableFeaturesDocumenter.get(key);
				System.out.println(key);
				for (PropertyDoc prop:mdd.getReportablePropertiesInfo()){
					System.out.println("\t" + prop.dottedName + " " + prop.typeString);
				}
			}
//			assertEquals(REPORTABLE_JHOVE2.length, mdj.getReportableFeaturesDocumenter().keySet().size());
			for (String rFeature :REPORTABLE_JHOVE2){
				assertTrue( mdj.getReportableFeaturesDocumenter().keySet().contains(rFeature));
			}
				
		} catch (JHOVE2Exception e) {
			fail("Exception thrown");
			e.printStackTrace();
		}
		
	}
	/**
	 * Test method for {@link org.jhove2.app.util.traverser.ReportableTypeTraverser#extractReportableInfo()}.
	 */
	@Test
	public void testExtractReportableInfo() {
		try {
			mdj.setReportableInfo(mdj.extractReportableInfo());
		} catch (JHOVE2Exception e) {
			e.printStackTrace();
			fail("Exception");
		}
		assertEquals(NAME_JHOVE2, mdj.getReportableInfo().name);
		assertEquals(CLASS_JHOVE2, mdj.getReportableInfo().className);
		assertEquals(ID_JHOVE2, mdj.getReportableInfo().id);
		
		try {
			mds.setReportableInfo(mds.extractReportableInfo());
		} catch (JHOVE2Exception e) {
			e.printStackTrace();
			fail("Exception");
		}
		assertEquals(NAME_SGML, mds.getReportableInfo().name);
		assertEquals(CLASS_SGML, mds.getReportableInfo().className);
		assertEquals(ID_SGML, mds.getReportableInfo().id);
		
		try {
			mdx.setReportableInfo(mdx.extractReportableInfo());
		} catch (JHOVE2Exception e) {
			e.printStackTrace();
			fail("Exception");
		}
		assertEquals(NAME_XML, mdx.getReportableInfo().name);
		assertEquals(CLASS_XML, mdx.getReportableInfo().className);
		assertEquals(ID_XML, mdx.getReportableInfo().id);
	}


	/**
	 * @return the jHOVE2
	 */
	public JHOVE2 getJHOVE2() {
		return JHOVE2;
	}

	/**
	 * @param jHOVE2 the jHOVE2 to set
	 */
	@Resource
	public void setJHOVE2(JHOVE2 jHOVE2) {
		JHOVE2 = jHOVE2;
	}

	/**
	 * @return the testSgmlModule
	 */
	public SgmlModule getTestSgmlModule() {
		return testSgmlModule;
	}

	/**
	 * @param testSgmlModule the testSgmlModule to set
	 */
	@Resource
	public void setTestSgmlModule(SgmlModule testSgmlModule) {
		this.testSgmlModule = testSgmlModule;
	}

	/**
	 * @return the xmlModule
	 */
	public XmlModule getXmlModule() {
		return XmlModule;
	}

	/**
	 * @param xmlModule the xmlModule to set
	 */
	@Resource
	public void setXmlModule(XmlModule xmlModule) {
		XmlModule = xmlModule;
	}

}
