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
grammar SgmlParseMessages;

options {
  language = Java;
}

@parser::header {
package org.jhove2.module.format.sgml;

import java.util.List;
import java.util.LinkedList;

import org.jhove2.module.format.sgml.OpenSpMessageParser;
}

@lexer::header{
package org.jhove2.module.format.sgml;
}

@members {
 
int eLevelMessageCount = 0;
int wLevelMessageCount = 0;
int iLevelMessageCount = 0;
int xLevelMessageCount = 0;
int qLevelMessageCount = 0;
int totMessageCount = 0;

List<String> openSpMessages = new ArrayList<String>();

// members and methods to trap any errors during parse so they can be reported
 List<String> sgmlMessagesParseErrors = new LinkedList<String>();

 public void displayRecognitionError(String[] tokenNames,
                                        RecognitionException e) {
        String hdr = getErrorHeader(e);
        String msg = getErrorMessage(e, tokenNames);
        sgmlMessagesParseErrors.add(OpenSpMessageParser.OSPMESSAGEERR + hdr + " " + msg);

 }
 public List<String> getSgmlMessagesParseErrors() {
        return sgmlMessagesParseErrors;
 }
 public boolean isMessageCode (String messageLevel){
    boolean isCode = false;
    if (messageLevel != null){
        if ((messageLevel.equals("E")) ||
            (messageLevel.equals("W")) || 
            (messageLevel.equals("I")) ||
            (messageLevel.equals("Q")) ||
            (messageLevel.equals("X"))
           ){
              isCode = true;
           }    
    }
    return isCode;
 }
}

errMessages : (errMessage)* EOF;

errMessage :  (codedMessage|inLine) RETURN? NEWLINE;

/**
COLON is used as a delimiter, but is also part of paths and of message text

C:
/usr/bin/onsgmls:
C:                                                          3
/sgmlModule/examples/dir01/aipspacebeforedocytpe1_1.sgm:    4
17:                                                         5
73:                                                         6
1844095592.338:                                             7
W:                                                          8
cannot generate system identifier for general entity "uml"  9


C:
/usr/bin/onsgmls:
/sgmlModule/examples/dir01/aipspacebeforedocytpe1_1.sgm:    3
17:                                                         4
73:                                                         5
1844095592.338:                                             6
W:                                                          7
cannot generate system identifier for general entity "uml"  8



/usr/bin/onsgmls:
C:                                                          2
/sgmlModule/examples/dir01/aipspacebeforedocytpe1_1.sgm:    3
17:                                                         4
73:                                                         5
1844095592.338:                                             6
W:                                                          7
cannot generate system identifier for general entity "uml"  8


/usr/bin/onsgmls:
/sgmlModule/examples/dir01/aipspacebeforedocytpe1_1.sgm:    2
17:                                                         3
73:                                                         4
1844095592.338:                                             5
W:                                                          6
cannot generate system identifier for general entity "uml"  7


/usr/bin/onsgmls:
/cygdrive/c/svn_repository/portico-docs/data/RequiredFiles/requiredFiles/Elsevier_Full_Length_Article_DTD_4.3.1/art431.dtd:  2
827:                                                        3
231:                                                        4
relevant clauses:                                           5
ISO 8879:                                                   6
1986 11.2.4p11                                              7

/usr/bin/onsgmls:
/cygdrive/c/svn_repository/portico-docs/data/RequiredFiles/requiredFiles/Elsevier_Full_Length_Article_DTD_4.3.1/art431.dtd:
827:
231:
relevant clauses:
ISO 8879
:1986 11.2.4p11

/usr/bin/sgmlnorm:
/sgmlModule/examples/dir/aipspacebeforedocytpe1_1.02.sgm:
17:
73:
open elements:
ARTICLE FRONT[1] AUTHGRP[1] AUTHOR[1] SURNAME[1] (#PCDATA[1])


/usr/bin/sgmlnorm:
C:
/sgmlModule/examples/dir/aipspacebeforedocytpe1_1.02.sgm:
17:
72:
entity was defined here

*/

codedMessage : tok1 COLON tok2 COLON tok3 COLON tok4 COLON tok5 (COLON tok6 (COLON tok7 (COLON tok8 (COLON tok9)?)?)?)?
{
  
  String messageText=null;
  String messageLevel=null;;
  String messageCode=null;;
  String posNumber=null;;
  String lineNumber=null;;
  String sgmlFilePath=null;;
 
  if ($tok9.text!=null){
    messageText = $tok9.text;
    messageLevel = $tok8.text;
    messageCode = $tok7.text;
    posNumber = $tok6.text;
    lineNumber = $tok5.text;
    sgmlFilePath = $tok3.text.concat(":").concat($tok4.text);
  }
  else if ($tok8.text!=null){
    messageText = $tok8.text;
    messageLevel = $tok7.text;
    messageCode = $tok6.text;
    posNumber = $tok5.text;
    lineNumber = $tok4.text;
    sgmlFilePath = $tok2.text.concat(":").concat($tok3.text);
  }
  else if ($tok7.text!=null){
    messageText = $tok7.text;
    messageLevel = $tok6.text;
    messageCode = $tok5.text;
    posNumber = $tok4.text;
    lineNumber = $tok3.text;
    sgmlFilePath = $tok2.text;
  }
   else if ($tok6.text!=null){
    messageText = "";
    messageLevel = $tok6.text;
    messageCode = $tok5.text;
    posNumber = $tok4.text;
    lineNumber = $tok3.text;
    sgmlFilePath = $tok2.text;
  }
  else {
    messageText = "";
    messageLevel = "";
    messageCode = $tok5.text;
    posNumber = $tok4.text;
    lineNumber = $tok3.text;
    sgmlFilePath = $tok2.text;
  } 
                       
  if (isMessageCode(messageLevel)){   
  
      String messageStr = 
         OpenSpMessageParser.createCodedMessageString(sgmlFilePath,
                 lineNumber, posNumber, 
                  messageText, messageLevel, messageCode);
      openSpMessages.add(messageStr);
 
    if (messageLevel.equals("E")){
         eLevelMessageCount++;
    }
    else if (messageLevel.equals("W")){
         wLevelMessageCount++;
    }
    else if (messageLevel.equals("I")){
         iLevelMessageCount++;
    }
    else if (messageLevel.equals("Q")){
         qLevelMessageCount++;
    }
    else if (messageLevel.equals("X")){
         xLevelMessageCount++;
    }
  } 
  else {
     messageText = messageCode.concat(messageLevel).concat(messageText);
          String messageStr = 
         OpenSpMessageParser.createMessageString(sgmlFilePath,
                 lineNumber, posNumber, 
                  messageText);
        openSpMessages.add(messageStr);
  }
  totMessageCount++;
};
tok1 : (STUFF|SPACE)+;
tok2 : (STUFF|SPACE)+;
tok3 : (STUFF|SPACE)+;
tok4 : (STUFF|SPACE)+;
tok5 : (STUFF|SPACE)+;
tok6 : (STUFF|SPACE)+;
tok7 : (STUFF|SPACE)+;
tok8 : (STUFF|SPACE)+;
tok9 : (STUFF|SPACE)+;

inLine : 'In ' (STUFF|SPACE|COLON)+;

COLON : ':';
SPACE:  ' ';           //32  x20  
STUFF : ~('\n'|'\r'|' '|':');
RETURN :  '\r';
NEWLINE:   '\n';  //13  x0D  10  x0A

