package org.jhove2.module.assess;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.annotation.Resource;

import org.jhove2.module.assess.Rule.Quantor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:**/mock-rule-config.xml"})
public class RuleTest {
    
    private static String name = "TestRule";
    private static String description = "The description";
    private static Quantor quantifier = Quantor.ANY_OF;
    private static String consequent = "The consequent";
    private static String alternative = "The alternative";
    private static String predicate0 = "reportable0 == value0";
    private static String predicate1 = "reportable1 == value1";
    
    /* The Rule whose fields are being examined */
    private Rule rule;

    /* Construct a Rule object using Spring */
    @Resource(name = "MockRule")
    public void setRule(Rule rule)  {
         this.rule = rule;
    }

    @Test
    public void testGetName() {
        assertEquals(name, rule.getName());
    }

    @Test
    public void testGetDescription() {
        assertEquals(description, rule.getDescription());
    }

    @Test
    public void testGetQuantifier() {
        assertEquals(quantifier, rule.getQuantifier());
    }

    @Test
    public void testGetPredicates() {
        List<String> predicates = rule.getPredicates();
        assertEquals(predicate0, predicates.get(0));
        assertEquals(predicate1, predicates.get(1));
    }

    @Test
    public void testGetConsequent() {
        assertEquals(consequent, rule.getConsequent());
    }

    @Test
    public void testGetAlternative() {
        assertEquals(alternative, rule.getAlternative());
    }

}
