FRUGALDB_HOME=`cd "$(dirname "$0")";cd ..;pwd`
OPTS="
-Dnewhybrid.envpath=$FRUGALDB_HOME/conf/newhybrid-env
-Dlog4j.configuration=file:$FRUGALDB_HOME/conf/log4j.properties
-Dnewhybrid.logdir=$FRUGALDB_HOME/logs
-Dnewhybrid.logger.name=CLIENT_LOGGER
-Dnewhybrid.workloaddir=$FRUGALDB_HOME/workloads
"
#TODO for every workload file 
workloadlist=`ls $FRUGALDB_HOME/workloads | grep .txt$`
for workloadfile in $workloadlist
do
	echo "start testing for workload ${workloadfile}..."
	java -classpath $CLASSPATH:$FRUGALDB_HOME/lib/*:$FRUGALDB_HOME/bin $OPTS test.TestServerReloadWorkload $workloadfile 
	java -classpath $CLASSPATH:$FRUGALDB_HOME/lib/*:$FRUGALDB_HOME/bin $OPTS test.TestServerReconfigure mysql 2000 
	$FRUGALDB_HOME/script/client.sh $workloadfile ${workloadfile}_mysql.results 
	java -classpath $CLASSPATH:$FRUGALDB_HOME/lib/*:$FRUGALDB_HOME/bin $OPTS test.TestServerReconfigure hybrid 2000 
	$FRUGALDB_HOME/script/client.sh $workloadfile ${workloadfile}_hybrid_2000M.results
	echo "${workloadfile} testing finished."
done

