#!/bin/sh
# JHOVE2 -- Next-generation architecture for format-aware characterization
# Copyright 2009-2010 by The Regents of the University of California, Ithaka
# Harbors, Inc., and The Board of Trustees of the Leland Stanford Junior
# University.  All rights reserved.
#
# jhove2_doc.sh -- JHOVE2 Reportable documentation utility for Windows. The
# properties of the reportable (name, identifier, properties, messages) are
# determined by reflection of the class.
#
# usage: ./jhove2_doc reportableBeanName ...
#
# See env.sh for environment variable settings.


. env.sh 

${JAVA} -cp $CP org.jhove2.app.util.ARules $@
