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
public class ArcVersionHeaderTest extends ArcModuleTestBase {

	Object[][] cases = new Object[][] {
			{Validity.False, "invalid-versionheader-1.arc", new String[][] {
					{
						"[ERROR/OBJECT] Invalid ARC file"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-versionheader-2.arc", new String[][] {
					{
						"[ERROR/OBJECT] Invalid ARC file"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-versionheader-3.arc", new String[][] {
					{
						"[ERROR/OBJECT] Error in ARC version block, reason 'Version line empty'",
						"[ERROR/OBJECT] Error in ARC version block, reason 'Block definition empty'",
						"[ERROR/OBJECT] Error in ARC version block, reason 'Version block is not valid!'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-versionheader-4.arc", new String[][] {
					{
						"[ERROR/OBJECT] Error in ARC version block, reason 'Version line empty'",
						"[ERROR/OBJECT] Error in ARC version block, reason 'Block definition empty'",
						"[ERROR/OBJECT] Error in ARC version block, reason 'Version block is not valid!'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-versionheader-5.arc", new String[][] {
					{
						"[ERROR/OBJECT] Error in ARC version block, reason 'Version line empty'",
						"[ERROR/OBJECT] Error in ARC version block, reason 'Block definition empty'",
						"[ERROR/OBJECT] Error in ARC version block, reason 'Version block is not valid!'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-versionheader-6.arc", new String[][] {
					{
						"[ERROR/OBJECT] Error in ARC version block, reason 'Version line empty'",
						"[ERROR/OBJECT] Error in ARC version block, reason 'Block definition empty'",
						"[ERROR/OBJECT] Error in ARC version block, reason 'Version block is not valid!'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-versionheader-7.arc", new String[][] {
					{
						"[ERROR/OBJECT] Error in ARC version block, reason 'Version line empty'",
						"[ERROR/OBJECT] Error in ARC version block, reason 'Version block is not valid!'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-versionheader-8.arc", new String[][] {
					{
						"[ERROR/OBJECT] Error in ARC version block, reason 'Version line empty'",
						"[ERROR/OBJECT] Error in ARC version block, reason 'Version block is not valid!'",
						"[ERROR/OBJECT] Invalid ARC record does not match the version block definition, value: '2', expected: '1'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-versionheader-9.arc", new String[][] {
					{
						"[ERROR/OBJECT] Error in ARC version block, reason 'Version line empty'",
						"[ERROR/OBJECT] Error in ARC version block, reason 'Version block is not valid!'",
						"[ERROR/OBJECT] Invalid ARC record does not match the version block definition, value: '1', expected: '2'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-versionheader-10.arc", new String[][] {
					{
						"[ERROR/OBJECT] Error in ARC version block, reason 'Version line empty'",
						"[ERROR/OBJECT] Error in ARC version block, reason 'Version block is not valid!'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-versionheader-11.arc", new String[][] {
					{
						"[ERROR/OBJECT] Error in ARC version block, reason 'Block definition empty'",
						"[ERROR/OBJECT] Error in ARC version block, reason 'Version block is not valid!'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-versionheader-12.arc", new String[][] {
					{
						"[ERROR/OBJECT] Error in ARC version block, reason 'Block definition empty'",
						"[ERROR/OBJECT] Error in ARC version block, reason 'Version block is not valid!'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-versionheader-13.arc", new String[][] {
					{
						"[ERROR/OBJECT] Error in ARC version block, reason 'Block definition empty'",
						"[ERROR/OBJECT] Error in ARC version block, reason 'Version block is not valid!'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-versionheader-14.arc", new String[][] {
					{
						"[ERROR/OBJECT] Error in ARC version block, reason 'Block definition empty'",
						"[ERROR/OBJECT] Error in ARC version block, reason 'Version block is not valid!'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-versionheader-15.arc", new String[][] {
					{
						"[ERROR/OBJECT] Error in ARC version block, reason 'Block definition empty'",
						"[ERROR/OBJECT] Error in ARC version block, reason 'Version block is not valid!'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-versionheader-16.arc", new String[][] {
					{
						"[ERROR/OBJECT] Error in ARC version block, reason 'Block definition empty'",
						"[ERROR/OBJECT] Error in ARC version block, reason 'Version block is not valid!'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-versionheader-17.arc", new String[][] {
					{
						"[ERROR/OBJECT] Invalid 'Version-number' value, value: 'x', expected: 'Numeric format'",
						"[ERROR/OBJECT] Invalid ARC version block",
						"[ERROR/OBJECT] Error in ARC version block, reason 'Block definition empty'",
						"[ERROR/OBJECT] Error in ARC version block, reason 'Version block is not valid!'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-versionheader-18.arc", new String[][] {
					{
						"[ERROR/OBJECT] Invalid 'Version-number' value, value: 'x', expected: 'Numeric format'",
						"[ERROR/OBJECT] Invalid ARC version block",
						"[ERROR/OBJECT] Error in ARC version block, reason 'Block definition empty'",
						"[ERROR/OBJECT] Error in ARC version block, reason 'Version block is not valid!'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-versionheader-19.arc", new String[][] {
					{
						"[ERROR/OBJECT] Invalid 'Reserved' value, value: 'x', expected: 'Numeric format'",
						"[ERROR/OBJECT] Invalid ARC version block",
						"[ERROR/OBJECT] Error in ARC version block, reason 'Block definition empty'",
						"[ERROR/OBJECT] Error in ARC version block, reason 'Version block is not valid!'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-versionheader-20.arc", new String[][] {
					{
						"[ERROR/OBJECT] Invalid 'Reserved' value, value: 'x', expected: 'Numeric format'",
						"[ERROR/OBJECT] Invalid ARC version block",
						"[ERROR/OBJECT] Error in ARC version block, reason 'Block definition empty'",
						"[ERROR/OBJECT] Error in ARC version block, reason 'Version block is not valid!'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-versionheader-21.arc", new String[][] {
					{
						"[ERROR/OBJECT] Invalid ARC version block",
						"[ERROR/OBJECT] Error in ARC version block, reason 'Version block is not valid!'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-versionheader-22.arc", new String[][] {
					{
						"[ERROR/OBJECT] Invalid ARC version block",
						"[ERROR/OBJECT] Error in ARC version block, reason 'Version block is not valid!'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-versionheader-23.arc", new String[][] {
					{
						"[ERROR/OBJECT] Invalid ARC version block",
						"[ERROR/OBJECT] Error in ARC version block, reason 'Block definition empty'",
						"[ERROR/OBJECT] Error in ARC version block, reason 'Version block is not valid!'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-versionheader-24.arc", new String[][] {
					{
						"[ERROR/OBJECT] Invalid ARC version block",
						"[ERROR/OBJECT] Error in ARC version block, reason 'Block definition empty'",
						"[ERROR/OBJECT] Error in ARC version block, reason 'Version block is not valid!'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-versionheader-25.arc", new String[][] {
					{
						"[ERROR/OBJECT] Invalid ARC version block",
						"[ERROR/OBJECT] Error in ARC version block, reason 'Block definition empty'",
						"[ERROR/OBJECT] Error in ARC version block, reason 'Version block is not valid!'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-versionheader-26.arc", new String[][] {
					{
						"[ERROR/OBJECT] Invalid ARC version block",
						"[ERROR/OBJECT] Error in ARC version block, reason 'Block definition empty'",
						"[ERROR/OBJECT] Error in ARC version block, reason 'Version block is not valid!'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-versionheader-27.arc", new String[][] {
					{
						"[ERROR/OBJECT] Invalid ARC version block",
						"[ERROR/OBJECT] Error in ARC version block, reason 'Version block is not valid!'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-versionheader-28.arc", new String[][] {
					{
						"[ERROR/OBJECT] Invalid ARC version block",
						"[ERROR/OBJECT] Error in ARC version block, reason 'Version block is not valid!'",
						"[ERROR/OBJECT] Invalid ARC record does not match the version block definition, value: '2', expected: '1'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-versionheader-29.arc", new String[][] {
					{
						"[ERROR/OBJECT] Invalid ARC version block",
						"[ERROR/OBJECT] Error in ARC version block, reason 'Version block is not valid!'",
						"[ERROR/OBJECT] Invalid ARC record does not match the version block definition, value: '1', expected: '2'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-versionheader-30.arc", new String[][] {
					{
						"[ERROR/OBJECT] Invalid ARC version block",
						"[ERROR/OBJECT] Error in ARC version block, reason 'Version block is not valid!'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-versionheader-31.arc", new String[][] {
					{
						"[ERROR/OBJECT] Invalid ARC version block",
						"[ERROR/OBJECT] Error in ARC version block, reason 'Version block is not valid!'",
						"[ERROR/OBJECT] Invalid ARC record does not match the version block definition, value: '1', expected: '2'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-versionheader-32.arc", new String[][] {
					{
						"[ERROR/OBJECT] Invalid ARC version block",
						"[ERROR/OBJECT] Error in ARC version block, reason 'Version block is not valid!'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-versionheader-33.arc", new String[][] {
					{
						"[ERROR/OBJECT] Invalid ARC record does not match the version block definition, value: '2', expected: '1'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-versionheader-34.arc", new String[][] {
					{
						"[ERROR/OBJECT] Invalid ARC record does not match the version block definition, value: '2', expected: '1'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-versionheader-35.arc", new String[][] {
					{
						"[ERROR/OBJECT] Invalid ARC record does not match the version block definition, value: '1', expected: '2'"
					}
			}, new String[] {
			}},
			{Validity.True, "valid-versionheader-1.arc", new String[][] {
					{}
			}, new String[] {
			}},
			{Validity.True, "valid-versionheader-2.arc", new String[][] {
					{}
			}, new String[] {
			}},
			{Validity.True, "valid-versionheader-3.arc", new String[][] {
					{}
			}, new String[] {
			}}
	};

	@Test
	public void test_arcversionheaders() {
		test_cases(cases, false);
	}

}
