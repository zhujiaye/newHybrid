FRUGALDB_HOME=`cd "$(dirname "$0")";cd ..;pwd`
OPTS="
-Dnewhybrid.envpath=$FRUGALDB_HOME/conf/newhybrid-env
-Dlog4j.configuration=file:$FRUGALDB_HOME/conf/log4j.properties
-Dnewhybrid.logdir=$FRUGALDB_HOME/logs
-Dnewhybrid.logger.name=SERVER_LOGGER
-Dnewhybrid.workloaddir=$FRUGALDB_HOME/workloads
"
(nohup $FRUGALDB_HOME/script/server-start.sh >/dev/null 2>$FRUGALDB_HOME/logs/error.log )&
