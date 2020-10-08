#!/bin/ksh

#empty
SPACE=" "

JAVA_HOME=/usr/java7_64
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

DAEMONCLASS=co.kr.istarbucks.xo.batch.main.TradeCorrection
DAEMONNAME=XO_TradeCorrection
DAEMONARGS1=$2

CONSOLEFILE=$LOG/tradeCorrection_console.log

JAVA_PNAME="-Dpn=$DAEMONNAME"
JAVA_PROPERTY="-Xms64m -Xmx512m"

if [ $# -eq 1 ]; then
      echo "start XO_TradeCorrection!"
      nohup $JAVA_COMMAND $JAVA_PNAME $JAVA_PROPERTY -cp $CLASSPATH $DAEMONCLASS > $CONSOLEFILE 2>&1 &
      exit 0
      
elif [ $# -eq 2 ]; then
      echo "start XO_TradeCorrection! [period] $DAEMONARGS1"
      nohup $JAVA_COMMAND $JAVA_PNAME $JAVA_PROPERTY -cp $CLASSPATH $DAEMONCLASS $1 $2 > $CONSOLEFILE 2>&1 &
      exit 0
      
else
	echo "USAGE : tradeCorrection.sh start"
fi
