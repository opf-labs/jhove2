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

package org.jhove2.core;

/** A JHOVE2 message digest.
 * 
 * @author mstrong, slabrams
 */
public class Digest
	implements Comparable<Digest>
{
	/** Message digest algorithms. */
	public enum Algorithm {
		Adler32("Adler-32"), /* As define by RFC 1950. */
		CRC32  ("CRC-32"),   /* As defined by IEEE 802.3. */
		MD2    ("MD2"),      /* As defined by RFC 1319. */
		MD5    ("MD5"),      /* As defined by RFC 1310. */
		SHA1   ("SHA-1"),    /* As defined by FIPS PUB 180-2. */
		SHA256 ("SHA-256"),
		SHA384 ("SHA-384"),
		SHA512 ("SHA-512");
		
		/** Algorithm name, taken from "Java Cryptography Architecture:
		 * Standard Algorithm Name Documentation for Java Platform Standard
		 * Edition 6".
		 * @see http://java.sun.com/javase/6/docs/api/
		 */
		private String name;
		
		/** Instantiate a new <code>Algorithm</code>.
		 * @param name Algorithm name
		 */
		private Algorithm(String name) {
			this.name = name;
		}
		
		@Override
		public String toString() {
			return this.name;
		}
	}
	
	/** Message digest algorithm. */
	protected Algorithm algorithm;
	
	/** Message digest value, hex encoded. */
	protected String value;
	
	/** Instantiate a new <code>Digest</code>.
	 * @param value     Message digest value, hex encoded
	 * @param algorithm Message digest algorithm
	 */
	public Digest(String value, Algorithm algorithm) {
		this.value     = value;
		this.algorithm = algorithm;
	}

	/** Get the message digest algorithm.
	 * @return Message digest algorithm
	 */
	public Algorithm getAlgorithm ()
	{
		return this.algorithm;
	}

	/** Get the message digest value.
	 * @return Message digest value, hex encoded
	 */
	public String getValue ()
	{
		return this.value;
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
	
	/** Get a String representation of the message digest, in the form
	 * "algorithm:value".
	 * @return String representation of the message digest
	 */
	@Override
	public String toString()
	{
		return this.algorithm.toString() + ":" + value;
	}

	/** Lexically compare message digest.
	 * @param digest Message digest to be compared
	 * @return -1, 0, or 1 if this identifier value is less than, equal
	 *         to, or greater than the second
	 * @see java.lang.comparable#compareTo(Object)
	 */
	@Override
	public int compareTo(Digest digest)
	{
		int ret = this.algorithm.toString().compareToIgnoreCase(digest.getAlgorithm().toString());
		if (ret == 0) {
			ret = this.value.compareToIgnoreCase(digest.getValue());
		}
		return ret;
	}
}
