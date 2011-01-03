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

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.reportable.AbstractReportable;

import com.sleepycat.persist.model.Persistent;

/**
 * This class is used to hold information about a <i>processing instruction</i>
 * discovered during parsing of an XML instance.
 * <p>
 * Processing instructions allow documents to contain instructions for
 * applications. A processing instruction has the syntax &lt;?target value?&gt;.
 * </p>
 * <p>
 * The <i>target</i> identifies the application to which the instruction (the
 * value) is directed. The XML Notation mechanism may be used for formal
 * declaration of targets.
 * </p>
 * @author rnanders
 * @see <a href="http://www.w3.org/TR/REC-xml/#sec-pi">Extensible Markup Language (XML) 1.0 -- Processing Instructions</a>
 */
@Persistent
public class ProcessingInstruction extends AbstractReportable {

    /** The processing instruction target. */
    protected String target;

    /** The processing instruction data. */
    protected String data;
    
    protected ProcessingInstruction(){
    	super();
    }

    /**
     * Gets the processing instruction target.
     * 
     * @return the processing instruction target
     */
    @ReportableProperty(order = 1, value = "Processing Instruction Target")
    public String getTarget() {
        return target;
    }

    /**
     * Gets the processing instruction data.
     * 
     * @return the processing instruction data
     */
    @ReportableProperty(order = 2, value = "Processing Instruction Data")
    public String getData() {
        return data;
    }

}
