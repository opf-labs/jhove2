/**
 * JHOVE2 - Next-generation architecture for format-aware characterization
 * <p>
 * Copyright (c) 2010 by The Regents of the University of California. All rights reserved.
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
		return byteOrdering + " (" +
		      ((byteOrdering.equals("II")? "little-endian" : "big-endian")) + ")";
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
