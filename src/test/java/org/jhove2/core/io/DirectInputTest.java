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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DirectInputTest {

	int bufferSize; 
	static AbstractInput abstractInput = null;
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
		//		abstractInput.close();
	}

	@Test
	public void testGetInput() {

		try {
			abstractInput = InputFactory.getInput(testFile, bufferSize, Type.Direct, ByteOrder.LITTLE_ENDIAN);			
			assertTrue("AbstractInput Type is Direct with LITTLE_ENDIAN", abstractInput.getBuffer().order() == ByteOrder.LITTLE_ENDIAN);
			abstractInput.close();
			abstractInput = InputFactory.getInput(testFile, bufferSize,
			                       Type.Direct);
			assertTrue("AbstractInput Type is Direct", abstractInput.getClass().getName().equalsIgnoreCase(DirectInput.class.getName()));
			assertTrue("AbstractInput Type is Direct with BIG_ENDIAN", abstractInput.getBuffer().order() == ByteOrder.BIG_ENDIAN);
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
		assertTrue("File is not same as abstractInput", testFile.getName().equals(inputableFile.getName()));
	}

	@Test
	public void testGetBufferOffset() {
		try {
			// test within the bounds of the current buffer.  Buffer Offset will
			// not change because you have not exceeded the buffer limit
			abstractInput.setPosition(50);
			long bufferOffset = abstractInput.getBufferOffset();
			assertTrue("Buffer Offset is not set to expected value", bufferOffset==0);

			// test outside the bounds of the current buffer
			// bufferOffset will be set to the beginning position of the buffer that 
			// is read into.
			long newBufferOffset = bufferSize + 50;
			abstractInput.setPosition(newBufferOffset);
			bufferOffset = abstractInput.getBufferOffset();
			assertTrue("Buffer Offset is not set to expected value", bufferOffset == newBufferOffset);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}


	@Test
	public void testGetBufferSize() {
		int bufferSize = abstractInput.getBufferSize();
		assertTrue("Buffer size not equal to set Buffer size", bufferSize == this.bufferSize);
	}


	@Test
	public void testGetByteArray() {
		byte[] byteArray = abstractInput.getByteArray();
		assertTrue("ByteArray not equal to buffersize", byteArray.length == bufferSize);
	}

	@Test
	public void testGetNextBuffer() {
		try {
			/* 
			 * start at byte offset 0 of inputable, retrieve the next buffer and 
			 * test that the buffer offset and position are set correctly
			 */
			abstractInput.setPosition(0);
			long bytesRead = abstractInput.getNextBuffer();
			long position = abstractInput.getPosition();
			assertTrue("Buffer Offset not at expected value", abstractInput.getBufferOffset() == bufferSize);
			assertTrue("Current Position not at expected value", position == abstractInput.getBufferOffset());

			/* 
			 * test that the last buffer read returns expected number of bytes
			 */ 
			abstractInput.setPosition(0);
			File inFile = abstractInput.getFile();
			long size = inFile.length();
			long lastBufferSizeChunk = size -((size/bufferSize) * bufferSize);
			for (int i=0; i < size/bufferSize; i++) {
				bytesRead = abstractInput.getNextBuffer();
			}
			assertTrue("Size of last buffer not what expected", bytesRead == lastBufferSizeChunk);

			/*
			 *  test getNextBuffer() returns EOF when EOF reached
			 */
			abstractInput.setPosition(0);
			while (bytesRead != Input.EOF)
				bytesRead = abstractInput.getNextBuffer();
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
			assertTrue("getPosition did not return value expected", abstractInput.getPosition() == 0);
		} catch (IOException e) {
			fail(e.getMessage());			
		}
	}

	/* 
	 * Test that getUnsignedByte() returns 1 byte 
	 */
	@Test
	public void testGetUnsignedByte() {
		short testValue = 0;
		try {
			abstractInput.setPosition(0);
			testValue = abstractInput.getUnsignedByte(); 			
			// check if high order bit is set
			assertTrue("High Order bit not set", (testValue > 128 && testValue < 255));			
			out.println(testValue);
			out.printf( "0x%X\n", testValue );
		} catch (IOException e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
	}

	/* 
	 * Test that getUnsignedShort() returns 2 bytes 
	 */
	@Test
	public void testGetUnsignedShort() {
		int testValue = 0;
		try {
			abstractInput.setPosition(0);
			testValue = abstractInput.getUnsignedShort();
			String testStr  = Integer.toHexString(testValue);
			assertTrue("High Order Bit not set ", testValue > 32768 && testValue < 65535);
			assertTrue("Hex value does not match expected 'bbbf'", testStr.equalsIgnoreCase("EFBB"));
			out.printf("%d\n", testValue);
			out.printf( "0x%X\n", testValue );
		} catch (IOException e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
	}

	/* 
	 * Test that getUnsignedInt() returns 4 bytes 
	 */
	@Test
	public void testGetUnsignedInt() {
		long testValue = 0;
		try {
			abstractInput.setPosition(0);
			testValue = abstractInput.getUnsignedInt();
			String testStr  = Long.toHexString(testValue);
			assertTrue("High Order Bit Not Set", (testValue > Math.pow(2,31) && testValue < Math.pow(2,32)));
			assertTrue("Hex value does not match expected 'efbbbf46'", testStr.equalsIgnoreCase("efbbbf46"));
			out.println(testValue);
			out.printf( "0x%X\n", testValue );
		} catch (IOException e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
	}

	/* 
	 * Test that getUnsignedInt() returns 4 bytes 
	 */
	@Test
	public void testGetSignedInt() {
		long testValue = 0;
		try {
			abstractInput.setPosition(0);
			int intTestValue = abstractInput.getSignedInt();
			String testStr  = Integer.toHexString(intTestValue);
			assertTrue("Value returned is not signed", (intTestValue < 0 ));
			assertTrue("Hex value does not match expected 'efbbbf46'", testStr.equalsIgnoreCase("efbbbf46"));
			out.println(intTestValue);
			out.printf( "0x%X\n", intTestValue );
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
			assertTrue("ByteOrder not set to LITTLE_ENDIAN correctly", (abstractInput.getBuffer().order()==ByteOrder.LITTLE_ENDIAN));
			// test how the byte order is returned when you set the byte order
			try {
				// for LITTLE ENDIAN, expect the least-significant byte to come first
				abstractInput.setPosition(0);
				testValue = abstractInput.getUnsignedInt();
				String testStr  = Long.toHexString(testValue);
				assertTrue("Hex value does not match expected '46BFBBEF'", testStr.equalsIgnoreCase("46BFBBEF"));
				out.println(testValue);
				out.printf( "Byte Order = %s: 0x%X\n", abstractInput.getBuffer().order(), testValue );

				// for BIG ENDIAN, expect the most-significant byte to come first
				abstractInput.setByteOrder(ByteOrder.BIG_ENDIAN);			
				abstractInput.setPosition(0);
				testValue = abstractInput.getUnsignedInt();
				testStr  = Long.toHexString(testValue);
				assertTrue("Hex value does not match expected 'EFBBBF46'", testStr.equalsIgnoreCase("EFBBBF46"));
				out.println(testValue);
				out.printf( "Byte Order = %s: 0x%X\n", abstractInput.getBuffer().order(), testValue );
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else if (bo == ByteOrder.LITTLE_ENDIAN) {
			abstractInput.setByteOrder(ByteOrder.BIG_ENDIAN);
			assertTrue("ByteOrder not set to BIG_ENDIAN correctly", abstractInput.getBuffer().order()==ByteOrder.BIG_ENDIAN);
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
			assertTrue("Set Position not set to correct position", position == newPosition);

			/*
			 * Test that the value expected to be at new position is true
			 */
			short b = abstractInput.getUnsignedByte();
			char byteChar = (char)(b);
			assertTrue("Position of inputable is not where its expected to be", byteChar=='f'); 
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
	 * bytes at position 98: 0A6F663A
	 * BIG_ENDIAN expected value = 0A6F663A
	 * LITTLE_ENDIAN expected value = 3A666F0A
	 */
	@Test
	public void testBufferBoundaries() {
		try {
			// test going beyond boundary explicitly with BIG_ENDIAN byte ordering
			abstractInput.setPosition(98);
			long testValue = abstractInput.getUnsignedInt();
			out.println(testValue);
			out.printf( "Byte Order = %s: 0x%X\n", abstractInput.getBuffer().order(), testValue );
			String testStr  = Long.toHexString(testValue);
			assertTrue("Hex value does not match expected '0A 6F 66 3A'", testStr.equalsIgnoreCase("A6F663A"));

			// test going beyond boundary explicitly with LITTLE_ENDIAN byte ordering
			ByteOrder bo = abstractInput.getBuffer().order();
			abstractInput.setByteOrder(ByteOrder.LITTLE_ENDIAN);			
			abstractInput.setPosition(98);
			testValue = abstractInput.getUnsignedInt();
			out.println(testValue);
			out.printf( "Byte Order = %s: 0x%X\n", abstractInput.getBuffer().order(), testValue );
			testStr  = Long.toHexString(testValue);
			assertTrue("Hex value does not match expected '3A666F0A'", testStr.equalsIgnoreCase("3A666F0A"));

			/*
			 *  test going beyond boundary without setting position explicitly beyond boundary
			 * (setPosition() takes care of getting the next buffer properly)
			 */
			abstractInput.setPosition(0);
			abstractInput.setByteOrder(ByteOrder.BIG_ENDIAN);
			long position = 0;
			while ((position = abstractInput.getPosition()) < 96)				
				testValue = abstractInput.getUnsignedInt();
			int shortValue = abstractInput.getUnsignedShort();
			position = abstractInput.getPosition();
			assertTrue("Position not at value 98 as expected", position == 98);
			testValue = abstractInput.getUnsignedInt();
			out.println(testValue);
			out.printf( "Byte Order = %s: 0x%X\n", abstractInput.getBuffer().order(), testValue );
			testStr  = Long.toHexString(testValue);
			assertTrue("Hex value does not match expected '3A666F0A'", testStr.equalsIgnoreCase("A6F663A"));

			/* 
			 * same test as above but for LITTLE_ENDIAN
			 */
			abstractInput.setPosition(0);
			abstractInput.setByteOrder(ByteOrder.LITTLE_ENDIAN);
			position = 0;
			while ((position = abstractInput.getPosition()) < 96)				
				testValue = abstractInput.getUnsignedInt();
			shortValue = abstractInput.getUnsignedShort();
			position = abstractInput.getPosition();
			assertTrue("Position not at value 98 as expected", position == 98);
			testValue = abstractInput.getUnsignedInt();
			out.println(testValue);
			out.printf( "Byte Order = %s: 0x%X\n", abstractInput.getBuffer().order(), testValue );
			testStr  = Long.toHexString(testValue);
			assertTrue("Hex value does not match expected '3A666F0A'", testStr.equalsIgnoreCase("3A666F0A"));

		} catch (IOException e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
	}

}
