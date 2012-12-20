#!/bin/sh
# JHOVE2 -- Next-generation architecture for format-aware characterization
# Copyright 2009-2010 by The Regents of the University of California, Ithaka
# Harbors, Inc., and The Board of Trustees of the Leland Stanford Junior
# University.  All rights reserved.
#
# jhove2.sh -- Driver script for main JHOVE2 application under Windows. For
# usage and configuration information, see the JHOVE2 User's Guide at
# http://jhove2.org.

ProgDir=`dirname "$0"`
. "${ProgDir}/env.sh"

if [ -z "${JAVA_OPTS}" ]; then
  JAVA_OPTS="-Xms256m -Xmx1024m -XX:PermSize=64M -XX:MaxPermSize=256M"
fi

"${JAVA}" ${JAVA_OPTS} -cp "$CP" org.jhove2.app.JHOVE2CommandLine "$@"
