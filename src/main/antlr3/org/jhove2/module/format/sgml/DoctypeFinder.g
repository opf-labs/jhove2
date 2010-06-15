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

grammar DoctypeFinder;

options {
  language = Java;
}
@parser::header {
package org.jhove2.module.format.sgml;

import java.util.LinkedList;
import java.util.List;

import org.jhove2.module.format.sgml.DoctypeParser;
}

@lexer::header{
package org.jhove2.module.format.sgml;
}

@members {
  boolean foundDoctype = false;
  boolean foundPubid = false;
  boolean foundSysid = false;
  String pubid = null;
  String lastPubid = null;
  String systemId = null;
  String lastSystemId = null;
  

  // members and methods to trap any errors during parse so they can be reported
 private List<String> doctypeFinderParseErrors = new LinkedList<String>();

 public void displayRecognitionError(String[] tokenNames,
                                        RecognitionException e) {
        String hdr = getErrorHeader(e);
        String msg = getErrorMessage(e, tokenNames);
        doctypeFinderParseErrors.add(DoctypeParser.DOCTYPEERR + hdr + " " + msg);
    }
    public List<String> getDoctypeFinderParseErrors() {
        return doctypeFinderParseErrors;
    }
}

/**
This grammar is a recognizer for the output of the OpenSp sgmlnorm command.  It is intended to discover the
generated DOCTYPE statement, if one exists, in the output of that command.

This command is part of the Open Jade distribution (see http://sourceforge.net/projects/openjade/, 
which is based on James Clark's OpenSP(http://www.jclark.com/sp/index.htm) 

*/
normdoc : doctype? otherstuff;

doctype : LESSTHAN BANG DCHAR OCHAR CCHAR TCHAR YCHAR PCHAR ECHAR  SPACE+ element (SPACE+ pubidDecl)? (SPACE+ sysidDecl)? 
    {
      if (!foundDoctype){
        foundDoctype = true;
          if ($pubidDecl.text != null){
            foundPubid = true;
            String tmpPubid = lastPubid;
            pubid = tmpPubid;
          }
          if ($sysidDecl.text != null){
            foundSysid = true;
            String tmpSysid = lastSystemId;
            systemId = tmpSysid;
          }
      }
    };
 
element : (OTHERCHARS|BCHAR |CCHAR|DCHAR |ECHAR|ICHAR|LCHAR |MCHAR |OCHAR |PCHAR|SCHAR|TCHAR|UCHAR|YCHAR)+ ;

pubidDecl : PCHAR UCHAR BCHAR LCHAR ICHAR CCHAR SPACE+ pubid  ;

pubid : QUOTE (OTHERCHARS|BCHAR |CCHAR|DCHAR |ECHAR|ICHAR|LCHAR |MCHAR |OCHAR |PCHAR|SCHAR|TCHAR|UCHAR|YCHAR|SPACE)+ QUOTE 
      {
        lastPubid = $pubid.text;
      };

sysidDecl :  SCHAR YCHAR SCHAR TCHAR ECHAR MCHAR SPACE+ sysid;   
sysid : QUOTE (OTHERCHARS|BCHAR |CCHAR|DCHAR |ECHAR|ICHAR|LCHAR |MCHAR |OCHAR |PCHAR|SCHAR|TCHAR|UCHAR|YCHAR|PUBLIC|SYSTEM|SPACE)+ QUOTE 
      {
        lastSystemId = $sysid.text;
      };


otherstuff : (~(LESSTHAN BANG DCHAR OCHAR CCHAR TCHAR YCHAR PCHAR ECHAR) )? (OTHERCHARS|BCHAR |CCHAR|DCHAR |ECHAR|ICHAR|LCHAR |MCHAR |OCHAR |PCHAR|
                           SCHAR|TCHAR|UCHAR|YCHAR|LESSTHAN|BANG|QUOTE|DOCTYPEENDTAG|SPACE|NEWLINE)* EOF;



LESSTHAN : '<';                       //u003C
BANG : '!';  
QUOTE   : '"' | '\'';                 //u0022, u0027
DOCTYPEENDTAG  : '>'  ;               //u003E
SPACE   : ' ' | '\t'  ;               //u0020, u0009
NEWLINE  :    '\r'? '\n';             //u000D  u000A
BCHAR : B;
CCHAR  :C;
DCHAR : D;
ECHAR : E;
ICHAR : I;
LCHAR : L;
MCHAR : M;
OCHAR : O;
PCHAR : P;
SCHAR : S;
TCHAR : T;
UCHAR : U;
YCHAR : Y;
OTHERCHARS : U2 |U3 |U4 |U5 |U6 |U7 |U8 |
             U9 |U10 |U11|U12|U13|U14|U15|U16|U17|;

                         //u0021
fragment 
B : 'B'|'b';        //u0042     u0062
fragment 
C : 'C'|'c';        //u0043     u0063     
fragment
D : 'D'|'d';        //u0044     u0064
fragment
E : 'E'|'e';        //u0045     u0065
fragment 
I : 'I'|'i';        //u0049     u0069
fragment 
L : 'L'|'l';        //u004C     u006C
fragment
M : 'M'|'m';        //u004D     u006D
fragment
O : 'O'|'o';        //u004F     u006F
fragment  
P : 'P'|'p';        //u0050     u0070
fragment
S : 'S'|'s';        //u0053     u0073
fragment
T : 'T'|'t';        //u0054     u0074
fragment 
U : 'U'|'u';        //u0055     u0075
fragment
Y : 'Y'|'y';        //u0059     u0079


fragment
U2 : '\u0023'..'\u0026';
fragment
U3 : '\u0028'..'\u003B';
fragment
U4: '\u003D';
fragment
U5 : '\u003F'..'\u0041' ;
fragment
U6 :  '\u0046'..'\u0048' ;
fragment
U7 :  '\u004A'..'\u004B' ;
fragment
U8 : '\u004E';
fragment
U9:  '\u0051'..'\u0052' ;
fragment
U10 : '\u0056'..'\u0058' ;
fragment
U11 : '\u005A'..'\u0061' ;
fragment
U12 :  '\u0066'..'\u0068' ;
fragment
U13 :  '\u006A'..'\u006B' ;
fragment
U14 : '\u006E';
fragment
U15:  '\u0071'..'\u0072' ;
fragment
U16 : '\u0076'..'\u0078' ;
fragment
U17 : '\u007A'..'\uFFFF' ;
