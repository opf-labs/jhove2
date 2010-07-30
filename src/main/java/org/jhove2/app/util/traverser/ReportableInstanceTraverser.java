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
package org.jhove2.app.util.traverser;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.jhove2.app.util.FeatureConfigurationUtil;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.reportable.info.ReportablePropertyInfo;


/**
 * Traverses instance of a reportable object, collecting reportable feature information, 
 * including dotted path to reportable members
 * Intended for use in generating "dotted names" for Assessment configuration
 * NOTE:  can only detect inner reportable classes to one level of nesting
 * @author smorrissey
 *
 */
public class ReportableInstanceTraverser {
	public static final String USAGE = 
		"USAGE:  java -cp CLASSPATH " + ReportableInstanceTraverser.class.getName() 
		+ " fully-qualified-class-name output-dir-path {optional boolean should-recurse(default true)}";
	/** Error return code for erroneous command line invocation */
	public static final int EUSAGE = 1;
	/** Error return code if any exception is thrown while executing program */
	public static final int EEXCEPTION = 2;
	/** Return code for successful execution */
	public static final int SUCCESS = 0;

	/** class name for instance */
	protected String className;
	/** feature information for this object (non-recursive)*/
	protected SortedSet<PropertyDoc> reportablePropertiesInfo;
	/** recursive feature information for this object */
	protected SortedSet<PropertyDoc> allReportablePropertiesInfo;
	/** should recursively collect feature information for features that are themselves of a Reportable type*/
	protected boolean shouldRecurse;
	/** base name for creating dotted names for reportable members */
	protected String dottedBaseName;


