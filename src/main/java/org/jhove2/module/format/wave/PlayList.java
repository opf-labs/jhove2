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

/** WAVE play list.
 * 
 * @author slabrams
 */
@Persistent
public class PlayList
    extends AbstractReportable
    implements Parser
{
    /** Play list length, in samples. */
    protected long length;
    
    /** Play list name. */
    protected String name;

    /** Number of loops. */
    protected long numLoops;
    
    /** Instantiate a new <code>PlayList</code>. */
    public PlayList() {
        super();
    }    
    
    /** 
     * Parse a WAVE play list.
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
        
        /* Play list name. */
        StringBuffer sb = new StringBuffer(4);
        for (int i=0; i<4; i++) {
            short b = input.readUnsignedByte();
            sb.append((char) b);
        }
        this.name = sb.toString();
        consumed += 4;
        
        /* Play list length. */
        this.length = input.readUnsignedInt();
        consumed += 4;
        
        /* Number of loops. */
        this.numLoops = input.readUnsignedInt();
        consumed += 4;
        
        return consumed;
    }

    /** Get play list length, in samples.
     * @return Play list length
     */
    @ReportableProperty(order=2, value="Play list length, in samples.")
    public long getLength() {
        return this.length;
    }
    
    /** Get play list name.
     * @return Play list name
     */
    @ReportableProperty(order=1, value="Play list name.")
    public String getName() {
        return this.name;
    }
    
    /** Get number of loops.
     * @return Number of loops
     */
    @ReportableProperty(order=3, value="Number of loops.")
    public long getNumLoops() {
        return this.numLoops;
    }
}
