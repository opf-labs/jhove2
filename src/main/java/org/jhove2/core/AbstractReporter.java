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

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.jhove2.core.message.Message;
import org.jhove2.core.message.Message.Severity;

/** An abstract JHOVE2 reportable.  A reportable is a named set of properties
 * and messageCollection.  A reportable has two names, a short descriptive name based
 * on the non-package-qualified class name and a unique formal identifier in
 * the JHOVE2 namespace of the form: <code>info:jhove2/qname</code>, where
 * <code>qname</code> is the fully package-qualified class name with all
 * periods (.) replaced with slashes (/).
 * 
 * A property is a named, typed value.  Properties are defined by marking
 * their accessor (getter) methods with the
 * {@link org.core.annotation.ReportableProperty} annotation.  Like reportable
 * containers, properties have two names, a short descriptive name based on
 * the accessor method name and a formal identifier in the JHOVE2 namespace
 * of the form: <code>info:jhove2/qname/name</code>, where <code>qname</code>
 * is the reportable qname and <code>name</code> is the property name.
 * 
 * A message is a reportable.
 * 
 * @author mstrong, slabrams
 */
public abstract class AbstractReporter
	implements Reporter
{
	/** Reporter capabilities. */
	protected Set<Capability> capabilities;
	
	/** Reporter message manager. */
	protected Set<Message> messages;
	
	/** Number of reportable error messages. */
	protected int numErrorMessages;
	
	/** Reporter identifier in the JHOVE2 namespace, of the form:
	 * <code>info:jhove2/qname</code>, where <code>qname</code> is the
	 * full package-qualified class name with all periods (.) replaced
	 * with slashes (/). */
	protected Identifier reportableIdentifier;
	
	/** Reporter name, based on the qName. */
	protected String reportableName;
	
	/** Reporter packaed qualified name, based on the full package-qualified
	 * class name with all periods (.) replaced with slashes (/).
	 */
	protected String qName;
	
	/** Instantiate a new <code>AbstractReporter</code>.
	 */
	public AbstractReporter() {
		/* Retrieve the reportable name from its fully package-qualified
        * class name.
        */
        Class<? extends AbstractReporter> c = this.getClass();
        this.reportableName  = c.getSimpleName();
        this.qName = c.getName().replace('.', '/');
        this.reportableIdentifier =
        	new Identifier(Identifier.JHOVE2_PREFIX +
                           Identifier.JHOVE2_REPORTER_INFIX + this.qName);

        /* Determine the reportable capabilities by examining all interfaces
         * implemented by this reporter and its supertypes.
         */
        this.capabilities = new TreeSet<Capability>();
        while (!c.getSimpleName().equals("AbstractReporter")) {
            Class<? extends Reporter> [] ifs =
            	(Class<? extends Reporter> []) c.getInterfaces();
        	for (int i=0; i<ifs.length; i++) {
        		setCapability(ifs[i]);
        	}
        	c = (Class<? extends AbstractReporter>) c.getSuperclass();
        }
        this.messages = new TreeSet<Message>();
        this.numErrorMessages = 0;
	}
	
	/** Reinitialize reportable properties and clear all messages.
	 */
	public void reset() {
		this.messages.clear();
		this.numErrorMessages = 0;
	}
	
	/** Determine fail fast status.
	 * @param jhove2    JHOVE2 framework
	 * @param numErrors Number of errors
	 * @return True if the number of errors is over the JHOVE2 framework fail
	 *         fast limit
	 */
	public static synchronized boolean failFast(JHOVE2 jhove2, int numErrors) {
		int failFastLimit = jhove2.getFailFastLimit();
		if (failFastLimit > 0 && numErrors > failFastLimit) {
			return true;
		}
		
		return false;
	}
	
	/** Set reportable capabilities based on the interfaces implemented by the
	 * reporter.
	 * @param ife Interface
	 */
	protected void setCapability(Class<?> ife) {
   		String name = ife.getName();
		int n = name.lastIndexOf(".");
		if (n > -1) {
			name = name.substring(n+1);
		}
		if      (name.equals("Digestable")) {
			this.capabilities.add(Capability.MessageDigesting);
		}
		else if (name.equals("Identifiable")) {
			this.capabilities.add(Capability.Identification);
		}
		else if (name.equals("Parsable")) {
			this.capabilities.add(Capability.Parsing);
		}
		else if (name.equals("Validatable")) {
			this.capabilities.add(Capability.Validation);
		}
		
		/* Check all superinterfaces extended by this interface. */
		Class<? extends Reporter> [] ifs =
			(Class<? extends Reporter> []) ife.getInterfaces();
		if (ifs != null) {
			for (int i=0; i<ifs.length; i++) {
				setCapability(ifs[i]);
			}
		}
	}
	
	/** Add a reportable message.
	 * @param message Reporter message
	 * @see org.jhove2.core.Reporter#addMessage(Message)
	 */
	public void addMessage(Message message) {
		this.messages.add(message);
		
		if (message.getSeverity() == Severity.Error) {
			this.numErrorMessages++;
		}
	}
	
	/** Add reportable messages.
	 * @param messages Reporter messages
	 * @see org.jhove2.core.Reporter#addMessages(Set)
	 */
	public void addMessages(Set<Message> messages) {
		Iterator<Message> iter = messages.iterator();
		while (iter.hasNext()) {
			this.messages.add(iter.next());
		}
	}
	
	/** Get reportable capabilities.
	 * @return Set of reportable capabilities
	 * @see org.jhove2.core.Reporter@getCapabilities()
	 */
	@Override
	public Set<Capability> getCapabilities() {
		return this.capabilities;
	}

	/** Get reportable messages.
	 * @return Reporter messages
	 * @see org.jhove2.core.Reporter#getMessages() 
	 */
	@Override
	public Set<Message> getMessages() {
		return this.messages;
	}
	
	/** Get number of reportable error messages.
	 * @return Number of reportable error messages.
	 */
	public int getNumErrorMessages() {
		return this.numErrorMessages;
	}
	
	/** Get number of reportable messages.
	 * @return Number of reportable messages
	 */
	public int getNumMessages() {
		return this.messages.size();
	}
	
	/** Get reportable identifier in the JHOVE2 namespace of the form:
	 * <code>info:jhove2/qname</code>, where <code>qname</code> is the
	 * full package-qualified reportable name.
	 * @return Reporter identifier
	 * @see org.jhove2.core.Reporter#getReportableIdentifier()
	 */
	@Override
	public Identifier getReportableIdentifier() {
		return this.reportableIdentifier;
	}	
	
	/** Get reportable name.  The name is based on the non-package-qualified
	 * class name.
	 * @rerturn Reporter name
	 * @see org.jhove2.core.Reporter#getReportableName()
	 */
	@Override
	public String getReportableName() {
		return this.reportableName;
	}
	
	/** Get qualified reportable name.  The name is based on the full package-qualified
	 * class name with all periods (.) replaced with slashes (/).
	 * @return Reporter name
	 * @see org.jhove2.core.Reporter#getQName()
	 */
	public String getQName() {
		return this.qName;
	}

	/** Get reportable capability status.
	 * @param capability Reporter capability
	 * @return True if the reportable has the capability
	 * @see org.jhove2.core.Reporter#isCapableOf(Capability)
	 */
	@Override
	public boolean isCapableOf(Capability capability) {
		return this.capabilities.contains(capability);
	}
}
