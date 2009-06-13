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
package org.jhove2.module.digest;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.jhove2.core.Digest;

/**
 * @author slabrams
 *
 */
public abstract class AbstractBufferDigester
	implements BufferDigester
{
	/** Message digest algorithm. */
	protected String algorithm;
	
	/** Message digester. */
	protected MessageDigest digester;
	
	/** Instantiate a new <code>AbstractBufferDigester</code>
	 */
	public AbstractBufferDigester(String algorithm)
		throws NoSuchAlgorithmException
	{
		this.algorithm = algorithm;

		this.digester = MessageDigest.getInstance(algorithm);
	}
	
	/** Update a message digest.
	 * @param buffer Buffer
	 * @see org.jhove2.module.digest.BufferDigester#update(java.nio.ByteBuffer)
	 */
	@Override
	public void update(ByteBuffer buffer) {
		this.digester.update(buffer);
	}

	/** Get message digest value, as a hexadecimal string.
	 * @return Message digest value, as a hexadecimal string
	 * @see org.jhove2.module.digest.Digester#getDigest()
	 */
	@Override
	public Digest getDigest() {
		byte [] value = this.digester.digest();
		
		return new Digest(toHexString(value), this.algorithm);
	}
	
	/** Format a message digest value as a hexadecimal string.
	 * @param digest Message digest value
	 * @return Message digest value as a hexadecimal string
	 */
	public static synchronized String toHexString(byte [] digest) {
		StringBuffer hex = new StringBuffer();
		for (int i=0; i<digest.length; i++) {
			int in = digest[i];
			if (in < 0) {
				in = 256 + in;
			}
			if (in < 16) {
				hex.append("0");
			}
			String h = Integer.toHexString(in);
			hex.append(h);
		}
		
		return hex.toString();
	}

	/** Format a message digest value as a hexadecimal string.
	 * @param digest Message digest value
	 * @return Message digest value as a hexadecimal string
	 */
	public static synchronized String toHexString(long digest) {
		StringBuffer hex = new StringBuffer();
		String h = Long.toHexString(digest);
		int len = h.length();
		for (int i=len; i<8; i++) {
			hex.append("0");
		}
		hex.append(h);
		
		return hex.toString();
	}
}
