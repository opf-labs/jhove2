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

package org.jhove2.app;

import java.util.List;
import java.util.Set;

import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.info.ReportableInfo;
import org.jhove2.core.info.ReportablePropertyInfo;
import org.jhove2.core.info.ReportableSourceInfo;
import org.jhove2.core.reportable.ReportableFactory;
import org.jhove2.core.reportable.Reportable;

/**
 * JHOVE2 {@link org.jhove2.core.reportable.Reportable} documentation utility. The
 * properties of the reportable (name, identifier, properties, messages) are
 * determined by reflection of the class.
 * 
 * @author mstrong, slabrams
 */
public class JHOVE2Doc {
	/**
	 * Main entry for JHOVE2 reportable documentation utility.
	 * 
	 * @param args
	 *            Command line arguments: package-qualified reportable class names
	 */
	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("usage: " + JHOVE2Doc.class.getName()
					+ " reportableBeanName ...");
			System.exit(1);
		}

		try {
			int n = 0;
			for (String arg : args) {
//				Class<? extends Reportable> cl =
//					(Class<? extends Reportable>) Class.forName(arg);
				Reportable reportable = ReportableFactory.getReportable(Reportable.class,
						arg);
				ReportableInfo info = new ReportableInfo(reportable);

				if (n > 0) {
					System.out.println();
				}
				System.out.println("Reportable: Name: " + info.getName());
				System.out.println("            Identifier: "
						+ info.getIdentifier());
				System.out.println("            Package: " + info.getPackage());

				List<ReportableSourceInfo> prop2 = info.getProperties();
				for (ReportableSourceInfo source : prop2) {
					System.out.println(" From: " + source.getSource() + " "
							+ source.getName());
					Set<ReportablePropertyInfo> props = source.getProperties();
					for (ReportablePropertyInfo prop : props) {
						String name = prop.getMethod().getName();
						int in = name.indexOf("get");
						if (in == 0) {
							name = name.substring(3);
						}

						String collection = null;
						String type = prop.getGenericType().toString();
						in = type.lastIndexOf('<');
						if (in > 0) {
							collection = type.substring(0, in);
							type = type.substring(in + 1, type.indexOf('>'));
							in = collection.lastIndexOf('.');
							if (in > 0) {
								collection = collection.substring(in + 1);
							}
						}
						in = type.lastIndexOf('.');
						if (in > 0) {
							type = type.substring(in + 1);
						}

						String description = prop.getDescription();
						String reference = prop.getReference();

						String label = "Property";
						if (type.equals("Message")) {
							label = " Message";
						}
						System.out.println("  " + label + ": Name: " + name);
						System.out.println("            Identifier: "
								+ prop.getIdentifier());
						if (collection == null) {
							System.out.println("            Type: " + type);
						} else {
							System.out.println("            Type: "
									+ collection + "<" + type + ">");
						}
						if (description != null) {
							System.out.println("            Description: "
									+ description);
						}
						if (reference != null) {
							System.out.println("            Reference: "
									+ reference);
						}
					}
				}
				n++;
			}
		} 
		catch (JHOVE2Exception e){
			e.printStackTrace();
		}
	}
}
