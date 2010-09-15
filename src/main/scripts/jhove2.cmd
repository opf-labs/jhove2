@ECHO OFF
REM JHOVE2 -- Next-generation architecture for format-aware characterization
REM Copyright 2009-2010 by The Regents of the University of California, Ithaka
REM Harbors, Inc., and The Board of Trustees of the Leland Stanford Junior
REM University.  All rights reserved.
REM
REM jhove2.cmd -- Driver script for main JHOVE2 application under Windows. For
REM usage and configuration information, see the JHOVE2 User's Guide at
REM http://jhove2.org.

setlocal enableextensions

call env

%JAVA% -cp %CP% org.jhove2.app.JHOVE2CommandLine %*

