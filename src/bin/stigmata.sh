#!/bin/sh

##############################################################
# The almost part of this script is copied from mvn command. #
##############################################################

ARGS=""
while [ "$1" != "" ] ; do
    ARGS="$ARGS \"$1\""
    shift
done

cygwin=false;
darwin=false;
mingw=false;

case "`uname`" in
    CYGWIN*) cygwin=true ;;
    MINGW*)  mingw=true ;;
    Darwin*) darwin=true
        if [-z "$JAVA_VERSION" ] ; then
            JAVA_VERSION="CurrentJDK"
        else
            echo "Using Java version: $JAVA_VERSION"
        fi
        if [ -z "$JAVA_HOME" ] ; then
            JAVA_HOME=/System/Library/Frameworks/JavaVM.framework/Versions/${JAVA_VERSION}/Home
        fi
        ;;
esac

if [ -z "$JAVA_HOME" ] ; then
    if [ -r /etc/gentoo-release ] ; then
        JAVA_HOME=`java-config --jre-home`
    fi
fi

if [ -z "$JAVACMD" ] ; then
    if [ -n "$JAVA_HOME" ] ; then
        if [ -x "$JAVA_HOME/jre/sh/java" ] ; then
            # IBM's JDK on AIX uses strange locations for the executables
            JAVACMD="$JAVA_HOME/jre/sh/java"
        else
            JAVACMD="$JAVA_HOME/bin/java"
        fi
    else
        JAVACMD=java
    fi
fi

if [ ! -x "$JAVACMD" ] ; then
    echo "Error: JAVA_HOME is not defined correctly."
    echo "  We cannot execute $JAVACMD"
    exit 1
fi

if [ -z "$JAVA_HOME" ] ; then
    echo "Warning: JAVA_HOME environment variable is not set."
fi

exec "$JAVACMD" -jar lib/stigmata-1.2.0.jar $ARGS
