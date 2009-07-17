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
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

import org.jhove2.core.I8R;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.config.Configure;
import org.jhove2.core.source.AggregateSource;
import org.jhove2.core.source.Source;
import org.jhove2.module.AbstractModule;
import org.jhove2.module.Module;
import org.jhove2.module.digest.Digester;
import org.jhove2.module.format.FormatModule;
import org.jhove2.module.format.FormatProfile;
import org.jhove2.module.format.Parser;
import org.jhove2.module.format.Validator;
import org.jhove2.module.identify.Aggrefier;
import org.jhove2.module.identify.Identifier;

/** JHOVE2 dispatcher module.  The module instantiates and invokes the module
 * associated with an identifier.
 * 
 * @author mstrong, slabrams
 */
public class DispatcherModule
	extends AbstractModule
	implements Dispatcher
{
	/** Dispatcher module version identifier. */
	public static final String VERSION = "1.0.0";

	/** Dispatcher module release date. */
	public static final String RELEASE = "2009-07-16";
	
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
		super(VERSION, RELEASE, RIGHTS);
		
		this.dispatch = new TreeMap<String,String>();
		Properties props = Configure.getProperties("Dispatcher");
		Set<String> keys = props.stringPropertyNames();
		for (String key : keys) {
			String value = props.getProperty(key);
			this.dispatch.put(key, value);
		}
	}

	/** Dispatch a source unit to the module associated with an identifier.
	 * @param jhove2     JHOVE2 framework
	 * @param source     Source unit
	 * @param identifier Module identifier
	 * @return Module
	 * @throws EOFException    End-of-file encountered parsing the source unit
	 * @throws IOException     I/O exception encountered parsing the source unit
	 * @throws JHOVE2Exception 
	 * @see org.jhove2.module.dispatch.Dispatcher#dispatch(org.jhove2.core.JHOVE2, org.jhove2.core.source.Source, org.jhove2.core.I8R)
	 */
	@Override
	public Module dispatch(JHOVE2 jhove2, Source source, I8R identifier)
		throws EOFException, IOException, JHOVE2Exception
	{
		Module module = null;

		String name = this.dispatch.get(identifier.getValue());
		if (name != null) {
			module = Configure.getReportable(Module.class, name);
			if (module != null) {
				dispatch(jhove2, source, module);
			}
			else {
				/* TODO: module can't be instantiated. */
				System.out.println("# CAN'T INSTANTIATE " + name);
			}
		}
		else {
			/* TODO: report that no dispatchable module was found. */
			System.out.println("# CAN'T DISPATCH " + identifier);
		}
		
		return module;
	}
	
	/** Dispatch a source unit to a module, adding the module to the source.
	 * @param jhove2 JHOVE2 framework
	 * @param source Source unit
	 * @param Module Module
	 * @return Module
	 * @throws EOFException    End-of-file encountered parsing the source unit
	 * @throws IOException     I/O exception encountered parsing the source unit
	 * @throws JHOVE2Exception 
	 * @see org.jhove2.module.dispatch.Dispatcher#dispatch(org.jhove2.core.JHOVE2, org.jhove2.core.source.Source, org.jhove2.module.Module)
	 */
	public void dispatch(JHOVE2 jhove2, Source source, Module module)
		throws EOFException, IOException, JHOVE2Exception
	{
		dispatch(jhove2, source, module, Disposition.AddToSource);
	}
	
	/** Dispatch a source unit to a module.
	 * @param jhove2      JHOVE2 framework
	 * @param source      Source unit
	 * @param Module      Module
	 * @param disposition Module disposition
	 * @throws EOFException    End-of-file encountered parsing the source unit
	 * @throws IOException     I/O exception encountered parsing the source unit
	 * @throws JHOVE2Exception 
	 * @see org.jhove2.module.dispatch.Dispatcher#dispatch(org.jhove2.core.JHOVE2, org.jhove2.core.source.Source, org.jhove2.module.Module, org.jhove2.module.dispatch.Disposition)
	 */
	public void dispatch(JHOVE2 jhove2, Source source, Module module,
			             Disposition disposition)
		throws EOFException, IOException, JHOVE2Exception
	{
		module.setStartTime();
			
		if (module instanceof Aggrefier &&
			source instanceof AggregateSource) {
			((Aggrefier) module).identify(jhove2, (AggregateSource) source);
		}
		if (module instanceof Identifier) {
			((Identifier) module).identify(jhove2, source);
		}
				
		if (module instanceof FormatModule) {
			if (module instanceof Parser) {
				((Parser) module).parse(jhove2, source);
			}
			if (module instanceof Validator) {
				((Validator) module).validate(jhove2, source);
			}
					
			List<FormatProfile> profiles =
				((FormatModule) module).getProfiles();
			if (profiles.size() > 0) {
				for (FormatProfile profile : profiles) {
					if (profile instanceof Validator) {
						profile.setStartTime();
						((Validator) profile).validate(jhove2, source);
						profile.setEndTime();
					}
				}
			}
		}
				
		if (module instanceof Digester) {
			((Digester) module).digest(jhove2, source);
		}
		module.setEndTime();
		
		if (disposition == Disposition.AddToSource) {
			source.addModule(module);
		}
	}
}
