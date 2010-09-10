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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteOrder;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.io.Input;
import org.jhove2.core.io.Input.Type;

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
    
    /** Starting offset relative to parent source. */
    protected long startingOffset;
    
    /** Parent source. */
    protected Source parent;
    
    /** Size of the byte stream, in bytes. */
    protected long size;
    
    /** Instantiate a new <code>ByteStreamSource</code>.  The new byte stream
     * is automatically added as a child reportable of its parent source unit.
     * 
     * @param parent Parent source
     * @param offset Starting offset relative to parent
     * @param size   Size of the byte stream
     * @throws IOException 
     */
    public ByteStreamSource(Source parent, long offset, long size)
        throws IOException
    {
        super();
        
        this.parent         = parent;
        this.startingOffset = offset;
        this.endingOffset   = offset + size;
        this.size           = size;
        
        /* Make this byte stream a child of its parent. */
        parent.addChildSource(this);
        
        /* Re-use the existing open input of the parent. */
        this.input  = parent.getInput();
        
        /* Set the position to the start of the byte stream. */
        this.input.setPosition(offset);
    }
    
    /**
     * Get {@link org.jhove2.core.io.Input} for the source unit. Concrete
     * classes extending this abstract class must provide an implementation of
     * this method if they are are based on parsable input. Classes without
     * parsable input (e.g. {@link org.jhove2.core.source.ClumpSource} or
     * {@link org.jhove2.core.source.DirectorySource} can let this inherited
     * method return null.
     * 
     * @param bufferSize
     *            Input maximum buffer size, in bytes
     * @param bufferType
     *            Input buffer type
     * @param order
     *            Byte order
     * @return null
     * @throws FileNotFoundException
     *             File not found
     * @throws IOException
     *             I/O exception getting input
     */
    public Input getInput(int bufferSize, Type bufferType, ByteOrder order)
        throws FileNotFoundException, IOException
    {
        return this.input;
    }

    /** Get ending offset of the byte stream, relative to its parent.
     * @return Ending offset
     */
    @ReportableProperty(order=1, value="Ending offset of the byte stream, relative to its parent.")
    public long getEndingOffset() {
        return this.endingOffset;
    }
    
    /** Get starting offset of the byte stream, relative to its parent.
     * @return Starting offset
     */
    @ReportableProperty(order=2, value="Starting offset of the byte stream, relative to its parent.")
    public long getStartingOffset() {
        return this.startingOffset;
    }
    
    /** Get byte stream size, in bytes.
     * @return Byte stream size
     */
    @ReportableProperty(order=3, value="Byte stream size, in bytes.")
    public long getSize() {
        return this.size;
    }
}
