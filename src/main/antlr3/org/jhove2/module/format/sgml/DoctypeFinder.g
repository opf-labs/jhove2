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

@parser::members {
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
@lexer::members{
  boolean inDocType = false;
  boolean lexFoundPubid = false;
  boolean lexFoundSysid = false;
}

/**
This grammar is a recognizer for the output of the OpenSp sgmlnorm command.  It is intended to discover the
generated DOCTYPE statement, if one exists, in the output of that command.

This command is part of the Open Jade distribution (see http://sourceforge.net/projects/openjade/, 
which is based on James Clark's OpenSP(http://www.jclark.com/sp/index.htm) 

*/


normdoc : doctype? otherstuff;

doctype : (DOCTYPE|DOCTYPELC)  SPACE+ element SPACE+ publicIdDecl? systemIdDecl? ENDDOCTYPESTUFF*  CR? NEWLINE
    {
      if (!foundDoctype){
        foundDoctype = true;
          if ($publicIdDecl.text != null){
            foundPubid = true;
            pubid = lastPubid;
          }
          if ($systemIdDecl.text != null){
            foundSysid = true;
            systemId = lastSystemId;
          }
      }
    };
 
element : (STUFF)+;

publicIdDecl : ((PUBLIC|PUBLICLC) SPACE+ pubDecid SPACE* ){
  lastPubid = $pubDecid.text;
};

pubDecid : (QUOTE quotePubString QUOTE)|(SQUOTE squotePubString SQUOTE);

quotePubString : (~(QUOTE|CR|NEWLINE)+);

squotePubString : (~(SQUOTE|CR|NEWLINE)+);

systemIdDecl : ((SYSTEM|SYSTEMLC) SPACE+ sysid)
{
   lastSystemId = $sysid.text;
};

sysid : (QUOTE quotePubString QUOTE)|(SQUOTE squotePubString SQUOTE);


otherstuff : ~(DOCTYPE|DOCTYPELC|CR|NEWLINE)* CR? NEWLINE? ;


SPACE:  ' ';           //32  x20  
STUFF : ~('\n'|'\r'|' ');
CR :  '\r'{inDocType=false;};
NEWLINE:   '\n'{inDocType=false;lexFoundPubid=false;lexFoundSysid=false;};

DOCTYPE   : {$pos==0}?=> '<!DOCTYPE'{inDocType=true;};
DOCTYPELC : {$pos==0}?=> '<!doctype'{inDocType=true;};
PUBLIC    : {inDocType==true}?=> 'PUBLIC'{lexFoundPubid=true;};
PUBLICLC  : {inDocType==true}?=> 'public'{lexFoundPubid=true;};
SYSTEM    : {inDocType==true}?=> 'SYSTEM'{lexFoundSysid=true;};
SYSTEMLC  : {inDocType==true}?=> 'system'{lexFoundSysid=true;};
QUOTE     : {inDocType==true}?=> '"';
SQUOTE    : {inDocType==true}?=> '\'';
ENDDOCTYPESTUFF  : {inDocType==true && (lexFoundPubid==true||lexFoundSysid==true)}?=> ~('\n'|'\r'|' ');
