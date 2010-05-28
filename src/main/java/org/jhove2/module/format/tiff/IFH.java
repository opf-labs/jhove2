/**
 * 
 */
package org.jhove2.module.format.tiff;

import java.nio.ByteOrder;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.reportable.AbstractReportable;

/**
 * @author mstrong
 *
 */
public class IFH 
    extends AbstractReportable {
	
    /** byteOrder used within the TIFF file */
	protected ByteOrder byteOrder;
	
	/** String representing byteOrdering "II" little endian, "MM big endian */
	protected String byteOrdering;
	
    /** arbitrary but carefully chosen number to identify as 
     * TIFF (42 for TIFF, 43 for BigTIFF) */
    protected int magicNumber;
    
	/** 32-bit (4 bytes) offset (in bytes) of the first IFD */
	protected long firstIFD;
	
	protected long offset;
	
	public ByteOrder getByteOrder() {
		return byteOrder;
	}
	
	@ReportableProperty(order = 1, value = "IFH byte order.")
	public String getByteOrdering() {
		return byteOrdering;
	}
	
	@ReportableProperty(order = 3, value = "Offset of first IFD.") 
	public long getFirstIFD() {
		return firstIFD;
	}
	
	@ReportableProperty(order = 2, value = "IFH magic number.") 
	public int getMagicNumber() {
		return magicNumber;
	}
	
	@ReportableProperty(order = 4, value = "Byte offset of IFH.")
	public long getByteOffset() {
		return offset;
	}

    public void setByteOrder(ByteOrder endian) {
        byteOrder = endian;
    }

    public void setByteOrdering(String string) {
        byteOrdering = string;
    }

    public void setMagicNumber(int magic) {
        this.magicNumber = magic;
    }

    public void setOffset(long next) {
        this.offset = next;
    }
    public void setFirstIFD(long next) {
        this.firstIFD = next;
    }
	


}
