package org.jhove2.module.format.warc.properties;

import java.util.ArrayList;
import java.util.List;

import org.jhove2.core.reportable.AbstractReportable;
import org.jwat.common.HttpResponse;
import org.jwat.common.Payload;
import org.jwat.warc.WarcConstants;
import org.jwat.warc.WarcRecord;

import com.sleepycat.persist.model.Persistent;

@Persistent
public class WarcRecordData {

    public String warcType;
    public String warcFilename;
    public String warcRecordId;
    public String warcDate;
    public String contentLength;
    public String contentType;
    public String warcTruncated;
    public String warcIpAddress;
    public List<String> warcConcurrentToList;
    public String warcRefersTo;
    public String warcTargetUri;
    public String warcWarcinfoId;
    public String warcIdentifiedPayloadType;
    public String warcProfile;
    public String warcSegmentNumber;
    public String warcSegmentOriginId;
    public String warcSegmentTotalLength;

    public String blockDigest;
    public String blockDigestAlgorithm;
    public String blockDigestEncoding;
    public String payloadDigest;
    public String payloadDigestAlgorithm;
    public String payloadDigestEncoding;

    public String recordIdScheme;

    public Boolean bIsNonCompliant;

    public Boolean bHasPayload;
    public String payloadLength;

    public String ipVersion;

    public String resultCode;
    public String protocolVersion;
    public String protocolContentType;
    public String serverName;
    public String protocolUserAgent;

	public WarcRecordData() {
	}

	public WarcRecordData(WarcRecord record) {
        this.warcType = record.warcTypeStr;
        this.warcFilename = record.warcFilename;
        this.warcRecordId = record.warcRecordIdStr;
        this.warcDate = record.warcDateStr;
        this.contentLength = record.contentLengthStr;
        this.contentType = record.contentTypeStr;
        this.warcTruncated = record.warcTruncatedStr;
        this.warcIpAddress = record.warcIpAddress;
        // TODO Clone List in WarcRecord's getter at some point.
        if (record.warcConcurrentToStrList != null) {
            this.warcConcurrentToList = new ArrayList<String>(record.warcConcurrentToStrList);
        }
        this.warcRefersTo = record.warcRefersToStr;
        this.warcTargetUri = record.warcTargetUriStr;
        this.warcWarcinfoId = record.warcWarcinfoIdStr;
        this.warcIdentifiedPayloadType = record.warcIdentifiedPayloadTypeStr;
        this.warcProfile = record.warcProfileStr;
        this.warcSegmentNumber = record.warcSegmentNumberStr;
        this.warcSegmentOriginId = record.warcSegmentOriginIdStr;
        this.warcSegmentTotalLength = record.warcSegmentTotalLengthStr;

    	if (record.warcBlockDigest != null) {
			if ( record.warcBlockDigest.digestValue != null
					&& record.warcBlockDigest.digestValue.length() > 0) {
	    		blockDigest = record.warcBlockDigest.digestValue;
			}
	    	if (record.warcBlockDigest.algorithm != null
	    			&& record.warcBlockDigest.algorithm.length() > 0) {
	    		blockDigestAlgorithm = record.warcBlockDigest.algorithm;
	    	}
	    	if (record.warcBlockDigest.encoding != null
	    			&& record.warcBlockDigest.encoding.length() > 0) {
	    		blockDigestEncoding = record.warcBlockDigest.encoding;
	    	}
    	}

    	if (record.warcPayloadDigest != null) {
			if (record.warcPayloadDigest.digestValue != null
					&& record.warcPayloadDigest.digestValue.length() > 0) {
				payloadDigest = record.warcPayloadDigest.digestValue;
			}
	    	if (record.warcPayloadDigest.algorithm != null
	    			&& record.warcPayloadDigest.algorithm.length() > 0) {
	    		payloadDigestAlgorithm = record.warcPayloadDigest.algorithm;
	    	}
	    	if (record.warcPayloadDigest.encoding != null
	    			&& record.warcPayloadDigest.encoding.length() > 0) {
	    		payloadDigestEncoding = record.warcPayloadDigest.encoding;
	    	}
    	}

    	if (warcRecordId != null) {
    		int idx = warcRecordId.indexOf(':');
    		if (idx >= 0) {
    			if (warcRecordId.startsWith("<")) {
        	    	recordIdScheme = warcRecordId.substring(1, idx);
    			}
    			else {
        	    	recordIdScheme = warcRecordId.substring(0, idx);
    			}
    		}
    	}

    	// TODO What does this imply, add functionality to JWAT.
        bIsNonCompliant = false;

    	bHasPayload = record.hasPayload();
    	Payload payload = record.getPayload();
    	if (payload != null) {
			// TODO Verify meaning of JHove2 WARC draft
    		HttpResponse httpResponse = payload.getHttpResponse();
    		if (httpResponse != null) {
    			payloadLength = Long.toString(httpResponse.getPayloadLength());
    		}
    		else {
    			payloadLength = Long.toString(payload.getTotalLength());;
    		}
    	}
	}

