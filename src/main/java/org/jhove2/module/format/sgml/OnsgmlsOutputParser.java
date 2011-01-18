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
package org.jhove2.module.format.sgml;

import java.io.IOException;

import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.source.Source;

/**
 * Interface for parsers of output of ONGSMLS, based on James Clark's
 * OpenSP (http://www.jclark.com/sp/index.htm), which is available at SourceForge 
 * as part of the OpenJade distribution (see http://sourceforge.net/projects/openjade/).
 * 
 * ONSGMLS "normalizes" SGML output into the ISO 8879 Element Structure Information Set
 * (ESIS) format (see http://xml.coverpages.org/esisDeseyne.html).
 * 
 * The output is a series of lines. Lines can be arbitrarily long. 
 * Each line consists of an initial command character and one or more arguments. 
 * Arguments are separated by a single space, but when a command takes a fixed 
 * number of arguments the last argument can contain spaces. 
 * There is no space between the command character and the first argument. 
 * 
 * 
 * @author smorrissey
 *
 */
public interface OnsgmlsOutputParser {

	/** prefix for parser error messages to indicate which grammar generated parse errors */
	public static final String ESISERR = "ESIS: ";

	/**
	 * Invokes ANTLR parser on ESIS file passed as input parameter.
	 * Returns parser object which, after parse, maintains accumulated
	 * feature information about the SGML file, which will be accessed
	 * by the SgmlModule class to report properties about the file
	 * @param esisPath String containing path to onsmls ESIS output
	 * @param sgm SgmlModule to which messages may be attached
	 * @param JHOVE2 jhove2 object with ConfigInfo
	 * @param Source object to which messages may be attached
	 * @return SgmlDocumentProperties extracted properties
	 * @throws JHOVE2Exception
	 * @throws IOException 
	 * @throws RecognitionException 
	 */
	public SgmlDocumentProperties parseEsisFile(String esisPath, JHOVE2 jhove2,
			Source source, SgmlModule sgm) throws JHOVE2Exception, IOException;

}