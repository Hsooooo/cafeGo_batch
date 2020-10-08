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

DAEMONCLASS=co.kr.istarbucks.xo.batch.mon.MonitorLog
DAEMONNAME=MonitorLog
DAEMONARGS="$1"

CONSOLEFILE=$LOG/monitor_console.log

JAVA_PNAME="-Dpn=$DAEMONNAME"
JAVA_PROPERTY="-Xms64m -Xmx256m"

if [ $# -eq 0 ]; then
        echo 'Usage: '"$DAEMONNAME"'.sh [options]'
        echo 'Options: [options will execute in the order listed]'
        echo '{ start | stop | log | usage  }  start, stop, log, usage'
        exit 0
fi

while [ $# -ge 1 ]; do
        case "$1" in
                h)
                        echo 'Usage: "$DAEMONNAME".sh [options]'
                        echo 'Options: [options will execute in the order listed]'
                        echo '{ start | stop | log | usage  }  start, stop, log, usage'
                        exit 0
                        ;;
                start)
                        #touch $SMS_HOME/cmd/$PIDFILE
                        #PID=`cat $SMS_HOME/cmd/$PIDFILE`
                        #   if [ "x$PID" != "x" ]; then
                        #           echo "aleardy running $DAEMONCLASS process!!!"
                        #           echo "PID is $PID"
                        #      exit 1
                        #   else

                        echo "start $DAEMONNAME Daemon!"
                        nohup $JAVA_COMMAND $JAVA_PNAME $JAVA_PROPERTY -cp $CLASSPATH $DAEMONCLASS $DAEMONARGS > $CONSOLEFILE 2>&1 &
                        #echo $! > $SMS_HOME/cmd/$PIDFILE
                        #   fi
                        exit 0
                        ;;
                stop)
                        $BATCH_HOME/cmd/stop.sh pn=$DAEMONNAME
                        #echo "" > $SMS_HOME/cmd/$PIDFILE
                        echo "stop $DAEMONNAME!!!"
                        exit 0
                        ;;
                log)
                        echo "logfile : $LOGFILE"
                        tail -100f $LOGFILE
                        exit 0
                        ;;
                *)
                        echo 'Usage: "$DAEMONNAME".sh [options]'
                        echo 'Options: [options will execute in the order listed]'
                        echo '{ start | stop | log | usage  }  start, stop, log, usage'
                        ;;
                esac
        shift
done
