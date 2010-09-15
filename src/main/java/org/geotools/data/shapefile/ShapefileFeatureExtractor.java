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
package org.geotools.data.shapefile;

import com.vividsolutions.jts.geom.GeometryFactory;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.shapefile.dbf.DbaseFileHeader;
import org.geotools.data.shapefile.dbf.DbaseFileReader;
import org.geotools.data.shapefile.indexed.IndexType;
import org.geotools.data.shapefile.indexed.IndexedShapefileDataStore;
import org.geotools.data.shapefile.prj.PrjFileReader;
import org.geotools.data.shapefile.shp.ShapefileHeader;
import org.geotools.data.shapefile.shp.ShapefileReader;
import org.jhove2.module.format.shapefile.DbfHeader;
import org.jhove2.module.format.shapefile.ShapefileRecord;
import org.jhove2.module.format.shapefile.ShapefileFeatures;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Provides an interface to the geotools shapefile readers that are used to
 * extract properties of the shapefile.<br />
 * This class is in the geotools package namespace, allowing it to access
 * protected methods inside of those readers.
 * 
 * @author rnanders
 */
public class ShapefileFeatureExtractor {

    /**
     * The main shapefile (shp extension).
     */
    File shpFile;

    /**
     * The Geotools API inteface to the files comprising the Shapefile.
     */
    ShapefileDataStore shapefileDS;

    /**
     * The Geotools main shapefile parser.
     */
    ShapefileReader shpReader;

    /**
     * Instantiates a new shapefile feature extractor.
     * 
     * @param shpFile
     *            the main (shp extension) file
     * @throws Exception
     *             if the shapefile datastore cannot be created
     */
    public ShapefileFeatureExtractor(File shpFile) throws Exception {
        this.shpFile = shpFile;
        URL url = shpFile.toURI().toURL();
        URI namespace = null;
        boolean useMemoryMappedBuffer = false;
        shapefileDS = new ShapefileDataStore(url, namespace,
           useMemoryMappedBuffer);
        
    }

    /**
     * Extract all interesting features from the shapefile using Geotools
     * parsers
     * 
     * @param reportables
     *            the class used to hold the features values found
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public void extractFeatures(ShapefileFeatures reportables)
            throws IOException {
        try {
            shpReader = shapefileDS.openShapeReader(new GeometryFactory());
            reportables.dbfCharsetName = shapefileDS.getStringCharset().name();
            extractHeader(reportables);
            extractRecords(reportables);
            extractDbfHeader(reportables);
            extractProject(reportables);
        }
        finally {
            shpReader.close();
        }
    }

    /**
     * Extract information from the main shapefile header.
     * 
     * @param reportables
     *            the class used to hold the features values found
     */
    private void extractHeader(ShapefileFeatures features) {
        org.jhove2.module.format.shapefile.ShapefileHeader shr = features.shapefileHeader;
        ShapefileHeader shpHeader = shpReader.getHeader();
        shr.fileLength = shpHeader.getFileLength();
        shr.version = shpHeader.getVersion();
        shr.shapeTypeID = shpHeader.getShapeType().id;
        shr.shapeType = shpHeader.getShapeType().toString();
        shr.minX = shpHeader.minX();
        shr.maxX = shpHeader.maxX();
        shr.minY = shpHeader.minY();
        shr.maxY = shpHeader.maxY();
    }

    /**
     * Extract record information from the main shapefile.
     * 
     * @param reportables
     *            the class used to hold the features values found
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private void extractRecords(ShapefileFeatures features)
            throws IOException {
        List<ShapefileRecord> srecs = features.shapefileRecords;
        while (shpReader.hasNext()) {
            ShapefileReader.Record record = shpReader.nextRecord();
            ShapefileRecord srec = new ShapefileRecord();
            srec.offset = record.offset();
            srec.shapeTypeID = record.type.id;
            srec.shapeType = record.type.toString();
            srec.minX = record.minX;
            srec.maxX = record.maxX;
            srec.minY = record.minY;
            srec.maxY = record.maxY;
            srecs.add(srec);
        }
    }

    /**
     * Extract information from the DBF file header.
     * 
     * @param reportables
     *            the class used to hold the features values found
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private void extractDbfHeader(ShapefileFeatures features)
            throws IOException {
        DbaseFileReader dbfReader = null;
        try {
            dbfReader = shapefileDS.openDbfReader();
            DbaseFileHeader dbfHeader = dbfReader.getHeader();
            DbfHeader dbfh = features.dbfHeader;
            dbfh.recordCount = dbfHeader.getNumRecords();
            dbfh.recordLength = dbfHeader.getRecordLength();
            dbfh.fieldCount = dbfHeader.getNumFields();
            for (int i = 0; i < dbfHeader.getNumFields(); i++) {
                dbfh.fieldNames.add(dbfHeader.getFieldName(i));
            }
        }
        finally {
            if (dbfReader != null) {
                dbfReader.close();
            }
        }
    }

    /**
     * Extract coordinate information from the project (prj) file, if it exists
     * 
     * @param reportables
     *            the class used to hold the features values found
     */
    private void extractProject(ShapefileFeatures features) {
        PrjFileReader prj = null;
        try {
            prj = shapefileDS.openPrjReader();
            features.coordinateSystem = prj.getCoodinateSystem().toString()
                    .replaceAll("[\r\n]", "").replaceAll("  *", " ");
        }
        catch (Exception e) {
            return;
        }
        finally {
            if (prj != null) {
                try {
                    prj.close();
                }
                catch (IOException e) {
                    // ignore close error
                }
            }
        }
    }

}
