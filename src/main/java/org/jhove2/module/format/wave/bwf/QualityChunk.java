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

package org.jhove2.module.format.wave.bwf;

import java.io.EOFException;
import java.io.IOException;
import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.io.Input;
import org.jhove2.module.format.riff.GenericChunk;

/** Broadcast Wave Format (BWF) quality chunk.
 * 
 * @author slabrams
 */
public class QualityChunk
        extends GenericChunk
{
    /** Basic data. */
    protected String basicData;
    
    /** Cue sheet data. */
    protected String cueSheetData;
    
    /** End modulation data. */
    protected String endModulationData;
    
    /** Operator comments. */
    protected String operatorComments;
    
    /** Quality event data. */
    protected String qualityEventData;
    
    /** Quality parameter data. */
    protected String qualityParameterData;
   
    /** File security code of quality report. */
    protected long securityCodeReport;
    
    /** File security code of BWF wave data. */
    protected long securityCodeWAVE;
    
    /** Start modulation data. */
    protected String startModulationData;
  
    /** Instantiate a new <code>QualityChunk</code>. */
    public QualityChunk() {
        super();
    }
    
    /** 
     * Parse a WAVE chunk.
     * 
     * @param jhove2
     *            JHOVE2 framework
     * @param input
     *            WAVE input
     * @return Number of bytes consumed
     * @throws EOFException
     *             If End-of-File is reached reading the source unit
     * @throws IOException
     *             If an I/O exception is raised reading the source unit
     * @throws JHOVE2Exception
     */
    public long parse(JHOVE2 jhove2, Input input)
        throws EOFException, IOException, JHOVE2Exception
    {
        long consumed = super.parse(jhove2, input);
        
        this.securityCodeReport = input.readUnsignedInt();
        consumed += 4;
        
        this.securityCodeWAVE = input.readUnsignedInt();
        consumed += 4;
        
        return consumed;
    }
    
    /** Get basic data.
     * @return Basic data
     */
    @ReportableProperty(order=3, value="Basic data.")
    public String getBasicData() {
        return this.basicData;
    }
    
    /** Get cue sheet data.
     * @return Cue sheet data
     */
    @ReportableProperty(order=9, value="Cue sheet data.")
    public String getCueSheetData() {
        return this.cueSheetData;
    }
    
    /** Get end modulation data.
     * @return End modulation data
     */
    @ReportableProperty(order=7, value="End modulation data.")
    public String getEndModulationData() {
        return this.endModulationData;
    }
    
    /** Get the file security code of the quality report.
     * @return File security code
     */
    @ReportableProperty(order=1, value="File security code of the quality report.")
    public long getFileSecurityCodeForReport() {
        return this.securityCodeReport;
    }
    
    /** Get the file security code of BWF wave data.
     * @return File security code
     */
    @ReportableProperty(order=2, value="File security code of BWF wave data.")
    public long getFileSecurityCodeForWAVE() {
        return this.securityCodeWAVE;
    }
    
    /** Get operator comments.
     * @return Operator comments
     */
    @ReportableProperty(order=8, value="Operator comments.")
    public String getOperatorComments() {
        return this.operatorComments;
    }
    
    /** Get quality event data.
     * @return Quality event datae
     */
    @ReportableProperty(order=5, value="Quality event data.")
    public String getQualityEventData() {
        return this.qualityEventData;
    }
    
    /** Get quality parameter data.
     * @return Quality parameter data
     */
    @ReportableProperty(order=6, value="Quality parameter data.")
    public String getQualityParameterData() {
        return this.qualityParameterData;
    }
    
    /** Get start modulation data.
     * @return Start modulation data
     */
    @ReportableProperty(order=4, value="Start modulation data.")
    public String getStartModulationData() {
        return this.startModulationData;
    }
}
