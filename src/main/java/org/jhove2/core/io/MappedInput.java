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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * JHOVE2 mapped inputable. A direct byte buffer whose content is a
 * memory-mapped region of a file. Use of a direct buffer permits the JVM to use
 * native I/O for increased performance.
 * 
 * @author mstrong, slabrams
 */
public class MappedInput
    extends AbstractInput
{
	/**
	 * Instantiate a new, big-endian <code>MappedInput</code> object.
	 * 
	 * @param file
	 *            Java {@link java.io.File} underlying the inputable
     * @param deleteOnClose
     *            Temporary file deletion status: true if delete on close
	 * @param maxBufferSize
	 *            Size of the mapped byte buffer, in bytes
	 * @throws FileNotFoundException
	 *             File not found
	 * @throws IOException
	 *             I/O exception instantiating input
	 */
	public MappedInput(File file, boolean deleteOnClose, int maxBufferSize)
		throws FileNotFoundException, IOException
	{
		this(file, deleteOnClose, maxBufferSize, ByteOrder.BIG_ENDIAN);
	}

	/**
	 * Instantiate a new <code>MappedInput</code> object.
	 * 
	 * @param file
	 *            Java {@link java.io.File} underlying the inputable
     * @param deleteOnClose
     *            Temporary file deletion status: true if delete on close
	 * @param maxBufferSize
	 *            Size of the mapped byte buffer, in bytes
	 * @param order
	 *            Byte order
	 * @throws FileNotFoundException
	 *             File not found
	 * @throws IOException
	 *             I/O exception instantiating input
	 */
	public MappedInput(File file, boolean deleteOnClose, int maxBufferSize,
	                   ByteOrder order)
		throws FileNotFoundException, IOException
	{
		super(file, deleteOnClose, maxBufferSize, order);

		/* Allocate memory mapped buffer and initialize it.
		*  The buffer size will always be size of file channel size.
		*  Note that this overrides the buffer size used in the superclass
		*  when creating a buffered input stream from this Input.
		*/
		this.maxBufferSize = (int) this.channel.size();
		this.buffer = (MappedByteBuffer) buffer;
		/*
		 * TODO: fix Access Denied problem java.io.IOException: Access is denied
		 * at sun.nio.ch.FileChannelImpl.truncate0(Native Method) at
		 * sun.nio.ch.FileChannelImpl.map(FileChannelImpl.java:731) this works
		 * if maxBuffersize set to 0
		 */

		try {
			this.buffer = this.channel.map(FileChannel.MapMode.READ_ONLY, 0,
					                       this.maxBufferSize);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.inputablePosition = this.inputablePosition + bufferSize;
		this.bufferOffset = this.channel.position() - buffer.capacity();
		this.bufferSize = buffer.capacity();
	}
}
