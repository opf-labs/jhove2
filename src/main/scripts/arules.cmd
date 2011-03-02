@ECHO OFF
REM JHOVE2 -- Next-generation architecture for format-aware characterization
REM Copyright 2009-2010 by The Regents of the University of California, Ithaka
REM Harbors, Inc., and The Board of Trustees of the Leland Stanford Junior
REM University.  All rights reserved.
REM
REM arules -- Converts assessment rule files to bean XML. For more
REM information, refer to the JHOVE2 User's Guide.

setlocal enableextensions

call env

%JAVA% -cp "%CP%" org.jhove2.app.util.ARules %*


