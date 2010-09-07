package org.jhove2.module.format.xml;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.jhove2.module.format.Validator.Validity;
import org.junit.Before;
import org.junit.Test;

public class XmlRequiredAttributeTest extends XmlModuleTestBase {

    @Before 
    public void initialize() {
        if (! initialized) {
            parse("w3cTestSuite/xmlconf/sun/valid/required00.xml");
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
        ValidationMessageList warnings = vr.getParserWarnings();
        assertEquals(0, warnings.getValidationMessageCount());
        ValidationMessageList errors = vr.getParserErrors();
        assertEquals(0, errors.getValidationMessageCount());
        ValidationMessageList fatals = vr.getFatalParserErrors();
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
