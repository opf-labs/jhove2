@ECHO OFF
REM JHOVE2 -- Next-generation architecture for format-aware characterization
REM Copyright 2009-2010 by The Regents of the University of California, Ithaka
REM Harbors, Inc., and The Board of Trustees of the Leland Stanford Junior
REM University.  All rights reserved.
REM
REM jhove2_doc.cmd -- JHOVE2 Reportable documentation utility for Windows. The
REM properties of the reportable (name, identifier, properties, messages) are
REM determined by reflection of the class.
REM
REM usage: jhove2_doc reportableBeanName ...
REM
REM See env.cmd for environment variable settings.

setlocal enableextensions

call env

%JAVA% -cp %CP% org.jhove2.app.util.JHOVE2Doc %*


