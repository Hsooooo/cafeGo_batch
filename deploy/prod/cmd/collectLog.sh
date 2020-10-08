#!/usr/bin/ksh

##################### crontab registration info #######################
### Collect Service Log
### 00,05,10,15,20,25,30,35,40,45,50,55 * * * * /xop_app/xo/batch/cmd/collectLog.sh > /dev/null

LANG=ko_KR

##################### DATE and TIME SET ###############################

Day=`date +"%Y%m%d"`

hour=`date +"%H"`
minu=`date +"%M"`

if [ "$minu" -lt "05" ]
then
	hour2=`expr $hour - 1`
	minu2=`expr $minu - 5 + 60`
else
	hour2=$hour
	minu2=`expr $minu - 5`
fi

if [ "$minu2" -lt "10" ]
then
	minu2="0"$minu2
else
	minu2=$minu2
fi

key=$hour2":"$minu2":"
#key="08:41:"

toDate=`date +"%Y-%m-%d"`





##################### REAL Server Envi SET ###############################

filePath="/xop_app/work/log"

AppPath="/xop_app/resin-pro-4.0.43/log/app/resin/stderr-app1.log"
XoPath="/xop_app/resin-pro-4.0.43/log/app/xo/error/xo_error_$Day.log"
#WebPath="/JEUS/jeus7/domains/sta-eux-xowas1/servers/mango2/logs/JeusServer.log"
WebPath="/JEUS/log/web/error/web_error.$toDate.log"

##################### TEST Server Envi SET ###############################

#filePath="/xop_app/work/log"

#AppPath="/xop_app/resin/log/app/resin/stderr-app1.log"
#XoPath="/xop_app/resin/log/app/xo/error/xo_error_$Day.log"

######################## SCRIPT BEGIN ###################################
App=`cat -n $AppPath | grep $key | head -1 | awk '{print $1}'`
Xo=`cat -n $XoPath | grep $key | head -1 | awk '{print $1}'`
Web=`cat -n $WebPath | grep $key | head -1 | awk '{print $1}'`

#echo $key
#echo "app="$App
#echo "xo="$Xo

rm -f $filePath/stderr-app1
rm -f $filePath/xo_error1
rm -f $filePath/JeusServer1

if [ "$App" -ge "1" ]
then
        tail +$App $AppPath | grep -v "at " | grep -v "Cause:" | grep -v "NestedSQL" | grep "Exception" > $filePath/stderr-app1
else
        touch $filePath/stderr-app1
fi

if [ "$Xo" -ge "1" ]
then
        tail +$Xo $XoPath | grep -v "Cause:" | grep -v "NestedSQL" | grep "Exception" > $filePath/xo_error1
else
        touch $filePath/xo_error1
fi

if [ "$Web" -ge "1" ]
then
        tail +$Web $WebPath | grep -v "at " | grep -v "Cause:" | grep -v "NestedSQL" | grep -v "Exception__>>" | grep "Exception" > $filePath/JeusServer1
else
        touch $filePath/JeusServer1
fi

