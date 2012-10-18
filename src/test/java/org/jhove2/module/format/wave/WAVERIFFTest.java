/* JHOVE2 - Next-generation architecture for format-aware characterization
 * <p>
 * Copyright (c) 2011 by The Regents of the University of California, Ithaka
 * Harbors, Inc., and The Board of Trustees of the Leland Stanford Junior
 * University. All rights reserved.
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

package org.jhove2.module.format.wave;

import static org.junit.Assert.assertTrue;
import java.util.List;

import javax.annotation.Resource;

import org.jhove2.module.format.riff.Chunk;
import org.jhove2.module.format.riff.RIFFChunk;
import org.junit.Before;
import org.junit.Test;

/**
 * WAVE header test.
 * 
 * @author slabrams
 */

public class WAVERIFFTest
    extends WAVEModuleTestBase
{
    private String waveTestFile;
    private RIFFChunk riff;

    @Before
    public void setUp()
        throws Exception
    {
    	super.setUp();
        this.parse(waveTestFile);
        List<Chunk> chunks = this.testWaveModule.getChunks();
        this.riff = (RIFFChunk) chunks.get(0);
    }

    /**
     * Test methods for WAVE RIFF chunk
     */
    @Test
    public void testIdentifier() {
        String s = this.riff.getIdentifier();
        assertTrue("identifier = " + s, s.equals("RIFF"));
    }
    
    @Test
    public void testSize() {
        long ln = this.riff.getSize();
        assertTrue("size = " + ln, ln == 683110L);
    }
    
    @Test
    public void testFormType_raw() {
        String s = this.riff.getFormType_raw();
        assertTrue("type = " + s, s.equals("WAVE"));
    }
    
    @Test
    public void testFormType_descriptive() {
        String s = this.riff.getFormType_descriptive();
        assertTrue("type = " + s, s.equals("Waveform Audio Format"));
    }
 
    public String getWaveTestFile() {
        return this.waveTestFile;
    }

    @Resource
    public void setWaveTestFile(String waveTestFile) {
        this.waveTestFile = waveTestFile;
    }
}
