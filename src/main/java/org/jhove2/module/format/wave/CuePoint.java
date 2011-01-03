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

package org.jhove2.module.format.wave;

import java.io.EOFException;
import java.io.IOException;
import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.io.Input;
import org.jhove2.core.reportable.AbstractReportable;
import org.jhove2.core.source.Source;
import org.jhove2.module.format.Parser;

import com.sleepycat.persist.model.Persistent;

/** WAVE cue points chunk cue point.
 * 
 * @author slabrams
 */
@Persistent
public class CuePoint
    extends AbstractReportable
    implements Parser
{
    /** Start of the block containing the position. */
    protected long blockStart;
    
    /** Chunk containing the cue point. */
    protected String chunkName;
    
    /** Start of the data chunk containing the cue point. */
    protected long chunkStart;
    
    /** Cue point name. */
    protected String name;
    
    /** Cue point sample position. */
    protected long position;
    
    /** Cue point sample offset, relative to the block. */
    protected long sampleOffset;
    
    /** Instantiate a new <code>CuePoint</code>. */
    public CuePoint() {
        super();
    }
    
    /** 
     * Parse a WAVE cue point.
     * 
     * @param jhove2
     *            JHOVE2 framework
     * @param source
     *            WAVE source unit
     * @param input  WAVE source input
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
        
        /* Cue point name. */
        StringBuffer sb = new StringBuffer(4);
        for (int i=0; i<4; i++) {
            short b = input.readUnsignedByte();
            sb.append((char) b);
        }
        this.name = sb.toString();
        consumed += 4;
        
        /* Cue point position. */
        this.position = input.readUnsignedInt();
        consumed += 4;
        
        /* Chunk containing the cue point. */
        sb = new StringBuffer(4);
        for (int i=0; i<4; i++) {
            short b = input.readUnsignedByte();
            sb.append((char) b);
        }
        this.chunkName = sb.toString();
        consumed += 4;
        
        /* Start of chunk containing the cue point. */
        this.chunkStart = input.readUnsignedInt();
        consumed += 4;
        
        /* Start of block containing the cue point. */
        this.blockStart = input.readUnsignedInt();
        consumed += 4;
        
        /* Offset of cue point sample, relative to the block. */
        this.sampleOffset = input.readUnsignedInt();
        consumed += 4;
        
        return consumed;
    }
    
    /** Get start of the data block containing the cue point.
     * @return Start of the data block  
     */
    @ReportableProperty(order=5, value="Start of the data block containing the cue point.")
    public long getBlockStart() {
        return this.blockStart;
    }
    
    /** Get chunk containing the cue point.
     * @return Chunk containing the cue point
     */
    @ReportableProperty(order=3, value="Chunk containing the cue point.")
    public String getChunkName() {
        return this.chunkName;
    }
    
    /** Get start of the data chunk containing the cue point.
     * @return Start of the data chunk
     */
    @ReportableProperty(order=4, value="Start of the data chunk containing the cue point.")
    public long getChunkStart() {
        return this.chunkStart;
    }
    
    /** Get cue point name.
     * @return Cue point name
     */
    @ReportableProperty(order=1, value="Cue point name.")
    public String getName() {
        return this.name;
    }
    
    /** Get cue point sample position.
     * @return Cue point sample position
     */
    @ReportableProperty(order=2, value="Cue point sample position.")
    public long getPosition() {
        return this.position;
    }
    
    /** Get cue point sample offset, relative to the block.
     * @return Cue point sample offset
     */
    @ReportableProperty(order=6, value="Cue point sample offset, relative to the block.")
    public long getSampleOffset() {
        return this.sampleOffset;
    }
}
