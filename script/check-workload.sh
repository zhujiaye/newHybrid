#!/usr/bin/env bash
home=`cd "$(dirname "$0")";cd ..;pwd`

get_env(){
	. $home/conf/newhybrid-env.sh
}

get_env
workloadlist=`ls ${NEWHYBRID_WORKLOADDIR} | grep .txt$`
for workloadfile in $workloadlist
do
	if [ -z `echo ${workloadfile} | grep load550500_0.250.1` ]; then
		echo 1:$workloadfile
	else
		echo 2:$workloadfile
	fi
done
