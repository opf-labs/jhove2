/**
 * JHOVE2 - Next-generation architecture for format-aware characterization
 *
 * Copyright (c) 2009 by The Regents of the University of California,
 * Ithaka Harbors, Inc., and The Board of Trustees of the Leland Stanford
 * Junior University.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * o Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 *
 * o Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * o Neither the name of the University of California/California Digital
 *   Library, Ithaka Harbors/Portico, or Stanford University, nor the names of
 *   its contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package org.jhove2.module.format.warc.properties;

import java.util.ArrayList;
import java.util.List;

import org.jhove2.core.reportable.AbstractReportable;
import org.jwat.common.HeaderLine;
import org.jwat.common.HttpResponse;
import org.jwat.common.Payload;
import org.jwat.warc.WarcConstants;
import org.jwat.warc.WarcRecord;

import com.sleepycat.persist.model.Persistent;

/**
 * This class is a wrapper for the information available in an WARC record.
 * Since the WARC reader is not persistent its data must be moved to a simpler
 * data class which can be persisted instead.
 *
 * @author nicl
 */
@Persistent
public class WarcRecordData {

    public Long startOffset;
    public Long consumed;

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

    public String warcBlockDigest;
    public String warcBlockDigestAlgorithm;
    public String warcBlockDigestEncoding;
    public String warcPayloadDigest;
    public String warcPayloadDigestAlgorithm;
    public String warcPayloadDigestEncoding;

    public String computedBlockDigest;
    public String computedBlockDigestAlgorithm;
    public String computedBlockDigestEncoding;
    public String computedPayloadDigest;
    public String computedPayloadDigestAlgorithm;
    public String computedPayloadDigestEncoding;

    public String recordIdScheme;

    public Boolean bIsNonCompliant;
    public Boolean isValidBlockDigest;
    public Boolean isValidPayloadDigest;

    public Boolean bHasPayload;
    public String payloadLength;

    public String ipVersion;

    public String resultCode;
    public String protocolVersion;
    public String protocolContentType;
    public String protocolServer;
    public String protocolUserAgent;

    /**
     * Constructor required by the persistence layer.
     */
    public WarcRecordData() {
    }

