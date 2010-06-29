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
}


errMessages : errMessage* EOF;

errMessage :  (codedMessage|uncodedMessage) NEWLINE?
  {
    totMessageCount++;
  };

codedMessage : 
  cmdPath COLON sgmlFilepath COLON lineNumber COLON posNumber COLON somenumber DOT messageCode COLON messageLevel COLON messagetext 
  {
    String messageStr = 
        OpenSpMessageParser.createCodedMessageString($sgmlFilepath.text,
                $lineNumber.text, $posNumber.text, 
                 $messagetext.text, $messageLevel.text, $messageCode.text);
    openSpMessages.add(messageStr);
    String level = $messageLevel.text;
    if (level.equals("E")){
         eLevelMessageCount++;
    }
    else if (level.equals("W")){
         wLevelMessageCount++;
    }
    else if (level.equals("I")){
         iLevelMessageCount++;
    }
    else if (level.equals("Q")){
         qLevelMessageCount++;
    }
    else if (level.equals("X")){
         xLevelMessageCount++;
    }
  };

uncodedMessage : cmdPath COLON sgmlFilepath COLON lineNumber COLON posNumber COLON messagetext 
  {
       String messageStr = 
        OpenSpMessageParser.createMessageString($sgmlFilepath.text,
                $lineNumber.text, $posNumber.text, 
                 $messagetext.text);
       openSpMessages.add(messageStr);
  };

cmdPath :  unixPath | winpath ;

sgmlFilepath : unixPath | winpath ;

unixPath : (SLASH (~(SLASH|NEWLINE|COLON|EOF))*)+;

winpath : drive ((SLASH |BCKSLASH) (~(SLASH|BCKSLASH|NEWLINE|COLON|EOF))*)+ ;

drive : (MESSAGELEVEL|OTHERALPHA) COLON;

lineNumber : DIGIT+;

posNumber : DIGIT+;

somenumber : DIGIT+;

messageCode : DIGIT+;

messageLevel : MESSAGELEVEL;

messagetext : SPACE (COLON|DOT|SLASH|BCKSLASH|MESSAGELEVEL|DIGIT|OTHERALPHA|OTHERCHAR|SPACE)*;


NEWLINE       :    '\r'? '\n';  // x0D   x0A
SPACE         :    ' ';         // x20
COLON         :    ':';         //003A
DOT           :    '.';         //U+002E
SLASH         :    '/';         //U+002F
BCKSLASH      :    '\\';        //U+005C
MESSAGELEVEL  :    MESSAGELEVELS;
DIGIT         :    DIGITS;
OTHERALPHA    :    OTHERALPHAS;
OTHERCHAR     :    U2|U3|U4|U5|U6;

fragment
MESSAGELEVELS : 'E'|'W'|'I'|'Q'|'X';
fragment
DIGITS        : '0'..'9';
fragment
OTHERALPHAS   : 'a'..'z'|'A'..'D'|'F'..'H'|'J'..'P'|'R'..'V'|'Y'|'Z';


fragment
U2 : '\u0021'..'\u002D';
fragment
U3 : '\u003B'..'\u0040';
fragment
U4:  '\u005B';
fragment
U5:  '\u005D'..'\u0060';
fragment
U6 : '\u007B'..'\uFFFF' ;

