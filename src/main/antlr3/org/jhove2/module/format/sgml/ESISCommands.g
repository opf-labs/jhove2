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

grammar ESISCommands;

options {
  language = Java;
}

@parser::header {
package org.jhove2.module.format.sgml;

import java.util.SortedSet;
import java.util.TreeSet;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

import org.jhove2.module.format.sgml.EsisParser;
}

@lexer::header{
package org.jhove2.module.format.sgml;
}

@members {

// charactertistics of interest to the JHOVE2 SGML module
boolean isSgmlValid = false; // is this a conforming SGML file; indicated by presence of "C" command at end of file
String rootElementName; // element name of first startElementCommand

int publicIdCount = 0;
int fileNamesCount = 0;
int sysidsCount = 0;
int extTextEntCount = 0;
int notatDefCount = 0;
int extDataEntCount = 0;
int entrefCount = 0;
int intDataEntCount = 0;
int subDocEntityDefCount = 0;
int subDocCommandCount = 0;
int omitCommandCount = 0;
int elementAttributeCount = 0;
int dataAttrCount = 0;
int linkAttrCount = 0;
int elementCount = 0;  // number of start element commands (same element can occur more than once)
int dataCount = 0;
int includedSubElementsCount = 0;
int emptyElementsCount = 0;
int commentsCount = 0;
int sDataCount = 0;
int piCount = 0;
int appInfoCount = 0;

SortedSet<String> pubIds = new TreeSet<String>();
SortedSet<String> extEntFileNames = new TreeSet<String>();
SortedSet<String> extEntSysidNames = new TreeSet<String>();
SortedSet<String> extTextEntNames = new TreeSet<String>();
SortedSet<String> notatNames = new TreeSet<String>();
SortedSet<String> extDataEntNames = new TreeSet<String>();
SortedSet<String> entRefNames = new TreeSet<String>();
SortedSet<String> subDocEntDefNames = new TreeSet<String>();
SortedSet<String> subDocCommandNames = new TreeSet<String>();
SortedSet<String> elementNames = new TreeSet<String>();      
SortedSet<String> sdataNames = new TreeSet<String>();

HashMap<String, String> intEnt2Value = new HashMap<String, String>();
HashMap<String, String> intEnt2Type = new HashMap<String, String>();

HashMap<String, Integer> intEntType2Count = new HashMap<String, Integer>();
HashMap<String, Integer> elemAttributeType2Count = new HashMap<String, Integer>();
HashMap<String, Integer> dataAttributeType2Count = new HashMap<String, Integer>();
HashMap<String, Integer> linkAttributeType2Count = new HashMap<String, Integer>();

HashMap<String, List<String>> extEntName2dataAttrNames = new HashMap<String, List<String>>();

List<String> progInstructions = new ArrayList<String>();
List<String> appInfos = new ArrayList<String>();

// members and methods to trap any errors during parse so they can be reported
 private List<String> esisParseErrors = new LinkedList<String>();
 public void displayRecognitionError(String[] tokenNames,
                                        RecognitionException e) {
        String hdr = getErrorHeader(e);
        String msg = getErrorMessage(e, tokenNames);
        esisParseErrors.add(hdr + " " + msg);
    }
    public List<String> getEsisParseErrors() {
        return esisParseErrors;
    }

}


/**
This grammar is a recognizer for the output of ONGSMLS, based on James Clark's
OpenSP (http://www.jclark.com/sp/index.htm), which is available at SourceForge 
as part of the OpenJade distribution (see http://sourceforge.net/projects/openjade/).

ONSGMLS "normalizes" SGML output into the ISO 8879 Element Structure Information Set
(ESIS) format (see http://xml.coverpages.org/esisDeseyne.html).

The output is a series of lines. Lines can be arbitrarily long. 
Each line consists of an initial command character and one or more arguments. 
Arguments are separated by a single space, but when a command takes a fixed 
number of arguments the last argument can contain spaces. 
There is no space between the command character and the first argument. 

The comments above the rule for each command are taken from the documentation
that is part of the OpenSP distribution.

 @author smorrissey
 
*/


esis : (publicIdCommand | fileSysIdCommand |sysidCommand| externalTextEntityDefCommand |notationDefCommand|
        externalDataEntityDefCommand|entref|internalDataEntityDefCommand |subDocEntityDefCommand|
        startSubDocCommand | endSubDocCommand|omitCommand | elementAttributeCommand |dataAttributeCommand |
        linkAttributeCommand|startElementCommand |dataCommand |stopElementCommand | includeCommand |
        emptyElementCommand |commentCommand | piCommand |lineNumbCommand | appInfoCommand)+ 
        ((conformingCommand) | EOF);

