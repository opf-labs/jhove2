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
}

/**
This grammar is a recognizer for the output of the OpenSp sgmlnorm command.  It is intended to discover the
generated DOCTYPE statement, if one exists, in the output of that command.

This command is part of the Open Jade distribution (see http://sourceforge.net/projects/openjade/, 
which is based on James Clark's OpenSP(http://www.jclark.com/sp/index.htm) 

*/


normdoc : doctype? otherstuff;


doctype : (DOCTYPE|DOCTYPELC)  SPACE+ element SPACE+ publicIdDecl? systemIdDecl? CR? NEWLINE
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
NEWLINE:   '\n'{inDocType=false;};

DOCTYPE   : {$pos==0}?=> '<!DOCTYPE'{inDocType=true;};
DOCTYPELC : {$pos==0}?=> '<!doctype'{inDocType=true;};
PUBLIC    : {inDocType==true}?=> 'PUBLIC';
PUBLICLC  : {inDocType==true}?=> 'public';
SYSTEM    : {inDocType==true}?=> 'SYSTEM';
SYSTEMLC  : {inDocType==true}?=> 'system';
QUOTE     : {inDocType==true}?=> '"';
SQUOTE    : {inDocType==true}?=> '\'';
