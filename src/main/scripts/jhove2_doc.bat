@ECHO OFF
REM JHOVE2 - Next-generation architecture for format-aware characterization
REM Copyright 2009 by The Regents of the University of California, Ithaka
REM Harbors, Inc., and The Board of Trustees of the Leland Stanford Junior
REM University.
REM All rights reserved.
REM
REM jhove2_doc.sh  - JHOVE2 Reportable documentation utility. The
REM properties of the reportable (name, identifier, properties, messages) are
REM determined by reflection of the class.
REM
REM usage: org.jhove2.app.util.JHOVE2Doc reportableBeanName ...
REM
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

SET CP=%JHOVE2_HOME%\lib\jhove2-${project.version}.jar;%JHOVE2_HOME%\lib\aopalliance-1.0.jar;%JHOVE2_HOME%\lib\commons-logging-1.1.1.jar;%JHOVE2_HOME%\lib\commons-logging-api-1.1.jar;%JHOVE2_HOME%\lib\jdom-1.0.jar;%JHOVE2_HOME%\lib\junit-4.4.jar;%JHOVE2_HOME%\lib\log4j-1.2.14.jar;%JHOVE2_HOME%\lib\jargs-1.0.jar;%JHOVE2_HOME%\lib\spring-beans-2.5.6.jar;%JHOVE2_HOME%\lib\spring-context-2.5.6.jar;%JHOVE2_HOME%\lib\spring-core-2.5.6.jar;%JHOVE2_HOME%\lib\spring-test-2.5.6.jar;%JHOVE2_HOME%\lib\soap-2.3.1.jar;%JHOVE2_HOME%\lib\xercesImpl-2.9.1.jar;%JHOVE2_HOME%\lib\xml-apis-1.3.04.jar;%JHOVE2_HOME%\lib\xml-resolver-1.2.jar;%JHOVE2_HOME%\lib\antlr-runtime-3.2.jar;%JHOVE2_HOME%\config;%JHOVE2_HOME%\config\droid

REM NOTE: Nothing below this line should be edited
REM #########################################################################

SET ARGS=
:WHILE
IF "%1"=="" GOTO LOOP
  SET ARGS=%ARGS% %1
  SHIFT
  GOTO WHILE
:LOOP

%JAVA% -cp "%CP%" org.jhove2.app.util.JHOVE2Doc %ARGS%

