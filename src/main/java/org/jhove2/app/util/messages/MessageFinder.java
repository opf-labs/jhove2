/**
 * JHOVE2 - Next-generation architecture for format-aware characterization
 *
 * Copyright (c) 2012 by The Regents of the University of California,
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

package org.jhove2.app.util.messages;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author smorrissey
 *
 */
public class MessageFinder {
	

	/**
	 * 
	 */
	public MessageFinder() {
		super();
	}
	

//	new Message(Severity.INFO, Context.PROCESS,
//			"org.jhove2.module.format.BaseFormatModule.moduleDoesNotImplementValidatorInterface",
//			jhove2.getConfigInfo());
	public SortedSet<String> findMessageCodes(String javaFilePath) throws IOException{
		
		BufferedReader in		
		   = new BufferedReader(new FileReader(javaFilePath));
		StringBuilder codeText = new StringBuilder();;
		String nextLine = null;
		while (( nextLine=in.readLine())!=null){
			codeText.append(nextLine);
		}
		
		TreeSet<String> messageCodes = new TreeSet<String>();
		int fromIndex = -1;
		int partIndex = -1;
		int firstQuoteIndex = -1;
		int secondQuoteIndex = -1;
		int semiColonIndex = -1;
		
		while ((fromIndex = codeText.indexOf("new Message", fromIndex))> -1){
			partIndex = codeText.indexOf("Severity.",fromIndex);
			if (partIndex == -1){
				fromIndex += "new Message".length();
				continue;
			}
			partIndex = codeText.indexOf("Context.",partIndex);
			if (partIndex == -1){
				fromIndex += "new Message".length();
				continue;
			}
			semiColonIndex = codeText.indexOf(";",partIndex);
			if (semiColonIndex == -1){
				fromIndex += "new Message".length();
				continue;
			}
			firstQuoteIndex = codeText.indexOf("\"",partIndex);
			if (firstQuoteIndex == -1 || firstQuoteIndex > semiColonIndex){
				fromIndex += "new Message".length();
				continue;
			}
			firstQuoteIndex++;
			secondQuoteIndex = codeText.indexOf("\"",firstQuoteIndex);
			if (secondQuoteIndex == -1 || secondQuoteIndex > semiColonIndex){
				fromIndex += "new Message".length();
				continue;
			}
			try {
				String messageCode = codeText.substring(firstQuoteIndex, secondQuoteIndex);
				messageCodes.add(messageCode);
				fromIndex = secondQuoteIndex +1;
			}
			catch (StringIndexOutOfBoundsException e){
				fromIndex += "new Message".length();
				continue;
			}
		}
		if (in != null){
			try{
				in.close();
			}
			catch (Exception e){}
		}
		return messageCodes;
	}


}
