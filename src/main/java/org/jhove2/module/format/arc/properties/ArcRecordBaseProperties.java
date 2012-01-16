package org.jhove2.module.format.arc.properties;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.reportable.AbstractReportable;

import com.sleepycat.persist.model.Persistent;

@Persistent
public class ArcRecordBaseProperties extends AbstractReportable {

	protected ArcRecordData record;

	public ArcRecordBaseProperties() {
	}

	public ArcRecordBaseProperties(ArcRecordData record) {
		this.record = record;
	}

    @ReportableProperty(order = 1, value = "Arc-IP-Address header value.")
    public String getIpAddress() {
        return record.ipAddress;
    }

    @ReportableProperty(order = 2, value = "Ip-Address version.")
    public String getIpAddressVersion() {
    	return record.ipVersion;
    }

    @ReportableProperty(order = 3, value = "Arc-Date header value.")
    public String getArchiveDate() {
        return record.archiveDate;
    }

    @ReportableProperty(order = 4, value = "Raw Arc-Date header value.")
    public String getRawArchiveDate() {
        return record.rawArchiveDate;
    }

    @ReportableProperty(order = 5, value = "Arc-Content-Type header value.")
    public String getContentType() {
        return record.contentType;
    }

    @ReportableProperty(order = 6, value = "Arc-Length header value.")
    public String getLength() {
        return record.length;
    }

    @ReportableProperty(order = 7, value = "ProtocolResultCode header value.")
    public String getProtocolResultCode() {
        return record.resultCode;
    }

    @ReportableProperty(order = 8, value = "Arc-Checksum header value.")
    public String getChecksum() {
        return record.checksum;
    }

    @ReportableProperty(order = 9, value = "Arc-Location header value.")
    public String getLocation() {
        return record.location;
    }

    @ReportableProperty(order = 10, value = "Arc-Offset header value.")
    public String getOffset() {
        return record.offset;
    }

    @ReportableProperty(order = 11, value = "Arc-Filename header value.")
    public String getFilename() {
        return record.filename;
    }

    @ReportableProperty(order = 12, value = "hasPayload value.")
    public Boolean getHasPayload() {
        return record.bHasPayload;
    }

    @ReportableProperty(order = 13, value = "ObjectSize value.")
    public String getObjectSize() {
        return record.payloadLength;
    }

    @ReportableProperty(order = 14, value = "isNonCompliant value.")
    public Boolean getIsNonCompliant() {
        return record.bIsNonCompliant;
    }

    @ReportableProperty(order = 15, value = "Computed Block-Digest header value.")
    public String getComputedBlockDigest() {
        return record.blockDigest;
    }

    @ReportableProperty(order = 16, value = "Computed Block-Digest-Algorithm value.")
    public String getComputedBlockDigestAlgorithm() {
        return record.blockDigestAlgorithm;
    }

    @ReportableProperty(order = 17, value = "Computed Block-Digest-Encoding value.")
    public String getComputedBlockDigestEncoding() {
        return record.blockDigestEncoding;
    }

    @ReportableProperty(order = 18, value = "Computed Payload-Digest header value.")
    public String getComputedPayloadDigest() {
        return record.payloadDigest;
    }

    @ReportableProperty(order = 19, value = "Computed Payload-Digest-Algorithm value.")
    public String getComputedPayloadDigestAlgorithm() {
        return record.payloadDigestAlgorithm;
    }

    @ReportableProperty(order = 20, value = "Computed Payload-Digest-Encoding value.")
    public String getComputedPayloadDigestEncoding() {
        return record.payloadDigestEncoding;
    }

}
