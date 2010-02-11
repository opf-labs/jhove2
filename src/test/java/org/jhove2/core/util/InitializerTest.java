/* JHOVE2 - Next-generation architecture for format-aware characterization
 *
 * Copyright (c) 2008 by The Regents of the University of California.
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
 *   Library nor the names of its contributors may be used to endorse or
 *   promote products derived from this software without specific prior
 *   written permission.
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

package org.jhove2.core.util;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Properties;

import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.reportable.ReportableFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author mstrong
 * 
 */
public class InitializerTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for
	 * {@link org.jhove2.core.reportable.ReportableFactory#getReportable(java.lang.Class, java.lang.String)}
	 * .
	 */
	@Ignore
	@Test
	public void testGetReportable() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link org.jhove2.core.reportable.ReportableFactory#getReportableNames(java.lang.Class)}
	 * .
	 */
	@Ignore
	@Test
	public void testGetReportableNames() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link org.jhove2.core.reportable.ReportableFactory#getProperties(java.lang.String)}.
	 * 
	 * @throws JHOVE2Exception
	 */
	@Test
	public void testGetProperties() throws JHOVE2Exception {

		Properties props1 = ReportableFactory.getProperties("C0Control");
		assertTrue("Error loading C0Control", props1 != null);

		Properties props2 = ReportableFactory.getProperties("C1Control");
		assertTrue("Error loading C1Control", props2 != null);

		Properties props3 = ReportableFactory.getProperties("CodeBlock");
		assertTrue("Error loading block", props3 != null);

		Properties props4 = ReportableFactory.getProperties("DisplayVisibility");
		assertTrue("Error dispatch map", props4 != null);

	}
}
