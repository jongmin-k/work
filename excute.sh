#! /bin/bash

cd `dirname $0`
DIR_HOME=`pwd`
JAR="$DIR_HOME/build/libs/demo-0.0.1-SNAPSHOT.jar"
CONF="$DIR_HOME/conf/application.properties"
GRADLE="$DIR_HOME/gradlew"

OP_DAEMON=""

function print_help {
echo "
Usage :  $0 [arguments]
Arguments:

  -h                       help
  -d                       start by daemon
  -b                       build from source
  -i                       extract application.properties from jar
  -s                       stop running daemon
"
}


while getopts ":hbdis" arg; do
  case $arg in
    h )
        print_help
        exit 0;
        ;;
    d )
        OP_DAEMON=true
        ;;
    b )
        $GRADLE build;
		exit 0;
        ;;
    i )
        if [ ! -d $DIR_HOME/conf ]; then mkdir $DIR_HOME/conf ;fi
        if [ -f $DIR_HOME/conf/application.properties ] ; then mv $DIR_HOME/conf/application.properties $DIR_HOME/conf/application.properties.bak ;fi
        unzip -q -c $JAR BOOT-INF/classes/application.properties > $DIR_HOME/conf/application.properties
	if [ -f $DIR_HOME/conf/application.properties.bak ] ; then
        diff -y $DIR_HOME/conf/application.properties.bak $DIR_HOME/conf/application.properties; fi
        exit 0;
        ;;
    s )
        ps -ef | grep $JAR | grep -v grep | awk '{print $2}' | while read pid ; do kill -9 $pid ; done
        exit 0;
        ;;
    \? )
        echo "Unknown option: -$OPTARG" >&2
        print_help
        exit 1;
        ;;
    : )
        echo "Missing option argument for -$OPTARG" >&2
        print_help
        exit 1;
  esac
done



if [ ! -f $JAR ] ; then
echo "-- start build"
	$GRADLE build;
fi
if [ $OP_DAEMON ] ; then
echo "-- start with daemon"
nohup java -jar -XX:+UseG1GC -Dspring.config.location=$CONF $JAR 1>/dev/null 2>&1 &
else
	java -jar -XX:+UseG1GC -Dspring.config.location=$CONF $JAR
fi
