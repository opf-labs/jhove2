#!/bin/sh
# JHOVE2 -- Next-generation architecture for format-aware characterization
# Copyright 2009-2010 by The Regents of the University of California, Ithaka
# Harbors, Inc., and The Board of Trustees of the Leland Stanford Junior
# University.  All rights reserved.
#
# arules.sh -- Converts assessment rule files to bean XML. For more
# information, refer to the JHOVE2 User's Guide.
#
# See env.sh for environment variable settings.


. ./env.sh 

${JAVA} -cp "$CP" org.jhove2.app.util.ARules $@
