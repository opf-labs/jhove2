/**
 * 
 */
package org.jhove2.module.format.tiff;

import java.io.IOException;
import java.lang.Long;
import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.io.Input;
import org.jhove2.core.reportable.AbstractReportable;


/**
 * 
 * @author mstrong
 *
 */
public class Rational 
extends AbstractReportable {

    /** numerator of the Rational value */
    private long numerator;

    /** denominator of the Rational value */
    private long denominator;

    /**  no-arg constructor for Rational object */
    public Rational() {
    }

    /**
     * signed rational when you read SignedInt()
     * @param input
     * @throws IOException
     */
    public Rational (Input input) throws IOException {
        this.numerator = input.readSignedInt();
        this.denominator = input.readSignedInt();
    }

    public Rational (long numerator, long demoninator){
        this.numerator = numerator;
        this.denominator = demoninator;
    }

    public Rational (int numerator, int denominator) {
        this.numerator = (long) numerator & 0XFFFFFFFF;
        this.denominator = (long) denominator & 0XFFFFFFFF;
    }


    /**
     * @return the numerator
     */
    public long getNumerator() {
        return numerator;
    }

    /**
     * @return the denominator
     */
    public long getDenominator() {
        return denominator;
    }

    /**
     *  toString in the form of numerator/denominator.
     *  @return String
     */ 
    @ReportableProperty(order = 1, value = "TIFF Rational Value.")
    public String getValue ()
    {
        return this.toString();
    }
    /**
     *  toString in the form of numerator/denominator.
     *  @return String
     */ 
    public String toString ()
    {
        return Long.toString (this.numerator) + "/" +
        Long.toString (this.denominator);
    }

}
