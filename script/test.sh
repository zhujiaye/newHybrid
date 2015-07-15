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
	if [ -z `echo ${workloadfile} | grep load550500` ]; then
		java -classpath $CLASSPATH:$FRUGALDB_HOME/lib/*:$FRUGALDB_HOME/bin $OPTS test.TestServerReconfigure mysql 2000 
		java -classpath $CLASSPATH:$FRUGALDB_HOME/lib/*:$FRUGALDB_HOME/bin $OPTS test.TestServerReloadWorkload $workloadfile 
		$FRUGALDB_HOME/script/client.sh $workloadfile ${workloadfile}_mysql.results 
		java -classpath $CLASSPATH:$FRUGALDB_HOME/lib/*:$FRUGALDB_HOME/bin $OPTS test.TestServerReconfigure hybrid 2000 
		java -classpath $CLASSPATH:$FRUGALDB_HOME/lib/*:$FRUGALDB_HOME/bin $OPTS test.TestServerReloadWorkload $workloadfile 
		$FRUGALDB_HOME/script/client.sh $workloadfile ${workloadfile}_hybrid_2000M.results
	else
		java -classpath $CLASSPATH:$FRUGALDB_HOME/lib/*:$FRUGALDB_HOME/bin $OPTS test.TestServerReconfigure hybrid 1500 
		java -classpath $CLASSPATH:$FRUGALDB_HOME/lib/*:$FRUGALDB_HOME/bin $OPTS test.TestServerReloadWorkload $workloadfile 
		$FRUGALDB_HOME/script/client.sh $workloadfile ${workloadfile}_hybrid_1500M.results
		java -classpath $CLASSPATH:$FRUGALDB_HOME/lib/*:$FRUGALDB_HOME/bin $OPTS test.TestServerReconfigure hybrid 1000 
		java -classpath $CLASSPATH:$FRUGALDB_HOME/lib/*:$FRUGALDB_HOME/bin $OPTS test.TestServerReloadWorkload $workloadfile 
		$FRUGALDB_HOME/script/client.sh $workloadfile ${workloadfile}_hybrid_1000M.results
		java -classpath $CLASSPATH:$FRUGALDB_HOME/lib/*:$FRUGALDB_HOME/bin $OPTS test.TestServerReconfigure hybrid 500 
		java -classpath $CLASSPATH:$FRUGALDB_HOME/lib/*:$FRUGALDB_HOME/bin $OPTS test.TestServerReloadWorkload $workloadfile 
		$FRUGALDB_HOME/script/client.sh $workloadfile ${workloadfile}_hybrid_500M.results
	fi
	echo "${workloadfile} testing finished."
done

