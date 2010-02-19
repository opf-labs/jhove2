package org.jhove2.module.format.xml;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class XmlExternalUnparsedEntityTest extends XmlModuleTestBase {

    @Before 

    public void initialize() {
        if (! initialized) {
            parse("samples/unparsed-entity-sample.xml");
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
     * Test method for Entity declaration information
     */
    @Test
    public void testEntities() {
        List<Entity> entities = testXmlModule.getEntities();
        assertEquals(1, entities.size());
        Entity entity0 = entities.get(0);
        assertEquals("sulair",entity0.getName());
        assertEquals(Entity.EntityType.ExternalUnparsed,entity0.getType());
        assertEquals(null,entity0.getPublicID());
        assertEquals("http://sulair.stanford.edu/images/__o__/sulair_logo.gif",entity0.getSystemID());
        assertEquals("gif",entity0.getNotationName());
    }
    
    /**
     * Test method for XML Notation information
     */
    @Test
    public void testNotations() {
        List<Notation> notations = testXmlModule.getNotations();
        assertEquals(1, notations.size());
        Notation notation0 = notations.get(0);
        assertEquals("gif",notation0.getName());
        assertEquals("gif viewer",notation0.getPublicID());
    }

}