/**
ppubid 
This command applies to the next E, S, T or N command and specifies the associated public identifier. 
*/
publicIdCommand : pcmd publicId NEWLINE 
          {publicIdCount++; 
           pubIds.add($publicId.text);
          };
pcmd : PCMD;
publicId: (commandChar|OTHERASCII|BCKSLASH|BAR|LESSTHAN|GRTTHAN|SPACE)+ ;

/**
fsysid 
This command applies to the next E, S, T or, if the -onotation-sysid option was specified, N command and 
specifies the system identifier generated by the entity manager from the specified external identifier 
and other information about the entity or notation. 
*/
fileSysIdCommand : fcmd (LESSTHAN storageMgrInfo GRTTHAN)? sysId? NEWLINE 
           {fileNamesCount++; 
            if ($sysId.text != null){
              extEntFileNames.add($sysId.text);
            }
           };
fcmd : FCMD;
storageMgrInfo: (commandChar|OTHERASCII|BCKSLASH|BAR|SPACE)+;
sysId: (commandChar|OTHERASCII|BCKSLASH|BAR|SPACE)+ ;

/**
ssysid 
This command applies to the next E, S, T or N command and specifies the associated system identifier. 
*/
sysidCommand : scmd sysId? NEWLINE 
          {
           sysidsCount++; 
           if ($sysId.text != null)
              extEntSysidNames.add($sysId.text);       
          };
scmd : LSCMD;

/**
Tename 
Define an external SGML text entity named ename. This command will be preceded by an f command 
giving the system identifier generated by the entity manager (unless it was unable to generate one),
by a p command if a public identifier was declared for the entity, 
and by a s command if a system identifier was declared for the entity. 
This command will be output only if the -oentity option is specified.
*/
externalTextEntityDefCommand : tcmd extEntname NEWLINE  
          {extTextEntCount++;
           extTextEntNames.add($extEntname.text);
          };
tcmd : TCMD;
extEntname: (commandChar|OTHERASCII|BCKSLASH|BAR|SPACE)+ ;

/**
Nnname 
Define a notation nname. This command will be preceded by a p command if the notation was 
declared with a public identifier, and by a s command if the notation was declared with a 
system identifier. If the -onotation-sysid option was specified, this command will also be 
preceded by an f command giving the system identifier generated by the entity manager (unless 
it was unable to generate one). A notation will only be defined if it is to be referenced 
in an E command or in an A command for an attribute with a declared value of NOTATION.
*/
notationDefCommand : notatCmd nname NEWLINE 
          {notatDefCount++; 
           notatNames.add($nname.text);
          };
notatCmd : NCMD;
nname: (commandChar|OTHERASCII|BCKSLASH|BAR|SPACE)+;

/**
Eename typ nname 
Define an external data entity named ename with type typ (CDATA, NDATA or SDATA) and notation nname. 
This command will be preceded by an f command giving the system identifier generated by the entity manager 
(unless it was unable to generate one), by a p command if a public identifier was declared for the entity, 
and by a s command if a system identifier was declared for the entity. nname will have been defined using 
a N command. Data attributes may be specified for the entity using D commands. If the -oentity option is 
not specified, an external data entity will only be defined if it is to be referenced in a & command or 
in an A command for an attribute whose declared value is ENTITY or ENTITIES.
*/
externalDataEntityDefCommand : ecmd extDataEntname SPACE etype SPACE nname NEWLINE 
          {extDataEntCount++;
           extDataEntNames.add($extDataEntname.text);
          };
ecmd : ECMD;
extDataEntname : (commandChar|OTHERASCII|BCKSLASH|BAR)+;
etype: (commandChar|OTHERASCII|BCKSLASH|BAR|LESSTHAN|GRTTHAN)+;

/**
&name 
A reference to an external data entity name; name will have been defined using an E command. 
*/
entref : entrefcmd extDataEntname
        {entrefCount++;
         entRefNames.add($extDataEntname.text);
        };
entrefcmd : AMP;

