package org.jhove2.module.format.arc.properties;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.reportable.AbstractReportable;

import com.sleepycat.persist.model.Persistent;

@Persistent
public class ArcRecordProperties extends AbstractReportable {

	protected ArcRecordData record;

	public ArcRecordProperties() {
	}

	public ArcRecordProperties(ArcRecordData record) {
		this.record = record;
	}

    @ReportableProperty(order = 1, value = "ProtocolResultCode header value.")
    public String getProtocolResultCode() {
        return record.protocolResultCode;
    }

    @ReportableProperty(order = 2, value = "ProtocolVersion header value.")
    public String getProtocolVersion() {
        return record.protocolVersion;
    }

    @ReportableProperty(order = 3, value = "ProtocolContentType header value.")
    public String getProtocolContentType() {
        return record.protocolContentType;
    }

    @ReportableProperty(order = 4, value = "ServerName header value.")
    public String getServerName() {
        return record.serverName;
    }

}
