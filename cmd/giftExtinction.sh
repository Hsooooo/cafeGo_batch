#!/bin/ksh

#empty
SPACE=" "

JAVA_HOME=/usr/local/jdk1.6.0_45
BATCH_HOME=/sck/batch/xo
LANG=ko_KR

JAVA_COMMAND=$JAVA_HOME/bin/java

LIB=$BATCH_HOME/lib
LOG=$BATCH_HOME/logs

CLASSPATH=$BATCH_HOME/conf/:$BATCH_HOME/bin
for FILE in $BATCH_HOME/lib/*.jar
do
  CLASSPATH=$CLASSPATH:$FILE
done

DAEMONCLASS=co.kr.istarbucks.xo.batch.main.GiftExtinction
DAEMONNAME=GiftExtinction

CONSOLEFILE=$LOG/gift_extinction_console.log

JAVA_PNAME="-Dpn=$DAEMONNAME"
JAVA_PROPERTY="-Xms32m -Xmx128m"

if [ $# -le 1 ]; then
      echo "start eGift-extinction"
      nohup $JAVA_COMMAND $JAVA_PNAME $JAVA_PROPERTY -cp $CLASSPATH $DAEMONCLASS $1 > $CONSOLEFILE 2>&1 &
      exit 0
else
	echo "USAGE : giftExtinction.sh or giftExtinction.sh yyyyMMdd"
fi
