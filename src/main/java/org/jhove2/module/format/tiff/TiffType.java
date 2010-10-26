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

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.SortedSet;
import java.util.TreeSet;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;


/**
 * TiffType
 * 
 * Enum to store the defined TIFF Types.  
 *  
 * @author mstrong
 *
 */

public enum TiffType {
    BYTE       (1, 1),
    ASCII      (2, 1),
    SHORT      (3, 2),  /*(unsigned 16-bit) type */
    LONG       (4, 4),  /*(unsigned 32-bit) type */
    RATIONAL   (5, 8),  /*(two LONGs) type*/
    SBYTE      (6, 1),  /*(signed 8-bit) type */
    UNDEFINED  (7, 1),  /*(unsigned 8-bit) type */
    SSHORT     (8, 2),  /*(signed 16-bit) type */
    SLONG      (9, 4),  /*(signed 32-bit) type */
    SRATIONAL  (10, 8), /*(two SLONGs) type */
    FLOAT      (11, 2), /*(32-bit IEEE floating point) type */
    DOUBLE     (12, 8), /*(64-bit IEEE floating point) type */
    IFD        (13, 4); /*(LONG) type */


    /**
     *  field type number
     */
    private final int num;  

    /**
     *  size of field
     */
    private final int size; 
    TiffType(int num, int size) {
        this.num = num;
        this.size = size;
    }

    /**
     * @return int
     */
    public int num() {
        return num;
    }
    
    /**
     * 
     * @return int
     */
    public int size() {
        return size;
    }
    
    /**
     * Returns the TiffType Enum associated with the num
     * @param num
     * @return
     */
    public static TiffType getType(int num) {
        TiffType match = null;
        for (TiffType type : TiffType.values()) {
            if (type.num == num) {
                match = type;
                break;
            }
        }
        return match;
    }
}

