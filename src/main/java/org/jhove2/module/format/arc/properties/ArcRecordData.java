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

import java.text.DateFormat;

import org.jhove2.core.reportable.AbstractReportable;
import org.jwat.arc.ArcRecord;
import org.jwat.arc.ArcRecordBase;
import org.jwat.arc.ArcVersionBlock;
import org.jwat.common.HttpResponse;
import org.jwat.common.Payload;
import org.jwat.warc.WarcDateParser;

import com.sleepycat.persist.model.Persistent;

@Persistent
public class ArcRecordData {

    public String ipAddress;
    public String ipVersion;
    public String archiveDate;
    public String rawArchiveDate;
    public String contentType;
    public String length;
    public String resultCode;
    public String checksum;
    public String location;
    public String offset;
    public String filename;
    public Boolean bHasPayload;
    public String payloadLength;
    public boolean bIsNonCompliant;

    public String blockDigest;
    public String blockDigestAlgorithm;
    public String blockDigestEncoding;
    public String payloadDigest;
    public String payloadDigestAlgorithm;
    public String payloadDigestEncoding;

    public String versionNumber;
    public String reserved;
    public String originCode;

    public String protocolResultCode;
    public String protocolVersion;
    public String protocolContentType;
    public String serverName;

    /** WARC <code>DateFormat</code> as specified by the WARC ISO standard. */
    public transient DateFormat warcDateFormat = WarcDateParser.getWarcDateFormat();

    public ArcRecordData() {
    }

    public ArcRecordData(ArcVersionBlock versionBlock) {
        if (versionBlock.versionNumber != null) {
            // TODO JWAT should keep the raw value too.
            versionNumber = versionBlock.versionNumber.toString();
        }
        if (versionBlock.reserved != null) {
            // TODO JWAT should keep the raw value too.
            reserved = versionBlock.reserved.toString();
        }
        originCode = versionBlock.originCode;
        populateArcRecordBase(versionBlock);
    }

    public ArcRecordData(ArcRecord record) {
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
        populateArcRecordBase(record);
    }

    public void populateArcRecordBase(ArcRecordBase record) {
        ipAddress = record.recIpAddress;
        if (record.inetAddress != null) {
            if (record.inetAddress.getAddress().length == 4) {
                ipVersion = "4";
            }
            else {
                ipVersion = "6";
            }
        }
        if (record.archiveDate != null) {
            archiveDate = warcDateFormat.format(record.archiveDate);
        }
        rawArchiveDate = record.recArchiveDate;
        contentType = record.recContentType;
        if (record.recLength != null) {
            // TODO JWAT should probably same the raw value too.
            length = record.recLength.toString();
        }
        if (record.recResultCode != null) {
            // TODO JWAT should probably same the raw value too.
            resultCode = record.recResultCode.toString();
        }
        checksum = record.recChecksum;
        location = record.recLocation;
        if (record.recOffset != null) {
            // TODO JWAT should probably same the raw value too.
            offset = record.recOffset.toString();
        }
        filename = record.recFilename;

        bHasPayload = record.hasPayload();
        Payload payload = record.getPayload();
        if (payload != null) {
            // TODO Verify meaning of ObjectSize in JHove2 ARC specs
            HttpResponse httpResponse = payload.getHttpResponse();
            if (httpResponse != null) {
                payloadLength = Long.toString(httpResponse.getPayloadLength());
            }
            else {
                payloadLength = Long.toString(payload.getTotalLength());;
            }
        }

        // TODO What does this imply, add functionality to JWAT.
        bIsNonCompliant = false;

        /*
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
        */

        /*
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
        */
    }

    public AbstractReportable getArcRecordBaseProperties() {
        return new ArcRecordBaseProperties(this);
    }

    public AbstractReportable getArcVersionBlockProperties() {
        return new ArcVersionBlockProperties(this);
    }

    public AbstractReportable getArcRecordProperties() {
        return new ArcRecordProperties(this);
    }

}
