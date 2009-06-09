/**
 * JHOVE2 - Next-generation architecture for format-aware characterization
 *
 * Copyright (c) 2009 by The Regents of the University of California.
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

import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.zip.Adler32;
import java.util.zip.CRC32;

import org.jhove2.annotation.ReportableMessage;
import org.jhove2.core.AbstractComponent;
import org.jhove2.core.Digest;
import org.jhove2.core.Digestable;
import org.jhove2.core.Digest.Algorithm;
import org.jhove2.core.io.Input;
import org.jhove2.core.JHOVE2;
import org.jhove2.module.digest.message.AlgorithmNotSupported;

/** A message digester.
 * 
 * @author mstrong, slabrams
 */
public class Digester
	extends AbstractComponent
	implements Digestable
{
	/** Digester version identifier. */
	public static final String VERSION = "1.0.0";

	/** Digester release date. */
	public static final String DATE = "2009-05-28";
	
	/** Digester development stage. */
	public static final Stage STAGE = Stage.Development;
	
	/** Message digest values. */
	protected Set<Digest> digests;
	
	/** Instantiate a new <code>Digester</code> component.
	 */
	public Digester() {
		super(VERSION, DATE, STAGE);
		
		this.digests = new TreeSet<Digest>();
	}

	/** Calculate message digests.  The digest values are reported as
	 * hexadecimal strings.
	 * @param jhove2     JHOVE2 framework
	 * @param input      Input
	 * @param algorithms Message digest algorithms
	 * @throws IOException 
	 * @see org.jhove2.core.Digestable#digest(org.jhove2.core.io.Input, java.util.EnumSet)
	 */
	@Override
	public void digest(JHOVE2 jhove2, Input input,
			           EnumSet<Algorithm> algorithms)
		throws IOException
	{
		reset();
		Adler32 adler32 = null;
		CRC32   crc32   = null;

		/* Allocate a {@link java.security.MessageDigest} for each algorithm
		 * specified in the argument enum set.
		 */
		Map<Algorithm, MessageDigest> mds = new HashMap<Algorithm, MessageDigest>();
		Iterator<Algorithm> iter = algorithms.iterator();
		while (iter.hasNext()) {
			Algorithm algorithm = iter.next();
			if (algorithm != Algorithm.Adler32 &&
				algorithm != Algorithm.CRC32) {
				try {
					mds.put(algorithm, MessageDigest.getInstance(algorithm.toString()));
				} catch (NoSuchAlgorithmException e) {
					@ReportableMessage
					AlgorithmNotSupported msg = new AlgorithmNotSupported(algorithm);
					addMessage(msg);
				}
			}
		}
		/* Allocate a {@link java.util.zip.Adler32} and
		 * {@link java.util.zip.CRC32} if they are specified.
		 */ 
		if (algorithms.contains(Algorithm.Adler32)) {
			adler32 = new Adler32();
		}
		if (algorithms.contains(Algorithm.CRC32)) {
			crc32 = new CRC32();
		}

		/* Read through the {@link org.jhove2.core.io.Input} a buffer at a
		 * time, updating all digest values.
		 */
		int size = mds.size();
		if (size > 0 || adler32 != null || crc32 != null) {
			long inputSize  = input.getSize();
			long bufferSize = input.getMaxBufferSize();
			long ptr = 0L;
			while (inputSize - ptr > -1L) {
				input.setPosition(ptr);
				
				if (size > 0) {
					ByteBuffer buffer = input.getBuffer();
					Set<Algorithm> ks = mds.keySet();
					iter = ks.iterator();
					while (iter.hasNext()) {
						Algorithm algorithm = iter.next();
						MessageDigest md = mds.get(algorithm);
						buffer.position(0);
						md.update(buffer);
					}
				}
				if (adler32 != null || crc32 != null) {
					byte [] b = input.getByteArray();
					if (adler32 != null) {
						adler32.update(b);
					}
					if (crc32 != null) {
						crc32.update(b);
					}
				}
				ptr += bufferSize;
			}
			/* Add each algorithm value to the reportable. */
			Set<Algorithm> ks = mds.keySet();
			iter = ks.iterator();
			while (iter.hasNext()) {
				Algorithm algorithm = iter.next();
				MessageDigest md = mds.get(algorithm);
				byte [] b = md.digest();
				this.digests.add(new Digest(Digest.toHexString(b), algorithm));
			}
			if (adler32 != null) {
				long value = adler32.getValue();
				this.digests.add(new Digest(Digest.toHexString(value),
						                    Algorithm.Adler32));
			}
			if (crc32 != null) {
				long value = crc32.getValue();
				this.digests.add(new Digest(Digest.toHexString(value),
						                    Algorithm.CRC32));
			}
		}
	}

	/** Get message digest values.
	 * @return Message digest values
	 * @see org.jhove2.core.Digestable#getDigestValues()
	 */
	@Override
	public Set<Digest> getDigestValues() {
		return digests;
	}
}
