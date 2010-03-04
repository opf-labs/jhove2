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

package org.jhove2.app.util;


import java.lang.reflect.Type;
import java.util.HashMap;

import org.jhove2.core.reportable.info.ReportablePropertyInfo;


/**
 * Class to generate editable Java properties file for units of measusre settings for
 * Reportable features that have a Numeric type
 * @author smorrissey
 *
 */
public class UnitsPropertyFileGenerator extends PropertyFileGenerator {

	/**
	 * Header for top of file
	 */
	public static final String DISPLAY_HEADER = 
		 "# Units of measure properties\n" +
         "# Note: These unit of measure labels are descriptive only; changing the label\n" +
         "# does NOT change the determination of the underlying property value.";
    /**
     * Convention for naming file is {CLASSNAME}_unit.properties
     */
	public static final String PROP_BASE_NAME = "_unit";
	/** Usage string */
	public static final String USAGE = 
		"USAGE:  java -cp CLASSPATH org.jhove2.app.util.UnitsPropertyFileGenerator fully-qualified-class-name output-dir-path";
	/** Error return code for erroneous command line invocation */
	public static final int EUSAGE = 1;
	/** Error return code if any exception is thrown while executing program */
	public static final int EEXCEPTION = 2;
	/** Return code for successful execution */
	public static final int SUCCESS = 0;
	
	/**
	 * Constructor
	 */
	public UnitsPropertyFileGenerator() {
		super();
		this.setHeaderInfo(DISPLAY_HEADER);
		this.setPropertyFileBaseName(PROP_BASE_NAME);
	}

	@Override
	public  HashMap<String, String> createPropertyValues(){
		HashMap<String, String> map = new HashMap<String, String>();
		for (ReportablePropertyInfo info : this.getPropsList()){
			Type type = info.getGenericType();
			if (FeatureConfigurationUtil.isNumericType(type)){
				String value = " ";
				String mapKey = info.getIdentifier().getValue();
				map.put(mapKey, value);
			}
		}
		return map;
	}
	/**
	 * Main method invoked to generate Units of Measure properties files.
	 * @param args 2 arguments:  fully qualified class name for Reportable class for which
	 * property file is to be generated, and directory path to which file should be written
	 * File with be written to base-path plus sub-directories corresponding to class's package
	 * name, and file will have name baseclassname_unit.properties
	 */
	public static void main(String[] args) {
		if (args.length < 2){
			System.out.println(USAGE);
			System.exit(EUSAGE);
		}
		UnitsPropertyFileGenerator dpg = new UnitsPropertyFileGenerator();
		dpg.setClassName(args[0]);
		dpg.setBaseOutputDirectory(args[1]);
		try{
			dpg.createPropertyFile();
			System.out.println("Succesfully created unit property file for class " + dpg.getClassName());
			System.out.println("File can be found at " + dpg.getFullFilePath());
			System.exit(SUCCESS);
		}
		catch (Exception e){
			System.out.println("Exception thrown attempting to create a file for class "
					 + dpg.getClassName() + " in directory " + dpg.getBaseOutputDirectory());
			System.out.println(e.getMessage());
			e.printStackTrace(); 
			System.exit(EEXCEPTION);
		}
	}



}
