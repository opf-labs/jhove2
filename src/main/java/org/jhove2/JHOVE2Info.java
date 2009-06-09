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

package org.jhove2;

import java.io.PrintStream;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.jhove2.annotation.ReportableProperty;
import org.jhove2.core.AbstractReporter;
import org.jhove2.core.Identifier;
import org.jhove2.core.Reporter;
import org.jhove2.core.display.AbstractDisplayer;

/** JHOVE2 class information utility that displays the reportable properties
 * of a class inheriting from {@link org.jhove2.core.AbstractReporter}.
 * 
 * @author mstrong, slabrams
 */
public class JHOVE2Info {
	/** Return code for usage statement. */
	public static final int EUSAGE = 1;
	
	/** Return code for class not found exception. */
	public static final int ECLASSNOTFOUND = -1;
	
	/** JHOVE2 information utility main entry.
	 * @param args Package-qualified class name of a class inheriting from
	 *             {@link org.jhove2.core.AbstractReporter}
	 */
	public static void main(String [] args) {
		/* If no command line arguments, print the application usage statement
		 * to standard output.
		 */
		if (args.length < 1) {
			System.out.println("usage: java " +
					           JHOVE2Info.class.getName() +
					           " class");
			System.exit(EUSAGE);
		}
		
		try {
			Class<? extends AbstractReporter> c =
				(Class<? extends AbstractReporter>) Class.forName(args[0]);
			
			/* Construct the class qualified name. */
			String qName = c.getName().replace('.', '/');
			
			/* Print the class and its reportable properties. */
			System.out.println("Package " + c.getPackage());
			System.out.println("Class   " + c.getSimpleName() + " [" + printIdentifier(qName) + "]");
			printClass(System.out, qName, c);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(ECLASSNOTFOUND);
		}
	}
	
	/** Print reportable properties declared by a class.
	 * @param out   Output print stream
	 * @param qName Package-qualified class name
	 * @param c     Java {@link java.lang.Class}
	 */
	public static void printClass(PrintStream out, String qName,
			                      Class<? extends AbstractReporter> c) {
		/* Print properties directly declared by the class and its
		 * interfaces.
		 */
		printMethods   (System.out, qName, c);
		printInterfaces(System.out, qName, c);
		
		/* Print properties inherited from the superclass. */
		if (!c.getName().equals(AbstractReporter.class.getName())) {
			c = (Class<? extends AbstractReporter>) c.getSuperclass();
			printClass(out, qName, c);
		}
	}
	
	/** Print class identifier.
	 * @param qName Package-qualified class name
	 * @return
	 */
	public static String printIdentifier(String qName) {
		return Identifier.JHOVE2_PREFIX + Identifier.JHOVE2_REPORTER_INFIX +
		       qName;
	}
	
	/** Print reportable property identifier.
	 * @param qName Package-qualified class name
	 * @param name  Property name
	 * @return Property identifier
	 */
	public static String printIdentifier(String qName, String name) {
		return Identifier.JHOVE2_PREFIX + Identifier.JHOVE2_PROPERTY_INFIX +
	           qName + "/" + name;
	}
	
	/** Print reportable properties declared by a classes interfaces.
	 * @param out   Output print stream
	 * @param qName Package-qualified class name
	 * @param c     Java {@link java.lang.Class}
	 */
	public static void printInterfaces(PrintStream out, String qName,
			                           Class<? extends Reporter> c) {
		Class<? extends Reporter> [] ifs =
			(Class<? extends Reporter> []) c.getInterfaces();
		for (int i=0; i<ifs.length; i++) {
			printInterface(out, qName, ifs[i]);
		}
	}
	
	/** Print reportable properties declared by an interface.
	 * @param out   Output print stream
	 * @param qName Package-qualified class name
	 * @param c     Java {@link java.lang.Class} of the interface
	 */
	public static void printInterface(PrintStream out, String qName,
			                          Class<? extends Reporter> c) {
		printMethods(out, qName, c);
	}
	
	/** Print reportable properties declared by a class.
	 * @param out   Output print stream
	 * @param qName Package-qualified class name
	 * @param c     Java {@link java.lang.Class}
	 */
	public static void printMethods(PrintStream out, String qName,
			                        Class<? extends Reporter> c) {
		out.println(" [Reportable properties defined by " +
				    c.getName() + "]");
		Method [] dms = c.getDeclaredMethods();
		for (int i=0; i<dms.length; i++) {
			ReportableProperty rp =
				dms[i].getAnnotation(ReportableProperty.class);
			if (rp != null) {
				printMethod(out, qName, dms[i]);
			}
		}
	}
	
	/** Print reportable properties declared by a method.
	 * @param out   Output print stream
	 * @param qName Package-qualified class name
	 * @param m     Meethod
	 */
	public static void printMethod(PrintStream out, String qName, Method m) {
		String name = m.getName();
		int in = name.indexOf("get");
		if (in > -1) {
			name = name.substring(in+3);
		}
		
		String typeName = m.getReturnType().getSimpleName();
		Type type = m.getGenericReturnType();
		String baseType = null;
		if (type instanceof ParameterizedType) {
			Type [] types = ((ParameterizedType) type).getActualTypeArguments();
			if (types.length > 0) {
				baseType = ((Class<?>) types[0]).getSimpleName();
				typeName += "<" + baseType + ">";
			}
		}
		String id = printIdentifier(qName, name);
		out.println("  " + id + "\t" + typeName + " " + name);
		if (baseType != null) {
			String sd =
				AbstractDisplayer.singularIdentifier(new Identifier(id)).getValue();
			String sn = AbstractDisplayer.singularName(name);
			out.println("  " + sd + "\t" + baseType + " " + sn);
		}
	}
}
