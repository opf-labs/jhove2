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

package org.jhove2.module.format.zip;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.jhove2.core.Format;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.io.Input;
import org.jhove2.core.source.Source;
import org.jhove2.core.source.SourceFactory;
import org.jhove2.module.format.BaseFormatModuleCommand;
import org.jhove2.module.format.Validator.Coverage;

/**
 * JHOVE2 Zip module.
 * 
 * @author mstrong, slabrams
 */
public class ZipModule
	extends BaseFormatModuleCommand

{
	/** Zip module version identifier. */
	public static final String VERSION = "0.1.3";

	/** Zip module release date. */
	public static final String RELEASE = "2009-09-05";

	/** Zip module rights statement. */
	public static final String RIGHTS =
		"Copyright 2009 by The Regents of the University of California, " +
		"Ithaka Harbors, Inc., and The Board of Trustees of the Leland " +
		"Stanford Junior University. " +
		"Available under the terms of the BSD license.";

	/**
	 * Instantiate a new <code>ZipModule</code>.
	 * 
	 * @param format
	 *            Zip format
	 */
	public ZipModule(Format format) {
		super(VERSION, RELEASE, RIGHTS, format);
	}

	/**
	 * Parse a Zip source unit.
	 * 
	 * @param source
	 *            Zip ource unit
	 * @return 0
	 * @throws EOFException
	 *             If End-of-File is reached reading the source unit
	 * @throws IOException
	 *             If an I/O exception is raised reading the source unit
	 * @throws JHOVE2Exception
	 * @see org.jhove2.module.format.Parser#parse(org.jhove2.core.JHOVE2,
	 *      org.jhove2.core.source.Source)
	 */
	@Override
	public long parse(JHOVE2 jhove2, Source source)
		throws EOFException, IOException, JHOVE2Exception
	{
		Input input = null;
		try {
			input = source.getInput(jhove2.getAppConfigInfo().getBufferSize(), jhove2.getAppConfigInfo()
					.getBufferType());
			if (input != null) {
				File file = input.getFile();
				ZipFile zip = new ZipFile(file, ZipFile.OPEN_READ);

				/*
				 * Zip entries are not necessarily in hierarchical order. Build
				 * a map of directory names and source units so we can associate
				 * file entries to their correct parent.
				 */
				Map<String, Source> map = new TreeMap<String, Source>();
				Enumeration<? extends ZipEntry> en = zip.entries();
				while (en.hasMoreElements()) {
					ZipEntry entry = en.nextElement();
					if (entry.isDirectory()) {
						Source src = SourceFactory
								.getSource(jhove2.getAppConfigInfo().getTempPrefix(),
										jhove2.getAppConfigInfo().getTempSuffix(),
										jhove2.getAppConfigInfo().getBufferSize(),
										zip, entry);
						if (src != null) {
							String key = entry.getName();
							/* Remove trailing slash. */
							int len = key.length() - 1;
							if (key.charAt(len) == '/') {
								key = key.substring(0, len);
							}
							map.put(key, src);
						}
					}
				}

				/*
				 * Characterize each entry and associate it with its parent
				 * source unit.
				 */
				en = zip.entries();
				while (en.hasMoreElements()) {
					ZipEntry entry = en.nextElement();
					String name = entry.getName();
					if (entry.isDirectory()) {
						int len = name.length() - 1;
						if (name.charAt(len) == '/') {
							name = name.substring(0, len);
						}
						/* Get the source unit from the map. */
						Source src = map.get(name);
						if (src != null) {
							jhove2.characterize(src);

							int in = name.lastIndexOf('/');
							if (in > -1 && in < name.length() - 1) {
								/*
								 * Directory is a child of a Zip directory entry
								 * that can be retrieved from the map.
								 */
								String key = name.substring(0, in);
								Source parent = map.get(key);
								if (parent != null) {
									parent.addChildSource(src);
								}
							} else {
								/* Directory is a child of the Zip file. */
								source.addChildSource(src);
							}
						}
					} else {
						Source src = SourceFactory
								.getSource(jhove2.getAppConfigInfo().getTempPrefix(),
										jhove2.getAppConfigInfo().getTempSuffix(),
										jhove2.getAppConfigInfo().getBufferSize(), zip, entry);
						if (src != null) {
							jhove2.characterize(src);

							int in = name.lastIndexOf('/');
							if (in < 0) {
								/* File is a child of the Zip file. */
								source.addChildSource(src);
							} else {
								/*
								 * File is a child of a Zip file entry that can
								 * be retrieved from the map.
								 */
								String key = name.substring(0, in);
								Source parent = map.get(key);
								if (parent != null) {
									parent.addChildSource(src);
								}
							}
						}
					}
				}
			}
		} finally {
			if (input != null) {
				input.close();
			}
		}

		return 0;
	}
}
