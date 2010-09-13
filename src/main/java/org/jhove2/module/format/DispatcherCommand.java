/**
 * JHOVE2 - Next-generation architecture for format-aware characterization
 * 
 * Copyright (c) 2009 by The Regents of the University of California, Ithaka
 * Harbors, Inc., and The Board of Trustees of the Leland Stanford Junior
 * University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * o Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * 
 * o Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * o Neither the name of the University of California/California Digital
 * Library, Ithaka Harbors/Portico, or Stanford University, nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
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
import java.util.TreeSet;

import org.jhove2.core.I8R;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.Message;
import org.jhove2.core.Message.Context;
import org.jhove2.core.Message.Severity;
import org.jhove2.core.format.Format;
import org.jhove2.core.format.FormatFactory;
import org.jhove2.core.format.FormatIdentification;
import org.jhove2.core.io.Input;
import org.jhove2.core.source.Source;
import org.jhove2.module.AbstractCommand;
import org.jhove2.module.Module;

/**
 * Module that inspects the presumptive format identifications attached to a
 * Source object, and dispatches the Source to the appropriate format module(s)
 * for feature extraction and, where appropriate, format and profile validation
 * 
 * @author smorrissey, rnanders
 */
public class DispatcherCommand
    extends AbstractCommand
{
    /** Module version identifier. */
    public static final String VERSION = "2.0.0";

    /** Module release date. */
    public static final String RELEASE = "2010-09-10";

    /** Module rights statement. */
    public static final String RIGHTS = "Copyright 2009 by The Regents of the University of California, "
            + "Ithaka Harbors, Inc., and The Board of Trustees of the Leland "
            + "Stanford Junior University. "
            + "Available under the terms of the BSD license.";

    protected FormatFactory formatFactory;
    
    protected FormatModuleFactory formatModuleFactory;

    /**
     * Instantiate a new <code>DispatcherCommand</code>.
     */
    public DispatcherCommand() {
        super(VERSION, RELEASE, RIGHTS, Scope.Generic);
    }

    /**
     * Maps from Source FormatIdentifications to the appropriate JHOVE2 format
     * modules (if one exists), and invokes the modules to extract format
     * features of the format instance
     * 
     * @param jhove2
     *            JHOVE2 framework object
     * @param source
     *            Source with FormatIdentifications
     * @param input
     *            Source input
     * @throws JHOVE2Exception
     * @see org.jhove2.module.Command#execute(org.jhove2.core.JHOVE2,
     *      org.jhove2.core.source.Source)
     */
    @Override
    public void execute(JHOVE2 jhove2, Source source, Input input)
        throws JHOVE2Exception
    {
        /*
         * Sometimes more than one format identifier will match to the same
         * JHOVE2 format; eliminate duplicates from list of JHOVE2 format
         * modules to be run, then dispatch to each format module.
         */
        HashMap<I8R, Format> jhoveFormats = new HashMap<I8R, Format>();
        for (FormatIdentification fid : source.getPresumptiveFormats()) {
            /*
             * Make sure identifier found a match for format in the JHOVE2
             * namespace.
             */
            if (fid.getJHOVE2Identifier() != null) {
            	Format format = this.getFormatFactory().getFormat(fid.getJHOVE2Identifier());
            	if (format != null){
            		jhoveFormats.put(fid.getJHOVE2Identifier(), format);
            	}
            }
        }
        
        /*
         * More than one JHOVE2 format might map to the same format module, so
         * we will keep track of the modules we run so as not to run them more
         * than once per Source.
         */
        TreeSet<I8R> visitedModules = new TreeSet<I8R>();
        /* now invoke the format module. */
        for (I8R id : jhoveFormats.keySet()) {
            Format format = jhoveFormats.get(id);
            Module module = this.getFormatModuleFactory()
            	.getFormatModule(id);
            if (module == null) {
                BaseFormatModule bFormatModule = new BaseFormatModule();
                String[] parms = new String[] { id.getValue() };
                bFormatModule.setModuleNotFoundMessage(new Message(
                	Severity.ERROR,
                	Context.PROCESS,
                	"org.jhove2.module.format.DispatcherCommand.moduleNotFoundMessage",
                	(Object[]) parms, jhove2.getConfigInfo()));
                bFormatModule.setFormat(format);
                source.addModule(bFormatModule);
            }
            else if (!(module instanceof FormatModule)) {
                BaseFormatModule bFormatModule = new BaseFormatModule();
                String[] parms = new String[] { id.getValue() };
                bFormatModule.setModuleNotFormatModuleMessage(new Message(
                	Severity.ERROR,
                	Context.PROCESS,
                	"org.jhove2.module.format.DispatcherCommand.moduleNotFormatModuleMessage",
                	(Object[]) parms, jhove2.getConfigInfo()));
                bFormatModule.setFormat(format);
                source.addModule(bFormatModule);
            }
            else {
                FormatModule formatModule = (FormatModule) module;
                if (formatModule.getFormat() == null) {
                    formatModule.setFormat(format);
                }
                if (!visitedModules.contains(formatModule
                        .getReportableIdentifier())) {
                    visitedModules.add(formatModule.getReportableIdentifier());
                    formatModule.invoke(jhove2, source, input);
                }
            }
        }
    }

	/**
	 * @return the formatFactory
	 */
	public FormatFactory getFormatFactory() {
		return formatFactory;
	}

	/**
	 * @param formatFactory the formatFactory to set
	 */
	public void setFormatFactory(FormatFactory formatFactory) {
		this.formatFactory = formatFactory;
	}

	/**
	 * @return the formatModuleFactory
	 */
	public FormatModuleFactory getFormatModuleFactory() {
		return formatModuleFactory;
	}

	/**
	 * @param formatModuleFactory the formatModuleFactory to set
	 */
	public void setFormatModuleFactory(FormatModuleFactory formatModuleFactory) {
		this.formatModuleFactory = formatModuleFactory;
	}
}
