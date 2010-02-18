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

import java.io.IOException;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;


/**
 * Class to enable use of mask to find all related .properties files on classpath (for example,
 * all displayer.properties files for JHOVE2 Reportable feature display options, or
 * all unit.properties files for JHOVE2 Reportable feature units of measure.
 * Assumes that Spring config file will NOT make use of either the "location" or "locations"
 * properties, but instead will use the new "fileBaseName" property.
 * 
 * This enables creators of new modules, by following the naming conventions for these
 * standard configuration properties files,
 * and by putting such files on the classpath, to expose these files to discovery without
 * any changes required to the Spring configuration files
 * 
 * @author smorrissey
 *
 */
public class PropertiesFactoryBean 
extends org.springframework.beans.factory.config.PropertiesFactoryBean {
	/**
	 * base name to be used in search for related Java .properties files, for example,
	 * "displayer" for all files on the classpath that match the pattern
	 * properties/{0 or more directories here/{somename}_displayer.properties
	 */
	protected String propertyFileBaseName;

	/**
	 * @return the propertyFileBaseName
	 */
	public String getPropertyFileBaseName() {
		return propertyFileBaseName;
	}

	/**
	 * Uses the propertyFileBaseName to locate all .properties files on the classpath
	 * that match the pattern 
	 * "properties/{0 or more directories here/{somename}_displayer.properties",
	 * and sets the locations field of the parent class to these Resources
	 * @param propertyFileBaseName the propertyFileBaseName to set
	 * @throws IllegalArgumentException if any IOException is thrown attempting to resolve resource
	 */
	public void setPropertyFileBaseName(String propertyFileBaseName) {
		this.propertyFileBaseName = propertyFileBaseName;
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource[] resources = new Resource[0];
		try {
			resources = resolver.getResources
				("classpath*:properties/**/*_" + propertyFileBaseName + ".properties");
		} catch (IOException e) {
			Assert.isTrue(false, "IO exception when attempting to resolve resources for " +
					"classpath*:properties/**/*_" + propertyFileBaseName + ".properties");
		}
		this.setLocations(resources);
	}

	
	
}
