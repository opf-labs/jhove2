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

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests of XML Module
 * @see org.jhove2.module.xml.XmlModule
 * @author rnanders
 * 
 */
public class XmlSchemaCatalogTest  extends XmlModuleTestBase {

    
    @Before 
    public void initialize() {
        if (! initialized) {
            testXmlModule.saxParser.setUseXmlCatalog(true);
            File catalogFile = new File("src/test/resources/examples/xml/catalog/schema-catalog.xml");
            String catalogFilePath = catalogFile.getAbsolutePath();
            String[] xmlCatalogList = {catalogFilePath};
            testXmlModule.saxParser.setXmlCatalogList(xmlCatalogList);
            parse("samples/schema-sample.xml");
        }
    }

    /**
     * Test method for root element information
     */
    @Test
    public void testRootElement() {
        RootElement root = testXmlModule.rootElement;
        assertEquals("mets",root.getName());
        assertEquals("http://www.loc.gov/METS/",root.getNamespace());
    }

    /**
     * Test method for namespace information
     */
    @Test
    public void testNamespaceInfo() {
        NamespaceInformation nsInfo = testXmlModule.getNamespaceInformation();
        assertEquals(7,nsInfo.getNamespaceCount());
        String uri = nsInfo.getNamespaces().get(0).getURI();
        assertEquals("http://dlib.nyu.edu/METS/textmd",uri);
        Namespace premis = nsInfo.namespaces.get("http://www.loc.gov/standards/premis/v1");
        assertEquals(2,premis.declarations.size());
        String expectLocation = "http://www.loc.gov/standards/premis/v1/PREMIS-v1-1.xsd";
        String foundLocation = premis.schemaLocations.get(expectLocation).location;
        assertEquals(expectLocation,foundLocation);
    }
    
    /**
     * Test method for SAX Parser information
     */
    @Test
    public void testSaxParserInfo() {
        SaxParser saxParser = testXmlModule.saxParser;
        assertEquals("org.apache.xerces.parsers.SAXParser",saxParser.parser);
        List<String> properties = saxParser.getSaxProperties();
        assertTrue(properties.contains("http://apache.org/xml/properties/internal/entity-resolver = org.apache.xerces.util.XMLCatalogResolver"));
        List<String> catalogList = saxParser.getXmlCatalogs();
        assertEquals(1,catalogList.size());
        assertTrue(catalogList.get(0).contains("schema-catalog.xml"));
        try {
            String url = saxParser.resolver.resolveURI("http://www.loc.gov/mods/v3");
            assertTrue(url.contains("mods-3-2.xsd"));
        }
        catch (IOException e) {
            fail("Could not resolve url using XML Catalog");
        }
    }   
 
}
