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
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.reportable.AbstractReportable;

/**
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
 * In an instance document, the attribute <i>xsi:schemaLocation</i> provides
 * hints from the author to a processor regarding the location of schema
 * documents ... The schemaLocation attribute value consists of one or more
 * pairs of URI references, separated by white space. The first member of each
 * pair is a namespace name, and the second member of the pair is a hint
 * describing where to find an appropriate schema document for that namespace. A
 * schema is not required to have a namespace and so there is a
 * noNamespaceSchemaLocation attribute which is used to provide hints for the
 * locations of schema documents that do not have target namespaces.
 * 
 * @author rnanders
 * @see <a href="http://www.w3.org/TR/xml-names">Namespaces in XML 1.0</a>
 * @see <a href="http://www.w3.org/TR/xmlschema-0/#schemaLocation">schemaLocation</a>
 */
public class NamespaceInformation extends AbstractReportable {

    /** The regex pattern for a namespace URI and namespace location value pair. */
    private static final String SCHEMA_LOCATION_REGEX = "(?:\\s*([^\\s]+)\\s+([^\\s]+))";

    /** The compiled regex pattern for the namespace URI and namespace location value pair. */
    private static final Pattern SCHEMA_LOCATION_PATTERN = Pattern.compile(SCHEMA_LOCATION_REGEX);

    /** The de-duplicated list of namespaces declared in the XML document. */
    protected TreeMap<String, Namespace> namespaces = new TreeMap<String, Namespace>();

    /** Flag to test if schemaLocations were specified */
    protected boolean hasSchemaLocations;
    
    /**
     * Gets the count of unique namespaces.
     * 
     * @return the namespace count
     */
    @ReportableProperty(order = 1, value = "Namespace Count")
    public int getNamespaceCount() {
        return namespaces.entrySet().size();
    }

    /**
     * Gets the list of namespaces.
     * 
     * @return the namespaces
     */
    @ReportableProperty(order = 2, value = "Namespace List")
    public ArrayList<Namespace> getNamespaces() {
        return new ArrayList<Namespace>(namespaces.values());
    }

    /**
     * Gets the namespace warnings.
     * 
     * @return the namespace warnings
     */
    @ReportableProperty(order = 3, value = "Namespace Warnings")
    public ArrayList<String> getNamespaceWarnings() {
        ArrayList<String> namespaceWarnings = new ArrayList<String>();
        TreeMap<String, Integer> prefixUses = new TreeMap<String, Integer>();
        for (Namespace namespace : namespaces.values()) {
            Collection<Namespace.Declaration> declarationList = namespace.declarations.values();
            if (declarationList.size() > 1) {
                namespaceWarnings.add("Multiple prefixes used for " + namespace.uri);
            }
            for (Namespace.Declaration declaration : declarationList) {
                if (declaration.count > 1) {
                    namespaceWarnings.add("Prefix " + declaration.prefix
                            + " declared multiple times for " + namespace.uri);
                }
                Integer use = prefixUses.get(declaration.prefix);
                if (use != null) {
                    prefixUses.put(declaration.prefix, use + 1);
                }
                else {
                    prefixUses.put(declaration.prefix, 1);
                }
            }
            Collection<Namespace.SchemaLocation> schemaLocationList = namespace.schemaLocations.values();
            if (schemaLocationList.size() > 1) {
                namespaceWarnings.add("Multiple schema locations used for " + namespace.uri);
            }
            for (Namespace.SchemaLocation schemaLocation : schemaLocationList) {
                if (schemaLocation.count > 1) {
                    namespaceWarnings.add("Schema location "
                            + schemaLocation.location
                            + " declared multiple times for " + namespace.uri);
                }
            }
        }
        for (Entry<String, Integer> prefixUse : prefixUses.entrySet()) {
            if (prefixUse.getValue() > 1) {
                namespaceWarnings.add("Prefix " + prefixUse.getKey()
                        + " is used for multiple namespaces");
            }
        }
        return namespaceWarnings;
    }

    /**
     * Gets a namespace object, creating it if necessary
     * 
     * @param uri
     *            the URI of the namespace to be returned 
     * 
     * @return the namespace object
     */
    private Namespace getNamespace(String uri) {
        Namespace namespace = namespaces.get(uri);
        if (namespace == null) {
            namespace = new Namespace(uri);
            namespaces.put(uri, namespace);
        }
        return namespace;
    }

