/**
 * JHOVE2 - Next-generation architecture for format-aware characterization
 * 
 * Copyright (c) 2009 by The Regents of the University of California, Ithaka
 * Harbors, Inc., and The Board of Trustees of the Leland Stanford Junior
 * University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * o Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * 
 * o Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * o Neither the name of the University of California/California Digital
 * Library, Ithaka Harbors/Portico, or Stanford University, nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 * 
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
 */

package org.jhove2.config.spring;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.jhove2.core.JHOVE2Exception;
import org.jhove2.module.assess.RuleSetFactory;
import org.jhove2.module.assess.RuleSet;

/**
 * Spring-based implementation of
 * {@link org.jhove2.module.assess.RuleSetFactory RuleSetFactory} for creating a
 * collection of {@link org.jhove2.module.assess.RuleSet RuleSet} objects.
 * 
 * @author rnanders
 */
public class SpringRuleSetFactory
    implements RuleSetFactory
{
	/**
     * Container for the collection of {@link org.jhove2.module.assess.RuleSet
     * RuleSet}(s) found in the Spring config files.
     * Maps from ObjectFilter {@link Class#getName() className} to RuleSet.
      */
	static ConcurrentMap<String, RuleSet> ruleSetMap;


	/**
	 * Returns the {@link #ruleSetMap}.  First time it is called it
	 * creates the Map by searching the Spring config files 
	 * for all objects of type {@link org.jhove2.module.assess.RuleSet RuleSet},
	 * then indexing them by {@link org.jhove2.module.assess.RuleSet#getObjectFilter() RuleSet.objectFilter} 
	 * 
	 * @return {@link #ruleSetMap
	 * 
	 * @throws JHOVE2Exception
	 */
	public static ConcurrentMap<String, RuleSet> getRuleSetMap()
	    throws JHOVE2Exception
	{
	    if (ruleSetMap == null) {
	    	ruleSetMap = new ConcurrentHashMap<String, RuleSet>();
	        /* Use Spring to get instances of all RuleSet objects  */
	        Map<String, Object> map = SpringConfigInfo
	                .getObjectsForType(RuleSet.class);
	        /* For each of the RuleSets */
	        for ( Object object : map.values()) {
	        	RuleSet ruleSet;
	        	if (object instanceof RuleSet) {
	        		ruleSet = (RuleSet) object;
		            /* Get the className for which the RuleSet applies */
		            String className = ruleSet.getObjectFilter();	            		            
		            /* Add an entry into the ruleSetMap */
		            ruleSetMap.put(className, ruleSet );	        		
	        	}
	        }
	    }
	    return ruleSetMap;
	}

	/**
     * Returns a {@link org.jhove2.module.assess.RuleSet RuleSet} for assessment of a object having the type specified.
     */
	@Override
    public RuleSet getRuleSet(String className)
        throws JHOVE2Exception
    {
        return SpringRuleSetFactory.getRuleSetMap().get(className);
    }
}