	public static void main(String[]args){
		if (args.length<2){
			System.out.println(USAGE);
			System.exit(EUSAGE);
		}
		String clsName = args[0];
		String outFileName = args[1];
		boolean shdRecurs = true;
		if (args.length>2){
			String strShdRecurse =args[2];
			if (strShdRecurse.toLowerCase().equals("true")){
				shdRecurs = true;
			}
			else if (strShdRecurse.toLowerCase().equals("false")){
				shdRecurs = false;
			}
			else {
				System.out.println(USAGE);
				System.exit(EUSAGE);
			}
		}
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(
					new BufferedWriter(
							new FileWriter(outFileName)));
		} catch (IOException e) {
			System.out.println("IOException creating file " + outFileName);
			System.exit(EEXCEPTION);
		}
		ReportableInstanceTraverser rit = new ReportableInstanceTraverser(clsName,shdRecurs);
		try {
			rit.extractDocInfo();
			for(PropertyDoc prop:rit.getAllReportablePropertiesInfo()){
				writer.println(prop.dottedName + "\t " + prop.typeString);
			}
			writer.flush();
			writer.close();
		} catch (JHOVE2Exception e) {
			System.out.println("Exception thrown processing class " + clsName);
			e.printStackTrace();
			System.exit(EEXCEPTION);
		}
	}

	/**
	 * Constructor
	 * @param reportableClassName name of instance class
	 */
	public ReportableInstanceTraverser(String reportableClassName){
		this(reportableClassName, false);	
	}
	/**
	 * Constructor
	 * @param reportableClassName name of instance class
	 * @param shouldRecurse boolean should recursively collect feature information for features that are themselves of a Reportable type
	 */
	public ReportableInstanceTraverser(String reportableClassName, boolean shouldRecurse){
		this(reportableClassName, shouldRecurse, reportableClassName.concat("."));	
	}
	/**
	 * Constructor
	 * @param reportableClassName name of instance class
	 * @param shouldRecurse boolean should recursively collect feature information for features that are themselves of a Reportable type
	 * @param dottedBaseName base name for creating dotted names for reportable members
	 */
	public ReportableInstanceTraverser(String reportableClassName,
			boolean shouldRecurse, String dottedBaseName){
		this(reportableClassName, shouldRecurse, dottedBaseName, new TreeSet<PropertyDoc>());
	}
	/**
	 * Constructor
	 * @param reportableClassName name of instance class
	 * @param shouldRecurse boolean should recursively collect feature information for features that are themselves of a Reportable type
	 * @param dottedBaseName base name for creating dotted names for reportable members
	 * @param allReportablePropertiesInfo Set containing accumulated properties info
	 */
	public ReportableInstanceTraverser(String reportableClassName,
			boolean shouldRecurse, String dottedBaseName, SortedSet<PropertyDoc> allReportablePropertiesInfo){
		this.className = reportableClassName;
		this.dottedBaseName = dottedBaseName;
		this.shouldRecurse = shouldRecurse;	
		this.allReportablePropertiesInfo = allReportablePropertiesInfo;
	}

	/**
	 * Extract feature information about reportable object; recursively if so configured
	 * Entry point for getting all reportable instance information
	 * @return SortedSet<PropertyDoc> with reportable object information
	 * @throws JHOVE2Exception
	 */
	public SortedSet<PropertyDoc> extractDocInfo()
	throws JHOVE2Exception{
		this.reportablePropertiesInfo = this.extractReportablePropertiesInfo();
		this.allReportablePropertiesInfo.addAll(this.reportablePropertiesInfo);
		// now recurse over ReportableProperties
		// if any of them are of type Reportable, get their info too		
		HashMap<String,String> dottedName2Classname;
		if (this.reportablePropertiesInfo != null && this.shouldRecurse){
			dottedName2Classname= new HashMap<String,String>();
			for (PropertyDoc prop:reportablePropertiesInfo){
				String dName = prop.dottedName.concat(".");
				String pname= prop.type.getCanonicalName();
				if (prop.type.isPrimitive()){
					continue;
				}
				if (prop.type.isEnum()){
					prop.typeString = pname;
					continue;
				}
				if (prop.type.isArray()){
					Class<?> arrayClass = prop.type.getComponentType();
					if (FeatureConfigurationUtil.isReportableClass(arrayClass.getCanonicalName())){
						int i = dName.lastIndexOf(".");
						if (i>-1){
							dName = dName.substring(0, i);
							dName = dName.concat("[].");
						}
						dottedName2Classname.put(dName, arrayClass.getCanonicalName());
						continue;
					}
				}
				if (FeatureConfigurationUtil.isParameterizedType(prop.gType)){
					ParameterizedType pType =(ParameterizedType)prop.gType;
					for (Type type:pType.getActualTypeArguments()){
						String typeClassName = type.toString();					
						if (typeClassName.startsWith("class ")){
							typeClassName = typeClassName.substring("class ".length());
						}
						else if (typeClassName.startsWith("interface ")){
							typeClassName = typeClassName.substring("interface ".length());
						}
						String paramType = prop.typeString;
						int i = paramType.indexOf("<");
						if (i>-1){
							boolean shouldAddBrackets = false;
							boolean shouldAddKV = false;
							paramType = paramType.substring(0, i);
							Class<?> tClass = null;
							Class<?> cClass =null;
							try {
								tClass = Class.forName(paramType);
								cClass = Class.forName("java.util.Collection");
								if (cClass.isAssignableFrom(tClass)){
									shouldAddBrackets = true;
								}
								else {
									cClass = Class.forName("java.util.Map");
									if (cClass.isAssignableFrom(tClass)){
										shouldAddKV = true;
									}
								}
							} catch (ClassNotFoundException e) {
								;
							}
							int j = dName.lastIndexOf(".");
							if (j>0){
								if (shouldAddBrackets){
									dName = dName.substring(0, j).concat("[].");
								}
								else if(shouldAddKV){
									dName = dName.substring(0, j).concat("<K,V>.");
								}
							}							
						}// end if (i>-1)
						if (FeatureConfigurationUtil.isReportableClass(typeClassName)){

							dottedName2Classname.put(dName, typeClassName);
						}
					}
					continue;
				}
				prop.typeString = pname;
				if (FeatureConfigurationUtil.isReportableClass(pname)){
					dottedName2Classname.put(dName, pname);
					continue;
				}
				if (FeatureConfigurationUtil.isReportableInnerClass(pname)){
					dottedName2Classname.put(dName, pname);
					continue;
				}
				continue;		
			}
			for (String dName:dottedName2Classname.keySet()){
				String cName = dottedName2Classname.get(dName);
				// avoid infinite loop
				if (!cName.equals(this.className)){
					ReportableInstanceTraverser rit = new ReportableInstanceTraverser(
							cName,this.shouldRecurse,dName,this.allReportablePropertiesInfo);
					SortedSet<PropertyDoc> newPd = rit.extractDocInfo();
					this.allReportablePropertiesInfo.addAll(newPd);
				}
			}// end for (String dName:dottedName2Classname.keySet())
		}//end if shouldRecurse
		return this.allReportablePropertiesInfo;
	}

	/**
	 * Get ReportablePropertyInfo for this instance (non-recursive)
	 * @return SortedSet<PropertyDoc>
	 * @throws JHOVE2Exception
	 */
	protected SortedSet<PropertyDoc> extractReportablePropertiesInfo() throws JHOVE2Exception{
		SortedSet<PropertyDoc>pdocs = new TreeSet<PropertyDoc>();
		// get all reportable properties for this class and its superclasses
		Set<ReportablePropertyInfo> rpis = FeatureConfigurationUtil.
		getProperitiesAsReportablePropertyInfoSet(this.className);
		for (ReportablePropertyInfo prop:rpis){
			PropertyDoc pd = new PropertyDoc();
			Method method = prop.getMethod();
			pd.name = method.getName();
			if (pd.name.indexOf("get") == 0) {
				pd.name = pd.name.substring(3);
			}
			pd.id = prop.getIdentifier().getValue();
			pd.typeString = prop.getGenericType().toString();
			pd.type =  prop.getMethod().getReturnType();
			pd.gType = prop.getMethod().getGenericReturnType();
			pd.desc = prop.getDescription();
			if (pd.desc==null){
				pd.desc = "";
			}
			pd.ref = prop.getReference();
			if (pd.ref==null){
				pd.ref = "";
			}
			String firstLetter = pd.name.substring(0,1).toLowerCase();
			String fieldName = firstLetter;
			if (pd.name.length()>1){
				fieldName = fieldName + pd.name.substring(1);
			}
			pd.dottedName=this.dottedBaseName.concat(fieldName);
			pdocs.add(pd);
		}
		return pdocs;
	}

	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @return the reportablePropertiesInfo
	 */
	public SortedSet<PropertyDoc> getReportablePropertiesInfo() {
		return reportablePropertiesInfo;
	}

	/**
	 * @return the shouldRecurse
	 */
	public boolean isShouldRecurse() {
		return shouldRecurse;
	}

	/**
	 * @return the dottedBaseName
	 */
	public String getDottedBaseName() {
		return dottedBaseName;
	}

	/**
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * @param reportablePropertiesInfo the reportablePropertiesInfo to set
	 */
	public void setReportablePropertiesInfo(
			SortedSet<PropertyDoc> reportablePropertiesInfo) {
		this.reportablePropertiesInfo = reportablePropertiesInfo;
	}

	/**
	 * @param shouldRecurse the shouldRecurse to set
	 */
	public void setShouldRecurse(boolean shouldRecurse) {
		this.shouldRecurse = shouldRecurse;
	}

	/**
	 * @param dottedBaseName the dottedBaseName to set
	 */
	public void setDottedBaseName(String dottedBaseName) {
		this.dottedBaseName = dottedBaseName;
	}

	/**
	 * @return the allReportablePropertiesInfo
	 */
	public SortedSet<PropertyDoc> getAllReportablePropertiesInfo() {
		return allReportablePropertiesInfo;
	}

	/**
	 * @param allReportablePropertiesInfo the allReportablePropertiesInfo to set
	 */
	public void setAllReportablePropertiesInfo(
			SortedSet<PropertyDoc> allReportablePropertiesInfo) {
		this.allReportablePropertiesInfo = allReportablePropertiesInfo;
	}





}
