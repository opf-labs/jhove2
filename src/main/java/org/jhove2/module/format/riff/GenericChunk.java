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
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.io.Input;
import org.jhove2.core.reportable.AbstractReportable;
import org.jhove2.core.source.Source;
import org.jhove2.module.format.Validator.Validity;

import com.sleepycat.persist.model.Persistent;

/** Resource Interchange File Format (RIFF) chunk.
 * 
 * @author slabrams
 */
@Persistent
public class GenericChunk
    extends AbstractReportable
    implements Chunk
{
    /** Child chunks. */
    protected List<Chunk> chunks;
 
    /** Chunk pad byte status. */
    protected boolean hasPadByte;
    
    /** Chunk identifier. */
    protected String identifier;
    
    /** Chunk validation status. */
    protected Validity isValid;
    
    /** Starting offset of chunk. */
    protected long offset;

    /** Chunk data size. */
    protected long size;
    
    /** Instantiate a new <code>Chunk</code>. */
    public GenericChunk() {
        super();
        
        this.chunks     = new ArrayList<Chunk>();
        this.hasPadByte = false;
        this.isValid    = Validity.Undetermined;
    }
    
    /** 
     * Parse a RIFF chunk.
     * 
     * @param jhove2
     *            JHOVE2 framework
     * @param source
     *            RIFF source unit
     * @param
     *            RIFF source input
     * @return Number of bytes consumed
     * @throws EOFException
     *             If End-of-File is reached reading the source unit
     * @throws IOException
     *             If an I/O exception is raised reading the source unit
     * @throws JHOVE2Exception
     * @see org.jhove2.module.format.Parser#parse(org.jhove2.core.JHOVE2,
     *      org.jhove2.core.source.Source, org.jhove2.core.io.Input)
     */
    @Override
    public long parse(JHOVE2 jhove2, Source source, Input input)
        throws EOFException, IOException, JHOVE2Exception
    {
        long consumed = 0L;
        this.isValid  = Validity.True; 
        this.offset   = input.getPosition();
        
        /* Chunk identifier. */
        if (this.identifier == null) {
            StringBuffer sb = new StringBuffer(4);
            for (int i=0; i<4; i++) {
                short b = input.readUnsignedByte();
                sb.append((char) b);
            }
            this.identifier = sb.toString();
            consumed += 4;
        }
        else {
            this.offset -= 4L;
        }
        
        /* Chunk size. */
        this.size = input.readUnsignedInt();
        consumed += 4;

        /* If chunk size is odd, there is an extra padding byte at the end of
         * the chunk data.
         */
        if ((this.size & 0x00000001) != 0) {
            this.hasPadByte = true;
        }
 
        return consumed;
    }

    /** Get child chunks.
     * @return Child chunks
     * @see org.jhove2.module.format.riff.Chunk#getChildChunks()
     */
    public List<Chunk> getChildChunks() {
        return this.chunks;
    }
 
    /** Get the chunk identifier.
     * @return Chunk identifier
     * @see org.jhove2.module.format.riff.Chunk#getIdentifier()
     */
    public String getIdentifier() {
        return this.identifier;
    }
    
    /** Get next chunk starting offset, in bytes.
     * @return Next chunk starting offset
     * @see org.jhove2.module.format.riff.Chunk#getNextChunkOffset()
     */
    public long getNextChunkOffset() {
        long next = this.offset + 8L + this.size;
        if (this.hasPadByte) {
            next++;
        }
        return next;
    }
  
    /** Get starting offset of chunk.
     * @return Starting offset of chunk
     * @see org.jhove2.module.format.riff.Chunk#getOffset()
     */
    public long getOffset() {
        return this.offset;
    }
 
    /** Get the chunk data size, in bytes.
     * @return Chunk data size
     * @see org.jhove2.module.format.riff.Chunk#getSize()
     */
    public long getSize() {
        return this.size;
    }
    
    /** Get chunk pad byte status.
     * @return Chunk pad byte status
     * @see org.jhove2.module.format.riff.Chunk#hasPadByte()
     */
    public boolean hasPadByte() {
        return this.hasPadByte;
    }
    
    /** Get chunk validation status.
     * @return Chunk validation statue
     * @see org.jhove2.module.format.riff.Chunk#isValid()
     */
    public Validity isValid() {
        return this.isValid;
    }
    
    /** Set chunk identifier.
     * @param identifier Chunk identifier
     * @see org.jhove2.module.format.riff.Chunk#setIdentifier(String)
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
