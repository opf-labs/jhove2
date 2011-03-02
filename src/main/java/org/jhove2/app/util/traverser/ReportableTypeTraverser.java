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

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.jhove2.app.util.FeatureConfigurationUtil;
import org.jhove2.core.I8R;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.reportable.info.ReportablePropertyInfo;

/**
 * Traverses static code of a Reportable class to extract feature information.
 * NOTE:  can only detect inner reportable classes to one level of nesting
 * @author smorrissey
 */
public class ReportableTypeTraverser {
        /** Reportable class name*/
        protected String className;
        /** basic features of class */
        protected ReportableDoc reportableInfo;
        /** List of reportable feature information for class (non-recursive)*/
        protected List<PropertyDoc> reportablePropertiesInfo;
        /** map from class name to ReportableTypeTraverser information about that class */
        /** the reportableFeaturesDocumenter of all classes traversed will refer to the same instance of this map */
        protected Map <String, ReportableTypeTraverser> reportableFeaturesDocumenter;
        /** should recursively collect feature information for features that are themselves of a Reportable type*/
        protected boolean shouldRecurse;
        
        /**
         * Constructor
         * @param reportableClassName fully qualified Reportable class name
         */
        public ReportableTypeTraverser(String reportableClassName){
                this(reportableClassName, false);       
        }
        /**
         * Constructor
         * @param reportableClassName fully qualified Reportable class name
         * @param shouldRecurse should recursively collect feature information for features that are themselves of a Reportable type
         */
        public ReportableTypeTraverser(String reportableClassName, boolean shouldRecurse){
                this(reportableClassName,shouldRecurse, new HashMap<String, ReportableTypeTraverser>());        
        }
        
        /**
         * Constructor
         * @param reportableClassName fully qualified Reportable class name
         * @param shouldRecurse should recursively collect feature information for features that are themselves of a Reportable type
         * @param reportableFeaturesDocumenter Map <String, ReportableTypeTraverser> cumulative map from Reportable class name to properties
         */
        public ReportableTypeTraverser(String reportableClassName, boolean shouldRecurse,
                Map <String, ReportableTypeTraverser> reportableFeaturesDocumenter) {
                this.className = reportableClassName;
                this.reportableFeaturesDocumenter = reportableFeaturesDocumenter;
                this.shouldRecurse = shouldRecurse;
        }
        
