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

package org.jhove2.config;

import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.ConcurrentMap;

import org.jhove2.core.I8R;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.reportable.Reportable;

/**
 * Interface for methods necessary to obtain configuration information
 * @author smorrissey
 *
 */
public interface ConfigInfo {
	/**
	 * Method t construct an instance of a {@link org.jhove2.core.reportable.Reportable} object
	 * @param <R>
	 * @param reportableClass instance of a Class that implements or extends {@link org.jhove2.core.reportable.Reportable}
	 * @return instance of a {@link org.jhove2.core.reportable.Reportable} object
	 * @throws JHOVE2Exception
	 */
	public <R extends Reportable> R getReportable(Class<? super R> reportableClass)
	throws JHOVE2Exception;
	/**
	 * Method to construct a mapping of all Format aliases in a given Namespace available via configuration
	 * to the JHOVE2 {@link org.jhove2.core.I8R} for that Format
	 * @param namespace {@link org.jhove2.core.I8R} identifier for Namespace whose aliases we wish to map
	 * @return mapping from all configuration instances of aliases in the namespace to the JHOVE2 namespace
	 * @throws JHOVE2Exception
	 */
	public ConcurrentMap<String,String> getFormatAliasIdsToJ2Ids(I8R.Namespace namespace)
	throws JHOVE2Exception;
	
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
    public String getLocalizedMessageText(
            String messageCode, Object[] messageArgs, Locale locale)
            throws JHOVE2Exception;
    
    /**
     * Get Java properties from a named properties file.
     * 
     * @param name
     *            Properties file base name, i.e., without an extension
     * @return Java properties
     * @throws JHOVE2Exception
     */
    public Properties getProperties(String name)
            throws JHOVE2Exception; 
}