/**
Iename typ text 
Define an internal data entity named ename with type typ and entity text text. 
The typ will be CDATA or SDATA unless the -oentity option was specified, in which case 
it can also be PI or TEXT (for an SGML text entity). If the -oentity option is not 
specified, an internal data entity will only be defined if it is referenced in 
an A command for an attribute whose declared value is ENTITY or ENTITIES
*/
internalDataEntityDefCommand : icmd iename SPACE ietype SPACE ietext NEWLINE 
             {intDataEntCount++; 
              intEnt2Value.put($iename.text, $ietext.text);
              intEnt2Type.put($iename.text, $ietype.text);
              EsisParser.updateMapCounter(intEntType2Count, $ietype.text);
             };
icmd: ICMD;
iename: (commandChar|OTHERASCII|BCKSLASH|BAR|LESSTHAN|GRTTHAN)+;
ietype: (commandChar|OTHERASCII|BCKSLASH|BAR|LESSTHAN|GRTTHAN)+;
ietext: (commandChar|OTHERASCII|BCKSLASH|BAR|LESSTHAN|GRTTHAN|SPACE)+;


/**
Sename 
Define a subdocument entity named ename. This command will be preceded by 
an f command giving the system identifier generated by the entity manager 
(unless it was unable to generate one), by a p command if a public identifier 
was declared for the entity, and by a s command if a system identifier was 
declared for the entity. If the -oentity option is not specified, a subdocument 
entity will only be defined if it is referenced in a { command or in an 
A command for an attribute whose declared value is ENTITY or ENTITIES. 
*/
subDocEntityDefCommand :  subDoccmd subDocEntname NEWLINE
         {subDocEntityDefCount++;
          subDocEntDefNames.add($subDoccmd.text);
         };
subDoccmd : SCMD;
subDocEntname: (commandChar|OTHERASCII|BCKSLASH|BAR|LESSTHAN|GRTTHAN|SPACE)+ ;

/**
{ename 
The start of the SGML subdocument entity ename; ename will have been defined using a S command
*/
startSubDocCommand : startSubcmd subDocEntname NEWLINE
          {
           subDocCommandCount++;
           subDocCommandNames.add($subDocEntname.text);
          };
startSubcmd : LEFTBRACE;


/**
}ename 
The end of the SGML subdocument entity ename.
*/
endSubDocCommand : endSubcmd subDocEntname NEWLINE;
endSubcmd : RIGHTBRACE;


/**
o 
The actual markup for the next (, ), or A command was omitted from the input. This will be output 
only if one of the -oomitted, -otagomit, or -oattromit options is specified
*/
omitCommand : ocmd NEWLINE
        {
         omitCommandCount++;
        };
ocmd : OCMD;

/**
Aname val 
The next element to start has an attribute name with value val which takes one of the following forms: 
  IMPLIED 
      The value of the attribute is implied. 
  CDATA data 
      The attribute is character data. This is used for attributes whose declared value is CDATA. 
  NOTATION nname 
      The attribute is a notation name; nname will have been defined using a N command. 
      This is used for attributes whose declared value is NOTATION. 
  ENTITY name... 
      The attribute is a list of general entity names. Each entity name will have been defined 
      using an I, E or S command. This is used for attributes whose declared value 
      is ENTITY or ENTITIES. 
  TOKEN token... 
      The attribute is a list of tokens. This is used for attributes whose declared value is anything else. 
  ID token 
      The attribute is an ID value. This will be output only if the -oid option is specified. 
      Otherwise TOKEN will be used for ID values. 
  DATA nname data 
      The attribute is character data with an associated notation. The definition of the notation 
      and any applicable attributes of the notation will be given using subsequent Dname val lines. 
      This is used for attributes whose declared value is DATA. It will be output only if the 
      -odata-attribute option is specified. Otherwise CDATA will be used for DATA values. 
*/
elementAttributeCommand : attrcdm attrname SPACE attrType (SPACE attrVal)* NEWLINE
        {
         elementAttributeCount++;
         EsisParser.updateMapCounter(elemAttributeType2Count,$attrType.text);
        };
attrcdm: ACMD;
attrname:(commandChar|OTHERASCII)+;
attrType: (commandChar|OTHERASCII)+;
attrVal:  (commandChar|OTHERASCII|BCKSLASH|BAR|LESSTHAN|GRTTHAN)+;

