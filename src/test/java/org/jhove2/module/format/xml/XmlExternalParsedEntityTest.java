package org.jhove2.module.format.xml;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.TreeMap;

import org.jhove2.module.format.Validator.Validity;
import org.junit.Before;
import org.junit.Test;

public class XmlExternalParsedEntityTest extends XmlModuleTestBase {

    @Before 
    public void initialize() {
        if (! initialized) {
            parse("w3cTestSuite/xmlconf/sun/valid/ext01.xml");
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
        assertEquals("root",dtd0.getName());
        assertEquals(null,dtd0.getPublicID());
        assertEquals(null,dtd0.getSystemID());
    }

    /**
     * Test method for Entity declaration information
     */
    @Test
    public void testEntities() {
        List<Entity> entities = testXmlModule.getEntities();
        assertEquals(2, entities.size());
        Entity entity0 = entities.get(0);
        assertEquals("root",entity0.getName());
        assertEquals(Entity.EntityType.ExternalParsed,entity0.getType());
        assertEquals(null,entity0.getPublicID());
        assertEquals("ext01.ent",entity0.getSystemID());
        assertEquals(null,entity0.getNotationName());
        Entity entity1 = entities.get(1);
        assertEquals("null",entity1.getName());
        assertEquals(Entity.EntityType.ExternalParsed,entity1.getType());
        assertEquals(null,entity1.getPublicID());
        assertEquals("null.ent",entity1.getSystemID());
        assertEquals(null,entity1.getNotationName());
    }
    
    /**
     * Test method for Entity References
     */
    @Test
    public void testEntityReferences() {
        TreeMap<String, EntityReferences.EntityReference> erMap = testXmlModule.entityReferences.entityReferenceMap;
        assertEquals(2,erMap.size());
        assertEquals(2, erMap.get("null").getCount().intValue());
        assertEquals(2, erMap.get("root").getCount().intValue());
    }

    /**
     * Test method for Validation Information
     */   
    @Test
    public void testValidationInfo() {
        ValidationResults vr = testXmlModule.getValidationResults();
        ValidationResults.ValidationMessageList warnings = vr.getParserWarnings();
        assertEquals(0, warnings.getValidationMessageCount());
        ValidationResults.ValidationMessageList errors = vr.getParserErrors();
        assertEquals(0, errors.getValidationMessageCount());
        ValidationResults.ValidationMessageList fatals = vr.getFatalParserErrors();
        assertEquals(0, fatals.getValidationMessageCount());
    }

    /**
     * Test method for Validity
     */   
    @Test
    public void testValidatity() {
        assertEquals(Validity.True, testXmlModule.isWellFormed());
        assertEquals(Validity.True, testXmlModule.isValid());
    }

}
