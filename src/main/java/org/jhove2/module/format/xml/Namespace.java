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

/**
 * This class is used to hold information about a <i>namespace declaration</i>
 * discovered during parsing of an XML instance.
 * <p>
 * The XML namespace mechanism provide a simple method for qualifying element
 * and attribute names by associating them with unique URI references.
 * </p>
 * <p>
 * A namespace binding is declared using an attribute whose name must either be
 * <i>xmlns</i> or <i>xmlns:{prefix}</i>. The value of the attribute is the
 * <i>URI</i> that identifies the namespace.
 * </p>
 * <p>
 * If a <i>prefix</i> is declared, then the prefix can be used to construct
 * <i>qualified names</i> within the scope of the element to which the
 * declaration is attached. If no prefix is declared, then all unqualified
 * element names within the scope of the declaration are considered to be
 * associated with that <i>default namespace</i>.
 * </p>
 * @author rnanders
 * @see <a href="http://www.w3.org/TR/xml-names">Namespaces in XML 1.0</a>
 */
public class Namespace extends AbstractReportable {

    /** The namespace prefix. */
    protected String prefix;

    /** The namespace URI. */
    protected String uri;

    /**
     * Gets the namespace prefix.
     * 
     * @return the namespace prefix
     */
    @ReportableProperty(order = 1, value = "Namespace Prefix")
    public String getPrefix() {
        return prefix;
    }

    /**
     * Gets the namespace URI.
     * 
     * @return the namespace URI
     */
    @ReportableProperty(order = 2, value = "Namespace URI")
    public String getURI() {
        return uri;
    }

}
