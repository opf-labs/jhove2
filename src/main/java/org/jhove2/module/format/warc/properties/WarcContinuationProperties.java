package org.jhove2.module.format.warc.properties;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.reportable.AbstractReportable;

import com.sleepycat.persist.model.Persistent;

@Persistent
public class WarcContinuationProperties extends AbstractReportable {

	protected WarcRecordData record;

	public WarcContinuationProperties() {
	}

	public WarcContinuationProperties(WarcRecordData record) {
		this.record = record;
	}

    @ReportableProperty(order = 1, value = "Warc-Target-URI header value.")
    public String getWarcTargetUri() {
        return record.warcTargetUri;
    }

    @ReportableProperty(order = 2, value = "Warc-Segment-Origin-ID header value.")
    public String getWarcSegmentOriginId() {
        return record.warcSegmentOriginId;
    }

    @ReportableProperty(order = 3, value = "Warc-Segment-Total-Length header value.")
    public String getWarcSegmentTotalLength() {
        return record.warcSegmentTotalLength;
    }

    @ReportableProperty(order = 4, value = "Warc-Warcinfo-ID header value.")
    public String getWarcWarcinfoId() {
        return record.warcWarcinfoId;
    }

}
