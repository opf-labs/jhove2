package org.jhove2.module.assess;

import java.util.List;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.reportable.AbstractReportable;

public class MockReportable extends AbstractReportable {

    /** the values allowed for the enumeration */
    public enum MockEnum {
        EV0, EV1, EV2, EV3, EV4
    }

    /** a value from an enumeration */
    protected MockEnum cpEV;
    
    /** some text */
    protected String cpString;
    
    /** a long integer */
    protected long cpLong;
    
    /** a true/false value */
    protected boolean cpBoolean;
    
    /** a child object that contains data fields */
    protected MockReportable cpReportable;
    
    /** a list of text strings */
    protected List<String> cpListString;
    
   /**
     * @return the cpEV
     */
    @ReportableProperty(order = 1, value = "cpEV")
    public MockEnum getCpEV() {
        return cpEV;
    }

    /**
     * @param cpEV the cpEV to set
     */
    public void setCpEV(MockEnum cpEV) {
        this.cpEV = cpEV;
    }

    /**
     * @return the cpString
     */
    @ReportableProperty(order = 2, value = "cpString")
    public String getCpString() {
        return cpString;
    }

    /**
     * @param cpString the cpString to set
     */
    public void setCpString(String cpString) {
        this.cpString = cpString;
    }

    /**
     * @return the cpLong
     */
    @ReportableProperty(order = 3, value = "cpLong")
    public long getCpLong() {
        return cpLong;
    }

    /**
     * @param cpLong the cpLong to set
     */
    public void setCpLong(long cpLong) {
        this.cpLong = cpLong;
    }

    /**
     * @return the cpBoolean
     */
    @ReportableProperty(order = 4, value = "cpBoolean")
    public boolean isCpBoolean() {
        return cpBoolean;
    }

    /**
     * @param cpBoolean the cpBoolean to set
     */
    public void setCpBoolean(boolean cpBoolean) {
        this.cpBoolean = cpBoolean;
    }

    /**
     * @return the cpReportable
     */
    @ReportableProperty(order = 5, value = "cpReportable")
    public MockReportable getCpReportable() {
        return cpReportable;
    }

    /**
     * @param cpReportable the cpReportable to set
     */
    public void setCpReportable(MockReportable cpReportable) {
        this.cpReportable = cpReportable;
    }

    /**
     * @return the cpListString
     */
    @ReportableProperty(order = 6, value = "cpListString")
    public List<String> getCpListString() {
        return cpListString;
    }

    /**
     * @param cpListString the cpListString to set
     */
    public void setCpListString(List<String> cpListString) {
        this.cpListString = cpListString;
    }


}
