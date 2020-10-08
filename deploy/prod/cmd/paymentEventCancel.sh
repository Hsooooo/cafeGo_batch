#!/bin/ksh

#empty
SPACE=" "

JAVA_HOME=/usr/java6_64
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

DAEMONCLASS=co.kr.istarbucks.xo.batch.main.PaymentEventCancel
DAEMONNAME=XO_PaymentEventCancel

CONSOLEFILE=$LOG/paymenteventcancel_console.log

JAVA_PNAME="-Dpn=$DAEMONNAME"
JAVA_PROPERTY="-Xms32m -Xmx128m"

if [ $# -eq 1 ]; then
      echo "start XO_PaymentEventCancel!"
      nohup $JAVA_COMMAND $JAVA_PNAME $JAVA_PROPERTY -cp $CLASSPATH $DAEMONCLASS > $CONSOLEFILE 2>&1 &
      exit 0  
else
	echo "USAGE : paymentEventCancel.sh start"
fi
