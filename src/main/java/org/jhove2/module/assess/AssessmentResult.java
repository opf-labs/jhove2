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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.reportable.AbstractReportable;
import org.jhove2.module.format.Validator.Validity;
import org.mvel2.MVEL;

/**
 * An instance of this object stores the assessment outcome for a given rule
 * against the object being assessed. A list of AssessmentResult instances 
 * and a reference to the assessed object is
 * stored in a parent {@link AssessmentResultSet} object.
 */
public class AssessmentResult extends AbstractReportable {

     /** A reference to the rule evaluated to produce these results */
    protected Rule rule;

    /** The boolean result of the rule's evaluation of the object. */
    protected Validity booleanResult = Validity.Undetermined;

    /** The rule's consequent or alternative text, depending on the value of {@link #booleanResult} */
    protected String narrativeResult = "Undetermined";

    /** Documentation of the predicate expressions and the boolean evaluations of those expressions. */
    protected Map<String, Boolean> predicateEvaluations;
    
    
    /** Assessment Messages. */
    protected List<String> assessmentMessages = new ArrayList<String>();

    
    public AssessmentResult(){
    	super();
    }
    /**
     * Gets the {@link #rule}
     * 
     * @return rule 
     */
    public Rule getRule() {
        return rule;
    }

    /**
     * Sets the {@link #rule}
     * 
     * @param rule
     *            the rule to set
     */
    public void setRule(Rule rule) {
        this.rule = rule;
        predicateEvaluations = new LinkedHashMap<String, Boolean>();
    }

    /**
     * Gets the {@link Rule#name}
     * 
     * @return the Rule Name
     */
    @ReportableProperty(order = 1, value = "Rule Name")
    public String getRuleName() {
        return this.rule.getName();
    }

    /**
     * Gets the {@link Rule#description}
     * 
     * @return the Rule Description
     */
    @ReportableProperty(order = 2, value = "Rule Description")
    public String getRuleDescription() {
        return this.rule.getDescription();
    }

    /**
     * Gets the {@link #booleanResult} for the Rule Evaluation
     * 
     * @return booleanResult
     */
    @ReportableProperty(order = 3, value = "Boolean Result")
    public Validity getBooleanResult() {
        return booleanResult;
    }

    /**
     * Sets the {@link #booleanResult}
     * 
     * @param booleanResult
     *            the boolean value assigned to this result 
     */
    public void setBooleanResult(Validity booleanResult) {
        this.booleanResult = booleanResult;
        if (booleanResult.equals(Validity.True)) {
            narrativeResult = rule.getConsequent();
        }
        else if (booleanResult.equals(Validity.False)) {
            narrativeResult = rule.getAlternative();
        } else {
            narrativeResult = Validity.Undetermined.toString();
        }
    }

    /**
     * Gets the {@link #narrativeResult} for the Rule Evaluation
     * 
     * @return narrativeResult
     */
    @ReportableProperty(order = 4, value = "Narrative Result")
    public String getNarrativeResult() {
       return narrativeResult;
    }
    

    /**
     * Gets the {@link #predicateEvaluations}
     * 
     * @return predicateEvaluations
     */
    public Map<String, Boolean> getPredicateEvaluations() {
        return predicateEvaluations;
    }

    /**
     * Presents details of the conditional expression's evaluation.
     * This includes the text of the predicate expressions,
     * the boolean values of those expressions evaluated 
     * against the {@link AssessmentResultSet#assessedObject assessedObject},
     * and the quantifier specifying the ANDing or ORing of the boolean values.
     * 
     * @return the Assessment Details
     */
    @ReportableProperty(order = 5, value = "Conditional Details")
    public String getAssessmentDetails() {
        StringBuilder sb = new StringBuilder();
        sb.append(rule.getQuantifier().toString());
        sb.append(" { ");
        for (Entry<String, Boolean> entry : getPredicateEvaluations()
                .entrySet()) {
            sb.append(entry.getKey());
            sb.append(" => ");
            sb.append(entry.getValue());
            sb.append(";");
        }
        sb.append(" }");
        return sb.toString();
    }
    
    /**
     * Get Assessment messages.
     * 
     * @return Assessment messages
     */
    @ReportableProperty(order = 5, value = "Assessment Messages.")
    public List<String> getAssessmentMessages() {
        return this.assessmentMessages;
    }

    protected void fireRule(Object assessedObject) {
        /* Evaluate each of the Rule's predicates */
        for (String predicate : rule.getPredicates()) {
            Boolean predicateTruth;
            predicateTruth = false;
            try {
                predicateTruth = MVEL.evalToBoolean(predicate, assessedObject);
            } catch (Exception e) {
                predicateTruth = null;
                assessmentMessages.add(e.getMessage());
            }
            getPredicateEvaluations().put(predicate, predicateTruth);
        }
        /* Evaluate the overall outcome of the Rule evaluation */
        if (getAssessmentMessages().size() == 0) {
            boolean ruleTruth = false;
            switch (rule.getQuantifier()) {
                case ALL_OF:
                    ruleTruth = true;
                    for (Boolean predicateTruth : getPredicateEvaluations()
                            .values()) {
                        ruleTruth = (ruleTruth && predicateTruth.booleanValue());
                    }
                    break;
                case ANY_OF:
                    ruleTruth = false;
                    for (Boolean predicateTruth : getPredicateEvaluations()
                            .values()) {
                        ruleTruth = (ruleTruth || predicateTruth.booleanValue());
                    }
                    break;
                case NONE_OF:
                    ruleTruth = true;
                    for (Boolean predicateTruth : getPredicateEvaluations()
                            .values()) {
                        ruleTruth = (ruleTruth || ! predicateTruth.booleanValue());
                    }
                    break;
            }
            setBooleanResult(ruleTruth ? Validity.True : Validity.False );
        } else {
            setBooleanResult(Validity.Undetermined);
        }
    }

}
