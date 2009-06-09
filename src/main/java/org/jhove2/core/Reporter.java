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

import java.util.Set;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.message.Message;

/** Interface for all JHOVE2 reporters.  A reportable is a named set of
 * properties and messageCollection.  A reportable has two names, a short descriptive
 * name based on the non-package-qualified class name and a unique formal
 * identifier in the JHOVE2 namespace of the form:
 * <code>info:jhove2/qname</code>, where <code>qname</code> is the fully
 * package-qualified class name with all  * periods (.) replaced with slashes
 * (/).
 * 
 * A property is a named, typed value.  Properties are defined by marking
 * their accessor (getter) methods with the
 * {@link org.core.annotation.ReportableProperty} annotation.  Like
 * containers, properties have two names, a short descriptive name based on
 * the accessor method name and a formal identifier in the JHOVE2 namespace
 * of the form: <code>info:jhove2/qname/name</code>, where <code>qname</code>
 * is the reportable qname and <code>name</code> is the property name.
 * 
 * A message is a reportable.
 * 
 * @author mstrong, slabrams
 */
public interface Reporter {	
	/** Reporter capabilties. */
	public enum Capability {
		Assessment,
		Identification,
		MessageDigesting,
		Parsing,
		Validation
	}

	/** Add a reportable message.
	 * @param message Reporter message
	 */
	public void addMessage(Message message);
	
	/** Add reportable messages.
	 * @param messages Reporter messages
	 */
	public void addMessages(Set<Message> messages);
	
	/** Get reportable capabilities.
	 * @return Set of container capabilities
	 */
	@ReportableProperty(value=3, desc="Set of reportable processing " +
			"capabilities.")
	public Set<Capability> getCapabilities();
	
	/** Get reportable messages.
	 * @return Reporter messages
	 */
	@ReportableProperty(value=4, desc="Reporter messages")
	public Set<Message> getMessages();
	
	/** Get number of reportable error messages.
	 * @return Number of reportable error messages.
	 */
	public int getNumErrorMessages();
	
	/** Get number of reportable messages.
	 * @return Number of reportable messages
	 */
	public int getNumMessages();
	
	/** Get reportable identifier in the JHOVE2 namespace of the form:
	 * <code>info:jhove2/name</code>, where name is the fully
	 * package-qualified class name with all periods (.) replaced by
	 * slashes (/).
	 * @return Reporter identifier
	 */
	@ReportableProperty(value=2, desc="Reporter unique identifier in the " +
			"JHOVE2 namespace of the form: info:jhove2/<qname>, where " +
			"<qname> is the fully package-qualified class name with " +
			"periods (.) replaced with slashes (/).")
	public Identifier getReportableIdentifier();
	
	/** Get reportable name.  The name is based on the non-package-qualified
	 * class name.
	 * @return Reporter name
	 */
	@ReportableProperty(value=1, desc="AbstractReporter descriptive name " +
			"based on the non-package-qualified class name.")
	public String getReportableName();
		
	/** Get qualified reportable name.  The name is based on the full package-qualified
	 * class name with all periods (.) replaced with slashes (/).
	 * @return Reporter name
	 */
	public String getQName();
	
	/** Get reportable capability status.
	 * @param capability Reporter capability
	 * @return True if the reportable has the capability
	 */
	public boolean isCapableOf(Capability capability);
	
	/** Reinitialize reportable properties.
	 */
	public void reset();
}
