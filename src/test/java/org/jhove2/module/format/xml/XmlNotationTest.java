package org.jhove2.module.format.xml;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class XmlNotationTest extends XmlModuleTestBase {

    @Before 
    public void initialize() {
        if (! initialized) {
            parse("samples/notation-sample.xml");
        }
    }

    /**
     * Test method for DTD information
     */
    @Test
    public void testDTDs() {
        List<DTD> dtds = testXmlModule.getDTDs();
        assertEquals(1, dtds.size());
        DTD dtd0 = dtds.get(0);
        assertEquals("myRoot",dtd0.getName());
        assertEquals(null,dtd0.getPublicID());
        assertEquals(null,dtd0.getSystemID());
    }

    
    /**
     * Test method for XML Notation information
     */
    @Test
    public void testNotations() {
        List<Notation> notations = testXmlModule.getNotations();
        assertEquals(1, notations.size());
        Notation notation0 = notations.get(0);
        assertEquals("notationName",notation0.getName());
        assertEquals("notationValue",notation0.getPublicID());
    }

}
