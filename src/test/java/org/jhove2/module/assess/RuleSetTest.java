package org.jhove2.module.assess;

import static org.junit.Assert.*;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:**/mock-module-ruleset-0-config.xml"})
public class RuleSetTest {
    private static String name = "MockRuleSet0";
    private static String description  = "RuleSet0 for testing Mock Module";
    private static String objectFilter  = "org.jhove2.module.assess.MockModule";

    /* The Rule whose fields are being examined */
    private RuleSet ruleSet;

    /* Construct a RuleSet object using Spring */
    @Resource(name = "MockModuleRuleSet0")
    public void setRuleSet(RuleSet ruleSet)  {
        this.ruleSet = ruleSet;
    }
   
    @Test
    public void testGetName() {
        assertEquals(name, ruleSet.getName());
    }

    @Test
    public void testGetDescription() {
        assertEquals(description, ruleSet.getDescription());
    }

    @Test
    public void testGetObjectFilter() {
        assertEquals(objectFilter, ruleSet.getObjectFilter());
    }

    @Test
    public void testGetRules() {
        List<Rule> rules = ruleSet.getRules();
        assertTrue(rules.size() > 0);
    }

}
