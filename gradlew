#!/usr/bin/env sh

#
# Copyright 2015 the original author or authors.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

##############################################################################
##
##  Gradle start up script for UN*X
##
##############################################################################

# Attempt to set APP_HOME
# Resolve links: $0 may be a link
PRG="$0"
# Need this for relative symlinks.
while [ -h "$PRG" ] ; do
    ls -ld "$PRG"
    LINK=`ls -l "$PRG" | awk '{print $NF}'`
    case $LINK in
        /*) PRG="$LINK" ;;
        *) PRG=`dirname "$PRG"`"/$LINK" ;;
    esac
done
SAVE_PWD=`pwd`
cd "`dirname \"$PRG\"`" >/dev/null
APP_HOME=`pwd -P`
cd "$SAVE_PWD" >/dev/null

APP_NAME="Gradle"
APP_BASE_NAME=`basename "$0"`

# Add default JVM options here. You can also use JAVA_OPTS and GRADLE_OPTS to pass JVM options to this script.
DEFAULT_JVM_OPTS='\"\" \"\" \"\" \"\" \"\" \"\" \"\" \"\" \"\" \"\" \"\" \"\" \"\" \"\" \"\" \"\" \"\" \"\" \"\" \"\" \"\"'

# Use the maximum available, or set MAX_FD != maximum.
MAX_FD="maximum"

warn () {
    echo "$*"
} >&2

die () {
    echo
    echo "$*"
    echo
    exit 1
} >&2

# OS specific support (must be 'true' or 'false').
IS_CYGWIN=false
IS_MSYS=false
IS_MINGW=false
IS_DARWIN=false
IS_SUNOS=false
IS_LINUX=false
IS_FREEBSD=false
case "`uname`" in
  CYGWIN* )          IS_CYGWIN=true  ;;
  Darwin* )          IS_DARWIN=true  ;;
  MSYS* )            IS_MSYS=true    ;;
  MINGW* )           IS_MINGW=true   ;;
  SunOS* )           IS_SUNOS=true   ;;
  Linux* )           IS_LINUX=true   ;;
  FreeBSD* )         IS_FREEBSD=true ;;
esac

PROG="`basename \"$0\"`"
PROG_DIR="`dirname \"$PRG\"`"

export CLASSPATH="$PROG_DIR/gradle/wrapper/gradle-wrapper.jar"

if [ -z "$JAVA_HOME" ] ; then
    JAVA_EXE="java"
else
    JAVA_EXE="$JAVA_HOME/bin/java"
fi

if ! command -v "$JAVA_EXE" >/dev/null 2>&1 ; then
  die "ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH."
fi

if [ ! -f "$PROG_DIR/gradle/wrapper/gradle-wrapper.jar" ] ; then
  die "ERROR: gradle-wrapper.jar not found in gradle/wrapper directory."
fi

exec "$JAVA_EXE" "$@"
