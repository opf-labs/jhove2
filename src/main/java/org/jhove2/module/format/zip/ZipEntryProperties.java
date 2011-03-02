/**
 * 
 */

package org.jhove2.module.format.zip;

import java.util.Date;
import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.Digest;
import org.jhove2.core.reportable.AbstractReportable;
import com.sleepycat.persist.model.Persistent;

/** Zip entry-specific properties.
 * 
 * @author slabrams
 */
@Persistent
public class ZipEntryProperties
        extends AbstractReportable
{
    /** Entry comment. */
    protected String comment;
    
    /** Entry compressed size. */
    protected long compressedSize;
    
    /** Entry CRC-32 message digest. */
    protected Digest crc32;
    
    /** Entry last modified date. */
    protected Date lastModified;
    
    /** Entry name. */
    protected String name;
    
    /** Zero argument constructor. */
    public ZipEntryProperties()
    {
        super();
    }

    /** Instantiate a new <code>ZipEntryProperties</code> reportable.
     * 
     */
    public ZipEntryProperties(String name, long size, Digest crc32, String comment, Date lastModified) {
        super();
        this.name           = name;
        this.compressedSize = size;
        this.crc32          = crc32;
        this.comment        = comment;
        this.lastModified   = lastModified;
    }

    /**
     * Get entry comment.
     * @return Entry comment
     */
    @ReportableProperty(order = 5, value = "Zip entry comment.")
    public String getComment() {
        return this.comment;
    }

    /**
     * Get entry compressed size.
     * @return Entry compressed size
     */
    @ReportableProperty(order = 3, value = "Zip entry comment.")
    public long getCompressedSize() {
        return this.compressedSize;
    }
    
    /**
     * Get entry CRC-32 message digest.
     * @return Entry CRC-32 message digest
     */
    @ReportableProperty(order = 4, value = "Zip entry CRC-32 message digest.")
    public Digest getCRC32MessageDigest() {
        return this.crc32;
    }
    
    /**
     * Get entry last modified date.
     * @return Entry last modified date
     */
    @ReportableProperty(order = 2, value = "Zip entry last modified date.")
    public Date getLastModified() {
        return this.lastModified;
    }

    /**
     * Get entry name.
     * @return Entry name
     */
    @ReportableProperty(order = 1, value = "Zip entry name.")
    public String getName() {
        return this.name;
    }
}
