#!/usr/bin/env bash
FRUGALDB_HOME=`cd "$(dirname "$0")";cd ..;pwd`
workloadlist=`ls $FRUGALDB_HOME/workloads | grep .txt$`
for workloadfile in $workloadlist
do
	if [ -z `echo ${workloadfile} | grep load550500_0.30.1` ]; then
		echo 1:$workloadfile
	else
		echo 2:$workloadfile
	fi
done
