package org.jhove2.module.format.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.jhove2.module.format.Validator.Validity;
import org.junit.Before;
import org.junit.Test;

public class XmlNotWellFormedTest extends XmlModuleTestBase {

    @Before 
    public void initialize() {
        if (! initialized) {
            parse("w3cTestSuite/xmlconf/sun/not-wf/attlist07.xml");
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
        assertEquals(1, fatals.getValidationMessageCount());
        String msg = fatals.getValidationMessages().get(0).getMessage();
        assertEquals("The attribute type is required in the declaration of attribute \"number\" for element \"root\".", msg);
    }

    /**
     * Test method for Validity
     */   
    @Test
    public void testValidatity() {
        assertTrue(! testXmlModule.isWellFormed());
        assertEquals(Validity.False, testXmlModule.isValid());
    }

}
