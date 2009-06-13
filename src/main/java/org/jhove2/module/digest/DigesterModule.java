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

import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.jhove2.core.AbstractModule;
import org.jhove2.core.Digest;
import org.jhove2.core.Digestable;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.io.Input;

/** JHOVE2 message digester module.
 * 
 * @author mstrong,slabrams
 */
public class DigesterModule
	extends AbstractModule
	implements Digestable
{
	/** Framework version identifier. */
	public static final String VERSION = "1.0.0";

	/** Framework release date. */
	public static final String DATE = "2009-06-13";
	
	/** Framework rights statement. */
	public static final String RIGHTS =
		"Copyright 2009 by The Regents of the University of California, " +
		"Ithaka Harbors, Inc., and The Board of Trustees of the Leland " +
		"Stanford Junior University. " +
		"Available under the terms of the BSD license.";

	protected List<BufferDigester> digesters;
	
	/** Instantiate a new <code>DigesterModule</code>.
	 */
	public DigesterModule() {
		super(VERSION, DATE, RIGHTS);

		this.digesters = new ArrayList<BufferDigester>();
		
		/* TODO: configure via Spring. */
		try {
			this.digesters.add(new MD5Digester());
			this.digesters.add(new SHA1Digester());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/** Calculate message digests.
	 * @param jhove2 JHOVE2 framework
	 * @see org.jhove2.core.Digestable#digest(org.jhove2.core.JHOVE2, org.jhove2.core.io.Input)
	 */
	@Override
	public void digest(JHOVE2 jhove2, Input input)
		throws IOException
	{
		long inputSize  = input.getSize();
		long bufferSize = input.getMaxBufferSize();
		long ptr = 0L;
		while (inputSize - ptr > -1L) {
			input.setPosition(ptr);
			ByteBuffer buffer = input.getBuffer();
			if (this.digesters.size() > 0) {
				Iterator<BufferDigester> iter = this.digesters.iterator();
				while (iter.hasNext()) {
					BufferDigester digester = iter.next();
					digester.update(buffer);
				}
			}
			ptr += bufferSize;
		}
	}

	/** Get message digests.
	 * @return Message digests
	 * @see org.jhove2.core.Digestable#getDigests()
	 */
	@Override
	public Set<Digest> getDigests() {
		Set<Digest> set = new TreeSet<Digest>();
		if (this.digesters.size() > 0) {
			Iterator<BufferDigester> iter = this.digesters.iterator();
			while (iter.hasNext()) {
				BufferDigester digester = iter.next();
				Digest digest = digester.getDigest();
				set.add(digest);
			}
		}
		
		return set;
	}

	/** Set an algorithm-specific digester.
	 * @param digester Algorithm-specific digester
	 */
	public void setDigester(BufferDigester digester) {
		this.digesters.add((BufferDigester) digester);
	}
}
