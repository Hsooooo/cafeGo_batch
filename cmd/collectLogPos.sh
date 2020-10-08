#!/usr/bin/ksh

##################### crontab registration info #######################
### Collect Service Log
### 00,05,10,15,20,25,30,35,40,45,50,55 * * * * /xop_app/xo/batch/cmd/collectLogPos.sh > /dev/null

#LANG=ko_KR

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

##################### REAL Server Envi SET ###############################

filePath="/xop_app/work/log"

PosPath="/xop_app/xo/pos/logs/info/xo_pos_server_$Day.log"


##################### TEST Server Envi SET ###############################

#filePath="/xop_app/work/log"

#PosPath="/xop_app/xo/pos/logs/info/xo_pos_server_$Day.log"

######################## SCRIPT BEGIN ###################################
Pos=`cat -n $PosPath | grep $key | head -1 | awk '{print $1}'`

#echo $key
#echo "pos="$Pos

rm -f $filePath/POSServer1

if [ "$Pos" -ge "1" ]
then
        tail +$Pos $PosPath | grep -v "at " | grep -v "Cause:" | grep "Exception" > $filePath/POSServer1
else
        touch $filePath/POSServer1
fi
