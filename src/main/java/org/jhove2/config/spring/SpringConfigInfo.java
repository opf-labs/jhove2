/**
 * JHOVE2 - Next-generation architecture for format-aware characterization
 * 
 * Copyright (c) 2009 by The Regents of the University of California, Ithaka
 * Harbors, Inc., and The Board of Trustees of the Leland Stanford Junior
 * University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * o Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * 
 * o Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * o Neither the name of the University of California/California Digital
 * Library, Ithaka Harbors/Portico, or Stanford University, nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
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

package org.jhove2.config.spring;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.jhove2.config.ConfigInfo;
import org.jhove2.core.I8R;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.I8R.Namespace;
import org.jhove2.core.format.Format;
import org.jhove2.core.reportable.Reportable;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.sleepycat.persist.model.NotPersistent;
import com.sleepycat.persist.model.Persistent;

/**
 * Implementation of ConfigInfo using Spring.
 * Uses static instance of org.springframework.context.ApplicationContext to create and configure Reportable objects
 * 
 * @author mstrong, slabrams, smorrissey, rnanders
 */
@Persistent
public class SpringConfigInfo
    implements ConfigInfo
{
	/** Spring configuration classpath. */
	public static final String CLASSPATH = "classpath*:**/jhove2-*-config.xml";

	/** Spring application context. */
	@NotPersistent
	protected static ApplicationContext context;

	public SpringConfigInfo(){
		super();
	}
	
	@Override
	public <R extends Reportable> R getReportable(Class<? super R> cl)
	    throws JHOVE2Exception
	{
		R reportable = null;
		try {
			Map<String, Object> beans = getObjectsForType(cl);
			if (beans.size()==0){
				throw new JHOVE2Exception("No beans found matching name " +
				                          cl.getCanonicalName());
			}
			ArrayList<Reportable> matchingBeans = new ArrayList<Reportable>();
			for (String beanName:beans.keySet()){
				Reportable repBean = (Reportable)beans.get(beanName);
				Class<?> beanClass = repBean.getClass();
				if (beanClass.getCanonicalName().equals(cl.getCanonicalName())){
					matchingBeans.add(repBean);
				}
			}
			if (matchingBeans.size() == 0) {
				throw new JHOVE2Exception("No beans found matching class " +
				                          cl.getCanonicalName());
			}
			else if (matchingBeans.size() > 1) {
				throw new JHOVE2Exception("Duplicate beans found matching class " +
				                          cl.getCanonicalName());
			}
			else {
			    reportable = (R) matchingBeans.get(0);
			}
		}
		catch (JHOVE2Exception e) {
			throw e;
		}
		catch (Exception e){
			throw new JHOVE2Exception("Exception thrown when attempting to create a bean for class " +
					  cl.getCanonicalName(), e);
		}
		return reportable;
	}

	/**
	 * Get reportable by bean name.
	 * 
	 * @param <R>
	 *            Parameter scope of the reportable
	 * @param cl
	 *            Reportable class
	 * @param name
	 *            Reportable bean name
	 * @return Reportable
	 * @throws JHOVE2Exception
	 */
	@SuppressWarnings("unchecked")
	public static synchronized <R extends Reportable> R getReportable(Class<? super R> cl,
	                                                                  String name)
	    throws JHOVE2Exception
	{
		R reportable = null;
		try {
			ApplicationContext context = getContext();
			reportable = (R) context.getBean(name, cl);
		}
		catch (BeansException e) {
			throw new JHOVE2Exception("Can't instantiate reportable: " + name, e);
		}

		return reportable;
	}

	/**
	 * Get reportable names by scope.
	 * 
	 * @param reportable
	 *            Reportable class
	 * @return Reportable names
	 * @throws JHOVE2Exception
	 */
	public static synchronized String[] getReportableNames(Class<? extends Reportable> reportable)
	    throws JHOVE2Exception
	{
		String[] names = null;
		try {
			ApplicationContext context = getContext();
			names = context.getBeanNamesForType(reportable);
		}
		catch (BeansException e) {
			throw new JHOVE2Exception("Can't retrieve instantiation names "
					+ "for reportable: " + reportable.getName(), e);
		}

		return names;
	}

	/**
	 * Get Java properties from a named properties file.
	 * 
	 * @param name
	 *            Properties file base name, i.e., without an extension
	 * @return Java properties
	 * @throws JHOVE2Exception
	 */
	public Properties getProperties(String name)
	    throws JHOVE2Exception
	{
		Properties props = null;
		try {
			ApplicationContext context = getContext();
			props = (Properties) context.getBean(name);
		}
		catch (BeansException e) {
			throw new JHOVE2Exception("Can't instantiate properties: " + name,
					                  e);
		}
		return props;
	}

	/**
	 * Resolves message code to localized message text
	 * 
	 * @param messageCode
	 *            Key to message template in properties file
	 * @param messageArgs
	 *            Arguments for message template
	 * @param locale
	 *            Locale to be used in constructing message text
	 * @return String containing formatted localized message text
	 * @throws JHOVE2Exception
	 */
	@Override
	public String getLocalizedMessageText(String messageCode, Object[] messageArgs,
	                                      Locale locale)
	    throws JHOVE2Exception
	{
		String messageText = null;
		try {
			ApplicationContext context = getContext();
			messageText = context.getMessage(messageCode, messageArgs, locale);
		}
		catch (BeansException e) {
			throw new JHOVE2Exception("Can't retrieve localized message for messageCode " +
					                  messageCode, e);
		}
		catch (NoSuchMessageException e) {
			throw new JHOVE2Exception("Can't retrieve localized message for messageCode " +
	                  messageCode, e);
		}
		return messageText;
	}

	/**
	 * Return the objects (Spring bean instances) that match the given object
	 * scope (including subclasses),
	 * 
	 * @param scope
	 *            the class or interface to match, or null for all concrete
	 *            beans
	 * @return a Map with the matching objects (Spring beans), containing the
	 *         bean names as keys and the corresponding object instances as
	 *         values
	 * @throws JHOVE2Exception 
	 * @see 
	 *      org.springframework.beans.factory.ListableBeanFactory.getBeansOfType(
	 *      java.lang.Class)
	 * @see http
	 *      ://static.springsource.org/spring/docs/2.5.x/api/org/springframework
	 *      /beans/factory/ListableBeanFactory.html#getBeansOfType(java.lang.
	 *      Class)
	 * 
	 */

	@SuppressWarnings("unchecked")
	public static Map<String, Object> getObjectsForType(Class type)
	    throws JHOVE2Exception
	{
		Map<String, Object> objectMap = null;
		try {
			ApplicationContext context = getContext();  		
			objectMap = context.getBeansOfType(type);
		}
		catch (BeansException e) {
			throw new JHOVE2Exception("Exception thrown attempting to get Spring Context",
			                          e);
		}
		return objectMap;
	}
	
	/**
	 * Accessor for static ApplicationContext instance.
	 * Creates instance if it does not exist
	 * @return Spring ApplicationContext instance
	 * @throws BeansException
	 */
	public static synchronized ApplicationContext getContext()
	    throws BeansException
	{
		if (context == null) {
			ApplicationContext newContext =
				new ClassPathXmlApplicationContext(CLASSPATH);
			context = newContext;
		}
		return context;
	}

	@Override
	public ConcurrentMap<String, String> getFormatAliasIdsToJ2Ids(Namespace namespace) 
	    throws JHOVE2Exception
	{
		ConcurrentHashMap<String, String> map = new ConcurrentHashMap<String, String>();
		Map<String, Object> formatMap = SpringConfigInfo.getObjectsForType(Format.class);
		/* For each of the formats */
		for (Entry<String, Object> entry : formatMap.entrySet()) {
			/* Get the format object */
			Format format = (Format) entry.getValue();
			/* Get the JHOVE format identifier for the format */
			I8R formatID = format.getIdentifier();
			/* For each aliasIdentifier of the format */
			for (I8R alias :  format.getAliasIdentifiers()) {
				if (alias.getNamespace().equals(namespace)) {
					/* Add an entry into the format identifier to module map */
					map.put(alias.getValue(), formatID.getValue());
				}
			}			
		}
		return map;
	}
}
