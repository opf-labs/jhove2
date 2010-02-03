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
package org.jhove2.module.format.xml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeMap;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.reportable.AbstractReportable;

/**
 * A class to hold a sorted set of entity names and counts of how many times 
 * an entity reference was found in the XML document.
 */
public class EntityReferences extends AbstractReportable {
 
    /** The de-duplicated set of entities that have been referenced in the XML document. */
    TreeMap<String,EntityReference> entityReferenceMap = new TreeMap<String,EntityReference>();
    
    /**
     * Get the entity references found during XML parsing.
     * 
     * @return the entity references
     */
    @ReportableProperty(order = 1, value = "Entity references found during XML parsing")
     public ArrayList<EntityReference> getEntityReferenceList() {
        return new ArrayList<EntityReference>(entityReferenceMap.values());
    }
    
    /**
     * Increment the count of references found for this entity.
     * 
     * @param name the entity name
     */
    public void tally(String name) {
        EntityReference reference = entityReferenceMap.get(name);
        if (reference != null) {
            reference.count++;
        } else {
            entityReferenceMap.put(name, new EntityReference(name,1));
        }
     }
    
     /**
      * A nested class to contain a single entity name and the count of its references in the XML document
      */
     public class EntityReference extends AbstractReportable  {
         
         /** The entity name. */
         protected String name;

         /** The entity reference count. */
         protected Integer count;
         
         /**
          * Instantiates a new entity reference.
          * 
          * @param name the name
          * @param count the initial count
          */
         public EntityReference(String name, Integer count){
             this.name = name;
             this.count = count;
         }
             
         /**
          * Gets the entity name.
          * 
          * @return the name
          */
         @ReportableProperty(order = 1, value = "Entity name")
         public String getName() {
            return name;
        }

         /**
          * Gets the count.
          * 
          * @return the count
          */
         @ReportableProperty(order = 1, value = "Reference count")
         /** Gets the entity reference count. */
        public Integer getCount() {
            return count;
        }         
         
     }



}
