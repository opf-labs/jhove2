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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.ParameterizedType;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.I8R;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.info.ReportableInfo;
import org.jhove2.core.info.ReportablePropertyComparator;
import org.jhove2.core.info.ReportablePropertyInfo;
import org.jhove2.core.info.ReportableSourceInfo;
import org.jhove2.core.reportable.Reportable;

/**
 * Base class for utility applications to generate .properties files for
 * JHOVE2 {@link org.jhove2.core.reportable.Reportable} classes.
 * @author smorrissey
 *
 */
public class FeatureConfigurationUtil {

	public static ArrayList<String> booleanTypes;
	public static ArrayList<String> numericTypes;

	public static final String BOOLEAN_TYPE = "boolean";
	public static final String BOOLEAN = "java.lang.Boolean";

	public static final String SHORT_TYPE = "short";
	public static final String INTEGER_TYPE = "int";
	public static final String LONG_TYPE = "long";
	public static final String FLOAT_TYPE = "float";
	public static final String DOUBLE_TYPE = "double";
	public static final String SHORT = "java.lang.Short";
	public static final String INTEGER = "java.lang.Integer";
	public static final String LONG = "java.lang.Long";
	public static final String FLOAT = "java.lang.Float";
	public static final String DOUBLE = "java.lang.Double";
	public static final String BIG_DECIMAL = "java.math.BigDecimal";
	public static final String BIG_INTEGER = "java.math.BigInteger";
	public static final String NUMBER = "java.lang.Number";
	public static final String ATOMIC_INTEGER = "java.util.concurrent.atomic.AtomicInteger";
	public static final String ATOMIC_LONG = "java.util.concurrent.atomic.AtomicLong";

	public static final String OR = " | ";

