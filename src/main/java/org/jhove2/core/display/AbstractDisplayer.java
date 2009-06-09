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
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.jhove2.annotation.ReportableMessage;
import org.jhove2.annotation.ReportableProperty;
import org.jhove2.annotation.ReportablePropertyComparator;
import org.jhove2.core.AbstractComponent;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.Reporter;
import org.jhove2.core.Identifier;
import org.jhove2.core.message.Message;
import org.jhove2.core.message.ReflectionException;
import org.jhove2.core.message.SpringConfigurationException;
import org.jhove2.core.util.Initializer;
import org.springframework.beans.BeansException;

/** An abstract JHOVE2 displayer.
 * 
 * @author mstrong, slabrams
 */
public abstract class AbstractDisplayer
	extends AbstractComponent
	implements Displayer
{
	/** Displayer visibility map. */
	protected Map<String,Visibility> visible;
	
	/** Initialize a new <code>AbstractDisplayer</code>.
	 * @param version Displayer version identifier
	 * @param date    Displayer release date
	 * @param stage   Displayer development stage
	 */
	public AbstractDisplayer(String version, String date, Stage stage) {
		super(version, date, stage);
		
		try {
			Properties props = Initializer.getProperties("Displayer");
			/* Build the map of reportable property visibility directives. */
			this.visible = new TreeMap<String,Visibility>();
			Set<String> set = props.stringPropertyNames();
			Iterator<String> iter = set.iterator();
			while (iter.hasNext()) {
				String identifier = iter.next();
				String value      = props.getProperty(identifier);

				Visibility visibility = Visibility.valueOf(value);
				if (visibility != null) {
					this.visible.put(identifier, visibility);
				}
			}
		} catch (BeansException e) {
			@ReportableMessage
			SpringConfigurationException msg =
				new SpringConfigurationException(e);
			addMessage(msg);
		}
	}
	
	/** Utility method for setting proper indentation based on nesting level.
	 * @param level
	 * @return
	 */
	public static synchronized String getIndent(int level) {
		StringBuffer indent = new StringBuffer();
		for (int i=0; i<level; i++) {
			indent.append(" ");
		}
		
		return indent.toString();
	}

	/** Print the reportable properties and messages of a
	 * {@link org.jhove2.core.Reporter} to the standard output unit.  If
	 * the Reporter has children, they are also printed recursively.
	 * @param reporter Reporter
	 * @see org.jhove2.core.display.Displayer#print(org.jhove2.core.Reporter)
	 */
	@Override
	public void print(Reporter reporter) {
		print(System.out, reporter);
	}
	
	/** Print the reportable properties and messages of a
	 * {@link org.jhove2.core.Reporter}.  If the Reporter has children, they
	 * are also printed recursively.
	 * @param out      Output print stream
	 * @param reporter Reporter
	 * @see org.jhove2.core.display.Displayer#print(java.io.PrintStream, org.jhove2.core.Reporter)
	 */
	@Override
	public void print(PrintStream out, Reporter reporter) {
		if (reporter instanceof JHOVE2) {
			((JHOVE2) reporter).setDisplayer(this);
		}
		
		setInitialTime();
		
		printStart(out);
		print(out, reporter, 0, 0);
		printEnd(out);
		
		setFinalTime();
	}

	/** Print the reportable properties and messages of a
	 * {@link org.jhove2.core.Reporter}.  If the Reporter has children, they
	 * are also printed recursively.
	 * @param out      Output print stream
	 * @param reporter Reporter
	 * @param level    Nesting level
	 * @param count    Number of properties already displayed for this Reporter
	 */
	public void print(PrintStream out, Reporter reporter, int level, int count) {
		String name = reporter.getReportableName();
		Identifier identifier = reporter.getReportableIdentifier();
		
		printReporterStart(out, level, name, identifier,
				           ((count == 0) ? true : false));
		print(out, reporter, level,
			  (Class<? extends Reporter>) reporter.getClass(), 0);
		printReporterEnd(out, level, name, identifier);
	}

	/** Print the reportable properties and messages of a
	 * {@link org.jhove2.core.Reporter}.  The reportable properties are
	 * defined by marking their accessor methods with the
	 * {@link org.jhove2.annotation.ReportableProperty} annotation in an
	 * interface that extends Reporter.
	 * @param out      Output print stream
	 * @param reporter Reporter
	 * @param level    Nesting level
	 * @param c        AbstractReporter class
	 * @param count    Number of properties already displayed for this Reporter
	 */
	protected void print(PrintStream out, Reporter reporter, int level,
            Class<? extends Reporter> c, int count) {
		boolean isReportable = false;
		/* Check if the class itself defines reportable properties. */
		Method [] dm = c.getDeclaredMethods();
		count += print(out, reporter, level, dm, false, count);
		/* Check if any implemented interfaces define reportable properties. */
		Class<? extends Reporter> [] ifs = (Class<? extends Reporter> []) c.getInterfaces();
		for (int i=0; i<ifs.length; i++) {
			dm = ifs[i].getDeclaredMethods();
			count += print(out, reporter, level, dm, true, count);
			if (ifs[i].getName().equals(Reporter.class.getName())) {
				isReportable = true;
			}
		}
		/* If this class doesn't directly implement the
		 *  {@link org.jhove.core.Reportable} interface, process its
		 *  superclass.
		 */
		if (!isReportable) {
			print(out, reporter, level,
				  (Class<? extends Reporter>) c.getSuperclass(), count);
		}
	}

	/** Print the reportable properties and messages of a
	 * {@link org.jhove2.core.Reporter}.  The reportable properties are
	 * defined by marking their accessor methods with the
	 * {@link org.jhove2.annotation.ReportableProperty} annotation in an
	 * interface that extends Reporter.
	 * @param out         Output print stream
	 * @param reporter    Reporter
	 * @param level       Nesting level
	 * @param dm          Reporter declared methods
	 * @param isInterface True if the methods are declared in an interface,
	 *                    not a class
	 * @param count       Number of properties already displayed for this
	 *                    Reporter
	 * @return Number of properties displayed for this Reporter (including the
	 *         count passed in as a parameter)
	 */
	protected int print(PrintStream out, Reporter reporter, int level,
			            Method [] dm, boolean isInterface, int count) {
		/* Print reportable properties. */
		Set<Method> set = new TreeSet<Method>(new ReportablePropertyComparator());
		for (int j=0; j<dm.length; j++) {
			/* Only properties with accessor methods marked with the
			 * {@link org.jhove2.annotation.ReportableProperty}
			 * annotation are reportable.
			 */
			ReportableProperty rp = dm[j].getAnnotation(ReportableProperty.class);
			if (rp != null) {
				set.add(dm[j]);
			}
		}
		if (set.size() > 0) {
			count = print(out, reporter, level, set, isInterface, count);
		}
		
		/* Print reportable messages. */
		set = new TreeSet<Method>();
		for (int j=0; j<dm.length; j++) {
			/* Only messages with accessor methods marked with the
			 * {@link org.jhove2.annotation.ReportableMessage}
			 * annotation are reportable.
			 */
			ReportableMessage rm = dm[j].getAnnotation(ReportableMessage.class);
			if (rm != null) {
				set.add(dm[j]);
			}
		}
		if (set.size() > 0) {
			print(out, reporter, level, set, isInterface, 0);
		}
		
		return count;
	}

	/** Print the reportable properties and messages of a
	 * {@link org.jhove2.core.Reporter}.  The reportable properties are
	 * defined by marking their accessor methods with the
	 * {@link org.jhove2.annotation.ReportableProperty} annotation in an
	 * interface that extends Reporter.
	 * @param out         Output print stream
	 * @param reporter    Reporter
	 * @param level       Nesting level
	 * @param dm          Set of declared methods
	 * @param isInterface True if the methods are declared in an interface,
	 *                    not a class
	 * @param count       Number of properties already displayed for this
	 *                    Reporter
	 * @return Number of properties displayed for this Reporter (including the
	 *         count passed in as a parameter)
	 */
	protected int print(PrintStream out, Reporter reporter, int level,
			            Set<Method> set, boolean isInterface, int count) {
		Iterator<Method> iter = set.iterator();
		while (iter.hasNext()) {
			Method m = (Method) iter.next();
			String name  = m.getName();
			Object value = null;
			try {
				if (isInterface) {
					m = reporter.getClass().getMethod(name);
				}
				value = m.invoke(reporter);
			} catch (SecurityException e) {
				@ReportableMessage
				ReflectionException msg = new ReflectionException(e);
				addMessage(msg);
			} catch (NoSuchMethodException e) {
				@ReportableMessage
				ReflectionException msg = new ReflectionException(e);
				addMessage(msg);
			} catch (IllegalArgumentException e) {
				@ReportableMessage
				ReflectionException msg = new ReflectionException(e);
				addMessage(msg);
			} catch (IllegalAccessException e) {
				@ReportableMessage
				ReflectionException msg = new ReflectionException(e);
				addMessage(msg);
			} catch (InvocationTargetException e) {
				@ReportableMessage
				ReflectionException msg = new ReflectionException(e);
				addMessage(msg);
			}
			if (value != null) {
				/* Construct the property name and identifier. */
				int n = name.indexOf("get");
				if (n == 0) {
					name = name.substring(3);
				}
				Identifier identifier =
					new Identifier(Identifier.JHOVE2_PREFIX +
							       Identifier.JHOVE2_PROPERTY_INFIX +
							       reporter.getQName() + "/" + name);
				
				/* Print the specific property types. */
				count += print(out, level+1, name, identifier, value, count);
			}
		}
		
		return count;
	}

	/** Print a reportable property.
	 * @param out        Output print stream
	 * @param level      Nesting level
	 * @param name       Property name
	 * @param identifier Property identifier
	 * @param value      Property value
	 * @param count      Number of properties already displayed for this
	 *                   Reporter
	 * @return Number of properties displayed for this Reporter (including the
	 *         count passed in as a parameter)
	 */
	protected int print(PrintStream out, int level, String name,
			            Identifier identifier, Object value, int count) {
		if (this.visible != null) {
			Visibility visibility = this.visible.get(identifier.getValue());
			if (visibility != null) {
				if (visibility == Visibility.Never) {
					return count;
				}
				if (value instanceof Boolean) {
					boolean b = ((Boolean) value).booleanValue();
					if ((visibility == Visibility.IfFalse && b == true) ||
						(visibility == Visibility.IfTrue  && b == false)) {
						return count;
					}
				}
				else if (value instanceof Number) {
					double d = ((Number) value).doubleValue();
					if ((visibility == Visibility.IfNegative    && d >= 0) ||
						(visibility == Visibility.IfNonNegative && d <  0) ||
						(visibility == Visibility.IfNonPositive && d >  0) ||
						(visibility == Visibility.IfNonZero     && d == 0) ||
						(visibility == Visibility.IfPositive    && d <= 0) ||
						(visibility == Visibility.IfZero        && d != 0)) {
						return count;
					}
				}
			}
		}
		
		if (value instanceof Message) {
			/* Treat a message as a scalar value, not a reportable. */
			printProperty(out, level, name, identifier, value,
					      ((count == 0) ? true : false));
			count++;
		}
		else if (value instanceof Reporter) {
			printReporterStart(out, level, name, identifier,
					           ((count == 0) ? true : false));
			print(out, (Reporter) value, level+1, 0);
			printReporterEnd(out, level, name, identifier);
			count++;
		}
		else if (value instanceof List) {
			List<Object> list = (List<Object>) value;
			int size = list.size();
			if (size > 0) {
				String     nm = singularName(name);
				Identifier id = singularIdentifier(identifier);
				printCollectionStart(out, level, name, identifier, size,
						             ((count == 0) ? true : false));
				int ct = 0;
				Iterator<Object> it = list.iterator();
				while (it.hasNext()) {
					Object v = it.next();
					ct += print(out, level+1, nm, id, v, ct);
				}
				printCollectionEnd(out, level, name, identifier);
			}
			count++;
		}
		else if (value instanceof Set) {
			Set<Object> set = (Set<Object>) value;
			int size = set.size();
			if (size > 0) {
				String     nm = singularName(name);
				Identifier id = singularIdentifier(identifier);
				printCollectionStart(out, level, name, identifier, size,
						             ((count == 0) ? true : false));
				int ct = 0;
				Iterator<Object> it = set.iterator();
				while (it.hasNext()) {
					Object v = it.next();
					ct += print(out, level+1, nm, id, v, ct);
				}
				printCollectionEnd(out, level, name, identifier);
			}
			count++;
		}
		else {
			if (value instanceof Date) {
				value = ISO8601.format((Date) value);
			}
			printProperty(out, level, name, identifier, value,
					      ((count == 0) ? true : false));
			count++;
		}
		
		return count;
	}
	
	/** Print the start of the JHOVE2 output.
	 * @param out Output print stream
	 */
	public abstract void printStart(PrintStream out);

	/** Print the start of a reportable.
	 * @param out        Output print stream
	 * @param level      Reporter nesting level
	 * @param name       Reporter name
	 * @param identifier Reporter identifier, in the JHOVE2 namespace
	 * @param first      True if the first child reportable of a Reporter or
	 *                   collection
	 */
	public abstract void printReporterStart(PrintStream out, int level,
			                                String name,
			                                Identifier identifier,
			                                boolean first);
	
	/** Print the start of a collection of properties.
	 * @param out        Output print stream
	 * @param level      Property nesting level
	 * @param name       Property name
	 * @param identifier Property identifier, in the JHOVE2 namespace
	 * @param size       Number of properties in the collection
	 * @param frist      True if the first collection property of a Reporter
	 *                   or collection
	 */
	public abstract void printCollectionStart(PrintStream out, int level,
			                                  String name,
			                                  Identifier identifier,
			                                  int size, boolean first);
	
	/** Print a reportable property.
	 * @param out        Output print stream
	 * @param level      Property nesting level
	 * @param name       Property name
	 * @param identifier Property identifier, in the JHOVE2 namespace
	 * @param value      Property value
	 * @param first      True if this is the first property of a Reporter or collection
	 */
	public abstract void printProperty(PrintStream out, int level, String name,
			                           Identifier identifier, Object value,
			                           boolean first);
	
	/** Print the end of a collection of properties.
	 * @param out        Output print stream
	 * @param level      Property nesting level
	 * @param name       Property name
	 * @param identifier Property identifier, in the JHOVE2 namespace
	 */
	public abstract void printCollectionEnd(PrintStream out, int level,
			                                String name,
			                                Identifier identifier);
	
	/** Print the end of a reportable.
	 * @param out        Output print stream
	 * @param level      Reporter nesting level
	 * @param name       Reporter name
	 * @param identifier Reporter identifier, in the JHOVE2 namespace
	 */
	public abstract void printReporterEnd(PrintStream out, int level,
			                              String name,
			                              Identifier identifier);

	/** Print the end of the JHOVE2 output.
	 * @param out Output print stream
	 */
	public abstract void printEnd(PrintStream out);

	/** Get the singular form of a plural property identifier.
	 * @param identifier Property identifier
	 * @return Singular form of a property identifier
	 */
	public static synchronized Identifier singularIdentifier(Identifier identifier) {
		Identifier singular = null;
		String value = identifier.getValue();
		int in  = value.lastIndexOf('/') + 1;
		int len = value.length();
		if (value.substring(len-3).equals("ies")) {
			singular = new Identifier(value + "/" +
					                  value.substring(in, len-3) + "y");
		}
		else {
			singular = new Identifier(value + "/" +
					                  value.substring(in, len-1));
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
