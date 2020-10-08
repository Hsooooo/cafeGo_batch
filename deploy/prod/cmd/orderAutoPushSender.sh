#!/bin/ksh

#empty
SPACE=" "

LANG=ko_KR

JAVA_HOME=/usr/java7_64
BATCH_HOME=/sck/batch/xo

JAVA_COMMAND=$JAVA_HOME/bin/java

LIB=$BATCH_HOME/lib
LOG=$BATCH_HOME/logs

CLASSPATH=$BATCH_HOME/conf/:$BATCH_HOME/bin
for FILE in $BATCH_HOME/lib/*.jar
do
  CLASSPATH=$CLASSPATH:$FILE
done

DAEMONCLASS=co.kr.istarbucks.xo.batch.main.OrderAutoPushSender
DAEMONNAME=OrderAutoPushSender

DAEMONARGS1=$1

CONSOLEFILE=$LOG/OrderAutoPushSender_console.log

JAVA_PNAME="-Dpn=$DAEMONNAME"
JAVA_PROPERTY="-Xms32m -Xmx128m"

while [ $# -ge 1 ]; do
	case "$1" in
	
	start)
		echo "start OrderAutoPushSender!"
		nohup $JAVA_COMMAND $JAVA_PNAME $JAVA_PROPERTY -cp $CLASSPATH $DAEMONCLASS DAEMONARGS1 > $CONSOLEFILE 2>&1 &
		exit 0
		;;
	stop)
		echo "stop OrderAutoPushSender!"
		$BATCH_HOME/cmd/stop.sh $DAEMONNAME 
		exit 0
		;;
	esac
	shift
done
