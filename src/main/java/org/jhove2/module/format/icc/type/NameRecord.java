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

package org.jhove2.module.format.icc.type;

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

/** ICC multi-localized Unicode type name record, as defined in
 * ICC.1:2004-10, Table 44.
 * 
 * @author slabrams
 */
@Persistent
public class NameRecord
    extends AbstractReportable
    implements Parser
{
    /** ISO 3166 country code. */
    protected StringBuffer countryCode = new StringBuffer(2);
    
    /** ISO 639 language code. */
    protected StringBuffer languageCode = new StringBuffer(2);
    
    /** Name length in bytes, not characters. */
    protected long length;

    /** Name. */
    protected String name;
    
    /** Name offset in bytes. */
    protected long offset;
    
    /** Instantiate a new <code>NameRecord</code>. */
    public NameRecord() {
        super();
    }
    
    /** Parse an ICC tag type.
     * @param jhove2 JHOVE2 framework
     * @param source ICC source unit
     * @param input  ICC source input
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
        long consumed = 0L;
        
        /* Language code. */
        for (int i=0; i<2; i++) {
            short b = input.readUnsignedByte();
            this.languageCode.append((char) b);
        }
        consumed += 2;
        
        /* Country code. */
        for (int i=0; i<2; i++) {
            short b = input.readUnsignedByte();
            this.countryCode.append((char) b);
        }
        consumed += 2;
        
        /* Name length. */
        this.length = input.readUnsignedInt();
        consumed += 4;
        
        /* Name offset. */
        this.offset = input.readUnsignedInt();
        consumed += 4;
        
        /* Name. */
        
        
        return consumed;
    }
    
    /** Get ISO 3166 country code.
     * @return ISO 3166 country code
     */
    @ReportableProperty(order=2, value="ISO 3166 country code.",
            ref="ICC.1:2004-10, Table 44")
    public String getCountryCode() {
        return this.countryCode.toString();
    }
    
    /** Get ISO 639 language code.
     * @return ISO 639 language code
     */
    @ReportableProperty(order=1, value="ISO 639 language code.",
            ref="ICC.1:2004-10, Table 44")
    public String getLanguageCode() {
        return this.languageCode.toString();
    }
    
    /** Get name length in bytes, not characters.
     * @return Name length
     */
    @ReportableProperty(order=3, value="Name length in bytes, not characters.",
            ref="ICC.1:2004-10, Table 44")
    public long getLength() {
        return this.length;
    }
    
    /** Get name.
     * @return Name
     */
    @ReportableProperty(order=5, value="Name.",
            ref="ICC.1:2004-10, Table 44")
    public String getName() {
        return this.name.toString();
    }
    
    /** Get offset in bytes.
     * @return offset
     */
    @ReportableProperty(order=4, value="Name offset in bytes.",
            ref="ICC.1:2004-10, Table 44")
    public long getOffset() {
        return this.offset;
    }
    
    /** Set name.
     * @param name Name
     */
    public void setName(String name) {
        this.name = name;
    }
}
