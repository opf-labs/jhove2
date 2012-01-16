package org.jhove2.module.format.warc.properties;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.reportable.AbstractReportable;

import com.sleepycat.persist.model.Persistent;

@Persistent
public class WarcRevisitProperties extends AbstractReportable {

	protected WarcRecordData record;

	public WarcRevisitProperties() {
	}

	public WarcRevisitProperties(WarcRecordData record) {
		this.record = record;
	}

    @ReportableProperty(order = 1, value = "Warc-Target-URI header value.")
    public String getWarcTargetUri() {
        return record.warcTargetUri;
    }

    @ReportableProperty(order = 2, value = "Warc-Profile header value.")
    public String getWarcProfile() {
        return record.warcProfile;
    }

    @ReportableProperty(order = 3, value = "Warc-Refers-To header value.")
    public String getWarcRefersTo() {
        return record.warcRefersTo;
    }

    @ReportableProperty(order = 4, value = "Warc-IP-Address header value.")
    public String getWarcIpAddress() {
        return record.warcIpAddress;
    }

    @ReportableProperty(order = 5, value = "Ip-Address version.")
    public String getIpAddressVersion() {
    	return record.ipVersion;
    }

    @ReportableProperty(order = 6, value = "Warc-Warcinfo-ID header value.")
    public String getWarcWarcinfoId() {
        return record.warcWarcinfoId;
    }

}
