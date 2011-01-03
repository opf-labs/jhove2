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
 * This class is used to hold information about a <i>notation declaration</i>
 * discovered during parsing of an XML instance.
 * <p>
 * Notations identify by name the format of unparsed entities, the format of
 * elements bearing a notation attribute, or the application to which a
 * processing instruction is addressed.
 * </p>
 * <p>
 * An notation is declared by using the NOTATION keyword inside a DTD. The
 * declaration contains the <i>name</i> of the notation and the <i>external
 * identifier</i> which may allow an XML processor or its client application to
 * locate a helper application capable of processing data in the given notation.
 * </p>
 * <p>
 * An <i>external identifier</i> is either a <i>system identifier</i> or a
 * <i>public identifier</i>, the former of which must contain a <i>system
 * literal</i> (e.g. a URI reference), that points to a separate storage object
 * (such as a file or a web resource). A <i>public identifier</i> provides a
 * public resource name that may be used via a xml catalog (or similar) lookup
 * mechanism to resolve the entity reference.
 * </p>
 * @author rnanders
 * @see <a href="http://www.w3.org/TR/REC-xml/#Notations">Extensible Markup
 * Language (XML) 1.0 -- Notation Declarations</a>
 */
@Persistent
public class Notation extends AbstractReportable {

    /** The notation name. */
    protected String name;

    /** The public identifier. */
    protected String publicId;

    /** The system identifier. */
    protected String systemId;
    
    protected Notation(){
    	super();
    }

    /**
     * Gets the notation name.
     * 
     * @return the notation name
     */
    @ReportableProperty(order = 1, value = "Notation Name")
    public String getName() {
        return name;
    }

    /**
     * Gets the public identifier.
     * 
     * @return the public identifier
     */
    @ReportableProperty(order = 2, value = "Notation Public Identifier")
    public String getPublicID() {
        return publicId;
    }

    /**
     * Gets the system identifier.
     * 
     * @return the system identifier
     */
    @ReportableProperty(order = 3, value = "Notation System Identifier")
    public String getSystemID() {
        return systemId;
    }

}
