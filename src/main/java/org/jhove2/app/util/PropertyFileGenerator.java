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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.reportable.info.ReportablePropertyInfo;

/**
 * Base class for generating editable Java properties files for features of 
 * {@link org.jhove2.core.reportable.Reportable} classes (for example, properties
 * files for configuring display of {@link org.jhove2.core.reportable.Reportable} properties, 
 * or units of measure for {@link org.jhove2.core.reportable.Reportable} properties)
 * 
 * @author smorrissey
 *
 */
public abstract class PropertyFileGenerator {
	/** File extension for JHOVE2 properties files*/
	public static final String EXTENSION = ".properties";
	/** Boiler plate text for head of JHOVE2 properties files*, specific to each class of file */
	protected String headerInfo;
	/** Fully-qualified class name for which properties file is to be generated */
	protected String className;
	/** Key-Value map to be serialized as property file */
	protected Map<String, String> prop2Value;
	/** Base name for property file */
	protected String propertyFileBaseName;
	/** Base directory for output file.  Intermediate directories corresponding to segments in
	 * the fully qualified class name will be created as subdirectories of this base directory
	 * when the property file is created */
	protected String baseOutputDirectory;
	/**  Full path to property file to be created */
	protected String fullFilePath;
	/** A set of all {@link org.jhove2.core.reportable.info.ReportablePropertyInfo} descriptors of a
	 * {@link org.jhove2.core.reportable.Reportable} class */
	protected Set<ReportablePropertyInfo> propsList;

	/**
	 * Constructor
	 */
	public PropertyFileGenerator() {
		super();
		this.prop2Value = new TreeMap<String, String>();
	}

	/**
	 * Method to create actual list of possible property values (may be an empty list,
	 * as for units properties file)
	 * @return HashMap<String, String> containing actual list of possible property values 
	 */
	public abstract Map<String, String> createPropertyValues();

	/**
	 * Principal method.  Constructs a property file with JHOVE2 identifier as the key,
	 * and a "|" separated list of possible legal values (may be empty, as with units file)
	 * @throws JHOVE2Exception
	 */
	public void createPropertyFile() throws JHOVE2Exception{
		PrintStream printer = null;
		// get the list of features for the class
		this.setPropsList(FeatureConfigurationUtil.
				getProperitiesAsReportablePropertyInfoSet(this.getClassName()));
		// create the property values for each feature
		this.setProp2Value(this.createPropertyValues());
		// construct the full path name for properties files
		String relFilePath = this.constructFileNameFromClassName(this.getClassName());
		this.setFullFilePath(this.getBaseOutputDirectory().concat
				(relFilePath));
		// create any subdirectories needed for the file
		this.makeDirs(this.getFullFilePath());
		try {
			printer = 
				new PrintStream (new FileOutputStream(this.getFullFilePath()));
		} catch (FileNotFoundException e) {
			throw new JHOVE2Exception(
					"Unable to create output file " + this.getFullFilePath(), e);
		}
		if (this.getHeaderInfo() != null){
			printer.println(this.getHeaderInfo());
		}
		for (String property:this.getProp2Value().keySet()){
			StringBuffer sb = new StringBuffer(property);
			sb.append("\t");
			String value = this.getProp2Value().get(property);
			if (value != null){
				sb.append(value);
			}
			// escape any ":" in string
			String formattedString = this.convertToPropsFileString(sb.toString());
			printer.println(formattedString);
		}
		printer.flush();
		printer.close();
		return;
	}

