#!/usr/bin/env bash
USAGE="Usage: test.sh [-h] WHAT
where WHAT is one of:
\ttpcc-data-import\t\timport tpcc data
\ttest-consolidation\t\ttest consolidation
-h to display help information
"
get_env(){
	. $home/conf/newhybrid-env.sh
}
tpccDataImport(){
        java -classpath $CLASSPATH:$NEWHYBRID_LIBDIR/*:$NEWHYBRID_BINDIR $NEWHYBRID_CLIENT_JAVA_OPTS test.TpccDataImporter 
}
testConsolidation_single(){
	echo -e "testing consolidation for slo $1\n"
	left=1
	if [ ${1} -eq 500 ]; then
		right=200
	else 
		right=2000 
	fi
	for ((;left+1<right;))
	do
		((mid=(left+right)/2))
		echo "before testing the interval is (${left},${right})"
		echo "testing ${mid} tenant with slo $1................."
		result_file="SLO${1}_1_${mid}"
		java -classpath $CLASSPATH:$NEWHYBRID_LIBDIR/*:$NEWHYBRID_BINDIR $NEWHYBRID_CLIENT_JAVA_OPTS test.TestSLO 1 ${mid} ${1} ${result_file}
		if [ -f $result_file ]; then
			i=0
			declare -a array
			for num in `grep . ${result_file}`; do
				array[i]=${num}
				((i=i+1))
			done
			tot=${array[0]}
			success=${array[1]}
			((min=tot*95/100))
			echo "${min} query needed,${success} query success"
			if [ ${success} -lt ${min} ]; then 
				((right=mid))
			else
				((left=mid))
			fi
		else
			echo "${result_file} not exists!"
			exit 1
		fi
		echo "after testing the interval is (${left},${right})"
		echo -e "\n"
	done
	echo "${1}:${left}" >> "consolidation_single"
}
testConsolidation_hybrid(){
	user500=$1
	user50=$2
	user5=$3
	result_file_500="SLO_h_500"
	result_file_50="SLO_h_50"
	result_file_5="SLO_h_5"
	left1=1
	right1=$1
	((left2=right1+1))
	((right2=left2+user50-1))
	((left3=right2+1))
	((right3=left3+user5-1))
	echo "testing consolidation for hybrid (${user500},${user50},${user5})......" 
	declare -a processIDs
	echo "${left1},${right1}"
	echo "${left2},${right2}"
	echo "${left3},${right3}"
	(nohup java -classpath $CLASSPATH:$NEWHYBRID_LIBDIR/*:$NEWHYBRID_BINDIR $NEWHYBRID_CLIENT_JAVA_OPTS test.TestSLO 1 ${user500} 500 ${result_file_500})&
	 processIDs[0]=$!
	 (nohup java -classpath $CLASSPATH:$NEWHYBRID_LIBDIR/*:$NEWHYBRID_BINDIR $NEWHYBRID_CLIENT_JAVA_OPTS test.TestSLO 1 ${user50} 50 ${result_file_50})&
	 processIDs[1]=$!
	 (nohup java -classpath $CLASSPATH:$NEWHYBRID_LIBDIR/*:$NEWHYBRID_BINDIR $NEWHYBRID_CLIENT_JAVA_OPTS test.TestSLO 1 ${user5} 5 ${result_file_5})&
	 processIDs[2]=$!
	wait ${processID[0]}
	wait ${processID[1]}
	wait ${processID[2]}
	#echo -e "100\n100">${result_file_500}
	#echo -e "100\n90">${result_file_50}
	#echo -e "100\n95">${result_file_5}
	i=0
	declare -a array
	for num in `grep . ${result_file_500}`; do
		array[i]=${num}
		((i=i+1))
	done
	for num in `grep . ${result_file_50}`; do
		array[i]=${num}
		((i=i+1))
	done
	for num in `grep . ${result_file_5}`; do
		array[i]=${num}
		((i=i+1))
	done
	((tot=${array[0]}+${array[2]}+${array[4]}))
	((success=${array[1]}+${array[3]}+${array[5]}))
	((min=tot*95/100))
	echo "${min} query needed,${success} query success"
	if [ ${success} -lt ${min} ]; then 
		echo "failed!"
	else
		echo "success!"
		echo -e "(${user500},${user50},${user5})" >> consolidation_hybrid
	fi
}
testConsolidation(){
	#testConsolidation_single 500
	#testConsolidation_single 50
	#testConsolidation_single 5
	#500:100 50:1033 5:2000
	testConsolidation_hybrid 80 500 500
	testConsolidation_hybrid 80 500 0 
	testConsolidation_hybrid 80 250 1000 
	testConsolidation_hybrid 80 250 0 
	testConsolidation_hybrid 80 100 1000 
	testConsolidation_hybrid 80 100 0 
	testConsolidation_hybrid 60 750 500 
	testConsolidation_hybrid 60 750 0 
	testConsolidation_hybrid 60 500 1000 
	testConsolidation_hybrid 60 500 0 
	testConsolidation_hybrid 60 250 1000 
	testConsolidation_hybrid 60 250 0 
	testConsolidation_hybrid 40 750 1000 
	testConsolidation_hybrid 40 750 0 
	testConsolidation_hybrid 40 500 1000 
	testConsolidation_hybrid 40 500 0 
	testConsolidation_hybrid 20 1000 0 
	testConsolidation_hybrid 20 1000 250 
	testConsolidation_hybrid 20 750 1000 
	testConsolidation_hybrid 20 750 0 
	testConsolidation_hybrid 20 500 1000 
	testConsolidation_hybrid 20 500 0 
	testConsolidation_hybrid 0 1000 1000 
	testConsolidation_hybrid 0 1000 500 
}
home=`cd "$(dirname "$0")";cd ..;pwd`
get_env
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
if [ -z "${WHAT}" ]; then echo "Error: no WHAT specified"
	echo -e "${USAGE}"
	exit 1
fi
case "${WHAT}" in
    tpcc-data-import)
        tpccDataImport 
        ;;
    test-consolidation)
	testConsolidation
        ;;
    *)
        echo -e "${USAGE}"
        exit 1
        ;;
esac
