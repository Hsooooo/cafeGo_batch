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

DAEMONCLASS=co.kr.istarbucks.xo.batch.main.PaymentCancel
DAEMONNAME=XO_PaymentCancel
DAEMONARGS1=$2
DAEMONARGS2=$3

CONSOLEFILE=$LOG/paymentcancel_console.log

JAVA_PNAME="-Dpn=$DAEMONNAME"
JAVA_PROPERTY="-Xms32m -Xmx128m"

if [ $# -eq 1 ]; then
      echo "start XO_PaymentCancel!"
      nohup $JAVA_COMMAND $JAVA_PNAME $JAVA_PROPERTY -cp $CLASSPATH $DAEMONCLASS > $CONSOLEFILE 2>&1 &
      exit 0
elif [ $# -eq 2 ]; then
      echo "start XO_PaymentCancel! [period] $DAEMONARGS1"
      nohup $JAVA_COMMAND $JAVA_PNAME $JAVA_PROPERTY -cp $CLASSPATH $DAEMONCLASS $1 $2 > $CONSOLEFILE 2>&1 &
      exit 0
elif [ $# -eq 3 ]; then
      echo "start XO_PaymentCancel! [period] $DAEMONARGS1 ~ $DAEMONARGS2"
      nohup $JAVA_COMMAND $JAVA_PNAME $JAVA_PROPERTY -cp $CLASSPATH $DAEMONCLASS $1 $2 $3 > $CONSOLEFILE 2>&1 &
      exit 0  
else
	echo "USAGE : paymentCancel.sh start"
fi
