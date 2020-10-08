#!/bin/ksh

LANG=ko_KR


# D-40¿œ
DelDate1=`TZ=KST+960 date +%Y%m%d`

##### log Delete 

rm -rf "/xo_app/xo/pos/logs/info/"*$DelDate1* 
rm -rf "/xo_app/xo/pos/logs/text/"*$DelDate1* 







