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
public class ArcVersionBlockTest extends ArcModuleTestBase {

	Object[][] cases = new Object[][] {
			{Validity.False, "invalid-versionblock-1.arc", new String[][] {
					{
						"[ERROR/OBJECT] Invalid ARC file"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-versionblock-2.arc", new String[][] {
					{
						"[ERROR/OBJECT] Required 'Archive-length' value is missing",
						"[ERROR/OBJECT] Invalid ARC file"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-versionblock-3.arc", new String[][] {
					{
						"[ERROR/OBJECT] Invalid ARC version block",
						"[ERROR/OBJECT] Error in ARC version block, reason 'Version block is not valid!'",
						"[ERROR/OBJECT] Invalid ARC record does not match the version block definition, value: '1', expected: '2'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-versionblock-4.arc", new String[][] {
					{
						"[ERROR/OBJECT] Error in ARC file, expected 'Expected metadata payload not found in the version block'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-versionblock-5.arc", new String[][] {
					{
						"[ERROR/OBJECT] Undesired version block metadata payload: value: 'Metadata payload must not be present in this version'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-versionblock-6.arc", new String[][] {
					{
						"[ERROR/OBJECT] Undesired version block metadata payload: value: 'Metadata payload must not be present in this version'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-versionblock-7.arc", new String[][] {
					{
						"[ERROR/OBJECT] Required 'Content-type' value is missing",
						"[ERROR/OBJECT] Error in Content-type, expected '<type>/<sub-type>(; <argument>=<value>)*'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-versionblock-8.arc", new String[][] {
					{
						"[WARNING/OBJECT] Invalid Content-type, value: 'application/plain', expected: 'text/plain'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-versionblock-9.arc", new String[][] {
					{
						"[WARNING/OBJECT] Invalid Content-type, value: 'text/binary', expected: 'text/plain'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-versionblock-10.arc", new String[][] {
					{
						"[WARNING/OBJECT] Invalid Content-type, value: 'text/binary', expected: 'text/plain'"
					}
			}, new String[] {
			}},
			{Validity.False, "invalid-versionblock-11.arc", new String[][] {
					{
						"[WARNING/OBJECT] Invalid Content-type, value: 'text/binary', expected: 'text/plain'"
					}
			}, new String[] {
			}},
			{Validity.True, "valid-versionblock-1.arc", new String[][] {
					{}
			}, new String[] {
			}},
			{Validity.True, "valid-versionblock-2.arc", new String[][] {
					{}
			}, new String[] {
			}},
			{Validity.True, "valid-versionblock-3.arc", new String[][] {
					{}
			}, new String[] {
			}}
	};

	@Test
	public void test_arcversionblocks() {
		test_cases(cases, false);
	}

}
