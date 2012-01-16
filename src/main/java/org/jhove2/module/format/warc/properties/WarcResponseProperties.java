package org.jhove2.module.format.warc.properties;

import java.util.List;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.reportable.AbstractReportable;

import com.sleepycat.persist.model.Persistent;

@Persistent
public class WarcResponseProperties extends AbstractReportable {

	protected WarcRecordData record;

	public WarcResponseProperties() {
	}

	public WarcResponseProperties(WarcRecordData record) {
		this.record = record;
	}

    @ReportableProperty(order = 1, value = "Warc-Target-URI header value.")
    public String getWarcTargetUri() {
        return record.warcTargetUri;
    }

    @ReportableProperty(order = 2, value = "Warc-Concurrent-To header value.")
    public List<String> getWarcConcurrentTo() {
        return record.warcConcurrentToList;
    }

    @ReportableProperty(order = 3, value = "Warc-IP-Address header value.")
    public String getWarcIpAddress() {
        return record.warcIpAddress;
    }

    @ReportableProperty(order = 4, value = "Ip-Address version.")
    public String getIpAddressVersion() {
    	return record.ipVersion;
    }

    @ReportableProperty(order = 5, value = "Warc-Warcinfo-ID header value.")
    public String getWarcWarcinfoId() {
        return record.warcWarcinfoId;
    }

    @ReportableProperty(order = 6, value = "ProtocolResultCode header value.")
    public String getProtocolResultCode() {
        return record.resultCode;
    }

    @ReportableProperty(order = 7, value = "ProtocolVersion header value.")
    public String getProtocolVersion() {
        return record.protocolVersion;
    }

    @ReportableProperty(order = 8, value = "ProtocolContentType header value.")
    public String getProtocolContentType() {
        return record.protocolContentType;
    }

    @ReportableProperty(order = 9, value = "ServerName header value.")
    public String getServerName() {
        return record.serverName;
    }

}
