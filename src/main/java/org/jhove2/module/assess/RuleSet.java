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

import java.util.List;

/**
 * A container for a set of rules that are associated with a particular type of object .
 */
public class RuleSet {

    /** The name of the RuleSet. (Should be unique) */
    protected String name;

    /** A short textual description of the RuleSet's purpose */
    protected String description;

    /** The text value to be assigned to the {@link AssessmentResultSet#narrativeResult} if the RuleSet evaluates to true. */
    protected String consequent;

    /** The text value to be assigned to the {@link AssessmentResultSet#narrativeResult} if the RuleSet evaluates to false. */
    protected String alternative;

    /** The {@link Class#getName() className} (type) of objects that this RuleSet is designed to assess  */
    protected String objectFilter;

    /** The list of {@link Rule}(s) comprising the RuleSet */
    protected List<Rule> rules;

    /**
     * Instantiates a new RuleSet
     */
    public RuleSet() {
    }

    /**
     * Gets the ruleSet {@link #name}
     * 
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the ruleSet {@link #name}
     * 
     * @param name
     *            the name assigned to the RuleSet
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the ruleSet {@link #description}
     * 
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the ruleSet {@link #description}
     * 
     * @param description
     *            the description assigned to the RuleSet
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the ruleSet {@link #objectFilter}
     * 
     * @return objectFilter
     */
    public String getObjectFilter() {
        return objectFilter;
    }

    /**
     * Sets the ruleSet {@link #objectFilter}
     * 
     * @param objectFilter
     *            the objectFilter assigned to the RuleSet
     */
    public void setObjectFilter(String objectFilter) {
        this.objectFilter = objectFilter;
    }

    /**
     * Gets the ruleSet's list of {@link #rules}
     * 
     * @return list of rules
     */
    public List<Rule> getRules() {
        return rules;
    }

    /**
     * Sets the ruleSet's list of {@link #rules}
     * 
     * @param rules
     *            the list of rules assigned to the RuleSet
     */
    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

}
