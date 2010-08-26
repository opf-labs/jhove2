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
package org.jhove2.module.assess;

import java.util.ArrayList;
import java.util.List;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.Message;
import org.jhove2.core.Message.Context;
import org.jhove2.core.Message.Severity;
import org.jhove2.core.reportable.AbstractReportable;
import org.jhove2.module.format.Validator.Validity;
import org.mvel2.MVEL;

/**
 * An AssessmentResultSet
 * contains references to the object being assessed and the RuleSet being
 * applied. It evaluates each {@link org.jhove2.module.assess.Rule Rule} in the
 * RuleSet and stores the the assessment outcome for that Rule in a list of
 * {@link org.jhove2.module.assess.AssessmentResult AssessmentResult} objects.
 */
public class AssessmentResultSet extends AbstractReportable {

//    /**
//     * A reference to the object instance being assessed. This object could be a
//     * characterization module, such as a
//     * {@link org.jhove2.module.format.FormatModule FormatModule} or a
//     * {@link org.jhove2.core.source.Source Source} item.
//     */
//    protected Object assessedObject;

    /** The Set of {@link org.jhove2.module.assess.Rule Rule}(s) to be applied against the {@link #assessedObject}. */
    protected RuleSet ruleSet;
    
    /** The boolean result of the RuleSet's evaluation of the object. */
    protected Validity booleanResult;

    /** The rule's consequent or alternative text, depending on the value of {@link #booleanResult} */
    protected String narrativeResult;


    /** The list of outcomes for each assessment rule in the {@link org.jhove2.module.assess.RuleSet RuleSet}. */
    protected List<AssessmentResult> assessmentResults;
    


//    /**
//     * Gets the {@link #assessedObject}.
//     *
//     * @return  assessedObject
//     */
//    public Object getAssessedObject() {
//        return assessedObject;
//    }

//    /**
//     * Stores a {@link #assessedObject} reference to the object being evaluated
//     *
//     * @param assessedObject
//     *            the object to set
//     */
//    public void setAssessedObject(Object assessedObject) {
//        this.assessedObject = assessedObject;
//    }

    public AssessmentResultSet(){};

    /**
     * Gets the {@link #ruleSet}
     * 
     * @return  ruleSet
     */
    public RuleSet getRuleSet() {
        return ruleSet;
    }

    /**
     * Stores a reference to the {@link #ruleSet} and creates an {@link AssessmentResult} instance for each rule
     * 
     * @param ruleSet
     *            the ruleSet to set
     */
    public void setRuleSet(RuleSet ruleSet) {
        this.ruleSet = ruleSet;
        assessmentResults = new ArrayList<AssessmentResult>();
        for (Rule rule : ruleSet.getRules()) {
            AssessmentResult assessmentResult = new AssessmentResult();
            assessmentResult.setRule(rule);
            assessmentResults.add(assessmentResult);
        }
    }

    /**
     * Gets the {@link RuleSet#name}
     * 
     * @return the rule set name
     */
    @ReportableProperty(order = 1, value = "Rule Name")
    public String getRuleSetName() {
        return ruleSet.getName();
    }

    /**
     * Gets the {@link RuleSet#description} 
     * 
     * @return the RuleSet Description
     */
    @ReportableProperty(order = 2, value = "RuleSet Description")
    public String getRuleSetDescription() {
        return ruleSet.getDescription();
    }

    /**
     * Gets the {@link RuleSet#objectFilter}.
     * 
     * @return the Object Filter
     */
    @ReportableProperty(order = 3, value = "Object Filter")
    public String getObjectFilter() {
        return ruleSet.getObjectFilter();
    }

    
    /**
     * Gets the {@link #booleanResult} for the RuleSet 
     * 
     * @return booleanResult
     */
    @ReportableProperty(order = 4, value = "RuleSet Boolean Result")
    public Validity getBooleanResult() {
        return this.booleanResult;
    }
    
    /**
     * Gets the {@link #narrativeResult} for the RuleSet
     * 
     * @return narrativeResult
     */
    @ReportableProperty(order = 5, value = "Narrative Result")
    public String getNarrativeResult() {
       return narrativeResult;
    }
  

    
    /**
     * Gets the {@link #assessmentResults}.
     * 
     * @return the Assessment Results
     */
    @ReportableProperty(order = 6, value = "Assessment Results")
    public List<AssessmentResult> getAssessmentResults() {
        return assessmentResults;
    }

    /**
     * For each {@link org.jhove2.module.assess.Rule Rule} in the <a
     * name="ruleSet"></a>{@link #ruleSet}, evaluate the rule against the
     * {@link #assessedObject} and store the results in a
     * {@link AssessmentResult} member of the {@link assessmentResults}. The
     * outcome includes a boolean evaluation of the Rule's conditional
     * expression, and a textual statement based on the true or false value of
     * the evaluation.
     * @param assessedObject Object (Source or Module) to be assessed
     * @throws JHOVE2Exception
     */
    public void fireAllRules(Object assessedObject) throws JHOVE2Exception {
        /* Evaluate each Rule */
        for (AssessmentResult result : getAssessmentResults()) {
            result.fireRule(assessedObject);
        }
        /* Evaluate overall outcome of the RuleSet */
        Validity ruleSetTruth = null;
        for (AssessmentResult result : getAssessmentResults()) {
            Validity ruleTruth = result.getBooleanResult();
            switch (ruleTruth) {
                case True:
                    if (ruleSetTruth == null)
                        ruleSetTruth = Validity.True;
                    break;
                case False:
                    if (! Validity.Undetermined.equals(ruleSetTruth))
                        ruleSetTruth = Validity.False;
                    break;
                case Undetermined:
                    ruleSetTruth = Validity.Undetermined;
                    break;
            }
        }
        if (ruleSetTruth != null)
            this.booleanResult = ruleSetTruth;
        else
            this.booleanResult = Validity.Undetermined;
        /* Set Narrative Result for RuleSet */
        switch (ruleSetTruth) {
            case True:
                this.narrativeResult = ruleSet.consequent;
                break;
            case False:
                this.narrativeResult = ruleSet.alternative;
                break;
            case Undetermined:
                this.narrativeResult = Validity.Undetermined.toString();
                break;
        }

    }

}