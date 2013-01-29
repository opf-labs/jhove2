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

import org.jhove2.module.format.Validator.Validity;
import org.junit.Test;

/**
 * Tests of WARC Module
 * @see org.jhove2.module.warc.WarcModule
 * @author nicl
 */
public class WarcRecordTest extends WarcModuleTestBase {

	Object[][] cases = new Object[][] {
			{Validity.False, "invalid-warcrecord-1.warc", new String[][] {
					{
						"[ERROR/OBJECT] Required and invalid 'Content-Length' header: value: 'null'"
					},
					{},
					{
						"[ERROR/OBJECT] Required and invalid 'Content-Length' header: value: 'null'",
						"[ERROR/OBJECT] Invalid Trailing newlines, value: '0', expected: '2'"
					},
					{
						"[ERROR/OBJECT] Invalid Data before WARC version",
						"[ERROR/OBJECT] Invalid Empty lines before WARC version"
					},
					{
						"[WARNING/OBJECT] Recommend 'Content-Type' header missing"
					},
					{
						"[WARNING/OBJECT] Recommend 'Content-Type' value: value: 'application/warc-fields', instead of: 'text/plain'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-warcrecord-2.warc", new String[][] {
					{},
					{
						"[ERROR/OBJECT] Error in http header, reason 'Unable to parse http header!'"
					},
					{},
					{},
					{
						"[ERROR/OBJECT] Error in http header, reason 'Unable to parse http header!'"
					},
					{}
			}, new String[] {
			}},
			{Validity.False, "invalid-warcrecord-3.warc", new String[][] {
					{},
					{
						"[ERROR/OBJECT] Invalid Trailing newlines, value: '0', expected: '2'"
					},
					{
						"[ERROR/OBJECT] Invalid Data before WARC version",
						"[ERROR/OBJECT] Invalid Empty lines before WARC version"
					},
					{
						"[ERROR/OBJECT] Invalid Trailing newlines, value: '0', expected: '2'"
					},
					{
						"[ERROR/OBJECT] Invalid Data before WARC version",
						"[ERROR/OBJECT] Invalid Empty lines before WARC version",
						"[ERROR/OBJECT] Invalid Payload length mismatch, 'Payload truncated'",
						"[ERROR/OBJECT] Invalid Trailing newlines, value: '0', expected: '2'"
					}
			}, new String[] {
			}}
	};

	@Test
	public void test_warcrecords() {
    	test_cases(cases, false);
	}

}
