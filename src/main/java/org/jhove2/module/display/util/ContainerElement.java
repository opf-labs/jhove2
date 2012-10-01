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

package org.jhove2.module.display.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jhove2.module.display.util.ContainerMDWrapper.AttributeName;

/**
 *  ContainerElement
 *
 */
public final class ContainerElement
{
	private Map<AttributeName,Object> attributes = new LinkedHashMap<AttributeName,Object>();
	
	private final String elementName;
	
	private final String value;
	
	/**
	 * Creates a new ContainerElement
	 */
	public ContainerElement(){ this(null, null); }
	public ContainerElement(String name){ this(name, null); }
	public ContainerElement(String name, String value){ this.elementName = name; this.value = value; }
	
	/**
	 * Gets attributes
	 * @return <code>Map</code>
	 */
	public Map<AttributeName,Object> getAttributes()
	{
		return this.attributes;
	}
	
	@Override
	public String toString() {
		return this.toString(null);
	}

	public String toString(String prefix)
	{
		StringBuilder stringElements = new StringBuilder();
		stringElements.append("<");
		if ((prefix != null) && (prefix.length() != 0)) {
			stringElements.append(prefix).append(':');
		}
		stringElements.append(this.elementName);
			
			
		for(Entry<AttributeName,Object> e : attributes.entrySet() )
		{
			AttributeName name = e.getKey();
			Object v = e.getValue();
			if( v != null && (! "".equals(v)))
			{
				stringElements.append(" ");
				stringElements.append(name.key);
				stringElements.append("=");
				stringElements.append("\"");
				stringElements.append(v.toString());
				stringElements.append("\"");
			}
		}
			
		if( this.value != null)
		{
			stringElements.append(">");
			stringElements.append(this.value);
			stringElements.append("</");
			if ((prefix != null) && (prefix.length() != 0)) {
				stringElements.append(prefix).append(':');
			}
			stringElements.append(this.elementName).append(">");
		}else{
			stringElements.append("/>");
		}		
		return stringElements.toString();
	}
}