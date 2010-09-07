package org.jhove2.module.format.xml;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.reportable.AbstractReportable;

/**
 * A nested class to contain a single NCR code point and the count of its
 * occurrences
 */
public class NumericCharacterReference extends AbstractReportable {

    /** The integer representation of a Unicode character. */
    protected Integer codePoint;

    /** The reference count. */
    protected Integer count;

    /**
     * Instantiates a new numeric character reference.
     * 
     * @param codePoint
     *            the integer representation of a Unicode character
     */
    public NumericCharacterReference(Integer codePoint) {
        this.codePoint = codePoint;
        this.count = 1;
    }

    /**
     * Gets the numeric character reference code point in Unicode notation.
     * 
     * @return the code point for the NCR
     */
    @ReportableProperty(order = 1, value = "Numeric character reference code point")
    public String getCodePoint() {
        return String.format("U+%04X", codePoint);
    }

    /**
     * Gets the count of NCR occurrences.
     * 
     * @return the count
     */
    @ReportableProperty(order = 2, value = "Reference count")
    /** Gets the numeric character reference count. */
    public Integer getCount() {
        return count;
    }

}