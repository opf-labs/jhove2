package org.jhove2.module.format.arc.properties;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.reportable.AbstractReportable;

import com.sleepycat.persist.model.Persistent;

@Persistent
public class ArcVersionBlockProperties extends AbstractReportable {

	protected ArcRecordData record;

	public ArcVersionBlockProperties() {
	}

	public ArcVersionBlockProperties(ArcRecordData record) {
		this.record = record;
	}

    @ReportableProperty(order = 1, value = "Arc-VersionNumber header value.")
    public String getProtocolResultCode() {
        return record.versionNumber;
    }

    @ReportableProperty(order = 2, value = "Arc-Reserved header value.")
    public String getProtocolVersion() {
        return record.reserved;
    }

    @ReportableProperty(order = 3, value = "Arc-OriginCode header value.")
    public String getProtocolContentType() {
        return record.originCode;
    }

}
