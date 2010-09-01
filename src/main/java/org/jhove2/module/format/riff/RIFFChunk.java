/**
 * JHOVE2 - Next-generation architecture for format-aware characterization
 *
 * Copyright (c) 2009 by The Regents of the University of California.
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

package org.jhove2.module.format.riff;

import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.io.Input;
import org.jhove2.core.reportable.AbstractReportable;

/** Resource Interchange File Format (RIFF) chunk. Note that this class
 * represents a generic RIFF chunk, not the specific "RIFF" form chunk.
 * 
 * @author slabrams
 */
public abstract class RIFFChunk
        extends AbstractReportable
{
    /** Child chunks. */
    protected List<RIFFChunk> chunks;
    
    /** Chunk identifier. */
    protected StringBuffer identifier;
    
    /** Chunk data size. */
    protected long size;
    
    /** Instantiate a new <code>RIFFChunk</code>.
     * 
     */
    public RIFFChunk() {
        this.chunks     = new ArrayList<RIFFChunk>();
        this.identifier = new StringBuffer(4);
    }
    
    /** 
     * Parse a RIFF chunk.
     * 
     * @param jhove2
     *            JHOVE2 framework
     * @param input
     *            RIFF input
     * @return Number of bytes consumed
     * @throws EOFException
     *             If End-of-File is reached reading the source unit
     * @throws IOException
     *             If an I/O exception is raised reading the source unit
     * @throws JHOVE2Exception
     * @see org.jhove2.module.format.FormatModule#parse(org.jhove2.core.JHOVE2,
     *      org.jhove2.core.source.Source)
     */
    public long parse(JHOVE2 jhove2, Input input)
        throws EOFException, IOException, JHOVE2Exception
    {
        long consumed = 0L;
        
        /* Identifier. */
        for (int i=0; i<4; i++) {
            short b = input.readUnsignedByte();
            this.identifier.append((char) b);
        }
        consumed += 4;
        
        /* Size. */
        this.size = input.readUnsignedInt();
        
        return consumed;
    }

    /** Get child chunks.
     * @return Child chunks
     */
    @ReportableProperty(order=3, value="Child chunks.")
    public List<RIFFChunk> getChildChunks() {
        return this.chunks;
    }
    
    /** Get the chunk identifier.
     * @return Chunk identifier
     */
    @ReportableProperty(order=1, value="Chunk identifier.")
    public String getIdentifier() {
        return this.identifier.toString();
    }
    
    /** Get the chunk data size, in bytes.
     * @return Chunk data size
     */
    @ReportableProperty(order=2, value="Chunk data size, in bytes.")
    public long getSize() {
        return this.size;
    }
}
