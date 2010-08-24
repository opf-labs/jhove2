/**
 * JHOVE2 - Next-generation architecture for format-aware characterization
 * <p>
 * Copyright (c) 2009 by The Regents of the University of California, Ithaka
 * Harbors, Inc., and The Board of Trustees of the Leland Stanford Junior
 * University. All rights reserved.
 * </p>
 * <p>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * </p>
 * <ul>
 * <li>Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.</li>
 * <li>Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.</li>
 * <li>Neither the name of the University of California/California Digital
 * Library, Ithaka Harbors/Portico, or Stanford University, nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.</li>
 * </ul>
 * <p>
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
 * </p>
 */

package org.jhove2.module.assess;

import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.TimerInfo;
import org.jhove2.core.source.Source;
import org.jhove2.module.AbstractCommand;

/**
 * Implements the {@link org.jhove2.module.Command Command} interface
 * to execute assessment on a {@link org.jhove2.core.source.Source Source} unit
 * by creating and running a module that implements the {@link Assessor} interface
 * @author rnanders
 */
public class AssessorCommand extends AbstractCommand {
    /** AssessorCommand module version identifier. */
    public static final String VERSION = "0.1.0";

    /** AssessorCommand module release date. */
    public static final String RELEASE = "2010-05-19";

    /** AssessorCommand module rights statement. */
    public static final String RIGHTS = "Copyright 2010 by The Regents of the University of California, "
            + "Ithaka Harbors, Inc., and The Board of Trustees of the Leland "
            + "Stanford Junior University. "
            + "Available under the terms of the BSD license.";

    /**
     * Instantiate a new {@link AssessorCommand} instance.
     */
    public AssessorCommand() {
        super(VERSION, RELEASE, RIGHTS, Scope.Generic);
    }

    /** The factory instance used to create an assessment module that implements the {@link Assessor} interface */
    protected AssessorFactory assessorFactory;

    /**
     * Assess the reportable properties of the source unit.
     * 
     * @param jhove2
     *            JHOVE2 framework object
     * @param source
     *            Source to be identified
     * @throws JHOVE2Exception
     */
    @Override
    public void execute(JHOVE2 jhove2, Source source) throws JHOVE2Exception {
        try {
            Assessor assessor = this.getAssessorFactory().getAssessor();
            TimerInfo timer = assessor.getTimerInfo();
            timer.setStartTime();
            try {
                /* Register all assessment modules. */
                source.addModule(assessor);
                /* Assess the reportable properties. */
                assessor.assess(jhove2, source);
            }
            finally {
                timer.setEndTime();
            }
        }
        catch (Exception e) {
            throw new JHOVE2Exception("failed to execute assessor", e);
        }
        return;
    }

    /**
     * Gets the {@link #assessorFactory}
     * 
     * @return assessorFactory
     */
    public AssessorFactory getAssessorFactory() {
        return assessorFactory;
    }

    /**
     * Sets the {@link #assessorFactory}
     * 
     * @param assessorFactory
     *            the AssessorFactory instance assigned to the Command
     */
    public void setAssessorFactory(AssessorFactory assessorFactory) {
        this.assessorFactory = assessorFactory;
    }
}