        /**
         * Entry point for extracting Reportablie information about class
         * Recursive if so configured
         * @return Map <String, ReportableTypeTraverser> cumulative map from Reportable class name to properties
         * @throws JHOVE2Exception
         */
        public Map <String, ReportableTypeTraverser> extractDocInfo()
            throws JHOVE2Exception
        {
                this.reportableInfo = this.extractReportableInfo();
                if (this.reportableInfo != null){
                        this.reportablePropertiesInfo = this.extractReportablePropertiesInfo();
                        Collections.sort(this.reportablePropertiesInfo);
                        // now recurse over ReportableProperties
                        // if any of them are of type Reportable, document its type too         
                        SortedSet<String> reportableProps;

                        if (this.shouldRecurse){
                                reportableProps= new TreeSet<String>();

                                for (PropertyDoc prop:reportablePropertiesInfo){
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
                                                        reportableProps.add(arrayClass.getCanonicalName());
                                                        continue;
                                                }
                                                if (FeatureConfigurationUtil.isReportableInnerClass(arrayClass.getCanonicalName())){
                                                        reportableProps.add(arrayClass.getCanonicalName());
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
                                                        if (FeatureConfigurationUtil.isReportableClass(typeClassName)){
                                                                reportableProps.add(typeClassName);
                                                        }
                                                        else if (FeatureConfigurationUtil.isReportableInnerClass(typeClassName)){
                                                                reportableProps.add(typeClassName);
                                                        }
                                                }
                                                continue;
                                        }
                                        prop.typeString = pname;
                                        if (FeatureConfigurationUtil.isReportableClass(pname)){
                                                reportableProps.add(pname);
                                                continue;
                                        }
                                        if (FeatureConfigurationUtil.isReportableInnerClass(pname)){
                                                reportableProps.add(pname);
                                                continue;
                                        }
                                        continue;               
                                }
                                this.reportableFeaturesDocumenter.put(this.reportableInfo.className, this);
                                for (String rClassName:reportableProps){
                                        if (!this.reportableFeaturesDocumenter.containsKey(rClassName)){                                        
                                                ReportableTypeTraverser rd = 
                                                        new ReportableTypeTraverser(rClassName,this.shouldRecurse,this.reportableFeaturesDocumenter);
                                                Map <String, ReportableTypeTraverser> newRds = rd.extractDocInfo();
                                                for (String key:newRds.keySet()){
                                                        if (!this.reportableFeaturesDocumenter.containsKey(key)){
                                                                ReportableTypeTraverser value = newRds.get(key);
                                                                this.reportableFeaturesDocumenter.put(key, value);
                                                        }
                                                }
                                        }// end if !this.reportableFeaturesDocumenter.containsKey(rClassName)
                                }// end for (String rClassName:reportableProps)
                        }//end if shouldRecurse
                }// end if (this.reportableInfo != null)
                return this.reportableFeaturesDocumenter;
        }
        
        /**
         * Get ReportablePropertyInfo for this instance (non-recursive)
         * @return ReportableDoc reportable property info
         * @throws JHOVE2Exception
         */
        protected ReportableDoc extractReportableInfo()
            throws JHOVE2Exception
        { 
                ReportableDoc md = null;
                boolean isReportable = FeatureConfigurationUtil.isReportableClass(this.className) ;
                if (isReportable){
                        Class<?> rClass = null;
                        try {
                                rClass = Class.forName(this.className);
                                md = new ReportableDoc();
                                md.className = this.className;
                                md.name = rClass.getSimpleName();
                                I8R identifier = new I8R(I8R.JHOVE2_PREFIX + "/" +
                                                I8R.JHOVE2_REPORTABLE_INFIX + "/" +
                                                this.className.replace('.', '/'));
                                md.id = identifier.toString();
                        } catch (ClassNotFoundException e) {
                                throw new JHOVE2Exception(
                                                "Non-reportable class: " + this.className,
                                                e);
                        }               
                }
                else {
                        boolean isReportableInnerClass = FeatureConfigurationUtil.isReportableInnerClass(this.className);
                        if (isReportableInnerClass){
                                Class<?> rClass = null;
                                rClass = FeatureConfigurationUtil.getReportableFromInnerClassName(this.className);
                                if (!(rClass==null)){
                                        md = new ReportableDoc();
                                        md.className = rClass.getCanonicalName();
                                        md.name = rClass.getSimpleName();
                                        I8R identifier = new I8R(I8R.JHOVE2_PREFIX + "/" +
                                                        I8R.JHOVE2_REPORTABLE_INFIX + "/" +
                                                        md.className.replace('.', '/'));
                                        md.id = identifier.toString();
                                }
                        }
                }
                return md;
        }
        /**
         * Iterates over properties to extract reportable information
         * @return List<PropertyDoc> documenting features
         * @throws JHOVE2Exception
         */
        protected List<PropertyDoc> extractReportablePropertiesInfo()
            throws JHOVE2Exception
        {
                ArrayList<PropertyDoc>pdocs = new ArrayList<PropertyDoc>();
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
                        pd.dottedName=this.className.concat(".").concat(fieldName);
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
         * @return the reportableInfo
         */
        public ReportableDoc getReportableInfo() {
                return reportableInfo;
        }

        /**
         * @return the reportablePropertiesInfo
         */
        public List<PropertyDoc> getReportablePropertiesInfo() {
                return reportablePropertiesInfo;
        }

        /**
         * @return the reportableFeaturesDocumenter
         */
        public Map<String, ReportableTypeTraverser> getReportableFeaturesDocumenter() {
                return reportableFeaturesDocumenter;
        }

        /**
         * @param className the className to set
         */
        public void setClassName(String className) {
                this.className = className;
        }

        /**
         * @param reportableInfo the reportableInfo to set
         */
        public void setReportableInfo(ReportableDoc reportableInfo) {
                this.reportableInfo = reportableInfo;
        }

        /**
         * @param reportablePropertiesInfo the reportablePropertiesInfo to set
         */
        public void setReportablePropertiesInfo(
                        List<PropertyDoc> reportablePropertiesInfo) {
                this.reportablePropertiesInfo = reportablePropertiesInfo;
        }

        /**
         * @param reportableFeaturesDocumenter the reportableFeaturesDocumenter to set
         */
        public void setReportableFeaturesDocumenter(
                        Map<String, ReportableTypeTraverser> reportableFeaturesDocumenter) {
                this.reportableFeaturesDocumenter = reportableFeaturesDocumenter;
        }
}