	/**
	 * Creates {@link org.jhove2.core.reportable.Reportable} object corresponding
	 * to class name passed as parameter
	 * @param className name of class for which to create a
	 *  {@link org.jhove2.core.reportable.Reportable} instance
	 * @return {@link org.jhove2.core.reportable.Reportable}
	 * @throws JHOVE2Exception if class name represents a class which does not extend
	 *    {@link org.jhove2.core.reportable.Reportable}, or for any other
	 *    exception thrown, or if Reportable does not have 0-argument constructor
	 */
	public static Reportable getReportableFromClassName(
			String className) throws JHOVE2Exception {
		Class <? extends Reportable> rClass = null;
		Reportable reportable = null;
		//		try {
		try {
			rClass = (Class<? extends Reportable>)(Class.forName(className));
			reportable = (rClass.newInstance());
		} catch (ClassNotFoundException e1) {
			throw new JHOVE2Exception(
					"ClassNotFoundException trying to create Reportable object from classname "
					+ className, e1);
		} catch (InstantiationException e) {
			throw new JHOVE2Exception(
					"Instantiation Exception tyring to create Reportable object from classname "
					+ className, e);
		} catch (IllegalAccessException e) {
			throw new JHOVE2Exception(
					"IllegalAccess Exception  tyring to create Reportable object from classname "
					+ className, e);
		}
		return reportable;
	}
	/**
	 * Return list of all property {@link org.jhove2.core.I8R} value strings for a
	 * {@link org.jhove2.core.reportable.Reportable} object
	 * @param reportable {@link org.jhove2.core.reportable.Reportable} object
	 * @return List containing all property {@link org.jhove2.core.I8R} value strings
	 */
	public static List<String> getPropertiesAsList(Reportable reportable){
		ArrayList<String> propsList = new ArrayList<String>();
		ReportableInfo reportableInfo = new ReportableInfo(reportable);
		for (ReportableSourceInfo rsInfo:reportableInfo.getProperties()){
			for (ReportablePropertyInfo rpInfo : rsInfo.getProperties()){
				String idString = rpInfo.getIdentifier().getValue();
				propsList.add(idString);
			}			
		}
		return propsList;
	}
	/**
	 * Return list of all {@link org.jhove2.core.info.ReportablePropertyInfo} for all reportable properties
	 * of a {@link org.jhove2.core.reportable.Reportable} object
	 * @param reportable {@link org.jhove2.core.reportable.Reportable} object
	 * @return List containing all property {@link org.jhove2.core.info.ReportablePropertyInfo} for Reportable
	 */
	public static List<ReportablePropertyInfo> getPropertiesAsReportablePropertyInfoList
	(Reportable reportable){
		ArrayList<ReportablePropertyInfo> propsList = new ArrayList<ReportablePropertyInfo>();
		ReportableInfo reportableInfo = new ReportableInfo(reportable);
		for (ReportableSourceInfo rsInfo:reportableInfo.getProperties()){
			for (ReportablePropertyInfo rpInfo : rsInfo.getProperties()){			
				propsList.add(rpInfo);
			}			
		}
		return propsList;
	}
	/**
 	 * Get set of all {@link org.jhove2.core.info.ReportablePropertyInfo} for a class if it is a
	 * {@link org.jhove2.core.reportable.Reportable} class
	 * @param Class for which we want properties
	 * @return set of all {@link org.jhove2.core.info.ReportablePropertyInfo} for that class
	 * @throws JHOVE2Exception if class does not implement {@link org.jhove2.core.reportable.Reportable}
	 */
	public static Set<ReportablePropertyInfo> getProperitiesAsReportablePropertyInfoSet (String className)
	throws JHOVE2Exception {
		Class<? extends Reportable> cl = null;
		ReportablePropertyComparator comparator = new ReportablePropertyComparator();
		Map<String, String> idMap = new HashMap<String, String>();
		try {
			cl = (Class<? extends Reportable>) Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new JHOVE2Exception("Cannot create Reportable class for className " + className, e);
		}
		Set<ReportablePropertyInfo> set = new TreeSet<ReportablePropertyInfo>(
				comparator);	
		do{

			Method[] methods = cl.getDeclaredMethods();
			for (int j = 0; j < methods.length; j++) {
				ReportableProperty annot = methods[j]
				                                   .getAnnotation(ReportableProperty.class);
				if (annot != null) {
					// construct an I8R for each reportable field using
					// the field's accessor method
					I8R featureId = I8R.makeFeatureI8RFromMethod(methods[j], cl);
					if (idMap.get(featureId.getValue()) == null) {
						idMap.put(featureId.getValue(), featureId.getValue());
						ReportablePropertyInfo prop = new ReportablePropertyInfo(
								featureId, methods[j], annot.value(), 
								annot.ref());
						set.add(prop);
					}// end if we don't already have this feature
				}// end if (annot != null)
			}// end for
			checkInterfaces(cl.getInterfaces(), idMap, comparator, set);
		} while ((cl = (Class<? extends Reportable>) cl.getSuperclass()) != null);

		return set;
	}
	/**
	 * Introspect on interfaces (and superinterfaces) to retrieve reportable
	 * properties.
	 * 
	 * @param ifs
	 *            Interfaces to examine
	 * @param idMap
	 *            Map of properties identifiers already retrieved
	 * @param comparator
	 *            Reportable property comparator
	 */
	public static void checkInterfaces(Class<?>[] ifs, Map<String, String> idMap,
			ReportablePropertyComparator comparator, Set<ReportablePropertyInfo> rpiSet) {
		for (int i = 0; i < ifs.length; i++) {
			Set<ReportablePropertyInfo> set = new TreeSet<ReportablePropertyInfo>(
					comparator);
			Method[] methods = ifs[i].getDeclaredMethods();
			for (int j = 0; j < methods.length; j++) {
				ReportableProperty annot = methods[j]
				                                   .getAnnotation(ReportableProperty.class);
				if (annot != null) {
					// construct an I8R for each reportable field using
					// the field's accessor method
					Class<? extends Reportable> repClass = (Class<? extends Reportable>)ifs[i];
					I8R featureId = I8R.makeFeatureI8RFromMethod(methods[j], repClass);
					if (idMap.get(featureId.getValue()) == null) {
						idMap.put(featureId.getValue(), featureId.getValue());
						ReportablePropertyInfo prop = new ReportablePropertyInfo(
								featureId, methods[j], annot.value(), 
								annot.ref());
						set.add(prop);
					}
				}
			}
			if (set.size() > 0) {
				rpiSet.addAll(set);
			}
			checkInterfaces(ifs[i].getInterfaces(), idMap, comparator, rpiSet);
		}
	}
	/**
	 * Test if Type is boolean
	 * @param type to be tested
	 * @return true if boolean, else false
	 * @throws JHOVE2Exception
	 */
	public static boolean isBooleanType(Type type){
		boolean isBoolean = false;
		if (!isParameterizedType(type)){
			Class<?> tClass = (Class<?>)type;
			String className = tClass.getName();
			if (getBooleanTypes().contains(className)){
				isBoolean = true;
			}
		}
		return isBoolean;
	}
	/**
	 * Test if type is numeric
	 * @param type to be tested
	 * @return true if numeric, else false
	 * @throws JHOVE2Exception
	 */
	public static boolean isNumericType(Type type){
		boolean isNumeric = false;
		if (!isParameterizedType(type)){
			Class<?> tClass = (Class<?>)type;
			String className = tClass.getName();
			if (getNumericTypes().contains(className)){
				isNumeric = true;
			}
		}
		return isNumeric;
	}
	/**
	 * Tests if Type is a Parameterized Type
	 * @param Type to be tested
	 * @return true if Type is Parameterized type; else false
	 */
	public static boolean isParameterizedType(Type type){
		boolean isPType = false;
		try {
			ParameterizedType pType = (ParameterizedType)type;
			isPType = true;
		}
		catch(Exception e){;}
		return isPType;
	}
	/**
	 * Get list of Strings representing all boolean class type names
	 * @return the booleanTypes
	 */
	public static ArrayList<String> getBooleanTypes() {
		if (booleanTypes == null){
			ArrayList<String> bTypes = new ArrayList<String>();
			bTypes.add(BOOLEAN_TYPE);
			bTypes.add(BOOLEAN);
			booleanTypes = bTypes;
		}
		return booleanTypes;
	}
	/**
	 * Get list of Strings representing all numeric class type names
	 * @return the numericTypes
	 */
	public static ArrayList<String> getNumericTypes() {
		if (numericTypes == null){
			ArrayList<String> ntypes = new ArrayList<String>();
			ntypes.add(SHORT_TYPE);
			ntypes.add(INTEGER_TYPE);
			ntypes.add(LONG_TYPE);
			ntypes.add(FLOAT_TYPE);
			ntypes.add(DOUBLE_TYPE);
			ntypes.add(SHORT);
			ntypes.add(INTEGER);
			ntypes.add(LONG);
			ntypes.add(FLOAT);
			ntypes.add(DOUBLE);
			ntypes.add(BIG_DECIMAL);
			ntypes.add(BIG_INTEGER);
			ntypes.add(NUMBER);
			ntypes.add(ATOMIC_INTEGER);
			ntypes.add(ATOMIC_LONG);			
			numericTypes = ntypes;
		}
		return numericTypes;
	}

	/**
	 * Test if class implements {@link org.jhove2.core.reportable.Reportable}
	 * @param className
	 * @return true if class implements {@link org.jhove2.core.reportable.Reportable}, 
	 *            else false
	 */
	public static boolean isReportableClass(String className){
		boolean isReportable = false;
		try {
			Class<?> tClass = Class.forName(className);
			Class<?> rClass = Class.forName("org.jhove2.core.reportable.Reportable");
			if (rClass.isAssignableFrom(tClass)){
				isReportable = true;
			}
		} catch (ClassNotFoundException e) {;}
		return isReportable;
	}
}
