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

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.reportable.AbstractReportable;

import com.sleepycat.persist.model.Persistent;

/**
 * Reportable properties class for a WARC base record.
 *
 * @author nicl
 */
@Persistent
public class WarcRecordBaseProperties extends AbstractReportable {

    /** WARC record data container. */
    protected WarcRecordData record;

    /**
     * Constructor required by the persistence layer.
     */
    public WarcRecordBaseProperties() {
    }

    /**
     * Construct WARC record base property instance with the supplied data.
     * @param record WARC record data
     */
    public WarcRecordBaseProperties(WarcRecordData record) {
        this.record = record;
    }

    /**
     * Report record offset in WARC file.
     * @return record offset in WARC file
     */
    @ReportableProperty(order = 1, value = "Record offset in WARC file.")
    public Long getStartOffset() {
        return record.startOffset;
    }

    /**
     * Report date from WARC record.
     * @return date from WARC record
     */
    @ReportableProperty(order = 2, value = "Warc-Date header value.")
    public String getWarcVersion() {
        return record.warcVersionStr;
    }

    /**
     * Report date from WARC record.
     * @return date from WARC record
     */
    @ReportableProperty(order = 3, value = "Warc-Date header value.")
    public String getWarcDate() {
        return record.warcDate;
    }

    /**
     * Report record-id from WARC record.
     * @return record-id from WARC record
     */
    @ReportableProperty(order = 4, value = "Warc-Record-ID header value.")
    public String getWarcRecordId() {
        return record.warcRecordId;
    }

    /**
     * Report record-id scheme used.
     * @return record-id scheme used
     */
    @ReportableProperty(order = 5, value = "Record-ID-Scheme value.")
    public String getRecordIdScheme() {
        return record.recordIdScheme;
    }

    /**
     * Report content-type from WARC record.
     * @return content-type from WARC record
     */
    @ReportableProperty(order = 6, value = "Content-Type header value.")
    public String getContentType() {
        return record.contentType;
    }

    /**
     * Report content-length from WARC record.
     * @return content-length from WARC record
     */
    @ReportableProperty(order = 7, value = "Content-Length header value.")
    public String getContentLength() {
        return record.contentLength;
    }

    /**
     * Report warc-type from WARC record.
     * @return warc-type from WARC record
     */
    @ReportableProperty(order = 8, value = "Warc-Type header value.")
    public String getWarcType() {
        return record.warcType;
    }

    /**
     * Report block digest from WARC record.
     * @return block digest from WARC record
     */
    @ReportableProperty(order = 9, value = "Warc-Block-Digest header value.")
    public String getWarcBlockDigest() {
        return record.warcBlockDigest;
    }

    /**
     * Report block digest algorithm from WARC record.
     * @return block digest algorithm from WARC record
     */
    @ReportableProperty(order = 10, value = "Block-Digest-Algorithm value.")
    public String getBlockDigestAlgorithm() {
        return record.warcBlockDigestAlgorithm;
    }

    /**
     * Report block digest encoding, if identified.
     * @return block digest encoding, if identified
     */
    @ReportableProperty(order = 11, value = "Block-Digest-Encoding value.")
    public String getBlockDigestEncoding() {
        return record.warcBlockDigestEncoding;
    }

    /**
     * Report if block digest is valid, if checked.
     * @return if block digest is valid, if checked
     */
    @ReportableProperty(order = 12, value = "isValidBlockDigest boolean value.")
    public Boolean getIsValidBlockDigest() {
        return record.isValidBlockDigest;
    }

    /**
     * Report payload digest from WARC record.
     * @return payload digest from WARC record
     */
    @ReportableProperty(order = 13, value = "Warc-Payload-Digest header value.")
    public String getWarcPayloadDigest() {
        return record.warcPayloadDigest;
    }

    /**
     * Report payload digest algorithm from WARC record.
     * @return payload digest algorithm from WARC record
     */
    @ReportableProperty(order = 14, value = "Payload-Digest-Algorithm value.")
    public String getPayloadDigestAlgorithm() {
        return record.warcPayloadDigestAlgorithm;
    }

