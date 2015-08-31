#!/usr/bin/env bash
USAGE="Usage: newhybrid-start.sh [-h] WHAT
where WHAT is one of:
\tserver\t\t start a server
\tmysql_worker\t\t start a mysql worker
\tvoltdb_worker\t\t start a voltdb worker

-h to display help information
"
get_env(){
	. $home/conf/newhybrid-env.sh
}
start_server(){
    java -classpath $CLASSPATH:$NEWHYBRID_LIBDIR/*:$NEWHYBRID_BINDIR $NEWHYBRID_SERVER_JAVA_OPTS server.HServer 
}
start_mysql_worker(){
    java -classpath $CLASSPATH:$NEWHYBRID_LIBDIR/*:$NEWHYBRID_BINDIR $NEWHYBRID_WORKER_JAVA_OPTS -Dnewhybrid.worker.dbms.type=mysql worker.HWorker 
}
start_voltdb_worker(){
    java -classpath $CLASSPATH:$NEWHYBRID_LIBDIR/*:$NEWHYBRID_BINDIR $NEWHYBRID_WORKER_JAVA_OPTS -Dnewhybrid.worker.dbms.type=voltdb worker.HWorker 
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
        start_server
        ;;
    mysql_worker)
        start_mysql_worker 
        ;;
    voltdb_worker)
        start_voltdb_worker 
        ;;
    *)
        echo -e "${USAGE}"
        exit 1
        ;;
esac
