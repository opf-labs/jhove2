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

import com.sleepycat.persist.model.Persistent;

/**
 * The container for a rule specification.  A rule is evaluated against the 
 * {@link org.jhove2.annotation.ReportableProperty ReportableProperty}(s) 
 * of a object such as a 
 * {@link org.jhove2.module.format.FormatModule FormatModule} or 
 * {@link org.jhove2.core.source.Source Source} object instance.
 * This specification uses an if-then-else construct whose
 * basic structure (in pseudocode form) looks like this: 
<pre>
    If (condition) Then 
       (consequent)
    Else
       (alternative)
    End If 
</pre>
 * The condition portion of the rule consists of a quantifier and a list of predicate expressions looking like this:
<pre>
    {ALL_OFF | ANY_OF | NONE_OF} 
       (predicate)
       (predicate)
        ...
</pre>
 * Each predicate is a string containing a boolean expression written using 
 * a domain specific language.  Currently only MVEL is supported. 
 * The expression is usually of the form:
<pre>
    {reportableProperty} {logical operator} {value}
</pre>
 * If the ALL_OF quantifier is specified (the default), 
 * then all of the predicates must evaluate to true 
 * for the rule as a whole to be true.
 * If the ANY_OF quantifier is specified, 
 * then the rule will evaluate to true if any of the predicates are true.
 * If the NONE_OF quantifier is specified, 
 * then the rule will evaluate to true if all of the predicates are false.
 * In addition to the boolean value of the rule evaluation, 
 * A text value will be included in the assessment result's outcome.
 * This text value will be the consequent string value if the rule is true, 
 * else the alternative string will be used.
 * 
 * @see <a href="http://en.wikipedia.org/wiki/Conditional_(programming)">Conditional (programming) - Wikipedia</a>
 * @see <a href="http://www.cs.ucla.edu/ldl/tutorial/node22.html">The IF-THEN-ELSE Construct</a>
 * @see <a href="http://www.csm.ornl.gov/~sheldon/ds/sec1.6.html">Predicates and Quantifiers</a>
 * @see <a href="http://mvel.codehaus.org/">MVEL - Home</a>
 * @see <a href="http://mvel.codehaus.org/Language+Guide+for+2.0">MVEL Language Guide</a>
 * @author rnanders
 */
@Persistent
public class Rule {

    /** The name of the rule.  (Should be unique) */
    protected String name;

    /** A short textual description of the Rule's purpose. */
    protected String description;

    /** An enum used to specify the type of logical ANDing or ORing of the set of predicates comprising the rule's condition  */
    public enum Quantor {
        /** The rule evaluates true if all of the predicates are true */
        ALL_OF,
        /** The rule evaluates true if any of the predicates are true */
        ANY_OF,
        /** The rule evaluates true if all of the predicates are false */
        NONE_OF
    }

    /** The quantifier value to use for the rule.  See {@link Quantor} */
    protected Quantor quantifier = Quantor.ALL_OF;

    /** The list of predicate expressions that will be evaluated against an object. */
    protected List<String> predicates;

    /** The text value to be assigned to the {@link AssessmentResult#narrativeResult} if the rule evaluates to true. */
    protected String consequent;

    /** The text value to be assigned to the {@link AssessmentResult#narrativeResult} if the rule evaluates to false. */
    protected String alternative;
    
    /** Whether or not to evaluate this Rule */
    protected boolean enabled = true;

    /**
     * Instantiates a new Rule object.
     */
    public Rule() {
    }

    /**
     * Gets the rule {@link #name}
     * 
     * @return  name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the rule {@link #name}
     * 
     * @param name
     *            the name to be used for the rule
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the rule {@link #description}.
     * 
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the rule {@link #description}.
     * 
     * @param description
     *            the textual description of the rule's purpose
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the rule {@link #quantifier}
     * 
     * @return quantifier
     */
    public Quantor getQuantifier() {
        return quantifier;
    }

    /**
     * Sets the rule {@link #quantifier}
     * 
     * @param quantifier
     *            the quantifier operator used to combine the evaluations of the predicates
     */
    public void setQuantifier(Quantor quantifier) {
        this.quantifier = quantifier;
    }

    /**
     * Gets the rule {@link #predicates}
     * 
     * @return list of predicates
     */
    public List<String> getPredicates() {
        return predicates;
    }

    /**
     * Sets the rule {@link #predicates}
     * 
     * @param predicates
     *            the list of predicates used for rule evaluation
     */
    public void setPredicates(List<String> predicates) {
        this.predicates = predicates;
    }

    /**
     * Gets the rule {@link #consequent}
     * 
     * @return consequent
     */
    public String getConsequent() {
        return consequent;
    }

    /**
     * Sets the rule {@link #consequent}
     * 
     * @param consequent
     *            the string value to use as the consequent
     */
    public void setConsequent(String consequent) {
        this.consequent = consequent;
    }

    /**
     * Gets the rule {@link #alternative}
     * 
     * @return alternative
     */
    public String getAlternative() {
        return alternative;
    }

    /**
     * Sets the rule {@link #alternative}
     * 
     * @param alternative
     *            the string value to use as the alternative
     */
    public void setAlternative(String alternative) {
        this.alternative = alternative;
    }

    /**
     * @return the enabled status for the Rule
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * @param enabled Set the enabled status for the Rule
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
