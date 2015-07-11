FRUGALDB_HOME=`cd "$(dirname "$0")";cd ..;pwd`
OPTS="
-Dnewhybrid.envpath=$FRUGALDB_HOME/conf/newhybrid-env
-Dlog4j.configuration=file:$FRUGALDB_HOME/conf/log4j.properties
-Dnewhybrid.logdir=$FRUGALDB_HOME/logs
-Dnewhybrid.logger.name=CLIENT_LOGGER
-Dnewhybrid.workloaddir=$FRUGALDB_HOME/workloads
"
Usage="Usage: client.sh WORKLOAD_FILE RESULT_FILE
WORKLOAD_FILE is the path of the workload file. 
RESULT_FILE is the path of the result file.
"
declare -a processIDs
for ((i=0;i<15;i++))
do
	((start=i*200+1))	
	((end=(i+1)*200))
	(nohup java -classpath $CLASSPATH:$FRUGALDB_HOME/lib/*:$FRUGALDB_HOME/bin $OPTS test.TestMain>/dev/null 2>$FRUGALDB_HOME/logs/error.log $start $end $1 $2)&
	processIDs[i]=$!
done
for ((i=0;i<15;i++))
do
	wait ${processIDs[i]}
done
