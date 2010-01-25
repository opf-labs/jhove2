/* JHOVE2 - Next-generation architecture for format-aware characterization
 *
 * Copyright (c) 2008 by The Regents of the University of California.
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
 *   Library nor the names of its contributors may be used to endorse or
 *   promote products derived from this software without specific prior
 *   written permission.
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

import org.jhove2.core.io.Input.Type;
import org.jhove2.core.source.Source;
import org.jhove2.core.source.SourceFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author mstrong
 * 
 */

public class DirectInputTest {

	int bufferSize;
	static Input abstractInput = null;
	File testFile;
	PrintWriter out;

	@Before
	public void setUp() throws Exception {
		bufferSize = 100;
		testFile = new File("src/test/resources/examples/utf8/xmas_menu.txt");
		out = new PrintWriter(System.out, true);
	}

	@After
	public void tearDown() throws Exception {
		// abstractInput.close();
	}

	@Test
	public void testGetInput() {

		try {
			Source source = SourceFactory.getSource(testFile);
			abstractInput = source.getInput(bufferSize, Type.Direct);// ,
																		// ByteOrder.LITTLE_ENDIAN);
			assertTrue("AbstractInput Type is Direct with LITTLE_ENDIAN",
					abstractInput.getBuffer().order() == ByteOrder.LITTLE_ENDIAN);
			abstractInput.close();
			abstractInput = InputFactory.getInput(testFile, bufferSize,
					Type.Direct);
			assertTrue("AbstractInput Type is Direct", abstractInput.getClass()
					.getName().equalsIgnoreCase(DirectInput.class.getName()));
			abstractInput.setByteOrder(ByteOrder.BIG_ENDIAN);
			assertTrue("AbstractInput Type is Direct with BIG_ENDIAN",
					abstractInput.getBuffer().order() == ByteOrder.BIG_ENDIAN);
		} catch (FileNotFoundException e) {
			fail(e.getMessage());
		} catch (IOException e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
	}

	@Test
	public void testGetBuffer() {

		Buffer buffer = abstractInput.getBuffer();
		assertTrue("Buffer returned is null", buffer != null);
	}

	@Test
	public void testGetFile() {
		File inputableFile = abstractInput.getFile();
		assertTrue("File is not same as abstractInput", testFile.getName()
				.equals(inputableFile.getName()));
	}

	@Test
	public void testGetBufferOffset() {
		try {
			// test within the bounds of the current buffer. Buffer Offset will
			// not change because you have not exceeded the buffer limit
			abstractInput.setPosition(50);
			long bufferOffset = abstractInput.getBufferOffset();
			assertTrue("Buffer Offset is not set to expected value",
					bufferOffset == 0);

			// test outside the bounds of the current buffer
			// bufferOffset will be set to the beginning position of the buffer
			// that
			// is read into.
			long newBufferOffset = bufferSize + 50;
			abstractInput.setPosition(newBufferOffset);
			bufferOffset = abstractInput.getBufferOffset();
			assertTrue("Buffer Offset is not set to expected value",
					bufferOffset == newBufferOffset);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	@Test
	public void testGetBufferSize() {
		int bufferSize = abstractInput.getBufferSize();
		assertTrue("Buffer size not equal to set Buffer size",
				bufferSize == this.bufferSize);
	}

	@Test
	public void testGetByteArray() {
		byte[] byteArray = abstractInput.getByteArray();
		assertTrue("ByteArray not equal to buffersize",
				byteArray.length == bufferSize);
	}

	@Test
	public void testGetNextBuffer() {
		try {
			/*
			 * start at byte offset 0 of inputable, retrieve the next buffer and
			 * test that the buffer offset and position are set correctly
			 */
			abstractInput.setPosition(0);
			long bytesRead = ((AbstractInput) abstractInput).getNextBuffer();
			long position = abstractInput.getPosition();
			assertTrue("Buffer Offset not at expected value", abstractInput
					.getBufferOffset() == bufferSize);
			assertTrue("Current Position not at expected value",
					position == abstractInput.getBufferOffset());

			/*
			 * test that the last buffer read returns expected number of bytes
			 */
			abstractInput.setPosition(0);
			File inFile = abstractInput.getFile();
			long size = inFile.length();
			long lastBufferSizeChunk = size
					- ((size / bufferSize) * bufferSize);
			for (int i = 0; i < size / bufferSize; i++) {
				bytesRead = ((AbstractInput) abstractInput).getNextBuffer();
			}
			assertTrue("Size of last buffer not what expected",
					bytesRead == lastBufferSizeChunk);

			/*
			 * test getNextBuffer() returns EOF when EOF reached
			 */
			abstractInput.setPosition(0);
			while (bytesRead != Input.EOF)
				bytesRead = ((AbstractInput) abstractInput).getNextBuffer();
			assertTrue("EOF not returned as expected", bytesRead == Input.EOF);

		} catch (IOException e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
	}

	@Test
	public void testGetPosition() {
		try {
			abstractInput.setPosition(0);
			assertTrue("getPosition did not return value expected",
					abstractInput.getPosition() == 0);
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

	/*
	 * Test that getUnsignedByte() returns 1 byte
	 */
	@Test
	public void testReadUnsignedByte() {
		short testValue = 0;
		try {
			abstractInput.setPosition(0);
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

	/*
	 * Test that setByteOrder sets the byte order properly
	 */
	@Test
	public void testSetByteOrder() {
		long testValue = 0;
		ByteOrder bo = abstractInput.getBuffer().order();
		if (bo == ByteOrder.BIG_ENDIAN) {
			abstractInput.setByteOrder(ByteOrder.LITTLE_ENDIAN);
			assertTrue(
					"ByteOrder not set to LITTLE_ENDIAN correctly",
					(abstractInput.getBuffer().order() == ByteOrder.LITTLE_ENDIAN));
			// test how the byte order is returned when you set the byte order
			try {
				// for LITTLE ENDIAN, expect the least-significant byte to come
				// first
				abstractInput.setPosition(0);
				testValue = abstractInput.readUnsignedInt();
				String testStr = Long.toHexString(testValue);
				assertTrue("Hex value does not match expected '46BFBBEF'",
						testStr.equalsIgnoreCase("46BFBBEF"));
				out.println(testValue);
				out.printf("Byte Order = %s: 0x%X\n", abstractInput.getBuffer()
						.order(), testValue);

				// for BIG ENDIAN, expect the most-significant byte to come
				// first
				abstractInput.setByteOrder(ByteOrder.BIG_ENDIAN);
				abstractInput.setPosition(0);
				testValue = abstractInput.readUnsignedInt();
				testStr = Long.toHexString(testValue);
				assertTrue("Hex value does not match expected 'EFBBBF46'",
						testStr.equalsIgnoreCase("EFBBBF46"));
				out.println(testValue);
				out.printf("Byte Order = %s: 0x%X\n", abstractInput.getBuffer()
						.order(), testValue);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (bo == ByteOrder.LITTLE_ENDIAN) {
			abstractInput.setByteOrder(ByteOrder.BIG_ENDIAN);
			assertTrue("ByteOrder not set to BIG_ENDIAN correctly",
					abstractInput.getBuffer().order() == ByteOrder.BIG_ENDIAN);
		}
	}

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

}