    /**
     * Report payload digest encoding, if identified.
     * @return payload digest encoding, if identified
     */
    @ReportableProperty(order = 15, value = "Payload-Digest-Encoding value.")
    public String getPayloadDigestEncoding() {
        return record.warcPayloadDigestEncoding;
    }

    /**
     * Report if payload digest is valid, if checked.
     * @return if payload digest is valid, if checked
     */
    @ReportableProperty(order = 16, value = "isValidPayloadDigest boolean value.")
    public Boolean getIsValidPayloadDigest() {
        return record.isValidPayloadDigest;
    }

    /**
     * Report truncation reason from WARC record.
     * @return truncation reason from WARC record
     */
    @ReportableProperty(order = 17, value = "Warc-Truncated header value.")
    public String getWarcTruncated() {
        return record.warcTruncated;
    }

    /**
     * Report if the WARC record has a payload.
     * @return boolean indicating whether this record has a payload
     */
    @ReportableProperty(order = 18, value = "hasPayload value.")
    public Boolean getHasPayload() {
        return record.bHasPayload;
    }

    /**
     * Report payload length, which depends on a leading http response being
     * identified or not.
     * @return payload length, adjusted according to the actual payload content
     */
    @ReportableProperty(order = 19, value = "PayloadLength value.")
    public String getPayloadLength() {
        return record.payloadLength;
    }

    /**
     * Report identified-payload-type from WARC record.
     * @return identified-payload-type from WARC record
     */
    @ReportableProperty(order = 20, value = "Warc-Identified-Payload-Type header value.")
    public String getProtocolVersion() {
        return record.warcIdentifiedPayloadType;
    }

    /**
     * Report segment-number from WARC record.
     * @return segment-number from WARC record
     */
    @ReportableProperty(order = 21, value = "Warc-Segment-Number header value.")
    public String getWarcSegmentNumber() {
        return record.warcSegmentNumber;
    }

    /**
     * Report whether this record is compliant or not.
     * @return boolean indicating whether this record is compliant or not
     */
    @ReportableProperty(order = 22, value = "isNonCompliant value.")
    public Boolean getIsNonCompliant() {
        return record.bIsNonCompliant;
    }

    /**
     * Report computed block digest.
     * @return computed block digest
     */
    @ReportableProperty(order = 23, value = "Computed Block-Digest header value.")
    public String getComputedBlockDigest() {
        return record.computedBlockDigest;
    }

    /**
     * Report computed block digest algorithm.
     * @return computed block digest algorithm
     */
    @ReportableProperty(order = 24, value = "Computed Block-Digest-Algorithm value.")
    public String getComputedBlockDigestAlgorithm() {
        return record.computedBlockDigestAlgorithm;
    }

    /**
     * Report computed block digest encoding algorithm.
     * @return computed block digest encoding algorithm
     */
    @ReportableProperty(order = 25, value = "Computed Block-Digest-Encoding value.")
    public String getComputedBlockDigestEncoding() {
        return record.computedBlockDigestEncoding;
    }

    /**
     * Report computed payload digest.
     * @return computed payload digest
     */
    @ReportableProperty(order = 26, value = "Computed Payload-Digest header value.")
    public String getComputedPayloadDigest() {
        return record.computedPayloadDigest;
    }

    /**
     * Report computed payload digest algorithm.
     * @return computed payload digest algorithm
     */
    @ReportableProperty(order = 27, value = "Computed Payload-Digest-Algorithm value.")
    public String getComputedPayloadDigestAlgorithm() {
        return record.computedPayloadDigestAlgorithm;
    }

    /**
     * Report computed payload digest encoding algorithm.
     * @return computed payload digest encoding algorithm
     */
    @ReportableProperty(order = 28, value = "Computed Payload-Digest-Encoding value.")
    public String getComputedPayloadDigestEncoding() {
        return record.computedPayloadDigestEncoding;
    }

}
