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
public class WarcModuleTest extends WarcModuleTestBase {

	/*
IAH-20080430204825-00000-blackbook.warc
	*/

	Object[][] cases = new Object[][] {
			{Validity.False, "invalid-empty.warc", new String[][] {
			}, new String[] {
					"[ERROR/OBJECT] Error in WARC file, expected 'One or more records'"
			}},
			{Validity.False, "invalid-warcfile-contenttype-recommended.warc", new String[][] {
					{
						"[WARNING/OBJECT] Recommend 'Content-Type' header missing"
					},
					{
						"[WARNING/OBJECT] Recommend 'Content-Type' header missing"
					},
					{
						"[WARNING/OBJECT] Recommend 'Content-Type' header missing"
					},
					{
						"[WARNING/OBJECT] Recommend 'Content-Type' header missing"
					},
					{
						"[WARNING/OBJECT] Recommend 'Content-Type' header missing"
					},
					{
						"[WARNING/OBJECT] Recommend 'Content-Type' header missing"
					},
					{
						"[WARNING/OBJECT] Recommend 'Content-Type' header missing"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-warcfile-contenttype-warcinfo-recommended.warc", new String[][] {
					{
						"[WARNING/OBJECT] Recommend 'Content-Type' value: value: 'application/warc-fields', instead of: 'application/octet'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-warcfile-digest-fields.warc", new String[][] {
					{
						"[ERROR/OBJECT] Invalid Incorrect block digest, value: 'D5817BF5B4B35A296823509DD754700A6AD522B5', expected: '1B9310B445384A0F1D3C45B90E2346D608BC73D7'",
						"[ERROR/OBJECT] Invalid Incorrect payload digest, value: 'D5817BF5B4B35A296823509DD754700A6AD522B5', expected: '1DBBF1909F9142B639402A6241C3873C462DAB32'"
					},
					{
						"[ERROR/OBJECT] Invalid 'WARC-Payload-Digest' value, value: 'sha1:', expected: '<digest-algorithm>:<digest-encoded>'",
						"[ERROR/OBJECT] Invalid 'WARC-Block-Digest' value, value: 'sha1:', expected: '<digest-algorithm>:<digest-encoded>'"
					},
					{
						"[ERROR/OBJECT] Invalid 'WARC-Payload-Digest' value, value: ':2WAXX5NUWNNCS2BDKCO5OVDQBJVNKIVV', expected: '<digest-algorithm>:<digest-encoded>'",
						"[ERROR/OBJECT] Invalid 'WARC-Block-Digest' value, value: ':2WAXX5NUWNNCS2BDKCO5OVDQBJVNKIVV', expected: '<digest-algorithm>:<digest-encoded>'"
					},
					{
						"[ERROR/OBJECT] Invalid 'WARC-Payload-Digest' value, value: 'monkeypowah!', expected: '<digest-algorithm>:<digest-encoded>'",
						"[ERROR/OBJECT] Invalid 'WARC-Block-Digest' value, value: 'monkeypowah!', expected: '<digest-algorithm>:<digest-encoded>'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-warcfile-duplicate-fields.warc", new String[][] {
					{
						"[ERROR/OBJECT] Duplicate 'WARC-Type' header: 'warcinfo'",
						"[ERROR/OBJECT] Duplicate 'WARC-Date' header: '2008-04-30T20:48:25Z'",
						"[ERROR/OBJECT] Duplicate 'WARC-Filename' header: 'IAH-20080430204825-00000-blackbook.warc.gz'",
						"[ERROR/OBJECT] Duplicate 'WARC-Record-ID' header: '<urn:uuid:35f02b38-eb19-4f0d-86e4-bfe95815069c-1>'",
						"[ERROR/OBJECT] Duplicate 'Content-Type' header: 'application/warc-fields'",
						"[ERROR/OBJECT] Duplicate 'Content-Length' header: '483'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-warcfile-fields-empty.warc", new String[][] {
					{
						"[ERROR/OBJECT] Required and invalid 'WARC-Type' header: value: ''",
						"[ERROR/OBJECT] Required and invalid 'WARC-Record-ID' header: value: ''",
						"[ERROR/OBJECT] Required and invalid 'WARC-Date' header: value: ''",
						"[ERROR/OBJECT] Required and invalid 'Content-Length' header: value: ''",
						"[ERROR/OBJECT] Invalid Trailing newlines, value: '0', expected: '2'",
						"[WARNING/OBJECT] Empty 'WARC-Type' field",
						"[WARNING/OBJECT] Empty 'WARC-Target-URI' field",
						"[WARNING/OBJECT] Empty 'WARC-IP-Address' field",
						"[WARNING/OBJECT] Empty 'WARC-Date' field",
						"[WARNING/OBJECT] Empty 'WARC-Record-ID' field",
						"[WARNING/OBJECT] Empty 'Content-Length' field",
						"[WARNING/OBJECT] Empty 'Content-Type' field",
						"[WARNING/OBJECT] Empty 'WARC-Block-Digest' field",
						"[WARNING/OBJECT] Empty 'WARC-Segment-Number' field"
					}
			}, new String[] {
					"[ERROR/OBJECT] Invalid Data before WARC version",
					"[ERROR/OBJECT] Invalid Empty lines before WARC version"					
			}},
			{Validity.False, "invalid-warcfile-fields-invalidformat.warc", new String[][] {
					{
						"[ERROR/OBJECT] Invalid 'WARC-Target-URI' value, value: 'bad_uri', expected: 'Absolute URI'",
						"[ERROR/OBJECT] Invalid 'WARC-IP-Address' value, value: 'a.b.c.d', expected: 'IPv4 or IPv6 format'",
						"[ERROR/OBJECT] Invalid 'WARC-Date' value, value: 'blue monday', expected: 'yyyy-MM-dd'T'HH:mm:ss'Z''",
						"[ERROR/OBJECT] Invalid 'WARC-Record-ID' value, value: 'zaphod', expected: 'Absolute URI'",
						"[ERROR/OBJECT] Invalid 'Content-Length' value, value: 'very lengthy', expected: 'Numeric format'",
						"[ERROR/OBJECT] Invalid 'Content-Type' value, value: 'gif\\image', expected: '<type>/<sub-type>(; <argument>=<value>)*'",
						"[ERROR/OBJECT] Invalid 'WARC-Block-Digest' value, value: 'sharif-1; omar', expected: '<digest-algorithm>:<digest-encoded>'",
						"[ERROR/OBJECT] Invalid 'WARC-Segment-Number' value, value: 'one', expected: 'Numeric format'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Record-ID' header: value: '<zaphod>'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Date' header: value: 'blue monday'",
						"[ERROR/OBJECT] Required and invalid 'Content-Length' header: value: 'very lengthy'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Target-URI' value: value: 'bad_uri'",
						"[ERROR/OBJECT] Invalid Trailing newlines, value: '0', expected: '2'"
					}
			}, new String[] {
					"[ERROR/OBJECT] Invalid Data before WARC version",
					"[ERROR/OBJECT] Invalid Empty lines before WARC version"
			}},
			{Validity.False, "invalid-warcfile-fields-missing.warc", new String[][] {
					{
						"[ERROR/OBJECT] Required and invalid 'WARC-Type' header: value: ''",
						"[ERROR/OBJECT] Required and invalid 'WARC-Record-ID' header: value: ''",
						"[ERROR/OBJECT] Required and invalid 'WARC-Date' header: value: ''",
						"[ERROR/OBJECT] Required and invalid 'Content-Length' header: value: ''",
						"[ERROR/OBJECT] Invalid Trailing newlines, value: '0', expected: '2'",
						"[WARNING/OBJECT] Empty 'WARC-Type' field",
						"[WARNING/OBJECT] Empty 'WARC-Record-ID' field",
						"[WARNING/OBJECT] Empty 'WARC-Date' field",
						"[WARNING/OBJECT] Empty 'Content-Length' field",
						"[WARNING/OBJECT] Empty 'Content-Type' field",
						"[WARNING/OBJECT] Empty 'WARC-Concurrent-To' field",
						"[WARNING/OBJECT] Empty 'WARC-Block-Digest' field",
						"[WARNING/OBJECT] Empty 'WARC-Payload-Digest' field",
						"[WARNING/OBJECT] Empty 'WARC-IP-Address' field",
						"[WARNING/OBJECT] Empty 'WARC-Refers-To' field",
						"[WARNING/OBJECT] Empty 'WARC-Target-URI' field",
						"[WARNING/OBJECT] Empty 'WARC-Truncated' field",
						"[WARNING/OBJECT] Empty 'WARC-Warcinfo-ID' field",
						"[WARNING/OBJECT] Empty 'WARC-Filename' field",
						"[WARNING/OBJECT] Empty 'WARC-Profile' field",
						"[WARNING/OBJECT] Empty 'WARC-Identified-Payload-Type' field",
						"[WARNING/OBJECT] Empty 'WARC-Segment-Origin-ID' field",
						"[WARNING/OBJECT] Empty 'WARC-Segment-Number' field",
						"[WARNING/OBJECT] Empty 'WARC-Segment-Total-Length' field"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-warcfile-lonely-continuation.warc", new String[][] {
					{
						"[ERROR/OBJECT] Required and invalid 'WARC-Record-ID' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Date' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'Content-Length' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Target-URI' value: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Segment-Number' value: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Segment-Origin-ID' value: value: 'null'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-warcfile-lonely-monkeys.warc", new String[][] {
					{
						"[ERROR/OBJECT] Required and invalid 'WARC-Record-ID' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Date' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'Content-Length' header: value: 'null'",
						"[WARNING/OBJECT] Unknown 'WARC-Type' value: 'monkeys'",
						"[WARNING/OBJECT] Error in Missing CR, expected 'Sequence of CRLFs'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-warcfile-lonely-request-response-resource-conversion.warc", new String[][] {
					{
						"[ERROR/OBJECT] Required and invalid 'WARC-Record-ID' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Date' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'Content-Length' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Target-URI' value: value: 'null'"
					},
					{
						"[ERROR/OBJECT] Required and invalid 'WARC-Record-ID' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Date' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'Content-Length' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Target-URI' value: value: 'null'"
					},
					{
						"[ERROR/OBJECT] Required and invalid 'WARC-Record-ID' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Date' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'Content-Length' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Target-URI' value: value: 'null'"
					},
					{
						"[ERROR/OBJECT] Required and invalid 'WARC-Record-ID' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Date' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'Content-Length' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Target-URI' value: value: 'null'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-warcfile-lonely-revisit.warc", new String[][] {
					{
						"[ERROR/OBJECT] Required and invalid 'WARC-Record-ID' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Date' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'Content-Length' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Target-URI' value: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Profile' value: value: 'null'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-warcfile-lonely-warcinfo-metadata.warc", new String[][] {
					{
						"[ERROR/OBJECT] Required and invalid 'WARC-Record-ID' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Date' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'Content-Length' header: value: 'null'"
					},
					{
						"[ERROR/OBJECT] Required and invalid 'WARC-Record-ID' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'WARC-Date' header: value: 'null'",
						"[ERROR/OBJECT] Required and invalid 'Content-Length' header: value: 'null'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-warcfile-segment-number-continuation.warc", new String[][] {
					{
						"[ERROR/OBJECT] Invalid 'WARC-Segment-Number' value, value: '1', expected: '>1'"
					},
					{}
			}, new String[] {
			}},
			{Validity.False, "invalid-warcfile-segment-number-response.warc", new String[][] {
					{
						"[ERROR/OBJECT] Invalid Incorrect payload digest, value: 'D5817BF5B4B35A296823509DD754700A6AD522B5', expected: '1DBBF1909F9142B639402A6241C3873C462DAB32'"
					},
					{
						"[ERROR/OBJECT] Invalid 'WARC-Segment-Number' value, value: '2', expected: '1'",
						"[ERROR/OBJECT] Invalid Incorrect payload digest, value: 'D5817BF5B4B35A296823509DD754700A6AD522B5', expected: '1DBBF1909F9142B639402A6241C3873C462DAB32'"
					}
			}, new String[] {
			}},
			{Validity.True, "valid-warcfile-contenttype-continuation.warc", new String[][] {
					{}
			}, new String[] {
			}},
			{Validity.True, "valid-warcfile-duplicate-concurrentto.warc", new String[][] {
					{}
			}, new String[] {
			}},
			{Validity.True, "valid-warcfile-fields-continuation.warc", new String[][] {
					{}
			}, new String[] {
			}},
			{Validity.True, "valid-warcfile-fields-metainfo.warc", new String[][] {
					{}
			}, new String[] {
			}},
			{Validity.True, "valid-warcfile-fields-warcinfo.warc", new String[][] {
					{}
			}, new String[] {
			}},
			{Validity.True, "valid-warcfile-non-warc-headers.warc", new String[][] {
					{}
			}, new String[] {
			}},
			{Validity.True, "valid-warcfile-upper-lower-case.warc", new String[][] {
					{},
					{},
					{},
					{},
					{}
			}, new String[] {
			}},
			{Validity.True, "valid-warcfile-utf8.warc", new String[][] {
					{}
			}, new String[] {
			}}
	};

    @Test
    public void test_warcmodule() {
    	test_cases(cases, false);
    }

}
