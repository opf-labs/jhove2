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
import java.util.TreeMap;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.reportable.AbstractReportable;

import com.sleepycat.persist.model.Persistent;

/**
 * This class is used to hold information about a <i>namespace
 * declaration</i> discovered during parsing of an XML instance.
 * <p>
 * 
 * @author rnanders
 */
@Persistent
public class Namespace extends AbstractReportable {

    /** The namespace URI. */
    protected String uri;

    /** The namespace declarations for this namespace. */
    protected TreeMap<String, NamespaceDeclaration> declarations = new TreeMap<String, NamespaceDeclaration>();

    /** The schema locations for this namespace. */
    protected TreeMap<String, SchemaLocation> schemaLocations = new TreeMap<String, SchemaLocation>();

    protected Namespace(){
    	super();
    }
    /**
     * Instantiates a new namespace object.
     * 
     * @param uri
     *            the uri
     */
    protected Namespace(String uri) {
    	this();
        this.uri = uri;
    }

    /**
     * Gets the namespace URI.
     * 
     * @return the namespace URI
     */
    @ReportableProperty(order = 1, value = "Namespace URI")
    public String getURI() {
        return uri;
    }

    /**
     * Gets the namespace declarations.
     * 
     * @return the namespace declarations
     */
    @ReportableProperty(order = 2, value = "Namespace Declarations")
    public ArrayList<NamespaceDeclaration> getDeclarations() {
        return new ArrayList<NamespaceDeclaration>(declarations.values());
    }

    /**
     * Gets the schema locations.
     * 
     * @return the schema locations
     */
    @ReportableProperty(order = 3, value = "Schema Locations")
    public ArrayList<SchemaLocation> getSchemaLocations() {
        return new ArrayList<SchemaLocation>(schemaLocations.values());
    }

    /**
     * Tally the occurrence of a given prefix declaration.
     * 
     * @param prefix
     *            the namespace prefix
     */
    protected void tallyDeclaration(String prefix) {
        NamespaceDeclaration declaration = declarations.get(prefix);
        if (declaration == null) {
            declaration = new NamespaceDeclaration(prefix);
            declarations.put(prefix, declaration);
        }
        declaration.count++;
    }

    /**
     * Tally the occurrence of a given schema location.
     * 
     * @param location
     *            the location
     */
    protected void tallySchemaLocation(String location) {
        SchemaLocation schemaLocation = schemaLocations.get(location);
        if (schemaLocation == null) {
            schemaLocation = new SchemaLocation(location);
            schemaLocations.put(location, schemaLocation);
        }
        schemaLocation.count++;
    }

}