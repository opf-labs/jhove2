package org.jhove2.module.assess;

import static org.junit.Assert.assertEquals;

import javax.annotation.Resource;

import org.jhove2.module.format.Validator.Validity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:**/mock-rule-config.xml"})
public class AssessmentResultTest {
    
    private static String name = "TestRule";
    private static String description = "The description";
    private static String consequent = "The consequent";
    private static String alternative = "The alternative";
    private static String details = "ANY_OF { reportable0 == value0 => true;reportable1 == value1 => true; }";
    
   
    /* The AssessmentResult whose fields are being examined */
    private AssessmentResult assessmentResult = new AssessmentResult();

    /* Construct a Rule object using Spring */
    @Resource(name = "MockRule")
    public void setRule(Rule rule)  {
        assessmentResult.setRule(rule);
    }
    
    
    
    @Test
    public void testGetRuleName() {
        assertEquals(name, assessmentResult.getRuleName());
    }

    @Test
    public void testGetRuleDescription() {
        assertEquals(description, assessmentResult.getRuleDescription());
    }

    @Test
    public void testGetAssessmentDetails() {
        Rule rule = assessmentResult.getRule();
        for (String predicate : rule.getPredicates()) {
            assessmentResult.getPredicateEvaluations().put(predicate, true);
        }
        String found = assessmentResult.getAssessmentDetails();
        assertEquals(details,found);
    }

    @Test
    public void testTrueResult() {
        assessmentResult.setBooleanResult(Validity.True);        
        assertEquals(Validity.True, assessmentResult.getBooleanResult());
        assertEquals(consequent, assessmentResult.getNarrativeResult());
    }

    @Test
    public void testFalseResult() {
        assessmentResult.setBooleanResult(Validity.False);        
        assertEquals(Validity.False, assessmentResult.getBooleanResult());
        assertEquals(alternative, assessmentResult.getNarrativeResult());
    }

}
