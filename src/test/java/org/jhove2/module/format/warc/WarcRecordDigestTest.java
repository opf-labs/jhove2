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
public class WarcRecordDigestTest extends WarcModuleTestBase {

	Object[][] cases = new Object[][] {
			{Validity.False, "invalid-warcrecorddigests-1.warc", new String[][] {
					{},
					{
						"[ERROR/OBJECT] Unknown Record block digest encoding scheme: '71B506802DB4A192BF780C6401EE31DE'",
						"[ERROR/OBJECT] Unknown Record payload digest encoding scheme: 'A6D6869F680B1BDD0D27BF5A5F49482E'"
					},
					{
						"[ERROR/OBJECT] Unknown Record block digest encoding scheme: 'OG2QNABNWSQZFP3YBRSAD3RR3Y======'",
						"[ERROR/OBJECT] Unknown Record payload digest encoding scheme: 'U3LINH3IBMN52DJHX5NF6SKIFY======'"
					},
					{
						"[ERROR/OBJECT] Unknown Record block digest encoding scheme: 'cbUGgC20oZK/eAxkAe4x3g=='",
						"[ERROR/OBJECT] Unknown Record payload digest encoding scheme: 'ptaGn2gLG90NJ79aX0lILg=='"
					},
					{
						"[ERROR/OBJECT] Unknown Record block digest encoding scheme: 'EDE22581685942721C7B9743DCED317633D00E33'",
						"[ERROR/OBJECT] Unknown Record payload digest encoding scheme: '95046652B71AAA1E8A5A6AF91E24016DFEAE7BD4'"
					},
					{
						"[ERROR/OBJECT] Unknown Record block digest encoding scheme: '5XRCLALILFBHEHD3S5B5Z3JROYZ5ADRT'",
						"[ERROR/OBJECT] Unknown Record payload digest encoding scheme: 'SUCGMUVXDKVB5CS2NL4R4JABNX7K466U'"
					},
					{
						"[ERROR/OBJECT] Unknown Record block digest encoding scheme: '7eIlgWhZQnIce5dD3O0xdjPQDjM='",
						"[ERROR/OBJECT] Unknown Record payload digest encoding scheme: 'lQRmUrcaqh6KWmr5HiQBbf6ue9Q='"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-warcrecorddigests-2.warc", new String[][] {
					{
						"[ERROR/OBJECT] Unknown Record block digest encoding scheme: '1110110111100010001001011000000101101000010110010100001001110010000111000111101110010111010000111101110011101101001100010111011000110011110100000000111000110011'",
						"[ERROR/OBJECT] Unknown Record payload digest encoding scheme: '1001010100000100011001100101001010110111000110101010101000011110100010100101101001101010111110010001111000100100000000010110110111111110101011100111101111010100'"
					},
					{
						"[ERROR/OBJECT] Unknown Record block digest encoding scheme: '01110001101101010000011010000000001011011011010010100001100100101011111101111000000011000110010000000001111011100011000111011110'",
						"[ERROR/OBJECT] Unknown Record payload digest encoding scheme: '10100110110101101000011010011111011010000000101100011011110111010000110100100111101111110101101001011111010010010100100000101110'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-warcrecorddigests-3.warc", new String[][] {
					{
						"[ERROR/OBJECT] Invalid Incorrect block digest, value: 'A6D6869F680B1BDD0D27BF5A5F49482E', expected: '71B506802DB4A192BF780C6401EE31DE'",
						"[ERROR/OBJECT] Invalid Incorrect payload digest, value: '71B506802DB4A192BF780C6401EE31DE', expected: 'A6D6869F680B1BDD0D27BF5A5F49482E'"
					},
					{
						"[ERROR/OBJECT] Invalid Incorrect block digest, value: '95046652B71AAA1E8A5A6AF91E24016DFEAE7BD4', expected: 'EDE22581685942721C7B9743DCED317633D00E33'",
						"[ERROR/OBJECT] Invalid Incorrect payload digest, value: 'EDE22581685942721C7B9743DCED317633D00E33', expected: '95046652B71AAA1E8A5A6AF91E24016DFEAE7BD4'"
					}
			}, new String[] {
			}},
			{Validity.True, "valid-warcrecorddigests-1.warc", new String[][] {
					{},
					{},
					{},
					{},
					{},
					{},
					{}
			}, new String[] {
			}},
			{Validity.True, "valid-warcrecorddigests-2.warc", new String[][] {
					{},
					{},
					{},
					{},
					{},
					{},
					{}
			}, new String[] {
			}},
	};

	@Test
	public void test_warcrecorddigests() {
    	test_cases(cases, false);
	}

}
