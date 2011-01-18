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

import java.util.List;
import java.util.Set;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.module.AbstractModule;
import org.jhove2.module.assess.MockReportable.MockEnum;
import org.jhove2.persist.ModuleAccessor;

/**
 * @author rnanders
 *
 */
public class MockModule extends AbstractModule {

    /** Module version identifier. */
    public static final String VERSION = "0.1.0";

    /** Module release date. */
    public static final String RELEASE = "2010-06-16";

    /** Module rights statement. */
    public static final String RIGHTS = "Copyright 2010 by The Board of Trustees of the Leland Stanford Junior University. "
            + "Available under the terms of the BSD license.";

    public MockModule(ModuleAccessor moduleAccessor) {
        super(VERSION, RELEASE, RIGHTS, Scope.Specific, moduleAccessor);
    }
    
    public MockModule() {
        this(null);
    }

    /** a value from an enumeration */
    protected MockEnum mpEV;
    
    /** some text */
    protected String mpString;
    
    /** a long integer */
    protected long mpLong;
    
    /** a true/false value */
    protected boolean mpBoolean;
    
    /** a child object that contains data fields */
    protected MockReportable mpReportable;
    
    /** a list of text strings */
    protected List<String> mpListString;
    
    /** a set of text strings */
    protected Set<String> mpSetString;
    
    /** a list of child objects, each of which contains data fields */
    protected List<MockReportable> mpListReportable;

    /**
     * @return the mpEV
     */
    @ReportableProperty(order = 1, value = "mpEV")
       public MockEnum getMpEV() {
        return mpEV;
    }

    /**
     * @param mpEV the mpEV to set
     */
    public void setMpEV(MockEnum mpEV) {
        this.mpEV = mpEV;
    }

    /**
     * @return the mpString
     */
    @ReportableProperty(order = 2, value = "mpString")
    public String getMpString() {
        return mpString;
    }

    /**
     * @param mpString the mpString to set
     */
    public void setMpString(String mpString) {
        this.mpString = mpString;
    }

    /**
     * @return the mpLong
     */
    @ReportableProperty(order = 3, value = "mpLong")
    public long getMpLong() {
        return mpLong;
    }

    /**
     * @param mpLong the mpLong to set
     */
    public void setMpLong(long mpLong) {
        this.mpLong = mpLong;
    }

    /**
     * @return the mpBoolean
     */
    @ReportableProperty(order = 4, value = "mpBoolean")
        public boolean isMpBoolean() {
        return mpBoolean;
    }

    /**
     * @param mpBoolean the mpBoolean to set
     */
    public void setMpBoolean(boolean mpBoolean) {
        this.mpBoolean = mpBoolean;
    }

    /**
     * @return the mpReportable
     */
    @ReportableProperty(order = 5, value = "mpReportable")
    public MockReportable getMpReportable() {
        return mpReportable;
    }

    /**
     * @param mpReportable the mpReportable to set
     */
    public void setMpReportable(MockReportable mpReportable) {
        this.mpReportable = mpReportable;
    }

    /**
     * @return the mpListString
     */
    @ReportableProperty(order = 6, value = "mpListString")
        public List<String> getMpListString() {
        return mpListString;
    }

    /**
     * @param mpListString the mpListString to set
     */
    public void setMpListString(List<String> mpListString) {
        this.mpListString = mpListString;
    }

    /**
     * @return the mpSetString
     */
    @ReportableProperty(order = 7, value = "mpSetString")
    public Set<String> getMpSetString() {
        return mpSetString;
    }

    /**
     * @param mpSetString the mpSetString to set
     */
    public void setMpSetString(Set<String> mpSetString) {
        this.mpSetString = mpSetString;
    }

    /**
     * @return the mpListReportable
     */
    @ReportableProperty(order = 8, value = "mpListReportable")
    public List<MockReportable> getMpListReportable() {
        return mpListReportable;
    }

    /**
     * @param mpListReportable the mpListReportable to set
     */
    public void setMpListReportable(List<MockReportable> mpListReportable) {
        this.mpListReportable = mpListReportable;
    }
    
    
    
    
    
    
    
    
    
}
