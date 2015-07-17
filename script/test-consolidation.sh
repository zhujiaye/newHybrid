#!/usr/bin/env bash
FRUGALDB_HOME=`cd "$(dirname "$0")";cd ..;pwd`
OPTS="
-Dnewhybrid.envpath=$FRUGALDB_HOME/conf/newhybrid-env
-Dlog4j.configuration=file:$FRUGALDB_HOME/conf/log4j.properties
-Dnewhybrid.logdir=$FRUGALDB_HOME/logs
-Dnewhybrid.logger.name=CLIENT_LOGGER
-Dnewhybrid.workloaddir=$FRUGALDB_HOME/workloads
"
USAGE="Usage: test-consolidation [-h] WHAT lower_bound upper_bound
Where WHAT is one of:
\tmysql\t\t test mysql consolidation
\thybrid\t\t test hybrid consolidation

lower_bound is the estimated least mysql/hybrid consolidation number
upper_bound is the estimated largest mysql/hybrid consolidation number

-h to display help information
"
do_client(){
	declare -a processIDs
	for ((i=0;i<15;i++))
	do
		((start=i*200+1))	
		((end=(i+1)*200))
		(nohup java -classpath $CLASSPATH:$FRUGALDB_HOME/lib/*:$FRUGALDB_HOME/bin $OPTS test.TestConsolidation>/dev/null 2>$FRUGALDB_HOME/logs/error_client.log $start $end $1 $2) &
		processIDs[i]=$!
	done
	((ok=0))
	for ((i=0;i<15;i++))
	do
		wait ${processIDs[i]}
		v=$?
		if [ $v -eq 1 ]; then
			((ok=1))
		fi
	done
	return $ok
}
test_consolidation(){
	left=$2
	right=$3
	echo -e "testing $1 consolidation...left:${left} right:${right}\n"
	for ((;left+1<right;))
	do
		((mid=(left+right)/2))
		workloadfile="load${mid}.txt"
		echo "before testing the interval is (${left},${right})"
		echo "testing for ${workloadfile}.................."
		if [ -f "${FRUGALDB_HOME}/workloads/${workloadfile}" ]; then
			java -classpath $CLASSPATH:$FRUGALDB_HOME/lib/*:$FRUGALDB_HOME/bin $OPTS test.TestServerReconfigure $1 2000 
			java -classpath $CLASSPATH:$FRUGALDB_HOME/lib/*:$FRUGALDB_HOME/bin $OPTS test.TestServerReloadWorkload $workloadfile 
			do_client $workloadfile "${workloadfile}_hybrid_2000M.results"
			v=$?
			if [ $v -eq 0 ]; then
				((left=mid))
			else
				((right=mid))
			fi
		else
			echo "${workloadfile} not exists!"
			((right=mid))
		fi
		echo "after testing the interval is (${left},${right})"
		echo -e "\n"
	done
	echo "RESULTS consolidation:${left}"
}
while getopts "h" o; do
	case "${o}" in
		h)
			echo -e "${USAGE}"
			exit 0
			;;
		*)
			echo -e "${USAGE}"
			exit 1
			;;
	esac
done
shift $((OPTIND-1))

WHAT=$1

if [ -z "${WHAT}" ]; then
	echo "Error: no WHAT specified"
	echo -e "${USAGE}"
	exit 1
fi
if [ -z "${2}" ]; then
	echo "Error: no lower_bound specified"
	echo -e "${USAGE}"
	exit 1
fi
if [ -z "${3}" ]; then
	echo "Error: no upper_bound specified"
	echo -e "${USAGE}"
	exit 1
fi

case "${WHAT}" in
	mysql)
		test_consolidation mysql $2 $3 
		;;
	hybrid)
		test_consolidation hybrid $2 $3
		;;
	*)
		echo "Error: invalid WHAT: ${WHAT}"
		echo -e "${USAGE}"
		exit 1
esac
exit 0

