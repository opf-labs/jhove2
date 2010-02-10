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

package org.jhove2.core.reportable;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.jhove2.core.JHOVE2Exception;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Spring configuration utility.
 * 
 * @author mstrong, slabrams, smorrissey
 */
public class ReportableFactory {
    /** Spring configuration classpath. */
    public static final String CLASSPATH = "classpath*:**/*-config.xml";

    /** Spring application context. */
    protected static ApplicationContext context;

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
    public static synchronized <R extends Reportable> R getReportable(
            Class<? super R> cl, String name) throws JHOVE2Exception {
        R reportable = null;
        try {
            if (context == null) {
                context = new ClassPathXmlApplicationContext(CLASSPATH);
            }

            reportable = (R) context.getBean(name, cl);
        }
        catch (BeansException e) {
            throw new JHOVE2Exception("Can't instantiate reportable: " + name,
                    e);
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
    public static synchronized String[] getReportableNames(
            Class<? extends Reportable> reportable) throws JHOVE2Exception {
        String[] names = null;
        try {
            if (context == null) {
                context = new ClassPathXmlApplicationContext(CLASSPATH);
            }

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
    public static synchronized Properties getProperties(String name)
            throws JHOVE2Exception {
        Properties props = null;
        try {
            if (context == null) {
                context = new ClassPathXmlApplicationContext(CLASSPATH);
            }

            props = (Properties) context.getBean(name);
        }
        catch (BeansException e) {
            throw new JHOVE2Exception("Can't instantiate properties: " + name,
                    e);
        }
        // edu.stanford.sulair.util.Trace.indentedStackDump();
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
    public static synchronized String getLocalizedMessageText(
            String messageCode, Object[] messageArgs, Locale locale)
            throws JHOVE2Exception {
        String messageText = null;
        try {
            if (context == null) {
                context = new ClassPathXmlApplicationContext(CLASSPATH);
            }
            messageText = context.getMessage(messageCode, messageArgs, locale);
        }
        catch (BeansException e) {
            throw new JHOVE2Exception(
                    "Can't retrieve localized message for messageCode "
                            + messageCode, e);
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
    public static synchronized Map<String, Object> getObjectsForType(Class type) {
        Map<String, Object> objectMap = context.getBeansOfType(type);
        return objectMap;
    }

	/**
	 * Utility method to construct full path to a file on class path.  Used for example 
	 * to locate DROID signature and configuration
	 * files.  Assumes directory containing these files is on the classpath
	 * @param fileName File to be found on class path
	 * @param fileDescription descriptor of file to be used in any exception messages
	 * @return String containing path to file
	 * @throws JHOVE2Exception if file is not found or ClassLoader throws exception
	 */
	public static String getFilePathFromClasspath(String fileName, String fileDescription)throws JHOVE2Exception {
	    URI fileURI = null;
	    try {
	        fileURI = ClassLoader.getSystemResource(fileName).toURI();
	        if (fileURI == null){
	            throw new JHOVE2Exception(fileDescription + " " + fileName
	                    + " not found on classpath");
	        }
	    }
	    catch (URISyntaxException e){
	        throw new JHOVE2Exception("Exception thrown when attempting to find " + fileDescription 
	                + " on classpath", e);
	    }
	    String path = fileURI.getPath();
	    return path;        
	}

}
