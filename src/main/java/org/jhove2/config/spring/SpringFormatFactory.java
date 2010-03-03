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

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.jhove2.core.I8R;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.format.Format;
import org.jhove2.core.format.FormatFactory;

/**
 * Spring-based implementation of Factory class for {@link org.jhove2.core.format.Format} 
 * objects
 * 
 * @author smorrissey
 *
 */
public class SpringFormatFactory implements FormatFactory {

	/** Map from JHOVE2 format identifiers to bean name for format */
	public static ConcurrentMap<String, String> jhoveIdToBeanName;

	/* (non-Javadoc)
	 * @see org.jhove2.core.format.FormatFactory#makeFormat(java.lang.String)
	 */
	@Override
	public Format makeFormat(I8R formatIdentifier) throws JHOVE2Exception{
		/*
         * Use the JHOVE2 format id to get bean name for format in
         * Spring configuration file.
         */
		Format format = null;
        String beanName =
        	SpringFormatFactory.getJhoveIdToBeanName().get(formatIdentifier.getValue());
        if (beanName != null) {
            format =
            	SpringConfigInfo.getReportable(Format.class,
            			                        beanName);
        }
		return format;
	}

	/**
	 * Gets the mapping from format identifier to format object. Initializes the
	 * static map on first invocation.
	 * 
	 * @return map from JHOVE2 format identifier to format object bean name
	 * @throws JHOVE2Exception
	 */
	public static ConcurrentMap<String, String> getJhoveIdToBeanName()
	        throws JHOVE2Exception {
	    if (jhoveIdToBeanName == null) {
	        jhoveIdToBeanName = new ConcurrentHashMap<String, String>();
	        /*
	         * Use Spring to get instances of all objects inheriting from
	         * BaseFormatModule
	         */
	        Map<String, Object> formatMap = SpringConfigInfo.
	                getObjectsForType(Format.class);
	        /* For each of the formats */
	        for (Entry<String, Object> entry : formatMap.entrySet()) {
	            /* Get the Spring bean name for the format object */
	            String formatBeanName = entry.getKey();
	            /* Get the JHOVE format identifier for the format */
	            Format format = (Format) entry.getValue();
	            I8R formatID = format.getIdentifier();
	            /* Add an entry into the format identifier to module map */
	            jhoveIdToBeanName.put(formatID.getValue(), formatBeanName);
	            // System.out.println(formatID.getValue() + " = " + formatBeanName);
	        }
	    }
	    return jhoveIdToBeanName;
	}

}
