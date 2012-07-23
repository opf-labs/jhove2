#!/bin/sh
# JHOVE2 -- Next-generation architecture for format-aware characterization
# Copyright 2009-2010 by The Regents of the University of California, Ithaka
# Harbors, Inc., and The Board of Trustees of the Leland Stanford Junior
# University.  All rights reserved.
#
# jhove2.sh -- Driver script for main JHOVE2 application under Windows. For
# usage and configuration information, see the JHOVE2 User's Guide at
# http://jhove2.org.

. ./env.sh


#${JAVA} -cp "$CP" org.jhove2.app.JHOVE2CommandLine $@

${JAVA} -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=1044 -Xms256m -Xmx1024m -XX:PermSize=64M -XX:MaxPermSize=256M -cp "$CP" org.jhove2.app.JHOVE2CommandLine $@
