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

package org.jhove2.module.format.icc;

import static org.junit.Assert.assertTrue;
import java.util.List;

import javax.annotation.Resource;

import org.jhove2.module.format.icc.type.MultiLocalizedUnicodeType;
import org.jhove2.module.format.icc.type.NameRecord;
import org.junit.Before;
import org.junit.Test;

/**
* ICC tag table test.
* 
* @author slabrams
*/

public class ICCTagTest
   extends ICCModuleTestBase
{
   private String iccTestFile;
   private ICCTagTable table;
   private ICCTag tag;
   private MultiLocalizedUnicodeType type;
   private NameRecord record;

   @Before
   public void setUp()
       throws Exception
   {
       this.parse(iccTestFile);
       this.table = this.testIccModule.getTagTable();
       List<ICCTag> tags = this.table.getTags();
       this.tag = tags.get(0);
       this.type = this.tag.getMultiLocalizedUnicodeType();
       List<NameRecord> records = this.type.getNameRecords();
       this.record = records.get(0);
   }

   /**
    * Test methods for ICC header 
    */
   @Test
   public void testCount() {
       long ln = this.table.getCount();
       assertTrue("count = " + ln, ln == 9L);
   }
   
   @Test
   public void testTagSignature() {
       String s = this.tag.getSignature_raw();
       assertTrue("signature = " + s, s.equals("desc"));
       
       s = this.tag.getSignature_descriptive();
       assertTrue("signature = " + s, s.equals("Profile description"));
   }
   
   @Test
   public void testTagOffset() {
       long ln = this.tag.getOffset();
       assertTrue("offset = " + ln, ln == 240L);
   }
   
   @Test
   public void testTagSize() {
       long ln = this.tag.getSize();
       assertTrue("size = " + ln, ln == 118L);
   }
   
   @Test
   public void testTagVendor() {
       String s = this.tag.getVendor();
       assertTrue("vendor = " + s, s.equals(s));
   }
   
   @Test
   public void testNameNumber() {
       long ln = this.type.getNumberOfNames();
       assertTrue("number = " + ln, ln == 1L);
   }
   
   @Test
   public void testNameSize() {
       long ln = this.type.getNameRecordSize();
       assertTrue("record = " + ln, ln == 12L);
   }
   
   @Test
   public void testRecordLanguage() {
       String s = this.record.getLanguageCode();
       assertTrue("language = " + s, s.equals("en"));
   }
   
   @Test
   public void testCountryCode() {
       String s = this.record.getCountryCode();
       assertTrue("country = " + s, s.equals("US"));
   }
   
   @Test
   public void testRecordLength() {
       long ln = this.record.getLength();
       assertTrue("length = " + ln, ln == 90L);
   }
   
   @Test
   public void testRecordOffset() {
       long ln = this.record.getOffset();
       assertTrue("offset = " + ln, ln == 28L);
   }
   
   @Test
   public void testRecordName() {
       String s = this.record.getName();
       assertTrue("name = " + s, s.equals("sRGB v4 ICC preference perceptual intent beta"));
   }
    public String getIccTestFile() {
       return this.iccTestFile;
   }

   @Resource
   public void setIccTestFile(String iccTestFile) {
       this.iccTestFile = iccTestFile;
   }
}
