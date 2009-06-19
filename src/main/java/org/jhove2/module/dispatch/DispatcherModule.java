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

package org.jhove2.module.dispatch;

import java.io.EOFException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

import org.jhove2.core.AbstractModule;
import org.jhove2.core.Dispatchable;
import org.jhove2.core.Format;
import org.jhove2.core.FormatIdentification;
import org.jhove2.core.I8R;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.Parsable;
import org.jhove2.core.config.Configure;
import org.jhove2.core.source.Source;

/** JHOVE2 dispatcher module.  The module instantiates and invokes the module
 * appropriate for a format.
 * 
 * @author mstrong, slabrams
 */
public class DispatcherModule
	extends AbstractModule
	implements Dispatchable
{
	/** Dispatcher module version identifier. */
	public static final String VERSION = "1.0.0";

	/** Dispatcher module release date. */
	public static final String DATE = "2009-06-16";
	
	/** Dispatcher module rights statement. */
	public static final String RIGHTS =
		"Copyright 2009 by The Regents of the University of California, " +
		"Ithaka Harbors, Inc., and The Board of Trustees of the Leland " +
		"Stanford Junior University. " +
		"Available under the terms of the BSD license.";
	
	/** Dispatch map. */
	protected Map<String,String> dispatch;
	
	/** Instantiate a new <code>DispatcherModule</code>.
	 * @throws JHOVE2Exception 
	 */
	public DispatcherModule()
		throws JHOVE2Exception
	{
		super(VERSION, DATE, RIGHTS);
		
		this.dispatch = new TreeMap<String,String>();
		Properties props = Configure.getProperties("Dispatch");
		Set<String> keys = props.stringPropertyNames();
		Iterator<String> iter = keys.iterator();
		while (iter.hasNext()) {
			String key   = iter.next();
			String value = props.getProperty(key);
			this.dispatch.put(key, value);
		}
	}

	/** Dispatch a module appropriate for a source unit's format.
	 * @param jhove2   JHOVE2 framework
	 * @param source   Source unit
	 * @param formatID Source unit format identification
	 * @throws JHOVE2Exception 
	 * @throws IOException 
	 * @throws EOFException 
	 * @see org.jhove2.core.Dispatchable#dispatch(org.jhove2.core.JHOVE2, org.jhove2.core.FormatIdentification)
	 */
	@Override
	public Parsable dispatch(JHOVE2 jhove2, Source source,
			                 FormatIdentification formatID)
		throws EOFException, IOException, JHOVE2Exception
	{
		Parsable module = null;
		
		Format format     = formatID.getFormat();
		I8R    identifier = format.getIdentifier();
		String name       = this.dispatch.get(identifier.getValue());
		if (name != null) {
			module = Configure.getReportable(Parsable.class, name);
			if (module != null) {
				module.setStartTime();
				module.parse(jhove2, source);
				module.setEndTime();
			}
		}
		
		return module;
	}
}
