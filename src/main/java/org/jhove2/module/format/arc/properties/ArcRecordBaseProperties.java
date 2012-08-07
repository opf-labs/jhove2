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

/**
 * Reportable properties class for a common ARC record base.
 *
 * @author nicl
 */
@Persistent
public class ArcRecordBaseProperties extends AbstractReportable {

    /** ARC record base data container. */
    protected ArcRecordData record;

    /**
     * Constructor required by the persistence layer.
     */
    public ArcRecordBaseProperties() {
    }

    /**
     * Construct ARC record base property instance with the supplied data.
     * @param record ARC record base property data
     */
    public ArcRecordBaseProperties(ArcRecordData record) {
        this.record = record;
    }

    /**
     * Report record offset in ARC file.
     * @return record offset in ARC file
     */
    @ReportableProperty(order = 1, value = "Record offset in ARC file.")
    public long getStartOffset() {
        return record.startOffset;
    }

    /**
     * Report url from ARC record.
     * @return ARC record url
     */
    @ReportableProperty(order = 2, value = "Arc Block description version header value.")
    public Integer getArcBlockDescVersion() {
        return record.blockDescVersion;
    }

    /**
     * Report url from ARC record.
     * @return ARC record url
     */
    @ReportableProperty(order = 3, value = "Arc-Url header value.")
    public String getUrl() {
        return record.url;
    }

    /**
     * Report ip-address from ARC record.
     * @return ARC record ip-address
     */
    @ReportableProperty(order = 4, value = "Arc-IP-Address header value.")
    public String getIpAddress() {
        return record.ipAddress;
    }

    /**
     * Report ip-address format as found in the ARC record,
     * either nothing, 4 or 6.
     * @return nothing, 4 or 6 depending on the ip-address format
     */
    @ReportableProperty(order = 5, value = "Ip-Address version.")
    public String getIpAddressVersion() {
        return record.ipVersion;
    }

    /**
     * Report date from ARC record.
     * @return ARC record date
     */
    @ReportableProperty(order = 6, value = "Arc-Date header value.")
    public String getArchiveDate() {
        return record.archiveDate;
    }

    /**
     * Report raw date from ARC record.
     * @return Raw ARC record date
     */
    @ReportableProperty(order = 7, value = "Raw Arc-Date header value.")
    public String getRawArchiveDate() {
        return record.rawArchiveDate;
    }

    /**
     * Report content-type from ARC record.
     * @return ARC record content-type
     */
    @ReportableProperty(order = 8, value = "Arc-Content-Type header value.")
    public String getContentType() {
        return record.contentType;
    }

    /**
     * Report length from ARC record
     * @return ARC record length
     */
    @ReportableProperty(order = 9, value = "Archive-Length header value.")
    public String getLength() {
        return record.length;
    }

    /**
     * Report protocol result code from ARC record.
     * @return ARC record protocol result code
     */
    @ReportableProperty(order = 10, value = "Result-Code header value.")
    public String getResultCode() {
        return record.resultCode;
    }

    /**
     * Report checksum from ARC record.
     * @return ARC record checksum
     */
    @ReportableProperty(order = 11, value = "Arc-Checksum header value.")
    public String getChecksum() {
        return record.checksum;
    }

    /**
     * Report location from ARC record.
     * @return ARC record location
     */
    @ReportableProperty(order = 12, value = "Arc-Location header value.")
    public String getLocation() {
        return record.location;
    }

    /**
     * Report offset from ARC record.
     * @return ARC record offset
     */
    @ReportableProperty(order = 13, value = "Arc-Offset header value.")
    public String getOffset() {
        return record.offset;
    }

    /**
     * Report filename from ARC record.
     * @return ARC record filename
     */
    @ReportableProperty(order = 14, value = "Arc-Filename header value.")
    public String getFilename() {
        return record.filename;
    }

    /**
     * Report if the ARC record has a payload.
     * @return boolean indicating whether this record has a payload
     */
    @ReportableProperty(order = 15, value = "hasPayload value.")
    public Boolean getHasPayload() {
        return record.bHasPayload;
    }

    /**
     * Report ARC record payload object size.
     * @return payload object size
     */
    @ReportableProperty(order = 16, value = "ObjectSize value.")
    public String getObjectSize() {
        return record.payloadLength;
    }

    /**
     * Report whether this record is compliant or not.
     * @return boolean indicating whether this record is compliant or not
     */
    @ReportableProperty(order = 17, value = "isNonCompliant value.")
    public Boolean getIsNonCompliant() {
        return record.bIsNonCompliant;
    }

    /**
     * Report computed block digest.
     * @return computed block digest
     */
    @ReportableProperty(order = 18, value = "Computed Block-Digest header value.")
    public String getComputedBlockDigest() {
        return record.computedBlockDigest;
    }

    /**
     * Report computed block digest algorithm.
     * @return computed block digest algorithm
     */
    @ReportableProperty(order = 19, value = "Computed Block-Digest-Algorithm value.")
    public String getComputedBlockDigestAlgorithm() {
        return record.computedBlockDigestAlgorithm;
    }

    /**
     * Report computed block digest encoding algorithm.
     * @return computed block digest encoding algorithm
     */
    @ReportableProperty(order = 20, value = "Computed Block-Digest-Encoding value.")
    public String getComputedBlockDigestEncoding() {
        return record.computedBlockDigestEncoding;
    }

    /**
     * Report computed payload digest.
     * @return computed payload digest
     */
    @ReportableProperty(order = 21, value = "Computed Payload-Digest header value.")
    public String getComputedPayloadDigest() {
        return record.computedPayloadDigest;
    }

    /**
     * Report computed payload digest algorithm.
     * @return computed payload digest algorithm
     */
    @ReportableProperty(order = 22, value = "Computed Payload-Digest-Algorithm value.")
    public String getComputedPayloadDigestAlgorithm() {
        return record.computedPayloadDigestAlgorithm;
    }

    /**
     * Report computed payload digest encoding algorithm.
     * @return computed payload digest encoding algorithm
     */
    @ReportableProperty(order = 23, value = "Computed Payload-Digest-Encoding value.")
    public String getComputedPayloadDigestEncoding() {
        return record.computedPayloadDigestEncoding;
    }

}
