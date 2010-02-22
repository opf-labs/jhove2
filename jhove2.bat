@ECHO OFF
REM JHOVE2 - Next-generation architecture for format-aware characterization
REM Copyright 2009 by The Regents of the University of California, Ithaka
REM Harbors, Inc., and The Board of Trustees of the Leland Stanford Junior
REM University.
REM All rights reserved.
REM
REM Usage: jhove2 [-ik] [-b size] [-B Direct|NonDirect|Mapped]
REM               [-d JSON|Text|XML] [-f limit] [-t temp] [-T] [-o file] file ...
REM
REM  -i  Show the unique formal identifiers for all reportable properties in results.
REM  -k  Calculate message digests.
REM  -b size     I/O buffer size (default=131072)
REM  -B scope     I/O buffer type (default=Direct)
REM  -d format   Results format (default=Text)
REM  -f limit    Fail fast limit (default=0; no limit on the number of reported errors.
REM  -t temp     Temporary directory (default=java.io.tmpdir)
REM  -T  Delete temporary files
REM  -o file     Output file (default=standard output unit)
REM  -h  Display a help message
REM  file ...    One or more files or directories to be characterized.
REM
REM NOTE: Edit the following lines to refer to the proper file system paths
REM       of the JHOVE2 installation directory and the Java command
REM

SET JHOVE2_HOME="C:\Program Files\jhove2"
SET JAVA="C:\Program Files\Java\jdk1.6.0_14\bin\java"

SET CP=%JHOVE2_HOME%\lib\jhove2-1.9.5.jar;%JHOVE2_HOME%\aopalliance\aopalliance\1.0\aopalliance-1.0.jar;%JHOVE2_HOME%\commons-logging\commons-logging\1.1.1\commons-logging-1.1.1.jar;%JHOVE2_HOME%\commons-logging\commons-logging-api\1.1\commons-logging-api-1.1.jar;%JHOVE2_HOME%\jdom\jdom\1.0\jdom-1.0.jar;%JHOVE2_HOME%\junit\junit\4.4\junit-4.4.jar;%JHOVE2_HOME%\log4j\log4j\1.2.14\log4j-1.2.14.jar;%JHOVE2_HOME%\net\sf\jargs\1.0\jargs-1.0.jar;%JHOVE2_HOME%\org\springframework\spring-beans\2.5.6\spring-beans-2.5.6.jar;%JHOVE2_HOME%\org\springframework\spring-context\2.5.6\spring-context-2.5.6.jar;%JHOVE2_HOME%\org\springframework\spring-core\2.5.6\spring-core-2.5.6.jar;%JHOVE2_HOME%\org\springframework\spring-test\2.5.6\spring-test-2.5.6.jar;%JHOVE2_HOME%\soap\soap\2.3.1\soap-2.3.1.jar;%JHOVE2_HOME%\xerces\xercesImpl\2.9.1\xercesImpl-2.9.1.jar;%JHOVE2_HOME%\xml-apis\xml-apis\1.3.04\xml-apis-1.3.04.jar;%JHOVE2_HOME%\xml-resolver\xml-resolver\1.2\xml-resolver-1.2.jar;%JHOVE2_HOME%\config;%JHOVE2_HOME%\config\droid

REM NOTE: Nothing below this line should be edited
REM #########################################################################

SET ARGS=
:WHILE
IF "%1"=="" GOTO LOOP
  SET ARGS=%ARGS% %1
  SHIFT
  GOTO WHILE
:LOOP

%JAVA% -cp %CP% org.jhove2.app.JHOVE2CommandLine %ARGS%

