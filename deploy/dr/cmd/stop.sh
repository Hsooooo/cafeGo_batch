#!/usr/bin/ksh
if [ -z $1 ]
then
    echo Usage: stop.sh {unique process name}
    exit 1
fi

pid=`ps -ef | grep xop_app | grep java | grep $1$2 | grep -v grep | awk '{print $2}'`

kill -TERM $pid
