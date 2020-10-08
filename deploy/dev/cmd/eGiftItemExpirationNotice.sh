#!/bin/ksh

#empty

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

DAEMONCLASS=co.kr.istarbucks.xo.batch.main.EGiftItemExpirationNotice
DAEMONNAME=XO_EGiftItemExpirationNotice

CONSOLEFILE=/sck/batch/logs/xo/eGiftItemExpirationNotice_console.log

JAVA_PNAME="-Dpn=$DAEMONNAME"
JAVA_PROPERTY="-Xms32m -Xmx128m"

processCount=`ps -ef | grep ${DAEMONNAME}|grep -v grep | grep -v tail | wc -l`

echo `date`

if [ ${processCount} -lt 2 ];then
      echo "start XO_EGiftItemExpirationNotice!"
      nohup $JAVA_COMMAND $JAVA_PNAME $JAVA_PROPERTY -cp $CLASSPATH $DAEMONCLASS $1 > $CONSOLEFILE 2>&1 &
      exit 0
else
        echo "USAGE : eGiftItemExpirationNotice.sh"
fi
