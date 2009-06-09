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

package org.jhove2.core.message;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.Reporter;

/** Interface for JHOVE2 messages.
 * 
 * @author mstrong, slabrams
 */
public interface Message
	extends Comparable<Message>, Reporter
{
	/** Message context. */
	public enum Context {
		Object,
		Process
	}
	
	/** Message severities. */
	public enum Severity {
		Error    (1),
		Warning  (2),
		Info     (3) ;
		
		/** Message severity. */
		private int severity;
		
		/** Instantiate a new <code>Severity</code> object.
		 * @param severity Message severity
		 */
		private Severity(int severity) {
			this.severity = severity;
		}
		
		/** Get message severity.
		 * @return Message severity
		 */
		public int getSeverity() {
			return this.severity;
		}
	}
	/** Get class name in which the message was generated.
	 * @return Class name in which the message was generated 
	 */
	public String getClassName();
	
	/** Get fully-package-qualified class name in which the message was
	 * generated.
	 * @return Fully-package-qualified name in which the message was generated
	 */
	@ReportableProperty(value=1, desc="Object name in which the message " +
			"was generated.")
	public String getClassQName();
	
	/** Get message context.
	 * @return Message context
	 */
	@ReportableProperty(value=1, desc="Message context: Object or Process.")
	public Context getContext();
	
	/** Get message text.
	 * @return Message text
	 */
	@ReportableProperty(value=4, desc="Message text.")
	public String getMessage();
	
	/** Get method name in which the message was generated.
	 * @return Method name in which the message was generated
	 */
	@ReportableProperty(value=2, desc="Method name in which the message was " +
			"generated.")
	public String getMethodName();
	
	/** Get message sequence number.  The sequence number documents the
	 * ordinal position of the message among all messages generated in 
	 * a given JHOVE2 invocation.
	 * @return Message sequence number
	 */
	@ReportableProperty(value=3, desc="Message sequence number.")
	public int getSequence();
	
	/** Get message severity.
	 * @return Message severity
	 */
	@ReportableProperty(value=2, desc="Message severity.")
	public Severity getSeverity();
	
	/** Get {@link java.lang.String} representation of the message.  The
	 * canonical form of a message is:
	 * <code>
	 * SEVERITY text: arg=value; arg=value; ...
	 * </code>
	 * @return String representation of the message
	 */
	public String toString();
}
