/**
 * JHOVE2 - Next-generation architecture for format-aware characterization
 * <p>
 * Copyright (c) 2010 by The Regents of the University of California. All rights reserved.
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
package org.jhove2.config.spring;

import static org.junit.Assert.*;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import org.jhove2.core.I8R;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.format.Format;
import org.junit.Before;
import org.junit.Test;

/**
 * @author smorrissey
 *
 */
public class SpringTiff2FormatMapFactoryTest {

	SpringTiff2FormatMapFactory factory;
	String XmlPuid = "fmt/101";
	int XmlTiffId = 700;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		factory = new SpringTiff2FormatMapFactory();
	}

	/**
	 * Test method for {@link org.jhove2.config.spring.SpringTiff2FormatMapFactory#getTagtoFormatMap()}.
	 */
	@Test
	public void testGetTagtoFormatMap() {
		ConcurrentMap<String, Format> tagToFormatMap = null;
		try {
			tagToFormatMap = factory.getTagtoFormatMap();
			assertNotNull(tagToFormatMap);
		}
		catch (Exception e){
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/**
	 * Test method for {@link org.jhove2.config.spring.SpringTiff2FormatMapFactory#getFormat(int)}.
	 */
	@Test
	public void testGetFormat() {
		Map<String, String> puidToJhoveId = null;
		SpringConfigInfo configInfo = new SpringConfigInfo();
		try {
			puidToJhoveId = configInfo.getFormatAliasIdsToJ2Ids(I8R.Namespace.PUID);
			if (!puidToJhoveId.containsKey(XmlPuid)){
				fail("unable to match XMLPUID");
			}
			String jhoveFormatId = puidToJhoveId.get(XmlPuid);
			ConcurrentMap<String, Format> tagToFormatMap = factory.getTagtoFormatMap();
			Format format = null;
			assertTrue(tagToFormatMap.containsKey(String.valueOf(XmlTiffId)));
			format =  factory.getFormat(XmlTiffId);
			assertNotNull(format);
			assertEquals(jhoveFormatId, format.getIdentifier().getValue());
		} catch (JHOVE2Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
	}

}
