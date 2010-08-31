package org.jhove2.module.format.xml;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.reportable.AbstractReportable;

/**
 * A nested class to hold information about the declaration for a namespace.
 */
public class NamespaceDeclaration extends AbstractReportable {

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
    protected NamespaceDeclaration(String prefix) {
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