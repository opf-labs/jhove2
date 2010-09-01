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
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.Invocation;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.format.Format;
import org.jhove2.core.io.Input;
import org.jhove2.core.source.Source;
import org.jhove2.module.format.BaseFormatModule;
import org.jhove2.module.format.Validator;

/** WAVE (waveform audio file format) module.
 * 
 * @author slabrams
 */
public class WAVEModule
        extends BaseFormatModule
        implements Validator
{
    /** WAVE module version identifier. */
    public static final String VERSION = "2.0.0";

    /** WAVE module release date. */
    public static final String RELEASE = "2010-09-10";

    /** WAVE module rights statement. */
    public static final String RIGHTS =
        "Copyright 2010 by The Regents of the University of California" +
        "Available under the terms of the BSD license.";
    
    /** Module validation coverage. */
    public static final Coverage COVERAGE = Coverage.Inclusive;
  
    /** WAVE chunks. */
    protected List<WAVEChunk> chunks;
    
    /** WAVE validation status. */
    protected Validity isValid;

    /** Instantiate a new <code>WAVEModule</code>.
     * @param format WAVE format
     */
    public WAVEModule(Format format)
    {
        super(VERSION, RELEASE, RIGHTS, format);
        
        this.chunks  = new ArrayList<WAVEChunk>();
        this.isValid = Validity.Undetermined;
    }
    
    /** 
     * Parse a WAVE source unit.
     * 
     * @param jhove2
     *            JHOVE2 framework
     * @param source
     *            ICC source unit
     * @return Number of bytes consumed
     * @throws EOFException
     *             If End-of-File is reached reading the source unit
     * @throws IOException
     *             If an I/O exception is raised reading the source unit
     * @throws JHOVE2Exception
     * @see org.jhove2.module.format.FormatModule#parse(org.jhove2.core.JHOVE2,
     *      org.jhove2.core.source.Source)
     */
    @Override
    public long parse(JHOVE2 jhove2, Source source)
        throws EOFException, IOException, JHOVE2Exception
    {
        long consumed = 0L;
        this.isValid = Validity.True;
        Input input = null;
        try {
            Invocation config = jhove2.getInvocation();
            input = source.getInput(config.getBufferSize(), 
                                    config.getBufferType());
            input.setByteOrder(ByteOrder.LITTLE_ENDIAN);
            input.setPosition(0L);
            
            WAVEChunk chunk = new WAVEChunk();
            consumed += chunk.parse(jhove2, input);
            this.chunks.add(chunk);
          }
        finally {
            if (input != null) {
                input.close();
            }
        }

        return consumed;
    }

    /** Validate the WAVE source unit.
     * @param jhove2 JHOVE2 framework
     * @param source WAVE source unit
     * @see org.jhove2.module.format.Validator#validate(org.jhove2.core.JHOVE2, org.jhove2.core.source.Source)
     */
    @Override
    public Validity validate(JHOVE2 jhove2, Source source)
            throws JHOVE2Exception
    {
        return this.isValid();
    }

    /** Get chunks.
     * @return Chunks
     */
    @ReportableProperty(order=1, value="Chunks.")
    public List<WAVEChunk> getChunks() {
        return this.chunks;
    }
    
    /** Get module coverage.
     * @return Module coverage
     * @see org.jhove2.module.format.Validator#getCoverage()
     */
    @Override
    public Coverage getCoverage()
    {
        return COVERAGE;
    }

    /** Get validation status.
     * @return Validation status
     * @see org.jhove2.module.format.Validator#isValid()
     */
    @Override
    public Validity isValid()
    {
        return this.isValid;
    }
}
