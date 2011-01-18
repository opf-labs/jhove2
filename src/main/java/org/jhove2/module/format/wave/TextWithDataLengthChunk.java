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
import org.jhove2.core.source.Source;
import org.jhove2.module.format.riff.GenericChunk;

import com.sleepycat.persist.model.Persistent;

/** WAVE format text with data length chunk.
 * 
 * @author slabrams
 */
@Persistent
public class TextWithDataLengthChunk
    extends GenericChunk
{
    /** Code page. */
    protected int codePage;
    
    /** Country code in raw form. */
    protected int countryCode;
    
    /** Dialect code in raw form. */
    protected int dialectCode;
    
    /** Language code in raw form. */
    protected int languageCode;
    
    /** Name. */
    protected String name;
    
    /** Number of samples. */
    protected long numSamples;
    
    /** Purpose. */
    protected String purpose;
    
    /** Instantiate a new <code>TextWithDataLengthChunk</code>. */
    public TextWithDataLengthChunk() {
        super();
    }
    
    /** 
     * Parse a WAVE text with data length chunk.
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
     */
    @Override
    public long parse(JHOVE2 jhove2, Source source, Input input)
        throws EOFException, IOException, JHOVE2Exception
    {
        long consumed = super.parse(jhove2, source, input);
        
        /* Name. */
        StringBuffer sb = new StringBuffer(4);
        for (int i=0; i<4; i++) {
            short b = input.readUnsignedByte();
            sb.append((char) b);
        }
        this.name = sb.toString();
        consumed += 4;
        
        /* Number of samples. */
        this.numSamples = input.readUnsignedInt();
        consumed += 4;
        
        /* Purpose. */
        sb = new StringBuffer(4);
        for (long i=0; i<4; i++) {
            short b = input.readUnsignedByte();
            sb.append((char) b);
        }
        this.purpose = sb.toString();
        consumed += 4;
        
        /* Country code. */
        this.countryCode = input.readUnsignedShort();
        consumed += 2;
        
        /* Language code. */
        this.languageCode = input.readUnsignedShort();
        consumed += 2;
        
        /* Dialect code. */
        this.dialectCode = input.readUnsignedShort();
        consumed += 2;
        
        /* Code page. */
        this.codePage = input.readUnsignedShort();
        consumed += 2;
        
        return consumed;
    }
    
    /** Get code page.
     * @return Code page
     */
    @ReportableProperty(order=10, value="Code page.")
    public int getCodePage() {
        return this.codePage;
    }
    
    /** Get country code in raw form.
     * @return Country code
     */
    @ReportableProperty(order=4, value="Get country code in raw form.")
    public int getCountryCode_raw() {
        return this.countryCode;
    }
    
    /** Get dialect code in raw form.
     * @return Dialect code
     */
    @ReportableProperty(order=8, value="Get dialect code in raw form.")
    public int getDialectCode_raw() {
        return this.dialectCode;
    }
    
    /** Get language code in raw form.
     * @return Language code
     */
    @ReportableProperty(order=6, value="Get language code in raw form.")
    public int getLanguageCode_raw() {
        return this.languageCode;
    }
    
    /** Get name.
     * @return name
     */
    @ReportableProperty(order=1, value="Name.")
    public String getName() {
        return this.name;
    }
    
    /** Get number of samples.
     * @return Number of samples
     */
    @ReportableProperty(order=2, value="Number of samples.")
    public long getNumberOfSamples() {
        return this.numSamples;
    }
    
    /** Get purpose.
     * @return purpose
     */
    @ReportableProperty(order=3, value="Purpose.")
    public String getPurpose() {
        return this.purpose;
    }
}
