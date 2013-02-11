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
package org.jhove2.module.format.arc;

import org.jhove2.module.format.Validator.Validity;
import org.junit.Test;

/**
 * Tests of ARC Module
 * @see org.jhove2.module.warc.ArcModule
 * @author nicl
 */
public class ArcHeaderTest extends ArcModuleTestBase {

	Object[][] cases = new Object[][] {
			{Validity.False, "invalid-archeader-1.arc", new String[][] {
			}, new String[] {
					"[ERROR/OBJECT] Invalid Empty lines before ARC record",
					"[ERROR/OBJECT] Error in ARC file, expected 'One or more records'"
			}},
			{Validity.False, "invalid-archeader-2.arc", new String[][] {
			}, new String[] {
					"[ERROR/OBJECT] Invalid Data before ARC record",
					"[ERROR/OBJECT] Error in ARC file, expected 'One or more records'"
			}},
			{Validity.False, "invalid-archeader-3.arc", new String[][] {
					{
						"[ERROR/OBJECT] Required 'URL' value is missing",
						"[ERROR/OBJECT] Required 'IP-address' value is missing",
						"[ERROR/OBJECT] Required 'Archive-date' value is missing",
						"[ERROR/OBJECT] Required 'Content-type' value is missing",
						"[ERROR/OBJECT] Required 'Result-code' value is missing",
						"[ERROR/OBJECT] Required 'Offset' value is missing",
						"[ERROR/OBJECT] Required 'Filename' value is missing",
						"[ERROR/OBJECT] Required 'Archive-length' value is missing",
						"[ERROR/OBJECT] Error in ARC file, expected 'Expected a version block as the first record.'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-archeader-4.arc", new String[][] {
					{
						"[ERROR/OBJECT] Invalid 'URL' value, value: '4270', expected: 'Absolute URI'",
						"[ERROR/OBJECT] Invalid 'IP-address' value, value: 'http://cctr.umkc.edu:80/user/jbenz/tst.htm', expected: 'IPv4 or IPv6 format'",
						"[ERROR/OBJECT] Invalid 'Archive-date' value, value: '134.193.4.1', expected: 'yyyyMMddHHmmss'",
						"[ERROR/OBJECT] Invalid 'Content-type' value, value: '19970417175710', expected: '<type>/<sub-type>(; <argument>=<value>)*'",
						"[ERROR/OBJECT] Invalid 'Archive-length' value, value: 'text/html', expected: 'Numeric format'",
						"[ERROR/OBJECT] Error in ARC file, expected 'Expected a version block as the first record.'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-archeader-5.arc", new String[][] {
					{
						"[ERROR/OBJECT] Invalid 'URL' value, value: '40', expected: 'Absolute URI'",
						"[ERROR/OBJECT] Invalid 'IP-address' value, value: 'http://www.antiaction.com/', expected: 'IPv4 or IPv6 format'",
						"[ERROR/OBJECT] Invalid 'Archive-date' value, value: '192.168.1.2', expected: 'yyyyMMddHHmmss'",
						"[ERROR/OBJECT] Invalid 'Content-type' value, value: '20120712144000', expected: '<type>/<sub-type>(; <argument>=<value>)*'",
						"[ERROR/OBJECT] Invalid 'Result-code' value, value: 'text/htlm', expected: 'Numeric format'",
						"[ERROR/OBJECT] Invalid 'Offset' value, value: 'location', expected: 'Numeric format'",
						"[ERROR/OBJECT] Invalid 'Archive-length' value, value: 'filename', expected: 'Numeric format'",
						"[ERROR/OBJECT] Error in ARC file, expected 'Expected a version block as the first record.'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-archeader-6.arc", new String[][] {
					{
						"[ERROR/OBJECT] Invalid 'Result-code' value, value: '99', expected: 'A number between 100 and 999'",
						"[ERROR/OBJECT] Invalid 'Offset' value, value: '-4321', expected: 'A non negative number'",
						"[ERROR/OBJECT] Invalid 'Archive-length' value, value: '-42', expected: 'A non negative number'",
						"[ERROR/OBJECT] Error in ARC file, expected 'Expected payload not found in the record block'",
						"[ERROR/OBJECT] Error in ARC file, expected 'Expected a version block as the first record.'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-archeader-7.arc", new String[][] {
					{
						"[ERROR/OBJECT] Invalid 'Result-code' value, value: '1000', expected: 'A number between 100 and 999'",
						"[ERROR/OBJECT] Error in http header, reason 'Unable to parse http header!'",
						"[ERROR/OBJECT] Error in ARC file, expected 'Expected a version block as the first record.'",
						"[ERROR/OBJECT] Invalid Payload length mismatch, 'Payload truncated'"
					}
			}, new String[] {
			}},
			{Validity.True, "valid-archeader-1.arc", new String[][] {
					{},
					{}
			}, new String[] {
			}},
			{Validity.True, "valid-archeader-2.arc", new String[][] {
					{},
					{}
			}, new String[] {
			}},
			{Validity.True, "valid-archeader-3.arc", new String[][] {
					{},
					{}
			}, new String[] {
			}}
	};

	@Test
	public void test_archeaders() {
		test_cases(cases, false);
	}

}