/**
Dename name val 
This is the same as the A command, except that it specifies a data attribute for an external 
entity named ename. Any D commands will come after the E command that defines the entity to which 
they apply, but before any & or A commands that reference the entity. 
*/
dataAttributeCommand : dataattrcmd extDataEntname SPACE attrname SPACE attrType (SPACE attrVal)* NEWLINE
        {dataAttrCount++; 
         EsisParser.updateMapList(extEntName2dataAttrNames,$extDataEntname.text, $attrname.text);
         EsisParser.updateMapCounter(dataAttributeType2Count,$attrType.text);
        };
dataattrcmd: DCMD;

/**
atype name val 
The next element to start has a link attribute with link type type, name name, and value val, 
which takes the same form as with the A command. 
*/
linkAttributeCommand : linkAttrcmd linkType SPACE attrname SPACE attrType (SPACE attrVal)* NEWLINE
        {linkAttrCount++; 
         EsisParser.updateMapCounter(linkAttributeType2Count,$linkType.text);
        };
linkAttrcmd : LACMD;
linkType :(commandChar|OTHERASCII|BCKSLASH|BAR|LESSTHAN|GRTTHAN)+;

/**
(gi 
The start of an element whose generic identifier is gi. Any attributes for this element 
will have been specified with A commands. 
*/
startElementCommand : startcmd elementName NEWLINE 
        {
         if (elementCount==0){
            rootElementName = $elementName.text;
         }
         elementCount++;
         elementNames.add($elementName.text);
        };
startcmd : LEFTPAREN;
elementName : (commandChar|OTHERASCII|BCKSLASH|BAR|LESSTHAN|GRTTHAN|SPACE)+;

/**
-data 
Data (e.g., the contents of an element). 
*/
dataCommand : dataCmd dataText NEWLINE 
        {dataCount++;};
dataText: (sdataEntity | (commandChar|OTHERASCII|LESSTHAN|GRTTHAN|SPACE|BCKSLASH|BAR))+;
dataCmd : DASH;

/**
)gi 
The end of an element whose generic identifier is gi. 
*/
stopElementCommand : stopcmd elementName NEWLINE;
stopcmd : RIGHTPAREN;

/**
i 
The next element to start is an included subelement. This will be output only if the -oincluded option is specified.
*/
includeCommand : includecmd NEWLINE
        {includedSubElementsCount++;
        };
includecmd: LCICMD;

/**
e 
The next element to start has a declared content of EMPTY or a content reference attribute, and so its end-tag 
must be omitted. This will be output only if the -oempty option is specified.
*/
emptyElementCommand : emptycmd NEWLINE
        {emptyElementsCount++;
        };
emptycmd : LECMD ;

/**
_comment 
A comment with data comment. This will be output only if the -ocomment option is specified. 
*/
commentCommand : commentCmd commentText NEWLINE
        {commentsCount++;
        };
commentCmd: UNDER;
commentText : (commandChar|OTHERASCII|BCKSLASH|BAR|LESSTHAN|GRTTHAN|SPACE)+ ; 

/**
\| 
Internal SDATA entities are bracketed by these. 
*/
sdataEntity : (SDATASEP sdataEntityName SDATASEP) 
           {sDataCount++;
            sdataNames.add($sdataEntityName.text);
           };
sdataEntityName  : (commandChar|OTHERASCII|LESSTHAN|GRTTHAN|SPACE)+ ;

/**
?pi 
A processing instruction with data pi. 
*/
piCommand :picmd piText NEWLINE
        {piCount++;
         progInstructions.add($piText.text);
        };
picmd : PI;       
piText:(commandChar|OTHERASCII|BCKSLASH|BAR|LESSTHAN|GRTTHAN|SPACE)+ ;        

/**
Llineno file 
Llineno 
Set the current line number and filename. The file argument will be omitted if only the line 
number has changed. This will be output only if the -l option has been given
*/
lineNumbCommand : lineCmd lineNumb (SPACE lineFile)? NEWLINE;
lineCmd : LCMD;
lineNumb : OTHERASCII+;
lineFile : (commandChar|OTHERASCII|BCKSLASH|BAR|LESSTHAN|GRTTHAN|SPACE)+;

/**
#text 
An APPINFO parameter of text was specified in the SGML declaration. This is not strictly part of the ESIS, 
but a structure-controlled application is permitted to act on it. No # command will be output if 
APPINFO NONE was specified. A # command will occur at most once, and may be preceded only by a single L command. 
*/
appInfoCommand : appInfocmd appInfoText NEWLINE
        {appInfoCount++;
         appInfos.add($appInfoText.text);
        };
appInfocmd : POUND;
appInfoText : (commandChar|OTHERASCII|BCKSLASH|BAR|LESSTHAN|GRTTHAN|SPACE)+;

