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
