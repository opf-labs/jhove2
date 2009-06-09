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

package org.jhove2.core;

import java.util.ArrayList;
import java.util.List;


/** An abstract JHOVE2 format component, in other words, a component that
 * models a format.
 * 
 * @author mstrong, slabrams
 */
public abstract class AbstractFormatComponent
	extends AbstractComponent
	implements FormatComponent
{
	/** Component format. */
	protected Format format;
	
	/** Format component profiles. */
	protected List<FormatProfileComponent> profiles;
	
	/** Instantiate a new <code>AbstractFormatComponent</code>.
	 * @param version Format component version identifier
	 * @param date    Format component release date
	 * @param stage   Format component development stage
	 * @param format  Format component format
	 */
	public AbstractFormatComponent(String version, String date, Stage stage,
			                       Format format) {
		super(version, date, stage);

		this.format   = format;
		this.profiles = new ArrayList<FormatProfileComponent>();
	}

	/** Add a format component profile.  This implicitly sets this format
	 * component as the base format component of the profile.
	 * @param profile Component format profile
	 * @see org.jhove2.core.FormatComponent#addProfile()
	 */
	@Override
	public void setProfiles(List<FormatProfileComponent> profile) {

		for (FormatProfileComponent pfc: profile) {
			pfc.setBase(this);
			this.profiles.add(pfc);
		}
	}

	/** Get component format.
	 * @return Component format
	 * @see org.jhove2.core.FormatComponent#getFormat()
	 */
	@Override
	public Format getFormat() {
		return this.format;
	}
	
	/** Get format component profiles.
	 * @return List of format component profiles
	 * @see org.jhove2.core.FormatComponent#getProfiles()
	 */
	@Override
	public List<FormatProfileComponent> getProfiles() {
		return this.profiles;
	}
}
