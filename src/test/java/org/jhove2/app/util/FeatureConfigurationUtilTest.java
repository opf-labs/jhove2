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


package org.jhove2.app.util;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.lang.reflect.Type;

import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.reportable.Reportable;
import org.junit.Before;
import org.junit.Test;

/**
 * @author smorrissey
 *
 */
public class FeatureConfigurationUtilTest {

	private String rClassName  = "org.jhove2.core.JHOVE2";
	private String rClassName2 = "org.jhove2.core.format.FormatIdentification";
	private String notRClassName = "org.jhove2.app.Parser";
	private String phoneyClassName = "no.such.classname";

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Test method for {@link org.jhove2.app.FeatureConfigurationUtil#getReportableFromClassName(java.lang.String)}.
	 */
	@Test
	public void testGetReportableFromClassName() {
		try{
			Reportable r = FeatureConfigurationUtil.
			getReportableFromClassName(rClassName);
		}
		catch (JHOVE2Exception e){
			fail("unable to create reportable for " + rClassName);
		}
		try{
			Reportable r = FeatureConfigurationUtil.
			getReportableFromClassName(notRClassName);
			fail("Should not have been able to create Reportable for class "
					+ notRClassName);
		}
		catch (JHOVE2Exception e){
			assert(true);
		}
		try{
			Reportable r = FeatureConfigurationUtil.
				getReportableFromClassName(phoneyClassName);
			fail("Should not have been able to create Reportable for class "
					+ phoneyClassName);
		}
		catch (JHOVE2Exception e){
			assert(true);
		}
		try{
			Reportable r = FeatureConfigurationUtil.
				getReportableFromClassName(rClassName2);
			fail("Should not have been able to create Reportable for class "
					+ phoneyClassName);
		}
		catch (JHOVE2Exception e){
			assert(true);
		}

	}
	/**
	 * Test method for {@link org.jhove2.app.FeatureConfigurationUtil#testGetPropertiesAsList(org.jhove2.core.reportable.Reportable)}.
	 */
	@Test
	public void testGetPropertiesAsList(){
		try{
			Reportable r = FeatureConfigurationUtil.
			getReportableFromClassName(rClassName);
			List<String> props = FeatureConfigurationUtil.
			getPropertiesAsList(r);
			assertTrue(props.size()==15);
		}
		catch (JHOVE2Exception e){
			fail("unable to create reportable for " + rClassName);
		}
	}

	@Test
	public void testIsBooleanType(){
		boolean[] b1 = new boolean[]{true, false};
		Boolean bigB= new Boolean("true");
		String str = "true";
		String[] strArray = new String[]{"true", "false"};
		HashMap<Boolean, Boolean> map = new HashMap<Boolean, Boolean>();
		Class<?> theClass;	
		Type type = Boolean.TYPE;
		assertTrue(FeatureConfigurationUtil.isBooleanType(type));
		theClass = bigB.getClass();
		type = (Type)theClass;
		assertTrue(FeatureConfigurationUtil.isBooleanType(type));
		theClass = b1.getClass();
		type = (Type)theClass;
		assertFalse(FeatureConfigurationUtil.isBooleanType(type));
		theClass = str.getClass();
		type = (Type)theClass;
		assertFalse(FeatureConfigurationUtil.isBooleanType(type));
		theClass = strArray.getClass();
		type = (Type)theClass;
		assertFalse(FeatureConfigurationUtil.isBooleanType(type));
		theClass = map.getClass();
		type = (Type)theClass;
		assertFalse(FeatureConfigurationUtil.isBooleanType(type));	
	}

	@Test
	public void testIsReportableClass(){
		assertTrue(FeatureConfigurationUtil.isReportableClass(rClassName));
		assertTrue(FeatureConfigurationUtil.isReportableClass(rClassName));
		assertFalse(FeatureConfigurationUtil.isReportableClass(notRClassName));
		assertFalse(FeatureConfigurationUtil.isReportableClass(phoneyClassName));
	}

}
