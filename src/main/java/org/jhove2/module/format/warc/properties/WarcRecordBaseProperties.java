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
