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
package org.jhove2.module.format.xml;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.reportable.AbstractReportable;

import com.sleepycat.persist.model.Persistent;

/**
 * This class is used to hold information about a <i>document scope declaration</i>
 * discovered during parsing of an XML instance.
 * <p>
 * An <i>external identifier</i> (if present) is either a <i>system identifier</i> or a
 * <i>public identifier</i>, either of which must contain a <i>system
 * literal</i> (e.g. a URI reference), that points to a separate storage object
 * (such as a file or a web resource). A <i>public identifier</i> also includes
 * a public resource name that may be used via a xml catalog (or similar) lookup
 * mechanism to resolve the entity reference.
 * </p>
 * @author rnanders 
 */
@Persistent
public class DTD extends AbstractReportable {
	
	/** The document scope name. */
	protected String name;
	
	/** The declared public identifier for the external DTD subset, or null if none was declared. */
	protected String publicID;
	
	/** The declared system identifier for the external DTD subset, or null if none was declared. */
	protected String systemID;
	
	protected DTD(){
		super();
	}
	/**
	 * Gets the document scope name.
	 * 
	 * @return the document scope name
	 */
	@ReportableProperty(order = 1, value = "DTD Name")
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the DTD public identifier.
	 * 
	 * @return the DTD public identifier
	 */
	@ReportableProperty(order = 2, value = "DTD PublicID")
	public String getPublicID() {
		return publicID;
	}
	
	/**
	 * Gets the DTD system identifier.
	 * 
	 * @return the DTD system identifier
	 */
	@ReportableProperty(order = 3, value = "DTD SystemID")
	public String getSystemID() {
		return systemID;
	}
}
