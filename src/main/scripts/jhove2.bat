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
REM  -t temp     Temporary file directory (default=java.io.tmpdir)
REM  -T  		 Save temporary files
REM  -o file     Output file (default=standard output unit)
REM  -h  Display a help message
REM  file ...    One or more files or directories to be characterized.
REM
REM NOTE: Edit the following lines to refer to the proper file system paths
REM       of the JHOVE2 installation directory and the Java command
REM


REM
REM If JAVA_HOME is not set, C:/WINDOWS/System32/java.exe is used
REM
if "%JAVA_HOME%" == "" (
   set JAVA=java
) else (
   set JAVA="%JAVA_HOME%\bin\java"
)
SET JHOVE2_HOME=%~dp0

REM ECHO JAVA = %JAVA%
REM ECHO JAVA_HOME = %JAVA_HOME%
REM ECHO JHOVE2_HOME = %JHOVE2_HOME%

SET CP=%JHOVE2_HOME%\lib\jhove2-0.6.0.jar;%JHOVE2_HOME%\lib\aopalliance-1.0.jar;%JHOVE2_HOME%\lib\commons-logging-1.1.1.jar;%JHOVE2_HOME%\lib\commons-logging-api-1.1.jar;%JHOVE2_HOME%\lib\jdom-1.0.jar;%JHOVE2_HOME%\lib\junit-4.4.jar;%JHOVE2_HOME%\lib\log4j-1.2.14.jar;%JHOVE2_HOME%\lib\jargs-1.0.jar;%JHOVE2_HOME%\lib\spring-beans-2.5.6.jar;%JHOVE2_HOME%\lib\spring-context-2.5.6.jar;%JHOVE2_HOME%\lib\spring-core-2.5.6.jar;%JHOVE2_HOME%\lib\spring-test-2.5.6.jar;%JHOVE2_HOME%\lib\soap-2.3.1.jar;%JHOVE2_HOME%\lib\xercesImpl-2.9.1.jar;%JHOVE2_HOME%\lib\xml-apis-1.3.04.jar;%JHOVE2_HOME%\lib\xml-resolver-1.2.jar;%JHOVE2_HOME%\lib\antlr-runtime-3.2.jar;%JHOVE2_HOME%\config;%JHOVE2_HOME%\config\droid

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

