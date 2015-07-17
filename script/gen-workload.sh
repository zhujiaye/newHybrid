#!/usr/bin/env bash
FRUGALDB_HOME=`cd "$(dirname "$0")";cd ..;pwd`
USAGE="Usage: gen-workload [-h] WHAT [DB] [lower_bound] [upper_bound] 
Where WHAT is one of
	main\t\t generate workloads for main testing
	consolidation\t\t generate workloads for consolidation testing

if WHAT is consolidation then you must specify the following vaiables:
	DB\t\t the kind of database you want to test consolidation, must be mysql or hybrid
	lower_bound\t\t the estimated least consolidation number
	upper_bound\t\t the estimated largest consolidation number

-h to display help information
"
gen_consolidation(){
	DB=$1
	case "${DB}" in
		mysql)
			for ((i=$2;i<=$3;i++))
			do
				java -jar $FRUGALDB_HOME/workloads/WorkloadGenerator.jar 5_50_500 $i 1.0 0.1 1.0
				mv load.txt $FRUGALDB_HOME/workloads/load$i.txt
			done
			;;
		hybrid)
			for ((i=$2;i<=$3;i++))
			do
				java -jar $FRUGALDB_HOME/workloads/WorkloadGenerator.jar 5_50_500 $i 1.0 0.1 1.0
				mv load.txt $FRUGALDB_HOME/workloads/load$i.txt
			done
			;;
		*)
			echo "Error: invalid DB: ${DB}" 
			echo -e "${USAGE}"
			exit 1
			;;
	esac
}
gen_main(){
	for ((i=1;i<=5;i++))
	do
		java -jar $FRUGALDB_HOME/workloads/WorkloadGenerator.jar 20_60_200 2000 0.25 0.1 1.0
		mv load.txt $FRUGALDB_HOME/workloads/load2060200_0.250.1_test$i.txt
		java -jar $FRUGALDB_HOME/workloads/WorkloadGenerator.jar 100_150_200 2000 0.25 0.1 1.0
		mv load.txt $FRUGALDB_HOME/workloads/load100150200_0.250.1_test$i.txt
		java -jar $FRUGALDB_HOME/workloads/WorkloadGenerator.jar 5_50_500 2000 0.25 0.1 1.0
		mv load.txt $FRUGALDB_HOME/workloads/load550500_0.250.1_test$i.txt
		java -jar $FRUGALDB_HOME/workloads/WorkloadGenerator.jar 5_50_500 2000 0.3 0.1 1.0
		mv load.txt $FRUGALDB_HOME/workloads/load550500_0.30.1_test$i.txt
		java -jar $FRUGALDB_HOME/workloads/WorkloadGenerator.jar 5_50_500 2000 0.35 0.1 1.0
		mv load.txt $FRUGALDB_HOME/workloads/load550500_0.350.1_test$i.txt
		java -jar $FRUGALDB_HOME/workloads/WorkloadGenerator.jar 5_50_500 2000 0.4 0.1 1.0
		mv load.txt $FRUGALDB_HOME/workloads/load550500_0.40.1_test$i.txt
	done
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
case "${WHAT}" in
	consolidation)
		if [ -z "${3}" ]; then
			echo "Error: no lower_bound specified"
			echo -e "${USAGE}"
			exit 1
		fi
		if [ -z "${4}" ]; then
			echo "Error: no upper_bound specified"
			echo -e "${USAGE}"
			exit 1
		fi
		gen_consolidation $2 $3 $4
		;;
	main)
		gen_main
		;;
	*)
		echo "Error: invalid WHAT: ${WHAT}" 
		echo -e "${USAGE}"
		exit 1
		;;
esac
exit 0
