/* JHOVE2 - Next-generation architecture for format-aware characterization
 *
 * Copyright (c) 2008 by The Regents of the University of California.
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
 *   Library nor the names of its contributors may be used to endorse or
 *   promote products derived from this software without specific prior
 *   written permission.
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

package org.jhove2.core.util;

import java.util.Properties;

/** JHOVE2 utility class to load properties from java Properties file.
 * 
 * @author mstrong
 */
public class ConfigurableProperties {
	/** Java properties. */
	Properties properties;

	/** Instantiate a new <code>ConfigurableProperties</code>.
	 */
	public ConfigurableProperties() {
	}

	/** Instantiate a new <code>ConfigurableProperties</code>.
	 */
	public ConfigurableProperties(Properties properties) {
		this.properties = properties;
	}
	
	/** Get Java properties.
	 * @return Java properties
	 */
	public Properties getProperties() {
		return properties;
	}

	/** Set Java properties.
	 * @param properties Java properties
	 */
	public void setProperties(Properties properties) {
		this.properties = properties;
	}
}
