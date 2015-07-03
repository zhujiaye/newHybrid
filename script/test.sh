FRUGALDB_HOME=`cd "$(dirname "$0")";cd ..;pwd`
OPTS="
-Dnewhybrid.envpath=$FRUGALDB_HOME/conf/newhybrid-env
-Dlog4j.configuration=file:$FRUGALDB_HOME/conf/log4j.properties
-Dnewhybrid.logdir=$FRUGALDB_HOME/logs
-Dnewhybrid.logger.name=CLIENT_LOGGER
-Dnewhybrid.workloaddir=$FRUGALDB_HOME/workloads
"
#TODO for every workload file 
echo "start testing for workload load1.txt..."
java -classpath $CLASSPATH:$FRUGALDB_HOME/lib/*:$FRUGALDB_HOME/bin $OPTS test.TestServerReloadWorkload load1.txt 
java -classpath $CLASSPATH:$FRUGALDB_HOME/lib/*:$FRUGALDB_HOME/bin $OPTS test.TestServerReconfigure mysql 2000 
$FRUGALDB_HOME/script/client.sh load1.txt load1_mysql.results 
java -classpath $CLASSPATH:$FRUGALDB_HOME/lib/*:$FRUGALDB_HOME/bin $OPTS test.TestServerReconfigure hybrid 2000 
$FRUGALDB_HOME/script/client.sh load1.txt load1_hybrid_2000M.results
echo "load1.txt testing finished."

