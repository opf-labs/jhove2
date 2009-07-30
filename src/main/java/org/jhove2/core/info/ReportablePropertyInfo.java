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

package org.jhove2.core.info;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.I8R;

/** A reportable property discovered by introspection.
 * 
 * @author mstrong, slabrams
 */
public class ReportablePropertyInfo {
	/** Property description, as defined by the
	 * {@link org.jhove2.annotation.ReportableProperty} annotation
	 * <code>value</code> argument.
	 */
	protected String description;
	
	/** Property generic type, as defined by the generic return type of the
	 * method.  For collection types, this represents the entire nested type.
	 */
	protected Type genericType;
	
	/** Property identifier in the JHOVE2 namespace. */
	protected I8R identifier;
	
	/** Method defining the property. */
	protected Method method;

	/** Property reference, as defined by the
	 * {@link org.jhove2.annotation.ReportableProperty} annotation
	 * <code>ref</code> argument.
	 */
	protected String reference;
	
	/** Instantiate a new <code>ReportablePropertyInfo</code>.
	 * @param identifier  Property identifier in the JHOVE2 namespace
	 * @param method      Method defining the property
	 * @param description Property description, as defined by the
	 *                    {@link org.jhove2.annotation.ReportableProperty}
	 *                    annotation
	 * @param reference   Property reference, as defined by the
	 *                    {@link org.jhove2.annotation.ReportableProperty}
	 *                    annotation
	 */
	public ReportablePropertyInfo(I8R identifier, Method method,
			                      String description, String reference) {
		this.identifier  = identifier;
		this.method      = method;
		this.genericType = method.getGenericReturnType();
		this.description = description;
		this.reference   = reference;
	}
	
	/** Get property description, as defined by the
	 * {@link org.jhove2.annotation.ReportableProperty} annotation
	 * <code>value</code> argument.
	 * @return Property description, or null if not defined
	 */
	public String getDescription() {
		if (this.description.equals(ReportableProperty.DEFAULT)) {
			return null;
		}
		return this.description;
	}
	
	/** Get property identifier in the JHOVE2 namespace.
	 * @return Property identifier in the JHOVE2 namespace
	 */
	public I8R getIdentifier() {
		return this.identifier;
	}
	
	/** Get method defining the property.
	 * @param Method defining the property
	 */
	public Method getMethod() {
		return this.method;
	}
	
	/** Get property generic type, as defined by the generic return type of
	 * the method.  For collection types this represents the entire nested
	 * type.
	 * @return Property generic type
	 */
	public Type getGenericType() {
		return this.genericType;
	}
	
	/** Get property reference, as defined by the
	 * {@link org.jhove2.annotation.ReportableProperty} annotation
	 * <code>value</code> argument.
	 * @return Property description, or null if not defined
	 */
	public String getReference() {
		if (this.reference.equals(ReportableProperty.DEFAULT)) {
			return null;
		}
		return this.reference;
	}
}
