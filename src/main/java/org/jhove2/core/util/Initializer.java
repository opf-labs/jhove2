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

import java.util.Enumeration;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/** JHOVE2 utility class for initializing from Java properties files or Spring
 * configuration files.
 * 
 * @author mStrong, slabrams
 */
public class Initializer {
	/** Log. */
	protected static final Log log = LogFactory.getLog(Initializer.class);

	/** Spring configuration file path. */
	public final static String SPRING_CONFIG_CONTEXT = "classpath*:**/*-config.xml";

	/** Spring application context. */
	public static ApplicationContext context;
	
	/** Get Spring application context.
	 * @return Spring application context
	 */
	public static ApplicationContext getContext()
		throws BeansException
	{
		if (context == null) {
			context =  new ClassPathXmlApplicationContext(SPRING_CONFIG_CONTEXT);
		}
		
		return context;
	}

	/** Get Java properties from a named properties file.
	 * @param name Properties file base name, i.e., without an extension
	 * @return Java properties
	 */
	public static synchronized Properties getProperties(String name)
		throws BeansException
	{
		if (context == null) {
			context = getContext();
		}
		ConfigurableProperties cp =
			(ConfigurableProperties) context.getBean(name);
		Properties props = cp.getProperties();

		return props;
	}

	/** Get Java properties from a resource bundle.
	 * @param name Properties file base name, i.e., without an extension
	 * @return Java properties
	 */
	public static synchronized Properties initFromBundle(String name)
		throws MissingResourceException
	{
		ResourceBundle rb =
				ResourceBundle.getBundle(name, Locale.getDefault());
		Properties props = new Properties();
		Enumeration<String> keys = rb.getKeys();
		while (keys.hasMoreElements()) {
			String key   = keys.nextElement();
			String value = rb.getString(key);

			props.put(key, value); 
		}

		return props;
	}
}
