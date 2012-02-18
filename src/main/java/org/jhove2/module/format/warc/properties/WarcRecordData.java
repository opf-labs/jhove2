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
 * Note: Some populate methods currently do not include any functionality.
 * However they are included for backwards compatibility in case the ISO
 * standard changes and extra properties are required.
 *
 * @author nicl
 */
@Persistent
public class WarcRecordData {

    protected Long startOffset;
    protected Long consumed;

    protected String warcType;
    protected String warcFilename;
    protected String warcRecordId;
    protected String warcDate;
    protected String contentLength;
    protected String contentType;
    protected String warcTruncated;
    protected String warcIpAddress;
    protected List<String> warcConcurrentToList;
    protected String warcRefersTo;
    protected String warcTargetUri;
    protected String warcWarcinfoId;
    protected String warcIdentifiedPayloadType;
    protected String warcProfile;
    protected String warcSegmentNumber;
    protected String warcSegmentOriginId;
    protected String warcSegmentTotalLength;

    protected String warcBlockDigest;
    protected String warcBlockDigestAlgorithm;
    protected String warcBlockDigestEncoding;
    protected String warcPayloadDigest;
    protected String warcPayloadDigestAlgorithm;
    protected String warcPayloadDigestEncoding;

    protected String computedBlockDigest;
    protected String computedBlockDigestAlgorithm;
    protected String computedBlockDigestEncoding;
    protected String computedPayloadDigest;
    protected String computedPayloadDigestAlgorithm;
    protected String computedPayloadDigestEncoding;

    protected String recordIdScheme;

    protected Boolean bIsNonCompliant;
    protected Boolean isValidBlockDigest;
    protected Boolean isValidPayloadDigest;

    protected Boolean bHasPayload;
    protected String payloadLength;

    protected String ipVersion;

    protected String resultCode;
    protected String protocolVersion;
    protected String protocolContentType;
    protected String protocolServer;
    protected String protocolUserAgent;

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
        HeaderLine headerLine;
        if (payload != null) {
            // TODO Verify meaning of JHove2 WARC draft
            HttpResponse httpResponse = payload.getHttpResponse();
            if (httpResponse != null) {
                payloadLength = Long.toString(httpResponse.getPayloadLength());
                // TODO Fix this when request and response are separate objects.
                protocolVersion = httpResponse.protocolVersion;
                resultCode = httpResponse.resultCodeStr;
                protocolContentType = httpResponse.contentType;
                headerLine = httpResponse.getHeader("server");
                if (headerLine != null && headerLine.value != null) {
                    protocolServer = headerLine.value;
                }
            	// TODO HttpRequest not supported yet in JWAT
                protocolVersion = httpResponse.protocolVersion;
                headerLine = httpResponse.getHeader("user-agent");
                if (headerLine != null && headerLine.value != null) {
                    protocolUserAgent = headerLine.value;
                }
            }
            else {
                payloadLength = Long.toString(payload.getTotalLength());;
            }
        }
        /*
         * IpVersion, common for several record properties.
         */
        if (record.warcInetAddress != null) {
            if (record.warcInetAddress.getAddress().length == 4) {
                ipVersion = "4";
            }
            else {
                ipVersion = "6";
            }
        }
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
                warcTypeProperties = new WarcWarcinfoProperties(this);
                break;
            case WarcConstants.RT_IDX_RESPONSE:
                warcTypeProperties = new WarcResponseProperties(this);
                break;
            case WarcConstants.RT_IDX_RESOURCE:
                warcTypeProperties = new WarcResourceProperties(this);
                break;
            case WarcConstants.RT_IDX_REQUEST:
                warcTypeProperties = new WarcRequestProperties(this);
                break;
            case WarcConstants.RT_IDX_METADATA:
                warcTypeProperties = new WarcMetadataProperties(this);
                break;
            case WarcConstants.RT_IDX_REVISIT:
                warcTypeProperties = new WarcRevisitProperties(this);
                break;
            case WarcConstants.RT_IDX_CONVERSION:
                warcTypeProperties = new WarcConversionProperties(this);
                break;
            case WarcConstants.RT_IDX_CONTINUATION:
                warcTypeProperties = new WarcContinuationProperties(this);
                break;
            }
        }
        return warcTypeProperties;
    }

}
