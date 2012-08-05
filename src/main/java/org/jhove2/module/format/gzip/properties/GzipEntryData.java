package org.jhove2.module.format.gzip.properties;

import java.util.Date;

import org.jhove2.core.reportable.AbstractReportable;
import org.jhove2.module.format.gzip.field.CompressionMethod;
import org.jhove2.module.format.gzip.field.CompressionType;
import org.jhove2.module.format.gzip.field.OperatingSystem;
import org.jwat.gzip.GzipEntry;

import com.sleepycat.persist.model.Persistent;

@Persistent
public class GzipEntryData {

	protected boolean isNonCompliant;
    protected int index;
    protected long offset;
    protected CompressionMethod method;
    protected CompressionType extraFlags;
    protected String fileName;
    protected OperatingSystem os;
    protected String comment;
    protected boolean asciiFlag;
    protected Integer readCrc16;
    protected int computedCrc16;

    /* Non immutable fields: use defensive copy in getter method. */
    protected Date date;
    protected byte[] extraFields;

    protected int errors = 0;
    protected long size  = -1L;
    protected long csize = -1L;
    protected long readISize = -1L;
    protected long computedISize = -1;
    protected int readCrc32;
    protected int computedCrc32;

    /**
     * Constructor required by the persistence layer.
     */
    public GzipEntryData() {
    }

    public GzipEntryData(GzipEntry entry) {
    	this.isNonCompliant = !entry.isCompliant();
    	this.offset = entry.getStartOffset();
    	this.method = CompressionMethod.fromValue(entry.cm);
    	this.extraFlags = CompressionType.fromValue(entry.xfl);
    	this.fileName = entry.fname;
    	this.os = OperatingSystem.fromValue(entry.os);
    	this.comment = entry.fcomment;
    	this.asciiFlag = entry.bFText;
    	this.readCrc16 = entry.crc16;
    	this.computedCrc16 = entry.comp_crc16;
    	this.csize = entry.compressed_size;
    	this.size = entry.uncompressed_size;
    	this.readCrc32 = entry.crc32;
    	this.computedCrc32 = entry.comp_crc32;
    	this.readISize = entry.isize;
    	this.computedISize = entry.comp_isize;
    }

    /**
     * Returns a persistent reportable warc record base property instance.
     * @return a persistent reportable warc record base property instance
     */
    public AbstractReportable getGzipEntryProperties() {
        return new GzipEntryProperties(this);
    }

}
