/**
 * JHOVE2 - Next-generation architecture for format-aware characterization
 * <p>
 * Copyright (c) 2009 by The Regents of the University of California, Ithaka
 * Harbors, Inc., and The Board of Trustees of the Leland Stanford Junior
 * University. All rights reserved.
 * </p>
 * <p>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * </p>
 * <ul>
 * <li>Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.</li>
 * <li>Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.</li>
 * <li>Neither the name of the University of California/California Digital
 * Library, Ithaka Harbors/Portico, or Stanford University, nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.</li>
 * </ul>
 * <p>
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
 * </p>
 */
package org.jhove2.module.format.shapefile;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.reportable.AbstractReportable;

import java.util.List;
import java.util.ArrayList;


/**
 * This class holds feature information extracted from the DBF file's header
 * 
 * @author rnanders
 */
public class DbfHeader extends AbstractReportable {

    /** the number of records in the file. */
    public long recordCount;

    /** the length of the records in bytes. */
    public long recordLength;

    /** the number of fields in the records. */
    public long fieldCount;

    /** The names of the table columns. */
    public List<String> fieldNames = new ArrayList<String>();

    /**
     * Gets the number of records in the file.
     *
     * @return the record count
     */
    @ReportableProperty(order = 1, value ="Record Count")
    public long getRecordCount() {
        return recordCount;
    }

    /**
     * Gets the length of the records in bytes.
     *
     * @return the record length
     */
    @ReportableProperty(order = 2, value ="Record Length")
    public long getRecordLength() {
        return recordLength;
    }

    /**
     * Gets the number of fields in the records.
     *
     * @return the field count
     */
    @ReportableProperty(order = 3, value ="Field Count")
    public long getFieldCount() {
        return fieldCount;
    }

    /**
     * Gets the names of the table columns.
     *
     * @return the field names
     */
    @ReportableProperty(order = 4, value ="Field Names")
    public List<String> getFieldNames() {
        return fieldNames;
    }
}