    /**
     * Tally a xmlns declaration of a namespace.
     * 
     * @param uri
     *            the namespace URI
     * @param prefix
     *            the namespace prefix
     */
    protected void tallyDeclaration(String uri, String prefix) {
        getNamespace(uri).tallyDeclaration(prefix);
    }

    /**
     * Parses the schemaLocation and noNamsepaceSchemaLocation attributes of an element.
     * 
     * @param schemaLocation
     *            the schema location
     * @param noNamespaceSchemaLocation
     *            the no-namespace schema location
     */
    protected void parseSchemaLocation(String schemaLocation,
            String noNamespaceSchemaLocation) {
        if (schemaLocation != null) {
            /* Use regular expression capture groups to extract values */
            Matcher m = SCHEMA_LOCATION_PATTERN.matcher(schemaLocation);
            /* for each pair of space-delimited values found in the string */
            while (m.find()) {
                String uri = m.group(1);
                String location = m.group(2);
                tallySchemaLocation(uri, location);
            }
        }
        if (noNamespaceSchemaLocation != null) {
            tallySchemaLocation("[noNamespace]", noNamespaceSchemaLocation);
        }
    }

    /**
     * Tally a single pair of space-delimited values mapping a uri to a location.
     * 
     * @param uri
     *            the uri
     * @param location
     *            the location
     */
    protected void tallySchemaLocation(String uri, String location) {
        getNamespace(uri).tallySchemaLocation(location);
        hasSchemaLocations = true;
    }

    /**
     * This class is used to hold information about a <i>namespace
     * declaration</i> discovered during parsing of an XML instance.
     * <p>
     * 
     * @author rnanders
     */
    public class Namespace extends AbstractReportable {

        /** The namespace URI. */
        protected String uri;

        /** The namespace declarations for this namespace. */
        protected TreeMap<String, Declaration> declarations = new TreeMap<String, Declaration>();

        /** The schema locations for this namespace. */
        protected TreeMap<String, SchemaLocation> schemaLocations = new TreeMap<String, SchemaLocation>();

        /**
         * Instantiates a new namespace object.
         * 
         * @param uri
         *            the uri
         */
        protected Namespace(String uri) {
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
        public ArrayList<Declaration> getDeclarations() {
            return new ArrayList<Declaration>(declarations.values());
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
            Declaration declaration = declarations.get(prefix);
            if (declaration == null) {
                declaration = new Declaration(prefix);
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

        /**
         * A nested class to hold information about the declaration for a namespace.
         */
        public class Declaration extends AbstractReportable {

            /** The namespace prefix. */
            protected String prefix;

            /** The number of times this prefix has been declared as bound to the namespace. */
            protected int count;

            /**
             * Instantiates a new declaration object.
             * 
             * @param prefix
             *            the prefix
             */
            protected Declaration(String prefix) {
                if (prefix.length() < 1) {
                    this.prefix = "[default]";
                }
                else {
                    this.prefix = prefix;
                }
            }

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
             * Gets the count of how many times this namespace prefix was
             * declared.
             * 
             * @return the count of namespace prefix declaration.
             */
            @ReportableProperty(order = 1, value = "Namespace Prefix Declaration Count")
            public int getCount() {
                return count;
            }
        }

        /**
         * A class to hold SchemaLocation information for a namespace.
         */
        public class SchemaLocation extends AbstractReportable {

            /** The schema location. */
            protected String location;

            /** The number of times this schema location has been declared for this namespace. */
            protected int count;

            /**
             * Instantiates a new schema location object.
             * 
             * @param location
             *            the location
             */
            protected SchemaLocation(String location) {
                this.location = location;
            }

            /**
             * Gets the schema location.
             * 
             * @return the schema location
             */
            @ReportableProperty(order = 1, value = "Schema Location")
            public String getLocation() {
                return location;
            }

            /**
             * Gets the count of how many times this schema location was
             * declared.
             * 
             * @return the count of schema location declarations.
             */
            @ReportableProperty(order = 1, value = "Schema Location Declaration Count")
            public int getCount() {
                return count;
            }
        }

    }

}
