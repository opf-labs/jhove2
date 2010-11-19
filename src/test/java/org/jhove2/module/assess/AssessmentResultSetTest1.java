package org.jhove2.module.assess;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import javax.annotation.Resource;

import org.jhove2.core.JHOVE2Exception;
import org.jhove2.module.format.Validator.Validity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:**/mock-module-ruleset-1-config.xml",
        "classpath*:**/mock-module-object-config.xml"})
public class AssessmentResultSetTest1 {
    private static String name = "MockRuleSet1";
    private static String description  = "RuleSet1 for testing Mock Module";
    private static String objectFilter  = "org.jhove2.module.assess.MockModule";
   
    /* The AssessmentResultSet whose  being examined */
    private AssessmentResultSet resultSet = new AssessmentResultSet();

    /* The Assessed Object */
    private Object assessedObject = new Object();

    /* Construct a RuleSet object using Spring */
    @Resource(name = "MockModuleRuleSet1")
    public void setRuleSet(RuleSet ruleSet)  {
        resultSet.setRuleSet(ruleSet);
    }
   
    /* Construct a Module object using Spring */
    @Resource(name = "MockModule")
    public void setAssessedObject(MockModule assessedObject)  {
        this.assessedObject = assessedObject;
    }

    @Test
    public void testGetRuleSetName() {
        assertEquals(name, resultSet.getRuleSetName());
    }

    @Test
    public void testGetDescription() {
        assertEquals(description, resultSet.getRuleSetDescription());
    }

    @Test
    public void testGetObjectFilter() {
        assertEquals(objectFilter, resultSet.getObjectFilter());
    }

    @Test
    public void testGetRuleSet() {
        List<Rule> rules = resultSet.getRuleSet().getRules();
        assertTrue(rules.size() > 0);
    }

    @Test
    public void testGetAssessedObject() {
        assertTrue(assessedObject instanceof MockModule);
    }
    
    @Test
    public void testFireAllRules() {
        try {
            resultSet.fireAllRules(assessedObject);
            assertEquals(Validity.True,resultSet.assessmentResults.get(0).getBooleanResult());
            assertEquals(Validity.True,resultSet.assessmentResults.get(1).getBooleanResult());
            assertEquals(Validity.True,resultSet.assessmentResults.get(2).getBooleanResult());
            assertEquals(Validity.True,resultSet.assessmentResults.get(3).getBooleanResult());
            assertEquals(Validity.True,resultSet.assessmentResults.get(4).getBooleanResult());
            /*
            assertEquals(Validity.True,resultSet.assessmentResults.get(5).getBooleanResult());
            assertEquals(Validity.True,resultSet.assessmentResults.get(6).getBooleanResult());        
            assertEquals(Validity.True,resultSet.assessmentResults.get(7).getBooleanResult());        
            assertEquals(Validity.Undetermined,resultSet.assessmentResults.get(8).getBooleanResult());        
            assertEquals(Validity.Undetermined,resultSet.getBooleanResult());
            assertTrue(Validity.Undetermined.toString().equals(resultSet.getNarrativeResult()));
            */
        }
        catch (Exception e) {
            fail(e.getMessage());
            // e.printStackTrace();
        }
    }
}
