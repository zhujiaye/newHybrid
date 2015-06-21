FRUGALDB_HOME=`cd ..;pwd`
OPTS="
-Dnewhybrid.envpath=$FRUGALDB_HOME/conf/newhybrid-env
-Dlog4j.configuration=file:$FRUGALDB_HOME/conf/log4j.properties
-Dnewhybrid.logdir=$FRUGALDB_HOME/logs
"
java -classpath $CLASSPATH:$FRUGALDB_HOME/lib/*:$FRUGALDB_HOME/bin $OPTS test.Test 
