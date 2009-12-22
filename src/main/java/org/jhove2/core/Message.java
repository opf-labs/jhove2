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

import java.util.Locale;

import org.jhove2.core.reportable.ReportableFactory;

/**
 * JHOVE2 message. A message has a severity, a context, and a textual value.
 * Messages are localized.
 * 
 * @author slabrams, smorrissey
 * 
 */
public class Message {
	/** Message contexts. */
	public enum Context {
		PROCESS, OBJECT
	}
	/** Message severities. */
	public enum Severity {
		ERROR, WARNING, INFO
	}
	/** Message code. */
	protected String messageCode;

	/** Localized Message Text */
	protected String localizedMessageText;

	/** Message context. */
	protected Context context;

	/** Message severity. */
	protected Severity severity;

	/** Message Locale. */
	protected Locale locale;

	/** Default Locale */
	private static Locale defaultLocale;

		static {
			defaultLocale = Locale.getDefault();
		}

	/**
	 * Instantiate a new Localized <code>Message</code>.
	 * 
	 * @param severity
	 *            Message severity
	 * @param context
	 *            Message context
	 * @param messageCode
	 *            Key to message text in localized property file
	 * @throws JHOVE2Exception 
	 */
	public Message(Severity severity, Context context, String messageCode) throws JHOVE2Exception {
		this(severity, context, messageCode, new Object[0], defaultLocale);
	}
	
	
	/**
	 * Instantiate a new Localized <code>Message</code>.
	 * 
	 * @param severity
	 *            Message severity
	 * @param context
	 *            Message context
	 * @param messageCode
	 *            Key to message text in localized property file
	 * @param messageArgs
	 * 	          Arguments to message format template
	 * @throws JHOVE2Exception
	 */
	public Message(Severity severity, Context context, String messageCode, Object[] messageArgs) 
	throws JHOVE2Exception {
		this(severity, context, messageCode, messageArgs, defaultLocale);
	}

	/**
	 * Instantiate a new Localized <code>Message</code>.
	 * 
	 * @param severity
	 *            Message severity
	 * @param context
	 *            Message context
	 * @param messageCode
	 *            Key to message text in localized property file
	 * @param messageArgs
	 * 	          Arguments to message format template
	 * @param locale
	 * 			  Locale for message text
	 * @throws JHOVE2Exception
	 */
	public Message(Severity severity, Context context, String messageCode, 
			Object[] messageArgs, Locale locale) throws JHOVE2Exception {
		this.severity = severity;
		this.context = context;
		this.locale = locale;
		this.messageCode = messageCode;
		this.localizedMessageText = this.localizeMessageText(messageCode, messageArgs, locale);
	}

	/**
	 * Resolves message code and produces localized message text
	 * @param messageCode
	 *            Key to message text in localized property file
	 * @param messageArgs
	 * 			  Arguments to message format template
	 * @param locale
	 *             Locale for message text
	 * @return  
	 *         Localized formatted message text
	 * @throws JHOVE2Exception
	 */
	protected String localizeMessageText(String messageCode, Object[] messageArgs, Locale locale) 
	throws JHOVE2Exception{
		String localizedMessage = null;
		localizedMessage = ReportableFactory.getLocalizedMessageText(messageCode, 
				messageArgs, locale);
		return localizedMessage;
	}
	/**
	 * Get {@link java.lang.String} representation of the message.
	 */
	@Override
	public String toString() {
		return "[" + this.severity + "/" + this.context + "] " + this.localizedMessageText;
	}

	/**
	 * @return the message
	 */
	public String getMessageCode() {
		return messageCode;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}

	/**
	 * @return the context
	 */
	public Context getContext() {
		return context;
	}

	/**
	 * @param context the context to set
	 */
	public void setContext(Context context) {
		this.context = context;
	}

	/**
	 * @return the severity
	 */
	public Severity getSeverity() {
		return severity;
	}

	/**
	 * @param severity the severity to set
	 */
	public void setSeverity(Severity severity) {
		this.severity = severity;
	}


	/**
	 * @return the localizedMessageText
	 */
	public String getLocalizedMessageText() {
		return localizedMessageText;
	}

	/**
	 * @param localizedMessageText the localizedMessageText to set
	 */
	public void setLocalizedMessageText(String localizedMessageText) {
		this.localizedMessageText = localizedMessageText;
	}

}