	public WarcRecordData populateWarcinfo(WarcRecord record) {
		return this;
	}

	public WarcRecordData populateResponse(WarcRecord record) {
    	if (record.warcInetAddress != null) {
    		if (record.warcInetAddress.getAddress().length == 4) {
    	    	ipVersion = "4";
    		}
    		else {
        		ipVersion = "6";
    		}
    	}

    	Payload payload = record.getPayload();

    	if (payload != null) {
    		HttpResponse httpResponse = payload.getHttpResponse();
    		if (httpResponse != null) {
    			resultCode = httpResponse.resultCode;
    			protocolVersion = httpResponse.protocolVersion;
    			protocolContentType = httpResponse.contentType;
    			// TODO Not supported in HttpResponse yet.
    			//serverName = httpResponse.getHeader("servername");
    		}
    	}
    	return this;
	}

	public WarcRecordData populateResource(WarcRecord record) {
    	if (record.warcInetAddress != null) {
    		if (record.warcInetAddress.getAddress().length == 4) {
    	    	ipVersion = "4";
    		}
    		else {
        		ipVersion = "6";
    		}
    	}
		return this;
	}

	public WarcRecordData populateRequest(WarcRecord record) {
    	if (record.warcInetAddress != null) {
    		if (record.warcInetAddress.getAddress().length == 4) {
    	    	ipVersion = "4";
    		}
    		else {
        		ipVersion = "6";
    		}
    	}

    	Payload payload = record.getPayload();

    	if (payload != null) {
    		HttpResponse httpResponse = payload.getHttpResponse();
    		if (httpResponse != null) {
    			// TODO Not supported in HttpResponse yet.
    			protocolVersion = httpResponse.protocolVersion;
    			// TODO Not supported in HttpResponse yet.
    			//serverName = httpResponse.getHeader("user-agent");
    		}
    	}
    	return this;
	}

	public WarcRecordData populateMetadata(WarcRecord record) {
    	if (record.warcInetAddress != null) {
    		if (record.warcInetAddress.getAddress().length == 4) {
    	    	ipVersion = "4";
    		}
    		else {
        		ipVersion = "6";
    		}
    	}
		return this;
	}

	public WarcRecordData populateRevisit(WarcRecord record) {
    	if (record.warcInetAddress != null) {
    		if (record.warcInetAddress.getAddress().length == 4) {
    	    	ipVersion = "4";
    		}
    		else {
        		ipVersion = "6";
    		}
    	}
		return this;
	}

	public WarcRecordData populateConversion(WarcRecord record) {
		return this;
	}

	public WarcRecordData populateContinuation(WarcRecord record) {
		return this;
	}

	public AbstractReportable getWarcRecordBaseProperties() {
        return new WarcRecordBaseProperties(this);
	}

	public AbstractReportable getWarcTypeProperties(WarcRecord record) {
		AbstractReportable warcTypeProperties = null;
		if (record != null && record.warcTypeIdx != null) {
	        switch (record.warcTypeIdx) {
	        case WarcConstants.RT_IDX_WARCINFO:
	        	populateWarcinfo(record);
		        warcTypeProperties = new WarcWarcinfoProperties(this);
		        break;
	        case WarcConstants.RT_IDX_RESPONSE:
	        	populateResponse(record);
	        	warcTypeProperties = new WarcResponseProperties(this);
		        break;
	        case WarcConstants.RT_IDX_RESOURCE:
	        	populateResource(record);
	        	warcTypeProperties = new WarcResourceProperties(this);
		        break;
	        case WarcConstants.RT_IDX_REQUEST:
	        	populateRequest(record);
	        	warcTypeProperties = new WarcRequestProperties(this);
		        break;
	        case WarcConstants.RT_IDX_METADATA:
	        	populateMetadata(record);
	        	warcTypeProperties = new WarcMetadataProperties(this);
		        break;
	        case WarcConstants.RT_IDX_REVISIT:
	        	populateRevisit(record);
	        	warcTypeProperties = new WarcRevisitProperties(this);
		        break;
	        case WarcConstants.RT_IDX_CONVERSION:
	        	populateConversion(record);
	        	warcTypeProperties = new WarcConversionProperties(this);
		        break;
	        case WarcConstants.RT_IDX_CONTINUATION:
	        	populateContinuation(record);
	        	warcTypeProperties = new WarcContinuationProperties(this);
		        break;
	        }
        }
		return warcTypeProperties;
	}

}
