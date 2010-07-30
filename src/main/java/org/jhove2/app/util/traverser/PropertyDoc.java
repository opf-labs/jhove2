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

import java.lang.reflect.Type;
/**
 * Wrapper for information about a reportable property
 * @author smorrissey
 *
 */
public class PropertyDoc implements Comparable<Object>{
	String name;
	String id;
	String desc;
	String ref;
	String dottedName;
	String typeString;
	Class<?> type;
	Type gType;
	String stringValue;
	
	@Override
	public int compareTo(Object o) {
		int retcode = -1;
		if (o instanceof PropertyDoc){
			PropertyDoc p = (PropertyDoc)o;
			retcode = dottedName.compareTo(p.dottedName);
		}
		return retcode;
	}
	@Override
	public boolean equals(Object o){
		boolean isEqual = false;
		if (o instanceof PropertyDoc){
			PropertyDoc p = (PropertyDoc)o;
			isEqual = dottedName.equals(p.dottedName);
		}
		return isEqual;	
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}
	/**
	 * @return the ref
	 */
	public String getRef() {
		return ref;
	}
	/**
	 * @return the dottedName
	 */
	public String getDottedName() {
		return dottedName;
	}
	/**
	 * @return the typeString
	 */
	public String getTypeString() {
		return typeString;
	}
	/**
	 * @return the type
	 */
	public Class<?> getType() {
		return type;
	}
	/**
	 * @return the gType
	 */
	public Type getgType() {
		return gType;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}
	/**
	 * @param ref the ref to set
	 */
	public void setRef(String ref) {
		this.ref = ref;
	}
	/**
	 * @param dottedName the dottedName to set
	 */
	public void setDottedName(String dottedName) {
		this.dottedName = dottedName;
	}
	/**
	 * @param typeString the typeString to set
	 */
	public void setTypeString(String typeString) {
		this.typeString = typeString;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(Class<?> type) {
		this.type = type;
	}
	/**
	 * @param gType the gType to set
	 */
	public void setgType(Type gType) {
		this.gType = gType;
	}
	/**
	 * @return the stringValue
	 */
	public String getStringValue() {
		return stringValue;
	}
	/**
	 * @param stringValue the stringValue to set
	 */
	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}
	
}
