#!/bin/ksh

#empty
SPACE=" "

JAVA_HOME=/usr/local/jdk1.6.0_45
BATCH_HOME=/sck/batch/xo
LANG=ko_KR

JAVA_COMMAND=$JAVA_HOME/bin/java

LIB=$BATCH_HOME/lib
LOG=/sck/batch/logs/xo

CLASSPATH=$BATCH_HOME/conf/:$BATCH_HOME/bin
for FILE in $BATCH_HOME/lib/*.jar
do
  CLASSPATH=$CLASSPATH:$FILE
done

DAEMONCLASS=co.kr.istarbucks.xo.batch.main.LogFileRemover
DAEMONNAME=LogFileRemover

DAEMONARGS1=$1

CONSOLEFILE=$LOG/logFileRemover_console.log

JAVA_PNAME="-Dpn=$DAEMONNAME"
JAVA_PROPERTY="-Xms32m -Xmx128m"


if [ $# -eq 1 ]; then
      echo "start logFileRemover!"
      nohup $JAVA_COMMAND $JAVA_PNAME $JAVA_PROPERTY -cp $CLASSPATH $DAEMONCLASS $DAEMONARGS1 > $CONSOLEFILE 2>&1 &
      exit 0
else
	echo "USAGE : logFileRemover.sh start"
fi
