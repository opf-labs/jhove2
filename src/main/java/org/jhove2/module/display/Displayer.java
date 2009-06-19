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

package org.jhove2.module.display;

import org.jhove2.core.I8R;

/** JHOVE2 displayer utility.
 * 
 * @author mstrong, slabrams
 */
public class Displayer {
	/** Get indentation appropriate for a nesting level.
	 * @param level Nesting level
	 */
	public static synchronized String getIndent(int level) {
		StringBuffer indent = new StringBuffer();
		for (int i=0; i<level; i++) {
			indent.append(" ");
		}
		
		return indent.toString();
	}

	/** Get the singular form of a plural property identifier.
	 * @param identifier Property identifier
	 * @return Singular form of a property identifier
	 */
	public static synchronized I8R singularIdentifier(I8R identifier) {
		I8R singular = null;
		String value = identifier.getValue();
		int in  = value.lastIndexOf('/') + 1;
		int len = value.length();
		if (value.substring(len-3).equals("ies")) {
			singular = new I8R(value + "/" + value.substring(in, len-3) + "y");
		}
		else {
			singular = new I8R(value + "/" + value.substring(in, len-1));
		}
		
		return singular;
	}

	/** Get the singular form of a plural property name.
	 * @param name Property name
	 * @return Singular form of a property name
	 */
	public static synchronized String singularName(String name) {
		String singular = null;
		int len = name.length();
		if (name.substring(len-3).equals("ies")) {
			singular = name.substring(0, len-3) + "y";
		}
		else {
			singular = name.substring(0, len-1);
		}
		
		return singular;
	}
}
