#!/bin/sh

############################################################################
# JHOVE2 - Next-generation architecture for format-aware characterization
# Copyright 2009 by The Regents of the University of California, Ithaka
# Harbors, Inc., and The Board of Trustees of the Leland Stanford Junior
# University.
# All rights reserved.
#
# Usage: jhove2 [-ik] [-b size] [-B Direct|NonDirect|Mapped]
#               [-d JSON|Text|XML] [-f limit] [-t temp] [-T] [-o file] file ...
#
#  -i  Show the unique formal identifiers for all reportable properties in results.
#  -k  Calculate message digests.
#  -b size     I/O buffer size (default=131072)
#  -B scope     I/O buffer type (default=Direct)
#  -d format   Results format (default=Text)
#  -f limit    Fail fast limit (default=0; no limit on the number of reported errors.
#  -t temp     Temporary file directory (default=java.io.tmpdir)
#  -T  		   Save temporary files
#  -o file     Output file (default=standard output unit)
#  -h  Display a help message
##  file ...    One or more files or directories to be characterized.
#
#
# NOTE: Edit the following line to refer to the proper Java command
#	This MUST be Java 1.6 or greater.

JAVA=/usr/bin/java
PRG=$0

# need this for relative symlinks
while [ -h "$PRG" ] ; do
    ls=`ls -ld "$PRG"`
    link=`expr "$ls" : '.*-> \(.*\)$'`
    if expr "$link" : '/.*' > /dev/null; then
      PRG="$link"
    else
      PRG="`dirname "$PRG"`/$link"
    fi
done

JHOVE2_HOME=`dirname $PRG`

# make it fully qualified
JHOVE2_HOME=`cd $JHOVE2_HOME && pwd`


CP=$JHOVE2_HOME/lib/jhove2-1.9.5.jar:$JHOVE2_HOME/lib/aopalliance-1.0.jar:$JHOVE2_HOME/lib/commons-logging-1.1.1.jar:$JHOVE2_HOME/lib/commons-logging-api-1.1.jar:$JHOVE2_HOME/lib/jdom-1.0.jar:$JHOVE2_HOME/lib/junit-4.4.jar:$JHOVE2_HOME/lib/log4j-1.2.14.jar:$JHOVE2_HOME/lib/jargs-1.0.jar:$JHOVE2_HOME/lib/spring-beans-2.5.6.jar:$JHOVE2_HOME/lib/spring-context-2.5.6.jar:$JHOVE2_HOME/lib/spring-core-2.5.6.jar:$JHOVE2_HOME/lib/spring-test-2.5.6.jar:$JHOVE2_HOME/lib/soap-2.3.1.jar:$JHOVE2_HOME/lib/xercesImpl-2.9.1.jar:$JHOVE2_HOME/lib/xml-apis-1.3.04.jar:$JHOVE2_HOME/lib/xml-resolver-1.2.jar:$JHOVE2_HOME/config:$JHOVE2_HOME/config/droid

# NOTE: Nothing below this line should be edited
############################################################################

ARGS=""
for ARG do
    ARGS="$ARGS $ARG"
done

${JAVA} -classpath $CP org.jhove2.app.JHOVE2CommandLine $ARGS
