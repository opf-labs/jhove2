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
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.Message;
import org.jhove2.core.TimerInfo;
import org.jhove2.core.Message.Context;
import org.jhove2.core.Message.Severity;
import org.jhove2.core.format.Format;
import org.jhove2.core.io.Input;
import org.jhove2.core.source.Source;
import org.jhove2.module.AbstractModule;

/**
 * Base JHOVE2 format module.
 * Modules for specific formats/format families will inherit from this module.
 * Any single format module that inherits from this class can be invoked as
 * a Command.
 * 
 * @author mstrong, slabrams, smorrissey
 */
public class BaseFormatModule
	extends AbstractModule
	implements FormatModule
{
	/** Directory module version identifier. */
	public static final String VERSION = "2.0.0";

	/** Directory module release date. */
	public static final String RELEASE = "2010-09-10";

	/** Directory module rights statement. */
	public static final String RIGHTS = "Copyright 2010 by The Regents of the University of California, "
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
	
	/** Module does not implement the {@link org.jhove2.module.format.Validator} interface message. */
	protected Message moduleDoesNotImplementValidatorInterfaceMessage;
	
	/**
	 * Instantiate a new <code>BaseFormatModule</code>.
	 */
	public BaseFormatModule(){
		this(VERSION, RELEASE, RIGHTS, null);
	}

	/**
	 * Instantiate a new <code>BaseFormatModule</code>
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
		this(version, release, rights, Scope.Specific, format);
	}
	
	/**
	 * Instantiate a new <code>BaseFormatModule</code>
	 * @param version
	 *            Format module version identifier in three-part form: "M.N.P"
	 * @param release
	 *            Format module release date in ISO 8601 format: "YYYY-MM-DD"
	 * @param rights
	 *            Format module rights statement
	 * @param scope
	 *            Format scope: generic or specific (to a source unit)
	 * @param format
	 *            Format module format
	 */
	public BaseFormatModule(String version, String release, String rights,
			                Scope scope, Format format)
	{
		super(version, release, rights, scope);
		this.format   = format;
		this.profiles = new ArrayList<FormatProfile>();
	}

	/**
	 * Invoke the parsing of the {@link org.jhove2.core.source.Source} unit's
	 * {@link org.jhove2.core.io.Input} and validate all registered profiles.
	 * @param jhove2 JHOVE2 framework
	 * @param source Source to be parsed
	 * @param input  Source input
	 * @throws JHOVE2Exception
	 * @see org.jhove2.module.Command#execute(org.jhove2.core.JHOVE2, org.jhove2.core.source.Source, org.jhove2.core.io.Input)
	 */
	@Override
	public void invoke(JHOVE2 jhove2, Source source, Input input)
	   throws JHOVE2Exception
	{
        this.timerInfo.setStartTime();
        source.addModule(this);
		try {
			this.parse(jhove2, source, input);
			if (this instanceof Validator) {
				((Validator) this).validate(jhove2, source, input);
			}
			else {
			    this.moduleDoesNotImplementValidatorInterfaceMessage =
			        new Message(Severity.INFO, Context.PROCESS,
			                "org.jhove2.module.format.BaseFormatModule.moduleDoesNotImplementValidatorInterface",
			                jhove2.getConfigInfo());
			}
			List<FormatProfile> profiles = this.getProfiles();
			if (profiles.size() > 0) {
				for (FormatProfile profile : profiles) {
					if (profile instanceof Validator) {
					    profile.setFormatModule(this);
					    
						TimerInfo timer = profile.getTimerInfo();
						timer.setStartTime();
						
						((Validator) profile).validate(jhove2, source, input);
						
						timer.setEndTime();
					}
				}
			}
		}
		catch (EOFException e) {
			this.addExceptionMessageToSource(jhove2, source, e);
		}
		catch (IOException e) {
			this.addExceptionMessageToSource(jhove2, source, e);
		}
		finally {
		    this.timerInfo.setEndTime();
		}
	}

    /** Parse the formatted {@link org.jhove2.core.source.Source} unit's
     * {@link org.jhove2.core.io.Input}.
     * @param jhove2 JHOVE2 framework
     * @param source Source unit
     * @param input  Source input
     */
    @Override
    public long parse(JHOVE2 jhove2, Source source, Input input)
        throws EOFException, IOException, JHOVE2Exception
    {
        return 0;
    }
    	
	protected void addExceptionMessageToSource(JHOVE2 jhove2, Source source, Exception e)
	    throws JHOVE2Exception
	{
		String exceptionType = e.getClass().getSimpleName();
		String eMessage = e.getLocalizedMessage();
		if (eMessage == null) {
			eMessage = "";
		}
		String[] messageText = {exceptionType, eMessage};
		source.addMessage(new Message(Severity.ERROR,
                Context.PROCESS,
                "org.jhove2.module.format.BaseFormatModule.IOExceptionOnParseMessage",
                messageText, jhove2.getConfigInfo()));
		
	}
	
	/**
	 * Get format module format.
	 * @return Format module format
	 * @see org.jhove2.module.format.FormatProfile#getFormat()
	 */
	@Override
	public Format getFormat() {
		return this.format;
	}
	
	/** Set the base format.
	 * @param format Base format
	 */
	@Override
	public void setFormat(Format format){
		this.format = format;
	}

	/**
	 * Get format module format profiles.
	 * @return Format module format profiles
	 * @see org.jhove2.module.format.FormatModule#getProfiles()
	 */
	@Override
	public List<FormatProfile> getProfiles() {
		return this.profiles;
	}

	/**
	 * Set format module format profile. 
	 * @param profiles
	 *            List of FormatProfiles
	 * @see org.jhove2.module.format.FormatModule#setProfiles(List)
	 */
	@Override
	public void setProfiles(List<FormatProfile> profiles) {
		this.profiles = profiles;
	}

	/** Get module does not implement the {@link org.jhove2.module.format.Validator} interface message
	 * @return Message
	 */
	@ReportableProperty(order=17, value="Module does not implement the Validator interface message.")
	public Message getModuleDoesNotImplementValidatorInterfaceMessage() {
	    return this.moduleDoesNotImplementValidatorInterfaceMessage;
	}

	/**
	 * Accessor for moduleNotFoundMessage
	 * @return moduleNotFoundMessage
	 */
	@ReportableProperty(order = 15, value = "Format module not found message.")
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
	 * @return Message
	 */
	@ReportableProperty(order = 16, value = "Module is not format module.")
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
