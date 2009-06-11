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

package org.jhove2.core.display;

import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jhove2.annotation.Reportable;
import org.jhove2.core.AbstractModule;
import org.jhove2.core.Reporter;
import org.jhove2.core.util.Info;

/** An abstract JHOVE2 {@link org.jhove2.core.display.Displayer}.
 * 
 * @author mstrong, slabrams
 */
@Reportable("Abstract displayer.")
public abstract class AbstractDisplayer
	extends AbstractModule
	implements Displayer
{
	/** Instantiate a new <code>AbstractDisplayer</code>.
	 * @param version Displayer version identifier
	 * @param date    Displayer release date
	 * @param rights  Displayer rights statement 
	 */
	public AbstractDisplayer(String version, String date, String rights) {
		super(version, date, rights);
	}
	
	/** Display a {@link org.jhove2.core.Reporter} to the standard output
	 * stream.
	 * @param reporter Reporter
	 * @see org.jhove2.core.display.Displayer#display(org.jhove2.core.Reporter)
	 */
	@Override
	public void display(Reporter reporter) {
		display(System.out, reporter);
	}
	
	/** Display a {@link org.jhove2.core.Reporter}.
	 * @param reporter Reporter
	 * @see org.jhove2.core.display.Displayer#display(java.io.PrintStream,org.jhove2.core.Reporter)
	 */
	@Override
	public void display(PrintStream out, Reporter reporter) {
		display(out, reporter, 0);
	}
	
	/** Display a {@link org.jhove2.core.Reporter}.
	 * @param reporter Reporter
	 * @param level    Nesting level
	 */
	protected void display(PrintStream out, Reporter reporter, int level) {
		String indent = getIndent(level);
		Info   info   = new Info(reporter);
		
		System.out.println(indent + info.getName() + ":");
		List<Set<Method>> list = info.getProperties();
		Iterator<Set<Method>> iter = list.iterator();
		while (iter.hasNext()) {
			Set<Method> methods = iter.next();
			Iterator<Method> it2 = methods.iterator();
			while (it2.hasNext()) {
				Method method = it2.next();
				String name = method.getName();
				if (name.indexOf("get") == 0) {
					name = name.substring(3);
				}
				
				try {
					Object value = method.invoke(reporter);
					out.println(indent + " " + name + ": " + value);
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	/** Get the indention associated with a nesting level.
	 * @param level Nesting level
	 * @return Indentation associated with a nesting level
	 */
	protected static synchronized String getIndent(int level) {
		StringBuffer indent = new StringBuffer();
		for (int i=0; i<level; i++) {
			indent.append(" ");
		}
		
		return indent.toString();
	}
}
