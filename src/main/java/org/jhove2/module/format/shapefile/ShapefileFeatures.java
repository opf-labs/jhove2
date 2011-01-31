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

import java.util.ArrayList;
import java.util.List;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.reportable.AbstractReportable;

import com.sleepycat.persist.model.Persistent;

/**
 * This class is used to hold all feature values extracted from the shapefile.
 * 
 * @author rnanders
 */
@Persistent
public class ShapefileFeatures extends AbstractReportable {

    /**
     * File filename stem shared by the Shapefile main, index, dBASE, etc.
     * files.
     */
    protected String shapefileStem = null;

    /** The list of member files comprising the shapefile. */
    public List<String> memberFiles; 

    /** The information extracted from the main shapefile header. */
    public ShapefileHeader shapefileHeader = new ShapefileHeader();

    /** The information extracted from the main shapefile record structures. */
    public List<ShapefileRecord> shapefileRecords = new ArrayList<ShapefileRecord>();

    /** The character set used to decode strings in the DBF file. */
    public String dbfCharsetName;

    /** The information extracted from the DBF file header. */
    public DbfHeader dbfHeader = new DbfHeader();

    /** The coordinate system read in from the project file, if it exists. */
    public String coordinateSystem = null;

    public ShapefileFeatures(){
    	super();
    }
    /**
     * Get filename stem shared by the Shapefile main, index, dBASE, etc files.
     * 
     * @return String shapefileStem
     */
    @ReportableProperty(order = 1, value = "Filename stem shared by all member files")
    public String getShapefileStem() {
        return this.shapefileStem;
    }

    /**
     * Gets the list of member files comprising the shapefile.
     * 
     * @return the member files
     */
    @ReportableProperty(order = 2, value = "Files that consitute the shapefile entity")
    public List<String> getMemberFiles() {
        return memberFiles;
    }

    /**
     * Gets the information extracted from the main shapefile header.
     * 
     * @return the shapefile header
     */
    @ReportableProperty(order = 3, value = "Shapefile Header")
    public ShapefileHeader getShapefileHeader() {
        return shapefileHeader;
    }

    /**
     * Gets the information extracted from the main shapefile record structures.
     * 
     * @return the shapefile records
     */
    public List<ShapefileRecord> getShapefileRecords() {
        return shapefileRecords;
    }

    /**
     * Gets the shapefile record count.
     * 
     * @return the shapefile record count
     */
    @ReportableProperty(order = 4, value = "Shapefile Record Count")
    public long getShapefileRecordCount() {
        return new Long(shapefileRecords.size());
    }

    /**
     * Gets the character set used to decode strings in the DBF file.
     * 
     * @return the character set used to decode strings in the DBF file
     */
    @ReportableProperty(order = 5, value = "DBF Character Set")
    public String getDbfCharsetName() {
        return dbfCharsetName;
    }

    /**
     * Gets the information extracted from the DBF file header.
     * 
     * @return the dbf header
     */
    @ReportableProperty(order = 6, value = "DBF Header")
    public DbfHeader getDbfHeader() {
        return dbfHeader;
    }

    /**
     * Gets the coordinate system information.
     * 
     * @return the coordinate system
     */
    @ReportableProperty(order = 7, value = "Coordinate System")
    public String getCoordinateSystem() {
        return coordinateSystem;
    }

}
