#!/usr/bin/env bash
USAGE="Usage: newhybrid-start.sh [-h] WHAT [port]
where WHAT is one of:
\tserver\t\t start a server
\tmysql-worker\t\t start a mysql worker
\tvoltdb-worker\t\t start a voltdb worker
\tcommand-line\t\t start a CLI mode to interact with server 

if port does not defined,default port will be used

-h to display help information
"
get_env(){
	. $home/conf/newhybrid-env.sh
}
start_server(){
    if [ -z "${1}"]; then 
        java -classpath $CLASSPATH:$NEWHYBRID_LIBDIR/*:$NEWHYBRID_BINDIR $NEWHYBRID_SERVER_JAVA_OPTS server.HServer 
    else
        java -classpath $CLASSPATH:$NEWHYBRID_LIBDIR/*:$NEWHYBRID_BINDIR $NEWHYBRID_SERVER_JAVA_OPTS -Dnewhybrid.server.port=$1 server.HServer 
    fi
}
start_mysql_worker(){
    if [ -z "${1}"]; then 
        java -classpath $CLASSPATH:$NEWHYBRID_LIBDIR/*:$NEWHYBRID_BINDIR $NEWHYBRID_WORKER_JAVA_OPTS -Dnewhybrid.worker.dbms.type=mysql worker.HWorker 
    else        
        java -classpath $CLASSPATH:$NEWHYBRID_LIBDIR/*:$NEWHYBRID_BINDIR $NEWHYBRID_WORKER_JAVA_OPTS -Dnewhybrid.worker.dbms.type=mysql -Dnewhybrid.worker.port=$1 worker.HWorker 
    fi
}
start_voltdb_worker(){
    if [ -z "${1}"]; then 
        java -classpath $CLASSPATH:$NEWHYBRID_LIBDIR/*:$NEWHYBRID_BINDIR $NEWHYBRID_WORKER_JAVA_OPTS -Dnewhybrid.worker.dbms.type=voltdb worker.HWorker 
    else        
        java -classpath $CLASSPATH:$NEWHYBRID_LIBDIR/*:$NEWHYBRID_BINDIR $NEWHYBRID_WORKER_JAVA_OPTS -Dnewhybrid.worker.dbms.type=voltdb -Dnewhybrid.worker.port=$1 worker.HWorker 
    fi
}
start_cli(){
    if [ -z "${1}"]; then
        java -classpath $CLASSPATH:$NEWHYBRID_LIBDIR/*:$NEWHYBRID_BINDIR $NEWHYBRID_CLIENT_JAVA_OPTS client.Command
    else
        java -classpath $CLASSPATH:$NEWHYBRID_LIBDIR/*:$NEWHYBRID_BINDIR $NEWHYBRID_CLIENT_JAVA_OPTS -Dnewhybrid.server.port=$1 client.Command
    fi
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
    server)
        start_server $2
        ;;
    mysql-worker)
        start_mysql_worker $2
        ;;
    voltdb-worker)
        start_voltdb_worker $2 
        ;;
    command-line)
        start_cli $2
        ;;
    *)
        echo -e "${USAGE}"
        exit 1
        ;;
esac
