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

package org.jhove2.core.config;

import java.util.Properties;

import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.Reportable;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/** Spring configuration utility.
 * 
 * @author mstrong, slabrams
 */
public class Configure {
	/** Spring configuration classpath. */
	public static final String CLASSPATH = "classpath*:**/*-config.xml";
	
	/** Spring application context. */
	protected static ApplicationContext context;
	
	/** Get reportable by bean name.
	 * @param cl   Reportable class
	 * @param name Reportable bean name
	 * @return Reportable
	 * @throws JHOVE2Exception
	 */
	public static synchronized <R> R getReportable(Class<? super R> cl, String name)
		throws JHOVE2Exception
	{
		R reportable = null;
		try {
			if (context == null) {
				context = new ClassPathXmlApplicationContext(CLASSPATH);
			}
		
			reportable = (R) context.getBean(name, cl);
		} catch (BeansException e) {
			throw new JHOVE2Exception("Can't instantiate reportable: " + name, e);
		}
		
		return reportable;
	}
	
	/** Get reportable class by bean name.
	 * @param cl   Reportable class (or super class)
	 * @param name Reportable bean name
	 * @return Reportable
	 * @throws JHOVE2Exception
	 */
	public static synchronized <R> Class<R> getReportableClass(Class<? super R> cl, String name)
		throws JHOVE2Exception
	{
		Class<R> cls = null;
		try {
			if (context == null) {
				context = new ClassPathXmlApplicationContext(CLASSPATH);
			}
		
			cls = (Class<R>) context.getType(name);
		} catch (BeansException e) {
			throw new JHOVE2Exception("Can't retrieve reportable class: " + name, e);
		}
		
		return cls;
	}
	
	/** Get reportable names by type.
	 * @param type Reportable type
	 * @return Reportable names
	 * @throws JHOVE2Exception 
	 */
	public static synchronized String [] getReportableNames(Class<? extends Reportable> reportable)
		throws JHOVE2Exception
	{
		String [] names = null;
		try {
			if (context == null) {
				context = new ClassPathXmlApplicationContext(CLASSPATH);
			}
		
			names = context.getBeanNamesForType(reportable);
		} catch (BeansException e) {
			throw new JHOVE2Exception("Can't retrieve instantiation names " +
					                  "for reportable: " + reportable.getName(),
					                  e);
		}
		
		return names;
	}
	
	/** Get Java properties from a named properties file.
	 * @param name Properties file base name, i.e., without an extension
	 * @return Java properties
	 */
	public static synchronized Properties getProperties(String name)
		throws JHOVE2Exception
	{
		Properties props = null;
		try {
			if (context == null) {
				context = new ClassPathXmlApplicationContext(CLASSPATH);
			}
			
			props = (Properties) context.getBean(name);
		} catch (BeansException e) {
			throw new JHOVE2Exception("Can't instantiate properties: " + name, e);	
		}


		return props;
	}

}
