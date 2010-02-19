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
package org.jhove2.module.format.xml;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.jhove2.module.format.Validator.Validity;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests of XML Module
 * @see org.jhove2.module.xml.XmlModule
 * @author rnanders
 * 
 */
public class XmlNamespaceTest  extends XmlModuleTestBase {

    
    @Before 
    public void initialize() {
        if (! initialized) {
            parse("w3cTestSuite/xmlconf/eduni/namespaces/1.0/001.xml");
        }
    }

    /**
     * Test method for root element information
     */
    @Test
    public void testRootElement() {
        RootElement root = testXmlModule.rootElement;
        assertEquals("foo",root.getName());
        assertEquals("http://example.org/namespace",root.getNamespace());
    }

    /**
     * Test method for namespace information
     */
    @Test
    public void testNamespaceInfo() {
        NamespaceInformation nsInfo = testXmlModule.getNamespaceInformation();
        assertEquals(1,nsInfo.getNamespaceCount());
        String uri = nsInfo.getNamespaces().get(0).getURI();
        assertEquals("http://example.org/namespace",uri);
    }

    

 
}
