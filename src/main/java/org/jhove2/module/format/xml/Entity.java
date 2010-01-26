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
 * This class is used to hold information about an <i>entity declaration</i>
 * discovered during parsing of an XML instance.
 * <p>
 * The XML DTD syntax provides a declaration mechanism for specifying a symbolic
 * entity name that refers to a string constant (<i>internal entity</i>), or to
 * a separate storage unit, such as a file containing either text or binary data
 * (<i>external entity</i>).
 * </p>
 * <p>
 * If the entity's references are to be used in the document content, then it is
 * a <i>general entity</i>. If its references are for use within the DTD, then
 * it is a <i>parameter entity</i>. Parameter entity names begin with '%'.
 * </p>
 * <p>
 * If the entity's definition specifies replacement text to be inserted at the
 * location of an entity reference, then it is considered a <i>parsed
 * entity</i>. If the entity's name is used as the value of an attribute having
 * scope ENTITY or ENTITIES, then it is considered an <i>unparsed entity</i>. An
 * example of the latter would be a binary file that is being referenced via its
 * symbolic entity name, but whose content is not substituted into the document.
 * A unparsed entity's declaration may optionally include a notation reference.
 * </p>
 * <p>
 * <i>Internal entities</i> are always <i>parsed entities</i>. <i>Unparsed
 * entities</i> are always <i>external entities</i>.
 * </p>
 * <p>
 * An entity is declared by using the ENTITY keyword inside a DTD. The
 * declaration contains the name of the entity and the <i>entity value</i> to be
 * substituted for entity references or a <i>external identifier</i> specifying
 * a separate physical storage object.
 * </p>
 * <p>
 * An <i>external identifier</i> is either a <i>system identifier</i> or a
 * <i>public identifier</i>, either of which must contain a <i>system
 * literal</i> (e.g. a URI reference), that points to a separate storage object
 * (such as a file or a web resource). A <i>public identifier</i> also includes
 * a public resource name that may be used via a xml catalog (or similar) lookup
 * mechanism to resolve the entity reference.
 * </p>
 * @author rnanders
 * @see <a href="http://www.w3.org/TR/REC-xml/#sec-physical-struct">Extensible
 * Markup Language (XML) 1.0 -- Physical Structures</a>
 */
public class Entity extends AbstractReportable {

    /** An enumeration of entity types. */
    protected enum EntityType {
        Internal, ExternalParsed, ExternalUnparsed
    }

    /** The entity name. */
    protected String name;

    /** The entity scope. */
    protected EntityType type;

    /** The entity value (for internal parsed entities). */
    protected String value;

    /** The public identifier (for external entities). */
    protected String publicId;

    /** The system identifier (for external entities). */
    protected String systemId;

    /** An optional reference to a notation (for external unparsed entities). */
    protected String notationName;

    /**
     * Gets the entity name.
     * 
     * @return the entity name
     */
    @ReportableProperty(order = 1, value = "Entity Name")
    public String getName() {
        return name;
    }

    /**
     * Gets the entity scope.
     * 
     * @return the entity scope
     */
    @ReportableProperty(order = 2, value = "Entity Scope")
    public EntityType getType() {
        return type;
    }

    /**
     * Gets the entity value.
     * 
     * @return the entity value
     */
    @ReportableProperty(order = 3, value = "Entity Value")
    public String getValue() {
        return value;
    }

    /**
     * Gets the public identifier.
     * 
     * @return the public identifier
     */
    @ReportableProperty(order = 4, value = "Entity Public Identifier")
    public String getPublicID() {
        return publicId;
    }

    /**
     * Gets the system identifier.
     * 
     * @return the system identifier
     */
    @ReportableProperty(order = 5, value = "Entity System Identifier")
    public String getSystemID() {
        return systemId;
    }

    /**
     * Gets the notation name.
     * 
     * @return the notation name
     */
    @ReportableProperty(order = 6, value = "Entity Notation Name")
    public String getNotationName() {
        return notationName;
    }

}
