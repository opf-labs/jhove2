/**
 * JHOVE2 - Next-generation architecture for format-aware characterization
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

package org.jhove2.module.format.icc;

import static org.junit.Assert.assertTrue;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

/**
 * ICC header test.
 * 
 * @author slabrams
 */

public class ICCHeaderTest
    extends ICCModuleTestBase
{
    private String iccTestFile;
    private ICCHeader header;

    @Before
    public void setUp()
        throws Exception
    {
        this.parse(iccTestFile);
        this.header = this.testIccModule.getHeader();
    }

    /**
     * Test methods for ICC header 
     */
    @Test
    public void testSize() {
        long ln = this.header.getProfileSize();
        assertTrue("size = " + ln, ln == 60960L);
    }
    
    @Test
    public void testVersion() {
        String s = this.header.getProfileVersionNumber();
        assertTrue("version = " + s, s.equals("4.2.0.0"));
    }
    
    @Test
    public void testClass_raw() {
        String s = this.header.getProfileDeviceClass_raw();
        assertTrue("class = " + s, s.equals("spac"));
    }
    
    @Test
    public void testClass_descriptive() {
        String s = this.header.getProfileDeviceClass_descriptive();
        assertTrue("class = " + s, s.equals("ColorSpace Conversion profile"));
    }
 
    @Test
    public void testColourSpace_raw() {
        String s = this.header.getColourSpace_raw();
        assertTrue("colourspace = " + s, s.equals("RGB "));
    }
    
    @Test
    public void testPCS_raw() {
        String s = this.header.getProfileConnectionSpace_raw();
        assertTrue("pcs = " + s, s.equals("Lab "));
    }
    
    @Test
    public void testLinkProfile() {
        boolean bool = this.header.isDeviceLinkProfile();
        assertTrue("link profile = " + bool, !bool);
    }
    
    @Test
    public void testDate() {
        Date d = this.header.getDateAndTime();
        TimeZone tz = TimeZone.getTimeZone("GMT:00");
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        fmt.setTimeZone( tz );
        String s = fmt.format(d);
        String dat1 = s.substring(0, 22);
        String dat2 = s.substring(22);
        s = dat1 + ":" + dat2;
        assertTrue("date = " + s, s.equals("2007-07-25T00:05:37+00:00"));
    }
    
    @Test
    public void testSignature() {
        String s = this.header.getProfileFileSignature();
        assertTrue("signature = " + s, s.equals("acsp"));
    }
    
    @Test
    public void testPCSIlluminant() {
        String s = this.header.getPCSIlluminant().toString();
        assertTrue("PCS = " + s, s.equals("0.9642, 1.0, 0.8249"));
    }

    @Test
    public void testD50Illuminant() {
        boolean bool = this.header.isD50Illuminant();
        assertTrue("d50 illuminant = " + bool, bool);
    }

    public String getIccTestFile() {
        return this.iccTestFile;
    }

    @Resource
    public void setIccTestFile(String iccTestFile) {
        this.iccTestFile = iccTestFile;
    }
}
