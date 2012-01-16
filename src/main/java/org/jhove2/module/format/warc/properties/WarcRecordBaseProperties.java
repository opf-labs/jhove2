package org.jhove2.module.format.warc.properties;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.reportable.AbstractReportable;

import com.sleepycat.persist.model.Persistent;

@Persistent
public class WarcRecordBaseProperties extends AbstractReportable {

	protected WarcRecordData record;

	public WarcRecordBaseProperties() {
	}

	public WarcRecordBaseProperties(WarcRecordData record) {
		this.record = record;
	}

    @ReportableProperty(order = 1, value = "Warc-Date header value.")
    public String getWarcDate() {
        return record.warcDate;
    }

    @ReportableProperty(order = 2, value = "Warc-Record-ID header value.")
    public String getWarcRecordId() {
        return record.warcRecordId;
    }

    @ReportableProperty(order = 3, value = "Record-ID-Scheme value.")
    public String getRecordIdScheme() {
        return record.recordIdScheme;
    }

    @ReportableProperty(order = 4, value = "Content-Type header value.")
    public String getContentType() {
        return record.contentType;
    }

    @ReportableProperty(order = 5, value = "Content-Length header value.")
    public String getContentLength() {
        return record.contentLength;
    }

    @ReportableProperty(order = 6, value = "Warc-Type header value.")
    public String getWarcType() {
        return record.warcType;
    }

    @ReportableProperty(order = 7, value = "Warc-Block-Digest header value.")
    public String getWarcBlockDigest() {
        return record.blockDigest;
    }

    @ReportableProperty(order = 8, value = "Block-Digest-Algorithm value.")
    public String getBlockDigestAlgorithm() {
        return record.blockDigestAlgorithm;
    }

    @ReportableProperty(order = 9, value = "Block-Digest-Encoding value.")
    public String getBlockDigestEncoding() {
        return record.blockDigestEncoding;
    }

    @ReportableProperty(order = 10, value = "Warc-Payload-Digest header value.")
    public String getWarcPayloadDigest() {
        return record.payloadDigest;
    }

    @ReportableProperty(order = 11, value = "Payload-Digest-Algorithm value.")
    public String getPayloadDigestAlgorithm() {
        return record.payloadDigestAlgorithm;
    }

    @ReportableProperty(order = 12, value = "Payload-Digest-Encoding value.")
    public String getPayloadDigestEncoding() {
        return record.payloadDigestEncoding;
    }

    @ReportableProperty(order = 13, value = "Warc-Truncated header value.")
    public String getWarcTruncated() {
    	return record.warcTruncated;
    }

    @ReportableProperty(order = 14, value = "hasPayload value.")
    public Boolean getHasPayload() {
        return record.bHasPayload;
    }

    @ReportableProperty(order = 15, value = "PayloadLength value.")
    public String getPayloadLength() {
        return record.payloadLength;
    }

    @ReportableProperty(order = 16, value = "Warc-Identified-Payload-Type header value.")
    public String getProtocolVersion() {
        return record.warcIdentifiedPayloadType;
    }

    @ReportableProperty(order = 17, value = "Warc-Segment-Number header value.")
    public String getWarcSegmentNumber() {
        return record.warcSegmentNumber;
    }

    @ReportableProperty(order = 18, value = "isNonCompliant value.")
    public Boolean getIsNonCompliant() {
        return record.bIsNonCompliant;
    }

}
