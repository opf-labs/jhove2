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

import org.jhove2.core.Invocation;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.io.Input.Type;

/**
 * JHOVE2 {@link org.jhove2.core.io.Input} factory.
 * 
 * @author mstrong, slabrams
 */
public class InputFactory {
	/**
	 * Factory to create an appropriate big-endian <code>AbstractInput</code>.
	 * @param jhove2 JHOVE2 framework object
	 * @param file
	 *            Java {java.io.File} underlying the inputable
     * @param isTemp
     *            Temporary file status: true if a temporary file
	 * @return Input
	 * @throws FileNotFoundException
	 *             File not found
	 * @throws IOException
	 *             I/O exception instantiating input
	 */
	public static Input getInput(JHOVE2 jhove2, File file, boolean isTemp)
		throws FileNotFoundException, IOException
	{
		return getInput(jhove2, file, isTemp, ByteOrder.BIG_ENDIAN);
	}

	/**
	 * Factory to create an appropriate <code>AbstractInput</code>.  
	 * MemoryMapped IO will not be used for files which are larger 
	 * than Integer.MAX_VALUE (2GB on most systems) and Direct IO
	 * Input type will be returned.
	 *  
	 * NOTE: A process is limited to 2GB of virtual memory on 32-bit 
	 * editions of Windows which means it is not possible to map 
	 * a region close to that size. If sharing is disabled 
	 * (-Xshare:off or use server VM) then it should be possible 
	 * to map a region up to ~1.6GB. It will be less than this 
	 * if the java heap needs to be increased beyond its default. 
	 * If sharing is enabled then the maximum will be reduced 
	 * to about ~1-1.1GB.
	 * @param jhove2 JHOVE2 framework object
	 * @param file
	 *            Java {java.io.File} underlying the inputable
     * @param isTemp
     *            Temporary file status: true if a temporary file
	 * @param order
	 *            ByteOrder Endianess of buffer
	 * @return Input
	 * @throws FileNotFoundException
	 *             File not found
	 * @throws IOException
	 *             I/O exception instantiating input
	 */
	public static Input getInput(JHOVE2 jhove2, File file, boolean isTemp,
	                             ByteOrder order)
	    throws IOException
	{
		AbstractInput input = null;
		if (file != null && file.exists() && file.canRead()) {
		    Invocation inv = jhove2.getInvocation();
		    Type type = inv.getBufferType();
		    if (type.equals(Type.Direct)) {
		        input = new DirectInput(jhove2, file, isTemp, order);
		    }
		    else if (type.equals(Type.NonDirect)) {
		        input = new NonDirectInput(jhove2, file, isTemp, order);
		    }
		    else if (type.equals(Type.Mapped)) {
		        /* Only files smaller than Input.MAX_MAPPED_FILESIZE can utilize 
		         * MappedByteBuffers 
		         */
		        if (file.length() < Input.MAX_MAPPED_FILE) {
		            input = new MappedInput(jhove2, file, isTemp, order);
		        }
		        else {
		            input = new DirectInput(jhove2, file, isTemp, order);
		        }
		    }
		}

		return input;
	}
}
