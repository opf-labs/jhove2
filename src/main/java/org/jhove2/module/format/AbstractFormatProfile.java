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

import static com.sleepycat.persist.model.DeleteAction.NULLIFY;
import static com.sleepycat.persist.model.Relationship.MANY_TO_ONE;

import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.format.Format;
import org.jhove2.module.AbstractModule;
import org.jhove2.persist.FormatProfileAccessor;

import com.sleepycat.persist.model.Persistent;
import com.sleepycat.persist.model.SecondaryKey;

/**
 * Abstract JHOVE2 format profile, which is {@link org.jhove2.module.Module}
 * that models a specific format in a format family.
 * 
 * @author mstrong, slabrams, smorrissey
 */
@Persistent
public abstract class AbstractFormatProfile
	extends AbstractModule
	implements FormatProfile
{
    /** Format profile coverage. */
    protected Coverage coverage;
    
	/** Format profile format. */
	protected Format format;
	
	/** Format profile validation status. */
	protected Validity isValid;

	/** Format profile format module. */
	@SecondaryKey(relate=MANY_TO_ONE, relatedEntity=AbstractModule.class,
			onRelatedEntityDelete=NULLIFY)
	protected Long formatModuleId;
	

	/**
	 * Instantiate a new <code>AbstractFormatProfile</code>.
	 * 
	 * @param version
	 *            Format profile version identifier in three-part form: "M.N.P"
	 * @param release
	 *            Format profile release date in ISO 8601 format: "YYYY-MM-DD"
	 * @param rights
	 *            Format profile rights statement
	 * @param format
	 *            Format profile format
	 * @param formatProfileAccessor 
	 * 			 Format profile persistence manager
	 */
	public AbstractFormatProfile(String version, String release, String rights,
			                     Format format, FormatProfileAccessor formatProfileAccessor) 
   {
		super(version, release, rights, Scope.Specific, formatProfileAccessor);
		this.format   = format;
        this.isValid = Validity.Undetermined;
		this.moduleAccessor = formatProfileAccessor;
	}

	/**
	 *  Instantiate a new <code>AbstractFormatProfile</code>.
	 * @param format 
	 *            Format profile format
	 * @param formatProfileAccessor 
	 * 			 Format profile persistence manager
	 */
	private AbstractFormatProfile(Format format, 
			FormatProfileAccessor formatProfileAccessor){
		this(null, null, null, format, formatProfileAccessor);
	}

	/**
	  Instantiate a new <code>AbstractFormatProfile</code>.
	 */
	@SuppressWarnings("unused")
	private AbstractFormatProfile(){
		this(null, null);
	}

    /** Get validation coverage.
     * @return Validation coverage
     */
    @Override
    public Coverage getCoverage() {
        return this.coverage;
    }
 
	/**
	 * Get format profile format.
	 * 
	 * @return Format profile format
	 * @see org.jhove2.module.format.FormatProfile#getFormat()
	 */
	@Override
	public Format getFormat() {
		return this.format;
	}
	   
    /**
     * Get validation status.
     * 
     * @return Validation status
     */
    @Override
    public Validity isValid() {
        return this.isValid;
    }
    
    /** Set format profile validation coverage.
     * @param coverage Format profile validation coverage
     */
    public void setCoverage(Coverage coverage) {
        this.coverage = coverage;
    }
    
    /** Set format profile format.
     * @param format Format profile format
     */
    @Override
    public void setFormat(Format format) {
        this.format = format;
    }
    
	/**
	 * Set format profile format module.
	 * 
	 * @param module
	 *            Format module
	 * @see org.jhove2.module.format.FormatProfile#setFormatModule(org.jhove2.module.format.FormatModule)
	 */
	@Override
	public FormatProfile setFormatModule(FormatModule formatModule)
			throws JHOVE2Exception {
		if (this.getModuleAccessor()==null){
			throw new JHOVE2Exception(" FormatModuleAccessor is null");
		}
		FormatProfileAccessor fpa = 
			(FormatProfileAccessor)this.getModuleAccessor();
		return fpa.setFormatModule(this, formatModule);	
	}

	@Override
	public FormatModule getFormatModule()  throws JHOVE2Exception{
		if (this.getModuleAccessor()==null){
			throw new JHOVE2Exception(" FormatModuleAccessor is null");
		}
		FormatProfileAccessor fpa = 
			(FormatProfileAccessor)this.getModuleAccessor();
		return fpa.getFormatModule(this);
	}

	@Override
	public Long getFormatModuleId() {
		return formatModuleId;
	}

	@Override
	public void setFormatModuleId(Long formatModuleId) throws JHOVE2Exception {
		Long oldId = this.formatModuleId;
		this.formatModuleId = formatModuleId;
		this.getModuleAccessor().verifyNewParentModuleId(this, oldId, formatModuleId);
	}
}
