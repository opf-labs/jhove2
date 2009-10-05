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
package org.jhove2.module.format;

import java.util.HashMap;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.jhove2.core.Format;
import org.jhove2.core.FormatIdentification;
import org.jhove2.core.I8R;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Command;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.Message;
import org.jhove2.core.Message.Context;
import org.jhove2.core.Message.Severity;
import org.jhove2.core.config.Configure;
import org.jhove2.core.source.Source;
import org.jhove2.module.AbstractModule;
import org.jhove2.module.Module;
import org.jhove2.module.identify.DroidIdentifier;

/**
 * Module that inspects the presumptive format identifications attached to a 
 * Source object, and dispatches the Source to the appropriate format module(s) for
 * feature extraction and, where appropriate, format and profile validation
 * 
 * @author smorrissey
 */
public class FeatureExtractorCommand extends AbstractModule 
implements JHOVE2Command {

	/** Directory module version identifier. */
	public static final String VERSION = "0.0.1";

	/** Directory module release date. */
	public static final String RELEASE = "2009-09-16";

	/** Directory module rights statement. */
	public static final String RIGHTS = "Copyright 2009 by The Regents of the University of California, "
		+ "Ithaka Harbors, Inc., and The Board of Trustees of the Leland "
		+ "Stanford Junior University. "
		+ "Available under the terms of the BSD license.";

	/** Dispatch map. 
	 * 	Maps from unique identifiers to Spring bean names for the modules
	 *  associated with the formats.*/
	private static ConcurrentMap<String, String> dispatchMap;

	/** map from JHOVE2 format ids to bean name for format*/
	private static ConcurrentMap<String, String> jhoveIdToBeanName;
	/**
	 * Constructor
	 */
	public FeatureExtractorCommand(){
		this(VERSION, RELEASE, RIGHTS);
	}

	/**
	 * Constructor
	 * @param version Version of this module
	 * @param release Release date of module
	 * @param rights  Rights statement for module
	 */
	public FeatureExtractorCommand(String version, String release, String rights) {
		super(version, release, rights);
	}

	/**
	 * Maps from Source FormatIdentifications to the appropriate JHOVE2 format
	 * modules (if one exists), and invokes the modules to extract format features
	 * of the format instance
	 * @param source Source with FormatIdentifications
	 * @param jhove2 JHOVE2 framework object
	 * @throws JHOVE2Exception
	 * @see org.jhove2.core.JHOVE2Command#execute(org.jhove2.core.source.Source, org.jhove2.core.JHOVE2)
	 */
	@Override
	public void execute(Source source, JHOVE2 jhove2) throws JHOVE2Exception {
		source.addModule(this);
		// Sometimes more than one format identification will match to the same 
		// JHOVE2 format; eliminate duplicates from list of JHOVE2 format modules
		// to be run, then dispatch to each format module
		HashMap<I8R, Format> jhoveFormats = new HashMap<I8R, Format>();
		for (FormatIdentification fid : source.getPresumptiveFormatIdentifications()){
			// make sure identification found a match for format in JHOVE2 namespace
			if (fid.getJhove2Identification() != null) {
				// use the JHOVE2 format id to get bean name for format in Spring config file
				String beanName = getJhoveIdToBeanName().get(fid.getJhove2Identification().getValue());		
				if (beanName != null){
					Format format = Configure.getReportable(Format.class, beanName);	
					jhoveFormats.put(fid.getJhove2Identification(), format);
				}
			}
		}
		// more than one JHOVE2 format might map to the same format module,
		// so we will keep track of the modules we run so as not to run them
		// more than once per Source
		TreeSet<I8R> visitedModules = new TreeSet<I8R>();
		// now invoke the format module
		for (I8R id:jhoveFormats.keySet()){
			Format format = jhoveFormats.get(id);			
			Module module = this.getModuleFromIdentifier(id);
			if (module == null){
				BaseFormatModule bFormatModule = new BaseFormatModule();
				String[]parms = new String[]{id.getValue()};
				bFormatModule.setModuleNotFoundMessage(
						new Message(Severity.ERROR,
								Context.PROCESS,
								"org.jhove2.module.format.FeatureExtractorCommand.moduleNotFoundMessage",
								(Object[])parms));
				bFormatModule.setFormat(format);
				source.addModule(bFormatModule);
			}
			else if (!(module instanceof FormatModule)){
				BaseFormatModule bFormatModule = new BaseFormatModule();
				String[]parms = new String[]{id.getValue()};
				bFormatModule.setModuleNotFormatModuleMessage(
						new Message(Severity.ERROR,
								Context.PROCESS,
								"org.jhove2.module.format.FeatureExtractorCommand.moduleNotFormatModuleMessage",
								(Object[])parms));
				bFormatModule.setFormat(format);
				source.addModule(bFormatModule);
			}
			else {
				FormatModule formatModule = (FormatModule)module;
				if (formatModule.getFormat()==null){
					formatModule.setFormat(format);
				}
				if (!visitedModules.contains(formatModule.getReportableIdentifier())){
					visitedModules.add(formatModule.getReportableIdentifier());
					formatModule.execute(source, jhove2);
				}// end if we have not already run this module
			}// end if this is a non-null instance of a FormatModule
		}// end for each JHOVE2 format

		return;
	}// end method

	/**
	 * Loads static map from JHOVE2 identifier to module from properties file if necessary,
	 * then returns map
	 * @return map from JHOVE2 identifier to module from properties if necessary
	 * @throws JHOVE2Exception
	 */
	public static ConcurrentMap<String, String> getDispatchMap () throws JHOVE2Exception{
		if (dispatchMap==null){
			dispatchMap = new ConcurrentHashMap<String, String>();
			Properties props = Configure.getProperties("DispatchMap");
			Set<String> keys = props.stringPropertyNames();
			for (String key : keys) {
				String value = props.getProperty(key);
				dispatchMap.put(key, value);
			}
		}
		return dispatchMap;
	}

	/**
	 * Determine module to be invoked from format identifier
	 * @param identifier JHOVE2 identifier for format
	 * @return Module mapped to the JHOVE2 format identifier, or null if no 
	 *          module has been mapped to the JHOVE2 identifier
	 * @throws JHOVE2Exception
	 */
	public Module getModuleFromIdentifier(I8R identifier) throws JHOVE2Exception{
		Module module = null;
		String name = getDispatchMap().get(identifier.getValue());
		if (name != null) {
			module = Configure.getReportable(Module.class, name);
		} 
		return module;
	}

	/**
	 * Loads map from JHOVE2 format ID to bean name for that format from properties file if 
	 * map is not already populated and returns map; otherwise just returns map
	 * @return map from JHOVE2 format ID to bean name for that format 
	 * @throws JHOVE2Exception
	 */
	public static ConcurrentMap<String,String> getJhoveIdToBeanName () throws JHOVE2Exception{
		if (FeatureExtractorCommand.jhoveIdToBeanName==null){
			ConcurrentHashMap<String, String> map = new ConcurrentHashMap<String, String>();
			Properties props = null;
			props = Configure.getProperties(DroidIdentifier.JHOVE2BEANMAP_BEANNAME);
			Set<String> keys = props.stringPropertyNames();
			for (String key : keys) {
				String value = props.getProperty(key);
				map.put(key, value);
			}
			FeatureExtractorCommand.jhoveIdToBeanName = map;
		}
		return FeatureExtractorCommand.jhoveIdToBeanName;
	}

}

