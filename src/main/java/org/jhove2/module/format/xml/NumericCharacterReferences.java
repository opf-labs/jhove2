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
 * A class to hold a sorted set of numeric character references and the counts of how many times 
 * each reference was found in the XML document.
 */
public class NumericCharacterReferences extends AbstractReportable {
 
    /** The de-duplicated set of numeric character references found in the XML document. */
    TreeMap<String,NumericCharacterReference> numericCharacterReferenceMap = new TreeMap<String,NumericCharacterReference>();
    
    /**
     * Get the numeric character references found during XML parsing.
     * 
     * @return the numeric character references
     */
    @ReportableProperty(order = 1, value = "numeric character references found during XML parsing")
     public ArrayList<NumericCharacterReference> getNumericCharacterReferenceList() {
        return new ArrayList<NumericCharacterReference>(numericCharacterReferenceMap.values());
    }
    
    /**
     * Increment the instance count for this numeric character reference.
     * 
     * @param name the numeric character reference code
     */
    public void tally(String code) {
        NumericCharacterReference reference = numericCharacterReferenceMap.get(code);
        if (reference != null) {
            reference.count++;
        } else {
            numericCharacterReferenceMap.put(code, new NumericCharacterReference(code));
        }
     }
    
     /**
      * A nested class to contain a single numeric character reference and the count of its occurrences in the XML document
      */
     public class NumericCharacterReference extends AbstractReportable  {
         
         /** The numeric character reference markup construct. */
         protected String code;

         /** The reference count. */
         protected Integer count;
         
         /**
          * Instantiates a new numeric character reference.
          * 
          * @param code the markup construct
          * @param count the initial count
          */
         public NumericCharacterReference(String code){
             this.code = code;
             this.count = 1;
         }
             
         /**
          * Gets the numeric character reference.
          * 
          * @return the name
          */
         @ReportableProperty(order = 1, value = "Numeric character reference code")
         public String getCode() {
            return code;
        }

         /**
          * Gets the count.
          * 
          * @return the count
          */
         @ReportableProperty(order = 2, value = "Reference count")
         /** Gets the numeric character reference count. */
        public Integer getCount() {
            return count;
        }         
         
     }



}
