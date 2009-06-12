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

package org.jhove2.core;

import java.io.PrintStream;

/** Interface for JHOVE2 displayers.
 * 
 * @author mstrong, slabrams
 */
public interface Displayable
{
	/** Start display.
	 * @param out   Print stream
	 * @param level Nesting level
	 */
	public void startDisplay(PrintStream out, int level);
	
	/** Start the display of a {@link org.jhove2.Reportable}.
	 * @param out        Print stream
	 * @param level      Nesting level
	 * @param name       Reportable name
	 * @param identifier Property collection identifier in the JHOVE2 namespace
	 * @param order      Ordinal position of this reportable with respect to
	 *                   its enclosing {@link org.jhove2.core.Reportable} or
	 *                   collection
	 */
	public void startReportable(PrintStream out, int level, String name,
			                    I8R identifier, int order);
	
	/** Start display of a property collection.
	 * @param out        Print stream
	 * @param level      Nesting level
	 * @param name       Property collection name
	 * @param identifier Property collection identifier in the JHOVE2 namespace
	 * @param size       Property collection size
	 * @param order      Ordinal position of this property collection with
	 *                   respect to its enclosing {@link org.jhove2.core.Reportable)
	 *                   or collection
	 */
	public void startCollection(PrintStream out, int level, String name,
			                    I8R identifier, int size, int order);
	
	/** Display property.
	 * @param out        Print stream
	 * @param level      Nesting level
	 * @param name       Property name
	 * @param identifier Property identifier in the JHOVE2 namespace
	 * @param value      Property value
	 * @param order      Ordinal position of this property with respect to
	 *                   its enclosing {@link org.jhove2.core.Reportable} or
	 *                   collection
	 */
	public void displayProperty(PrintStream out, int level, String name,
			                    I8R identifier, Object value, int order);
	
	/** End display of a property collection.
	 * @param out        Print stream
	 * @param level      Nesting level
	 * @param name       Property collection name
	 * @param identifier Property identifier in the JHOVE2 namespace
	 * @param size       Property collection size
	 */
	public void endCollection(PrintStream out, int level, String name,
			                  I8R identifier, int size);
	
	/** End display of a {@link org.jhove2.core.Reportable}.
	 * @param out        Print stream
	 * @param level      Nesting level
	 * @param name       Reportable name
	 * @param identifier Reportable identifier in the JHOVE2 namespace
	 */
	public void endReportable(PrintStream out, int level, String name,
			                  I8R identifier);
	
	/** End display.
	 * @param out   Print stream
	 * @param level Nesting level
	 */
	public void endDisplay(PrintStream out, int level);
}
