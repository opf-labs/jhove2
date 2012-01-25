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
 * Reportable properties class for an ARC record.
 *
 * @author nicl
 */
@Persistent
public class ArcRecordProperties extends AbstractReportable {

    /** ARC record data container. */
    protected ArcRecordData record;

    /**
     * Constructor required by the persistence layer.
     */
    public ArcRecordProperties() {
    }

    /**
     * Construct ARC record property instance with the supplied data.
     * @param record ARC record property data
     */
    public ArcRecordProperties(ArcRecordData record) {
        this.record = record;
    }

    /**
     * Report http response result code from ARC record payload.
     * @return http response result code
     */
    @ReportableProperty(order = 1, value = "ProtocolResultCode header value.")
    public String getProtocolResultCode() {
        return record.protocolResultCode;
    }

    /**
     * Report http response protocol version from ARC record payload.
     * @return http response protocol version
     */
    @ReportableProperty(order = 2, value = "ProtocolVersion header value.")
    public String getProtocolVersion() {
        return record.protocolVersion;
    }

    /**
     * Report http response content-type from ARC record payload.
     * @return http response content-type
     */
    @ReportableProperty(order = 3, value = "ProtocolContentType header value.")
    public String getProtocolContentType() {
        return record.protocolContentType;
    }

    /**
     * Report http response server header from ARC record payload.
     * @return http response server header
     */
    @ReportableProperty(order = 4, value = "ServerName header value.")
    public String getServerName() {
        return record.protocolServer;
    }

}
