#!/bin/sh
# JHOVE2 -- Next-generation architecture for format-aware characterization
# Copyright 2009-2010 by The Regents of the University of California, Ithaka
# Harbors, Inc., and The Board of Trustees of the Leland Stanford Junior
# University.  All rights reserved.
#
# jhove2_upfg.sh -- Generates editable Java properties file for units of 
# measure settings for Reportable features that have a numeric type.
#
# USAGE:  ./jhove2_upfg.sh fully-qualified-class-name output-dir-path
#
# See env.sh for environment variable settings.
# 


. ./env.sh

${JAVA} -cp "$CP" org.jhove2.app.util.UnitsPropertyFileGenerator $@
