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
import org.jwat.common.HeaderLine;
import org.jwat.common.HttpResponse;
import org.jwat.common.Payload;
import org.jwat.warc.WarcDateParser;

import com.sleepycat.persist.model.Persistent;

/**
 * This class is a wrapper for the information available in an ARC record.
 * Since the ARC reader is not persistent its data must be moved to a simpler
 * data class which can be persisted instead.
 *
 * @author nicl
 */
@Persistent
public class ArcRecordData {

    protected Long startOffset;
    protected Long consumed;

    protected String url;
    public String protocol;
    protected String ipAddress;
    protected String ipVersion;
    protected String archiveDate;
    protected String rawArchiveDate;
    protected String contentType;
    protected String length;
    protected String resultCode;
    protected String checksum;
    protected String location;
    protected String offset;
    protected String filename;
    protected Boolean bHasPayload;
    protected String payloadLength;
    protected boolean bIsNonCompliant;

    protected String computedBlockDigest;
    protected String computedBlockDigestAlgorithm;
    protected String computedBlockDigestEncoding;
    protected String computedPayloadDigest;
    protected String computedPayloadDigestAlgorithm;
    protected String computedPayloadDigestEncoding;

    protected String versionNumber;
    protected String reserved;
    protected String originCode;

    protected String protocolResultCode;
    protected String protocolVersion;
    protected String protocolContentType;
    protected String protocolServer;

    /** WARC <code>DateFormat</code> as specified by the WARC ISO standard. */
    protected transient DateFormat warcDateFormat = WarcDateParser.getWarcDateFormat();

    /**
     * Constructor required by the persistence layer.
     */
    public ArcRecordData() {
    }

    /**
     * Constructs an object using the data in the <code>VersionBlock</code>
     * object.
     * @param versionBlock parsed ARC version block
     */
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

    /**
     * Constructs an object used the data in the <code>ArcRecord</code>
     * object.
     * @param record parsed ARC record
     */
    public ArcRecordData(ArcRecord record) {
        Payload payload = record.getPayload();
        if (payload != null) {
            HttpResponse httpResponse = payload.getHttpResponse();
            HeaderLine headerLine;
            if (httpResponse != null) {
                resultCode = httpResponse.resultCodeStr;
                protocolVersion = httpResponse.protocolVersion;
                protocolContentType = httpResponse.contentType;
                headerLine = httpResponse.getHeader("server");
                if (headerLine != null && headerLine.value != null) {
                    protocolServer = headerLine.value;
                }
            }
        }
        populateArcRecordBase(record);
    }

    /**
     * Populate this object with the common data available in the
     * <code>ArcRecordBase</code> which is extended by both the version block
     * and arc record classes.
     * @param record record containing common data
     */
    protected void populateArcRecordBase(ArcRecordBase record) {
        startOffset = record.getOffset();
        consumed = record.getConsumed();
        url = record.recUrl;
        protocol = record.protocol;
        ipAddress = record.recIpAddress;
        if (record.inetAddress != null) {
            if (record.inetAddress.getAddress().length == 4) {
                ipVersion = "4";
            }
            else if (record.inetAddress.getAddress().length == 16) {
                ipVersion = "6";
            }
        }
        if (record.archiveDate != null) {
            archiveDate = warcDateFormat.format(record.archiveDate);
        }
        rawArchiveDate = record.recArchiveDate;
        contentType = record.recContentType;
        if (record.recLength != null) {
            // TODO JWAT should probably save the raw value too.
            length = record.recLength.toString();
        }
        if (record.recResultCode != null) {
            // TODO JWAT should probably save the raw value too.
            resultCode = record.recResultCode.toString();
        }
        checksum = record.recChecksum;
        location = record.recLocation;
        if (record.recOffset != null) {
            // TODO JWAT should probably save the raw value too.
            offset = record.recOffset.toString();
        }
        filename = record.recFilename;
        /*
         * Payload.
         */
        bHasPayload = record.hasPayload();
        Payload payload = record.getPayload();
        if (payload != null) {
        	// payloadLength is reported back as ObjectSize in the Jhove2 specs
            HttpResponse httpResponse = payload.getHttpResponse();
            if (httpResponse != null) {
                payloadLength = Long.toString(httpResponse.getPayloadLength());
            }
            else {
                payloadLength = Long.toString(payload.getTotalLength());;
            }
        }
        /*
         * Compliance.
         */
        bIsNonCompliant = !record.isCompliant();
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
    }

    /**
     * Returns a persistent reportable arc record base property instance.
     * @return a persistent reportable arc record base property instance
     */
    public AbstractReportable getArcRecordBaseProperties() {
        return new ArcRecordBaseProperties(this);
    }

    /**
     * Returns a persistent reportable arc version block property instance.
     * @return a persistent reportable arc version block property instance
     */
    public AbstractReportable getArcVersionBlockProperties() {
        return new ArcVersionBlockProperties(this);
    }

    /**
     * Returns a persistent reportable arc record property instance.
     * @return a persistent reportable arc record property instance
     */
    public AbstractReportable getArcRecordProperties() {
        return new ArcRecordProperties(this);
    }

}
