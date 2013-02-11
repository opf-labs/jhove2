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
import org.jwat.arc.ArcDateParser;
import org.jwat.arc.ArcHeader;
import org.jwat.arc.ArcRecordBase;
import org.jwat.arc.ArcVersionHeader;
import org.jwat.common.HeaderLine;
import org.jwat.common.HttpHeader;
import org.jwat.common.Payload;
import org.jwat.common.PayloadWithHeaderAbstract;

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

	/** Start offset of record in input stream. */
    protected long startOffset;
    /** Number of bytes consumed validating record. */
    protected long consumed;

    /** Version 1 or 2 record format. */
    protected Integer blockDescVersion;

    /** URL read from header. */
    protected String url;
    /** Protocol read from URL. */
    public String protocol;
    /** IP-Address read from header. */
    protected String ipAddress;
    /** IP-Address version identified. (4 or 6) */
    protected String ipVersion;
    /** Archive-date from header, if valid. */
    protected String archiveDate;
    /** Archive-date read from header. */
    protected String rawArchiveDate;
    /** Content-type read from header. */
    protected String contentType;
    /** Archive-length read from header. */
    protected String length;
    /** Result-code read from header, if present. */
    protected String resultCode;
    /** Checksum read from header, if present. */
    protected String checksum;
    /** Location read from header, if present. */
    protected String location;
    /** Offset read from header, if present. */
    protected String offset;
    /** Filename read from header, if present. */
    protected String filename;

    /* Does the record have a payload. */
    protected Boolean bHasPayload;
    /** Payload length, without payload header (version block/HTTP header). */
    protected String payloadLength;
    /** Boolean indicating whether this record is compliant or not. */
    protected boolean bIsNonCompliant;

    /** Computed block digest. */
    protected String computedBlockDigest;
    /** Computed block digest algorithm. */
    protected String computedBlockDigestAlgorithm;
    /** Computed block digest encoding. */
    protected String computedBlockDigestEncoding;
    /** Computed payload digest, if applicable. */
    protected String computedPayloadDigest;
    /** Computed payload digest algorithm, if applicable. */
    protected String computedPayloadDigestAlgorithm;
    /** Computed payload digest encoding, if applicable. */
    protected String computedPayloadDigestEncoding;

    /** Version number field read from version block.*/
    protected String versionNumber;
    /** Reserved field read from version block. */
    protected String reserved;
    /** Origin-code field read from version block. */
    protected String originCode;

    /** Result-code read from HTTP header, if present. */
    protected String protocolResultCode;
    /** Protocol version read from HTTP header, if present. */
    protected String protocolVersion;
    /** Content-type read from HTTP header, if present. */
    protected String protocolContentType;
    /** Server header entry read from HTTP header, if present. */
    protected String protocolServer;

    /** ARC <code>DateFormat</code> as specified by the ARC specification. */
    protected transient DateFormat arcDateFormat = ArcDateParser.getDateFormat();

    /**
     * Constructor required by the persistence layer.
     */
    public ArcRecordData() {
    }

    /**
     * Constructs an object using the data in the <code>ArcRecordBase</code>
     * object.
     * @param record parsed ARC record
     */
    public ArcRecordData(ArcRecordBase record) {
    	if (record == null) {
    		throw new IllegalArgumentException("'record' should never be null");
    	}
        HeaderLine headerLine;
        switch (record.recordType) {
        case ArcRecordBase.RT_VERSION_BLOCK:
        	ArcVersionHeader versionHeader = record.versionHeader;
        	if (versionHeader != null) {
                if (versionHeader.versionNumber != null) {
                    versionNumber = versionHeader.versionNumberStr;
                }
                if (versionHeader.reserved != null) {
                    reserved = versionHeader.reservedStr;
                }
                originCode = versionHeader.originCode;
        	}
        	break;
        case ArcRecordBase.RT_ARC_RECORD:
            Payload payload = record.getPayload();
            if (payload != null) {
                PayloadWithHeaderAbstract payloadHeaderWrapped = payload.getPayloadHeaderWrapped();
                HttpHeader httpHeader = null;
                if (payloadHeaderWrapped instanceof HttpHeader) {
                    httpHeader = (HttpHeader)payloadHeaderWrapped;
                }
                if (httpHeader != null) {
                	if (httpHeader.headerType == HttpHeader.HT_RESPONSE) {
                        protocolResultCode = httpHeader.statusCodeStr;
                        protocolVersion = httpHeader.httpVersion;
                        protocolContentType = httpHeader.contentType;
                        headerLine = httpHeader.getHeader("server");
                        if (headerLine != null && headerLine.value != null) {
                            protocolServer = headerLine.value;
                        }
                	}
                }
            }
        	break;
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
    	ArcHeader header = record.header;
        startOffset = record.getStartOffset();
        consumed = record.getConsumed();
        blockDescVersion = header.recordFieldVersion;
        /*
         * Header.
         */
        url = header.urlStr;
        protocol = header.urlScheme;
        ipAddress = header.ipAddressStr;
        if (header.inetAddress != null) {
            if (header.inetAddress.getAddress().length == 4) {
                ipVersion = "4";
            }
            else if (header.inetAddress.getAddress().length == 16) {
                ipVersion = "6";
            }
        }
        rawArchiveDate = header.archiveDateStr;
        if (header.archiveDate != null) {
            archiveDate = arcDateFormat.format(header.archiveDate);
        }
        contentType = header.contentTypeStr;
        resultCode = header.resultCodeStr;
        checksum = header.checksumStr;
        location = header.locationStr;
        offset = header.offsetStr;
        filename = header.filenameStr;
        length = header.archiveLengthStr;
        /*
         * Payload.
         */
        bHasPayload = record.hasPayload() && !record.hasPseudoEmptyPayload();
        Payload payload = record.getPayload();
        if (payload != null) {
        	// payloadLength is reported back as ObjectSize in the Jhove2 specs
            PayloadWithHeaderAbstract payloadHeaderWrapped = payload.getPayloadHeaderWrapped();
            HttpHeader httpHeader = null;
            if (payloadHeaderWrapped instanceof HttpHeader) {
                httpHeader = (HttpHeader)payloadHeaderWrapped;
            }
            if (httpHeader != null) {
                payloadLength = Long.toString(httpHeader.getPayloadLength());
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
