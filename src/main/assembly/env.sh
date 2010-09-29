# JHOVE2 -- Next-generation architecture for format-aware characterization.
# Copyright 2009-2010 by The Regents of the University of California, Ithaka
# Harbors, Inc., and The Board of Trustees of the Leland Stanford Junior
# University.  All rights reserved.
#
# This file is called by all the other .sh files in order to set JAVA,
# JHOVE2_HOME, and CP. Edit this file if you need to modify the settings for
# these variables or define additional variables, such as JAVA_HOME. For more
# information, refer to the JHOVE2 User's Guide at http://jhove2.org.
#
# NOTE: The jhove2-<version>.jar filename @jarName@ and the 
# classpath @classpath@
# are injected via filtering as part of the maven build process.    

# If JAVA_HOME is not set, use the java in the execution path
if [ ${JAVA_HOME} ] ; then
  JAVA=$JAVA_HOME/bin/java
else
  JAVA=java
fi

# JHOVE2_HOME must point to home directory of JHOVE2 install.

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

# CP must contain a colon-separated list of JARs used by JHOVE2.
CP=$JHOVE2_HOME/lib/@jarName@.jar:@classpath@:$JHOVE2_HOME/config:$JHOVE2_HOME/config/droid

