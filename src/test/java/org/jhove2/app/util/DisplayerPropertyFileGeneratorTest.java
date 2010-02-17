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

import java.util.List;
import java.util.Set;

import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.info.ReportablePropertyInfo;
import org.jhove2.core.reportable.Reportable;
import org.junit.Test;

/**
 * @author smorrissey
 *
 */
public class DisplayerPropertyFileGeneratorTest {
	private String allChoices = "Always | Never";
	private String booleanChoices = "IfTrue | IfFalse";
	private String numericChoices = "IfNegative | IfNonNegative | IfNonPositive | IfNonZero | IfPositive | IfZero";
	private String allPlusNumericChoices = 
		"Always | Never | IfNegative | IfNonNegative | IfNonPositive | IfNonZero | IfPositive | IfZero";
	private String memoryUsage = "http://jhove2.org/terms/property/org/jhove2/core/JHOVE2/MemoryUsage";
	private String rClassName  = "org.jhove2.core.JHOVE2";
	private String rClassName2 = "org.jhove2.core.FormatIdentification";


	/**
	 * Test method for {@link org.jhove2.app.DisplayerPropertyFileGenerator#getDisplayChoices(org.jhove2.core.info.ReportablePropertyInfo)}.
	 */
	@Test
	public void testGetDisplayChoices() {
		try{
			Reportable r = FeatureConfigurationUtil.
				getReportableFromClassName(rClassName);
			List<ReportablePropertyInfo> props = FeatureConfigurationUtil.
			getPropertiesAsReportablePropertyInfoList(r);
			assertTrue(props.size()==15);
			for (ReportablePropertyInfo info : props){
				String choices = DisplayerPropertyFileGenerator.getDisplayChoices(info);
				String propName = info.getIdentifier().getValue();
				if (propName.equals(memoryUsage)){
					assertEquals(allPlusNumericChoices,choices);
				}
				else {
					assertEquals(allChoices, choices);
				}
			}
		}
		catch (JHOVE2Exception e){
			System.out.println(e.getMessage());
			fail("unable to create reportable for " + rClassName);
		}
		try{
			Set<ReportablePropertyInfo> props = FeatureConfigurationUtil.
			getProperitiesAsReportablePropertyInfoSet(rClassName);
			assertTrue(props.size()==15);
			for (ReportablePropertyInfo info : props){
				String choices = DisplayerPropertyFileGenerator.getDisplayChoices(info);
				String propName = info.getIdentifier().getValue();
				if (propName.equals(memoryUsage)){
					assertEquals(allPlusNumericChoices,choices);
				}
				else {
					assertEquals(allChoices, choices);
				}
			}
		}
		catch (JHOVE2Exception e){
			System.out.println(e.getMessage());
			fail("unable to create reportable for " + rClassName);
		}
		try{
			Set<ReportablePropertyInfo> props = FeatureConfigurationUtil.
			getProperitiesAsReportablePropertyInfoSet(rClassName2);
			assertTrue(props.size()==6);
			for (ReportablePropertyInfo info : props){
				String choices = DisplayerPropertyFileGenerator.getDisplayChoices(info);
				assertEquals(allChoices, choices);
			}
		}
		catch (JHOVE2Exception e){
			System.out.println(e.getMessage());
			fail("unable to create reportable for " + rClassName2);
		}
	}

	/**
	 * Test method for {@link org.jhove2.app.DisplayerPropertyFileGenerator#getAllTypesDisplayChoices()}.
	 */
	@Test
	public void testGetAllTypesDisplayChoices() {
		String choices = DisplayerPropertyFileGenerator.getAllTypesDisplayChoices();
		assertEquals(allChoices,choices);
	}

	/**
	 * Test method for {@link org.jhove2.app.DisplayerPropertyFileGenerator#getBooleanDisplayChoices()}.
	 */
	@Test
	public void testGetBooleanDisplayChoices() {
		String choices = DisplayerPropertyFileGenerator.getBooleanDisplayChoices();
		assertEquals(booleanChoices, choices);
	}

	/**
	 * Test method for {@link org.jhove2.app.DisplayerPropertyFileGenerator#getNumericDisplayChoices()}.
	 */
	@Test
	public void testGetNumericDisplayChoices() {
		String choices = DisplayerPropertyFileGenerator.getNumericDisplayChoices();
		assertEquals(numericChoices, choices);
	}

}
