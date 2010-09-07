package org.jhove2.module.format.xml;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.reportable.AbstractReportable;

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