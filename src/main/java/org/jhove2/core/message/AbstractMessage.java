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
import org.jhove2.core.AbstractReporter;

/** An abstract JHOVE2 message.
 * 
 * @author mstrong, slabrams
 */
public abstract class AbstractMessage
	extends AbstractReporter
	implements Message
{	
	/** AbstractMessage sequence number.  Sequence numbers document the ordinal
	 * positions of all messages generated in a given JHOVE2 invocation.
	 */
	protected static int masterSequence = 0;
	/** Class name in which the message was generated. */
	protected String className;
	
	/** Fully-package-qualified class name in which the message was generated. */
	protected String classQName;
		
	/** Message context. */
	protected Context context;
	
	/** Message text. */
	protected String message;
	
	/** Method name in which the message was generated. */
	protected String methodName;
	
	/** Message sequence number. */
	protected int sequenceNumber;

	/** Message severity. */
	protected Severity severity;

	/** Instantiate a new <code>AbstractMessage</code>.
	 * @param context  Message context
	 * @param severity Message severity
	 * @param message  Message text
	 */
	public AbstractMessage(Context context, Severity severity, String message) {
		super();

		this.context  = context;
		this.severity = severity;
		this.message  = message;
		
		Throwable t = new Throwable();
		StackTraceElement [] stack = t.getStackTrace();
		this.classQName = stack[2].getClassName();
		this.methodName = stack[2].getMethodName();
		
		int n = this.classQName.lastIndexOf('.');
		if (n > -1) {
			this.className = this.classQName.substring(n+1);
		}
		else {
			this.className = this.classQName;
		}
		
		this.sequenceNumber = ++masterSequence;
	}

	/** Get class name in which the message was generated.
	 * @return Class name in which the message was generated
	 * @see org.jhove2.core.message.Message#getObject()
	 */
	@Override
	public String getClassName() {
		return this.className;
	}

	/** Get fully-package-qualified class name in which the message was
	 * generated.
	 * @return Class name in which the message was generated
	 * @see org.jhove2.core.message.Message#getObject()
	 */
	@Override
	public String getClassQName() {
		return this.classQName;
	}
	
	/** Get message context.
	 * @return Message context
	 */
	@ReportableProperty(value=1, desc="Message context: Object or Process.")
	public Context getContext() {
		return this.context;
	}
	
	/** Get message text.
	 * @return Message text
	 * @see org.jhove2.core.message.Message#getMessage()
	 */
	@Override
	public String getMessage() {
		return this.message;
	}

	/** Get method name in which the message was generated.
	 * @return Method name in which the message was generated
	 * @see org.jhove2.core.message.Message#getMethod()
	 */
	@Override
	public String getMethodName() {
		return this.methodName;
	}
	
	/** Get message sequence number.  The sequence number documents the
	 * ordinal position of the message among all messages generated in 
	 * a given JHOVE2 invocation.
	 * @return Message sequence number
	 * @see org.jhove2.core.message.Message#getSequence()
	 */
	@Override
	public int getSequence() {
		return this.sequenceNumber;
	}
		
	/** Get message severity.
	 * @return Message severity
	 * @see org.jhove2.core.message.Message#getSeverity()
	 */
	@Override
	public Severity getSeverity() {		
		return this.severity;
	}
	
	/** Compare the message to the specified message.  All exception messages
	 * are less than all error messages, which are less than all warning
	 * messages, which are less than all info messages.
	 * @return -1, 0, 1 if the message is less then, equal to, or greater than
	 *         the specified message
	 * @see java.lang.Comparable#compareTo(Object)
	 */
	public int compareTo(Message m) {
		int ret = 0;
		int thisSeverity = this.severity.getSeverity();
		int thatSeverity = m.getSeverity().getSeverity();
		if      (thisSeverity < thatSeverity) {
			ret = -1;
		}
		else if (thisSeverity > thatSeverity) {
			ret =  1;
		}
		else {
			int sequence = m.getSequence();
			if      (this.sequenceNumber < sequence) {
				ret = -1;
			}
			else if (this.sequenceNumber > sequence) {
				ret =  1;
			}
		}
		
		return ret;
	}

	/** Get {@link java.lang.String} representation of the message.  The
	 * canonical form of a message is:
	 * <code>
	 * SEVERITY text: arg=value; arg=value; ...
	 * </code>
	 * @return String representation of the message
	 * @see org.jhove2.core.message.Message#toString()
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer("[");
		buffer.append(this.context.toString().toUpperCase());
		buffer.append("/");
		buffer.append(this.severity.toString().toUpperCase());
		buffer.append(":");
		buffer.append(this.sequenceNumber);
		buffer.append("] ");
		buffer.append(this.message);
		if (this.context == Context.Process) {
			buffer.append(" [");
			buffer.append(this.className);
			buffer.append(".");
			buffer.append(this.methodName);
			buffer.append("]");
		}
		
		return buffer.toString();
	}
}
