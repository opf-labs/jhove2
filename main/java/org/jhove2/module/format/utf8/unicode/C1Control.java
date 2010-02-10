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
package org.jhove2.module.format.utf8.unicode;

import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.reportable.ReportableFactory;

/**
 * Unicode C1 control. Controls are initialized from a properties file in the
 * form:
 * 
 * <code>mnemonic codepoint</code>
 * 
 * for the control mnemonic name and code point (in hexadecimal).
 * 
 * @author mstrong, slabrams
 * 
 */
public class C1Control implements Comparable<C1Control> {
	/** Singleton C1 controls. */
	protected static Set<C1Control> controls;

	/** Control code point. */
	protected int codePoint;

	/** Control mnemonic. */
	protected String mnemonic;

	/**
	 * Instantiate a <code>C1Control</code> object.
	 * 
	 * @param mnemonic
	 *            Control mnemonic
	 * @param codePoint
	 *            Control code point
	 */
	public C1Control(String mnemonic, int codePoint) {
		this.codePoint = codePoint;
		this.mnemonic = mnemonic;
	}

	/**
	 * Get the C1 control for a code point.
	 * 
	 * @param codePoint
	 *            Code point
	 * @return Control, or null if the code point is not a C1 control
	 * @throws JHOVE2Exception
	 */
	public static synchronized C1Control getControl(int codePoint)
			throws JHOVE2Exception {
		if (controls == null) {
			/* Initialize the controls from Java Properties File */
			controls = new TreeSet<C1Control>();
			Properties props = ReportableFactory.getProperties("C1Control");
			if (props != null) {
				Set<String> set = props.stringPropertyNames();
				Iterator<String> iter = set.iterator();
				while (iter.hasNext()) {
					String mnemonic = iter.next();
					String value = props.getProperty(mnemonic);

					int point = Integer.parseInt(value, 16);
					C1Control control = new C1Control(mnemonic, point);
					controls.add(control);
				}
			}
		}

		C1Control control = null;
		Iterator<C1Control> iter = controls.iterator();
		while (iter.hasNext()) {
			C1Control ctrl = iter.next();
			if (codePoint == ctrl.getCodePoint()) {
				control = ctrl;
				break;
			}
		}

		return control;
	}

	/**
	 * Get the C1 controls.
	 * 
	 * @return C1 controls
	 */
	public static Set<C1Control> getControls() {
		return controls;
	}

	/**
	 * Get the control character code point.
	 * 
	 * @return Control character code point
	 */
	public int getCodePoint() {
		return this.codePoint;
	}

	/**
	 * Get the control character mnemonic.
	 * 
	 * @return Control character mnemonic
	 */
	public String getMnemonic() {
		return this.mnemonic;
	}

	/**
	 * Convert the C0 control to a Java string, of the form: "mnemonic (0xHH)".
	 * 
	 * @return Java string representation of the control
	 */
	public String toString() {
		String hh = Integer.toHexString(codePoint).toUpperCase();
		if (this.codePoint < 16) {
			hh = "0" + hh;
		}

		return this.getMnemonic() + " (0x" + hh + ")";
	}

	/**
	 * Compare C0 block.
	 * 
	 * @param control
	 *            C0 control to be compared
	 * @return -1, 0, or 1 if this control is less than, equal to, or greater
	 *         than the second
	 */
	@Override
	public int compareTo(C1Control control) {
		int ret = 0;
		int codePoint = control.getCodePoint();
		if (this.codePoint < codePoint) {
			ret = -1;
		} else if (this.codePoint > codePoint) {
			ret = 1;
		}

		return ret;
	}
}
