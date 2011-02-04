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

package org.jhove2.module.format.icc;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteOrder;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.format.Format;
import org.jhove2.core.io.Input;
import org.jhove2.core.source.MensurableSource;
import org.jhove2.core.source.Source;
import org.jhove2.module.format.BaseFormatModule;
import org.jhove2.module.format.Validator;
import org.jhove2.persist.FormatModuleAccessor;

import com.sleepycat.persist.model.Persistent;

/** JHOVE2 ICC colour profile module.
 * 
 * @author slabrams
 */
@Persistent
public class ICCModule
    extends BaseFormatModule
    implements Validator
{
    /** ICC module version identifier. */
    public static final String VERSION = "2.0.0";

    /** ICC module release date. */
    public static final String RELEASE = "2010-09-10";

    /** ICC module rights statement. */
    public static final String RIGHTS =
        "Copyright 2010 by The Regents of the University of California" +
        "Available under the terms of the BSD license.";
    
    /** Module validation coverage. */
    public static final Coverage COVERAGE = Coverage.Inclusive;

    /** Profile header. */
    protected ICCHeader header;
    
    /** ICC validation status. */
    protected Validity isValid;

    /** Profile tag table. */
    protected ICCTagTable tagTable;
 
    /** Instantiate a new <code>ICCModule</code>
     * 
     * @param format ICC format
     * @param formatModuleAccessor persistence manager
     */
    public ICCModule(Format format, FormatModuleAccessor formatModuleAccessor) {
        super(VERSION, RELEASE, RIGHTS, format, formatModuleAccessor);
        
        this.isValid = Validity.Undetermined;
    }
    
    @SuppressWarnings("unused")
	private ICCModule(){
    	this(null, null);
    }
    

    /** 
     * Parse an ICC source unit.
     * 
     * @param jhove2
     *            JHOVE2 framework
     * @param source
     *            ICC source unit
     * @param input
     *            ICC source input
     * @return Number of bytes consumed
     * @throws EOFException
     *             If End-of-File is reached reading the source unit
     * @throws IOException
     *             If an I/O exception is raised reading the source unit
     * @throws JHOVE2Exception
     * @see org.jhove2.module.format.FormatModule#parse(org.jhove2.core.JHOVE2,
     *      org.jhove2.core.source.Source, org.jhove2.core.io.Input)
     */
    @Override
    public long parse(JHOVE2 jhove2, Source source, Input input)
        throws EOFException, IOException, JHOVE2Exception
    {
        long consumed = 0L;
        this.isValid = Validity.True;
        input.setByteOrder(ByteOrder.BIG_ENDIAN);
        input.setPosition(((MensurableSource) source).getStartingOffset());
        long start = 0L;
        if ((start = input.getPosition()) == 0) {
            input.setPosition(0L);
        }
        this.header = new ICCHeader();
        this.header.setOffset(start);
        consumed += this.header.parse(jhove2, source, input);
        Validity validity = header.isValid();
        if (validity != Validity.True) {
            this.isValid = validity;
        }
                
        this.tagTable = new ICCTagTable();
        consumed += this.tagTable.parse(jhove2, source, input, header);
        validity = this.tagTable.isValid();
        if (validity != Validity.True) {
            this.isValid = validity;
        }

        return consumed;
    }

    /** Validate the ICC color profile.
     * @param jhove2 JHOVE2 framework object
     * @param source ICC color profile source unit
     * @see org.jhove2.module.format.Validator#validate(org.jhove2.core.JHOVE2, org.jhove2.core.source.Source,
     * org.jhove2.core.io.Input)
     */
    @Override
    public Validity validate(JHOVE2 jhove2, Source source, Input input)
            throws JHOVE2Exception
    {
        return this.isValid();
    }
    
    /** Get validation coverage.
     * @return Validation coverage
     * @see org.jhove2.module.format.Validator#getCoverage()
     */
    @Override
    public Coverage getCoverage() {
        return COVERAGE;
    }
    
    /** Get profile header.
     * @return Profile header
     */
    @ReportableProperty(order=1, value="Profile header",
            ref="ICC.1:2004-10, \u00a7 7.2")
    public ICCHeader getHeader()
    {
        return this.header;
    }
    
    /** Get profile tag table.
     * @return Profile tag table
     */
    @ReportableProperty(order=2, value="Profile tag table",
            ref="ICC.1:2004-10, \u00a7 7.3")
    public ICCTagTable getTagTable()
    {
        return this.tagTable;
    }

    /** Get validity.
     * @return Validity
     * @see org.jhove2.module.format.Validator#isValid()
     */
    @Override
    public Validity isValid()
    {
        return this.isValid;
    }
}
