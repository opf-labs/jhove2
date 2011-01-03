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
package org.jhove2.persist;

import java.util.Properties;

import org.jhove2.config.ConfigInfo;
import org.jhove2.core.JHOVE2Exception;

/**
 * @author smorrissey
 *
 */
public class PersistenceManagerUtil {
	/** name of properties file with class name for PersistenceManagerFactory*/
	public static final String PERSISTENCE_PROPERTIES = "persistence";
	/** property name for persistence manager factory class */
	public static final String PERSISTENCE_CLASSNAME = "classname";
	/** static instance of PersistenceManagerFactory */
	private static PersistenceManagerFactory persistenceFactory;

	
	/**
	 * Private constructor
	 */
	private PersistenceManagerUtil() {
		super();
	}
	/**
	 * Uses methods in ConfigInfo to resolve property file location and extract property for persistence manager class
	 * Initializes static PersistenceManagerFactory instance
	 * @param configInfo ConfigInfo to resolve properties filse
	 * @throws JHOVE2Exception
	 */
	public static void createPersistenceManagerFactory(ConfigInfo configInfo)
	throws JHOVE2Exception {
		if (persistenceFactory==null){
			Properties properties = configInfo.getProperties(PERSISTENCE_PROPERTIES);
			String pClassName = properties.getProperty(PERSISTENCE_CLASSNAME);
			createPersistenceManagerFactory(pClassName);
		}
		return;
	}
	/**
	 *  Initializes static PersistenceManagerFactory instance
	 * @param factoryClassName fully qualified class name of persistence manager factory
	 * @throws JHOVE2Exception
	 */
	public static void createPersistenceManagerFactory(String factoryClassName) 
	throws JHOVE2Exception{
		if (persistenceFactory==null){
			Class<?> pClass = null;
			try {
				pClass = Class.forName(factoryClassName);
			} catch (ClassNotFoundException e) {
				throw new JHOVE2Exception("No class found for " + factoryClassName, e);
			}
			try {
				persistenceFactory = (PersistenceManagerFactory) pClass.newInstance();
			} catch (Exception e) {
				throw new JHOVE2Exception("Unable to instantiate instance for " + factoryClassName, e);
			} 
		}
		return;
	}
	
	public static PersistenceManagerFactory getPersistenceManagerFactory()
	throws JHOVE2Exception {
		return persistenceFactory;
	}
}
