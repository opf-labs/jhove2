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

import java.util.ArrayList;
import java.util.List;

import org.jhove2.core.Format;
import org.jhove2.module.AbstractModule;

/**
 * Abstract JHOVE2 format module.
 * 
 * @author mstrong, slabrams
 */
public abstract class AbstractFormatModule extends AbstractModule implements
		FormatModule {
	/** Format module format. */
	protected Format format;

	/** Format module format profiles. */
	protected List<FormatProfile> profiles;

	/**
	 * Instantiate a new <code>AbstractFormatModule</code>
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
	public AbstractFormatModule(String version, String release, String rights,
			Format format) {
		super(version, release, rights);

		this.format = format;
		this.profiles = new ArrayList<FormatProfile>();
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

	/**
	 * Get format module format profiles.
	 * 
	 * @return Format module format rofiles
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
}
