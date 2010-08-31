package org.jhove2.module.format.xml;

import java.util.ArrayList;
import java.util.TreeMap;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.reportable.AbstractReportable;

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
    protected TreeMap<String, NamespaceDeclaration> declarations = new TreeMap<String, NamespaceDeclaration>();

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