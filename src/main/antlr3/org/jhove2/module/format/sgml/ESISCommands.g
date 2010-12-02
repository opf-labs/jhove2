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

@parser::members {

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
 List<String> esisParseErrors = new LinkedList<String>();

 public void displayRecognitionError(String[] tokenNames,
                                        RecognitionException e) {
        String hdr = getErrorHeader(e);
        String msg = getErrorMessage(e, tokenNames);
        esisParseErrors.add(EsisParser.ESISERR + hdr + " " + msg);
    }
    public List<String> getEsisParseErrors() {
        return esisParseErrors;
    }
}

@lexer::members{
  boolean inData = false;
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


esis : (elementAttributeCommand|startElementCommand|stopElementCommand|dataCommand
                |entref|piCommand|dataAttributeCommand|linkAttributeCommand|notationDefCommand
                |externalDataEntityDefCommand|internalDataEntityDefCommand|subDocEntityDefCommand
                |externalTextEntityDefCommand|sysidCommand|publicIdCommand|fileSysIdCommand
                |startSubDocCommand|endSubDocCommand|lineNumbCommand|appInfoCommand
                |includeCommand|emptyElementCommand|commentCommand|omitCommand)+ conformingCommand? EOF {
                
};



/**
Aname val 
      IMPLIED 
      CDATA data 
      NOTATION nname 
      ENTITY name...
      TOKEN token... 
      ID token 
      DATA nname data 
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

elementAttributeCommand :  ACMD aName  SPACE attrType afterElemAttrType? CR? NEWLINE?
{
         elementAttributeCount++;
         EsisParser.updateMapCounter(elemAttributeType2Count,$attrType.text);
};
aName : (STUFF+);  
attrType : (STUFF+);
afterElemAttrType : (SPACE (sdataEntity|STUFF|SPACE)+ );

/**
Dename name val 
This is the same as the A command, except that it specifies a data attribute for an external 
entity named ename. Any D commands will come after the E command that defines the entity to which 
they apply, but before any & or A commands that reference the entity. 
*/
dataAttributeCommand : DCMD extDataEntname SPACE attrname SPACE attrType afterElemAttrType? CR? NEWLINE?
        {dataAttrCount++; 
         EsisParser.updateMapList(extEntName2dataAttrNames,$extDataEntname.text, $attrname.text);
         EsisParser.updateMapCounter(dataAttributeType2Count,$attrType.text);
        };
extDataEntname : (STUFF+);
attrname : (STUFF+);

/**
atype name val 
The next element to start has a link attribute with link type type, name name, and value val, 
which takes the same form as with the A command. 
*/
linkAttributeCommand : LACMD linkType SPACE attrname SPACE stuffPlus afterElemAttrType? CR? NEWLINE?
        {linkAttrCount++; 
         EsisParser.updateMapCounter(linkAttributeType2Count,$linkType.text);
        };
linkType : (STUFF+);
stuffPlus : (STUFF+);
/**
(gi 
The start of an element whose generic identifier is gi. Any attributes for this element 
will have been specified with A commands. 
*/
startElementCommand : LEFTPAREN elementName CR? NEWLINE?
{    
  if (elementCount==0){
     rootElementName = $elementName.text;
  }
  elementCount++;
  elementNames.add($elementName.text);
};
elementName : (STUFF+);

/**
-data 
Data (e.g., the contents of an element). 
*/
dataCommand : DASH (sdataEntity|STUFF|SPACE)* CR? NEWLINE? 
{
  dataCount++;
};

/**
)gi 
The end of an element whose generic identifier is gi. 
*/
stopElementCommand : RIGHTPAREN (STUFF+) CR? NEWLINE?;

/**
&name 
A reference to an external data entity name; name will have been defined using an E command. 
*/
entref : AMP commandText CR? NEWLINE?
        {entrefCount++;
         entRefNames.add($commandText.text);
        };
commandText : (STUFF|SPACE)*;

/**
?pi 
A processing instruction with data pi. 
*/
piCommand :PI commandText CR? NEWLINE?
        {piCount++;
         progInstructions.add($commandText.text);
        };

/**
Nnname 
Define a notation nname. This command will be preceded by a p command if the notation was 
declared with a public identifier, and by a s command if the notation was declared with a 
system identifier. If the -onotation-sysid option was specified, this command will also be 
preceded by an f command giving the system identifier generated by the entity manager (unless 
it was unable to generate one). A notation will only be defined if it is to be referenced 
in an E command or in an A command for an attribute with a declared value of NOTATION.
*/
notationDefCommand : NCMD commandText CR? NEWLINE? 
          {notatDefCount++; 
           notatNames.add($commandText.text);
          };

/**
Eename typ nname 
Define an external data entity named ename with type typ (CDATA, NDATA or SDATA) and notation nname. 
This command will be preceded by an f command giving the system identifier generated by the entity manager 
(unless it was unable to generate one), by a p command if a public identifier was declared for the entity, 
and by a s command if a system identifier was declared for the entity. nname will have been defined using 
a N command. Data attributes may be specified for the entity using D commands. If the -oentity option is 
not specified, an external data entity will only be defined if it is to be referenced in a & command or 
in an A command for an attribute whose declared value is ENTITY or ENTITIES.
Eename typ nname 
       CDATA
       NDATA
       SDATA
*/
externalDataEntityDefCommand : ECMD extDataEntDefname SPACE STUFF+ afterElemAttrType? CR? NEWLINE?
          {extDataEntCount++;
           extDataEntNames.add($extDataEntDefname.text);
          };          
extDataEntDefname : (STUFF+);

/**
Iename typ text 
Define an internal data entity named ename with type typ and entity text text. 
The typ will be CDATA or SDATA unless the -oentity option was specified, in which case 
it can also be PI or TEXT (for an SGML text entity). If the -oentity option is not 
specified, an internal data entity will only be defined if it is referenced in 
an A command for an attribute whose declared value is ENTITY or ENTITIES
*/

internalDataEntityDefCommand : ICMD iename SPACE ietype afterElemAttrType? CR? NEWLINE?
             {intDataEntCount++; 
              intEnt2Value.put($iename.text, $afterElemAttrType.text);
              intEnt2Type.put($iename.text, $ietype.text);
              EsisParser.updateMapCounter(intEntType2Count, $ietype.text);
             };
iename : STUFF+;
ietype :(STUFF+);

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
subDocEntityDefCommand :  SCMD subDocEntDefName CR? NEWLINE?
         {subDocEntityDefCount++;
          subDocEntDefNames.add($subDocEntDefName.text);
         };
subDocEntDefName : (STUFF+);
/**
Tename 
Define an external SGML text entity named ename. This command will be preceded by an f command 
giving the system identifier generated by the entity manager (unless it was unable to generate one),
by a p command if a public identifier was declared for the entity, 
and by a s command if a system identifier was declared for the entity. 
This command will be output only if the -oentity option is specified.
*/
externalTextEntityDefCommand : TCMD extEntname CR? NEWLINE?  
          {extTextEntCount++;
           extTextEntNames.add($extEntname.text);
          };
extEntname : (STUFF+);

/**
ssysid 
This command applies to the next E, S, T or N command and specifies the associated system identifier. 
*/
sysidCommand : LSCMD sysId=((STUFF|SPACE)+) CR? NEWLINE? 
          {
           sysidsCount++; 
           if ($sysId.text != null)
              extEntSysidNames.add($sysId.text);       
          };

/**
ppubid 
This command applies to the next E, S, T or N command and specifies the associated public identifier. 
*/
publicIdCommand : PCMD publicId  CR? NEWLINE?
          {publicIdCount++; 
           pubIds.add($publicId.text);          
          };
publicId :   (STUFF|SPACE)+ ;    
   
/**
fsysid 
This command applies to the next E, S, T or, if the -onotation-sysid option was specified, N command and 
specifies the system identifier generated by the entity manager from the specified external identifier 
and other information about the entity or notation. 
*/
fileSysIdCommand : FCMD fsysId CR? NEWLINE? 
           {fileNamesCount++; 
            if ($fsysId.text != null){
              extEntFileNames.add($fsysId.text);
            }         
           };
fsysId : (STUFF|SPACE)+ ; 

/**
{ename 
The start of the SGML subdocument entity ename; ename will have been defined using a S command
*/
startSubDocCommand : LEFTBRACE subDocEntname=((STUFF|SPACE)+) CR? NEWLINE?
          {
           subDocCommandCount++;
           subDocCommandNames.add($subDocEntname.text);
           System.out.println("$subDocEntname=" + $subDocEntname.text);
          };
          
/**
}ename 
The end of the SGML subdocument entity ename.
*/
endSubDocCommand : RIGHTBRACE subDocEntname=((STUFF|SPACE)+) CR? NEWLINE?;

/**
Llineno file 
Llineno 
Set the current line number and filename. The file argument will be omitted if only the line 
number has changed. This will be output only if the -l option has been given
*/
lineNumbCommand : LCMD ((STUFF|SPACE)+)CR? NEWLINE?;

/**
#text 
An APPINFO parameter of text was specified in the SGML declaration. This is not strictly part of the ESIS, 
but a structure-controlled application is permitted to act on it. No # command will be output if 
APPINFO NONE was specified. A # command will occur at most once, and may be preceded only by a single L command. 
*/
appInfoCommand : POUND appInfoText=((STUFF|SPACE)+) CR? NEWLINE?
        {appInfoCount++;
         appInfos.add($appInfoText.text);
        };
        
/**
C 
This command indicates that the document was a conforming SGML document. If this command is output, 
it will be the last command. An SGML document is not conforming if it references a subdocument 
entity that is not conforming. 
*/
conformingCommand : CCMD CR? NEWLINE?
      {
        isSgmlValid=true;
      };

/**
i 
The next element to start is an included subelement. This will be output only if the -oincluded option is specified.
*/
includeCommand : LCICMD CR? NEWLINE?
        {includedSubElementsCount++;
        };
   
/**
e 
The next element to start has a declared content of EMPTY or a content reference attribute, and so its end-tag 
must be omitted. This will be output only if the -oempty option is specified.
*/
emptyElementCommand : LECMD CR? NEWLINE?
        {emptyElementsCount++;
        }; 

/**
_comment 
A comment with data comment. This will be output only if the -ocomment option is specified. 
*/
commentCommand : UNDER (STUFF|SPACE)+ CR? NEWLINE?
        {commentsCount++;
        };

/**
o 
The actual markup for the next (, ), or A command was omitted from the input. This will be output 
only if one of the -oomitted, -otagomit, or -oattromit options is specified
*/
omitCommand : OCMD CR? NEWLINE?
        {
         omitCommandCount++;
        };

/**
\| 
Internal SDATA entities are bracketed by these. 
*/
sdataEntity : ('\\|' sdataEntityName '\\|') 
           {sDataCount++;
            sdataNames.add($sdataEntityName.text);
           };
sdataEntityName  : ~('\\|'|'\r'|'\n')+ ;
   

SPACE:  ' ';           //32  x20  
STUFF : ~('\n'|'\r'|' ');
CR :  '\r';
NEWLINE:   '\n';  //13  x0D  10  x0A

  
/** ESIS Command characters */  
POUND     : {$pos==0}?=> '#';    //35  x23  
AMP       : {$pos==0}?=> '&';    //38  x26
LEFTPAREN : {$pos==0}?=> '(';    //40  x28
RIGHTPAREN: {$pos==0}?=> ')';    //41  x29
DASH      : {$pos==0}?=> '-';    //45  x2D
PI        : {$pos==0}?=> '?';    //63  x3F
ACMD      : {$pos==0}?=> 'A';   //65  x41
CCMD      : {$pos==0}?=> 'C';    //67  x43
DCMD      : {$pos==0}?=> 'D';   //68  x44
ECMD      : {$pos==0}?=> 'E';   //69  x45
ICMD      : {$pos==0}?=> 'I';   //74  x49
LCMD      : {$pos==0}?=> 'L';   //76  x4C
NCMD      : {$pos==0}?=> 'N';    //78  x4E
SCMD      : {$pos==0}?=> 'S';    //83  x53
TCMD      : {$pos==0}?=> 'T';    //84  x54
UNDER     : {$pos==0}?=> '_';    //95  x5F
LACMD     : {$pos==0}?=> 'a';   //97  x61
LECMD     : {$pos==0}?=> 'e';   //101  x65
FCMD      : {$pos==0}?=> 'f';   //102  x66
LCICMD    : {$pos==0}?=> 'i';   //105  x69
OCMD      : {$pos==0}?=> 'o';   //111  x6f
PCMD      : {$pos==0}?=> 'p';   //112  x70
LSCMD     : {$pos==0}?=> 's';   //115  x73
LEFTBRACE : {$pos==0}?=> '{';   //123  x7B
RIGHTBRACE: {$pos==0}?=> '}';   //125  x7d