	/**
	 * Utility method to replace instances of ":" with "\:" in JHOVE2 I8R strings
	 * @param sourceString String to be converted
	 * @return converted String
	 */
	public String convertToPropsFileString(String sourceString){
		String targetString = sourceString;
		if (sourceString != null){
			targetString = targetString.replace(":", "\\:");
		}
		return targetString;
	}
	/**
	 * Constructs relative path to properties file that is constructed
	 * from the dotted notation of the fully-qualified class name
	 * @param fully-qualified class name 
	 * @return String containing relative path of new property file
	 */
	public String constructFileNameFromClassName(String className){
		StringBuffer relativePath = new StringBuffer();
		String separator = System.getProperty("file.separator");
		if (!this.getBaseOutputDirectory().endsWith(separator)){
			relativePath.append(separator);
		}
		String classAsPath = className.replace(".", separator);
		relativePath.append(classAsPath);
		relativePath.append(this.getPropertyFileBaseName());
		relativePath.append(EXTENSION);
		return relativePath.toString();
	}

	/**
	 * Makes any necessary directories from base path to full property file path
	 * @param full path name of property file
	 * @throws JHOVE2Exception
	 */
	public void makeDirs(String fullFilePath) throws JHOVE2Exception{
		String dirPath = fullFilePath;
		int i = fullFilePath.lastIndexOf(System.getProperty("file.separator"));
		if (i>0){
			dirPath = fullFilePath.substring(0,i+1);
			File dirFile = new File(dirPath);
			if (dirFile.exists()){
				if (!dirFile.isDirectory()){
					throw new JHOVE2Exception(
							"Directory path " + dirPath + " exists but is not directory");
				}
				else if (!dirFile.canWrite()){
					throw new JHOVE2Exception(
							"Directory path " + dirPath + " exists but is not writable");
				}
			}// end if file exists
			else {
				try{
					dirFile.mkdirs();
				}
				catch (SecurityException e){
					throw new JHOVE2Exception (
							"Exception thrown trying to create directory " + dirPath, e);
				}
			}// end else if dir does not exist
		}// end if file separator charcter found in file name
		return;
	}

	/**
	 * Accessor for String containing any information to be placed at beginning of properties file
	 * @return the headerInfo
	 */
	public String getHeaderInfo() {
		return headerInfo;
	}
	/**
	 * Mutator for any information to be placed at end of properties file
	 * @param headerInfo the headerInfo to set
	 */
	public void setHeaderInfo(String headerInfo) {
		this.headerInfo = headerInfo;
	}
	/**
	 * Accessor for name of class for which properties file is to be generated
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}
	/**
	 * Mutator for name of class for which properties file is to be generated
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}
	/**
	 * @return the prop2Value
	 */
	public Map<String, String> getProp2Value() {
		return prop2Value;
	}
	/**
	 * @param prop2Value the prop2Value to set
	 */
	public void setProp2Value(Map<String, String> prop2Value) {
		this.prop2Value = prop2Value;
	}
	/**
	 * @return the propertyFileBaseName
	 */
	public String getPropertyFileBaseName() {
		return propertyFileBaseName;
	}
	/**
	 * @param propertyFileBaseName the propertyFileBaseName to set
	 */
	public void setPropertyFileBaseName(String propertyFileBaseName) {
		this.propertyFileBaseName = propertyFileBaseName;
	}
	/**
	 * @return the baseOutputDirectory
	 */
	public String getBaseOutputDirectory() {
		return baseOutputDirectory;
	}
	/**
	 * @param baseOutputDirectory the baseOutputDirectory to set
	 */
	public void setBaseOutputDirectory(String baseOutputDirectory) {
		this.baseOutputDirectory = baseOutputDirectory;
	}
	/**
	 * @return the fullFilePath
	 */
	public String getFullFilePath() {
		return fullFilePath;
	}

	/**
	 * @param fullFilePath the fullFilePath to set
	 */
	public void setFullFilePath(String fullFilePath) {
		this.fullFilePath = fullFilePath;
	}

	/**
	 * @return the propsList
	 */
	public Set<ReportablePropertyInfo> getPropsList() {
		return propsList;
	}

	/**
	 * @param propsList the propsList to set
	 */
	public void setPropsList(Set<ReportablePropertyInfo> propsList) {
		this.propsList = propsList;
	}

}
