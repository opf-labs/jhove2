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
import org.jhove2.annotation.ReportableProperty;
import org.jhove2.annotation.ReportableProperty.PropertyType;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.Message;
import org.jhove2.core.Message.Context;
import org.jhove2.core.Message.Severity;
import org.jhove2.core.io.Input;
import org.jhove2.core.source.Source;
import org.jhove2.module.format.Validator.Validity;
import org.jhove2.module.format.riff.field.FormType;

/** RIFF chunk.
 * 
 * @author slabrams
 */
public class RIFFChunk
    extends GenericChunk
{   
    /** RIFF chunk form type in raw form. */
    protected String formType;
    
    /** RIFF chunk form type in descriptive form. */
    protected String formType_d;
    
    /** Invalid RIFF chunk form type message. */
    protected Message invalidFormTypeMessage;
    
    /** Instantiate a new <code>RIFFChunk</code>. */
    public RIFFChunk() {
        super();
    }
    
    /** 
     * Parse a RIFF chunk.
     * 
     * @param jhove2
     *            JHOVE2 framework
     * @param source
     *            RIFF source unit
     * @param input
     *            RIFF source input
     * @return Number of bytes consumed
     * @throws EOFException
     *             If End-of-File is reached reading the source unit
     * @throws IOException
     *             If an I/O exception is raised reading the source unit
     * @throws JHOVE2Exception
     */
    @Override
    public long parse(JHOVE2 jhove2, Source source, Input input)
        throws EOFException, IOException, JHOVE2Exception
    {
        /* Chunk identifier and size. */
        long consumed = super.parse(jhove2, source, input);
        long offset   = source.getStartingOffset();
        int numErrors = 0;
        
        /* Chunk form type. */
        StringBuffer sb = new StringBuffer(4);
        for (int i=0; i<4; i++) {
            short b = input.readUnsignedByte();
            sb.append((char) b);
        }
        this.formType = sb.toString();
        FormType type = FormType.getFormType(this.formType, jhove2);
        if (type != null) {
            this.formType_d = type.getDescription();
        }
        else {
            numErrors++;
            this.isValid = Validity.False;
            Object [] args = new Object [] {input.getPosition()-4L-offset, this.formType};
            this.invalidFormTypeMessage = new Message(Severity.ERROR,
                    Context.OBJECT,
                    "org.jhove2.module.format.riff.Chunk.invalidFormType",
                    args, jhove2.getConfigInfo());
        }
        consumed += 4;
        
        /* Child chunks. */
        long pos = input.getPosition();
        long max = this.getNextChunkOffset();
        while (pos < max) {
            sb = new StringBuffer(4);
            for (int i=0; i<4; i++) {
                short b = input.readUnsignedByte();
                sb.append((char) b);
            }
            consumed += 4;
            Chunk chunk = ChunkFactory.getChunk(sb.toString(), jhove2);
            consumed += chunk.parse(jhove2, source, input);
            this.chunks.add(chunk);
            
            pos = chunk.getNextChunkOffset();
            input.setPosition(pos);
        }

        return consumed;
    }
      
    /** Get RIFF chunk form type in descriptive form.
     * @return RIFF chunk form type
     */
    @ReportableProperty(order=2, value="RIFF chunk form type in descriptive form.",
            type=PropertyType.Descriptive)
    public String getFormType_descriptive() {
        return this.formType_d;
    }
    
    /** Get RIFF chunk form type in raw form.
     * @return RIFF chunk form type
     */
    @ReportableProperty(order=1, value="RIFF chunk form type in raw form.",
            type=PropertyType.Raw)
    public String getFormType_raw() {
        return this.formType;
    }
    
    /** Get invalid RIFF chunk form type message.
     * @return Invalid RIFF chunk form type message
     */
    @ReportableProperty(order=11, value="Invalid RIFF chunk form type message.")
    public Message getInvalidFormTypeMessage() {
        return this.invalidFormTypeMessage;
    }
}
