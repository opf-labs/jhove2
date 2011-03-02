@ECHO OFF
REM JHOVE2 -- Next-generation architecture for format-aware characterization
REM Copyright 2009-2010 by The Regents of the University of California, Ithaka
REM Harbors, Inc., and The Board of Trustees of the Leland Stanford Junior
REM University.  All rights reserved.
REM
REM jhove2_upfg.cmd -- Generates editable Java properties file for units of 
REM measure settings for Reportable features that have a numeric type.
REM
REM USAGE:  jhove2_upfg.cmd fully-qualified-class-name output-dir-path
REM
REM See env.cmd for environment variable settings.
REM 

setlocal enableextensions

call env

%JAVA% -cp "%CP%" org.jhove2.app.util.UnitsPropertyFileGenerator %*
