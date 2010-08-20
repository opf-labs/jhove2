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
package org.jhove2.module.format.utf8.unicode;

import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;

/**
 * A Unicode code block, a range of code points associated with a named language
 * or script system. Code blocks are initialized from a properties file
 * formatted the same as the Unicode database (UCB) file Blocks.txt:
 * 
 * <code>start..end; name</code>
 * 
 * with the starting and ending code point (in hexadecimal), and block name.
 * 
 * @author mstrong, slabrams
 */
public class CodeBlock implements Comparable<CodeBlock> {
	/** Singleton Unicode code blocks. */
	protected static Set<CodeBlock> codeBlocks;

	/** Unicode code block range ending value. */
	protected int end;

	/** Unicode code block name. */
	protected String name;

	/** Unicode code block range starting value. */
	protected int start;

	/**
	 * Instantiate a <code>CodeBlock</code> object.
	 * 
	 * @param start
	 *            Starting code point of the block
	 * @param end
	 *            Ending code point of the block
	 * @param name
	 *            Code block name
	 */
	public CodeBlock(int start, int end, String name) {
		this.start = start;
		this.end = end;
		this.name = name;
	}

	/**
	 * Get the code blocks.
	 * 
	 * @return Code blocks
	 */
	public static Set<CodeBlock> getCodeBlocks() {
		return codeBlocks;
	}

	/**
	 * Get the code block for a code point.
	 * 
	 * @param codePoint
	 *            Code point
	 * @param properties TODO
	 * @return Code block, or null if the code point is outside of all defined
	 *         blocks
	 * @throws JHOVE2Exception
	 */
	public static synchronized CodeBlock getBlock(int codePoint, JHOVE2 jhove2)
			throws JHOVE2Exception {
		if (codeBlocks == null) {
			/* Initialize the code blocks from Java Properties. */
			codeBlocks = new TreeSet<CodeBlock>();
	        Properties props = jhove2.getConfigInfo().getProperties("CodeBlock");
			if (props != null) {
				Set<String> set = props.stringPropertyNames();
				Iterator<String> iter = set.iterator();
				while (iter.hasNext()) {
					String range = iter.next();
					String name = props.getProperty(range);

					int st = range.indexOf('.');
					int en = range.indexOf(';');
					String start = range.substring(0, st);
					String end = range.substring(st + 2, en);

					st = Integer.parseInt(start, 16);
					en = Integer.parseInt(end, 16);
					CodeBlock block = new CodeBlock(st, en, name);
					codeBlocks.add(block);
				}
			}
		}
		CodeBlock block = null;
		Iterator<CodeBlock> iter = codeBlocks.iterator();
		while (iter.hasNext()) {
			CodeBlock blk = iter.next();
			if (codePoint >= blk.getStart() && codePoint <= blk.getEnd()) {
				block = blk;
				break;
			}
		}

		return block;
	}

	/**
	 * Get the ending code point for the block.
	 * 
	 * @return Ending code point
	 */
	public int getEnd() {
		return this.end;
	}

	/**
	 * Get the code block name.
	 * 
	 * @return Code block name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Get the starting code point for the block.
	 * 
	 * @return Starting code point
	 */
	public int getStart() {
		return this.start;
	}

	/**
	 * Convert the code block to a Java string.
	 * 
	 * @return Java string representation of the code block
	 */
	public String toString() {
		return this.getName();
	}

	/**
	 * Compare Unicode code block.
	 * 
	 * @param block
	 *            Unicode code block to be compared
	 * @return -1, 0, or 1 if this block is less than, equal to, or greater than
	 *         the second
	 */
	@Override
	public int compareTo(CodeBlock block) {
		int ret = 0;
		int start = block.getStart();
		if (this.start < start) {
			ret = -1;
		} else if (this.start > start) {
			ret = 1;
		}

		return ret;
	}
}
