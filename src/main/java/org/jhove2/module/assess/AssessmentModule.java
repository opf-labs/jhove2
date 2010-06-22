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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.Message;
import org.jhove2.core.TimerInfo;
import org.jhove2.core.Message.Context;
import org.jhove2.core.Message.Severity;
import org.jhove2.core.source.Source;
import org.jhove2.module.AbstractModule;
import org.jhove2.module.Module;

/**
 * JHOVE2 module performing policy-based assessment of the reportable properties
 * previously generated during characterization of source units.
 * 
 * @author rnanders
 */
public class AssessmentModule extends AbstractModule implements Assessor {
    /** Assessment module version identifier. */
    public static final String VERSION = "0.1.0";

    /** Assessment module release date. */
    public static final String RELEASE = "2010-06-04";

    /** Assessment module rights statement. */
    public static final String RIGHTS = "Copyright 2010 by The Regents of the University of California, "
            + "Ithaka Harbors, Inc., and The Board of Trustees of the Leland "
            + "Stanford Junior University. "
            + "Available under the terms of the BSD license.";

    /** The factory object for creating {@link org.jhove2.module.assess.RuleSet RuleSet} instances */
    protected RuleSetFactory ruleSetFactory;

    /**
     * The list of all {@link org.jhove2.module.assess.AssessmentResultSet
     * AssessmentResultSet} instances that where created during assessment of the
     * {@link org.jhove2.core.source.Source Source} object.   
     * An AssessmentResultSet instance is created for each of a
     * Source item's characterization
     * {@link rg.jhove2.module.Module Module}(s), if there exists a corresponding
     * {@link org.jhove2.module.assess.RuleSet RuleSet} for that module type. An
     * additional AssessmentResultSet will be created for the Source item itself, if
     * there exists a RuleSet for that Source object type. 
     */
    protected List<AssessmentResultSet> assessmentResultSets;
    
    protected JHOVE2 jhove2;
    

    /**
     * Instantiate a new <code>AssessmentModule</code>.
     */
    public AssessmentModule() {
        super(VERSION, RELEASE, RIGHTS, Scope.Generic);
        assessmentResultSets = new ArrayList<AssessmentResultSet>();
    }

    /**
     * Gets the {@link #assessmentResultSets}
     * 
     * @return assessmentResultSets
     */
    @ReportableProperty(order = 1, value = "Assessment Results")
    public List<AssessmentResultSet> getAssessmentResultSets() {
        return assessmentResultSets;
    }
    
    /**
     * Get Assessment messages.
     * 
     * @return Assessment messages
     * @throws JHOVE2Exception 
     */
    @ReportableProperty(order = 5, value = "Assessment Messages.")
    public List<Message> getAssessmentMessages() throws JHOVE2Exception {
        List<Message> messages = new ArrayList<Message>();
        for ( AssessmentResultSet resultSet : getAssessmentResultSets()) {
            for (AssessmentResult result : resultSet.getAssessmentResults()) {
                for (String message: result.getAssessmentMessages()) {
                    Object[]messageArgs = new Object[] {message};
                    messages.add(new Message(Severity.ERROR,
                            Context.OBJECT,
                            "org.jhove2.module.assess.assessmentErrorsFound",
                            messageArgs, jhove2.getConfigInfo()));
                }
                
            }
        }
        return messages;
   }


    /**
     * Evaluate selected properties of a {@link org.jhove2.core.source.Source Source} unit.
     * Assessment is attempted for each of a Source item's characterization 
     * {@link rg.jhove2.module.Module Module}(s) and 
     * against the Source unit itself.
     * 
     * @param jhove2
     *            The JHOVE2 framework
     * @param source
     *            The Source unit to be assessed
     * @throws IOException
     * @throws JHOVE2Exception
     */
    @Override
    public void assess(JHOVE2 jhove2, Source source) throws IOException,
            JHOVE2Exception {
        /* Assess the source unit. */
        // TODO is Timer syntax OK?
        TimerInfo timer = this.getTimerInfo();
        timer.setStartTime();
        this.jhove2 = jhove2;
        try {
            List<Module> modules = source.getModules();
            for (Module module : modules) {
                assessObject(module);
            }
            assessObject(source);
        }
        finally {
            timer.setEndTime();
        }
    }

    /**
     * Assessment of a RuleSet is performed against the specified object
     * if there exists a corresponding RuleSet for that object type
     * 
     * @param assessedObject
     *            the assessed object
     * @throws JHOVE2Exception
     *             the jHOV e2 exception
     */
    private void assessObject(Object assessedObject) throws JHOVE2Exception {
        String className = assessedObject.getClass().getName();
        RuleSet ruleSet = getRuleSetFactory().getRuleSet(className);
        if (ruleSet != null) {
            AssessmentResultSet resultSet = new AssessmentResultSet();
            assessmentResultSets.add(resultSet);
            resultSet.setAssessedObject(assessedObject);
            resultSet.setRuleSet(ruleSet);
            resultSet.fireAllRules();
        }
    }

    /**
     * Gets the {@link #ruleSetFactory}
     * 
     * @return ruleSetFactory
     */
    public RuleSetFactory getRuleSetFactory() {
        return ruleSetFactory;
    }

    /**
     * Sets the {@link #ruleSetFactory}
     * 
     * @param ruleSetFactory
     *            the ruleSetFactory to be used by this Assessor
     */
    public void setRuleSetFactory(RuleSetFactory ruleSetFactory) {
        this.ruleSetFactory = ruleSetFactory;
    }

}