    /**
     * Constructs an object using the data in the <code>WarcRecord</code>
     * object.
     * @param record parsed WARC record
     */
    public WarcRecordData(WarcRecord record) {
        startOffset = record.getOffset();
        consumed = record.getConsumed();
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
        /*
         * Warc-Block-Digest.
         */
        if (record.warcBlockDigest != null) {
            if ( record.warcBlockDigest.digestString != null
                    && record.warcBlockDigest.digestString.length() > 0) {
                warcBlockDigest = record.warcBlockDigest.digestString;
            }
            if (record.warcBlockDigest.algorithm != null
                    && record.warcBlockDigest.algorithm.length() > 0) {
                warcBlockDigestAlgorithm = record.warcBlockDigest.algorithm;
            }
            if (record.warcBlockDigest.encoding != null
                    && record.warcBlockDigest.encoding.length() > 0) {
                warcBlockDigestEncoding = record.warcBlockDigest.encoding;
            }
        }
        /*
         * Warc-Payload-Digest.
         */
        if (record.warcPayloadDigest != null) {
            if (record.warcPayloadDigest.digestString != null
                    && record.warcPayloadDigest.digestString.length() > 0) {
                warcPayloadDigest = record.warcPayloadDigest.digestString;
            }
            if (record.warcPayloadDigest.algorithm != null
                    && record.warcPayloadDigest.algorithm.length() > 0) {
                warcPayloadDigestAlgorithm = record.warcPayloadDigest.algorithm;
            }
            if (record.warcPayloadDigest.encoding != null
                    && record.warcPayloadDigest.encoding.length() > 0) {
                warcPayloadDigestEncoding = record.warcPayloadDigest.encoding;
            }
        }
        /*
         * Computed-Block-Digest.
         */
        if (record.computedBlockDigest != null) {
            if ( record.computedBlockDigest.digestString != null
                    && record.computedBlockDigest.digestString.length() > 0) {
                computedBlockDigest = record.computedBlockDigest.digestString;
            }
            if (record.computedBlockDigest.algorithm != null
                    && record.computedBlockDigest.algorithm.length() > 0) {
                computedBlockDigestAlgorithm = record.computedBlockDigest.algorithm;
            }
            if (record.computedBlockDigest.encoding != null
                    && record.computedBlockDigest.encoding.length() > 0) {
                computedBlockDigestEncoding = record.computedBlockDigest.encoding;
            }
        }
        /*
         * Computed-Payload-Digest.
         */
        if (record.computedPayloadDigest != null) {
            if (record.computedPayloadDigest.digestString != null
                    && record.computedPayloadDigest.digestString.length() > 0) {
                computedPayloadDigest = record.computedPayloadDigest.digestString;
            }
            if (record.computedPayloadDigest.algorithm != null
                    && record.computedPayloadDigest.algorithm.length() > 0) {
                computedPayloadDigestAlgorithm = record.computedPayloadDigest.algorithm;
            }
            if (record.computedPayloadDigest.encoding != null
                    && record.computedPayloadDigest.encoding.length() > 0) {
                computedPayloadDigestEncoding = record.computedPayloadDigest.encoding;
            }
        }
        /*
         * Record-Id scheme.
         */
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
        /*
         * Compliance.
         */
        bIsNonCompliant = !record.isCompliant();
        isValidBlockDigest = record.isValidBlockDigest;
        isValidPayloadDigest = record.isValidPayloadDigest;
        /*
         * Payload.
         */
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

    /**
     * Populate object with WarcInfo record type specific data which is to be
     * reported back as properties.
     * @return this object with populated data for WarcInfo properties
     */
    public WarcRecordData populateWarcinfo(WarcRecord record) {
        return this;
    }

    /**
     * Populate object with Response record type specific data which is to be
     * reported back as properties.
     * @return this object with populated data for Response properties
     */
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
            HeaderLine headerLine;
            if (httpResponse != null) {
                resultCode = httpResponse.resultCode;
                protocolVersion = httpResponse.protocolVersion;
                protocolContentType = httpResponse.contentType;
                headerLine = httpResponse.getHeader("server");
                if (headerLine != null && headerLine.value != null) {
                    protocolServer = headerLine.value;
                }
            }
        }
        return this;
    }

    /**
     * Populate object with Resource record type specific data which is to be
     * reported back as properties.
     * @return this object with populated data for Resource properties
     */
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

    /**
     * Populate object with Request record type specific data which is to be
     * reported back as properties.
     * @return this object with populated data for Request properties
     */
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
            HeaderLine headerLine;
            if (httpResponse != null) {
            	// TODO HttpRequest not supported yet in JWAT
                protocolVersion = httpResponse.protocolVersion;
                headerLine = httpResponse.getHeader("user-agent");
                if (headerLine != null && headerLine.value != null) {
                    protocolUserAgent = headerLine.value;
                }
            }
        }
        return this;
    }

    /**
     * Populate object with Metadata record type specific data which is to be
     * reported back as properties.
     * @return this object with populated data for Metadata properties
     */
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

    /**
     * Populate object with Revisit record type specific data which is to be
     * reported back as properties.
     * @return this object with populated data for Revisit properties
     */
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

    /**
     * Populate object with Conversion record type specific data which is to be
     * reported back as properties.
     * @return this object with populated data for Conversion properties
     */
    public WarcRecordData populateConversion(WarcRecord record) {
        return this;
    }

    /**
     * Populate object with Continuation record type specific data which is to be
     * reported back as properties.
     * @return this object with populated data for Continuation properties
     */
    public WarcRecordData populateContinuation(WarcRecord record) {
        return this;
    }

    /**
     * Returns a persistent reportable warc record base property instance.
     * @return a persistent reportable warc record base property instance
     */
    public AbstractReportable getWarcRecordBaseProperties() {
        return new WarcRecordBaseProperties(this);
    }

    /**
     * Returns a persistent reportable property instance based on the warc-type.
     * @return a persistent reportable property instance based on the warc-type
     */
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
