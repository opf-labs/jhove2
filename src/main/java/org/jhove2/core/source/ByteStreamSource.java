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

package org.jhove2.core.source;

import java.io.IOException;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.JHOVE2;

/** JHOVE2 byte stream source.  A byte stream source is always a child of
 * some other source unit.
 * 
 * @author mstrong, slabrams
 */
public class ByteStreamSource
    extends AbstractSource
{
    /** Starting offset relative to parent source. */
    protected long endingOffset;
     
    /** Parent source. */
    protected Source parent;
    
    /** Size of the byte stream, in bytes. */
    protected long size;
    
    /** Instantiate a new <code>ByteStreamSource</code>.  The new byte stream
     * is automatically added as a child reportable of its parent source unit.
     * @param jhove2 JHOVE2 framework
     * @param parent Parent source
     * @param offset Starting offset relative to parent
     * @param size   Size of the byte stream
     * @throws IOException 
     */
    public ByteStreamSource(JHOVE2 jhove2, Source parent, long offset, long size)
        throws IOException
    {
        super();
        
        this.parent         = parent;
        this.startingOffset = offset;
        this.endingOffset   = offset + size;
        this.size           = size;
        
        /* Make this byte stream a child of its parent. */
        parent.addChildSource(this);
     }
  
    /** Get ending offset of the byte stream, relative to its parent.
     * @return Ending offset
     */
    @ReportableProperty(order=2, value="Ending offset of the byte stream, relative to its parent.")
    public long getEndingOffset() {
        return this.endingOffset;
    }
     
    /** Get byte stream size, in bytes.
     * @return Byte stream size
     */
    @ReportableProperty(order=1, value="Byte stream size, in bytes.")
    public long getSize() {
        return this.size;
    }
}
