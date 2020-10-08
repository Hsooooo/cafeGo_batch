#!/bin/ksh

#empty
SPACE=" "

LANG=ko_KR

JAVA_HOME=/usr/local/jdk1.6.0_45
BATCH_HOME=/sck/batch/xo

JAVA_COMMAND=$JAVA_HOME/bin/java

LIB=$BATCH_HOME/lib
LOG=/sck/batch/logs/xo

CLASSPATH=$BATCH_HOME/conf/:$BATCH_HOME/bin
for FILE in $BATCH_HOME/lib/*.jar
do
  CLASSPATH=$CLASSPATH:$FILE
done

DAEMONCLASS=co.kr.istarbucks.xo.batch.ss.SamsungAutoPushSender
DAEMONNAME=SamsungAutoPushSender

DAEMONARGS1=$1

CONSOLEFILE=/sck/batch/logs/xo/SamsungAutoPushSender_console.log

JAVA_PNAME="-Dpn=$DAEMONNAME"
JAVA_PROPERTY="-Xms32m -Xmx128m"

while [ $# -ge 1 ]; do
	case "$1" in
	
	start)
		echo "start SamsungAutoPushSender!"
		nohup $JAVA_COMMAND $JAVA_PNAME $JAVA_PROPERTY -cp $CLASSPATH $DAEMONCLASS DAEMONARGS1 > $CONSOLEFILE 2>&1 &
		exit 0
		;;
	stop)
		echo "stop SamsungAutoPushSender!"
		$BATCH_HOME/cmd/stop.sh $DAEMONNAME 
		exit 0
		;;
	esac
	shift
done
