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
public class ArcRecordTest extends ArcModuleTestBase {

	Object[][] cases = new Object[][] {
			{Validity.False, "invalid-arcrecord-1.arc", new String[][] {
					{},
					{
						"[ERROR/OBJECT] Error in ARC file, expected 'Expected payload not found in the record block'"
					},
					{
						"[ERROR/OBJECT] Invalid 'IP-address' value, value: 'Tuesday,', expected: 'IPv4 or IPv6 format'",
						"[ERROR/OBJECT] Invalid 'Archive-date' value, value: '21-Aug-96', expected: 'yyyyMMddHHmmss'",
						"[ERROR/OBJECT] Invalid 'Content-type' value, value: '05:14:05', expected: '<type>/<sub-type>(; <argument>=<value>)*'",
						"[ERROR/OBJECT] Invalid 'Archive-length' value, value: 'GMT', expected: 'Numeric format'",
						"[ERROR/OBJECT] Invalid Data before ARC record",
						"[WARNING/OBJECT] Error in Misplaced CR, expected 'Sequence of LFs'",
						"[WARNING/OBJECT] Error in Misplaced LF, expected 'Sequence of LFs'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-arcrecord-2.arc", new String[][] {
					{},
					{
						"[ERROR/OBJECT] Invalid 'Archive-length' value, value: 'invalid', expected: 'Numeric format'",
						"[ERROR/OBJECT] Error in ARC file, expected 'Expected payload not found in the record block'"
					},
					{
						"[ERROR/OBJECT] Invalid 'IP-address' value, value: 'Tuesday,', expected: 'IPv4 or IPv6 format'",
						"[ERROR/OBJECT] Invalid 'Archive-date' value, value: '21-Aug-96', expected: 'yyyyMMddHHmmss'",
						"[ERROR/OBJECT] Invalid 'Content-type' value, value: '05:14:05', expected: '<type>/<sub-type>(; <argument>=<value>)*'",
						"[ERROR/OBJECT] Invalid 'Archive-length' value, value: 'GMT', expected: 'Numeric format'",
						"[ERROR/OBJECT] Invalid Data before ARC record",
						"[WARNING/OBJECT] Error in Misplaced CR, expected 'Sequence of LFs'",
						"[WARNING/OBJECT] Error in Misplaced LF, expected 'Sequence of LFs'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-arcrecord-3.arc", new String[][] {
					{},
					{
						"[ERROR/OBJECT] Error in http header, reason 'Unable to parse http header!'"
					}
			}, new String[] {
			}},
			{Validity.True, "valid-arcrecord-1.arc", new String[][] {
					{},
					{}
			}, new String[] {
			}},
			{Validity.True, "valid-arcrecord-2.arc", new String[][] {
					{},
					{}
			}, new String[] {
			}},
			{Validity.True, "valid-arcrecord-3.arc", new String[][] {
					{},
					{}
			}, new String[] {
			}},
			{Validity.True, "valid-arcrecord-4.arc", new String[][] {
					{},
					{}
			}, new String[] {
			}},
			{Validity.True, "valid-arcrecord-5.arc", new String[][] {
					{},
					{}
			}, new String[] {
			}},
			{Validity.True, "valid-arcrecord-6.arc", new String[][] {
					{},
					{}
			}, new String[] {
			}},
			{Validity.True, "valid-arcrecord-7.arc", new String[][] {
					{},
					{}
			}, new String[] {
			}}
	};

	@Test
	public void test_arcrecords() {
		test_cases(cases, false);
	}

}