/**
C 
This command indicates that the document was a conforming SGML document. If this command is output, 
it will be the last command. An SGML document is not conforming if it references a subdocument 
entity that is not conforming. 
*/
conformingCommand : conformcmd NEWLINE? EOF 
      {
        isSgmlValid=true;
      };
conformcmd : CCMD;
                    
commandChar : (POUND|AMP|LEFTPAREN|RIGHTPAREN|DASH|PI|ACMD|CCMD|DCMD|ECMD|
               ICMD|LCMD|NCMD|SCMD|TCMD|UNDER|LACMD|LECMD|FCMD|LCICMD|OCMD|PCMD|
               LSCMD|LEFTBRACE|RIGHTBRACE);

/** ESIS Command characters */          
POUND:      '#';   //35  x23  
AMP:        '&';   //38  x26
LEFTPAREN:  '(';   //40  x28
RIGHTPAREN: ')';   //41  x29
DASH:       '-';   //45  x2D
PI:         '?';   //63  x3F
ACMD:       'A';   //65  x41
CCMD:       'C';   //67  x43
DCMD:       'D';   //68  x44
ECMD:       'E';   //69  x45
ICMD:       'I';   //74  x49
LCMD:       'L';   //76  x4C
NCMD:       'N';   //78  x4E
SCMD:       'S';   //83  x53
TCMD:       'T';   //84  x54
UNDER:      '_';   //95  x5F
LACMD:      'a';   //97  x61
LECMD:      'e';  //101  x65
FCMD:       'f';  //102  x66
LCICMD:     'i';  //105  x69
OCMD:       'o';  //111  x6f
PCMD:       'p';  //112  x70
LSCMD:      's';  //115  x73
LEFTBRACE:  '{';  //123  x7B
RIGHTBRACE: '}';  //125  x7d
/** ESIS Command characters end*/  

NEWLINE:    '\r'? '\n';  //13  x0D  10  x0A
SPACE:      ' ';   //32  x20
LESSTHAN:   '<';   //60  x3C
GRTTHAN:    '>';   //62  x3E

BCKSLASH:   BCKSLASHCHAR;   //92  x5C
BAR:        BARCHAR;   //124  x7C
SDATASEP: BCKSLASHCHAR BARCHAR;

fragment 
BCKSLASHCHAR:   '\\';   //92  x5C
fragment
BARCHAR:        '|';   //124  x7C

OTHERASCII : UU1|UU2 |UU3|UU3A|UU4|UU5|UU6|UU7|UU7A|UU7B|UU8|UU9|UU10|UU11|
             UU12|UU13|UU14|UU14A|UU15|UU16|UU17|UU18|UU19|UU20|UU21|DIGITS|
             OTHERUU;
fragment
UU1: '\u0009'; //tab
fragment
UU2: '\u000B'..'\u000C';
fragment
UU3: '\u000E'..'\u001F';
fragment
UU3A: '\u0021'..'\u0022';
fragment
UU4: '\u0024'..'\u0025';
fragment
UU5: '\u0027';
fragment
UU6: '\u002A'..'\u002C';
fragment
UU7: '\u002E'..'\u002F';
fragment
DIGITS : '\u0030'..'\u0039'; // 0..9
fragment
UU7A:  '\u003A'..'\u003B';  //: ;
fragment
UU7B: '\u003D';
fragment
UU8: '\u0040';
fragment
UU9: '\u0042';      //B
fragment
UU10: '\u0046'..'\u0048';  //F G H
fragment
UU11: '\u004A'..'\u004B';   //J K
fragment
UU12: '\u004d';  //M
fragment
UU13: '\u004F'..'\u0052'; //O P Q R
fragment
UU14: '\u0055'..'\u005B'; //U V W X Y Z [
fragment
UU14A: '\u005D'..'\u005E';  //]^
fragment
UU15: '\u0060';   //~
fragment
UU16: '\u0062'..'\u0064';  //b c d
fragment
UU17: '\u0067'..'\u0068';  //g h
fragment
UU18: '\u006A'..'\u006E';  // j k l m n
fragment
UU19: '\u0071'..'\u0072';  //q r
fragment
UU20: '\u0074'..'\u007A';  // t u v w x y z
fragment
UU21: '\u007E'..'\u007F';  //~ DEL
fragment
OTHERUU: '\u0080'..'\uFFFF';


