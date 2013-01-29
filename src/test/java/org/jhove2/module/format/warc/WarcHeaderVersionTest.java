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
public class WarcHeaderVersionTest extends WarcModuleTestBase {

	Object[][] cases = new Object[][] {
			{Validity.False, "invalid-warcheaderversion-1.warc", new String[][] {
					{
						"[ERROR/OBJECT] Unknown Magic version number: '0.16'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Type' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Record-ID' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Date' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'Content-Length' header: value: 'null'",
						"[ERROR/OBJECT] Invalid Trailing newlines, value: '0', expected: '2'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-warcheaderversion-2.warc", new String[][] {
					{
						"[ERROR/OBJECT] Required and invalid 'WARC-Type' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Record-ID' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Date' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'Content-Length' header: value: 'null'",
						"[ERROR/OBJECT] Invalid Trailing newlines, value: '0', expected: '2'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-warcheaderversion-3.warc", new String[][] {
					{
						"[ERROR/OBJECT] Required and invalid 'WARC-Type' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Record-ID' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Date' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'Content-Length' header: value: 'null'",
						"[ERROR/OBJECT] Invalid Trailing newlines, value: '0', expected: '2'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-warcheaderversion-4.warc", new String[][] {
					{
						"[ERROR/OBJECT] Unknown Magic version number: '0.19'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Type' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Record-ID' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Date' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'Content-Length' header: value: 'null'",
						"[ERROR/OBJECT] Invalid Trailing newlines, value: '0', expected: '2'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-warcheaderversion-5.warc", new String[][] {
					{
						"[ERROR/OBJECT] Unknown Magic version number: '0.99'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Type' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Record-ID' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Date' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'Content-Length' header: value: 'null'",
						"[ERROR/OBJECT] Invalid Trailing newlines, value: '0', expected: '2'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-warcheaderversion-6.warc", new String[][] {
					{
						"[ERROR/OBJECT] Required and invalid 'WARC-Type' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Record-ID' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Date' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'Content-Length' header: value: 'null'",
						"[ERROR/OBJECT] Invalid Trailing newlines, value: '0', expected: '2'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-warcheaderversion-7.warc", new String[][] {
					{
						"[ERROR/OBJECT] Unknown Magic version number: '1.1'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Type' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Record-ID' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Date' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'Content-Length' header: value: 'null'",
						"[ERROR/OBJECT] Invalid Trailing newlines, value: '0', expected: '2'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-warcheaderversion-8.warc", new String[][] {
					{
						"[ERROR/OBJECT] Unknown Magic version number: '2.0'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Type' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Record-ID' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Date' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'Content-Length' header: value: 'null'",
						"[ERROR/OBJECT] Invalid Trailing newlines, value: '0', expected: '2'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-warcheaderversion-9.warc", new String[][] {
					{
						"[ERROR/OBJECT] Unknown Magic version number: 'x.x'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Type' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Record-ID' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Date' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'Content-Length' header: value: 'null'",
						"[ERROR/OBJECT] Invalid Trailing newlines, value: '0', expected: '2'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-warcheaderversion-10.warc", new String[][] {
					{
						"[ERROR/OBJECT] Invalid Magic Version string, '1.0.0'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Type' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Record-ID' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Date' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'Content-Length' header: value: 'null'",
						"[ERROR/OBJECT] Invalid Trailing newlines, value: '0', expected: '2'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-warcheaderversion-11.warc", new String[][] {
					{
						"[ERROR/OBJECT] Invalid Magic Version string, '1.0.1'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Type' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Record-ID' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Date' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'Content-Length' header: value: 'null'",
						"[ERROR/OBJECT] Invalid Trailing newlines, value: '0', expected: '2'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-warcheaderversion-12.warc", new String[][] {
					{
						"[ERROR/OBJECT] Invalid Magic Version string, '1'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Type' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Record-ID' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Date' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'Content-Length' header: value: 'null'",
						"[ERROR/OBJECT] Invalid Trailing newlines, value: '0', expected: '2'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-warcheaderversion-13.warc", new String[][] {
					{
						"[ERROR/OBJECT] Invalid Magic Version string, '1.2.3.4.5'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Type' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Record-ID' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Date' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'Content-Length' header: value: 'null'",
						"[ERROR/OBJECT] Invalid Trailing newlines, value: '0', expected: '2'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-warcheaderversion-14.warc", new String[][] {
					{
						"[ERROR/OBJECT] Invalid Magic Version string, ''",
						"[ERROR/OBJECT] Required and invalid 'WARC-Type' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Record-ID' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Date' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'Content-Length' header: value: 'null'",
						"[ERROR/OBJECT] Invalid Trailing newlines, value: '0', expected: '2'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-warcheaderversion-15.warc", new String[][] {
					{
						"[ERROR/OBJECT] Invalid Magic Version string, 'WARC'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Type' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Record-ID' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Date' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'Content-Length' header: value: 'null'",
						"[ERROR/OBJECT] Invalid Trailing newlines, value: '0', expected: '2'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-warcheaderversion-16.warc", new String[][] {
			}, new String[] {
					"[ERROR/OBJECT] Invalid Data before WARC version",
					"[ERROR/OBJECT] Error in WARC file, expected 'One or more records'"
			}},
			{Validity.False, "invalid-warcheaderversion-17.warc", new String[][] {
			}, new String[] {
					"[ERROR/OBJECT] Invalid Data before WARC version",
					"[ERROR/OBJECT] Error in WARC file, expected 'One or more records'"
			}},
			{Validity.False, "invalid-warcheaderversion-18.warc", new String[][] {
			}, new String[] {
					"[ERROR/OBJECT] Invalid Data before WARC version",
					"[ERROR/OBJECT] Error in WARC file, expected 'One or more records'"
			}},
			{Validity.False, "invalid-warcheaderversion-19.warc", new String[][] {
			}, new String[] {
					"[ERROR/OBJECT] Invalid Data before WARC version",
					"[ERROR/OBJECT] Error in WARC file, expected 'One or more records'"
			}},
			{Validity.False, "invalid-warcheaderversion-20.warc", new String[][] {
			}, new String[] {
					"[ERROR/OBJECT] Invalid Empty lines before WARC version",
					"[ERROR/OBJECT] Error in WARC file, expected 'One or more records'"
			}},
			{Validity.False, "invalid-warcheaderversion-21.warc", new String[][] {
					{
						"[ERROR/OBJECT] Required and invalid 'WARC-Type' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Record-ID' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Date' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'Content-Length' header: value: 'null'",
						"[ERROR/OBJECT] Invalid Trailing newlines, value: '0', expected: '2'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-warcheaderversion-22.warc", new String[][] {
					{
						"[ERROR/OBJECT] Required and invalid 'WARC-Record-ID' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Date' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'Content-Length' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Target-URI' value: value: 'null'",
						"[ERROR/OBJECT] Invalid Trailing newlines, value: '0', expected: '2'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-warcheaderversion-23.warc", new String[][] {
					{
						"[ERROR/OBJECT] Required and invalid 'WARC-Record-ID' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Date' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'Content-Length' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Target-URI' value: value: 'null'",
						"[ERROR/OBJECT] Invalid Trailing newlines, value: '0', expected: '2'",
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-warcheaderversion-24.warc", new String[][] {
					{
						"[ERROR/OBJECT] Required and invalid 'WARC-Type' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Record-ID' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Date' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'Content-Length' header: value: 'null'",
						"[ERROR/OBJECT] Invalid Trailing newlines, value: '0', expected: '2'",
						"[WARNING/OBJECT] Unknown Header line: 'WARC-Type resource'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-warcheaderversion-25.warc", new String[][] {
					{
						"[ERROR/OBJECT] Required and invalid 'WARC-Type' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Record-ID' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Date' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'Content-Length' header: value: 'null'",
						"[ERROR/OBJECT] Invalid Trailing newlines, value: '0', expected: '2'",
						"[WARNING/OBJECT] Empty Header line"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-warcheaderversion-26.warc", new String[][] {
					{
						"[ERROR/OBJECT] Required and invalid 'WARC-Record-ID' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Date' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'Content-Length' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Target-URI' value: value: 'null'",
						"[ERROR/OBJECT] Invalid Trailing newlines, value: '0', expected: '2'"
					}
			}, new String[] {
			}}
	};

	@Test
	public void test_warcheaderversion() {
    	test_cases(cases, false);
	}

}
