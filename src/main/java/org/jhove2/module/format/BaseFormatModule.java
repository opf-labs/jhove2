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

import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.Format;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.Message;
import org.jhove2.core.source.Source;
import org.jhove2.module.AbstractModule;

/**
 * Base JHOVE2 format module.
 * Modules for specific formats/format families will inherit from this module.
 * Any single format module that inherits from this class can be invoked as
 * a JHOVE2Command.
 * 
 * @author mstrong, slabrams, smorrissey
 */
public class BaseFormatModule
	extends AbstractModule
	implements FormatModule
{
	/** Directory module version identifier. */
	public static final String VERSION = "0.0.1";

	/** Directory module release date. */
	public static final String RELEASE = "2009-09-16";

	/** Directory module rights statement. */
	public static final String RIGHTS = "Copyright 2009 by The Regents of the University of California, "
			+ "Ithaka Harbors, Inc., and The Board of Trustees of the Leland "
			+ "Stanford Junior University. "
			+ "Available under the terms of the BSD license.";

	/** Format module format. */
	protected Format format;

	/** Format module format profiles. */
	protected List<FormatProfile> profiles;
	
	/** Module not found Error Message
	 * Created when no map exists from format name to format module */
	protected Message moduleNotFoundMessage;

	/** Module not Format Module Error message
	 *  Created when map exists from format name to module 
	 *    returns a non-format module*/
	protected Message moduleNotFormatModuleMessage;
	
	
	/**
	 * Constructor
	 */
	public BaseFormatModule(){
		this(VERSION, RELEASE, RIGHTS, null);
	}

	/**
	 * Instantiate a new <code>BaseFormatModule</code>
	 * 
	 * @param version
	 *            Format module version identifier in three-part form: "M.N.P"
	 * @param release
	 *            Format module release date in ISO 8601 format: "YYYY-MM-DD"
	 * @param rights
	 *            Format module rights statement
	 * @param format
	 *            Format module format
	 */
	public BaseFormatModule(String version, String release, String rights,
			Format format) {
		super(version, release, rights);
		this.format   = format;
		this.profiles = new ArrayList<FormatProfile>();
	}

	/**
	 * Parses format instance
	 * If format can validate, validates instance
	 * If format has profiles and those profiles can validate, validates profiles
	 * @param Source to be parsed
	 * @param JHOVE2 framework for callbacks
	 * @throws JHOVE2Exception
	 * @see org.jhove2.core.JHOVE2Command#execute(org.jhove2.core.source.Source, org.jhove2.core.JHOVE2)
	 */
	@Override
	public void execute(JHOVE2 jhove2, Source source)
	   throws JHOVE2Exception {
		source.addModule(this);
		this.getTimerInfo().setStartTime();
		try {
			this.parse(jhove2, source);
			if (this instanceof Validator) {
				((Validator) this).validate(jhove2, source);
			}
			List<FormatProfile> profiles = this.getProfiles();
			if (profiles.size() > 0) {
				for (FormatProfile profile : profiles) {
					if (profile instanceof Validator) {
						profile.getTimerInfo().setStartTime();
						((Validator) profile).validate(jhove2, source);
						profile.getTimerInfo().setEndTime();
					}
				}
			}
		} catch (EOFException e) {
			throw new JHOVE2Exception("EOFException",e);
		} catch (IOException e) {
			throw new JHOVE2Exception("IOException",e);
		}
		this.getTimerInfo().setEndTime();
		
		return;
	}
	/**
	 * Get format module format.
	 * 
	 * @return Format module format
	 * @see org.jhove2.module.format.FormatProfile#getFormat()
	 */
	@Override
	public Format getFormat() {
		return this.format;
	}
	
	public void setFormat(Format format){
		this.format = format;
	}

	/**
	 * Get format module format profiles.
	 * 
	 * @return Format module format profiles
	 * @see org.jhove2.module.format.FormatModule#getProfiles()
	 */
	@Override
	public List<FormatProfile> getProfiles() {
		return this.profiles;
	}

	/**
	 * Set format module format profile.
	 * 
	 * @param profile
	 *            Format module format profile
	 * @see org.jhove2.module.format.FormatModule#setProfile(org.jhove2.module.format.FormatProfile)
	 */
	@Override
	public void setProfile(FormatProfile profile) {
		profile.setFormatModule(this);
		this.profiles.add(profile);
	}

	@Override
	public long parse(JHOVE2 jhove2, Source source) throws EOFException,
			IOException, JHOVE2Exception {
		return 0;
	}

	/**
	 * Accessor for moduleNotFoundMessage
	 * @return moduleNotFoundMessage
	 */
	@ReportableProperty(order = 15, value = "Format Module Not Found Error Message")
	public Message getModuleNotFoundMessage() {
		return moduleNotFoundMessage;
	}
	/**
	 * Mutator for moduleNotFoundMessage
	 * @param moduleNotFoundMessage
	 */
	public void setModuleNotFoundMessage(Message moduleNotFoundMessage) {
		this.moduleNotFoundMessage = moduleNotFoundMessage;
	}
	/**
	 * Accessor for moduleNotFormatModuleMessage
	 * @return
	 */
	@ReportableProperty(order = 16, value = "Module returned is not Format Module Error Message")
	public Message getModuleNotFormatModuleMessage() {
		return moduleNotFormatModuleMessage;
	}
	/**
	 * Mutator for moduleNotFormatModuleMessage
	 * @param moduleNotFormatModuleMessage
	 */
	public void setModuleNotFormatModuleMessage(Message moduleNotFormatModuleMessage) {
		this.moduleNotFormatModuleMessage = moduleNotFormatModuleMessage;
	}
}
