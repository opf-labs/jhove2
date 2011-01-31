/**
 * JHOVE2 - Next-generation architecture for format-aware characterization
 *
 * Copyright (c) 2009 by The Regents of the University of California,
 * Ithaka Harbors, Inc., and The Board of Trustees of the Leland Stanford
 * Junior University.
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
package org.jhove2.core.io;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.Buffer;
import java.nio.ByteOrder;

import javax.annotation.Resource;

import org.jhove2.app.util.FeatureConfigurationUtil;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.io.Input.Type;
import org.jhove2.core.source.Source;
import org.jhove2.core.source.SourceFactory;
import org.jhove2.persist.inmemory.InMemorySourceFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author mstrong
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:**/abstractdisplayer-config.xml",
		"classpath*:**/filepaths-config.xml"})
public class MappedInputTest {

    int bufferSize;
    static MappedInput abstractInput = null;
	private String utf8DirBasePath;
	private String testFile01;
	private File testFile;
    PrintWriter out;
    ByteOrder nativeOrder = ByteOrder.nativeOrder(); 

    @Before
    public void setUp() throws Exception {
        bufferSize = 100;
		String utf8DirPath = null;
		try {
			utf8DirPath = 
				FeatureConfigurationUtil.getFilePathFromClasspath(utf8DirBasePath, "utf8 dir");
		} catch (JHOVE2Exception e1) {
			fail("Could not create base directory");
		}
		String filePath = utf8DirPath.concat(testFile01);
		testFile = new File(filePath);
        out = new PrintWriter(System.out, true);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetInput() {

        try {
        	SourceFactory factory = new InMemorySourceFactory();
            Source source = factory.getSource(testFile);
            /* Buffers are always created big-endian, not native-endian, so this test
             * isn't operative.
             */
            /*
            abstractInput = (MappedInput) source.getInput(bufferSize, Type.Mapped);
            out.printf("Native ByteOrder is %s \n", nativeOrder);
            out.printf("AbstractInput buffer order is %s\n",  abstractInput.getBuffer().order());
            String assertString = "MemoryMapped AbstractInput byte order "+ abstractInput.getBuffer().order()+ 
            " matches with native ByteOrder" + nativeOrder;
            assertTrue(assertString, abstractInput.getBuffer().order() != nativeOrder);
            abstractInput.close();
            */
            abstractInput = (MappedInput) InputFactory.getInput(testFile,
                    source.isTemp(), source.getDeleteTempFiles(),
                    bufferSize, Type.Mapped);
            assertTrue("AbstractInput Scope is MemoryMapped", abstractInput.getClass()
                    .getName().equalsIgnoreCase(MappedInput.class.getName()));
            out.printf("AbstractInput buffer order is %s\n",  abstractInput.getBuffer().order());
            
            out.printf("Setting AbstractInput buffer order to  %s\n",  ByteOrder.LITTLE_ENDIAN);
            abstractInput.setByteOrder(ByteOrder.LITTLE_ENDIAN);
            assertTrue("AbstractInput is MemoryMapped with LITTLE_ENDIAN",
                    abstractInput.getBuffer().order() != ByteOrder.BIG_ENDIAN);
            out.printf("AbstractInput buffer order is %s\n",  abstractInput.getBuffer().order());
        } catch (FileNotFoundException e) {
            fail(e.getMessage());
        } catch (IOException e) {
            fail(e.getMessage());
            e.printStackTrace();
        } catch (JHOVE2Exception e) {
            fail(e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void testGetBuffer() {

        Buffer buffer = abstractInput.getBuffer();
        assertTrue("Buffer returned is null", buffer != null);
    }

    /**
     * Test method for {@link org.jhove2.core.io.MappedInput#getMaxBufferSize()}.
     */
    @Test
    public void testGetMaxBufferSize() {
    	long fileLength = testFile.length();
    	assertTrue("Mapped Buffersize does not match filesize", 
    			(abstractInput.getBufferSize() == fileLength));
    }

    /**
     * Test method for {@link org.jhove2.core.io.AbstractInput#getByteArray()}.
     */
    @Test
    public void testGetByteArray() {
       byte [] byteArray = null;
       byteArray = abstractInput.getByteArray();
       String byteString = new String(byteArray);
       assertTrue("ByteArray is null", byteArray != null);

    }


    /**
     * Test method for {@link org.jhove2.core.io.AbstractInput#readSignedByte()}.
     */
    @Test
    public void testReadByte() {
		short testValue = 0;
		try {
			abstractInput.setPosition(0);
			
			// read unsigned byte
			testValue = abstractInput.readUnsignedByte();
			// check if high order bit is set
			assertTrue("High Order bit not set",
					(testValue > 128 && testValue < 255));
			out.println(testValue);
			out.printf("0x%X\n", testValue);

			// read signed byte
			abstractInput.setPosition(0);
			byte byteTestValue = abstractInput.readSignedByte();
			// check if high order bit is set
			assertTrue("High Order bit set",
					(byteTestValue > -128 && byteTestValue < 127));
			out.println(byteTestValue);
			out.printf("0x%X\n", byteTestValue);
		} catch (IOException e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
	}

	/*
	 * Test that getUnsignedShort() returns 2 bytes
	 */
	@Test
	public void testReadShort() {
		int testValue = 0;
		try {
			abstractInput.setPosition(0);
			abstractInput.setByteOrder(ByteOrder.BIG_ENDIAN);
			testValue = abstractInput.readUnsignedShort();
			String testStr = Integer.toHexString(testValue);
			assertTrue("High Order Bit not set ", testValue > 32768
					&& testValue < 65535);
			assertTrue("Hex value does not match expected 'efbb'", testStr
					.equalsIgnoreCase("EFBB"));
			out.printf("%d\n", testValue);
			out.printf("0x%X\n", testValue);

			abstractInput.setPosition(0);
			short shortTestValue = abstractInput.readSignedShort();
			assertTrue("High Order Bit not set ", shortTestValue > -32768
					&& shortTestValue < 32768);
			assertTrue("Hex value does not match expected 'efbb'", testStr
					.equalsIgnoreCase("EFBB"));
			out.printf("%d\n", shortTestValue);
			testStr = String.format("%X", shortTestValue);
			assertTrue("Hex value does not match expected 'efbb'", testStr
					.equalsIgnoreCase("EFBB"));
			out.printf("0x%X\n", shortTestValue);
		} catch (IOException e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
	}

	/*
	 * Test that getUnsignedInt() returns 4 bytes
	 */
	@Test
	public void testReadInt() {
		try {
			// test ReadSignedInt()
			abstractInput.setPosition(0);
			int intTestValue = abstractInput.readSignedInt();
			String testStr = Integer.toHexString(intTestValue);
			assertTrue("Value returned is not signed", (intTestValue < 0));
			assertTrue("Hex value does not match expected 'efbbbf46'", testStr
					.equalsIgnoreCase("efbbbf46"));
			out.println(intTestValue);
			out.printf("0x%X\n", intTestValue);

			// test ReadUnsignedInt()
			abstractInput.setPosition(0);
			long longTestValue = abstractInput.readUnsignedInt();
			testStr = Long.toHexString(longTestValue);
			assertTrue("Value returned is signed", (longTestValue > 0));
			assertTrue("Hex value does not match expected 'efbbbf46'", testStr
					.equalsIgnoreCase("efbbbf46"));
			out.println(longTestValue);
			out.printf("0x%X\n", longTestValue);

		} catch (IOException e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
	}


    /**
     * Test method for {@link org.jhove2.core.io.AbstractInput#setPosition(long)}.
     */
	@Test
	public void testSetPosition() {
		try {
			/*
			 * Test the position is set correctly
			 */
			long newPosition = 100;
			abstractInput.setPosition(newPosition);
			long position = abstractInput.getPosition();
			assertTrue("Set Position not set to correct position",
					position == newPosition);

			/*
			 * Test that the value expected to be at new position is true
			 */
			short b = abstractInput.readUnsignedByte();
			char byteChar = (char) (b);
			assertTrue("Position of inputable is not where its expected to be",
					byteChar == 'f');
		} catch (IOException e) {
			fail(e.getMessage());
			e.printStackTrace();
		}

	}

	/*
	 * test obtaining values that partially reside outside the current buffer
	 * are retrieved properly
	 * 
	 * Buffer ends at offset 100, retrieve a 4-byte int at offset 98 to force
	 * going outside of buffer boundary
	 * 
	 * bytes at position 98: 0A6F663A BIG_ENDIAN expected value = 0A6F663A
	 * LITTLE_ENDIAN expected value = 3A666F0A
	 */
	@Test
	public void testBufferBoundaries() {
		try {
			// test going beyond boundary explicitly with BIG_ENDIAN byte
			// ordering
			abstractInput.setPosition(98);
			long testValue = abstractInput.readUnsignedInt();
			out.println(testValue);
			out.printf("Byte Order = %s: 0x%X\n", abstractInput.getBuffer()
					.order(), testValue);
			String testStr = Long.toHexString(testValue);
			assertTrue("Hex value does not match expected '0A 6F 66 3A'",
					testStr.equalsIgnoreCase("A6F663A"));

			// test going beyond boundary explicitly with LITTLE_ENDIAN byte
			// ordering
			ByteOrder bo = abstractInput.getBuffer().order();
			abstractInput.setByteOrder(ByteOrder.LITTLE_ENDIAN);
			abstractInput.setPosition(98);
			testValue = abstractInput.readUnsignedInt();
			out.println(testValue);
			out.printf("Byte Order = %s: 0x%X\n", abstractInput.getBuffer()
					.order(), testValue);
			testStr = Long.toHexString(testValue);
			assertTrue("Hex value does not match expected '3A666F0A'", testStr
					.equalsIgnoreCase("3A666F0A"));

			/*
			 * test going beyond boundary without setting position explicitly
			 * beyond boundary (setPosition() takes care of getting the next
			 * buffer properly)
			 */
			abstractInput.setPosition(0);
			abstractInput.setByteOrder(ByteOrder.BIG_ENDIAN);
			long position = 0;
			while ((position = abstractInput.getPosition()) < 96)
				testValue = abstractInput.readUnsignedInt();
			int shortValue = abstractInput.readUnsignedShort();
			position = abstractInput.getPosition();
			assertTrue("Position not at value 98 as expected", position == 98);
			testValue = abstractInput.readUnsignedInt();
			out.println(testValue);
			out.printf("Byte Order = %s: 0x%X\n", abstractInput.getBuffer()
					.order(), testValue);
			testStr = Long.toHexString(testValue);
			assertTrue("Hex value does not match expected '3A666F0A'", testStr
					.equalsIgnoreCase("A6F663A"));

			/*
			 * same test as above but for LITTLE_ENDIAN
			 */
			abstractInput.setPosition(0);
			abstractInput.setByteOrder(ByteOrder.LITTLE_ENDIAN);
			position = 0;
			while ((position = abstractInput.getPosition()) < 96)
				testValue = abstractInput.readUnsignedInt();
			shortValue = abstractInput.readSignedShort();
			position = abstractInput.getPosition();
			assertTrue("Position not at value 98 as expected", position == 98);
			testValue = abstractInput.readUnsignedInt();
			out.println(testValue);
			out.printf("Byte Order = %s: 0x%X\n", abstractInput.getBuffer()
					.order(), testValue);
			testStr = Long.toHexString(testValue);
			assertTrue("Hex value does not match expected '3A666F0A'", testStr
					.equalsIgnoreCase("3A666F0A"));

		} catch (IOException e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
	}

	public String getTestFile01() {
		return testFile01;
	}
	@Resource
	public void setTestFile01(String testFile01) {
		this.testFile01 = testFile01;
	}

	public String getUtf8DirBaseBath() {
		return utf8DirBasePath;
	}
	@Resource
	public void setUtf8DirBasePath(String testDir) {
		this.utf8DirBasePath = testDir;
	}


}
